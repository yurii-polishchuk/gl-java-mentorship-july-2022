# GL Java Mentoring Program (July, 2022)

## Overview

* Status Service - ...

## Setup

1. Download and install:

    * SDKMAN! https://sdkman.io
    * Java: v.17.x.x (OpenJDK Temurin, sdk install java <version>)
    * Gradle: v.7.x.x (wrapper, sdk install gradle)


2. The Preferred IDE -> IntelliJ IDEA with plugins:
    * enable annotation processing
    * MapStruct: http://mapstruct.org
    * MapStruct Plugin: https://plugins.jetbrains.com/plugin/10036-mapstruct-support

## Build

1. JAR will be generated in `./build/libs`.

    * macOS/Linux: `./gradlew build`
    * Windows: `gradlew build`

## Tests

1. Run all unit/integration tests:

    * macOS/Linux/UNIX: `./gradlew test`
    * Windows: `gradlew test`

## Documentation

1. JavaDoc

    * macOS/Linux: `./gradlew javadoc`
    * Windows: `gradlew javadoc`

3. Other TBD

## Local Run

* Unix/Linux:

    * `./gradlew bootRun -Dspring.profiles.active=dev`
* macOS:

    * `./gradlew bootRun -Dspring.profiles.active=dev`
* Windows

    * `gradlew bootRun -Dspring.profiles.active=dev`
