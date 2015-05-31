package anna.parser

import org.scalatest.FlatSpec
import org.scalatest.Matchers.{be, contain, convertToAnyShouldWrapper}

class AnnaParserTest extends FlatSpec with TestUtils {

  "AnnaParser" should "parse the job spec" in {
    val input = readFileAsString("/jobs/job1.ingest")
    val jobSpec = AnnaParser.parse(input)
    jobSpec.source should be(OperationSpec("a.b.c.FromTSV", Some("tsvSource")))
    jobSpec.name should be("readTsvAndParse")

    val stage = StageSpec(
      transformer = List(
        OperationSpec("a.b.c.TSVToHtml", None),
        OperationSpec("a.b.c.HtmlToParseResult", Some("parser"))
      ),
      validator = List(
        OperationSpec("d.e.f.ParseResultValidator", None)
      ),
      sink = OperationSpec("x.y.z.PailWriter", Some("parseResult"))
    )

    jobSpec.stages should contain(stage)
  }

  it should "parse simple TRANSFORM" in {
    val input = "TRANSFORM USING \"a.b.c.TSVToHtml\""
    val opSpec = AnnaParser.parse(AnnaParser.operationSpec("TRANSFORM"), input).get
    opSpec.using should be("a.b.c.TSVToHtml")
  }

  it should "parse TRANSFORM with AS operator" in {
    val input = "TRANSFORM USING \"a.b.c.HtmlToParseResult\" AS \"parser\""
    val opSpec = AnnaParser.parse(AnnaParser.operationSpec("TRANSFORM"), input).get
    opSpec.using should be("a.b.c.HtmlToParseResult")
    opSpec.as should be(Some("parser"))
  }

}
