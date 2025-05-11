## Maven打包

**1.可以通过package命令打包**
`mvn package -DskipTests`
打包后生成的文件:

* javafxTool-xxx.jar 项目jar包
* lib 依赖jar包
* CHANGELOG.md 变更日志
* reports/apidocs javadoc文档
* license 第三方license信息

**2.通过maven-assembly-plugin插件打包**
目前在qe和smc中引入了maven-assembly-plugin插件，可以通过 `mvn package -DskipTests -Pzip`打包生成zip产物
maven-assembly-plugin 的配置文件放在qe/smc 下config/zip.xml

!> 如果出现打包引用的core/frame模块源码与当前源码不同时，可以先执行 `mvn install -DskipTests` 将core/frame等模块更新到本地maven仓库

## IDEA打包

当使用IDEA开发时，可以使用IDEA提供的构建来生成fatjar

* 配置: 项目结构 -> 工件 -> 新增JAR -> 提取到目标JAR -> 选择自己的清单文件路径 (
  src/main/resources/META-INF/MANIFEST.MF) -> 完成
* 打包: 构建 -> 构建工件 -> 构建

目前已提供qe和smc的打包配置，可以直接进行打包，配置文件在 [.idea/artifacts](https://github.com/unknowIfGuestInDream/javafxTool/tree/master/.idea/artifacts ':target=_blank')
