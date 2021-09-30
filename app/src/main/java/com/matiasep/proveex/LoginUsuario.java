package com.matiasep.proveex;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginUsuario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    JsonRequest jrq;
    EditText txtUser, txtPwd;
    Button btnSesion, btnRegistrar, btnGoogle;
    String user, pwd;
    private ProgressDialog mProcessDialog;
    private AdView mAdView;
    private AdView mAdView2;
    private static final int googleSignIn = 9001;
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        mAdView2 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);


        // Initialize Firebase Auth
        //mAuth = FirebaseAuth.getInstance();

        txtUser = (EditText) findViewById(R.id.txtuser);
        txtPwd = (EditText) findViewById(R.id.txtpwd);
        mProcessDialog=new ProgressDialog(this);
        btnSesion = (Button) findViewById(R.id.btnsesion);
        btnRegistrar = (Button) findViewById(R.id.btnregistrar);
        btnGoogle=(Button) findViewById(R.id.googlebutton);
        btnGoogle.setVisibility(View.INVISIBLE);
        rq = Volley.newRequestQueue(getApplicationContext());

        recuperarPreferencias();

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=txtUser.getText().toString();
                pwd=txtPwd.getText().toString();
                if(!user.isEmpty() || !pwd.isEmpty()){
                    iniciar_sesion();
                }else {
                    Toast.makeText(LoginUsuario.this,getResources().getString(R.string.usu), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar_usu();
            }
        });
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/

    }
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == googleSignIn) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Error en el inicio de sesión de Google", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, googleSignIn);
    }
    private void updateUI(FirebaseUser user) {

    }*/
    private void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user", user);
        editor.putString("pwd", pwd);
        editor.putBoolean("sesion",true);
        editor.commit();
    }
    private void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
        txtUser.setText(preferences.getString("user",""));
        txtPwd.setText(preferences.getString("pwd",""));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProcessDialog.hide();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.usu1), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.usu2) + txtUser.getText().toString(), Toast.LENGTH_SHORT).show();
        Usuario usuario = new Usuario();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            usuario.setUser(jsonObject.optString("user", user));
            usuario.setPwd(jsonObject.optString("pwd", pwd));
            usuario.setNombre(jsonObject.optString("nombre"));

            mProcessDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginUsuario.this,getResources().getString(R.string.usu3), Toast.LENGTH_SHORT).show();
            mProcessDialog.hide();
        }
        guardarPreferencias();
        Intent intencion = new Intent(getApplicationContext(), MenuInicio.class);
        //intencion.putExtra(MainActivity.nombres, usuario.getNombre());
        startActivity(intencion);
        finish();
    }

    void iniciar_sesion() {

        mProcessDialog.setTitle(getResources().getString(R.string.usu4));
        mProcessDialog.setMessage(getResources().getString(R.string.usu5));
        mProcessDialog.setCancelable(false);
        mProcessDialog.show();

        String url = "https://matiaspereyrajap.000webhostapp.com/webservice/sesion.php?user=" + txtUser.getText().toString() +
                "&pwd=" + txtPwd.getText().toString();
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    void registrar_usu(){
        Intent i = new Intent(this, RegistrarUsuario.class);
        startActivity(i);
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