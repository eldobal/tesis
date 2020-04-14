package com.example.practicadiseo;

import com.google.gson.annotations.SerializedName;

public class Usuario {


    private String RUT;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private String Contrasena;
    private String Fono;

    @SerializedName("id_idCiudad")
    private int idCiudad;
    @SerializedName("id_EstadoUsuario")
    private int idEstado;
    @SerializedName("id_TipoUsuario")
    private int idTipo;

    public String getRut() {
        return RUT;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public String getFono() {
        return Fono;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public int getIdTipo() {
        return idTipo;
    }
}
