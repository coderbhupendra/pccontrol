package com.example.pccontrol1;

import java.io.File;

public class DriveName {


   public static void main(String[] args) {
      
      File[] paths;
      
      try{      
         // returns pathnames for files and directory
         paths = File.listRoots();
         
         // for each pathname in pathname array
         for(File path:paths)
         {
            // prints file and directory paths
            System.out.println(path);
         }
      }catch(Exception e){
         // if any error occurs
         e.printStackTrace();
      }
   }
}