package com.bc_manga2.Adapder;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;

/**純ImageUrl列表*/
public abstract class BaseReadingAdapder extends PagerAdapter{

	/**取得當前話數圖片列表*/
	public abstract ArrayList<String> GetImageUrls();
	
}
