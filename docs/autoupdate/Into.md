# Auto Update

> Optional self-update module for javafxTool application modules

## Overview

The `autoupdate` module turns the existing "detect a new version" flow into a
full **check → let the user choose → download with progress → verify →
install / reveal** self-update experience. Application modules integrate it with
minimal changes: one dependency, one `requires` directive, and a few lines that
build an `UpdateInfo` and call `AutoUpdate.promptAndUpdate(...)`.

It is implemented on top of the JDK `HttpClient` and existing `core` helpers and
adds **no third-party dependency**.

## Flow

```text
AutoUpdate.promptAndUpdate(info)
        │
        ▼
  confirm dialog ── user declines ──▶ stop
        │ user accepts
        ▼
  progress dialog + background download
        │
        ▼
  SHA-256 verify (when info.sha256 is set)
        │
        ├─ autoApply=false (default) ──▶ info dialog + reveal package
        │
        └─ autoApply=true & installDir set ──▶ confirm restart ──▶ update + relaunch
```

## Integration

### 1. Add the Maven dependency

```xml
<dependency>
    <groupId>com.tlcsdm</groupId>
    <artifactId>javafxTool-autoupdate</artifactId>
    <scope>compile</scope>
</dependency>
```

### 2. Require the module

```java
requires com.tlcsdm.autoupdate;
```

### 3. Build an `UpdateInfo` and prompt

```java
UpdateInfo info = UpdateInfo.builder()
    .version(latestVersion)
    .currentVersion(currentVersion)
    .downloadUrl(assetDownloadUrl)
    .fileName(assetName)
    .releaseNotes(releaseBody)
    .releaseUrl(releaseHtmlUrl)
    .size(assetSizeBytes)
    .sha256(assetSha256)
    .build();

AutoUpdate.promptAndUpdate(info);
```

`VersionCheckerService.parseReleaseResult(url, result)` now attaches an `assets`
list (`name`, `downloadUrl`, `size`, `sha256`) to each release map, so providers
can pick the asset matching the current platform (`OSUtil.getOS()`) and map it
onto the builder.

## What to modify

| File | Change |
| ---- | ------ |
| `<app>/pom.xml` | Add the `javafxTool-autoupdate` dependency. |
| `<app>/src/main/java/module-info.java` | Add `requires com.tlcsdm.autoupdate;`. |
| Your `VersionCheckerService` provider | Build an `UpdateInfo` and call `AutoUpdate.promptAndUpdate(...)`. |

`smc` is the reference integration: `SmcVersionCheckerProvider` builds an
`UpdateInfo` from the matching platform asset and wires it onto the existing
"new version" notification, so clicking the notification starts the update.

## Behavior options

`AutoUpdate.promptAndUpdate(info)` uses `AutoUpdateOptions.defaults()`
(`targetDir` = `${java.io.tmpdir}/javafxTool-update`, `autoApply` = `false`):
after a verified download it shows the package location and reveals the file for
manual installation.

To apply the update and relaunch automatically, pass options with
`autoApply(true)` and an `installDir`:

```java
AutoUpdateOptions options = AutoUpdateOptions.builder()
    .autoApply(true)
    .installDir(Path.of(appInstallDirectory))
    .relaunchCommand(relaunchCommand)
    .build();

AutoUpdate.promptAndUpdate(info, options);
```

## Public API

| Type | Purpose |
| ---- | ------- |
| `AutoUpdate` | Facade: `isNewer`, `promptAndUpdate`. |
| `UpdateInfo` | Immutable update description (builder based). |
| `AutoUpdateOptions` | Immutable behavior configuration (builder based, `defaults()`). |
| `AutoUpdateDownloader` | Download engine with progress + SHA-256 helpers. |
| `UpdateApplier` | `reveal`, `applyAndRestart`, and the pure `buildUpdateScript`. |

## Internationalization

UI strings live in
`autoupdate/src/main/resources/com/tlcsdm/autoupdate/i18n/messages_{en,zh,ja}.properties`
and are resolved through `I18nUtils.get(key, args...)`. Keep the three bundles
in sync when adding a key.

## Troubleshooting

If a download fails with `HttpConnectTimeoutException: HTTP connect timed out`,
the downloader could not reach the release host within the connect timeout. This
is a network-reachability issue, and the underlying cause is logged through
`StaticLog`. A GitHub `browser_download_url` redirects to the
`objects.githubusercontent.com` CDN, which is a different host from the
`api.github.com` endpoint used for the version check — so the check can succeed
while the download times out. Retry on a stable connection, mirror the asset, or
route traffic through a proxy: the downloader honors `ProxySelector.getDefault()`,
so launch the app with `-Dhttps.proxyHost`/`-Dhttps.proxyPort` or
`-Djava.net.useSystemProxies=true`.

## Requirements

- **JDK 21+**
- **JavaFX 21**
- **Maven 3.6.0+**

The full integration manual is available in the module directory:
`autoupdate/README.md` (English) and `autoupdate/README_zh.md` (简体中文).
