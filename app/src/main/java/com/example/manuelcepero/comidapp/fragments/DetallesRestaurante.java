package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.models.Restaurante;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

public class DetallesRestaurante extends Fragment {
    private Restaurante r;
    private View view;
    private Button botonAsistir;
    private boolean asiste=false;

    public DetallesRestaurante() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalles_restaurante, container, false);
        Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }

        anadirDatos();

        return view;
    }

    public void anadirDatos() {

        final TextView nombre = view.findViewById(R.id.detallesRestauranteNombre);
        final TextView direccion = view.findViewById(R.id.detallesRestauranteDireccion);
        final TextView ciudad = view.findViewById(R.id.detallesRestauranteCiudad);
        final TextView telefono = view.findViewById(R.id.detallesResturanteTelefono);
        final TextView email = view.findViewById(R.id.detallesRestauranteEmail);

        nombre.setText(r.getNombre());
        direccion.setText("Dirección: " + r.getDireccion());
        ciudad.setText("Ciudad: " + r.getCiudad());
        telefono.setText("Teléfono: " + r.getTelefono());
        email.setText("Email: " + r.getEmail());
    }
}
