package com.matiasep.proveex.webservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.matiasep.proveex.AcercaDeActivity;
import com.matiasep.proveex.ContactoActivity;
import com.matiasep.proveex.LoginUsuario;
import com.matiasep.proveex.MenuInicio;
import com.matiasep.proveex.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaProductos extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    //private static final String URL_PRODUCTOS="";
    RequestQueue requestQueue;
    RecyclerView recyclerProductos;
    ArrayList<Producto> listaProductos;
    private ProgressDialog mProcessDialog;
    JsonObjectRequest jsonObjectRequest;
    ImageView imagSinConexion;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //requestQueue=Volley.newRequestQueue(getApplicationContext());
        listaProductos=new ArrayList<Producto>();
        recyclerProductos= (RecyclerView) findViewById(R.id.listaProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerProductos.setHasFixedSize(true);
        imagSinConexion=(ImageView) findViewById(R.id.SinConexion);
        imagSinConexion.setVisibility(View.INVISIBLE);
        mProcessDialog=new ProgressDialog(this);

        ConnectivityManager con= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=con.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            imagSinConexion.setVisibility(View.INVISIBLE);
            cargarWebService();
        }else {
            imagSinConexion.setVisibility(View.VISIBLE);
            Toast.makeText(ListaProductos.this,"Verifique su conexión a internet e Intente nuevamente",Toast.LENGTH_LONG).show();
        }

    }
    private void cargarWebService() {

        mProcessDialog.setTitle("Cargando...");
        mProcessDialog.setMessage("Cargando Lista de Productos");
        mProcessDialog.setCancelable(false);
        mProcessDialog.show();

        String url="https://matiaspereyrajap.000webhostapp.com/webservice/listar_producto.php";
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        //requestQueue.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProcessDialog.hide();
        Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Producto producto=null;

        JSONArray json=response.optJSONArray("producto");

        try {

            for (int i=0;i<json.length();i++){
                producto=new Producto();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                producto.setCodigo(jsonObject.optString("codigo"));
                producto.setDescripcion(jsonObject.optString("producto"));
                //producto.setId_usu(jsonObject.optInt("id_usu"));
                producto.setPreciocosto(jsonObject.optInt("preciocosto"));
                producto.setPrecioventa(jsonObject.optInt("precioventa"));
                producto.setFabricante(jsonObject.optString("fabricante"));
                producto.setDato(jsonObject.optString("imagen"));
                listaProductos.add(producto);
            }
            mProcessDialog.hide();
            ProductosAdapter adapter=new ProductosAdapter(listaProductos);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Selección: "+listaProductos.get(recyclerProductos.getChildAdapterPosition(view)).getDescripcion(),Toast.LENGTH_SHORT).show();
                }
            });
            recyclerProductos.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error de conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            mProcessDialog.hide();
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

        return super.onOptionsItemSelected(item);
    }
}