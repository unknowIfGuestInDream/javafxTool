# CHANGELOG

## [v1.0.10-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.10-smc) - 2023-11-04 04:45:40

1. Maven packaging configuration modification
2. Fix the UI is not displayed after the animation splash screen under Mac
3. Version update supports gitlab
4. Added simpleHttpServer support
5. Improve SystemSetting
6. Optimize theme style
7. Dependency upgrade

### Feature

- general:
  - Improve theme (#892) ([1051907](https://github.com/unknowIfGuestInDream/javafxTool/commit/10519073689e9c9cc7597779fffc3f33463c9eb7))
  - Improve SystemSetting (#891) ([e4477c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/e4477c58a8a194ad79664c757d0a6504136ff68f))
  - 语言切换功能集成 (#876) ([9bf9754](https://github.com/unknowIfGuestInDream/javafxTool/commit/9bf975415dedc302815ecb687af9909de783bb4c))
  - simpleHttpServer支持 (#864) ([e315622](https://github.com/unknowIfGuestInDream/javafxTool/commit/e31562256676803d5fbea6dc66a8ec1a503e7b6c))

- frame:
  - Improve VersionCheckerService (#867) ([76129b4](https://github.com/unknowIfGuestInDream/javafxTool/commit/76129b475af73ffe2048f8cbcef8294a56c8afb5))

- qe:
  - DALI UI调整 (#813) ([377e672](https://github.com/unknowIfGuestInDream/javafxTool/commit/377e672a6abddd919c42dc66511a37b479f8273f))

- cg:
  - 初始化CG (#804) ([2883c79](https://github.com/unknowIfGuestInDream/javafxTool/commit/2883c797894015a2ff159970f68ecf52fcacc1f1))

### Bug Fixes

- general:
  - 修复mac 下动画闪屏后UI未显示的问题 (#833) ([6fd8ade](https://github.com/unknowIfGuestInDream/javafxTool/commit/6fd8adeab02fef481f58e3fe4ea36788c9b92f05))

## [v1.0.0-qe](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.0-qe) - 2023-10-02 02:34:44

1. First release
2. JS/CSS compression

### Feature

- common:
  - 增加DataFormatConvert (#799) ([ee72a1b](https://github.com/unknowIfGuestInDream/javafxTool/commit/ee72a1b86771dddbfe504a368ee256bbb6e5261e))
  - 新增DataFormatConvert工具 (#773) ([6c29ff0](https://github.com/unknowIfGuestInDream/javafxTool/commit/6c29ff0723b0f97d39171cd38bc34c0cbaf084b9))
  - common模块设计修改 (#787) ([a8c9fad](https://github.com/unknowIfGuestInDream/javafxTool/commit/a8c9fad5080236186b0e623e4894986f76f5f889))

- qe:
  - 补充compress的说明 (#770) ([3c2b665](https://github.com/unknowIfGuestInDream/javafxTool/commit/3c2b665e0527007b2ece7b6caaf3125804d99d0b))
  - 调整动画UI (#784) ([c2d7ab0](https://github.com/unknowIfGuestInDream/javafxTool/commit/c2d7ab0d28a5439a8df16cebd8a51143ac189de6))
  - 更换启动logo (#781) ([d376a25](https://github.com/unknowIfGuestInDream/javafxTool/commit/d376a258a6c9cc131643203fbd5a747aa20dcea0))
  - js/css压缩 (#755) ([0e55f0c](https://github.com/unknowIfGuestInDream/javafxTool/commit/0e55f0c1e3a9ff7b8616223185172fa1a763fd53))
  - dali config添加 (#742) ([bb89e04](https://github.com/unknowIfGuestInDream/javafxTool/commit/bb89e04ca3cadb84faa3cf4b789d5ef488d094f2))

- frame:
  - 接口重命名 (#780) ([b993080](https://github.com/unknowIfGuestInDream/javafxTool/commit/b993080c628d51d4f0f7f0cbdfa0f04f1dd08cb0))
  - 隐藏闪屏的托盘图标 (#779) ([f439109](https://github.com/unknowIfGuestInDream/javafxTool/commit/f4391097cb0a91d0ef62d6b0fd1d600728c1c4f8))
  - 优化启动动画时的托盘图标 (#772) ([3654320](https://github.com/unknowIfGuestInDream/javafxTool/commit/3654320b1896d9a1cde1185851374693b36cfdb1))
  - 新增菜单缓存 (#748) ([8d4eb83](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d4eb83f5385621cfb637b3974bc7f0ce45e7333))
  - 接口权限控制 (#750) ([fcff569](https://github.com/unknowIfGuestInDream/javafxTool/commit/fcff569bf0f97dd565cb9bcbcd97ced16ce4ebfe))
  - 增加选择主题接口 (#747) ([9133dba](https://github.com/unknowIfGuestInDream/javafxTool/commit/9133dbaf0f31c295d67b36a2a68f3f99d2f76144))

- core:
  - ProgressStage增加高斯模糊效果 (#769) ([2c63b93](https://github.com/unknowIfGuestInDream/javafxTool/commit/2c63b93eed1a81297d2f45b7dcbe0b344b22bd29))
  - Jackson配置优化 (#761) ([11efd7c](https://github.com/unknowIfGuestInDream/javafxTool/commit/11efd7c7e9cf6bfecdddef4499376fd54b5f5946))
  - 新增ScreenColorPickerHideWindow配置 (#734) ([caadc4e](https://github.com/unknowIfGuestInDream/javafxTool/commit/caadc4ebfdb071c7efa2f4ac1f90805480bfc6fe))

- smc:
  - girret工具bug修复 (#765) ([4c8335a](https://github.com/unknowIfGuestInDream/javafxTool/commit/4c8335ac0d64c4b4ff1531e458883350df36866a))
  - specGeneral使能条件优化，功能优化 (#757) ([332e158](https://github.com/unknowIfGuestInDream/javafxTool/commit/332e15839275bfaf75d708c52174c8311ccd0606))
  - SpecGeneralTest UI使能条件优化 (#754) ([e020e5e](https://github.com/unknowIfGuestInDream/javafxTool/commit/e020e5e18ddef1d3f8c619e33486bef2f2713e6f))
  - 修复按钮的国际化问题 (#719) ([08be18e](https://github.com/unknowIfGuestInDream/javafxTool/commit/08be18e09d30f1d6e6938b9b54d7b5713df9c955))

- general:
  - jackson替换hutool-json (#756) ([1f503e0](https://github.com/unknowIfGuestInDream/javafxTool/commit/1f503e057ff0f0f95e2321688ab94bd6f7c4ebbb))

### Bug Fixes

- frame:
  - 修复mac arm环境下隐藏失败的问题 (#783) ([cc437a6](https://github.com/unknowIfGuestInDream/javafxTool/commit/cc437a68a0eadc015b385d0c7bf87a9575314536))

- core:
  - 修复导入设置取消时的异常重启 (#777) ([28a2acc](https://github.com/unknowIfGuestInDream/javafxTool/commit/28a2acccedeba5becdd7f4dd7fb53ac02cfe4937))
  - PathWatchTool国际化错误修复 (#760) ([8f97804](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f97804591a43e336c765c7d418a56e6cb8f116f))

## [v1.0.8-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.8-smc) - 2023-09-10 06:28:48

1. GroovyCodeArea stack overflow fixed and style modified
2. Modify LicenseDialog style
3. Support startup animation
4. Modify theme style

### Feature

- smc:
  - 修改主题 (#715) ([9ea0da0](https://github.com/unknowIfGuestInDream/javafxTool/commit/9ea0da0773bbc99b67dd3dc803110c5913e2d1f2))
  - 获取菜单树数据方法重构 (#713) ([58577a2](https://github.com/unknowIfGuestInDream/javafxTool/commit/58577a21cf517e8b315795d0b199b211a62ed100))

- frame:
  - 闪屏功能支持动画效果 (#709) ([764e3c2](https://github.com/unknowIfGuestInDream/javafxTool/commit/764e3c25aeedee25ed0ec9277d1962a7b0d88a23))

- core:
  - groovyCodeArea 注释收缩样式 (#708) ([428a117](https://github.com/unknowIfGuestInDream/javafxTool/commit/428a117ec3b095d16435a4dd3390b6e90d5d4ee4))
  - preference view配置加载功能优化 (#706) ([a886573](https://github.com/unknowIfGuestInDream/javafxTool/commit/a8865737dc0b569ea87bc290b144c5416f3831fa))
  - JavaCodeArea&GroovyCodeArea样式修改 (#704) ([831a25a](https://github.com/unknowIfGuestInDream/javafxTool/commit/831a25a30d0a1b0f6520dd683e84c36f9f636446))

### Bug Fixes

- frame:
  - 修复启动图片关闭失败的问题 (#712) ([98e8863](https://github.com/unknowIfGuestInDream/javafxTool/commit/98e88632608b22337257a241bd3b4344c14b5896))

- core:
  - 修复LicenseDialog样式问题 (#701) ([6daa0f7](https://github.com/unknowIfGuestInDream/javafxTool/commit/6daa0f7e022b655443a1f1e9668037487c1d11b0))
  - 修复groovyCodeArea处理开头版权信息时堆栈溢出问题 (#696) ([9b278dc](https://github.com/unknowIfGuestInDream/javafxTool/commit/9b278dc5ef959995d29145c3f49375db7a32db35))

## [v1.0.7-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.7-smc) - 2023-09-03 09:39:14

1. Fixed CoreUtil.getRootPath() result error issue
2. Fix groovy path scanning error issue
3. ECM code refactoring
4. Add preferencesfx to refactor the system settings UI

### Feature

- smc:
  - ecm 优化 (#692) ([bcfdb79](https://github.com/unknowIfGuestInDream/javafxTool/commit/bcfdb79eace54efab41fe3fa2d718fecba643332))
  - ECM重构 (#691) ([f077b15](https://github.com/unknowIfGuestInDream/javafxTool/commit/f077b152d0bc6a224a30420bf18967c43ebfbe6e))

### Bug Fixes

- core:
  - 修复groovy路径扫描错误的问题 (#687) ([9f07055](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f07055069ece8dc30ed3434a930709f5d8c9089))
  - 修复CoreUtil.getRootPath()问题 (#686) ([24e5496](https://github.com/unknowIfGuestInDream/javafxTool/commit/24e549697183196f84fa9a3f82dea31eaa49bd9a))

## [v1.0.6-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.6-smc) - 2023-08-29 13:28:07

1. Add LicenseDialog
2. Add groovy support
3. Modify ECM Script

### Feature

- smc:
  - 新增帮助文档 (#672) ([37e462e](https://github.com/unknowIfGuestInDream/javafxTool/commit/37e462e3d72495b5e47b4c1e4c1128bfd9427bed))

- general:
  - Ecm脚本修改 (#671) ([4de2914](https://github.com/unknowIfGuestInDream/javafxTool/commit/4de291448956820f984db38ebe0e87a8ca27049b))
  - Add groovy support (#668) ([38d81ab](https://github.com/unknowIfGuestInDream/javafxTool/commit/38d81ab4574da6d1435fc92525ec4e1100747471))
  - 新增Order注解 (#660) ([87c57df](https://github.com/unknowIfGuestInDream/javafxTool/commit/87c57df1e8a9de82dc43d8b5b61a9d0b6ef8980d))

- core:
  - Add LicenseDialog (#666) ([fc14e4b](https://github.com/unknowIfGuestInDream/javafxTool/commit/fc14e4bbf2685d4255657ba2cbf4a3d76b80ac3b))
  - Add EventBus (#664) ([8d31b7f](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d31b7fe2cfb5acf802b35584232ffa40ecd3302))
  - Add PdfViewStage (#647) ([4566126](https://github.com/unknowIfGuestInDream/javafxTool/commit/4566126f77d0feb7e8ca424207a9ce2b7ff9fd0b))

### Bug Fixes

- core:
  - 修改simpleHttpServer (#675) ([11cb60d](https://github.com/unknowIfGuestInDream/javafxTool/commit/11cb60db4b8028dfd3ae994071652d6dd80d5251))

## [v1.0.5-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.5-smc) - 2023-08-06 07:59:33

1. Added control Panel setting interface for frame
2. U2C ECM adds special handling
3. Added open source dependency information display
4. Added color picker and screenshot tool
5. Added documentation links for JavaFX, CSS and FXML

### Feature

- smc:
  - 新增文档链接 (#643) ([311ab4e](https://github.com/unknowIfGuestInDream/javafxTool/commit/311ab4e4fb0a51bc46b4ffb270860e2432c4849c))

- core:
  - 新增颜色取号器 (#642) ([ab3c267](https://github.com/unknowIfGuestInDream/javafxTool/commit/ab3c267b4f83a9d9b41302344d79e1816d9f4047))

- general:
  - Improve HyperlinkTableCell (#639) ([43e12d4](https://github.com/unknowIfGuestInDream/javafxTool/commit/43e12d4394d9f187ee5fa79f79e30196482f9a60))
  - Add Dependency tableView modal (#638) ([53abab8](https://github.com/unknowIfGuestInDream/javafxTool/commit/53abab8bf4459b54cf46813bf144c8e9f52d150b))

- frame:
  - Add rightPane control interface (#618) ([5ba63c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/5ba63c51640bb973fad6b423b2edcd386065997e))

### Bug Fixes

- smc:
  - U2C数据特殊处理 (#629) ([6497faa](https://github.com/unknowIfGuestInDream/javafxTool/commit/6497faa0822e8f159619088e0d705f0409763323))

- core:
  - richtext工具添加 (#617) ([e55ae68](https://github.com/unknowIfGuestInDream/javafxTool/commit/e55ae68d31cfbcfe9ac8a1884940f93d1ff182a7))

## [v1.0.4-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.4-smc) - 2023-07-23 01:32:36

1. Fix unrecognized problem with common module
2. JDK replaced by Amazon Corretto with Eclipse Temurin
3. Java and javafx upgrade to 17.0.8
4. SpecGeneral support for xlsm file type

### Feature

- core:
  - richtextfx支持 (#604) ([a6291e1](https://github.com/unknowIfGuestInDream/javafxTool/commit/a6291e15e1e0b3395da89aae061ebf69ee6aa3f7))

### Bug Fixes

- common:
  - 修复common打包后spi配置被覆盖问题 (#612) ([0a80334](https://github.com/unknowIfGuestInDream/javafxTool/commit/0a803347e513381639d238da87d4cfc33c588add))

- smc:
  - 修复SpecGeneral功能问题 (#600) ([80b9d3b](https://github.com/unknowIfGuestInDream/javafxTool/commit/80b9d3b657f1abce88b4d0da8845a6c6930bd171))

## [v1.0.3-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.3-smc) - 2023-07-11 12:38:48

1. Console color log configuration
2. Ecm script adds U2C module
3. GirretReview adds repository filtering
4. ThreadPoolTaskExecutor and DiffHandleUtils optimization
5. Fix the vulnerability of Dom4jUtil
6. Add PathWatchTool
7. Add drag and drop function to FileChooser

### Feature

- core:
  - PathWatchTool实现 (#581) ([6f444dd](https://github.com/unknowIfGuestInDream/javafxTool/commit/6f444dd8027cdaf370a94a2948baa0a491a28afa))
  - PDFBox支持 (#570) ([c17e70c](https://github.com/unknowIfGuestInDream/javafxTool/commit/c17e70c5f3f2d8a916db3df018b646528e876745))
  - DiffHandleUtils优化 (#572) ([31160e0](https://github.com/unknowIfGuestInDream/javafxTool/commit/31160e06eb6a9cdf196009e38c16b26a81fdab18))

- qe:
  - 更换qe的logo (#562) ([805720a](https://github.com/unknowIfGuestInDream/javafxTool/commit/805720a70cf5c716989762c062c7cc2bfbd3d810))

- general:
  - 线程池uncaughtExceptionHandler属性配置 (#554) ([c15c9da](https://github.com/unknowIfGuestInDream/javafxTool/commit/c15c9da43227eb653237ade0477fd733cd11081a))

- smc:
  - girret repo条件增强 (#548) ([9190205](https://github.com/unknowIfGuestInDream/javafxTool/commit/9190205135662994b842e3f174361121bedc86a9))
  - girret增强 (#547) ([78ae034](https://github.com/unknowIfGuestInDream/javafxTool/commit/78ae0342779ba99985a7bad667ea80291fd515c1))
  - ecm脚本修改 (#545) ([80b25c8](https://github.com/unknowIfGuestInDream/javafxTool/commit/80b25c8d9a6f61f329833ef5b8da8caac8551049))

### Bug Fixes

- core:
  - 修复Dom4jUtil的漏洞 (#573) ([0fccf1f](https://github.com/unknowIfGuestInDream/javafxTool/commit/0fccf1f8cf1514692c578e23b36ef9168653cb0d))

## [v1.0.2-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.2-smc) - 2023-05-04 08:50:55

1. ECM data file generation function is perfect
2. Add common module
3. UI optimization, new button binding function
4. Added import and export settings function
5. Hide the moneyToChinese component

### Feature

- general:
  - ecm重构 (#539) ([dba213b](https://github.com/unknowIfGuestInDream/javafxTool/commit/dba213b2e68c5af72d6e6b9aab3a0172ca3ec5be))
  - qe新增右键菜单 (#536) ([e73a6a2](https://github.com/unknowIfGuestInDream/javafxTool/commit/e73a6a2c6c96f07e73d2934751fc2981480aad04))
  - TreeViewCellFactory类重构，TreeView功能新增 (#534) ([8f6990c](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f6990c6e416c15b337934af6e14e7d31bc3fe05))
  - 新增导入导出功能 (#531) ([82e1964](https://github.com/unknowIfGuestInDream/javafxTool/commit/82e1964fc89921e568472edaf31333fc995d0b63))
  - groovyUtil 修改 (#517) ([154b03b](https://github.com/unknowIfGuestInDream/javafxTool/commit/154b03b69ee51b49a03ceb441116c4e343888b80))
  - 完善ecm u2c (#499) ([432ddd6](https://github.com/unknowIfGuestInDream/javafxTool/commit/432ddd6ade15aa3b396ec5cbe391f655eaba25f1))
  - 完善common模块 (#497) ([4ef5129](https://github.com/unknowIfGuestInDream/javafxTool/commit/4ef51298c9bf864d0cb8266df16878ed2416e8a5))
  - 新增docs文档 (#491) ([5d540f8](https://github.com/unknowIfGuestInDream/javafxTool/commit/5d540f816ff092a33361da33c643e88dc4b731b9))
  - ecm脚本修改 (#470) ([565e67c](https://github.com/unknowIfGuestInDream/javafxTool/commit/565e67ca94447cf6b2e9cd481091782fd9a16e65))
  - 修改qe样式和icon (#464) ([f2985c9](https://github.com/unknowIfGuestInDream/javafxTool/commit/f2985c921c7294aea6cc95a8c92b30988ca9b248))
  - 新增qe模块 (#462) ([d83d684](https://github.com/unknowIfGuestInDream/javafxTool/commit/d83d6843425399fff120a4897aae6dc6c50e1d6b))
  - 完善DMATriggerSourceCode 的bind (#457) ([63ad897](https://github.com/unknowIfGuestInDream/javafxTool/commit/63ad8979d945548ac34d956f723ff3d4a2db28f4))
  - 新增皮肤 (#452) ([611bfe7](https://github.com/unknowIfGuestInDream/javafxTool/commit/611bfe746201b7674405c2a48308d667cb9e9d07))
  - 更改icon (#450) ([fa4840d](https://github.com/unknowIfGuestInDream/javafxTool/commit/fa4840def1756010fdafd553fa7a0ba1010aa0ed))
  - 新增getSampleImageIcon接口 (#448) ([3aefa34](https://github.com/unknowIfGuestInDream/javafxTool/commit/3aefa3406cce5eeb6ec76e21953b73e9e62aeb2d))
  - 隐藏moneyToChinese组件 (#447) ([5f0cc5e](https://github.com/unknowIfGuestInDream/javafxTool/commit/5f0cc5e16e3555772763e6c182046a1f5e8c5be4))
  - ecm脚本修改 (#446) ([9ab458d](https://github.com/unknowIfGuestInDream/javafxTool/commit/9ab458d4817b1faf992f0ccdfb248fbd7944f8cd))
  - 新增SamplesTreeViewConfiguration接口 (#445) ([732409b](https://github.com/unknowIfGuestInDream/javafxTool/commit/732409b7847ae7dd7eab148f3c3e028bc695daa2))
  - 修改ecm脚本 (#441) ([09c9379](https://github.com/unknowIfGuestInDream/javafxTool/commit/09c93795e320af39d991b6d67bae3a2582066aa6))
  - 修改smc菜单栏 (#439) ([f70b3a1](https://github.com/unknowIfGuestInDream/javafxTool/commit/f70b3a110b7dffd929dc99f93aa0f9935b2a50f1))

- core:
  - 新增SimpleHttpServer脚本 (#509) ([3982157](https://github.com/unknowIfGuestInDream/javafxTool/commit/3982157d8bc389ac352e3297ebe033c3389f9c4b))

- smc:
  - 集成common模块 (#494) ([d285755](https://github.com/unknowIfGuestInDream/javafxTool/commit/d285755b5cbc72520b06f0376fa7a941024a6ca6))
  - ecm脚本修改 (#487) ([c5d67ae](https://github.com/unknowIfGuestInDream/javafxTool/commit/c5d67ae2a896830904c1b0e66c2d95aa1ff48dde))

- common:
  - 新增common模块 (#485) ([3596fb8](https://github.com/unknowIfGuestInDream/javafxTool/commit/3596fb8f178f369327be6fa6f4660f2d28daf8eb))

- qe:
  - 完善HtmlEscape组件 (#484) ([e6c8556](https://github.com/unknowIfGuestInDream/javafxTool/commit/e6c85560aaeb6641b665a61ef96f78f40b5e784d))
  - 新增Html转义组件，修改icon (#476) ([65bb8d5](https://github.com/unknowIfGuestInDream/javafxTool/commit/65bb8d5d1e8687e3713a77da384d1f24bb502f60))

## [v1.0.1-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.1-smc) - 2023-04-02 12:59:56

1. Added Ecm data generation component
2. Adapt to macOS
3. The SpecGeneral component adds the function of merging results and generating files only
4. Added startup check for updates
5. DmaTriggerSourceCode component enhancement
6. Added log printing console component
7. UI optimization
8. Added sample menu tree expansion rules, as well as sample data processing and verification at startup
9. Solve the problem that the version number fails to be obtained after packaging as exe
10. Fix the problem that FileDiff special character processing fails

### Feature

- general:
  - 完善ecm国际化 (#433) ([5ff04c8](https://github.com/unknowIfGuestInDream/javafxTool/commit/5ff04c872318577f54714a8ed42b19c902835d02))
  - Ecm功能实现 (#432) ([776227b](https://github.com/unknowIfGuestInDream/javafxTool/commit/776227b9ea8fe2058e7e46a38037e4a79c4445be))
  - 新增jackson依赖以及工具类 (#430) ([54f1d52](https://github.com/unknowIfGuestInDream/javafxTool/commit/54f1d529c751eee6abb9361da5d0e58e9a732342))
  - diff工具增强，新增合并结果功能 (#426) ([ddc27eb](https://github.com/unknowIfGuestInDream/javafxTool/commit/ddc27ebeb274d5ccab0b77a9d45805c58ef41bb0))
  - 新增启动检查更新功能 (#425) ([e8d7c72](https://github.com/unknowIfGuestInDream/javafxTool/commit/e8d7c7213d3ce789dd35a4dcf896450b427261fe))
  - 新增sample后置处理 (#418) ([55a3d04](https://github.com/unknowIfGuestInDream/javafxTool/commit/55a3d04ee20130cd95b0c40c67856fadf955fd01))
  - 新增sample展开规则，初始化ecm组件 (#379) ([36cab80](https://github.com/unknowIfGuestInDream/javafxTool/commit/36cab80154f258663e0ecf07a60c75372d7e13be))
  - specGeneral 打开输出位置功能增强 (#406) ([6e527a0](https://github.com/unknowIfGuestInDream/javafxTool/commit/6e527a0ebc3d76d4897d3ecf01fdf7367e14b55b))
  - 新增日志打印控制台组件 (#378) ([d92ccd2](https://github.com/unknowIfGuestInDream/javafxTool/commit/d92ccd257309c0c4858e923045fed8a69e296f5b))
  - DMATriggerSourceCode修改 (#377) ([59c4c9f](https://github.com/unknowIfGuestInDream/javafxTool/commit/59c4c9fe3ef419c6d800b8472488259f48d9e5f4))
  - DmaTriggerSourceCode组件增强 (#374) ([fa9d7ac](https://github.com/unknowIfGuestInDream/javafxTool/commit/fa9d7acf9c5fc3599e4683530a32436f53deb791))
  - FxButton增强，优化specGeneral组件 (#371) ([13a8180](https://github.com/unknowIfGuestInDream/javafxTool/commit/13a8180744e12cd4ea7c963555b35ac03778c0d1))

### Bug Fixes

- general:
  - 修复ecm模板下载失败问题 (#434) ([eaaf6cc](https://github.com/unknowIfGuestInDream/javafxTool/commit/eaaf6cca3b0f2a73b9a91f779300d5d9de60901d))
  - 修复macos下打开配置文件错误问题 (#429) ([fe6c28b](https://github.com/unknowIfGuestInDream/javafxTool/commit/fe6c28bc078232cb6cbf1d1cb575f5443054aa81))
  - 修复可执行文件获取清单数据失败问题 (#414) ([a5d29d0](https://github.com/unknowIfGuestInDream/javafxTool/commit/a5d29d0a4f45965af322b264efd7a4922582eb9d))
  - 修复DtsTriggerSourceDoc 初始值设定的判断问题 (#412) ([df5c50c](https://github.com/unknowIfGuestInDream/javafxTool/commit/df5c50c3d1990fc93756a005f3320f674086edec))
  - 解决打包为执行文件后项目配置获取不到的问题 (#408) ([856c1cb](https://github.com/unknowIfGuestInDream/javafxTool/commit/856c1cbb970cfdcbe0fea033b414a10e87e9eadf))
  - DmaTriggerSourceCode 头文件模板新增注释 (#404) ([8e6942d](https://github.com/unknowIfGuestInDream/javafxTool/commit/8e6942d211ba67d1538181a39d61c46068151cca))
  - 修复specGeneral组件对多行注释的处理 (#402) ([e3ad748](https://github.com/unknowIfGuestInDream/javafxTool/commit/e3ad7480fffe34efd787233c91b0a49c883243ed))
  - dmaTriggerSourceCode 修改使能类输入 (#391) ([81addc1](https://github.com/unknowIfGuestInDream/javafxTool/commit/81addc186a3fb94848afa5db1de482ca6f5ef629))
  - dmaTriggerSourceCode模板完善 (#380) ([985a906](https://github.com/unknowIfGuestInDream/javafxTool/commit/985a90607be596c974f234a8a0b1bb0c05a96833))
  - 修复smc中文件选择错误 (#368) ([581b3e4](https://github.com/unknowIfGuestInDream/javafxTool/commit/581b3e4bee12e40435165b33d40a259e54567210))

## [v1.0.0-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.0-smc) - 2023-03-19 13:19:45

1. SmcTool release
2. Contains UD, CD, UT and other tools of smc

### Feature

- general:
  - DmaTriggerSourceCode setting完善 (#353) ([a731842](https://github.com/unknowIfGuestInDream/javafxTool/commit/a731842a1c7224a1f92505ec5cd231d0fc72e24c))
  - update .gitattributes (#345) ([ddf4b0c](https://github.com/unknowIfGuestInDream/javafxTool/commit/ddf4b0c5b52698f3b1347f506456d2e089f4832c))
  - Add .gitattributes (#344) ([aeef4c6](https://github.com/unknowIfGuestInDream/javafxTool/commit/aeef4c6a76895c0f7a6945f0015af04d117ebe2f))
  - DmaTriggerSourceCode setting完善 (#342) ([88f16bd](https://github.com/unknowIfGuestInDream/javafxTool/commit/88f16bde3f5dc0540d784582b56831c81fa27a22))
  - DmaTriggerSourceCode 模板完善 (#341) ([7bf91ee](https://github.com/unknowIfGuestInDream/javafxTool/commit/7bf91ee4589406627903d47b1c7349fdb725bd88))
  - DmaTriggerSourceCode 模板完善 (#340) ([7965e0a](https://github.com/unknowIfGuestInDream/javafxTool/commit/7965e0ab2e13d1e6349b0cf86e033b2e5d3b46b3))
  - DmaTriggerSourceCode 组件完成 (#337) ([457c280](https://github.com/unknowIfGuestInDream/javafxTool/commit/457c28065ed7bfa53561521c2bc5e3e12f7f6541))
  - DmaTriggerSourceCode 下载等功能完善 (#335) ([883f155](https://github.com/unknowIfGuestInDream/javafxTool/commit/883f155258f72848c702ee232a7e1228514436d6))
  - DmaTriggerSourceCode 组件信息完善 (#334) ([087ab34](https://github.com/unknowIfGuestInDream/javafxTool/commit/087ab34c74573a5accea368c49f5052ba9588368))
  - dmaTeiggerSourceCode UI完善 (#333) ([8342b61](https://github.com/unknowIfGuestInDream/javafxTool/commit/8342b612115ba2c01bca3eefa5564e85d05a0862))
  - DmaTriggerSourceCode 测试类完成 (#332) ([6abf16d](https://github.com/unknowIfGuestInDream/javafxTool/commit/6abf16db1d50cf58ad6b9af749a1314e63feb191))
  - 组件描述以及README.md完善 (#331) ([b3d6b58](https://github.com/unknowIfGuestInDream/javafxTool/commit/b3d6b58a2fcce3cdac59060e970467cdba941884))
  - DmaTriggerSourceCode 组件新增 (#330) ([56388ea](https://github.com/unknowIfGuestInDream/javafxTool/commit/56388ea4728c6eaea3f0c75180ea0e5f41f685d6))
  - 新增列名计算器 (#324) ([cddfa10](https://github.com/unknowIfGuestInDream/javafxTool/commit/cddfa10b4a0381c754be6a9b5fdb8b6a0e97af9a))
  - 新增ProgressStage 加载组件 (#316) ([dfc8831](https://github.com/unknowIfGuestInDream/javafxTool/commit/dfc88318964e696d323166da759e8cdbafdba284))
  - 新增闪屏图片功能，优化启动 (#313) ([0092c57](https://github.com/unknowIfGuestInDream/javafxTool/commit/0092c57d40f5af124a647dd8c700b1011e64f106))
  - HconvertExcel完成 (#307) ([7f4cf39](https://github.com/unknowIfGuestInDream/javafxTool/commit/7f4cf396fee7e7172456e830dfbf58d4abdc5c95))
  - 更换smc logo (#305) ([b861826](https://github.com/unknowIfGuestInDream/javafxTool/commit/b8618268c83bf45b0edbac011aaedbb027a8be73))
  - HconvertExcel 初版 (#301) ([63338ac](https://github.com/unknowIfGuestInDream/javafxTool/commit/63338ac30c686d3adbf7cf6902f2a48c9b455c28))
  - HconvertExcel 测试类完成 (#299) ([8f4e856](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f4e856bc2c30d7d24f68037a5e19cccf3c7be17))
  - DtsTriggerSourceDoc 描述完善 (#296) ([c2cb888](https://github.com/unknowIfGuestInDream/javafxTool/commit/c2cb888a6c26989b12fc0e323819830763d796c1))
  - DtsTriggerSourceDoc 完成 (#295) ([24e47a6](https://github.com/unknowIfGuestInDream/javafxTool/commit/24e47a62250dafbb7ef603c15859cdadbd343a86))
  - DtsTriggerSourceDoc 功能完善 (#293) ([d57d709](https://github.com/unknowIfGuestInDream/javafxTool/commit/d57d709922d1cc2c3f094027312f824152ad4fe6))
  - dts document 生成初步完成 (#291) ([92d8b49](https://github.com/unknowIfGuestInDream/javafxTool/commit/92d8b49a0e7547f0cca30cb589351f068fa23473))
  - DtsTriggerSourceDoc 开发 (#290) ([0750ac1](https://github.com/unknowIfGuestInDream/javafxTool/commit/0750ac1470149765fb9a4111388edc2ebdb1c8d7))
  - dtc trigger source doc 功能调试 (#286) ([b116892](https://github.com/unknowIfGuestInDream/javafxTool/commit/b116892773893dcbfeb10592638170b1c1936f8c))
  - 修复girret启动问题 (#282) ([8c13910](https://github.com/unknowIfGuestInDream/javafxTool/commit/8c13910083eabbfdfe8069db60630481d481b8c7))
  - 新增bindUserData增强方法，smc girretReview优化token信息初始化 (#281) ([7fd7319](https://github.com/unknowIfGuestInDream/javafxTool/commit/7fd731953cee7b02ab531b59a7854c4cc0eb2180))
  - logo以及名称抽象化，smc新增测试组件 (#280) ([552f4b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/552f4b68170cc691d07ee96f18096268408b20d1))
  - 优化getWelcomeBackgroundImagePane接口 (#279) ([85be2ad](https://github.com/unknowIfGuestInDream/javafxTool/commit/85be2ad0e6f35bfa0209e020887129c133a16eb5))
  - logback分包配置修改 (#278) ([8e0897a](https://github.com/unknowIfGuestInDream/javafxTool/commit/8e0897a89e8832485640977dde441f1d4693abf4))
  - 新增smc模块 welcome背景，以及修复在eclipse环境下logback路径问题 (#275) ([81e1118](https://github.com/unknowIfGuestInDream/javafxTool/commit/81e1118f2c0db1a1532e3816c4b9761eab48e255))
  - logback模块分包配置，完善smc国际化描述 (#271) ([44cff62](https://github.com/unknowIfGuestInDream/javafxTool/commit/44cff62444456f4450aff6b46b6f9c9549f77c53))
  - 新增tlcsdm-asyncTool依赖 (#267) ([99bb30e](https://github.com/unknowIfGuestInDream/javafxTool/commit/99bb30ecf348dd62c52effec88cf3b4ed32bddd0))
  - 新增规则引擎aviator以及logback配置去除configruation2启动异常日志 (#265) ([53980e9](https://github.com/unknowIfGuestInDream/javafxTool/commit/53980e9edaef865a43bc3b6f8cd1ca8b190bfb84))
  - 新增copyright信息 (#261) ([db156a5](https://github.com/unknowIfGuestInDream/javafxTool/commit/db156a55a041bf48bb8e0ba3c14d372f1f4e432a))
  - freemarker集成以及项目结构调整 (#259) ([8e627b2](https://github.com/unknowIfGuestInDream/javafxTool/commit/8e627b27be38fb1173c60564fb1e5f108fac7c0d))
  - smc PasswordField 数据切换为aes加密 (#257) ([0d2cece](https://github.com/unknowIfGuestInDream/javafxTool/commit/0d2cecea792c784a1b8d788a6f80ab4f4cf6577e))
  - 线程池设置为懒汉单例 (#255) ([101ab1c](https://github.com/unknowIfGuestInDream/javafxTool/commit/101ab1ce8e0d3337f24a3f72e6c443431381c02b))
  - 新增InitializingFactory初始化接口以及线程池初始化实现 (#254) ([2b2b9fa](https://github.com/unknowIfGuestInDream/javafxTool/commit/2b2b9fa71473a31cab41e5449b496b7e1529c798))
  - DtsTriggerSourceXml 完成 (#248) ([48724fe](https://github.com/unknowIfGuestInDream/javafxTool/commit/48724fe5df694d28164d06807fb4e370358eb2ae))
  - DtsTriggerSourceDoc组件初始化 (#243) ([4057278](https://github.com/unknowIfGuestInDream/javafxTool/commit/40572785f5a2792aad2d18136ebad9ee5c6e30ad))
  - dtsTriggerSourceXml 初始化 (#242) ([0a4866e](https://github.com/unknowIfGuestInDream/javafxTool/commit/0a4866e8b00c6d2cf1c4f5654b2e889be43afd1a))
  - 新增DtsTriggerSourceXml组件 (#237) ([9f97719](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f97719f2dd66ee1a91d94386c14140dceb390ca))
  - SpecGeneralTest完成，优化SampleScanner,新增mistSliverSkin (#236) ([0c3e3f2](https://github.com/unknowIfGuestInDream/javafxTool/commit/0c3e3f253a54c75025ba3bb83d85f4fb4ad86710))
  - 新增freemarker 以及增加相关测试，新增log4j-slf4j依赖 (#231) ([47a5407](https://github.com/unknowIfGuestInDream/javafxTool/commit/47a54076404fc46fa3e2b058422e464420ed049b))
  - 修改smc下包名test -> unitTest (#230) ([6ef5e73](https://github.com/unknowIfGuestInDream/javafxTool/commit/6ef5e73b58a1e698b91263ea5535017679ff0fdb))
  - 新增HcontentExcel工具包，代码规范和结构优化 (#228) ([5ca1028](https://github.com/unknowIfGuestInDream/javafxTool/commit/5ca10280090e1acfccd33964458ebc578b5a2a08))
  - SpecGeneralTest功能实现完成 (#227) ([622e115](https://github.com/unknowIfGuestInDream/javafxTool/commit/622e1150501fe17aaa30da2e0f6390a6f0ec6c43))
  - core helper包增强 (#224) ([47e76c3](https://github.com/unknowIfGuestInDream/javafxTool/commit/47e76c3bd5692b06cd97aefd7df0636d3c39e444))
  - 新增FxAction和FxActionGroup (#223) ([fd25094](https://github.com/unknowIfGuestInDream/javafxTool/commit/fd250947213b6b9a5ed567a58143ad7ce84bb133))
  - 优化about action中图片获取方式 (#222) ([b8f3d14](https://github.com/unknowIfGuestInDream/javafxTool/commit/b8f3d147e7f995b67bae1e7f0f5fabb49aa31da1))
  - core新增FxButton和FxAction封装工具包，SpecGeneral组件创建 (#220) ([c513c8c](https://github.com/unknowIfGuestInDream/javafxTool/commit/c513c8c1203b042ce604a919958c2227ffcfb220))
  - 新增CoreUtil (#218) ([783033d](https://github.com/unknowIfGuestInDream/javafxTool/commit/783033da488e075180c167ccfd033f3dcecb49aa))
  - SpecGeneralTest初步实现以及filediff优化 (#217) ([225b547](https://github.com/unknowIfGuestInDream/javafxTool/commit/225b547fa50e4070095ba066fb3923b413ca3048))
  - SampleBase userData对PasswordField的值加密 (#215) ([c3803fe](https://github.com/unknowIfGuestInDream/javafxTool/commit/c3803fee2479dbf5609ec20ecbc4379e96e8f0d7))
  - 新增DTS U2C 文档生成工具 (#214) ([19205e9](https://github.com/unknowIfGuestInDream/javafxTool/commit/19205e9f40e386e85f67b7cf7b14c69280212a11))
  - 新增加密解密工具，新增测试文件资源 (#213) ([011964f](https://github.com/unknowIfGuestInDream/javafxTool/commit/011964f3a402df53e838960939b50007ee460832))
  - data文件中新增注释 (#212) ([d035ade](https://github.com/unknowIfGuestInDream/javafxTool/commit/d035adee6c35e637bdb96f8eb454cecb11d3158b))
  - PinAssignment pin处理工具完成 (#207) ([bf96146](https://github.com/unknowIfGuestInDream/javafxTool/commit/bf96146bc6980efa67473d0f79e5751b94dddfe2))
  - pin 内容处理 (#204) ([3899d4d](https://github.com/unknowIfGuestInDream/javafxTool/commit/3899d4d74b80d13c175f4f38e928999b3f1a80a2))
  - MoneyToChinese 模块完成，项目资源配置重构 (#202) ([7e177f5](https://github.com/unknowIfGuestInDream/javafxTool/commit/7e177f54d54a00d4102636c5259a08a164de50a0))
  - 新增金额转换大写类 (#200) ([17e5cef](https://github.com/unknowIfGuestInDream/javafxTool/commit/17e5ceff78dc9856b0f75140a6816baaeae76c29))
  - smc girretReview userData支持，新增查看用户数据菜单 (#197) ([a0ebf67](https://github.com/unknowIfGuestInDream/javafxTool/commit/a0ebf67095eb79d0ac4fe6cf4207b4cc47410d38))
  - codeStyle120 支持保留用户数据，后续需要调试 (#195) ([7effaea](https://github.com/unknowIfGuestInDream/javafxTool/commit/7effaeaf026be9b157da177ffc75c0a1cb855300))
  - 增强userData xml数据结构，新增保存顺序 (#194) ([a9879e3](https://github.com/unknowIfGuestInDream/javafxTool/commit/a9879e3dc94ad6c5d306efb00a97dca3c082a07b))
  - smc fileDiff 集成 userData功能 (#193) ([87cf5c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/87cf5c5129d4e51fa3b38697d717b17fc639616f))
  - core新增exception类 (#192) ([e0a3a25](https://github.com/unknowIfGuestInDream/javafxTool/commit/e0a3a25cad42ef84c7de6aa60e6ebdabfbe8c78a))
  - 新增money2ChineseUtil工具类 (#191) ([d0cdbb6](https://github.com/unknowIfGuestInDream/javafxTool/commit/d0cdbb6d93263c03aa17fc016f36018c858cce58))
  - 用户数据方案初步设计以及计划新增数字转换中文文字的功能 (#189) ([e5c285f](https://github.com/unknowIfGuestInDream/javafxTool/commit/e5c285f9775b650df9ab64e7e76ddc6e4751ff47))
  - FxXmlUtil工具类完善 (#184) ([330a71c](https://github.com/unknowIfGuestInDream/javafxTool/commit/330a71c8282725a84130d1dd0bfc5a7eda356feb))
  - xml配置文件工具 (#182) ([73e8ac7](https://github.com/unknowIfGuestInDream/javafxTool/commit/73e8ac7492ad92d88dba8e563168f2a6d211d582))
  - XMLPropertiesConfiguration 测试以及配置 (#181) ([da6b78a](https://github.com/unknowIfGuestInDream/javafxTool/commit/da6b78ab0021025988af1395726a17f5a44ab9ef))
  - 系统设置功能完成 (#178) ([8a27b17](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a27b17218407ca871aa7ad73b67197e8c4c8290))
  - smc welcomePage国际化 (#180) ([06e33b0](https://github.com/unknowIfGuestInDream/javafxTool/commit/06e33b01582153108add883ecb6a36dd8f8f86a1))
  - smc 菜单功能完善 (#177) ([344dd30](https://github.com/unknowIfGuestInDream/javafxTool/commit/344dd305ff1303059b6774e46facc2790969bd08))
  - smc welcomePage 优化，菜单新增查看系统配置 (#167) ([8fdfcda](https://github.com/unknowIfGuestInDream/javafxTool/commit/8fdfcda7c273308e84b2785b27fa31a338506706))
  - girret review组件完成 (#164) ([4706a86](https://github.com/unknowIfGuestInDream/javafxTool/commit/4706a86c0873ba44fed06e1f8672b183ffaefaa1))
  - 新增getSampleId接口 (#159) ([def0ae6](https://github.com/unknowIfGuestInDream/javafxTool/commit/def0ae6f580250884a86d8041b47a078c0523df8))
  - core, smc功能完善 (#158) ([b0fe63b](https://github.com/unknowIfGuestInDream/javafxTool/commit/b0fe63b903025a85a8e31067412d8d87e4514a5f))
  - smc 国际化功能实现 (#157) ([9edb4fc](https://github.com/unknowIfGuestInDream/javafxTool/commit/9edb4fcd32b9dee88a900da3986bfbbb8625b88d))
  - 系统功能完善 (#155) ([8a2c28f](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a2c28fca0eb54813c16a8449bd2f87373063011))
  - 增加logback日志功能 (#154) ([6ed9012](https://github.com/unknowIfGuestInDream/javafxTool/commit/6ed9012c7eb08931b5c31bef2dad8477590057c5))
  - code style 组件完善 (#153) ([cb676f3](https://github.com/unknowIfGuestInDream/javafxTool/commit/cb676f335b3a60a31c691ae463efc7db727a9462))
  - code style组件完善 (#150) ([5559d65](https://github.com/unknowIfGuestInDream/javafxTool/commit/5559d6548c8a1659f0e9a1cda0f56a41baa0e66f))
  - code style组件完善 (#149) ([10c6956](https://github.com/unknowIfGuestInDream/javafxTool/commit/10c69566f3cae8e2d8de1689d1b0cc743c4c1649))
  - 修改工作流组件 (#132) ([ca89723](https://github.com/unknowIfGuestInDream/javafxTool/commit/ca89723108c9b134b98c59ac82e961c2a709e3cb))
  - 新增组件版本号 (#130) ([e168531](https://github.com/unknowIfGuestInDream/javafxTool/commit/e1685313a6086fd4194ce3b2625d8d4f690f1c45))
  - core功能完善 (#127) ([78f636f](https://github.com/unknowIfGuestInDream/javafxTool/commit/78f636f7ecb6ce43a8c34fa6b60ed33b8dd685a7))
  - 新增core模块 (#124) ([1db2060](https://github.com/unknowIfGuestInDream/javafxTool/commit/1db2060009e3e17f4851ea706d1eca482fd15ff3))
  - 完善smc FileDiff工具 (#123) ([d401506](https://github.com/unknowIfGuestInDream/javafxTool/commit/d401506b6d4ac717fa339ba661ccfd0e67bd88f8))
  - 文件比对初步实现 (#122) ([8a0476b](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a0476bb448336ffdb983c554ca2a5d0ec0b26d0))
  - frame新增关闭和确认关闭功能 (#121) ([b945df7](https://github.com/unknowIfGuestInDream/javafxTool/commit/b945df7fa21ddac54a290b9568c17b3d1309b85f))
  - 新增文件差异比对功能 (#119) ([cde0d4b](https://github.com/unknowIfGuestInDream/javafxTool/commit/cde0d4be904f3ce8ac465a9b3e0766db11bc7a5b))
  - 完善smc (#115) ([694d44c](https://github.com/unknowIfGuestInDream/javafxTool/commit/694d44caaee33a9fd2c1336df47803e79e9fc0fd))
  - 完善welcomePage (#114) ([22eb185](https://github.com/unknowIfGuestInDream/javafxTool/commit/22eb1858935b19d03e8fb6f7a4778083066d9a05))
  - 完善工作流 ([c32f5a1](https://github.com/unknowIfGuestInDream/javafxTool/commit/c32f5a1abac8a215190fc980d498dbafca641d7d))
  - 完善工作流 ([0dfcea5](https://github.com/unknowIfGuestInDream/javafxTool/commit/0dfcea5f6ff9c7b855c110571e0226691f47b535))
  - 完善工作流 ([06b5d00](https://github.com/unknowIfGuestInDream/javafxTool/commit/06b5d00f1c5e02689c9d8b07f1567519c58631e7))
  - 完善工作流 ([60b02d0](https://github.com/unknowIfGuestInDream/javafxTool/commit/60b02d03a1a862ee09a80307753a2cd4a00ce328))
  - 完善工作流 ([e51e547](https://github.com/unknowIfGuestInDream/javafxTool/commit/e51e547af93c806afa4a6c2b082fd7ab74a6854e))
  - 完善工作流 ([ebb498e](https://github.com/unknowIfGuestInDream/javafxTool/commit/ebb498e8cffa4a5165f006f68394280350f696eb))
  - 完善工作流 ([ad87660](https://github.com/unknowIfGuestInDream/javafxTool/commit/ad876608210f9bd4a18bf62e0d16b54d210f51b1))
  - 完善工作流 ([c218a65](https://github.com/unknowIfGuestInDream/javafxTool/commit/c218a65f3307a2048de360426bdedbb1bf90ab04))
  - 完善工作流 ([fe08635](https://github.com/unknowIfGuestInDream/javafxTool/commit/fe08635b7906655a7f431c796a186109183ad45f))
  - 完善工作流 (#93) ([bcf1d1d](https://github.com/unknowIfGuestInDream/javafxTool/commit/bcf1d1d0a87409e87bceb67633444a7e75ce5bc8))
  - 完善工作流 (#91) ([ea909aa](https://github.com/unknowIfGuestInDream/javafxTool/commit/ea909aa4721e349d0c6397d6326255d1518f8b1d))
  - 完善工作流 ([1c04c5e](https://github.com/unknowIfGuestInDream/javafxTool/commit/1c04c5e7b08151e5c15d7b90ed7216c572169fbf))
  - 完善工作流 ([16cc575](https://github.com/unknowIfGuestInDream/javafxTool/commit/16cc57517f78cb913a182bd8daa987d5171cc37d))
  - 完善工作流 ([7405f03](https://github.com/unknowIfGuestInDream/javafxTool/commit/7405f0387e58c7ad4972968d3412c24e7d8e1394))
  - 完善工作流 ([5619bad](https://github.com/unknowIfGuestInDream/javafxTool/commit/5619bad419a6dffff7c1eb3534879011786d4719))
  - 完善工作流 ([d2e008d](https://github.com/unknowIfGuestInDream/javafxTool/commit/d2e008d7bf9470a8366761d24ac99bac149c93b2))
  - 完善工作流 ([21c6a24](https://github.com/unknowIfGuestInDream/javafxTool/commit/21c6a2464bafe10e7335508dce0fb8443f00c040))
  - 完善工作流 (#74) ([6c194f4](https://github.com/unknowIfGuestInDream/javafxTool/commit/6c194f4363cb4c93e96fb8a258d5910556827c4c))
  - smc menubar增加图标 (#71) ([7880b44](https://github.com/unknowIfGuestInDream/javafxTool/commit/7880b44976feab75916a8142cfd440e76039f1bd))
  - 新增语言功能 (#70) ([f3e874b](https://github.com/unknowIfGuestInDream/javafxTool/commit/f3e874b5c7a07fc4af1e9bf8e3dd9c7f4378dbc3))
  - smc menubar 完善 (#66) ([9c5ca3f](https://github.com/unknowIfGuestInDream/javafxTool/commit/9c5ca3f994ecaaf6c79ecc19583f8be8a18942b6))
  - about功能初始实现 (#64) ([be027d8](https://github.com/unknowIfGuestInDream/javafxTool/commit/be027d8f32f2c54d11db83ecd8a55ab2792872b5))
  - configration接口实现 (#63) ([19abb23](https://github.com/unknowIfGuestInDream/javafxTool/commit/19abb23a11d66ef271957e80a155b41a9b33bd67))
  - smc模块 主程序菜单栏接口实现 (#61) ([abef11a](https://github.com/unknowIfGuestInDream/javafxTool/commit/abef11ae49f3a5593f3140cfa114c779ecec37a6))
  - 新增CodeStyleLength120 功能 (#60) ([10adab1](https://github.com/unknowIfGuestInDream/javafxTool/commit/10adab142f1d3fc9056921c7a4f179ea62d0e2bc))
  - 菜单排序接口优化 (#59) ([74f6d3e](https://github.com/unknowIfGuestInDream/javafxTool/commit/74f6d3ed7e023aadcb533995b4e4a59ab2a40884))
  - 新增 prefixSelection 组件demo (#56) ([25de062](https://github.com/unknowIfGuestInDream/javafxTool/commit/25de062a355ba6c311722fa888331a3a66d9c150))
  - 新增 MaskerPane 遮盖层 (#55) ([8f076e1](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f076e1a4a2023431f29f296ff38a6087d893614))
  - smc centerPanel 设计 (#51) ([8d32cf1](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d32cf1f9d18a0ead19012af7d35ac0abc5f395c))
  - 修改module.info文件 (#50) ([f144149](https://github.com/unknowIfGuestInDream/javafxTool/commit/f1441499c488c742b01cb04f68931d0b032eac6b))
  - smc初始化 (#47) ([fb033e4](https://github.com/unknowIfGuestInDream/javafxTool/commit/fb033e47fddadc8c53140a7a3294a95c789e144c))
  - Sample 设计调整 (#46) ([06d830e](https://github.com/unknowIfGuestInDream/javafxTool/commit/06d830ee19c0825c0c70b49fbf63b95b208afafe))
  - 完善工作流配置文件 ([6458c4a](https://github.com/unknowIfGuestInDream/javafxTool/commit/6458c4aebb026b7023a1821a0831d4e5083b7e66))
  - 完善工作流 (#41) ([067e476](https://github.com/unknowIfGuestInDream/javafxTool/commit/067e476a9d64761e91e59a6af6c37319b9e0f36f))
  - 完善demo (#39) ([a2f8f1f](https://github.com/unknowIfGuestInDream/javafxTool/commit/a2f8f1fd11dcc0c97c1a81a4a10500efe75eb1a3))
  - 添加工作流 (#38) ([5d89d78](https://github.com/unknowIfGuestInDream/javafxTool/commit/5d89d78112e380d379207939ba8f7e35b0eb01c1))
  - 初始化smc模块 (#33) ([e1b37c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/e1b37c5f8377c71ca4e72c16ee3e0fc25e45d084))
  - codecov工作流 ([8d0bb50](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d0bb508c1788b8664d86ce6c315989260e59bc7))
  - codecov工作流 ([e43421b](https://github.com/unknowIfGuestInDream/javafxTool/commit/e43421bf4c2f87a35268cfbba02a81022e576caf))
  - smc引用模块 (#30) ([564da58](https://github.com/unknowIfGuestInDream/javafxTool/commit/564da588186f1d531d2d08e6832c4f6111605804))
  - 完善README (#29) ([64dfa81](https://github.com/unknowIfGuestInDream/javafxTool/commit/64dfa812f066c146336afb5be50d610f6fcb8f58))
  - 完善README ([7cac5ea](https://github.com/unknowIfGuestInDream/javafxTool/commit/7cac5ea9cf6fef0850175858b2a233b2db60b86e))
  - 完善label工作流 ([7ac59ce](https://github.com/unknowIfGuestInDream/javafxTool/commit/7ac59ceb6dc46fd4765d44ef6e99a7e3936e05ae))
  - 完善label工作流 ([0c4b135](https://github.com/unknowIfGuestInDream/javafxTool/commit/0c4b135e59e2508d95c7a418fc713dc949144610))
  - 完善label工作流 (#27) ([7b8a218](https://github.com/unknowIfGuestInDream/javafxTool/commit/7b8a218ec88355ef07f3ff2da8ddbc16d080a6b5))
  - improve centerPanel SPI (#24) ([e6da1c2](https://github.com/unknowIfGuestInDream/javafxTool/commit/e6da1c282cb6676eced7e13f4b7ae52b4434f0e6))
  - 新增国际化支持 (#23) ([f106f6a](https://github.com/unknowIfGuestInDream/javafxTool/commit/f106f6a79dbe4f5443dbed4bf9303d7d3d6659ab))
  - improve登录模块 ([c971019](https://github.com/unknowIfGuestInDream/javafxTool/commit/c9710198393109796ff0a5a55b327daaee11ae55))
  - improve login module, support #15 (#22) ([9f309b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f309b67352ee6e2e345262509ae6210fd4d8ce0))
  - 修改login模块 (#21) ([c6c5b32](https://github.com/unknowIfGuestInDream/javafxTool/commit/c6c5b322918159e1295cb63e221027c6c34e571d))
  - 初始化登录框架 (#19) ([6aa5823](https://github.com/unknowIfGuestInDream/javafxTool/commit/6aa58235fdbc180605a54fcf0941b1fd563bd150))
  - 修复css引用错误 (#14) ([387311b](https://github.com/unknowIfGuestInDream/javafxTool/commit/387311b8e90f89b1e392d7ba2e41dce1a365192b))
  - 更新test依赖版本 ([c80a6b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/c80a6b6ad8f5edcf8af8bd8c43c0a4c8b559146c))
  - support menubar (#12) ([d465f76](https://github.com/unknowIfGuestInDream/javafxTool/commit/d465f76907373b55f6dc70149f27392686043f0c))
  - 修改菜单栏 (#10) ([614494e](https://github.com/unknowIfGuestInDream/javafxTool/commit/614494ed7cce3989ca314ab27c016d08d272ccdd))
  - menubar ([a52f267](https://github.com/unknowIfGuestInDream/javafxTool/commit/a52f267154f2b45dcbc4fbbdf69da73bf3d13fb4))
  - modify config. ([d16ccce](https://github.com/unknowIfGuestInDream/javafxTool/commit/d16ccce5f8ec9250efd265b79979479e44a608a8))

- smc:
  - girret review results功能完善 (#161) ([6882ca8](https://github.com/unknowIfGuestInDream/javafxTool/commit/6882ca863cda4eb2198dc6a54f0e8bab7b405bd7))

### Bug Fixes

- general:
  - 修复smc general #ifndef相关判断 (#320) ([cc48b0a](https://github.com/unknowIfGuestInDream/javafxTool/commit/cc48b0a6983d8320cf9a5d23fac3ab4ef314f62f))
  - 修复日语资源加载错误的问题 (#317) ([49556a8](https://github.com/unknowIfGuestInDream/javafxTool/commit/49556a8c3d29a934b3af54cfb3601e543c569e92))
  - 修复smc StdtriggerSourceDoc 生成错误 (#312) ([a8ec52f](https://github.com/unknowIfGuestInDream/javafxTool/commit/a8ec52f821060b668839433213e498dfb01bac64))
  - 功能修复 (#308) ([003bd4a](https://github.com/unknowIfGuestInDream/javafxTool/commit/003bd4a30a842160f6b6381bc4b44beafbd15f34))
  - 修复打包问题，以及打包后template加载器错误 (#288) ([a21675f](https://github.com/unknowIfGuestInDream/javafxTool/commit/a21675f39a1a3c0d30101f62f62372cef22d77c3))
  - 解决configuration2 加载list数据toString的问题 (#196) ([239dd0f](https://github.com/unknowIfGuestInDream/javafxTool/commit/239dd0f6ddc35066543d1643114606bb2dab6e40))
  - ExceptionDialog bug修复 (#175) ([bdbd108](https://github.com/unknowIfGuestInDream/javafxTool/commit/bdbd1082468440d4ddf32b5b8689c04ed1a0d3c3))
  - girret review bug修复 (#166) ([489725c](https://github.com/unknowIfGuestInDream/javafxTool/commit/489725c9854e6265751be735b16b75dab38eea81))
  - 修复路径错误 (#112) ([8a1fe03](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a1fe03fe4fc7f4238abdecbdeacd0a361c12202))
  - 修复css错误， 完善菜单栏国际化 (#68) ([1910569](https://github.com/unknowIfGuestInDream/javafxTool/commit/191056971caa04afd87d6bb84a9684d8c216445d))
  - 优化CenterPanelService 接口设计 (#53) ([f729b60](https://github.com/unknowIfGuestInDream/javafxTool/commit/f729b60ba323ec13a15d2ef2785b07e789f201c0))
  - 优化模块配置 (#49) ([317fb45](https://github.com/unknowIfGuestInDream/javafxTool/commit/317fb456f898139786041c32ec19357c78e77725))
  - 修复javafx:Jlink打包镜像错误 (#42) ([1e8f55f](https://github.com/unknowIfGuestInDream/javafxTool/commit/1e8f55f0eae1553b13288100ddb9b47ca6b8b401))
  - 修改工作流配置 ([c6ea3b9](https://github.com/unknowIfGuestInDream/javafxTool/commit/c6ea3b9dfffd3fb828cb7d861bed4a4417944b68))
  - 修改工作流配置 ([0992f7d](https://github.com/unknowIfGuestInDream/javafxTool/commit/0992f7d7620396749cc2779ea94a0f7bfe6d78e8))
  - 修复编译错误 ([171bf33](https://github.com/unknowIfGuestInDream/javafxTool/commit/171bf3386ff6c706f89b42018c8fdbbe95ee84eb))
  - 修复静态文件路径 (#6) ([311abf3](https://github.com/unknowIfGuestInDream/javafxTool/commit/311abf33a80aca54367f3a60358719becbaf96c4))

\* *This CHANGELOG was automatically generated by [auto-generate-changelog](https://github.com/BobAnkh/auto-generate-changelog)*
