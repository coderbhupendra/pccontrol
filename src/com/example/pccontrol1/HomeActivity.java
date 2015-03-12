package com.example.pccontrol1;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import songs.Favsongs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mdg.pccontrol1.R;

public class HomeActivity extends Activity {
	
	//int counter  database check
//	SplashScreenActivity spa=new SplashScreenActivity();
	//int counter=spa.counter;
	//file or not
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SplashScreenActivity.counter++;
    	SplashScreenActivity.countersong++;
    	
    }
    public boolean onCreateOptionsMenu(Menu menu) {
  		// Inflate the menu; this adds items to the action bar if it is present.
  		getMenuInflater().inflate(R.menu.activity_main_actions, menu);
  		return true;
  	}
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		
		
		case R.id.action_help:
			// help action
			return true;
		case R.id.action_settings:
			// check for updates action
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	} 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                // your code here
        	Intent intent = new Intent(this, IPEntry.class);
			startActivity(intent);
                return false;
        }
    return super.onKeyDown(keyCode, event);
}
    /**
     * Button click handler on Main activity
     * @param v
     */
    public void onButtonClicker(View v)
    {
    	Intent intent;
    	
    	switch (v.getId()) {
		case R.id.computer:
			intent = new Intent(HomeActivity.this, computer.class);
			startActivity(intent);
			break;

		case R.id.settings:
			intent = new Intent(this, IPEntry.class);
			startActivity(intent);
			break;
		case R.id.help:
			intent = new Intent(this, instructions.class);
			startActivity(intent);
			break;
		case R.id.music:
			intent = new Intent(this, music.class);
			startActivity(intent);
			break;
		case R.id.favfile:
			intent = new Intent(this, impfilesOuter.class);
			startActivity(intent);
			break;
		case R.id.mouse:
			intent = new Intent(this, mouse.class);
			//intent = new Intent(this, livescreen.class);
			//intent = new Intent(this, impfiles.class);
			startActivity(intent);
			break;
		case R.id.impfiles:

			intent = new Intent(this, Favsongs.class);
			startActivity(intent);
			break;
		//case R.id.email:
			
		
			
			
		
		default:
			break;
		}
    }
}

