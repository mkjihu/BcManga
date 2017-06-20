package com.bc_manga2.Adapder;

import java.util.ArrayList;

import com.bc_manga2.R;
import com.bc_manga2.Ui.ImageView.TouchImageView;
import com.bc_manga2.Ui.ImageView.TouchImageView2;
import com.bc_manga2.Ui.ProgressBar.ProgressWheel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**純ImageUrl列表*/
public class Reading1Adapder extends BaseReadingAdapder{

	private LayoutInflater inflater;
	private ArrayList<String> ImageUrl;
	private Context context;
	
	public Reading1Adapder(Context context,ArrayList<String> ImageUrl) 
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
	public Object instantiateItem(ViewGroup container, int position) 
	{
		View view = inflater.inflate(R.layout.item_reading_image, container, false);
		ProgressWheel ProgressBar = (ProgressWheel)view.findViewById(R.id.ProgressBar);
		TextView number = (TextView)view.findViewById(R.id.number);
		TouchImageView TouchImage = (TouchImageView)view.findViewById(R.id.TouchImage);
		
		number.setText(position+"");
		
		Glide.with(context)
   				.load(ImageUrl.get(position))
   				//.centerCrop()//中心切圖, 會填滿
   				.asBitmap()
   				.fitCenter()//中心fit, 以原本圖片的長寬為主
   				.error(R.mipmap.ikgbi)//load失敗的Drawable
   				.placeholder(R.mipmap.ikgbi)//loading時候的Drawable
   				//.crossFade()//交叉淡入淡出
   				//.diskCacheStrategy(DiskCacheStrategy.SOURCE)//仅仅只缓存原来的全分辨率的图像
   				.into(new Target(TouchImage,number,ProgressBar));
	
		container.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		return view;
	}

	class Target extends SimpleTarget<Bitmap>
	{
		private TouchImageView TouchImage;
		private TextView number;
		private ProgressWheel ProgressBar;
		public Target(TouchImageView TouchImage,TextView number,ProgressWheel ProgressBar) {
			this.TouchImage = TouchImage;
			this.number = number;
			this.ProgressBar = ProgressBar;
		}
		@Override
		public void onResourceReady(Bitmap arg0, GlideAnimation<? super Bitmap> arg1) {
			//Log.i("ok", "ok");
			TouchImage.setImageBitmap(arg0);
			TouchImage.setZoom(1);
			//TouchImage.setMaxZoom(3.5f);//--最大放大倍率
			ProgressBar.setVisibility(View.GONE);
			number.setVisibility(View.GONE);
		}
	}

	@Override
	public ArrayList<String> GetImageUrls() {
		return ImageUrl;
	}
	
	///=========================
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		 if (object instanceof View) {
//			 try {
//				mRequestQueue.cancelAll(position+"");
//			} catch (Exception e) { }
			container.removeView((View) object);//放置一個回收陣列回收被銷毀的View
		 }
	}

	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return view.equals(arg1);//这行代码很重要，它用于判断你当前要显示的页面
	}

	
}
