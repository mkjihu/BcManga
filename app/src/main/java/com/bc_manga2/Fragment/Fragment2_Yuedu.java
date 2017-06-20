package com.bc_manga2.Fragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.bc_manga2.Activity.ComicDirectoryPage;
import com.bc_manga2.R;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.Presenter.Fragment2_YueduPresenter;
import com.bc_manga2.View.maView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import de.greenrobot.BcComicdao.BcIndexData;

public class Fragment2_Yuedu extends BaseFragment implements BaseFragment.DataCallback,maView{

	private RecyclerView recycler_view;
	//private SwipeRefreshLayout mSwipeRefreshLayout;
	private Fragment2_YueduPresenter presenter;


	//閱讀紀錄
	public Fragment2_Yuedu() {}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		presenter = new Fragment2_YueduPresenter(this);
		EventBus.getDefault().register(this);//注册
		//GetDBData();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	//GetDBData();
	    }
	}
    
	@Subscribe(threadMode = ThreadMode.POSTING)
	public void UpdateListEventBus(MessageEvent event) {
    	//EventBus.getDefault().registerSticky(subscriber);
        //Log.e("Yuedu", event.getMessage());
		if (event.getMessage().equals(TagInfo.YueduTage)) {
			GetDBData();
			EventBus.getDefault().cancelEventDelivery(event) ;  //取消事件传递
		}
    }
	
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==TagInfo.Yuedu) {
			GetDBData();
			EventBus.getDefault().post(new MessageEvent(TagInfo.ColleTage));
		}
	}	
	
	/**取得DB資料*/
	public void GetDBData() {
		presenter.GetYueduAdapder();
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
		startActivityForResult(intent, TagInfo.Yuedu);
	}
	
	


	@Override
    public void onDestroy() {
    	presenter.destroy();
    	EventBus.getDefault().unregister(this);//反注册
        super.onDestroy();
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
}
