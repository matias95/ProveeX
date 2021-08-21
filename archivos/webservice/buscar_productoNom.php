<?php
     include 'conexionproveex.php';

     $producto1=$_GET['producto'];
     //$producto=$_POST["producto"];
     //$precio=$_POST["precio"];
     //$fabricante=$_POST["fabricante"];

     $consulta="select * from producto where producto = '$producto1'";
     $resultado=$conexion -> query($consulta);

     while ($fila=$resultado -> fetch_array()) {
     	$producto[]=array_map('utf8_encode', $fila);
     }

     echo json_encode($producto);
     $resultado -> close();
    
?>