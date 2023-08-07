## Introduce
[中文文档](./README_zh.md)  [日本語文書](./README_jp.md)\
Javafx scaffolding, built on JDK17 + JavaFX17 + controlsfx 11.x.x + Maven

Frame and login are basic modules, which are pluggable through java SPI to facilitate the integration of application
modules. demo is the provided example module.

## Component

- docs: Documentation (built with docsify)
- frame: Application main UI framework (provide SPI interface)
- core: Application core components, including some common functions
- login: Login module (provides SPI interface)
- demo: demo example (based on controlsfx demo transformation)
- common: Common component module, used by application modules
- smc, qe: Personal application (no reference required)

## Interface

### core

- TemplateLoaderService: Freemarker TemplateLoader loads, subclass implementation to add the template path of the submodule

### frame

- FXSamplerProject: Project information, including project name, module, package name, welcome page, etc.
- MenubarConfigration: Menu bar configuration
- FXSamplerConfiguration: Project style, title and icon configuration
- CenterPanelService: Node configuration in the central area, including the interface when clicking and switching components
- SplashScreen: Splash screen image
- SamplePostProcessorService: Sample post-processing
- VersionCheckerService: Version update check
- SamplesTreeViewConfiguration: Menu tree configuration

### login

- LoginCheck: Login verification

## Pack

It is recommended to package through IDEA\
Configuration: Project Structure -> Artifacts -> Add New JAR -> Extract to Target JAR -> Choose your own manifest file
path -> Done\
Package: Build -> Build Artifacts -> Build

## Connect

If you have any questions, you can send an email to liang.tang.cx@gmail.com

## Thanks

- <a href="https://jb.gg/OpenSource"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png?_gl=1*98642y*_ga*MTIxMDA5OTM5Ni4xNjgwMzQyNjgy*_ga_9J976DJZ68*MTY4MTIxMDIzMy41LjEuMTY4MTIxMTE1MS4wLjAuMA..&_ga=2.268101710.1369693703.1681210234-1210099396.1680342682" width="100px" alt="jetbrains">
- **Thanks to JetBrains for the free open source license**</a>
