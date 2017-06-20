package com.bc_manga2.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.bc_manga2.Model.EventBus.MessageEvent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

public abstract  class BaseFragment  extends Fragment implements OnTouchListener{
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//EventBus.getDefault().register(this);//注册
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutResource(), container, false); 
        view.setOnTouchListener(this);  
        
        initData(savedInstanceState);
        findViewById(view);
        initVariables();
		return view;
	}
	

	protected void initVariables() {
    }

    protected void initData(Bundle savedInstanceState) {
    }
    
    /**加载页面View*/
    protected abstract void findViewById(View view);
    
    /**载入layout*/
    protected abstract int getLayoutResource();

    /**进入目录*/
	public abstract void ToComicDirectory(Object object);
	
	
    @Override
    public void onDestroy() {
    	//EventBus.getDefault().unregister(this);//反注册
        super.onDestroyView();
    }
    

    public static interface DataCallback {
    	void RecyclerViewsetData(RecyclerView.Adapter<ViewHolder> adapter);
    	
        void offRefresh();
        void openRefresh();

    }
    public static interface DataCallback2 {
    	void PagerAdapterData(PagerAdapter adapter);
    	
        void offRefresh();
        void openRefresh();

    }
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
