package com.example.manuelcepero.comidapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.Inicio;
import com.example.manuelcepero.comidapp.MainActivity;
import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.Registro;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Perfil extends Fragment{
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        //Bundle args = this.getArguments();

        anadirDatos();

        final Button editar = view.findViewById(R.id.perfilBoton);
        final Button cerrarSesion = view.findViewById(R.id.perfilBotonCerrarSesion);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new EditarPerfil())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(this.getClass().getName())
                        .commit();
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void anadirDatos() {

        final TextView nombre = view.findViewById(R.id.perfilNombre);
        final TextView usuario = view.findViewById(R.id.perfilUsuario);
        final TextView email = view.findViewById(R.id.perfilEmail);
        final TextView direccion = view.findViewById(R.id.perfilDireccion);
        final TextView ciudad = view.findViewById(R.id.perfilCiudad);

        nombre.setText("Nombre: " + UsuarioActual.getNombre());
        usuario.setText("Usuario: " + UsuarioActual.getUsuario());
        email.setText("Email: " + UsuarioActual.getEmail());
        direccion.setText("Dirección: " + UsuarioActual.getDireccion());
        ciudad.setText("Ciudad: " + UsuarioActual.getCiudad());
    }

}
