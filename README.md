[![MIT](https://img.shields.io/github/license/unknowIfGuestInDream/javafxTool)](https://github.com/unknowIfGuestInDream/javafxTool?tab=MIT-1-ov-file#readme)
[![Java CI with Maven](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/maven.yml/badge.svg)](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/maven.yml)
[![Java package with Maven](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/artifact.yml/badge.svg?branch=master)](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/artifact.yml)
[![Deploy Docs](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/deploy-docs.yml/badge.svg?branch=master)](https://javafxtool.tlcsdm.com/)
[![Gitee repos mirror](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/sync-gitee.yml/badge.svg?branch=master)](https://gitee.com/unknowIfGuestInDream/javafxTool)
[![GitHub repo size](https://img.shields.io/github/repo-size/unknowIfGuestInDream/javafxTool)](https://github.com/unknowIfGuestInDream/javafxTool)
[![Smc release](https://img.shields.io/github/v/release/unknowIfGuestInDream/javafxTool?filter=v*-smc)](https://github.com/unknowIfGuestInDream/javafxTool/releases?q=smc&expanded=true)
[![Qe release](https://img.shields.io/github/v/release/unknowIfGuestInDream/javafxTool?filter=v*-qe)](https://github.com/unknowIfGuestInDream/javafxTool/releases?q=qe&expanded=true)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=unknowIfGuestInDream_javafxTool&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=unknowIfGuestInDream_javafxTool)
[![Qodana](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/qodana_code_quality.yml/badge.svg)](https://github.com/unknowIfGuestInDream/javafxTool/actions/workflows/qodana_code_quality.yml)
[![Jenkins](https://jenkins.tlcsdm.com/job/javafxtool/badge/icon)](https://jenkins.tlcsdm.com/job/javafxtool)
[![OpenSSF Scorecard](https://api.scorecard.dev/projects/github.com/unknowIfGuestInDream/javafxTool/badge)](https://scorecard.dev/viewer/?uri=github.com/unknowIfGuestInDream/javafxTool)

# Introduce

[中文文档](./README_zh.md)  [日本語文書](./README_jp.md)\
Javafx scaffolding, built on JDK17 + JavaFX21 + controlsfx 11.x.x + Maven

Frame and login are basic modules, which are pluggable through java SPI to facilitate the integration of application
modules. demo is the provided example module.

# Component

- docs: Documentation (built with docsify)
- frame: Application main UI framework (provide SPI interface)
- core: Application core components, including some common functions
- login: Login module (provides SPI interface)
- demo: demo example (based on controlsfx demo transformation)
- common: Common component module, used by application modules
- smc, qe, cg: Personal application (no reference required)

# Interface

## core

- TemplateLoaderService: Freemarker TemplateLoader loads, subclass implementation to add the template path of the
  submodule
- GroovyLoaderService: Groovy script path loading, subclass implementation to add submodule script path

## frame

- FXSamplerProject: Project information, including project name, module, package name, welcome page, etc.
- MenubarConfigration: Menu bar configuration
- FXSamplerConfiguration: Project style, title and icon configuration
- CenterPanelService: Node configuration in the central area, including the interface when clicking and switching
  components
- SplashScreen: Splash screen image
- SamplePostProcessorService: Sample post-processing
- VersionCheckerService: Version update check
- SamplesTreeViewConfiguration: Menu tree configuration
- BannerPrinterService: Start Banner
- EasterEggService: Easter Egg

## login

- LoginCheck: Login verification

# Build requirements

The build commands require the installation and setup of Java 17 or higher and Maven version 3.5.4 or higher.

# Pack

1. IDEA artifact packaging\
   Configuration: Project Structure -> Artifacts -> Add New JAR -> Extract to Target JAR -> Choose your own manifest
   file
   path -> Done\
   Package: Build -> Build Artifacts -> Build
2. It is recommended to package through `mvn package`.

# Integration builds

The integrations (nightly) build jobs are hosted on Jenkins instance https://jenkins.tlcsdm.com/job/javafxtool/

# Connect

If you have any questions, you can send an email to liang.tang.cx@gmail.com

# Thanks

- <a href="https://jb.gg/OpenSource"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png?_gl=1*98642y*_ga*MTIxMDA5OTM5Ni4xNjgwMzQyNjgy*_ga_9J976DJZ68*MTY4MTIxMDIzMy41LjEuMTY4MTIxMTE1MS4wLjAuMA..&_ga=2.268101710.1369693703.1681210234-1210099396.1680342682" width="100px" alt="jetbrains">
- **Thanks to JetBrains for the free open source license**</a>
