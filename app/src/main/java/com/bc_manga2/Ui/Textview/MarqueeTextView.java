package com.bc_manga2.Ui.Textview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

public class MarqueeTextView extends TextView 
{
	   Context context;  
	   Scroller mScroller;  
	   int mDistance;  
	   int mDuration;  
	   float mVelocity;  
	   public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {  
	     super(context, attrs, defStyle);  
	     this.context = context;  
	     setSingleLine();  
	     // 在 new Scroller 時候加入 LinearInterpolater(線性插補器) 參數  
	     // 此參數可以讓我們的跑馬燈以線性等速度移動，不然預設是黏滯地(viscous)移動  
	     mScroller = new Scroller(context, new LinearInterpolator());  
	     setScroller(mScroller);  
	   }  
	   public MarqueeTextView(Context context, AttributeSet attrs) {  
	     super(context, attrs);  
	     this.context = context;  
	     setSingleLine();  
	     mScroller = new Scroller(context, new LinearInterpolator());  
	     setScroller(mScroller);  
	   }  
	   public MarqueeTextView(Context context) {  
	     super(context);  
	     this.context = context;  
	     setSingleLine();  
	     mScroller = new Scroller(context, new LinearInterpolator());  
	     setScroller(mScroller);  
	   }  
	   @Override  
	   public void computeScroll() {  
	     super.computeScroll();  
	     if(mScroller.computeScrollOffset()){  
	       scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
	     } else {  
	       if(mScroller.isFinished()){  
	         mScroller.startScroll(-getWidth(), 0, calculateMoveDistance(false, mVelocity), 0, mDuration);  
	       }  
	     }  
	   }  
	   /**  
	    * 計算應該移動的距離  
	    * @param isFirstRun 是否是第一輪  
	    * @param velocity 速率  
	    * @return  
	    */  
	   private int calculateMoveDistance(boolean isFirstRun, float velocity){  
	     Rect rect = new Rect();  
	     String textString = (String) getText();  
	     getPaint().getTextBounds(textString, 0, textString.length(), rect);  
	     int moveDistance = rect.width();  
	     rect = null;  
	     this.mDistance = isFirstRun ? moveDistance : moveDistance + getWidth();  
	     this.mDuration = (int) (velocity * mDistance);  
	     return this.mDistance;  
	   }  
	   /**  
	    * 從這裡設定速度值  
	    * @param velocity 速度值越小越快  
	    */  
	   public void scrollText(float velocity){  
	     this.mVelocity = velocity;  
	     mScroller.startScroll(0, 0, calculateMoveDistance(true, velocity), 0, mDuration);  
	   }    
}
