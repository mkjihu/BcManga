package com.bc_manga2.Network;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.Application.GivenHttp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory.CacheDirectoryGetter;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import android.content.Context;
import android.os.Environment;
import okhttp3.OkHttpClient;

public class MyGlideModule implements GlideModule 
{
	Context context;
	int maxSize = 300 * 1024 * 1024; //将缓存图片的大小设置为20M  //磁盘缓存大小
	public MyGlideModule(){}
	public MyGlideModule(Context context,int maxSize) {
		this.context = context;
		//this.maxSize = maxSize;
	}
    @Override 
    public void applyOptions(Context context, GlideBuilder builder) {
       
    	/**方法1*/
    	//内存缓存
        //int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        //int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
    	int memoryCacheSize = 100*1024*1024;
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        //-设置BitmapPool缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
        
        
    	/**方法2*/
    	//ExternalCacheDiskCacheFactory代表/sdcard/Android/data/<application package>/cache
    	//InternalCacheDiskCacheFactory代表/data/data/<application package>/cache
    	//一个是sdcard路径,另外一个则是自身路径
        //String cacheDir = "imgcache";
        //builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheDir, 250 * 1024 * 1024));
        //builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheDir, 250 * 1024 * 1024));
    	
    	/**方法3*/
    	//File ext = Environment.getExternalStorageDirectory();
        //File cacheDir = new File(ext, "itr/Glid/im");//指定的是数据的缓存地址
        //设置磁盘缓存大小
        //builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), maxSize));
        
        /**方法4*/
        builder.setDiskCache(new DiskLruCacheFactory(new CacheDi(), maxSize));
        
        
        /**方法5*/
        //-设置图片解码格式 - 默认格式RGB_565使用内存是ARGB_8888的一半，但是图片质量就没那么高了，而且不支持透明度
        //builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        
        
    }

    @Override 
    public void registerComponents(Context context, Glide glide) {
    	OkHttpClient client = new OkHttpClient().newBuilder()
    			.connectTimeout(60, TimeUnit.SECONDS)
    			.readTimeout(60,TimeUnit.SECONDS).build();
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
        glide.register(GlideUrl.class, InputStream.class, factory);
    	//glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
    
    
    public class CacheDi implements CacheDirectoryGetter
	{
		@Override
		public File getCacheDirectory() {
		   	//File ext = Environment.getExternalStorageDirectory();
	        File cacheDir = new File(GivenHttp.CachePath,GivenHttp.GlideCache);
			return cacheDir;
		}
	}    
}