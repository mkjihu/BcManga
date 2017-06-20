package com.bc_manga2.Presenter;

import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.reactivestreams.Subscription;
import com.bc_manga2.Adapder.BaseReadingAdapder;
import com.bc_manga2.Adapder.Reading2Adapder;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Fragment.Fragment_ComicItem;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Image.AG_Ck101;
import com.bc_manga2.Resolve.Image.AG_k886;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import de.greenrobot.BcComicdao.BcImageData;
import de.greenrobot.BcComicdao.BcImageDataDao;
import de.greenrobot.BcComicdao.BcIndexDataDao.Properties;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class Fragment_ComicItemPresenter  extends BasePresenter<Fragment_ComicItem>  {

	public Fragment_ComicItemPresenter(Fragment_ComicItem view) {
		super(view);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 初次進入判定DB資料呼叫
	 */
	public void GetDB(final ItemComicIndex itemComicIndex) {
		Disposable d  = Flowable.just(itemComicIndex)
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//显示加载-解析資料中
						getView().openRefresh();
					}
				})
				.flatMap(new Function<ItemComicIndex, Flowable<ArrayList<String>>>() {
					@Override
					public Flowable<ArrayList<String>> apply(ItemComicIndex comicIndex) throws Exception {
						BcImageDataDao bcImageDataDao = BcApplication.getInstance().getDaoSession().getBcImageDataDao();
						BcImageData bcImageData  = bcImageDataDao.queryBuilder().where(Properties.PKUrl.eq(comicIndex.getItemUrl())).unique();
						
						if (bcImageData==null||UpdateTimeContrast(bcImageData)) {//-無資料或是強制更新
							return GetImageA(comicIndex);
						}
						else{
							return GetImageB(bcImageData);
						}
					} 
				})
				.map(new Function<ArrayList<String>, BaseReadingAdapder>() {
					@Override
					public BaseReadingAdapder apply(ArrayList<String> urls) throws Exception {
						return new Reading2Adapder(getView().getContext(), urls);
					}
				})
				.onErrorReturn(new Function<Throwable, BaseReadingAdapder>() {
					@Override
					public BaseReadingAdapder apply(Throwable arg0) throws Exception {
						Log.i("GG0", arg0.getMessage());
						//throw Exceptions.propagate(new RuntimeException("解析錯誤"));
						BcImageDataDao bcImageDataDao = BcApplication.getInstance().getDaoSession().getBcImageDataDao();
						BcImageData bcImageData  = bcImageDataDao.queryBuilder().where(Properties.PKUrl.eq(itemComicIndex.getItemUrl())).unique();
						ArrayList<String> urls =  new Gson().fromJson(bcImageData.getItemReArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						return new Reading2Adapder(getView().getContext(), urls);
					}
				})
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
			   	.unsubscribeOn(Schedulers.io())//允許取消訂閱  
			   	.subscribeWith(new DisposableSubscriber<BaseReadingAdapder>() {
					@Override
					public void onError(Throwable arg0) {
						Log.i("GG", arg0.getMessage());
					}
					@Override
					public void onNext(BaseReadingAdapder arg0) {
						//5
						getView().offRefresh();
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);
	}
	
	public Flowable<ArrayList<String>> GetImageA(final ItemComicIndex comicIndex) {
		return HttpApiClient.getInstance().GetHtml_F(comicIndex.getItemUrl())
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				.flatMap(new Function<String, Flowable<BcImageData>>() { 
					@Override
					public Flowable<BcImageData> apply(String Html) throws Exception {
						
						switch (comicIndex.getHomePK()) {
						case "ck101":
							AG_Ck101 ag1 = new AG_Ck101(Html, comicIndex, true);

							return ag1.GetBcImageData();
						case "k886":
							AG_k886 ag_k886 = new AG_k886(Html, comicIndex,true);
							return ag_k886.GetBcImageData();
						default:
							AG_Ck101 ag2 = new AG_Ck101(Html, comicIndex, true);
							return ag2.GetBcImageData();
						}
					}
				})
				.map(new Function<BcImageData, ArrayList<String>>() {
					@Override
					public ArrayList<String> apply(BcImageData bcImageData) throws Exception {
						ArrayList<String> urls = null;
						if (!bcImageData.getItemReArrayGSON().equals("")) {
							urls =  new Gson().fromJson(bcImageData.getItemReArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						return urls;
					}
				});
	}	
	
	private Flowable<ArrayList<String>> GetImageB(BcImageData bcImageData) {
		return Flowable.just(bcImageData)
				.map(new Function<BcImageData, ArrayList<String>>() {
					@Override
					public ArrayList<String> apply(BcImageData bcImageData) throws Exception {
						//-這裡取得bcImageData資料
						ArrayList<String> urls = null;
						if (!bcImageData.getItemReArrayGSON().equals("")) {
							urls =  new Gson().fromJson(bcImageData.getItemReArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						/*//--註解掉是因為盡量不要傳需二度解析Array
						else {
							urls =  new Gson().fromJson(bcImageData.getItemArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
						}
						*/
						return urls;
					}
				});
	}
	
	/***
	 * 時間判定
	 * @param bcImageData
	 * @return 是否准許更新
	 */
	public boolean UpdateTimeContrast(BcImageData bcImageData) {
		//Date todate = new Date();
		DateTime end= new DateTime();
		DateTime begin = new DateTime(bcImageData.getLastUpdated());//Date date = bcHomeData.getLastUpdated();
		
		//计算区间毫秒数  
		Duration d = new Duration(begin, end);  
		long time = d.getMillis();  
		//比較時間//long oic = todate.getTime()-date.getTime();//傳回自 1970/1/1 以來之毫秒數
		long timc_out = (time/1000);//得到秒數
		//Log.i("得到秒數", ""+timc_out);
		//Log.i("跟上次時間差", time+"秒");
		if (timc_out>60*60*24*5) {//超過5天
			return true;//准許更新
		} else {
			return false;
		}
	}
}
