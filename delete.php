<?php
	
		$id = $_GET['id'];
         $con = mysqli_connect("localhost","root","","ieee");
		 $sql = "delete from events where EventID = $id";
		 if($res=mysqli_query($con,$sql)){
			 echo "Deleted";
		 }else{
			 echo "Problem";
		 }
?>
