package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class crearsolicitudFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SharedPreferences prefs;
    SweetAlertDialog dp;
    ImageView fotosacada;
    EditText descripcion;
    private int idrubro=0;
    Button btnfoto,btncrearsolicitud;
    private String ruttrabajador="",rutcliente="",nombretrabajador="",estadotrabajador="",calificaciontrabajador="",descripcionfinal="";
    private TextView rut,nombre,estado,calificacion;
    int idestadosolicitud=0;
    public crearsolicitudFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crearsolicitud, container, false);
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        rut =(TextView) v.findViewById(R.id.txtrutcrearsolicitudtrabajador);
        nombre =(TextView) v.findViewById(R.id.txtnombrecrearsolicitudtrabajador);
        estado =(TextView) v.findViewById(R.id.txtestadocrearsolicitudtrabajador);
        calificacion =(TextView) v.findViewById(R.id.txtcalificacioncrearsolicitudtrabajador);
        Bundle args = getArguments();
        if (args == null) {
            // No hay datos, manejar excepción
        }else{
            ruttrabajador= args.getString("ruttrabajador");
            nombretrabajador = args.getString("nombretrabajador");
            estadotrabajador = args.getString("estadotrabajador");
            calificaciontrabajador = args.getString("calificaciontrabajador");
            idrubro = args.getInt("idrubro");

        }

        rut.setText("Rut: "+ruttrabajador);
        nombre.setText(nombretrabajador);
        estado.setText(estadotrabajador);
        calificacion.setText(calificaciontrabajador);

        setcredentiasexist();
        final Button cargar = (Button) v.findViewById(R.id.btncargarfoto);
        final Button btncrearsolicitud = (Button) v.findViewById(R.id.btncrearsolicitud);


         fotosacada = (ImageView) v.findViewById(R.id.fotocrearsolicitud);


         descripcion =(EditText) v.findViewById(R.id.txtdescripcioncrearsolicitud);

        descripcionfinal= descripcion.getText().toString();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String Fechasolicitud = sdf.format(calendar.getTime());



        btncrearsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descripcionfinal= descripcion.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://proyectotesis.ddns.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
                Call<Solicitud> call1 = tesisAPI.PostSolicitud(Fechasolicitud,descripcionfinal,rutcliente,ruttrabajador,idrubro);

                call1.enqueue(new Callback<Solicitud>() {
                    @Override
                    public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                        } else {
                            Solicitud solicitud = response.body();

                            dp =new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                            dp.setTitleText("Solicitud Creada");
                            dp.setContentText("El Trabajador Debera aprobar tu Solicitu mantengase atend@");
                            dp.setConfirmText("ok Volver a menu!");
                            dp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                             updateDetail();
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();

                        }
                    }
                    @Override
                    public void onFailure(Call<Solicitud> call, Throwable t) { }
                });

            }
        });



        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarintent();
            }
        });

        return v;
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                fotosacada.setImageBitmap(imageBitmap);
            }
        }



    private void llamarintent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void setcredentiasexist() {
        String rutq = getuserrutprefs();
        if (!TextUtils.isEmpty(rutq)) {
            rutcliente=rutq.toString();
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }


    public void updateDetail() {
        Intent intent = new Intent(getActivity(), menuActivity.class);
        startActivity(intent);
    }
}
