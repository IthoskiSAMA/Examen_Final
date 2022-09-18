package app.ij.mlwithtensorflowlite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorPaises extends ArrayAdapter<Paises> {
    public AdaptadorPaises(Context context, ArrayList<Paises> datos) {
        super(context, R.layout.ly_item, datos);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemData = inflater.inflate(R.layout.ly_item, null);

        TextView lblPais = (TextView)itemData.findViewById(R.id.lblNombre);
        TextView lbliso2 = (TextView)itemData.findViewById(R.id.lblEmail);
        TextView lblurlpais = (TextView)itemData.findViewById(R.id.lblweb);

        ImageView imageView = (ImageView)itemData.findViewById(R.id.imgUsr);

        //seleccionar el pais para mostrar la info
        itemData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemData.getContext(), MapsActivity.class);
                intent.putExtra("Pais",getItem(position).getNombrePais());
                intent.putExtra("url_imagen",getItem(position).getUrl_Pais().toString());
                intent.putExtra("iso2",getItem(position).getIso2().toString());
                intent.putExtra("coordenadas",getItem(position).getCoordenadasPais());
                itemData.getContext().startActivity(intent);
            }
        });

        Glide.with(this.getContext())
                .load(getItem(position).getUrl_Pais())
                .into(imageView);

        //.error(R.drawable.imgnotfound)



        lblPais.setText(getItem(position).getNombrePais());
        lbliso2.setText(getItem(position).getIso2());
        lblurlpais.setText(getItem(position).getUrl_Pais());



        return(itemData);
    }
}
