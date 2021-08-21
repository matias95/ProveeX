<?php
     $servername = "localhost";
     $database = "id14173370_proveex_ws";
     $username = "id14173370_japinfotec";
     $password = "K1go-VNrMr7M<]x7";

     $json=array();

     $conn = mysqli_connect($servername, $username, $password, $database);

     //$sql = mysqli_prepare($conn, "SELECT * FROM producto");
     $consulta="select * from producto";
     $resultado=mysqli_query($conn,$consulta);

     while ($registro=mysqli_fetch_array($resultado)){
          $result["codigo"]=$registro['codigo'];
          $result["producto"]=$registro['producto'];
          $result["preciocosto"]=$registro['preciocosto'];
          $result["precioventa"]=$registro['precioventa'];
          $result["fabricante"]=$registro['fabricante'];
          $result["imagen"]=base64_encode($registro['imagen']);
          $json['producto'][]=$result;
     }

     //$response = array();
     //$response["sucess"]= true;
     mysqli_close($conn);
     echo json_encode($json);
?>