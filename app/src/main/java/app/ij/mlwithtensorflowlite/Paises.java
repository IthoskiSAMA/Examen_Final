package app.ij.mlwithtensorflowlite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Paises implements Serializable {
    private String nombrePais;
    private String url_Pais;
    private double [] coordenadasPais;
    private String iso2;
    private double [] geo_pt;

    public Paises() {
    }

    public double[] getGeo_pt() {
        return geo_pt;
    }

    public void setGeo_pt(double[] geo_pt) {
        this.geo_pt = geo_pt;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getUrl_Pais() {
        return url_Pais;
    }

    public void setUrl_Pais(String url_Pais) {
        this.url_Pais = url_Pais;
    }

    public Paises(String npais, String nurl, double [] ncoordenadasPais, String ncapital) throws JSONException {
        nombrePais = npais;
        url_Pais = nurl;
        coordenadasPais=ncoordenadasPais;
        iso2 =ncapital;

    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public static ArrayList<Paises> JsonObjectsBuild(JSONObject datos) throws JSONException {
        ArrayList<Paises> paises = new ArrayList<>();

        JSONObject results=datos.getJSONObject("Results");
        JSONArray namesBD=results.names();

        try {
            for (int i = 0; i < namesBD.length(); i++) {

                String namebd= namesBD.getString(i);
                JSONObject datosBD=results.getJSONObject(namebd);
                String nombrePais=datosBD.getString("Name");
                JSONObject countryCodes=datosBD.getJSONObject("CountryCodes");
                String iso2=countryCodes.getString("iso2");
                String stringCapital;
                try {
                    JSONObject objcapital = datosBD.getJSONObject("Capital");
                    stringCapital = objcapital.getString("Name");
                } catch (Exception er) {
                    System.out.println(er.getMessage() + " " + er.getCause());
                    stringCapital = "Vacio";
                }

                JSONObject georectangle=datosBD.getJSONObject("GeoRectangle");
                JSONArray geopt=datosBD.getJSONArray("GeoPt");

                double [] datosRectangulo = new double[6];
                datosRectangulo[0]=georectangle.getDouble("West");
                datosRectangulo[1]=georectangle.getDouble("East");
                datosRectangulo[2]=georectangle.getDouble("North");
                datosRectangulo[3]=georectangle.getDouble("South");

                datosRectangulo[4]=geopt.getDouble(0);
                datosRectangulo[5]=geopt.getDouble(1);



                paises.add(new Paises(nombrePais,"http://www.geognos.com/api/en/countries/flag/"+iso2+".png",datosRectangulo, iso2));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return paises;
    }


    public double [] getCoordenadasPais() {
        return coordenadasPais;
    }

    public void setCoordenadasPais(double [] coordenadasPais) {
        this.coordenadasPais = coordenadasPais;
    }
}
