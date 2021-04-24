package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EditarPerfil extends Fragment {
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        //Bundle args = this.getArguments();

        anadirDatos();

        Button botonGuardar = view.findViewById(R.id.guardarPerfil);

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarPerfil();
            }
        });

        return view;
    }

    public void anadirDatos() {

        final TextView nombre = view.findViewById(R.id.editarPerfilTextoNombre);
        final TextView usuario = view.findViewById(R.id.editarPerfilTextoUsuario);
        final TextView email = view.findViewById(R.id.editarPerfilTextoEmail);
        final TextView direccion = view.findViewById(R.id.editarPerfilTextoDireccion);
        final TextView ciudad = view.findViewById(R.id.editarPerfilTextoCiudad);

        nombre.setText(UsuarioActual.getNombre());
        usuario.setText(UsuarioActual.getUsuario());
        email.setText(UsuarioActual.getEmail());
        direccion.setText(UsuarioActual.getDireccion());
        ciudad.setText(UsuarioActual.getCiudad());
    }

    public void modificarPerfil(){
        final TextView nombre = view.findViewById(R.id.editarPerfilTextoNombre);
        final TextView usuario = view.findViewById(R.id.editarPerfilTextoUsuario);
        final TextView contrasena = view.findViewById(R.id.editarPerfilTextoContrasena);
        final TextView email = view.findViewById(R.id.editarPerfilTextoEmail);
        final TextView direccion = view.findViewById(R.id.editarPerfilTextoDireccion);
        final TextView ciudad = view.findViewById(R.id.editarPerfilTextoCiudad);

        if (contrasena.getText().toString().isEmpty()) {
            SocketHandler.getOut().println(Mensajes.PETICION_EDITAR_PERFIL + "--" + UsuarioActual.getUsuario() + "--" + usuario.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + direccion.getText() + "--" + ciudad.getText());
        }else{
            SocketHandler.getOut().println(Mensajes.PETICION_EDITAR_PERFIL + "--" + UsuarioActual.getUsuario() + "--" + usuario.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + direccion.getText() + "--" + ciudad.getText()+"--"+contrasena.getText().toString());
        }

        UsuarioActual.setUsuario(usuario.getText().toString());
        UsuarioActual.setNombre(nombre.getText().toString());
        UsuarioActual.setEmail(email.getText().toString());
        UsuarioActual.setDireccion(direccion.getText().toString());
        UsuarioActual.setCiudad(ciudad.getText().toString());

        getFragmentManager().beginTransaction().replace(R.id.container, new Perfil())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(this.getClass().getName())
                .commit();
    }
}
