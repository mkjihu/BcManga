package com.bc_manga2.Adapder;

import java.util.ArrayList;

import com.bc_manga2.Fragment.Fragment_ComicItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
/**
 * FragmentStatePagerAdapter与FragmentPagerAdapter的不同：

	Fragment在FragmentStatePagerAdapter类的destroyItem方法中被remove时，Fragment的onDestroy方法和onDetach方法都被调用到。
	当Fragment重新被add时，Fragment的生命周期全部重新调用，但是savedInstanceState参数保留着之前存储的数据。
 * @author kevinh
 *
 */
public class BsFragmentStatePagerAdapter extends FragmentStatePagerAdapter
{
	private ArrayList<? extends Fragment> mFragments;
	public BsFragmentStatePagerAdapter(FragmentManager fm,ArrayList<Fragment_ComicItem> mFragments2) {
		super(fm);
		this.mFragments = mFragments2;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}
	
    @Override
    public Object instantiateItem(ViewGroup container, int position) {//初始子View方法
        return super.instantiateItem(container, position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁子View
        super.destroyItem(container, position, object);
    }

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return super.isViewFromObject(view, object);
	}
    
}
