package com.bc_manga2.Presenter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException; 
import org.reactivestreams.Subscription; 
import com.bc_manga2.R;
import com.bc_manga2.Application.BcApplication; 
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Fragment.Fragment1_Home; 
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Home.Resolve_Ck101;
import com.bc_manga2.Resolve.Home.Resolves_k886;
import com.bc_manga2.obj.ToastUnity;  
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log; 
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.ResponseBody; 

public class Fragment1_HomePresenter extends BasePresenter<Fragment1_Home> {

	public Fragment1_HomePresenter(Fragment1_Home view) {
		super(view);
	}

	/**
	 * @param Url
	 * @param isload 显示加载-等于强制刷新资料库
	 */
	public void GetHtmlDate(final String Url,final boolean isload) {
		
		Log.i("Url", Url);
		Disposable d = isMobile(Url) 
				.subscribeOn(Schedulers.io())//--跑在線程背後--只讀一次
				//总共重试3次，重试间隔3000毫秒
                //.retryWhen(new HttpApiClient.RetryWithDelay(3, 3000))
				.doOnSubscribe(new Consumer<Subscription>() {
					@Override
					public void accept(Subscription arg0) throws Exception {
						//if (isload) //显示加载-等于强制刷新资料库
						getView().openRefresh();	
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread()) // 指定上面的doOnSubscribe跑主线程
				.map(new Function<String, Adapter<ViewHolder>>()  { 
					@Override
					public Adapter<ViewHolder> apply(String Html) throws Exception {
						
						String Home = Shared.GetHomeUrlkey(getView().getContext());
						Log.i("Home", Home);
						switch (Home) {
						case "ck101":
							return new Resolve_Ck101(Home , Html , Url , getView()).SendAdapter(isload);
						case "k886":
							return new Resolves_k886(Home , Html , Url , getView()).SendAdapter(isload);
						default:
							return new Resolve_Ck101(Home , Html , Url , getView()).SendAdapter(isload);
						}
					}
				})
				/**
				  * 錯誤或是沒有網路時攔截 - 使用該攔截 一定會進入onNext()
				  * onErrorReturn()，在遇到错误时发射一个特定的数据
					onErrorResumeNext()，在遇到错误时发射一个数据序列
				  */
				.onErrorReturn(new Function<Throwable, Adapter<ViewHolder>>() { 
					@Override
					public Adapter<ViewHolder> apply(Throwable e) throws Exception {
						Log.i("出錯", "出錯"+e.getLocalizedMessage());
						if (e instanceof TimeoutException || e instanceof SocketTimeoutException || e instanceof UnknownHostException ){
							ToastUnity.ShowTost(getView().getContext(), getView().getResources().getString(R.string.on_Network_Error));
						}
						String Home = Shared.GetHomeUrlkey(getView().getContext());
						switch (Home) {
						case "ck101":
							return new Resolve_Ck101(Home , "" , Url , getView()).SendAdapter(isload);
						case "k886":
							return new Resolves_k886(Home , "" , Url , getView()).SendAdapter(isload);
						default:
							return new Resolve_Ck101(Home , "" , Url , getView()).SendAdapter(isload);
						}
					}
				})
				.onBackpressureBuffer()//加上背压策略 - 無限大
				.unsubscribeOn(Schedulers.io())//允許取消訂閱  
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示  
				.subscribeWith(new DisposableSubscriber<Adapter<ViewHolder>>() { 
					@Override
					public void onError(Throwable e) {
						Log.i("GG", e.getMessage());
						getView().offRefresh();
						if (e instanceof TimeoutException || e instanceof SocketTimeoutException || e instanceof UnknownHostException ){
							ToastUnity.ShowTost(getView().getContext(), getView().getResources().getString(R.string.on_Network_Error));
						}
						
					}
					@Override
					public void onNext(Adapter<ViewHolder> adapter) {
						getView().offRefresh();
						getView().RecyclerViewsetData(adapter);
					}
					@Override
					public void onComplete() { }
				});
		getDisposable().add(d);	
	}
 
	/**分門別類*/
	public Flowable<String> isMobile(String Url)
	{
		String Home = Shared.GetHomeUrlkey(getView().getContext());
		switch (Home) {
		case "ck101":
			return HttpApiClient.getInstance().GetHtml_F(Url);
		case "k886":
			return HttpApiClient.getInstance().GetHtml_F(Url);
		case "wnacg":
			return HttpApiClient.getInstance().GetHtml2_F(Url,BcApplication.getInstance().getUserAgent());
		case "cartoonmad":
			return HttpApiClient.getInstance().GetHtml2_F_ResponseBody(Url)
					.map(new Function<ResponseBody, String>() {
						@Override
						public String apply(ResponseBody Html) throws Exception {
							String Htmlbig5 = null; 
							Htmlbig5 = new String(Html.bytes(), "big5");//--取得Html 經過big5解碼
							return Htmlbig5;
						}
					});
		default:
			return HttpApiClient.getInstance().GetHtml_F(Url);
		}
	}
	
}
