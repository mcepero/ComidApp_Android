package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.ValoracionAdapter;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.models.Valoracion;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Valoraciones extends Fragment {

    private RecyclerView recyclerView;
    private ValoracionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Valoracion> listaValoraciones;
    private Restaurante r;
    Button anadirValoracion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaValoraciones = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_lista_valoraciones, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewValoraciones);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }

        obtenerValoraciones();

        anadirValoracion = (Button) v.findViewById(R.id.anadirValoracion);

        anadirValoracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnadirValoracion anadirValoracion = new AnadirValoracion();
                Bundle bundle = new Bundle();
                bundle.putParcelable("restaurante", r);
                anadirValoracion.setArguments(bundle);

                showSelectedFragment(anadirValoracion);
            }
        });

        return v;
    }



    public void obtenerValoraciones(){
        SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_VALORACIONES+"--"+r.getId());
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_MOSTRAR_VALORACIONES_CORRECTO)){
                int numValoraciones = Integer.parseInt(args[1]);

                for (int i=0; i<numValoraciones; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Valoracion v = new Valoracion(Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
                    listaValoraciones.add(v);
                }
                adapter = new ValoracionAdapter(getContext(), listaValoraciones);
                recyclerView.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSelectedFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerDetalles, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(this.getClass().getName())
                .commit();
    }
}
