# javafxTool-autoupdate

[English](README.md) | 简体中文

javafxTool 应用模块可选集成的**自动更新**模块。它把现有的“检测到新版本”流程，
升级为完整的“检测 → 由用户选择 → 带进度下载 → 校验 → 安装 / 定位”自助更新体验。

设计方针是*尽量少的集成改动*：已经实现 `VersionCheckerService` 的应用模块，
只需新增一个依赖、一条 `requires` 声明，以及几行构建 `UpdateInfo` 并调用
`AutoUpdate.promptAndUpdate(...)` 的代码即可。

## 为什么自研该模块

模块基于 JDK 的 `HttpClient` 与现有的 `javafxTool-core` 工具实现，
刻意**不引入任何第三方依赖**：

- `update4j` 是 Java 生态中最常见的自助更新库，但自 2024 年 3 月起已归档并处于
  只读状态，不满足“近半年有人维护”的要求。
- 项目约定：标准库能满足时，尽量不引入新依赖。

## 功能特性

- 下载前先弹窗确认，更新与否始终由用户决定。
- 在共享的 `ThreadPoolTaskExecutor` 上后台下载，并显示模态进度弹窗
  （百分比 + 进度条）。
- 可选的 **SHA-256** 校验；校验失败会删除文件并安全终止本次更新。
- 跨平台安装辅助：在文件管理器中定位下载好的安装包，或生成并执行更新脚本，
  解压/启动新包并重启应用。
- 完整国际化（英文、简体中文、日文），沿用项目统一的
  `messages_*.properties` 约定。

## 工作流程

```text
AutoUpdate.promptAndUpdate(info)
        │
        ▼
   确认弹窗 ──── 用户取消 ──▶ 结束
        │ 用户确认
        ▼
   进度弹窗 + 后台下载 (AutoUpdateDownloader)
        │
        ▼
   SHA-256 校验（当 info.sha256 已设置时）
        │
        ├─ autoApply=false（默认）──▶ 信息弹窗 + 定位安装包
        │
        └─ autoApply=true 且设置 installDir ──▶ 确认重启
                                              └─▶ 执行更新脚本并重启
```

## 集成步骤

### 1. 添加 Maven 依赖

版本由根 `pom.xml` 的 `<dependencyManagement>` 统一管理，因此在应用模块的
`pom.xml` 中无需指定版本：

```xml
<dependency>
    <groupId>com.tlcsdm</groupId>
    <artifactId>javafxTool-autoupdate</artifactId>
    <scope>compile</scope>
</dependency>
```

### 2. 声明模块依赖

在应用模块的 `module-info.java` 中加入 `requires`：

```java
requires com.tlcsdm.autoupdate;
```

### 3. 构建 `UpdateInfo` 并触发更新

在 `VersionCheckerService` 实现中检测到新版本后，构建 `UpdateInfo` 并交给门面类。
下载地址通常来自 release 附件（见第 4 步）。

```java
UpdateInfo info = UpdateInfo.builder()
    .version(latestVersion)              // 如 "2.5.0"
    .currentVersion(currentVersion)      // 当前运行版本
    .downloadUrl(assetDownloadUrl)       // 安装包的直接下载地址
    .fileName(assetName)                 // 可选；缺省时从 URL 推断
    .releaseNotes(releaseBody)           // 可选；展示给用户
    .releaseUrl(releaseHtmlUrl)          // 可选；release 网页地址
    .size(assetSizeBytes)                // 可选；用于精确的进度条
    .sha256(assetSha256)                 // 可选；用于开启校验
    .build();

AutoUpdate.promptAndUpdate(info);
```

`AutoUpdate.promptAndUpdate(...)` 可在 JavaFX 应用线程中安全调用
（例如通知的 `onAction` 回调或菜单项中）。

### 4. 从 release 中获取下载地址（可选辅助）

`VersionCheckerService.parseReleaseResult(url, result)` 现在会为每个 release
附加 `assets` 项，省去再次解析原始 JSON。每个附件是一个
`Map<String, Object>`，包含：

| 键 | 含义 |
| -- | ---- |
| `name` | 附件文件名（如 `smcTool-win-2.5.0.zip`）。 |
| `downloadUrl` | 附件的直接下载地址。 |
| `size` | 附件字节大小（仅 GitHub）。 |
| `sha256` | 校验和，当 release 接口暴露附件摘要时提供（仅 GitHub）。 |

根据当前平台（`OSUtil.getOS()`）挑选匹配的附件，再把字段映射到
`UpdateInfo` 构造器即可。

## 需要修改哪些地方 —— 清单

| 文件 | 改动 |
| ---- | ---- |
| `<app>/pom.xml` | 添加 `javafxTool-autoupdate` 依赖。 |
| `<app>/src/main/java/module-info.java` | 添加 `requires com.tlcsdm.autoupdate;`。 |
| 你的 `VersionCheckerService` 实现 | 构建 `UpdateInfo` 并调用 `AutoUpdate.promptAndUpdate(...)`。 |
| `messages_*.properties`（可选） | 添加集成引入的新 UI 文案。 |

可参考 `smc` 的实现：`SmcVersionCheckerProvider` 会根据当前平台附件构建
`UpdateInfo`，并挂接到现有的“新版本”通知上，点击通知即开始更新。

## 行为配置

`AutoUpdate.promptAndUpdate(info)` 使用 `AutoUpdateOptions.defaults()`：

- `targetDir` = `${java.io.tmpdir}/javafxTool-update`
- `autoApply` = `false` —— 下载成功后先校验，再弹出信息弹窗提示安装包位置，
  并在文件管理器中定位该文件，由用户手动安装。这是最稳妥的默认行为。

若希望模块自动应用更新并重启应用，可传入配置：

```java
AutoUpdateOptions options = AutoUpdateOptions.builder()
    .targetDir(Path.of(System.getProperty("java.io.tmpdir"), "myapp-update"))
    .autoApply(true)
    .installDir(Path.of(appInstallDirectory))   // 解压安装包的目录
    .relaunchCommand(relaunchCommand)            // 可选，重启应用的命令
    .build();

AutoUpdate.promptAndUpdate(info, options);
```

当 `autoApply` 为 `true` 且设置了 `installDir` 时，模块会请用户确认重启，
生成平台更新脚本（`UpdateApplier.buildUpdateScript(...)`），执行后退出，
由脚本替换文件并重启。

## 公共 API

| 类型 | 用途 |
| ---- | ---- |
| `AutoUpdate` | 门面类：`isNewer(latest, current)`、`promptAndUpdate(info)`、`promptAndUpdate(info, options)`。 |
| `UpdateInfo` | 更新信息的不可变描述（基于构造器）。 |
| `AutoUpdateOptions` | 不可变的行为配置（基于构造器，`defaults()`）。 |
| `AutoUpdateDownloader` | 更底层的下载引擎，提供进度与 SHA-256 辅助方法。 |
| `UpdateApplier` | `reveal(...)`、`applyAndRestart(...)` 以及纯函数 `buildUpdateScript(...)`。 |

## 国际化

UI 文案位于
`autoupdate/src/main/resources/com/tlcsdm/autoupdate/i18n/messages_{en,zh,ja}.properties`，
通过 `I18nUtils.get(key, args...)` 解析，遵循 `Config.defaultLocale` 的运行时语言。
新增键时请同步维护三份资源文件。

## 常见问题

**下载时抛出 `HttpConnectTimeoutException: HTTP connect timed out`。**
表示下载器在连接超时时间内无法与 release 主机建立 TCP 连接，属于网络可达性问题而非逻辑错误，
其根本异常已通过 `StaticLog` 记录在下载线程日志中。常见原因与处理方式：

- **资源主机在当前网络下不可达。** GitHub 的 `browser_download_url` 会从 `github.com`
  重定向到 `objects.githubusercontent.com` CDN，它与版本检查使用的 `api.github.com`
  并非同一主机，因此可能出现「检查成功但下载超时」的情况。请在网络稳定时重试，
  或将 release 资源托管到用户可访问的镜像地址。
- **需要通过代理访问。** JDK 的 `HttpClient` 不会自动使用系统代理。内置下载器已调用
  `ProxySelector.getDefault()`，因此可以在启动应用时通过标准 JVM 参数指定代理，例如
  `-Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7890`，或使用
  `-Djava.net.useSystemProxies=true` 复用操作系统的代理设置。

## 说明与限制

- 仅支持 HTTP/HTTPS 下载地址。
- 生成的更新脚本目前支持 `.zip` 安装包（解压到 `installDir`）与可执行文件
  （直接启动）。其他格式可自行扩展 `UpdateApplier`。
- 模块绝不会在未经用户确认的情况下下载任何内容。
