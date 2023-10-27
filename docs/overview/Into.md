# javafxTool
javafx脚手架，基于JDK17 + JavaFX17 + controlsfx 11.x.x + Maven开发。

## Goals Overview
- 方便快速开发所需要的工具集

## Code structure
- docs: 文档(使用docsify构建)
- frame: 应用主体UI框架(提供SPI接口)
  - cache: 缓存实现，主要用于UI缓存
  - event: 事件对象
  - modal: 数据modal
  - service: SPI接口
  - util: 工具包
- core: 应用核心组件，包含一些共同功能
  - annotation: 注解
  - event: 事件对象
  - eventbus: 事件总线实现
  - factory: 工厂对象
  - freemarker: freemarker工具
  - groovy: groovy工具
  - javafx: javafx对象封装
  - logging: 日志对象
  - util: 工具包
- login: 登录模块(提供SPI接口，需要使用登录校验的话还需要额外实现，这里仅提供了部分框架)
- demo: demo示例(展示controlsfx的demo)
- common: 通用组件模块，用于应用模块使用
- smc, qe, cg: 实现的应用
  - config: 项目配置
  - provider: SPI接口实现
  - util: 工具包

## Interface
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
- login
  1. LoginCheck: 登录校验  
