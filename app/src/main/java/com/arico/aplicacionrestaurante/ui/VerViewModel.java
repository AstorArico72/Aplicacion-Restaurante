package com.arico.aplicacionrestaurante.ui;

import com.arico.aplicacionrestaurante.ui.MainViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.arico.aplicacionrestaurante.modelos.FilaOrden;

import java.util.ArrayList;
import java.util.List;

public class VerViewModel extends androidx.lifecycle.AndroidViewModel {
    private ArrayList<FilaOrden> ListaOrden;

    public VerViewModel(@NonNull Application application) {
        super(application);
        ListaOrden = new ArrayList<>();
    }


    /** Ésto suma un artículo con su cantidad a la orden.
     * @param idArticulo El ID del artículo que va en la nueva fila.
     * @param cantidad Cuánto de ése artículo.
    */
    public void NuevaFila (int idArticulo, int cantidad) {
        FilaOrden fila = new FilaOrden(idArticulo, cantidad);
        MainViewModel.getNuevaFila().postValue(fila);
    }
}
