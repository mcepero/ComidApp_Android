package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.ProductoAdapter;
import com.example.manuelcepero.comidapp.adapters.RestauranteAdapter;
import com.example.manuelcepero.comidapp.models.Producto;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartaRestaurante extends Fragment{

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Producto> listaProductos;
    private Restaurante r;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaProductos = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_carta, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewProductos);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }

        obtenerRestaurantes();
        return v;
    }

    public void obtenerRestaurantes(){
        SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_CARTA+"--"+r.getId());
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_MOSTRAR_CARTA_CORRECTO)){
                int numRestaurantes = Integer.parseInt(args[1]);

                for (int i=0; i<numRestaurantes; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Producto p = new Producto(args[1], args[2], Double.parseDouble(args[3]));
                    listaProductos.add(p);
                }
                adapter = new ProductoAdapter(getContext(), listaProductos);
                recyclerView.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
