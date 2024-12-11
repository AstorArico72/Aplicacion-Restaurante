package com.arico.aplicacionrestaurante.modelos;

import androidx.annotation.Nullable;

public class FilaOrden implements java.io.Serializable {
    @Nullable
    private int orden;
    private int artículo;
    private int cantidad;

    public FilaOrden (int idArticulo, int cantidad) {
        this.artículo = idArticulo;
        this.cantidad = cantidad;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getArticulo() {
        return artículo;
    }

    public void setArticulo(int articulo) {
        this.artículo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
