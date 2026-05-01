# GitHub Copilot Instructions for javafxTool

These are concise, repo-wide instructions. Detailed guidance for specific
workflows lives in dedicated skills under [`.github/skills/`](skills/) — prefer
those skills when their topic is in scope (commit messages, documentation
updates, dependency updates, day-to-day JavaFX/Maven implementation work).

## Project at a glance

- JavaFX scaffolding framework: **JDK 21+**, **JavaFX 21**, **ControlsFX 11**, **Maven 3.6.0+**.
- Multi-module Maven project; modules are wired together through Java SPI
  (`ServiceLoader` + `provides` / `uses` in `module-info.java`).
- Modules: `frame` (UI shell + SPI), `core` (utilities + SPI), `login` (auth UI + SPI),
  `common` (shared controls), `demo` (reference samples), `smc` / `qe` / `cg`
  (application modules), `docs` (docsify site), `doxygen` (API site).
- Detailed module/SPI/runtime descriptions live in [`docs/develop/`](../docs/develop/)
  and [`doxygen/pages/`](../doxygen/pages/) — read those before designing changes
  that span modules.

## Commit message rules (must follow)

This project follows the **AngularJS Git Commit Message Conventions**. Every
commit you author must use this format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

- **type** (required): one of `feat`, `fix`, `docs`, `style`, `refactor`,
  `perf`, `test`, `chore`, `revert`, `build`, `ci`.
- **scope** (optional): the affected module or area, e.g. `core`, `frame`,
  `login`, `smc`, `qe`, `cg`, `common`, `demo`, `docs`, `doxygen`, `ci`.
- **subject**: imperative, present tense, lowercase start, **no trailing period**.
- **header line ≤ 50 characters**; body lines wrapped at **≤ 72 characters**.
- Use `Closes #123` in the footer to link and close issues.
- Use `BREAKING CHANGE:` in the footer for breaking changes.

Example:

```
feat(frame): add easter egg SPI registration

- discover EasterEggService implementations via ServiceLoader
- start enabled providers after sample post-processing
- document the contract in docs/develop/Interface.md

Closes #123
```

> See [`.github/skills/commit-conventions/SKILL.md`](skills/commit-conventions/SKILL.md)
> for the full checklist and more examples.

## Branch naming

- New features: `feature/<feature-name>`
- Bug fixes: `fix/<feature-name>`
- Documentation: `docs/<scope>`
- Never submit PRs from the `master` branch.

## Coding standards

- **Sun coding conventions** enforced by Checkstyle (`.github/linters/sun_checks.xml`).
- 4-space indentation, LF line endings, UTF-8, final newline, no trailing whitespace.
- Maximum line length: **120 characters**.
- Naming: classes `PascalCase`, methods/variables `camelCase`, constants
  `UPPER_SNAKE_CASE`, packages lowercase (e.g. `com.tlcsdm.login`).
- Group imports in this order: Java SE → JavaFX → third-party → project.
- Use Java modules (`module-info.java` is present in every module).

### Source file header

Every Java source file must begin with the project's MIT-style copyright
header. Do **not** copy the header text into instructions or PR descriptions —
copy it from any existing source file (for example
[`core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java`](../core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java))
and update the year to the current year or year range (e.g. `2024`, `2019, 2024`).

## JavaFX guidelines

- Use JavaFX properties and bindings for reactive UI; prefer ControlsFX
  controls when a suitable one exists.
- Externalize all UI strings via `I18nUtils.get("key")`. Resource bundles live
  in module `resources/` directories and must be updated for **all** supported
  languages (English, Chinese, Japanese) when a key is added or changed.
- Keep UI logic separate from business logic. Either FXML or programmatic
  JavaFX is acceptable; stay consistent within a module.

## Build, test, lint

| Task | Command |
| ---- | ------- |
| Repo-wide build (skip tests) | `mvn -DskipTests package` |
| Full install | `mvn clean install` |
| Unit tests | `mvn test` |
| Single module | `cd <module> && mvn clean install` |
| Platform zip (smc/qe) | `mvn -Djavafx.platform=win -DskipTests -Pzip package` (`win` / `mac` / `linux`) |
| Checkstyle | `mvn checkstyle:check` |
| Doxygen API site | `doxygen doxygen/Doxyfile` (run from repo root) |

Tests use **JUnit 5**; JavaFX UI tests use **TestFX**. Test sources live in
`src/test/java`.

## Dependencies

- Avoid adding new dependencies; reuse what's already declared.
- New dependencies: prefer adding the version to the root POM
  `<dependencyManagement>` and consume them version-less in child modules.
- When updating a dependency you must also update:
  - `.idea/artifacts/` — IDEA artifact configurations
  - `core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java` — UI list of
    bundled dependencies
  - `docs/README.md` — public dependency table
- See [`.github/skills/dependency-update/SKILL.md`](skills/dependency-update/SKILL.md)
  for the full checklist.

## Documentation

- User-facing documentation: [`docs/`](../docs/) (docsify, deployed to
  https://javafxtool.tlcsdm.com/).
- API / design documentation: [`doxygen/`](../doxygen/) (built with
  `doxygen doxygen/Doxyfile`).
- README files exist in three languages: `README.md`, `README_zh.md`,
  `README_jp.md` — update all of them for user-visible changes.
- See [`.github/skills/documentation-update/SKILL.md`](skills/documentation-update/SKILL.md)
  for the dual-site (docsify + doxygen) update workflow.

## CI/CD and quality gates

- GitHub Actions: artifact build, docs deployment, Gitee mirroring.
- Jenkins: <https://jenkins.tlcsdm.com/job/javafxtool/>.
- Qodana and SonarCloud also run on pull requests.

## Security

- Report vulnerabilities to <liang.tang.cx@gmail.com> (see `SECURITY.md`).
- Never commit secrets or credentials.
- Validate all user input; handle errors explicitly.
