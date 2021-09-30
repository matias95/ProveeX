package com.matiasep.proveex;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONObject;

public class RegistrarUsuario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    JsonRequest jrq;
    EditText txtUser, txtPwd, txtNames;
    Button btnRegistrar;
    private ProgressDialog mProcessDialog;
    private AdView mAdView;
    String nombre, user, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        txtUser = (EditText) findViewById(R.id.edtUser);
        txtPwd = (EditText) findViewById(R.id.edtPas);
        txtNames= (EditText) findViewById(R.id.edtNombre);
        mProcessDialog=new ProgressDialog(this);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrarUsu);
        rq = Volley.newRequestQueue(getApplicationContext());

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre=txtNames.getText().toString();
                user=txtUser.getText().toString();
                pwd=txtPwd.getText().toString();
                if(!user.isEmpty() && !pwd.isEmpty() && !nombre.isEmpty()){
                    registrar();
                }else {
                    Toast.makeText(RegistrarUsuario.this,getResources().getString(R.string.usu6),Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*<com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:passwordToggleEnabled="true"
                android:layout_weight="2">*/
    }

    void registrar(){

        mProcessDialog.setTitle(getResources().getString(R.string.usu7));
        mProcessDialog.setMessage(getResources().getString(R.string.usu8));
        mProcessDialog.setCancelable(false);
        mProcessDialog.show();

        String url = "https://matiaspereyrajap.000webhostapp.com/webservice/registrarUsuario.php?nombre=" +txtNames.getText().toString()+"&user="+ txtUser.getText().toString() +
                "&pwd=" + txtPwd.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProcessDialog.hide();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.usu9) + txtUser.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        mProcessDialog.hide();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.usu10) + txtUser.getText().toString(), Toast.LENGTH_SHORT).show();
        limpiarCajas();
        Intent i=new Intent(RegistrarUsuario.this,LoginUsuario.class);
        RegistrarUsuario.this.startActivity(i);
    }
    void limpiarCajas() {
        txtNames.setText("");
        txtUser.setText("");
        txtPwd.setText("");
    }
    private void compartirApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String aux = getResources().getString(R.string.c);
            aux = aux + "\n https://play.google.com/store/apps/details?id=com.matiasep.proveex";
            i.putExtra(Intent.EXTRA_TEXT, aux);
            startActivity(i);
        } catch (Exception e) {
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main22,menu );
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
        if (id == R.id.action_acercade22) {
            Intent acercade = new Intent(this, AcercaDeActivity.class);
            startActivity(acercade);
        }
        else if (id == R.id.action_contacto22) {
            Intent contacto = new Intent(this, ContactoActivity.class);
            startActivity(contacto);
        }
        if(id == R.id.action_compartir22){
            compartirApp();
        }
        else if (id == R.id.action_salir22) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }
}