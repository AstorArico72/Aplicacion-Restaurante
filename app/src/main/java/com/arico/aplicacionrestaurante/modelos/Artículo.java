package com.arico.aplicacionrestaurante.modelos;


import java.util.List;

public class Artículo implements java.io.Serializable {
    private int id; //Sin signo - no admite valores negativos.
    private String nombre;
    private int precio; //Sin signo - no admite valores negativos.
    private String imagen;

    public String getUriFoto() {
        return imagen;
    }

    public void setUriFoto(String uriFoto) {
        this.imagen = uriFoto;
    }

    private List<String> atributos;

    public List<String> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<String> atributos) {
        this.atributos = atributos;
    }

    private String urlFoto;

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}