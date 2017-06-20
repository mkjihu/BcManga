package com.bc_manga2.Application;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
	
	//---紀錄選取Url
	public static void SetHomeUrl(Context context,String Url) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("HomeUrl", Url);
		editor.commit();
	}
	
	//--取得選取Url
	public static String GetHomeUrl(Context context) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		String Url = settings.getString("HomeUrl",GivenHttp.k886);
		return Url;
	}

	//---紀錄選取HomeUrlkey
	public static void SetHomeUrlkey(Context context,String Home) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("Home", Home);
		editor.commit();
	}	
	//--取得選取HomeUrlkey
	public static String GetHomeUrlkey(Context context) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		String Url = settings.getString("Home","k886");
		return Url;
	}
	
	/*
	//---紀錄選取分類頁Url
	public static void SetSortUrl(Context context,String Url) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("Url", Url);
		editor.commit();
	}
	
	//--取得選取分類頁Url
	public static String GetSortUrl(Context context) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		String Url = settings.getString("HomeUrl",GivenHttp.ck101);
		return Url;
	}
	*/
	
	
	/**是否取純圖片列表*/
	public static void SetIsImageAnalysis(Context context,boolean ImageA) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("IsImageAnalysis", ImageA);
		editor.commit();
	}	
	/**是否取純圖片列表*/
	public static boolean GetIsImageAnalysis(Context context) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		return settings.getBoolean("IsImageAnalysis", true);
	}
	
	
	/**閱讀方式是否橫向*/
	public static void SetIReadingMethod(Context context,boolean Method) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("ReadingMethod", Method);
		editor.commit();
	}	
	/**閱讀方式是否橫向*/
	public static boolean GetReadingMethod(Context context) {
		SharedPreferences settings = context.getSharedPreferences(GivenHttp.MainName, 0);
		return settings.getBoolean("ReadingMethod", true);
	}
	
}
