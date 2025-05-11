# CHANGELOG

## [v1.0.1-qe](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.1-qe) - 2025-05-11 12:37:47

1. Add Keys.SkipBootAnimation config
2. Add HttpTool
3. Add RCP Icon Creator
4. Upgrade common tool
5. Dependency upgrade ( javafx to 21.0.7)

### Feature

- qe:
    - Improve SerialPortTool (
      #2138) ([a6739e1](https://github.com/unknowIfGuestInDream/javafxTool/commit/a6739e122b5046c46d648c30233add111dd5a248)) ([#2138](https://github.com/unknowIfGuestInDream/javafxTool/pull/2138))
    - Add SerialPortTool (
      #2021) ([80ff615](https://github.com/unknowIfGuestInDream/javafxTool/commit/80ff615bee6a44eb9b52be6f5b6c300af9da8bd8)) ([#2021](https://github.com/unknowIfGuestInDream/javafxTool/pull/2021))

- common:
    - Add HttpTool (
      #2142) ([58342e1](https://github.com/unknowIfGuestInDream/javafxTool/commit/58342e1a448b0047d8e602da5a2393a478bba0a3)) ([#2142](https://github.com/unknowIfGuestInDream/javafxTool/pull/2142))
    - Hidden some ControlPanel (
      #2012) ([da797a2](https://github.com/unknowIfGuestInDream/javafxTool/commit/da797a2d78ea1f20dd76fe89c14dd6ebd49c38bb)) ([#2012](https://github.com/unknowIfGuestInDream/javafxTool/pull/2012))

- core:
    - 支持JDK Tool ActionGroup (
      #2130) ([de11091](https://github.com/unknowIfGuestInDream/javafxTool/commit/de11091b6ceb5db5d06887908217ab11a907a62f)) ([#2130](https://github.com/unknowIfGuestInDream/javafxTool/pull/2130))
    - Add PropertiesDialog (
      #2097) ([4e53757](https://github.com/unknowIfGuestInDream/javafxTool/commit/4e537570b3ca7a5f6eaf3be4ccd0dcf9c7b58aed)) ([#2097](https://github.com/unknowIfGuestInDream/javafxTool/pull/2097))
    - Add deepseek support (
      #2123) ([34d8d0c](https://github.com/unknowIfGuestInDream/javafxTool/commit/34d8d0c06844280f7e18dde2cf178f448266b160)) ([#2123](https://github.com/unknowIfGuestInDream/javafxTool/pull/2123))
    - Add watermark 实现 (
      #2122) ([350d513](https://github.com/unknowIfGuestInDream/javafxTool/commit/350d513a6a8c4f3d68781e5db4bd3a68680df33c)) ([#2122](https://github.com/unknowIfGuestInDream/javafxTool/pull/2122))
    - Add SkipBootAnimation Config (
      #2020) ([94807e4](https://github.com/unknowIfGuestInDream/javafxTool/commit/94807e4161e1f2415a969be345144ae8c8df3627)) ([#2020](https://github.com/unknowIfGuestInDream/javafxTool/pull/2020))

- general:
    - Add
      cyclonedx-maven-plugin ([f3f6d61](https://github.com/unknowIfGuestInDream/javafxTool/commit/f3f6d61bd44d9182ba51061c2af3a0c030d63eaf))
    - Add powerShell support (
      #1998) ([b653cd6](https://github.com/unknowIfGuestInDream/javafxTool/commit/b653cd6a60db2105bc4ac771b1c6edbfd68cb311)) ([#1998](https://github.com/unknowIfGuestInDream/javafxTool/pull/1998))
    - Add JAXB test case (
      #1930) ([18257c2](https://github.com/unknowIfGuestInDream/javafxTool/commit/18257c22af827ab84a986b92eeebe1f947babe70)) ([#1930](https://github.com/unknowIfGuestInDream/javafxTool/pull/1930))
    - Prepare for smc
      1.0.11 ([555379a](https://github.com/unknowIfGuestInDream/javafxTool/commit/555379a6ebb502e48fa15e239966a651ae6f89eb))
    - Prepare for smc
      1.0.11 ([aea286d](https://github.com/unknowIfGuestInDream/javafxTool/commit/aea286dcd26c4e0ff207f5011b09f841ea7d69e7))
    - Prepare for smc
      1.0.11 ([fcba1a7](https://github.com/unknowIfGuestInDream/javafxTool/commit/fcba1a77ebf40a1b917d3ea61ddbecb7351a3464))
    - Prepare for smc
      1.0.11 ([75348d0](https://github.com/unknowIfGuestInDream/javafxTool/commit/75348d0c1e71311af0d71c7ba28f4e1052c8c4db))

### Bug Fixes

- qe:
    - 修复SkipBootAnimation 初始值异常 (
      #2139) ([e0759dd](https://github.com/unknowIfGuestInDream/javafxTool/commit/e0759ddf8cbe5f318de2a13d4579e45ac5baecbe)) ([#2139](https://github.com/unknowIfGuestInDream/javafxTool/pull/2139))

- general:
    - Fix stage hidden action In Mac Arm (
      #2133) ([e504a59](https://github.com/unknowIfGuestInDream/javafxTool/commit/e504a59998ec787bc1aa1d5540d7708d8b06d331)) ([#2133](https://github.com/unknowIfGuestInDream/javafxTool/pull/2133))
    - Fix FxJDKTool run error in Mac (
      #2132) ([707afeb](https://github.com/unknowIfGuestInDream/javafxTool/commit/707afebe552586948c97d0631d1e8aca307aaf26)) ([#2132](https://github.com/unknowIfGuestInDream/javafxTool/pull/2132))

- core:
    - beanutils升级后兼容问题 (
      #2116) ([c2e9178](https://github.com/unknowIfGuestInDream/javafxTool/commit/c2e91782c3835a487f2b0678da9681f0c1ce4619)) ([#2116](https://github.com/unknowIfGuestInDream/javafxTool/pull/2116))

## [v1.0.11-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.11-smc) - 2025-01-04 08:47:11

1. FileDiff optimization
2. Fixed BannerPrinterService loading exception after packaging
3. Modified groovy and freemarker script loading location
4. Fixed the problem of too long links in about
5. Add Keys.UseDevMode, initializeProperties, useDevMode and KeyCombination Config
6. GirretReview is adapted to girret 3.9
7. Improve download action
8. Support FullScreen
9. Add EasterEggService
10. Dependency upgrade ( javafx to 21.0.4)

### Feature

- general:
    - Prepare for smc 1.0.11 (
      #1910) ([1fb5e25](https://github.com/unknowIfGuestInDream/javafxTool/commit/1fb5e25502f4403792ee1ffb7a60d73084279725)) ([#1910](https://github.com/unknowIfGuestInDream/javafxTool/pull/1910))
    - Add PicHider (
      #1886) ([dbdc55c](https://github.com/unknowIfGuestInDream/javafxTool/commit/dbdc55c5872c805416187c09377f382fc2d5a666)) ([#1886](https://github.com/unknowIfGuestInDream/javafxTool/pull/1886))
    - Improve U2B DMA Read (
      #1857) ([9d4a886](https://github.com/unknowIfGuestInDream/javafxTool/commit/9d4a886523cb2462a290d7af3401000e9310442f)) ([#1857](https://github.com/unknowIfGuestInDream/javafxTool/pull/1857))
    - Improve CIEData1931 (
      #1754) ([c1aa95d](https://github.com/unknowIfGuestInDream/javafxTool/commit/c1aa95d09d791f99868d2cdcf41bd915bd2f1a03)) ([#1754](https://github.com/unknowIfGuestInDream/javafxTool/pull/1754))
    - Add ChromaticityDiagram (
      #1748) ([7f30321](https://github.com/unknowIfGuestInDream/javafxTool/commit/7f30321de078b12601d5bc19c59d8e38d40e2dc8)) ([#1748](https://github.com/unknowIfGuestInDream/javafxTool/pull/1748))
    - Add CS+ .map parse test (
      #1747) ([b212668](https://github.com/unknowIfGuestInDream/javafxTool/commit/b2126683067c9e0360f82ec12a0dd1dbaa0d8bd3)) ([#1747](https://github.com/unknowIfGuestInDream/javafxTool/pull/1747))
    - Add jSerialComm (
      #1713) ([7de0f8e](https://github.com/unknowIfGuestInDream/javafxTool/commit/7de0f8ef2529a1255aef610c558574c3f65f8e12)) ([#1713](https://github.com/unknowIfGuestInDream/javafxTool/pull/1713))
    - Disable EasterEgg in MAC (
      #1712) ([75e0eb0](https://github.com/unknowIfGuestInDream/javafxTool/commit/75e0eb0bee6690ad2063e13ac3ae0b2804bd448b)) ([#1712](https://github.com/unknowIfGuestInDream/javafxTool/pull/1712))
    - Upgrade javadoc (
      #1701) ([150441c](https://github.com/unknowIfGuestInDream/javafxTool/commit/150441c52485ac93ff4354347f2a4a711925dc42)) ([#1701](https://github.com/unknowIfGuestInDream/javafxTool/pull/1701))
    - Improve ZoomImageView (
      #1697) ([3215b85](https://github.com/unknowIfGuestInDream/javafxTool/commit/3215b855f0754f03482f747944347dfc0705915a)) ([#1697](https://github.com/unknowIfGuestInDream/javafxTool/pull/1697))
    - Add PlutoExplorer for zoom image (
      #1678) ([a0aee81](https://github.com/unknowIfGuestInDream/javafxTool/commit/a0aee81ea4432dbc51abceacafe413c0498f499a)) ([#1678](https://github.com/unknowIfGuestInDream/javafxTool/pull/1678))
    - Add ListImageView (
      #1696) ([434c469](https://github.com/unknowIfGuestInDream/javafxTool/commit/434c469996017dffe183a3f8eb506da910147fbe)) ([#1696](https://github.com/unknowIfGuestInDream/javafxTool/pull/1696))
    - Add maven-shade-plugin for exe package (
      #1610) ([9a806b8](https://github.com/unknowIfGuestInDream/javafxTool/commit/9a806b8559ff499a1c4175de756f39770935c51e)) ([#1610](https://github.com/unknowIfGuestInDream/javafxTool/pull/1610))
    - Improve jackson (
      #1609) ([d0c56ee](https://github.com/unknowIfGuestInDream/javafxTool/commit/d0c56eeaa0309e7a23c1ee0e3441b34a2e0193cb)) ([#1609](https://github.com/unknowIfGuestInDream/javafxTool/pull/1609))
    - (
      #1465) ([d557c4b](https://github.com/unknowIfGuestInDream/javafxTool/commit/d557c4b7853e96aa85ac9da11d8fb039e2784dcf)) ([#1465](https://github.com/unknowIfGuestInDream/javafxTool/pull/1465))
    - Improve iconTool (
      #1453) ([7b50777](https://github.com/unknowIfGuestInDream/javafxTool/commit/7b50777fc1f17bc1c28de14ff7607ee6585e15ad)) ([#1453](https://github.com/unknowIfGuestInDream/javafxTool/pull/1453))
    - Add impsort-maven-plugin (
      #1448) ([75bf0df](https://github.com/unknowIfGuestInDream/javafxTool/commit/75bf0dfea1d5c6618e3ae5bcf53d387fb16f926e)) ([#1448](https://github.com/unknowIfGuestInDream/javafxTool/pull/1448))
    - Add RegexTester (
      #1439) ([e9d5da5](https://github.com/unknowIfGuestInDream/javafxTool/commit/e9d5da5d9d2058f1d81f7f9348f2add250397269)) ([#1439](https://github.com/unknowIfGuestInDream/javafxTool/pull/1439))
    - Add ScanPortTool (
      #1394) ([4c02dbd](https://github.com/unknowIfGuestInDream/javafxTool/commit/4c02dbd3fb1fa510b0a9f75bd96205197e905605)) ([#1394](https://github.com/unknowIfGuestInDream/javafxTool/pull/1394))
    - Add IconTool (
      #1390) ([16e0f23](https://github.com/unknowIfGuestInDream/javafxTool/commit/16e0f23b4e80c82d27196c85385b0c92725ae4d1)) ([#1390](https://github.com/unknowIfGuestInDream/javafxTool/pull/1390))
    - Add DecorationCheckBox (
      #1389) ([b6b9670](https://github.com/unknowIfGuestInDream/javafxTool/commit/b6b96703f5563c3e4954db51ee273b0df8fc1917)) ([#1389](https://github.com/unknowIfGuestInDream/javafxTool/pull/1389))
    - Add common-email (
      #1379) ([13b20dc](https://github.com/unknowIfGuestInDream/javafxTool/commit/13b20dc9527521294fdac1d6c7d70b892dc1a96f)) ([#1379](https://github.com/unknowIfGuestInDream/javafxTool/pull/1379))
    - Add UrlEscape (
      #1371) ([622a95b](https://github.com/unknowIfGuestInDream/javafxTool/commit/622a95baee9e2825c3c9367f8cf84ac5bd5e500b)) ([#1371](https://github.com/unknowIfGuestInDream/javafxTool/pull/1371))
    - Add SubmitButton (
      #1370) ([3f9cb6d](https://github.com/unknowIfGuestInDream/javafxTool/commit/3f9cb6d280f4343a847fa03a9f368432c741e401)) ([#1370](https://github.com/unknowIfGuestInDream/javafxTool/pull/1370))
    - improve freemarker code (
      #1368) ([d76c1f3](https://github.com/unknowIfGuestInDream/javafxTool/commit/d76c1f3b76539080c04c611acfd68db7ea7571b0)) ([#1368](https://github.com/unknowIfGuestInDream/javafxTool/pull/1368))
    - Improve freemarker (
      #1358) ([54c4f12](https://github.com/unknowIfGuestInDream/javafxTool/commit/54c4f1270aa27bac4eee388c391a6335e7eea64a)) ([#1358](https://github.com/unknowIfGuestInDream/javafxTool/pull/1358))
    - Add ColorCode (
      #1356) ([42d9d64](https://github.com/unknowIfGuestInDream/javafxTool/commit/42d9d6485c86ac30249c2245a8b99231bd049b42)) ([#1356](https://github.com/unknowIfGuestInDream/javafxTool/pull/1356))
    - Improve Escape Tools (
      #1355) ([f01f4a1](https://github.com/unknowIfGuestInDream/javafxTool/commit/f01f4a15e6567ca3274cc7c5f267632e05bcb8dd)) ([#1355](https://github.com/unknowIfGuestInDream/javafxTool/pull/1355))
    - Add icons (
      #1354) ([b5c53b3](https://github.com/unknowIfGuestInDream/javafxTool/commit/b5c53b30a454243542a8caaac0448d1d4f510ba3)) ([#1354](https://github.com/unknowIfGuestInDream/javafxTool/pull/1354))
    - Add UnderlineTextField (
      #1353) ([92ac132](https://github.com/unknowIfGuestInDream/javafxTool/commit/92ac132881c74baa4055130f6a3bf9063eedfe46)) ([#1353](https://github.com/unknowIfGuestInDream/javafxTool/pull/1353))
    - Add properties-maven-plugin (
      #1352) ([e8191eb](https://github.com/unknowIfGuestInDream/javafxTool/commit/e8191ebbd658d159f58ecc786ef505173113417c)) ([#1352](https://github.com/unknowIfGuestInDream/javafxTool/pull/1352))
    - Add Switch (
      #1350) ([88cb11e](https://github.com/unknowIfGuestInDream/javafxTool/commit/88cb11ef67dc305271ce384884962d96d5c25a02)) ([#1350](https://github.com/unknowIfGuestInDream/javafxTool/pull/1350))
    - Improve HtmlEscape (
      #1347) ([d3d36cc](https://github.com/unknowIfGuestInDream/javafxTool/commit/d3d36cc9aca5920f98f96997731e5bc41e9d3dd9)) ([#1347](https://github.com/unknowIfGuestInDream/javafxTool/pull/1347))
    - Use teenyhttpd instead of hutool-http (
      #1342) ([67db5f9](https://github.com/unknowIfGuestInDream/javafxTool/commit/67db5f9064eb332b141344c7b62a5b1f377b94e4)) ([#1342](https://github.com/unknowIfGuestInDream/javafxTool/pull/1342))
    - Improve searchTextfield (
      #1318) ([5642d2a](https://github.com/unknowIfGuestInDream/javafxTool/commit/5642d2a79d5d649ddfd094adbb818c72bddbcee6)) ([#1318](https://github.com/unknowIfGuestInDream/javafxTool/pull/1318))
    - Add cssfx (
      #1316) ([b521db1](https://github.com/unknowIfGuestInDream/javafxTool/commit/b521db1b1c617b467f982f25ce8e26708e05bca3)) ([#1316](https://github.com/unknowIfGuestInDream/javafxTool/pull/1316))
    - Improve DecorationComboBox (
      #1312) ([f321052](https://github.com/unknowIfGuestInDream/javafxTool/commit/f3210524d4bdae598f338c16c65b5d5815e86e78)) ([#1312](https://github.com/unknowIfGuestInDream/javafxTool/pull/1312))
    - Improve FreemarkerTest (
      #1298) ([9f1f511](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f1f511aad8c12b93b6b618fc787f6cb34e73352)) ([#1298](https://github.com/unknowIfGuestInDream/javafxTool/pull/1298))
    - Add ResizePane (
      #1297) ([eb9aac4](https://github.com/unknowIfGuestInDream/javafxTool/commit/eb9aac4479c1560f1a8d932fd4431dba6e0ae450)) ([#1297](https://github.com/unknowIfGuestInDream/javafxTool/pull/1297))
    - Add ZoomImageView (
      #1295) ([d47f9c0](https://github.com/unknowIfGuestInDream/javafxTool/commit/d47f9c086bf7a18a0e34a2b81aeb95e75a7bcd0b)) ([#1295](https://github.com/unknowIfGuestInDream/javafxTool/pull/1295))
    - Add DecorationComboBox (
      #1291) ([1d9889a](https://github.com/unknowIfGuestInDream/javafxTool/commit/1d9889a3506179f07d77a3ef6ae01e0d8c61fb00)) ([#1291](https://github.com/unknowIfGuestInDream/javafxTool/pull/1291))
    - Improve DecorationTitlePane (
      #1275) ([b0c5f05](https://github.com/unknowIfGuestInDream/javafxTool/commit/b0c5f056a1a9cfdb59e2066763c4d3398fd98d54)) ([#1275](https://github.com/unknowIfGuestInDream/javafxTool/pull/1275))
    - Improve CodeRainState (
      #1289) ([3bab5a1](https://github.com/unknowIfGuestInDream/javafxTool/commit/3bab5a1204ff1f6410df703c29395ede0da6d1df)) ([#1289](https://github.com/unknowIfGuestInDream/javafxTool/pull/1289))
    - Add plantuml (
      #1287) ([9612953](https://github.com/unknowIfGuestInDream/javafxTool/commit/961295391abbe40fb7bfa4ea147e3e8603b6420c)) ([#1287](https://github.com/unknowIfGuestInDream/javafxTool/pull/1287))
    - Improve InterfaceScanner (
      #1286) ([7a5429c](https://github.com/unknowIfGuestInDream/javafxTool/commit/7a5429c43bff22375d05ae7d4fbc386d32019028)) ([#1286](https://github.com/unknowIfGuestInDream/javafxTool/pull/1286))
    - Add zip4j (
      #1274) ([293eda7](https://github.com/unknowIfGuestInDream/javafxTool/commit/293eda7aecfce63ee0f80e3c789f40667ec89901)) ([#1274](https://github.com/unknowIfGuestInDream/javafxTool/pull/1274))
    - Improve DecorationTextfieldSkin (
      #1268) ([dd98d0a](https://github.com/unknowIfGuestInDream/javafxTool/commit/dd98d0a18a5a5b9e74ca3bc7d1877d6b55c358bf)) ([#1268](https://github.com/unknowIfGuestInDream/javafxTool/pull/1268))
    - Improve CustomTextField (
      #1266) ([56f8717](https://github.com/unknowIfGuestInDream/javafxTool/commit/56f8717e0e5b88c60986172fb74dc14eb6af59f3)) ([#1266](https://github.com/unknowIfGuestInDream/javafxTool/pull/1266))
    - Improve DecorationTextfieldSkin (
      #1258) ([aaca54a](https://github.com/unknowIfGuestInDream/javafxTool/commit/aaca54a20431e5b1e393eeb3cdc681b652a8a6a5)) ([#1258](https://github.com/unknowIfGuestInDream/javafxTool/pull/1258))
    - Improve ScalableContentPane (
      #1257) ([2549964](https://github.com/unknowIfGuestInDream/javafxTool/commit/2549964cde007a61b6eda6d9c9ae0fe7bcadfe6a)) ([#1257](https://github.com/unknowIfGuestInDream/javafxTool/pull/1257))
    - Improve Borders (
      #1256) ([531d002](https://github.com/unknowIfGuestInDream/javafxTool/commit/531d00224b09abd1f4082c881b187c79e86ccc83)) ([#1256](https://github.com/unknowIfGuestInDream/javafxTool/pull/1256))
    - Add checker-qual (
      #1245) ([68748b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/68748b63aa31850d1661c94ea108d134aac8f083)) ([#1245](https://github.com/unknowIfGuestInDream/javafxTool/pull/1245))
    - Use classgraph instead of reflections (
      #1237) ([22a814a](https://github.com/unknowIfGuestInDream/javafxTool/commit/22a814aecb8969d9d2eaa7d069129e75f968c253)) ([#1237](https://github.com/unknowIfGuestInDream/javafxTool/pull/1237))
    - Improve RangeSlider (
      #1236) ([c148ad7](https://github.com/unknowIfGuestInDream/javafxTool/commit/c148ad7eef0282ba2ebab44246d9a690f2581696)) ([#1236](https://github.com/unknowIfGuestInDream/javafxTool/pull/1236))
    - Improve ColourUtil and DecorationTextfield (
      #1232) ([c331105](https://github.com/unknowIfGuestInDream/javafxTool/commit/c331105206f9e6927ded72feffb60408b11eff09)) ([#1232](https://github.com/unknowIfGuestInDream/javafxTool/pull/1232))
    - Add FileDiffWithWeb (
      #1225) ([5523637](https://github.com/unknowIfGuestInDream/javafxTool/commit/552363759ade1ced356b1fea5f792e87aa94a13f)) ([#1225](https://github.com/unknowIfGuestInDream/javafxTool/pull/1225))
    - Improve javafx-web scope (
      #1224) ([fd6795e](https://github.com/unknowIfGuestInDream/javafxTool/commit/fd6795e82d9cf345ac1a3e101b01f248ddd3c562)) ([#1224](https://github.com/unknowIfGuestInDream/javafxTool/pull/1224))
    - Add DecorationTextfield (
      #1222) ([7070200](https://github.com/unknowIfGuestInDream/javafxTool/commit/707020000014388adb42f39fc25ca45ff40048de)) ([#1222](https://github.com/unknowIfGuestInDream/javafxTool/pull/1222))
    - Improve DiffHandleUtil (
      #1201) ([74e4bd6](https://github.com/unknowIfGuestInDream/javafxTool/commit/74e4bd624da14789c2d9eb1a2fad003d1b04ed89)) ([#1201](https://github.com/unknowIfGuestInDream/javafxTool/pull/1201))
    - Add RegorDirective (
      #1198) ([81042c6](https://github.com/unknowIfGuestInDream/javafxTool/commit/81042c68b1a4eb0405c9d03a0a64a6dfcb2e76e5)) ([#1198](https://github.com/unknowIfGuestInDream/javafxTool/pull/1198))
    - Improve preference (
      #1197) ([de5ceed](https://github.com/unknowIfGuestInDream/javafxTool/commit/de5ceede2fe7c43936d1545fc4df8c9f0980bef6)) ([#1197](https://github.com/unknowIfGuestInDream/javafxTool/pull/1197))
    - Add Jssc (
      #1195) ([608a3dd](https://github.com/unknowIfGuestInDream/javafxTool/commit/608a3dde550052ea5940e22d26e4e2c01a7a6e49)) ([#1195](https://github.com/unknowIfGuestInDream/javafxTool/pull/1195))
    - Add ColourUtil (
      #1192) ([5f8136f](https://github.com/unknowIfGuestInDream/javafxTool/commit/5f8136fbc462553a7fe970273840d18c4ef6eb17)) ([#1192](https://github.com/unknowIfGuestInDream/javafxTool/pull/1192))
    - Add fullscreen menu (
      #1187) ([377c139](https://github.com/unknowIfGuestInDream/javafxTool/commit/377c1395e907092bcf380bd8a65cd5745dc6ffb9)) ([#1187](https://github.com/unknowIfGuestInDream/javafxTool/pull/1187))
    - Add coor2Rgb (
      #1186) ([51659df](https://github.com/unknowIfGuestInDream/javafxTool/commit/51659df88868cd5c3a9c69e107f5ec80c8e24525)) ([#1186](https://github.com/unknowIfGuestInDream/javafxTool/pull/1186))
    - Add SearchableComboBox (
      #1183) ([92bfb8c](https://github.com/unknowIfGuestInDream/javafxTool/commit/92bfb8c66039fec9b5d98db744f1c6a78b3ea22c)) ([#1183](https://github.com/unknowIfGuestInDream/javafxTool/pull/1183))
    - Upgrade jfx to 21.0.1 (
      #1182) ([8c43bda](https://github.com/unknowIfGuestInDream/javafxTool/commit/8c43bdadeac46d2020e2fd389ddf312454841e51)) ([#1182](https://github.com/unknowIfGuestInDream/javafxTool/pull/1182))
    - Add RangeSlider (
      #1177) ([fc89e7f](https://github.com/unknowIfGuestInDream/javafxTool/commit/fc89e7fe15a88a9fca1e64417b9c5c475222fb96)) ([#1177](https://github.com/unknowIfGuestInDream/javafxTool/pull/1177))
    - Add WeakConcurrent (
      #1171) ([19aba74](https://github.com/unknowIfGuestInDream/javafxTool/commit/19aba74ebf1ddbe156ac83b918c53b83ec40787a)) ([#1171](https://github.com/unknowIfGuestInDream/javafxTool/pull/1171))
    - Add ColorUtil (
      #1168) ([d1de855](https://github.com/unknowIfGuestInDream/javafxTool/commit/d1de85567bbca4045c002f8faa1cb89dd1c7cf93)) ([#1168](https://github.com/unknowIfGuestInDream/javafxTool/pull/1168))
    - Improve fileDiff (
      #1142) ([dfa8396](https://github.com/unknowIfGuestInDream/javafxTool/commit/dfa8396d74125dadf46bb7c35af8f5bc0573db1d)) ([#1142](https://github.com/unknowIfGuestInDream/javafxTool/pull/1142))
    - Add CIE data (
      #1141) ([2bd40b0](https://github.com/unknowIfGuestInDream/javafxTool/commit/2bd40b0851976f2b4b45a621ec99433dcc32a4c9)) ([#1141](https://github.com/unknowIfGuestInDream/javafxTool/pull/1141))
    - Add ProcessUtil (
      #1138) ([d2a26c2](https://github.com/unknowIfGuestInDream/javafxTool/commit/d2a26c2c0345333623f354670979afe366d500d8)) ([#1138](https://github.com/unknowIfGuestInDream/javafxTool/pull/1138))
    - Add EasterEggService (
      #1136) ([9a129f8](https://github.com/unknowIfGuestInDream/javafxTool/commit/9a129f8969ca6d7a8dcb076107b1dad68f8d29a2)) ([#1136](https://github.com/unknowIfGuestInDream/javafxTool/pull/1136))
    - Add DataSourceUtil (
      #1135) ([8ee86de](https://github.com/unknowIfGuestInDream/javafxTool/commit/8ee86de82be4fb6ece0262e216423a791e873a07)) ([#1135](https://github.com/unknowIfGuestInDream/javafxTool/pull/1135))
    - Add chart support (
      #1132) ([e28a91c](https://github.com/unknowIfGuestInDream/javafxTool/commit/e28a91cae69055b8f20d2ebb0a5b6ee7cfd30528)) ([#1132](https://github.com/unknowIfGuestInDream/javafxTool/pull/1132))
    - Add WebViewStage (
      #1122) ([00f9f13](https://github.com/unknowIfGuestInDream/javafxTool/commit/00f9f132f5e78b815002c7bd8e0d3dea57468905)) ([#1122](https://github.com/unknowIfGuestInDream/javafxTool/pull/1122))
    - Modify logback config (
      #1130) ([49709e7](https://github.com/unknowIfGuestInDream/javafxTool/commit/49709e7c7c91b2da37befc2e9dabc27fc76a2322)) ([#1130](https://github.com/unknowIfGuestInDream/javafxTool/pull/1130))
    - Improve logback (
      #1127) ([0fd0b1d](https://github.com/unknowIfGuestInDream/javafxTool/commit/0fd0b1dd6167a98849e1d08f284846db884875c6)) ([#1127](https://github.com/unknowIfGuestInDream/javafxTool/pull/1127))
    - Improve freemarker (
      #1107) ([c67bdae](https://github.com/unknowIfGuestInDream/javafxTool/commit/c67bdaea9885dbcf01c9fa3a06757502d843d093)) ([#1107](https://github.com/unknowIfGuestInDream/javafxTool/pull/1107))
    - Add HexDirective (
      #1099) ([8591422](https://github.com/unknowIfGuestInDream/javafxTool/commit/85914220a4e3ac199d20e19eeee5516d962a5a20)) ([#1099](https://github.com/unknowIfGuestInDream/javafxTool/pull/1099))
    - Support h2 (
      #1090) ([975336f](https://github.com/unknowIfGuestInDream/javafxTool/commit/975336f1c8e1ad3ff8513820507228660ec3e89a)) ([#1090](https://github.com/unknowIfGuestInDream/javafxTool/pull/1090))
    - Add KeyCombination (
      #1095) ([3c2fd17](https://github.com/unknowIfGuestInDream/javafxTool/commit/3c2fd17b4049862f072b113a650af6816b7b485d)) ([#1095](https://github.com/unknowIfGuestInDream/javafxTool/pull/1095))
    - Add IterateINI for DSL (
      #1085) ([b6d9ddc](https://github.com/unknowIfGuestInDream/javafxTool/commit/b6d9ddc0b8d6cd2448407981a5a8dacd673df0a9)) ([#1085](https://github.com/unknowIfGuestInDream/javafxTool/pull/1085))
    - Add DSL tool (
      #1084) ([ecd3a2f](https://github.com/unknowIfGuestInDream/javafxTool/commit/ecd3a2f7504bb1b0634a8f5e9fb878f7ca3b06cf)) ([#1084](https://github.com/unknowIfGuestInDream/javafxTool/pull/1084))
    - Support useDevMode (
      #1079) ([8bddcab](https://github.com/unknowIfGuestInDream/javafxTool/commit/8bddcabd7bdcc1b2f9d8e6142d422bda6fe1a9dd)) ([#1079](https://github.com/unknowIfGuestInDream/javafxTool/pull/1079))
    - Add Keys.UseDevMode and initializeProperties (
      #1068) ([2c3d652](https://github.com/unknowIfGuestInDream/javafxTool/commit/2c3d652590c3cb40840414b13ccbde05cd7ef608)) ([#1068](https://github.com/unknowIfGuestInDream/javafxTool/pull/1068))
    - 优化about中链接过长的问题 (
      #1067) ([39345a9](https://github.com/unknowIfGuestInDream/javafxTool/commit/39345a9747209dc2ad82293ff931021b77b99723)) ([#1067](https://github.com/unknowIfGuestInDream/javafxTool/pull/1067))
    - Improve get or download result (
      #1064) ([e182109](https://github.com/unknowIfGuestInDream/javafxTool/commit/e182109b716023af246b2ce73b6c2086675eb700)) ([#1064](https://github.com/unknowIfGuestInDream/javafxTool/pull/1064))
    - Modify demo Borders (
      #1055) ([ced56d8](https://github.com/unknowIfGuestInDream/javafxTool/commit/ced56d8e2811ce9806dd9900442ba3964bec9711)) ([#1055](https://github.com/unknowIfGuestInDream/javafxTool/pull/1055))
    - FileDiff优化，用户指定文件名 (
      #1053) ([0764bb9](https://github.com/unknowIfGuestInDream/javafxTool/commit/0764bb901b14114fcf00b6a5e9d6ff6d2779c005)) ([#1053](https://github.com/unknowIfGuestInDream/javafxTool/pull/1053))
    - Support common develop standalone (
      #1040) ([8aa216e](https://github.com/unknowIfGuestInDream/javafxTool/commit/8aa216ea164e6c3f18d345ed01a31897630846eb)) ([#1040](https://github.com/unknowIfGuestInDream/javafxTool/pull/1040))
    - Add ScheduledTaskExecutor (
      #1037) ([83ffda8](https://github.com/unknowIfGuestInDream/javafxTool/commit/83ffda8eeec17833153dd07c682ad6be2ba79812)) ([#1037](https://github.com/unknowIfGuestInDream/javafxTool/pull/1037))
    - Add SnowApp (
      #898) ([fa8ac1f](https://github.com/unknowIfGuestInDream/javafxTool/commit/fa8ac1f532903bf8436c429437bd62ec5034fbea)) ([#898](https://github.com/unknowIfGuestInDream/javafxTool/pull/898))
    - Add testfx (
      #899) ([4a33ef3](https://github.com/unknowIfGuestInDream/javafxTool/commit/4a33ef30861d862a8391e895661f2d973785e9dc)) ([#899](https://github.com/unknowIfGuestInDream/javafxTool/pull/899))
    - Add bytebuddy (
      #948) ([dc3153e](https://github.com/unknowIfGuestInDream/javafxTool/commit/dc3153e968e1db3e22b7c54c53a3e2d854d4db41)) ([#948](https://github.com/unknowIfGuestInDream/javafxTool/pull/948))
    - Add jython support (
      #942) ([1c6eed2](https://github.com/unknowIfGuestInDream/javafxTool/commit/1c6eed223e17077930ea0ebe2661470898d1200d)) ([#942](https://github.com/unknowIfGuestInDream/javafxTool/pull/942))
    - Add banner print (
      #941) ([959d481](https://github.com/unknowIfGuestInDream/javafxTool/commit/959d481589ef5eb2225f91e60aba415c3c1c45f6)) ([#941](https://github.com/unknowIfGuestInDream/javafxTool/pull/941))
    - JacksonUtil重构, 支持yaml (
      #923) ([d4bc0db](https://github.com/unknowIfGuestInDream/javafxTool/commit/d4bc0db005976ec673b677bb0f9b0932cde8a1df)) ([#923](https://github.com/unknowIfGuestInDream/javafxTool/pull/923))

- common:
    - Add ImageSplit (
      #1309) ([4ed122c](https://github.com/unknowIfGuestInDream/javafxTool/commit/4ed122ce6864a1408ea36e61b41ca5a7d2fb79ea)) ([#1309](https://github.com/unknowIfGuestInDream/javafxTool/pull/1309))
    - Improve AsciiPicTool (
      #1307) ([914bec6](https://github.com/unknowIfGuestInDream/javafxTool/commit/914bec6d964425d3d324dca0aa38abbbc2ad5350)) ([#1307](https://github.com/unknowIfGuestInDream/javafxTool/pull/1307))
    - Add AsciiPicTool (
      #1302) ([42e32bc](https://github.com/unknowIfGuestInDream/javafxTool/commit/42e32bcc3b4939ba81406e3f34e4e76c10b84044)) ([#1302](https://github.com/unknowIfGuestInDream/javafxTool/pull/1302))

- core:
    - Improve TemplateLoaderScanner (
      #1100) ([e7c7232](https://github.com/unknowIfGuestInDream/javafxTool/commit/e7c723251c83e1a17205a4f627f3a6c6998f3048)) ([#1100](https://github.com/unknowIfGuestInDream/javafxTool/pull/1100))
    - Add Borders (
      #1094) ([0f3e0e5](https://github.com/unknowIfGuestInDream/javafxTool/commit/0f3e0e5b852503a0b7d6cb03efec161cac2d4f59)) ([#1094](https://github.com/unknowIfGuestInDream/javafxTool/pull/1094))
    - Add tess4j (
      #973) ([3efffa7](https://github.com/unknowIfGuestInDream/javafxTool/commit/3efffa7ba7a435ec081f3c794f6f1f47489046ef)) ([#973](https://github.com/unknowIfGuestInDream/javafxTool/pull/973))

- frame:
    - Support FullScreen (
      #1098) ([032ef36](https://github.com/unknowIfGuestInDream/javafxTool/commit/032ef36dc357c84dad7d9b931c450b48d09db080)) ([#1098](https://github.com/unknowIfGuestInDream/javafxTool/pull/1098))

- smc:
    - Improve download action (
      #1093) ([4563752](https://github.com/unknowIfGuestInDream/javafxTool/commit/4563752a4642f02b3d3afaba4c382d875dfe9cd8)) ([#1093](https://github.com/unknowIfGuestInDream/javafxTool/pull/1093))
    - GirretReview适配girret 3.9 (
      #1075) ([440249f](https://github.com/unknowIfGuestInDream/javafxTool/commit/440249f838ceb5fb502ecf3b4ea76be9e65dec09)) ([#1075](https://github.com/unknowIfGuestInDream/javafxTool/pull/1075))
    - 修改groovy和freemarker脚本加载位置 (
      #952) ([35c141c](https://github.com/unknowIfGuestInDream/javafxTool/commit/35c141c7aa92dbf3afabd0139d9b42e701e9fc9c)) ([#952](https://github.com/unknowIfGuestInDream/javafxTool/pull/952))

### Bug Fixes

- general:
    - 修复升级javadoc的生成路径修改问题 (
      #1690) ([a1a0eb3](https://github.com/unknowIfGuestInDream/javafxTool/commit/a1a0eb366e7c23320fc38dba5cfb0f0b2e1dd9a3)) ([#1690](https://github.com/unknowIfGuestInDream/javafxTool/pull/1690))
    - Update pom.xml (
      #1680) ([e3a566a](https://github.com/unknowIfGuestInDream/javafxTool/commit/e3a566a467dfbc67f1727eeb3a49860c89612101)) ([#1680](https://github.com/unknowIfGuestInDream/javafxTool/pull/1680))
    - Use reflections instead of classgraph (
      #1416) ([c22cbfe](https://github.com/unknowIfGuestInDream/javafxTool/commit/c22cbfe0a87a2f135add2dc3305628a623f31ca4)) ([#1416](https://github.com/unknowIfGuestInDream/javafxTool/pull/1416))
    - Build result error (
      #1284) ([ca77493](https://github.com/unknowIfGuestInDream/javafxTool/commit/ca7749358a620569389ceaec0d163d0f20466c76)) ([#1284](https://github.com/unknowIfGuestInDream/javafxTool/pull/1284))
    - Fix getDiffHtml error (
      #1246) ([2264f40](https://github.com/unknowIfGuestInDream/javafxTool/commit/2264f40d2032566f3d22359730ba67acfb6ea55f)) ([#1246](https://github.com/unknowIfGuestInDream/javafxTool/pull/1246))
    - 修复日期判断问题 (
      #1160) ([c0944b1](https://github.com/unknowIfGuestInDream/javafxTool/commit/c0944b16ee09f68b5fa5c2064c500f89d27f9a7b)) ([#1160](https://github.com/unknowIfGuestInDream/javafxTool/pull/1160))
    - fix DefaultEasterEggService close fail (
      #1137) ([d83377f](https://github.com/unknowIfGuestInDream/javafxTool/commit/d83377f511091202f286b8248b69225d0b7dd28c)) ([#1137](https://github.com/unknowIfGuestInDream/javafxTool/pull/1137))

- frame:
    - 修改打包后BannerPrinterService加载异常 (
      #954) ([7438fb8](https://github.com/unknowIfGuestInDream/javafxTool/commit/7438fb8a8a879ef3748478a345e4cfcfe6f02f9a)) ([#954](https://github.com/unknowIfGuestInDream/javafxTool/pull/954))

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
    - Improve theme (
      #892) ([1051907](https://github.com/unknowIfGuestInDream/javafxTool/commit/10519073689e9c9cc7597779fffc3f33463c9eb7))
    - Improve SystemSetting (
      #891) ([e4477c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/e4477c58a8a194ad79664c757d0a6504136ff68f))
    - 语言切换功能集成 (
      #876) ([9bf9754](https://github.com/unknowIfGuestInDream/javafxTool/commit/9bf975415dedc302815ecb687af9909de783bb4c))
    - simpleHttpServer支持 (
      #864) ([e315622](https://github.com/unknowIfGuestInDream/javafxTool/commit/e31562256676803d5fbea6dc66a8ec1a503e7b6c))

- frame:
    - Improve VersionCheckerService (
      #867) ([76129b4](https://github.com/unknowIfGuestInDream/javafxTool/commit/76129b475af73ffe2048f8cbcef8294a56c8afb5))

- qe:
    - DALI UI调整 (
      #813) ([377e672](https://github.com/unknowIfGuestInDream/javafxTool/commit/377e672a6abddd919c42dc66511a37b479f8273f))

- cg:
    - 初始化CG (
      #804) ([2883c79](https://github.com/unknowIfGuestInDream/javafxTool/commit/2883c797894015a2ff159970f68ecf52fcacc1f1))

### Bug Fixes

- general:
    - 修复mac 下动画闪屏后UI未显示的问题 (
      #833) ([6fd8ade](https://github.com/unknowIfGuestInDream/javafxTool/commit/6fd8adeab02fef481f58e3fe4ea36788c9b92f05))

## [v1.0.0-qe](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.0-qe) - 2023-10-02 02:34:44

1. First release
2. JS/CSS compression

### Feature

- common:
    - 增加DataFormatConvert (
      #799) ([ee72a1b](https://github.com/unknowIfGuestInDream/javafxTool/commit/ee72a1b86771dddbfe504a368ee256bbb6e5261e))
    - 新增DataFormatConvert工具 (
      #773) ([6c29ff0](https://github.com/unknowIfGuestInDream/javafxTool/commit/6c29ff0723b0f97d39171cd38bc34c0cbaf084b9))
    - common模块设计修改 (
      #787) ([a8c9fad](https://github.com/unknowIfGuestInDream/javafxTool/commit/a8c9fad5080236186b0e623e4894986f76f5f889))

- qe:
    - 补充compress的说明 (
      #770) ([3c2b665](https://github.com/unknowIfGuestInDream/javafxTool/commit/3c2b665e0527007b2ece7b6caaf3125804d99d0b))
    - 调整动画UI (
      #784) ([c2d7ab0](https://github.com/unknowIfGuestInDream/javafxTool/commit/c2d7ab0d28a5439a8df16cebd8a51143ac189de6))
    - 更换启动logo (
      #781) ([d376a25](https://github.com/unknowIfGuestInDream/javafxTool/commit/d376a258a6c9cc131643203fbd5a747aa20dcea0))
    - js/css压缩 (
      #755) ([0e55f0c](https://github.com/unknowIfGuestInDream/javafxTool/commit/0e55f0c1e3a9ff7b8616223185172fa1a763fd53))
    - dali config添加 (
      #742) ([bb89e04](https://github.com/unknowIfGuestInDream/javafxTool/commit/bb89e04ca3cadb84faa3cf4b789d5ef488d094f2))

- frame:
    - 接口重命名 (
      #780) ([b993080](https://github.com/unknowIfGuestInDream/javafxTool/commit/b993080c628d51d4f0f7f0cbdfa0f04f1dd08cb0))
    - 隐藏闪屏的托盘图标 (
      #779) ([f439109](https://github.com/unknowIfGuestInDream/javafxTool/commit/f4391097cb0a91d0ef62d6b0fd1d600728c1c4f8))
    - 优化启动动画时的托盘图标 (
      #772) ([3654320](https://github.com/unknowIfGuestInDream/javafxTool/commit/3654320b1896d9a1cde1185851374693b36cfdb1))
    - 新增菜单缓存 (
      #748) ([8d4eb83](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d4eb83f5385621cfb637b3974bc7f0ce45e7333))
    - 接口权限控制 (
      #750) ([fcff569](https://github.com/unknowIfGuestInDream/javafxTool/commit/fcff569bf0f97dd565cb9bcbcd97ced16ce4ebfe))
    - 增加选择主题接口 (
      #747) ([9133dba](https://github.com/unknowIfGuestInDream/javafxTool/commit/9133dbaf0f31c295d67b36a2a68f3f99d2f76144))

- core:
    - ProgressStage增加高斯模糊效果 (
      #769) ([2c63b93](https://github.com/unknowIfGuestInDream/javafxTool/commit/2c63b93eed1a81297d2f45b7dcbe0b344b22bd29))
    - Jackson配置优化 (
      #761) ([11efd7c](https://github.com/unknowIfGuestInDream/javafxTool/commit/11efd7c7e9cf6bfecdddef4499376fd54b5f5946))
    - 新增ScreenColorPickerHideWindow配置 (
      #734) ([caadc4e](https://github.com/unknowIfGuestInDream/javafxTool/commit/caadc4ebfdb071c7efa2f4ac1f90805480bfc6fe))

- smc:
    - girret工具bug修复 (
      #765) ([4c8335a](https://github.com/unknowIfGuestInDream/javafxTool/commit/4c8335ac0d64c4b4ff1531e458883350df36866a))
    - specGeneral使能条件优化，功能优化 (
      #757) ([332e158](https://github.com/unknowIfGuestInDream/javafxTool/commit/332e15839275bfaf75d708c52174c8311ccd0606))
    - SpecGeneralTest UI使能条件优化 (
      #754) ([e020e5e](https://github.com/unknowIfGuestInDream/javafxTool/commit/e020e5e18ddef1d3f8c619e33486bef2f2713e6f))
    - 修复按钮的国际化问题 (
      #719) ([08be18e](https://github.com/unknowIfGuestInDream/javafxTool/commit/08be18e09d30f1d6e6938b9b54d7b5713df9c955))

- general:
    - jackson替换hutool-json (
      #756) ([1f503e0](https://github.com/unknowIfGuestInDream/javafxTool/commit/1f503e057ff0f0f95e2321688ab94bd6f7c4ebbb))

### Bug Fixes

- frame:
    - 修复mac arm环境下隐藏失败的问题 (
      #783) ([cc437a6](https://github.com/unknowIfGuestInDream/javafxTool/commit/cc437a68a0eadc015b385d0c7bf87a9575314536))

- core:
    - 修复导入设置取消时的异常重启 (
      #777) ([28a2acc](https://github.com/unknowIfGuestInDream/javafxTool/commit/28a2acccedeba5becdd7f4dd7fb53ac02cfe4937))
    - PathWatchTool国际化错误修复 (
      #760) ([8f97804](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f97804591a43e336c765c7d418a56e6cb8f116f))

## [v1.0.8-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.8-smc) - 2023-09-10 06:28:48

1. GroovyCodeArea stack overflow fixed and style modified
2. Modify LicenseDialog style
3. Support startup animation
4. Modify theme style

### Feature

- smc:
    - 修改主题 (
      #715) ([9ea0da0](https://github.com/unknowIfGuestInDream/javafxTool/commit/9ea0da0773bbc99b67dd3dc803110c5913e2d1f2))
    - 获取菜单树数据方法重构 (
      #713) ([58577a2](https://github.com/unknowIfGuestInDream/javafxTool/commit/58577a21cf517e8b315795d0b199b211a62ed100))

- frame:
    - 闪屏功能支持动画效果 (
      #709) ([764e3c2](https://github.com/unknowIfGuestInDream/javafxTool/commit/764e3c25aeedee25ed0ec9277d1962a7b0d88a23))

- core:
    - groovyCodeArea 注释收缩样式 (
      #708) ([428a117](https://github.com/unknowIfGuestInDream/javafxTool/commit/428a117ec3b095d16435a4dd3390b6e90d5d4ee4))
    - preference view配置加载功能优化 (
      #706) ([a886573](https://github.com/unknowIfGuestInDream/javafxTool/commit/a8865737dc0b569ea87bc290b144c5416f3831fa))
    - JavaCodeArea&GroovyCodeArea样式修改 (
      #704) ([831a25a](https://github.com/unknowIfGuestInDream/javafxTool/commit/831a25a30d0a1b0f6520dd683e84c36f9f636446))

### Bug Fixes

- frame:
    - 修复启动图片关闭失败的问题 (
      #712) ([98e8863](https://github.com/unknowIfGuestInDream/javafxTool/commit/98e88632608b22337257a241bd3b4344c14b5896))

- core:
    - 修复LicenseDialog样式问题 (
      #701) ([6daa0f7](https://github.com/unknowIfGuestInDream/javafxTool/commit/6daa0f7e022b655443a1f1e9668037487c1d11b0))
    - 修复groovyCodeArea处理开头版权信息时堆栈溢出问题 (
      #696) ([9b278dc](https://github.com/unknowIfGuestInDream/javafxTool/commit/9b278dc5ef959995d29145c3f49375db7a32db35))

## [v1.0.7-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.7-smc) - 2023-09-03 09:39:14

1. Fixed CoreUtil.getRootPath() result error issue
2. Fix groovy path scanning error issue
3. ECM code refactoring
4. Add preferencesfx to refactor the system settings UI

### Feature

- smc:
    - ecm 优化 (
      #692) ([bcfdb79](https://github.com/unknowIfGuestInDream/javafxTool/commit/bcfdb79eace54efab41fe3fa2d718fecba643332))
    - ECM重构 (
      #691) ([f077b15](https://github.com/unknowIfGuestInDream/javafxTool/commit/f077b152d0bc6a224a30420bf18967c43ebfbe6e))

### Bug Fixes

- core:
    - 修复groovy路径扫描错误的问题 (
      #687) ([9f07055](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f07055069ece8dc30ed3434a930709f5d8c9089))
    - 修复CoreUtil.getRootPath()问题 (
      #686) ([24e5496](https://github.com/unknowIfGuestInDream/javafxTool/commit/24e549697183196f84fa9a3f82dea31eaa49bd9a))

## [v1.0.6-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.6-smc) - 2023-08-29 13:28:07

1. Add LicenseDialog
2. Add groovy support
3. Modify ECM Script

### Feature

- smc:
    - 新增帮助文档 (
      #672) ([37e462e](https://github.com/unknowIfGuestInDream/javafxTool/commit/37e462e3d72495b5e47b4c1e4c1128bfd9427bed))

- general:
    - Ecm脚本修改 (
      #671) ([4de2914](https://github.com/unknowIfGuestInDream/javafxTool/commit/4de291448956820f984db38ebe0e87a8ca27049b))
    - Add groovy support (
      #668) ([38d81ab](https://github.com/unknowIfGuestInDream/javafxTool/commit/38d81ab4574da6d1435fc92525ec4e1100747471))
    - 新增Order注解 (
      #660) ([87c57df](https://github.com/unknowIfGuestInDream/javafxTool/commit/87c57df1e8a9de82dc43d8b5b61a9d0b6ef8980d))

- core:
    - Add LicenseDialog (
      #666) ([fc14e4b](https://github.com/unknowIfGuestInDream/javafxTool/commit/fc14e4bbf2685d4255657ba2cbf4a3d76b80ac3b))
    - Add EventBus (
      #664) ([8d31b7f](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d31b7fe2cfb5acf802b35584232ffa40ecd3302))
    - Add PdfViewStage (
      #647) ([4566126](https://github.com/unknowIfGuestInDream/javafxTool/commit/4566126f77d0feb7e8ca424207a9ce2b7ff9fd0b))

### Bug Fixes

- core:
    - 修改simpleHttpServer (
      #675) ([11cb60d](https://github.com/unknowIfGuestInDream/javafxTool/commit/11cb60db4b8028dfd3ae994071652d6dd80d5251))

## [v1.0.5-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.5-smc) - 2023-08-06 07:59:33

1. Added control Panel setting interface for frame
2. U2C ECM adds special handling
3. Added open source dependency information display
4. Added color picker and screenshot tool
5. Added documentation links for JavaFX, CSS and FXML

### Feature

- smc:
    - 新增文档链接 (
      #643) ([311ab4e](https://github.com/unknowIfGuestInDream/javafxTool/commit/311ab4e4fb0a51bc46b4ffb270860e2432c4849c))

- core:
    - 新增颜色取号器 (
      #642) ([ab3c267](https://github.com/unknowIfGuestInDream/javafxTool/commit/ab3c267b4f83a9d9b41302344d79e1816d9f4047))

- general:
    - Improve HyperlinkTableCell (
      #639) ([43e12d4](https://github.com/unknowIfGuestInDream/javafxTool/commit/43e12d4394d9f187ee5fa79f79e30196482f9a60))
    - Add Dependency tableView modal (
      #638) ([53abab8](https://github.com/unknowIfGuestInDream/javafxTool/commit/53abab8bf4459b54cf46813bf144c8e9f52d150b))

- frame:
    - Add rightPane control interface (
      #618) ([5ba63c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/5ba63c51640bb973fad6b423b2edcd386065997e))

### Bug Fixes

- smc:
    - U2C数据特殊处理 (
      #629) ([6497faa](https://github.com/unknowIfGuestInDream/javafxTool/commit/6497faa0822e8f159619088e0d705f0409763323))

- core:
    - richtext工具添加 (
      #617) ([e55ae68](https://github.com/unknowIfGuestInDream/javafxTool/commit/e55ae68d31cfbcfe9ac8a1884940f93d1ff182a7))

## [v1.0.4-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.4-smc) - 2023-07-23 01:32:36

1. Fix unrecognized problem with common module
2. JDK replaced by Amazon Corretto with Eclipse Temurin
3. Java and javafx upgrade to 17.0.8
4. SpecGeneral support for xlsm file type

### Feature

- core:
    - richtextfx支持 (
      #604) ([a6291e1](https://github.com/unknowIfGuestInDream/javafxTool/commit/a6291e15e1e0b3395da89aae061ebf69ee6aa3f7))

### Bug Fixes

- common:
    - 修复common打包后spi配置被覆盖问题 (
      #612) ([0a80334](https://github.com/unknowIfGuestInDream/javafxTool/commit/0a803347e513381639d238da87d4cfc33c588add))

- smc:
    - 修复SpecGeneral功能问题 (
      #600) ([80b9d3b](https://github.com/unknowIfGuestInDream/javafxTool/commit/80b9d3b657f1abce88b4d0da8845a6c6930bd171))

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
    - PathWatchTool实现 (
      #581) ([6f444dd](https://github.com/unknowIfGuestInDream/javafxTool/commit/6f444dd8027cdaf370a94a2948baa0a491a28afa))
    - PDFBox支持 (
      #570) ([c17e70c](https://github.com/unknowIfGuestInDream/javafxTool/commit/c17e70c5f3f2d8a916db3df018b646528e876745))
    - DiffHandleUtils优化 (
      #572) ([31160e0](https://github.com/unknowIfGuestInDream/javafxTool/commit/31160e06eb6a9cdf196009e38c16b26a81fdab18))

- qe:
    - 更换qe的logo (
      #562) ([805720a](https://github.com/unknowIfGuestInDream/javafxTool/commit/805720a70cf5c716989762c062c7cc2bfbd3d810))

- general:
    - 线程池uncaughtExceptionHandler属性配置 (
      #554) ([c15c9da](https://github.com/unknowIfGuestInDream/javafxTool/commit/c15c9da43227eb653237ade0477fd733cd11081a))

- smc:
    - girret repo条件增强 (
      #548) ([9190205](https://github.com/unknowIfGuestInDream/javafxTool/commit/9190205135662994b842e3f174361121bedc86a9))
    - girret增强 (
      #547) ([78ae034](https://github.com/unknowIfGuestInDream/javafxTool/commit/78ae0342779ba99985a7bad667ea80291fd515c1))
    - ecm脚本修改 (
      #545) ([80b25c8](https://github.com/unknowIfGuestInDream/javafxTool/commit/80b25c8d9a6f61f329833ef5b8da8caac8551049))

### Bug Fixes

- core:
    - 修复Dom4jUtil的漏洞 (
      #573) ([0fccf1f](https://github.com/unknowIfGuestInDream/javafxTool/commit/0fccf1f8cf1514692c578e23b36ef9168653cb0d))

## [v1.0.2-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.2-smc) - 2023-05-04 08:50:55

1. ECM data file generation function is perfect
2. Add common module
3. UI optimization, new button binding function
4. Added import and export settings function
5. Hide the moneyToChinese component

### Feature

- general:
    - ecm重构 (
      #539) ([dba213b](https://github.com/unknowIfGuestInDream/javafxTool/commit/dba213b2e68c5af72d6e6b9aab3a0172ca3ec5be))
    - qe新增右键菜单 (
      #536) ([e73a6a2](https://github.com/unknowIfGuestInDream/javafxTool/commit/e73a6a2c6c96f07e73d2934751fc2981480aad04))
    - TreeViewCellFactory类重构，TreeView功能新增 (
      #534) ([8f6990c](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f6990c6e416c15b337934af6e14e7d31bc3fe05))
    - 新增导入导出功能 (
      #531) ([82e1964](https://github.com/unknowIfGuestInDream/javafxTool/commit/82e1964fc89921e568472edaf31333fc995d0b63))
    - groovyUtil 修改 (
      #517) ([154b03b](https://github.com/unknowIfGuestInDream/javafxTool/commit/154b03b69ee51b49a03ceb441116c4e343888b80))
    - 完善ecm u2c (
      #499) ([432ddd6](https://github.com/unknowIfGuestInDream/javafxTool/commit/432ddd6ade15aa3b396ec5cbe391f655eaba25f1))
    - 完善common模块 (
      #497) ([4ef5129](https://github.com/unknowIfGuestInDream/javafxTool/commit/4ef51298c9bf864d0cb8266df16878ed2416e8a5))
    - 新增docs文档 (
      #491) ([5d540f8](https://github.com/unknowIfGuestInDream/javafxTool/commit/5d540f816ff092a33361da33c643e88dc4b731b9))
    - ecm脚本修改 (
      #470) ([565e67c](https://github.com/unknowIfGuestInDream/javafxTool/commit/565e67ca94447cf6b2e9cd481091782fd9a16e65))
    - 修改qe样式和icon (
      #464) ([f2985c9](https://github.com/unknowIfGuestInDream/javafxTool/commit/f2985c921c7294aea6cc95a8c92b30988ca9b248))
    - 新增qe模块 (
      #462) ([d83d684](https://github.com/unknowIfGuestInDream/javafxTool/commit/d83d6843425399fff120a4897aae6dc6c50e1d6b))
    - 完善DMATriggerSourceCode 的bind (
      #457) ([63ad897](https://github.com/unknowIfGuestInDream/javafxTool/commit/63ad8979d945548ac34d956f723ff3d4a2db28f4))
    - 新增皮肤 (
      #452) ([611bfe7](https://github.com/unknowIfGuestInDream/javafxTool/commit/611bfe746201b7674405c2a48308d667cb9e9d07))
    - 更改icon (
      #450) ([fa4840d](https://github.com/unknowIfGuestInDream/javafxTool/commit/fa4840def1756010fdafd553fa7a0ba1010aa0ed))
    - 新增getSampleImageIcon接口 (
      #448) ([3aefa34](https://github.com/unknowIfGuestInDream/javafxTool/commit/3aefa3406cce5eeb6ec76e21953b73e9e62aeb2d))
    - 隐藏moneyToChinese组件 (
      #447) ([5f0cc5e](https://github.com/unknowIfGuestInDream/javafxTool/commit/5f0cc5e16e3555772763e6c182046a1f5e8c5be4))
    - ecm脚本修改 (
      #446) ([9ab458d](https://github.com/unknowIfGuestInDream/javafxTool/commit/9ab458d4817b1faf992f0ccdfb248fbd7944f8cd))
    - 新增SamplesTreeViewConfiguration接口 (
      #445) ([732409b](https://github.com/unknowIfGuestInDream/javafxTool/commit/732409b7847ae7dd7eab148f3c3e028bc695daa2))
    - 修改ecm脚本 (
      #441) ([09c9379](https://github.com/unknowIfGuestInDream/javafxTool/commit/09c93795e320af39d991b6d67bae3a2582066aa6))
    - 修改smc菜单栏 (
      #439) ([f70b3a1](https://github.com/unknowIfGuestInDream/javafxTool/commit/f70b3a110b7dffd929dc99f93aa0f9935b2a50f1))

- core:
    - 新增SimpleHttpServer脚本 (
      #509) ([3982157](https://github.com/unknowIfGuestInDream/javafxTool/commit/3982157d8bc389ac352e3297ebe033c3389f9c4b))

- smc:
    - 集成common模块 (
      #494) ([d285755](https://github.com/unknowIfGuestInDream/javafxTool/commit/d285755b5cbc72520b06f0376fa7a941024a6ca6))
    - ecm脚本修改 (
      #487) ([c5d67ae](https://github.com/unknowIfGuestInDream/javafxTool/commit/c5d67ae2a896830904c1b0e66c2d95aa1ff48dde))

- common:
    - 新增common模块 (
      #485) ([3596fb8](https://github.com/unknowIfGuestInDream/javafxTool/commit/3596fb8f178f369327be6fa6f4660f2d28daf8eb))

- qe:
    - 完善HtmlEscape组件 (
      #484) ([e6c8556](https://github.com/unknowIfGuestInDream/javafxTool/commit/e6c85560aaeb6641b665a61ef96f78f40b5e784d))
    - 新增Html转义组件，修改icon (
      #476) ([65bb8d5](https://github.com/unknowIfGuestInDream/javafxTool/commit/65bb8d5d1e8687e3713a77da384d1f24bb502f60))

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
    - 完善ecm国际化 (
      #433) ([5ff04c8](https://github.com/unknowIfGuestInDream/javafxTool/commit/5ff04c872318577f54714a8ed42b19c902835d02))
    - Ecm功能实现 (
      #432) ([776227b](https://github.com/unknowIfGuestInDream/javafxTool/commit/776227b9ea8fe2058e7e46a38037e4a79c4445be))
    - 新增jackson依赖以及工具类 (
      #430) ([54f1d52](https://github.com/unknowIfGuestInDream/javafxTool/commit/54f1d529c751eee6abb9361da5d0e58e9a732342))
    - diff工具增强，新增合并结果功能 (
      #426) ([ddc27eb](https://github.com/unknowIfGuestInDream/javafxTool/commit/ddc27ebeb274d5ccab0b77a9d45805c58ef41bb0))
    - 新增启动检查更新功能 (
      #425) ([e8d7c72](https://github.com/unknowIfGuestInDream/javafxTool/commit/e8d7c7213d3ce789dd35a4dcf896450b427261fe))
    - 新增sample后置处理 (
      #418) ([55a3d04](https://github.com/unknowIfGuestInDream/javafxTool/commit/55a3d04ee20130cd95b0c40c67856fadf955fd01))
    - 新增sample展开规则，初始化ecm组件 (
      #379) ([36cab80](https://github.com/unknowIfGuestInDream/javafxTool/commit/36cab80154f258663e0ecf07a60c75372d7e13be))
    - specGeneral 打开输出位置功能增强 (
      #406) ([6e527a0](https://github.com/unknowIfGuestInDream/javafxTool/commit/6e527a0ebc3d76d4897d3ecf01fdf7367e14b55b))
    - 新增日志打印控制台组件 (
      #378) ([d92ccd2](https://github.com/unknowIfGuestInDream/javafxTool/commit/d92ccd257309c0c4858e923045fed8a69e296f5b))
    - DMATriggerSourceCode修改 (
      #377) ([59c4c9f](https://github.com/unknowIfGuestInDream/javafxTool/commit/59c4c9fe3ef419c6d800b8472488259f48d9e5f4))
    - DmaTriggerSourceCode组件增强 (
      #374) ([fa9d7ac](https://github.com/unknowIfGuestInDream/javafxTool/commit/fa9d7acf9c5fc3599e4683530a32436f53deb791))
    - FxButton增强，优化specGeneral组件 (
      #371) ([13a8180](https://github.com/unknowIfGuestInDream/javafxTool/commit/13a8180744e12cd4ea7c963555b35ac03778c0d1))

### Bug Fixes

- general:
    - 修复ecm模板下载失败问题 (
      #434) ([eaaf6cc](https://github.com/unknowIfGuestInDream/javafxTool/commit/eaaf6cca3b0f2a73b9a91f779300d5d9de60901d))
    - 修复macos下打开配置文件错误问题 (
      #429) ([fe6c28b](https://github.com/unknowIfGuestInDream/javafxTool/commit/fe6c28bc078232cb6cbf1d1cb575f5443054aa81))
    - 修复可执行文件获取清单数据失败问题 (
      #414) ([a5d29d0](https://github.com/unknowIfGuestInDream/javafxTool/commit/a5d29d0a4f45965af322b264efd7a4922582eb9d))
    - 修复DtsTriggerSourceDoc 初始值设定的判断问题 (
      #412) ([df5c50c](https://github.com/unknowIfGuestInDream/javafxTool/commit/df5c50c3d1990fc93756a005f3320f674086edec))
    - 解决打包为执行文件后项目配置获取不到的问题 (
      #408) ([856c1cb](https://github.com/unknowIfGuestInDream/javafxTool/commit/856c1cbb970cfdcbe0fea033b414a10e87e9eadf))
    - DmaTriggerSourceCode 头文件模板新增注释 (
      #404) ([8e6942d](https://github.com/unknowIfGuestInDream/javafxTool/commit/8e6942d211ba67d1538181a39d61c46068151cca))
    - 修复specGeneral组件对多行注释的处理 (
      #402) ([e3ad748](https://github.com/unknowIfGuestInDream/javafxTool/commit/e3ad7480fffe34efd787233c91b0a49c883243ed))
    - dmaTriggerSourceCode 修改使能类输入 (
      #391) ([81addc1](https://github.com/unknowIfGuestInDream/javafxTool/commit/81addc186a3fb94848afa5db1de482ca6f5ef629))
    - dmaTriggerSourceCode模板完善 (
      #380) ([985a906](https://github.com/unknowIfGuestInDream/javafxTool/commit/985a90607be596c974f234a8a0b1bb0c05a96833))
    - 修复smc中文件选择错误 (
      #368) ([581b3e4](https://github.com/unknowIfGuestInDream/javafxTool/commit/581b3e4bee12e40435165b33d40a259e54567210))

## [v1.0.0-smc](https://github.com/unknowIfGuestInDream/javafxTool/releases/tag/v1.0.0-smc) - 2023-03-19 13:19:45

1. SmcTool release
2. Contains UD, CD, UT and other tools of smc

### Feature

- general:
    - DmaTriggerSourceCode setting完善 (
      #353) ([a731842](https://github.com/unknowIfGuestInDream/javafxTool/commit/a731842a1c7224a1f92505ec5cd231d0fc72e24c))
    - update .gitattributes (
      #345) ([ddf4b0c](https://github.com/unknowIfGuestInDream/javafxTool/commit/ddf4b0c5b52698f3b1347f506456d2e089f4832c))
    - Add .gitattributes (
      #344) ([aeef4c6](https://github.com/unknowIfGuestInDream/javafxTool/commit/aeef4c6a76895c0f7a6945f0015af04d117ebe2f))
    - DmaTriggerSourceCode setting完善 (
      #342) ([88f16bd](https://github.com/unknowIfGuestInDream/javafxTool/commit/88f16bde3f5dc0540d784582b56831c81fa27a22))
    - DmaTriggerSourceCode 模板完善 (
      #341) ([7bf91ee](https://github.com/unknowIfGuestInDream/javafxTool/commit/7bf91ee4589406627903d47b1c7349fdb725bd88))
    - DmaTriggerSourceCode 模板完善 (
      #340) ([7965e0a](https://github.com/unknowIfGuestInDream/javafxTool/commit/7965e0ab2e13d1e6349b0cf86e033b2e5d3b46b3))
    - DmaTriggerSourceCode 组件完成 (
      #337) ([457c280](https://github.com/unknowIfGuestInDream/javafxTool/commit/457c28065ed7bfa53561521c2bc5e3e12f7f6541))
    - DmaTriggerSourceCode 下载等功能完善 (
      #335) ([883f155](https://github.com/unknowIfGuestInDream/javafxTool/commit/883f155258f72848c702ee232a7e1228514436d6))
    - DmaTriggerSourceCode 组件信息完善 (
      #334) ([087ab34](https://github.com/unknowIfGuestInDream/javafxTool/commit/087ab34c74573a5accea368c49f5052ba9588368))
    - dmaTeiggerSourceCode UI完善 (
      #333) ([8342b61](https://github.com/unknowIfGuestInDream/javafxTool/commit/8342b612115ba2c01bca3eefa5564e85d05a0862))
    - DmaTriggerSourceCode 测试类完成 (
      #332) ([6abf16d](https://github.com/unknowIfGuestInDream/javafxTool/commit/6abf16db1d50cf58ad6b9af749a1314e63feb191))
    - 组件描述以及README.md完善 (
      #331) ([b3d6b58](https://github.com/unknowIfGuestInDream/javafxTool/commit/b3d6b58a2fcce3cdac59060e970467cdba941884))
    - DmaTriggerSourceCode 组件新增 (
      #330) ([56388ea](https://github.com/unknowIfGuestInDream/javafxTool/commit/56388ea4728c6eaea3f0c75180ea0e5f41f685d6))
    - 新增列名计算器 (
      #324) ([cddfa10](https://github.com/unknowIfGuestInDream/javafxTool/commit/cddfa10b4a0381c754be6a9b5fdb8b6a0e97af9a))
    - 新增ProgressStage 加载组件 (
      #316) ([dfc8831](https://github.com/unknowIfGuestInDream/javafxTool/commit/dfc88318964e696d323166da759e8cdbafdba284))
    - 新增闪屏图片功能，优化启动 (
      #313) ([0092c57](https://github.com/unknowIfGuestInDream/javafxTool/commit/0092c57d40f5af124a647dd8c700b1011e64f106))
    - HconvertExcel完成 (
      #307) ([7f4cf39](https://github.com/unknowIfGuestInDream/javafxTool/commit/7f4cf396fee7e7172456e830dfbf58d4abdc5c95))
    - 更换smc logo (
      #305) ([b861826](https://github.com/unknowIfGuestInDream/javafxTool/commit/b8618268c83bf45b0edbac011aaedbb027a8be73))
    - HconvertExcel 初版 (
      #301) ([63338ac](https://github.com/unknowIfGuestInDream/javafxTool/commit/63338ac30c686d3adbf7cf6902f2a48c9b455c28))
    - HconvertExcel 测试类完成 (
      #299) ([8f4e856](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f4e856bc2c30d7d24f68037a5e19cccf3c7be17))
    - DtsTriggerSourceDoc 描述完善 (
      #296) ([c2cb888](https://github.com/unknowIfGuestInDream/javafxTool/commit/c2cb888a6c26989b12fc0e323819830763d796c1))
    - DtsTriggerSourceDoc 完成 (
      #295) ([24e47a6](https://github.com/unknowIfGuestInDream/javafxTool/commit/24e47a62250dafbb7ef603c15859cdadbd343a86))
    - DtsTriggerSourceDoc 功能完善 (
      #293) ([d57d709](https://github.com/unknowIfGuestInDream/javafxTool/commit/d57d709922d1cc2c3f094027312f824152ad4fe6))
    - dts document 生成初步完成 (
      #291) ([92d8b49](https://github.com/unknowIfGuestInDream/javafxTool/commit/92d8b49a0e7547f0cca30cb589351f068fa23473))
    - DtsTriggerSourceDoc 开发 (
      #290) ([0750ac1](https://github.com/unknowIfGuestInDream/javafxTool/commit/0750ac1470149765fb9a4111388edc2ebdb1c8d7))
    - dtc trigger source doc 功能调试 (
      #286) ([b116892](https://github.com/unknowIfGuestInDream/javafxTool/commit/b116892773893dcbfeb10592638170b1c1936f8c))
    - 修复girret启动问题 (
      #282) ([8c13910](https://github.com/unknowIfGuestInDream/javafxTool/commit/8c13910083eabbfdfe8069db60630481d481b8c7))
    - 新增bindUserData增强方法，smc girretReview优化token信息初始化 (
      #281) ([7fd7319](https://github.com/unknowIfGuestInDream/javafxTool/commit/7fd731953cee7b02ab531b59a7854c4cc0eb2180))
    - logo以及名称抽象化，smc新增测试组件 (
      #280) ([552f4b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/552f4b68170cc691d07ee96f18096268408b20d1))
    - 优化getWelcomeBackgroundImagePane接口 (
      #279) ([85be2ad](https://github.com/unknowIfGuestInDream/javafxTool/commit/85be2ad0e6f35bfa0209e020887129c133a16eb5))
    - logback分包配置修改 (
      #278) ([8e0897a](https://github.com/unknowIfGuestInDream/javafxTool/commit/8e0897a89e8832485640977dde441f1d4693abf4))
    - 新增smc模块 welcome背景，以及修复在eclipse环境下logback路径问题 (
      #275) ([81e1118](https://github.com/unknowIfGuestInDream/javafxTool/commit/81e1118f2c0db1a1532e3816c4b9761eab48e255))
    - logback模块分包配置，完善smc国际化描述 (
      #271) ([44cff62](https://github.com/unknowIfGuestInDream/javafxTool/commit/44cff62444456f4450aff6b46b6f9c9549f77c53))
    - 新增tlcsdm-asyncTool依赖 (
      #267) ([99bb30e](https://github.com/unknowIfGuestInDream/javafxTool/commit/99bb30ecf348dd62c52effec88cf3b4ed32bddd0))
    - 新增规则引擎aviator以及logback配置去除configruation2启动异常日志 (
      #265) ([53980e9](https://github.com/unknowIfGuestInDream/javafxTool/commit/53980e9edaef865a43bc3b6f8cd1ca8b190bfb84))
    - 新增copyright信息 (
      #261) ([db156a5](https://github.com/unknowIfGuestInDream/javafxTool/commit/db156a55a041bf48bb8e0ba3c14d372f1f4e432a))
    - freemarker集成以及项目结构调整 (
      #259) ([8e627b2](https://github.com/unknowIfGuestInDream/javafxTool/commit/8e627b27be38fb1173c60564fb1e5f108fac7c0d))
    - smc PasswordField 数据切换为aes加密 (
      #257) ([0d2cece](https://github.com/unknowIfGuestInDream/javafxTool/commit/0d2cecea792c784a1b8d788a6f80ab4f4cf6577e))
    - 线程池设置为懒汉单例 (
      #255) ([101ab1c](https://github.com/unknowIfGuestInDream/javafxTool/commit/101ab1ce8e0d3337f24a3f72e6c443431381c02b))
    - 新增InitializingFactory初始化接口以及线程池初始化实现 (
      #254) ([2b2b9fa](https://github.com/unknowIfGuestInDream/javafxTool/commit/2b2b9fa71473a31cab41e5449b496b7e1529c798))
    - DtsTriggerSourceXml 完成 (
      #248) ([48724fe](https://github.com/unknowIfGuestInDream/javafxTool/commit/48724fe5df694d28164d06807fb4e370358eb2ae))
    - DtsTriggerSourceDoc组件初始化 (
      #243) ([4057278](https://github.com/unknowIfGuestInDream/javafxTool/commit/40572785f5a2792aad2d18136ebad9ee5c6e30ad))
    - dtsTriggerSourceXml 初始化 (
      #242) ([0a4866e](https://github.com/unknowIfGuestInDream/javafxTool/commit/0a4866e8b00c6d2cf1c4f5654b2e889be43afd1a))
    - 新增DtsTriggerSourceXml组件 (
      #237) ([9f97719](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f97719f2dd66ee1a91d94386c14140dceb390ca))
    - SpecGeneralTest完成，优化SampleScanner,新增mistSliverSkin (
      #236) ([0c3e3f2](https://github.com/unknowIfGuestInDream/javafxTool/commit/0c3e3f253a54c75025ba3bb83d85f4fb4ad86710))
    - 新增freemarker 以及增加相关测试，新增log4j-slf4j依赖 (
      #231) ([47a5407](https://github.com/unknowIfGuestInDream/javafxTool/commit/47a54076404fc46fa3e2b058422e464420ed049b))
    - 修改smc下包名test -> unitTest (
      #230) ([6ef5e73](https://github.com/unknowIfGuestInDream/javafxTool/commit/6ef5e73b58a1e698b91263ea5535017679ff0fdb))
    - 新增HcontentExcel工具包，代码规范和结构优化 (
      #228) ([5ca1028](https://github.com/unknowIfGuestInDream/javafxTool/commit/5ca10280090e1acfccd33964458ebc578b5a2a08))
    - SpecGeneralTest功能实现完成 (
      #227) ([622e115](https://github.com/unknowIfGuestInDream/javafxTool/commit/622e1150501fe17aaa30da2e0f6390a6f0ec6c43))
    - core helper包增强 (
      #224) ([47e76c3](https://github.com/unknowIfGuestInDream/javafxTool/commit/47e76c3bd5692b06cd97aefd7df0636d3c39e444))
    - 新增FxAction和FxActionGroup (
      #223) ([fd25094](https://github.com/unknowIfGuestInDream/javafxTool/commit/fd250947213b6b9a5ed567a58143ad7ce84bb133))
    - 优化about action中图片获取方式 (
      #222) ([b8f3d14](https://github.com/unknowIfGuestInDream/javafxTool/commit/b8f3d147e7f995b67bae1e7f0f5fabb49aa31da1))
    - core新增FxButton和FxAction封装工具包，SpecGeneral组件创建 (
      #220) ([c513c8c](https://github.com/unknowIfGuestInDream/javafxTool/commit/c513c8c1203b042ce604a919958c2227ffcfb220))
    - 新增CoreUtil (
      #218) ([783033d](https://github.com/unknowIfGuestInDream/javafxTool/commit/783033da488e075180c167ccfd033f3dcecb49aa))
    - SpecGeneralTest初步实现以及filediff优化 (
      #217) ([225b547](https://github.com/unknowIfGuestInDream/javafxTool/commit/225b547fa50e4070095ba066fb3923b413ca3048))
    - SampleBase userData对PasswordField的值加密 (
      #215) ([c3803fe](https://github.com/unknowIfGuestInDream/javafxTool/commit/c3803fee2479dbf5609ec20ecbc4379e96e8f0d7))
    - 新增DTS U2C 文档生成工具 (
      #214) ([19205e9](https://github.com/unknowIfGuestInDream/javafxTool/commit/19205e9f40e386e85f67b7cf7b14c69280212a11))
    - 新增加密解密工具，新增测试文件资源 (
      #213) ([011964f](https://github.com/unknowIfGuestInDream/javafxTool/commit/011964f3a402df53e838960939b50007ee460832))
    - data文件中新增注释 (
      #212) ([d035ade](https://github.com/unknowIfGuestInDream/javafxTool/commit/d035adee6c35e637bdb96f8eb454cecb11d3158b))
    - PinAssignment pin处理工具完成 (
      #207) ([bf96146](https://github.com/unknowIfGuestInDream/javafxTool/commit/bf96146bc6980efa67473d0f79e5751b94dddfe2))
    - pin 内容处理 (
      #204) ([3899d4d](https://github.com/unknowIfGuestInDream/javafxTool/commit/3899d4d74b80d13c175f4f38e928999b3f1a80a2))
    - MoneyToChinese 模块完成，项目资源配置重构 (
      #202) ([7e177f5](https://github.com/unknowIfGuestInDream/javafxTool/commit/7e177f54d54a00d4102636c5259a08a164de50a0))
    - 新增金额转换大写类 (
      #200) ([17e5cef](https://github.com/unknowIfGuestInDream/javafxTool/commit/17e5ceff78dc9856b0f75140a6816baaeae76c29))
    - smc girretReview userData支持，新增查看用户数据菜单 (
      #197) ([a0ebf67](https://github.com/unknowIfGuestInDream/javafxTool/commit/a0ebf67095eb79d0ac4fe6cf4207b4cc47410d38))
    - codeStyle120 支持保留用户数据，后续需要调试 (
      #195) ([7effaea](https://github.com/unknowIfGuestInDream/javafxTool/commit/7effaeaf026be9b157da177ffc75c0a1cb855300))
    - 增强userData xml数据结构，新增保存顺序 (
      #194) ([a9879e3](https://github.com/unknowIfGuestInDream/javafxTool/commit/a9879e3dc94ad6c5d306efb00a97dca3c082a07b))
    - smc fileDiff 集成 userData功能 (
      #193) ([87cf5c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/87cf5c5129d4e51fa3b38697d717b17fc639616f))
    - core新增exception类 (
      #192) ([e0a3a25](https://github.com/unknowIfGuestInDream/javafxTool/commit/e0a3a25cad42ef84c7de6aa60e6ebdabfbe8c78a))
    - 新增money2ChineseUtil工具类 (
      #191) ([d0cdbb6](https://github.com/unknowIfGuestInDream/javafxTool/commit/d0cdbb6d93263c03aa17fc016f36018c858cce58))
    - 用户数据方案初步设计以及计划新增数字转换中文文字的功能 (
      #189) ([e5c285f](https://github.com/unknowIfGuestInDream/javafxTool/commit/e5c285f9775b650df9ab64e7e76ddc6e4751ff47))
    - FxXmlUtil工具类完善 (
      #184) ([330a71c](https://github.com/unknowIfGuestInDream/javafxTool/commit/330a71c8282725a84130d1dd0bfc5a7eda356feb))
    - xml配置文件工具 (
      #182) ([73e8ac7](https://github.com/unknowIfGuestInDream/javafxTool/commit/73e8ac7492ad92d88dba8e563168f2a6d211d582))
    - XMLPropertiesConfiguration 测试以及配置 (
      #181) ([da6b78a](https://github.com/unknowIfGuestInDream/javafxTool/commit/da6b78ab0021025988af1395726a17f5a44ab9ef))
    - 系统设置功能完成 (
      #178) ([8a27b17](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a27b17218407ca871aa7ad73b67197e8c4c8290))
    - smc welcomePage国际化 (
      #180) ([06e33b0](https://github.com/unknowIfGuestInDream/javafxTool/commit/06e33b01582153108add883ecb6a36dd8f8f86a1))
    - smc 菜单功能完善 (
      #177) ([344dd30](https://github.com/unknowIfGuestInDream/javafxTool/commit/344dd305ff1303059b6774e46facc2790969bd08))
    - smc welcomePage 优化，菜单新增查看系统配置 (
      #167) ([8fdfcda](https://github.com/unknowIfGuestInDream/javafxTool/commit/8fdfcda7c273308e84b2785b27fa31a338506706))
    - girret review组件完成 (
      #164) ([4706a86](https://github.com/unknowIfGuestInDream/javafxTool/commit/4706a86c0873ba44fed06e1f8672b183ffaefaa1))
    - 新增getSampleId接口 (
      #159) ([def0ae6](https://github.com/unknowIfGuestInDream/javafxTool/commit/def0ae6f580250884a86d8041b47a078c0523df8))
    - core, smc功能完善 (
      #158) ([b0fe63b](https://github.com/unknowIfGuestInDream/javafxTool/commit/b0fe63b903025a85a8e31067412d8d87e4514a5f))
    - smc 国际化功能实现 (
      #157) ([9edb4fc](https://github.com/unknowIfGuestInDream/javafxTool/commit/9edb4fcd32b9dee88a900da3986bfbbb8625b88d))
    - 系统功能完善 (
      #155) ([8a2c28f](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a2c28fca0eb54813c16a8449bd2f87373063011))
    - 增加logback日志功能 (
      #154) ([6ed9012](https://github.com/unknowIfGuestInDream/javafxTool/commit/6ed9012c7eb08931b5c31bef2dad8477590057c5))
    - code style 组件完善 (
      #153) ([cb676f3](https://github.com/unknowIfGuestInDream/javafxTool/commit/cb676f335b3a60a31c691ae463efc7db727a9462))
    - code style组件完善 (
      #150) ([5559d65](https://github.com/unknowIfGuestInDream/javafxTool/commit/5559d6548c8a1659f0e9a1cda0f56a41baa0e66f))
    - code style组件完善 (
      #149) ([10c6956](https://github.com/unknowIfGuestInDream/javafxTool/commit/10c69566f3cae8e2d8de1689d1b0cc743c4c1649))
    - 修改工作流组件 (
      #132) ([ca89723](https://github.com/unknowIfGuestInDream/javafxTool/commit/ca89723108c9b134b98c59ac82e961c2a709e3cb))
    - 新增组件版本号 (
      #130) ([e168531](https://github.com/unknowIfGuestInDream/javafxTool/commit/e1685313a6086fd4194ce3b2625d8d4f690f1c45))
    - core功能完善 (
      #127) ([78f636f](https://github.com/unknowIfGuestInDream/javafxTool/commit/78f636f7ecb6ce43a8c34fa6b60ed33b8dd685a7))
    - 新增core模块 (
      #124) ([1db2060](https://github.com/unknowIfGuestInDream/javafxTool/commit/1db2060009e3e17f4851ea706d1eca482fd15ff3))
    - 完善smc FileDiff工具 (
      #123) ([d401506](https://github.com/unknowIfGuestInDream/javafxTool/commit/d401506b6d4ac717fa339ba661ccfd0e67bd88f8))
    - 文件比对初步实现 (
      #122) ([8a0476b](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a0476bb448336ffdb983c554ca2a5d0ec0b26d0))
    - frame新增关闭和确认关闭功能 (
      #121) ([b945df7](https://github.com/unknowIfGuestInDream/javafxTool/commit/b945df7fa21ddac54a290b9568c17b3d1309b85f))
    - 新增文件差异比对功能 (
      #119) ([cde0d4b](https://github.com/unknowIfGuestInDream/javafxTool/commit/cde0d4be904f3ce8ac465a9b3e0766db11bc7a5b))
    - 完善smc (
      #115) ([694d44c](https://github.com/unknowIfGuestInDream/javafxTool/commit/694d44caaee33a9fd2c1336df47803e79e9fc0fd))
    - 完善welcomePage (
      #114) ([22eb185](https://github.com/unknowIfGuestInDream/javafxTool/commit/22eb1858935b19d03e8fb6f7a4778083066d9a05))
    -
    完善工作流 ([c32f5a1](https://github.com/unknowIfGuestInDream/javafxTool/commit/c32f5a1abac8a215190fc980d498dbafca641d7d))
    -
    完善工作流 ([0dfcea5](https://github.com/unknowIfGuestInDream/javafxTool/commit/0dfcea5f6ff9c7b855c110571e0226691f47b535))
    -
    完善工作流 ([06b5d00](https://github.com/unknowIfGuestInDream/javafxTool/commit/06b5d00f1c5e02689c9d8b07f1567519c58631e7))
    -
    完善工作流 ([60b02d0](https://github.com/unknowIfGuestInDream/javafxTool/commit/60b02d03a1a862ee09a80307753a2cd4a00ce328))
    -
    完善工作流 ([e51e547](https://github.com/unknowIfGuestInDream/javafxTool/commit/e51e547af93c806afa4a6c2b082fd7ab74a6854e))
    -
    完善工作流 ([ebb498e](https://github.com/unknowIfGuestInDream/javafxTool/commit/ebb498e8cffa4a5165f006f68394280350f696eb))
    -
    完善工作流 ([ad87660](https://github.com/unknowIfGuestInDream/javafxTool/commit/ad876608210f9bd4a18bf62e0d16b54d210f51b1))
    -
    完善工作流 ([c218a65](https://github.com/unknowIfGuestInDream/javafxTool/commit/c218a65f3307a2048de360426bdedbb1bf90ab04))
    -
    完善工作流 ([fe08635](https://github.com/unknowIfGuestInDream/javafxTool/commit/fe08635b7906655a7f431c796a186109183ad45f))
    - 完善工作流 (
      #93) ([bcf1d1d](https://github.com/unknowIfGuestInDream/javafxTool/commit/bcf1d1d0a87409e87bceb67633444a7e75ce5bc8))
    - 完善工作流 (
      #91) ([ea909aa](https://github.com/unknowIfGuestInDream/javafxTool/commit/ea909aa4721e349d0c6397d6326255d1518f8b1d))
    -
    完善工作流 ([1c04c5e](https://github.com/unknowIfGuestInDream/javafxTool/commit/1c04c5e7b08151e5c15d7b90ed7216c572169fbf))
    -
    完善工作流 ([16cc575](https://github.com/unknowIfGuestInDream/javafxTool/commit/16cc57517f78cb913a182bd8daa987d5171cc37d))
    -
    完善工作流 ([7405f03](https://github.com/unknowIfGuestInDream/javafxTool/commit/7405f0387e58c7ad4972968d3412c24e7d8e1394))
    -
    完善工作流 ([5619bad](https://github.com/unknowIfGuestInDream/javafxTool/commit/5619bad419a6dffff7c1eb3534879011786d4719))
    -
    完善工作流 ([d2e008d](https://github.com/unknowIfGuestInDream/javafxTool/commit/d2e008d7bf9470a8366761d24ac99bac149c93b2))
    -
    完善工作流 ([21c6a24](https://github.com/unknowIfGuestInDream/javafxTool/commit/21c6a2464bafe10e7335508dce0fb8443f00c040))
    - 完善工作流 (
      #74) ([6c194f4](https://github.com/unknowIfGuestInDream/javafxTool/commit/6c194f4363cb4c93e96fb8a258d5910556827c4c))
    - smc menubar增加图标 (
      #71) ([7880b44](https://github.com/unknowIfGuestInDream/javafxTool/commit/7880b44976feab75916a8142cfd440e76039f1bd))
    - 新增语言功能 (
      #70) ([f3e874b](https://github.com/unknowIfGuestInDream/javafxTool/commit/f3e874b5c7a07fc4af1e9bf8e3dd9c7f4378dbc3))
    - smc menubar 完善 (
      #66) ([9c5ca3f](https://github.com/unknowIfGuestInDream/javafxTool/commit/9c5ca3f994ecaaf6c79ecc19583f8be8a18942b6))
    - about功能初始实现 (
      #64) ([be027d8](https://github.com/unknowIfGuestInDream/javafxTool/commit/be027d8f32f2c54d11db83ecd8a55ab2792872b5))
    - configration接口实现 (
      #63) ([19abb23](https://github.com/unknowIfGuestInDream/javafxTool/commit/19abb23a11d66ef271957e80a155b41a9b33bd67))
    - smc模块 主程序菜单栏接口实现 (
      #61) ([abef11a](https://github.com/unknowIfGuestInDream/javafxTool/commit/abef11ae49f3a5593f3140cfa114c779ecec37a6))
    - 新增CodeStyleLength120 功能 (
      #60) ([10adab1](https://github.com/unknowIfGuestInDream/javafxTool/commit/10adab142f1d3fc9056921c7a4f179ea62d0e2bc))
    - 菜单排序接口优化 (
      #59) ([74f6d3e](https://github.com/unknowIfGuestInDream/javafxTool/commit/74f6d3ed7e023aadcb533995b4e4a59ab2a40884))
    - 新增 prefixSelection 组件demo (
      #56) ([25de062](https://github.com/unknowIfGuestInDream/javafxTool/commit/25de062a355ba6c311722fa888331a3a66d9c150))
    - 新增 MaskerPane 遮盖层 (
      #55) ([8f076e1](https://github.com/unknowIfGuestInDream/javafxTool/commit/8f076e1a4a2023431f29f296ff38a6087d893614))
    - smc centerPanel 设计 (
      #51) ([8d32cf1](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d32cf1f9d18a0ead19012af7d35ac0abc5f395c))
    - 修改module.info文件 (
      #50) ([f144149](https://github.com/unknowIfGuestInDream/javafxTool/commit/f1441499c488c742b01cb04f68931d0b032eac6b))
    - smc初始化 (
      #47) ([fb033e4](https://github.com/unknowIfGuestInDream/javafxTool/commit/fb033e47fddadc8c53140a7a3294a95c789e144c))
    - Sample 设计调整 (
      #46) ([06d830e](https://github.com/unknowIfGuestInDream/javafxTool/commit/06d830ee19c0825c0c70b49fbf63b95b208afafe))
    -
    完善工作流配置文件 ([6458c4a](https://github.com/unknowIfGuestInDream/javafxTool/commit/6458c4aebb026b7023a1821a0831d4e5083b7e66))
    - 完善工作流 (
      #41) ([067e476](https://github.com/unknowIfGuestInDream/javafxTool/commit/067e476a9d64761e91e59a6af6c37319b9e0f36f))
    - 完善demo (
      #39) ([a2f8f1f](https://github.com/unknowIfGuestInDream/javafxTool/commit/a2f8f1fd11dcc0c97c1a81a4a10500efe75eb1a3))
    - 添加工作流 (
      #38) ([5d89d78](https://github.com/unknowIfGuestInDream/javafxTool/commit/5d89d78112e380d379207939ba8f7e35b0eb01c1))
    - 初始化smc模块 (
      #33) ([e1b37c5](https://github.com/unknowIfGuestInDream/javafxTool/commit/e1b37c5f8377c71ca4e72c16ee3e0fc25e45d084))
    -
    codecov工作流 ([8d0bb50](https://github.com/unknowIfGuestInDream/javafxTool/commit/8d0bb508c1788b8664d86ce6c315989260e59bc7))
    -
    codecov工作流 ([e43421b](https://github.com/unknowIfGuestInDream/javafxTool/commit/e43421bf4c2f87a35268cfbba02a81022e576caf))
    - smc引用模块 (
      #30) ([564da58](https://github.com/unknowIfGuestInDream/javafxTool/commit/564da588186f1d531d2d08e6832c4f6111605804))
    - 完善README (
      #29) ([64dfa81](https://github.com/unknowIfGuestInDream/javafxTool/commit/64dfa812f066c146336afb5be50d610f6fcb8f58))
    -
    完善README ([7cac5ea](https://github.com/unknowIfGuestInDream/javafxTool/commit/7cac5ea9cf6fef0850175858b2a233b2db60b86e))
    -
    完善label工作流 ([7ac59ce](https://github.com/unknowIfGuestInDream/javafxTool/commit/7ac59ceb6dc46fd4765d44ef6e99a7e3936e05ae))
    -
    完善label工作流 ([0c4b135](https://github.com/unknowIfGuestInDream/javafxTool/commit/0c4b135e59e2508d95c7a418fc713dc949144610))
    - 完善label工作流 (
      #27) ([7b8a218](https://github.com/unknowIfGuestInDream/javafxTool/commit/7b8a218ec88355ef07f3ff2da8ddbc16d080a6b5))
    - improve centerPanel SPI (
      #24) ([e6da1c2](https://github.com/unknowIfGuestInDream/javafxTool/commit/e6da1c282cb6676eced7e13f4b7ae52b4434f0e6))
    - 新增国际化支持 (
      #23) ([f106f6a](https://github.com/unknowIfGuestInDream/javafxTool/commit/f106f6a79dbe4f5443dbed4bf9303d7d3d6659ab))
    -
    improve登录模块 ([c971019](https://github.com/unknowIfGuestInDream/javafxTool/commit/c9710198393109796ff0a5a55b327daaee11ae55))
    - improve login module, support #15 (
      #22) ([9f309b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/9f309b67352ee6e2e345262509ae6210fd4d8ce0))
    - 修改login模块 (
      #21) ([c6c5b32](https://github.com/unknowIfGuestInDream/javafxTool/commit/c6c5b322918159e1295cb63e221027c6c34e571d))
    - 初始化登录框架 (
      #19) ([6aa5823](https://github.com/unknowIfGuestInDream/javafxTool/commit/6aa58235fdbc180605a54fcf0941b1fd563bd150))
    - 修复css引用错误 (
      #14) ([387311b](https://github.com/unknowIfGuestInDream/javafxTool/commit/387311b8e90f89b1e392d7ba2e41dce1a365192b))
    -
    更新test依赖版本 ([c80a6b6](https://github.com/unknowIfGuestInDream/javafxTool/commit/c80a6b6ad8f5edcf8af8bd8c43c0a4c8b559146c))
    - support menubar (
      #12) ([d465f76](https://github.com/unknowIfGuestInDream/javafxTool/commit/d465f76907373b55f6dc70149f27392686043f0c))
    - 修改菜单栏 (
      #10) ([614494e](https://github.com/unknowIfGuestInDream/javafxTool/commit/614494ed7cce3989ca314ab27c016d08d272ccdd))
    -
    menubar ([a52f267](https://github.com/unknowIfGuestInDream/javafxTool/commit/a52f267154f2b45dcbc4fbbdf69da73bf3d13fb4))
    - modify
      config. ([d16ccce](https://github.com/unknowIfGuestInDream/javafxTool/commit/d16ccce5f8ec9250efd265b79979479e44a608a8))

- smc:
    - girret review results功能完善 (
      #161) ([6882ca8](https://github.com/unknowIfGuestInDream/javafxTool/commit/6882ca863cda4eb2198dc6a54f0e8bab7b405bd7))

### Bug Fixes

- general:
    - 修复smc general #ifndef相关判断 (
      #320) ([cc48b0a](https://github.com/unknowIfGuestInDream/javafxTool/commit/cc48b0a6983d8320cf9a5d23fac3ab4ef314f62f))
    - 修复日语资源加载错误的问题 (
      #317) ([49556a8](https://github.com/unknowIfGuestInDream/javafxTool/commit/49556a8c3d29a934b3af54cfb3601e543c569e92))
    - 修复smc StdtriggerSourceDoc 生成错误 (
      #312) ([a8ec52f](https://github.com/unknowIfGuestInDream/javafxTool/commit/a8ec52f821060b668839433213e498dfb01bac64))
    - 功能修复 (
      #308) ([003bd4a](https://github.com/unknowIfGuestInDream/javafxTool/commit/003bd4a30a842160f6b6381bc4b44beafbd15f34))
    - 修复打包问题，以及打包后template加载器错误 (
      #288) ([a21675f](https://github.com/unknowIfGuestInDream/javafxTool/commit/a21675f39a1a3c0d30101f62f62372cef22d77c3))
    - 解决configuration2 加载list数据toString的问题 (
      #196) ([239dd0f](https://github.com/unknowIfGuestInDream/javafxTool/commit/239dd0f6ddc35066543d1643114606bb2dab6e40))
    - ExceptionDialog bug修复 (
      #175) ([bdbd108](https://github.com/unknowIfGuestInDream/javafxTool/commit/bdbd1082468440d4ddf32b5b8689c04ed1a0d3c3))
    - girret review bug修复 (
      #166) ([489725c](https://github.com/unknowIfGuestInDream/javafxTool/commit/489725c9854e6265751be735b16b75dab38eea81))
    - 修复路径错误 (
      #112) ([8a1fe03](https://github.com/unknowIfGuestInDream/javafxTool/commit/8a1fe03fe4fc7f4238abdecbdeacd0a361c12202))
    - 修复css错误， 完善菜单栏国际化 (
      #68) ([1910569](https://github.com/unknowIfGuestInDream/javafxTool/commit/191056971caa04afd87d6bb84a9684d8c216445d))
    - 优化CenterPanelService 接口设计 (
      #53) ([f729b60](https://github.com/unknowIfGuestInDream/javafxTool/commit/f729b60ba323ec13a15d2ef2785b07e789f201c0))
    - 优化模块配置 (
      #49) ([317fb45](https://github.com/unknowIfGuestInDream/javafxTool/commit/317fb456f898139786041c32ec19357c78e77725))
    - 修复javafx:Jlink打包镜像错误 (
      #42) ([1e8f55f](https://github.com/unknowIfGuestInDream/javafxTool/commit/1e8f55f0eae1553b13288100ddb9b47ca6b8b401))
    -
    修改工作流配置 ([c6ea3b9](https://github.com/unknowIfGuestInDream/javafxTool/commit/c6ea3b9dfffd3fb828cb7d861bed4a4417944b68))
    -
    修改工作流配置 ([0992f7d](https://github.com/unknowIfGuestInDream/javafxTool/commit/0992f7d7620396749cc2779ea94a0f7bfe6d78e8))
    -
    修复编译错误 ([171bf33](https://github.com/unknowIfGuestInDream/javafxTool/commit/171bf3386ff6c706f89b42018c8fdbbe95ee84eb))
    - 修复静态文件路径 (
      #6) ([311abf3](https://github.com/unknowIfGuestInDream/javafxTool/commit/311abf33a80aca54367f3a60358719becbaf96c4))

