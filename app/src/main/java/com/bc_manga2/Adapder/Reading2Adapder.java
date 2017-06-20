package com.bc_manga2.Adapder;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**要二次解析列表*/
public class Reading2Adapder extends BaseReadingAdapder{
	
	private LayoutInflater inflater;
	private ArrayList<String> ImageUrl;
	private Context context;
	
	public Reading2Adapder(Context context,ArrayList<String> ImageUrl) 
	{
		this.context =context;
		this.ImageUrl = ImageUrl;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return ImageUrl.size();
	}
	@Override
	public ArrayList<String> GetImageUrls() {
		return ImageUrl;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	
	
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		 if (object instanceof View) {
//			 try {
//				mRequestQueue.cancelAll(position+"");
//			} catch (Exception e) { }
			
			container.removeView((View) object);
		    //mReusableViews.add((View) object);//放置一個回收陣列回收被銷毀的View
		    //mReusableViews.offer((View) object);
		 }
		
	}

	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return view.equals(arg1);//这行代码很重要，它用于判断你当前要显示的页面
	}

	
}
