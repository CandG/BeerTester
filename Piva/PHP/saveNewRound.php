<?php
      include "connectDB.php";    
    
      $q=mysql_query("
            update piva_na_udalosti SET poradi = (SELECT MAX(poradi) FROM (select poradi from piva_na_udalosti where id_udalosti = '".$_REQUEST["idOfEvent"]."') d) + 1 
            where id_piva = '".$_REQUEST["choosenBeer"]."' and id_udalosti = '".$_REQUEST["idOfEvent"]."'
                    ");
          $output[0]["ok"]=$q;
      
 
      print(json_encode($output));
 
      mysql_close();
?>
