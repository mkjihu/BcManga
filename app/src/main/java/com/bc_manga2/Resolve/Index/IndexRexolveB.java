package com.bc_manga2.Resolve.Index;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Presenter.ComicDirectoryPresenter.SetInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import de.greenrobot.BcComicdao.BcIndexData;
import de.greenrobot.BcComicdao.BcIndexDataDao;
import de.greenrobot.BcComicdao.BcIndexDataDao.Properties;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

//第二階段 先解析資料跟加載所需url 列表
abstract class IndexRexolveB implements DBAction 
{

	
	/***/
	protected String PKUrl;//主要item Url(獨特)--PK //真正的第1目錄頁的網址
	protected String HomePK; //來自於哪個主網--所屬網站分類
	protected String Html;
	//protected boolean interpret;//是否需解析資料
	
	private BcIndexDataDao bcIndexDataDao;
	private BcIndexData bcIndexData;
	
	/**
	 * 
	 * @param Html 
	 * @param PKUrl 真正的第1目錄頁的網址
	 * @param HomePK 來自於哪個主網址--所屬網站分類
	 * @param interpret 是否需解析資料
	 */
	public IndexRexolveB(String Html,String PKUrl,String HomePK,boolean interpret) {
		this.Html = Html;
		this.PKUrl = PKUrl;
		this.HomePK = HomePK;
		bcIndexDataDao = BcApplication.getInstance().getDaoSession().getBcIndexDataDao();
		
		//Log.i("這裡", "這裡1"+interpret);
		if(interpret) {//如果是經過二次解析
			bcIndexData = bcIndexDataDao.queryBuilder().where(Properties.PKUrl.eq(GePKtUrl(Html))).unique();
		} else {
			bcIndexData = bcIndexDataDao.queryBuilder().where(Properties.PKUrl.eq(PKUrl)).unique();
		}
	}
	
	/**
	 * 是否重抓
	 */
	@Override
	public boolean UpdateTimeContrast() {
		if(bcIndexData!=null)//--有存取過資料
		{
			DateTime end= new DateTime();
			DateTime begin = new DateTime(bcIndexData.getClickDate());//Date date = bcIndexData.getClickDate();
			
			//比較時間
			Duration d = new Duration(begin, end);  
			long time = d.getMillis();  
			long timc_out = (time/1000);//得到秒數
			//Log.i("跟上次時間差", timc_out+"秒");
			long p = 5 * 60 * 60 ; //超過5小時
			if (timc_out>p) {
				return true;//准許更新
			} else {
				return false;
			}
		}
		return true;
	}

	/**取得真網址*/
	protected abstract String GePKtUrl(String Html);	
	/**從HTML解析基本資料*/
	protected abstract BcIndexData HtmlRoInfo(String Html);

	/**從HTML解析加載列表*/
	protected abstract ArrayList<String> HtmlItemUrls(String Html);
	
	/**從HTML列表取得所有item*/
	protected abstract ArrayList<ItemComicIndex> HtmlItems(List<String> ItemUrls);
	

	
	/**
	 * A方法 使用資料庫資料時使用
	 * @return
	 */
	private BcIndexData a()
	{
		
		bcIndexData.setClickDate(new Date());
		bcIndexDataDao.insertOrReplace(bcIndexData);
		return bcIndexData;
	}
	
	/**
	 * B方法 使用HTML資料
	 */
	private BcIndexData b(String Html)
	{
		
		if (bcIndexData ==null) {
			bcIndexData = new BcIndexData();
			bcIndexData.setId(null);
			bcIndexData.setIsCollect(false);
			bcIndexData.setIsDownload(false);
			bcIndexData.setType("");
			bcIndexData.setComicDownloadPath("");
			//Log.i("这里", "创新bcIndexData");
		}
		BcIndexData indexInfo = HtmlRoInfo(Html);
		bcIndexData.setHomePK(HomePK);
		bcIndexData.setPKUrl(GePKtUrl(Html));
		bcIndexData.setImageUrl(indexInfo.getImageUrl());
		bcIndexData.setTitleName(indexInfo.getTitleName());
		bcIndexData.setAuthorName(indexInfo.getAuthorName());
		bcIndexData.setUpdated_day(indexInfo.getUpdated_day());
		bcIndexData.setGist(indexInfo.getGist());
		bcIndexData.setType(indexInfo.getType());
		bcIndexData.setClickDate(new Date());	
		//bcIndexData.setItemArrayGSON("");
		//bcIndexDataDao.insertOrReplace(bcIndexData);
		return bcIndexData;
	}

	
	/**
	 * 傳入map 動作 在主線程顯示BcIndexData資料
	 * 從DB取資料
	 */
	public Flowable<ArrayList<ItemComicIndex>> AA(Function<BcIndexData, String> a_map) {
		
		return Flowable.just(a())
			//.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
			.observeOn(AndroidSchedulers.mainThread())//--在主線程中顯示  
			.map(a_map)
			.observeOn(Schedulers.io())
			.map(new Function<String, ArrayList<ItemComicIndex>>() {
				@Override
				public ArrayList<ItemComicIndex> apply(String arg0) throws Exception {
					return new Gson().fromJson(arg0, new TypeToken<ArrayList<ItemComicIndex>>(){}.getType());//先取出資料
				}
			});
	}
	
//	public static class Fuanc1 implements Func1<BcIndexData, String> {
//		@Override
//		public String call(BcIndexData arg0) {
//			return arg0.getItemArrayGSON();
//		}
//	}
	
	/**
	 * 從HTML重載
	 */
	public Flowable<ArrayList<ItemComicIndex>> BB(Function<BcIndexData, String> b_map) {
		
		return Flowable.just(b(Html))
			//.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
			.observeOn(AndroidSchedulers.mainThread())//--在主線程中顯示  
			.map(b_map)
			//.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
			.observeOn(Schedulers.io())
			.map(new Function<String, ArrayList<String>>() {
				@Override
				public ArrayList<String> apply(String arg0) throws Exception {
					return HtmlItemUrls(Html);
				}
			})
			.concatMap(new Function<ArrayList<String> , Flowable<List<String>>>() {
				@Override
				public Flowable<List<String>> apply(ArrayList<String> arg0) throws Exception {
					return Flowable.fromIterable(arg0)
							.concatMap(new Function<String, Flowable<String>>() {
								@Override
								public Flowable<String> apply(String Url) throws Exception {
									return HttpApiClient.getHtml(Url);
								}
							})
							.toList().toFlowable();
				}
			})
			.map(new Function<List<String>, ArrayList<ItemComicIndex>>() {
				@Override
				public ArrayList<ItemComicIndex> apply(List<String> arg0) throws Exception {
					ArrayList<ItemComicIndex> arrayList = HtmlItems(arg0);
					String ItemArrayGSON = new Gson().toJson(arrayList);
					bcIndexData.setItemArrayGSON(ItemArrayGSON);
					bcIndexData.setClickDate(new Date());
					bcIndexDataDao.insertOrReplace(bcIndexData);
					/*
					try {
						
					} catch (Exception e) {
						Log.i("这里出错", e.getMessage());
					}
					*/
					return arrayList;
				}
			});
	}

	/**由系統時間判定是否重抓*/
	public Flowable<ArrayList<ItemComicIndex>> CC(Function<BcIndexData, String> b_map) {
		if (UpdateTimeContrast()) { //--是否重抓
			return BB(b_map);
		} else {
			return AA(b_map);
		}
	}
	
	
}
