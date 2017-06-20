package com.bc_manga2.Presenter;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscription;

import com.bc_manga2.ComicDirectoryPage;
import com.bc_manga2.Adapder.IndexAdapder;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Model.Cminfo;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Index.AI_Ck101;
import com.bc_manga2.Resolve.Index.BI_Ck101;
import com.bc_manga2.Resolve.Index.BI_k886;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.bc_manga2.obj.ToastUnity;
import com.bc_manga2.obj.UnicodeDecoder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import de.greenrobot.BcComicdao.BcIndexData;
import de.greenrobot.BcComicdao.BcIndexDataDao;
import de.greenrobot.BcComicdao.BcIndexDataDao.Properties;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ComicDirectoryPresenter extends BasePresenter<ComicDirectoryPage>{

	public ComicDirectoryPresenter(ComicDirectoryPage view) {
		super(view);
	}
	/**
	 * @param Url 連接
	 * @param interpret 是否二度解析
	 */
	public void GetIndexDate(final String HomePK,final String Url,final boolean interpret)
	{
		Disposable d = isMobile(HomePK,Url)
				
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
				.flatMap(new Function<String, Flowable<String>>() {
					@Override
					public Flowable<String> apply(String Html) throws Exception {
						//--由interpret 決定是否要取得正確資料Url
						//Log.i("跑2", "跑2"+Html);
						switch (HomePK) {//--判定主站URL
						case "ck101":
							return new AI_Ck101("http://comic.ck101.com",Html, interpret).GetHtml();
						case "k886":
							return Flowable.just(Html);//-直接傳出回傳資料
						default:
							return new AI_Ck101("http://comic.ck101.com",Html, interpret).GetHtml();
						}
					}
				})
				.concatMap(new Function<String, Flowable<ArrayList<ItemComicIndex>>>() {
					@Override
					public Flowable<ArrayList<ItemComicIndex>> apply(String Html) throws Exception {
						//-這裡取得正確資料得HTML-並且下傳要抓取所有項目的Urls
						
						//Log.i("正確資料", "正確資料2"+Html);
						switch (HomePK) {
						case "ck101":
							BI_Ck101 b = new BI_Ck101(Html,Url,"ck101", interpret); 
							return b.CC(new SetInfo());
						case "k886":
							BI_k886 bi_k886 = new BI_k886(Html,Url, "k886", interpret);
							return bi_k886.CC(new SetInfo());
						default:
							return null;
						}
					}
				})
				.observeOn(Schedulers.io())//--在線程中  
				.map(new Function<ArrayList<ItemComicIndex>, IndexAdapder>() {
					@Override
					public IndexAdapder apply(ArrayList<ItemComicIndex> comicIndexs) throws Exception {
						Log.i("項目", comicIndexs.size()+"");	
						/*
						Log.i("項目", comicIndexs.size()+"");	
						Log.i("項目",  comicIndexs.get(488).getItemName()+"_"+comicIndexs.get(488).getItemUrl());	
						for (int i = 0; i < comicIndexs.size(); i++) {
							Log.i("項目"+i, comicIndexs.get(i).getItemName());	
							//Log.i("項目-"+i, comicIndexs.get(i).getItemUrl());
						}
						*/
						return new IndexAdapder(getView(), comicIndexs);
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  *//**/
				.observeOn(Schedulers.io())//--在線程中  
				.onErrorResumeNext(new Function<Throwable, Flowable<IndexAdapder>>() {
					@Override
					public Flowable<IndexAdapder> apply(Throwable arg0) throws Exception {
						Log.i("出错C", arg0.getMessage());
						return ErrorState(Url, new SetInfo());
					}
				 })
				//.onBackpressureBuffer()//加上背压策略 - 無限大
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
			   	.subscribeWith(new DisposableSubscriber<IndexAdapder>() {
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
						ToastUnity.ShowTost(getView(), arg0.getMessage());
						getView().offRefresh();
					}
					@Override
					public void onNext(IndexAdapder adapter) {
						getView().RecyclerViewsetData(adapter);
						getView().offRefresh();
					}
					@Override
					public void onComplete() {}
				});
		
		getDisposable().add(d);
	}
	
	/**錯誤狀態處理
	 * 傳入加載URL去DB找
	 * @return 
	 * */
	public Flowable<IndexAdapder> ErrorState(String Url,Function<BcIndexData, String> map)
	{
		return Flowable.just(Url)
				.observeOn(Schedulers.io())
				.map(new Function<String, BcIndexData>() {
					@Override
					public BcIndexData apply(String Url) throws Exception {
						BcIndexDataDao bcIndexDataDao = BcApplication.getInstance().getDaoSession().getBcIndexDataDao();
						BcIndexData bcIndexData = bcIndexDataDao.queryBuilder().where(Properties.PKUrl.eq(Url)).unique();
						if (bcIndexData !=null) {
							return bcIndexData;
						}else {
							throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						}
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--在主線程中顯示  
				.map(map)//中間如果找到資料就先傳出去
				.observeOn(Schedulers.io())
				.flatMap(new Function<String, Flowable<IndexAdapder>>() {
					@Override
					public Flowable<IndexAdapder> apply(String arg0) throws Exception {
						return Flowable.just(arg0)
								.map(new Function<String, IndexAdapder>() {
									@Override
									public IndexAdapder apply(String arg0) throws Exception {
										ArrayList<ItemComicIndex> comicIndexs = new Gson().fromJson(arg0, new TypeToken<ArrayList<ItemComicIndex>>(){}.getType());//先取出資料
										return new IndexAdapder(getView(), comicIndexs);//先取出資料
									}
								});
					}
				});
	}
	
	
	
	
	public class SetInfo implements Function<BcIndexData, String> {
		@Override
		public String apply(BcIndexData arg0) throws Exception {
			getView().SetInfoDate(arg0);
			//-RxJava2 禁止傳入null參數 所以這理要攔截
			if (arg0.getItemArrayGSON()==null) {
				return "";
			} 
			return arg0.getItemArrayGSON();
		}
	}
	/**分門別類*/
	public Flowable<String> isMobile(String Homekey,String Url)
	{
		switch (Homekey) {
		case "ck101":
			return HttpApiClient.getInstance().GetHtml_F(Url);
		case "k886":
			return HttpApiClient.getInstance().GetHtml_F(Url);
		case "wnacg":
			return HttpApiClient.getInstance().GetHtml2_F(Url,BcApplication.getInstance().getUserAgent());
		default:
			return HttpApiClient.getInstance().GetHtml_F(Url);
		}
	}
	
	//============k88 有API試寫==============
	public void GetIndexDate_Api(final String Url,final String id) {

		Disposable s = Flowable
							.range(1, Integer.MAX_VALUE)
							.subscribeOn(Schedulers.io())
							.concatMap(new Function<Integer, Flowable<String>>() {
								@Override
								public Flowable<String> apply(Integer arg0) throws Exception {
									String murl = String.format(Url, id)+arg0;//串API URL
									
									if (arg0==1) { //-如果是第一頁就先取基本資料
										return HttpApiClient.getInstance()
												.GetHtml_F(murl)
												.subscribeOn(Schedulers.io())
												.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
												.map(new Function<String, String>() {
													@Override
													public String apply(String arg0) throws Exception {
														String okstr = UnicodeDecoder.decode(arg0);//--轉Unicode碼
														Cminfo cminfo = new Gson().fromJson(okstr, Cminfo.class);
														
														/*
														 *
														 * 
														 * 未完成
														 * 
														 * 
														 */
																												
														return arg0;
													}
												});
									}
									return HttpApiClient.getInstance().GetHtml_F(murl);
								}
							})
							/**///--方法1  TakeWhile发射原始Observable，直到你指定的某个条件不成立的那一刻，它停止发射原始Observable，并终止自己的Observable。
							.takeWhile(new Predicate<String>() {
								@Override
								public boolean test(String arg0) throws Exception {
									String okstr = UnicodeDecoder.decode(arg0);//--轉Unicode碼
									Cminfo cminfo = new Gson().fromJson(okstr, Cminfo.class);
									if (cminfo.getmSelectArray() != null) {
										return true;
									} else { //-回传资料条件不成立---停止
										return false;
									}
								}
							})
							.toList().toFlowable()
							.map(new Function<List<String>, String>() {
								@Override
								public String apply(List<String> arg0) throws Exception {
									
									for (int i = 0; i < arg0.size(); i++) {
										String okstr = UnicodeDecoder.decode(arg0.get(i));//--轉Unicode碼
										Cminfo cminfo = new Gson().fromJson(okstr, Cminfo.class);
										for (int j = 0; j < cminfo.getmSelectArray().size(); j++) {
											Log.i("OK", cminfo.getmSelectArray().get(j).getName());
										}
									}
									return  arg0.size()+"";
								}
							})
							/**
							  * 錯誤或是沒有網路時攔截
							  * onErrorReturn()，在遇到错误时发射一个特定的数据
							  * onErrorResumeNext()，在遇到错误时发射一个数据序列
							  */
							 .onErrorReturn(new Function<Throwable, String>() {
								@Override
								public String apply(Throwable arg0) throws Exception {
									return arg0.getMessage();
								}
							})
							.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
							.unsubscribeOn(Schedulers.io())//允許取消訂閱  
							.subscribeWith(new DisposableSubscriber<String>() {
								@Override
								public void onError(Throwable arg0) {
								}
								
								@Override
								public void onNext(String arg0) {
									
								}
								@Override
								public void onComplete() {
									
								}
					        });

				 
		getDisposable().add(s);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//===========================收藏==================================
	
	public void KeepData(String  Index_PKUrl)
	{
		Disposable d = Observable.just(Index_PKUrl)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.map(new Function<String, Boolean>() {
					@Override
					public Boolean apply(String Index_PKUrl) throws Exception {
						BcIndexDataDao bcIndexDataDao = BcApplication.getInstance().getDaoSession().getBcIndexDataDao(); 
						BcIndexData bcIndexData = bcIndexDataDao.queryBuilder().where(Properties.PKUrl.eq(Index_PKUrl)).unique();
						if (bcIndexData.getIsCollect()) {//--如果已收藏就取消
							bcIndexData.setIsCollect(false);
						} else {
							bcIndexData.setIsCollect(true);
						}
						bcIndexDataDao.update(bcIndexData);
						return bcIndexData.getIsCollect();
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.subscribeWith(new DisposableObserver<Boolean>() {
					@Override
					public void onError(Throwable arg0) { 
						Log.i("GG2G", arg0.getMessage());
					}
					@Override
					public void onNext(Boolean arg0) {
						Log.i("開始收藏", arg0+"");
						getView().SetKeepChecked(arg0);
					}
					@Override
					public void onComplete() {}
				});
		getDisposable().add(d);
	}
	
	//===========================當前索引==================================
	public void Control(String Index_PKUrl,final int target)
	{
		Disposable d = Observable.just(Index_PKUrl)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.map(new Function<String, String>() {
					@Override
					public String apply(String Index_PKUrl) throws Exception {

						BcIndexDataDao bcIndexDataDao = BcApplication.getInstance().getDaoSession().getBcIndexDataDao(); 
						BcIndexData bcIndexData = bcIndexDataDao.queryBuilder().where(Properties.PKUrl.eq(Index_PKUrl)).unique();
						bcIndexData.setFocusItem(target);
						bcIndexDataDao.update(bcIndexData);
						
						return "";
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
			   	.subscribeWith(new DisposableObserver<String>() {
					@Override
					public void onError(Throwable arg0) { 
						Log.i("GG2G", arg0.getMessage());
					}
					@Override
					public void onNext(String arg0) {
						
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
}
