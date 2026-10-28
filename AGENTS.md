# AGENTS.md

## Overview
`arcane-framework-test` provides testkit utilities, mock services, and layers for testing Arcane streaming applications (Scala 3.9, sbt 2, ZIO 2, Iceberg).

## Build & Test Commands
All commands run via `sbt`.
- **Compile:** `sbt compile`
- **Run tests:** `sbt test` (note: test suites run with `Test / logBuffered := false`)
- **Check formatting:** `sbt scalafmtCheckAll`
- **Reformat code:** `sbt scalafmtAll`
- **Multiple tasks:** In sbt 2, sequential commands in batch mode cannot be passed as space-separated arguments (e.g. `sbt clean compile` fails). Use separate invocations (`sbt clean && sbt compile`) or semicolon syntax (`sbt "clean; compile"`).

## Authentication & Credentials
- Resolution of artifacts from `https://maven.pkg.github.com/SneaksAndData/arcane-framework-scala` and publication to `SneaksAndData/arcane-framework-test` requires a GitHub token.
- The build checks for `GITHUB_TOKEN` in the environment, falling back to reading a token from the `.ghtoken` file at repository root.
- `.ghtoken` is ignored by git.

