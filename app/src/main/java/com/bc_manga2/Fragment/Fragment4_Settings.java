package com.bc_manga2.Fragment;

import java.util.concurrent.TimeUnit;

import org.greenrobot.eventbus.EventBus;

import com.bc_manga2.R;
import com.bc_manga2.Adapder.HomeAdapder.Actiaon1;
import com.bc_manga2.Application.GivenHttp;
import com.bc_manga2.Application.Shared;
import com.bc_manga2.Application.TagInfo;
import com.bc_manga2.Model.SpinnerData;
import com.bc_manga2.Model.EventBus.MessageEvent;
import com.bc_manga2.View.maView;
import com.bc_manga2.obj.ToastUnity;
import com.jakewharton.rxbinding2.view.RxView;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import io.reactivex.functions.Consumer;

public class Fragment4_Settings extends BaseFragment implements OnTouchListener,maView
{
	private TextView Sett_Version,CheckHome_tv;
	private FrameLayout Version,ClearCache,CheckHome,ReadingDirection,loadMode,SearchSettings;
	
	private AlertDialog.Builder dialog;
	private ArrayAdapter<SpinnerData> selsehome;
	
	
	//設定
	public Fragment4_Settings() {
		
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	protected void findViewById(View view) {
		ClearCache = (FrameLayout)view.findViewById(R.id.ClearCache);
		CheckHome = (FrameLayout)view.findViewById(R.id.CheckHome);
		ReadingDirection = (FrameLayout)view.findViewById(R.id.ReadingDirection);
		loadMode = (FrameLayout)view.findViewById(R.id.loadMode);
		SearchSettings = (FrameLayout)view.findViewById(R.id.SearchSettings);
		
		CheckHome_tv = (TextView)view.findViewById(R.id.CheckHome_tv);
		CheckHome_tv.setText(Shared.GetHomeUrlkey(getActivity()));
		
		dialog = new AlertDialog.Builder(getActivity(),R.style.RadioDialogTheme);
		initclick();
	}

	private void initclick() {
		RxView.clicks(CheckHome)
		.throttleFirst(1, TimeUnit.SECONDS)
		.subscribe(new Consumer<Object>(){
			@Override
			public void accept(Object arg0) throws Exception {
				dialog.show();
			}
		});
		
		selsehome =  new ArrayAdapter<SpinnerData>(getActivity(), android.R.layout.select_dialog_item);
		selsehome.add(new SpinnerData("ck101", "ck101"));
		selsehome.add(new SpinnerData("k886", "k886"));
		selsehome.add(new SpinnerData("wnacg", "wnacg"));
		dialog.setTitle("選擇加載主頁");

		dialog.setSingleChoiceItems(selsehome,0, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (selsehome.getItem(which).toString()) {
				case "ck101":
					Shared.SetHomeUrl(getActivity(), GivenHttp.ck101);
					break;
				case "k886":
					Shared.SetHomeUrl(getActivity(), GivenHttp.k886);
					break;
				case "wnacg":
					//Shared.SetHomeUrl(getActivity(), GivenHttp.wnacg);
					ToastUnity.ShowTost(getActivity(), "準備中");
					break;
				}
				CheckHome_tv.setText(selsehome.getItem(which).toString());
				Shared.SetHomeUrlkey(getActivity(), selsehome.getItem(which).toString());
				
				EventBus.getDefault().post(new MessageEvent(TagInfo.ChangeHome));
				dialog.dismiss();
			}
		});
		
	}
	
	@Override
	public void ToComicDirectory(Object object) {
		
	}
	

	@Override
	protected int getLayoutResource() {
		return R.layout.fragment4_settings;
	}


	

	

	
	
}
