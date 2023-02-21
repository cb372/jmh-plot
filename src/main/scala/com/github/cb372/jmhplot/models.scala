package com.github.cb372.jmhplot

import cats.syntax.either.*
import io.circe.*
import io.circe.generic.semiauto.*

enum BenchmarkMode(val label: String):
  case Throughput     extends BenchmarkMode("thrpt")
  case AverageTime    extends BenchmarkMode("avgt")
  case SampleTime     extends BenchmarkMode("sample")
  case SingleShotTime extends BenchmarkMode("ss")

object BenchmarkMode:
  given Decoder[BenchmarkMode] = Decoder.decodeString.emap(label =>
    Either.fromOption(
      BenchmarkMode.values.find(_.label == label),
      s"Unsupported benchmark mode: $label"
    )
  )

case class ScoreError(error: Double)
object ScoreError:
  given Decoder[ScoreError] = Decoder.instance(cursor =>
    ScoreError(
      Decoder.decodeDouble
        .tryDecode(cursor)
        .getOrElse(0.0)
    ).asRight
  )

case class Metric(
    score: Double,
    scoreError: ScoreError,
    scoreUnit: String
)

case class Benchmark(
    benchmark: String,
    mode: BenchmarkMode,
    threads: Int,
    forks: Int,
    jdkVersion: String,
    warmupIterations: Int,
    measurementIterations: Int,
    params: Option[Map[String, String]],
    primaryMetric: Metric
)
object Benchmark:
  given Decoder[Benchmark] = deriveDecoder[Benchmark]

case class BenchmarkRow(
    name: String,
    score: Double,
    error: Double
)

case class ScoreDescription(
    mode: BenchmarkMode,
    unit: String
)

enum OutputFormat:
  case Qt
  case Png(filename: String)
  case Svg(filename: String)
