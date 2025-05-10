# javafxTool

javafx脚手架，基于JDK17 + JavaFX21 + controlsfx 11 + Maven开发。

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
