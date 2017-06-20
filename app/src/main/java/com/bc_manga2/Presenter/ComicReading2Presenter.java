package com.bc_manga2.Presenter;

import java.util.ArrayList;

import org.reactivestreams.Subscription;

import com.bc_manga2.Activity.ComicReading2Page;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Fragment.Fragment_ComicItem;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;
import de.greenrobot.BcComicdao.BcIndexData;
import de.greenrobot.BcComicdao.BcIndexDataDao;
import de.greenrobot.BcComicdao.BcIndexDataDao.Properties;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ComicReading2Presenter extends BasePresenter<ComicReading2Page>{

	public ComicReading2Presenter(ComicReading2Page view) {
		super(view);
		// TODO Auto-generated constructor stub
	}
	/**初次進入判定DB資料呼叫*/
	public void GetDB(String Index_PKUrl,final String Image_PKUrl) {
		Disposable d  = Flowable.just(Index_PKUrl)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-解析資料中
						getView().opLoad();
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.map(new Function<String, ArrayList<ItemComicIndex>>() { 
					@Override
					public ArrayList<ItemComicIndex> apply(String Index_PKUrl) throws Exception {
						BcIndexDataDao bcIndexDataDao = BcApplication.getInstance().getDaoSession().getBcIndexDataDao(); 
						BcIndexData bcIndexData = bcIndexDataDao.queryBuilder().where(Properties.PKUrl.eq(Index_PKUrl)).unique();
						ArrayList<ItemComicIndex> alist = new Gson().fromJson(bcIndexData.getItemArrayGSON(), new TypeToken<ArrayList<ItemComicIndex>>(){}.getType());	
						return alist;
					}
				})
				.map(new Function<ArrayList<ItemComicIndex>, ArrayList<ItemComicIndex>>() {  
					@Override
					public ArrayList<ItemComicIndex> apply(ArrayList<ItemComicIndex> arg0) throws Exception {
						getView().GetDBArrayData(arg0);
						return arg0;
					}
				})
				.map(new Function<ArrayList<ItemComicIndex>,Integer>() { 
					@Override
					public Integer apply(ArrayList<ItemComicIndex> alist) throws Exception {
						//return alist.indexOf(Image_PKUrl);//匹配成功則傳回該元素所在位置的索引，失敗則傳回-1
						for (int i = 0; i < alist.size(); i++) {
							if (alist.get(i).getItemUrl().equals(Image_PKUrl)) {
								return i;
							}
						}
						return null;
					}
				})
				.onErrorReturn(new Function<Throwable, Integer>() {
					@Override
					public Integer apply(Throwable arg0) throws Exception {
						Log.i("GG0", arg0.getMessage());
						throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						//return null;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
			   	.subscribeWith(new DisposableSubscriber<Integer>() {
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
					}
					@Override
					public void onNext(Integer arg0) {
						getView().GetDBtarget(arg0);
					}
					@Override
					public void onComplete() {
						
					}
				});
		getDisposable().add(d);
	}
	
	public void GetImageList(ArrayList<ItemComicIndex> arrayList) {
		Disposable d  = Flowable.just(arrayList)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.map(new Function<ArrayList<ItemComicIndex>, ArrayList<Fragment_ComicItem>>() { 
					@Override
					public ArrayList<Fragment_ComicItem> apply(ArrayList<ItemComicIndex> arg0) throws Exception {
						ArrayList<Fragment_ComicItem> mFragments = new ArrayList<>();
						for (int i = 0; i < arg0.size(); i++) {
							mFragments.add(new Fragment_ComicItem(arg0.get(i)));
						}
						return mFragments;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
			   	.subscribeWith(new DisposableSubscriber<ArrayList<Fragment_ComicItem>>() {
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
					}
					@Override
					public void onNext(ArrayList<Fragment_ComicItem> arg0) {
						
					}
					@Override
					public void onComplete() {
						getView().colLoad();
					}
				});
		getDisposable().add(d);
	}
	
}
