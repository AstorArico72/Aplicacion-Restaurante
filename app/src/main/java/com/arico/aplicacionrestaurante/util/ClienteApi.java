package com.arico.aplicacionrestaurante.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.modelos.Artículo;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Las operaciones de API del lado del cliente se hacen aquí.
 */

public class ClienteApi {

    public static final String UrlBase = "http://192.168.1.150:5179";

    /**
     * Ésto crea una interfaz Retrofit para poder hacer pedidos al API.
     *
     * @return Instancia de la clase InterfazApi
     * @see InterfazApi
     */
    public static InterfazApi ConseguirApi () {
        Gson NotAJson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.150:5179/Api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(NotAJson))
                .build();
        return retrofit.create(InterfazApi.class);
    }

    /**
     * Ésto guarda el JSON Web Token traido del API.
     *
     * @param token JWT Bearer token guardado en las preferencias compartidas.
     * @param applicationContext Contexto de la aplicación, traido de la vista principal.
     */
    public static void GuardarToken (String token, Context applicationContext) {
        SharedPreferences preferences = applicationContext.getSharedPreferences("PreferenciasCompartidas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TokenAcceso", token);
        editor.apply();
    }

    /**
     * Ésto lee el JSON Web Token del archivo de preferencias compartidas.
     *
     * @param applicationContext Contexto de la aplicación, traido de la vista principal
     * @return El JSON Web Token, si no hay uno, retorna null.
     * @see SharedPreferences
     */
    @Nullable
    public static String LeerToken (@NonNull Context applicationContext) {
        SharedPreferences preferences = applicationContext.getSharedPreferences("PreferenciasCompartidas", Context.MODE_PRIVATE);
        return preferences.getString("TokenAcceso", null);
    }

    public static void BorrarToken (Context applicationContext) {
        SharedPreferences preferences = applicationContext.getSharedPreferences("PreferenciasCompartidas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("TokenAcceso");
        editor.apply();
    }

    public interface InterfazApi {
        /**
         * Ésto conecta al servidor, permitiendo hacer otros pedidos al API.
         *
         * @param token JWT Bearer para la autorización, si es aplicable.
         * @return Llamada retrofit.
         * @see retrofit2.Call
         */
        @GET("Conectarse/ConexionCliente")
        Call<String> HabilitarMesa (@Nullable @Header("Authorization") String token);
        @GET("Conectarse/ConseguirIdMesa")
        Call<Integer> ConseguirId (@Header("Authorization") String token);

        /**
         * Ésto trae todos los artículos del menú, para poder mostrarlos en la aplicación.
         *
         * @param token JWT Bearer para la autorización.
         * @return Llamada retrofit con objetos Artículo
         * @see retrofit2.Call
         * @see Artículo
         */
        @GET("Articulos/Menu")
        Call<List<Artículo>> ObtenerMenu (@Header("Authorization") String token);
        @Multipart
        @POST("Ordenes/Nueva")
        Call<String> NuevaOrden (@Header("Authorization") String token, @Part("filas") String filas);
        @GET("Articulos/Detalles/{id}")
        Call<Artículo> DetallesArticulo (@Header("Authorization") String token, @Path(value = "id") int id);
    }
}
