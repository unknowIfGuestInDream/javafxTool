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

## 接口
### core
- TemplateLoaderService: freemarker TemplateLoader加载，子类实现以添加子模块的模板路径
### frame
- FXSamplerProject: project信息，包含project名，模块，包名，欢迎页
- MenubarConfigration: 菜单栏配置
- FXSamplerConfiguration: 项目style, title和icon配置
- CenterPanelService: 中心区域的Node配置, 包含点击和切换组件时的接口
- SplashScreen: 闪屏图片
### login
- LoginCheck: 登录校验

## 打包
推荐通过IDEA打包  
配置: 项目结构 -> 工件 -> 新增JAR -> 提取到目标JAR -> 选择自己的清单文件路径 -> 完成  
打包: 构建 -> 构建工件 -> 构建
## 联系
如果有什么问题，可以发邮件到 tang97155@gmail.com
