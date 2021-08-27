package com.matiasep.proveex.SQLite.nuevo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
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

import java.util.ArrayList;

public class ListaProdRecycler extends AppCompatActivity implements  SearchView.OnQueryTextListener{

    ArrayList<ProductoN> listaProducto;
    RecyclerView recyclerViewProductos;
    TextView imagSinConexion;
    SearchView svSearch;
    ProductosAdapterN adapter;
    private InterstitialAd mInterstitialAdr, mInterstitialAdc;

    ConexionSQLiteHelperN conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_p);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //InterstitialAd
        mInterstitialAdr = new InterstitialAd(this);
        mInterstitialAdc = new InterstitialAd(this);
        mInterstitialAdr.setAdUnitId("ca-app-pub-5454483537308682/7700721695");
        mInterstitialAdc.setAdUnitId("ca-app-pub-5454483537308682/7700721695");
        mInterstitialAdr.loadAd(new AdRequest.Builder().build());
        mInterstitialAdc.loadAd(new AdRequest.Builder().build());

        mInterstitialAdr.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAdr.loadAd(new AdRequest.Builder().build());
                registrar();

            }

        });
        mInterstitialAdc.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAdc.loadAd(new AdRequest.Builder().build());
                consultar();
            }

        });

        conn=new ConexionSQLiteHelperN(getApplicationContext(),"bd_productos",null,1);

        imagSinConexion= findViewById(R.id.SinConexionCal);
        imagSinConexion.setVisibility(View.INVISIBLE);
        svSearch = findViewById(R.id.svSearch);

        listaProducto=new ArrayList<>();

        consultarListaProductos();

        recyclerViewProductos= (RecyclerView) findViewById(R.id.listaProductosL);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        adapter=new ProductosAdapterN(listaProducto);
        recyclerViewProductos.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterstitialAdr.isLoaded()){
                    mInterstitialAdr.show();

                }else {
                    registrar();
                }
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterstitialAdc.isLoaded()){
                    mInterstitialAdc.show();

                }else {
                    consultar();
                }
            }
        });

        svSearch.setOnQueryTextListener(this);

    }
    private void registrar(){
        Intent intent = new Intent(this, RegistroProductoN.class);
        startActivity(intent);
    }
    private void consultar(){
        Intent intent = new Intent(this, ConsultarProductoN.class);
        startActivity(intent);
    }

    private void consultarListaProductos() {
        SQLiteDatabase db=conn.getReadableDatabase();

        ProductoN productoN=null;
       // listaUsuarios=new ArrayList<Usuario>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PRODUCTO,null);

        while (cursor.moveToNext()){
            productoN =new ProductoN();
            productoN.setCodigo(cursor.getInt(0));
            productoN.setNombre(cursor.getString(1));
            productoN.setPreciocosto(cursor.getString(2));
            productoN.setPrecioventa(cursor.getString(3));
            productoN.setFabricante(cursor.getString(4));

            listaProducto.add(productoN);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu21) {
            Intent mn = new Intent(this, MenuInicio.class);
            startActivity(mn);
        }
        else if (id == R.id.action_acercade21) {
            Intent acercade = new Intent(this, AcercaDeActivity.class);
            startActivity(acercade);
        }
        if (id == R.id.action_contacto21) {
            Intent contacto = new Intent(this, ContactoActivity.class);
            startActivity(contacto);
        }
        else if (id == R.id.action_salir21) {
            SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            FirebaseAuth.getInstance().signOut();

            Intent i=new Intent(getApplicationContext(), LoginUsuario.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.action_calc) {
            Toast.makeText(this, "CALCULADORA", Toast.LENGTH_SHORT).show();
            Intent cal = new Intent(this, CalcuActivity.class);
            startActivity(cal);
        }
        return super.onOptionsItemSelected(item);
    }

}
