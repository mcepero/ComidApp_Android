package com.example.manuelcepero.comidapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String nombre;
    private String usuario;
    private String contrasena;
    private String email;
    private String direccion;
    private String ciudad;

    public Usuario(String nombre, String usuario, String contrasena, String email, String direccion) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.email = email;
        this.direccion = direccion;
    }

    public Usuario(String nombre, String email, String direccion, String ciudad) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public Usuario(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    protected Usuario(Parcel in) {
        nombre = in.readString();
        usuario = in.readString();
        contrasena = in.readString();
        email = in.readString();
        direccion = in.readString();
        ciudad = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        dest.writeString(usuario);
        dest.writeString(contrasena);
        dest.writeString(email);
        dest.writeString(direccion);
        dest.writeString(ciudad);
    }
}

