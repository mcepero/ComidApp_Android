package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.RestauranteAdapter;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListaRestaurantes extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    private RestauranteAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Restaurante> listaRestaurantes;
    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaRestaurantes = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_lista_restaurantes, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        searchView = v.findViewById(R.id.searchView);
        initListener();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        obtenerRestaurantes();
        return v;
    }

    public void obtenerRestaurantes(){
        SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_RESTAURANTES);
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_MOSTRAR_RESTAURANTES_CORRECTO)){
                int numRestaurantes = Integer.parseInt(args[1]);

                for (int i=0; i<numRestaurantes; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Restaurante r = new Restaurante(args[1], args[2], args[3], args[4], args[5]);
                    listaRestaurantes.add(r);
                }
                adapter = new RestauranteAdapter(getContext(), listaRestaurantes);
                recyclerView.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initListener(){
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}
