package com.bc_manga2.Adapder;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.R;
import com.bc_manga2.Fragment.BaseFragment;
import com.bc_manga2.Resolve.Sort.ItemSort;
import com.jakewharton.rxbinding2.view.RxView;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

/**
 * 分類1
 * 
 */
public class SortAdapder extends RecyclerView.Adapter<ViewHolder>{
	
	private LayoutInflater mLayoutInflater;
	private BaseFragment fragment;
	private ArrayList<ItemSort> itemSorts;
	
	public Integer[] colos = {R.color.sortcolo1,R.color.sortcolo2,R.color.sortcolo13,R.color.sortcolo4
			,R.color.sortcolo5,R.color.sortcolo6,R.color.sortcolo7,R.color.sortcolo8
			,R.color.sortcolo9,R.color.sortcolo10,R.color.sortcolo11,R.color.sortcolo12
			,R.color.sortcolo13,R.color.sortcolo14,R.color.sortcolo15,};
	
	public SortAdapder(BaseFragment fragment,ArrayList<ItemSort> itemSorts) {
		this.itemSorts = itemSorts;
		this.fragment= fragment;
		mLayoutInflater = LayoutInflater.from(fragment.getContext());
	}

	@Override
	public int getItemCount() {
		return itemSorts.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		((SortGridLayoutView)holder).SetGridDate(position);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new SortGridLayoutView(mLayoutInflater.inflate(R.layout.item_sort_gidview_item, arg0, false));
	}

	
	public class SortGridLayoutView extends ViewHolder
	{
		
		public CardView item_sort_card;
		public TextView sort_itemtv1,sort_itemtv2;
		
		public SortGridLayoutView(View itemView) {
			super(itemView);
			item_sort_card = (CardView)itemView.findViewById(R.id.item_sort_card);
			sort_itemtv1 = (TextView)itemView.findViewById(R.id.sort_itemtv1);
			sort_itemtv2 = (TextView)itemView.findViewById(R.id.sort_itemtv2);
			//sort_itemtv1.setTypeface(Typeface.createFromAsset(fragment.getActivity().getAssets(), "fonts/setofont.ttf"));//-設置字體太耗資源了
		}
		
		public void SetGridDate(int position) {
			int si = new Random().nextInt(15);
			item_sort_card.setCardBackgroundColor(ContextCompat.getColor(fragment.getContext(), colos[si]));
			sort_itemtv1.setText(itemSorts.get(position).getItemName());
			sort_itemtv2.setText(itemSorts.get(position).getItemName());
			
			RxView.clicks(item_sort_card)
        		.throttleFirst(1, TimeUnit.SECONDS)
        		.subscribe(new Actiaon1(itemSorts.get(position)));
        	
			//item_sort_card.setOnClickListener(new Actiaon2(itemSorts.get(position)));
		}
		
		/*
		public class Actiaon2 implements OnClickListener
		{
			public Object itemSort;
			public Actiaon2(Object itemSort) {
				this.itemSort = itemSort;
			}
			@Override
			public void onClick(View v) { 
				fragment.ToComicDirectory(itemSort);
			} 
		}
		*/
		
		
		public class Actiaon1 implements Consumer<Object>
		{
			public Object itemSort;
			public Actiaon1(Object itemSort) {
				this.itemSort = itemSort;
			}
			@Override
			public void accept(Object arg0) throws Exception {
				fragment.ToComicDirectory(itemSort);
			}
		}
	}
}
