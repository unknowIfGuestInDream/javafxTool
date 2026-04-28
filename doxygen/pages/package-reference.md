# Package Reference {#package-reference}

Doxygen builds the package tree from Java package declarations and the namespace
entries in `doxygen/packages.dox`. Use the generated **Packages** navigation item
for the complete expandable package list.

Key package families:

| Package family | Description |
| -------------- | ----------- |
| `com.tlcsdm.core` | Core infrastructure, JavaFX helper APIs and shared runtime utilities. |
| `com.tlcsdm.frame` | Framework shell, service contracts and sample tree integration points. |
| `com.tlcsdm.login` | Login frame and login verification service contract. |
| `com.tlcsdm.jfxcommon` | Common reusable tools and controls used by application modules. |
| `com.tlcsdm.demo` | Demo samples and ControlsFX showcase code. |
| `com.tlcsdm.smc` | SMC application module providers and tools. |
| `com.tlcsdm.qe` | QE application module providers and tools. |
| `com.tlcsdm.cg` | CG application module providers and tools. |

Package descriptions are also supplied by `package-info.java` files in production
source directories so that package pages remain readable in the generated API
reference.
