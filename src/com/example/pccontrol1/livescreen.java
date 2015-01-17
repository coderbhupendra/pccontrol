package com.example.pccontrol1;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

import com.mdg.pccontrol1.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore.Images;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class livescreen extends DashBoardActivity {
	public AsyncTask<String, String, String> mTask;
	
	static ObjectInputStream streamFromServer;
	static 	 PrintStream streamToServer;
	static Socket toServer,soc;
	ImageView imagescreen;
	 Bitmap  yourSelectedImage;
	 byte[] by_new;
	  Button buttonscreen;
	
	
String IPADDRESS,data;
    // Called when the activity is first created. 
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livescreen);
        setHeader(getString(R.string.mouse), true, true);
       // Toast.makeText(getBaseContext(), "yes", Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
 		   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
 		    StrictMode.setThreadPolicy(policy);
 		}
        
        IPEntry ip=new IPEntry();
        IPADDRESS=ip.ip;
        
       
        imagescreen = (ImageView) findViewById(R.id.imageViewscreen);
		// surface =  findViewById(R.id.surfaceViewscreen);
		
		 
		 buttonscreen=(Button) findViewById(R.id.buttonscreen);
        
         new LongOperationpoints().execute();
    }
    
    

	 private class LongOperationpoints extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	Socket toServer;
	        	ObjectInputStream streamFromServer;
	        	PrintStream streamToServer;
	        	
	        	try
	        	{
	        	toServer = new Socket(IPADDRESS,1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

                   
	        	streamToServer.println("screen");
	        	
	        	
				streamFromServer.readFully(by_new);
				 yourSelectedImage = BitmapFactory.decodeByteArray(by_new, 0, by_new.length);
	        	/*Vector<String>image= new Vector<String>(1,1);
	        	 image=(Vector<String>) streamFromServer.readObject();
	        	  	
	        	  data=image.elementAt(0);
	        	  	by_new = data.getBytes();
	        		yourSelectedImage = BitmapFactory.decodeByteArray(by_new, 0, by_new.length);
	        		*/
	        	streamFromServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
	        	
		return "";
	       }	
	        
	        protected void onPostExecute(String result) {
	        
	        	
	        	imagescreen.setImageBitmap(yourSelectedImage);
	        Toast.makeText(getApplicationContext(), by_new .length +"ss", Toast.LENGTH_LONG).show();
	        	System.out.println("ddd"+by_new.length+"\n"+data.getBytes()) ;
	        	
				super.onPostExecute(result);
			  }
	        
	}//end of long process
	      	  
       
  
  	
 
   
  	public boolean onCreateOptionsMenu(Menu menu) {
  		// Inflate the menu; this adds items to the action bar if it is present.
  		getMenuInflater().inflate(R.menu.main, menu);
  		return true;
  	}	

}