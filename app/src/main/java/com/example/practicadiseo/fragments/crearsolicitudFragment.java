package com.example.practicadiseo.fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicadiseo.R;
import com.example.practicadiseo.activitys.menuActivity;
import com.example.practicadiseo.clases.SolicitudDb;
import com.example.practicadiseo.interfaces.tesisAPI;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class crearsolicitudFragment extends Fragment implements Serializable {

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    private  Bitmap bitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    SharedPreferences prefs,prefsmaps;
    SweetAlertDialog dp,pDialog;
    ImageView fotosacada,imgperfil;
    EditText descripcion;
    private int idrubro=0;
    private RelativeLayout relativeLayoutfoto;
    Button btnfoto,btncrearsolicitud,cargar,btnmapa;
    private String ruttrabajador="",rutcliente="",nombretrabajador="",estadotrabajador="",calificaciontrabajador="",descripcionfinal="",rutafoto="",imagenstring="",Fechasolicitud="";
    private TextView rut,nombre,estado,calificacion,textomapaseleccionado;
    //declaracion de la ruta estatica del servidor
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    int idestadosolicitud=0;
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    String latorigen="",longorigen="",contrasena="",rutusuario="";
    NetworkInfo activeNetwork;
    ConnectivityManager cm ;
    AlertDialog dialog;
    public crearsolicitudFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //declarar todos los componentes de la vistas
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        prefsmaps = this.getActivity().getSharedPreferences("ubicacionmapa", Context.MODE_PRIVATE);

        saveOnPreferences("","");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crearsolicitud, container, false);
        //se verifica si existe el rut de usuario y su contraseña
        setcredentiasexist();
        rut =(TextView) v.findViewById(R.id.txtrutcrearsolicitudtrabajador);
        nombre =(TextView) v.findViewById(R.id.txtnombrecrearsolicitudtrabajador);
        estado =(TextView) v.findViewById(R.id.txtestadocrearsolicitudtrabajador);
        calificacion =(TextView) v.findViewById(R.id.txtcalificacioncrearsolicitudtrabajador);
        textomapaseleccionado =(TextView) v.findViewById(R.id.txtseleccionado);
        imgperfil=(ImageView) v.findViewById(R.id.imgperfil);
        cargar = (Button) v.findViewById(R.id.btncargarfoto);
        btncrearsolicitud = (Button) v.findViewById(R.id.btncrearsolicitud);
        btnmapa=(Button) v.findViewById(R.id.btnmapa);
        fotosacada = (ImageView) v.findViewById(R.id.fotocrearsolicitud);
        descripcion =(EditText) v.findViewById(R.id.txtdescripcioncrearsolicitud);
        relativeLayoutfoto = (RelativeLayout) v.findViewById(R.id.relativefoto);

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

        if(!rutcliente.isEmpty() && !contrasena.isEmpty()){
            rut.setText("Rut: "+ruttrabajador);
            nombre.setText(nombretrabajador);
            estado.setText("Estado: "+estadotrabajador);
            //se setean las estrellas dependiendo de la calificacion
            setestrellas(calificaciontrabajador);
            Glide.with(getContext()).load(String.valueOf(rutaservidor+rutafoto)).into(imgperfil);

            descripcionfinal= descripcion.getText().toString();
            //formato del calendario el cual toma la fecha actual.
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Fechasolicitud = sdf.format(calendar.getTime());
            setlatlongexist();

            if(!imagenstring.isEmpty()){
                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "La imagen se encuentra en el imagenstring ", Snackbar.LENGTH_LONG);
                snackBar.show();
                StringToBitMap(imagenstring);

            }




            final Handler handler = new Handler();
            Timer timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if(latorigen.isEmpty() && latorigen.isEmpty()){
                                //Ejecuta tu AsyncTask!
                                // reiniciarfragment(rutusuario, contrasena);
                                setlatlongexist();
                            }
                        }
                    });
                }
            };
            timer.schedule(task, 0, 1000);  //ejecutar en intervalo definido por el programador



            if(!latorigen.isEmpty() && !latorigen.isEmpty()){
                textomapaseleccionado.setVisibility(View.VISIBLE);
                textomapaseleccionado.setText("La Ubicacion ha sido Seleccionada");
            }

            btncrearsolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setlatlongexist();
                    descripcionfinal= descripcion.getText().toString();
                    //se hace la validacion si se ha escojido la direccion
                    if(!latorigen.isEmpty() && !longorigen.isEmpty() ){
                        //validacion si es que la imagen y la descripcion estan vacios
                        if(descripcionfinal.isEmpty()){
                            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "Ingrese una descripcion / cargue una foto", Snackbar.LENGTH_LONG);
                            snackBar.show();
                        }else{
                            //se verifica que la descripcion tenga una extencion de 300 caracteres maximos
                            if(descripcionfinal.length() < 300) {

                                btncrearsolicitud.setClickable(false);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = getLayoutInflater();
                                View viewsync = inflater.inflate(R.layout.alerdialogloading, null);
                                builder.setView(viewsync);
                                dialog = builder.create();
                                dialog.setCancelable(false);
                                dialog.show();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                //metodo para crear la solicitud con los parametros
                                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                activeNetwork = cm.getActiveNetworkInfo();
                                if (activeNetwork != null) {
                                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    crearsolicitud();
                                }else{
                                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                            "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                                    snackBar.show();
                                }
                            }
                            }else{
                                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "La descripcion no debe tener mas de 300 caracteristicas", Snackbar.LENGTH_LONG);
                                snackBar.show();
                            }

                        }
                    }else{
                        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Seleccione la ubicacion donde se realizara el trabajo", Snackbar.LENGTH_LONG);
                        snackBar.show();
                    }

                }
            });

            //metodo el cual carga el fragment de mapa
            btnmapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSelectedFragment(new mapaFragment());
                }
            });

            //metodo para cargar la foto
            cargar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llamarintent();
                }
            });

        }else{
            updateDetail();
        }


        return v;
    }

    //metodo el cual verifica la version del so para crear el canal
    private void createNotificationChannel(){
        //se verifica que el SO sea igual o superior a oreo
        //si es superior crea el notification chanel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //metodo para crear la notificacion personalizada
    private void crearnotificacion() {
        //se instancia el builder para crear la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);
        //se declaran las propiedades y atributos
        builder.setSmallIcon(R.drawable.userprofile);
        builder.setContentTitle("Solicitud Creada Exitosamene!");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.CYAN, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        //texto para mostrar de forma exancible
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("La solicitud se ha creado exitosamente, el " +nombretrabajador+
                ". sera notificado de inmediato, porfavor este atento a la respuesta" +
                " podra confirmar la solicitud desde el apartado mis solicitudes"));
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        //se instancia la notificacion
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    private void crearsolicitud(){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
            //falta pasar el bitmap de la imagen sacada en el post hacia el web api
            Call<SolicitudDb> call1 = tesisAPI.PostSolicitud(Fechasolicitud,descripcionfinal,rutcliente,ruttrabajador,idrubro,latorigen,longorigen,contrasena,imagenstring);
            call1.enqueue(new Callback<SolicitudDb>() {
                @Override
                public void onResponse(Call<SolicitudDb> call1, Response<SolicitudDb> response) {
                    if (!response.isSuccessful()) {
                        //alert informativo
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View viewsync = inflater.inflate(R.layout.alerdialogerrorresponce,null);
                        builder.setView(viewsync);
                        AlertDialog dialog2 = builder.create();
                        dialog2.setCancelable(false);
                        dialog2.show();
                        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                        texto.setText("Ha ocurrido un error con la respuesta al tratar de crear la solicitu. intente en un momento nuevamente.");
                        Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                        btncerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                updateDetail();
                                dialog2.dismiss();
                            }
                        });

                     //   Toast.makeText(getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        SolicitudDb solicitud = response.body();
                        saveOnPreferences("","");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View viewsync = inflater.inflate(R.layout.alertdialogcrearsolicitud,null);
                        builder.setView(viewsync);
                        AlertDialog dialog3 = builder.create();
                        dialog3.setCancelable(false);
                        dialog3.show();
                        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button solicitudcreada = viewsync.findViewById(R.id.btncrearsolicitudexito);

                        solicitudcreada.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createNotificationChannel();
                                crearnotificacion();
                                updateDetail();
                                dialog3.dismiss();
                            }
                        });

                    }
                }
                @Override
                public void onFailure(Call<SolicitudDb> call1, Throwable t) {
                                       AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View viewsync = inflater.inflate(R.layout.alerdialogerrorservidor,null);
                    builder.setView(viewsync);
                    AlertDialog dialog4 = builder.create();
                    dialog4.setCancelable(false);
                    dialog4.show();
                    dialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView texto = (TextView) viewsync.findViewById(R.id.txterrorservidor);
                    texto.setText("Ha ocurrido un error con la coneccion del servidor, Estamos trabajando para solucionarlo.");
                    Button btncerrar =(Button) viewsync.findViewById(R.id.btncerraralert);

                    btncerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });

                  //  Toast.makeText(getContext(), "EL ERROR ESTA ACA: "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    updateDetail();
                }
            });
    }

    private void llamarintent() {
        //se crea un alertdialog para que
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog01,null);
        builder.setView(view);
        AlertDialog dialog5 = builder.create();
        dialog5.show();
        dialog5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btntomarfoto = view.findViewById(R.id.alertbtntomarfoto);
        Button btncargarfoto = view.findViewById(R.id.alertbtncargarfoto);
        Button btncancelar = view.findViewById(R.id.alertbtncancelar);

        btncargarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);
                dialog5.dismiss();
            }
        });

        btntomarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                dialog5.dismiss();
            }
        });

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog5.dismiss();
            }
        });


        /*
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
        alertOpciones.show();     */

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
            relativeLayoutfoto.setVisibility(View.VISIBLE);
        }
        //si la opcion es cargar la foto desde el dispocitivo
                if(requestCode == 10){
            Uri path= data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                fotosacada.setImageBitmap(bitmap);
                relativeLayoutfoto.setVisibility(View.VISIBLE);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //se carga y convierte la foto la cual es usuario cargo/tomo desde su celular
        imagenstring = convertirimgstring(bitmap);
    }

    private void showSelectedFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
    }

    //Metodo en el cual se recive un bitmap lo comprime y lo transforma en base64
    private String convertirimgstring(Bitmap bitmap){
        if(bitmap==null){
            //retorna un vacio si esque el bitmat no tiene una foto dentro
            return "";
        }else {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, array);
            byte[] Imagenbyte = array.toByteArray();
            String imagenString = Base64.encodeToString(Imagenbyte, Base64.DEFAULT);
            return imagenString;
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            fotosacada.setImageBitmap(bitmap);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void saveOnPreferences(String latitud, String longitud) {
        SharedPreferences.Editor editor = prefsmaps.edit();

        //putDoublelatitud(prefsmaps.edit(),"latitud",latitud);
        //putDoublelongirud(prefsmaps.edit(),"longitud",longitud);
        editor.putString("Latitud",latitud.toString());
        editor.putString("Longitud",longitud.toString());
        //linea la cual guarda todos los valores en la pref antes de continuar
        editor.commit();
        editor.apply();
    }

    //metodo para traer el rut del usuario hacia la variable local
    private void setcredentiasexist() {
        String rut = getuserrutprefs();
        String contraseña = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(contraseña)) {
            rutcliente=rut.toString();
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

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), menuActivity.class);
        startActivity(intent);
    }

    private void setlatlongexist() {
        String latitud = getlatitud();
        String longitud = getlongitud();
    if (!TextUtils.isEmpty(latitud)  && !TextUtils.isEmpty(longitud) ) {
            latorigen=latitud;
            longorigen=longitud;
        }
    }

    private String getlatitud() {
        return prefsmaps.getString("Latitud", "");
    }

    private String getlongitud() {
        return prefsmaps.getString("Longitud", "");
    }

    private void  setestrellas(String calificaciona) {
        if(calificaciona.equals("5")){
            calificacion.setText("Calificacion: ★★★★★");
        }
        if(calificaciona.equals("4")){
            calificacion.setText("Calificacion: ★★★★");
        }
        if(calificaciona.equals("3")){
            calificacion.setText("Calificacion: ★★★");
        }
        if(calificaciona.equals("2")){
            calificacion.setText("Calificacion: ★★");
        }
        if(calificaciona.equals("1")){
            calificacion.setText("Calificacion: ★");
        }
        if(calificaciona.equals("0")){
            calificacion.setText("Calificacion: No posee Calificacion");
        }
    }


}
