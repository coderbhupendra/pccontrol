package com.example.pccontrol1;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mdg.pccontrol1.R;

/*
 * This is our first activity.
 */

public class SplashScreenActivity extends Activity {
TextView appname;
//int counter  database check
	static public int counter=0,countersong=0;
	@Override
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// our layout xml
		setContentView(R.layout.activity_splash_screen);
counter=1;countersong=1;
		appname=(TextView) findViewById(R.id.appname);
		 Typeface custom_font = Typeface.createFromAsset(getAssets(),
       	      "fonts/timeburner_regular.ttf");
        appname.setTypeface(custom_font,Typeface.BOLD_ITALIC);
		
		// we're gonna use a timer task to show the main activity after 4 seconds
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				// go to the main activity
				Intent nextActivity = new Intent(SplashScreenActivity.this,
						IPEntry.class);
				startActivity(nextActivity);

				// make sure splash screen activity is gone
				SplashScreenActivity.this.finish();

			}

		};

		// Schedule a task for single execution after a specified delay.
		// Show splash screen for 4 seconds
		new Timer().schedule(task, 2000);
		
		MediaPlayer player=MediaPlayer.create(SplashScreenActivity.this,R.raw.sms);
		player.start();

		
		
	}

}
