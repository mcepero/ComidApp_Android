package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.fragments.DetallesPedidoRepartidor;
import com.example.manuelcepero.comidapp.fragments.ListaRestaurantes;
import com.example.manuelcepero.comidapp.fragments.UltimosPedidosRepartidor;
import com.example.manuelcepero.comidapp.models.Pedido;
import com.example.manuelcepero.comidapp.models.Producto;
import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PedidoRepartidorAdapter extends RecyclerView.Adapter<PedidoRepartidorAdapter.ViewHolder>{
    private List<Pedido> listaPedidos;
    private int layout;
    private Activity activity;
    private View.OnClickListener listener;
    private Context context;
    private List<Pedido> originalItems;

    public PedidoRepartidorAdapter(Context context, List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
        this.context = context;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaPedidos);
    }

    public PedidoRepartidorAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PedidoRepartidorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pedido_repartidor, parent, false);
        return new PedidoRepartidorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoRepartidorAdapter.ViewHolder holder, int position) {
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

            nombreRestaurante = (TextView) itemView.findViewById(R.id.nombreRestauranteListaPedidoRepartidor);
            fecha = (TextView) itemView.findViewById(R.id.fechaListaPedidoRepartidor);
            precio = (TextView) itemView.findViewById(R.id.precioListaPedidoRepartidor);
            estado = (TextView) itemView.findViewById(R.id.estadoListaPedidoRepartidor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_DETALLES_PEDIDO_REPARTIDOR+"--"+listaPedidos.get(position).getId());

                    String received;
                    String flag = "";
                    String[] args;

                    try {
                        received = SocketHandler.getIn().readLine();
                        args=received.split("--");
                        flag = args[0];

                        if (flag.equals(Mensajes.PETICION_MOSTRAR_DETALLES_PEDIDO_REPARTIDOR_CORRECTO)){
                            Pedido p = listaPedidos.get(position);
                            Usuario c = new Usuario(args[6],args[7],args[8],args[9]);
                            DetallesPedidoRepartidor detallesPedidoRepartidor = new DetallesPedidoRepartidor();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("pedido", p);
                            bundle.putParcelable("cliente", c);
                            detallesPedidoRepartidor.setArguments(bundle);

                            AppCompatActivity activity = (AppCompatActivity) v.getContext();

                            if (activity.getSupportFragmentManager().getBackStackEntryCount()> 1) {
                                activity.getSupportFragmentManager().popBackStackImmediate();
                            }
                            activity.getSupportFragmentManager().beginTransaction().
                                    replace(R.id.containerRepartidor, detallesPedidoRepartidor)
                                    .addToBackStack(UltimosPedidosRepartidor.class.getName())
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
