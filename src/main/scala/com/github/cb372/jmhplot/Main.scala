package com.github.cb372.jmhplot

import cats.effect.*
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp
import io.circe.parser.decode

import java.nio.charset.StandardCharsets
import java.nio.file.Files

object Main
    extends CommandIOApp(
      name = "jmh-plot",
      header = "Render your JMH benchmark results using gnuplot"
    ):

  override def main: Opts[IO[ExitCode]] =
    Options.parser.map { options =>
      for {
        json       <- IO(String(Files.readAllBytes(options.inputFile), StandardCharsets.UTF_8))
        benchmarks <- IO.fromEither(decode[List[Benchmark]](json))
        output = render(benchmarks, options)
        _ <- IO.println(output)
      } yield ExitCode.Success
    }

  private def render(benchmarks: List[Benchmark], options: Options): String =
    val rows = toBenchmarkRows(benchmarks)

    // we assume all benchmarks use the same mode and unit
    // (otherwise it's not clear how we would render them as a box plot)
    val scoreDescription = ScoreDescription(
      mode = benchmarks.head.mode,
      unit = benchmarks.head.primaryMetric.scoreUnit
    )

    val format =
      OutputFormat.Png("examples/jwt-benchmarks.png") // TODO CLI option

    Render.render(
      rows,
      options.title,
      scoreDescription,
      format
    )

  private def toBenchmarkRows(benchmarks: List[Benchmark]): List[BenchmarkRow] =
    val commonPrefix = benchmarks
      .map(_.benchmark.split("\\.").toList)
      .reduce(lcp)
      .mkString("", ".", ".")

    benchmarks.map(x =>
      BenchmarkRow(
        name = x.benchmark.stripPrefix(commonPrefix),
        score = x.primaryMetric.score,
        error = x.primaryMetric.scoreError.error
      )
    )

  // find the longest common prefix of two lists
  private def lcp(xs: List[String], ys: List[String]): List[String] =
    (xs, ys) match
      case (h1 :: t1, h2 :: t2) if h1 == h2 => h1 :: lcp(t1, t2)
      case _                                => Nil
