 <?php
		$email = $_GET['email'];
		$pass = $_GET['pass'];
		$found = false;
		$wrongPass = false;
		
         $con = mysqli_connect("localhost","root","root","ieee");
		$sql="select * from members where Email = '$email'";
		 $res=mysqli_query($con,$sql);
		 $result=array();
		  while($row = mysqli_fetch_array($res)){
			  
			  
			  if($row['Password'] == $pass){
				  array_push($result,array('Message'=>'1','Email'=>$row['Email'],'Password'=>$row['Password'],'Name'=>$row['Name'],'Section'=>$row['Section'],'SubSection'=>$row['SubSection'],'Membership'=>$row['Membership']));
				  $found = true;
				  break;
			  }else{
				  array_push($result,array('Message'=>'0'));
				  $wrongPass = true;
				  break;
			  }
			  
		  
		  }		  
		  if(!$found){
			  if(!$wrongPass)
				  array_push($result,array('Message'=>'-1'));
		  }
		  
		  echo json_encode(array('result'=>$result));
		  mysqli_close($con);
?>