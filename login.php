 <?php
         $con = mysqli_connect("localhost","root","","ieee");
		 $sql="select * from members";
		 $res=mysqli_query($con,$sql);
		 $result=array();
		  while($row = mysqli_fetch_array($res)){
		  array_push($result,array('Email'=>$row['Email'],'Password'=>$row['Password'],'Name'=>$row['Name'],'Section'=>$row['Section'],'SubSection'=>$row['SubSection'],'Membership'=>$row['Membership']));
		  }		  
		  echo json_encode(array('result'=>$result));
		  mysqli_close($con);
?>