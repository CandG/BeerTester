<?php
      include "connectDB.php";    
    
      $q=mysql_query("SELECT id_udalosti AS id, nazev AS name, unix_timestamp(kdy) AS time, kde AS place
                      FROM udalost order by kdy desc
                    ");
      if ( mysql_num_rows($q) == 0)
          $output[0]["empty"]=true;
      else {
        while($e=mysql_fetch_assoc($q)) {
            $e["time"] = date("j.n. Y",$e["time"]);
            $output[]=$e;
          }
      }
 
      print(json_encode($output));
 
      mysql_close();
?>
