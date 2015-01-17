package com.example.pccontrol1;

import com.mdg.pccontrol1.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class impfiles extends DashBoardActivity implements AdapterView.OnItemClickListener {

	ListView list;
	String[] memoTitles={"a","a"};
	String[] memoDescriptions={"a","a"};
	int[] images={R.drawable.ic_launcher,R.drawable.ic_launcher};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.impfiles);
		setHeader(getString(R.string.app_name), true, true);
		
		list=(ListView)findViewById(R.id.listViewimpsongs);
		
		VivzAdapter adapter =new VivzAdapter(this,memoTitles,memoDescriptions,images); 
		list.setAdapter(adapter);
		list.setOnItemClickListener( this);
		 }
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int i, long l) {
		
		Toast.makeText(this,"new row"+i,Toast.LENGTH_SHORT).show();
		
	}
}








