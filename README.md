## 介绍

javafx工具包，JDK17 + JavaFX17 + controlsfx 11.x.x + Maven

frame 和 login 是基础模块, 通过java SPI实现可拔插，方便应用模块集成  
demo是提供的示例模块

## 结构

frame: 应用主体框架(提供SPI接口)  
login: 登录模块(提供SPI接口)  
demo: demo示例  
smc: 个人应用(无需参考)

## 打包命令示例

在windows上执行需要安装wix310.exe  
先执行javafx:jlink打包镜像

```shell
javafx: jlink
```

```shell
D:\JDK\JDK17\bin\jpackage.exe --type app-image -n tlcsdm-demo -m com.tlcsdm.demo/com.tlcsdm.demo.ControlsFXSampler --runtime-image E:\javaWorkSpace\javafxTool\javafxTool\demo\target\image --temp F:/temp --dest F:/TM --app-version 1.0.0 --copyright unknowIfGuestInDream --vendor unknowIfGuestInDream --description demo工具
```

## 联系

如果有什么问题，可以发邮件到 tang97155@gmail.com
