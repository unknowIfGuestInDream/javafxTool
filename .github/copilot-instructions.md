# GitHub Copilot Instructions for javafxTool

## Project Overview

javafxTool is a JavaFX scaffolding framework built on:
- **JDK 21+** (required for building)
- **JavaFX 21**
- **ControlsFX 11**
- **Maven 3.6.0+**

The project provides a modular architecture where application modules can be integrated through Java SPI (Service Provider Interface).

## Architecture and Module Structure

### Core Modules

- **frame**: Main UI framework providing SPI interfaces for project structure
- **core**: Core components with common functionality and utilities
- **login**: Login module with authentication SPI interface
- **common**: Shared components used by application modules
- **demo**: Example module demonstrating framework usage
- **docs**: Documentation built with docsify
- **smc, qe, cg**: Application-specific modules

### SPI Interfaces

When implementing new features, be aware of these key SPI interfaces:

**core module:**
- `TemplateLoaderService`: Add Freemarker template paths for submodules
- `GroovyLoaderService`: Add Groovy script paths for submodules

**frame module:**
- `FXSamplerProject`: Define project information (name, modules, package, welcome page)
- `MenubarConfiguration`: Configure menu bar
- `FXSamplerConfiguration`: Set project style, title, and icon
- `CenterPanelService`: Configure central area nodes
- `SplashScreen`: Define splash screen image
- `SamplePostProcessorService`: Post-process samples
- `VersionCheckerService`: Implement version update checks
- `SamplesTreeViewConfiguration`: Configure menu tree
- `BannerPrinterService`: Customize startup banner
- `EasterEggService`: Add easter eggs

**login module:**
- `LoginCheck`: Implement login verification

## Coding Standards

### Java Code Style

- Follow **Sun coding conventions** (enforced by Checkstyle with `sun_checks.xml`)
- Use **4 spaces** for indentation (not tabs)
- Maximum line length: **120 characters**
- Use **LF line endings** (Unix-style)
- Always include **final newline** in files
- Trim trailing whitespace
- Character encoding: **UTF-8**

### File Headers

All Java source files must include the BSD 3-Clause license header:

```java
/*
 * Copyright (c) {YEAR} unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
```

Replace `{YEAR}` with the current year or year range (e.g., `2023`, `2019, 2023`).

**Note:** For template usage in IDEs, you can use `$today.year` to automatically insert the current year.

**Important:** While the project is licensed under MIT (see LICENSE file), source files use the BSD 3-Clause header format shown above. This is the established convention for this codebase.

### Naming Conventions

- Classes: PascalCase (e.g., `LoginFrame`, `FXSamplerProject`)
- Methods and variables: camelCase (e.g., `tfUser`, `btLogIn`)
- Constants: UPPER_SNAKE_CASE
- Package names: lowercase (e.g., `com.tlcsdm.login`)

### Code Organization

- Group imports logically (Java SE, JavaFX, third-party, project)
- Use ServiceLoader pattern for SPI implementations
- Leverage Java modules (module-info.java present in each module)
- Keep UI components modular and reusable

## Build and Test Instructions

### Building

```bash
# Clean build
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Build specific module
cd <module-name> && mvn clean install

# Package with dependencies
mvn package

# Generate platform-specific zip (smc/qe projects)
mvn -Djavafx.platform=win -Dmaven.test.skip=true -Pzip package
# Replace 'win' with 'mac' or 'linux' as needed
```

### Testing

- Tests use **JUnit 5** (Jupiter)
- JavaFX tests use **TestFX** framework
- Run tests: `mvn test`
- Test files located in `src/test/java` directories

### Linting

The project uses Checkstyle with Sun conventions:
```bash
mvn checkstyle:check
```

Configuration file: `.github/linters/sun_checks.xml`

## Commit Message Conventions

Follow **AngularJS Git Commit Message Conventions**:

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Formatting, missing semicolons, etc.
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `perf`: Performance improvement
- `test`: Adding or correcting tests
- `chore`: Maintenance, build process, auxiliary tools
- `revert`: Reverts a previous commit
- `build`: Build tools or dependencies
- `ci`: Continuous Integration changes

### Rules

- Header line: max 50 characters
- Body lines: max 72 characters per line
- Subject: imperative, present tense, lowercase start, no ending period
- Use `Closes #123` to link and close issues
- Use `BREAKING CHANGE:` in footer for breaking changes

### Example

```
feat(contributor): add option for round avatar

- add option to choose avatar in circle or square
- add new template in python script to handle it
- update usage and example in README.md

Closes #123
```

## Branch Naming Conventions

- New features: `feature/<feature-name>`
- Bug fixes: `fix/<feature-name>`
- Documentation: `docs/<scope>`
- Never submit PRs from `master` branch

## JavaFX-Specific Guidelines

### UI Components

- Use JavaFX properties and bindings for reactive UI
- Leverage ControlsFX controls when appropriate
- Implement proper localization using resource bundles (see `I18nUtils`)
- Keep UI logic in separate classes from business logic

### Internationalization

- All UI strings should be externalized using `I18nUtils.get("key")`
- Support for multiple languages (English, Chinese, Japanese)
- Resource bundles located in module resources

### FXML vs Java

- Both FXML and programmatic JavaFX are acceptable
- Use consistent approach within a module
- Document complex UI structures

## Documentation

- Update relevant documentation in `docs/` directory when adding features
- Documentation is built with docsify and deployed to https://javafxtool.tlcsdm.com/
- README files exist in multiple languages (README.md, README_zh.md, README_jp.md)
- Update all language versions when making significant changes

## Dependencies

- Avoid adding new dependencies unless absolutely necessary
- Check Dependabot alerts for security issues
- Use Maven dependency management from the root project POM (javafxTool/pom.xml)
- When adding dependencies to a module, check if version is already managed in root `<dependencyManagement>` section
- When updating Maven dependencies, you must also update:
  - `.idea/artifacts/` directory: Update IDEA artifact configuration files
  - `core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java`: Update the dependency information records
- Document any new dependencies in commit messages

## CI/CD

- Jenkins builds at https://jenkins.tlcsdm.com/job/javafxtool/
- GitHub Actions for artifact builds, docs deployment, and Gitee mirroring
- Qodana for code quality checks
- SonarCloud for code analysis

## Security

- Report security issues to liang.tang.cx@gmail.com
- Follow SECURITY.md guidelines
- Never commit secrets or credentials
- Use secure coding practices (input validation, proper error handling)

## Additional Notes

- Project inception: 2022
- License: MIT (see LICENSE file)
- Minimum Java version: JDK 21
- Minimum Maven version: 3.6.0
- The framework is designed to be pluggable - new modules integrate via SPI
- Use `.editorconfig` settings for consistent formatting across IDEs
