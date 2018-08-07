<?php
  
    $file_path = "uploads/";
    $KEY = 'uploaded_file';
    $file_path = $file_path . basename( $_FILES[$KEY]['name']);
    
    if(move_uploaded_file($_FILES[$KEY]['tmp_name'], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }
 ?>