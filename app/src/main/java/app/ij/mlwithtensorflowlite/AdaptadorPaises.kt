package app.ij.mlwithtensorflowlite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList

class AdaptadorPaises(context: Context?, datos: ArrayList<Paises?>?) :
    ArrayAdapter<Paises?>(context, R.layout.ly_item, datos) {
    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val itemData: View = inflater.inflate(R.layout.ly_item, null)
        val lblPais = itemData.findViewById(R.id.lblNombre) as TextView
        val lbliso2 = itemData.findViewById(R.id.lblEmail) as TextView
        val lblurlpais = itemData.findViewById(R.id.lblweb) as TextView
        val imageView: ImageView = itemData.findViewById(R.id.imgUsr) as ImageView

        //seleccionar el pais para mostrar la info
        itemData.setOnClickListener(object : OnClickListener() {
            fun onClick(v: View?) {
                val intent = Intent(itemData.getContext(), MapsActivity::class.java)
                intent.putExtra("Pais", getItem(position).getNombrePais())
                intent.putExtra("url_imagen", getItem(position).getUrl_Pais().toString())
                intent.putExtra("iso2", getItem(position).getIso2().toString())
                intent.putExtra("coordenadas", getItem(position).getCoordenadasPais())
                itemData.getContext().startActivity(intent)
            }
        })
        Glide.with(this.context)
            .load(getItem(position).getUrl_Pais())
            .into(imageView)

        //.error(R.drawable.imgnotfound)
        lblPais.setText(getItem(position).getNombrePais())
        lbliso2.setText(getItem(position).getIso2())
        lblurlpais.setText(getItem(position).getUrl_Pais())
        return itemData
    }
}