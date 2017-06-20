package com.bc_manga2.Resolve.Home;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.bc_manga2.Application.BcApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;
import de.greenrobot.BcComicdao.BcHomeData;
import de.greenrobot.BcComicdao.BcHomeDataDao;
import de.greenrobot.BcComicdao.BcHomeDataDao.Properties;


public  abstract  class   HomeResolve<T> implements DBAction{
	
	/***/
	private String placeUrl;
	/**需解析資料*/
	protected String Html;
	/**資料庫PK*/
	protected String DataPK;
	
	private BcHomeDataDao bcHomeDataDao;
	private BcHomeData bcHomeData;
	
	public HomeResolve(String DataPK,String Html,String placeUrl) {
		this.Html = Html;
		this.DataPK = DataPK;
		this.placeUrl = placeUrl;
		
		bcHomeDataDao = BcApplication.getInstance().getDaoSession().getBcHomeDataDao();
		bcHomeData = bcHomeDataDao.queryBuilder().where(Properties.HomePK.eq(DataPK)).unique();
	}
	
	/**從HTML解析Page列表*/
	protected abstract ArrayList<ItemRotation> HtmlRotationItem(String Html);
	
	/**從HTML解析格狀列表 */
	protected abstract ArrayList<ItemCriterion> HtmlCriterionItem(String Html);
	
	
	
	/**從資料庫取得首頁Page列表 */
	private ArrayList<ItemRotation> SQLRotationItem(String DataPK) {
		ArrayList<ItemRotation> itemRotations = new ArrayList<ItemRotation>();
		if(bcHomeData!=null)//--有存取過資料
		{
			itemRotations = new Gson().fromJson(bcHomeData.getHomePageItemArrayGSON(), new TypeToken<ArrayList<ItemRotation>>(){}.getType());
			if (itemRotations!=null)
				return itemRotations;
		}
		return itemRotations;
	}
	/**從資料庫取得解析格狀列表*/
	private ArrayList<ItemCriterion> SQLCriterionItem(String DataPK) {
		
		ArrayList<ItemCriterion> criterions = new ArrayList<ItemCriterion>();
		if(bcHomeData!=null && !bcHomeData.getHomeItemArrayGSON().equals(""))//--有存取過資料
		{
			criterions = new Gson().fromJson(bcHomeData.getHomeItemArrayGSON(), new TypeToken<ArrayList<ItemCriterion>>(){}.getType());
			if (criterions!=null)
				return criterions;
		}
		return criterions;
	}
	
	
	/**強制刷新資料回存*/
	private T SQLRefresh(ArrayList<ItemRotation> itemRotations,ArrayList<ItemCriterion> criterions,String DataPK) 
	{
		String HomePageItemArrayGSON = new Gson().toJson(itemRotations);	
		String HomeItemArrayGSON  = new Gson().toJson(criterions);
		Log.i("w1", HomePageItemArrayGSON);
		Log.i("w2", HomeItemArrayGSON);
		BcHomeData bcHomeData = new BcHomeData(null, DataPK, placeUrl, HomePageItemArrayGSON, HomeItemArrayGSON, new Date());	
		bcHomeDataDao.insertOrReplace(bcHomeData);
		return SendAdapter(itemRotations,criterions);
	}
	
	/**實作要傳出的Adapder類型 --需要接受參數*/
	protected abstract T SendAdapter(ArrayList<ItemRotation> itemRotations,ArrayList<ItemCriterion> criterions);
	
	@Override
	public boolean UpdateTimeContrast(String DataPK) {
		if(bcHomeData!=null)//--有存取過資料
		{
			//Date todate = new Date();
			DateTime end= new DateTime();
			DateTime begin = new DateTime(bcHomeData.getLastUpdated());//Date date = bcHomeData.getLastUpdated();
			
			//计算区间毫秒数  
			Duration d = new Duration(begin, end);  
			long time = d.getMillis();  
			//比較時間//long oic = todate.getTime()-date.getTime();//傳回自 1970/1/1 以來之毫秒數
			long timc_out = (time/1000);//得到秒數
			//Log.i("得到秒數", ""+timc_out);
			//Log.i("跟上次時間差", time+"秒");
			if (timc_out>360) {//超過6分鐘
				return true;//准許更新
			} else {
				return false;
			}
		}
		return true;
	}
	
	
	
	private T Enforce(boolean replace) {
		
		if (replace || UpdateTimeContrast(DataPK)) {
			//強制刷新-或用時間判訂-或Html不等於空
			Log.i("1", "1");
			return SQLRefresh(HtmlRotationItem(Html),HtmlCriterionItem(Html), DataPK);
		}
		else {
			//直接丢资料库资料
			Log.i("2", "2");
			return SendAdapter(SQLRotationItem(DataPK), SQLCriterionItem(DataPK));
		}
	}
	
	/**傳出實做好的Adapder*/
	public T SendAdapter(boolean replace) {
		if (!Html.equals("")&&Html != null) {
			return Enforce(replace);
		}
		else {
			//沒有取得Html
			return SendAdapter(SQLRotationItem(DataPK), SQLCriterionItem(DataPK));//沒網路也沒存取過 throw Exceptions.propagate(new RuntimeException("解析錯誤1"));//把它包装成Unchecked异常。	
		}

	}
	/*
	private boolean getIsData() {
		if(bcHomeData!=null)//--有存取過資料
		{
			return true;
		}
		return false;
	}
	*/
}
