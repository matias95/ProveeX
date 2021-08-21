<?PHP
$hostname_localhost ="localhost";
$database_localhost ="id14173370_proveex_ws";
$username_localhost ="id14173370_japinfotec";
$password_localhost ="K1go-VNrMr7M<]x7";

$json['img']=array();

	//if(true){)
	if(isset($_POST["btn"])){
		
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		
		$ruta="imagenes";
		$archivo=$_FILES['imagen']['tmp_name'];
		echo 'Archivo';
		echo '<br>';
		echo $archivo;
		$nombreArchivo=$_FILES['imagen']['name'];
		echo 'Nombre Archivo';
		echo '<br>';
		echo $nombreArchivo;
		move_uploaded_file($archivo,$ruta."/".$nombreArchivo);
		$ruta=$ruta."/".$nombreArchivo;
		$codigo=$_POST['codigo'];
		$producto=$_POST['producto'];
		$preciocosto=$_POST['preciocosto'];
		$precioventa=$_POST['precioventa'];
		$fabricante=$_POST['fabricante'];
		
		echo '<br>';
		echo 'Codigo: ';
		echo $codigo;
		echo '<br>';
		echo 'Producto: ';
		echo $producto;
		echo '<br>';
		echo 'Preciocosto: ';
		echo $preciocosto;
		echo '<br>';
		echo 'Precioventa: ';
		echo $precioventa;
		echo '<br>';
		echo 'Fabricante: ';
		echo $fabricante;
		echo '<br>';
		echo 'ruta :';
		echo $ruta;
		echo '<br>';
		echo 'Tipo Imagen: ';
		echo ($_FILES['imagen']['type']);
		echo '<br>';
		echo '<br>';
		echo "Imagen: <br><img src='$ruta'>";
		echo '<br>';
		echo '<br>';
		echo 'imagen en Bytes: ';
		echo '<br>';
		echo '<br>';
		//echo $bytesArchivo=file_get_contents($ruta);
		echo '<br>';
		
		$bytesArchivo=file_get_contents($ruta);
		$sql= "INSERT INTO producto(codigo,producto,preciocosto,precioventa,fabricante,imagen) VALUES (?,?,?,?,?,?)";
		$stm=$conexion->prepare($sql);
		$stm->bind_param('ssiiss',$codigo,$producto,$preciocosto,$precioventa,$fabricante,$bytesArchivo);
		
		if($stm->execute()){
			echo 'imagen Insertada Exitosamente ';
			$consulta="select * from producto where codigo='{$codigo}'";
			$resultado=mysqli_query($conexion,$consulta);
			echo '<br>';
			while ($row=mysqli_fetch_array($resultado)){
				echo $row['codigo'].' - '.$row['producto'].'<br/>';
				array_push($json['img'],array('codigo'=>$row['codigo'],'producto'=>$row['producto'],'preciocosto'=>$row['preciocosto'],'precioventa'=>$row['precioventa'],'fabricante'=>$row['fabricante'],'photo'=>base64_encode($row['producto']),'ruta'=>$row['ruta_imagen']));
			}
			mysqli_close($conexion);
			
			echo '<br>';
			echo 'Objeto JSON 2';
			echo '<br>';
			echo json_encode($json);
		}
	}
?>