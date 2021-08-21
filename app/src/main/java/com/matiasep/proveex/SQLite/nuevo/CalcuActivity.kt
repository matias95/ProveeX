package com.matiasep.proveex.SQLite.nuevo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.matiasep.proveex.AcercaDeActivity
import com.matiasep.proveex.ContactoActivity
import com.matiasep.proveex.LoginUsuario
import com.matiasep.proveex.R
import kotlinx.android.synthetic.main.activity_calculadora.*

class CalcuActivity : AppCompatActivity() {
    private var num1 = 0.0
    private var num2 = 0.0
    private var operacion = 0
    private var mAdView: AdView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)

        //Banner
        MobileAds.initialize(
            this
        ) { }
        mAdView = findViewById(R.id.adView)
        val adRequest =
            AdRequest.Builder().build()
        mAdView?.loadAd(adRequest)

        resultadoText.text = "0"
        operacion = SIN_OPERACION

        unoBtn.setOnClickListener { numberPressed("1") }
        dosBtn.setOnClickListener { numberPressed("2") }
        tresBtn.setOnClickListener { numberPressed("3") }
        cuatroBtn.setOnClickListener { numberPressed("4") }
        cincoBtn.setOnClickListener { numberPressed("5") }
        seisBtn.setOnClickListener { numberPressed("6") }
        sieteBtn.setOnClickListener { numberPressed("7") }
        ochoBtn.setOnClickListener { numberPressed("8") }
        nueveBtn.setOnClickListener { numberPressed("9") }
        ceroBtn.setOnClickListener { numberPressed("0") }
        puntoBtn.setOnClickListener { numberPressed(".") }

        clearBtn.setOnClickListener { resetAll() }

        sumaBtn.setOnClickListener { operationPressed(SUMA) }
        restaBtn.setOnClickListener { operationPressed(RESTA) }
        multiplicarBtn.setOnClickListener { operationPressed(MULTIPLICACION) }
        divisionBtn.setOnClickListener { operationPressed(DIVISION) }

        igualBtn.setOnClickListener { resolvePressed() }
    }

    private fun numberPressed(num: String){
        if(resultadoText.text == "0" && num != ".") {
            resultadoText.text = "$num"
        } else {
            resultadoText.text = "${resultadoText.text}$num"
        }

        if(operacion == SIN_OPERACION){
            num1 = resultadoText.text.toString().toDouble()
        } else {
            num2 = resultadoText.text.toString().toDouble()
        }
    }

    private fun operationPressed(operacion: Int){
        this.operacion = operacion
        num1 = resultadoText.text.toString().toDouble()

        resultadoText.text = "0"
    }

    private fun resolvePressed(){

        val result = when(operacion) {
            SUMA -> num1 + num2
            RESTA -> num1 - num2
            MULTIPLICACION -> num1 * num2
            DIVISION -> num1 / num2
            else -> 0
        }

        num1 = result as Double

        resultadoText.text = if("$result".endsWith(".0")) { "$result".replace(".0","") } else { "%.2f".format(result) }
    }

    private fun resetAll(){
        resultadoText.text = "0"
        num1 = 0.0
        num2 = 0.0
    }

    companion object {
        const val SUMA = 1
        const val RESTA = 2
        const val MULTIPLICACION = 3
        const val DIVISION = 4
        const val SIN_OPERACION = 0
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = menuInflater
        inflater.inflate(R.menu.main2, menu)
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_acercade2) {
            val acercade = Intent(this, AcercaDeActivity::class.java)
            startActivity(acercade)
        } else if (id == R.id.action_contacto2) {
            val contacto = Intent(this, ContactoActivity::class.java)
            startActivity(contacto)
        }
        if (id == R.id.action_salir2) {
            val preferences =
                getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE)
            preferences.edit().clear().commit()
            FirebaseAuth.getInstance().signOut()
            val i = Intent(applicationContext, LoginUsuario::class.java)
            startActivity(i)
            finish()
            /*Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }
        //return true;
        return super.onOptionsItemSelected(item)
    }
}