# Implementation Principles {#implementation-principles}

javafxTool favors small, pluggable modules over a monolithic application. Core
services expose stable contracts, while application modules contribute UI,
configuration and tools through Java SPI.

## SPI first

Framework extension points are represented by service interfaces such as
`FXSamplerProject`, `MenubarConfigration`, `CenterPanelService`,
`TemplateLoaderService`, `GroovyLoaderService` and `LoginCheck`. Implementations
are discovered through `ServiceLoader`, allowing modules to be assembled without
hard-coded dependencies.

## Service discovery flow

Each module contributes features by exporting implementation classes and
registering them in `META-INF/services`. The framework starts with the stable
SPI contracts, asks `ServiceLoader` for providers and then applies each provider
to the corresponding runtime area. In the table below, the discovery point is
the class that calls `ServiceLoader`, the injection timing shows when the
provider is consumed, and the runtime responsibility is what the framework does
with each discovered provider.

| Service contract | Discovery point | Injection timing | Runtime responsibility |
| ---------------- | --------------- | ---------------- | ---------------------- |
| `BannerPrinterService` | `FXSampler.printBanner()` | First step in `FXSampler.start()` after properties are initialized. | Print custom startup banner text before the stage is assembled. |
| `SplashScreen` | `FXSampler.loadSplash()` | Early startup, before system resources and the main UI are initialized. | Supply the splash image shown until the main stage opens or splash animation completes. |
| `FXSamplerConfiguration` | `FXSampler.loadConfiguration()` and `SampleBase` | During `initializeSystem()`, before `FxApp.init()`, menus and center panel selection; also available to samples. | Resolve the application icon, title, scene stylesheet and theme. |
| `MenubarConfigration` | `FXSampler.initializeSystem()` | After `FxApp.init()` and before sample scanning. | Provide the top menu bar used by the main border pane. |
| `CenterPanelService` | `FXSampler.initializeSystem()` | After menu discovery and before `InterfaceScanner.invoke(InitializingFactory.initialize)`. | Provide the main center node and react to sample selection changes. |
| `TemplateLoaderService` | `TemplateLoaderScanner.initialize()` | During `InitializingFactory.initialize`, before samples are scanned. | Add module-specific FreeMarker template loaders ahead of the core default templates. |
| `GroovyLoaderService` | `GroovyLoaderScanner.initialize()` | During `InitializingFactory.initialize`, before samples are scanned. | Add module-specific Groovy script locations ahead of the core default scripts. |
| `FXSamplerProject` | `SampleScanner` | When `initializeSystem()` constructs `SampleScanner` and scans samples. | Collect project metadata, sample base packages and welcome pages. |
| `SamplesTreeViewConfiguration` | `FXSampler.initializeUI()` | After the `TreeView` is created and before the stage is shown. | Customize the sample tree view, tree cell factory and model. |
| `VersionCheckerService` | `FXSampler.initializeUI()` stage shown handler | After the primary stage is shown and update checking is enabled. | Run optional version checks on the task executor. |
| `SamplePostProcessorService` | `FXSampler.initializeSource()` | Background initialization after the UI posts `ApplicationPreparedEvent`. | Adjust collected sample tree items after scanning. |
| `EasterEggService` | `FXSampler.initializeEasterEggs()` | Background initialization after sample post-processing. | Register and start optional hidden UI behavior when enabled. |
| `LoginCheck` | `LoginFrame.start()` | At login window startup, before login controls are assembled. | Validate login input and execute login/register actions. |

@dot
 digraph service_discovery_flow {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.45", ranksep="0.65"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   contracts [label="SPI contracts\\ncore / frame / login"];
   descriptors [label="module-info.java\\nuses / provides"];
   providers [label="Provider classes\\ncommon / demo / smc / qe / cg"];
   loader [label="ServiceLoader.load(...)"];
   timing [label="Startup phase consumes providers"];
   runtime [label="Runtime assembly\\nmenus, tree, resources, login"];
   contracts -> descriptors -> providers -> loader -> timing -> runtime;
  }
@enddot

## Application shell flow

The frame module owns the primary JavaFX shell. During startup it discovers
projects, configuration providers and UI extension services, then composes the
menu bar, tree view, center panel and optional welcome content. Application
modules only need to provide SPI implementations; they do not need to change the
frame startup sequence.

@dot
 digraph shell_flow {
   graph [rankdir=TB, bgcolor="transparent", nodesep="0.4", ranksep="0.55"];
   node [shape=box, style="rounded,filled", fillcolor="#eff6ff", color="#93c5fd", fontname="Helvetica"];
   edge [color="#64748b", arrowsize="0.8"];
   start [label="FXSampler.start()"];
   banner [label="printBanner()\\nBannerPrinterService"];
   splash [label="loadSplash()\\nSplashScreen"];
   system [label="initializeSystem()\\nconfiguration, menu, center"];
   factories [label="InitializingFactory.initialize\\nTemplate and Groovy loaders"];
   scan [label="SampleScanner\\nFXSamplerProject and Sample classes"];
   ui [label="initializeUI()\\nTreeView, center panel, scene"];
   ready [label="ApplicationPreparedEvent"];
   background [label="initializeSource()\\npost processors and easter eggs"];
   shown [label="Stage shown\\nVersionCheckerService"];
   start -> banner -> splash -> system -> factories -> scan -> ui -> ready -> background;
   ui -> shown;
  }
@enddot

## Resource extension flow

Template and script resources are also pluggable. `TemplateLoaderScanner`
combines user template directories, module-provided `TemplateLoaderService`
instances and the core default templates into one FreeMarker
`MultiTemplateLoader`. `GroovyLoaderScanner` follows the same provider-first
pattern for module Groovy paths so application modules can add scripts without
changing core code.

@dot
 digraph resource_flow {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.45", ranksep="0.65"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   user [label="User configuration directory\\nhighest priority", fillcolor="#dcfce7", color="#22c55e"];
   module [label="Module providers\\nTemplateLoaderService\\nGroovyLoaderService"];
   core [label="Core defaults\\n/templates and /groovy"];
   freemarker [label="FreeMarker\\nMultiTemplateLoader"];
   groovy [label="GroovyScriptEngine\\nSecureASTCustomizer"];
   user -> freemarker;
   module -> freemarker;
   core -> freemarker;
   user -> groovy;
   module -> groovy;
   core -> groovy;
 }
@enddot

## UI composition

JavaFX views are kept modular. Shared UI helpers live in `core` and `common`,
while modules such as `smc`, `qe` and `cg` contribute their own tools and sample
nodes.

@dot
 digraph implementation_flow {
   graph [rankdir=TB, bgcolor="transparent", nodesep="0.4", ranksep="0.55"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   boot [label="Application startup"];
   services [label="ServiceLoader discovers SPI implementations"];
   frame [label="Frame builds menu, tree and center panel"];
   tools [label="Application modules provide tools and samples"];
   runtime [label="Core/common utilities support runtime behavior"];
   boot -> services -> frame -> tools;
   runtime -> frame;
   runtime -> tools;
 }
@enddot

## Documentation strategy

Doxygen is configured to preserve source comments, extract all Java symbols and
include private, protected, package, static and local members. This makes the
source browser useful for maintainers while keeping the public package tree
available from the generated navigation.

The generated site is intended to document both API contracts and implementation
details. Page-level guides explain architecture, diagrams show module and
runtime relationships, and source pages retain class, field and method comments
so maintainers can navigate from high-level concepts down to individual symbols.
