package com.example.practicadiseo.clases;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.bumptech.glide.Glide;
import com.example.practicadiseo.fragments.DetalleSolicitudFragment;
import com.example.practicadiseo.R;
import com.example.practicadiseo.interfaces.tesisAPI;

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
    SharedPreferences prefs;
    String rutusuario="",contrasena="";

    Solicitud soli = new Solicitud();


    public Adaptador(Context contexto, ArrayList<Solicitud> listasolicitudes) {
        this.contexto = contexto;
        this.listasolicitudes = listasolicitudes;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    //metodo el cual refresca el listview
    public void refresh(ArrayList<Solicitud> listasolicitudes){
        this.listasolicitudes = listasolicitudes;
        this.notifyDataSetChanged();
    }

    public void clearData() {  listasolicitudes.clear(); }

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

        prefs = contexto.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();

        //declaracion de la vista de cada item de la solicitud
        final View vista = inflater.inflate(R.layout.elemento_solicitud, null);
        TextView numerosolicitud = (TextView) vista.findViewById(R.id.txtfilanumerosolicitud);
        TextView fechasolicitud = (TextView) vista.findViewById(R.id.txtfilafechasolicitud);
        TextView estadosolicitud = (TextView) vista.findViewById(R.id.txtfilaestadosolicitudelemento);
        TextView nombretrabajador = (TextView) vista.findViewById(R.id.txtfilanombretrabajador);
       // TextView descripcion = (TextView) vista.findViewById(R.id.txtdescripciondetallesolicitud);
        ImageView fototrabajador = (ImageView) vista.findViewById(R.id.imgperfilfilasolicitud);
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
        //se carga la imagen del trabajor en la lista de los trabajadores desde la lista de solicitudes
        Glide.with(vista.getContext()).load(String.valueOf(listasolicitudes.get(i).getFotoT())).into(fototrabajador);

        final int posicion = i;
        detalle.setTag(i);

        if(listasolicitudes.get(i).getEstado().equals("ATENDIENDO") ) {

            detalle.setText("Detalle");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorAccent));
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
        }
        if(listasolicitudes.get(i).getEstado().equals("FINALIZADO") ) {

            detalle.setText("Detalle");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
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
        }

        if(listasolicitudes.get(i).getEstado().equals("COMPLETADA Y PAGADA")  ) {

            detalle.setText("Detalle");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorPrimary));
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
        }

        if(listasolicitudes.get(i).getEstado().equals("CONFIRMADA")){

            detalle.setText("Confirmar");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            detalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //alertdialog personalizado
                    AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                    View viewsync = inflater.inflate(R.layout.alertdialogsolicitudesconfirmada,null);
                    builder.setView(viewsync);
                    AlertDialog dialog3 = builder.create();
                    dialog3.show();
                    dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView textoalertnotificacion= (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                    textoalertnotificacion.setText("Si confirma esta solicitud el trabajador realizara el trabajo. Si cancela la solicitud se le notificara al trabajador y se eliminara esta solicitud de la lista.(PRECIO ESTIMADO "+listasolicitudes.get(i).getPrecio()+")");
                    Button btnconfirmar = viewsync.findViewById(R.id.btnconfirmarnotificacion);
                    Button btncancelar = viewsync.findViewById(R.id.btncancelarnotificacion);
                    Button dismiss = viewsync.findViewById(R.id.btnocultaralert);
                    //btn para aceptar la solicitud y confirmarla por completo
                    btnconfirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            final String Fechasolicitud = sdf.format(calendar.getTime());
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://proyectotesis.ddns.net/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
                            Call<String> call = tesisAPI.EstadoAtendiendo(listasolicitudes.get(i).getIdSolicitud(),rutusuario,contrasena);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(vista.getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                                    } else {
                                        //alertdialog personalizado
                                        AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                                        View viewsync = inflater.inflate(R.layout.alertdialogperfilactualizado,null);
                                        builder.setView(viewsync);
                                        AlertDialog dialog5 = builder.create();
                                        dialog5.setCancelable(false);
                                        dialog5.show();
                                        dialog5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                                        texto.setText("Felicitaciones Ha confirmado la Solicitud satisfactoriamente!");
                                        Button btncerraralert = viewsync.findViewById(R.id.btnalertperfilexito);

                                        btncerraralert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog5.dismiss();
                                                dialog3.dismiss();
                                                listasolicitudes.remove(i);
                                                refresh(listasolicitudes);
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(vista.getContext(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                    //btn para cancelar y eliminar la solicitud
                    btncancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://proyectotesis.ddns.net/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
                            Call<String> call2 = tesisAPI.CancelarSolicitud(listasolicitudes.get(i).getIdSolicitud(),rutusuario,contrasena);
                            call2.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call2, Response<String> response) {
                                    if(!response.isSuccessful()){
                                        Toast.makeText(vista.getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        listasolicitudes.remove(i);
                                        refresh(listasolicitudes);
                                        //alertdialog personalizado
                                        AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                                        View viewsync = inflater.inflate(R.layout.alertdialogperfilactualizado,null);
                                        builder.setView(viewsync);
                                        AlertDialog dialog4 = builder.create();
                                        dialog4.show();
                                        dialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                                        texto.setText("Felicitaciones Ha cancelado la solicitud satisfactoriamente!");
                                        Button btncerraralert = viewsync.findViewById(R.id.btnalertperfilexito);

                                        btncerraralert.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog4.dismiss();
                                                dialog3.dismiss();
                                            }
                                        });


                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call2, Throwable t) {
                                    Toast.makeText(vista.getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                    //btn para cerrar el cuadro explicativo
                    dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog3.dismiss();
                        }
                    });

                }
            });

        }if(listasolicitudes.get(i).getEstado().equals("PENDIENTE")) {
            detalle.setText("Cancelar");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            detalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //alertdialog personalizado
                    AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                    View viewsync = inflater.inflate(R.layout.alertdialogsolicitudespendiente,null);
                    builder.setView(viewsync);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView textoalertnotificacion= (TextView) viewsync.findViewById(R.id.txtalertnotificacion);

                    Button btncancelar = viewsync.findViewById(R.id.btncancelarnotificacion);
                    Button dismiss = viewsync.findViewById(R.id.btnocultaralert);

                    btncancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://proyectotesis.ddns.net/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
                            Call<String> call3 = tesisAPI.CancelarSolicitud(listasolicitudes.get(i).getIdSolicitud(),rutusuario,contrasena);
                            call3.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call3, Response<String> response) {
                                    if(!response.isSuccessful()){
                                        Toast.makeText(v.getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        listasolicitudes.remove(i);
                                        refresh(listasolicitudes);
                                        dialog.dismiss();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                                        View viewsync = inflater.inflate(R.layout.alerdialogsolicitudeliminada,null);
                                        builder.setView(viewsync);
                                        AlertDialog dialog2 = builder.create();
                                        dialog2.show();
                                        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        Button dismiss = viewsync.findViewById(R.id.btnalertperfilexito);

                                        dismiss.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog2.dismiss();
                                            }
                                        });



                                      /* solicitudeFragment solicitudeFragment = new solicitudeFragment();
                                        FragmentManager fm = ((AppCompatActivity) contexto).getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.replace(R.id.container, solicitudeFragment);
                                        ft.commit();*/
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call3, Throwable t) {
                                    Toast.makeText(v.getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                    dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });



                }
            });

        }if(listasolicitudes.get(i).getEstado().equals("FINALIZANDO")){

            detalle.setText("FINALIZANDO");
            detalle.setBackgroundDrawable(ContextCompat.getDrawable(vista.getContext(), R.drawable.bg_ripplecancelar) );
            numerosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            fechasolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            estadosolicitud.setTextColor(vista.getResources().getColor(R.color.colorAccent));
            nombretrabajador.setTextColor(vista.getResources().getColor(R.color.colorAccent));
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


        }




        return vista;
    }


    //metodo para traer el rut del usuario hacia la variable local
    private void setcredentiasexist() {
        String rut = getuserrutprefs();
        String contraseña = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(contraseña)) {
            rutusuario=rut.toString();
            contrasena=contraseña.toString();
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }

    private int getuseridciudadprefs() {
        return prefs.getInt("idCiudad", 0);
    }

    private String getusercontraseñaprefs() {
        return prefs.getString("ContraseNa", "");
    }

}
