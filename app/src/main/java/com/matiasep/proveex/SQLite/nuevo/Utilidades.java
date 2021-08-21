package com.matiasep.proveex.SQLite.nuevo;


public class Utilidades {

    //Constantes campos tabla usuario
    public static final String TABLA_PRODUCTO="producto";
    public static final String CAMPO_CO="codigo";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_PC="preciocosto";
    public static final String CAMPO_PV="precioventa";
    public static final String CAMPO_F="marca";

    public static final String CREAR_TABLA_PRODUCTO="CREATE TABLE " +
            ""+TABLA_PRODUCTO+" ("+CAMPO_CO+" " +
            "INTEGER, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_PC+" TEXT, "+CAMPO_PV+" TEXT, "+CAMPO_F+" TEXT)";

    /*Constantes campos tabla mascota
    public static final String TABLA_MASCOTA="mascota";
    public static final String CAMPO_ID_MASCOTA="id_mascota";
    public static final String CAMPO_NOMBRE_MASCOTA="nombre_mascota";
    public static final String CAMPO_RAZA_MASCOTA="raza_mascota";
    public static final String CAMPO_ID_DUENIO="id_duenio";

    public static final String CREAR_TABLA_MASCOTA="CREATE TABLE " +
            ""+TABLA_MASCOTA+" ("+CAMPO_ID_MASCOTA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_MASCOTA+" TEXT, "+CAMPO_RAZA_MASCOTA+" TEXT,"+CAMPO_ID_DUENIO+" INTEGER)";*/

}
