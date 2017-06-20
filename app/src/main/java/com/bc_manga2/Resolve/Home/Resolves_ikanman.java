package com.bc_manga2.Resolve.Home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;

public class Resolves_ikanman  extends HomeResolve<Adapter<ViewHolder>>{
	private Document xmlDoc;	
	private String placeUrl = "http://www.ssoomm.com/";
	
	
	public Resolves_ikanman(String DataPK, String Html, String placeUrl) {
		super(DataPK, Html, placeUrl);
		xmlDoc = Jsoup.parse(Html);
		
	}

	/**該網取隨機8個*/
	@Override
	protected ArrayList<ItemRotation> HtmlRotationItem(String Html) {
		ArrayList<ItemRotation> arrayList = new ArrayList<ItemRotation>();
		Elements elements = xmlDoc.getElementsByClass("bookBak");//翻轉前
		/*
		for (int i = 0; i < elements.size(); i++) {
			Element msg = elements.get(i).getElementsByTag("a").first();
			String Url = placeUrl+msg.attr("href");
			//Log.i("翻轉前-Image", msg.getElementsByTag("img").first().attr("src"));//圖
			//Log.i("翻轉前-Name", msg.getElementsByTag("img").first().attr("title"));//名
			//Log.i("翻轉前-Url", Url);//連
		}
		*/
		Collections.shuffle(elements);//--隨機排序
		for (int i = 0; i < 5; i++) {//取前5個
			Element msg = elements.get(i).getElementsByTag("a").first();
			String Url = placeUrl+msg.attr("href");
			arrayList.add(new ItemRotation(msg.getElementsByTag("img").first().attr("src")
											, msg.getElementsByTag("img").first().attr("title")
											, Url));
		}
		
		return arrayList;
	}

	@Override
	protected ArrayList<ItemCriterion> HtmlCriterionItem(String Html) {
		//Log.i("分析線程",RxPresenter.isInMainThread());
		ArrayList<ItemCriterion> criterions = new ArrayList<ItemCriterion>();
		//-該網特別方式 - 先取最新上架分開取
		Elements elements = xmlDoc.getElementsByClass("series");//取得大框
		
		
		
		
		
		for (int i = 0; i < elements.size(); i++) {
			Element msg = elements.get(i).getElementsByTag("a").first();
			String Url = placeUrl+msg.attr("href");
			//Log.i("翻轉前-Image", msg.getElementsByTag("img").first().attr("src"));//圖
			//Log.i("翻轉前-Name", msg.getElementsByTag("img").first().attr("title"));//名
			//Log.i("翻轉前-Url", Url);//連
		}
		
		return null;
	}

	
	
	
	
	@Override
	protected Adapter<ViewHolder> SendAdapter(ArrayList<ItemRotation> itemRotations,
			ArrayList<ItemCriterion> criterions) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
}
