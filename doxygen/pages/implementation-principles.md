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
