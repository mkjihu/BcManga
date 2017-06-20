package com.bc_manga2.Application;


import java.io.File;
import java.io.InputStream;

import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Network.OkHttpImageDownloader;
import com.bc_manga2.Network.imagepipeline_okhttp3.OkHttpImagePipelineConfigFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebSettings;
import de.greenrobot.BcComicdao.DaoMaster;
import de.greenrobot.BcComicdao.DaoSession;
import okhttp3.OkHttpClient;
import de.greenrobot.BcComicdao.DaoMaster.DevOpenHelper;

public class BcApplication extends Application{
    
	private static Context context;
    private static BcApplication instance;
    //---SQL
    private DaoSession daoSession;
    private DevOpenHelper helper;
    
    
    
    
    /**Fresco 最大缓存图片300M */
    private static final long MAX_IMG_CACHE_SIZE = 1024 * 1024 * 300;
    
    public BcApplication() {
        instance = this;
    }
    
	@Override
	public void onCreate() {
		super.onCreate();
		BcApplication.context = getApplicationContext();
		instance = this;
		HttpApiClient.initialize();
	
		FrescoInit();
		//Glide 設定
		//Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));
		
		
	}
	
	//--Fresco 設定
	private void FrescoInit() {
		
	    File cacheDir = new File(GivenHttp.CachePath);//得到缓存的路径
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(cacheDir)//缓存文件的路径
                .setBaseDirectoryName(GivenHttp.FrescoCache)//缓存文件的名字
                .setMaxCacheSize(MAX_IMG_CACHE_SIZE).build();//缓存文件的最大值，必须设置
        //ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context).setMainDiskCacheConfig(diskCacheConfig).build();
        
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory
        	    .newBuilder(context, HttpApiClient.client)
        	    .setMainDiskCacheConfig(diskCacheConfig)
        	    .build();
		Fresco.initialize(this,imagePipelineConfig);
		/**/
		//Fresco.initialize(this);
	}
	
    public static Context getAppContext() {
        return BcApplication.context;
    }
    
    public static BcApplication getInstance() {
        return instance;
    }
    
    public DaoSession getDaoSession() 
	{
    	if (daoSession==null) {
    		initDaoSession();
		}
		return daoSession;
	}
    
    /**初始化DB*/
    public void initDaoSession() 
    {
    	/**宣告資料庫*/
		// 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
		helper = new DevOpenHelper(this, GivenHttp.SQLName, null);
    	SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
    }
   
	
    /**判定是否在主線程*/
    public static String isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper() ? "是主線程":"幹~不是主線程";
    }
    
    /**
     * 判断网络是否可用
     * @param context
     */
    public static Boolean isNetworkReachable() {
    	ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
	/**取得User-Agent*/
    public String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    //====Universal-Imageloader 設定=========================
	
	public static void initImageLoader(Context context) 
	{
		//设置缓存的路径
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache"); 

        
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY-2);//加載圖片的序列數
		config.denyCacheImageMultipleSizesInMemory();//拒绝缓存多个图片。
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//定義緩存文件名稱Md5FileNameGenerator()：通過Md5將url認為唯一名子
	
		config.memoryCacheExtraOptions(2048, 2048); //即保存的每个內存緩存文件的最大长宽 
		
		//config.memoryCacheSizePercentage(13);//限制內存的緩存百分比 （以可用的應用程序的內存百分比）。默認值-可用內存的應用程序1/8。
		//config.denyCacheImageMultipleSizesInMemory();//设置内存缓存不允许缓存一张图片的多个尺寸，默认允许。
		
		
		config.diskCacheExtraOptions(2048, 2048, new BitmapProcessor() {
			
			@Override
			public Bitmap process(Bitmap arg0) {
				Log.w("SD卡緩存", "SD卡緩存");
				Log.w("SD緩存高", arg0.getHeight()+"");
				Log.w("SD緩存寬", arg0.getWidth()+"");
				return arg0;
			}
		});//设置图片保存到本地的最大长宽    //BitmapProcessor 设置图片加入缓存前，对bitmap进行设置
		//注意..這只有第一次獲取圖片時會執行比於內存緩存還先執行
		
		
		
		config.diskCache(new UnlimitedDiskCache(cacheDir));//自定义缓存路径 --不指定大小
		//config.diskCacheSize(300 * 1024 * 1024); // 300 MiB// SD缓冲大小
		//config.diskCacheFileCount(300);//缓存的文件数量  
		//以上三个参数会互相覆盖，只使用一个
		
		//config.memoryCacheSize( 50 * 1024 * 1024);//內存緩衝大小50 MiB
		
		//memoryCache(...)//缓存策略
		//和memoryCacheSize(...)这两个参数会互相覆盖，所以在ImageLoaderConfiguration中使用一个就好了
		
		config.memoryCache(new WeakMemoryCache());//控制内存缓存管理new FIFOLimitedMemoryCache(50 * 1024 * 1024)
		//LruMemoryCache：當某一個bitmap被讀取時，它被移到隊首，當內存不足時，從隊尾清理。
		//UsingFreqLimitedMemoryCache當內存不足時，會先清沒什麼再用的
		//FIFOLimitedMemoryCache：先進先出原理去清理
		//LargestLimitedMemoryCache：當内存不足时，會清理sizes最大的值。
		//重點//如果限制的大小還是會跳錯 就給他new WeakMemoryCache()無限緩存
	
		
		
		
		/**
		 * BaseImageDownloader 这里超级重要，可以更改成OkHttpImageDownloader  okHttp
		 */
		config.imageDownloader(new OkHttpImageDownloader(context, new OkHttpClient()));
		//config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 10 * 1000));// connectTimeout (5 s), readTimeout (30 s)超时时间    
		//config.imageDownloader(new OkHttpImageDownloader(context, HttpApiClient.client));
		//當下載跟讀取超時多久時就去指定的drawable檔找
		//預設BaseImageDownloader 是connectTimeout(連線) 為5000 (5秒) readTimeout(讀取)為 20秒
		//可以自定.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
		
		config.imageDecoder(new BaseImageDecoder(false));//根据ImageView的宽高，ScaleType去裁剪图片
		
		config.tasksProcessingOrder(QueueProcessingType.LIFO);// 設定工作排序方式
		config.writeDebugLogs(); // Remove for release app
		
		//config.defaultDisplayImageOptions(DisplayImageOptions.createSimple()); //重要=加載LoaderImage的圖片顯示規則
		config.defaultDisplayImageOptions(options());//重要=加載LoaderImage的圖片顯示規則
        config.writeDebugLogs(); // 打印debug log 

		ImageLoader.getInstance().init(config.build());
		
	}

	
	public static DisplayImageOptions options()
	{
		//定義LoaderImage的圖片顯示規則
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		//.showImageOnLoading(R.drawable.ic_stub)//下載其間顯示的圖片
		//.showImageForEmptyUri(R.drawable.cuowu)//Uri找不到圖或是錯誤時的圖片
		//.showImageOnFail(R.drawable.xinshoushanglu)//加載，解碼過成錯誤的圖片
		.cacheOnDisk(true)//设置下载的图片是否缓存在SD中
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中
		
		.considerExifParams(false)//是否讀取圖片的旋轉參數
		
		
		//.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//圖像縮放類型
		.imageScaleType(ImageScaleType.NONE)//圖像縮放類型
		 //EXACTLY :图像将完全按比例缩小的目标大小
         //EXACTLY_STRETCHED:图片会缩放到目标大小完全
         //IN_SAMPLE_INT:图像将被二次采样的整数倍  除二的意思
         //預設IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
         //NONE:图片不会调整
		
		
		
		.postProcessor(new BitmapProcessor() {
			@Override
			public Bitmap process(Bitmap arg0) {
				Log.w("內存緩存", "內存緩存");
				Log.w("內存緩存高", arg0.getHeight()+"");
				Log.w("內存緩存寬", arg0.getWidth()+"");
				return arg0;
			}
		})
		
		
		
		//.delayBeforeLoading(200)//下載前延遲的時間1000=1秒 
		
		.displayer(new SimpleBitmapDisplayer())
		//.displayer(new RoundedBitmapDisplayer(20))//设置图片的显示方式
			//RoundedBitmapDisplayer（int roundPixels）设置圆角图片
			// FakeBitmapDisplayer（）这个类什么都没做
			// FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
			//SimpleBitmapDisplayer()正常显示一张图片
	
		.resetViewBeforeLoading(true)//圖片在下載前是否重置
		.bitmapConfig(Config.ARGB_8888)//圖片解碼類型~~32位
		//.bitmapConfig(Config.RGB_565)//圖片解碼類型--低解析16位
		.build();
		return options;
	}
	
    
    
}
