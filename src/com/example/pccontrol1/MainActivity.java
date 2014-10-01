package com.example.pccontrol1;
import database.Comment;
import database.MySQLiteHelper;
import database.CommentsDataSource;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.ActionBar.OnNavigationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener ,AdapterView.OnItemSelectedListener 
,OnEditorActionListener {
	ListView list;
	String[] titles;
	String[] descriptions;
	int[] images;
	static int choice=0;
	
	TextView header;
	EditText searchword;
	//Button searchbutton;
	static String headpath="nopath";
	static String head="nohead";
	static String search=" ";
	
	
	

	static Vector<String> vector,searchvector;
	static Vector vectorBack = new Vector();
	static Vector vectorFav = new Vector();

	static int size=6;
	VivzAdapter adapter;
    static	String fi;
    
	public CommentsDataSource datasource;
	public MySQLiteHelper help;
	int Scheck=0;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
        header=(TextView) findViewById(R.id.textView1);
        searchword=(EditText) findViewById(R.id.editText1);
		//searchbutton=(Button) findViewById(R.id.button6);
        
       
        
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		//database
	
		datasource = new CommentsDataSource(this);
	    datasource.open();
		
		
	    help=new MySQLiteHelper(getApplicationContext());
	  List<String> listfiles=help.getAllToDos();
     //  vectorFav=(Vector) listfiles;
       for(int i=0;i<listfiles.size();i++)
       {
    	  vectorFav.add(i, listfiles.get(i)); 
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
		}
		}
	
	
	
	@Override
	   public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    onBackPressed();
		
	}

	return super.onKeyDown(keyCode, event);
	}
	
//button defenetion
	//public void search(View v) throws Exception {
	//	search=searchword.getText().toString();
		//filter();
	//}
	public void CDrive(View v) throws Exception {
		choice=1;
		send();
	}
public void EDrive(View v) throws Exception {
	choice=2;
	send();
	
	}
public void GDrive(View v) throws Exception {
	choice=3;
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
	vectorFav=fv.getVectorFav();
	
	Intent intent=new Intent(MainActivity.this,FavList.class);  
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
					}
          
                  }  
                  }

}  

	private void display() {
		// TODO Auto-generated method stub
		
		
		 list=(ListView)findViewById(R.id.listView1);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 
		list.setOnItemClickListener( this);
		list.setOnItemLongClickListener(this);
		list.setOnItemSelectedListener(this);
		
		
	}
	
	private void displaysearchlist() {
		// TODO Auto-generated method stub
		display();
	}

//getters ans setters
	
	public Vector<String> getFavVector() {
		return vectorFav;
		
	}

	public void setFavVector(Vector<String> vec) {
		vectorFav=vec;
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
	    	
	    }
	 
	 public void filterlist() throws Exception {
			
		 //check if there is any element present in backvector
		 	 new LongOperationfilter().execute();
	    	
	    }
	 
	//longoperations
		 private class LongOperationfilter extends AsyncTask<String, String, String> {
		        @SuppressWarnings("unchecked")
				protected String doInBackground(String... params) {
		        	
		        	
		        	try
		        	{

		        	//receive vectors from the server
		        	
		        	 size=vector.size();
		        	 //convert firatcharater
		        	String firstLetter = String.valueOf(search.charAt(0)).toUpperCase();
		        	String newletter=firstLetter+search.substring(1, search.length()-1);
		        	 
		        	int j=0;
		        	 for(int i=0;i<size;i++){
	    	      			
	    	      			String path = String.valueOf(vector.elementAt(i));
	    	                 int pos = path.lastIndexOf("\\");
	    	                 String name =path.substring(pos+1 , path.length());
	    	                 if(search!=" ")
	    	                 {
	    	               if(name.contains(search)||name.contains(newletter))
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
		    	                	 Scheck=1;
		    	                if(name.contains(search)||name.contains(newletter))
		    	               {titles[j]=String.valueOf(i+1);
		    	               descriptions[j]=name;
		    	               images[j]=R.drawable.folder2;
		    	               
		    	               searchvector.add(j,String.valueOf(vector.elementAt(i)));
		    	               j++;
		    	               }
		    	                
		    	                }
		    	                 
		    	      	       
		    	      	       
		    	      		}
		               
		                	// if(search!=" ")vector=searchvector;
		    	               

		        	}//end of try
		        	catch(Exception e)
		        	{
		        	System.out.println("Exception9" + e ) ;
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
	        	toServer = new Socket("192.168.43.169",1001);
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
	    	      	      else images[i]=R.drawable.right;
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
	    	      			images[i]=R.drawable.folder2;
	    	      		}
	                }
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
	    // TODO Auto-generated method stub
	    	  
	    super.onPreExecute();
	    }
	        
	        protected void onPostExecute(String result) {
	        	display();
	        	//putting the header in the  textview
	        	
	        	header.setText(head);
			 
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
	        	toServer = new Socket("192.168.43.169",1001);
	        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
	        	streamToServer = new PrintStream(toServer.getOutputStream());

                    
	        	streamToServer.println("back");
	        	int getsize=vectorBack.size();int s=0;
	        	if(getsize==1)s=1;
	        	else s=2;
	        	
	        	streamToServer.println(vectorBack.elementAt(vectorBack.size()-s));
	        	//now remove the last position from backvector
	        	
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
	    		 
	    		 for(int i=0;i<size;i++){
	      			titles[i]=String.valueOf(i+1);
	      			
	      			//seperating the  last name from filenam
	      			String path = String.valueOf(vector.elementAt(i));
	                 int pos = path.lastIndexOf("\\");
	                 String name =path.substring(pos+1 , path.length());
	      	        descriptions[i]=name;
	      	      File file = new File(vector.elementAt(i));
	      	    images[i]=R.drawable.folder2;
	      	      
	      		}
	        	
	    		 vectorBack.removeElementAt(vectorBack.size()-1);
		        	
	        	streamFromServer.close();

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
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l){
		// TODO Auto-generated method stub
		//Toast.makeText(this, "longclick", Toast.LENGTH_LONG).show();
		String Fav;
		if(Scheck==1){Fav=String.valueOf(searchvector.elementAt(i));  Scheck=0;}
		else 
		Fav=String.valueOf(vector.elementAt(i));
		vectorFav.add(Fav);
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
        	toServer = new Socket("192.168.43.169",1001);
        	streamFromServer = new ObjectInputStream(toServer.getInputStream());
        	streamToServer = new PrintStream(toServer.getOutputStream());


        	//send a message to the server
        	streamToServer.println("inside");
    		streamToServer.println(fi);
    		
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
        	 
        	 for(int i=0;i<size;i++){
     			titles[i]=String.valueOf(i+1);
     			
     			//seperating the  last name from filenam
     			String path = String.valueOf(vector.elementAt(i));
                int pos = path.lastIndexOf("\\");
                String name =path.substring(pos+1 , path.length());
     	        descriptions[i]=name;
     	       File file = new File(vector.elementAt(i));
     	      images[i]=R.drawable.folder2;
     	     }


     		//recode the path for back functionality
     		vectorBack.addElement(fi);
     		
        	
        	streamFromServer.close();

        	}//end of try
        	catch(Exception e)
        	{
        	System.out.println("Exception9" + e ) ;
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



class VivzAdapter extends 	ArrayAdapter<String>
{
Context context;
String[] titleArray;
String[] descriptionArray;
int[] images;
	public VivzAdapter(Context c,String[] titles,String[] desc,int [] imgs) {
		super(c,R.layout.single_row,R.id.textView1,titles);
		this.context=c;
		this.images=imgs;
		this.titleArray=titles;
		this.descriptionArray=desc;
		}

	class MyViewHolder
	{ImageView myimages;
	TextView myTitle;
	TextView myDescription;
		MyViewHolder(View v){
		 myimages=(ImageView)v.findViewById(R.id.imageView1);
	 myTitle=(TextView) v.findViewById(R.id.textView1);
	 myDescription=(TextView) v.findViewById(R.id.textView2);
	
		}	
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row=convertView;
		MyViewHolder holder=null;
		if(row==null){
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row=inflater.inflate(R.layout.single_row,parent,false);
		
		holder=new MyViewHolder(row);
		row.setTag(holder);
		Log.d("vivz","new row");
		
		}
		
		else
		{
			holder=(MyViewHolder) row.getTag();
			Log.d("vivz","recycling row");
		}
		
		holder.myimages.setImageResource(images[position]);
		holder.myTitle.setText(titleArray[position]);
		holder.myDescription.setText(descriptionArray[position]);
		
		return row;
	}
	}