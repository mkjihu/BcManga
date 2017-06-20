package com.bc_manga2.Network;

import com.android.volley.toolbox.HurlStack;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory; 
  
/**
 * Android API 23 之後請使用這個
	前面OkHttpStack已经不支持OkHttp2.0，见How to implement Android Volley with OkHttp 2.0? 。
	现在的解决办法是：
	下载或者设置对okhttp-urlconnection的依赖。
	使用OkUrlFactory类重写OkHttpStack ：
 */


public class OkHttpStack3 extends HurlStack
{

	 private OkHttpClient okHttpClient;

	 /**
	  * Create a OkHttpStack with default OkHttpClient.
	  */
	 public OkHttpStack3() {
	     this(new OkHttpClient());
	 }

	 /**
	  * Create a OkHttpStack with a custom OkHttpClient
	  * @param okHttpClient Custom OkHttpClient, NonNull
	  */
	 public OkHttpStack3(OkHttpClient okHttpClient) {
	     this.okHttpClient = okHttpClient;
	 }

	 @Override
	 protected HttpURLConnection createConnection(URL url) throws IOException {
	     OkUrlFactory okUrlFactory = new OkUrlFactory(okHttpClient);
	     return okUrlFactory.open(url);
	 }
	
} 