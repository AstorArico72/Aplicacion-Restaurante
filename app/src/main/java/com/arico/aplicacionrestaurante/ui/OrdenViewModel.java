package com.arico.aplicacionrestaurante.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.util.ClienteApi;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdenViewModel extends androidx.lifecycle.AndroidViewModel {
    private Context ContextoAplicacion;
    public OrdenViewModel(@NonNull Application application) {
        super(application);
        this.ContextoAplicacion = application.getApplicationContext();
    }

    public void EnviarOrden (List<FilaOrden> filas) {
        Gson ListaGson = new Gson();
        String FilasGson = ListaGson.toJson(filas);
        ClienteApi.InterfazApi api = ClienteApi.ConseguirApi();
        String token = ClienteApi.LeerToken(ContextoAplicacion);
        Call<String> llamada = api.NuevaOrden(token, FilasGson);
        llamada.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d ("RespuestaHTTP", response.toString());
                if (response.code() == 204) {
                    Toast.makeText(ContextoAplicacion, "Orden enviada.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}
