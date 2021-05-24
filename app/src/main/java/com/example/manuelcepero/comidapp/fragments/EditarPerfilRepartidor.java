package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EditarPerfilRepartidor extends Fragment {
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editar_perfil_repartidor, container, false);
        //Bundle args = this.getArguments();

        anadirDatos();

        Button botonGuardar = view.findViewById(R.id.guardarPerfilRepartidor);

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarPerfil();
            }
        });

        return view;
    }

    public void anadirDatos() {

        final TextView nombre = view.findViewById(R.id.editarPerfilRepartidorTextoNombre);
        final TextView usuario = view.findViewById(R.id.editarPerfilRepartidorTextoUsuario);
        final TextView email = view.findViewById(R.id.editarPerfilRepartidorTextoEmail);
        final TextView dni = view.findViewById(R.id.editarPerfilRepartidorTextoDni);

        nombre.setText(UsuarioActual.getNombre());
        usuario.setText(UsuarioActual.getUsuario());
        email.setText(UsuarioActual.getEmail());
        dni.setText(UsuarioActual.getDni());
    }

    public void modificarPerfil(){
        final TextView nombre = view.findViewById(R.id.editarPerfilRepartidorTextoNombre);
        final TextView usuario = view.findViewById(R.id.editarPerfilRepartidorTextoUsuario);
        final TextView contrasena = view.findViewById(R.id.editarPerfilRepartidorTextoContrasena);
        final TextView email = view.findViewById(R.id.editarPerfilRepartidorTextoEmail);
        final TextView dni = view.findViewById(R.id.editarPerfilRepartidorTextoDni);

        if (contrasena.getText().toString().isEmpty()) {
            SocketHandler.getOut().println(Mensajes.PETICION_EDITAR_PERFIL_REPARTIDOR + "--" + UsuarioActual.getUsuario() + "--" + usuario.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + dni.getText());
        }else{
            SocketHandler.getOut().println(Mensajes.PETICION_EDITAR_PERFIL_REPARTIDOR + "--" + UsuarioActual.getUsuario() + "--" + usuario.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + dni.getText() + "--"+contrasena.getText().toString());
        }

        UsuarioActual.setUsuario(usuario.getText().toString());
        UsuarioActual.setNombre(nombre.getText().toString());
        UsuarioActual.setEmail(email.getText().toString());
        UsuarioActual.setDni(dni.getText().toString());

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerRepartidor, new PerfilRepartidor())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(this.getClass().getName())
                .commit();
    }
}
