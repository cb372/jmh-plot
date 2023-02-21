package com.github.cb372.jmhplot

import cats.syntax.apply.*
import com.monovore.decline.Opts
import java.nio.file.Path

case class Options(
    inputFile: Path,
    title: String
)

object Options:

  val parser: Opts[Options] =
    (
      Opts.argument[Path]("input file"),
      Opts
        .option[String]("title", help = "Title to display above the plot")
        .withDefault("Benchmark results"),
    ).mapN(Options.apply)
