<?php
     $servername = "localhost";
     $database = "id14173370_proveex_ws";
     $username = "id14173370_japinfotec";
     $password = "K1go-VNrMr7M<]x7";

     $conn = mysqli_connect($servername, $username, $password, $database);

     $producto=$_POST["producto"];
     
     $sql = mysqli_prepare($conn, "DELETE FROM producto WHERE producto = ?");
     mysqli_stmt_bind_param($sql,"s",$producto);
     mysqli_stmt_execute($sql);

     //$response = array();
     //$response["sucess"]= true;
     mysqli_close($conn);
     //echo json_encode($response);
?>