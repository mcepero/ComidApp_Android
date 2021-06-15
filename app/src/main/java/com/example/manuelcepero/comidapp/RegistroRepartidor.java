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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroRepartidor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_repartidor);

        final Button registro = findViewById(R.id.registroRepartidorBoton);
        final TextView iniciarSesion = findViewById(R.id.registroRepartidorIniciarSesion);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarse();
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

    public void registrarse(){
        final EditText usuario = findViewById(R.id.registroRepartidorUsuario);
        final EditText contrasena = findViewById(R.id.registroRepartidorContrasena);
        final EditText email = findViewById(R.id.registroRepartidorEmail);
        final EditText nombre = findViewById(R.id.registroRepartidorNombre);
        final EditText dni = findViewById(R.id.registroRepartidorDni);

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email.getText());

        if(usuario.getText().length()>0 && contrasena.getText().length()>0 && email.getText().length()>0 && nombre.getText().length()>0 && dni.getText().length()>0) {
            if (mather.find() == true) {
                try {
                    SocketHandler.getOut().println(Mensajes.PETICION_REGISTRO_REPARTIDOR + "--" + usuario.getText() + "--" + contrasena.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + dni.getText());
                    registro();
                } catch (NullPointerException ex) {
                    Toast.makeText(this, "Error al conectar con el servidor.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email incorrecto.",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No puede haber campos vacíos.",
                    Toast.LENGTH_LONG).show();
        }
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
        }catch(NullPointerException e){
            System.out.println("Error de conexión");
            Toast.makeText(getApplicationContext(), "Error de conexión",
                Toast.LENGTH_LONG).show();
        }
    }
}
