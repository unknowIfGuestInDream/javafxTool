## 介绍

javafx工具包，JDK17 + JavaFX17 + controlsfx 11.x.x + Maven

frame 和 login 是基础模块, 通过java SPI实现可拔插，方便应用模块集成  
demo是提供的示例模块

## 结构

frame: 应用主体框架(提供SPI接口)  
core: 应用核心组件，包含一些共同功能    
login: 登录模块(提供SPI接口)  
demo: demo示例  
smc: 个人应用(无需参考)

## 问题

在eclipse环境下，logback路径寻找出现问题时，可以为程序添加VM 参数: `-Duser.dir=xxx/xxx/javafxTool`

## 联系

如果有什么问题，可以发邮件到 tang97155@gmail.com
