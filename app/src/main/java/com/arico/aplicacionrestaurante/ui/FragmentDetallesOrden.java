package com.arico.aplicacionrestaurante.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.databinding.FragmentDetallesOrdenBinding;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.util.AdaptadorOrden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentDetallesOrden extends Fragment {
    private Context ContextoAplicacion;
    private FragmentDetallesOrdenBinding binder;
    private DetallesOrdenViewModel ViewModel;

    public FragmentDetallesOrden() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binder = FragmentDetallesOrdenBinding.inflate(inflater);
        getActivity().setContentView(binder.getRoot());
        this.ContextoAplicacion = getActivity().getApplicationContext();
        this.ViewModel = new ViewModelProvider(this).get(DetallesOrdenViewModel.class);
        @Nullable ArrayList<FilaOrden> orden = (ArrayList<FilaOrden>) getArguments().getSerializable("Orden");
        AtomicInteger total = new AtomicInteger();
        Thread hiloPrecio = new Thread(()-> {
            if (orden != null && !orden.isEmpty()) {
                orden.forEach(fila -> {
                    try {
                        total.addAndGet(ViewModel.LeerPrecio(fila.getArticulo(), fila.getCantidad()));
                        binder.ImporteOrden.setText("Total: $" + total.intValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
        hiloPrecio.start();
        if (!orden.isEmpty() && orden != null) {
            binder.ListaArticulosVO.setAdapter(new AdaptadorOrden(ContextoAplicacion, orden));
            binder.ListaArticulosVO.setLayoutManager(new LinearLayoutManager(ContextoAplicacion));
        }


        binder.BotonNuevaOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContextoAplicacion, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ContextoAplicacion.startActivity(intent);
            }
        });

        return binder.getRoot();
    }
}