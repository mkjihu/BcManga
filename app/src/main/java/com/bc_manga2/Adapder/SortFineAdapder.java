package com.bc_manga2.Adapder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.R;
import com.bc_manga2.SortFinePage;
import com.bc_manga2.Resolve.SortFine.SortFineItem;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.view.RxView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

public class SortFineAdapder extends RecyclerView.Adapter<ViewHolder> {

	private ArrayList<SortFineItem> sortFineItems;
	private LayoutInflater mLayoutInflater;
	private SortFinePage activity;
	
	public SortFineAdapder(SortFinePage activity,ArrayList<SortFineItem> SortFineItems) {
		this.sortFineItems = SortFineItems;
		this.activity =activity;
		mLayoutInflater = LayoutInflater.from(activity);
	}
	
	@Override
	public int getItemCount() {
		return sortFineItems.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		((ItemLayoutView)arg0).SetGridDate(arg1);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new ItemLayoutView(mLayoutInflater.inflate(R.layout.item_sort_fine, arg0, false));
	}

	public ArrayList<SortFineItem> getSortFineItem() {
		return sortFineItems;
	}

	public void setSortFineItem(ArrayList<SortFineItem> SortFineItems) {
		this.sortFineItems = SortFineItems;
		notifyDataSetChanged();
	}
	
	public class ItemLayoutView extends ViewHolder
	{
		public TextView tv1;
		public SimpleDraweeView draweeView;
		
		public ItemLayoutView(View itemView) {
			super(itemView);
			tv1 = (TextView)itemView.findViewById(R.id.textView1);
			draweeView = (SimpleDraweeView)itemView.findViewById(R.id.imageg1);
		}
		
		public void SetGridDate(int position) {
			tv1.setText(sortFineItems.get(position).getName());
			draweeView.setImageURI(sortFineItems.get(position).getImage());
			
			RxView.clicks(draweeView)
        		.throttleFirst(1, TimeUnit.SECONDS)
        		.subscribe(new Actiaon1(sortFineItems.get(position)));
        		
			//draweeView.setOnClickListener(new Actiaon2(bcIndexDatas.get(position)));
		}
	}
	
	public class Actiaon1 implements Consumer<Object>
	{
		public SortFineItem sortFineItem;
		public Actiaon1(SortFineItem sortFineItem) {
			this.sortFineItem = sortFineItem;
		}
		@Override
		public void accept(Object arg0) throws Exception {
			activity.ToComicDirectory(sortFineItem);
		}
	}
	
}
