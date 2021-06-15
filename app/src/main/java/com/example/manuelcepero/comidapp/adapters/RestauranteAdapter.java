package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.fragments.ContenedorDetalles;
import com.example.manuelcepero.comidapp.fragments.DetallesRestaurante;
import com.example.manuelcepero.comidapp.fragments.ListaRestaurantes;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.ViewHolder>{
    private List<Restaurante> listaRestaurantes;
    private int layout;
    private Activity activity;
    private View.OnClickListener listener;
    private Context context;
    private List<Restaurante> originalItems;


    public RestauranteAdapter(Context context, ArrayList<Restaurante> listaRestaurantes, View.OnClickListener listener) {
        this.context = context;
        this.listaRestaurantes = listaRestaurantes;
        this.listener = listener;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaRestaurantes);
    }

    public RestauranteAdapter(Context context, View.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.listaRestaurantes = new ArrayList<>();
    }

    public RestauranteAdapter(Context context, List<Restaurante> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
        this.context = context;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaRestaurantes);

    }

    public RestauranteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RestauranteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurantes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteAdapter.ViewHolder holder, int position) {
        Restaurante restaurante = listaRestaurantes.get(position);

        holder.nombre.setText(restaurante.getNombre());
        holder.direccion.setText("Dirección: " + restaurante.getDireccion()+" ("+restaurante.getCiudad()+")");
        holder.telefono.setText("Teléfono: " + restaurante.getTelefono());
        holder.categoria.setText("Categoría: " + restaurante.getCategoria());
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public void filter(final String strSearch, final String categoria) {
        if (strSearch.length() == 0 && categoria.equals("Todas")) {
            listaRestaurantes.clear();
            listaRestaurantes.addAll(originalItems);
            notifyDataSetChanged();
        }else if(strSearch.length() == 0 && categoria.length()>0 && !categoria.equals("Todas")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listaRestaurantes.clear();

                //Filtra por categoría
                List<Restaurante> collect = originalItems.stream().filter(i -> i.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
                        .collect(Collectors.<Restaurante>toList());

                listaRestaurantes.addAll(collect);
            } else {
                listaRestaurantes.clear();
                for (Restaurante i : originalItems) {
                    if (i.getCategoria().toLowerCase().contains(categoria.toLowerCase())) {
                        listaRestaurantes.add(i);
                    }
                }
            }
            notifyDataSetChanged();
        }else if(strSearch.length() > 0 && categoria.equals("Todas")){
            listaRestaurantes.clear();
            for (Restaurante i : originalItems) {
                if (i.getNombre().toLowerCase().contains(strSearch)) {
                    listaRestaurantes.add(i);
                }
            }
            notifyDataSetChanged();
        }else {
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listaRestaurantes.clear();
                for (Restaurante i : originalItems) {
                    if (i.getNombre().toLowerCase().contains(strSearch) && i.getCategoria().toLowerCase().contains(categoria.toLowerCase())) {
                        listaRestaurantes.add(i);
                    }
                }
           // }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        public LinearLayout linearLayout;
        public TextView nombre, direccion, telefono, categoria;

        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombreRestauranteLista);
            direccion = (TextView) itemView.findViewById(R.id.direccionRestauranteLista);
            telefono = (TextView) itemView.findViewById(R.id.telefonoRestauranteLista);
            categoria = (TextView) itemView.findViewById(R.id.categoriaRestauranteLista);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Restaurante r = listaRestaurantes.get(position);


                    //DetallesRestaurante detallesRestauranteFragment = new DetallesRestaurante();
                    ContenedorDetalles contenedorDetalles = new ContenedorDetalles();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("restaurante", r);
                    contenedorDetalles.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    if (activity.getSupportFragmentManager().getBackStackEntryCount()> 1) {
                        activity.getSupportFragmentManager().popBackStackImmediate();
                    }
                    activity.getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, contenedorDetalles)
                            .addToBackStack(ListaRestaurantes.class.getName())
                            .commit();

                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }
}
