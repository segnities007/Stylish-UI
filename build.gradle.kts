import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.tasks.Exec
import java.io.File
import java.security.MessageDigest
import java.time.Instant
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

composeCompiler {
    metricsDestination.set(layout.buildDirectory.dir("compose-metrics"))
    reportsDestination.set(layout.buildDirectory.dir("compose-reports"))
}

// ---------------------------------------------------------------------------
// Per-module ABI snapshots (binary-compatibility-validator).
//
// The extracted headless modules must satisfy the same public-API discipline as
// the root artifact (`api/jvm/Stylish-UI.api`). Their own build files stay
// minimal; the validator is applied ONCE here (root) so the plugin classpath
// and snapshot policy live in exactly one place. Applying it again per
// subproject from this script duplicates the plugin's `bcv-rt-jvm-cp`
// configuration and fails configuration time.
//
// NOTE: projects listed in `ignoredProjects` get NO per-module apiCheck/apiDump
// tasks at all, so nothing may be listed here. First dumps are generated via:
//   GRADLE_USER_HOME=$PWD/.gradle-local ./gradlew :foundation:apiDump :structure:apiDump --no-daemon --max-workers=1
// producing `foundation/api/jvm/foundation.api` and `structure/api/jvm/structure.api`,
// which the aggregated root `apiCheck` enforces from then on.
// ---------------------------------------------------------------------------
apiValidation {
    ignoredProjects.addAll(emptyList())
}


// Keep the component catalogue from silently shrinking during refactors. The thresholds are
// intentionally conservative; this is an inventory guard, not a substitute for API review.
tasks.register("checkComponentInventory") {
    group = "verification"
    description = "Checks the minimum Linux/common component inventory."
    doLast {
        val root = project.layout.projectDirectory.dir("src/commonMain/kotlin/com/segnities007/stylishui/components")
        val minimums = mapOf("atoms" to 40, "molecules" to 50, "organisms" to 20, "patterns" to 10, "charts" to 7)
        minimums.forEach { (category, minimum) ->
            val count = root.dir(category).asFile.listFiles { file -> file.extension == "kt" }?.size ?: 0
            logger.lifecycle("Stylish UI inventory: $category=$count (minimum $minimum)")
            check(count >= minimum) { "$category inventory regressed: $count < $minimum" }
        }
    }
}

// Keep the Atomic Design and Foundation → Structure → Finish dependency directions executable.
// This catches architectural drift before a compiler or reviewer has to infer intent from a PR.
tasks.register<Exec>("checkArchitecture") {
    group = "verification"
    description = "Checks Stylish UI layer dependency direction."
    commandLine("bash", layout.projectDirectory.file("scripts/verify-architecture.sh").asFile.absolutePath)
}

tasks.register<Exec>("checkModuleBoundaries") {
    group = "verification"
    description = "Checks the Gradle module graph and source package boundaries."
    commandLine(
        "python3",
        layout.projectDirectory.file("scripts/verify-module-boundaries.py").asFile.absolutePath,
    )
}

tasks.register<Exec>("checkComponentContracts") {
    group = "verification"
    description = "Checks KDoc and preview contracts for public Compose components."
    commandLine(
        "bash",
        layout.projectDirectory.file("scripts/verify-component-contracts.sh").asFile.absolutePath,
        "--strict",
    )
}

tasks.register<Exec>("checkCatalogStateMatrix") {
    group = "verification"
    description = "Builds the public Compose API ↔ catalog ↔ state coverage matrix."
    commandLine(
        "python3",
        layout.projectDirectory.file("scripts/verify-catalog-state-matrix.py").asFile.absolutePath,
    )
}

// Produce a self-contained CycloneDX 1.5 inventory from Gradle's resolved
// component graph.  This intentionally lives in the build rather than in a
// best-effort text parser: the release report must describe what Gradle
// actually resolved for every published project.  License names are retained
// verbatim from cached POM metadata and classified by the companion verifier.
tasks.register("generateSbom") {
    group = "reporting"
    description = "Generates CycloneDX SBOM, third-party notices, checksums, and license review inputs."
    outputs.dir(layout.buildDirectory.dir("reports/release"))
    doLast {
        val reportDir = layout.buildDirectory.dir("reports/release").get().asFile.apply { mkdirs() }
        val components = linkedMapOf<String, MutableMap<String, Any?>>()
        val gradleCache = gradle.gradleUserHomeDir.resolve("caches/modules-2/files-2.1")
        val pomLicenses = linkedMapOf<String, List<Pair<String, String>>>()
        val binaryFiles = linkedMapOf<String, File?>()

        fun componentKey(id: ModuleComponentIdentifier) = "${id.group}:${id.module}:${id.version}"
        fun cachedFiles(group: String, module: String, version: String): Sequence<File> {
            val dir = gradleCache.resolve(group).resolve(module).resolve(version)
            return if (dir.isDirectory) dir.walkTopDown().filter { it.isFile } else emptySequence()
        }
        fun pomInfo(group: String, module: String, version: String, visited: MutableSet<String> = linkedSetOf()): List<Pair<String, String>> {
            val key = "$group:$module:$version"
            if (!visited.add(key)) return emptyList()
            return pomLicenses.getOrPut(key) {
                val pom = cachedFiles(group, module, version).firstOrNull { it.name.endsWith(".pom") }
                    ?: return@getOrPut emptyList()
                runCatching {
                    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pom)
                    val declared = document.getElementsByTagName("license").let { nodes ->
                        (0 until nodes.length).mapNotNull { index ->
                            val node = nodes.item(index)
                            val name = node.childNodes.let { children ->
                                (0 until children.length).map { children.item(it) }
                                    .firstOrNull { it.nodeName == "name" }?.textContent?.trim()
                            }
                            val url = node.childNodes.let { children ->
                                (0 until children.length).map { children.item(it) }
                                    .firstOrNull { it.nodeName == "url" }?.textContent?.trim()
                            }
                            name?.takeIf { it.isNotBlank() }?.let { it to (url ?: "") }
                        }
                    }
                    if (declared.isNotEmpty()) declared
                    else {
                        val parent = document.getElementsByTagName("parent").item(0)
                        val parentValue = { tag: String ->
                            parent?.childNodes?.let { children ->
                                (0 until children.length).map { children.item(it) }
                                    .firstOrNull { it.nodeName == tag }?.textContent?.trim()
                            }
                        }
                        val parentGroup = parentValue("groupId") ?: group
                        val parentArtifact = parentValue("artifactId")
                        val parentVersion = parentValue("version")
                        if (parentArtifact != null && parentVersion != null) {
                            pomInfo(parentGroup, parentArtifact, parentVersion, visited)
                        } else emptyList()
                    }
                }.getOrDefault(emptyList())
            }
        }
        fun sha256(file: File): String {
            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { input ->
                val buffer = ByteArray(128 * 1024)
                while (true) {
                    val read = input.read(buffer)
                    if (read <= 0) break
                    digest.update(buffer, 0, read)
                }
            }
            return digest.digest().joinToString("") { "%02x".format(it) }
        }

        allprojects.forEach { owner ->
            owner.configurations.filter { it.isCanBeResolved }.forEach { configuration ->
                runCatching {
                    configuration.incoming.resolutionResult.allComponents.forEach { component ->
                        val id = component.id as? ModuleComponentIdentifier ?: return@forEach
                        val key = componentKey(id)
                        val scope = when {
                            configuration.name.contains("test", ignoreCase = true) -> "test"
                            configuration.name.contains("runtime", ignoreCase = true) -> "runtime"
                            configuration.name.contains("compile", ignoreCase = true) -> "compile"
                            else -> "development"
                        }
                        if (key !in components) {
                            val licenses = pomInfo(id.group, id.module, id.version)
                            val artifact = cachedFiles(id.group, id.module, id.version).firstOrNull {
                                it.extension.lowercase() in setOf("jar", "aar", "klib", "zip")
                            }
                            binaryFiles[key] = artifact
                            components[key] = linkedMapOf(
                                "type" to "library",
                                "bom-ref" to "pkg:maven/${id.group}/${id.module}@${id.version}",
                                "group" to id.group,
                                "name" to id.module,
                                "version" to id.version,
                                "purl" to "pkg:maven/${id.group}/${id.module}@${id.version}",
                                "licenses" to licenses.map { (name, url) ->
                                    linkedMapOf("license" to linkedMapOf("name" to name, "url" to url))
                                },
                                "hashes" to artifact?.let { listOf(linkedMapOf("alg" to "SHA-256", "content" to sha256(it))) },
                                "properties" to buildList {
                                    add(linkedMapOf("name" to "stylish:gradle-scope", "value" to scope))
                                    if (artifact == null) add(linkedMapOf("name" to "stylish:artifact", "value" to "metadata-only"))
                                },
                            )
                        }
                    }
                }.onFailure { logger.warn("SBOM skipped ${owner.path}:${configuration.name}: ${it.message}") }
            }
        }

        val ordered = components.toSortedMap().map { (key, component) ->
            component
        }
        val projectComponent = linkedMapOf<String, Any?>(
            "type" to "library",
            "bom-ref" to "pkg:maven/io.github.segnities007/stylish-ui@${project.version}",
            "group" to "io.github.segnities007",
            "name" to "stylish-ui",
            "version" to project.version.toString(),
            "purl" to "pkg:maven/io.github.segnities007/stylish-ui@${project.version}",
            "licenses" to listOf(linkedMapOf("license" to linkedMapOf("id" to "Apache-2.0"))),
        )
        fun jsonValue(value: Any?): String = when (value) {
            null -> "null"
            is String -> "\"${value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")}\""
            is Number, is Boolean -> value.toString()
            is Map<*, *> -> value.entries.joinToString(",", "{", "}") { (k, v) -> "${jsonValue(k.toString())}:${jsonValue(v)}" }
            is Iterable<*> -> value.joinToString(",", "[", "]", transform = ::jsonValue)
            else -> jsonValue(value.toString())
        }
        val bom = linkedMapOf<String, Any?>(
            "bomFormat" to "CycloneDX",
            "specVersion" to "1.5",
            "serialNumber" to "urn:uuid:${project.version}-${System.getenv("GITHUB_SHA") ?: "local"}",
            "version" to 1,
            "metadata" to linkedMapOf(
                "timestamp" to Instant.now().toString(),
                "tools" to listOf(linkedMapOf("vendor" to "Gradle", "name" to "Stylish-UI generateSbom", "version" to gradle.gradleVersion)),
                "component" to projectComponent,
            ),
            "components" to ordered,
        )
        reportDir.resolve("sbom.json").writeText(jsonValue(bom) + "\n")
        reportDir.resolve("third-party-notices.txt").bufferedWriter().use { out ->
            out.appendLine("Stylish-UI third-party notices (generated from Gradle-resolved components)")
            out.appendLine("sourceRevision=${System.getenv("GITHUB_SHA") ?: "local"}")
            ordered.forEach { component ->
                out.appendLine("${component["group"]}:${component["name"]}:${component["version"]}")
                val licenses = component["licenses"] as? List<*>
                if (licenses.isNullOrEmpty()) out.appendLine("  license: NOASSERTION")
                else licenses.forEach { license -> out.appendLine("  license: $license") }
            }
        }
        reportDir.resolve("checksums.txt").bufferedWriter().use { out ->
            ordered.forEach { component ->
                val key = "${component["group"]}:${component["name"]}:${component["version"]}"
                val artifact = binaryFiles[key] ?: return@forEach
                // Keep the checksum file useful to downstream release tooling.  The old
                // implementation looked up a non-existent `hash` field and emitted literal
                // `null` values, even though the CycloneDX component correctly contained a
                // SHA-256 entry under `hashes`.
                val hashes = component["hashes"] as? List<*>
                val sha256 = hashes
                    ?.asSequence()
                    ?.mapNotNull { it as? Map<*, *> }
                    ?.firstOrNull { it["alg"] == "SHA-256" }
                    ?.get("content")
                    ?.toString()
                    ?.takeIf { it.matches(Regex("[0-9a-f]{64}")) }
                    ?: error("SBOM hash missing for $key")
                // Prefix the file name with coordinates so duplicate cache names (for
                // example annotation.klib) cannot be mistaken for one another.
                out.appendLine("$sha256  $key/${artifact.name}")
            }
        }
        reportDir.resolve("license-check.txt").writeText(
            "components=${ordered.size}\n" +
                "missingLicense=${ordered.count { (it["licenses"] as? List<*>)?.isEmpty() != false }}\n" +
                "policy=docs/sbom-license-policy.md\n" +
                "status=REVIEW_REQUIRED\n",
        )
        logger.lifecycle("SBOM generated: ${ordered.size} Gradle-resolved components -> ${reportDir.absolutePath}")
    }
}

tasks.named("check") {
    dependsOn("checkComponentInventory")
    dependsOn("checkArchitecture")
    dependsOn("checkModuleBoundaries")
    dependsOn("checkComponentContracts")
    dependsOn("checkCatalogStateMatrix")
    dependsOn(":samples:adapters:jvmTest")
    // Compile and test the physical headless Structure boundary and its
    // direct downstream consumer as part of the same release gate.
    dependsOn(":structure:jvmTest")
    dependsOn(":samples:structure-consumer:jvmTest")
}

group = "io.github.segnities007"

version = providers.fileContents(
    rootProject.layout.projectDirectory.file("version.properties")
).asText.map { text ->
    text.lineSequence()
        .map { it.substringBefore("#") }
        .map { it.trim() }
        .filter { it.isNotEmpty() && it.contains("=") }
        .associate { line ->
            val (key, value) = line.split("=", limit = 2)
            key.trim() to value.trim()
        }["version"] ?: "0.1.0"
}.orElse("0.1.0").get()

kotlin {
    explicitApi()

    android {
        namespace = "com.segnities007.stylishui"
        compileSdk = 37
        minSdk = 26
        withHostTest {}
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            // The framework-neutral contract is published separately as :foundation. The root
            // artifact retains a binary-compatibility copy until the next major release; keeping
            // this dependency out of the runtime classpath prevents duplicate Android/Wasm types.
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.util)
            implementation(libs.compose.multiplatform.ui.tooling.preview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.material.kolor)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            // Android-specific dependencies are kept here.
        }

        jvmMain.dependencies {
            // Desktop-specific dependencies can be added here.
        }

        jvmTest.dependencies {
            implementation(libs.compose.ui.test)
            implementation(compose.desktop.currentOs)
        }
    }
}

dokka {
    moduleName.set("Stylish UI")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "io.github.segnities007",
        artifactId = "stylish-ui",
        version = version.toString(),
    )

    pom {
        name = "Stylish UI"
        description = "Compose Multiplatform design system components for Stylish My Vehicles."
        url = "https://github.com/segnities007/Stylish-UI"
        inceptionYear = "2026"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "segnities007"
                name = "segnities007"
                url = "https://github.com/segnities007"
            }
        }

        scm {
            url = "https://github.com/segnities007/Stylish-UI"
            connection = "scm:git:git://github.com/segnities007/Stylish-UI.git"
            developerConnection = "scm:git:ssh://git@github.com:segnities007/Stylish-UI.git"
        }
    }
}
