package com.example.pccontrol1;

import com.example.pccontrol1.*;
import com.mdg.pccontrol1.R;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class IPEntry extends Activity {

   TextView name ;
   TextView ipaddress,ipname;
   
   public static final String MyPREFERENCES = "MyPrefs" ;
   public static final String Name = "nameKey"; 
   public static final String IP = "IPKey";
   public static String ip = "IPKey";
   

   public static  SharedPreferences sharedpreferences;

   @Override
   public void onBackPressed(){
     Intent intent = new Intent(Intent.ACTION_MAIN);
     intent.addCategory(Intent.CATEGORY_HOME);
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     startActivity(intent);
   }
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.ip);

      name = (TextView) findViewById(R.id.editTextName);
      ipaddress = (TextView) findViewById(R.id.editTextIP);
      ipname= (TextView) findViewById(R.id.textViewip);
      String i  = ipaddress.getText().toString();
      
      final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
  	final DhcpInfo dhcp = manager.getDhcpInfo();
  	final String address = Formatter.formatIpAddress(dhcp.gateway);
  	ipname.setText(address);
      sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
				HomeActivity.class);
		startActivity(nextActivity);

   }
   
   public void help(View view){
	     
		  Intent nextActivity = new Intent(IPEntry.this,
					instructions.class);
			startActivity(nextActivity);

	   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   

}
