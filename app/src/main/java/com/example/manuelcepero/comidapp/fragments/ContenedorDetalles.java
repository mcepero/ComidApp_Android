package com.example.manuelcepero.comidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.models.Restaurante;
import com.example.manuelcepero.comidapp.models.Valoracion;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ContenedorDetalles extends Fragment {
    private Restaurante r;
    private View view;
    TabLayout tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contenedor_restaurante, container, false);
        tabs = (TabLayout) view.findViewById(R.id.menusuperior);
        Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }

        DetallesRestaurante detallesRestaurante = new DetallesRestaurante();
        Bundle bundle = new Bundle();
        bundle.putParcelable("restaurante", r);
        detallesRestaurante.setArguments(bundle);

        showSelectedFragment(detallesRestaurante);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        DetallesRestaurante detallesRestaurante = new DetallesRestaurante();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("restaurante", r);
                        detallesRestaurante.setArguments(bundle);

                        showSelectedFragment(detallesRestaurante);
                        break;
                    case 1:
                        CartaRestaurante cartaRestaurante = new CartaRestaurante();
                        bundle = new Bundle();
                        bundle.putParcelable("restaurante", r);
                        cartaRestaurante.setArguments(bundle);

                        showSelectedFragment(cartaRestaurante);
                        break;
                    case 2:
                        Valoraciones valoraciones = new Valoraciones();
                        bundle = new Bundle();
                        bundle.putParcelable("restaurante", r);
                        valoraciones.setArguments(bundle);

                        showSelectedFragment(valoraciones);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void showSelectedFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerDetalles, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(this.getClass().getName())
                .commit();
    }
}
