package com.bc_manga2.Resolve.Index;

import java.util.ArrayList;
import java.util.List;

import com.bc_manga2.Application.GivenHttp;
import com.bc_manga2.Model.Cminfo;
import com.bc_manga2.obj.UnicodeDecoder;
import com.google.gson.Gson;

import android.util.Log;
import de.greenrobot.BcComicdao.BcIndexData;

public class BI_k886 extends IndexRexolveB{

	public BI_k886(String Html, String PKUrl, String HomePK, boolean interpret) {
		super(Html, PKUrl, HomePK, interpret);
	}

	@Override
	protected String GePKtUrl(String Html) {
		return PKUrl;
	}

	@Override
	protected BcIndexData HtmlRoInfo(String Html) {
		String okstr = UnicodeDecoder.decode(Html);//--轉Unicode碼
		Cminfo cminfo = new Gson().fromJson(okstr, Cminfo.class);
		BcIndexData bcIndexData = new BcIndexData();
		bcIndexData.setImageUrl(cminfo.getmIconUrl());
		bcIndexData.setTitleName(cminfo.getmName());
		bcIndexData.setAuthorName(cminfo.getmAuthor());
		bcIndexData.setType(cminfo.getmType());
		bcIndexData.setUpdated_day(cminfo.getmDate());
		Log.i("1", cminfo.getmIntroduction());
		bcIndexData.setGist(cminfo.getmIntroduction());
		
		return bcIndexData;
	}

	@Override
	protected ArrayList<String> HtmlItemUrls(String Html) {
		String okstr = UnicodeDecoder.decode(Html);//--轉Unicode碼
		Cminfo cminfo = new Gson().fromJson(okstr, Cminfo.class);
		ArrayList<String> Urls = new ArrayList<String>();
		int max_id = (Integer.valueOf(cminfo.getmTotalNum()) / 100)+1 ;//-除100算剩餘頁數
		String ukurl = PKUrl.substring(0,PKUrl.length()-1);
		for (int i = 1; i <= max_id; i++) {
			Urls.add(ukurl+i);
		}
		return Urls;
	}

	@Override
	protected ArrayList<ItemComicIndex> HtmlItems(List<String> ItemUrls) {
		ArrayList<ItemComicIndex> comicIndexs = new ArrayList<ItemComicIndex>();
		for (int i = 0; i < ItemUrls.size(); i++) {
			String okstr = UnicodeDecoder.decode(ItemUrls.get(i));//--轉Unicode碼
			Cminfo cminfo = new Gson().fromJson(okstr, Cminfo.class);
			for (int j = 0; j < cminfo.getmSelectArray().size(); j++) {
				
				String ItemUrl = GivenHttp.k886_Image+cminfo.getmSelectArray().get(j).getId();
				String ItemName = cminfo.getmSelectArray().get(j).getName();
				
				comicIndexs.add(new ItemComicIndex("k886", ItemUrl, ItemName));
			}
		}
		return comicIndexs;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
