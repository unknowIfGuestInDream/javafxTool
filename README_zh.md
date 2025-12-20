# 介绍

javafx脚手架，JDK21 + JavaFX 21 + controlsfx 11 + Maven

frame 和 login 是基础模块, 通过java SPI实现可拔插，方便应用模块集成 demo是提供的示例模块

# 结构

- docs: 文档(使用docsify构建)
- frame: 应用主体UI框架(提供SPI接口)
- core: 应用核心组件，包含一些共同功能
- login: 登录模块(提供SPI接口)
- demo: demo示例(基于controlsfx demo改造)
- common: 通用组件模块，用于应用模块使用
- smc, qe, cg: 个人应用(无需参考)

# 接口

## core

- TemplateLoaderService: freemarker TemplateLoader加载，子类实现以添加子模块的模板路径
- GroovyLoaderService: Groovy脚本路径加载，子类实现添加子模块脚本路径

## frame

- FXSamplerProject: project信息，包含project名，模块，包名，欢迎页等
- MenubarConfigration: 菜单栏配置
- FXSamplerConfiguration: 项目style, title和icon配置
- CenterPanelService: 中心区域的Node配置, 包含点击和切换组件时的接口
- SplashScreen: 闪屏图片
- SamplePostProcessorService: Sample资源后置处理
- VersionCheckerService: 版本更新检查
- SamplesTreeViewConfiguration: 菜单树配置
- BannerPrinterService: 启动Banner
- EasterEggService: 彩蛋

## login

- LoginCheck: 登录校验

# 构建要求

构建命令需要安装和设置 Java 21 或更高版本以及 Maven 版本 3.5.4 或更高版本。

# 打包

1. IDEA工件打聚合包\
   配置: 项目结构 -> 工件 -> 新增JAR -> 提取到目标JAR -> 选择自己的清单文件路径 -> 完成\
   打包: 构建 -> 构建工件 -> 构建
2. 推荐`mvn package`打包, 依赖在lib文件夹下
3. 对于smc和qe工程，通过指定Profile=zip还可以生成zip产物 (javafx.platform=win/mac/linux)
   `mvn -Djavafx.platform=win -Dmaven.test.skip=true -Pzip package`

# 集成构建

集成（夜间）构建作业托管在 Jenkins 实例 https://jenkins.tlcsdm.com/job/javafxtool/

# 联系

如果有什么问题，可以发邮件到 liang.tang.cx@gmail.com

# 感谢

- <a href="https://jb.gg/OpenSource"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png?_gl=1*98642y*_ga*MTIxMDA5OTM5Ni4xNjgwMzQyNjgy*_ga_9J976DJZ68*MTY4MTIxMDIzMy41LjEuMTY4MTIxMTE1MS4wLjAuMA..&_ga=2.268101710.1369693703.1681210234-1210099396.1680342682" width="100px" alt="jetbrains">
- **感谢 JetBrains 提供的免费开源 License**</a>
