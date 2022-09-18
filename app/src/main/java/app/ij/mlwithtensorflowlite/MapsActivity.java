package app.ij.mlwithtensorflowlite;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.annotation.Nonnull;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private double[] coordenadas;
    private GoogleMap mMap;
    private TextView nPais;
    private TextView infoPais;
    private ImageView imgPais;
    private TextView distanciaPaises;
    Paises paises;

    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        paises= new Paises();

        nPais= findViewById(R.id.infoPais_txt);
        infoPais=findViewById(R.id.infotextPais);
        imgPais=findViewById(R.id.imagenPais);
        distanciaPaises=findViewById(R.id.distanciatxt);
        Bundle bundle = this.getIntent().getExtras();

        nPais.setText(bundle.getString("Pais"));
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapPais);

        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@Nonnull  GoogleMap googleMap){
        mMap= googleMap;
        set_map(mMap);
    }

    private void set_map(GoogleMap mp) {
        Glide.with( this.getApplicationContext()).load(getIntent().getStringExtra("url_imagen")).into(imgPais);

        mp.getUiSettings().setZoomControlsEnabled(true);
        mp.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        nPais.setText(getIntent().getStringExtra("Pais"));

        double [] coordenadas = getIntent().getDoubleArrayExtra("coordenadas");

        String PaisInfo = "Pais: " + getIntent().getStringExtra("Pais") + "\n" +
                "Url bandera: " + getIntent().getStringExtra("url_imagen") + "\n" +
                "Iso2: " + getIntent().getStringExtra("iso2") + "\n" +
                "Coordenadas: " + "\n" +
                "WEST: " +coordenadas[0] + "\n" +
                "EAST: " + coordenadas[1] + "\n" +
                "NORTH: " + coordenadas[2] + "\n" +
                "SOUTH" + coordenadas[3];
        infoPais.setText(PaisInfo);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(coordenadas[4],coordenadas[5]), 5);
        mp.moveCamera(cameraUpdate);

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(new LatLng(coordenadas[2], coordenadas[0])) //NOR OESTE
                .add(new LatLng(coordenadas[2], coordenadas[1])) //NOR ESTE
                .add(new LatLng(coordenadas[3], coordenadas[1]))  //SUR ESTE
                .add(new LatLng(coordenadas[3], coordenadas[0]))  // SUR OESTE
                .add(new LatLng(coordenadas[2], coordenadas[0]));  // NOR OESTE
        polylineOptions.width(8);
        polylineOptions.color(Color.RED);
        mp.addPolyline(polylineOptions);

        Location location = new Location("localizacion 1");
        location.setLatitude(coordenadas[4]);
        location.setLongitude(coordenadas[5]);
        Location location1 = new Location("localizacion 2");
        location1.setLatitude(-1.01121);
        location1.setLongitude(-79.444893);
        double distancia = location.distanceTo(location1);
        distanciaPaises.setText("Distancia= "+distancia);

        PolylineOptions polydistancia = new PolylineOptions()
                .add(new LatLng(coordenadas[4],coordenadas[5]))
                .add(new LatLng(-1.01121,-79.444893));
        polydistancia.width(7);
        polydistancia.color(Color.YELLOW);
        mp.addPolyline(polydistancia);


    }
}