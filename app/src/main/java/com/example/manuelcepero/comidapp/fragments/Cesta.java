package com.example.manuelcepero.comidapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelcepero.comidapp.BuildConfig;
import com.example.manuelcepero.comidapp.R;
import com.example.manuelcepero.comidapp.SocketHandler;
import com.example.manuelcepero.comidapp.adapters.ProductoAdapter;
import com.example.manuelcepero.comidapp.adapters.ProductoCestaAdapter;
import com.example.manuelcepero.comidapp.models.Producto;
import com.example.manuelcepero.comidapp.models.Restaurante;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.math.BigDecimal;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manuelcepero.comidapp.utils.Mensajes;
import com.example.manuelcepero.comidapp.utils.UsuarioActual;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import static android.app.Activity.RESULT_OK;

public class Cesta extends Fragment {

    private RecyclerView recyclerView;
    private ProductoCestaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<Producto> listaProductos = new ArrayList<>();
    TextView precio;
    private Restaurante r;
    double precioTotal=0;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("AejPzHTA5RM1P6pWbQFqJIoPswfJto150Xbsj_vmUyS1xEHETOYtokUzhZN-9adwFMu57qjvqKyueM7r");


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(getActivity(), "Pago correcto.", Toast.LENGTH_SHORT).show();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String fecha = format.format(new Date());
                        String idProductos="";
                        for (int i=0; i<listaProductos.size(); i++){
                            idProductos+=listaProductos.get(i).getId()+"/";
                        }
                        SocketHandler.getOut().println(Mensajes.PETICION_REALIZAR_PEDIDO+"--"+precioTotal+"--"+fecha+"--"+ UsuarioActual.getId()+"--"+listaProductos.get(0).getIdRestaurante()+"--"+idProductos);
                        SocketHandler.getOut().println(listaProductos.size());
                        listaProductos.clear();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Cesta())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(this.getClass().getName())
                                .commit();
                        recargarPrecioTotal();
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(getActivity(), "Pago cancelado.", Toast.LENGTH_SHORT).show();
                    }
                    else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Toast.makeText(getActivity(), "La cesta está vacía.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cesta, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewCesta);
        precio = (TextView) v.findViewById(R.id.precioTotal);
        Button botonPago = v.findViewById(R.id.realizarPago);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        /*Bundle args = this.getArguments();
        if (args != null) {
            r= args.getParcelable("restaurante");
        }*/
        recargarPrecioTotal();

        adapter = new ProductoCestaAdapter(getContext(), listaProductos, this);
        recyclerView.setAdapter(adapter);

        botonPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(precioTotal), "EUR", "Being payment for items ordered" ,
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getActivity(), PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                mStartForResult.launch(intent);
            }
        });

        return v;
    }

    public static ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public static void setListaProductos(ArrayList<Producto> listaProductos) {
        Cesta.listaProductos = listaProductos;
    }

    public void recargarPrecioTotal(){
        DecimalFormat df = new DecimalFormat("#.##");

        precioTotal=0;
        for (int i=0; i<listaProductos.size(); i++){
            precioTotal+=listaProductos.get(i).getPrecio();
        }
        precio.setText("Total: " + df.format(precioTotal)+"€");
    }
}
