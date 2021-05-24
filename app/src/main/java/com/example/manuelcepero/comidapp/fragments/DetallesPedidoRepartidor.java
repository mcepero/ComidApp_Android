package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.models.Pedido;
import com.example.manuelcepero.comidapp.models.Usuario;
import com.example.manuelcepero.comidapp.utils.Mensajes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class DetallesPedidoRepartidor extends Fragment {
    private Pedido p;
    private Usuario c;
    private View view;
    private Spinner listaEstados;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalles_pedido_repartidor, container, false);
        listaEstados = view.findViewById(R.id.detallesPedidoRepartidorListaEstados);
        Bundle args = this.getArguments();
        if (args != null) {
            p= args.getParcelable("pedido");
            c= args.getParcelable("cliente");
            System.out.println(c.getNombre()+"-"+c.getDireccion());
        }

        try {
            anadirDatos();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Button actualizarEstado = view.findViewById(R.id.detallesPedidoRepartidorBoton);

        actualizarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = listaEstados.getSelectedItemPosition()+1;
                SocketHandler.getOut().println(Mensajes.PETICION_ACTUALIZAR_ESTADO+"--"+p.getId()+"--"+posicion);
            }
        });

        return view;
    }

    public void anadirDatos() throws IOException, ClassNotFoundException {

        final TextView direccion = view.findViewById(R.id.detallesPedidoRepartidorDireccion);
        final TextView fecha = view.findViewById(R.id.detallesPedidoRepartidorFecha);
        final TextView precio = view.findViewById(R.id.detallesPedidoRepartidorPrecio);
        final TextView estado = view.findViewById(R.id.detallesPedidoRepartidorEstado);
        //final TextView productos = view.findViewById(R.id.detallesPedidoRepartidorProductos);

        direccion.setText(c.getDireccion());
        fecha.setText("Fecha: " + p.getFecha());
        precio.setText("Precio: " + p.getPrecio()+"€");

        String listaProductos="";
        for(int i=0; i<p.getListaProductos().size();i++){
            listaProductos+="\n"+p.getListaProductos().get(i).getNombre()+" - "+p.getListaProductos().get(i).getPrecio()+"€";
        }
       // productos.setText("Productos: " + listaProductos);

        ArrayList<String> estados = new ArrayList<>();
        SocketHandler.getOut().println(Mensajes.PETICION_OBTENER_ESTADOS);
        String received;
        String flag = "";
        String[] args;

        received = SocketHandler.getIn().readLine();
        args=received.split("--");
        flag = args[0];

        if (flag.equals(Mensajes.PETICION_OBTENER_ESTADOS_CORRECTO)){
            int numCategorias = Integer.parseInt(args[1]);
            for (int i=0; i<numCategorias; i++){
                received = SocketHandler.getIn().readLine();
                args=received.split("--");
                flag = args[0];
                estados.add(args[1]);
            }
        }
        //ObjectInputStream entrada2=new ObjectInputStream(SocketHandler.getSocket().getInputStream());
       // ArrayList<String> estados = (ArrayList<String>) entrada2.readObject();

        /*ArrayList<String> estados = new ArrayList<>();
        estados.add("Recibido");
        estados.add("En preparación");
        estados.add("Enviado");
        estados.add("Entregado");*/

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item, estados);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        listaEstados.setAdapter(adapter);

        listaEstados.setSelection(adapter.getPosition(p.getEstado()));
    }
}