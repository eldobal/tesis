package com.example.practicadiseo.interfaces;

import com.example.practicadiseo.clases.Ciudad;
import com.example.practicadiseo.clases.Notificacion;
import com.example.practicadiseo.clases.Solicitud;
import com.example.practicadiseo.clases.SolicitudDb;
import com.example.practicadiseo.clases.Usuario;
import com.example.practicadiseo.clases.UsuarioTrabajador;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface tesisAPI {

    //metodo el cual llama a la api la cual entrega un usario /usando actualemente para login

    @GET("api/UsuarioAPI")
    Call<Usuario> getLogin(@Query("id") String id,
                           @Query("pass") String pass
    );

    //llamada que se utiliza en el perfilfragment del usuario
    @GET("api/UsuarioAPI")
    Call<Usuario> getUsuario(@Query("RUTUSUARIO") String id,
                             @Query("Contrasena") String pass
    );

    //api que se ocupa en la listabuscarrubro
    @GET("api/RubroTrabajadorAPI")
    Call<List<UsuarioTrabajador>> getRubroTrabajador(@Query("idRubro") int id_Rubro,
                                                     @Query("idciudad") int idCiudad,
                                                     @Query("RUT") String rut,
                                                     @Query("Contrasena") String contrasena
    );


    //api para el perfiltrabajador
    @GET("api/UsuarioAPI")
    Call<UsuarioTrabajador> getUsuarioTrabajador(@Query("RUT") String rut,
                                                 @Query("RUTU") String rutusuario,
                                                 @Query("Contrasena") String contrasena
    );


    //metodo para traer las notificaciones del cliente que esta ocupando la app
    @GET("api/NotificacionAPI")
    Call<List<Notificacion>> getNotificacion(@Query("RUT") String rut,
                                             @Query("Contrasena") String contrasena
    );


    //metodo llamada para hacer el insert de la solicitud del cliente
    @POST("api/SolicitudAPI")
    Call<SolicitudDb> PostSolicitud(@Query("Fecha") String Fecha,
                                    @Query("Descripcion") String Descripcion,
                                    @Query("RUT_Cliente") String RUT_Cliente,
                                    @Query("RUT_Trabajador") String RUT_Trabajador,
                                    @Query("Rubro") int Rubro,
                                    @Query("latitud") String latitud,
                                    @Query("longitud") String longitud,
                                    @Query("Contrasena") String contrasenacliente,
                                    @Body() String foto
    );


    //api que se ocupa en registrar cliente
    @POST("api/UsuarioAPI")
    Call<Usuario> PostUsuario(@Query("RUT") String RUT,
                              @Query("Nombre") String Nombre,
                              @Query("Apellido") String Apellido,
                              @Query("Correo") String Correo,
                              @Query("Contrasena") String Contrasena,
                              @Query("Fono") String Fono,
                              @Query("id_idCiudad") int id_idCiudad,
                              @Query("id_EstadoUsuario") int id_EstadoUsuario,
                              @Query("id_TipoUsuario") int id_TipoUsuario,
                              @Body() String foto
    );


    //api que se ocupa en el adptador cuando esta confirmada
    @POST("api/SolicitudAPI")
    Call<String> EstadoAtendiendo(@Query("idSolicitud") int idsolicitud,
                                  @Query("RUTU") String rutusuario,
                                  @Query("Contrasena") String contrasena

    );



    //api que se ocupoa en actualizar el perfil
    @POST("api/UsuarioAPI")
    Call<Usuario> ActualizarUsuario(@Query("RUT") String RUT,
                              @Query("Nombre") String Nombre,
                              @Query("Apellido") String Apellido,
                              @Query("Correo") String Correo,
                              @Query("Fono") String Fono,
                              @Query("id_idCiudad") int id_idCiudad,
                                    @Query("Contrasena") String contrasena
    );


    //api para cambiar la contraseña del cliente
    @POST("api/UsuarioAPI")
    Call<Usuario> UsuarioPass(@Query("RUT") String RUT,
                              @Query("Contrasena") String Contrasena,
                              @Query("Contrasenaantigua") String Contrasenaantigua
    );

    //metodo el cual trae el listado de ciudades / usando acutalmente para cargar el spiner de ciudades en el registrar usaurio
    @GET("api/CiudadAPI/")
    Call<List<Ciudad>> getCiudades();


    //metodo para cancelar la solicitud del cliente
    @POST("api/SolicitudAPI")
    Call<String> CancelarSolicitud(@Query("idcancelarC") int idSolicitud,
                                   @Query("RUTU") String rutusuario,
                                   @Query("Contrasena") String contrasena
    );


    //api que se ocuapa en listar las solicitudes
    @GET("api/SolicitudAPI")
    Call<List<Solicitud>> getSolicitudes(@Query("RUT") String rut,
                                         @Query("Contrasena") String contrasena
    );

    //api que se ocupa en detalle
    @GET("api/SolicitudAPI")
    Call<Solicitud> getSolicitudCliente(@Query("id") int id,
                                        @Query("RUTU") String rutusuario,
                                        @Query("Contrasena") String contrasena
                                        );

    //metodo para cancelar la solicitud del cliente
    @POST("api/SolicitudAPI")
    Call<String> EliminarSoliPermanente(@Query("Delete") int idSolicitud,
                                        @Query("RUTU") String rutusuario,
                                        @Query("Contrasena") String contrasena
    );

    //metodo para confirmar si el pago esta correcto
    @POST("api/SolicitudAPI")
    Call<String> pagarCliente(@Query("RUT") String rut,
                               @Query("Contrasena") String contrasena,
                               @Query("idSolicitud") int idsolicitud,
                               @Query("metodoPago") String metodopago,
                              @Query("calificacion") int calificacion,
                              @Query("comentario") String comentario
    );

    //metodo para cancelar la solicitud del cliente
    @POST("api/SolicitudAPI")
    Call<String> borrarNotificacion(@Query("RUT") String rutusuario,
                                        @Query("Contrasena") String contrasena,
                                    @Query("Notificacion") int idnotificacion

    );


}
