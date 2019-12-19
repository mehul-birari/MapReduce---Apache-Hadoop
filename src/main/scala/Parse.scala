import com.typesafe.config.ConfigException.Parse
import com.typesafe.config.{Config, ConfigFactory}
import javax.xml.parsers.SAXParserFactory
import org.slf4j.{Logger, LoggerFactory}
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io._
import java.util.Scanner
import scala.collection.mutable.ListBuffer


object Parse {
  val Logger: Logger = LoggerFactory.getLogger(classOf[Parse])
  val Config: Config = ConfigFactory.parseResources("application.conf")

  @throws[FileNotFoundException]
  def main(argv: Array[String]): Unit = {
    val cs_professors = new ListBuffer[String]()
    val file = new File(Config.getString("CS_Professors.name"))
    val sc = new Scanner(file)
    while ( {
      sc.hasNextLine
    }) cs_professors += sc.nextLine()
    try {
      val props = System.getProperties
      props.setProperty("entityExpansionLimit", "2000000")
      val factory = SAXParserFactory.newInstance
      val saxParser = factory.newSAXParser
      val handler = new DefaultHandler() {
        var article = false
        var author = false
        val title = false
        val name = false
        val cs_prof = false
        val year = false
        val authors = new ListBuffer[String]()

        override def startElement(uri: String, localName: String, qName: String, attributes: Attributes): Unit = {
          if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) this.article = true
          if (qName.equalsIgnoreCase("author") && this.article) this.author = true
        }

        override def endElement(uri: String, localName: String, qName: String): Unit = {
          if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
            this.author = false
            this.article = false
            var auth = ""
            if (this.authors.size == 1) {
              Logger.info("Single author " + this.authors(0))
              Parse.appendStrToFile(this.authors(0) + "\n")
            }
            else {
              var i = 0
              if (this.authors.size > 1 && this.authors.size < 3) {
                i = 0
                while ( {
                  i < this.authors.size
                }) {
                  if (i == 0) auth = this.authors(i)
                  else auth = auth + "," + this.authors(i)

                  {
                    i += 1; i
                  }
                }
                Logger.info("Two authors: " + auth)
                Parse.appendStrToFile(auth + "\n")
              }
              else if (this.authors.size > 2) {
                i = 0
                while ( {
                  i < this.authors.size - 1
                }) {
                  var authPair = ""
                  var j = i + 1
                  while ( {
                    j < this.authors.size
                  }) {
                    authPair = this.authors(i) + "," + this.authors(j)

                    {
                      j += 1; j
                    }
                  }
                  Logger.info("More than two authors: " + authPair)
                  Parse.appendStrToFile(authPair + "\n")

                  {
                    i += 1; i
                  }
                }
              }
            }
            this.authors.clear()
          }
        }


        override def characters(ch: Array[Char], start: Int, length: Int): Unit = {
          if (this.author) {
            val authorName = new String(ch, start, length)
            if (cs_professors.contains(authorName) && !this.authors.contains(authorName)) {
              Logger.info("Add author to list")
              this.authors += (authorName)
            }
          }
        }
      }
      saxParser.parse("dblp.xml", handler)
    } catch {
      case exc: Exception =>
        exc.printStackTrace()
    }
  }

  def appendStrToFile(str: String): Boolean = try if (str != null) {
    val out = new BufferedWriter(new FileWriter(Config.getString("auth_pairs.output"), true))
    out.write(str)
    Logger.info(str)
    out.close()
    true
  }
  else false
  catch {
    case exc: IOException =>
      Logger.error("exception occoured" + exc)
      false
  }
}
