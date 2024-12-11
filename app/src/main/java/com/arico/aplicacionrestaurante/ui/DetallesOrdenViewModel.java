package com.arico.aplicacionrestaurante.ui;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.arico.aplicacionrestaurante.modelos.Artículo;
import com.arico.aplicacionrestaurante.util.ClienteApi;

import java.io.IOException;

import retrofit2.Call;

public class DetallesOrdenViewModel extends androidx.lifecycle.AndroidViewModel{
    private Context ContextoAplicacion;
    private ClienteApi.InterfazApi api;
    private String token;
    public DetallesOrdenViewModel(@NonNull Application application) {
        super(application);
        this.ContextoAplicacion = application.getApplicationContext();
        api = ClienteApi.ConseguirApi();
        token = ClienteApi.LeerToken(ContextoAplicacion);
    }

    public int LeerPrecio (int id, int cantidad) throws IOException {
        Call<Artículo> llamada = api.DetallesArticulo(token, id);
        Artículo item = llamada.execute().body();
        return item.getPrecio() * cantidad;
    }
}
