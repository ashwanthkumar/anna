package anna.parser

case class OperationSpec(using: String, as: Option[String])

case class StageSpec(transformer: List[OperationSpec], validator: List[OperationSpec], sink: OperationSpec)

case class JobSpec(name: String, source: OperationSpec, stages: List[StageSpec])

