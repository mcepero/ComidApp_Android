package com.example.manuelcepero.comidapp;

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

import androidx.appcompat.app.AppCompatActivity;

public class RegistroRepartidor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_repartidor);

        final EditText usuario = findViewById(R.id.registroRepartidorUsuario);
        final EditText contrasena = findViewById(R.id.registroRepartidorContrasena);
        final EditText email = findViewById(R.id.registroRepartidorEmail);
        final EditText nombre = findViewById(R.id.registroRepartidorNombre);
        final EditText dni = findViewById(R.id.registroRepartidorDni);
        final Button registro = findViewById(R.id.registroRepartidorBoton);
        final TextView iniciarSesion = findViewById(R.id.registroRepartidorIniciarSesion);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHandler.getOut().println(Mensajes.PETICION_REGISTRO_REPARTIDOR+"--"+usuario.getText()+"--"+contrasena.getText()+"--"+email.getText()+"--"+ nombre.getText()+"--"+dni.getText());
                registro();
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (RegistroRepartidor.this, LoginRepartidor.class);
                startActivity(intent);
            }
        });

    }

    public void registro(){

        try {
            String received = SocketHandler.getIn().readLine();
            if (received.equals(Mensajes.PETICION_REGISTRO_REPARTIDOR_CORRECTO))
                Toast.makeText(this, "Registro correcto.", Toast.LENGTH_LONG).show();
            else if(received.equals(Mensajes.PETICION_REGISTRO_REPARTIDOR_ERROR))
                Toast.makeText(this, "Error al registrarse.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
