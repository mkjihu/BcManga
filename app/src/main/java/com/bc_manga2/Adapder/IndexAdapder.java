package com.bc_manga2.Adapder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.Activity.ComicDirectoryPage;
import com.bc_manga2.R;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.jakewharton.rxbinding2.view.RxView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

public class IndexAdapder extends RecyclerView.Adapter<ViewHolder>{

	private ArrayList<ItemComicIndex> comicIndexs;
	private ComicDirectoryPage activity;
	private LayoutInflater mLayoutInflater;
	/**
	 * 標記看到話數的位置
	 * 預設為-1沒有
	 */
	private int FocusItemKey=-1;//標記看到話數的位置
	
	public IndexAdapder(ComicDirectoryPage activity,ArrayList<ItemComicIndex> comicIndexs) {
		this.activity = activity;
		this.comicIndexs = comicIndexs;
		mLayoutInflater = LayoutInflater.from(activity);
	}
	
	public IndexAdapder(ComicDirectoryPage activity,ArrayList<ItemComicIndex> comicIndexs,int FocusItemKey) {
		this.activity = activity;
		this.comicIndexs = comicIndexs;
		this.FocusItemKey = FocusItemKey;
		mLayoutInflater = LayoutInflater.from(activity);
	}
	
	@Override
	public int getItemCount() {
		return comicIndexs.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder arg0, int Position) {
		IndexItem indexItem = (IndexItem)arg0;
		if(getItemCount()>0)
		{
			indexItem.SetItemData(comicIndexs.get(Position),Position);
			//Log.d("arg1", arg1+"");
			//indexItem.ComicIndexItem_text.setTag(arg1);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		//產生View
		return new IndexItem(mLayoutInflater.inflate(R.layout.item_comic_index,arg0, false));
	}
	
	public class IndexItem extends ViewHolder
	{
		private FrameLayout ComicIndexItem_frame;
		//private ScrollingTextView ComicIndexItem_text;
		private TextView ComicIndexItem_text;
		public IndexItem(View itemView) {
			super(itemView);
			ComicIndexItem_frame = (FrameLayout)itemView.findViewById(R.id.ComicIndexItem_frame);
			//ComicIndexItem_text = (ScrollingTextView)itemView.findViewById(R.id.ComicIndexItem_text);
			ComicIndexItem_text = (TextView)itemView.findViewById(R.id.ComicIndexItem_text);
		}
		
		private void SetItemData(ItemComicIndex comicIndexItem,int Position) 
		{
			
			if(FocusItemKey!=-1) {
				/*//快速標記法
				if (FocusItemKey == Position) {
					ComicIndexItem_frame.setBackgroundResource(R.drawable.item_button_focus);
				}
				//嚴謹檢查標記法
				if(comicIndexItem.isFocusItem()) {					
					ComicIndexItem_frame.setBackgroundResource(R.drawable.item_button_focus);
				}
				*/				
			}
			ComicIndexItem_text.setText(comicIndexItem.getItemName());
			
    		RxView.clicks(ComicIndexItem_frame)
        	.throttleFirst(1, TimeUnit.SECONDS)
        	.subscribe(new Act(comicIndexItem.getItemUrl()));
        	/*
			ComicIndexItem_text.setOnClickListener(new Actiaon2(comicIndexItem.getItemUrl()));
        	*/
		}
		/*
		public class Actiaon2 implements OnClickListener
		{
			public String Image_PKUrl;
			public Actiaon2(String Image_PKUrl) {
				this.Image_PKUrl = Image_PKUrl;
			}
			@Override
			public void onClick(View v) {
				activity.ToComicReading(Image_PKUrl);
			}
			
		}*/
		public class Act implements Consumer<Object> {
			public String Image_PKUrl;
			public Act(String Image_PKUrl) {
				this.Image_PKUrl = Image_PKUrl;
			}
			@Override
			public void accept(Object arg0) throws Exception {
				activity.ToComicReading(Image_PKUrl);
			}
			
		}
		
	}
}
