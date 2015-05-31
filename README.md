[![Build Status](https://snap-ci.com/ashwanthkumar/anna/branch/master/build_image)](https://snap-ci.com/ashwanthkumar/anna/branch/master)

# anna
Hobby project inspired from [Goblin](https://github.com/linkedin/gobblin).

## Introduction
Anna is a data ingestion system for making ingesting data from various data sources a solved problem. It has an easy
to use DSL to define your job along with a configuration file that goes along with it. With the same job, you can
change the configurations at runtime based on various environments / systems.

## Job Specification
You can define an ingestion job like
```
JOB "readTsvAndParse"
    SOURCE USING "a.b.c.FromTSV" as "tsvSource"
    TRANSFORM USING "a.b.c.TSVToHtml"
    TRANSFORM USING "a.b.c.HtmlToParseResult" AS "parser"
    VALIDATE USING "d.e.f.ParseResultValidator"
    SINK USING "x.y.z.PailWriter" AS "parseResult"
END
```
You could also have FORKs in your processing pipeline as in the following example - `readTsvAndParse.ingest`. These are
useful when you want to sink the data to multiple locations on HDFS / multiple sources.
```
JOB "readTsvAndParse"
    SOURCE USING "a.b.c.FromTSV" as "tsvSource"
    TRANSFORM USING "a.b.c.TSVToHtml"
    TRANSFORM USING "a.b.c.HtmlToParseResult" AS "parser"
    VALIDATE USING "d.e.f.ParseResultValidator"
    FORK
        TRANSFORM USING "a.b.c.ParseResultToProduct"
        VALIDATE USING "d.e.f.ProductValidator"
        SINK USING "x.y.z.PailWriter" AS "product"
    FORK
        TRANSFORM USING "a.b.c.ParseResultToPrice"
        VALIDATE USING "d.e.f.PriceValidator"
        SINK USING "x.y.z.PailWriter" AS "price"
END
```
Every job specification is associated with a job configuration that's used for passing parameters to the specification.
```hocon
tsvSource {
    input.path = "/path/to/input"
    input.frequency = "hourly"
}
product {
    output.path = "/path/to/product/output"
    output.frequency = "hourly"
}
price {
    output.path = "/path/to/price/output"
    output.frequency = "hourly"
}
```
In the specification - `SOURCE`, `TRANSFORM`, `VALIDATE` and `SINK` are called operators. These operators can be marked
with an identifier which can later be used as a reference to pick the right configurations from the job conf. Example
you could find `SOURCE` is identified as `tsvSource` in the job spec. The configuration(s) for the `SOURCE`
implementation are namespaced in the job conf under `tsvSource`. This helps to keep the job specification and the values
for the job separate. For the same job specification we can swap in multiple configurations for various environments /
users / teams etc.

## TODOs
- Add more descriptive documentation
- Add notes on API endpoints
