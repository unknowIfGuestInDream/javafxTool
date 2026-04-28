# Module Guide {#module-guide}

javafxTool is organized as a Maven multi-module project. The modules are loaded
together to provide a pluggable JavaFX scaffolding framework.

| Module | Role |
| ------ | ---- |
| `core` | Shared utilities, JavaFX helpers, event bus, template and script loading support. |
| `frame` | Main UI shell and SPI definitions for integrating tools and samples. |
| `login` | Optional login framework and authentication SPI. |
| `common` | Reusable controls, configuration helpers and developer tools. |
| `demo` | Example module showing framework and ControlsFX usage. |
| `smc`, `qe`, `cg` | Application modules that integrate through the framework SPI. |

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

@dot
 digraph module_dependencies {
   graph [rankdir=LR, bgcolor="transparent", nodesep="0.45", ranksep="0.65"];
   node [shape=box, style="rounded,filled", fillcolor="#eff6ff", color="#93c5fd", fontname="Helvetica"];
   edge [color="#64748b", arrowsize="0.8"];
   core -> frame;
   core -> common;
   frame -> login;
   common -> demo;
   frame -> smc;
   frame -> qe;
   frame -> cg;
   common -> smc;
   common -> qe;
   common -> cg;
 }
@enddot

New application modules normally depend on `core`, implement the SPI contracts in
`frame` or `login`, and register those implementations through Java service
provider configuration files.
