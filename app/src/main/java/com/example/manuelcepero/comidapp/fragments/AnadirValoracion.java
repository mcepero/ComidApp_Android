package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.models.Valoracion;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AnadirValoracion  extends Fragment {
    private Restaurante r;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_anadir_valoracion, container, false);

        Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }

        Spinner s = (Spinner) view.findViewById(R.id.puntuacionAnadirValoracion);
        EditText comentario = (EditText) view.findViewById(R.id.comentarioAnadirValoracion);
        Button anadirValoracion = (Button) view.findViewById(R.id.botonEnviarValoracion);

        String[] arraySpinner = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        anadirValoracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean usuarioRepetido=false;
                for (int i=0; i<Valoraciones.getListaValoraciones().size(); i++){
                    if (Valoraciones.getListaValoraciones().get(i).getIdCliente()==UsuarioActual.getId()){
                        usuarioRepetido=true;
                        break;
                    }
                }

                if (!usuarioRepetido) {
                    SocketHandler.getOut().println(Mensajes.PETICION_ANADIR_VALORACION + "--" + r.getId() + "--" + comentario.getText() + "--" + s.getSelectedItem().toString() + "--" + UsuarioActual.getId());

                    try {
                        if (SocketHandler.getIn().readLine().equals(Mensajes.PETICION_ANADIR_VALORACION_CORRECTO)) {
                            Valoraciones valoraciones = new Valoraciones();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("restaurante", r);
                            valoraciones.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerDetalles, valoraciones)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .addToBackStack(this.getClass().getName())
                                    .commit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "Ya has añadido una valoración para este restaurante.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }


}