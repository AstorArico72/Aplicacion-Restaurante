package com.arico.aplicacionrestaurante.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.arico.aplicacionrestaurante.R;
import com.arico.aplicacionrestaurante.modelos.Artículo;
import com.arico.aplicacionrestaurante.modelos.FilaOrden;
import com.arico.aplicacionrestaurante.ui.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdaptadorOrden extends RecyclerView.Adapter <AdaptadorOrden.Holder> {
    private Context ContextoAplicacion;
    private int ImporteOrden;
    @Nullable
    private List <FilaOrden> Orden;

    public AdaptadorOrden (@NonNull Context contextoAplicacion, List<FilaOrden> items) {
        this.ContextoAplicacion = contextoAplicacion;
        this.Orden = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(ContextoAplicacion).inflate(R.layout.articulo_orden, parent, false);
        return new Holder (vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (Orden != null && !Orden.isEmpty()) {
            FilaOrden fila = Orden.get(position);
            holder.IdArticulo.setText(fila.getArticulo() + "");
            holder.CantidadArticulo.setText(fila.getCantidad() + "");
            ClienteApi.InterfazApi api = ClienteApi.ConseguirApi();
            String token = ClienteApi.LeerToken(ContextoAplicacion);

            Call<Artículo> llamada = api.DetallesArticulo(token, fila.getArticulo());
            llamada.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Artículo> call, Response<Artículo> response) {
                    if (response.code() == 200) {
                        Artículo item = response.body();
                        holder.NombreArticulo.setText(item.getNombre());
                        int subtotal = item.getPrecio() * fila.getCantidad();
                        ImporteOrden += subtotal;
                        holder.SubtotalArticulo.append(subtotal + "");
                        MainViewModel.ConseguirImporte().postValue(ImporteOrden);
                    }
                }

                @Override
                public void onFailure(Call<Artículo> call, Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (Orden == null) {
            return 0;
        } else {
            return Orden.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView IdArticulo;
        TextView NombreArticulo;
        TextView CantidadArticulo;
        TextView SubtotalArticulo;
        public Holder(@NonNull View itemView) {
            super(itemView);
            this.IdArticulo = itemView.findViewById(R.id.IdOArticulo);
            this.NombreArticulo = itemView.findViewById(R.id.NombreOArticulo);
            this.CantidadArticulo = itemView.findViewById(R.id.CantidadOArticulo);
            this.SubtotalArticulo = itemView.findViewById(R.id.Subtotal);
        }
    }
}
