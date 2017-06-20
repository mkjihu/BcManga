package com.bc_manga2.Resolve.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.greenrobot.BcComicdao.BcImageData;
import de.greenrobot.BcComicdao.BcImageDataDao;
import de.greenrobot.BcComicdao.BcImageDataDao.Properties;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

abstract class ImageRexolve implements ImageInter{

	protected String Html;
	protected ItemComicIndex comicIndex;
	protected String HomePK; //來自於哪個主網址--所屬網站分類
	protected boolean interpret;//是否需解析資料
	//protected String HomePK;
	private BcImageDataDao bcImageDataDao;
	private BcImageData bcImageData;
	
	/**
	 * @param Html 
	 * @param PKUrl 該話數開頭網址--(獨特)--PK
	 * @param interpret 是否拿純圖片列表
	 */
	public ImageRexolve(String Html,ItemComicIndex comicIndex,boolean interpret) {
		this.HomePK = comicIndex.getHomePK();
		this.Html = Html;
		this.comicIndex = comicIndex;
		this.interpret = interpret;
		bcImageDataDao = BcApplication.getInstance().getDaoSession().getBcImageDataDao();
		bcImageData  = bcImageDataDao.queryBuilder().where(Properties.PKUrl.eq(comicIndex.getItemUrl())).unique();
//		if (bcImageData!=null) {
//			Log.i("有", "有");
//		}
	}

	
	/**取得頁數*/
	protected abstract int HtmlQuantity(String Html);
	/**取得名稱*/
	protected abstract String HtmlComicName(String Html);
	
	/**從HTML解析需要二次加載列表*/
	protected abstract ArrayList<String> HtmlImageUrls(String Html);
	/**從HTML解析純圖片列表*/
	protected abstract ArrayList<String> HtmlImages(List<String> Htmls);
	
	
	@Override
	public boolean UpdateTimeContrast() {
		if(bcImageData!=null)//--有存取過資料
		{
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
			if (timc_out>60*60*24*10) {//超過10天
				return true;//准許更新
			} else {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Flowable<BcImageData> GetBcImageData() {
		if (bcImageData==null||UpdateTimeContrast()) { //無資料 或是有資料但超過10天
			bcImageData = new BcImageData(null);
			bcImageData.setHomePK(HomePK);
			bcImageData.setPKUrl(comicIndex.getItemUrl());
			bcImageData.setComicName(HtmlComicName(Html));
			bcImageData.setQuantity(HtmlQuantity(Html));
			bcImageData.setItemArrayGSON(new Gson().toJson(HtmlImageUrls(Html)));
			bcImageData.setItemReArrayGSON("");
			bcImageData.setLastUpdated(new Date());
			bcImageDataDao.insertOrReplace(bcImageData);
			if (interpret) { //拿純圖片列表
				return A(HtmlImageUrls(Html));
			}
			return Flowable.just(bcImageData);
		}
		else {
			String gson = bcImageData.getItemReArrayGSON();
			if (interpret && "".equals(gson)) { //拿純圖片列表但是又沒有資料
				ArrayList<String> urls =  new Gson().fromJson(bcImageData.getItemArrayGSON(), new TypeToken<ArrayList<String>>(){}.getType());
				return A(urls);
			}
			return Flowable.just(bcImageData) ;
			
		}
	}





	/**
	 * 二次加載圖片列表
	 * @param Urls 未解析列表 
	 * @return
	 */
	private Flowable<BcImageData> A(ArrayList<String> Urls) {
		
		return Flowable.fromIterable(Urls)
				.concatMap(new Function<String, Flowable<String>>() {
					@Override
					public Flowable<String> apply(String Url) throws Exception {
						return HttpApiClient.getHtml(Url);
					}
				})
				.toList().toFlowable()
				.map(new Function<List<String>, BcImageData>() {
					@Override
					public BcImageData apply(List<String> arg0) throws Exception {
						ArrayList<String> images = HtmlImages(arg0);
						bcImageData.setItemReArrayGSON(new Gson().toJson(images));
						bcImageDataDao.insertOrReplace(bcImageData);
						return bcImageData;
					}
				});
	} 
	
	
	
}
