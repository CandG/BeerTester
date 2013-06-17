<?php
      include "connectDB.php";    
    
      $q=mysql_query("insert into hodnoceni (id_uzivatele, id_udalosti, id_piva_tip, hodnoceni, id_piva, popis) 
            VALUES ('".$_REQUEST["idOfTester"]."', '".$_REQUEST["idOfEvent"]."','".$_REQUEST["choosenBeer"]."',
            '".$_REQUEST["mark"]."','".$_REQUEST["idOfTestingBeer"]."', '".$_REQUEST["comment"]."')
                    ");
          $output[0]["ok"]=$q;
      
 
      print(json_encode($output));
 
      mysql_close();
?>
