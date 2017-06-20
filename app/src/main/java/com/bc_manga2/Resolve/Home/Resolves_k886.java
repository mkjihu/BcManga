package com.bc_manga2.Resolve.Home;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bc_manga2.Adapder.HomeAdapder;
import com.bc_manga2.Application.GivenHttp;
import com.bc_manga2.Fragment.BaseFragment;
import com.google.gson.Gson;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;

public class Resolves_k886 extends HomeResolve<Adapter<ViewHolder>>{
	
	private Document xmlDoc;	
	private BaseFragment fragment;
	
	
	public Resolves_k886(String DataPK, String Html, String placeUrl,BaseFragment fragment) {
		super(DataPK, Html, placeUrl);
		xmlDoc = Jsoup.parse(Html);
		this.fragment = fragment;
	}

	@Override
	protected ArrayList<ItemRotation> HtmlRotationItem(String Html) {
		//-沒有輪播列
		ArrayList<ItemRotation> itemRotations = new ArrayList<ItemRotation>();
//
//		String HomePageItemArrayGSON = new Gson().toJson(itemRotations);	
//		Log.i("savitemRotations", HomePageItemArrayGSON+" ");
		return itemRotations;
	}

	@Override
	protected ArrayList<ItemCriterion> HtmlCriterionItem(String Html) {

		
		Elements cont = xmlDoc.getElementsByClass("latest-cont");
		Elements list = xmlDoc.getElementsByClass("latest-list");
		
		ArrayList<ItemCriterion> arrayList = new ArrayList<>();
		
		for (int i = 0; i < cont.size(); i++) {
			//Log.i("latest-cont", cont.get(i).select("strong").text());//取得strong該項目
			ItemCriterion itemCriterion = new ItemCriterion();
			itemCriterion.setCritName(cont.get(i).select("strong").text());
			ArrayList<ItemRotation> itemRotations = new ArrayList<>();
			
			Elements li = list.get(i).select("li");//取得該項目li列表
			for (int j = 0; j < li.size(); j++) {
				Element ik = li.get(j).select("a").first();
				
				String image =ik.getElementsByTag("img").first().attr("src");
				String Name =ik.getElementsByClass("tit").first().text();
				String UpDate =ik.select("font").first().text();
				String newAion =ik.select("em").first().text();
				
				String id = ik.attr("href").replace("http://www.k886.net/index-comic-id-", "");//-取得id
				String url = String.format(GivenHttp.k886_Index, id);
				ItemRotation itemRotation = new ItemRotation(DataPK, image, Name, url, false);
				itemRotation.setUpDate(UpDate);
				itemRotation.setNewAion(newAion);
				itemRotations.add(itemRotation);
			}
			itemCriterion.setItemRotations(itemRotations);
			arrayList.add(itemCriterion);
		}
		return arrayList;
	}
	
	@Override
	protected Adapter<ViewHolder> SendAdapter(ArrayList<ItemRotation> itemRotations,ArrayList<ItemCriterion> criterions) {
		if (fragment !=null && criterions.size() != 0) {
			return new HomeAdapder(fragment, itemRotations, criterions);
		}
		return null;
	}

}
