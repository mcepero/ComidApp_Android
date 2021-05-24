package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.fragments.Cesta;
import com.example.manuelcepero.comidapp.fragments.ContenedorDetalles;
import com.example.manuelcepero.comidapp.fragments.DetallesPedido;
import com.example.manuelcepero.comidapp.fragments.ListaRestaurantes;
import com.example.manuelcepero.comidapp.models.Pedido;
import com.example.manuelcepero.comidapp.models.Producto;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder>{
    private List<Pedido> listaPedidos;
    private int layout;
    private Activity activity;
    private View.OnClickListener listener;
    private Context context;
    private List<Pedido> originalItems;

    public PedidoAdapter(Context context, List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
        this.context = context;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaPedidos);
    }

    public PedidoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pedido, parent, false);
        return new PedidoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.ViewHolder holder, int position) {
        Pedido pedido = listaPedidos.get(position);

        holder.nombreRestaurante.setText(pedido.getRestaurante());
        holder.fecha.setText("Fecha: " + pedido.getFecha());
        holder.precio.setText("Precio total: " + pedido.getPrecio());
        holder.estado.setText("Estado: " + pedido.getEstado());

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        public LinearLayout linearLayout;
        public TextView nombreRestaurante, fecha, precio, estado;


        public ViewHolder(View itemView) {
            super(itemView);

            nombreRestaurante = (TextView) itemView.findViewById(R.id.nombreRestauranteListaPedido);
            fecha = (TextView) itemView.findViewById(R.id.fechaListaPedido);
            precio = (TextView) itemView.findViewById(R.id.precioListaPedido);
            estado = (TextView) itemView.findViewById(R.id.estadoListaPedido);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_DETALLES_PEDIDO+"--"+listaPedidos.get(position).getId());

                    String received;
                    String flag = "";
                    String[] args;

                    try {
                        received = SocketHandler.getIn().readLine();
                        args=received.split("--");
                        flag = args[0];

                        if (flag.equals(Mensajes.PETICION_MOSTRAR_DETALLES_PEDIDO_CORRECTO)){
                            Pedido p = listaPedidos.get(position);

                            int numProductos = Integer.parseInt(args[1]);
                            for (int i=0; i<numProductos; i++){
                                received = SocketHandler.getIn().readLine();
                                args=received.split("--");
                                flag = args[0];

                                p.getListaProductos().add(new Producto(args[1], args[3], Double.parseDouble(args[2])));
                            }
                            DetallesPedido detallesPedido = new DetallesPedido();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("pedido", p);
                            detallesPedido.setArguments(bundle);

                            AppCompatActivity activity = (AppCompatActivity) v.getContext();

                            if (activity.getSupportFragmentManager().getBackStackEntryCount()> 1) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }
                            activity.getSupportFragmentManager().beginTransaction().
                                    replace(R.id.container, detallesPedido).addToBackStack(ListaRestaurantes.class.getName())
                                    .addToBackStack(ListaRestaurantes.class.getName())
                                    .commit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

}
