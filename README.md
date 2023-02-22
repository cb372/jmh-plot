# jmh-plot

A command-line tool to render [JMH](https://github.com/openjdk/jmh) benchmark
results using [gnuplot](http://www.gnuplot.info/).

![Example plot](./examples/jwt-benchmarks.png)

## How to install

Only MacOS x86 binary available for now.

Download it from the `bin` directory in this repo and add it to your `PATH`.

## How to use

When you run JMH (or [sbt-jmh](https://github.com/sbt/sbt-jmh)), pass these
arguments to make JMH write its results to a JSON file:

```
-rf json -rff my-benchmarks.json
```

Then feed that JSON file to `jmh-plot`:

```
jmh-plot my-benchmarks.json
```

`jmh-plot` will print a gnuplot script to stdout, so you can pipe it straight
into `gnuplot`:

```
jmh-plot my-benchmarks.json | gnuplot
```

## CLI options

```
Usage: jmh-plot [--title <string>] <input file>

Render your JMH benchmark results using gnuplot

Options and flags:
    --help
        Display this help text.
    --title <string>
        Title to display above the plot
```

## How to build

```
sbt nativeLink
```

Or if you're happy running it with `java -jar` you can build it with `sbt
assembly` instead.

## Future work

* Support benchmarks with parameters
* Figure out a good way to distribute the tool. Coursier? Scala Native binaries
  on GitHub releases?
