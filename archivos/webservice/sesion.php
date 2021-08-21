<?PHP
$hostname="localhost";
$database="id14173370_proveex_ws";
$username="id14173370_japinfotec";
$password="K1go-VNrMr7M<]x7";
$json=array();
	if(isset($_GET["user"]) && isset($_GET["pwd"])){
		$user=$_GET['user'];
		$pwd=$_GET['pwd'];
		
		$conexion=mysqli_connect($hostname,$username,$password,$database);
		
		$consulta="SELECT id_usu, user, pwd, nombre FROM usuario WHERE user= '{$user}' AND pwd = '{$pwd}'";
		$resultado=mysqli_query($conexion,$consulta);

		if($consulta){
		
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}

		else{
			$results["user"]='';
			$results["pwd"]='';
			$results["nombre"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
		
	}
	else{
		   	$results["user"]='';
			$results["pwd"]='';
			$results["nombre"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
?>