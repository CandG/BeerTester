<?php
      include "connectDB.php";    
    
      $q=mysql_query("SELECT p.id_piva AS id, p.nazev AS name
                      FROM piva p JOIN piva_na_udalosti pu ON p.id_piva = pu.id_piva
                      where pu.id_udalosti = '".$_REQUEST["id"]."'
                    ");
      if ( mysql_num_rows($q) == 0)
          $output[0]["empty"]=true;
      else {
        while($e=mysql_fetch_assoc($q)) {
            $output[]=$e;
          }
      }
 
      print(json_encode($output));
 
      mysql_close();
?>
