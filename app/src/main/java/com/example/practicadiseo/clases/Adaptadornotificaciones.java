package com.example.practicadiseo.clases;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.practicadiseo.R;
import com.example.practicadiseo.interfaces.tesisAPI;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adaptadornotificaciones  extends BaseAdapter implements Serializable {

    SweetAlertDialog dp;
    private static LayoutInflater inflater = null;
    Context contexto;
    ArrayList<Notificacion> listanotificaciones;
    SharedPreferences prefs;
    String rutusuario="",contrasena="";


    Notificacion notificacion = new Notificacion();


    public Adaptadornotificaciones(Context contexto, ArrayList<Notificacion> listanotificaciones) {
        this.contexto = contexto;
        this.listanotificaciones = listanotificaciones;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    //metodo el cual se utiliza para actualizar la lista con los cambios
    public void refresh(ArrayList<Notificacion> listanotificaciones) {
        this.listanotificaciones = listanotificaciones;
        this.notifyDataSetChanged();
    }

    //metodo el cual limpia la lista con los elementos que tenga dentro
    public void clearData() {
        listanotificaciones.clear();
    }

    @Override
    public int getCount() {
        return listanotificaciones.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.elementonotificacion, null);

        prefs = contexto.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();
        //datos del elemento en el cual se cargaran los trabajadores
        //  TextView cliente = (TextView) vista.findViewById(R.id.txtclientesolicituddetalle);
        CardView card = (CardView) vista.findViewById(R.id.cardnotificacion);
        TextView mensaje = (TextView) vista.findViewById(R.id.mensajenotificacion);

        mensaje.setText(listanotificaciones.get(i).getMensaje());

        notificacion.setId(listanotificaciones.get(i).getId());
        notificacion.setMensaje(listanotificaciones.get(i).getMensaje());
        notificacion.setRUT(listanotificaciones.get(i).getRUT());
        notificacion.setIdSolicitud(listanotificaciones.get(i).getIdSolicitud());
        final int posicion = i;
        card.setTag(i);



            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String textocomparar = "Solicitud " +listanotificaciones.get(i).getIdSolicitud()+ " fue cancelada";


                    if (listanotificaciones.get(i).getMensaje().equals(textocomparar)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View viewsync = inflater.inflate(R.layout.alernotificacioncancelada, null);
                        builder.setView(viewsync);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView textoalertnotificacion = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                        Button dismiss = viewsync.findViewById(R.id.btnocultaralert2);
                        textoalertnotificacion.setText("La notificacion con el id: " + notificacion.getId() + " ha sido cancelada por el cliente" +
                                "lo cual significa que la solitud se ha eliminado ");

                        dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://proyectotesis.ddns.net/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
                                Call<String> call = tesisAPI.EliminarSoliPermanente(listanotificaciones.get(i).getIdSolicitud(),rutusuario,contrasena);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (!response.isSuccessful()) {
                                            Toast.makeText(v.getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                                        } else {
                                            dialog.dismiss();
                                            listanotificaciones.remove(i);
                                            refresh(listanotificaciones);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(v.getContext(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });

                    }


                    if (!listanotificaciones.get(i).getMensaje().equals(textocomparar)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                    View viewsync = inflater.inflate(R.layout.alertdialogsolicitudesconfirmada, null);
                    builder.setView(viewsync);
                    AlertDialog dialog3 = builder.create();
                    dialog3.show();
                    dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView textoalertnotificacion = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
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
                            Call<String> call = tesisAPI.EstadoAtendiendo(listanotificaciones.get(i).getIdSolicitud(),rutusuario,contrasena);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(vista.getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                                    } else {
                                        listanotificaciones.remove(i);
                                        refresh(listanotificaciones);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                                        View viewsync = inflater.inflate(R.layout.alertdialogperfilactualizado, null);
                                        builder.setView(viewsync);
                                        AlertDialog dialog5 = builder.create();
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
                            Call<String> call2 = tesisAPI.CancelarSolicitud(listanotificaciones.get(i).getIdSolicitud(),rutusuario,contrasena);
                            call2.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call2, Response<String> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(vista.getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                                    } else {
                                        listanotificaciones.remove(i);
                                        refresh(listanotificaciones);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(vista.getContext());
                                        View viewsync = inflater.inflate(R.layout.alertdialogperfilactualizado, null);
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
                                    Toast.makeText(vista.getContext(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
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
                }
            });




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
