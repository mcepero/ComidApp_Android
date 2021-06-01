package com.example.manuelcepero.comidapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class Producto implements Parcelable {
    private int id;
    private String nombre;
    private String ingredientes;
    private double precio;
    private int idRestaurante;
    private File file;

    public Producto(String nombre, String ingredientes, double precio) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.precio = precio;
    }

    public Producto(int id, String nombre, String ingredientes, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.precio = precio;
    }

    public Producto(int id, String nombre, String ingredientes, double precio, int idRestaurante) {
        this.id = id;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.precio = precio;
        this.idRestaurante = idRestaurante;
    }

    protected Producto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        ingredientes = in.readString();
        precio = in.readDouble();
        idRestaurante = in.readInt();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(ingredientes);
        dest.writeDouble(precio);
        dest.writeInt(idRestaurante);
    }
}
