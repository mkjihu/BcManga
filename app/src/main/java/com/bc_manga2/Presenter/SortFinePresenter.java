package com.bc_manga2.Presenter;

import java.util.ArrayList;
import org.reactivestreams.Subscription;
import com.bc_manga2.Activity.SortFinePage;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Application.GivenHttp;
import com.bc_manga2.Model.Category;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.SortFine.ASF_Ck101;
import com.bc_manga2.Resolve.SortFine.SortFineItem;
import com.bc_manga2.obj.ToastUnity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SortFinePresenter extends BasePresenter<SortFinePage> {

	public int i = 1;
	public SortFinePresenter(SortFinePage view) {
		super(view);
		i = 1;//-重設
	}

	/** 
	 * @param HomePK 主站識別PK
	 * @param ItemUrl 細項頁Url
	 */
	public void GetSortFineArray1(final String HomePK,final String ItemUrl)
	{
		Disposable d = isMobile(HomePK,ItemUrl)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-等于强制刷新资料库
						getView().openRefresh();
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.observeOn(Schedulers.io())//--在線程中  
				.map(new Function<String,ArrayList<SortFineItem>>() { 
					@Override
					public ArrayList<SortFineItem> apply(String Html) throws Exception {
						switch (HomePK) {
						case "ck101":
							ASF_Ck101 ch101 = new ASF_Ck101(Html, HomePK);
							String NextUrl = ch101.NextUrl();
							getView().getNextUrl(NextUrl);
							return ch101.GetSortFineItemArray();
						case "k886":
							
							ArrayList<Category> arrayList =new Gson().fromJson(Html, new TypeToken<ArrayList<Category>>(){}.getType());
							ArrayList<SortFineItem> sortFineItems = new ArrayList<>();
							
							for (int i = 0; i < arrayList.size(); i++) {
								
								String Url = String.format(GivenHttp.k886_Index, arrayList.get(i).getmId());
								
								SortFineItem sortFineItem = new SortFineItem(arrayList.get(i).getmIconUrl()
																		, arrayList.get(i).getmName()
																		, Url
																		, false);
								sortFineItem.setHomePK(HomePK);
								sortFineItem.setUpDate(arrayList.get(i).getmDate());
								sortFineItem.setNewAion(arrayList.get(i).getmNum());
								sortFineItems.add(sortFineItem);
							}
							
							getView().getNextUrl(ItemUrl);
							return sortFineItems;
						case "wnacg":
							
							return null;
						default:
							return null;
						}
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截 - 使用該攔截 一定會進入onNext()
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  */
				.onErrorReturn(new Function<Throwable, ArrayList<SortFineItem>>() { 
					@Override
					public ArrayList<SortFineItem> apply(Throwable arg0) throws Exception {
						Log.i("GG", arg0.getMessage());
						throw Exceptions.propagate(new RuntimeException("解析錯誤，網路異常或是無法連結網路"));
						//return null;
					}
				})
				//.onBackpressureBuffer()//加上背压策略 - 無限大
				.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
				.subscribeWith(new DisposableSubscriber<ArrayList<SortFineItem>>() { 
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
						ToastUnity.ShowTost(getView(), arg0.getMessage());
					}
					@Override
					public void onNext(ArrayList<SortFineItem> arg0) {
						getView().OneData(arg0);
					}
					@Override
					public void onComplete() {
						getView().offRefresh();
					}
				});
		getDisposable().add(d);	
	}
	
	
	/**分門別類*/
	public Flowable<String> isMobile(String Homekey,String Url)
	{
		switch (Homekey) {
		case "ck101":
			return HttpApiClient.getInstance().GetHtml_F(Url);
		case "k886":
			String Url2 = String.format(Url, 1);
			return HttpApiClient.getInstance().GetHtml_F(Url2);
		case "wnacg":
			return HttpApiClient.getInstance().GetHtml2_F(Url,BcApplication.getInstance().getUserAgent());
		default:
			return HttpApiClient.getInstance().GetHtml_F(Url);
		}
	}
	
	
	/**
	 * 上拉加載更多
	 * @param HomePK
	 * @param ItemUrl
	 */
	public void GetSortFineArray2(final String HomePK,String NextUrl,final ArrayList<SortFineItem> fineItems)
	{
		Disposable d = isMobile2(HomePK,NextUrl)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-等于强制刷新资料库
						//getView().openRefresh();
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.observeOn(Schedulers.io())//--在線程中  
				.map(new Function<String,ArrayList<SortFineItem>>() { 
					@Override
					public ArrayList<SortFineItem> apply(String Html) throws Exception {
						
						switch (HomePK) {
						case "ck101":
							ASF_Ck101 ch101 = new ASF_Ck101(Html, HomePK);
							String NextUrl = ch101.NextUrl();
							getView().getNextUrl(NextUrl);
							ArrayList<SortFineItem> NewArray = fineItems;
							NewArray.addAll(ch101.GetSortFineItemArray());
							return NewArray;
							
						case "k886":
							ArrayList<Category> arrayList =new Gson().fromJson(Html, new TypeToken<ArrayList<Category>>(){}.getType());
							ArrayList<SortFineItem> sortFineItems = new ArrayList<>();
							for (int i = 0; i < arrayList.size(); i++) {
								String Url = String.format(GivenHttp.k886_Index, arrayList.get(i).getmId());
								SortFineItem sortFineItem = new SortFineItem(arrayList.get(i).getmIconUrl()
																		, arrayList.get(i).getmName()
																		, Url
																		, false);
								sortFineItem.setHomePK(HomePK);
								sortFineItem.setUpDate(arrayList.get(i).getmDate());
								sortFineItem.setNewAion(arrayList.get(i).getmNum());
								sortFineItems.add(sortFineItem);
							}
							return sortFineItems;
						case "wnacg":
							
							return null;
						default:
							return null;
						}
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截 - 使用該攔截 一定會進入onNext()
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  */
				.onErrorReturn(new Function<Throwable, ArrayList<SortFineItem>>() { 
					@Override
					public ArrayList<SortFineItem> apply(Throwable arg0) throws Exception {
						//Log.i("GG", arg0.getMessage());
						throw Exceptions.propagate(new RuntimeException("解析錯誤，網路異常或是無法連結網路"));
						//return null;
					}
				})
				//.onBackpressureBuffer()//加上背压策略 - 無限大
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.subscribeWith(new DisposableSubscriber<ArrayList<SortFineItem>>() { 
					@Override
					public void onError(Throwable arg0) {
						ToastUnity.ShowTost(getView(), arg0.getMessage());
						getView().offLoadMore(false);
					}
					@Override
					public void onNext(ArrayList<SortFineItem> arg0) {
						getView().NextData(arg0);
						getView().offLoadMore(true);
					}
					@Override
					public void onComplete() {}
				});
		getDisposable().add(d);	
	}
	
	/**分門別類*/
	public Flowable<String> isMobile2(String Homekey,String Url)
	{
		switch (Homekey) {
		case "ck101":
			return HttpApiClient.getInstance().GetHtml_F(Url);
		case "k886":
			String Url2 = String.format(Url, i++);
			return HttpApiClient.getInstance().GetHtml_F(Url2);
		case "wnacg":
			return HttpApiClient.getInstance().GetHtml2_F(Url,BcApplication.getInstance().getUserAgent());
		default:
			return HttpApiClient.getInstance().GetHtml_F(Url);
		}
	}
}
