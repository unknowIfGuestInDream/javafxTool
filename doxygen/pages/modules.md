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
