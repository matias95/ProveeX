package com.matiasep.proveex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class PresentacionActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion=preferences.getBoolean("sesion",false);
                if(sesion){
                    //Usuario usuario = new Usuario();
                    Intent i=new Intent(getApplicationContext(),MenuInicio.class);
                    //i.putExtra(MainActivity.nombres, usuario.getNombre());
                    startActivity(i);
                    finish();
                }else{
                    Intent i=new Intent(getApplicationContext(),LoginUsuario.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 1000);
    }
}