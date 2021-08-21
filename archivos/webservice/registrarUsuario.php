<?PHP
$hostname="localhost";
$database="id14173370_proveex_ws";
$username="id14173370_japinfotec";
$password="K1go-VNrMr7M<]x7";
$json=array();
	if( isset($_GET["nombre"]) && isset($_GET["user"]) && isset($_GET["pwd"])){
		//$id_usu=$_GET['id_usu']
		$nombre=$_GET['nombre'];
		$user=$_GET['user'];
		$pwd=$_GET['pwd'];
		
		$conexion=mysqli_connect($hostname,$username,$password,$database);
		
		$consulta="INSERT INTO usuario(id_usu, nombre, user, pwd) VALUES (NULL,'{$nombre}','{$user}' , '{$pwd}')";
		$resultado=mysqli_query($conexion,$consulta);

       
		if($consulta){
		   $consulta="SELECT * FROM usuario WHERE nombre='{$nombre}'";
		   $resultado=mysqli_query($conexion,$consulta);

			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}

		else{
			$results["id_usu"]=0;
			$results["nombre"]='';
			$results["user"]='';
			$results["pwd"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
		
	}
	else{   
		    $results["id_usu"]=0;
		    $results["nombre"]='';
		   	$results["user"]='';
			$results["pwd"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
?>