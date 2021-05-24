package com.example.manuelcepero.comidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;
import com.example.manuelcepero.comidapp.viewmodels.LoginViewModel;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class LoginRepartidor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_repartidor);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final EditText usuario = findViewById(R.id.loginRepartidorUsuario);
        final EditText contrasena = findViewById(R.id.loginRepartidorContrasena);
        final Button login = findViewById(R.id.loginRepartidorLogin);
        final TextView registro = findViewById(R.id.loginRepartidorRegistro);
        final TextView loginCliente = findViewById(R.id.loginRepartidorLoginCliente);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usuario.getText().toString(), contrasena.getText().toString());
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (LoginRepartidor.this, RegistroRepartidor.class);
                startActivity(intent);
            }
        });

        loginCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (LoginRepartidor.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void login(String usuario, String contrasena){

        SocketHandler.getOut().println(Mensajes.PETICION_LOGIN_REPARTIDOR+"--"+usuario+"--"+contrasena);
        String received;
        String flag = "";
        String[] args;

        try {
            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_LOGIN_REPARTIDOR_CORRECTO)){
                Toast.makeText(this, "Login correcto.",
                        Toast.LENGTH_SHORT).show();
                UsuarioActual.setUsuario(usuario);
                UsuarioActual.setId(Integer.parseInt(args[1]));
                UsuarioActual.setEmail(args[2]);
                UsuarioActual.setNombre(args[3]);
                UsuarioActual.setDni(args[4]);
                UsuarioActual.setRestaurante(args[5]);
                //UsuarioActual.setCiudad(args[5]);
                Intent intent= new Intent (LoginRepartidor.this, InicioRepartidor.class);
                startActivity(intent);
                finish();
            }else if(flag.equals(Mensajes.PETICION_LOGIN_REPARTIDOR_ERROR)){
                Toast.makeText(this, "Login incorrecto.",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
