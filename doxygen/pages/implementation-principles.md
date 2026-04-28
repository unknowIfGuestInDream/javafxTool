# Implementation Principles {#implementation-principles}

javafxTool favors small, pluggable modules over a monolithic application. Core
services expose stable contracts, while application modules contribute UI,
configuration and tools through Java SPI.

## SPI first

Framework extension points are represented by service interfaces such as
`FXSamplerProject`, `MenubarConfiguration`, `CenterPanelService`,
`TemplateLoaderService`, `GroovyLoaderService` and `LoginCheck`. Implementations
are discovered through `ServiceLoader`, allowing modules to be assembled without
hard-coded dependencies.

## Service discovery flow

Each module contributes features by exporting implementation classes and
registering them in `META-INF/services`. The framework starts with the stable
SPI contracts, asks `ServiceLoader` for providers and then applies each provider
to the corresponding runtime area. In the table below, the discovery
point is the class that calls `ServiceLoader`, and the runtime responsibility is
what the framework does with each discovered provider.

| Service contract | Discovery point | Runtime responsibility |
| ---------------- | --------------- | ---------------------- |
| `FXSamplerProject` | `SampleScanner` | Collect project metadata, sample base packages and welcome pages. |
| `FXSamplerConfiguration` | `FXSampler` and `SampleBase` | Resolve the application title, icon, stylesheet and theme. |
| `MenubarConfiguration` | `FXSampler` | Build the top menu bar supplied by application modules. |
| `SamplesTreeViewConfiguration` | `FXSampler` | Customize the sample tree view and tree cell factory. |
| `CenterPanelService` | `FXSampler` | Provide the main center node and react to sample selection changes. |
| `SamplePostProcessorService` | `FXSampler` | Adjust collected sample tree items after scanning. |
| `VersionCheckerService` | `FXSampler` | Run optional version checks after startup. |
| `SplashScreen` | `FXSampler` | Supply the splash image shown before the main stage opens. |
| `BannerPrinterService` | `FXSampler` | Print custom startup banner text. |
| `EasterEggService` | `FXSampler` | Register optional hidden UI behavior. |
| `TemplateLoaderService` | `TemplateLoaderScanner` | Add module-specific FreeMarker template loaders. |
| `GroovyLoaderService` | `GroovyLoaderScanner` | Add module-specific Groovy script locations. |
| `LoginCheck` | `LoginFrame` | Validate login input and execute login/register actions. |

@dot
 digraph service_discovery_flow {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.45", ranksep="0.65"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   contracts [label="SPI contracts"];
   providers [label="Module providers\\nMETA-INF/services"];
   loader [label="ServiceLoader"];
   runtime [label="Runtime assembly"];
   contracts -> providers -> loader -> runtime;
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
   start [label="FXSampler startup"];
   project [label="Load FXSamplerProject"];
   config [label="Apply FXSamplerConfiguration"];
   menu [label="Build MenubarConfiguration"];
   tree [label="Configure SamplesTreeViewConfiguration"];
   center [label="Attach CenterPanelService"];
   post [label="Run SamplePostProcessorService"];
   start -> project -> config -> menu -> tree -> center -> post;
 }
@enddot

## Resource extension flow

Template and script resources are also pluggable. `TemplateLoaderScanner`
combines user template directories, module-provided `TemplateLoaderService`
instances and the core default templates into one FreeMarker
`MultiTemplateLoader`. `GroovyLoaderScanner` follows the same provider-first
pattern for module Groovy paths so application modules can add scripts without
changing core code.

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
