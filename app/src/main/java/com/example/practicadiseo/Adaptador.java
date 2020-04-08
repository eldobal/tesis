package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//import com.example.practicadiseo.DetalleSolicitud;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    ArrayList<Solicitud> solicitudes;
    ArrayList<Solicitud> lista;
    int[] imagenes;
    Solicitud soli = new Solicitud();

    public Adaptador(Context contexto, ArrayList<Solicitud> solicitudes, int[] imagenes) {
        this.contexto = contexto;
        this.solicitudes = solicitudes;
        this.imagenes = imagenes;

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

        TextView nombreUsuario = (TextView) vista.findViewById(R.id.txtnombretrabajador);
        TextView fecha = (TextView) vista.findViewById(R.id.txtfechasolicitud);
        TextView nsolicitud = (TextView) vista.findViewById(R.id.txtnumerosolicitud);
        ImageView icono = (ImageView) vista.findViewById(R.id.imgperfilfilasolicitud);
        final Button detalle = (Button) vista.findViewById(R.id.btndetallesolicitud);

        nombreUsuario.setText(solicitudes.get(i).getSolicitadopor_Usuario());
        fecha.setText(solicitudes.get(i).getFecha());
     //   nsolicitud.setText("Pendiente");
        icono.setImageResource(imagenes[0]);


        soli.setIdSolicitud(solicitudes.get(i).getIdSolicitud());
        soli.setFecha(solicitudes.get(i).getFecha());
        soli.setDescripcion(solicitudes.get(i).getDescripcion());
        soli.setEstado_idEstado(solicitudes.get(i).getEstado_idEstado());
        soli.setSolicitadopor_Usuario(solicitudes.get(i).getSolicitadopor_Usuario());
        soli.setAtendidopor_Usuario(solicitudes.get(i).getAtendidopor_Usuario());

        final int posicion = i;
        detalle.setTag(i);
        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Solicitud s;
                s=solicitudes.get(posicion);
               // Intent vistaDetalle = new Intent(contexto, DetalleSolicitud.class);
               // vistaDetalle.putExtra("soli", s);
               // contexto.startActivity(vistaDetalle);
            }
        });

        return vista;
    }
}
