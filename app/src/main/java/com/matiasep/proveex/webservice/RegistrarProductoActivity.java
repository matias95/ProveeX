package com.matiasep.proveex.webservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.matiasep.proveex.AcercaDeActivity;
import com.matiasep.proveex.ContactoActivity;
import com.matiasep.proveex.LoginUsuario;
import com.matiasep.proveex.MenuInicio;
import com.matiasep.proveex.R;
import com.matiasep.proveex.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegistrarProductoActivity extends AppCompatActivity{

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;
    private static final long GAME_LENGTH_MILLISECONDS = 3000;
    private static final String AD_UNIT_ID = "ca-app-pub-5454483537308682/9975083519";
    private static final String AD_UNIT_ID2 = "ca-app-pub-3940256099942544/1033173712";
    private static final String TAG = "MyActivity";

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    EditText edtCodigo,edtDescripcion,edtPrecioCosto,edtPrecioVenta,edtFabricante;
    Button btnRegistrarProduc, btnFoto, btnEliminarCampo;
    ImageView imgFoto;
    //Spinner comboPro;
    ProgressDialog mProcess;
    RequestQueue requestQueue;
    //RequestQueue requestQueue1;

    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    String codigo, descripcion, fabricante, imagen;
    Integer precio;

    ArrayList<String> listaProv;
    ArrayList<Usuario> provList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Interstical
        AdRequest adRequest = new AdRequest.Builder().build();

        /*mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                ejecutarServicio("https://matiaspereyrajap.000webhostapp.com/webservice/insertar_producto3.php");

            }
        });*/

        //Banner
        mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listaProv=new ArrayList<>();

        mProcess=new ProgressDialog(this);
        edtCodigo=(EditText)findViewById(R.id.codiProduc);
        edtDescripcion=(EditText)findViewById(R.id.producto);
        edtPrecioCosto=(EditText)findViewById(R.id.precioCosto);
        edtPrecioVenta=(EditText)findViewById(R.id.precioVenta);
        edtFabricante=(EditText)findViewById(R.id.fabricante);
        btnRegistrarProduc=(Button)findViewById(R.id.btnRegistrarPro);
        btnFoto=(Button)findViewById(R.id.btnFoto);
        btnEliminarCampo=(Button)findViewById(R.id.btnEliminarCampos);
        imgFoto=(ImageView)findViewById(R.id.imgFoto);
        //comboPro= (Spinner) findViewById(R.id.comboPro);
        /*<Spinner
                android:id="@+id/comboPro"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"/>*/
        requestQueue=Volley.newRequestQueue(getApplicationContext());

        consultarListaProv();
        /*comboPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String provee=comboPro.getItemAtPosition(comboPro.getSelectedItemPosition()).toString();
                //Toast.makeText(getApplicationContext(),provee,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });*/
        //Permisos
        if(solicitaPermisosVersionesSuperiores()){
            btnFoto.setEnabled(true);
        }else{
            btnFoto.setEnabled(false);
        }

        btnRegistrarProduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigo=edtCodigo.getText().toString();
                descripcion=edtDescripcion.getText().toString();
                if(!codigo.isEmpty() && !descripcion.isEmpty()){
                    ejecutarServicio("https://matiaspereyrajap.000webhostapp.com/webservice/insertar_producto3.php");
                }else{
                    Toast.makeText(RegistrarProductoActivity.this,"Completa todos los campos y foto para registrar",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogOpciones();
            }
        });

        btnEliminarCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarFomulario();
                cerrarTecladoMovil();
            }
        });

    }

    private void consultarListaProv() {
        String url="http://192.168.0.175/webservice/listar_provee.php";
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("usuario");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String country=jsonObject1.getString("id_usu");
                                listaProv.add(country);
                            }
                            //comboPro.setAdapter(new ArrayAdapter<String>(RegistrarProductoActivity.this, android.R.layout.simple_spinner_dropdown_item, listaProv));
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void Archivo(View view){
        Toast.makeText(this, "Productos Registrados", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ListaProductos.class);
        startActivity(i);
    }
    private void cerrarTecladoMovil() {
        if(getCurrentFocus()  != null){
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
    private void limpiarFomulario(){
        edtCodigo.setText("");
        edtDescripcion.setText("");
        edtPrecioCosto.setText("");
        edtPrecioVenta.setText("");
        edtFabricante.setText("");
        imgFoto.setImageResource(R.drawable.sinimagen);
    }

    private void mostrarDialogOpciones() {

        final CharSequence[] opciones={"Tomar Foto","Fotos del Celular","Cancelar"};
        AlertDialog.Builder builder=new AlertDialog.Builder(RegistrarProductoActivity.this);
        builder.setTitle("Seleccione Una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //Toast.makeText(getApplicationContext(),"Foto",Toast.LENGTH_SHORT).show();
                    abriCamara();
                }else{
                    if (opciones[i].equals("Fotos del Celular")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Seleccione Aplicación"), COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private void abriCamara() {
        File miFile=new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada=miFile.exists();
        String nombreImagen="";

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }

        if(isCreada==true){
            nombreImagen= (System.currentTimeMillis()/1000)+".jpg";
            //String nombre=consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombreImagen;//indicamos la ruta de almacenamiento

            fileImagen=new File(path);

            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            //startActivityForResult(intent,COD_FOTO);

            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getApplicationContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(getApplicationContext(),authorities,fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent,COD_FOTO);
            ////
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });
                bitmap= BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);
                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }

    //permisos
    ////////////////
    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getApplicationContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getApplicationContext().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(getApplicationContext(),"Permisos aceptados",Toast.LENGTH_SHORT);
                btnFoto.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegistrarProductoActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getApplicationContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistrarProductoActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }
    ///////////////

    private void ejecutarServicio(String URL){

        mProcess.setTitle("Registrando...");
        mProcess.setMessage("Registrando Producto");
        mProcess.setCancelable(false);
        mProcess.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                mProcess.hide();
                Toast.makeText(getApplicationContext(), "PRODUCTO REGISTRADO", Toast.LENGTH_SHORT).show();
                limpiarFomulario();
                Intent i=new Intent(RegistrarProductoActivity.this,ListaProductos.class);
                RegistrarProductoActivity.this.startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"ERROR DE CONEXIÓN",Toast.LENGTH_SHORT).show();
                mProcess.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen=convertirImgString(bitmap);
                Map<String, String> parametros = new HashMap<>();
                parametros.put("codigo", edtCodigo.getText().toString());
                parametros.put("producto", edtDescripcion.getText().toString());
                //parametros.put("id_usu", comboPro.getSelectedItem().toString());
                parametros.put("preciocosto", edtPrecioCosto.getText().toString());
                parametros.put("precioventa", edtPrecioVenta.getText().toString());
                parametros.put("fabricante", edtFabricante.getText().toString());
                parametros.put("imagen",imagen);

                return parametros;
            }
        };
        //requestQueue= Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);

        /*final String codigo=edtCodigo.getText().toString();
        final String producto=edtProducto.getText().toString();
        final int precio=Integer.parseInt(edtPrecio.getText().toString());
        final String fabricante=edtFabricante.getText().toString();
        final String imagen=convertirImgString(bitmap);

        Response.Listener<String> respoListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean sucess= jsonObject.getBoolean("sucess");
                    if(sucess){
                        mProcess.hide();
                        Toast.makeText(getApplicationContext(), "OPERACIÓN EXITOSA", Toast.LENGTH_SHORT).show();
                        limpiarFomulario();
                        Intent i=new Intent(RegistrarProductoActivity.this,ListaProductos.class);
                        RegistrarProductoActivity.this.startActivity(i);
                    }else {
                        AlertDialog.Builder builder= new AlertDialog.Builder(RegistrarProductoActivity.this);
                        builder.setMessage("Error de registro")
                                .setNegativeButton("Retry",null)
                                .create().show();
                        mProcess.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RegisterRequest registerRequest=new RegisterRequest(codigo,producto,precio,fabricante,respoListener);
        RequestQueue requestQueue= Volley.newRequestQueue(RegistrarProductoActivity.this);
        requestQueue.add(registerRequest);*/
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
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

        return super.onOptionsItemSelected(item);
    }


}