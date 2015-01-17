package songs;
import java.util.Vector;
import com.example.pccontrol1.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mdg.pccontrol1.R;

public class FavList extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

	ListView list;
	String[] titles;
	String[] descriptions;
	int[] images;
	static int choice=0;
	
	//TextView header;
	static String headpath="nopath";
	static String head="nohead";
	
	

	
	static Vector vectorFav = new Vector();

	public FavList() {
		// TODO Auto-generated constructor stub
		computer MA=new computer();
		vectorFav=MA.getFavVector();
	}
	
	static int size=6;
	VivzAdapter adapter;
    static	String fi;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_layout);
       
		setFinishOnTouchOutside(false);
		
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
	
	
	public Vector<String> getVectorFav() {
		return vectorFav;
	}
	
	public void FavBack(View v) {

        Intent intent=new Intent();  
        intent.putExtra("MESSAGE","cancel");  
          
        setResult(2,intent);  
          
        finish();//finishing activity  

	}
	

	private void display() {
		// TODO Auto-generated method stub
		
		
		 list=(ListView)findViewById(R.id.listView2);
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
	      			titles[i]=String.valueOf(i+1);
	      			
	      			//seperating the  last name from filenam
	      			String path = String.valueOf(vectorFav.elementAt(i));
	                 int pos = path.lastIndexOf("\\");
	                 String name =path.substring(pos+1 , path.length());
	      	        descriptions[i]=name;
	      			images[i]=R.drawable.impfile;
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
			 
				super.onPostExecute(result);
			  }
	        
	}//end of long process

	 
	
	 
	 
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
		
		
		fi=String.valueOf(vectorFav.elementAt(i));
		
		
         Intent intent=new Intent();  
         intent.putExtra("MESSAGE",fi);  
           
         setResult(2,intent);  
           
         finish();//finishing activity  
		
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
 
	
	