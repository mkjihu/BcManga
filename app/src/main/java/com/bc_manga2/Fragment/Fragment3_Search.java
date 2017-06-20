package com.bc_manga2.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.bc_manga2.R;
import com.bc_manga2.Activity.SortFinePage;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.Presenter.Fragment3_SearchPresenter;
import com.bc_manga2.Resolve.Sort.ItemSort;
import com.bc_manga2.View.maView;
import com.bc_manga2.obj.IwillPaint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public class Fragment3_Search extends BaseFragment implements BaseFragment.DataCallback,maView
{
	public Fragment3_SearchPresenter presenter;
	private RecyclerView recycler_view;
	private IwillPaint iwillPaint;
	private boolean isVisible = true;//是否初次加載
	//搜索&分類
	public Fragment3_Search() {}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		EventBus.getDefault().register(this);//注册
		presenter = new Fragment3_SearchPresenter(this);
		iwillPaint = new IwillPaint(this.getContext(), "",1);
		
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser && isVisible) {
	    	isVisible = false;
	    	GetDate();
	    }
	}
	
	private void GetDate() {
		presenter.GetSortAdapder();//Log.i("開始Search", "開始Search");	
    	//presenter.GetSortAdapder("http://comic.ck101.com/");
	}
	
	@Subscribe(threadMode = ThreadMode.POSTING)
	public void UpdateListEventBus(MessageEvent event) {
		if (event.getMessage().equals(TagInfo.ChangeHome)) {
			presenter.GetSortAdapder();//Log.i("開始Search", "開始Search");	
			
			//EventBus.getDefault().cancelEventDelivery(event) ;  //取消事件传递
		}
	}
	
	
	
	@Override
	public void ToComicDirectory(Object object) {
		ItemSort itemSort = (ItemSort)object;
		Intent intent = new Intent(getActivity(), SortFinePage.class);
		intent.putExtra("HomePK", itemSort.getHomePK());//-主站識別
		intent.putExtra("ItemUrl", itemSort.getItemUrl());//網址--預設為空
		intent.putExtra("SortName", itemSort.getItemName());//分類名稱
		startActivity(intent);
	}
	
	@Override
	public void RecyclerViewsetData(Adapter<ViewHolder> adapter) {
		recycler_view.setAdapter(adapter);
	}
	
	@Override
	protected void findViewById(View view) {
		recycler_view = (RecyclerView)view.findViewById(R.id.recycler_view);
		recycler_view.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
		recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 3));//这里用线性宫格显示 类似于grid view
	}
	
	@Override
	public void onDestroy() {
		presenter.destroy();
		EventBus.getDefault().unregister(this);//反注册
		super.onDestroy();
	}
	
	@Override
	protected int getLayoutResource() {
		return R.layout.fragment3_search;
	}
	@Override
	public void offRefresh() {
		iwillPaint.dissdig();
	}

	@Override
	public void openRefresh() {
		iwillPaint.showdig();
	}

}
