package com.example.pccontrol1;

import com.mdg.pccontrol1.R;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VivzAdapter extends 	ArrayAdapter<String>
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
	
	 Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
   	      "fonts/timeburner_regular.ttf");
   	      myDescription.setTypeface(custom_font, Typeface.BOLD_ITALIC);
   	    
	
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