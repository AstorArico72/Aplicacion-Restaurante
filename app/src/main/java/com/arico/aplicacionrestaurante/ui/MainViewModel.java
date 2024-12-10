package com.arico.aplicacionrestaurante.ui;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.util.ClienteApi;
import com.arico.aplicacionrestaurante.modelos.Artículo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    private Context ContextoAplicacion;
    public static MutableLiveData <Integer> ImporteOrden;
    public static MutableLiveData <List<FilaOrden>> Orden;
    public static MutableLiveData <FilaOrden> FilaNueva;

    public static MutableLiveData<FilaOrden> getNuevaFila() {
        if (FilaNueva == null) {
            FilaNueva = new MutableLiveData<>();
        }
        return FilaNueva;
    }

    public static MutableLiveData <List <FilaOrden>> LeerOrden () {
        if (Orden == null) {
            Orden = new MutableLiveData<>();
        }
        return Orden;
    }

    public static MutableLiveData <Integer> ConseguirImporte () {
        if (ImporteOrden == null) {
            ImporteOrden = new MutableLiveData<>();
        }
        return ImporteOrden;
    }

    private MutableLiveData<List<Artículo>> MutableListaArticulos;
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.ContextoAplicacion = application.getApplicationContext();
    }

    public MutableLiveData <List <Artículo>> ConseguirArticulos () {
        if (MutableListaArticulos == null) {
            MutableListaArticulos = new MutableLiveData<>();
        }
        return MutableListaArticulos;
    }

    public void Conectarse () {
        ClienteApi.InterfazApi api = ClienteApi.ConseguirApi();

        @Nullable String TokenAcceso = ClienteApi.LeerToken(ContextoAplicacion);
        Call<String> llamada = api.HabilitarMesa(TokenAcceso);
        llamada.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i ("RespuestaHTTP", response.toString());
                if (response.code() == 200) {
                    Toast.makeText(ContextoAplicacion, "Generando token de acceso.", Toast.LENGTH_LONG).show();
                    ClienteApi.GuardarToken("Bearer " + response.body(), ContextoAplicacion);
                } else if (response.code() == 204) {
                    Toast.makeText(ContextoAplicacion, "Ya existe un token de acceso válido.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }

    public void ConseguirMenu () {
        ClienteApi.InterfazApi api = ClienteApi.ConseguirApi();

        Call <List <Artículo>> llamada = api.ObtenerMenu(ClienteApi.LeerToken(ContextoAplicacion));

        llamada.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Artículo>> call, Response<List<Artículo>> response) {
                Log.i ("RespuestaHTTP", response.toString());
                if (response.code() == 200) {
                    MutableListaArticulos.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Artículo>> call, Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}