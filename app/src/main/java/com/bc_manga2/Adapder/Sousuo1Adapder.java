package com.bc_manga2.Adapder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.R;
import com.bc_manga2.Fragment.BaseFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.view.RxView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.greenrobot.BcComicdao.BcIndexData;
import io.reactivex.functions.Consumer;

/**觀看紀錄*/
public class Sousuo1Adapder extends RecyclerView.Adapter<ViewHolder> {

	private List<BcIndexData> bcIndexDatas; //分類集合版
	private BaseFragment fragment;
	private LayoutInflater mLayoutInflater;
	
	public Sousuo1Adapder(BaseFragment fragment,List<BcIndexData> bcIndexDatas) {
		this.fragment = fragment;
		this.bcIndexDatas = bcIndexDatas;
		mLayoutInflater = LayoutInflater.from(fragment.getContext());
	}
	
	@Override
	public int getItemCount() {
		return bcIndexDatas.size();
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new GridLayoutView(mLayoutInflater.inflate(R.layout.item_sousuo_item, arg0, false));
	}
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		((GridLayoutView)holder).SetGridDate(position);
	}

	
	public class GridLayoutView extends ViewHolder
	{
		public TextView tv1;
		public SimpleDraweeView draweeView;
		
		public GridLayoutView(View itemView) {
			super(itemView);
			tv1 = (TextView)itemView.findViewById(R.id.textView1);
			draweeView = (SimpleDraweeView)itemView.findViewById(R.id.imageg1);
		}
		
		public void SetGridDate(int position) {
			tv1.setText(bcIndexDatas.get(position).getTitleName());
			draweeView.setImageURI(bcIndexDatas.get(position).getImageUrl());
			
			RxView.clicks(draweeView)
        		.throttleFirst(1, TimeUnit.SECONDS)
        		.subscribe(new Actiaon1(bcIndexDatas.get(position)));
        		
			//draweeView.setOnClickListener(new Actiaon2(bcIndexDatas.get(position)));
		}
	}
	/*
	public class Actiaon2 implements OnClickListener
	{
		public BcIndexData bcIndexData;
		public Actiaon2(BcIndexData bcIndexData) {
			this.bcIndexData = bcIndexData;
		}
		@Override
		public void onClick(View v) { 
			fragment.ToComicDirectory(bcIndexData);
		} 
	}
	*/
	
	public class Actiaon1 implements Consumer<Object>
	{
		public BcIndexData bcIndexData;
		public Actiaon1(BcIndexData bcIndexData) {
			this.bcIndexData = bcIndexData;
		}
		@Override
		public void accept(Object arg0) throws Exception {
			fragment.ToComicDirectory(bcIndexData);
		}
	}
}
