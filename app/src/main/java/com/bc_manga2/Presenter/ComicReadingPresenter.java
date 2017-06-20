package com.bc_manga2.Presenter;

import java.util.ArrayList;

import org.reactivestreams.Subscription;

import com.bc_manga2.ComicReadingPage;
import com.bc_manga2.Adapder.BaseReadingAdapder;
import com.bc_manga2.Adapder.BaseReadingAdapder2;
import com.bc_manga2.Adapder.Reading1Adapder;
import com.bc_manga2.Adapder.ReadingRecyc1Adapder;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Image.AG_Ck101;
import com.bc_manga2.Resolve.Image.AG_k886;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken; 
import android.util.Log;
import de.greenrobot.BcComicdao.BcImageData;
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

public class ComicReadingPresenter  extends BasePresenter<ComicReadingPage>{

	public ComicReadingPresenter(ComicReadingPage view) {
		super(view);
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
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
	
	/**
	 * 取圖--橫向
	 * @param HomePK
	 * @param comicIndex
	 * @param interpret 是否重新加載資料
	 */
	public void GetImageList(String HomePK, ItemComicIndex comicIndex,boolean isReg) {
		Disposable d  = General(HomePK, comicIndex,isReg)
				.map(new Function<BcImageData, BaseReadingAdapder>() { 
					@Override
					public BaseReadingAdapder apply(BcImageData bcImageData) throws Exception {
						//-這裡取得bcImageData資料
						ArrayList<String> urls;
						if (!bcImageData.getItemReArrayGSON().equals("")) {
							urls =  new Gson().fromJson(bcImageData.getItemReArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						else {
							urls =  new Gson().fromJson(bcImageData.getItemArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						return new Reading1Adapder(getView(), urls);
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  */
				 .onErrorReturn(new Function<Throwable, BaseReadingAdapder>() { 
					@Override
					public BaseReadingAdapder apply(Throwable arg0) throws Exception {
						Log.i("GG", arg0.getMessage());
						throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						//return "";
					}
				 })
				 .onBackpressureBuffer()//加上背压策略 - 無限大
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.subscribeWith(new DisposableSubscriber<BaseReadingAdapder>() { 
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG1", arg0.getMessage());
					}
					@Override
					public void onNext(BaseReadingAdapder arg0) {
						//---關閉加載
						getView().colLoad();
						getView().OPamin();
						getView().SetViewPageData(arg0);
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
	/**
	 * 取圖--直向
	 * @param HomePK
	 * @param comicIndex
	 * @param interpret 是否重新加載資料
	 */
	public void GetImageList2( String HomePK, ItemComicIndex comicIndex,boolean isReg) {
		Disposable d  = General(HomePK, comicIndex,isReg)
				.map(new Function<BcImageData, BaseReadingAdapder2>() { 
					@Override
					public BaseReadingAdapder2 apply(BcImageData bcImageData) throws Exception {
						//-這裡取得bcImageData資料
						ArrayList<String> urls;
						if (!bcImageData.getItemReArrayGSON().equals("")) {
							urls =  new Gson().fromJson(bcImageData.getItemReArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						else {
							urls =  new Gson().fromJson(bcImageData.getItemArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						return new ReadingRecyc1Adapder(getView(), urls);
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  */
				 .onErrorReturn(new Function<Throwable, BaseReadingAdapder2>() { 
					@Override
					public BaseReadingAdapder2 apply(Throwable arg0) throws Exception {
						Log.i("GG", arg0.getMessage());
						throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						//return "";
					}
				 })
				 .onBackpressureBuffer()//加上背压策略 - 無限大
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.subscribeWith(new DisposableSubscriber<BaseReadingAdapder2>() { 
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG2", arg0.getMessage());
					}
					@Override
					public void onNext(BaseReadingAdapder2 arg0) {
						//---關閉加載
						getView().colLoad();
						getView().OPamin();
						getView().SetRecyclerViewData(arg0);
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
	///---直向橫向通用前段取資料---
	public Flowable<BcImageData>  General(final String HomePK,final ItemComicIndex comicIndex,final boolean isReg)
	{
		return HttpApiClient.getInstance().GetHtml_F(comicIndex.getItemUrl())
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-等于强制刷新资料库
						
						
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.flatMap(new Function<String, Flowable<BcImageData>>() { 
					@Override
					public Flowable<BcImageData> apply(String Html) throws Exception {
						//--由interpret 決定是否要取得正確資料Url
						switch (HomePK) {
						case "ck101":
							AG_Ck101 ag1 = new AG_Ck101(Html, comicIndex, true);

							return ag1.GetBcImageData();
						case "k886":
							AG_k886 ag_k886 = new AG_k886(Html, comicIndex,isReg);

							return ag_k886.GetBcImageData();
							
						default:
							AG_Ck101 ag2 = new AG_Ck101(Html, comicIndex, true);
							return ag2.GetBcImageData();
						}
					}
				});
		
	}
	
	
	
	//==載入下一話或上一話==
	/**
	 * 
	 * @param alist 全部話
	 * @param target 當前
	 * @param NoL 往前或往后
	 */
	public void NextorLast(final String HomePK, ArrayList<ItemComicIndex> alist,final int target,final String NoL)
	{
		Disposable d  = Flowable.just(alist)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-解析資料中
						getView().opLoad();
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
				.map(new Function<ArrayList<ItemComicIndex>, ItemComicIndex>() {
					@Override
					public ItemComicIndex apply(ArrayList<ItemComicIndex> arrayList) throws Exception {
						switch (NoL)
						{
						case "N": //-下1
							if (target < (arrayList.size()-1)) {//有下一
								int N=target+1;
								getView().DBNextorLast(N);
								return arrayList.get(N);
							} else {
								throw Exceptions.propagate(new RuntimeException("没有下一话"));
							}
						case "L"://-上1
							if (target > 0) {//有上一
								int L=target-1;
								getView().DBNextorLast(L);
								return arrayList.get(L);
							} else {
								throw Exceptions.propagate(new RuntimeException("没有上一话"));
							}
						}
						return null;
					}
				})
				.observeOn(Schedulers.io())//--在線程中  
				.flatMap(new Function<ItemComicIndex, Flowable<BcImageData>>() {

					@Override
					public Flowable<BcImageData> apply(ItemComicIndex arg0) throws Exception {
						return General(HomePK, arg0,false);//--尽量不要再次加载
					}
				})
				.map(new Function<BcImageData, ArrayList<String> >() {

					@Override
					public ArrayList<String> apply(BcImageData bcImageData) throws Exception {
						ArrayList<String> urls;
						if (!bcImageData.getItemReArrayGSON().equals("")) {
							urls =  new Gson().fromJson(bcImageData.getItemReArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						else {
							urls =  new Gson().fromJson(bcImageData.getItemArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						return urls;
					}
				})
				
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
			   	.subscribeWith(new DisposableSubscriber<ArrayList<String>>() {
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
					}
					@Override
					public void onNext(ArrayList<String> urls) {
						//---關閉加載
						getView().colLoad();
						//--是否橫向加載
						if (Shared.GetReadingMethod(getView())) {
							getView().NextorLast(new Reading1Adapder(getView(), urls), NoL);
						} else {
							getView().NextorLast(new ReadingRecyc1Adapder(getView(), urls),NoL);
						}
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
	
}
