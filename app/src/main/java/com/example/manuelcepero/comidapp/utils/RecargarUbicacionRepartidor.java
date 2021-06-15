package com.example.manuelcepero.comidapp.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.models.Usuario;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class RecargarUbicacionRepartidor extends Thread {

    private LocationManager locationManager;

    public RecargarUbicacionRepartidor(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void run() {
        while (true) {
            try {
//                SocketHandler.getOut().println(Mensajes.PETICION_RECARGAR_UBICACION + "--" + UsuarioActual.getId() + "--" + location.getLatitude() + "--" + location.getLongitude());
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
