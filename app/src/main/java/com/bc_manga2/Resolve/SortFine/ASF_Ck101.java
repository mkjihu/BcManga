package com.bc_manga2.Resolve.SortFine;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class ASF_Ck101 extends SortFineRexolve{
	
	private Document xmlDoc ;
	private String placeUrl = "http://comic.ck101.com";
	
	public ASF_Ck101(String Html, String HomePK) {
		super(Html, HomePK);
		xmlDoc =  Jsoup.parse(Html);
	}

	@Override
	protected ArrayList<SortFineItem> HtmlSortFineItem(String Html) {
		
		ArrayList<SortFineItem> arrayList = new ArrayList<SortFineItem>(); 
		
		Elements element = xmlDoc.getElementsByClass("case").select("li");//查詢在Class = case 下 所有li 標籤
		for (int i = 0; i < element.size(); i++) {
			Element msg = element.get(i).getElementsByTag("a").first();//获取对应的元素
			//Log.i("圖", msg.getElementsByTag("img").first().attr("src"));
			//Log.i("名", msg.attr("title"));
			//Log.i("連", msg.attr("href"));
			String Image = BigImageGet(msg.getElementsByTag("img").first().attr("src"));
			String Name = msg.attr("title");
			String Url = placeUrl+msg.attr("href");
			
			SortFineItem fineItem = new SortFineItem(Image, Name, Url);
			fineItem.setHomePK(HomePK);
			
			arrayList.add(fineItem);
		}
		
		return arrayList;
	}
	
	@Override
	protected String GetNextUrl(String Html) {
		///有可能有下一頁有可能沒有
		Document xmlDoc =  Jsoup.parse(Html);
		String NextUrl="";
		try {
			Element element = xmlDoc.getElementsByClass("pagination").first();
			Element element2 = element.getElementsByAttributeValue("title", "下一頁").first();
			//Log.i("工作",element2.attr("href"));
			NextUrl = placeUrl+element2.attr("href");
		} catch (Exception e) {
			// 沒有下一頁
			Log.i("沒有下一頁", "沒有下一頁");
		}
		return NextUrl;
	}
	
	
	/**
	 * 针对卡提诺首页取得大图--
	 * @param ImageUrl
	 */
	private String BigImageGet(String ImageUrl) 
	{
		//方法1--不嚴謹
		//String BigImage = ImageUrl.replaceFirst("http://s1.comic.ccddnn.net/img/t","http://s1.comic.ccddnn.net/img/o");//將第一個符合 regex 的子字串置換成 replacement
		//方法2
		String BigImage = ImageUrl.replace("img/t", "img/o");//將第一個符合 regex 的子字串置換成 replacement
		//Log.i("大圖", BigImage);
		return BigImage;

	}


}
