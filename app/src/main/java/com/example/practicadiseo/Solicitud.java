package com.example.practicadiseo;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;

public class Solicitud{

    private int idSolicitud;
    private String Rubro;
    private String Nombre;
    private String Apellido;
    private String FechaS;
    private String RUT;
    private int Precio;
    private String metodoPago;
    private String FechaA;
    private String estado;
    private String DescripcionP;
    private String Diagnostico;
    private String Solucion;
    private int IdFoto;





    private ArrayList<Solicitud> lista = new ArrayList<Solicitud>();


    public Solicitud() {
        this.idSolicitud= idSolicitud;
        this.FechaS= FechaS;
        this.Rubro=Rubro;
        this.DescripcionP= DescripcionP;
        this.RUT= RUT;
        this.Precio= Precio;
        this.IdFoto= IdFoto;
        this.metodoPago=metodoPago;
        this.FechaA=FechaA;
        this.Diagnostico=Diagnostico;
        this.Solucion=Solucion;
        this.estado= estado;
        this.Nombre= Nombre;
        this.Apellido= Apellido;

    }


    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getRubro() {
        return Rubro;
    }

    public void setRubro(String rubro) {
        Rubro = rubro;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getFechaS() {
        return FechaS;
    }

    public void setFechaS(String fechaS) {
        FechaS = fechaS;
    }

    public String getRUT() {
        return RUT;
    }

    public void setRUT(String RUT) {
        this.RUT = RUT;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getFechaA() {
        return FechaA;
    }

    public void setFechaA(String fechaA) {
        FechaA = fechaA;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcionP() {
        return DescripcionP;
    }

    public void setDescripcionP(String descripcionP) {
        DescripcionP = descripcionP;
    }

    public String getDiagnostico() {
        return Diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        Diagnostico = diagnostico;
    }

    public String getSolucion() {
        return Solucion;
    }

    public void setSolucion(String solucion) {
        Solucion = solucion;
    }

    public int getIdFoto() {
        return IdFoto;
    }

    public void setIdFoto(int idFoto) {
        IdFoto = idFoto;
    }

    public ArrayList<Solicitud> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Solicitud> lista) {
        this.lista = lista;
    }
}

