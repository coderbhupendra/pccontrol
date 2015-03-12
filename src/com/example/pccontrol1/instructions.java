package com.example.pccontrol1;



import com.example.pccontrol1.*;
import com.mdg.pccontrol1.R;

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

public class instructions extends Activity {

   TextView instructions ;
  
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.instructions);

      instructions = (TextView) findViewById(R.id.textinst);
      
   }

   public void ok(View view){
      
	   finish();
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.activity_main_actions, menu);
      return true;
   }

}