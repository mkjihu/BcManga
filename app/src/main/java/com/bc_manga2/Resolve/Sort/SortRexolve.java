package com.bc_manga2.Resolve.Sort;

import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import com.bc_manga2.Application.BcApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import de.greenrobot.BcComicdao.BcSortData;
import de.greenrobot.BcComicdao.BcSortDataDao;
import de.greenrobot.BcComicdao.BcSortDataDao.Properties;


abstract class SortRexolve  implements DBAction {
	protected String Html;
	private BcSortDataDao bcSortDataDao;
	private BcSortData bcSortData;
	/**資料庫PK*/
	protected String HomePK;
	
	protected String SortUrl;//分類頁網址
	
	/**
	 * 
	 * @param Html 
	 * @param HomePK 資料庫PK
	 * @param SortUrl 分類頁網址
	 */
	public SortRexolve(String Html,String HomePK,String SortUrl) {
		this.Html = Html;
		this.SortUrl = SortUrl;
		this.HomePK = HomePK;
		bcSortDataDao = BcApplication.getInstance().getDaoSession().getBcSortDataDao();
		bcSortData = bcSortDataDao.queryBuilder().where(Properties.HomePK.eq(HomePK)).unique();
	}
	
	/**從HTML解析Sort格狀列表 */
	protected abstract ArrayList<ItemSort> HtmlSortItem(String Html);
	
	/**從資料庫取得解析格狀列表*/
	private ArrayList<ItemSort> SQLSortItem() {
		if(bcSortData!=null) {//--有存取過資料
			return new Gson().fromJson(bcSortData.getSortItemArrayGSON(), new TypeToken<ArrayList<ItemSort>>(){}.getType());
		}
		return new ArrayList<ItemSort>();
	}

	@Override
	public boolean UpdateTimeContrast(String DataPK) {
		if(bcSortData!=null)//--有存取過資料
		{
			DateTime end= new DateTime();
			DateTime begin = new DateTime(bcSortData.getLastUpdated());//Date date = bcHomeData.getLastUpdated();
			
			//计算区间毫秒数  
			Duration d = new Duration(begin, end);  
			long time = d.getMillis();  
			long timc_out = (time/1000);//得到秒數
			Log.i("跟上次時間差", timc_out+"秒");
			if (timc_out>3600*24) {//超過24 小時
				return true;//准許更新
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**傳出SortArrary*/
	public ArrayList<ItemSort> GetSortItemArrary()
	{
		if (UpdateTimeContrast(HomePK)) {
			//--刷新資料庫
			ArrayList<ItemSort> itemSorts = HtmlSortItem(Html);
			String itemSortsGSON  = new Gson().toJson(itemSorts);
			BcSortData bcSortData = new BcSortData(null, HomePK, SortUrl, itemSortsGSON, new Date());
			bcSortDataDao.insertOrReplace(bcSortData);
			return itemSorts;
		} else {
			return SQLSortItem();
		}
	}
	
}
