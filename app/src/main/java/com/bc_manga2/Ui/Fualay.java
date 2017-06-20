package com.bc_manga2.Ui;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.FrameLayout;

public class Fualay extends FrameLayout{

	private GestureDetector mGD;
    //----------
    //---各種手勢動作監聽-----
    private OnPrViewListener onPrViewListener = null;

	private float Stand_x1,Stand_x2;//x軸的區域點
	private float Stand_y1,Stand_y2;//y軸的區域點

	private void SteStandard(int w, int h)//x由左往右算，y由上往下算
	{
		Stand_x1 = 2*((float)w/7);
		Stand_x2 = 5*((float)w/7);
		Stand_y1 = ((float)h/5);
		Stand_y2 = 4*((float)h/5);
		//Log.w("計算的點", Stand_x1+"_"+Stand_x2+"_"+Stand_y1+"_"+Stand_y2);
		
	}
	
	
	
    
    public Fualay(Context context) 
    {
        super(context);
        setGsDs(context);
    }
    
    public Fualay(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        setGsDs(context);
    }
    
    private void setGsDs(Context context)
    {
    	mGD = new GestureDetector(context, new GestureListener());
    	mGD.setIsLongpressEnabled(false); //设置为长按不启用
    }
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.i("高4", h+"");
		Log.i("寬4", w+"");
		SteStandard(w, h);
		super.onSizeChanged(w, h, oldw, oldh);
	}

    
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) 
    {
    	int action = event.getAction() & MotionEventCompat.ACTION_MASK;
    	
    	mGD.onTouchEvent(event);//通过 .onTouchEvent(event); 传递给onTouch。
    	
    	switch (action) 
    	{
    	case MotionEvent.ACTION_DOWN:  //触摸
			Log.i("摸", "摸");
			setOnDown(event);
			this.requestDisallowInterceptTouchEvent(false);//允許攔截觸摸事件
			
			/*
			//计算 x，y 的距离
            int index = MotionEventCompat.getActionIndex(event);
            mActivePointerId = MotionEventCompat.getPointerId(event, index);
            if (mActivePointerId == INVALID_POINTER)
                break;
            mLastMotionX = mInitialMotionX = MotionEventCompat.getX(event, index);//记录上一次手指触摸的点
            mLastMotionY = MotionEventCompat.getY(event, index);//记录上一次手指触摸的点
            
            Log.i("触摸的点", "X："+mLastMotionX+"  "+"Y:"+mLastMotionY);
           */
            
			
			break;  
    	 case MotionEvent.ACTION_UP:  //抬起	
         	Log.i("抬起", "抬起");
         	//this.requestDisallowInterceptTouchEvent(false);//允許攔截觸摸事件
         	
         	/*
         	int index2 = MotionEventCompat.getActionIndex(event);
            mActivePointerId = MotionEventCompat.getPointerId(event, index2);
            if (mActivePointerId == INVALID_POINTER)
                break;
            mLastMotionX = mInitialMotionX = MotionEventCompat.getX(event, index2);//记录上一次手指触摸的点
            mLastMotionY = MotionEventCompat.getY(event, index2);//记录上一次手指触摸的点
            
         	Log.i("抬起的点", "X："+mLastMotionX+"  "+"Y:"+mLastMotionY);
         	*/
         	//return super.onInterceptTouchEvent(event);
			
         	break;
         	
        case MotionEvent.ACTION_MOVE:  //滑动
        	//Log.i("滑", "滑");
        	this.requestDisallowInterceptTouchEvent(false);//允許攔截觸摸事件
        	
        	
        	return super.onInterceptTouchEvent(event);
       
        case MotionEvent.ACTION_POINTER_DOWN: //多手指按下
        	//Log.i("摸2點", "摸2點");
        	this.requestDisallowInterceptTouchEvent(true);//要求不允許攔截觸摸事件
        	///this.requestParentDisallowInterceptTouchEvent(true);//要求家長不允許攔截觸摸事件
        	
        	
        	return super.onInterceptTouchEvent(event);
        case MotionEvent.ACTION_POINTER_UP: //多手指抬起
        	//Log.i("摸2點-抬起", "摸2點-抬起");
        	return super.onInterceptTouchEvent(event);
        	//break;
		default:
			break;
		}
    	
    	/** 
         * 当发行屏幕触控事件的时候，首先出发此方法，再通过此方法，监听具体的整件 
         * 在onTouch()方法中，我们调用GestureDetector的onTouchEvent 
         * ()方法，将捕捉到的MotionEvent交给GestureDetector 来分析是否有合适的callback函数来处理用户的手势 
         */  
    	
    	
    	//mGD.onTouchEvent(event);//通过 .onTouchEvent(event); 传递给onTouch。
    	return super.onInterceptTouchEvent(event);
    	//return true;
    	/*
        try 
        {
        	//return false;
            return super.onInterceptTouchEvent(event);
        }  
        catch (IllegalArgumentException e) {
            //不理会      
        	Log.i("不理会", "不理会1");
            return false;
        }
        catch(ArrayIndexOutOfBoundsException e ){
            //不理会
        	Log.i("不理会", "不理会2");
            return false;
        }
        */
    }

    
    
	private class GestureListener extends SimpleOnGestureListener
	{
		@Override
		public void onLongPress(MotionEvent e)
		{
			// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发  
			//Log.w("onLongPress", "长按");  
			super.onLongPress(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) 
		{
			// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发  
			// e1：第1个ACTION_DOWN MotionEvent  
	        // e2：最后一个ACTION_MOVE MotionEvent  
	        // distanceX沿X轴（轴），因为到onScroll最后呼叫已滚动的距离。这不是e1和e2之间的距离。
	        // distanceY沿自于onScroll最后一次通话已滚动Y轴的距离。这不是e1和e2之间的距离。
			//Log.w("onScroll", "拖动");  
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) // Touch了滑动一点距离后，up时触发 
		{
			/* 
		     * 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 
		     * 1个ACTION_UP触发(non-Javadoc) 
		     * Fling事件的处理代码：除了第一个触发Fling的ACTION_DOWN和最后一个ACTION_MOVE中包含的坐标等信息外 
		     * ，我们还可以根据用户在X轴或者Y轴上的移动速度作为条件 
		     * 比如下面的代码中我们就在用户移动超过100个像素，且X轴上每秒的移动速度大于200像素时才进行处理。 
		     *  
		     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view. 
		     * MotionEvent, android.view.MotionEvent, float, float) 
		     * 这个例子中，tv.setLongClickable( true )是必须的，因为 
		     * 只有这样，view才能够处理不同于Tap（轻触）的hold（即ACTION_MOVE，或者多个ACTION_DOWN） 
		     * ，我们同样可以通过layout定义中的android:longClickable来做到这一点 
		     */  
			// 参数解释：  
	        // e1：第1个ACTION_DOWN MotionEvent  
	        // e2：最后一个ACTION_MOVE MotionEvent  
	        // velocityX：X轴上的移动速度，像素/秒  
	        // velocityY：Y轴上的移动速度，像素/秒  
	  
	        // 触发条件 ：  
	        // X轴的坐标位移大于FLING_MIN_DISTANCE(70)，且移动速度大于FLING_MIN_VELOCITY(100)个像素/秒  
			final int FLING_MIN_DISTANCE = 50, FLING_MIN_VELOCITY = 100;  
			
			
			//Log.w("onFling",  HackyViewPager_v3.this.getCurrentItem()+""); 
			
			if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) 
			{  
	            //X轴的坐标位移大于FLING_MIN_DISTANCE
	            //Log.w("onFling"+DoubleTap, "左丟"); 
		
	        } 
			else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE  && Math.abs(velocityX) > FLING_MIN_VELOCITY) 
	        {  
				//X轴的坐标位移大于FLING_MIN_DISTANCE
	            //Log.w("onFling", "右丟");
				
	        }  
			
			//return true;  
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onShowPress(MotionEvent e) 
		{
			//用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发 
			//注意和onDown()的区别，强调的是没有松开或者拖动的状态 (单击没有松开或者移动时候就触发此事件，再触发onLongPress事件) 
			 /* 
	         * Touch了还没有滑动时触发 (1)onDown只要Touch Down一定立刻触发 (2)Touch 
	         * Down后过一会没有滑动先触发onShowPress再触发onLongPress So: Touch Down后一直不滑动，onDown 
	         * -> onShowPress -> onLongPress这个顺序触发。 
	         */  
			//Log.w("onShowPress", "尚未松开或拖动");  
			super.onShowPress(e);
		}

		
		@Override
		public boolean onDown(MotionEvent e)
		{
			// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发 
			//int action = MotionEventCompat.getActionMasked(e);
			//Log.w("onDown", "触摸"+action);  
			
			return super.onDown(e);
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) 
		{
			// 双击的第二下Touch down时触发
			//Log.w("onDoubleTap", "双击第二下down");
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) 
		{
			// 双击的第二下Touch down和up都会触发，可用e.getAction()区分  
			
			
			switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.w("onDoubleTapEvent", "目前是雙擊down");
				//DoubleTap = true; //目前是雙擊/
				
				break;
			case MotionEvent.ACTION_UP:
				Log.w("onDoubleTapEvent", "雙擊結束up");
				
				//DoubleTap = false; //雙擊結束
				break;
			default:
				break;
			}
			
			return super.onDoubleTapEvent(e);
		}
		
		
		 /* 
         * 两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发 
         * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed 
         * 点击一下稍微慢点的(不滑动)Touch Up://确认是单击事件触发 
         * onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed 
         */  
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) 
		{
			// 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed
			Log.w("快速短按", "快速短按"+e.getX()+"_"+e.getY());
			
			//int index = MotionEventCompat.getActionIndex(e);
			//Log.w("快速短按2", MotionEventCompat.getX(e, index)+"_"+MotionEventCompat.getY(e, index));
			//這裡呼叫換頁
			
			//狀況1
			boolean ta1 = (e.getX()<Stand_x1-1);
			//狀況1_1
			boolean ta1_1 = (e.getX()<Stand_x2-5 && e.getY()<Stand_y1-1);
			
			
			//狀況2
			boolean ta2 = (e.getX()>Stand_x2+1);
			//狀況2_1
			boolean ta2_1 = (e.getX()>Stand_x1+5 && e.getY()>Stand_y2+1);
			
			//狀況3
			boolean ta3 = ((e.getX()>Stand_x1+5 && e.getX()<Stand_x2-5) && (e.getY()>Stand_y1+5 && e.getY()<Stand_y2-5));
			
			if(ta1 ||ta1_1)
			{
				//Log.i("上一頁", "上一頁");
				setOnPrevious();
			}
			else if(ta2 || ta2_1)
			{
				//Log.i("下一頁", "下一頁");
				setOnNext();
			}
			else if(ta3)
			{
				Log.i("中間", "中間");
				
				setOnIntermediate();
			}
			
			return super.onSingleTapConfirmed(e);
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) 
		{
			// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发  
			//点击一下稍微慢点的(不滑动) onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed 
			
			Log.w("onSingleTapUp", "漫速短按");
			return super.onSingleTapUp(e);
		}
	}
	
	
	/**
     * 自定義要傳出的監聽事件
     */
	public interface OnPrViewListener 
	{
	   public void OnPrevious(); 
	   /**點擊中間*/
	   public void OnIntermediate(); 
	   public void OnNext(); 
	   /** 摸*/
	   public void OnDown(MotionEvent e);  
	   /**左丟又丟*/
	   public void OnFlingL();  
	   /**左丟又丟*/
	   public void OnFlingR(); 
	}
	
	/**
	 *左丟又丟
	 */
	public interface OnFlingLViewListener 
	{
	   public void OnFlingL(int ItemIndex);  
	}
	public interface OnFlingRViewListener 
	{
	   public void OnFlingR(int ItemIndex);  
	}
	
	
	/**
	 * 監聽
	 */
	public void setOnPrevious() 
	{
        if (onPrViewListener != null) 
        {
        	onPrViewListener.OnPrevious();
        }
    }
	
	public void setOnIntermediate() 
	{
        if (onPrViewListener != null) 
        {
        	onPrViewListener.OnIntermediate();
        }
    }
	
	public void setOnNext() 
	{
        if (onPrViewListener != null) 
        {
        	onPrViewListener.OnNext();
        }
    }
	
	public void setOnFlingL(int a) 
	{
        if (onPrViewListener != null) 
        {
        	onPrViewListener.OnFlingL();
        }
    }
	public void setOnFlingR(int a) 
	{
        if (onPrViewListener != null) 
        {
        	onPrViewListener.OnFlingR();
        }
    }
	public void setOnDown(MotionEvent e) 
	{
		if(onPrViewListener != null)
		{
			onPrViewListener.OnDown(e);
		}
    }
	
	
	/**
     * 创建点击事件接口
     */
	 public void setOnPreviousViewListener(OnPrViewListener onPrViewListener)
	 {
         this.onPrViewListener = onPrViewListener;
	 } 
 
	
}
