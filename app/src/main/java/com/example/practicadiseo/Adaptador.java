package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.practicadiseo.DetalleSolicitudFragment;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adaptador extends BaseAdapter implements Serializable {

    private static LayoutInflater inflater = null;
    SweetAlertDialog dp;
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
        fechasolicitud.setText("Fecha: "+listasolicitudes.get(i).getFechaS());
        estadosolicitud.setText(listasolicitudes.get(i).getEstado());


        nombretrabajador.setText(listasolicitudes.get(i).getNombre()+" "+listasolicitudes.get(i).getApellido());
        //icono.setImageResource(imagenes[0]);

        soli.setIdSolicitud(listasolicitudes.get(i).getIdSolicitud());
        soli.setFechaS(listasolicitudes.get(i).getFechaS());
        soli.setEstado(listasolicitudes.get(i).getEstado());
        soli.setNombre(listasolicitudes.get(i).getNombre()+" "+listasolicitudes.get(i).getApellido());


        final int posicion = i;
        detalle.setTag(i);

        if(!listasolicitudes.get(i).getEstado().equals("PENDIENTE")) {
            //boton sobre el detalle de una solicitud individual
            detalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Solicitud ut;
                    ut = listasolicitudes.get(posicion);
                    Bundle bundle = new Bundle();
                    //id de la solicitud para que se pueda buscar en el detalle
                    bundle.putInt("idsolicitud", idsolicitud);
                    DetalleSolicitudFragment detalleSolicitudFragment = new DetalleSolicitudFragment();
                    detalleSolicitudFragment.setArguments(bundle);
                    FragmentManager fm = ((AppCompatActivity) contexto).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, detalleSolicitudFragment);
                    ft.commit();
                }
            });
        }else {
            detalle.setText("Cancelar");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            detalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dp=   new SweetAlertDialog(vista.getContext(), SweetAlertDialog.WARNING_TYPE);
                    dp.setTitleText("Desea Eliminar la Solicitud?");
                    dp.setContentText("Esta Solicitud sera eliminada de tu lista");
                    dp.setConfirmText("Si!");
                    dp.setCancelText("No!");
                    dp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://proyectotesis.ddns.net/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
                            Call<Solicitud> call = tesisAPI.CancelarSolicitud(listasolicitudes.get(i).getIdSolicitud());
                            call.enqueue(new Callback<Solicitud>() {
                                @Override
                                public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                                    if(!response.isSuccessful()){
                                        Toast.makeText(v.getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        solicitudeFragment solicitudeFragment = new solicitudeFragment();
                                        FragmentManager fm = ((AppCompatActivity) contexto).getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.replace(R.id.container, solicitudeFragment);
                                        ft.commit();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Solicitud> call, Throwable t) {
                                    Toast.makeText(v.getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            sDialog.dismissWithAnimation();

                        }
                    })    .show();
                 dp.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                            .show();

                }
            });

        }



        return vista;
    }


}
