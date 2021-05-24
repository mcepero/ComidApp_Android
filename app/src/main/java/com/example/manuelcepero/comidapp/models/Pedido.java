package com.example.manuelcepero.comidapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Pedido implements Parcelable {
    private int id;
    private double precio;
    private String fecha;
    private String estado;
    private String restaurante;
    private ArrayList<Producto> listaProductos;

    public Pedido(int id, double precio, String fecha, String estado, String restaurante) {
        this.id = id;
        this.precio = precio;
        this.fecha = fecha;
        this.estado = estado;
        this.restaurante = restaurante;
        listaProductos = new ArrayList<>();
    }

    protected Pedido(Parcel in) {
        id = in.readInt();
        precio = in.readDouble();
        fecha = in.readString();
        estado = in.readString();
        restaurante = in.readString();
    }

    public static final Creator<Pedido> CREATOR = new Creator<Pedido>() {
        @Override
        public Pedido createFromParcel(Parcel in) {
            return new Pedido(in);
        }

        @Override
        public Pedido[] newArray(int size) {
            return new Pedido[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(String restaurante) {
        this.restaurante = restaurante;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(precio);
        dest.writeString(fecha);
        dest.writeString(estado);
        dest.writeString(restaurante);
    }
}
