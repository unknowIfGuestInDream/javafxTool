# Contribution Workflow {#contribution-workflow}

This page describes the end-to-end developer workflow used by the javafxTool
project, from issue creation through merge. It complements the
@subpage module-guide and @subpage implementation-principles pages by focusing
on the *process* around the code rather than the runtime behavior of the
modules themselves.

## Workflow overview

The workflow is intentionally lightweight: a single mainline branch
(`master`), short-lived topic branches and a small set of automated quality
gates.

@dot
 digraph contribution_workflow {
   graph [rankdir=TB, bgcolor="transparent", nodesep="0.45", ranksep="0.7"];
   node [shape=box, style="rounded,filled", fillcolor="#eff6ff", color="#93c5fd", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   issue   [label="Issue\nbackground / goal / DoD", fillcolor="#fef3c7", color="#f59e0b"];
   branch  [label="Topic branch\nfeature/* / fix/* / docs/*"];
   code    [label="Code + tests + docs\nI18n keys, SPI, JavaDoc"];
   verify  [label="Local verification\nmvn -DskipTests package\nmvn checkstyle:check\ndoxygen doxygen/Doxyfile"];
   commit  [label="Commit\nAngularJS conventions"];
   pr      [label="Pull Request\nsame title format as commit"];
   ci      [label="CI checks\nGitHub Actions / Jenkins\nQodana / SonarCloud", fillcolor="#dcfce7", color="#22c55e"];
   review  [label="Review\nmaintainer feedback", fillcolor="#f5f3ff", color="#a78bfa"];
   merge   [label="Squash merge to master", fillcolor="#dcfce7", color="#22c55e"];
   release [label="Release / artifact\nGitHub Actions + Jenkins"];

   issue   -> branch;
   branch  -> code;
   code    -> verify;
   verify  -> code [label="fix", style=dashed, color="#94a3b8"];
   verify  -> commit;
   commit  -> pr;
   pr      -> ci;
   ci      -> review;
   ci      -> code [label="failed", style=dashed, color="#ef4444"];
   review  -> code [label="changes", style=dashed, color="#94a3b8"];
   review  -> merge;
   merge   -> release;
 }
@enddot

## Branch model

Branch names encode intent so reviewers can scan the branch list at a glance.

| Pattern | Use it for | Example |
| ------- | ---------- | ------- |
| `feature/<short-name>` | New feature or SPI | `feature/easter-egg-spi` |
| `fix/<short-name>`     | Bug fix            | `fix/login-null-username` |
| `docs/<scope>`         | Documentation only | `docs/doxygen-workflow` |
| `refactor/<scope>`     | Behavior-preserving refactor | `refactor/core-utils` |

Pull requests must not be opened from `master`.

## Local verification

The repository ships with a small set of commands that match what CI runs.
Always exercise the closest one to your change before pushing.

| Goal | Command | When to run |
| ---- | ------- | ----------- |
| Resolve dependencies and produce module jars | `mvn -DskipTests package` | After any Java or POM change. |
| Install snapshots into the local Maven repo  | `mvn clean install` | When another module needs to consume the change. |
| Unit and TestFX tests | `mvn test` | When code under `src/main/java` changes. |
| Style and headers | `mvn checkstyle:check` | When new files or formatting changes are introduced. |
| API / design site preview | `doxygen doxygen/Doxyfile` | When `doxygen/`, JavaDoc or pages under `doxygen/pages/` change. |
| Platform fat-jar (smc/qe) | `mvn -Djavafx.platform=win -DskipTests -Pzip package` | Before validating end-user behavior on a target platform. |

@dot
 digraph local_verification {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.45", ranksep="0.65"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   src [label="Java sources\n(*.java, module-info.java)"];
   pom [label="POMs\n(root + modules)"];
   doc [label="Docs\n(docs/, doxygen/, README*)"];
   build [label="mvn -DskipTests package", fillcolor="#fef3c7", color="#f59e0b"];
   tests [label="mvn test", fillcolor="#fef3c7", color="#f59e0b"];
   style [label="mvn checkstyle:check", fillcolor="#fef3c7", color="#f59e0b"];
   dox [label="doxygen doxygen/Doxyfile", fillcolor="#fef3c7", color="#f59e0b"];
   pr [label="Push & PR", fillcolor="#dcfce7", color="#22c55e"];
   src -> build; pom -> build;
   src -> tests;
   src -> style;
   doc -> dox;
   build -> pr; tests -> pr; style -> pr; dox -> pr;
 }
@enddot

## Commit message contract

Every commit (and the PR title used for the squash-merge commit) follows the
**AngularJS Git Commit Message Conventions**:

```
<type>(<scope>): <subject>

<body>

<footer>
```

- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`,
  `chore`, `build`, `ci`, `revert`.
- Header line ≤ 50 characters; body wrapped at ≤ 72 characters per line.
- Subject is imperative, lowercase, no trailing period.
- Use `Closes #123` in the footer to link and auto-close the issue.
- Use `BREAKING CHANGE:` in the footer with a migration note for breaking
  changes; mark the type with `!` (e.g. `refactor(core)!: …`).

## Quality gates

Pull requests trigger the following automated checks. They mirror the local
verification list above so failing locally usually fails CI.

@dot
 digraph quality_gates {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.5", ranksep="0.7"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   pr [label="Pull Request", fillcolor="#dcfce7", color="#22c55e"];
   gha [label="GitHub Actions\nartifact build\ndocs deploy\nGitee mirror"];
   jenkins [label="Jenkins\nbuild + archive"];
   qodana [label="Qodana\nstatic analysis"];
   sonar [label="SonarCloud\ncoverage + smells"];
   merge [label="Squash merge\nto master", fillcolor="#dcfce7", color="#22c55e"];
   pr -> gha;
   pr -> jenkins;
   pr -> qodana;
   pr -> sonar;
   gha -> merge;
   jenkins -> merge;
   qodana -> merge;
   sonar -> merge;
 }
@enddot

## Documentation surfaces

Most user-visible changes update three surfaces in lockstep:

1. The docsify site under `docs/` (deployed to <https://javafxtool.tlcsdm.com/>).
2. The Doxygen API site generated from `doxygen/` and JavaDoc.
3. The three README languages: `README.md`, `README_zh.md`, `README_jp.md`.

The companion Copilot skill `.github/skills/documentation-update` lists the
exact files to touch for each kind of change.

## Skills as workflow accelerators

Three project-scoped Copilot skills under `.github/skills/` wrap the most
repetitive parts of this workflow:

| Skill | Triggers | What it enforces |
| ----- | -------- | ---------------- |
| `commit-conventions` | Authoring or amending commits and PR titles. | Allowed types, subject style, header / body length, footer tags. |
| `documentation-update` | Editing anything under `docs/`, `doxygen/` or the READMEs. | Which sites to update together, sidebar / page anchor conventions, build commands. |
| `dependency-update` | Adding, upgrading or removing Maven dependencies. | All files that mirror the dependency set (`DependencyInfo.java`, `.idea/artifacts/`, READMEs, version-pinned docs). |
| `javafxtool-development` | General implementation work in the codebase. | Smallest-change rule, MIT source header style, Maven validation steps. |
