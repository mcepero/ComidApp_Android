package com.example.manuelcepero.comidapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.LoginRepartidor;
import com.example.manuelcepero.comidapp.MainActivity;
import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import java.io.IOException;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PerfilRepartidor extends Fragment{
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil_repartidor, container, false);

        anadirDatos();

        final Button editar = view.findViewById(R.id.perfilRepartidorBoton);
        final Button cerrarSesion = view.findViewById(R.id.perfilRepartidorBotonCerrarSesion);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerRepartidor, new EditarPerfilRepartidor())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(this.getClass().getName())
                        .commit();
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getActivity(), LoginRepartidor.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void anadirDatos() {

        final TextView nombre = view.findViewById(R.id.perfilRepartidorNombre);
        final TextView usuario = view.findViewById(R.id.perfilRepartidorUsuario);
        final TextView email = view.findViewById(R.id.perfilRepartidorEmail);
        final TextView dni = view.findViewById(R.id.perfilRepartidorDni);
        final TextView restaurante = view.findViewById(R.id.perfilRepartidorRestaurante);

        nombre.setText("Nombre: " + UsuarioActual.getNombre());
        usuario.setText("Usuario: " + UsuarioActual.getUsuario());
        email.setText("Email: " + UsuarioActual.getEmail());
        dni.setText("DNI: " + UsuarioActual.getDni());
        SocketHandler.getOut().println(Mensajes.PETICION_RESTAURANTE_REPARTIDOR+"--"+ UsuarioActual.getUsuario());

        String received = null;
        try {
            received = SocketHandler.getIn().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] args=received.split("--");
        String flag = args[0];

        if (flag.equals(Mensajes.PETICION_RESTAURANTE_REPARTIDOR_CORRECTO)){
            UsuarioActual.setRestaurante(args[1]);
        }

        if (UsuarioActual.getRestaurante().equals("null")) {
            restaurante.setText("Restaurante: ");
        }else{
            restaurante.setText("Restaurante: " + UsuarioActual.getRestaurante());
        }
    }

}
