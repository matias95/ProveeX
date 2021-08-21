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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ConsultarProductos extends AppCompatActivity {
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    EditText edtCodigo,edtDescripcion,edtPrecioCosto,edtPrecioVenta,edtFabricante;
    Button btnFoto, btnBuscarCod, btnEliminarCampo, btnEditar, btnEliminar, btnBuscarNom;
    ImageView imgFoto;
    ProgressDialog mProcess;
    RequestQueue requestQueue;

    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    String codigo, descripcion, fabricante, imagen;
    Integer precio;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd, mInterstitialAd1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_productos);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Interstical
        AdRequest adRequest = new AdRequest.Builder().build();

        //Banner

        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        mProcess=new ProgressDialog(this);
        edtCodigo=(EditText)findViewById(R.id.codiProducCons);
        edtDescripcion=(EditText)findViewById(R.id.descripcionCons);
        edtPrecioCosto=(EditText)findViewById(R.id.preciocCons);
        edtPrecioVenta=(EditText)findViewById(R.id.preciovCons);
        edtFabricante=(EditText)findViewById(R.id.fabricanteCons);
        btnBuscarCod=(Button)findViewById(R.id.btnBuscarCons);
        btnBuscarNom=(Button)findViewById(R.id.btnBuscarNomCons);
        btnFoto=(Button)findViewById(R.id.btnFotoCons);
        btnEliminarCampo=(Button)findViewById(R.id.btnEliCampos);
        btnEliminar=(Button)findViewById(R.id.btnEliminarCons);
        btnEditar=(Button)findViewById(R.id.btnEditarCons);
        imgFoto=(ImageView)findViewById(R.id.imgFotoCons);
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        //Permisos
        if(solicitaPermisosVersionesSuperiores()){
            btnFoto.setEnabled(true);
        }else{
            btnFoto.setEnabled(false);
        }

        btnEliminarCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarFomulario();
                cerrarTecladoMovil();
            }
        });
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogOpciones();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigo=edtCodigo.getText().toString();
                descripcion=edtDescripcion.getText().toString();

                if(!codigo.isEmpty() && !descripcion.isEmpty()){
                    ejecutarEliminar("https://matiaspereyrajap.000webhostapp.com/webservice/eliminar_producto.php");
                }else{
                    Toast.makeText(ConsultarProductos.this,"Busque el producto para ser eliminado",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigo=edtCodigo.getText().toString();
                descripcion=edtDescripcion.getText().toString();
                if(!codigo.isEmpty() && !descripcion.isEmpty()){
                    ejecutarEditar("https://matiaspereyrajap.000webhostapp.com/webservice/editar_producto.php");
                }else{
                    Toast.makeText(ConsultarProductos.this,"Busque el producto y suba la foto del mismo para ser actualizado",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnBuscarCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigo=edtCodigo.getText().toString();

                if(!codigo.isEmpty()){
                    buscarProducto("https://matiaspereyrajap.000webhostapp.com/webservice/buscar_producto.php?codigo="+edtCodigo.getText().toString());
                    cerrarTecladoMovil();
                }else{
                    Toast.makeText(ConsultarProductos.this,"Introduzca el CODIGO del producto",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBuscarNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descripcion=edtDescripcion.getText().toString();

                if(!descripcion.isEmpty()){
                    buscarNom("https://matiaspereyrajap.000webhostapp.com/webservice/buscar_productoNom.php?producto="+edtDescripcion.getText().toString());
                    cerrarTecladoMovil();
                }else{
                    Toast.makeText(ConsultarProductos.this,"Introduzca el NOMBRE del producto",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void buscarProducto(String URL){

        mProcess.setTitle("Consultando...");
        mProcess.setMessage("Consultando Producto");
        mProcess.setCancelable(false);
        mProcess.show();
        /*jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mProcess.hide();
                Producto miProducto=new Producto();
                JSONArray json=response.optJSONArray("producto");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    miProducto.setProducto(jsonObject.optString("producto"));
                    miProducto.setPrecio(jsonObject.optInt("precio"));
                    miProducto.setFabricante(jsonObject.optString("fabricante"));
                    miProducto.setDato(jsonObject.optString("imagen"));

                }catch (JSONException e){
                    e.printStackTrace();
                }
                edtProducto.setText(miProducto.getProducto());
                edtPrecio.setText(miProducto.getPrecio());
                edtFabricante.setText(miProducto.getFabricante());

                if(miProducto.getImagen()!=null){
                    imgFoto.setImageBitmap(miProducto.getImagen());
                }else{
                    imgFoto.setImageResource(R.drawable.sinimagen);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProcess.hide();
                Toast.makeText(getApplicationContext(),"ERROR DE CONEXIÓN",Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);*/

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                mProcess.hide();
                JSONObject jsonObject=null;
                for (int i=0; i<response.length(); i++){
                    try {
                        jsonObject=response.getJSONObject(i);
                        edtDescripcion.setText(jsonObject.getString("producto"));
                        edtPrecioCosto.setText(jsonObject.getString("preciocosto"));
                        edtPrecioVenta.setText(jsonObject.getString("precioventa"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));

                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), "Error al Extraer datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                mProcess.hide();
            }
        }
        );
        //requestQueue= Volley.newRequestQueue(this);
        //requestQueue.add(jsonArrayRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }
    private void buscarNom(String URL){
        mProcess.setTitle("Buscando...");
        mProcess.setMessage("Buscando Producto");
        mProcess.setCancelable(false);
        mProcess.show();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                mProcess.hide();
                JSONObject jsonObject=null;
                for (int i=0; i<response.length(); i++){
                    try {
                        jsonObject=response.getJSONObject(i);
                        edtCodigo.setText(jsonObject.getString("codigo"));
                        edtPrecioCosto.setText(jsonObject.getString("preciocosto"));
                        edtPrecioVenta.setText(jsonObject.getString("precioventa"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));

                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), "Error al Extraer Datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                mProcess.hide();
            }
        }
        );
        //requestQueue= Volley.newRequestQueue(this);
        //requestQueue.add(jsonArrayRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void Archivo(View view){
        Toast.makeText(this, "Productos", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(ConsultarProductos.this);
        builder.setTitle("Seleccione Una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //Toast.makeText(getApplicationContext(),"Foto",Toast.LENGTH_SHORT).show();
                    abriCamara();
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, COD_FOTO);*/
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
        AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ConsultarProductos.this);//estamos en fragment
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
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ConsultarProductos.this);
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

    private void ejecutarEditar(String URL){

        mProcess.setTitle("Actualizando...");
        mProcess.setMessage("Actualizando Producto");
        mProcess.setCancelable(false);
        mProcess.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProcess.hide();
                Toast.makeText(getApplicationContext(), "PRODUCTO ACTUALIZADO", Toast.LENGTH_SHORT).show();
                limpiarFomulario();
                Intent i=new Intent(ConsultarProductos.this,ListaProductos.class);
                ConsultarProductos.this.startActivity(i);
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
                parametros.put("producto", edtDescripcion.getText().toString());
                parametros.put("preciocosto", edtPrecioCosto.getText().toString());
                parametros.put("precioventa", edtPrecioVenta.getText().toString());
                parametros.put("fabricante", edtFabricante.getText().toString());
                parametros.put("imagen",imagen);
                parametros.put("codigo", edtCodigo.getText().toString());

                return parametros;
            }
        };
        //requestQueue= Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }
    private void ejecutarEliminar(String URL){

        mProcess.setTitle("Eliminando...");
        mProcess.setMessage("Eliminando Producto");
        mProcess.setCancelable(false);
        mProcess.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProcess.hide();
                Toast.makeText(getApplicationContext(), "PRODUCTO ELIMINADO", Toast.LENGTH_SHORT).show();
                limpiarFomulario();
                Intent i=new Intent(ConsultarProductos.this,ListaProductos.class);
                ConsultarProductos.this.startActivity(i);
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
                Map<String, String> parametros = new HashMap<>();
                parametros.put("codigo", edtCodigo.getText().toString());
                parametros.put("producto", edtDescripcion.getText().toString());
                return parametros;
            }
        };
        //requestQueue= Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
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
            Intent contacto = new Intent(this, AcercaDeActivity.class);
            startActivity(contacto);
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