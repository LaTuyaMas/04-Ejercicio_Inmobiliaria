package com.cieep.a04_ejercicioinmobiliaria;

import android.content.Intent;
import android.os.Bundle;

import com.cieep.a04_ejercicioinmobiliaria.configs.Constantes;
import com.cieep.a04_ejercicioinmobiliaria.modelos.Inmueble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import com.cieep.a04_ejercicioinmobiliaria.databinding.ActivityMainBinding;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int currentlyEditing;
    private ActivityMainBinding binding;
    private ArrayList<Inmueble> inmueblesList;

    private ActivityResultLauncher<Intent> crearInmuebleLauncher;
    private ActivityResultLauncher<Intent> editarInmuebleLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inmueblesList = new ArrayList<>();
        inicializaLunchers();

        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               crearInmuebleLauncher.launch(new Intent(MainActivity.this, AddInmuebleActivity.class));
            }
        });
    }

    private void inicializaLunchers() {
        crearInmuebleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                if (result.getData().getExtras() != null) {
                                    if (result.getData().getExtras().getSerializable(Constantes.INMUEBLE) != null) {
                                        Inmueble inmueble = (Inmueble) result.getData().getExtras().getSerializable(Constantes.INMUEBLE);
                                        inmueblesList.add(inmueble);
                                        mostrarInmueblesContenedor();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "El bundle no lleva el tag INMUEBLE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "NO HAY BUNDLE EN EL INTENT", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT EN EL RESULT", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Ventana Cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        editarInmuebleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_CANCELED){
                            if (result.getData() != null){
                                if (result.getData().getExtras() != null){
                                    if (result.getData().getExtras().getInt(Constantes.BORRAR) == 1){
                                        inmueblesList.remove(currentlyEditing);
                                        Toast.makeText(MainActivity.this, "Borrado con exito", Toast.LENGTH_SHORT).show();
                                        mostrarInmueblesContenedor();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "El bundle no lleva el tag BORRAR", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "NO HAY BUNDLE EN EL INTENT", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                if (result.getData().getExtras() != null) {
                                    if (result.getData().getExtras().getSerializable(Constantes.INMUEBLE) != null) {
                                        Inmueble inmueble = (Inmueble) result.getData().getExtras().getSerializable(Constantes.INMUEBLE);
                                        inmueblesList.set(currentlyEditing, inmueble);
                                        Toast.makeText(MainActivity.this, "Editado con exito", Toast.LENGTH_SHORT).show();
                                        mostrarInmueblesContenedor();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "El bundle no lleva el tag INMUEBLE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "NO HAY BUNDLE EN EL INTENT", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT EN EL RESULT", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Ventana Cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void mostrarInmueblesContenedor() {
        binding.contentMain.contenedor.removeAllViews();

        for (int i = 0; i < inmueblesList.size(); i++) {
            // DATO
            Inmueble inmueble = inmueblesList.get(i);
            // PLANTILLA
            View inmuebleView = LayoutInflater.from(MainActivity.this).inflate(R.layout.inmueble_model_view, null);
            TextView lblCalle = inmuebleView.findViewById(R.id.lblCalleModelInmueble);
            TextView lblNumero = inmuebleView.findViewById(R.id.lblNumeroModelInmueble);
            TextView lblCiudad = inmuebleView.findViewById(R.id.lblCiudadModelInmueble);
            TextView lblProvincia = inmuebleView.findViewById(R.id.lblProvinciaModelInmueble);
            RatingBar rbValoracion = inmuebleView.findViewById(R.id.rbValoracionModelInmueble);
            // ASIGNO VALORES desde DATO a PLANTILLA
            lblCalle.setText(inmueble.getCalle());
            lblNumero.setText(String.valueOf(inmueble.getNumero()));
            lblCiudad.setText(inmueble.getCiudad());
            lblProvincia.setText(inmueble.getProvincia());
            rbValoracion.setRating(inmueble.getValoracion());
            // INSERTO EL ELEMENTO EN EL CONTENEDOR
            binding.contentMain.contenedor.addView(inmuebleView);

            // CREAR EL EVENTO DE CLICK
            int finalI = i;
            inmuebleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentlyEditing = finalI;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constantes.INMUEBLE, inmueblesList.get(currentlyEditing));
                    Intent intent = new Intent(MainActivity.this, EditInmuebleActivity.class);
                    intent.putExtras(bundle);
                    editarInmuebleLauncher.launch(intent);
                }
            });
        }
    }


}