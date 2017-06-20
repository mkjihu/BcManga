package com.bc_manga2.Activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.bc_manga2.R;
import com.bc_manga2.Ui.ProgressBar.ArrowDownloadButton;
import com.bc_manga2.View.maView;
import com.jakewharton.rxbinding2.view.RxView;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import io.reactivex.functions.Consumer;

public class SplashPage extends AppCompatActivity implements maView {

	private ImageView sagerjkoj;
	private ArrowDownloadButton button;
	private int count = 0;
	private int progress = 0;
	private Timer timer;
	
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_page);
		findViewById();
		
		try {
	        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				//申请WRITE_EXTERNAL_STORAGE权限
				Log.i("問", "問");
				ActivityCompat.requestPermissions(this, new String[]{
						Manifest.permission.READ_EXTERNAL_STORAGE
						,Manifest.permission.WRITE_EXTERNAL_STORAGE
						, Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_PERMISSIONS);
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}

        
        
        
        RxView.clicks(button)
		.throttleFirst(1, TimeUnit.SECONDS)
    	.subscribe(new Consumer<Object>() {
			@Override
			public void accept(Object arg0) throws Exception {
				if (count== 0) 
                {
            		count++;
                	button.startAnimating();
                    timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            runOnUiThread(new Runnable() 
                            {
                                @Override
                                public void run() 
                                {
                                    progress = progress + 1;
                                    button.setProgress(progress);
                                    if(button.getProgress()>=100)
                                    {
                                    	timer.cancel();
                                    	button.setVisibility(View.GONE);
                                    	amuudng();
                                    }
                                }
                            });
                        }
                    }, 800, 20);//启动定时任务，从现在起过0.8秒以后，每隔0.02秒执行壹次
                }
			}
    	});
		
		
		
	    	
	}
	
	
	private void findViewById() 
	{
		sagerjkoj = (ImageView)findViewById(R.id.sagerjkoj);
		button = (ArrowDownloadButton)findViewById(R.id.arrow_download_button);
		sagerjkoj.setAlpha(0f);
		sagerjkoj.setVisibility(View.GONE);
		
	}
	
	
	private void amuudng() //淡入
	{
		sagerjkoj.setVisibility(View.VISIBLE);
		sagerjkoj.animate()
        .alpha(1f)
        .setDuration(2000)
        .setListener(new AnimatorListenerAdapter() 
        {
        	@Override
            public void onAnimationEnd(Animator animation) 
        	{
               Intent intent = new Intent(SplashPage.this,MainActivity.class);
               intent.putExtra("Preset_item_key", "0");
               startActivity(intent);
               overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left); 
               finish();
            }
        });
		
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}	
}
