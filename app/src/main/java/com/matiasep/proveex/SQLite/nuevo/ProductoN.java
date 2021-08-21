package com.matiasep.proveex.SQLite.nuevo;

import java.io.Serializable;


public class ProductoN implements  Serializable{

    private Integer codigo;
    private String nombre;
    private String preciocosto;
    private String precioventa;
    private String fabricante;

    public ProductoN() {
        this.codigo = codigo;
        this.nombre = nombre;
        this.preciocosto = preciocosto;
        this.precioventa = precioventa;
        this.fabricante = fabricante;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPreciocosto() {
        return preciocosto;
    }

    public void setPreciocosto(String preciocosto) {
        this.preciocosto = preciocosto;
    }

    public String getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(String precioventa) {
        this.precioventa = precioventa;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
}
