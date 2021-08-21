<?php
     include 'conexionproveex.php';

     $codigo=$_GET['codigo'];
     //$producto=$_POST["producto"];
     //$precio=$_POST["precio"];
     //$fabricante=$_POST["fabricante"];

     $consulta="select * from producto where codigo = '$codigo'";
     $resultado=$conexion -> query($consulta);

     while ($fila=$resultado -> fetch_array()) {
     	$producto[]=array_map('utf8_encode', $fila);
     }

     echo json_encode($producto);
     $resultado -> close();
    
?>