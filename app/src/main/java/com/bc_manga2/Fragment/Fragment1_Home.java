package com.bc_manga2.Fragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.bc_manga2.Activity.ComicDirectoryPage;
import com.bc_manga2.R;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.Presenter.Fragment1_HomePresenter;
import com.bc_manga2.Resolve.Home.ItemRotation;
import com.bc_manga2.View.maView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;

public class Fragment1_Home extends BaseFragment implements BaseFragment.DataCallback,maView,SwipeRefreshLayout.OnRefreshListener
{
	private Fragment1_HomePresenter presenter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private RecyclerView recycler_view;
	//private IwillPaint iwillPaint;


	public Fragment1_Home()
	{
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		presenter = new Fragment1_HomePresenter(this);
		EventBus.getDefault().register(this);//注册
		presenter.GetHtmlDate(Shared.GetHomeUrl(getView().getContext()), false);
		//iwillPaint = new IwillPaint(this.getContext(), "",1);
		
	}
	
	/**
	 * 放置資料
	 */
	@Override
	public void RecyclerViewsetData(Adapter<ViewHolder> adapter) {
		Log.i("有", "有");
		//recycler_view.setAdapter(null);
		try {
			recycler_view.setAdapter(adapter);	
		} catch (Exception e) {}	
		//adapder1.notifyDataSetChanged();
	}

	
	@Subscribe(threadMode = ThreadMode.POSTING)
	public void UpdateListEventBus(MessageEvent event) {
		if (event.getMessage().equals(TagInfo.ChangeHome)) {
			//iwillPaint.showdig();
			presenter.GetHtmlDate(Shared.GetHomeUrl(getView().getContext()), true);

			//EventBus.getDefault().cancelEventDelivery(event) ;  //取消事件传递
		}
	}
	
	
	@Override
	public void onRefresh() {
		//下拉更新執行
		new Handler().postDelayed(new Runnable()  {
			@Override
			public void run()  {
				presenter.GetHtmlDate(Shared.GetHomeUrl(getView().getContext()), true);
			}
		}, 1000);
	}
	
	@Override
	protected void findViewById(View view) {
		Log.i("有0", "有0");
		recycler_view = (RecyclerView)view.findViewById(R.id.recycler_view);
		recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
		mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.mSwipeRefreshLayout);
		
		//设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
		mSwipeRefreshLayout.setProgressViewOffset(true, 0, 200);
		//设置下拉圆圈的大小，两个值 LARGE， DEFAULT
		//mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
		// 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
		mSwipeRefreshLayout.setColorSchemeResources(
		    android.R.color.holo_blue_bright,
		    android.R.color.holo_green_light,
		    android.R.color.holo_orange_light,
		    android.R.color.holo_red_light);
		
		mSwipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	public void offRefresh() {
		mSwipeRefreshLayout.setRefreshing(false);
		//iwillPaint.dissdig();
	}
	@Override
	public void openRefresh() {
		mSwipeRefreshLayout.setRefreshing(true);
	}
	
	@Override
	public void onDestroy() {
		presenter.destroy();
		EventBus.getDefault().unregister(this);//反注册
		super.onDestroy();
	}
	@Override
	protected int getLayoutResource() {
		return R.layout.fragment1_home;
	}
	@Override
	public void ToComicDirectory(Object object) {
		
		ItemRotation itemRotation = (ItemRotation)object;
		Intent intent = new Intent(getActivity(),ComicDirectoryPage.class);
		intent.putExtra("SpecialType", itemRotation.isSpecialType());
		intent.putExtra("ImageUrl", itemRotation.getImage());
		intent.putExtra("Name", itemRotation.getName());
		intent.putExtra("Url", itemRotation.getUrl());
		//String HomePK = Shared.GetHomeUrlkey(getActivity());
		intent.putExtra("HomePK", itemRotation.getHomePK());//-主站
		startActivity(intent);
	}


}
