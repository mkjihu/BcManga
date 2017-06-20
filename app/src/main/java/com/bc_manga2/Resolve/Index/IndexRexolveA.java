package com.bc_manga2.Resolve.Index;


import com.bc_manga2.Network.HttpApiClient;

import android.util.Log;
import io.reactivex.Flowable;
//第一階段 先解析資料跟加載所需url 列表
abstract class IndexRexolveA
{
	protected String Html;
	/**是否需解析資料*/
	protected boolean interpret;
	/**
	 * @param Html 獲得HTML
	 * @param interpret 是否二度解析
	 */
	public IndexRexolveA(String Html,boolean interpret) {
		this.Html = Html;
		this.interpret = interpret;
		
	}

	/**從HTML解析正確網址*/
	protected abstract String UrlRexolve(String Html);

	
	public Flowable<String> GetHtml()
	{
		if (interpret) {
			//二度解析
			//Log.i("二度解析", "二度解析");
			String Url = UrlRexolve(Html);
			return HttpApiClient.getHtml(Url);
		}
		return Flowable.just(Html);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
