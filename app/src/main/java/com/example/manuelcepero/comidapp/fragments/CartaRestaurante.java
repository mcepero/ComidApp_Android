package com.example.manuelcepero.comidapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.ProductoAdapter;
import com.example.manuelcepero.comidapp.models.Producto;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartaRestaurante extends Fragment{

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Producto> listaProductos;
    private Restaurante r;
    InputStream in = null;
    OutputStream out = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listaProductos = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_carta, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewProductos);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        try {
            in = SocketHandler.getSocket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }

        obtenerProductos();
   //     recorrerImagenes();
        return v;
    }

    public void obtenerProductos(){
        SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_CARTA+"--"+r.getId());
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_MOSTRAR_CARTA_CORRECTO)){
                int numRestaurantes = Integer.parseInt(args[1]);

                for (int i=0; i<numRestaurantes; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Producto p = new Producto(Integer.parseInt(args[4]), args[1], args[2], Double.parseDouble(args[3]), Integer.parseInt(args[5]));
                    listaProductos.add(p);
                }
                adapter = new ProductoAdapter(getContext(), listaProductos, in, out);
                //adapter.recorrerImagenes();
                recyclerView.setAdapter(adapter);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(NullPointerException e){
            System.out.println("Error de conexión");
            Toast.makeText(getContext(), "Error de conexión",
                    Toast.LENGTH_LONG).show();
        }
    }
}
