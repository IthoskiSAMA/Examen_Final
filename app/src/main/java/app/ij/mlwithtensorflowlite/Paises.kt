package app.ij.mlwithtensorflowlite
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.Serializable
import java.util.ArrayList

class Paises : Serializable {
    var nombrePais: String? = null
    var url_Pais: String? = null
    var coordenadasPais: DoubleArray
    var iso2: String? = null
    var geo_pt: DoubleArray

    constructor() {}
    constructor(npais: String?, nurl: String?, ncoordenadasPais: DoubleArray, ncapital: String?) {
        nombrePais = npais
        url_Pais = nurl
        coordenadasPais = ncoordenadasPais
        iso2 = ncapital
    }

    companion object {
        @Throws(JSONException::class)
        fun JsonObjectsBuild(datos: JSONObject): ArrayList<Paises> {
            val paises: ArrayList<Paises> = ArrayList()
            val results = datos.getJSONObject("Results")
            val namesBD = results.names()
            try {
                for (i in 0 until namesBD.length()) {
                    val namebd = namesBD.getString(i)
                    val datosBD = results.getJSONObject(namebd)
                    val nombrePais = datosBD.getString("Name")
                    val countryCodes = datosBD.getJSONObject("CountryCodes")
                    val iso2 = countryCodes.getString("iso2")
                    var stringCapital: String
                    stringCapital = try {
                        val objcapital = datosBD.getJSONObject("Capital")
                        objcapital.getString("Name")
                    } catch (er: Exception) {
                        println(er.message + " " + er.cause)
                        "Vacio"
                    }
                    val georectangle = datosBD.getJSONObject("GeoRectangle")
                    val geopt = datosBD.getJSONArray("GeoPt")
                    val datosRectangulo = DoubleArray(6)
                    datosRectangulo[0] = georectangle.getDouble("West")
                    datosRectangulo[1] = georectangle.getDouble("East")
                    datosRectangulo[2] = georectangle.getDouble("North")
                    datosRectangulo[3] = georectangle.getDouble("South")
                    datosRectangulo[4] = geopt.getDouble(0)
                    datosRectangulo[5] = geopt.getDouble(1)
                    paises.add(
                        Paises(
                            nombrePais,
                            "http://www.geognos.com/api/en/countries/flag/$iso2.png",
                            datosRectangulo,
                            iso2
                        )
                    )
                }
            } catch (e: Exception) {
                println(e.message)
            }
            return paises
        }
    }
}
