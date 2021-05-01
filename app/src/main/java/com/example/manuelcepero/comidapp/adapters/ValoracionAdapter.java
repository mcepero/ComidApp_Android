package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.models.Valoracion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ValoracionAdapter extends RecyclerView.Adapter<ValoracionAdapter.ViewHolder>{
    private List<Valoracion> listaValoraciones;
    private int layout;
    private Activity activity;
    private View.OnClickListener listener;
    private Context context;
    private List<Valoracion> originalItems;


    public ValoracionAdapter(Context context, ArrayList<Valoracion> listaValoraciones, View.OnClickListener listener) {
        this.context = context;
        this.listaValoraciones = listaValoraciones;
        this.listener = listener;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaValoraciones);
    }

    public ValoracionAdapter(Context context, View.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.listaValoraciones = new ArrayList<>();
    }

    public ValoracionAdapter(Context context, List<Valoracion> listaValoraciones) {
        this.listaValoraciones = listaValoraciones;
        this.context = context;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaValoraciones);

    }

    public ValoracionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ValoracionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_valoracion, parent, false);
        return new ValoracionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ValoracionAdapter.ViewHolder holder, int position) {
        Valoracion valoracion = listaValoraciones.get(position);

        holder.comentario.setText(valoracion.getComentario());
        holder.puntuacion.setText("Puntuación: " + valoracion.getNota()+"/10");
    }

    @Override
    public int getItemCount() {
        return listaValoraciones.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        public LinearLayout linearLayout;
        public TextView comentario, puntuacion;


        public ViewHolder(View itemView) {
            super(itemView);

            comentario = (TextView) itemView.findViewById(R.id.comentarioValoracion);
            puntuacion = (TextView) itemView.findViewById(R.id.notaValoracion);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }
}
