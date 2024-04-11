import scala.io.Source

object Main extends App {
    val validFile = "validMessages"
    val invalidFile = "invalidMessages"
    println(
        s"this file $validFile should be valid: ${validateMessagesFile(validFile)}"
    )
    println(
        s"this file $invalidFile should be invalid: ${validateMessagesFile(invalidFile)}"
    )
}

def validateMessagesFile(filePath: String): Boolean = {
    val source = Source.fromFile(filePath)
    val lines = source.getLines().filterNot(_.trim.isEmpty)
    val valid = lines.zipWithIndex.forall { case (line, index) =>
        if (!validateLocalizeTranslation(line)) {
            println(s"Line ${index + 1} does not match the allowed pattern: $line")
            false
        } else true
    }
    source.close()
    valid
}

def validateLocalizeTranslation(line: String): Boolean = {
    val allowedPattern =
        "([a-zA-Z_]+(\\.[a-zA-Z_0-9-]+)*(=|\\s=|=\\s|\\s=\\s]).*)|^(#.*)$|^(\\s)*$".r.pattern
    allowedPattern.matcher(line).matches
}