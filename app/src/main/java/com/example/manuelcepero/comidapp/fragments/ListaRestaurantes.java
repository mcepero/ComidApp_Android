package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.RestauranteAdapter;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListaRestaurantes extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private RecyclerView recyclerView;
    private RestauranteAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Restaurante> listaRestaurantes;
    private SearchView searchView;
    private Spinner categorias;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaRestaurantes = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_lista_restaurantes, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        searchView = v.findViewById(R.id.searchView);
        categorias = v.findViewById(R.id.categorias);
        initListener();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                System.out.println(categorias.getSelectedItem().toString());
                adapter.filter(searchView.getQuery().toString(), categorias.getSelectedItem().toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        anadirCategorias();
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
               // ObjectInputStream entrada2=new ObjectInputStream(SocketHandler.getSocket().getInputStream());
                // ArrayList<Restaurante> listaRestaurantes = (ArrayList<Restaurante>) entrada2.readObject();
                int numRestaurantes = Integer.parseInt(args[1]);

                for (int i=0; i<numRestaurantes; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Restaurante r = new Restaurante(Integer.parseInt(args[7]), args[1], args[2], args[3], args[4], args[5], args[6]);
                    listaRestaurantes.add(r);
                }
                adapter = new RestauranteAdapter(getContext(), listaRestaurantes);
                recyclerView.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void anadirCategorias(){
        SocketHandler.getOut().println(Mensajes.PETICION_OBTENER_CATEGORIAS);
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];
            System.out.println(received);
            ArrayList<String> listaCategorias = new ArrayList<>();
            listaCategorias.add("Todas");
            int numCategorias = Integer.parseInt(args[1]);

            for (int i=0; i<numCategorias; i++){
                received = SocketHandler.getIn().readLine();
                args=received.split("--");
                flag = args[0];
                listaCategorias.add(args[1]);
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, listaCategorias);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

            categorias.setAdapter(adapter);
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
        adapter.filter(newText, categorias.getSelectedItem().toString());
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("HOLA!!!!!!!");
        adapter.filter(categorias.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
