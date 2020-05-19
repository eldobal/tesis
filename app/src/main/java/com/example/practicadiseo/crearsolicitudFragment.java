package com.example.practicadiseo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


/**
 * A simple {@link Fragment} subclass.
 */
public class crearsolicitudFragment extends Fragment {

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    private  Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SharedPreferences prefs;
    SweetAlertDialog dp;
    ImageView fotosacada,imgperfil;
    EditText descripcion;
    private int idrubro=0;
    Button btnfoto,btncrearsolicitud;
    private String ruttrabajador="",rutcliente="",nombretrabajador="",estadotrabajador="",calificaciontrabajador="",descripcionfinal="",rutafoto="",imagenstring="";
    private TextView rut,nombre,estado,calificacion;
    //declaracion de la ruta estatica del servidor
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    int idestadosolicitud=0;



    public crearsolicitudFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
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
        imgperfil=(ImageView) v.findViewById(R.id.imgperfil);

        Bundle args = getArguments();
        if (args == null) {
            // No hay datos, manejar excepción
        }else{
            //se cargan los datos enviados del trabajador
            ruttrabajador= args.getString("ruttrabajador");
            nombretrabajador = args.getString("nombretrabajador");
            estadotrabajador = args.getString("estadotrabajador");
            calificaciontrabajador = args.getString("calificaciontrabajador");
            idrubro = args.getInt("idrubro");
            rutafoto = args.getString("imgperfiltrabajador");
        }

        rut.setText("Rut: "+ruttrabajador);
        nombre.setText(nombretrabajador);
        estado.setText(estadotrabajador);
        calificacion.setText(calificaciontrabajador);
        Glide.with(getContext()).load(String.valueOf(rutaservidor+rutafoto)).into(imgperfil);
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
                imagenstring = convertirimgstring(bitmap);
                descripcionfinal= descripcion.getText().toString();
                //se hace la validacion
                if(imagenstring.isEmpty()||descripcionfinal.isEmpty()){
                    //no se ha cargado una foto o la descripcion
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Ingrese una descripcion / cargue una foto", Snackbar.LENGTH_LONG);
                    snackBar.show();
                }else{
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://proyectotesis.ddns.net/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
                    //falta pasar el bitmap de la imagen sacada en el post hacia el web api
                    Call<Solicitud> call1 = tesisAPI.PostSolicitud(Fechasolicitud,descripcionfinal,rutcliente,ruttrabajador,idrubro,imagenstring);
                    call1.enqueue(new Callback<Solicitud>() {
                        @Override
                        public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                                updateDetail();
                            } else {
                                Solicitud solicitud = response.body();
                                btncrearsolicitud.setClickable(false);
                                dp=  new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                dp.setTitleText("Solicitud Creada!");
                                dp.show();
                                new CountDownTimer(1500,1000){
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    @Override
                                    public void onFinish() {
                                        updateDetail();
                                    }
                                }.start();
                            }
                        }
                        @Override
                        public void onFailure(Call<Solicitud> call, Throwable t) {
                        }
                    });
                }
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


    private void llamarintent() {
        //se crea un alertdialog para que
        final CharSequence[] opciones ={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones =new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");

        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }else{
                    //en esta parte del codigo va el metodo /codigo para cargar la foto desde la galeria
                    if(opciones[i].equals("Cargar Imagen")){
                        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);
                    }else{
                        dialog.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();


    }

    //metodo con el cual se capturara el evento luego de seleccionar la opcion en el alert dialog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //si la opcion es tomarla en el mismo momento
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            fotosacada.setImageBitmap(bitmap);
        }
        //si la opcion es cargar la foto desde el dispocitivo
        if(requestCode == 10){
            Uri path= data.getData();
            fotosacada.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                fotosacada.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    //Metodo en el cual se recive un bitmap lo comprime y lo transforma en base64
    private String convertirimgstring(Bitmap bitmap){
        if(bitmap==null){
            //retorna un vacio si esque el bitmat no tiene una foto dentro
            return "";
        }else {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
            byte[] Imagenbyte = array.toByteArray();
            String imagenString = Base64.encodeToString(Imagenbyte, Base64.DEFAULT);
            return imagenString;
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
