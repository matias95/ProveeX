package com.matiasep.proveex;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class ScreenActivity extends AppCompatActivity {
    private final int DURACION_SPLASH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(ScreenActivity.this, PresentacionActivity.class);
                startActivity(intent);
                finish();
            }
            ;
        }, DURACION_SPLASH);

    }
}