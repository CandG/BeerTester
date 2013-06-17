<?php
      include "connectDB.php";    
    
      $q=mysql_query("SELECT p.id_uzivatele AS id, p.jmeno AS name
                      FROM uzivatele p JOIN uzivatele_na_udalosti pu ON p.id_uzivatele = pu.id_uzivatele
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
