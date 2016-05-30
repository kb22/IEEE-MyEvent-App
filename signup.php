<?php

         $con = mysql_connect("localhost","root","");
         if (!$con)
           {
             die('Could not connect: ' . mysql_error());
           }

           mysql_select_db("ieee", $con);


           $v1=$_REQUEST['f1'];
           $v2=$_REQUEST['f2'];
           $v3=$_REQUEST['f3'];
           $v4=$_REQUEST['f4']; 
           $v5=$_REQUEST['f5']; 
           $v6=$_REQUEST['f6']; 
	   $v7=$_REQUEST['f7'];  


              if($v1==NULL && $v2==NULL && $v3==NULL && $v4==NULL && $v5==NULL && $v6==NULL && $v7==NULL)
             {
                $r["re"]="Fill the all fields!!!";
                 print(json_encode($r));
                die('Could not connect: ' . mysql_error());
             }


            else
          {
           $i=mysql_query("select * from members where f5=$v5",$con);
           $check='';
                  while($row = mysql_fetch_array($i))
                    {
  
                          $check=$row['f1'];

                     }

           
                   if($check==NULL)
                  {

                        $q="insert into members values('$v1','$v2','$v3','$v4','$v5','$v6','$v7')";
                        $s= mysql_query($q); 
                        if(!$s)
                          {
                                $r["re"]="Inserting problem in batabase";
                  
                               print(json_encode($r));
                           }
                         else
                          {
                             $r["re"]="Record inserted successfully";
                              print(json_encode($r));
                           }
             }
            else
             {
               $r["re"]="Record is repeated";
                 print(json_encode($r));
      
              } 
}
 mysql_close($con);
               
    ?>  