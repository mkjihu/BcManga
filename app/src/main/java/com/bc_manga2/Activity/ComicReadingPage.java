package com.bc_manga2.Activity;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import com.bc_manga2.Adapder.BaseReadingAdapder;
import com.bc_manga2.Adapder.BaseReadingAdapder2;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Presenter.ComicReadingPresenter;
import com.bc_manga2.R;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.bc_manga2.Ui.Fualay;
import com.bc_manga2.Ui.Fualay.OnPrViewListener;
import com.bc_manga2.Ui.ViewPager.ViewPager;
import com.bc_manga2.Ui.ViewPager.ViewPager2;
import com.bc_manga2.Ui.ViewPager.ViewPager2.OnPageChangeListener;
import com.bc_manga2.View.maView;
import com.jakewharton.rxbinding2.view.RxView;

import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

public class ComicReadingPage extends AppCompatActivity implements maView
{
	private final String Tag = "ComicReadingPage";
	
	private ViewPager2 viewPager;
	private Fualay fualay;
	private ComicReadingPresenter presenter;
	private FrameLayout wet;
	//private TextView loadtext0,loadtext1;
	//------------上下左右-----------
	private PopupWindow popupWindow_top,popupWindow_bottom,popupWindow_R,popupWindow_L;
	private View popupview_top,popupview_bottom,popupview_R,popupview_L;
	private SeekBar seekd;
	private boolean kr_tgs =false;//狀態         true：目前上下打開      false：目前上下關閉
	
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
	private ArrayList<String> ImageUrls;//-當前話數圖片列表
	
	//---預計之後+
	private RecyclerView recycler_view;
	private LinearLayoutManager layoutManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		setContentView(R.layout.activity_comic_reading_page);
		fid();
		presenter = new ComicReadingPresenter(this);
		
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
	
	//真正的visible时间点是onWindowFocusChanged()函数被执行时
	@Override 
	public void onWindowFocusChanged(boolean hasFocus) { 
	    super.onWindowFocusChanged(hasFocus); 
	    //Log.i("時間點", hasFocus+"");
	    /*
	    if (hasFocus) {
	    	//預設動畫動做
			OPamin();
		}
		*/
	}
	
	/**取得當前是第幾話的第一頁*/
	public void GetDBtarget(int target) {
		this.target = target;
		popupview_title.setText(TitleName);
		titlename.setText(alist.get(target).getItemName());
		
		//--是否橫向加載
		if (Shared.GetReadingMethod(this)) {
			presenter.GetImageList(HomePK,alist.get(target),false);
		}else{
			presenter.GetImageList2(HomePK,alist.get(target),false);
		}
	}
	
	/**載入下一話或上一話*/
	public void DBNextorLast(int target) {
		this.target = target;
		popupview_title.setText(TitleName);
		titlename.setText(alist.get(target).getItemName());
	}
	
	
	/**載入橫向Adapder*/
	public void SetViewPageData(BaseReadingAdapder adapter) {
		try {
			recycler_view.setAdapter(null);
			recycler_view.setVisibility(View.GONE);
		} catch (Exception e) {}
		viewPager.setAlpha(0f);
		viewPager.setVisibility(View.VISIBLE);
		viewPager.animate()
        .alpha(1f)
        .setDuration(800);
		//Log.i(Tag, adapter.GetImageUrls().size()+"");
		this.ImageUrls = adapter.GetImageUrls();
		
		seekd.setMax(adapter.getCount()-1);
		
		Denominator.setText(adapter.getCount()+"");//--最大數
		//Molecule.setText(viewPager.getCurrentItem()+1+"");
		
		//number1.setText(viewPager.getCurrentItem()+1+"");
		number2.setText(adapter.getCount()+"");
		viewPager.setAdapter(adapter);
	}
	/**載入直向Adapder*/
	public void SetRecyclerViewData(BaseReadingAdapder2 adapter) {

		try {
			viewPager.setAdapter(null);
			viewPager.setVisibility(View.GONE);
		} catch (Exception e) {}
		recycler_view.setAlpha(0f);
		recycler_view.setVisibility(View.VISIBLE);
		recycler_view.animate()
        .alpha(1f)
        .setDuration(800);
		//Log.i(Tag, adapter.GetImageUrls().size()+"");
		this.ImageUrls = adapter.GetImageUrls();
		
		seekd.setMax(adapter.getItemCount()-1);
		
		Denominator.setText(adapter.getItemCount()+"");//--最大數
		//Molecule.setText(viewPager.getCurrentItem()+1+"");
		
		//number1.setText(viewPager.getCurrentItem()+1+"");
		number2.setText(adapter.getItemCount()+"");
		
		recycler_view.setAdapter(adapter);
		
		
	}
	
	/**橫向下一 或上一 */
	public void NextorLast(BaseReadingAdapder adapter,String NoL)
	{
		viewPager.setAlpha(0f);
		seekd.setMax(adapter.getCount()-1);
		Denominator.setText(adapter.getCount()+"");//--最大數
		number2.setText(adapter.getCount()+"");
		viewPager.setAdapter(adapter);
		switch (NoL) {
		case "N": //-下1
			viewPager.setCurrentItem(0, false);
			setSeekd(0);
			try {setMolecule(1);} catch (Exception e) {}
			break;
		case "L"://-上1
			viewPager.setCurrentItem(adapter.getCount()-1, false);
			setSeekd(adapter.getCount()-1);
			try {setMolecule(adapter.getCount());} catch (Exception e) {}
			break;
		}
		viewPager.animate()
        .alpha(1f)
        .setDuration(800);
	}
	/**直向下一 或上一 */
	public void NextorLast(BaseReadingAdapder2 adapter,String NoL)
	{
		recycler_view.setAlpha(0f);
		seekd.setMax(adapter.getItemCount()-1);
		Denominator.setText(adapter.getItemCount()+"");//--最大數
		recycler_view.setAdapter(adapter);
		switch (NoL) {
		case "N": //-下1
			//viewPager.setCurrentItem(0, false);
			setSeekd(0);
			try {setMolecule(1);} catch (Exception e) {}
			break;
		case "L"://-上1
			//viewPager.setCurrentItem(adapter.getCount()-1, false);
			recycler_view.scrollToPosition(adapter.getItemCount() - 1);
			setSeekd(adapter.getItemCount()-1);
			try {setMolecule(adapter.getItemCount());} catch (Exception e) {}
			break;
		}
		recycler_view.animate()
        .alpha(1f)
        .setDuration(800);
	}
	
	
	@Override
	protected void onDestroy() {
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
	/**
	 * viewpage 監聽
	 */
	public boolean misScrolled;
	private void ViewPageListener()
	{
		viewPager.setOnPageChangeListener(new OnPageChangeListener() 
		{
			@Override
			public void onPageSelected(int arg0) 
			{
				/**頁數改動  將Seekd指向參數，觸發onProgressChanged */
				setSeekd(arg0);
				try {
					setMolecule(arg0+1);
				} catch (Exception e) {}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int state) {//arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做
				/**
				 * SCROLL_STATE_IDLE: pager处于空闲状态 
				 * SCROLL_STATE_DRAGGING： pager处于正在拖拽中
				 * SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
				 */
		        switch (state) {  
		        case ViewPager.SCROLL_STATE_DRAGGING:  
		            misScrolled = false;  
		            break;  
		        case ViewPager.SCROLL_STATE_SETTLING:  
		            misScrolled = true;  
		            break;  
		        case ViewPager.SCROLL_STATE_IDLE:  
		            if (!misScrolled) {
		            	if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 ) {
							//Log.i("滑動到底", "左滑到底");
							presenter.NextorLast(HomePK, alist, target, "N");
						}
		            	if (viewPager.getCurrentItem() == 0 ) {
							//Log.i("滑動到底", "右滑到底");
		            		presenter.NextorLast(HomePK, alist, target, "L");
						}
		            }  
		            misScrolled = true;  
		            break;  
		        } 
			}
		});
	}
	//=====================================================================================
	private void setlinte()
	{
		fualay.setOnPreviousViewListener(new OnPrViewListener() {
			@Override
			public void OnPrevious() {
				if(kr_tgs==false && !popupWindow_top.isShowing() && viewPager.getCurrentItem()>0){
					if(Shared.GetReadingMethod(ComicReadingPage.this)) {
						viewPager.setCurrentItem(viewPager.getCurrentItem()-1, false);
					}
						
				}
			}
			@Override
			public void OnNext() {
				Log.w("",viewPager.getCurrentItem()+"");
				if(kr_tgs==false && !popupWindow_top.isShowing()){
					if(Shared.GetReadingMethod(ComicReadingPage.this)) {
						viewPager.setCurrentItem(viewPager.getCurrentItem()+1, false);
					}
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
				if (Shared.GetReadingMethod(ComicReadingPage.this)) {
					//--横向方式跳页
					viewPager.setCurrentItem(seekBar.getProgress(),false);
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
				setMolecule(progress+1);
			}
			
		});
		
		RxView.clicks(popupview_bottom_img1)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				//-改直向阅读
				if (Shared.GetReadingMethod(ComicReadingPage.this)) {
					Shared.SetIReadingMethod(ComicReadingPage.this, false);
					GetDBtarget(target);//presenter.GetImageList2(HomePK,alist.get(target),false);
				}
			}
		});
		
		RxView.clicks(popupview_bottom_img2)
    	.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				//-改横向阅读
				if (!Shared.GetReadingMethod(ComicReadingPage.this)) {
					Shared.SetIReadingMethod(ComicReadingPage.this, true);
					GetDBtarget(target);//presenter.GetImageList(HomePK,alist.get(target),false);
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
	/** 頁數改動 將Molecule指向參數
	 */
	private void setMolecule(int i)
	{
		Molecule.setText(i+"");
		number1.setText(i+"");
	}
	
	public void OPamin() {
		popupWindow_bottom.showAtLocation(fualay, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
		popupWindow_top.showAtLocation(fualay, Gravity.TOP|Gravity.CENTER,0,0);
		popupWindow_L.showAtLocation(fualay, Gravity.LEFT|Gravity.CENTER, 0, -100);
		popupWindow_R.showAtLocation(fualay, Gravity.RIGHT|Gravity.CENTER, 0, -100);
	}
	
	private void fid() {
		viewPager = (ViewPager2)findViewById(R.id.viewPager);
		fualay = (Fualay)findViewById(R.id.fualay);
		wet = (FrameLayout)findViewById(R.id.wet);
		//loadtext0 = (TextView)findViewById(R.id.loadtext0);
		//loadtext1 = (TextView)findViewById(R.id.loadtext1);
		//viewPager.setOffscreenPageLimit(8);//懶加載
		recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
		layoutManager = new LinearLayoutManager(this);
		recycler_view.setLayoutManager(layoutManager);
		//recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//--RecyclerView 間距 Android支持庫25.0.0版本引入了DividerItemDecoration 
		
		recycler_view.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				// TODO Auto-generated method stub
				super.onScrollStateChanged(recyclerView, newState);
				
				int a = layoutManager.findFirstVisibleItemPosition();
				int b = layoutManager.findFirstCompletelyVisibleItemPosition();
				Log.i("滚动状态", a+"__"+b);
				try {
					if (b==0 || b==-1) {
						setSeekd(a+1);
						setMolecule(a+1);
					}else{
						setSeekd(b+1);
						setMolecule(b+1);
					}
				} catch (Exception e) {}
			}
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});
		
		number1 = (TextView)findViewById(R.id.number1);
		number2 = (TextView)findViewById(R.id.number2);
		titlename = (TextView)findViewById(R.id.titlename);
		
		viewPager.setPageMargin(8);//-間距
	}
	
}
