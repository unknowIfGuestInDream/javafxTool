# Module Guide {#module-guide}

javafxTool is organized as a Maven multi-module project. The modules are loaded
together to provide a pluggable JavaFX scaffolding framework. The root project
keeps dependency versions, build plugins and shared quality settings in one
place, while each child module owns its Java Platform Module System descriptor
and any SPI providers it contributes.

| Module | Role |
| ------ | ---- |
| `core` | Shared utilities, JavaFX helpers, event bus, configuration paths, FreeMarker template loading and Groovy script loading. |
| `frame` | Main UI shell, sample scanner, project metadata model and SPI definitions for integrating tools and samples. |
| `login` | Optional login window and authentication SPI that can be reused by application modules. |
| `common` | Reusable controls, shared configuration helpers, default templates and common sample pages. |
| `demo` | Example module showing framework wiring and ControlsFX behavior. |
| `smc`, `qe`, `cg` | Application modules that integrate through the framework SPI and provide domain tools. |

The Java Platform Module System descriptors (`module-info.java`) are kept with
their owning Maven modules in source control. They are summarized here instead
of being listed as standalone root-level file entries in the generated file
index.

| Descriptor | Module focus |
| ---------- | ------------ |
| `core/src/main/java/module-info.java` | Core runtime utilities and shared JavaFX infrastructure. |
| `frame/src/main/java/module-info.java` | Main shell, samples, project metadata and UI extension SPI. |
| `login/src/main/java/module-info.java` | Login UI and authentication extension SPI. |
| `common/src/main/java/module-info.java` | Reusable common components and developer tools. |
| `demo/src/main/java/module-info.java` | Demonstration samples for framework and ControlsFX behavior. |
| `smc/src/main/java/module-info.java` | SMC application module tools and providers. |
| `qe/src/main/java/module-info.java` | QE application module tools and providers. |
| `cg/src/main/java/module-info.java` | CG application module tools and providers. |

## Runtime layers

The framework is intentionally split into a small set of layers. Lower layers
provide reusable runtime support; upper layers contribute concrete tools through
SPI registrations. Application modules can therefore be added or removed without
editing the frame module.

@dot
 digraph runtime_layers {
   graph [rankdir=TB, bgcolor="transparent", nodesep="0.45", ranksep="0.7"];
   node [shape=box, style="rounded,filled", fillcolor="#eff6ff", color="#93c5fd", fontname="Helvetica"];
   edge [color="#64748b", arrowsize="0.8"];
   build [label="Root Maven project\\nversions, plugins, quality config", fillcolor="#fef3c7", color="#f59e0b"];
   core [label="core\\nconfiguration, events, utilities\\nTemplateLoaderService, GroovyLoaderService"];
   frame [label="frame\\nFXSampler shell, sample scanner\\nUI and project SPI"];
   login [label="login\\nlogin scene and LoginCheck SPI"];
   common [label="common\\nshared controls, templates, samples"];
   apps [label="smc / qe / cg\\napplication tools and providers", fillcolor="#dcfce7", color="#22c55e"];
   demo [label="demo\\nreference SPI implementation", fillcolor="#f5f3ff", color="#a78bfa"];
   build -> core;
   build -> frame;
   build -> login;
   build -> common;
   build -> demo;
   build -> apps;
   core -> frame;
   core -> common;
   core -> apps;
   frame -> login;
   frame -> common;
   frame -> demo;
   frame -> apps;
   login -> demo;
   common -> demo;
   common -> apps;
 }
@enddot

## SPI provider map

New application modules normally depend on `core`, implement the SPI contracts in
`frame` or `login`, and register those implementations through Java service
provider declarations in `module-info.java`.

| Providing modules | Service contracts commonly provided |
| ----------------- | ----------------------------------- |
| `common` | `FXSamplerProject`, `TemplateLoaderService` |
| `demo` | `FXSamplerProject`, `MenubarConfigration`, `CenterPanelService`, `LoginCheck` |
| `smc` | `FXSamplerProject`, `MenubarConfigration`, `CenterPanelService`, `FXSamplerConfiguration`, `SplashScreen`, `TemplateLoaderService`, `GroovyLoaderService`, `SamplePostProcessorService`, `VersionCheckerService`, `SamplesTreeViewConfiguration`, `BannerPrinterService` |
| `qe` | `FXSamplerProject`, `MenubarConfigration`, `CenterPanelService`, `FXSamplerConfiguration`, `SplashScreen`, `TemplateLoaderService`, `GroovyLoaderService`, `SamplePostProcessorService`, `VersionCheckerService`, `SamplesTreeViewConfiguration`, `BannerPrinterService` |
| `cg` | `FXSamplerProject`, `MenubarConfigration`, `CenterPanelService`, `FXSamplerConfiguration`, `SplashScreen`, `SamplePostProcessorService`, `VersionCheckerService`, `SamplesTreeViewConfiguration`, `BannerPrinterService` |

@dot
 digraph provider_map {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.45", ranksep="0.65"];
   node [shape=box, style="rounded,filled", fillcolor="#f8fafc", color="#bfdbfe", fontname="Helvetica"];
   edge [color="#475569", arrowsize="0.8"];
   subgraph cluster_contracts {
     label="SPI contracts";
     color="#cbd5e1";
     project [label="FXSamplerProject"];
     ui [label="UI shell SPI\\nMenubar / CenterPanel\\nTreeView / Configuration"];
     resources [label="Resource SPI\\nTemplateLoader\\nGroovyLoader"];
     lifecycle [label="Lifecycle SPI\\nSplash / Banner\\nPostProcessor / Version\\nEasterEgg"];
     login [label="LoginCheck"];
   }
   subgraph cluster_modules {
     label="Provider modules";
     color="#cbd5e1";
     common [label="common"];
     demo [label="demo"];
     apps [label="smc / qe / cg"];
     frame [label="frame default\\nDefaultEasterEggService"];
   }
   common -> project;
   common -> resources;
   demo -> project;
   demo -> ui;
   demo -> login;
   apps -> project;
   apps -> ui;
   apps -> resources;
   apps -> lifecycle;
   frame -> lifecycle;
 }
@enddot

## Adding an application module

An application module usually follows this path:

1. Declare Maven and JPMS dependencies on the framework modules it uses.
2. Implement `FXSamplerProject` to expose the project name, base package and
   welcome page.
3. Add sample classes under the configured base package so `SampleScanner` can
   find them.
4. Implement optional UI services such as menu, center panel or tree view
   configuration.
5. Implement optional resource services when the module needs its own
   FreeMarker templates or Groovy scripts.
6. Register providers with `provides ... with ...` in `module-info.java`.
