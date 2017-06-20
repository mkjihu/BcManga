package com.base.BackActivity;

import java.util.LinkedList;
import java.util.List;

import com.bc_manga2.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;


public class SwipeBackLayout extends FrameLayout {
	private static final String TAG = SwipeBackLayout.class.getSimpleName();
	private View mContentView;
	private int mTouchSlop;
	private int downX;
	private int downY;
	private int tempX;
	private Scroller mScroller;
	private int viewWidth;
	private boolean isSilding;
	private boolean isFinish;
	private Drawable mShadowDrawable;
	private Activity mActivity;
	private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();
	
	
	//*------
    /*页面边缘阴影的宽度默认值*/
    private final int SHADOW_WIDTH = 10 ;
    /*页面边缘阴影的宽度*/
    private int mShadowWidth ;
    /*绘制阴影背景的画笔*/
    private Paint mShadowPaint;
    /*用于绘制阴影时的梯度变化*/
    private float mRatio;
    /*页面滑动的距离*/
    private int mPix;
    /*是否开始滑动*/
    private boolean mIsBeingDragged;
    /*共外面调用的监听事件*/
    private OnPageChangeListener mListener;
    /*内部监听事件*/
    private OnPageChangeListener mInternalPageChangeListener;
    
    
    
	
	public SwipeBackLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);

		mShadowDrawable = getResources().getDrawable(R.drawable.left_shadow);
		
		//--陰引寬度---
		final float density = context.getResources().getDisplayMetrics().density;
		mShadowWidth = (int) (SHADOW_WIDTH*density);
		//背景音影
		mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mShadowPaint.setColor(0xff000000);
		mInternalPageChangeListener = new InternalPageChangeListener();
	}
	
	
	public void attachToActivity(Activity activity) {
		mActivity = activity;
		TypedArray a = activity.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);
		a.recycle();
		Log.w("background", background+"");
		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setBackgroundResource(background);
		
		//decorChild.getBackground().setAlpha(100);
		decor.removeView(decorChild);
		addView(decorChild);
		setContentView(decorChild);
		decor.addView(this);
	}

	private void setContentView(View decorChild) {
		mContentView = (View) decorChild.getParent();
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
		Log.i(TAG, "mViewPager = " + mViewPager);
		
		if(mViewPager != null && mViewPager.getCurrentItem() != 0){
			return super.onInterceptTouchEvent(ev);
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = tempX = (int) ev.getRawX();
			downY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getRawX();

			if (moveX - downX > mTouchSlop
					&& Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
				return true;
			}
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getRawX();
			int deltaX = tempX - moveX;
			tempX = moveX;
			if (moveX - downX > mTouchSlop
					&& Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
				isSilding = true;
			}

			if (moveX - downX >= 0 && isSilding) {
				mContentView.scrollBy(deltaX, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			isSilding = false;
			if (mContentView.getScrollX() <= -viewWidth / 2) {
				isFinish = true;
				scrollRight();
			} else {
				scrollOrigin();
				isFinish = false;
			}
			break;
		}

		return true;
	}
	

	private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent){
		int childCount = parent.getChildCount();
		for(int i=0; i<childCount; i++){
			View child = parent.getChildAt(i);
			if(child instanceof ViewPager){
				mViewPagers.add((ViewPager)child);
			}else if(child instanceof ViewGroup){
				getAlLViewPager(mViewPagers, (ViewGroup)child);
			}
		}
	}
	
	

	private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev){
		if(mViewPagers == null || mViewPagers.size() == 0){
			return null;
		}
		Rect mRect = new Rect();
		for(ViewPager v : mViewPagers){
			v.getHitRect(mRect);
			
			if(mRect.contains((int)ev.getX(), (int)ev.getY())){
				return v;
			}
		}
		return null;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			viewWidth = this.getWidth();
			
			getAlLViewPager(mViewPagers, this);
			Log.i(TAG, "ViewPager size = " + mViewPagers.size());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mShadowDrawable != null && mContentView != null) {

			int left = mContentView.getLeft() - mShadowDrawable.getIntrinsicWidth();
			int right = left + mShadowDrawable.getIntrinsicWidth();
			int top = mContentView.getTop();
			int bottom = mContentView.getBottom();

			//mShadowDrawable.setBounds(left, top, right, bottom);
			//mShadowDrawable.draw(canvas);
		}
		/*绘制偏移的背影颜色*/
        drawBackground(canvas);
		drawShadow(canvas);
	}
	

    /**
     * 绘制shadow阴影
     * @param canvas
     */
    private void drawShadow(Canvas canvas){
        /*保存画布当前的状态，这个用法在我前面将自定义View 的时候将的很详细*/
        canvas.save() ;
        /*设置 drawable 的大小范围*/
        mShadowDrawable.setBounds(0, 0, mShadowWidth, getHeight());
        /*让画布平移一定距离*/
        canvas.translate(-mShadowWidth,0);
        /*绘制Drawable*/
        mShadowDrawable.draw(canvas);
        /*恢复画布的状态*/
        canvas.restore();
    }
    /**
     * 绘制背景颜色，随着距离的改变而改变
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        mShadowPaint.setAlpha((int) ((1 - mRatio) * 180));
        canvas.drawRect(-mPix, 0, 0, getHeight(), mShadowPaint);
    }
    
    
	
	private void scrollRight() {
		final int delta = (viewWidth + mContentView.getScrollX());

		mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
				Math.abs(delta));
		postInvalidate();
	}

	
	private void scrollOrigin() {
		int delta = mContentView.getScrollX();
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		
		if (mScroller.computeScrollOffset()) {
			mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();

			if (mScroller.isFinished() && isFinish) {
				mActivity.finish();
			}
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
        /*设置回调事件的值*/
        pageScrolled(x);
	}
	
	
    private void pageScrolled(int xpos) {
        final int widthWithMargin = getWidth();
        /*获取当前页面*/
        int position = Math.abs(xpos) / widthWithMargin;
        /*获取当前滑动的距离*/
        final int offsetPixels = Math.abs(xpos) % widthWithMargin;
        /*通过滑动的距离来获取梯度值*/
        final float offset = (float) offsetPixels / widthWithMargin;
        /*这里需要做特殊处理，因为只有一个页面*/
        position = mIsBeingDragged ? 0 : position;
        onPageScrolled(position, offset, offsetPixels);
    }
    
    
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, offset, offsetPixels);
        }
        mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
    }
    
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }
    
    
    private class InternalPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                mRatio = positionOffset;
                mPix = positionOffsetPixels;
                //invalidate();
        }

        @Override
        public void onPageSelected(int position) {}
    }
    public interface OnPageChangeListener {

        /*滑动页面滑动状态，当前页和页面的偏移梯度，页面的偏移位置*/
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        /*当前页面*/
        public void onPageSelected(int position);
    }
}
