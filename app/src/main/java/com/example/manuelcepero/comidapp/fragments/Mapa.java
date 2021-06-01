package com.example.manuelcepero.comidapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.example.manuelcepero.comidapp.utils.PermissionUtils;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import androidx.fragment.app.FragmentActivity;

import static android.content.Context.LOCATION_SERVICE;

public class Mapa extends SupportMapFragment implements OnMapReadyCallback {

    private ArrayList<Restaurante> listaRestaurantes;
    private GoogleMap mMap;
    boolean statusOfGPS;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        listaRestaurantes = new ArrayList<>();

        //LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE );
        //statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        getMapAsync(this);

        return rootView;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
               // LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
               // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               // statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                //if (statusOfGPS) {
                //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                //}
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        mMap = map;

        //Ubicación actual
        /*if (statusOfGPS) {
            LocationServices.getFusedLocationProviderClient(getContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mMap.setMyLocationEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

                }
            });
        }*/

        mMap.getUiSettings().setZoomControlsEnabled(true);

        //map.setMyLocationEnabled(true);

        obtenerRestaurantes(mMap);

        mMap.setOnMarkerClickListener(marker -> {
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

        enableMyLocation();
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
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());

                    String strAddress = r.getDireccion()+","+r.getCiudad();
                    //Falla en la linea de abajo !!!!!!
                    List<Address> address  = geocoder.getFromLocationName(strAddress, 1);

                    Address location=address.get(0);
                    //
                    double latitude= location.getLatitude();
                    double longitude= location.getLongitude();
                    final LatLng p1 = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(p1).title(r.getNombre()).snippet(r.getDireccion() + "--" + r.getCiudad()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}