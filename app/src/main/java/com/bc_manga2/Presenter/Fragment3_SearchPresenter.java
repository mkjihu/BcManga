package com.bc_manga2.Presenter;

import java.util.ArrayList;

import org.reactivestreams.Subscription;

import com.bc_manga2.Adapder.SortAdapder;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Application.GivenHttp;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Fragment.Fragment3_Search;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Sort.AS_Ck101;
import com.bc_manga2.Resolve.Sort.AS_k886;
import com.bc_manga2.Resolve.Sort.ItemSort;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber; 
public class Fragment3_SearchPresenter  extends BasePresenter<Fragment3_Search>{

	public Fragment3_SearchPresenter(Fragment3_Search view) {
		super(view);
	}

	/**取得分類*/
	public void GetSortAdapder()
	{
		Disposable d = Flowable.just(Shared.GetHomeUrlkey(getView().getContext()))
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-解析資料中
						getView().openRefresh();
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.flatMap(new Function<String, Flowable<String>>() {
					@Override
					public Flowable<String> apply(String arg0) throws Exception {
						switch (arg0) {
						case "ck101":
							return HttpApiClient.getHtml(GivenHttp.ck101);		
						case "k886":
							return Flowable.just(arg0);
						default:
							return HttpApiClient.getHtml(GivenHttp.ck101);
						}
					}
				})
				.map(new Function<String, Adapter<ViewHolder>>() {

					@Override
					public Adapter<ViewHolder> apply(String Html) throws Exception {
						String HomePK = Shared.GetHomeUrlkey(getView().getContext());
						ArrayList<ItemSort> itemSorts = new ArrayList<>();
						Log.i("GGs", HomePK);
						switch (HomePK) {
						case "ck101":
							itemSorts = new AS_Ck101(Html, HomePK, GivenHttp.ck101).GetSortItemArrary();
							break;
						case "k886":
							itemSorts = new AS_k886(HomePK).SortItem();
							break;
						default:
							itemSorts = new AS_Ck101(Html, HomePK, GivenHttp.ck101).GetSortItemArrary();
							break;
						}
						return new SortAdapder(getView(), itemSorts);
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截 - 使用該攔截 一定會進入onNext()
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  */
				.onErrorReturn(new Function<Throwable, Adapter<ViewHolder>>() {
					@Override
					public Adapter<ViewHolder> apply(Throwable arg0) throws Exception {
						Log.i("GG", arg0.getMessage()+BcApplication.isInMainThread());
						throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						//return null;
					}
				})
				//.onBackpressureBuffer()//加上背压策略 - 無限大
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.subscribeWith(new DisposableSubscriber<Adapter<ViewHolder>>() { 
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
						getView().offRefresh();
					}
					@Override
					public void onNext(Adapter<ViewHolder> arg0) {
						getView().RecyclerViewsetData(arg0);
						getView().offRefresh();
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
}
