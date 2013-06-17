<?php
      include "connectDB.php";    
      
      /*
      stav:
      1 - nalivac na rade
      2 - tester na rade
      3 - konec      
      */
      
      
      function nalivac(){
         $q=mysql_query("SELECT p.id_piva as idBeerOption, p.nazev as nameBeerOption FROM piva_na_udalosti pu JOIN piva p ON p.id_piva = pu.id_piva
          where pu.id_udalosti = '".$_REQUEST["id"]."' and pu.poradi = 0 order by p.nazev asc");
          if ( mysql_num_rows($q) == 0){
            $output[0]["state"]=3;  
          } else {
             while($e=mysql_fetch_assoc($q)) {
              $e["state"] = 1;
              $output[]=$e;
            }
          }
          return $output;
      }
      
    
      $q=mysql_query("SELECT id_piva FROM piva_na_udalosti where id_udalosti = '".$_REQUEST["id"]."' and poradi!=0 order by poradi desc limit 1");
      if ( mysql_num_rows($q) == 0){
          $output = nalivac();  
      }
      else {
        $e=mysql_fetch_assoc($q);
        $id_piva = $e["id_piva"];
        $q=mysql_query("
        SELECT u.id_uzivatele, uu.jmeno FROM uzivatele_na_udalosti u JOIN uzivatele uu ON u.id_uzivatele = uu.id_uzivatele
        LEFT JOIN hodnoceni h on u.id_uzivatele = h.id_uzivatele AND u.id_udalosti = h.id_udalosti AND h.id_piva = '".$id_piva."' 
        where u.id_udalosti = '".$_REQUEST["id"]."'  AND id_piva_tip IS NULL order by u.poradi asc limit 1");
        if ( mysql_num_rows($q) == 0){
          $output = nalivac();  
        } else {
          $e=mysql_fetch_assoc($q);
          $id_uzivatele = $e["id_uzivatele"]; 
          $jmeno = $e["jmeno"];
          
          $q=mysql_query("
            SELECT p.id_piva as idBeerOption, p.nazev as nameBeerOption FROM piva_na_udalosti pu JOIN piva p ON p.id_piva = pu.id_piva
            where pu.id_udalosti = '".$_REQUEST["id"]."' AND 
            (pu.poradi = 0 or pu.poradi = (select max(pp.poradi) from piva_na_udalosti pp where pp.id_udalosti = '".$_REQUEST["id"]."')) order by p.nazev asc");
          if ( mysql_num_rows($q) == 0){
            $output[0]["state"]=3; 
          }
          while($e=mysql_fetch_assoc($q)) {
              $e["state"] = 2;
              $e["idOfTestingBeer"] = $id_piva;    
              $e["idOfTester"] = $id_uzivatele;
              $e["nameOfTester"] = $jmeno;
              $output[]=$e;
            }
        }
      }
 
      print(json_encode($output));
 
      mysql_close();
?>
