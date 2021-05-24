package com.example.manuelcepero.comidapp.fragments;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.RestauranteAdapter;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Mapa extends SupportMapFragment implements OnMapReadyCallback {

    private ArrayList<Restaurante> listaRestaurantes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        listaRestaurantes = new ArrayList<>();

        getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        //Pide permisos
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.i("Permisos", "Permisos correctos");
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION }, 1222);
            }
        }

        //Ubicación actual
        map.setMyLocationEnabled(true);*/
        LocationServices.getFusedLocationProviderClient(getContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                map.setMyLocationEnabled(true);
            }
        });

        map.getUiSettings().setZoomControlsEnabled(true);

        obtenerRestaurantes(map);

        map.setOnMarkerClickListener(marker -> {
            try {
                String[] direccionRestaurante = marker.getSnippet().split("--");
                SocketHandler.getOut().println(Mensajes.PETICION_OBTENER_RESTAURANTE + "--" + marker.getTitle() + "--" + direccionRestaurante[0] + "--" + direccionRestaurante[1]);
                String received;
                String flag = "";
                String[] args;

                received = SocketHandler.getIn().readLine();
                args = received.split("--");
                flag = args[0];

                if (flag.equals(Mensajes.PETICION_OBTENER_RESTAURANTE_CORRECTO)){
                    ContenedorDetalles contenedorDetalles = new ContenedorDetalles();
                    Restaurante r = new Restaurante(Integer.parseInt(args[7]), args[1], args[2], args[3], args[4], args[5], args[6]);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("restaurante", r);
                    contenedorDetalles.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, contenedorDetalles)
                            .addToBackStack(Mapa.class.getName())
                            .commit();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            return true;
        });
    }

    public void obtenerRestaurantes(GoogleMap map){
        SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_RESTAURANTES);
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_MOSTRAR_RESTAURANTES_CORRECTO)){
                int numRestaurantes = Integer.parseInt(args[1]);

                for (int i=0; i<numRestaurantes; i++){
                    received = SocketHandler.getIn().readLine();
                    args=received.split("--");
                    flag = args[0];

                    Restaurante r = new Restaurante(args[1], args[2], args[3], args[4], args[5], args[6]);
                    listaRestaurantes.add(r);

                    //
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    String strAddress = r.getDireccion()+","+r.getCiudad();
                    List<Address> address  = geocoder.getFromLocationName(strAddress, 1);

                    //
                    //Añade restaurante al mapa
                    /*Geocoder geocoder = new Geocoder(getContext());

                    //Obtiene la longitud y latitud de la dirección
                    String strAddress = r.getDireccion()+","+r.getCiudad();
                    List<Address> address;

                    address = geocoder.getFromLocationName(strAddress,1);*/
                        //if (address==null) {
                        //    return null;
                        //}
                    Address location=address.get(0);
                    //
                    double latitude= location.getLatitude();
                    double longitude= location.getLongitude();
                    final LatLng p1 = new LatLng(latitude, longitude);
                    map.addMarker(new MarkerOptions().position(p1).title(r.getNombre()).snippet(r.getDireccion() + "--" + r.getCiudad()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}