package com.bc_manga2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.Fragment.Fragment1_Home;
import com.bc_manga2.Fragment.Fragment2_Record;
import com.bc_manga2.Fragment.Fragment3_Search;
import com.bc_manga2.Fragment.Fragment4_Settings;
import com.bc_manga2.Fragment.FragmentA;
import com.bc_manga2.Fragment.FragmentC;
import com.bc_manga2.Fragment.FragmentD;
import com.bc_manga2.Ui.NoScrollViewPager;
import com.bc_manga2.View.maView;
import com.jakewharton.rxbinding2.view.RxView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import devlight.io.library.ntb.NavigationTabBar;
import devlight.io.library.ntb.NavigationTabBar.Model;
import devlight.io.library.ntb.NavigationTabBar.OnTabBarSelectedIndexListener;
import io.reactivex.functions.Consumer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;



public class MainActivity extends AppCompatActivity implements maView {

	private NoScrollViewPager viewPager;//
	private NavigationTabBar navigationTabBar ;
	//首頁 收藏&紀錄 搜尋 設定
	private TextView toolbar_title;
	private String[] texts = {"首頁","收藏&紀錄","分類&搜尋","設定"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Fragment1_Home());//(new FragmentA());//Fragment1_Home()
        fragmentList.add(new Fragment2_Record());//(new Fragment2_Record()());
        fragmentList.add(new Fragment3_Search());//Fragment3_Search()
        fragmentList.add(new Fragment4_Settings());//Fragment4_Settings()
        //final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);//
        
        viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), fragmentList));
       
        /*
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from( getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(new RecycleAdapter());

                container.addView(view);
                return view;
            }
        });
         */
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final int bgColor = ContextCompat.getColor(this, R.color.BarColor2);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<Model> models = new ArrayList<>();
        
        models.add(
                new Model.Builder(
                		ContextCompat.getDrawable(this, R.drawable.q1)
                		,bgColor)
                        //.selectedIcon(ContextCompat.getDrawable(this, R.drawable.ic_sixth))
                        .title("首頁")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new Model.Builder(
                		ContextCompat.getDrawable(this, R.drawable.q2),
                        bgColor)
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("收藏&紀錄")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new Model.Builder(
                		ContextCompat.getDrawable(this, R.drawable.q3), bgColor)
                        //.selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("分類&搜尋")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new Model.Builder(
                		ContextCompat.getDrawable(this, R.drawable.q4),
                        bgColor)
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("設定")
                        .badgeTitle("icon")
                        .build()
        );

        
        navigationTabBar.setModels(models);
        navigationTabBar.setModelIndex(0, false);
         //
        //navigationTabBar.setViewPager(viewPager);
        navigationTabBar.setBehaviorEnabled(true);//
        navigationTabBar.setOnTabBarSelectedIndexListener(new OnTabBarSelectedIndexListener() {
			
			@Override
			public void onStartTabSelected(Model model, int position) {
				navigationTabBar.getModels().get(position).hideBadge();
                viewPager.setCurrentItem(position, true);
                toolbar_title.setText(texts[position]);
			}
			@Override
			public void onEndTabSelected(Model model, int position) {
				// TODO Auto-generated method stub
			}
		});
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            	Log.i("2", position+"");
            }

            @Override
            public void onPageSelected(final int position) {
            	Log.i("1", position+"");
                navigationTabBar.getModels().get(position).hideBadge();
                viewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        /**/
        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
        
        /*
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final String title = String.valueOf(new Random().nextInt(15));
                            if (!model.isBadgeShowed()) {
                                model.setBadgeTitle(title);
                                model.showBadge();
                            } else model.updateBadgeTitle(title);
                        }
                    }, i * 100);
                }

                coordinatorLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Snackbar snackbar = Snackbar.make(navigationTabBar, "Coordinator NTB", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
                        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                                .setTextColor(Color.parseColor("#423752"));
                        snackbar.show();
                    }
                }, 1000);
            }
        });
         */
        
	}
	 //上次按下返回键的系统时间  
    private long lastBackTime = 0;  
    //当前按下返回键的系统时间  
    private long currentBackTime = 0;  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        //捕获返回键按下的事件  
        if(keyCode == KeyEvent.KEYCODE_BACK){  
            //获取当前系统时间的毫秒数  
            currentBackTime = System.currentTimeMillis();  
            //比较上次按下返回键和当前按下返回键的时间差，如果大于1.5秒，则提示再按一次退出  
            if(currentBackTime - lastBackTime > 1.5 * 1000){  
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();  
                lastBackTime = currentBackTime;  
            }else{ //如果两次按下的时间差小于2秒，则退出程序  
                finish();  
            }  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
	
    //we need the outState to memorize the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    
    //==================================================================================
    
    public class TabFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
            
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
        
        @Override
        public int getCount() {
            return mFragments.size();
        }

        
    	@Override
    	public void destroyItem(ViewGroup container, int position, Object object) {
    		//Log.i("destroyItem", "destroyItem");
    		super.destroyItem(container, position, object);
    	}

    	@Override
    	public Object instantiateItem(ViewGroup arg0, int arg1) {
    		return super.instantiateItem(arg0, arg1);
    	}
        
        
    }
	

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt.setText(String.format("Navigation Item #%d", position));
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txt;
            public ViewHolder(final View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
                
	    		RxView.clicks(txt)
	        	.throttleFirst(1, TimeUnit.SECONDS)
	        	.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object arg0) throws Exception {
						Intent intent = new Intent(MainActivity.this,ComicDirectoryPage.class);
						intent.putExtra("SpecialType", false);
						intent.putExtra("ImageUrl", "http://1.ci.cdvcdn.com/img/oQ1QWR9yG.jpg");
						intent.putExtra("Name", "測試");
						intent.putExtra("Url", "http://comic.ck101.com/comic/14648");
						
						startActivity(intent);
					}
				});
				
            }
        }
    }
	
}
