package com.bc_manga2.Adapder;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.bc_manga2.R;
import com.bc_manga2.Fragment.BaseFragment;
import com.bc_manga2.Resolve.Home.ItemCriterion;
import com.bc_manga2.Resolve.Home.ItemRotation;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.view.RxView;
import android.net.Uri;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lightsky.infiniteindicator.IndicatorConfiguration;
import cn.lightsky.infiniteindicator.InfiniteIndicator;
import cn.lightsky.infiniteindicator.OnPageClickListener;
import cn.lightsky.infiniteindicator.Page;
import cn.lightsky.infiniteindicator.indicator.CircleIndicator;
import io.reactivex.functions.Consumer;

public class HomeAdapder extends RecyclerView.Adapter<ViewHolder>
{
	
	private ArrayList<ItemRotation> itemRotations; //輪播圖片列表
	private ArrayList<ItemCriterion> criterions; //分類集合版
	private BaseFragment fragment;
	private LayoutInflater mLayoutInflater;
	
	
	
	public HomeAdapder(BaseFragment fragment,ArrayList<ItemRotation> itemRotations, ArrayList<ItemCriterion> criterions) {
		this.fragment = fragment;
		this.itemRotations = itemRotations;
		this.criterions = criterions;
		mLayoutInflater = LayoutInflater.from(fragment.getActivity());
	}
	
	@Override
	public int getItemCount() {
		return itemRotations.size()>0 ? criterions.size()+1:criterions.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		if(itemRotations.size() >0 && position ==0) {//如果輪播區圖片大於一張
			return 0;
		} else  {
			return 1;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case 0:
			return new PageLayoutView(mLayoutInflater.inflate(R.layout.item_home_page, parent, false));
		case 1:
			return new GridLayoutView(mLayoutInflater.inflate(R.layout.item_home_gidview, parent, false));
		default:
			return null;
		}
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)//放入資料
	{
		 if (holder instanceof PageLayoutView) {
	        ((PageLayoutView)holder).SetPageDate();
	     }
		 else if (holder instanceof GridLayoutView) {
			if (itemRotations.size()>0) {
				((GridLayoutView)holder).SetGridDate(position-1);
			} else {
				((GridLayoutView)holder).SetGridDate(position);
			}
		 }
	}


	public class PageLayoutView extends ViewHolder implements OnPageChangeListener,OnPageClickListener
	{
		private InfiniteIndicator indicatorDefaultCircle;
		private ArrayList<Page> pageViews;
		private TextView item_home_textView1;
		public PageLayoutView(View itemView) {
			super(itemView);
			item_home_textView1 = (TextView)itemView.findViewById(R.id.item_home_textView1);
			indicatorDefaultCircle = (InfiniteIndicator)itemView.findViewById(R.id.indicator_default_circle);
		    IndicatorConfiguration configuration = new IndicatorConfiguration.Builder()
	                .imageLoader(new GlideLoader())
	                .isStopWhileTouch(true)
	                .onPageChangeListener(this)
	                .onPageClickListener(this)
	                .direction(IndicatorConfiguration.LEFT)
	                .position(IndicatorConfiguration.IndicatorPosition.Right_Bottom)
	                .internal(5000)
	                .build();
		    indicatorDefaultCircle.init(configuration);
		    
		    CircleIndicator circleIndicator = ((CircleIndicator) indicatorDefaultCircle.getPagerIndicator());
		    //final float density = activity.getResources().getDisplayMetrics().density;
		    //circleIndicator.setPageColor(0x667B7B7B);
	        circleIndicator.setFillColor(0xFFFFFFFF);
	        circleIndicator.setStrokeColor(0xFFFFFFFF);
	        //circleIndicator.setStrokeWidth(0f * density);
		}
		
		public void SetPageDate() {
			pageViews = new ArrayList<>();
			for (int i = 0; i < itemRotations.size(); i++) {
				pageViews.add(new Page(itemRotations.get(i).getImage()));
			}
			indicatorDefaultCircle.notifyDataChange(pageViews);
		}

		@Override
		public void onPageClick(int position, Page page) {
			Log.i("Page點擊", itemRotations.get(position).getUrl());
			ToComicDirectory(itemRotations.get(position));
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageSelected(int arg0) {
			item_home_textView1.setText(itemRotations.get(arg0).getName());
		}
		
		
	}
	
	public Integer[] icons = {R.drawable.u0,R.drawable.u1,R.drawable.u2,R.drawable.u3,R.drawable.u4
			,R.drawable.u5,R.drawable.u6,R.drawable.u7,R.drawable.u8,R.drawable.u9};
	
	public class GridLayoutView extends ViewHolder
	{
		public TextView GidTitle;
		public GridLayout choice_grid;
		public ImageView giicon;
		public GridLayoutView(View itemView) {
			super(itemView);
			giicon = (ImageView)itemView.findViewById(R.id.giicon);
			GidTitle = (TextView)itemView.findViewById(R.id.GidTitle);
			choice_grid = (GridLayout)itemView.findViewById(R.id.choice_grid);
		}
		
		public void SetGridDate(int position) {
			GidTitle.setText(criterions.get(position).getCritName());
			int si = new Random().nextInt(10);
			Glide.with(fragment.getActivity()).load(icons[si]).into(giicon);
			ArrayList<ItemRotation> isu = criterions.get(position).getItemRotations();
			for (int i = 0; i < isu.size(); i++) {
				View v = mLayoutInflater.inflate(R.layout.item_home_gidview_item, choice_grid, false);
				
				TextView  tv1 = (TextView)v.findViewById(R.id.textView1);
				tv1.setText(isu.get(i).getName());
				tv1.setWidth(37);
				
				TextView  tv2 = (TextView)v.findViewById(R.id.textView2);
				TextView  tv3 = (TextView)v.findViewById(R.id.textView3);
				
				Uri uri = Uri.parse(isu.get(i).getImage());
				SimpleDraweeView draweeView = (SimpleDraweeView)v.findViewById(R.id.imageg1);
				draweeView.setImageURI(uri);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
	        			, GridLayout.spec(GridLayout.UNDEFINED, 1, 1f));
//				params.height = 200;
	        	params.setGravity(Gravity.FILL);        	
//	        	//v.setBackgroundColor(Color.BLUE);
//	        	//v.setGravity(Gravity.CENTER);
	        	v.setLayoutParams(params);	    
	    		RxView.clicks(v)
	        		.throttleFirst(1, TimeUnit.SECONDS)
	        		.subscribe(new Actiaon1(isu.get(i)));
	        	choice_grid.addView(v);
			}

			
		}
	}
	/*
	public class Actiaon2 implements OnClickListener
	{
		public ItemRotation itemRotation;
		public Actiaon2(ItemRotation itemRotation) {
			this.itemRotation = itemRotation;
		}
		@Override
		public void onClick(View v) {
			ToComicDirectory(itemRotation);
		} 
	}
	*/
	
	public class Actiaon1 implements Consumer<Object>
	{
		public ItemRotation itemRotation;
		public Actiaon1(ItemRotation itemRotation) {
			this.itemRotation = itemRotation;
		}
		@Override
		public void accept(Object arg0) throws Exception {
			ToComicDirectory(itemRotation);
		}
	}
	
	
	public void ToComicDirectory(ItemRotation itemRotation)
	{
		fragment.ToComicDirectory(itemRotation);
	}
	
	/*
    Glide.with(context)
       .load((String) res)
       .centerCrop()//中心切圖, 會填滿
       //.fitCenter()//中心fit, 以原本圖片的長寬為主
       .error(R.mipmap.ikgbi)//load失敗的Drawable
       .placeholder(R.mipmap.ikgbi)//loading時候的Drawable
       .crossFade()//交叉淡入淡出
       .transform(transformations)
       .into(targetView);
	 */	
	
	
}
