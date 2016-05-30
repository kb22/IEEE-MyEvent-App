 <?php
	$eventID = $_GET['id'];
         $con = mysqli_connect("localhost","root","","ieee");
		 $sql="select * from events where EventID = '$eventID'";
		 $res=mysqli_query($con,$sql);
		 $result=array();
		  while($row = mysqli_fetch_array($res)){
		  array_push($result,array('Name'=>$row['Name'],'ShortDescription'=>$row['ShortDescription'],'LongDescription'=>$row['LongDescription'],'StartDate'=>$row['StartDate'],'EndDate'=>$row['EndDate'],'ContactDetails'=>$row['ContactDetails'],'eventtype'=>$row['eventtype'],'EventID'=>$row['EventID'],
'Membership'=>$row['Membership']));
		  }		  
		  echo json_encode(array('result'=>$result));
		  mysqli_close($con);
?>