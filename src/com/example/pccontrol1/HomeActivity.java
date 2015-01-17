package com.example.pccontrol1;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import songs.Favsongs;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mdg.pccontrol1.R;

public class HomeActivity extends DashBoardActivity {
	
	//int counter  database check
//	SplashScreenActivity spa=new SplashScreenActivity();
	//int counter=spa.counter;
	//file or not
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setHeader(getString(R.string.HomeActivityTitle), false, true);
    	SplashScreenActivity.counter++;
    	SplashScreenActivity.countersong++;
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
			intent = new Intent(this, impsongs.class);
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

