package com.arico.aplicacionrestaurante.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.arico.aplicacionrestaurante.databinding.FragmentVerArticuloBinding;
import com.arico.aplicacionrestaurante.modelos.Artículo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VerArticuloFragment extends BottomSheetDialogFragment {
    Artículo item;
    FragmentVerArticuloBinding binder;
    VerViewModel ViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.item = (Artículo) getArguments().getSerializable("ArticuloAVer");
        this.binder = FragmentVerArticuloBinding.inflate(inflater);
        this.ViewModel = new ViewModelProvider(this).get(VerViewModel.class);
        binder.NombreVArticulo.setText(item.getNombre());
        for (int i = 0; i < item.getAtributos().size(); i++) {
            switch (item.getAtributos().get(i)) {
                case "Vegano": {
                    binder.AtributosVArticulo.append("Apto para veganos");
                    if (i < item.getAtributos().size() -1 && item.getAtributos().size() != 1) {
                        binder.AtributosVArticulo.append(", ");
                    }
                    break;
                }
                case "Kosher": {
                    binder.AtributosVArticulo.append("Certificado Kosher");
                    if (i < item.getAtributos().size() -1 && item.getAtributos().size() != 1) {
                        binder.AtributosVArticulo.append(", ");
                    }
                    break;
                }
                case "Halal": {
                    binder.AtributosVArticulo.append("Certificado Halal");
                    if (i < item.getAtributos().size() -1 && item.getAtributos().size() != 1) {
                        binder.AtributosVArticulo.append(", ");
                    }
                    break;
                }
                default: {
                    binder.AtributosVArticulo.append("Contiene " + item.getAtributos().get(i));
                    if (i < item.getAtributos().size() -1 && item.getAtributos().size() != 1) {
                        binder.AtributosVArticulo.append(", ");
                    }
                    break;
                }
            }
            if (i == item.getAtributos().size() -1) {
                binder.AtributosVArticulo.append(".");
            }
        }

        binder.PrecioVArticulo.setText("$" + item.getPrecio());

        binder.SumarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(binder.CantidadArticulo.getText().toString());
                cantidad++;
                if (cantidad > 255) {
                    cantidad = 255;
                }
                binder.CantidadArticulo.setText(cantidad + "");
            }
        });

        binder.RestarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = Integer.parseInt(binder.CantidadArticulo.getText().toString());
                cantidad--;
                if (cantidad < 1) {
                    cantidad = 1;
                }
                binder.CantidadArticulo.setText(cantidad + "");
            }
        });

        //TODO: Hacer la llamada API para guardar las órdenes.
        binder.BotonOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int IdArticulo = item.getId();
                int CantidadArticulo = Integer.parseInt(binder.CantidadArticulo.getText().toString());
                ViewModel.NuevaFila(IdArticulo, CantidadArticulo);
            }
        });

        return binder.getRoot();
    }
}
