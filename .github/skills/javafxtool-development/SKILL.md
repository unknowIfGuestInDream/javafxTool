---
name: javafxtool-development
description: Use this project skill when implementing, testing, or reviewing changes in the javafxTool JavaFX/Maven codebase.
license: MIT
---

# javafxTool Development

Use this skill for repository-specific implementation work in javafxTool.

## Project context

- Build with JDK 21 or newer and Maven 3.6.0 or newer.
- The project is a multi-module Maven application with JavaFX 21 and ControlsFX 11.
- Core modules include `frame`, `core`, `login`, `common`, `demo`, `smc`, `qe`, and `cg`.
- New application features are commonly integrated through Java SPI providers.

## Working process

1. Make the smallest focused change that fully addresses the requested task.
2. Keep unrelated formatting, dependency, generated file, and IDE metadata changes out of the pull request.
3. For Java UI strings, prefer resource bundle keys accessed through `I18nUtils.get("key")`.
4. When adding or changing Java source files, preserve the repository MIT source header style.
5. Use existing Maven modules, dependency management, and test patterns instead of introducing new tools.

## Validation

- For repository-wide package validation, run `mvn -DskipTests package` from the repository root.
- For tests, run `mvn test` from the repository root or from the affected module when a targeted module test is sufficient.
- For style checks, run `mvn checkstyle:check` when Java source formatting or headers are changed.
- If a change affects documentation only, validate the changed Markdown or workflow syntax directly when no Java build is needed.
