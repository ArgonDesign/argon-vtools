# Alint

Alint is a tool for processing Verilog files. Currently it implements
2 operations, linting and obfuscation.

## Building

Building Alint requires `sbt`. To build a local copy, run:

```
  sbt stage
```

This will build the executable at: `./target/universal/stage/bin/alint`

## Linting

The `lint` subcommand can be used to lint Verilog files:

```
  alint lint foo.v bar.v
```

Lint errors are printed on the standard output.

## Obfuscation

If a set of Verilog files are lint clean, then the `mangle` subcommand can
be used to obfuscate those source files:

```
  alint mangle --odir out foo.v bar.v
```

This will mangle the source code, except for the interface signals of the
top level module, and place the result in the `out` directory. See the `--help`
option for further parameters.
