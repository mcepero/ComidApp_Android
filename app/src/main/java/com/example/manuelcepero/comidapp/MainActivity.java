package com.example.manuelcepero.comidapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;
import com.example.manuelcepero.comidapp.viewmodels.LoginViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    String hostServerName = "192.168.0.16";//"10.0.2.2";
    int servicePort = 4444;

    //LoginViewModel loginViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Pide permisos
        //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.i("Permisos", "Permisos correctos");
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION }, 1222);
            }
        }

        conectar();

        //loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        final EditText usuario = findViewById(R.id.user);
        final EditText contrasena = findViewById(R.id.pass);
        final Button login = findViewById(R.id.login);
        final TextView registro = findViewById(R.id.registro);
        final TextView loginRepartidor = findViewById(R.id.loginRepartidor);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usuario.getText().toString(), contrasena.getText().toString());
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });

        loginRepartidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (MainActivity.this, LoginRepartidor.class);
                startActivity(intent);
            }
        });

    }

    public void login(String usuario, String contrasena){

        SocketHandler.getOut().println(Mensajes.PETICION_LOGIN+"--"+usuario+"--"+contrasena);
        String received;
        String flag = "";
        String[] args;

        try {
            received = SocketHandler.getIn().readLine();
            System.out.println(received);
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_LOGIN_CORRECTO)){
                Toast.makeText(this, "Login correcto.",
                        Toast.LENGTH_SHORT).show();
                UsuarioActual.setUsuario(usuario);
                UsuarioActual.setId(Integer.parseInt(args[1]));
                UsuarioActual.setEmail(args[2]);
                UsuarioActual.setNombre(args[3]);
                UsuarioActual.setDireccion(args[4]);
                UsuarioActual.setCiudad(args[5]);
                Intent intent= new Intent (MainActivity.this, Inicio.class);
                startActivity(intent);
                finish();
            }else if(flag.equals(Mensajes.PETICION_LOGIN_ERROR)){
                Toast.makeText(this, "Login incorrecto.",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void conectar(){
            try {
                SocketHandler.setSocket(new Socket(hostServerName, servicePort));
                SocketHandler.setOut();
                SocketHandler.setIn();
            } catch (IOException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
}
