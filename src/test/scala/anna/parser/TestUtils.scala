package anna.parser

import scala.io.Source

trait TestUtils {
  def readFileAsString(resourcePath: String) = {
    Source
      .fromInputStream(getClass.getResourceAsStream(resourcePath))("UTF-8")
      .getLines()
      .mkString("\n")
  }
}
