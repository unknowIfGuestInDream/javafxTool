> SPI接口

- core
    1. TemplateLoaderService: freemarker TemplateLoader加载，子类实现以添加子模块的模板路径
    2. GroovyLoaderService: Groovy脚本路径加载，子类实现添加子模块脚本路径
- frame
    1. FXSamplerProject: project信息，包含project名，模块，包名，欢迎页等
    2. MenubarConfigration: 菜单栏配置
    3. FXSamplerConfiguration: 项目style, title和icon配置
    4. CenterPanelService: 中心区域的Node配置, 包含点击和切换组件时的接口
    5. SplashScreen: 闪屏支持
    6. SamplePostProcessorService: Sample资源后置处理
    7. VersionCheckerService: 版本更新检查
    8. SamplesTreeViewConfiguration: 菜单树配置
    9. BannerPrinterService: 启动Banner
    10. EasterEggService: 彩蛋
- login
    1. LoginCheck: 登录校验
