package com.bc_manga2.Resolve.Sort;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class AS_Ck101 extends SortRexolve{
	
	private Document xmlDoc;
	private String placeUrl = "http://comic.ck101.com";
	public AS_Ck101(String Html, String HomePK, String SortUrl) {
		super(Html, HomePK, SortUrl);
		xmlDoc = Jsoup.parse(Html);
	}

	@Override
	protected ArrayList<ItemSort> HtmlSortItem(String Html) {
		ArrayList<ItemSort> itemSorts = new ArrayList<>();
		Element element = xmlDoc.getElementsByClass("nav").first();
		Elements sor = element.select("li").select("a[href]");//查詢li 標籤下  带有href属性的a元素
		Log.i("s", sor.size()+"");
		for (int i = 0; i < sor.size(); i++) {
			//Log.i("title", sor.get(i).attr("title"));
			//Log.i("Url", placeUrl+sor.get(i).attr("href"));
			String title = sor.get(i).attr("title");
			String ItemUrl = placeUrl+sor.get(i).attr("href");
			itemSorts.add(new ItemSort(HomePK, title, ItemUrl, ""));
		}
		return itemSorts;
	}

}
