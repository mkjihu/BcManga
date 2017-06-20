package com.bc_manga2;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.base.BackActivity.SwipeBackActivity;
import com.bc_manga2.Adapder.SortFineAdapder;
import com.bc_manga2.Presenter.SortFinePresenter;
import com.bc_manga2.Resolve.SortFine.SortFineItem;
import com.bc_manga2.View.maView;
import com.bc_manga2.obj.ToastUnity;
import com.jakewharton.rxbinding2.view.RxView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import io.reactivex.functions.Consumer;


public class SortFinePage extends SwipeBackActivity implements maView,SwipeBackActivity.DataCallback,XRefreshView.XRefreshViewListener {
	
	private RecyclerView recyclerView;
	private XRefreshView xRefreshView;
	private ImageView back_icon;
	private	TextView title0;
	
	private SortFinePresenter presenter;
	private String HomePK;//-主站識別
	private String ItemUrl;//網址
	private String SortName;//分類名稱
	
	private String NextUrl;
	public SortFineAdapder adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_page);
		presenter = new SortFinePresenter(this);
		fid();
		HomePK = getIntent().getExtras().getString("HomePK");
		ItemUrl = getIntent().getExtras().getString("ItemUrl");
		SortName = getIntent().getExtras().getString("SortName");
		title0.setText(SortName);
		presenter.GetSortFineArray1(HomePK, ItemUrl);
			
	}

	public void getNextUrl(String nextUrl) {
		NextUrl = nextUrl;
		Log.i("下一頁", NextUrl);
	}
	
	/**初次加載 */
	public void OneData(ArrayList<SortFineItem> fineItems) {
		adapter = new SortFineAdapder(this, fineItems);
		recyclerView.setAdapter(adapter);	
	}
	
	/**後續到底加載*/
	public void NextData(ArrayList<SortFineItem> fineItems) {
		if (recyclerView.getAdapter() !=null || recyclerView.getAdapter().getItemCount()>0) {
			adapter.setSortFineItem(fineItems);
		} else {
			adapter = new SortFineAdapder(this, fineItems);
			recyclerView.setAdapter(adapter);
		}
	}
	
	/**进入目录*/
	public void ToComicDirectory(SortFineItem sortFineItem) {
		Intent intent = new Intent(this,ComicDirectoryPage.class);
		intent.putExtra("SpecialType", sortFineItem.isSpecialType());
		intent.putExtra("ImageUrl", sortFineItem.getImage());
		intent.putExtra("Name", sortFineItem.getName());
		intent.putExtra("Url", sortFineItem.getUrl());
		intent.putExtra("HomePK", sortFineItem.getHomePK());//-主站
		startActivity(intent);
	};
	
	private void fid() {
		back_icon = (ImageView)findViewById(R.id.back_icon);
		title0 = (TextView)findViewById(R.id.title0);
		
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
		xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
		recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
		recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
		
        xRefreshView.setPinnedTime(700); //设置刷新完成以后，headerview固定的时间
		xRefreshView.setMoveForHorizontal(true);//手指横向移动的时候，让XRefreshView不拦截事件
		xRefreshView.setPullLoadEnable(true); // 设置是否可以上拉刷新 
		xRefreshView.setPullRefreshEnable(true); // 设置是否可以下拉刷新  
		xRefreshView.setAutoLoadMore(false);//滑动到底部自动加载更多
		xRefreshView.enableReleaseToLoadMore(true);//启用释放以加载更多
		//xRefreshView.setPinnedContent(true);//刷新时，不让里面的列表上下滑动
		//xRefreshView.enableRecyclerViewPullUp(true);
	    //xRefreshView.enablePullUpWhenLoadCompleted(true);//启用加载完成时启用	
		//xRefreshView.setSilenceLoadMore();// 设置静默加载模式
		xRefreshView.setCustomFooterView(new XRefreshViewFooter(this)); //如果没有使用库里的adapter，那么设置footerview的方法和其他view一样，都是用xRefreshView.setCustomFooterView(new XRefreshViewFooter(this))
		xRefreshView.setXRefreshViewListener(this);
		
		RxView.clicks(back_icon)
		.throttleFirst(1, TimeUnit.SECONDS)
		.subscribe(new Consumer<Object>(){
			@Override
			public void accept(Object arg0) throws Exception {
				onBackPressed();
			}
		});
	}
	@Override
	protected void onDestroy() {
		presenter.destroy();
		super.onDestroy();
	}
	
	
	@Override
	public void onRefresh(boolean isPullDown) {
		//下拉刷新
		presenter.GetSortFineArray1(HomePK, ItemUrl);
	}	
	@Override
	public void openRefresh() {
		try {
			xRefreshView.startRefresh();
		} catch (Exception e) { }
	}
	@Override
	public void offRefresh() {
		xRefreshView.stopRefresh();
	}

	
	@Override
	public void onLoadMore(boolean isSilence) {
		if (!NextUrl.equals("")) {
			presenter.GetSortFineArray2(HomePK, NextUrl, adapter.getSortFineItem());
		}else{
			ToastUnity.ShowTost(SortFinePage.this, "已無更多數據");
			xRefreshView.setLoadComplete(true);
		}
	}
	/**加載結束*/
	public void offLoadMore(boolean isok) {
		xRefreshView.stopLoadMore(isok);
		//xRefreshView.stopLoadMore();//当数据加载失败 不需要隐藏footerview时，可以调用以下方法，传入false，不传默认为true          
	}
	

	@Override
	public void onRelease(float direction) {}
	@Override
	public void onHeaderMove(double headerMovePercent, int offsetY) {}	
	@Override
	public void onRefresh() {}
	@Override
	public void RecyclerViewsetData(Adapter<ViewHolder> adapter) { }


}
