package com.bc_manga2.Resolve.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Model.imgseries;
import com.bc_manga2.Resolve.Index.ItemComicIndex;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.greenrobot.BcComicdao.BcImageData;
import de.greenrobot.BcComicdao.BcImageDataDao;
import de.greenrobot.BcComicdao.BcImageDataDao.Properties;
import io.reactivex.Flowable;

public class AG_k886 implements ImageInter{
	
	protected String API;
	protected ItemComicIndex comicIndex;
	//protected String HomePK; //來自於哪個主網址--所屬網站分類
	protected boolean isReg;//是否重新加載資料
	private BcImageDataDao bcImageDataDao;
	private BcImageData bcImageData;
	
	public AG_k886(String API,ItemComicIndex comicIndex,boolean isReg) {
		this.API = API;
		this.comicIndex = comicIndex;
		this.isReg = isReg;
		bcImageDataDao = BcApplication.getInstance().getDaoSession().getBcImageDataDao();
		bcImageData  = bcImageDataDao.queryBuilder().where(Properties.PKUrl.eq(comicIndex.getItemUrl())).unique();
	}
	
	
	@Override
	public Flowable<BcImageData> GetBcImageData() {
		if (UpdateTimeContrast()||bcImageData==null||isReg)  {  //無資料 或是有資料但超過5天 或強制刷新
			
			ArrayList<imgseries> imgseries = new Gson().fromJson(API, new TypeToken<ArrayList<imgseries>>(){}.getType());
			ArrayList<String> images = new ArrayList<>();
			for (int i = 0; i < imgseries.size(); i++) {
				images.add(imgseries.get(i).getImg());
			}
			bcImageData = new BcImageData(null);
			bcImageData.setHomePK(comicIndex.getHomePK());
			bcImageData.setPKUrl(comicIndex.getItemUrl());
			bcImageData.setComicName(comicIndex.getItemUrl());
			bcImageData.setQuantity(imgseries.size());
			bcImageData.setItemArrayGSON("");//-不需要二次加載列表
			bcImageData.setItemReArrayGSON(new Gson().toJson(images));
			bcImageData.setLastUpdated(new Date());
			bcImageDataDao.insertOrReplace(bcImageData);
		} 
		
		//除此之外丟出資料
		return Flowable.just(bcImageData);
	}


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
			if (timc_out>60*60*24*5) {//超過2天
				return true;//准許更新
			} else {
				return false;
			}
		}
		return true;
	}


	


	
	
}
