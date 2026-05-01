---
name: documentation-update
description: Use this skill whenever you change user-facing or developer-facing documentation in javafxTool. The project ships two documentation sites — docsify (`docs/`) and Doxygen (`doxygen/`) — and three README languages, all of which usually need to be updated together.
license: MIT
---

# Documentation Update

javafxTool ships documentation in three places. A user-visible change usually
needs all three updated in the same pull request.

| Surface | Tool | Source | Output |
| ------- | ---- | ------ | ------ |
| docsify site | docsify 4 | `docs/` | <https://javafxtool.tlcsdm.com/> |
| API / design site | Doxygen | `doxygen/` + Java sources | `docs-gen/` (CI publishes) |
| Repo READMEs | Markdown | `README.md`, `README_zh.md`, `README_jp.md` | GitHub repo home |

## Decide what to update

| Change kind | docsify? | Doxygen? | READMEs? |
| ----------- | :------: | :------: | :------: |
| New SPI contract | ✅ `docs/develop/Interface.md` | ✅ `doxygen/pages/implementation-principles.md` + JavaDoc | If end-user-visible |
| New module / app | ✅ new page under `docs/<module>/` + sidebar | ✅ `doxygen/pages/modules.md` + `packages.dox` | ✅ all three |
| Workflow / process | ✅ `docs/develop/Workflow.md` | ✅ `doxygen/pages/contribution-workflow.md` | Maybe |
| Bug fix without UX change | Optional | If JavaDoc affected | ❌ |
| Dependency bump | If listed in README | If listed | ✅ all three |

## docsify (`docs/`)

- Sidebar: [`docs/_sidebar.md`](../../../docs/_sidebar.md). Add an entry when
  you add a page.
- Navbar: [`docs/_navbar.md`](../../../docs/_navbar.md).
- Page conventions:
  - First line is a level-1 heading (`# Title`).
  - Place pages under the matching folder (`overview/`, `develop/`,
    `common/`, `smc/`, `qe/`, …).
  - Cross-link other pages with relative paths (e.g. `[link](Interface.md)`).
  - For diagrams, prefer ASCII fenced in a ```` ```text ```` block — the
    bundled docsify build does **not** load Mermaid.
  - Tables use GitHub-flavored Markdown.
- Keep Chinese and English content style consistent with existing pages
  (`docs/overview/Into.md`, `docs/develop/Note.md`).

## Doxygen (`doxygen/`)

- Configuration: [`doxygen/Doxyfile`](../../../doxygen/Doxyfile). The `INPUT`
  list controls what is parsed; `EXCLUDE_PATTERNS` filters tests and generated
  artifacts.
- Long-form prose lives in [`doxygen/pages/`](../../../doxygen/pages/).
- Add a new page by:
  1. Creating `doxygen/pages/<name>.md` starting with
     `# Title {#anchor-name}`.
  2. Linking it from
     [`doxygen/pages/related-pages.md`](../../../doxygen/pages/related-pages.md)
     with `- @subpage <anchor-name>`.
- Diagrams use Graphviz via Doxygen's `@dot` … `@enddot` blocks. Follow the
  styling already used in `doxygen/pages/modules.md` and
  `doxygen/pages/implementation-principles.md` (rounded boxes, transparent
  background, the existing color palette).
- Java source documentation uses **JavaDoc** (`/** ... */`); `JAVADOC_AUTOBRIEF`
  is enabled, so the first sentence becomes the brief description.

### Build the Doxygen site

From the repository root:

```bash
doxygen doxygen/Doxyfile
```

Output is written to `docs-gen/` and is git-ignored.

## READMEs

When you change anything user-visible (features, screenshots, dependencies,
build steps, supported platforms), update **all three** language files in the
same commit so they don't drift:

- `README.md` — English
- `README_zh.md` — Simplified Chinese
- `README_jp.md` — Japanese

If you don't speak one of the languages, mirror the structure of the change
exactly and leave a `TODO(translate)` marker only as a last resort.

## Validation

- docsify: serve locally with any static server
  (e.g. `python3 -m http.server -d docs 3000`) and verify the new page
  renders and is linked from the sidebar.
- Doxygen: run `doxygen doxygen/Doxyfile` and check the warning log near the
  end of the output for unresolved references or missing files.
- For Markdown-only changes you can skip the Maven build.

## Commit message

Use the `docs` type from the
[`commit-conventions`](../commit-conventions/SKILL.md) skill, e.g.
`docs(doxygen): add contribution workflow page`.
