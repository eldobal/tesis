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

import com.example.practicadiseo.DetalleSolicitud;

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

        TextView nombreUsuario = (TextView) vista.findViewById(R.id.tvUsuario);
        TextView fecha = (TextView) vista.findViewById(R.id.tvFecha);
        TextView estado = (TextView) vista.findViewById(R.id.tvEstado);
        ImageView icono = (ImageView) vista.findViewById(R.id.ivIcono);
        final Button detalle = (Button) vista.findViewById(R.id.btnDetalle);

        nombreUsuario.setText(solicitudes.get(i).getSolicitadopor_Usuario());
        fecha.setText(solicitudes.get(i).getFecha());
        estado.setText("Pendiente");
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
                Intent vistaDetalle = new Intent(contexto, DetalleSolicitud.class);
                vistaDetalle.putExtra("soli", s);
                contexto.startActivity(vistaDetalle);
            }
        });

        return vista;
    }
}
