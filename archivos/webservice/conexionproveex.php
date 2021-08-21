<?php
     $hostname='localhost';
     $database='id14173370_proveex_ws';
     $username='id14173370_japinfotec';
     $password='K1go-VNrMr7M<]x7';
     $conexion=new mysqli($hostname,$username,$password,$database);
     if($conexion->connect_errno){
     	echo "El sitio web está experimentado problemas";
     }
?>