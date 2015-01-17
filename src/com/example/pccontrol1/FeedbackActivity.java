package com.example.pccontrol1;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdg.pccontrol1.R;

public class FeedbackActivity  extends DashBoardActivity {
    /** Called when the activity is first created. */
	ImageView imv;
	TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setHeader(getString(R.string.FeedbackActivityTitle), true, false);
        imv=(ImageView) findViewById(R.id.imageView1);
        tv=(TextView) findViewById(R.id.textViewip);
        tv.setText("IPADDRESS OF COMPUTER\n"+IPEntry.ip);
        
        
        
        
        
    }
}
