package com.example.pccontrol1;

import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pccontrol1.IPEntry;
import com.mdg.pccontrol1.R;

public class music extends DashBoardActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    /** Called when the activity is first created. */
	Socket toServer;
	ObjectInputStream streamFromServer;
	PrintStream streamToServer;
	
    ListView list;
	String[] titles;
	String[] descriptions;
	String drive ="";
	int[] images;
	static int choice=0,check=0;
	
	//TextView header;
	static String headpath="nopath";
	static String head="nohead";
	
	IPEntry ip=new IPEntry();
	String IPADDRESS=ip.ip;
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 0; 
	
	public static Vector vector = new Vector();

	
	
	static int size=6;
	VivzAdapter adapter;
    static	String fi;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.music);
	       setHeader(getString(R.string.music), true, true);
       
	
		try {
		//	send();
			new LongOperationdrives().execute();
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
	
	
	

	private void display() {
		// TODO Auto-generated method stub
		
		
		 list=(ListView)findViewById(R.id.listViewmusic);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 list.setOnItemClickListener( this);
			list.setOnItemLongClickListener(this);
			//list.setOnItemSelectedListener(this);
	}



	public void send(String path) throws Exception {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Searching Songs. Please wait...");
		pDialog.setIndeterminate(false);
		pDialog.setMax(100);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.setCancelable(true);
		pDialog.show();
	    	new LongOperation().execute(path);
	    	
	    }
	 //ch=1 check=1
	 private class LongOperation extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	
	        	
	        	
	        	
	        	try
	        	{
	        	toServer = new Socket(IPADDRESS,1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

           
	        	//send a message to the server
	        	streamToServer.println("music");
	        	streamToServer.println(params[0]);
	        	
	        	 publishProgress(""+(int)10);
	        	//receive vectors from the server
	        	 vector =(Vector<String>)streamFromServer.readObject();
	        	 publishProgress(""+(int)20);
	        	 size=vector.size();
	        	 publishProgress(""+(int)40);
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		 publishProgress(""+(int)60);
	    		 //path for headpath
	    		   headpath = String.valueOf(vector.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	                
	    		 //on create activity this code will run
	                
	                	for(int i=0;i<size;i++){
	    	      			titles[i]=String.valueOf(i+1);
	    	      			 publishProgress(""+(int)80);
	    	      			//seperating the  last name from filenam
	    	      			String path = String.valueOf(vector.elementAt(i));
	    	                 int pos = path.lastIndexOf("\\");
	    	                 String name =path.substring(pos+1 , path.length());
	    	      	        descriptions[i]=name;
	    	      	        images[i]=R.drawable.song1;
	    	      	      check=1;
	    	      		}
	                	choice=1;
	                	 publishProgress(""+(int)100);
	        	streamFromServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        @Override
			protected void onPreExecute() {
				super.onPreExecute();
				showDialog(progress_bar_type);
			}
	        protected void onProgressUpdate(String... progress) {
				// setting progress percentage
	            pDialog.setProgress(Integer.parseInt(progress[0]));
	       }
	        protected void onPostExecute(String result) {
	        	pDialog.dismiss();

	    		if(check==0) {Toast.makeText(getApplicationContext(), "NO SONG FOUND", Toast.LENGTH_SHORT).show();}
	    		else {display();check=0;}
	        	//	dismissDialog(progress_bar_type);
	    	        
	        	//putting the header in the  textview
	        	
				super.onPostExecute(result);
			  }
	        
	}//end of long process

	 //ch=2
	 private class LongOperationdrives extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	
	        	
	        	
	        	
	        	try
	        	{
	        	toServer = new Socket(IPADDRESS,1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

              
	        	//send a message to the server
	        	streamToServer.println("From Timer");
	        	
	        	
	        	//receive vectors from the server
	        	 vector =(Vector<String>)streamFromServer.readObject();
	        	
	        	 size=vector.size();
	        
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		
	    		 //path for headpath
	    		   headpath = String.valueOf(vector.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	                
	    		 //on create activity this code will run
	                
	                	for(int i=0;i<size;i++){
	    	      			titles[i]=String.valueOf(i+1);
	    	      			
	    	      			//seperating the  last name from filenam
	    	      			String path = String.valueOf(vector.elementAt(i));
	    	                 int pos = path.lastIndexOf("\\");
	    	                 String name =path.substring(0 , path.length()-2);
	    	      	        descriptions[i]=name+" MUSIC ";
	    	      	        
	    	      	        images[i]=R.drawable.song;
	    	      		}
	                
	                	choice=2;
	        	streamFromServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{;
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        @Override
			protected void onPreExecute() {
				super.onPreExecute();
			
			}
	        protected void onPostExecute(String result) {
	        	
	        		display();
	        	//	dismissDialog(progress_bar_type);
	    	        
	        	//putting the header in the  textview
	        	
				super.onPostExecute(result);
			  }
	        
	}//end of long process

	 
	 private class LongOperationplay extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	
	        	
	        	
	        	
	        	try
	        	{
	        	toServer = new Socket(IPADDRESS,1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

           
	        	//send a message to the server
	        	streamToServer.println("play");
	    		
	    		streamToServer.println(params[0]);
	        	
	        	
	        	
	        	streamFromServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        protected void onPostExecute(String result) {
	        
	        	//Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_LONG).show();
				super.onPostExecute(result);
			  }
	        
	}//end of long process

	 
	 
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
		String Fav=String.valueOf(vector.elementAt(i));
		
		 
			if(choice==2){try {
			send(Fav);
			//choice=1;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		else if(choice==1) {
		
		playmusic(Fav);
		//choice=1;
		}
		

		//Toast.makeText(this, Fav, Toast.LENGTH_LONG).show();
	}
	
	private void playmusic(String fav) {
		// TODO Auto-generated method stub
		new LongOperationplay().execute(fav);
	}




	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l){
		// TODO Auto-generated method stub
		
		String Fav=String.valueOf(vector.elementAt(i));
		//playmusic(Fav);
		

		
	//Toast.makeText(this, Fav, Toast.LENGTH_LONG).show();
		
	  
		
		return false;
	}
}
