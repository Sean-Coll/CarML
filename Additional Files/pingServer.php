<?php
$server_name = "localhost";
$mysql_user = "root";
$mysql_pass = "";
$db_name = "CarML";

$con = mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
if(!$con)
{
	echo "Connection failed";
}
else
{	
	$array = array();
	$sql = "SELECT * FROM DD_Cars LIMIT 1";
	$result = mysqli_query($con, $sql) or die("Error with SQL " . mysqli_error($con));
	$emparray = array();
	while($row = mysqli_fetch_assoc($result))
	{
		$emparray[] = $row;
	}
	echo json_encode($emparray);
	mysqli_close($con);
}
	
?>