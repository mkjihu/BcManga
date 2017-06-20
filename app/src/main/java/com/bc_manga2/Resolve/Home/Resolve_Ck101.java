package com.bc_manga2.Resolve.Home;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bc_manga2.Adapder.HomeAdapder;
import com.bc_manga2.Fragment.BaseFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;

public class Resolve_Ck101 extends HomeResolve<Adapter<ViewHolder>>{

	private Document xmlDoc;	
	private String placeUrl = "http://comic.ck101.com";
	private BaseFragment fragment;
	
	public Resolve_Ck101(String DataPK, String Html, String placeUrl,BaseFragment fragment) {
		super(DataPK, Html, placeUrl);
		xmlDoc = Jsoup.parse(Html);
		this.fragment = fragment;
	}
	@Override
	protected ArrayList<ItemRotation> HtmlRotationItem(String Html) {

		ArrayList<ItemRotation> arrayList = new ArrayList<ItemRotation>();
		Elements elements = xmlDoc.getElementsByClass("orderList");
		Elements ik = elements.get(0).getElementsByTag("a");
		for (int i = 0; i < ik.size(); i++) {
			arrayList.add(new ItemRotation(ik.get(i).attr("rel")
										, ik.get(i).attr("title")
										, ik.get(i).attr("href")));
		}
		
		return arrayList;
	}

	@Override
	protected ArrayList<ItemCriterion> HtmlCriterionItem(String Html) {
		//Log.i("分析線程",RxPresenter.isInMainThread());
		ArrayList<ItemCriterion> criterions = new ArrayList<ItemCriterion>();
		//取得大標題
		Elements titles = xmlDoc.getElementsByClass("comicBox");//取得有comicBox的Class div所有內容
		for (int i = 1; i < titles.size(); i++)//從第2項開始
		{
			//取得大標題
			Element title = titles.get(i).getElementsByTag("h2").first();//取得div所有內容   有h2 tag的第一項
			//Log.i("大標題", title.text().toString());

			ItemCriterion itemCriterion = new ItemCriterion();
			itemCriterion.setCritName(title.text().toString());
			
			Elements elements2 = titles.get(i).select("li");//取得該項目列表
			ArrayList<ItemRotation> rotations = getSitemRotations(elements2, Html,i);//分析該項目圖片
			
			itemCriterion.setItemRotations(rotations);
			
			criterions.add(itemCriterion);
		}
		return criterions;
	}

	@Override
	protected Adapter<ViewHolder> SendAdapter(ArrayList<ItemRotation> itemRotations, ArrayList<ItemCriterion> criterions) 
	{
//		for (int i = 0; i < itemRotations.size(); i++) {
//			Log.i("page圖", itemRotations.get(i).getUrl());
//		}
//		
//		for (int i = 0; i < criterions.size(); i++) {
//			Log.i("項目名稱", criterions.get(i).getCritName()+"_"+criterions.get(i).getItemRotations().size());
//			for (int j = 0; j < criterions.get(i).getItemRotations().size(); j++) {
//				Log.i("子項圖", criterions.get(i).getItemRotations().get(j).getUrl());
//			}
//		}
		if (fragment !=null && criterions.size() != 0) {
			return new HomeAdapder(fragment, itemRotations, criterions);
		}
		return null;
	}




	//========================================擴增方法==============================================================
	/**取得大項目底下子項列表*/
	private ArrayList<ItemRotation> getSitemRotations(Elements Area,String Html,int i)
	{
		ArrayList<ItemRotation> itemRotations  = new ArrayList<ItemRotation>();
		ArrayList<String> ImageUrls = getImageUrl(Html,i-1);
		
		for (int j = 0; j < Area.size(); j++) {
			
			Elements elementsN = Area.get(j).select("h3 > a");//在h3元素之后的a元素
			String ItemName = elementsN.first().text();//Log.i("測試","取名稱_"+elementsN.first().text()+"");//--取名稱
			
			String LinkUrl = placeUrl+elementsN.first().attr("href");//Log.i("測試","取連結_"+placeUrl()+elementsN.first().attr("href")+"");	//--取連結
			
			Log.i("測試","取圖_"+ImageUrls.get(j)+"");	//--取圖
			Log.i("測試","取圖2_"+BigImageGet(ImageUrls.get(j))+"");	//--取圖
			String Image;
			try {
				Image = ImageUrls.get(j);
			} catch (Exception e) {
				Image = "http://nsm.ckcdn.com/images/noImg.jpg";
			}	
			
			if (i==1) {
				itemRotations.add(new ItemRotation(DataPK,Image, ItemName, LinkUrl,true));//--解析方式為需要特殊手段
			}
			else{
				ItemRotation itemRotation =new ItemRotation(Image, ItemName, LinkUrl);
				itemRotation.setHomePK(DataPK);
				itemRotations.add(itemRotation);
			}			
		}
		return itemRotations;
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
	
	/**
	 * 分析圖片連結
	 * @param HTml 內文
	 * @param Section 指定區段
	 */
	private ArrayList<String> getImageUrl(String HTml,int Section)
	{
		//try {} catch (Exception e)  {Log.i("首頁圖片取得失敗", "首頁圖片取得失敗");}
		
		//使用正則取圖片
		String t1_to = "<div class=\"hotComic\">";//Pattern p = Pattern.compile(t1+"([\\s|\\S]+?)</div>");//元
		String t1_over = "([\\s|\\S]*?)</div>";//Pattern p = Pattern.compile("<h2>(.*?)</h2>");
		//第一階段取大框
		Pattern p1 = Pattern.compile(t1_to+t1_over);
		Matcher m1 = p1.matcher(HTml);
		
		//第一階段取得結果
		int index = 0;//起始區段
		while (m1.find()) 
		{
			String codeGroup0 = m1.group(0);
			if(index == Section)//取得指定區段
			{
				return ImageUrl_SecondStage(codeGroup0);//第一階段取得結果逐項分析
			}
			index++;
		}
		return new ArrayList<String>();
	}
	//第一階段取得結果逐項分析
	private ArrayList<String> ImageUrl_SecondStage(String codeGroup0)
	{
		ArrayList<String> MacImageUrls = new ArrayList<String>();

		String t2_to = "<img src=\"";
		String t2_over = "\" alt";
		Pattern p2 = Pattern.compile(t2_to+"([^\"]*)"+t2_over);//Pattern p2 = Pattern.compile(t2+"([\\s|\\S]*?)"+t2_1);
		Matcher m2 = p2.matcher(codeGroup0);
		
		while (m2.find()) 
		{
			String ImageUrl = m2.group(1);
			//Log.i("測試", ImageUrl);
			MacImageUrls.add(ImageUrl);
		}
		return MacImageUrls;
	}
		
	




}
