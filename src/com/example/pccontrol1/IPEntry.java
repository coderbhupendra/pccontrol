package com.example.pccontrol1;

import com.example.pccontrol1.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class IPEntry extends Activity {

   TextView name ;
   TextView ipaddress;
   
   public static final String MyPREFERENCES = "MyPrefs" ;
   public static final String Name = "nameKey"; 
   public static final String IP = "IPKey";
   public static String ip = "IPKey";
   

   public static  SharedPreferences sharedpreferences;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.ip);

      name = (TextView) findViewById(R.id.editTextName);
      ipaddress = (TextView) findViewById(R.id.editTextIP);
      
      String i  = ipaddress.getText().toString();
      
      
      sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_WORLD_READABLE);

      if (sharedpreferences.contains(Name))
      {
         name.setText(sharedpreferences.getString(Name, ""));
        

      }
      if (sharedpreferences.contains(IP))
      {
    	  ipaddress.setText(sharedpreferences.getString(IP, ""));
    	 
          ip=sharedpreferences.getString(IP, "");
                  
      }      

   }

   public void run(View view){
      String n  = name.getText().toString();
      String i  = ipaddress.getText().toString();
     
      Editor editor = sharedpreferences.edit();
      editor.putString(Name, n);
      editor.putString(IP, i);
      
      editor.commit(); 
      
      
      ip=sharedpreferences.getString(IP, "");
      Toast.makeText(getBaseContext(), ip , Toast.LENGTH_SHORT).show();
      
	  Intent nextActivity = new Intent(IPEntry.this,
				MainActivity.class);
		startActivity(nextActivity);

   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}