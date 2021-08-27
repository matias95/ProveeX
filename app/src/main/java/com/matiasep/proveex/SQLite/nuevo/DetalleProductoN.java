package com.matiasep.proveex.SQLite.nuevo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.matiasep.proveex.SQLite.nuevo.ProductoN;

public class DetalleProductoN extends AppCompatActivity {

    EditText campoId,campoNombre,campoPC,campoPV,campoF;
    Button btnEdit, btndele,btnlista;
    private AdView mAdView;


    private ProductoN productoN;
    //Integer codigo=0;
    ConexionSQLiteHelperN conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_p);

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        campoId=  findViewById(R.id.campoId);
        campoNombre=  findViewById(R.id.campoNombre);
        campoPC=  findViewById(R.id.campoPC);
        campoPV=  findViewById(R.id.campoPV);
        campoF=  findViewById(R.id.campoF);
        btnEdit= findViewById(R.id.btnActualizar);
        btndele= findViewById(R.id.btnEliminar);
        btnlista=findViewById(R.id.listaS);

        /*if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                codigo = Integer.parseInt(null);
            } else {
                codigo = extras.getInt("CO");
            }
        } else {
            codigo = (Integer) savedInstanceState.getSerializable("CO");
        }*/

        conn= new ConexionSQLiteHelperN(getApplicationContext(),"bd_productos",null,1);

        //verProducto(codigo);

        /*if(productoN != null){
            campoId.setText(productoN.getCodigo());
            campoNombre.setText(productoN.getNombre());
            campoPC.setText(productoN.getPreciocosto());
            campoPV.setText(productoN.getPrecioventa());
            campoF.setText(productoN.getFabricante());
        }*/

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto();
            }
        });
        btndele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleProductoN.this);
                builder.setMessage("¿Desea eliminar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                eliminarProducto();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista();
            }
        });

    }
    /*private void verProducto(int codigo) {

        SQLiteDatabase db=conn.getReadableDatabase();

        ProductoN productoN=null;
        Cursor cursorPN;

        cursorPN=db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PRODUCTO + " WHERE codigo = " + codigo + " LIMIT 1",null);

        if (cursorPN.moveToFirst()){
            productoN =new ProductoN();
            productoN.setCodigo(cursorPN.getInt(0));
            productoN.setNombre(cursorPN.getString(1));
            productoN.setPreciocosto(cursorPN.getString(2));
            productoN.setPrecioventa(cursorPN.getString(3));
            productoN.setFabricante(cursorPN.getString(4));

            cursorPN.close();

            return;
        }
    }*/
    private void eliminarProducto() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={campoId.getText().toString()};

        db.delete(Utilidades.TABLA_PRODUCTO,Utilidades.CAMPO_CO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"PRODUCTO ELIMINADO",Toast.LENGTH_LONG).show();
        campoId.setText("");
        limpiar();
        db.close();
    }

    private void actualizarProducto() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={campoId.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE,campoNombre.getText().toString());
        values.put(Utilidades.CAMPO_PC,campoPC.getText().toString());
        values.put(Utilidades.CAMPO_PV,campoPV.getText().toString());
        values.put(Utilidades.CAMPO_F,campoF.getText().toString());

        db.update(Utilidades.TABLA_PRODUCTO,values,Utilidades.CAMPO_CO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"PRODUCTO ACTUALIZADO",Toast.LENGTH_LONG).show();
        db.close();

    }
    private void initValues(){
        productoN = (ProductoN) getIntent().getExtras().getSerializable("itemDetail");

        campoId.setText(productoN.getCodigo());
        campoNombre.setText(productoN.getNombre());
        campoPC.setText(productoN.getPreciocosto());
        campoPV.setText(productoN.getPrecioventa());
        campoF.setText(productoN.getFabricante());
    }
    private void limpiar(){
        campoId.setText("");
        campoNombre.setText("");
        campoPC.setText("");
        campoPV.setText("");
        campoF.setText("");
    }
    private void lista(){
        Intent intent = new Intent(this, ListaProdRecycler.class);
        startActivity(intent);
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
            Intent acerca = new Intent(this, AcercaDeActivity.class);
            startActivity(acerca);
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
