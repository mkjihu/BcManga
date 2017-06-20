package com.bc_manga2.obj;


import com.bc_manga2.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class IwillPaint 
{
	//public Context context;
	public Dialog drg3;
	//public String title;
	
	public IwillPaint(Context context,String title,int sty) 
	{
		
		//-
		View dr3;
		switch (sty) {
		case 1:
			dr3= LayoutInflater.from(context).inflate(R.layout.dig3_layout,null);
			break;
		case 2:
			dr3= LayoutInflater.from(context).inflate(R.layout.dig4_layout,null);
			break;
		default:
			dr3= LayoutInflater.from(context).inflate(R.layout.dig3_layout,null);
			break;
		}
		
		
		TextView dr3_tv = (TextView)dr3.findViewById(R.id.dr3_tv);
		if (title.equals("")) {
			dr3_tv.setVisibility(View.GONE);
		}
		else
		{
			dr3_tv.setText(title);
		}
		
		
		drg3 = new Dialog(context,R.style.MyDialog);
		drg3.setCanceledOnTouchOutside(false);
		drg3.setCancelable(true);
		drg3.setContentView(dr3);
		
		//drg3.show();
	}
	
	public void dissdig() {
		if(drg3!=null && drg3.isShowing())
		{
			drg3.dismiss();
		}
	}
	public void showdig() {
		if(drg3!=null && !drg3.isShowing())
		{
			drg3.show();
		}
	}
}
