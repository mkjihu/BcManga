package com.bc_manga2.Resolve.Index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AI_Ck101 extends IndexRexolveA{

	private String MainUrl;
	
	public AI_Ck101(String MainUrl,String Html, boolean interpret) {
		super(Html, interpret);
		this.MainUrl = MainUrl;
	}
	@Override
	protected String UrlRexolve(String Html) 
	{
		Document xmlDoc =  Jsoup.parse(Html);
		String ComicIndexUrl="";
		//取得大標題
		Element titles = xmlDoc.getElementsByClass("pageTitle").first();//取得有pageTitle的Class div所有內容
		if (titles!=null){
			Element gs = titles.getElementsByTag("a").get(1);
			ComicIndexUrl = MainUrl+gs.attr("href");
		}
		return ComicIndexUrl;
	}

}
