package com.bc_manga2.Network;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Application.GivenHttp;

import android.util.Log;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpApiClient
{
    public static OkHttpClient client;
    public static Retrofit retrofit;
    
    public static API_Url api_Url;
    
    //--API接口啟動
    public static API_Url getInstance() {
        if (api_Url == null)  {
        	api_Url = getRetrofit().create(API_Url.class);
        }
        return api_Url;
    }
    
    //-retrofit啟動
	public static Retrofit getRetrofit() {
		if (retrofit==null) {
			initialize();
		}
		return retrofit;
	}
	
	public static Flowable<String> getHtml(String Url)
	{
		getRetrofit();	
		api_Url = retrofit.create(API_Url.class);
		return api_Url.GetHtml_F(Url);
	}
	public static Observable<String> getHtml_O(String Url)
	{
		getRetrofit();	
		api_Url = retrofit.create(API_Url.class);
		return api_Url.GetHtml_O(Url);
	}
	
    //--宣告啟動
    public static void initialize()
    {
    	
    	HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	    File cacheFile = new File(GivenHttp.CachePath);  
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb

        client = new OkHttpClient().newBuilder()
        		.cache(cache)
        		//.addInterceptor(logging)//--攔截器-输出网络请求和结果的 Log
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)//方法为设置出现错误进行重新连接。
                .addNetworkInterceptor(new MInterceptor()) //-让所有网络请求都附上你的拦截器---ok
                .addInterceptor(new MInterceptor()) //-让所有网络请求都附上你的拦截器---ok
                //.addNetworkInterceptor(mRewriteCacheControlInterceptor) //---ok
                //.addInterceptor(mRewriteCacheControlInterceptor) //---ok
                .build();
        
        
        retrofit = new Retrofit.Builder()
        		.baseUrl("https://www")//.baseUrl("https://www.google.com.tw/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

	public static class MInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            //-设置max-age为60s之后，这60s之内不管你有没有网,都读缓存。max-stale设置为1天，意思是，网络未连接的情况下设置缓存时间为1天。
        	//拦截reqeust
            Request request = chain.request();
            
            //判断网络连接状况
            if (BcApplication.isNetworkReachable()) {
            	//Log.i("有網1", "有網1");
            	request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();//有網路就從網路取
            } else {
            	//无网络时只从缓存中读取
            	//Log.i("沒有網1", "沒有網1");
            	//request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            	request = request.newBuilder().cacheControl(new CacheControl.Builder()
            			.onlyIfCached()
            			.maxAge(90, TimeUnit.SECONDS) //设置有效时间 沒有網路90秒內讀取緩存
            			.maxStale(360, TimeUnit.SECONDS)//设置最大失效时间，失效则不使用   网络未连接的情况下设置缓存时间最常可讀取为360秒
            			.build()).build();              
            }
            
        	Response originalResponse = chain.proceed(request);
        	
            if (BcApplication.isNetworkReachable()) {
            	//Log.i("有網2", "有網2");
                int maxAge = 60; // 有网络时 设置缓存超时时间0.5个小时
                return originalResponse.newBuilder()
                		.header("Cache-Control", "public, max-age="+maxAge) //-----緩存60秒
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
            	//Log.i("沒有網2", "沒有網2---超时");
                int maxStale = 60 * 2; // 无网络时，设置超时为3天
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale="+maxStale) //设置缓存策略，及超时策
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
    
    //RxJava 用失敗重試
    public static class RetryWithDelay implements Function<Flowable<? extends Throwable>, Flowable<?>> {

        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(int maxRetries, int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
        }

		@Override
		public Flowable<?> apply(Flowable<? extends Throwable> attempts) throws Exception {
			 return attempts
	                    .flatMap(new Function<Throwable, Flowable<?>>() {

							@Override
							public Flowable<?> apply(Throwable throwable) throws Exception {
								 if (++retryCount <= maxRetries) {
		                                // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
		                                Log.i("重試", "重試");
		                                return Flowable.timer(retryDelayMillis,
		                                        TimeUnit.MILLISECONDS);
		                            }
		                            // Max retries hit. Just pass the error along.
		                            return Flowable.error(throwable);
							}
	                    });
		}
    }
}
