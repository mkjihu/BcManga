package com.bc_manga2.Ui.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PhotoViewPager extends ViewPager {
	
	public PhotoViewPager(Context context) {
	        super(context);
	}
	
	public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    
    ///
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            return false; //-不允许滑动在页面之间切换
        }
    }
	    
}
