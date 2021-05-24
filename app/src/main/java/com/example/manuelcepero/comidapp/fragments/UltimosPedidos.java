package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.PedidoAdapter;
import com.example.manuelcepero.comidapp.adapters.ProductoAdapter;
import com.example.manuelcepero.comidapp.models.Pedido;
import com.example.manuelcepero.comidapp.models.Producto;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UltimosPedidos extends Fragment {

    private RecyclerView recyclerView;
    private PedidoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Pedido> listaPedidos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaPedidos = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_pedidos, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewPedidos);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        obtenerPedidos();

        return v;
    }

    public void obtenerPedidos(){
        SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_PEDIDOS_USUARIO+"--"+ UsuarioActual.getId());
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_MOSTRAR_PEDIDOS_USUARIO_CORRECTO)){
                int numRestaurantes = Integer.parseInt(args[1]);
                System.out.println("!!!"+numRestaurantes);

                for (int i=0; i<numRestaurantes; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Pedido p = new Pedido(Integer.parseInt(args[1]), Double.parseDouble(args[2]), args[3], args[4], args[5]);
                    listaPedidos.add(p);
                }
                adapter = new PedidoAdapter(getContext(), listaPedidos);
                //adapter.recorrerImagenes();
                recyclerView.setAdapter(adapter);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
