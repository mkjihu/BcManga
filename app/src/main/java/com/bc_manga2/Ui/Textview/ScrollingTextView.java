package com.bc_manga2.Ui.Textview;
import android.content.Context;  
import android.graphics.Paint;
import android.graphics.Rect;  
import android.util.AttributeSet;  
import android.widget.TextView; 

public class ScrollingTextView extends TextView {  
   
    public ScrollingTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
   
    public ScrollingTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
   
    public ScrollingTextView(Context context) {  
        super(context);  
    }  
    @Override  
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {  
        if(focused)  
            super.onFocusChanged(focused, direction, previouslyFocusedRect);  
    }  
   
    @Override  
    public void onWindowFocusChanged(boolean focused) {  
        if(focused)  
            super.onWindowFocusChanged(focused);  
    }  
   
   
    @Override  
    public boolean isFocused() {  
        return true;  
    }

	@Override
	protected void onTextChanged(CharSequence text, int start,int lengthBefore, int lengthAfter) {
		
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}  
	
	
	
	private int getAvailableWidth()
    {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
    private boolean isOverFlowed()
    {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) return true;
        return false;
    }
}  