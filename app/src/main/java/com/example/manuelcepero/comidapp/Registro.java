package com.example.manuelcepero.comidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Registro extends AppCompatActivity {

    //Socket socket = null;
    //PrintWriter out = null;
    //BufferedReader in = null;
    BufferedReader sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final EditText usuario = findViewById(R.id.registro_user);
        final EditText contrasena = findViewById(R.id.registro_pass);
        final EditText email = findViewById(R.id.registro_email);
        final EditText nombre = findViewById(R.id.registro_nombre);
        final EditText direccion = findViewById(R.id.registro_direccion);
        final EditText ciudad = findViewById(R.id.registro_ciudad);
        final Button registro = findViewById(R.id.registro_registro);
        final TextView iniciarSesion = findViewById(R.id.registro_iniciarsesion);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHandler.getOut().println(Mensajes.PETICION_REGISTRO+"--"+usuario.getText()+"--"+email.getText()+"--"+contrasena.getText()+"--"+ nombre.getText()+"--"+direccion.getText()+"--"+ciudad.getText());
                registro();
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (Registro.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void conectar(){
        sc = new BufferedReader(new InputStreamReader(System.in));
    }


    public void registro(){
        try {
            String recived = SocketHandler.getIn().readLine();

            if (recived.equals(Mensajes.PETICION_REGISTRO_CORRECTO))
                Toast.makeText(this, "Registro correcto.", Toast.LENGTH_LONG).show();
            else if(recived.equals(Mensajes.PETICION_REGISTRO_ERROR))
                Toast.makeText(this, "Error al registrarse.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
