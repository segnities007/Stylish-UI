.PHONY: compile build run clean apiDump apiCheck test

compile:
	./gradlew :compileKotlinWasmJs

build:
	./gradlew :website-wasm:wasmJsBrowserDevelopmentRun

run:
	./gradlew :website-wasm:wasmJsBrowserDevelopmentRun

clean:
	./gradlew clean

apiDump:
	./gradlew apiDump

apiCheck:
	./gradlew apiCheck

test:
	./gradlew jvmTest
