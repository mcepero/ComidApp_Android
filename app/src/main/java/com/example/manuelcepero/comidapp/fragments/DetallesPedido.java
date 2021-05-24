package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.models.Pedido;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;

public class DetallesPedido extends Fragment {
    private Pedido p;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalles_pedido, container, false);
        Bundle args = this.getArguments();
        if (args != null) {
            p= args.getParcelable("pedido");
        }

        anadirDatos();

        return view;
    }

    public void anadirDatos() {

        final TextView restaurante = view.findViewById(R.id.detallesPedidoNombreRestaurante);
        final TextView fecha = view.findViewById(R.id.detallesPedidoFecha);
        final TextView precio = view.findViewById(R.id.detallesPedidoPrecio);
        final TextView estado = view.findViewById(R.id.detallesPedidoEstado);
        final TextView productos = view.findViewById(R.id.detallesPedidoProductos);

        restaurante.setText(p.getRestaurante());
        fecha.setText("Fecha: " + p.getFecha());
        precio.setText("Precio: " + p.getPrecio()+"€");
        estado.setText("Estado: " + p.getEstado());

        String listaProductos="";
        for(int i=0; i<p.getListaProductos().size();i++){
            listaProductos+="\n"+p.getListaProductos().get(i).getNombre()+" - "+p.getListaProductos().get(i).getPrecio()+"€";
        }
        productos.setText("Productos: " + listaProductos);
    }
}