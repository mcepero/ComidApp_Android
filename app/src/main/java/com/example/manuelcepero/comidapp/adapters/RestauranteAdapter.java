package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.fragments.DetallesRestaurante;
import com.example.manuelcepero.comidapp.fragments.ListaRestaurantes;
import com.example.manuelcepero.comidapp.models.Restaurante;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
        holder.email.setText("Email: " + restaurante.getEmail());
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public void filter(final String strSearch) {
        if (strSearch.length() == 0) {
            listaRestaurantes.clear();
            listaRestaurantes.addAll(originalItems);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listaRestaurantes.clear();

                List<Restaurante> collect = originalItems.stream().filter(i -> i.getNombre().toLowerCase().contains(strSearch))
                        .collect(Collectors.<Restaurante>toList());

                listaRestaurantes.addAll(collect);

                //Filtra por ciudad
                /*collect = originalItems.stream().filter(i -> i.getCiudad().toLowerCase().contains(strSearch))
                        .collect(Collectors.<Restaurante>toList())

                listaRestaurantes.addAll(collect);*/

            } else {
                listaRestaurantes.clear();
                for (Restaurante i : originalItems) {
                    if (i.getNombre().toLowerCase().contains(strSearch)) {
                        listaRestaurantes.add(i);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        public LinearLayout linearLayout;
        public TextView nombre, direccion, telefono, email;


        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombreRestauranteLista);
            direccion = (TextView) itemView.findViewById(R.id.direccionRestauranteLista);
            telefono = (TextView) itemView.findViewById(R.id.telefonoRestauranteLista);
            email = (TextView) itemView.findViewById(R.id.emailRestauranteLista);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Restaurante r = listaRestaurantes.get(position);


                    DetallesRestaurante detallesRestauranteFragment = new DetallesRestaurante();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("restaurante", r);
                    detallesRestauranteFragment.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    if (activity.getSupportFragmentManager().getBackStackEntryCount()> 1) {
                        activity.getSupportFragmentManager().popBackStackImmediate();
                    }
                    activity.getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, detallesRestauranteFragment).addToBackStack(ListaRestaurantes.class.getName())
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
