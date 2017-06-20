package com.bc_manga2.Ui.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	
	
    public CustomViewPager(Context context) {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }   
    
	/** 
     * 上一次x坐标 
     */  
    private float beforeX;  
    private boolean isCanScroll = false;  
    private boolean right_ok = false;//右 
    private boolean left_ok = false; //左 
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
    	//Log.i("控", "2");
    	if(!isCanScroll)
    	{  
    		return false;//基本上不允許父控件控制
            //return super.onInterceptTouchEvent(ev);  
        }
    	else 
    	{  
            switch (ev.getAction()) {  
            case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）  
                beforeX = ev.getX();  
                break;  
            case MotionEvent.ACTION_MOVE:  
                float motionValue = ev.getX() - beforeX;  
                
                if (left_ok) {
                	if (motionValue < 0) {//左滑  
                    	Log.i("left_ok", "1");
                        //return  super.onInterceptTouchEvent(ev); 
                        return true;//左滑一定是父控
                    }  
                    else if (motionValue > 0 ) //往右
                    {
                    	Log.i("left_ok", "2");
                    	//return super.onInterceptTouchEvent(ev); 
                    	 return false;
                    }
				}
                if (right_ok) {
                	if (motionValue < 0) {//左滑  
                    	Log.i("right_ok", "1");
                      // return  super.onInterceptTouchEvent(ev); 
                         return false;
                    }  
                    else if (motionValue > 0 ) //往右
                    {
                    	Log.i("right_ok", "2");//右滑一定是父控
                    	//return super.onInterceptTouchEvent(ev); 
                    	return true;
                    }
				}
                
                
                beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题  
                  
                break;  
            default:  
                break;  
            }  
            //
            return super.onInterceptTouchEvent(ev);  
        }  
    }  
      
    //-----禁止左滑-------左滑：上一次坐标 > 当前坐标  
    public boolean isScrollble() {  
        return isCanScroll;  
    }  
    /** 设置 是否可以滑动 */  
    public void setScrollble(boolean isCanScroll) {  
        this.isCanScroll = isCanScroll;  
    }
	public boolean isRight_ok() {
		return right_ok;
	}
	public void setRight_ok(boolean right_ok) {
		this.right_ok = right_ok;
	}
	public boolean isLeft_ok() {
		return left_ok;
	}
	public void setLeft_ok(boolean left_ok) {
		this.left_ok = left_ok;
	}  
  
    
        
}
