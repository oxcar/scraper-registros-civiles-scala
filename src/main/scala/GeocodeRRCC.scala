
/**
  * Geocode RRCC in data/not-geocode folder
  */

import java.io.File

import com.github.tototoshi.csv._
import com.google.maps.model.GeocodingResult
import com.google.maps.{GeoApiContext, GeocodingApi}

object GeocodeRRCC {

  implicit object CsvFormat extends DefaultCSVFormat {
    override val delimiter = ','
    override val quoteChar = '"'
    override val quoting = QUOTE_ALL
  }

  val googleApiKey = ""
  val geoApiContext = new GeoApiContext().setApiKey(googleApiKey)

  def main(args: Array[String]): Unit = {

    val notGeocodedDirectory = new File("data/not-geocoded")

    val files = {
      if (notGeocodedDirectory.exists && notGeocodedDirectory.isDirectory) {
        notGeocodedDirectory.listFiles.filter(_.isFile).toList
      } else {
        List[File]()
      }
    }

    files.foreach(file => {
      println(file)
      val reader = CSVReader.open(file)
      reader.readNext()
      val lines = reader.all()

      val lines_geocoded = lines.map(line => GeocodingApi.geocode(geoApiContext, line(1)).await() match {
        case Array() => line ::: List("0", "0")
        case xs: Array[GeocodingResult] => line ::: List(xs(0).geometry.location.lat.toString, xs(0).geometry.location.lng.toString)
        case _ => line ::: List("0", "0")
      })

      val csv_out = new File(s"data/geocoded/${file.getName}")
      val writer = CSVWriter.open(csv_out)
      writer.writeRow(List("nombre", "direccion", "contacto", "latitud", "longitud"))
      writer.writeAll(lines_geocoded)
    })

  }

}
