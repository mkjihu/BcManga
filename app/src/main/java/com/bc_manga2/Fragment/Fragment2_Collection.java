package com.bc_manga2.Fragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.bc_manga2.ComicDirectoryPage;
import com.bc_manga2.R;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.CollectionEvent;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.Model.EventBus.YueduEvent;
import com.bc_manga2.Presenter.Fragment2_CollectionPresenter;
import com.bc_manga2.View.maView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import de.greenrobot.BcComicdao.BcIndexData;

public class Fragment2_Collection extends BaseFragment implements BaseFragment.DataCallback,maView{

	private RecyclerView recycler_view;
	//private SwipeRefreshLayout mSwipeRefreshLayout;
	private Fragment2_CollectionPresenter presenter;
	protected boolean isVisible;

	//收藏
	public Fragment2_Collection() {}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		EventBus.getDefault().register(this);//注册
		presenter = new Fragment2_CollectionPresenter(this);
		Log.i("2_Coll", "ok0");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	//GetDBData();
	    	Log.i("2_Coll", "ok1");
	    }
	}
	
	/**取得DB資料*/
	public void GetDBData()
	{
		presenter.GetCollAdapder();
	}
	
	
	/**进入目录*/
	@Override
	public void ToComicDirectory(Object bcIndexData)
	{
		
		BcIndexData indexData = (BcIndexData)bcIndexData;
		
		Intent intent = new Intent(getActivity(),ComicDirectoryPage.class);
		intent.putExtra("SpecialType", false);
		intent.putExtra("ImageUrl", indexData.getImageUrl());
		intent.putExtra("Name", indexData.getTitleName());
		intent.putExtra("Url", indexData.getPKUrl());
		intent.putExtra("HomePK", indexData.getHomePK());
		startActivityForResult(intent, TagInfo.Colle);
	}
	
	/**
	 * threadMode 默认为ThreadMode.POSTING 指定当前订阅事件的执行线程

		ThreadMode.MainThread 在主线程执行
		ThreadMode.POSTING 和发布事件的线程保持一致
		ThreadMode.ASYNC 强行开启一个异步线程执行
		ThreadMode.BACKGROUND 如果发送事件是在子线程，则和发布事件的线程保持一致，如果发布事件在主线程，则新开线程执行
		sticky 默认为false 指定当前订阅者能否执行sticky事件。

		sticky事件是指EventBus发送事件的时候，订阅者并没有注册，但是当订阅者注册的时候，立马可以执行事件。
		当你希望你的事件不被马上执行，或者你发送事件的同时，并没有注册订阅者的时候可以使用。
	*/
	@Subscribe(threadMode = ThreadMode.POSTING)
	public void UpdateListEventBus(MessageEvent event) {
    	//EventBus.getDefault().registerSticky(subscriber);
		//Log.i("Colle", event.getMessage());
		if (event.getMessage().equals(TagInfo.ColleTage)) {
			GetDBData();
			EventBus.getDefault().cancelEventDelivery(event) ;  //取消事件传递
		}
    }
    
	
	
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==TagInfo.Colle) {
			GetDBData();
			EventBus.getDefault().post(new MessageEvent(TagInfo.YueduTage));
		}
	}

    @Override
    public void onDestroy() {
    	presenter.destroy();
    	EventBus.getDefault().unregister(this);//反注册
        super.onDestroy();
    }
    

	@Override /**放置資料*/
	public void RecyclerViewsetData(Adapter<ViewHolder> adapter) {
		try {
			recycler_view.setAdapter(adapter);	
		} catch (Exception e) {}	
	}
	@Override
	public void offRefresh() {
		//mSwipeRefreshLayout.setRefreshing(false);
	}
	@Override
	public void openRefresh() {
		//mSwipeRefreshLayout.setRefreshing(true);
	}
	@Override
	protected void findViewById(View view) {
		recycler_view = (RecyclerView)view.findViewById(R.id.recycler_view);
		recycler_view.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
		recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 3));//这里用线性宫格显示 类似于grid view
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.fragment2_sousuo;
	}


}
