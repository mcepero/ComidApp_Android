package com.example.manuelcepero.comidapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
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
import com.example.manuelcepero.comidapp.models.Producto;
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
       // holder.nombre.setText(producto.getNombre());
        holder.ingredientes.setText("Ingredientes: " + producto.getIngredientes());
        holder.precio.setText("Precio: " + producto.getPrecio());

        /*SocketHandler.getOut().println(Mensajes.PETICION_FOTO_PRODUCTO + "--" + producto.getId());
        leerImagen();
        String filePath = context.getFilesDir().getPath().toString() + "/imagenproducto.jpg";
        System.out.println(filePath);
        File imagen = new File(filePath);
        Picasso.get().load(imagen).into(holder.imagen);*/
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {
        public LinearLayout linearLayout;
        public TextView nombre, ingredientes, precio;
        public ImageView imagen;


        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombreProductoLista);
            ingredientes = (TextView) itemView.findViewById(R.id.ingredientesProductoLista);
            precio = (TextView) itemView.findViewById(R.id.precioProductoLista);
            imagen = (ImageView) itemView.findViewById(R.id.imagenProducto);
            Button anadirCesta = (Button) itemView.findViewById(R.id.anadirCesta);

            anadirCesta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Ok...");
                    int position = getAdapterPosition();
                    Producto p= listaProductos.get(position);

                    boolean restauranteDistinto=false;
                    for(int i=0; i<Cesta.getListaProductos().size(); i++){
                        System.out.println(Cesta.getListaProductos().get(i).getIdRestaurante()+ " " + p.getIdRestaurante());
                        if (Cesta.getListaProductos().get(i).getIdRestaurante()!=p.getIdRestaurante()){
                            restauranteDistinto=true;
                            break;
                        }
                    }

                    if (!restauranteDistinto) {
                        Cesta.getListaProductos().add(p);
                    }else{
                        Toast.makeText(context, "No se pueden añadir productos de distintos restaurantes.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

    public void leerImagen() {
        InputStream in = null;
        OutputStream out = null;
        ArrayList<Byte> bytesImagen = new ArrayList<>();
        String filePath = context.getFilesDir().getPath().toString() + "/imagenproducto.jpg";
        File imagen = new File(filePath);

        try {
            in = SocketHandler.getSocket().getInputStream();
            out = new FileOutputStream(imagen);
            byte[] bytes = new byte[8096];

            int count;

            do {
                count = in.read(bytes);
                System.out.println(bytes);
                out.write(bytes, 0, count);
            } while (count == 8096);

            out.close();
            /*in.close();*/
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
