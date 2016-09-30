import java.io.File
import java.net.URL

import com.github.tototoshi.csv._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.io.Source

object ScrapeRRCC {

  val browser = JsoupBrowser()

  implicit object CsvFormat extends DefaultCSVFormat {
    override val delimiter = ','
    override val quoteChar = '"'
    override val quoting = QUOTE_NONNUMERIC
  }

  def main(args: Array[String]): Unit = {

    for (line <- Source.fromInputStream(getClass.getResourceAsStream("urls.txt")).getLines) {
      val doc = browser.get(line)
      val entidad = new URL(line).getPath.drop(1)
      println(s"Procesando $entidad")
      val oficialias: List[Element] = doc >> elementList("table.oficialias tbody tr")

      val rrcc = oficialias.map { tr =>
        val children = tr.children
        val nombre = children.head.children.head.text
        val direccion = children.drop(1).head.text
        val contacto = children.drop(2).head.text
        List(nombre, direccion, contacto)
      }

      val csv_out = new File(s"data/not-geocoded/$entidad.csv")
      val writer = CSVWriter.open(csv_out)
      writer.writeRow(List("nombre", "direccion", "contacto"))
      writer.writeAll(rrcc)
    }

  }

}
