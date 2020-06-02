package com.example.practicadiseo.clases;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.practicadiseo.R;
import com.example.practicadiseo.fragments.perfiltrabajadorFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class Adaptadortrabajadores extends BaseAdapter implements Serializable{

    private static LayoutInflater inflater = null;
    Context contexto;
    ArrayList<UsuarioTrabajador> Perfilestrabajadores;
    ArrayList<UsuarioTrabajador> lista;

    UsuarioTrabajador trabajadorlista = new UsuarioTrabajador();


    public Adaptadortrabajadores(Context contexto, ArrayList<UsuarioTrabajador> Perfilestrabajadores) {
        this.contexto = contexto;
        this.Perfilestrabajadores = Perfilestrabajadores;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }
    //metodo el cual se utiliza para actualizar la lista con los cambios
    public void refresh(ArrayList<UsuarioTrabajador> Perfilestrabajadores){
        this.Perfilestrabajadores = Perfilestrabajadores;
        this.notifyDataSetChanged();
    }

    public void clearData() {  Perfilestrabajadores.clear(); }


    @Override
    public int getCount() {
        return Perfilestrabajadores.size();
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
        final View vista = inflater.inflate(R.layout.elementoperfiltrabajador, null);
        //datos del elemento en el cual se cargaran los trabajadores
        //  TextView cliente = (TextView) vista.findViewById(R.id.txtclientesolicituddetalle);
        TextView nombretrabajador = (TextView) vista.findViewById(R.id.txtnombretrabajador);
        TextView ruttrabajador = (TextView) vista.findViewById(R.id.txtruttrabajador);
        final TextView calificaciontrabajador = (TextView) vista.findViewById(R.id.txtcalificaciontrabajador);
        TextView ciudadtrabajador = (TextView) vista.findViewById(R.id.txtciudadTrabajador);
        ImageView fotoperfil = (ImageView) vista.findViewById(R.id.imgperfiltrabajador);
        final Button perfil = (Button) vista.findViewById(R.id.btnperfiltrabajador);

        nombretrabajador.setText(Perfilestrabajadores.get(i).getNombre());
        ruttrabajador.setText("Rut:"+Perfilestrabajadores.get(i).getRUT());
        ciudadtrabajador.setText("Ciudad:"+Perfilestrabajadores.get(i).getCiudad());
        //se carga la foto del trabajador
        Glide.with(vista.getContext()).load(String.valueOf(Perfilestrabajadores.get(i).getFoto())).into(fotoperfil);

        //declaracion de la calificacion en forma de estrellas
        if(Perfilestrabajadores.get(i).getCalificacion().equals("5")){
                calificaciontrabajador.setText("Calificacion:★★★★★");
            }
        if(Perfilestrabajadores.get(i).getCalificacion().equals("4")){
                calificaciontrabajador.setText("Calificacion:★★★★");
            }
        if(Perfilestrabajadores.get(i).getCalificacion().equals("3")){
                calificaciontrabajador.setText("Calificacion:★★★");
            }
        if(Perfilestrabajadores.get(i).getCalificacion().equals("2")){
                calificaciontrabajador.setText("Calificacion:★★");
            }
        if(Perfilestrabajadores.get(i).getCalificacion().equals("1")){
                calificaciontrabajador.setText("Calificacion:★");
            }
        if(Perfilestrabajadores.get(i).getCalificacion().equals("0")){
                calificaciontrabajador.setText("No posee Calificacion");
            }

        final int idrubro = Perfilestrabajadores.get(i).getIdRubro();
        trabajadorlista.setNombre(Perfilestrabajadores.get(i).getNombre());
        trabajadorlista.setRubro(Perfilestrabajadores.get(i).getRubro());
        trabajadorlista.setIdCiudad(Perfilestrabajadores.get(i).getIdCiudad());
        trabajadorlista.setCalificacion(Perfilestrabajadores.get(i).getCalificacion());
        final int posicion = i;
        perfil.setTag(i);
        //boton sobre el detalle de una solicitud individual
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioTrabajador ut;
                ut=Perfilestrabajadores.get(posicion);
                Bundle bundle = new Bundle();
                bundle.putString("ruttrabajador", ut.getRUT());
                bundle.putInt("idrubro", ut.getIdRubro());
                perfiltrabajadorFragment perfiltrabajador = new perfiltrabajadorFragment();
                perfiltrabajador.setArguments(bundle);
                FragmentManager fm = ((AppCompatActivity) contexto).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, perfiltrabajador);
                ft.commit();
            }
        });
        return vista;
    }
}
