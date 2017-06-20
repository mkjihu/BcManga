package com.bc_manga2.Presenter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import com.bc_manga2.R;
import com.bc_manga2.Application.BcApplication;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Fragment.Fragment1_Home;
import com.bc_manga2.Network.HttpApiClient;
import com.bc_manga2.Resolve.Home.Resolve_Ck101;
import com.bc_manga2.Resolve.Home.Resolves_k886;
import com.bc_manga2.obj.LogU;
import com.bc_manga2.obj.ToastUnity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.ResponseBody;

public class Fragment1_HomePresenter extends BasePresenter<Fragment1_Home> {

	public int EmptyData = 0;//空數據獲取狀態//--0為正常

	public Fragment1_HomePresenter(Fragment1_Home view) {
		super(view);
	}

	/**
	 * @param Url
	 * @param isload 显示加载-等于强制刷新资料库
	 */
	public void GetHtmlDate(final String Url,final boolean isload) {

		LogU.i("Url", Url);
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
						LogU.i("內容", "A)A"+Html);
						LogU.i("Home", Home);
						if (Html.equals("")) { //-獲取空數據 重新取
							EmptyData = 1;
						}
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
				.onErrorResumeNext(new Function<Throwable, Publisher<Adapter<ViewHolder>>>() {
					@Override
					public Publisher<Adapter<ViewHolder>> apply(@NonNull Throwable e) throws Exception {
						LogU.i("出錯", "出錯"+e.getLocalizedMessage());
						if (EmptyData==1) {
							return EmptyDataAcquisition(Url);//-無限重覆取數據
						}
						else
						{
							if (e instanceof TimeoutException || e instanceof SocketTimeoutException || e instanceof UnknownHostException ){
								ToastUnity.ShowTost(getView().getContext(), getView().getResources().getString(R.string.on_Network_Error));
							}
							String Home = Shared.GetHomeUrlkey(getView().getContext());
							switch (Home) {
								case "ck101":
									return Flowable.just(new Resolve_Ck101(Home , "" , Url , getView()).SendAdapter(isload));
								case "k886":
									return Flowable.just(new Resolves_k886(Home , "" , Url , getView()).SendAdapter(isload));
								default:
									return Flowable.just(new Resolve_Ck101(Home , "" , Url , getView()).SendAdapter(isload));
							}
						}
					}
				})
				.onBackpressureBuffer()//加上背压策略 - 無限大
				.unsubscribeOn(Schedulers.io())//允許取消訂閱
				.observeOn(AndroidSchedulers.mainThread())//--結果在主線程中顯示
				.subscribeWith(new DisposableSubscriber<Adapter<ViewHolder>>() {
					@Override
					public void onError(Throwable e) {
						LogU.i("GG", e.getMessage());
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


	/**取到數據為止*/
	public Flowable<Adapter<ViewHolder>> EmptyDataAcquisition(final String Url)
	{
		return isMobile(Url)
				.retryWhen(new HttpApiClient.RetryWithDelay(10, 1000))//总共重试10次，重试间隔1秒
				.repeatWhen(new Function<Flowable<Object>, Publisher<?>>() {
					@Override
					public Publisher<?> apply(@NonNull Flowable<Object> observable) throws Exception {
						/**这个方法只会被调用一次。1 表示每次重复的调用（repeated call）会被延迟1s。*/
						LogU.i("輪詢", "1秒後-重查");
						return observable.delay(2, TimeUnit.SECONDS);
					}
				})
				.takeUntil(new Predicate<String>() {
					@Override
					public boolean test(@NonNull String s) throws Exception {
						/** 在这里，我们可以检查服务器返回的数据是否正确，和决定我们是否应该
						 *  停止轮询。
						 *  当服务器的任务完成时，我们停止轮询。
						 *  换句话说，“当任务（job）完成时，我们不拿（take）了”
						 */
						if (s.equals("")) {
							return false;//-繼續輪巡
						} else {
							return true;
						}
					}
				})
				.filter(new Predicate<String>() {
					@Override
					public boolean test(@NonNull String s) throws Exception {
						/**
						 * 如果我们在这里返回“false”的话，那这个结果会被过滤掉（filter）
						 * 过滤（Filtering） 表示 onNext() 不会被调用.
						 * 但是 onComplete() 仍然会被传递.
						 */
						if (s.equals("")) {
							return false;//-繼續輪巡
						} else {
							return true;
						}
					}
				})
				//.timeout(20, TimeUnit.SECONDS)///超時為10 秒

				.map(new Function<String, Adapter<ViewHolder>>()  {
					@Override
					public Adapter<ViewHolder> apply(String Html) throws Exception {

						String Home = Shared.GetHomeUrlkey(getView().getContext());
						LogU.i("轮询內容", "A)A"+Html);
						LogU.i("轮询Home", Home);
						if (Html.equals("")) { //-獲取空數據 重新取
							EmptyData = 1;
						}
						switch (Home) {
							case "ck101":
								return new Resolve_Ck101(Home , Html , Url , getView()).SendAdapter(true);
							case "k886":
								return new Resolves_k886(Home , Html , Url , getView()).SendAdapter(true);
							default:
								return new Resolve_Ck101(Home , Html , Url , getView()).SendAdapter(true);
						}
					}
				})
				;








	}

}
