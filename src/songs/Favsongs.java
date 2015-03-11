package songs;
import java.util.Vector;

import com.example.pccontrol1.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mdg.pccontrol1.R;

public class Favsongs extends DashBoardActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

	ListView list;
	String[] titles;
	String[] descriptions;
	int[] images;
	static int choicesong=0;
	
	//TextView header;
	static String headpath="nopath";
	static String head="nohead";
	
	Button addsongs;
	
	static Vector vectorFavSongs = new Vector();

	
	static int size=6;
	VivzAdapter adapter;
    static	String fi;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favsongs);
       
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion <= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
		// Do something for froyo and above versions
		}
		else
		{
	     setFinishOnTouchOutside(false);
		}
		
	
        setHeader(getString(R.string.app_name), true, true);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	
		
		addsongs=(Button) findViewById(R.id.buttonaddsong);
		
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
	
	
	public void addsongs(View v) {
		 Toast.makeText(getApplicationContext(), "size "+vectorFavSongs.size(), Toast.LENGTH_LONG).show();
			
		Intent intent = new Intent(Favsongs.this, computerFavsongs.class);
		startActivity(intent);
		finish();
	}
	
	public Vector<String> getvectorFavSongs() {
		return vectorFavSongs;
	}
	
	public void SongsFavBack(View v) {

        Intent intent=new Intent();  
        intent.putExtra("MESSAGE","cancel");  
          
        setResult(2,intent);  
          
        finish();//finishing activity  

	}
	

	private void display() {
		// TODO Auto-generated method stub
		
		
		 list=(ListView)findViewById(R.id.listViewsongs);
		 adapter =new VivzAdapter(this,titles,descriptions,images); 
		 list.setAdapter(adapter);
		 adapter.notifyDataSetChanged();
		 list.setOnItemClickListener( this);
			list.setOnItemLongClickListener(this);
			//list.setOnItemSelectedListener(this);
	}



	public void send() throws Exception {
		computer MA=new computer();
		vectorFavSongs=MA.getFavVector();
			new LongOperation().execute();
	    	
	    }
	 
	 
	 
	 //longoperations
	 private class LongOperation extends AsyncTask<String, String, String> {
	        @SuppressWarnings("unchecked")
			protected String doInBackground(String... params) {
	        	
	        	
	        	try
	        	{
	        	
                  
	        	

	        	//receive vectors from the server
	        	 size=vectorFavSongs.size();
	        	
	        	
	        	 titles=new String[size];
	    		 descriptions=new String[size];
	    		 images=new int[size];
	    		 
	    		 //path for headpath
	    		   headpath = String.valueOf(vectorFavSongs.elementAt(0));
	                int pos1 = headpath.lastIndexOf("\\");
	                head =headpath.substring(0 , pos1);
	    		 
	    		 for(int i=0;i<size;i++){
	      			
	      			
	      			//seperating the  last name from filenam
	      			String path = String.valueOf(vectorFavSongs.elementAt(i));
	      			if(path.charAt(0)=='@')
	      			{titles[i]=String.valueOf(i+1);
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
	        	 Toast.makeText(getApplicationContext(), "size "+size, Toast.LENGTH_LONG).show();
	     		
				super.onPostExecute(result);
			  }
	        
	}//end of long process

	 
	
	 
	 
	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
		
      choicesong=1;
		
		fi=String.valueOf(vectorFavSongs.elementAt(i));
		
		Toast.makeText(this, " "+fi, Toast.LENGTH_LONG).show();
		Intent intent=new Intent(this,computerFavsongs.class);
		Bundle info =new Bundle();
		info.putString("open", fi);
		intent.putExtras(info);
		startActivity(intent);
	    
        finish();//finishing activity  
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l){
		// TODO Auto-generated method stub
		//Toast.makeText(this, "longclick", Toast.LENGTH_LONG).show();
	  String Fav=String.valueOf(vectorFavSongs.elementAt(i));
		vectorFavSongs.remove(i);
		///////////////////////////////////////
		 computer passvec=new computer();
		 passvec.delFavVector(Fav);
		 passvec.setFavVector(vectorFavSongs);
		
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
		
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
 
	
	