package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.PedidoRepartidorAdapter;
import com.example.manuelcepero.comidapp.models.Pedido;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.Thread.sleep;

public class UltimosPedidosRepartidor extends Fragment {

    private RecyclerView recyclerView;
    private PedidoRepartidorAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Pedido> listaPedidos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaPedidos = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_pedidos_repartidor, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewPedidosRepartidor);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        obtenerPedidos();

        return v;
    }

    public void obtenerPedidos(){

        SocketHandler.getOut().println(Mensajes.PETICION_RESTAURANTE_REPARTIDOR+"--"+ UsuarioActual.getUsuario());

        String received = null;
        try {
            received = SocketHandler.getIn().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] args=received.split("--");
        String flag = args[0];

        if (flag.equals(Mensajes.PETICION_RESTAURANTE_REPARTIDOR_CORRECTO)){
            UsuarioActual.setRestaurante(args[1]);
        }

        if (!UsuarioActual.getRestaurante().equals("null")) {
            SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_PEDIDOS_REPARTIDOR + "--" + UsuarioActual.getId());
            try {
                received = SocketHandler.getIn().readLine();
                args = received.split("--");
                flag = args[0];

                if (flag.equals(Mensajes.PETICION_MOSTRAR_PEDIDOS_REPARTIDOR_CORRECTO)) {
                    int numPedidos = Integer.parseInt(args[1]);

                    for (int i = 0; i < numPedidos; i++) {
                        received = SocketHandler.getIn().readLine();
                        args = received.split("--");
                        flag = args[0];

                        Pedido p = new Pedido(Integer.parseInt(args[1]), Double.parseDouble(args[2]), args[3], args[4], args[5]);
                        listaPedidos.add(p);
                    }
                    adapter = new PedidoRepartidorAdapter(getContext(), listaPedidos);
                    recyclerView.setAdapter(adapter);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Error de conexión");
                Toast.makeText(getContext(), "Error de conexión",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
