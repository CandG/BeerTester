<?php
  define ("SQL_HOST", "mysql5-2");
  define ("SQL_USERNAME", "testovani.95389");
  define ("SQL_PASSWORD", "majkl");
  define ("SQL_DBNAME", "testovani_95389"); 
  
  mysql_connect(SQL_HOST, SQL_USERNAME, SQL_PASSWORD);
  mysql_select_db(SQL_DBNAME);
  mysql_query("set names 'utf8'");
  
  if ($_REQUEST['kontrola'] != "krucifix")
    exit("Nepovoleny pristup!");

?>
