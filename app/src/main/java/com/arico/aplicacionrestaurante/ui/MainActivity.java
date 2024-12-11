package com.arico.aplicacionrestaurante.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.modelos.Artículo;
import com.arico.aplicacionrestaurante.databinding.ActivityMainBinding;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.util.AdaptadorArticulo;
import com.arico.aplicacionrestaurante.util.ClienteApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binder;
    private MainViewModel ViewModel;
    private Context ContextoAplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        this.binder = ActivityMainBinding.inflate(getLayoutInflater());
        this.ContextoAplicacion = getApplicationContext();
        SolicitarPermisos();
        ViewModel.Conectarse();
        CargarMenu(null);
        if (MainViewModel.LeerOrden().getValue() != null && MainViewModel.getNuevaFila().getValue() != null) {
            MainViewModel.LeerOrden().getValue().clear();
            MainViewModel.getNuevaFila().setValue(null);
        }
        MainViewModel.ConseguirImporte().setValue(0);

        ViewModel.ConseguirArticulos().observe(this, new Observer<List<Artículo>>() {
            @Override
            public void onChanged(List<Artículo> artículos) {
                CargarMenu(artículos);
            }
        });

        MainViewModel.getNuevaFila().observe(this, new Observer<FilaOrden>() {
            @Override
            public void onChanged(FilaOrden filaOrden) {
                List <FilaOrden> orden = MainViewModel.LeerOrden().getValue();
                if (orden == null) {
                    orden = new ArrayList<>();
                }
                if (filaOrden == null) {
                    MainViewModel.LeerOrden().getValue().clear();
                } else {
                    orden.add(filaOrden);
                    MainViewModel.LeerOrden().postValue(orden);
                    Toast.makeText(ContextoAplicacion, "Añadido a la orden.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewModel.ConseguirMenu();

        binder.BotonMostrarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerOrdenFragment ver = new VerOrdenFragment(ContextoAplicacion);
                ver.show(getSupportFragmentManager(), "Orden");
            }
        });

        setContentView(binder.getRoot());
    }

    private void CargarMenu (@Nullable List<Artículo> artículos) {
        FragmentManager GestorFragmentos = getSupportFragmentManager();
        AdaptadorArticulo adaptador = new AdaptadorArticulo(ContextoAplicacion, artículos, GestorFragmentos);
        RecyclerView reciclador = binder.ListaPlatos;
        reciclador.setAdapter(adaptador);
        reciclador.setLayoutManager(new GridLayoutManager(ContextoAplicacion, 2));
    }
    private void SolicitarPermisos(){
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1000);
        }
    }
}