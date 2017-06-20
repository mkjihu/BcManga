package com.bc_manga2.Adapder;

import com.bc_manga2.R;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.widget.ImageView;
import cn.lightsky.infiniteindicator.ImageLoader;

public class GlideLoader implements ImageLoader {

    public void initLoader(Context context) {

    }

	@Override
	public void load(Context context, ImageView targetView, Object res) {
		
		targetView.setScaleType(ImageView.ScaleType.FIT_XY);
		
		if (res instanceof String){
			
            Glide.with(context)
            	.load((String) res)
            	//.centerCrop()//中心切圖, 會填滿
            	.fitCenter()//中心fit, 以原本圖片的長寬為主
            	.error(R.mipmap.ikgbi)//load失敗的Drawable
            	.placeholder(R.mipmap.ikgbi)//loading時候的Drawable
            	.crossFade()//交叉淡入淡出
            	.into(targetView);
            
            
        }
		
		
	}

}
