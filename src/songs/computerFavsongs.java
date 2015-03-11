package songs;
import com.mdg.pccontrol1.R;
import com.example.pccontrol1.*;

import database.Comment;
import database.MySQLiteHelper;
import database.CommentsDataSource;
import database.MySQLiteHelperSong;

import java.io.File;
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
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class computerFavsongs extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener ,AdapterView.OnItemSelectedListener 
 {
	ListView list;
	String[] titles;
	String[] descriptions;
	int[] images;
	static int choice=0,fileornotflag=0;
	
	
	//Button searchbutton;
	static String headpath="nopath";
	static String head="PLEASE START THE HOTSPOT FIRST .\n" +
	"ENTER CORRECT IP ADSRESS. \n "+
			
			"FOR THAT READ THE INSTRUCTIONS IN IP SETTINGS";
	static String search=" ";
	
	SplashScreenActivity spa= new SplashScreenActivity();
	
	
	
	String IPADDRESS="192.168.43.19";
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs" ;
	   public static final String Name = "nameKey"; 
	   public static final String IP = "IPKey"; 
	   
	static Vector<String> vectorsong,searchvector;
	static Vector vectorBackSongs = new Vector();
	static Vector vectorFavSongs = new Vector();

	static int size=6;
	VivzAdapter adapter;
    static	String fi="null";
    
public static CommentsDataSource datasourcesongs;
public static MySQLiteHelperSong helpsong;
	int Scheck=0;
	int checksong=0;
	/** An array of strings to populate dropdown list */
    String[] actions = new String[] {
        "Settings",
        "IP Setting",
        "Instructions"
    };
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.computerfavsongs);
		
		
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
			//database
		 IPEntry ipe=new IPEntry();
	     IPADDRESS=ipe.ip;
	       
		datasourcesongs = new CommentsDataSource(this);
	    datasourcesongs.open();
		
	    helpsong=new MySQLiteHelperSong(getApplicationContext());
	    
		
			
    try {
		send();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	display();

		 }
	
	@Override
	  protected void onResume() {
	    datasourcesongs.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasourcesongs.close();
	    super.onPause();
	  }
	
	//redefining the back button of phone
	public void onBackPressed(){
	   try {
		sendback();
		   
		   
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void SongsFavBack() {

		 //uncomment this line to open favsong list on last  back of list 19/1/2015
		
		
		//Intent intent = new Intent(this,Favsongs.class);
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
        finish();//finishing activity  

	}
	
	
	public void filter(){
		   try {
			filterlist();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	
		public void Refresh(View v) throws Exception {
		choice=0;
		send();
	}	

		
public void backer(View v) throws Exception {

	sendback();
}



	private void display() {
		// TODO Auto-generated method stub
		
		try{
		 list=(ListView)findViewById(R.id.listViewaddsongsfolder);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 
		list.setOnItemClickListener( this);
		list.setOnItemLongClickListener(this);
		list.setOnItemSelectedListener(this);
		}
		catch(Exception e)
    	{
    	System.out.println("no network" + e ) ;
    	
     	}
		
	}
	
	private void displaysearchlist() {
		// TODO Auto-generated method stub
		 list=(ListView)findViewById(R.id.listViewaddsongsfolder);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 
		list.setOnItemClickListener( this);
		list.setOnItemLongClickListener(this);
		list.setOnItemSelectedListener(this);
	}

//getters ans setters
	
	public Vector<String> getFavVector() {
		return vectorFavSongs;
		
	}

	public void setFavVector(Vector<String> vec) {
		vectorFavSongs=vec;
	}
	
	public void delFavVector(String del) {
		helpsong.deleteToDo(del);
		 Log.d("test del",del+1+" ");
		 
	}
	
	public void send() throws Exception {
		
		
	    new LongOperation().execute();
	    	
	    }
	 public void send1() throws Exception {
	    	new LongOperation1().execute();
	    	
	    }
	 public void sendback() throws Exception {
		
		 list.setEnabled(true);
      	 list.setClickable(true);

		 
		 //check if there is any element present in backvector
		 if(vectorBackSongs.size()>0)
	    	{
			 Toast.makeText(this,vectorBackSongs.size()+"fi"+vectorBackSongs.lastElement(),Toast.LENGTH_SHORT).show();
			 new LongOperationback().execute();
			 }
		 else SongsFavBack();
	    	
	    }
	 
	 public void filterlist() throws Exception {
			
		 //check if there is any element present in backvector
		 	 new LongOperationfilter().execute();
	    	
	    }
	 
	//longoperations
	 public class LongOperationplay extends AsyncTask<String, String, String> {
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

        
	        	//send a message to the server
	        	streamToServer.println("play");
	    		
	    		streamToServer.println(fi);
	        	
	        	
	        	
	    		if(streamFromServer!=null)streamFromServer.close();
	        	if(streamToServer!=null)streamToServer.close();
                if(toServer!=null)toServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception1" + e ) ;
	        	}
	        	
		return "";
	       }	
	        protected void onPostExecute(String result) {
	        
	        	//Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_LONG).show();
				super.onPostExecute(result);
			  }
	        
	}//end of long process 
	 private class LongOperationfileornot extends AsyncTask<String, String, String> {
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

        
	        	//send a message to the server 
	        	streamToServer.println("fileornot");
  	    	    streamToServer.println(fi);
  	    	  
  	    	 fileornotflag=(Integer) streamFromServer.readObject();
	        	
	        	
	        	
  	    	if(streamFromServer!=null)streamFromServer.close();
        	if(streamToServer!=null)streamToServer.close();
            if(toServer!=null)toServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception9" + e ) ;
	        	}
	        	
		return "";
	       }	
	        protected void onPostExecute(String result) {
	        	if(fileornotflag==1){new LongOperationplay().execute();}
   	    	 else {new LongOperation1().execute();  }
	        	//Toast.makeText(getApplicationContext(), fileornotflag +"dd", Toast.LENGTH_LONG).show();
				super.onPostExecute(result);
			  }
	        
	}//end of long process

		 @SuppressLint("DefaultLocale")
		private class LongOperationfilter extends AsyncTask<String, String, String> {
		        @SuppressLint("DefaultLocale")
				protected String doInBackground(String... params) {
		        	
		        	
		        	try
		        	{

		        	//receive vectors from the server
		        	
		        	 size=vectorsong.size();
		        	 //convert firatcharater
		        	//String firstLetter = String.valueOf(search.charAt(0)).toUpperCase();
		        	//String newletter=firstLetter+search.substring(1, search.length()-1);
		        	 
		        	int j=0;
		        	 for(int i=0;i<size;i++){
	    	      			
	    	      			String path = String.valueOf(vectorsong.elementAt(i));
	    	                 int pos = path.lastIndexOf("\\");
	    	                 String name =path.substring(pos+1 , path.length());
	    	                 if(search!=" ")
	    	                 {
	    	               if(name.contains(search))
	    	               {
	    	               j++;
	    	               }
	    	                 }
	    	                 } 
		        	
		        	 titles=new String[j];
		    		 descriptions=new String[j];
		    		 images=new int[j];
		    		 
		    		 //path for headpath
		    		   headpath = String.valueOf(vectorsong.elementAt(0));
		                int pos1 = headpath.lastIndexOf("\\");
		                head =headpath.substring(0 , pos1);
		                
		    		 //on create activity this code will run
		               j=0;
		                	for(int i=0;i<size;i++){
		    	      			
		    	      			
		    	      			//seperating the  last name from filenam
		    	      			String path = String.valueOf(vectorsong.elementAt(i));
		    	                 int pos = path.lastIndexOf("\\");
		    	                 String name =path.substring(pos+1 , path.length());
		    	                 if(search!=" ")
		    	                 {
		    	                	
		    	                if(name.contains(search))
		    	               {titles[j]=String.valueOf(i+1);
		    	               descriptions[j]=name;
		    	               images[j]=R.drawable.folder2;
		    	               
		    	               //  searchvector.add(String.valueOf(vector.elementAt(i)));
		    	             //  searchvector.
		    	               j++;
		    	               Scheck=1;
		    	               }
		    	                
		    	                }
		    	                 
		    	      	       
		    	      	       
		    	      		}
		               
		                	// if(search!=" ")vector=searchvector;
		    	               

		        	}//end of try
		        	catch(Exception e)
		        	{
		        	System.out.println("Exception92" + e ) ;
		        	}
		        	
			return "";
		       }	
		       @Override
		    protected void onPreExecute() {
		    // TODO Auto-generated method stub
		    	  
		    super.onPreExecute();
		    }
		        
		        protected void onPostExecute(String result) {
		        	 if(search!=" ")
		        	displaysearchlist();
		        	 else display();
		        	//putting the header in the  textview
		        	
		        	super.onPostExecute(result);
				  }
		        
		}//end of long process

	 
	 //longoperations
	 private class LongOperation extends AsyncTask<String, String, String> {
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

                  //checking the choice
	        	if(choice==0){
	        	//send a message to the server
	        	streamToServer.println("From Timer");}
	        	else if(choice==1){ vectorBackSongs.add("C:\\"); streamToServer.println("C");}
	        	else if(choice==2){ vectorBackSongs.add("E:\\"); streamToServer.println("E");}
	        	else if(choice==3){ vectorBackSongs.add("D:\\"); streamToServer.println("D");}
	        	
	        	//receive vectors from the server
	        	 vectorsong =(Vector<String>)streamFromServer.readObject();
	        	 size=vectorsong.size();
	        	
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		 
	    		 //path for headpath
	    		   headpath = String.valueOf(vectorsong.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	                
	    		 //on create activity this code will run
	                if(choice!=0){
	                	for(int i=0;i<size;i++){
	    	      			titles[i]=String.valueOf(i+1);
	    	      			
	    	      			//seperating the  last name from filenam
	    	      			String path = String.valueOf(vectorsong.elementAt(i));
	    	                 int pos = path.lastIndexOf("\\");
	    	                 String name =path.substring(pos+1 , path.length());
	    	      	        descriptions[i]=name;
	    	      	        
	    	      	        
	    	      	        //check if file or folder
	    	      	      File file = new File(vectorsong.elementAt(i));
	    	      	      if(file.isFile()){images[i]=R.drawable.folder2;}
	    	      	      //else images[i]=R.drawable.right;
	    	      		}
	                }
	                else
	                {
	                	head ="Drives";
	                	for(int i=0;i<size;i++){
	    	      			titles[i]=String.valueOf(i+1);
	    	      			
	    	      			//seperating the  last name from filenam
	    	      			String path = String.valueOf(vectorsong.elementAt(i));
	    	                descriptions[i]=path;
	    	      			images[i]=R.drawable.drive2;
	    	      		}
	                }
	                
	            if(streamFromServer!=null)streamFromServer.close();
	        	if(streamToServer!=null)streamToServer.close();
                if(toServer!=null)toServer.close();
	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception93" + e ) ;
	        	}
	        	
		return "";
	       }	
	       @Override
	    protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    	  
	    super.onPreExecute();
	    }
	        
	        protected void onPostExecute(String result) {
	        	display();
	        	//putting the header in the  textview
	        super.onPostExecute(result);
			  }
	        
	}//end of long process

	 
	 private class LongOperationback extends AsyncTask<String, String, String> {
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

                    
	        	streamToServer.println("backmusic");
	        	int getsize=vectorBackSongs.size();int s=0;
	        	if(getsize==1)s=1;
	        	else s=2;
	        	  
	        	
	        	streamToServer.println(vectorBackSongs.elementAt(vectorBackSongs.size()-s));
	        	//now remove the last position from backvector
	        	
	        	Vector<Integer> ids = new Vector<Integer>();
	        	//receive vectors from the server
	        	
	        	 
	        	//receive vectors from the server
	        	 vectorsong =(Vector<String>)streamFromServer.readObject();
	        	 ids=(Vector<Integer>)streamFromServer.readObject();
	        	 size=vectorsong.size();
	        	
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		 
	    		 //path for headpath
	    		    headpath = String.valueOf(vectorsong.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	    		 
	    		 for(int i=0;i<size;i++){
	      			titles[i]=String.valueOf(i+1);
	      			
	      			//seperating the  last name from filenam
	      			String path = String.valueOf(vectorsong.elementAt(i));
	                 int pos = path.lastIndexOf("\\");
	                 String name =path.substring(pos+1 , path.length());
	      	        descriptions[i]=name;
	      	      File file = new File(vectorsong.elementAt(i));
	     	       if(ids.elementAt(i)==1)
	     	      images[i]=R.drawable.file;
	     	       else  images[i]=R.drawable.folder2;
	      	      
	      		}
	        	
	    		 vectorBackSongs.removeElementAt(vectorBackSongs.size()-1);
	    		 
	    		  if(vectorBackSongs.size()==0&&checksong==0){send();}
	    		 
	    		 if(streamFromServer!=null)streamFromServer.close();
		        	if(streamToServer!=null)streamToServer.close();
	                if(toServer!=null)toServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{
	        	System.out.println("Exception94" + e ) ;
	        	}
	        	
		return "";
	       }	
	        
	        protected void onPostExecute(String result) {
	        	display();
	        	//putting the header in the  textview
	        	
				super.onPostExecute(result);
			  }
	        
	}//end of long process
	 
	 
	 
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
		
		int open=Integer.parseInt(titles[i]);
		fi=String.valueOf(vectorsong.elementAt(open-1));
		System.out.println(i+" "+open);
		
		
		
		try {
			send1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l){
		// TODO Auto-generated method stub
		
		String Fav;
	
		
		if(Scheck==1){
			TextView textView=(TextView) view.findViewById(R.id.textView1);
			String tt=textView.getText().toString();
		
			int num=Integer.parseInt(tt);num--;
			Fav=String.valueOf("@"+vectorsong.elementAt(num));
			
			
			Scheck=0;}
		else 
		{Fav=String.valueOf("@"+vectorsong.elementAt(i));}
		 computer passvec=new computer();
		 vectorFavSongs=passvec.getFavVector();
		
		 vectorFavSongs.add(Fav);
		//.////////////////
		passvec.setFavVector(vectorFavSongs);
		
		
		String path = String.valueOf(Fav);
        int pos = path.lastIndexOf("\\");
        String name =path.substring(pos+1 , path.length());
	       
		Toast.makeText(this,name+" added to your Favorite List", Toast.LENGTH_SHORT).show();
	//	Comment comment = datasource.createComment(Fav,vectorFavSongs.size()+1);
		Log.d("test size",vectorFavSongs.size()+" ");
		Comment comment = datasourcesongs.createComment(Fav);
		
	
		return false;
	}
 
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		}
	
	private class LongOperation1 extends AsyncTask<String, String, String> {
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


        	//send a message to the server
        	streamToServer.println("musicsteps");
    		streamToServer.println(fi);
    		
    		Vector<Integer> ids = new Vector<Integer>();
        	//receive vectors from the server
        	 vectorsong =(Vector<String>)streamFromServer.readObject();
        	 ids=(Vector<Integer>)streamFromServer.readObject();
        	 size=vectorsong.size();
        	 
        	 
               if(size!=0)
               {
            	   titles=new String[size];
          		 descriptions=new String[size];
          		 images=new int[size];
          		 
          		//path for headpath
        		   headpath = String.valueOf(vectorsong.elementAt(0));
                    int pos1 = headpath.lastIndexOf("\\");
                     head =headpath.substring(0 , pos1);
              	
            	   
            	   for(int i=0;i<size;i++){
     			titles[i]=String.valueOf(i+1);
     			
     			//seperating the  last name from filenam
     			String path = String.valueOf(vectorsong.elementAt(i));
                int pos = path.lastIndexOf("\\");
                String name =path.substring(pos+1 , path.length());
     	        descriptions[i]=name;
     	       File file = new File(vectorsong.elementAt(i));
     	       if(ids.elementAt(i)==1)
     	       images[i]=R.drawable.file;
     	       else  images[i]=R.drawable.folder2;
     	                               
            	   }
            	   
               }
               else
               {    titles=new String[1];
        		    descriptions=new String[1];
        		    images=new int[1];
        		 
            	   	titles[0]="SORRY";
              	    descriptions[0]="No songs in this folder";
              	    images[0]=R.drawable.file;
              	  list.setEnabled(false);
              	  list.setClickable(false);
                    
               }
          	//recode the path for back functionality
     		vectorBackSongs.addElement(fi);
     		
     		if(streamFromServer!=null)streamFromServer.close();
        	if(streamToServer!=null)streamToServer.close();
            if(toServer!=null)toServer.close();

        	}//end of try
        	catch(Exception e)
        	{
        	System.out.println("Exception95" + e ) ;
        	}
        	
	return "";
       }	
        
        protected void onPostExecute(String result) {
       	 display();
        	//Toast.makeText(getApplicationContext(),"123"+"size"+vectorsong.size(),Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		  }
        
}//end of long process



	
}



