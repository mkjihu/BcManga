package com.bc_manga2.Resolve.Image;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bc_manga2.Resolve.Index.ItemComicIndex;

import android.util.Log;

public class AG_Ck101 extends ImageRexolve{

	
	
	private Document xmlDoc;
	/**
	 * @param Html 
	 * @param comicIndex 取得項目
	 * @param interpret 是否拿純圖片列表
	 */
	public AG_Ck101(String Html, ItemComicIndex comicIndex, boolean interpret) {
		super(Html, comicIndex, interpret);
		xmlDoc =  Jsoup.parse(Html);
	}
	
	@Override
	public int HtmlQuantity(String Html) {
		Elements elements = xmlDoc.getElementsByTag("option");
		Log.i("數量", elements.size()+" ");	//
		return elements.size();
	}

	@Override
	public String HtmlComicName(String Html) {
		Element element = xmlDoc.getElementsByAttributeValue("property", "og:title").first();
		Log.i("取得名稱", element.attr("content"));
		return element.attr("content");
	}

	@Override
	public ArrayList<String> HtmlImageUrls(String Html) {
		Elements elements = xmlDoc.getElementsByTag("option");
		//Log.i("數量", elements.size()+" ");	
		String kgurl = comicIndex.getItemUrl().substring(0, comicIndex.getItemUrl().length()-1);
		Log.i("基底頁碼", kgurl+" ");
		
		ArrayList<String> urls = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			//Log.i("頁碼", elements.get(i).val()+"");
			//Log.i("頁碼2", elements.get(i).attr("value")+"");
			Log.i("頁碼整合-所有頁碼", kgurl+elements.get(i).attr("value")+"/1");
			urls.add(kgurl+elements.get(i).attr("value")+"/1"); ///--------此為每一頁的HTML網址---非圖片--要從裡面提取圖片
		}
		return urls;
	}
	
	@Override
	public ArrayList<String> HtmlImages(List<String> Htmls) {
		ArrayList<String> images = new ArrayList<String>();
		for (int i = 0; i < Htmls.size(); i++) {
			ArrayHTMLResolve(Htmls.get(i), images);
		}
		return images;
	}
	private void ArrayHTMLResolve(String html,ArrayList<String> images) 
	{	
		Document xmlDoc =  Jsoup.parse(html);
		Element element = xmlDoc.getElementsByAttributeValue("property", "og:image").first();
		//Log.i("第一階段取得圖片2-圖1", element.attr("content"));
		images.add(element.attr("content"));
	}

	
	
}
