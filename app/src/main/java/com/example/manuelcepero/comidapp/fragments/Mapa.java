package com.example.manuelcepero.comidapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
    LocationManager mLocationManager;

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);

                Location location = getLastKnownLocation();
                if (location!=null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
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
            }catch(NullPointerException e){
                System.out.println("Error de conexión");
            }
            return true;
        });

        enableMyLocation();
    }

    public void obtenerRestaurantes(GoogleMap map){
        SocketHandler.getOut().println(Mensajes.PETICION_OBTENER_NUMERO_RESTAURANTES);
        try {
            String received;
            String flag = "";
            String[] args;

            received = SocketHandler.getIn().readLine();
            args=received.split("--");
            flag = args[0];

            if (flag.equals(Mensajes.PETICION_OBTENER_NUMERO_RESTAURANTES_CORRECTO)) {
                int numRestaurantes = Integer.parseInt(args[1]);

                for (int i = 0; i < numRestaurantes; i++) {
                    SocketHandler.getOut().println(Mensajes.PETICION_MOSTRAR_RESTAURANTE_ID + "--" + i);
                    received = SocketHandler.getIn().readLine();
                    args = received.split("--");
                    flag = args[0];
                    if (flag.equals(Mensajes.PETICION_MOSTRAR_RESTAURANTE_ID_CORRECTO)) {
                        Restaurante r = new Restaurante(args[1], args[2], args[3], args[4], args[5], args[6]);
                        listaRestaurantes.add(r);

                        //
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                        String strAddress = r.getCiudad()+ " , " + r.getDireccion();

                        if (strAddress.equals(",")) {
                            System.out.println("Latitude and longitude not found");
                        } else {
                           /* List<Address> address = geocoder.getFromLocationName(strAddress, 1);
                            if (address.size() > 0) {
                                Address location = address.get(0);
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();*/
                                getLocationFromAddress(strAddress, r);//new LatLng(latitude, longitude);
                          //  }
                        }
                    }
                }
            }
        } catch(SocketException e){
            System.out.println("Error de conexión");
            Toast.makeText(getContext(), "Error de conexión",
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            //Añadir mensaje de error
            e.printStackTrace();
        }catch(NullPointerException e){
            System.out.println("Error de conexión");
            Toast.makeText(getContext(), "Error de conexión",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void getLocationFromAddress(String strAddress, Restaurante r){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String data="";
        //String loginURL = "http://api.positionstack.com/v1/forward?access_key=aade2175b6c36fee4eb350200beeb15c&query="+strAddress;
        //Funciona pero no coge todas las direcciones String loginURL = "https://open.mapquestapi.com/geocoding/v1/address?key=wo9t5r1WHIttHcIDB23tcPCkDXHger93&location="+strAddress;
        //String loginURL = "https://api.geocod.io/v1.6/geocode?q="+strAddress+"&api_key=170136c8887677c6567c75c75516083c56c5306";
        String loginURL = "https://api.tomtom.com/search/2/geocode/"+strAddress+".JSON?key=YqVUYViAyLspVe0z05bsLDvmlbPMIoI5";
        LatLng[] coordenadas = new LatLng[1];

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, loginURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            //API mapquestapi.com funciona pero no coge todas las direcciones
                            /*JSONArray ja = response.getJSONArray("results");
                            JSONObject ja2 = ja.getJSONObject(0);
                            JSONArray ja3 = ja2.getJSONArray("locations");
                            JSONObject ja4 = ja3.getJSONObject(0);
                            JSONObject ja5 = ja4.getJSONObject("latLng");
                            double latitud = ja5.getDouble("lat");
                            double longitud = ja5.getDouble("lng");*/


                            JSONArray ja = response.getJSONArray("results");
                            JSONObject ja2 = ja.getJSONObject(0);
                            JSONObject ja3 = ja2.getJSONObject("position");
                            double longitud = ja3.getDouble("lon");
                            double latitud = ja3.getDouble("lat");

                            coordenadas[0] = new LatLng(latitud,longitud);
                            addMarker(coordenadas[0], r);

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");

                    }
                }
        );
        requestQueue.add(jor);

        //return coordenadas[0];
    }

    public void addMarker(LatLng p1, Restaurante r){
        mMap.addMarker(new MarkerOptions().position(p1).title(r.getNombre()).snippet(r.getDireccion() + "--" + r.getCiudad()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }
}