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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.practicadiseo.DetalleSolicitudFragment;

public class Adaptador extends BaseAdapter implements Serializable {

    private static LayoutInflater inflater = null;

    Context contexto;
    ArrayList<Solicitud> listasolicitudes;
    ArrayList<Solicitud> lista;

    Solicitud soli = new Solicitud();


    public Adaptador(Context contexto, ArrayList<Solicitud> listasolicitudes) {
        this.contexto = contexto;
        this.listasolicitudes = listasolicitudes;


        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listasolicitudes.size();
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

        //declaracion de la vista de cada item de la solicitud
        final View vista = inflater.inflate(R.layout.elemento_solicitud, null);


         TextView numerosolicitud = (TextView) vista.findViewById(R.id.txtfilanumerosolicitud);
        TextView fechasolicitud = (TextView) vista.findViewById(R.id.txtfilafechasolicitud);
        TextView estadosolicitud = (TextView) vista.findViewById(R.id.txtfilaestadosolicitudelemento);
        TextView nombretrabajador = (TextView) vista.findViewById(R.id.txtfilanombretrabajador);

       // TextView descripcion = (TextView) vista.findViewById(R.id.txtdescripciondetallesolicitud);
        //solucionar el tema de las imagenes de los trabajadores
        ImageView icono = (ImageView) vista.findViewById(R.id.imgperfilfilasolicitud);


        final Button detalle = (Button) vista.findViewById(R.id.btndetallesolicitud);

        int idsolicitud = listasolicitudes.get(i).getIdSolicitud();

        numerosolicitud.setText("N Solicitud: "+String.valueOf(listasolicitudes.get(i).getIdSolicitud()));
        fechasolicitud.setText(listasolicitudes.get(i).getFechaS());
        estadosolicitud.setText(listasolicitudes.get(i).getEstado());
        nombretrabajador.setText(listasolicitudes.get(i).getNombre()+" "+listasolicitudes.get(i).getApellido());
        //icono.setImageResource(imagenes[0]);

        soli.setIdSolicitud(listasolicitudes.get(i).getIdSolicitud());
        soli.setFechaS(listasolicitudes.get(i).getFechaS());
        soli.setEstado(listasolicitudes.get(i).getEstado());
        soli.setNombre(listasolicitudes.get(i).getNombre()+" "+listasolicitudes.get(i).getApellido());


        final int posicion = i;
        detalle.setTag(i);


        //boton sobre el detalle de una solicitud individual
        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Solicitud ut;

                ut=listasolicitudes.get(posicion);
                Bundle bundle = new Bundle();
                //id de la solicitud para que se pueda buscar en el detalle
                bundle.putInt("idsolicitud",idsolicitud);
                DetalleSolicitudFragment detalleSolicitudFragment = new DetalleSolicitudFragment();
                detalleSolicitudFragment.setArguments(bundle);
                FragmentManager fm = ((AppCompatActivity) contexto).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, detalleSolicitudFragment);
                ft.commit();
            }
        });

        return vista;
    }


}
