package com.matiasep.proveex;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.matiasep.proveex.SQLite.nuevo.CalcuActivity;
import com.matiasep.proveex.SQLite.nuevo.ConsultarProductoN;
import com.matiasep.proveex.SQLite.nuevo.ListaProdRecycler;
import com.matiasep.proveex.SQLite.nuevo.RegistroProductoN;

public class MenuInicio extends AppCompatActivity {

    RelativeLayout layoutFondo;
    GridLayout gridMenu;
    CardView cardCons,cardRegis,cardlista,cardcalc;
    ProgressDialog mProcessDialog;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        layoutFondo=findViewById(R.id.idLayoutFondo);
        cardCons=findViewById(R.id.cardCons);
        cardRegis=findViewById(R.id.cardRegis);
        cardlista=findViewById(R.id.cardlista);
        cardcalc=findViewById(R.id.cardcalc);

        eventosMenu();
    }

    private void eventosMenu(){

        cardCons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Consultar Producto", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ConsultarProductoN.class);
                startActivity(i);
            }
        });

        cardRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Registrar Productos", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), RegistroProductoN.class);
                startActivity(i);
            }
        });

        cardlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Lista de Productos", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ListaProdRecycler.class);
                startActivity(i);
            }
        });

        cardcalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Calculadora", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), CalcuActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main2,menu );
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_acercade2) {
            Intent acercade = new Intent(this, AcercaDeActivity.class);
            startActivity(acercade);
        }
        else if (id == R.id.action_contacto2) {
            Intent contacto = new Intent(this, ContactoActivity.class);
            startActivity(contacto);
        }
        if (id == R.id.action_salir2) {
            SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            FirebaseAuth.getInstance().signOut();

            Intent i=new Intent(getApplicationContext(),LoginUsuario.class);
            startActivity(i);
            finish();
            /*Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }
}