package com.example.manuelcepero.comidapp.models;

public class Valoracion {
    private int id;
    private String comentario;
    private int nota;

    public Valoracion(int id, String comentario, int nota) {
        this.id = id;
        this.comentario = comentario;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
