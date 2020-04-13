package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.practicadiseo.DetalleSolicitudFragment;

public class Adaptador extends BaseAdapter implements Serializable {

    private static LayoutInflater inflater = null;

    Context contexto;
    ArrayList<Solicitud> solicitudes;
    ArrayList<Solicitud> lista;
    int[] imagenes;
    Solicitud soli = new Solicitud();


    public Adaptador(Context contexto, ArrayList<Solicitud> solicitudes) {
        this.contexto = contexto;
        this.solicitudes = solicitudes;


        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return solicitudes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.elemento_solicitud, null);

        final



      //  TextView cliente = (TextView) vista.findViewById(R.id.txtclientesolicituddetalle);
        TextView trabajador = (TextView) vista.findViewById(R.id.txtclientesolicituddetalle);
        TextView fecha = (TextView) vista.findViewById(R.id.txtfechasolicitud);
        final TextView nsolicitud = (TextView) vista.findViewById(R.id.txtnumerosolicitud);
       // TextView descripcion = (TextView) vista.findViewById(R.id.txtdescripciondetallesolicitud);

        ImageView icono = (ImageView) vista.findViewById(R.id.imgperfilfilasolicitud);


        final Button detalle = (Button) vista.findViewById(R.id.btndetallesolicitud);

        trabajador.setText(solicitudes.get(i).getRut_Trabajador());
        fecha.setText(solicitudes.get(i).getFecha());
        nsolicitud.setText(solicitudes.get(1).getIdSolicitud());
        icono.setImageResource(imagenes[0]);


        soli.setIdSolicitud(solicitudes.get(i).getIdSolicitud());
        soli.setFecha(solicitudes.get(i).getFecha());
        soli.setDescripcion(solicitudes.get(i).getDescripcion());
        soli.setRut_Trabajador(solicitudes.get(i).getRut_Trabajador());


        final int posicion = i;
        detalle.setTag(i);





        //boton sobre el detalle de una solicitud individual
        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Solicitud s;
                s=solicitudes.get(posicion);
                DetalleSolicitudFragment detalleSolicitudFragment = new DetalleSolicitudFragment();

                Bundle bundle = new Bundle();

                Intent intent = new Intent(contexto, DetalleSolicitudFragment.class);

                bundle.putInt("nsolicitud", Integer.parseInt(nsolicitud.getText().toString()));
                detalleSolicitudFragment.setArguments(bundle);



                contexto.startActivity(intent);


            }
        });

        return vista;
    }


}
