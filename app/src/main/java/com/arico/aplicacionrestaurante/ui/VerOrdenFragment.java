package com.arico.aplicacionrestaurante.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arico.aplicacionrestaurante.ui.MainViewModel;
import com.arico.aplicacionrestaurante.databinding.FragmentVerOrdenBinding;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.util.AdaptadorOrden;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class VerOrdenFragment extends BottomSheetDialogFragment {
    private FragmentVerOrdenBinding binder;
    private Context ContextoAplicacion;
    private OrdenViewModel OViewModel;

    public VerOrdenFragment (Context contextoAplicacion) {
        this.ContextoAplicacion = contextoAplicacion;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binder = FragmentVerOrdenBinding.inflate(inflater);
        this.OViewModel = new ViewModelProvider(this).get(OrdenViewModel.class);
        binder.ArticulosOrden.setAdapter(new AdaptadorOrden(ContextoAplicacion, MainViewModel.LeerOrden().getValue()));
        binder.ArticulosOrden.setLayoutManager(new LinearLayoutManager(ContextoAplicacion));

        MainViewModel.ConseguirImporte().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binder.ImporteOrden.setText("Total: $" + integer);
            }
        });

        binder.BotonEnviarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FilaOrden> orden = MainViewModel.LeerOrden().getValue();
                OViewModel.EnviarOrden(orden);
            }
        });

        return binder.getRoot();
    }
}
