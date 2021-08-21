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

public class ConsultarProductoN extends AppCompatActivity {

    EditText campoId,campoNombre,campoPC,campoPV, campoF;
    Button btnEdit, btndele, btnBC, btnBN, btnlista;
    private AdView mAdView;

    ConexionSQLiteHelperN conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);
        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        conn=new ConexionSQLiteHelperN(ConsultarProductoN.this,"bd_productos",null,1);

        campoId=  findViewById(R.id.campoId);
        campoNombre=  findViewById(R.id.campoNombre);
        campoPC=  findViewById(R.id.campoPC);
        campoPV=  findViewById(R.id.campoPV);
        campoF=  findViewById(R.id.campoF);
        btnEdit= findViewById(R.id.btnActualizar);
        btndele= findViewById(R.id.btnEliminar);
        btnBC= findViewById(R.id.btnCC);
        btnBN=findViewById(R.id.btnCN);
        btnlista= findViewById(R.id.lista);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto();
                Toast.makeText(getApplicationContext(),"PRODUCTO ACTUALIZADO",Toast.LENGTH_LONG).show();
                lista();
            }
        });
        btndele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsultarProductoN.this);
                builder.setMessage("¿Desea eliminar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                eliminarProducto();
                                Toast.makeText(getApplicationContext(),"PRODUCTO ELIMINADO",Toast.LENGTH_LONG).show();
                                campoId.setText("");
                                lista();
                                limpiar();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        btnBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarC();
            }
        });
        btnBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarN();
            }
        });
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista();
            }
        });

    }
    private void eliminarProducto() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={campoId.getText().toString()};

        db.delete(Utilidades.TABLA_PRODUCTO,Utilidades.CAMPO_CO+"=?",parametros);

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

        db.close();

    }

    private void consultarC() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={campoId.getText().toString()};

        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE+","+Utilidades.CAMPO_PC+","+Utilidades.CAMPO_PV+","+Utilidades.CAMPO_F+" FROM "+Utilidades.TABLA_PRODUCTO+" WHERE "+Utilidades.CAMPO_CO+"=? ",parametros);

            cursor.moveToFirst();
            campoNombre.setText(cursor.getString(0));
            campoPC.setText(cursor.getString(1));
            campoPV.setText(cursor.getString(2));
            campoF.setText(cursor.getString(3));


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El Código no existe",Toast.LENGTH_LONG).show();
            limpiar();
        }

    }

    private void consultarN() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={campoNombre.getText().toString()};

        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+Utilidades.CAMPO_CO+","+Utilidades.CAMPO_PC+","+Utilidades.CAMPO_PV+","+Utilidades.CAMPO_F+" FROM "+Utilidades.TABLA_PRODUCTO+" WHERE "+Utilidades.CAMPO_NOMBRE+"=? ",parametros);

            cursor.moveToFirst();
            campoId.setText(cursor.getString(0));
            campoPC.setText(cursor.getString(1));
            campoPV.setText(cursor.getString(2));
            campoF.setText(cursor.getString(3));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El Producto no existe",Toast.LENGTH_LONG).show();
            limpiar();
        }
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
