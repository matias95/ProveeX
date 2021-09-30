package com.matiasep.proveex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ContactoActivity extends AppCompatActivity  {
    private AdView mAdView;
    private AdView mAdView2;
    Button btnLin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        //Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        mAdView2 = findViewById(R.id.adView2);
        btnLin=(Button) findViewById(R.id.Lin);
        btnLin.setVisibility(View.INVISIBLE);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);

    }
    public void onClickLin(View v) {
        Uri uri = Uri.parse("https://www.linkedin.com/in/matias-pereyra");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void onClickMail(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:matiaspereyra02@gmail.com"));
        //emailIntent.setType("text/plain");
        //emailIntent.setType("message/rfc822");
        //emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto");
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email"));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactoActivity.this,"No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickYou(View v) {
        Uri uri = Uri.parse("https://www.youtube.com/channel/UCAiLTOmYyE9YHit8DQmGxJQ");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void irAweb(String d){
        Uri uri= Uri.parse(d);
        Intent intentNav= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intentNav);
    }
}

