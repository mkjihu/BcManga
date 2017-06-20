package com.bc_manga2.Adapder;

import java.io.File;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.content.Context;
import android.widget.ImageView;
import cn.lightsky.infiniteindicator.ImageLoader;

public class PicassoLoader implements ImageLoader {

    public PicassoLoader getImageLoader(Context context) {
        return new PicassoLoader();
    }


    @Override
    public void load(Context context,ImageView targetView, Object res) {
        if (res == null) {
            return;
        }

        Picasso picasso = Picasso.with(context);
        RequestCreator requestCreator = null;

        if (res instanceof String) {
            requestCreator = picasso.load((String) res);
        } else if (res instanceof File) {
            requestCreator = picasso.load((File) res);
        } else if (res instanceof Integer) {
            requestCreator = picasso.load((Integer) res);
        }

        requestCreator
        		//.placeholder(R.drawable.twa_default)//讀取顯示圖片
        		//.error(R.drawable.twa_default)
                //.centerCrop()
                //.centerInside
                .fit()
                //.centerInside()
                .tag(context)
                .into(targetView);

    }

}