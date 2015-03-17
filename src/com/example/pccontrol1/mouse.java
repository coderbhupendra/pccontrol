package com.example.pccontrol1;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.mdg.pccontrol1.R;


@SuppressLint("NewApi")
public class mouse extends Activity implements        OnDoubleTapListener, OnGestureListener{
	

	public AsyncTask<String, String, String> mTask;
	
	static ObjectInputStream streamFromServer;
	static 	 PrintStream streamToServer;
	static Socket toServer;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector; 
int x,y,xx,yy=0;

int widthpc=0,heightpc=0;
int widthand=0,heightand=0;
int finalx=0,finaly=0;
int newx=0, newy=0,newxp=0,newyp=0; 
int choice=0;
String IPADDRESS;
    // Called when the activity is first created. 
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mouse);
       // setHeader(getString(R.string.mouse), true, true);
       // Toast.makeText(getBaseContext(), "yes", Toast.LENGTH_LONG).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
 		   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
 		    StrictMode.setThreadPolicy(policy);
 		}
        
        IPEntry ip=new IPEntry();
        IPADDRESS=ip.ip;
        

      
     // get action bar   
   //     ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
  //      actionBar.setDisplayHomeAsUpEnabled(true);
        
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion <= android.os.Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			widthand = this.getWindowManager().getDefaultDisplay().getWidth();
			heightand = this.getWindowManager().getDefaultDisplay().getHeight();
		}
		else
		{
			 Display display = getWindowManager().getDefaultDisplay();
		        Point size = new Point();
		        display.getSize(size);
		        widthand = size.x;
		        heightand = size.y;
		        
		}
        
        
       
        
      // Toast.makeText(getApplicationContext(), widthand +"  "+heightand, Toast.LENGTH_LONG).show();
       
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
        Log.d(DEBUG_TAG,"onDown: " + "bh"); 
        
        
        new LongOperationpoints().execute();
        getActionBar().setHomeButtonEnabled(true);
    }
    @Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
        	//finalx=0;finaly=0;
       }


       if (event.getAction() == MotionEvent.ACTION_UP) {
    	   finalx=newxp;finaly=newyp;
             mTask.cancel(true);
             
    	 }
       if (event.getAction() == MotionEvent.ACTION_DOWN) {
    	   newxp=finalx; newyp=finaly;
    	   mTask = new LongOperation().execute();
       	
        }
        
        return super.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent event) { 
    
    	if((event.getAction()& MotionEvent.ACTION_MASK)==MotionEvent.ACTION_DOWN) 
    	{x=(int)event.getX(); y=(int)event.getY();
    	

    	}
        return true;
    }

  
    public boolean onFling(MotionEvent e1, MotionEvent e2, 
            float velocityX, float velocityY) {
   
        return true;
    }

    public void onLongPress(MotionEvent event) {
  
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
    	 mTask = new LongOperation().execute();
    	
    	xx=(int)e2.getX(); yy=(int)e2.getY();
    	 return true;
     }
    
    private void sendpostions() {
    	mTask = new LongOperation().execute();
    	
	}

	public void onShowPress(MotionEvent event) {
    }

    public boolean onSingleTapUp(MotionEvent event) {
    	choice=1;
		sendpostions();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
    	choice=6;
		sendpostions();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
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

                   
	        	streamToServer.println("size");
	        	
	        	Vector<Integer> points = new Vector<Integer>();
	             points=(Vector<Integer>)streamFromServer.readObject();
	        	widthpc=points.elementAt(0);
	        	heightpc=points.elementAt(1);
	        	
	        	  	
	        	streamFromServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        
	        protected void onPostExecute(String result) {
	        	
			 
				super.onPostExecute(result);
			  }
	        
	}//end of long process
	      	  
       
  	//class LongOperation started
  	private class LongOperation extends AsyncTask<String, String, String> {
  		
          protected String doInBackground(String... params) {
        	 
              
        if(!mTask.isCancelled())
        	            {
        	            	Socket toServer;
        	          	PrintStream streamToServer;
        	         	
        	          	if(getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
        	          	{
        	          	    // code to do for Portrait Mode
        	          		 newx=(((xx*widthpc)/widthand)+newxp); 
        	              	 newy=(((yy*heightpc*3)/(heightand*2))+newyp); 
        	          	} else {
        	          	    // code to do for Landscape Mode  
        	          		 newx=((xx*widthpc)/widthand)+finalx+newxp; 
        	              	 newy=((yy*heightpc)/heightand)+finaly+newyp; 
        	          	}
        	          	
        	          	
        	          	try {
        	      			toServer = new Socket(IPADDRESS,1001);
        	      			streamFromServer=new ObjectInputStream(toServer.getInputStream());
        	      			streamToServer = new PrintStream(toServer.getOutputStream());
        	      			
        	      				
        	      			if(choice==0){
        	      			
        	      			streamToServer.println("position");
        	      			streamToServer.println(newx);streamToServer.println(newy);
        	      			}
        	      			
        	      			else if(choice==1){streamToServer.println("first");}
        	      			else if(choice==2){streamToServer.println("second");}
        	      			else if(choice==3){streamToServer.println("third");}
        	      			else if(choice==4){streamToServer.println("wheelup");}  
        	      			else if(choice==5){streamToServer.println("wheeldown");}
        	      			else if(choice==6){streamToServer.println("enter");}  
        	      			choice=0;
        	      			
        	      		
        	          	} catch (UnknownHostException e) {
        	      			// TODO Auto-generated catch block
        	      			e.printStackTrace();
        	      		} catch (RejectedExecutionException e) {
        	      			// TODO Auto-generated catch block
        	      			e.printStackTrace();
        	      		} catch (StreamCorruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        	          	}
       	
      		
           	return "";
             }	
      }//class LongOperation ended
      
  	//defining buttons
  
  	public void first(View v) {
		choice=1;
		sendpostions();
	}
  public void second(View v) {
	  choice=2;
	  sendpostions();
	}
   public void third(View v) {
	   choice=3;
	   sendpostions();
	
  }
   //left
   public void wheelup(View v) {
	   choice=4;
	   sendpostions();
	
  }
   //right
   public void wheeldown(View v) {
	   choice=5;
	   sendpostions();
	
  }
  
   public boolean onCreateOptionsMenu(Menu menu) {
  		// Inflate the menu; this adds items to the action bar if it is present.
  		getMenuInflater().inflate(R.menu.activity_main_actions, menu);
  		return true;
  	}


   @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case android.R.id.home:
		      // ProjectsActivity is my 'home' activity
			 intent = new Intent(getApplicationContext(), HomeActivity.class);
			  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			  startActivity(intent);	   
			  return true;
		
		case R.id.action_help:
			// help action
			intent=new Intent(getApplicationContext(), instructions.class);
			startActivity(intent);
			return true;
		case R.id.action_settings:
			// check for updates action
			intent=new Intent(getApplicationContext(), IPEntry.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	} 	


	

}