package com.bc_manga2.Activity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.greenrobot.eventbus.EventBus;

import com.bc_manga2.Adapder.BaseReadingAdapder2;
import com.bc_manga2.Adapder.BsFragmentStatePagerAdapter;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Fragment.Fragment_ComicItem;
import com.bc_manga2.Presenter.ComicReading2Presenter;
import com.bc_manga2.R;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.bc_manga2.Ui.Fualay;
import com.bc_manga2.Ui.Fualay.OnPrViewListener;
import com.bc_manga2.Ui.ZoomRecrycleView;
import com.bc_manga2.Ui.ViewPager.CustomViewPager;
import com.bc_manga2.View.maView;
import com.jakewharton.rxbinding2.view.RxView;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

public class ComicReading2Page extends AppCompatActivity implements maView
{
	private final String Tag = "ComicReadingPage";
	
	private CustomViewPager viewPager;
	private Fualay fualay;
	private ComicReading2Presenter presenter;
	private BsFragmentStatePagerAdapter fragmentStatePagerAdapter;
	//------------上下左右-----------
	private PopupWindow popupWindow_top,popupWindow_bottom,popupWindow_R,popupWindow_L;
	private View popupview_top,popupview_bottom,popupview_R,popupview_L;
	private SeekBar seekd;
	private boolean kr_tgs =false;//狀態         true：目前上下打開      false：目前上下關閉
	private FrameLayout wet;
	private ImageView popupview_backimg;
	private ImageView popupview_bottom_img1,popupview_bottom_img2;
	private TextView Denominator,Molecule,popupview_title;
	
	//-下方顯示
	private TextView number1,number2,titlename;
	
	private LinearLayout faiel;
	private TextView AQtextview;
	
	//---------------------
	private String Index_PKUrl;//目錄頁PK-真目錄頁網址
	private String Image_PKUrl;//第幾話的第一頁 得URL 圖列PK
	private String HomePK;//-屬於哪個主站
	//=====
	//private BcIndexData data;
	private ArrayList<ItemComicIndex> alist = null;//DB裡所有話數的item列
	private int target;//目前第幾話
	private String TitleName;//書名
	//private boolean interpret = true;//interpret 是否拿純圖片列表
	//private ArrayList<String> ImageUrls;//-當前話數圖片列表
	
	//---預計之後+
	private ZoomRecrycleView recycler_view;
	private LinearLayoutManager layoutManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		setContentView(R.layout.activity_comic_reading2_page);
		EventBus.getDefault().register(this);//订阅
		fid();
		presenter = new ComicReading2Presenter(this);
		
		{
			newPopupWindow();
			ViewPageListener();
			setlinte();
		}
		{
			Index_PKUrl = getIntent().getExtras().getString("Index_PKUrl");//目錄頁PK
			Image_PKUrl = getIntent().getExtras().getString("Image_PKUrl");//第幾話的第一頁 URL
			HomePK = getIntent().getExtras().getString("HomePK");//主站key
			TitleName = getIntent().getExtras().getString("TitleName");//書名
			//interpret = Shared.GetIsImageAnalysis(this);
		}
		presenter.GetDB(Index_PKUrl, Image_PKUrl);

	}
	
	
	/**取得當前是第幾話的第一頁*/
	public void GetDBtarget(int target) {
		this.target = target;
		popupview_title.setText(TitleName);
		titlename.setText(alist.get(target).getItemName());
		
		//--是否橫向加載
		if (Shared.GetReadingMethod(this)) {
			presenter.GetImageList(alist);
		}else{
			//presenter.GetImageList2(HomePK,alist.get(target),false);
		}
	}
	
	/**載入橫向Adapder*/
	public void PackAdapder(ArrayList<Fragment_ComicItem> mFragments) {
		fragmentStatePagerAdapter = new BsFragmentStatePagerAdapter(getSupportFragmentManager(), mFragments);
		
		/**通过反射改变ViewPager的初始化页面index*/
		try {
			Field field  = ViewPager.class.getDeclaredField("mRestoredCurItem");
			field.setAccessible(true);
			field.set(viewPager, target);//target 为我们想要的第一次就展示的页面index
		} catch (Exception e) { }
		viewPager.setAdapter(fragmentStatePagerAdapter);
		pupe();
	}
	
	
	
	/**載入下一話或上一話*/
	public void NextorLast(int target) {
		this.target = target;
		popupview_title.setText(TitleName);
		titlename.setText(alist.get(target).getItemName());
	}
	
	
	/**載入直向Adapder*/
	public void SetRecyclerViewData(BaseReadingAdapder2 adapter) {

		auae();
		//Log.i(Tag, adapter.GetImageUrls().size()+"");
		//this.ImageUrls = adapter.GetImageUrls();
		
		seekd.setMax(adapter.getItemCount()-1);
		
		Denominator.setText(adapter.getItemCount()+"");//--最大數
		//Molecule.setText(viewPager.getCurrentItem()+1+"");
		
		//number1.setText(viewPager.getCurrentItem()+1+"");
		number2.setText(adapter.getItemCount()+"");
		
		recycler_view.setAdapter(adapter);
		
		
	}
	
	/**橫向下一 或上一 */
	public void NextorLast()
	{
		
	}
	
	/**直向下一 或上一 */
	public void NextorLast(BaseReadingAdapder2 adapter,String NoL)
	{
		
	}
	
	
	/**viewpage 監聽*/
	private void ViewPageListener()
	{
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			private int lastPage = 0;
			@Override
			public void onPageSelected(int arg0) {
				
	            //VerImageFragment fragment = fragmentPagerAdapter.getItem(arg0);
				Fragment_ComicItem fragment = (Fragment_ComicItem)fragmentStatePagerAdapter.instantiateItem(viewPager,arg0);
				//VerImageFragment fragment = fragments.get(arg0);
			    if(lastPage>arg0)
			    {
			    	//Log.i("滑動", "左邊");
			    	//fragment.clai("左邊");
			    } else{
			    	//Log.i("滑動", "右邊");
			    	//fragment.clai("右邊");
			    }
			    lastPage=arg0;
				
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
	}
	//=====================================================================================
	private void setlinte()
	{
		fualay.setOnPreviousViewListener(new OnPrViewListener() {
			@Override
			public void OnPrevious() {
				if(kr_tgs==false && !popupWindow_top.isShowing() && viewPager.getCurrentItem()>0){
					
					
				}
			}
			@Override
			public void OnNext() {
				if(kr_tgs==false && !popupWindow_top.isShowing()){
					
				}
			}
			@Override
			public void OnIntermediate() {
				if(kr_tgs==false && !popupWindow_top.isShowing())
				{
					OPamin();
				}
			}
			@Override
			public void OnDown(MotionEvent e) {
				disnissPopupWindow();
				if(popupWindow_top.isShowing()) {
					popupWindow_top.dismiss();
				}
			}
			@Override
			public void OnFlingL() {}
			@Override
			public void OnFlingR() {}
		});
	}
	/**PopupWindow 實作 內容物宣告*/
	private void newPopupWindow()
	{
		popupview_top = getLayoutInflater().inflate(R.layout.popupwindow1_top, null);
		//popupWindow_top = new PopupWindow(this);//popupWindow_top.setContentView(popupview_top);
		popupWindow_top = new PopupWindow(popupview_top, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//popupWindow_top.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
		popupWindow_top.setBackgroundDrawable(new ColorDrawable(0xC1000000));//ColorDrawable(0x00000000)
		popupWindow_top.setAnimationStyle(R.style.PopupAnimationTop);
		popupview_title = (TextView)popupview_top.findViewById(R.id.popupview_title);
		popupview_backimg = (ImageView)popupview_top.findViewById(R.id.popupview_backimg);
		
		RxView.clicks(popupview_backimg)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				finish();
			}
		});
		
		
		
		popupWindow_top.setOnDismissListener(new OnDismissListener() 
		{			
			@Override
			public void onDismiss() {
				
				if(popupWindow_bottom.isShowing())
				{
					popupWindow_bottom.dismiss();
				}
				if(popupWindow_L.isShowing())
				{
					popupWindow_L.dismiss();
				}
				if(popupview_R.isShown())
				{
					popupWindow_R.dismiss();
				}
			}
		});
		
		popupview_bottom = getLayoutInflater().inflate(R.layout.popupwindow1_bottom, null);
		popupWindow_bottom = new PopupWindow(popupview_bottom, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow_bottom.setAnimationStyle(R.style.PopupAnimation);
		popupWindow_bottom.setBackgroundDrawable(new ColorDrawable(0xC1000000));//ColorDrawable(0x00000000)
		
		Denominator = (TextView)popupview_bottom.findViewById(R.id.Denominator);
		Molecule = (TextView)popupview_bottom.findViewById(R.id.Molecule);
		popupview_bottom_img1 = (ImageView)popupview_bottom.findViewById(R.id.popupview_bottom_img1);
		popupview_bottom_img2 = (ImageView)popupview_bottom.findViewById(R.id.popupview_bottom_img2);
		seekd = (SeekBar)popupview_bottom.findViewById(R.id.seekd);
		seekd.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) //拖动进度条停止拖动的时候调用
			{
				//Log.w("拖动进度条停止拖动", "拖动进度条停止拖动");
				if (Shared.GetReadingMethod(ComicReading2Page.this)) {
					//--横向方式跳页
				} else{
					//--直向方式跳页
					//第一个参数代表你要滑动到哪个Item，第二个参数代表这个item距离屏幕顶部的距离
					layoutManager.scrollToPositionWithOffset(seekBar.getProgress(), 5);
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){} // 开始拖动的时候调用。
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) // 拖动进度条进度改变的时候调用
			{
				//Log.w("拖动进度条进度改变", "拖动进度条进度改变"+progress);
				
			}
			
		});
		
		RxView.clicks(popupview_bottom_img1)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				//-改直向阅读
				if (Shared.GetReadingMethod(ComicReading2Page.this)) {
					Shared.SetIReadingMethod(ComicReading2Page.this, false);
					
					
				}
			}
		});
		
		RxView.clicks(popupview_bottom_img2)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				//-改横向阅读
				if (!Shared.GetReadingMethod(ComicReading2Page.this)) {
					Shared.SetIReadingMethod(ComicReading2Page.this, true);
					
				}
			}
		});
		
		popupview_L = getLayoutInflater().inflate(R.layout.popupwindow1_left, null);
		popupWindow_L = new PopupWindow(popupview_L, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		popupWindow_L.setAnimationStyle(R.style.PopupAnimationLeft);
		
		//PopupWindow
		popupview_R = getLayoutInflater().inflate(R.layout.popupwindow1_right, null);
		popupWindow_R = new PopupWindow(popupview_R, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		popupWindow_R.setAnimationStyle(R.style.PopupAnimationRight);
		
		setPopupWindow_Status(popupWindow_R);
		setPopupWindow_Status(popupWindow_L);
		setPopupWindow_Status(popupWindow_top);
		setPopupWindow_Status(popupWindow_bottom);
	}
	/**
	 * 通用PopupWindow屬性
	 */
	private void setPopupWindow_Status(PopupWindow window)
	{
		if(window !=null)
		{
			window.setFocusable(false);
			window.setTouchable(true);
			window.setOutsideTouchable(false);
			//window.setBackgroundDrawable(new ColorDrawable(0xC1000000));//ColorDrawable(0x00000000)
			window.getContentView().setFocusableInTouchMode(true);
			window.getContentView().setFocusable(true);
		}
	}
	
	/**判定目前是否有PopupWindow*/
	private void disnissPopupWindow()
	{
		if(popupWindow_top.isShowing()) {
			kr_tgs = true;
		} else {
			kr_tgs = false;
		}
		Log.w("目前状态", kr_tgs+"");
	}
	/**取出DB 所有話數第一頁array資料*/
	public void GetDBArrayData(ArrayList<ItemComicIndex> alist) {
		this.alist = alist;
	}
	/**
	 * 頁數改動
	 * 將Seekd指向參數，觸發onProgressChanged
	 */
	private void setSeekd(int i)
	{
		seekd.setProgress(i);
	}

	
	public void OPamin() {
		popupWindow_bottom.showAtLocation(fualay, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
		popupWindow_top.showAtLocation(fualay, Gravity.TOP|Gravity.CENTER,0,0);
		popupWindow_L.showAtLocation(fualay, Gravity.LEFT|Gravity.CENTER, 0, -100);
		popupWindow_R.showAtLocation(fualay, Gravity.RIGHT|Gravity.CENTER, 0, -100);
	}
	
	private void fid() {
		viewPager = (CustomViewPager)findViewById(R.id.viewPager);
		viewPager.setPageMargin(5);//-間距
		
		/** 通过反射来修改 ViewPager的mScroller属性*/
		try {
		  Class clazz=Class.forName("android.support.v4.view.ViewPager");
		  Field f=clazz.getDeclaredField("mScroller");
		  FixedSpeedScroller fixedSpeedScroller=new FixedSpeedScroller(this,new LinearOutSlowInInterpolator());
		  fixedSpeedScroller.setmDuration(800);
		  f.setAccessible(true);
		  f.set(viewPager,fixedSpeedScroller);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
		
		wet = (FrameLayout)findViewById(R.id.wet);
		fualay = (Fualay)findViewById(R.id.fualay);
		recycler_view = (ZoomRecrycleView)findViewById(R.id.recycler_view);
		layoutManager = new LinearLayoutManager(this);
		recycler_view.setLayoutManager(layoutManager);
		//recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//--RecyclerView 間距 Android支持庫25.0.0版本引入了DividerItemDecoration 
		recycler_view.addOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				int a = layoutManager.findFirstVisibleItemPosition();
				int b = layoutManager.findFirstCompletelyVisibleItemPosition();
				Log.i("滚动状态", a+"__"+b);
				
				
			}
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});
		
		number1 = (TextView)findViewById(R.id.number1);
		number2 = (TextView)findViewById(R.id.number2);
		titlename = (TextView)findViewById(R.id.titlename);
		
		
	}
	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);//解除订阅
		presenter.destroy();
		super.onDestroy();
	}	
	/**啟用加載動畫*/
	public void opLoad() {
		wet.setVisibility(View.VISIBLE);
	}
	/**關閉家載動畫*/
	public void colLoad() {
		Log.i(Tag, "關閉");
		wet.setVisibility(View.GONE);
	}
	
	/**橫前置*/
	private void pupe() {
		try {
			recycler_view.setAdapter(null);
			recycler_view.setVisibility(View.GONE);
		} catch (Exception e) {}
		viewPager.setAlpha(0f);
		viewPager.setVisibility(View.VISIBLE);
		viewPager.animate()
        .alpha(1f)
        .setDuration(800);
		
	}
	
	/**直前置*/
	private void auae() {
		try {
			viewPager.setAdapter(null);
			viewPager.setVisibility(View.GONE);
		} catch (Exception e) {}
		recycler_view.setAlpha(0f);
		recycler_view.setVisibility(View.VISIBLE);
		recycler_view.animate()
        .alpha(1f)
        .setDuration(800);
	}
	
	
	
	
	
	
	
	/**
	 * 利用这个类来修正ViewPager的滑动速度
	 * 我们重写 startScroll方法，忽略传过来的 duration 属性
	 * 而是采用我们自己设置的时间
	 */
	public class FixedSpeedScroller extends Scroller {
	  public int mDuration=1500;
	  public FixedSpeedScroller(Context context) {
	    super(context);
	  }
	  public FixedSpeedScroller(Context context, Interpolator interpolator) {
	    super(context, interpolator);
	  }
	  public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
	    super(context, interpolator, flywheel);
	  }
	  @Override public void startScroll(int startX, int startY, int dx, int dy) {
	    startScroll(startX,startY,dx,dy,mDuration);
	  }
	  @Override public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	    //管你 ViewPager 传来什么时间，我完全不鸟你
	    super.startScroll(startX, startY, dx, dy, mDuration);
	  }
	  public int getmDuration() {
	    return mDuration;
	  }
	  public void setmDuration(int duration) {
	    mDuration = duration;
	  }
	}

}
