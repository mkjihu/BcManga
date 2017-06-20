package com.bc_manga2.Resolve.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.util.Log;
import de.greenrobot.BcComicdao.BcIndexData;

public class BI_Ck101 extends IndexRexolveB{

	private Document xmlDoc ;
	public BI_Ck101(String Html, String PKUrl, String HomePK, boolean interpret) {
		super(Html, PKUrl, HomePK, interpret);
		Log.i("測1", Html);
		xmlDoc =  Jsoup.parse(Html);
		
	}

	@Override
	protected String GePKtUrl(String Html) {
		Log.i("測1", Html);
		xmlDoc =  Jsoup.parse(Html);
		Element element6 = xmlDoc.getElementsByAttributeValue("property", "og:url").first();
		return element6.attr("content");
	}

	@Override
	protected BcIndexData HtmlRoInfo(String Html) {
		BcIndexData bcIndexData = new BcIndexData();

		Element element = xmlDoc.getElementsByAttributeValue("itemprop", "image").first();
		String ImageUrl="";
		if(element!=null) {
			ImageUrl= element.attr("src");
		}
		bcIndexData.setImageUrl(ImageUrl);
		
		Element element2 = xmlDoc.getElementsByAttributeValue("itemprop", "name").first();
		String TitleName ="";
		if (element2!=null) {
			TitleName = element2.text().replaceAll("\\s+", "");
		}
		bcIndexData.setTitleName(TitleName);
		

		//使用正則
		String t1_to = "</span>";//Pattern p = Pattern.compile(t1+"([\\s|\\S]+?)</div>");//元
		String t1_over = "(.*?)<br />";//Pattern p = Pattern.compile("<h2>(.*?)</h2>");
		Pattern p1 = Pattern.compile(t1_to+t1_over);
		Matcher m1 = p1.matcher(Html);
		String AuthorName = "";
		while (m1.find()) 
		{
			String codeGroup0 = m1.group(1);
			String kd = codeGroup0.replaceAll("\\s+", "");//去空白
			AuthorName = kd.substring(3);//精簡
		}
		bcIndexData.setAuthorName(AuthorName);		
		
		Element element3 = xmlDoc.getElementsByAttributeValue("property", "book:release_date").first();
		String Updated_day = "";
		if(element3!=null){
			Updated_day = element3.attr("content");
		}
		bcIndexData.setUpdated_day(Updated_day);
		
		Element element6 = xmlDoc.getElementsByAttributeValue("name", "description").first();//xmlDoc.getElementsByClass("brief").first();
		String Gist = element6.attr("content");
		
		//Gist = Gist.replaceAll("\n", " ");
		if (Gist.length()>500) 
		{
			Gist = Gist.substring(0, 199);
		}
//		//Log.i("Gist_L //說明_長", Gist);
		bcIndexData.setGist(Gist);
	
		return bcIndexData;
	}

	@Override
	protected ArrayList<String> HtmlItemUrls(String Html) {
		
		ArrayList<String> Urls = new ArrayList<String>();
		//先取列表
		Elements elements = xmlDoc.getElementsByClass("pagination");
		if (elements.size()>0) 
		{
			String IndexUrl_ik = GePKtUrl(Html)+"/0/0/";
			//Log.i("列表網址", IndexUrl_ik);
			
			//Log.i("列表", "有第二頁");
			Elements elements2 =elements.get(0).getElementsByTag("a");
			int yd = elements2.size();
			String bisrt = "0";
			if(elements.get(0).getElementsByClass("disabled").size()>1) {
				bisrt = elements2.get(yd-3).text().trim();//到數第三項
			}
			else {
				bisrt = elements2.get(yd-2).text().trim();//到數第二項
			}
			int itemskey = Integer.valueOf(bisrt);
			
			for (int i = 1; i <= itemskey; i++) {
				Urls.add(IndexUrl_ik+i);
				//Log.i("列表", IndexUrl_ik+i);
			}
		}
		else//並沒有第二頁
		{
			//Log.i("列表", "並沒有第二頁");
			Urls.add(GePKtUrl(Html));
		}
		return Urls;
	}

	@Override
	protected ArrayList<ItemComicIndex> HtmlItems(List<String> ItemUrls) {
		Log.i("HTML抓完", "HTML抓完-開始解析");		
		ArrayList<ItemComicIndex> comicIndexs = new ArrayList<ItemComicIndex>();
		for (int i = 0; i < ItemUrls.size(); i++) {
			ArrayHTMLResolve(ItemUrls.get(i), comicIndexs);
		}

		return comicIndexs;
	}


	/**列表分析*/
	private void ArrayHTMLResolve(String html,ArrayList<ItemComicIndex> comicIndexs) 
	{	
		String Main_Url = "http://comic.ck101.com";
		Document xmlDoc =  Jsoup.parse(html);
		Elements element4 = xmlDoc.getElementsByClass("relativeRec");
		if (element4.size()>1)//分列取值
		{
			Elements elements = element4.get(1).getElementsByClass("recTitle");
			Log.i("話數", elements.size()+"");
			
			for (int i = 0; i <elements.size() ; i++) 
			{
				//Log.i("G1", i+"");
				Element element2 = elements.get(i);
				String ItemUrl = Main_Url+element2.getElementsByTag("a").first().attr("href");
				String ItemName = element2.getElementsByTag("a").first().attr("title");
				//Log.i("連結", ComicItemUrl);
				//Log.i("話名", ComicItemName);
				ItemName = ItemName.replaceAll(" ", "");
				//String ComicItemKey = element2.getElementsByTag("a").first().attr("href");
				comicIndexs.add(new ItemComicIndex("ck101", ItemUrl, ItemName));
			}
		}
		else
		{
			Elements elements = xmlDoc.getElementsByClass("recTitle");
			for (int i = 0; i <elements.size() ; i++) 
			{
				//Log.i("G1", i+"");
				Element element2 = elements.get(i);
				String ItemUrl = Main_Url+element2.getElementsByTag("a").first().attr("href");
				String ItemName = element2.getElementsByTag("a").first().attr("title");
				//Log.i("連結", ComicItemUrl);
				//Log.i("話名", ComicItemName);
				ItemName = ItemName.replaceAll(" ", "");
				//String ComicItemKey = element2.getElementsByTag("a").first().attr("href");
				comicIndexs.add(new ItemComicIndex("ck101", ItemUrl, ItemName));				
			}
		}
	}
//Document xmlDoc =  Jsoup.parse(Html);
//		Element element6 = xmlDoc.getElementsByAttributeValue("property", "og:url").first();
//		return element6.attr("content");


}
