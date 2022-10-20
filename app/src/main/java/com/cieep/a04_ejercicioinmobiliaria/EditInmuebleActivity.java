package com.cieep.a04_ejercicioinmobiliaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cieep.a04_ejercicioinmobiliaria.configs.Constantes;
import com.cieep.a04_ejercicioinmobiliaria.databinding.ActivityEditInmuebleBinding;
import com.cieep.a04_ejercicioinmobiliaria.modelos.Inmueble;

public class EditInmuebleActivity extends AppCompatActivity {

    private ActivityEditInmuebleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditInmuebleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        organizarDatos();

        binding.btnCrearEditInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Inmueble inmueble;
                if ((inmueble = crearInmueble()) != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constantes.INMUEBLE, inmueble);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Toast.makeText(EditInmuebleActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnCancelarEditInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constantes.BORRAR, 1);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void organizarDatos(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            Inmueble inmueble = (Inmueble) bundle.getSerializable(Constantes.INMUEBLE);

            binding.txtCalleEditInumueble.setText(inmueble.getCalle());
            binding.txtCiudadEditInmueble.setText(inmueble.getCiudad());
            binding.txtCPEditInmueble.setText(inmueble.getCp());
            binding.txtNumeroEditInmueble.setText(String.valueOf(inmueble.getNumero()));
            binding.txtProvinciaEditInmueble.setText(inmueble.getProvincia());
            binding.rbValoracionEditInmueble.setRating(inmueble.getValoracion());
        }
        else {
            Toast.makeText(EditInmuebleActivity.this, "EL BUNDLE ESTA VACIO", Toast.LENGTH_SHORT).show();
        }
    }

    private Inmueble crearInmueble() {
        if (binding.txtCalleEditInumueble.getText().toString().isEmpty() ||
                binding.txtCiudadEditInmueble.getText().toString().isEmpty() ||
                binding.txtCPEditInmueble.getText().toString().isEmpty() ||
                binding.txtNumeroEditInmueble.getText().toString().isEmpty() ||
                binding.txtProvinciaEditInmueble.getText().toString().isEmpty() )
            return null;
        return new Inmueble(
                binding.txtCalleEditInumueble.getText().toString(),
                Integer.parseInt(binding.txtNumeroEditInmueble.getText().toString()),
                binding.txtCPEditInmueble.getText().toString(),
                binding.txtCiudadEditInmueble.getText().toString(),
                binding.txtProvinciaEditInmueble.getText().toString(),
                binding.rbValoracionEditInmueble.getRating()
        );
    }
}