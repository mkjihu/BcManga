package com.bc_manga2.Activity;

import java.util.concurrent.TimeUnit;

import com.bc_manga2.R;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.view.RxView;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;
import io.reactivex.functions.Consumer;

public class TextUIActivity extends AppCompatActivity {

	public ToggleButton button1;
	
	String aasaf = "http://99.1112223333.com/dm06//ok-comic06/y/8211/act_242/z_0001_75585.JPG";//"http://s1.comic.ccddnn.net/img/tQ1AdnXnK.jpg";//"https://store.mybenefit.com.tw/res/images//event/resize/re_big_event2016101215484444.jpg";
	ImageView imageView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_ui);
		//setContentView(R.layout.item_reading_image);
		
		
		SimpleDraweeView draweeView = (SimpleDraweeView)findViewById(R.id.imageg1);
		draweeView.setImageURI(Uri.parse(aasaf));
		
		/**/
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		
		Glide.with(this)
       		.load(aasaf)
       	//.centerCrop()//中心切圖, 會填滿
       		.fitCenter()//中心fit, 以原本圖片的長寬為主
       		.error(R.mipmap.ikgbi)//load失敗的Drawable
       		.placeholder(R.mipmap.ikgbi)//loading時候的Drawable
       		.crossFade()//交叉淡入淡出
       		.into(imageView1);
       		
		//((ExpandableTextView) findViewById(R.id.tv)).setText("asfassssssssssssssssssssssssssssssssssssssfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
		//MarqueeTextView ddddd = (MarqueeTextView)findViewById(R.id.ComicIndexItem_text);
		//ddddd.scrollText(15);
		
		
		button1 = (ToggleButton)findViewById(R.id.button1);
		
		//button1.setChecked(true);
		//button1.toggle();
		
		RxView.clicks(button1)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				Log.i("1", "1");
			}
		});
		
	}

}
