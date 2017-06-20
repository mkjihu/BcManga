package com.bc_manga2.Presenter;

import java.util.List;

import org.reactivestreams.Subscription;

import com.bc_manga2.Adapder.Sousuo1Adapder;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Fragment.Fragment2_Collection;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import de.greenrobot.BcComicdao.BcIndexData;
import de.greenrobot.BcComicdao.BcIndexDataDao;
import de.greenrobot.BcComicdao.BcIndexDataDao.Properties;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber; 

public class Fragment2_CollectionPresenter  extends BasePresenter<Fragment2_Collection> {

	public Fragment2_CollectionPresenter(Fragment2_Collection view) {
		super(view);
		// TODO Auto-generated constructor stub
	}
	
	
	public void GetCollAdapder()
	{
		Disposable d = Observable.just("")
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Disposable>() {
					@Override
					public void accept(Disposable arg0) throws Exception {
						//显示加载-解析資料中
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.map(new Function<String,Adapter<ViewHolder>>() {
					@Override
					public Adapter<ViewHolder> apply(String arg0) throws Exception {
						BcIndexDataDao bcIndexDataDao = BcApplication.getInstance().getDaoSession().getBcIndexDataDao();
						List<BcIndexData> bcIndexDatas = bcIndexDataDao.queryBuilder().where(Properties.IsCollect.eq(true)).orderDesc(Properties.ClickDate).limit(200).list();//-前200條
						return new Sousuo1Adapder(getView(), bcIndexDatas);
					}
				})
				.onErrorReturn(new Function<Throwable, Adapter<ViewHolder>>() {
					@Override
					public Adapter<ViewHolder> apply(Throwable arg0) throws Exception {
						Log.i("GG", arg0.getMessage());
						throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						//return null;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.subscribeWith(new DisposableObserver<Adapter<ViewHolder>>() { 
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", "GG");
					}
					@Override
					public void onNext(Adapter<ViewHolder> arg0) {
						//Log.i("刷新閱讀紀錄", "刷新閱讀紀錄");
						getView().RecyclerViewsetData(arg0);
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);	
	}
}
