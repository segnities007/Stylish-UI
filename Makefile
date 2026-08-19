.PHONY: compile build run clean

compile:
	./gradlew :compileKotlinWasmJs

build:
	./gradlew :website-wasm:wasmJsBrowserDevelopmentRun

run:
	./gradlew :website-wasm:wasmJsBrowserDevelopmentRun

clean:
	./gradlew clean
