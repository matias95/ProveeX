<?PHP
$hostname_localhost ="localhost";
$database_localhost ="id14173370_proveex_ws";
$username_localhost ="id14173370_japinfotec";
$password_localhost ="K1go-VNrMr7M<]x7";

$json=array();

	if(isset($_GET["codigo"])){
		$codigo=$_GET["codigo"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select * from producto where codigo= '{$codigo}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$result["codigo"]=$registro['codigo'];
			$result["producto"]=$registro['producto'];
			$result["preciocosto"]=$registro['preciocosto'];
			$result["precioventa"]=$registro['precioventa'];
			$result["fabricante"]=$registro['fabricante'];
			$result["imagen"]=base64_encode($registro['imagen']);
			$json['producto'][]=$result;
		}else{
			$resultar["codigo"]='no registra';
			$resultar["producto"]='no registra';
			$resultar["preciocosto"]=0;
			$resultar["precioventa"]=0;
			$resultar["fabricante"]='no registra';
			$resultar["imagen"]='no registra';
			$json['producto'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['producto'][]=$resultar;
		echo json_encode($json);
	}
?>