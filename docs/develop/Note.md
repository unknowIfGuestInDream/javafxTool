## 依赖更新

当pom.xml中的依赖升级时，由于需要在UI列出使用的依赖信息，因此需要在以下文件中进行相应的修改：

1. docs/README.md
2. core/src/main/java/com/tlcsdm/core/util/DependencyInfo.java

?> Javafx 大版本升级

需要修改以下配置:

1. JavaFX {old version} 全局替换
2. CoreConstant.JAVAFX_API_URL 修改javaFX API路径

?> JDK 大版本升级

需要修改以下配置:

1. JDK{old version} 全局替换
2. 修改Github工作流中的JDK最低支持版本
3. 修改jenkins文件夹下的jre.sh
4. 修改jenkins中的JRE工程
5. 修改jenkins中的javafxTool 使用的JDK版本

?> controlsfx 大版本升级
需要修改以下配置:

1. controlsfx {old version} 全局替换

## 发布新版本

**升级版本号**
当qe或者smc项目要升级版本号时，需要修改以下文件:

1. {program}/src/main/resources/META-INF/MANIFEST.MF 用于IDEA 构建fatjar方式使用
2. {program}/pom.xml 中的{project.version}
3. com.tlcsdm.{program}.util.{program}Constant 下的 PROJECT_VERSION
