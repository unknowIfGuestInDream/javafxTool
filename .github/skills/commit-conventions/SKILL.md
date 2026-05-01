---
name: commit-conventions
description: Use this skill whenever you author or amend git commits, write a pull request title, or generate suggested commit messages for the javafxTool repository. It enforces the AngularJS Git Commit Message Conventions used by the project.
license: MIT
---

# Commit Conventions

javafxTool follows the **AngularJS Git Commit Message Conventions**. Every
commit (including merge / squash titles and any commit messages produced by
automation) must match this format. PR titles should also use the format,
because the PR title is reused for squash-merge commit messages.

## Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

- `<type>` and `<subject>` are required. `<scope>`, `<body>` and `<footer>`
  are optional but recommended.
- Separate the header, body and footer with a single blank line.

## Allowed types

| Type | Use it for |
| ---- | ---------- |
| `feat` | A new feature visible to users or downstream modules. |
| `fix` | A bug fix. |
| `docs` | Documentation-only changes (`docs/`, `doxygen/`, `README*.md`, JavaDoc). |
| `style` | Formatting, white-space, missing semicolons — no functional change. |
| `refactor` | Code change that neither fixes a bug nor adds a feature. |
| `perf` | Performance improvement. |
| `test` | Adding or correcting tests. |
| `chore` | Maintenance, project metadata, IDE files, repo housekeeping. |
| `build` | Changes to the build system or external dependencies (Maven, plugins). |
| `ci` | Changes to GitHub Actions workflows, Jenkins files or other CI configuration. |
| `revert` | Reverts a previous commit; body must contain `Reverts: <sha>`. |

## Common scopes for this repo

Use the affected module or area name in lowercase. Common scopes:

`core`, `frame`, `login`, `common`, `demo`, `smc`, `qe`, `cg`,
`docs`, `doxygen`, `ci`, `deps`, `release`.

If the change spans multiple modules, omit the scope rather than inventing a
combined one.

## Subject rules

- Imperative, present tense: "add", "fix", "update" — not "added" / "adds".
- Lowercase first character.
- **No trailing period.**
- Header line (`<type>(<scope>): <subject>`) **≤ 50 characters**.
- Be specific. `fix(login): handle null username on submit` beats
  `fix(login): bug fix`.

## Body rules

- Wrap at **≤ 72 characters per line**.
- Explain *what* changed and *why*, not *how* (the diff already shows how).
- Use bullet points (`- `) for multiple items.

## Footer rules

- Reference issues with `Closes #123`, `Fixes #123` or `Refs #123` (one per line).
- Breaking changes start with `BREAKING CHANGE:` followed by a description and
  a migration note.

## Examples

Feature with issue link:

```
feat(frame): add easter egg SPI registration

- discover EasterEggService implementations via ServiceLoader
- start enabled providers after sample post-processing
- document the contract in docs/develop/Interface.md

Closes #123
```

Bug fix:

```
fix(login): trim whitespace before validating username

Closes #456
```

Documentation-only change:

```
docs(doxygen): add contribution workflow page
```

CI change:

```
ci: pin actions/checkout to v4
```

Breaking change:

```
refactor(core)!: rename TemplateLoaderService to TemplateLoaderProvider

BREAKING CHANGE: implementations must rename their service file under
META-INF/services and update `provides ... with ...` declarations in
module-info.java.
```

## Validation checklist (run before committing)

1. Header matches `^(feat|fix|docs|style|refactor|perf|test|chore|revert|build|ci)(\([a-z0-9,\-]+\))?!?: .+$`.
2. Header length ≤ 50 characters.
3. Subject is lowercase, imperative, no trailing period.
4. Blank line between header / body / footer.
5. Body lines ≤ 72 characters.
6. Issue references use `Closes #N` syntax.
7. PR title matches the same format as the commit header.

## Branch naming (related)

Branch names use a different convention but are tightly coupled to commit
practice:

- `feature/<short-name>` for new features
- `fix/<short-name>` for bug fixes
- `docs/<scope>` for documentation-only branches

Never push commits or open PRs from `master`.
