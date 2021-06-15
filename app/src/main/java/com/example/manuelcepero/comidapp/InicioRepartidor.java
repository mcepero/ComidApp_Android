package com.example.manuelcepero.comidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.manuelcepero.comidapp.fragments.Cesta;
import com.example.manuelcepero.comidapp.fragments.ListaRestaurantes;
import com.example.manuelcepero.comidapp.fragments.Mapa;
import com.example.manuelcepero.comidapp.fragments.Perfil;
import com.example.manuelcepero.comidapp.fragments.PerfilRepartidor;
import com.example.manuelcepero.comidapp.fragments.UltimosPedidosRepartidor;
import com.example.manuelcepero.comidapp.utils.RecargarUbicacionRepartidor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InicioRepartidor extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_repartidor);
        showSelectedFragment(new UltimosPedidosRepartidor());

       // RecargarUbicacionRepartidor recargarUbicacion = new RecargarUbicacionRepartidor(getApplicationContext());
      //  recargarUbicacion.start();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_navegacion_repartidor);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.menu_repartidor_pedidos){
                    showSelectedFragment(new UltimosPedidosRepartidor());
                }
                if (menuItem.getItemId()==R.id.menu_repartidor_perfil){
                    showSelectedFragment(new PerfilRepartidor());
                }
                return true;
            }
        });
    }

    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.containerRepartidor, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
