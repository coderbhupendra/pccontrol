package com.example.pccontrol1;
import com.mdg.pccontrol1.R;
import database.Comment;
import database.MySQLiteHelper;
import database.CommentsDataSource;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class computer extends DashBoardActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener ,AdapterView.OnItemSelectedListener 
,OnEditorActionListener {
	ListView list;
	String[] titles;
	String[] descriptions;
	int[] images;
	static int choice=0,fileornotflag=0,iperror=0;
	
	TextView header;
	EditText searchword;
	//Button searchbutton;
	static String headpath="nopath";
	static String head="sorry";
	static String search=" ";
	
	SplashScreenActivity spa= new SplashScreenActivity();
	
	
	
	String IPADDRESS="192.168.43.19";
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs" ;
	   public static final String Name = "nameKey"; 
	   public static final String IP = "IPKey"; 
	   
	static Vector<String> vector,searchvector;
	static Vector vectorBack = new Vector();
	static Vector vectorFav = new Vector();
	

	static int size=6;
	VivzAdapter adapter;
    static	String fi;
    
public static CommentsDataSource datasource;
public static MySQLiteHelper help;
	int Scheck=0;
	
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
		
		setContentView(R.layout.activity_main);
	setHeader(getString(R.string.app_name), true, true);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
		
        header=(TextView) findViewById(R.id.textView1);
        searchword=(EditText) findViewById(R.id.editText1);
		//searchbutton=(Button) findViewById(R.id.button6);
        
        
       IPEntry ipe=new IPEntry();
       IPADDRESS=ipe.ip;
       
        
        
        Typeface custom_font = Typeface.createFromAsset(getAssets(),
        	      "fonts/timeburner_regular.ttf");
        	      header.setTypeface(custom_font);
        	      searchword.setTypeface(custom_font,Typeface.BOLD_ITALIC);
		
        	      if(impfilesOuter.choice==4){ 
        	    	  fi=impfilesOuter.fi;
        	    	  //check if the path is file or not
        	    	// new LongOperationfileornot().execute();
        	    	 //copied in its postexecute
        	    	// if(fileornotflag==1){new LongOperationplay().execute();}
        	    	 //else 
        	    	  new LongOperation1().execute();
        	    	 impfilesOuter.choice=0 ;
        	      }
        	      else
        	      {
        	      try {
         				send();
         				} catch (Exception e) {
         					// TODO Auto-generated catch block
         					e.printStackTrace();
         				}
        	      
        	      }
        	       
			
			
		//database
	
		datasource = new CommentsDataSource(this);
	    datasource.open();
		
	    help=new MySQLiteHelper(getApplicationContext());
	    
		
		int counter =spa.counter;
		if(counter==2)
		{
	    
	  List<String> listfiles=help.getAllToDos();
     //  vectorFav=(Vector) listfiles;
       for(int i=0;i<listfiles.size();i++)
       {
    	  vectorFav.add(i, listfiles.get(i)); 
    	 
       }
       //counter=1;
       spa.counter++;
		}
        /////////////
		
		searchword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				search= searchword.getText().toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				search= searchword.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (0 != searchword.getText().length()) {
					search= searchword.getText().toString();
					
				} else {
					search="";
				}
				filter();
			}
		});
		
		 }
	
	//code to restart activity on exception to connect again to server . 
@SuppressLint("NewApi")
public void reset() {
	if (Build.VERSION.SDK_INT >= 11) {
	    recreate();
	} else {
	    Intent intent = getIntent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    overridePendingTransition(0, 0);

	    startActivity(intent);
	    overridePendingTransition(0, 0);
	}
}
	
	@Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	
	//redefining the back button of phone
	public void onBackPressed(){
	   try {
		sendback();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		reset();
	}
	}
	
	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void filter(){
		   try {
			filterlist();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reset();
		}
		}
	

	public void Refresh(View v) throws Exception {
		choice=0;
		send();
	}
	
public void backer(View v) throws Exception {

	sendback();
}

public void openDrive() throws Exception {
	send();
}

@SuppressLint("NewApi")
public void history(View v) throws Exception {
    FavList fv=new FavList();
    if(spa.counter==0)
	vectorFav=fv.getVectorFav();
	
	Intent intent=new Intent(computer.this,FavList.class);  
    startActivityForResult(intent, 2);// Activity is started with requestCode 2 

	//sendback();
}



@Override  
protected void onActivityResult(int requestCode, int resultCode, Intent data)  
{  
          super.onActivityResult(requestCode, resultCode, data);  
              
           // check if the request code is same as what is passed  here it is 2  
            if(requestCode==2)  
                  {  
                     String message=data.getStringExtra("MESSAGE");   
                    // textView1.setText(message);  
                     
                     if(message=="cancel"){}
                     else 
                    	 {
                    	 fi=message;
                     try {
						send1();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						reset();
					}
          
                  }  
                  }

}  

	private void display() {
		// TODO Auto-generated method stub
		
		try{
		 list=(ListView)findViewById(R.id.listView1);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 
		list.setOnItemClickListener( this);
		list.setOnItemLongClickListener(this);
		list.setOnItemSelectedListener(this);
		}
		catch(Exception e)
    	{
			reset();
    	System.out.println("no network" + e ) ;
    	header.setText("PLEASE START THE HOTSPOT FIRST");
    	
     	}
		
	}
	
	private void displaysearchlist() {
		// TODO Auto-generated method stub
		 list=(ListView)findViewById(R.id.listView1);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 
		list.setOnItemClickListener( this);
		list.setOnItemLongClickListener(this);
		list.setOnItemSelectedListener(this);
	}

//getters ans setters
	
	public Vector<String> getFavVector() {
		return vectorFav;
		
	}

	public void setFavVector(Vector<String> vec) {
		vectorFav=vec;
	}
	
	public void delFavVector(String del) {
		help.deleteToDo(del);
		 Log.d("test del",del+1+" ");
		 
	}
	
	public void send() throws Exception {
	    	new LongOperation().execute();
	    	
	    }
	 public void send1() throws Exception {
	    	new LongOperation1().execute();
	    	
	    }
	 public void sendback() throws Exception {
		
		 //check if there is any element present in backvector
		 if(vectorBack.size()>0)
	    	{
			 Toast.makeText(this,vectorBack.size()+"fi"+vectorBack.lastElement(),Toast.LENGTH_SHORT).show();
			 new LongOperationback().execute();}
		 else 
		 { Intent intent = new Intent(this,HomeActivity.class);
			startActivity(intent);
	        finish();//finishing activity  

		 }
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
	        	{datasource.close();
	        		reset();
	        	System.out.println("Exception91" + e ) ;
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
	        		datasource.close();reset();System.out.println("Exception92" + e ) ;
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
		        	
		        	 size=vector.size();
		        	 //convert firatcharater
		        	//String firstLetter = String.valueOf(search.charAt(0)).toUpperCase();
		        	//String newletter=firstLetter+search.substring(1, search.length()-1);
		        	 
		        	int j=0;
		        	 for(int i=0;i<size;i++){
	    	      			
	    	      			String path = String.valueOf(vector.elementAt(i));
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
		    		   headpath = String.valueOf(vector.elementAt(0));
		                int pos1 = headpath.lastIndexOf("\\");
		                head =headpath.substring(0 , pos1);
		                
		    		 //on create activity this code will run
		               j=0;
		                	for(int i=0;i<size;i++){
		    	      			
		    	      			
		    	      			//seperating the  last name from filenam
		    	      			String path = String.valueOf(vector.elementAt(i));
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
		        	{datasource.close();
		        		reset();
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
		        	 if(search!=" ")
		        	displaysearchlist();
		        	 else display();
		        	//putting the header in the  textview
		        	
		        	header.setText(head);
				 
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
	        	else if(choice==1){ vectorBack.add("C:\\"); streamToServer.println("C");}
	        	else if(choice==2){ vectorBack.add("E:\\"); streamToServer.println("E");}
	        	else if(choice==3){ vectorBack.add("D:\\"); streamToServer.println("D");}
	        	
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
	                if(choice!=0){
	                	for(int i=0;i<size;i++){
	    	      			titles[i]=String.valueOf(i+1);
	    	      			
	    	      			//seperating the  last name from filenam
	    	      			String path = String.valueOf(vector.elementAt(i));
	    	                 int pos = path.lastIndexOf("\\");
	    	                 String name =path.substring(pos+1 , path.length());
	    	      	        descriptions[i]=name;
	    	      	        
	    	      	        
	    	      	        //check if file or folder
	    	      	      File file = new File(vector.elementAt(i));
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
	    	      			String path = String.valueOf(vector.elementAt(i));
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
	        	datasource.close();
	        
	        	System.out.println("Exception94" + e ) ;
	        	iperror=1;
	            }
	        	
		return "";
	       }	
	       @Override
	    protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    	  
	    super.onPreExecute();
	    }
	        
	        protected void onPostExecute(String result) {
	        	System.out.println("Except size" + size +" ip "+ iperror ) ;
	        if(iperror==0)
	        	{
	        	display();
	        	//putting the header in the  textview
	        	header.setText(head);}
	        else
	        {iperror=0;
	        System.out.println("Except 44441" ) ;
	        	// Intent	intent = new Intent(computer.this, IPEntry.class);
	        	 finish();
			    //startActivity(intent);
			    
	        	
	        }display();
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

                    
	        	streamToServer.println("back");
	        	int getsize=vectorBack.size();int s=0;
	        	if(getsize==1)s=1;
	        	else s=2;
	        	  
	        	
	        	streamToServer.println(vectorBack.elementAt(vectorBack.size()-s));
	        	//now remove the last position from backvector
	        	
	        	Vector<Integer> ids = new Vector<Integer>();
	        	//receive vectors from the server
	        	
	        	 
	        	//receive vectors from the server
	        	 vector =(Vector<String>)streamFromServer.readObject();
	        	 ids=(Vector<Integer>)streamFromServer.readObject();
	        	 size=vector.size();
	        	
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		 
	    		 //path for headpath
	    		    headpath = String.valueOf(vector.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	    		 
	        		 for(int i=0;i<size;i++){
	      			titles[i]=String.valueOf(i+1);
	      			
	      			//seperating the  last name from filenam
	      			String path = String.valueOf(vector.elementAt(i));
	                 int pos = path.lastIndexOf("\\");
	                 String name =path.substring(pos+1 , path.length());
	      	        descriptions[i]=name;
	      	      File file = new File(vector.elementAt(i));
	     	       if(ids.elementAt(i)==1)
	     	      images[i]=R.drawable.file;
	     	       else  images[i]=R.drawable.folder2;
	      	      
	      		}
	        	
	    		 vectorBack.removeElementAt(vectorBack.size()-1);
		        
	    		 if(vectorBack.size()==0){send();}
	    		 
	    		 if(streamFromServer!=null)streamFromServer.close();
		        	if(streamToServer!=null)streamToServer.close();
	                if(toServer!=null)toServer.close();

	        	}//end of try
	        	catch(Exception e)
	        	{datasource.close();
	        	reset();
	        	System.out.println("Exception95" + e ) ;
	        	}
	        	
		return "";
	       }	
	        
	        protected void onPostExecute(String result) {
	        	display();
	        	//putting the header in the  textview
	        	
	        	header.setText(head);
			 
				super.onPostExecute(result);
			  }
	        
	}//end of long process
	 
	 
	 
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
		
		int open=Integer.parseInt(titles[i]);
		fi=String.valueOf(vector.elementAt(open-1));
		System.out.println(i+" "+open);
		
		
		
		try {
			send1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reset();
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
			Fav=String.valueOf(vector.elementAt(num));
			
			
			Scheck=0;}
		else 
		Fav=String.valueOf(vector.elementAt(i));
		vectorFav.add(Fav);
		
		String path = String.valueOf(Fav);
        int pos = path.lastIndexOf("\\");
        String name =path.substring(pos+1 , path.length());
	       
		Toast.makeText(this,name+" added to your Favorite List", Toast.LENGTH_SHORT).show();
	//	Comment comment = datasource.createComment(Fav,vectorFav.size()+1);
		Log.d("test size",vectorFav.size()+" ");
		Comment comment = datasource.createComment(Fav);
		
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
        	streamToServer.println("inside");
    		streamToServer.println(fi);
    		
    		Vector<Integer> ids = new Vector<Integer>();
        	//receive vectors from the server
        	 vector =(Vector<String>)streamFromServer.readObject();
        	 ids=(Vector<Integer>)streamFromServer.readObject();
        	 size=vector.size();
        	 
        	 titles=new String[size];
    		 descriptions=new String[size];
    		 images=new int[size];
    		 
    		//path for headpath
  		   headpath = String.valueOf(vector.elementAt(0));
              int pos1 = headpath.lastIndexOf("\\");
               head =headpath.substring(0 , pos1);
        	 
        	 for(int i=0;i<size;i++){
     			titles[i]=String.valueOf(i+1);
     			
     			//seperating the  last name from filenam
     			String path = String.valueOf(vector.elementAt(i));
                int pos = path.lastIndexOf("\\");
                String name =path.substring(pos+1 , path.length());
     	        descriptions[i]=name;
     	       File file = new File(vector.elementAt(i));
     	       if(ids.elementAt(i)==1)
     	      images[i]=R.drawable.file;
     	       else  images[i]=R.drawable.folder2;
     	     }
        	 if(size==0)
        	 {	titles[0]=String.valueOf(0);
        	    descriptions[0]="No Data";
        	    images[0]=R.drawable.file;
        	 }

     		//recode the path for back functionality
     		vectorBack.addElement(fi);
     		
     		if(streamFromServer!=null)streamFromServer.close();
        	if(streamToServer!=null)streamToServer.close();
            if(toServer!=null)toServer.close();

        	}//end of try
        	catch(Exception e)
        	{datasource.close();
        		reset();
        	System.out.println("Exception910" + e ) ;
        	datasource.close();
        	}
        	
	return "";
       }	
        
        protected void onPostExecute(String result) {
        	display();
        	header.setText(head);
        	//Toast.makeText(getApplicationContext(),"123"+"fi"+fi,Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		  }
        
}//end of long process



	


	


	
}



