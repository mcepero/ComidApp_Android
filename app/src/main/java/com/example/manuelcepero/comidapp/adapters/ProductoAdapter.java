package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.models.Producto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder>{
    private List<Producto> listaProductos;
    private int layout;
    private Activity activity;
    private View.OnClickListener listener;
    private Context context;
    private List<Producto> originalItems;


    public ProductoAdapter(Context context, ArrayList<Producto> listaProductos, View.OnClickListener listener) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.listener = listener;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaProductos);
    }

    public ProductoAdapter(Context context, View.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.listaProductos = new ArrayList<>();
    }

    public ProductoAdapter(Context context, List<Producto> listaRestaurantes) {
        this.listaProductos = listaRestaurantes;
        this.context = context;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaRestaurantes);

    }

    public ProductoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_carta, parent, false);
        return new ProductoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.nombre.setText(producto.getNombre());
        holder.nombre.setText(producto.getNombre());
        holder.ingredientes.setText("Ingredientes: " + producto.getIngredientes());
        holder.precio.setText("Precio: " + producto.getPrecio());
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        public LinearLayout linearLayout;
        public TextView nombre, ingredientes, precio;


        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombreProductoLista);
            ingredientes = (TextView) itemView.findViewById(R.id.ingredientesProductoLista);
            precio = (TextView) itemView.findViewById(R.id.precioProductoLista);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }
}
