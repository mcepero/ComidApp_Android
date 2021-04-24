package com.example.manuelcepero.comidapp.utils;

public class UsuarioActual {
    private static int id;
    private static String nombre;
    private static String usuario;
    private static String email;
    private static String direccion;
    private static String ciudad;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        UsuarioActual.id = id;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        UsuarioActual.nombre = nombre;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        UsuarioActual.usuario = usuario;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UsuarioActual.email = email;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion(String direccion) {
        UsuarioActual.direccion = direccion;
    }

    public static String getCiudad() {
        return ciudad;
    }

    public static void setCiudad(String ciudad) {
        UsuarioActual.ciudad = ciudad;
    }
}
