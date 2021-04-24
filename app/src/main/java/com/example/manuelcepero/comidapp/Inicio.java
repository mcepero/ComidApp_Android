package com.example.manuelcepero.comidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.manuelcepero.comidapp.fragments.ListaRestaurantes;
import com.example.manuelcepero.comidapp.fragments.Mapa;
import com.example.manuelcepero.comidapp.fragments.Perfil;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Inicio extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        showSelectedFragment(new ListaRestaurantes());

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_navegacion);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.menu_lista_restaurantes){
                    showSelectedFragment(new ListaRestaurantes());
                }
                if (menuItem.getItemId()==R.id.menu_mapa){
                    showSelectedFragment(new Mapa());
                }
                if (menuItem.getItemId()==R.id.menu_perfil){
                    showSelectedFragment(new Perfil());
                }
                return true;
            }
        });
    }

    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(this.getClass().getName())
                .commit();
    }
}
