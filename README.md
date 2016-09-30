# Scraper de Registros Civiles

Como parte de la Expedición de Datos [“Descubriendo personas invisibles”](https://sg.com.mx/buzz/expedici-n-datos-descubriendo-personas-invisibles#.V-zHD5MrJE4), organizado por el [Banco Interamericano de Desarrollo](http://www.iadb.org/es/banco-interamericano-de-desarrollo,2837.html) y [Software Gurú](https://sg.com.mx/).

Scripts para [scrapear](https://es.wikipedia.org/wiki/Web_scraping) la ubicación de los Registros Civiles (RRCC) de la web [http://www.registro-civil.com.mx/](http://www.registro-civil.com.mx/).

## Requerimientos

* Scala 2.11
* JDK 1.8

## Como funciona

El fichero [`urls.txt`](https://github.com/oxcarh/scraper-registros-civiles-scala/blob/master/src/main/resources/urls.txt) contiene las URLs donde están los listados de los RRCC de las entidades.

### Scrapeo de ubicaciones

El script [`ScrapeRRCC.scala`](https://github.com/oxcarh/scraper-registros-civiles-scala/blob/master/src/main/scala/ScrapeRRCC.scala) lee las urls del fichero `urls.txt` y extrae la información de las **oficialias**, contenidas en una tabla, y las guarda en ficheros [CSV](https://es.wikipedia.org/wiki/CSV), uno por cada URL en la carpeta [`data/not-geocoded`](https://github.com/oxcarh/scraper-registros-civiles-scala/tree/master/data/not-geocoded).

Estos ficheros CSV contienen la información de los RRCC, con los campos:

* nombre
* direccion
* contacto

### Geocodificación de las ubicaciones

Los ficheros CSV en la carpeta [`data/not-geocoded`](https://github.com/oxcarh/scraper-registros-civiles-scala/tree/master/data/not-geocoded) no contienen ubicación (latitud, longitud), solo la dirección.

El script [`GeocodeRRCC.scala`](https://github.com/oxcarh/scraper-registros-civiles-scala/blob/master/src/main/scala/GeocodeRRCC.scala) lee los ficheros CSV de la carpeta [`data/not-geocoded`](https://github.com/oxcarh/scraper-registros-civiles-scala/tree/master/data/not-geocoded) intenta geocodificar las direcciones y guarda el resultado en la carpeta [`data/geocoded`](https://github.com/oxcarh/scraper-registros-civiles-scala/tree/master/data/geocoded).

***Nota importante:*** Para la geocodificación se usó el servicio de Google, y muchas direcciones no regresan localización, y algunas regresan geolocalizaciones erróneas. Es necesario revisar mejor los datos para obtener geolocalizaciones correctas a partir de las direcciones de los RRCC.

## Notas
La web scrapeada no contiene información referente a los Registros Civiles de la entidad de Colima.

A su vez tiene dos listados diferentes para Guadalajara y Jalisco, y para Monterrey y Nuevo León.
