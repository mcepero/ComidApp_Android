package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email.getText());

        if(mather.find()==true){
            if (contrasena.getText().toString().isEmpty()) {
                SocketHandler.getOut().println(Mensajes.PETICION_EDITAR_PERFIL_REPARTIDOR + "--" + UsuarioActual.getUsuario() + "--" + usuario.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + dni.getText());
            }else{
                SocketHandler.getOut().println(Mensajes.PETICION_EDITAR_PERFIL_REPARTIDOR + "--" + UsuarioActual.getUsuario() + "--" + usuario.getText() + "--" + email.getText() + "--" + nombre.getText() + "--" + dni.getText() + "--"+contrasena.getText().toString());
            }

            String received = null;
            try {
                received = SocketHandler.getIn().readLine();
                String[] args = received.split("--");
                String flag = args[0];

                if (flag.equals(Mensajes.PETICION_EDITAR_PERFIL_REPARTIDOR_CORRECTO)){
                    UsuarioActual.setUsuario(usuario.getText().toString());
                    UsuarioActual.setNombre(nombre.getText().toString());
                    UsuarioActual.setEmail(email.getText().toString());
                    UsuarioActual.setDni(dni.getText().toString());

                    Toast.makeText(getContext(), "Perfil editado correctamente.",
                            Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerRepartidor, new PerfilRepartidor())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(this.getClass().getName())
                            .commit();
                }else if(flag.equals(Mensajes.PETICION_EDITAR_PERFIL_REPARTIDOR_ERROR)){
                    Toast.makeText(getContext(), "Error al editar perfil. Usuario o email repetidos.",
                            Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(), "Email inválido.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
