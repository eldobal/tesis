package com.example.practicadiseo.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.practicadiseo.clases.Ciudad;
import com.example.practicadiseo.R;
import com.example.practicadiseo.clases.Usuario;
import com.example.practicadiseo.interfaces.tesisAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class register2Activity extends AppCompatActivity {
    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private ArrayAdapter<Ciudad> adapter;
    private EditText txtrut,txtemail,txtcontraseña,txtcontraseña2,txtnombre,txtapellidos,txttelefono,txtdireccion;
    private Spinner spinnerciudades;
    private Button btnregistrar,btncargarfoto;
    private int posicion = 0;
    private List<Ciudad> listaciudades;
    NetworkInfo networkInfo;
    private int idCiudad =0;
    private  Bitmap bitmap;
    private ImageView fotosacada;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imagenstring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
        //listausuarios = new ArrayList<Usuario>();
        txtrut = (EditText) findViewById(R.id.rut) ;
        txtemail = (EditText) findViewById(R.id.email);
        txtcontraseña = (EditText) findViewById(R.id.password);
        txtcontraseña2 = (EditText) findViewById(R.id.password2);
        txtnombre = (EditText) findViewById(R.id.nombre);
        txtapellidos = (EditText) findViewById(R.id.apellido);
        txttelefono = (EditText) findViewById(R.id.telefono);
        spinnerciudades = (Spinner) findViewById(R.id.spinner);
        btnregistrar=(Button)findViewById(R.id.registrarse);
        btncargarfoto =(Button)findViewById(R.id.btncargarimgperfil);
        fotosacada = (ImageView)findViewById(R.id.imgperfil);

        if (networkInfo != null && networkInfo.isConnected()) {
            //carga las ciudades en el spinner
            cargarspiner();
        }else{
            //manejar exepcion
        }



        spinnerciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Ciudad ciudad = (Ciudad) parent.getSelectedItem();
                    displayciudaddata(ciudad);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //validaciones hechas con awesome validation
     // mAwesomeValidation.addValidation(this, R.id.rut, , R.string.err_rut);
        //se valida como +569 y el numero de 8 digitos del usuario
        mAwesomeValidation.addValidation(this, R.id.telefono, "^[+]?[0-9]{10,13}$", R.string.err_fono);
        mAwesomeValidation.addValidation(this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_correo);
        mAwesomeValidation.addValidation(this, R.id.nombre, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.apellido, "[a-zA-Z\\s]+", R.string.err_apellido);
        //validacion contraseñas con alto nivel de dificultad
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        mAwesomeValidation.addValidation(this, R.id.password, regexPassword, R.string.err_contraseña);
        mAwesomeValidation.addValidation(this, R.id.password2, regexPassword, R.string.err_contraseña);


        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAwesomeValidation.validate()) {
                    if(validaRut(txtrut.getText().toString())) {
                        if ((txtcontraseña.getText().toString().equals(txtcontraseña2.getText().toString()))) {
                            if (networkInfo != null && networkInfo.isConnected()) {
                            String RUT = txtrut.getText().toString();
                            String Correo = txtemail.getText().toString();
                            String Nombre = txtnombre.getText().toString();
                            String Apellido = txtapellidos.getText().toString();
                            String Fono = txttelefono.getText().toString();
                            String Contrasena = txtcontraseña2.getText().toString();
                            Integer id_idCiudad = idCiudad;
                            Integer id_EstadoUsuario = 2;
                            Integer id_TipoUsuario = 2;
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://proyectotesis.ddns.net/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
                            //metodo para llamar a la funcion que queramos
                            Call<Usuario> call = tesisAPI.PostUsuario(RUT, Nombre, Apellido, Correo, Contrasena, Fono, id_idCiudad, id_EstadoUsuario, id_TipoUsuario,imagenstring);
                            try {
                                call.enqueue(new Callback<Usuario>() {
                                    @Override
                                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                        if (!response.isSuccessful()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(register2Activity.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View viewsync = inflater.inflate(R.layout.alertdialogusuarioexistente,null);
                                            builder.setView(viewsync);
                                            AlertDialog dialog = builder.create();
                                            dialog.setCancelable(false);
                                            dialog.show();
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            Button btnusuarioexiste = viewsync.findViewById(R.id.btnusuarioexiste);

                                            btnusuarioexiste.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    txtrut.setText("");
                                                    txtapellidos.setText("");
                                                    txtemail.setText("");
                                                    txtcontraseña.setText("");
                                                    txtcontraseña2.setText("");
                                                    txttelefono.setText("");
                                                    dialog.dismiss();
                                                }
                                            });

                                           // Toast.makeText(getApplicationContext(), "error code " + response.code(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Usuario usuarioresponce = response.body();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(register2Activity.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View viewsync = inflater.inflate(R.layout.alertdialogperfilactualizado,null);
                                            builder.setView(viewsync);
                                            AlertDialog dialog2 = builder.create();
                                            dialog2.setCancelable(false);
                                            dialog2.show();
                                            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                                            texto.setText("Felicidades ha creado de forma exitosa su perfil. sera redigido al login para que pueda iniciar sesion");
                                            Button btnusuarioexiste = viewsync.findViewById(R.id.btnusuarioexiste);

                                            btnusuarioexiste.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    txtrut.setText("");
                                                    txtapellidos.setText("");
                                                    txtemail.setText("");
                                                    txtcontraseña.setText("");
                                                    txtcontraseña2.setText("");
                                                    txttelefono.setText("");
                                                    dialog2.dismiss();
                                                    Intent intent = new Intent(register2Activity.this, login2Activity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Usuario> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "error code " + t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }else{
                                //manejar excepcion

                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Complete los campos Correctamente", Toast.LENGTH_LONG).show();
                        }




                    }else{
                        Toast.makeText(getApplicationContext(), "El Rut No es valido ", Toast.LENGTH_LONG).show();
                    }

           }else {
                  Toast.makeText(getApplicationContext(), "error validate", Toast.LENGTH_LONG).show();
               }



            }
        });





        btncargarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarintent();
            }
        });


    }

    /*private boolean validaciones(String temail,String tcontrasena,String tnombre,String tapellidos,String tfono,String tcorreo,int tdireccion){
        if (!temail.isEmpty() && !tcontrasena.isEmpty() && !tnombre.isEmpty() && !tapellidos.isEmpty() && !tcorreo.isEmpty() && tdireccion && !tfono.isEmpty()) {
            if (tfono.length() == 9) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Inserte 9 digitos en telefono ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }
*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //si la opcion es tomarla en el mismo momento
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            fotosacada.setImageBitmap(bitmap);

        }
        //si la opcion es cargar la foto desde el dispocitivo
        if(requestCode == 10){
            Uri path= data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),path);
                fotosacada.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //se carga y convierte la foto la cual es usuario cargo/tomo desde su celular
        imagenstring = convertirimgstring(bitmap);
    }

    private void llamarintent() {
        //se crea un alertdialog para que
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),10);
                dialog5.dismiss();
            }
        });

        btntomarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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




    private String getuserusuairoprefs(){

        return prefs.getString("usuario","");
    }

    private boolean validaciontelefono(String telefono){
        if(telefono.length()==9){

        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(register2Activity.this, login2Activity.class);
        startActivity(intent);

        super.onBackPressed();
    }

    //request para traer los datos desde la pag u cargarlos en un spinner
    private void cargarspiner(){
        try {
            ArrayList<Ciudad> listaciudades = new ArrayList<Ciudad>();
            ArrayList<String> listanombres = new ArrayList<String>();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);

            //metodo para llamar a la funcion que queramos
            Call<List<Ciudad>> call = tesisAPI.getCiudades();
            call.enqueue(new Callback<List<Ciudad>>() {
                @Override
                public void onResponse( Call<List<Ciudad>>call, Response<List<Ciudad>>response) {

                    //si esta malo se ejecuta este trozo
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                    }
                    //de lo contrario se ejecuta esta parte
                    else {
                        //respuesta del request
                        List<Ciudad> ciudades = response.body();

                        //declaracion de variables del response
                        for(Ciudad ciudad: ciudades){
                            //falta poder cargar la lista del response hacia un spinner
                            Ciudad ciudadl = new Ciudad();
                            ciudadl.setNombre(ciudad.getNombre()) ;
                            ciudadl.setIdCiudad(ciudad.getIdCiudad());
                            ciudadl.setId_idComuna(ciudad.getId_idComuna());

                            //problema con cargar los datos a la lista
                            listaciudades.add(ciudadl);
                        }

                        //cargar la lista de ciudades rescatadas en el spinner
                        ArrayAdapter<Ciudad> a = new ArrayAdapter<Ciudad>(register2Activity.this,android.R.layout.simple_spinner_item,listaciudades);
                        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerciudades.setAdapter(a);


                    }
                }

                //si falla el request a la pagina mostrara este error
                @Override
                public void onFailure(Call<List<Ciudad>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getSelectedCiudad(View v){
        Ciudad ciudad = (Ciudad) spinnerciudades.getSelectedItem();
        displayciudaddata(ciudad);
    }

    //metodo para validar el rut de la persona
    public static Boolean validaRut ( String rut ) {
        Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$");
        Matcher matcher = pattern.matcher(rut);
        if ( matcher.matches() == false ) return false;
        String[] stringRut = rut.split("-");
        return stringRut[1].toLowerCase().equals(register2Activity.dv(stringRut[0]));
    }

    public static String dv ( String rut ) {
        Integer M=0,S=1,T=Integer.parseInt(rut);
        for (;T!=0;T=(int) Math.floor(T/=10))
            S=(S+T%10*(9-M++%6))%11;
        return ( S > 0 ) ? String.valueOf(S-1) : "k";
    }

    //muestra los datos de la ciudad especifica selelcionada
    private void displayciudaddata(Ciudad ciudad){
        idCiudad = ciudad.getIdCiudad();
        String Nombre = ciudad.getNombre();
        int id_idComuna= ciudad.getId_idComuna();
        String ciudaddata ="ID ciudad :"+ idCiudad + " nombre ciudad"+Nombre + " id comuna: "+id_idComuna;
    }


}
