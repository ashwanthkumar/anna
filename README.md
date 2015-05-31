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

## TODOs
- Add more descriptive documentation
- Add notes on API endpoints
