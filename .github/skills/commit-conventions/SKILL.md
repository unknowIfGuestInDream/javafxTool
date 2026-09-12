---
name: commit-conventions
description: Use this skill whenever you author or amend git commits, write a pull request title, or generate suggested commit messages. It provides reusable AngularJS-style commit message guidance that can be applied across repositories.
license: MIT
---

# Commit Conventions

Use this skill whenever you need a consistent commit message or PR title.
It follows the **AngularJS Git Commit Message Conventions**, but keeps the
guidance generic so it can be reused across projects. If a repository has its
own documented commit rules, treat those as the source of truth and use this
skill as the default baseline.

Every commit (including merge / squash titles and automation-generated commit
messages) should match this format. PR titles should also use the same format,
because many platforms reuse the PR title for squash-merge commit messages.

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
| `feat` | A new feature visible to users or downstream consumers. |
| `fix` | A bug fix. |
| `docs` | Documentation-only changes. |
| `style` | Formatting, white-space, missing semicolons — no functional change. |
| `refactor` | Code change that neither fixes a bug nor adds a feature. |
| `perf` | Performance improvement. |
| `test` | Adding or correcting tests. |
| `chore` | Maintenance, metadata, repo housekeeping, or other non-product work. |
| `build` | Changes to the build system, packaging, or external dependencies. |
| `ci` | Changes to CI/CD workflows, automation, or release pipelines. |
| `revert` | Reverts a previous commit; body must contain `Reverts: <sha>`. |

## Choosing a scope

Use the affected module, package, app, service, or area name in lowercase when
it helps readers understand the change quickly.

Good examples:

- `api`
- `auth`
- `ui`
- `docs`
- `build`
- `release`

If the change spans multiple unrelated areas, omit the scope instead of
inventing an unclear combined value.

If a repository already has a stable list of scopes, use that list.

## Subject rules

- Imperative, present tense: "add", "fix", "update" — not "added" / "adds".
- Lowercase first character.
- **No trailing period.**
- Header line (`<type>(<scope>): <subject>`) **≤ 50 characters**.
- Be specific. `fix(login): handle null username on submit` beats
  `fix(auth): bug fix`.

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
feat(auth): add remember-me token refresh

- refresh tokens before expiry during active sessions
- reduce forced sign-ins for long-running browser sessions

Closes #123
```

Bug fix:

```
fix(api): reject empty filter values

Closes #456
```

Documentation-only change:

```
docs: add release checklist
```

CI change:

```
ci: pin actions/checkout to v4
```

Breaking change:

```
refactor(api)!: rename account service endpoints

BREAKING CHANGE: clients must update requests from `/account/*` to
`/accounts/*` and regenerate any API bindings that use the old paths.
```

## Validation checklist (run before committing)

1. Header matches `^(feat|fix|docs|style|refactor|perf|test|chore|revert|build|ci)(\([a-z0-9-]+\))?!?: .+$`.
2. Header length ≤ 50 characters.
3. Subject is lowercase, imperative, no trailing period.
4. Blank line between header / body / footer.
5. Body lines ≤ 72 characters.
6. Issue references use `Closes #N`, `Fixes #N`, or `Refs #N`.
7. PR title matches the same format as the commit header.

## Tailor it to each repository

This skill is intentionally generic. When you copy it into another repository,
customize only the project-specific parts:

- replace or extend the allowed type list if the project uses a different set
- document any repository-specific scope vocabulary
- add issue-tracker footer conventions if they differ
- add branch naming guidance only if that repository enforces it
- add examples that match the project's language and architecture

Keep the core format, subject rules, body rules, and footer rules unchanged
unless the repository has an explicit alternative standard.
