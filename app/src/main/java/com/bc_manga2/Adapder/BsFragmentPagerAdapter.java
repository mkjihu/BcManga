package com.bc_manga2.Adapder;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BsFragmentPagerAdapter extends FragmentPagerAdapter{

	private ArrayList<? extends Fragment> mFragments;
	
	//private List<String> mTagList; // 用来存放所有的 Tag
	private FragmentManager mFragmentManager;
	
	public BsFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragments) {
		super(fm);
		this.mFragments = mFragments;
		this.mFragmentManager = fm;	
		//this.mTagList = new ArrayList<>();
	}

	@Override
	public Fragment getItem(int arg0) {
		//对于FragmentPagerAdapter，当每页的Fragment被创建后，这个函数就不会被调到了。对于FragmentStatePagerAdapter，由于Fragment会被销毁
		//当adapter需要一个指定位置的Fragment，并且这个Fragment不存在时，getItem就被调到，返回一个Fragment实例给adapter
		//getItem是创建一个新的Fragment，但是这个方法名可能会被误认为是返回一个已经存在的Fragment
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int position) {
		//mTagList.add(position, makeFragmentName(arg0.getId(), (int) getItemId(position)));
		return super.instantiateItem(arg0, position);
	}

	@Override
	public int getItemPosition(Object object) {
		// 解决数据源清空，Item 不销毁的 bug
		//return POSITION_NONE; //notifyDataSetChanged 時 強制所有頁刷新  系统默认返回的是 	POSITION_UNCHANGED，未改变
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return super.getPageTitle(position);
	}


	
	
	///==========FragmentPageAdapter源码里给 Fragment 生成标签的方法 ”android:switcher:” + R.id.viewpager + “:0″ 这个字符窜表示的就是该fragment的tag===============
    private String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
	
/*
    public void update(int position, String str) {
        Fragment fragment = mFragmentManager.findFragmentByTag(mTagList.get(position));
        if (fragment == null) return;
        if (fragment instanceof VerImageFragment) {
        	
        
        }
        
        //notifyDataSetChanged();
    }

	public List<String> getmTagList() {
		return mTagList;
	}
*/

}
