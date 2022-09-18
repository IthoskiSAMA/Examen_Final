package app.ij.mlwithtensorflowlite
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

import javax.annotation.Nonnull

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val coordenadas: DoubleArray
    private var mMap: GoogleMap? = null
    private var nPais: TextView? = null
    private var infoPais: TextView? = null
    private var imgPais: ImageView? = null
    private var distanciaPaises: TextView? = null
    var paises: Paises? = null
    private val fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        paises = Paises()
        nPais = findViewById(R.id.infoPais_txt)
        infoPais = findViewById(R.id.infotextPais)
        imgPais = findViewById(R.id.imagenPais)
        distanciaPaises = findViewById(R.id.distanciatxt)
        val bundle = this.intent.extras
        nPais.setText(bundle!!.getString("Pais"))
        val supportMapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.mapPais) as SupportMapFragment?
        supportMapFragment.getMapAsync(this)
    }

    fun onMapReady(@Nonnull googleMap: GoogleMap?) {
        mMap = googleMap
        set_map(mMap)
    }

    private fun set_map(mp: GoogleMap?) {
        Glide.with(this.applicationContext).load(intent.getStringExtra("url_imagen")).into(imgPais)
        mp.getUiSettings().setZoomControlsEnabled(true)
        mp.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        nPais!!.text = intent.getStringExtra("Pais")
        val coordenadas = intent.getDoubleArrayExtra("coordenadas")
        val PaisInfo = """
            Pais: ${intent.getStringExtra("Pais")}
            Url bandera: ${intent.getStringExtra("url_imagen")}
            Iso2: ${intent.getStringExtra("iso2")}
            Coordenadas: 
            WEST: ${coordenadas!![0]}
            EAST: ${coordenadas[1]}
            NORTH: ${coordenadas[2]}
            SOUTH${coordenadas[3]}
            """.trimIndent()
        infoPais!!.text = PaisInfo
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(
                coordenadas[4],
                coordenadas[5]
            ), 5
        )
        mp.moveCamera(cameraUpdate)
        val polylineOptions: PolylineOptions = PolylineOptions()
            .add(LatLng(coordenadas[2], coordenadas[0])) //NOR OESTE
            .add(LatLng(coordenadas[2], coordenadas[1])) //NOR ESTE
            .add(LatLng(coordenadas[3], coordenadas[1])) //SUR ESTE
            .add(LatLng(coordenadas[3], coordenadas[0])) // SUR OESTE
            .add(LatLng(coordenadas[2], coordenadas[0])) // NOR OESTE
        polylineOptions.width(8)
        polylineOptions.color(Color.RED)
        mp.addPolyline(polylineOptions)
        val location = Location("localizacion 1")
        location.setLatitude(coordenadas[4])
        location.setLongitude(coordenadas[5])
        val location1 = Location("localizacion 2")
        location1.setLatitude(-1.01121)
        location1.setLongitude(-79.444893)
        val distancia: Double = location.distanceTo(location1)
        distanciaPaises!!.text = "Distancia= $distancia"
        val polydistancia: PolylineOptions = PolylineOptions()
            .add(LatLng(coordenadas[4], coordenadas[5]))
            .add(LatLng(-1.01121, -79.444893))
        polydistancia.width(7)
        polydistancia.color(Color.YELLOW)
        mp.addPolyline(polydistancia)
    }
}