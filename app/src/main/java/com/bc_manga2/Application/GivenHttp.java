package com.bc_manga2.Application;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class GivenHttp 
{
	public static final String MainName="bcmanga";
	/**資料庫名稱*/
	public static final String SQLName="BcCmangaDB-db";
	
	/**指定首頁文件緩存路徑*/
	public static final String HomeHtmlPath = Environment.getExternalStorageDirectory()+"/"+MainName+"/Folder/HomeAnFolder";
	/**指定首頁圖片緩存路徑*/
	public static final String HomeImagePath = Environment.getExternalStorageDirectory()+"/"+MainName+"/Cache/HomeImageFolder";
	/**指定目錄頁圖片緩存路徑*/
	public static final String IndexImagePath = Environment.getExternalStorageDirectory()+"/"+MainName+"/Cache/IndexImageFolder";
	/**指定目錄頁文件緩存路徑*/
	public static final String IndexHtmlPath = Environment.getExternalStorageDirectory()+"/"+MainName+"/Cache/IndexHtmlFolder";
	
	
	/**緩存路徑*/
	public static final String CachePath =  Environment.getExternalStorageDirectory()+"/"+MainName+"/Cache";
	
	
	public static final String FrescoCache = "FrescoCache";
	public static final String GlideCache = "GlideCache";
	
	
	
	//public static final String ssoomm ="http://www.ssoomm.com/";
	//public static final String somanhua ="http://www.somanhua.com/";
	
	public static final String comic99770 ="http://mh.99770.cc/";
	public static final String hhxxee99 ="http://99.hhxxee.com/";
	public static final String cartoonmad ="http://www.cartoonmad.com/";	
	public static final String iimanhua ="http://www.2manhua.com/";	
	public static final String comicbus ="http://www.comicbus.com/comic/";	
	public static final String animx ="http://www.2animx.com/";
	public static final String ikanman ="http://www.ikanman.com/";//--***
	public static final String mh57 ="http://www.57mh.com/";
	
	
	public static final String wnacg ="http://www.wnacg.com";
	//======已OK========
	public static final String ck101 ="http://comic.ck101.com/";//--*
	public static final String k886 ="http://www.k886.net/index-update";
	public static final String k886_Image ="http://www.k886.net/index.php/api-series?id=";//-Image
	public static final String k886_Index ="http://www.k886.net/index.php/api-info?id=%s&page=1";//-Index
	public static final String k886_SortFine ="http://www.k886.net/index.php/api-update-page-%s-category-";//-SortFine
	
	
	
	public final static String[] GivenHttps = 
	{
		"ck101","k886","wnacg"
	};
	
	/**
	 * 給編號回傳目前指定網頁
	 */
	/*
	public static String getGivenHttp(int HttpKey)
	{
		switch (HttpKey) {
		case 0:
			return ck101;
		case 1:
			return animx;
		case 2:
			return twcomic;
		case 3:
			return comic99;
		case 4:
			return comic99770;
		}
		return ck101;
	}
	*/
	
	/***
	 * Drawable 轉 Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {  
        // 取 drawable 的长宽  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
  
        // 取 drawable 的颜色格式  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // 建立对应 bitmap  
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
        // 建立对应 bitmap 的画布  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        // 把 drawable 内容画到画布中  
        drawable.draw(canvas);  
        return bitmap;  
    }  
	
	
	/**Bitmap转换成Drawable*/
	public static Drawable BitmaTopdrawable(Activity activity,Bitmap bm)
	{  
		Resources res = activity.getResources(); 
		BitmapDrawable bd= new BitmapDrawable(res, bm); //Bitmap bm=xxx; //xxx根据你的情况获取    
		return bd;//因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
	}
}
