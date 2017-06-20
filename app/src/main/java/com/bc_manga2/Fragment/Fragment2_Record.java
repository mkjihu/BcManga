package com.bc_manga2.Fragment;


import org.greenrobot.eventbus.EventBus;

import com.bc_manga2.R;
import com.bc_manga2.Adapder.RecAdapter;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.View.maView;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class Fragment2_Record extends Fragment implements OnTouchListener,maView
{

	private TabLayout tabLayout;
	private ViewPager viewpager;
	private RecAdapter adapter;
	//收藏&記錄
	public Fragment2_Record() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment2_record, null);  
        view.setOnTouchListener(this);  
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		fid();
		initclick();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	GetDBData();
	    }
	}
	
	/**取得DB資料*/
	public void GetDBData() {
		EventBus.getDefault().post(new MessageEvent(TagInfo.YueduTage));
		EventBus.getDefault().post(new MessageEvent(TagInfo.ColleTage));
	}
	
	public void fid() {
		viewpager= (ViewPager)getView().findViewById(R.id.viewpager);
		tabLayout= (TabLayout)getView().findViewById(R.id.tabs);
		
	}
	
	public void initclick() 
	{
		adapter = new RecAdapter(getActivity(), getChildFragmentManager());
		adapter.addFragment(new Fragment2_Yuedu(), "閱讀紀錄");//添加Fragment  
		adapter.addFragment(new Fragment2_Collection(), "收藏");  
		viewpager.setAdapter(adapter);//设置适配器  
		tabLayout.setupWithViewPager(viewpager);
		tabLayout.setTabMode(TabLayout.MODE_FIXED);
	}

	@Override
	public void onStart() 
	{
		super.onStart();
	}
	@Override
	public void onResume()
	{
		super.onResume();
	}
	@Override
	public void onPause() 
	{
		super.onPause();
	}
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

}
