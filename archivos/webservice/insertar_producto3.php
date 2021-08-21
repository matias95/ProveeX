<?php
     $servername = "localhost";
     $database = "id14173370_proveex_ws";
     $username = "id14173370_japinfotec";
     $password = "K1go-VNrMr7M<]x7";

     $conn = mysqli_connect($servername, $username, $password, $database);

     $codigo=$_POST["codigo"];
     $producto=$_POST["producto"];
     $preciocosto=$_POST["preciocosto"];
     $precioventa=$_POST["precioventa"];
     $fabricante=$_POST["fabricante"];
     $imagen=$_POST["imagen"];

     $path= "imagenes/$producto.jpg";
     $url = "http://$hostname_localhost/webservice/$path";
     file_put_contents($path,base64_decode($imagen));
     $bytesArchivo=file_get_contents($path);

     $sql = mysqli_prepare($conn, "INSERT INTO producto (codigo, producto, preciocosto, precioventa, fabricante, imagen) VALUES (?, ?, ?, ?, ?, ?)");
     mysqli_stmt_bind_param($sql,"ssiiss",$codigo,$producto,$preciocosto,$precioventa,$fabricante,$bytesArchivo);
     mysqli_stmt_execute($sql);

     $response = array();
     $response["sucess"]= true;
     mysqli_close($conn);
     echo json_encode($response);
?>