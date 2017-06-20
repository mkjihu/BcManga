package com.bc_manga2.Resolve.Image;

import de.greenrobot.BcComicdao.BcImageData;
import io.reactivex.Flowable;

public interface ImageInter {

	abstract Flowable<BcImageData> GetBcImageData();
	/**判定是否需要刷新資料*/
	boolean UpdateTimeContrast();
}
