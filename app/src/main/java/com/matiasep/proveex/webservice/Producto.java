package com.matiasep.proveex.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.matiasep.proveex.R;

import java.io.Serializable;


public class Producto implements Serializable {

    private String codigo;
    private String descripcion;
    private Integer id_usu;
    private Integer preciocosto;
    private Integer precioventa;
    private String fabricante;
    private String dato;
    private Bitmap imagen;
    private String rutaImagen;

    public Producto() {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.id_usu= id_usu;
        this.preciocosto = preciocosto;
        this.precioventa = precioventa;
        this.fabricante = fabricante;
        this.dato= dato;
        this.imagen = imagen;
        this.rutaImagen = rutaImagen;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles

            Bitmap foto=BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId_usu() {
        return id_usu;
    }

    public void setId_usu(Integer id_usu) {
        this.id_usu = id_usu;
    }

    public Integer getPreciocosto() {
        return preciocosto;
    }

    public void setPreciocosto(Integer preciocosto) {
        this.preciocosto = preciocosto;
    }

    public Integer getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(Integer precioventa) {
        this.precioventa = precioventa;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    public static Drawable Base64ToImage(Context varContext, String base64String) {
        try {
            if (!base64String.equals("null")) {
                String base64Image = base64String.split(",")[1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Drawable d = new BitmapDrawable(varContext.getResources(), BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                return d;
            }
            return varContext.getResources().getDrawable(R.drawable.sinimagen);
        } catch (Exception e) {
            return null;
        }
    }


}
