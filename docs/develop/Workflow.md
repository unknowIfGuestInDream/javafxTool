# 开发工作流

本页介绍在 javafxTool 上的端到端开发流程，覆盖从 Issue 立项、分支管理、提交规范、文档同步到 CI 验证的所有环节。
配合 [.github/skills](https://github.com/unknowIfGuestInDream/javafxTool/tree/master/.github/skills ':target=_blank')
中的 Copilot Skills 一起使用，可以显著降低重复劳动。

## 整体流程

```text
                ┌────────────────────┐
                │  Issue / Feature   │
                └──────────┬─────────┘
                           │ 1. 立项 / 讨论
                           ▼
                ┌────────────────────┐
                │   创建分支          │
                │  feature/...       │
                │  fix/...           │
                │  docs/...          │
                └──────────┬─────────┘
                           │ 2. 本地开发
                           ▼
                ┌────────────────────┐
                │  代码 / 测试 / 文档  │◀────┐
                └──────────┬─────────┘     │
                           │ 3. 自检       │ 反馈
                           ▼               │
                ┌────────────────────┐     │
                │  本地构建与校验      │─────┘
                │ mvn -DskipTests    │
                │ mvn checkstyle:check│
                │ doxygen Doxyfile   │
                └──────────┬─────────┘
                           │ 4. 提交 (AngularJS 规范)
                           ▼
                ┌────────────────────┐
                │  Pull Request       │
                └──────────┬─────────┘
                           │ 5. CI / 评审
                           ▼
                ┌────────────────────┐
                │  GitHub Actions     │
                │  Jenkins / Qodana   │
                │  SonarCloud         │
                └──────────┬─────────┘
                           │ 6. 合并
                           ▼
                ┌────────────────────┐
                │  master / 发布      │
                └────────────────────┘
```

## 1. 立项

- 在 GitHub Issues 创建 issue，描述背景、目标和验收标准。
- 必要时在 issue 上添加标签（`feature`、`bug`、`docs` 等），方便筛选。
- 重大变动（破坏性 API、依赖大版本升级）要在 issue 中明确说明。

## 2. 分支

| 类型 | 命名规则 | 适用场景 |
| ---- | -------- | -------- |
| 功能 | `feature/<short-name>` | 新增特性或 SPI |
| 缺陷 | `fix/<short-name>` | 修复 Bug |
| 文档 | `docs/<scope>` | 仅修改文档 |
| 重构 | `refactor/<scope>` | 不改变行为的重构 |

> 不要直接在 `master` 分支上提交或发起 PR。

## 3. 本地开发

- 阅读 [SPI 接口](Interface.md) 与
  [架构总览](https://github.com/unknowIfGuestInDream/javafxTool/blob/master/doxygen/pages/modules.md ':target=_blank')，确认改动归属的模块。
- 使用 `I18nUtils.get("key")` 引入国际化文本，并在所有语言资源里同步更新。
- 编写或更新单元测试（JUnit 5），UI 用例使用 TestFX。
- 遵循 Sun 风格：4 空格缩进、UTF-8、LF 行尾、行宽 ≤ 120。

## 4. 本地校验

| 目的 | 命令 |
| ---- | ---- |
| 仓库范围打包验证 | `mvn -DskipTests package` |
| 完整安装到本地仓库 | `mvn clean install` |
| 单元测试 | `mvn test` |
| 风格检查 | `mvn checkstyle:check` |
| Doxygen 站点 | `doxygen doxygen/Doxyfile` |
| 平台 zip 包（smc/qe） | `mvn -Djavafx.platform=win -DskipTests -Pzip package` |

完整的依赖升级流程参见
[依赖更新 Skill](https://github.com/unknowIfGuestInDream/javafxTool/blob/master/.github/skills/dependency-update/SKILL.md ':target=_blank')。

## 5. 提交规范

提交信息遵循 **AngularJS Git Commit Message Conventions**：

```text
<type>(<scope>): <subject>

<body>

<footer>
```

- 类型：`feat` / `fix` / `docs` / `style` / `refactor` / `perf` / `test`
  / `chore` / `build` / `ci` / `revert`。
- 标题 ≤ 50 字符；正文行宽 ≤ 72 字符；空行分隔标题、正文和页脚。
- 用 `Closes #123` 关联并自动关闭 issue。

完整的规则与示例参见
[提交规范 Skill](https://github.com/unknowIfGuestInDream/javafxTool/blob/master/.github/skills/commit-conventions/SKILL.md ':target=_blank')。

## 6. Pull Request 与 CI

- PR 模板位于 `.github/pull_request_template.md`，请按要求填写。
- PR 标题同样遵循提交规范（squash 合并时会作为最终提交标题）。
- 自动触发的检查：

```text
   ┌──────────────────────┐
   │      Pull Request     │
   └──────────┬───────────┘
              │
   ┌──────────┴────────────────────────────────┐
   │                                            │
   ▼                                            ▼
GitHub Actions                            Jenkins
- artifact 构建                            - 持续集成构建
- 文档站点部署                             - 制品归档
- Gitee 镜像                               
   │                                            │
   └──────────┬────────────────────────────────┘
              ▼
        Qodana / SonarCloud
        - 静态代码分析
        - 安全 / 质量门禁
```

## 7. 文档同步

文档分布在三处，请同步更新：

- 用户站点（docsify）：[`docs/`](https://github.com/unknowIfGuestInDream/javafxTool/tree/master/docs ':target=_blank')
- API / 设计站点（Doxygen）：[`doxygen/`](https://github.com/unknowIfGuestInDream/javafxTool/tree/master/doxygen ':target=_blank')
- 三语 README：`README.md` / `README_zh.md` / `README_jp.md`

详细的更新策略参见
[文档更新 Skill](https://github.com/unknowIfGuestInDream/javafxTool/blob/master/.github/skills/documentation-update/SKILL.md ':target=_blank')。

## 8. 发布

发布与版本号相关的修改清单参见 [注意事项](Note.md) 中的
"发布新版本" 章节。
