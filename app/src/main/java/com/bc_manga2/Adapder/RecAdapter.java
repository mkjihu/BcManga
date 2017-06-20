package com.bc_manga2.Adapder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RecAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();//添加的Fragment的集合  
    private final List<String> mFragmentsTitles = new ArrayList<>();//每个Fragment对应的title的集合  
    
	private Context mContext;
	public RecAdapter(Context context,FragmentManager fm) {
		super(fm);
		this.mContext = context;
	}
	
    public void addFragment(Fragment fragment, String fragmentTitle) {  
        mFragments.add(fragment);  
        mFragmentsTitles.add(fragmentTitle);  
    }  
    
    
	@Override
	public Fragment getItem(int arg0) {
		//得到对应position的Fragment  
        return mFragments.get(arg0); 
	}
	
    @Override  
    public int getCount() {  
        //返回Fragment的数量  
        return mFragments.size();  
    } 
    @Override  
    public CharSequence getPageTitle(int position) {  
        //得到对应position的Fragment的title  
        return mFragmentsTitles.get(position);  
    }
}
