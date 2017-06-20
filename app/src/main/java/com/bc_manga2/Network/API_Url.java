package com.bc_manga2.Network;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
public interface API_Url {
    @GET("/")
    Call<String> gret1();

    @Headers("Cache-Control: public, max-age=3600")
    @GET("https://tw.yahoo.com/")
    Call<String> gret_11();


    @GET("https://{url}/")
    Call<String> gret_12(@Path("url") String url);

    //@FormUrlEncoded
    @GET("/")
    Observable<String> gret2();
    
    
    //@Headers("Cache-Control: public, max-age=3600")
    @GET("https://tw.yahoo.com/")
    Observable<String> gret21();
    
    //@Headers("Cache-Control: max-age=62")
    @GET
    Observable<String> GetHtml_O(@Url String url);
    
    @GET
    Observable<String> GetHtml2_O(@Url String url, @Header("User-Agent") String UserAgent);
    
    
    //@Headers("Cache-Control: max-age=62")
    @GET
    Flowable<String> GetHtml_F(@Url String url);
    
    @GET
    Flowable<String> GetHtml2_F(@Url String url, @Header("User-Agent") String UserAgent);
    
    
    /**真對需要回應內容轉編碼--傳回未解編碼狀態*/
    @GET
    Flowable<ResponseBody> GetHtml2_F_ResponseBody(@Url String url);
    /**POST編碼Big5 格式內容--傳回未解編碼狀態*/
    @POST
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=big5")
    Flowable<ResponseBody> GetHtml2_F_big5(@Url String url, @Field(encoded = true, value = "keyword") String keyword, @Field("searchtype") String searchtyp);
     
}
