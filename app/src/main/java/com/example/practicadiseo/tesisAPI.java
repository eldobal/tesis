package com.example.practicadiseo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface tesisAPI {

    //metodo el cual llama a la api la cual entrega un usario /usando actualemente para login
    @GET("api/UsuarioAPI")
    Call<Usuario> getUsuario(@Query("id") String id,
                                   @Query("pass") String pass
    );



    @POST("api/UsuarioAPI")
    Call<Usuario> PostUsuario(@Query("RUT") String RUT,
                              @Query("Nombre") String Nombre,
                              @Query("Apellido") String Apellido,
                              @Query("Correo") String Correo,
                              @Query("Contrasena") String Contrasena,
                              @Query("Fono") String Fono,
                              @Query("id_idCiudad") int id_idCiudad,
                              @Query("id_EstadoUsuario") int id_EstadoUsuario,
                              @Query("id_TipoUsuario") int id_TipoUsuario
    );

    @POST("api/UsuarioAPI")
    Call<Usuario> ActualizarUsuario(@Query("RUT") String RUT,
                              @Query("Nombre") String Nombre,
                              @Query("Apellido") String Apellido,
                              @Query("Correo") String Correo,
                              @Query("Fono") String Fono,
                              @Query("id_idCiudad") int id_idCiudad
    );

    @POST("api/UsuarioAPI")
    Call<Usuario> UsuarioPass(@Query("RUT") String RUT,
                              @Query("Contrasena") String Contrasena

    );

    //metodo el cual trae el listado de ciudades / usando acutalmente para cargar el spiner de ciudades en el registrar usaurio
    @GET("api/CiudadAPI/")
    Call<List<Ciudad>> getCiudades();


    @GET("api/SolicitudAPI/")
    Call<List<Solicitud>> getSolicitudes(@Query("rut") String rut

    );
}
