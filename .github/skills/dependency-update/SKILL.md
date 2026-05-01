---
name: dependency-update
description: Use this skill whenever you add, upgrade, downgrade or remove a Maven dependency in javafxTool. The project surfaces its bundled dependencies in several places (UI, IDE artifact descriptors, READMEs) and they all have to stay in sync.
license: MIT
---

# Dependency Update

A dependency change in javafxTool is rarely a single-file edit. The bundled
dependencies are exposed to end users through the UI, packaged through IDEA
artifacts, and listed in the public README — so a missed file shows up as a
visible inconsistency or broken fat-jar.

## When to run this skill

- Adding a new Maven dependency.
- Upgrading or downgrading the version of an existing dependency.
- Removing a dependency.
- Bumping the Maven version of JavaFX, ControlsFX, or the JDK target.

## Files to update (checklist)

Run through this list every time. Skip an item only if you can prove it is
not affected.

1. **Maven version management — root POM**
   - File: [`pom.xml`](../../../pom.xml) (root).
   - Add or change the version inside `<dependencyManagement>` so child
     modules can declare the dependency without a version.

2. **Module POM(s)**
   - Add a version-less `<dependency>` entry in each module that needs it.
   - Avoid declaring versions in child POMs unless absolutely required.

3. **In-app dependency list (UI)**
   - File:
     [`core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java`](../../../core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java).
   - Add / update / remove the matching record so the runtime "About →
     Dependencies" panel reflects reality.
   - Keep the alphabetical / grouped order used by the surrounding entries.

4. **IDEA artifact descriptors**
   - Directory:
     [`.idea/artifacts/`](../../../.idea/artifacts/).
   - Update every `*.xml` artifact descriptor that bundles the affected jar.
     IDEA fat-jar builds (used for `qe` / `smc`) read these files; missed
     entries cause `ClassNotFoundException` at runtime.

5. **Public READMEs**
   - Files: `README.md`, `README_zh.md`, `README_jp.md`,
     [`docs/README.md`](../../../docs/README.md).
   - Update any version table or dependency list that mentions the package.

6. **Documentation pages (when version-sensitive)**
   - JavaFX major version bump → see `docs/develop/Note.md` checklist
     (replace `JavaFX <old>`, update `CoreConstant.JAVAFX_API_URL`).
   - JDK major version bump → update GitHub Actions workflows under
     `.github/workflows/`, `jenkins/jre.sh`, the Jenkins JRE/javafxTool
     jobs, and any "JDK <old>" string.
   - ControlsFX major version bump → global replace of `controlsfx <old>`.

## Suggested workflow

1. **Check the advisory database first.** Before adding or upgrading a Maven
   dependency, verify it has no known vulnerabilities at the chosen version.
2. **Edit the root POM** to add or change the managed version.
3. **Edit the consuming module POMs** to declare / drop the dependency.
4. **Run a build** to confirm dependency resolution and module-info
   compatibility:

   ```bash
   mvn -DskipTests package
   ```

5. **Update `DependencyInfo.java`** so the UI matches the new set of jars.
6. **Update every affected `.idea/artifacts/*.xml` descriptor.** Search for
   the old jar filename to find them all.
7. **Update READMEs and any docsify / Doxygen page that pins the version.**
8. **Rebuild and run the affected fat-jar** when possible to catch missing
   bundled jars before merge.
9. **Commit** with the
   [`commit-conventions`](../commit-conventions/SKILL.md) skill, e.g.:

   ```
   build(deps): bump controlsfx from 11.2.0 to 11.2.1

   - update root dependencyManagement version
   - refresh DependencyInfo entries and IDE artifacts
   - update README dependency tables (en/zh/jp)
   ```

## Things to avoid

- Adding a version directly in a child module POM when one is already managed
  in the root.
- Bumping a major version without updating the dedicated checklists in
  `docs/develop/Note.md`.
- Editing only `DependencyInfo.java` without updating `.idea/artifacts/` —
  the UI will then claim a jar is bundled while the fat-jar lacks it.
- Mixing an unrelated dependency change into a feature commit. Use a
  separate `build(deps): …` commit so changelogs stay clean.
