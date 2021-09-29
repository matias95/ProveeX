package com.matiasep.proveex.SQLite.nuevo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.matiasep.proveex.AcercaDeActivity;
import com.matiasep.proveex.ContactoActivity;
import com.matiasep.proveex.LoginUsuario;
import com.matiasep.proveex.MenuInicio;
import com.matiasep.proveex.R;

public class RegistroProductoN extends AppCompatActivity {

    EditText campoId,campoNombre,campoPC,campoPV, campoF;
    Button btnFoto,btnGuarda, btnlista, btnlimpiar;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_p);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //InterstitialAd
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5454483537308682/7700721695");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                registrarProducto();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.re), Toast.LENGTH_LONG).show();
                limpiar();
                lista();
            }

        });


        //Banner
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        campoId= findViewById(R.id.campoId);
        campoNombre=  findViewById(R.id.campoNombre);
        campoPC=  findViewById(R.id.campoPC);
        campoPV=  findViewById(R.id.campoPV);
        campoF= findViewById(R.id.campoF);
        btnGuarda= findViewById(R.id.btnRegistro);
        btnlimpiar=findViewById(R.id.btnEliminarC);
        btnlista=findViewById(R.id.btnlistaC);

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();

                }else {
                    registrarProducto();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.re), Toast.LENGTH_LONG).show();
                    limpiar();
                    lista();
                }

            }
        });
        btnlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
            }
        });
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista();
            }
        });

    }

    private void registrarProducto() {
        ConexionSQLiteHelperN conn=new ConexionSQLiteHelperN(this,"bd_productos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_CO,campoId.getText().toString());
        values.put(Utilidades.CAMPO_NOMBRE,campoNombre.getText().toString());
        values.put(Utilidades.CAMPO_PC,campoPC.getText().toString());
        values.put(Utilidades.CAMPO_PV,campoPV.getText().toString());
        values.put(Utilidades.CAMPO_F,campoF.getText().toString());
        Long idResultante=db.insert(Utilidades.TABLA_PRODUCTO,Utilidades.CAMPO_CO,values);

        db.close();
    }
    private void lista(){
        Intent intent = new Intent(this, ListaProdRecycler.class);
        startActivity(intent);
    }
    private void limpiar(){
        campoId.setText("");
        campoNombre.setText("");
        campoPC.setText("");
        campoPV.setText("");
        campoF.setText("");

    }
    private void compartirApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String aux = getResources().getString(R.string.c);
            aux = aux + "\n https://play.google.com/store/apps/details?id=com.matiasep.proveex"+getBaseContext().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, aux);
            startActivity(i);
        } catch (Exception e) {
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main21,menu );
        inflater.inflate(R.menu.maincal,menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_menu21:
                Intent mn = new Intent(this, MenuInicio.class);
                startActivity(mn);
                break;
            case R.id.action_acercade21:
                Intent acercade = new Intent(this, AcercaDeActivity.class);
                startActivity(acercade);
                break;
            case R.id.action_contacto21:
                Intent contacto = new Intent(this, ContactoActivity.class);
                startActivity(contacto);
                break;
            case R.id.action_compartir21:
                compartirApp();
                break;
            case R.id.action_salir21:
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                //FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getApplicationContext(), LoginUsuario.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_calc:
                Toast.makeText(this, getResources().getString(R.string.consu5), Toast.LENGTH_SHORT).show();
                Intent cal = new Intent(this, CalcuActivity.class);
                startActivity(cal);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
