package com.example.pccontrol1;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/*
 * This is our first activity.
 */
public class SplashScreenActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// our layout xml
		setContentView(R.layout.activity_splash_screen);

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

	}

}
