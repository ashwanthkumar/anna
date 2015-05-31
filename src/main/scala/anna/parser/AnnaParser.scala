package anna.parser

import scala.util.parsing.combinator.RegexParsers

object AnnaParser extends RegexParsers {
  protected def regexIgnoreCase(regex: String) = s"(?i)$regex".r
  def JOB = regexIgnoreCase("JOB")
  def USING = regexIgnoreCase("USING")
  def AS = regexIgnoreCase("AS")
  def END = regexIgnoreCase("END")
  def FORK = regexIgnoreCase("FORK")
  def QUOTE = literal("\"")
  def identifier = QUOTE ~> regex("[a-zA-Z0-9_]+".r) <~ QUOTE
  def implementation = QUOTE ~> regex("[a-zA-Z0-9_.]+".r).named("implementation name") <~ QUOTE
  def asName = AS ~> identifier
  def jobHeader = JOB ~> identifier

  def operationSpec(command: String) = regexIgnoreCase(command) ~> USING ~> implementation ~ opt(asName) ^^ {
    case using ~ as => OperationSpec(using, as)
  }

  def transformStatement = operationSpec("TRANSFORM")
  def validatorStatement = operationSpec("VALIDATE")
  def sinkStatement = operationSpec("SINK")
  def sourceStatement = operationSpec("SOURCE")

  def stagesWithForks = FORK.* ~> transformStatement.+ ~ validatorStatement.+ ~ sinkStatement ^^ {
    case transformers ~ validators ~ sink => StageSpec(isForked = true, transformers, validators, Some(sink))
  }

  def stageSpec = transformStatement.+ ~ validatorStatement.+ ~ opt(sinkStatement) ^^ {
    case transformers ~ validators ~ sink => StageSpec(isForked = false, transformers, validators, sink)
  }

  def annaSpec = jobHeader ~ sourceStatement ~ (stageSpec | stagesWithForks).+ <~ END ^^ {
    case jobName ~ source ~ stages => JobSpec(jobName, source, stages)
  }

  def parse(input: String): JobSpec = parse(annaSpec, input) match {
    case Success(result, _) => result
    case Failure(message, next) => displayError(message, next)
    case Error(message, next) => displayError(message, next)
  }

  private def displayError(msg: String, next: Input) = {
    val errorMessage =
      s"""
        |$msg
        |${next.source.toString.substring(next.offset).takeWhile(_ != '\n')}
        |^^^
      """.stripMargin
    sys.error(errorMessage)
  }
}
