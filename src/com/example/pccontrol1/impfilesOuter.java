package com.example.pccontrol1;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mdg.pccontrol1.R;

import database.MySQLiteHelper;

public class impfilesOuter extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

	ListView list;
	String[] titles;
	String[] descriptions;
	int[] images;
	static int choice=0;
	
	//TextView header;
	static String headpath="nopath";
	static String head="nohead";
	
	
	SplashScreenActivity spa= new SplashScreenActivity();
	public static MySQLiteHelper help;
	
	static Vector vectorFav = new Vector();

	public impfilesOuter() {
		// TODO Auto-generated constructor stub
		computer MA=new computer();
		vectorFav=MA.getFavVector();
	}
	
	static int size=6;
	VivzAdapter adapter;
    static	String fi;
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
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.impsongs);
		
		computer cfs=new computer();
		  MySQLiteHelper help=new MySQLiteHelper(getApplicationContext());
		List<String> listfiles=help.getAllToDos();
	//	Toast.makeText(this, "tt  "+listfiles.size(), Toast.LENGTH_SHORT).show();
		
		Vector vectorFavSongs = new Vector();
		SplashScreenActivity spa =new SplashScreenActivity();
		if(spa.counter==2)
		{
			for(int i=0;i<listfiles.size();i++)
		
	       {
	    	     String implist=listfiles.get(i);
	    	     if(implist.contains("@")) 
	    		 vectorFavSongs.add(listfiles.get(i));
	    	     else 
	    	       vectorFav.add(listfiles.get(i));
	    			
	       }
		spa.counter++;
		cfs.setFavVector(vectorFav);
		cfs.setFavSongVector(vectorFavSongs);
		}
		 
		
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getActionBar().setHomeButtonEnabled(true);
		 }
	    
	
	
	public Vector<String> getVectorFav() {
		return vectorFav;
	}
	
	
	

	private void display() {
		// TODO Auto-generated method stub
		
		
		 list=(ListView)findViewById(R.id.listViewimpfiles);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 list.setOnItemClickListener( this);
			list.setOnItemLongClickListener(this);
			//list.setOnItemSelectedListener(this);
	}



	public void send() throws Exception {
		
		computer MA=new computer();
		vectorFav=MA.getFavVector();
			new LongOperation().execute();
	    	
	    }
	 
	 
	 
	 //longoperations
	 private class LongOperation extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	
	        	
	        	try
	        	{
	        	
                  
	        	

	        	//receive vectors from the server
	        	 size=vectorFav.size();
	        	
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		 
	    		 //path for headpath
	    		   headpath = String.valueOf(vectorFav.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	    		 
	    		 for(int i=0;i<size;i++){
	      			
	      			//seperating the  last name from filenam
	      			String path = String.valueOf(vectorFav.elementAt(i));
	      			if(path.charAt(0)!='@')
	      			{
	      				titles[i]=String.valueOf(i+1);
		      			
	                 int pos = path.lastIndexOf("\\");
	                 String name =path.substring(pos+1 , path.length());
	      	        descriptions[i]=name;
	      			images[i]=R.drawable.impfile;
	      			}
	      		}
	        

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        
	        protected void onPostExecute(String result) {
	        	display();
	        	//putting the header in the  textview
	        	
	        	//header.setText(head);
	        	 System.out.print("size "+vectorFav.size());
				super.onPostExecute(result);
			  }
	        
	}//end of long process

	 
	 private class LongOperationfileornot extends AsyncTask<String, String, String> {
		 Vector<Integer> check = new Vector<Integer>();
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	Socket toServer;
	        	ObjectInputStream streamFromServer;
	        	PrintStream streamToServer;
	        	
	        	
	        	
	        	try
	        	{
	        	toServer = new Socket(IPEntry.ip,1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

     
	        	//send a message to the server 
	        	streamToServer.println("fileornot");
	    	    streamToServer.println(fi);
	    	  
	    	    
	        	//receive vectors from the server
	        	
	        	 
	        	//receive vectors from the server
	        	
	        	 check=(Vector<Integer>)streamFromServer.readObject();
	        	
	        	
	        	
	        	streamFromServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        protected void onPostExecute(String result) {
	        	if(check.elementAt(0)==1){new LongOperationplay().execute();}
	    	 else {
	    		 Intent intent = new Intent(impfilesOuter.this, computer.class);
	    			startActivity(intent);
	    	 }
	        //	Toast.makeText(getApplicationContext(), check.elementAt(0) +"dd", Toast.LENGTH_LONG).show();
				super.onPostExecute(result);
			  }
	        
	}//end of long process
	 
	 public class LongOperationplay extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	Socket toServer;
	        	ObjectInputStream streamFromServer;
	        	PrintStream streamToServer;
	        	try
	        	{
	        	toServer = new Socket(IPEntry.ip,1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

     
	        	//send a message to the server
	        	streamToServer.println("play");
	    		
	    		streamToServer.println(fi);
	        	
	        	
	        	
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
		
		
		fi=String.valueOf(vectorFav.elementAt(i));
		choice=4;
		//computer.choice=4;
		new LongOperationfileornot().execute();
		
       
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l){
		// TODO Auto-generated method stub
		//Toast.makeText(this, "longclick", Toast.LENGTH_LONG).show();
	  String Fav=String.valueOf(vectorFav.elementAt(i));
		vectorFav.remove(i);
		 
		
		//CommentsDataSource datasource=MainActivity.datasource;
	   
		
		
		//MySQLiteHelper del= MainActivity.help;
		
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		String tt=textView.getText().toString();
		
		String path = String.valueOf(Fav);
        int pos = path.lastIndexOf("\\");
        String name =path.substring(pos+1 , path.length());
	        
		Toast.makeText(getApplicationContext(),name+ " Removed From List", Toast.LENGTH_LONG).show();
		int num=Integer.parseInt(tt);num--;
		
		//datasource.deleteComment(num-1);
		//del.deleteToDo(num);
		
		//pass this vecmtor to mainactivity
		computer MA=new computer();
		MA.delFavVector(Fav);
		MA.setFavVector(vectorFav);
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
 
	
	