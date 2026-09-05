# javafxTool-autoupdate

English | [简体中文](README_zh.md)

Optional **auto-update** module for javafxTool application modules. It turns the
existing "detect a new version" flow into a full "detect → let the user choose →
download with progress → verify → install / reveal" self-update experience.

The goal is *minimal integration*: an application module that already implements
`VersionCheckerService` only needs to add one dependency, one `requires`
directive, and a few lines that build an `UpdateInfo` and call
`AutoUpdate.promptAndUpdate(...)`.

## Why a self-contained module

The module is implemented on top of the JDK `HttpClient` and the existing
`javafxTool-core` helpers. It intentionally adds **no third-party dependency**:

- `update4j`, the most common Java self-update library, has been archived and
  read-only since March 2024, so it does not satisfy the "maintained within the
  last six months" requirement.
- The project convention is to avoid new dependencies whenever the standard
  library is enough.

## Features

- Confirmation dialog before anything is downloaded (the user always chooses).
- Background download on the shared `ThreadPoolTaskExecutor` with a modal
  progress dialog (percentage + progress bar).
- Optional **SHA-256** checksum verification; a mismatched file is deleted and
  the update fails safely.
- Cross-platform install helpers: reveal the downloaded package in the file
  manager, or generate and run an update script that unpacks/launches the new
  package and relaunches the application.
- Fully internationalized (English, Simplified Chinese, Japanese) via the same
  `messages_*.properties` convention used across the project.

## How it works

```text
AutoUpdate.promptAndUpdate(info)
        │
        ▼
  confirm dialog ── user declines ──▶ stop
        │ user accepts
        ▼
  progress dialog + background download (AutoUpdateDownloader)
        │
        ▼
  SHA-256 verify (when info.sha256 is set)
        │
        ├─ autoApply=false (default) ──▶ info dialog + reveal package
        │
        └─ autoApply=true & installDir set ──▶ confirm restart
                                               └─▶ run update script + relaunch
```

## Integration guide

### 1. Add the Maven dependency

The version is managed in the root `pom.xml` `<dependencyManagement>`, so consume
it version-less in your application module `pom.xml`:

```xml
<dependency>
    <groupId>com.tlcsdm</groupId>
    <artifactId>javafxTool-autoupdate</artifactId>
    <scope>compile</scope>
</dependency>
```

### 2. Require the module

Add the `requires` directive to your application module `module-info.java`:

```java
requires com.tlcsdm.autoupdate;
```

### 3. Build an `UpdateInfo` and prompt

Inside your `VersionCheckerService` implementation, once you have detected a
newer version, build an `UpdateInfo` and hand it to the facade. The download URL
usually comes from a release asset (see step 4).

```java
UpdateInfo info = UpdateInfo.builder()
    .version(latestVersion)              // e.g. "2.5.0"
    .currentVersion(currentVersion)      // your app's running version
    .downloadUrl(assetDownloadUrl)       // direct download URL of the package
    .fileName(assetName)                 // optional; guessed from the URL if omitted
    .releaseNotes(releaseBody)           // optional; shown to the user
    .releaseUrl(releaseHtmlUrl)          // optional; the release web page
    .size(assetSizeBytes)                // optional; enables a precise progress bar
    .sha256(assetSha256)                 // optional; enables checksum verification
    .build();

AutoUpdate.promptAndUpdate(info);
```

`AutoUpdate.promptAndUpdate(...)` is safe to call from the JavaFX Application
Thread (for example from a notification `onAction` handler or a menu item).

### 4. Obtain the download URL from the release (optional helper)

`VersionCheckerService.parseReleaseResult(url, result)` now attaches an `assets`
entry to every release map so providers do not have to parse the raw JSON again.
Each asset is a `Map<String, Object>` with:

| Key | Meaning |
| --- | ------- |
| `name` | Asset file name (e.g. `smcTool-win-2.5.0.zip`). |
| `downloadUrl` | Direct download URL of the asset. |
| `size` | Asset size in bytes (GitHub only). |
| `sha256` | Checksum, when the release API exposes an asset digest (GitHub only). |

Pick the asset that matches the current platform (`OSUtil.getOS()`), then map its
fields onto the `UpdateInfo` builder.

## What to modify — checklist

| File | Change |
| ---- | ------ |
| `<app>/pom.xml` | Add the `javafxTool-autoupdate` dependency. |
| `<app>/src/main/java/module-info.java` | Add `requires com.tlcsdm.autoupdate;`. |
| Your `VersionCheckerService` provider | Build an `UpdateInfo` and call `AutoUpdate.promptAndUpdate(...)`. |
| `messages_*.properties` (optional) | Add any new UI strings your integration introduces. |

See `smc` for a working reference: `SmcVersionCheckerProvider` builds an
`UpdateInfo` from the matching platform asset and wires it onto the existing
"new version" notification, so clicking the notification starts the update.

## Behavior options

`AutoUpdate.promptAndUpdate(info)` uses `AutoUpdateOptions.defaults()`:

- `targetDir` = `${java.io.tmpdir}/javafxTool-update`
- `autoApply` = `false` — after a successful download the package is verified,
  an information dialog shows its location and the file is revealed in the file
  manager. The user installs it manually. This is the safest default.

To let the module apply the update and relaunch the application, pass options:

```java
AutoUpdateOptions options = AutoUpdateOptions.builder()
    .targetDir(Path.of(System.getProperty("java.io.tmpdir"), "myapp-update"))
    .autoApply(true)
    .installDir(Path.of(appInstallDirectory))   // where the package is unpacked
    .relaunchCommand(relaunchCommand)            // optional command to restart the app
    .build();

AutoUpdate.promptAndUpdate(info, options);
```

When `autoApply` is `true` and `installDir` is set, the module asks the user to
confirm the restart, generates a platform update script
(`UpdateApplier.buildUpdateScript(...)`), runs it and exits so the script can
replace files and relaunch.

## Public API

| Type | Purpose |
| ---- | ------- |
| `AutoUpdate` | Facade: `isNewer(latest, current)`, `promptAndUpdate(info)`, `promptAndUpdate(info, options)`. |
| `UpdateInfo` | Immutable description of an update (builder based). |
| `AutoUpdateOptions` | Immutable behavior configuration (builder based, `defaults()`). |
| `AutoUpdateDownloader` | Lower-level download engine with progress + SHA-256 helpers. |
| `UpdateApplier` | `reveal(...)`, `applyAndRestart(...)`, and the pure `buildUpdateScript(...)`. |

## Internationalization

UI strings live in
`autoupdate/src/main/resources/com/tlcsdm/autoupdate/i18n/messages_{en,zh,ja}.properties`
and are resolved through `I18nUtils.get(key, args...)`, which honors the running
locale from `Config.defaultLocale`. Keep the three bundles in sync when adding a
key.

## Troubleshooting

**Download fails with `HttpConnectTimeoutException: HTTP connect timed out`.**
The downloader could not open a TCP connection to the release host within the
connect timeout. This is a network-reachability problem rather than a logic
error — the underlying cause is logged through `StaticLog` on the download
thread. Common reasons and remedies:

- **The asset host is unreachable from your network.** A GitHub
  `browser_download_url` redirects from `github.com` to the
  `objects.githubusercontent.com` CDN, which is a different host from the
  `api.github.com` endpoint used for the version check — so the check can
  succeed while the download times out. Try again on a stable connection or
  host the release asset on a mirror your users can reach.
- **A proxy is required.** The JDK `HttpClient` does not use the system proxy
  automatically. The bundled downloader calls `ProxySelector.getDefault()`, so
  you can route traffic through a proxy by launching the app with the standard
  JVM options, for example
  `-Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7890`, or
  `-Djava.net.useSystemProxies=true` to reuse the operating-system settings.

## Notes and limitations

- Only HTTP/HTTPS download URLs are supported.
- The generated update script currently handles `.zip` packages (extracted to
  `installDir`) and executables (launched directly). Extend `UpdateApplier` for
  other package formats.
- The module never downloads anything without explicit user confirmation.
- Closing the progress dialog while a download is running cancels it and deletes
  the partially downloaded `.part` file, so no incomplete file is left behind.
