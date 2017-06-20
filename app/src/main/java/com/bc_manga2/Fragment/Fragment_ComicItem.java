package com.bc_manga2.Fragment;

import java.lang.reflect.Field;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.bc_manga2.R;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.Presenter.Fragment_ComicItemPresenter;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.bc_manga2.Ui.ViewPager.PhotoViewPager;
import com.bc_manga2.View.maView;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;

public class Fragment_ComicItem extends BaseFragment implements BaseFragment.DataCallback2,maView{
	
	private PhotoViewPager photoView;
	private Fragment_ComicItemPresenter presenter;
	private ItemComicIndex itemComicIndex;
    protected boolean isVisable;//是否可见
    public boolean isPrepared = false;  // 标志位，标志Fragment已经初始化完成。
    public boolean isReg = false;//是否可初次執行
    public String isStart ;//是否從起始頁開始顯示
    private boolean isScrolling_cok = true; //觸發到底開關
    
	public Fragment_ComicItem(ItemComicIndex itemComicIndex) {
		this.itemComicIndex = itemComicIndex;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		presenter = new Fragment_ComicItemPresenter(this);
		//EventBus.getDefault().register(this);//注册
		
	}
	
	/**
     * 实现Fragment数据的缓加载  
     * setUserVisibleHint是在onCreateView之前调用的
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser); 
        // 防止一开始加载的时候未 调用 p
        if (getUserVisibleHint()) {
            isVisable = true; 
            initVariables();
        } else {
            isVisable = false;
            //onInVisible();
        } 
    }

	
	
	@Override
	protected void initVariables() {
		
		photoView.addOnPageChangeListener(new OnPageChangeListener() {
			public boolean misScrolled;
			@Override
			public void onPageSelected(int arg0) {
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				 //SCROLL_STATE_IDLE: pager处于空闲状态 
				 //SCROLL_STATE_DRAGGING： pager处于正在拖拽中
				 //SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
		        switch (arg0) {  
		        case ViewPager.SCROLL_STATE_DRAGGING:  
		            misScrolled = false;  
		            break;  
		        case ViewPager.SCROLL_STATE_SETTLING:  
		            misScrolled = true;  
		            break;  
		        case ViewPager.SCROLL_STATE_IDLE:  
		            if (!misScrolled && isScrolling_cok) {
		            	if (photoView.getCurrentItem() == photoView.getAdapter().getCount() - 1 ) {
		            		EventBus.getDefault().post(new MessageEvent("left"));//Log.i("滑動到底", "左滑到底");
							isScrolling_cok=false;
						}
		            	if (photoView.getCurrentItem() == 0 ) {
		            		EventBus.getDefault().post(new MessageEvent("right"));//Log.i("滑動到底", "右滑到底");
							isScrolling_cok=false;
						}
		            }  
		            misScrolled = true;  
		            break;  
		        }
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			
		});
		
	}
	
	public void setStart(String isStart) {
		this.isStart = isStart;
		Log.i("-", isStart+"");
		
		if (isVisable) {
			switch (isStart) {
			case "Start":
				photoView.setCurrentItem(photoView.getAdapter().getCount()-1, false);
				break;
			case "End":
				photoView.setCurrentItem(0, false);
				break;
			}
		}
		isScrolling_cok = true;
	}
	
	
	
	
	
	@Override
	public void onDestroy() {
		presenter.destroy();
		//EventBus.getDefault().unregister(this);//反注册
		super.onDestroy();
	}

	@Override
	protected void findViewById(View view) {
		photoView = (PhotoViewPager)view.findViewById(R.id.photoView);
		  /**通过反射修改ViewPager最小滑动距离mTouchSlop*/
        try {
        	Field field = ViewPager.class.getDeclaredField("mTouchSlop"); // 通过ViewPager类得到字段，不能通过实例得到字段。  
            field.setAccessible(true); // 设置Java不检查权限。  
            field.setInt(photoView, 30); // 设置字段的值，此处应该使用ViewPager实例。设置只有滑动长度大于150px的时候，ViewPager才进行滑动  
		} catch (Exception e) { }
        
        
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.fragment_comicitem;
	}

	@Override
	public void ToComicDirectory(Object object) {}

	@Override
	public void PagerAdapterData(PagerAdapter adapter) {
		
	}

	@Override
	public void openRefresh() {
		
	}
	@Override
	public void offRefresh() {
		
	}
}
