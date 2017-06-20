package com.bc_manga2.Activity;
import java.util.concurrent.TimeUnit;

import org.greenrobot.eventbus.EventBus;

import com.base.BackActivity.SwipeBackActivity;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.Presenter.ComicDirectoryPresenter;
import com.bc_manga2.R;
import com.bc_manga2.Ui.ImageView.CircleImageView;
import com.bc_manga2.Ui.Textview.ExpandableTextView;
import com.bc_manga2.View.maView;
import com.bc_manga2.obj.FastBlurUtil;
import com.bc_manga2.obj.IwillPaint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.view.RxView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.greenrobot.BcComicdao.BcIndexData;
import io.reactivex.functions.Consumer;

public class ComicDirectoryPage extends SwipeBackActivity implements maView,SwipeRefreshLayout.OnRefreshListener,SwipeBackActivity.DataCallback {
	//SwipeBackActivity
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private RecyclerView recycler_view;
	private ComicDirectoryPresenter presenter;
	private AppBarLayout mAppBarLayout;
	private Toolbar mToolbar;
	private SimpleDraweeView maim_image;
	private ImageView back_icon,background_image;
	private ToggleButton keepbt;
	private TextView title0,title,AuthorName,Type,Updated_day;
	private ExpandableTextView Gist;
	
	private String HomePK;//-屬於哪個主站
	private String Url;//-進入錄頁網址
	private String ImageUrl;//--圖連
	private String Name;//--書名
	
	private String Index_PKUrl;//-真目錄頁網址
	
	/**
	 * 該連結解析方式是否需要特殊手段
	 * false為預設--不用
	 * true為要二度解析
	 */
	public boolean SpecialType = false;
	
	private CircleImageView profile_image;
	private FloatingActionButton fabb_bt1;
	
	private boolean isVisible = true;//是否初次加載
	private IwillPaint iwillPaint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comic_directory_page);//setContentView(R.layout.text_activity_comic_directory_page);
		frid();
		presenter = new ComicDirectoryPresenter(this);
		iwillPaint = new IwillPaint(this, "",1);
		/**/
		SpecialType = getIntent().getExtras().getBoolean("SpecialType");//是否重取
		ImageUrl = getIntent().getExtras().getString("ImageUrl");//
		Name = getIntent().getExtras().getString("Name");//名稱
		Url = getIntent().getExtras().getString("Url");//網址
		HomePK = getIntent().getExtras().getString("HomePK");//主站key
		if (!SpecialType) {
			title.setText(Name);
			GetDate(ImageUrl);
		}
		
		
		switch (HomePK) {
		case "k886":
			recycler_view.setLayoutManager(new GridLayoutManager(this, 4));//这里修改用线性宫格显示 类似于grid view
			break;

		default:
			break;
		}
		
		
		presenter.GetIndexDate(HomePK,Url, SpecialType);
		EventBus.getDefault().post(new MessageEvent(TagInfo.YueduTage));
		
	}
	
	public void SetInfoDate(BcIndexData indexData) {
		GetDate(indexData.getImageUrl());//GetDate(indexData.getImageUrl());
		title.setText(indexData.getTitleName());
		title0.setText(indexData.getTitleName());
		AuthorName.setText(indexData.getAuthorName());
		Type.setText(indexData.getType());
		Updated_day.setText(indexData.getUpdated_day());
		Log.i("Gist", indexData.getGist());
		Gist.setText(indexData.getGist());
		Index_PKUrl = indexData.getPKUrl();
		//Log.i("取得是否收藏", indexData.getIsCollect()+"");
		SetKeepChecked(indexData.getIsCollect());
		
		
		
		
		
	}
	
	public void ToComicReading(String Image_PKUrl) {
		Intent intent = new Intent(this, ComicReadingPage.class);
		intent.putExtra("Index_PKUrl", Index_PKUrl);//目錄頁PK-真目錄頁網址
		intent.putExtra("Image_PKUrl", Image_PKUrl);//第幾話的第一頁 得URL
		intent.putExtra("HomePK", HomePK);//主站key
		intent.putExtra("TitleName", title.getText().toString()+"");//--書名
		startActivity(intent);
	}
	
	
	private void GetDate(String imageURL) {
		Log.i("圖", imageURL);
		//如果圖片不為錯誤圖
		if (!imageURL.equals("http://s1.comic.ccddnn.net/img/on39pLvnw.jpg")) {
			maim_image.setImageURI(imageURL);
			
			Glide.with(this) 
		 	.load(imageURL)
		 	.error(R.mipmap.errorimg1)
	 		.placeholder(R.mipmap.errorimg1)
	 		.crossFade()//交叉淡入淡出
	 		.transform(new BlurTransformation(this))
	 	    //.bitmapTransform( new BlurTransformation( context ) ) // this would work too!
	 	    .into(background_image); 
			
			Glide.with(this) 
		 	.load(imageURL)
		 	.error(R.mipmap.errorimg1)
	 		.placeholder(R.mipmap.errorimg1)
	 	    .into(profile_image); 
		}
	}
	public class BlurTransformation extends BitmapTransformation {
	    public BlurTransformation(Context context) {
	        super( context );
	    }
	    @Override
	    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
	    	Log.i("是否執行緒", BcApplication.isInMainThread());
	        //return BitmapBlurHelper.doBlur(ComicDirectoryPage.this, toTransform,5); // todo
	    	
	    	int blurRadius = 8;
	    	/*
	    	int scaleRatio = 5;//可以设置模糊度哦
	    	Bitmap scaledBitmap = Bitmap.createScaledBitmap(toTransform,
	    			toTransform.getWidth() / scaleRatio,
	    			toTransform.getHeight() / scaleRatio,
	                false);*/
	        return FastBlurUtil.doBlur(toTransform, blurRadius, true);
	    }
	    @Override
	    public String getId() {
	    	return "blur"; // todo
	    }
	}
	
	
	public void SetKeepChecked(boolean checked) {
		keepbt.setChecked(checked);
		if (checked) {
			fabb_bt1.setImageResource(R.mipmap.keep_on);
		} else{
			fabb_bt1.setImageResource(R.mipmap.keep_off);
		}
		EventBus.getDefault().post(new MessageEvent(TagInfo.ColleTage));
	}
	

	@Override
	public void onRefresh() {
		//下拉更新執行
		new Handler().postDelayed(new Runnable()  {
			@Override
			public void run()  {
				try {
					recycler_view.setAdapter(null);	
				} catch (Exception e) {}
				presenter.GetIndexDate(HomePK,Url, SpecialType);
			}
		}, 1000);
	}
	
	@Override
	public void offRefresh() {
		mSwipeRefreshLayout.setRefreshing(false);
		try {
			iwillPaint.dissdig();
		} catch (Exception e) {}
	}
	@Override
	public void openRefresh() {
		if (isVisible) {//初次加載
			isVisible = false;
			iwillPaint.showdig();
		}
		mSwipeRefreshLayout.setRefreshing(true);
	}
	@Override
	public void RecyclerViewsetData(Adapter<ViewHolder> adapter) {
		try {
			recycler_view.setAdapter(adapter);	
		} catch (Exception e) {}
	}
	
	
	private void frid() {
		background_image = (ImageView)findViewById(R.id.background_image);
		back_icon = (ImageView)findViewById(R.id.back_icon);
		maim_image = (SimpleDraweeView)findViewById(R.id.maim_image);
		recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
		keepbt = (ToggleButton)findViewById(R.id.keepbt);
		
		
		title0 = (TextView)findViewById(R.id.title0);
		title = (TextView)findViewById(R.id.title);
		AuthorName = (TextView)findViewById(R.id.AuthorName);
		Type = (TextView)findViewById(R.id.Type);
		Updated_day = (TextView)findViewById(R.id.Updated_day);
		
		Gist =(ExpandableTextView) findViewById(R.id.Gist);
		recycler_view.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
		recycler_view.setLayoutManager(new GridLayoutManager(this, 3));//这里用线性宫格显示 类似于grid view
		//recycler_view.setAdapter(new RecycleAdapter());
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mSwipeRefreshLayout);
		//mSwipeRefreshLayout.setDistanceToTriggerSync(168);//触发刷新动画时手指需要下拉的距离
		//设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
		//mSwipeRefreshLayout.setProgressViewEndTarget(false, 200);
		mSwipeRefreshLayout.setColorSchemeResources(
			    android.R.color.holo_blue_bright,
			    android.R.color.holo_green_light,
			    android.R.color.holo_orange_light,
			    android.R.color.holo_red_light);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		 
	    profile_image = (CircleImageView)findViewById(R.id.profile_image);
		fabb_bt1 = (FloatingActionButton)findViewById(R.id.fabb_bt1);
		mToolbar= (Toolbar) findViewById(R.id.AppFragment_Toolbar);
		mAppBarLayout= (AppBarLayout) findViewById(R.id.AppFragment_AppBarLayout);
		/**/
		mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				
				//Log.i("數值百分比變化", Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()+"");
				//mToolbar.setBackgroundColor(changeAlpha(ContextCompat.getColor(ComicDirectoryPage.this, R.color.colorPrimary),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
				
				//mAppBarLayout.getTotalScrollRange() 拿到actionbar可滚动的最大距离
			    //verticalOffset 当前的滚动距离
			    if (Math.abs(verticalOffset) > mAppBarLayout.getTotalScrollRange()/1.15) {
			    	title0.setVisibility(View.VISIBLE);
			    	fabb_bt1.setVisibility(View.VISIBLE);
			    	profile_image.setVisibility(View.VISIBLE);
			    } else {
			    	title0.setVisibility(View.GONE); 
			    	fabb_bt1.setVisibility(View.GONE);
			    	profile_image.setVisibility(View.GONE);
			    }  
			}
		});
		
		
		RxView.clicks(back_icon)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				onBackPressed();
			}
		});
		
		RxView.clicks(keepbt)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				presenter.KeepData(Index_PKUrl);
			}
		});
		
		RxView.clicks(fabb_bt1)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				presenter.KeepData(Index_PKUrl);
			}
		});
		
	}
	
	
	@Override
	protected void onDestroy() {
		presenter.destroy();
		super.onDestroy();
	}	
	
	
	/** 根据百分比改变颜色透明度 */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
	//========================================





	
}
