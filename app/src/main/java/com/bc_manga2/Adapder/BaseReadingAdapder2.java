package com.bc_manga2.Adapder;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

/**純ImageUrl列表*/
public abstract class BaseReadingAdapder2 extends RecyclerView.Adapter<ViewHolder>{

	/**取得當前話數圖片列表*/
	public abstract ArrayList<String> GetImageUrls();
	
}
