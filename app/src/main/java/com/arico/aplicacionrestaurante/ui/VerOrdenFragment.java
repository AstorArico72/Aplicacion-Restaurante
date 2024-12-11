package com.arico.aplicacionrestaurante.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.ui.MainViewModel;
import com.arico.aplicacionrestaurante.databinding.FragmentVerOrdenBinding;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.util.AdaptadorOrden;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
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
        if (MainViewModel.LeerOrden().getValue() != null && !MainViewModel.LeerOrden().getValue().isEmpty()) {
            binder.OrdenVacia.setVisibility(View.INVISIBLE);
            binder.ArticulosOrden.setAdapter(new AdaptadorOrden(ContextoAplicacion, MainViewModel.LeerOrden().getValue()));
            binder.ArticulosOrden.setLayoutManager(new LinearLayoutManager(ContextoAplicacion));
        } else {
            binder.OrdenVacia.setVisibility(View.VISIBLE);
            binder.BotonEnviarOrden.setVisibility(View.INVISIBLE);
            binder.ArticulosOrden.setVisibility(View.INVISIBLE);
            binder.EtiquetaIdO.setVisibility(View.INVISIBLE);
            binder.EtiquetaCantidadO.setVisibility(View.INVISIBLE);
            binder.EtiquetaNombreO.setVisibility(View.INVISIBLE);
            binder.EtiquetaSubtotal.setVisibility(View.INVISIBLE);
            binder.ImporteOrden.setVisibility(View.INVISIBLE);
        }

        MainViewModel.ConseguirImporte().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binder.ImporteOrden.setText("Total: $" + integer);
            }
        });

        binder.BotonEnviarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<FilaOrden> orden = (ArrayList<FilaOrden>) MainViewModel.LeerOrden().getValue();
                OViewModel.EnviarOrden(orden);
                Bundle atado = new Bundle();
                atado.putSerializable("Orden", orden);
                Log.d ("Orden", orden.toString());
                FragmentDetallesOrden detallesOrden = new FragmentDetallesOrden();
                detallesOrden.setArguments(atado);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add (FragmentDetallesOrden.class, atado, null);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return binder.getRoot();
    }
}
