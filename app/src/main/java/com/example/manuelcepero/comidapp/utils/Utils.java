package com.example.manuelcepero.comidapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static LatLng getDireccion(Context context, String strAddress){
        LatLng p1=null;
        try {
            Geocoder geocoder = new Geocoder(context);

            //Obtiene la longitud y latitud de la dirección
            List<Address> address;

            address = geocoder.getFromLocationName(strAddress, 5);

            Address location = address.get(0);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            p1 = new LatLng(latitude, longitude);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return p1;
    }
}
