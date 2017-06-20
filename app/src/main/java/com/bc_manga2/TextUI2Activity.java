package com.bc_manga2;

import java.util.ArrayList;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextUI2Activity extends AppCompatActivity implements XRefreshView.XRefreshViewListener{

	public RecyclerView recyclerView;
	public XRefreshView xRefreshView;
	private int mLoadCount = 0;
	
	public ArrayList<String> Indexs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_ui2);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
		xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
		recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
		
		recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
		
		Indexs = new ArrayList<>();
		for (int i = 0; i < 35; i++) {
			Indexs.add(i+"");
		}
		recyclerView.setAdapter(new NormalRecyclerAdapter(Indexs));
		
        xRefreshView.setPinnedTime(1000); //设置刷新完成以后，headerview固定的时间
		xRefreshView.setMoveForHorizontal(true);//手指横向移动的时候，让XRefreshView不拦截事件
		xRefreshView.setPullLoadEnable(true); // 设置是否可以上拉刷新 
		xRefreshView.setPullRefreshEnable(true); // 设置是否可以下拉刷新  
		xRefreshView.setAutoLoadMore(false);//滑动到底部自动加载更多
		xRefreshView.enableReleaseToLoadMore(true);//启用释放以加载更多
		xRefreshView.setPinnedContent(true);//刷新时，不让里面的列表上下滑动
		
		//xRefreshView.setSilenceLoadMore();// 设置静默加载模式
		
		//xRefreshView.enableRecyclerViewPullUp(true);
	    //xRefreshView.enablePullUpWhenLoadCompleted(true);//启用加载完成时启用
		
		xRefreshView.setXRefreshViewListener(this);
		//xRefreshView.startRefresh();
		
		//xRefreshView.setCustomFooterView(footerView);
		xRefreshView.setCustomFooterView(new XRefreshViewFooter(this)); 
		
	}
	
	
	public class NormalRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>
	{
		private LayoutInflater mLayoutInflater;
		public ArrayList<String> Indexs;
		public NormalRecyclerAdapter(ArrayList<String> Indexs) {
			mLayoutInflater = LayoutInflater.from(TextUI2Activity.this);
			this.Indexs =Indexs;
		}

		@Override
		public int getItemCount() {
			return Indexs.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder arg0, int arg1) {
			((LayoutView)arg0).settserr(Indexs.get(arg1));
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
			//產生View
			return new LayoutView(mLayoutInflater.inflate(R.layout.item_sort_gidview_item, arg0, false));
		}
		
		public class LayoutView extends ViewHolder
		{

			public CardView item_sort_card;
			public TextView sort_itemtv1,sort_itemtv2;
			
			public LayoutView(View itemView) {
				super(itemView);
				item_sort_card = (CardView)itemView.findViewById(R.id.item_sort_card);
				sort_itemtv1 = (TextView)itemView.findViewById(R.id.sort_itemtv1);
				sort_itemtv2 = (TextView)itemView.findViewById(R.id.sort_itemtv2);
				
				
			}
			public void settserr(String string) {
				sort_itemtv1.setText(string);
				sort_itemtv2.setText(string);
			}
			
		}
		
		public ArrayList<String> getIndexs() {
			return Indexs;
		}

		public void setIndexs(ArrayList<String> indexs) {
			Indexs = indexs;
			notifyDataSetChanged();
		}

	}

	@Override
	public void onRefresh() { }
	@Override
	public void onRefresh(boolean isPullDown) {
		
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                xRefreshView.stopRefresh();
            }
        }, 2000);
	}
	@Override
	public void onLoadMore(boolean isSilence) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	// 刷新完成必须调用此方法停止加载
            	//xRefreshView.stopLoadMore();//当数据加载失败 不需要隐藏footerview时，可以调用以下方法，传入false，不传默认为true
                 //xRefreshView.stopLoadMore(false);
                xRefreshView.setLoadComplete(true);
                // xRefreshView.setLoadComplete(true); //没有新数据的时候，隐藏footerview
            }
        }, 2000);
	}
	@Override
	public void onRelease(float direction) {}
	@Override
	public void onHeaderMove(double headerMovePercent, int offsetY) {}

	
}
