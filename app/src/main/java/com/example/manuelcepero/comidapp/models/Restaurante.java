package com.example.manuelcepero.comidapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurante implements Parcelable {
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;

    public Restaurante(String nombre, String email, String telefono, String direccion, String ciudad) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    protected Restaurante(Parcel in) {
        nombre = in.readString();
        email = in.readString();
        telefono = in.readString();
        direccion = in.readString();
        ciudad = in.readString();
    }

    public static final Creator<Restaurante> CREATOR = new Creator<Restaurante>() {
        @Override
        public Restaurante createFromParcel(Parcel in) {
            return new Restaurante(in);
        }

        @Override
        public Restaurante[] newArray(int size) {
            return new Restaurante[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(telefono);
        dest.writeString(direccion);
        dest.writeString(ciudad);
    }
}
