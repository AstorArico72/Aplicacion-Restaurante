package com.arico.aplicacionrestaurante.util;

import static com.arico.aplicacionrestaurante.util.ClienteApi.UrlBase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.bumptech.glide.*;
import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.modelos.Artículo;
import com.arico.aplicacionrestaurante.ui.VerArticuloFragment;

public class AdaptadorArticulo extends RecyclerView.Adapter <AdaptadorArticulo.Holder> {
    @Nullable private List<Artículo> Menu;
    private Context ContextoAplicacion;
    private FragmentManager gestor;

    public AdaptadorArticulo (@NonNull Context contextoAplicacion, @Nullable List<Artículo> items, @NonNull FragmentManager fragmentManager) {
        this.Menu = items;
        this.ContextoAplicacion = contextoAplicacion;
        this.gestor = fragmentManager;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from (ContextoAplicacion).inflate(R.layout.item_articulo, parent, false);
        return new Holder (vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (Menu != null) {
            Artículo item = Menu.get(position);
            holder.NombreArticulo.setText(item.getNombre());
            holder.PrecioArticulo.setText("$" + item.getPrecio());
            if (!item.getAtributos().isEmpty()) {
                for (int i = 0; i < item.getAtributos().size(); i++) {
                    if (i == item.getAtributos().size() -1 || item.getAtributos().size() == 1) {
                        holder.AtributosArticulo.append(item.getAtributos().get(i) + ".");
                        break;
                    }
                    holder.AtributosArticulo.append(item.getAtributos().get(i) + ", ");
                }
            } else {
                holder.AtributosArticulo.append("");
            }
            Glide.with(ContextoAplicacion).load (UrlBase + item.getUriFoto()).into (holder.FotoArticulo);
            holder.TarjetaArticulo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle atado = new Bundle();
                    atado.putSerializable("ArticuloAVer", item);
                    VerArticuloFragment ver = new VerArticuloFragment();
                    ver.setArguments(atado);

                    ver.show (gestor, item.getNombre());
                    //TODO: Instanciar el fragmento aquí.
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (Menu != null) {
            return Menu.size();
        } else {
            return 0;
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView NombreArticulo;
        TextView AtributosArticulo;
        TextView PrecioArticulo;
        ImageView FotoArticulo;
        CardView TarjetaArticulo;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.NombreArticulo = itemView.findViewById(R.id.NombreArticulo);
            this.AtributosArticulo = itemView.findViewById(R.id.AtributosArticulo);
            this.PrecioArticulo = itemView.findViewById(R.id.PrecioArticulo);
            this.FotoArticulo = itemView.findViewById(R.id.FotoArticulo);
            this.TarjetaArticulo = itemView.findViewById(R.id.TarjetaArticulo);
        }
    }
}
