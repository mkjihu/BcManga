package com.bc_manga2.Adapder;

import java.util.ArrayList;

import com.bc_manga2.R;
import com.bc_manga2.Adapder.Reading1Adapder.Target;
import com.bc_manga2.Ui.ImageView.TouchImageView;
import com.bc_manga2.Ui.ProgressBar.ProgressWheel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/***
 * 直向圖片列
 * @author kevinh
 */
public class ReadingRecyc1Adapder extends BaseReadingAdapder2{
	
	private LayoutInflater inflater;
	private ArrayList<String> ImageUrl;
	private Context context;
	
	public ReadingRecyc1Adapder(Context context,ArrayList<String> ImageUrl) 
	{
		this.context =context;
		this.ImageUrl = ImageUrl;
		inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public ArrayList<String> GetImageUrls() {
		return ImageUrl;
	}

	@Override
	public int getItemCount() {
		return ImageUrl.size();
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		//產生View
		return new ImageItem(inflater.inflate(R.layout.item_reading_image2,arg0, false));
	}
	@Override
	public void onBindViewHolder(ViewHolder arg0, int position) {
		ImageItem imageItem = (ImageItem)arg0;
		if(getItemCount()>0)
		{
			imageItem.instantiateItem(position);
			//indexItem.ComicIndexItem_text.setTag(arg1);
		}
	}

	
	public class ImageItem extends ViewHolder
	{
		private ProgressWheel ProgressBar;
		private TextView number;
		//private TouchImageView TouchImage;
		private ImageView TouchImage;
		
		public ImageItem(View itemView) {
			super(itemView);
			ProgressBar = (ProgressWheel)itemView.findViewById(R.id.ProgressBar);
			number = (TextView)itemView.findViewById(R.id.number);
			//TouchImage = (TouchImageView)itemView.findViewById(R.id.TouchImage);
			TouchImage= (ImageView)itemView.findViewById(R.id.TouchImage);
			
		}
		
		public void instantiateItem(int position) {
			number.setText(position+"");
			Glide.with(context)
				.load(ImageUrl.get(position))
				.asBitmap()
				//.centerCrop()//中心切圖, 會填滿
				.fitCenter()//中心fit, 以原本圖片的長寬為主
				.error(R.mipmap.ikgbi)//load失敗的Drawable
				//.placeholder(R.mipmap.ikgbi)//loading時候的Drawable
				//.crossFade()//交叉淡入淡出
				//.diskCacheStrategy(DiskCacheStrategy.SOURCE)//仅仅只缓存原来的全分辨率的图像
				.into(new Target(TouchImage,number,ProgressBar));
		}
	}
	
	public class Target extends SimpleTarget<Bitmap>
	{
		//private TouchImageView TouchImage;
		private ImageView TouchImage;
		private TextView number;
		private ProgressWheel ProgressBar;
		public Target(ImageView TouchImage,TextView number,ProgressWheel ProgressBar) {
			this.TouchImage = TouchImage;
			this.number = number;
			this.ProgressBar = ProgressBar;
		}
		@Override
		public void onResourceReady(Bitmap arg0, GlideAnimation<? super Bitmap> arg1) {
			//Log.i("ok", "ok");
			TouchImage.setImageBitmap(arg0);
			//TouchImage.setZoom(1);
			//TouchImage.setMaxZoom(3.5f);//--最大放大倍率
			//TouchImage.setAdjustViewBounds(true);
			
			ProgressBar.setVisibility(View.GONE);
			number.setVisibility(View.GONE);
		}
	}
	
}
