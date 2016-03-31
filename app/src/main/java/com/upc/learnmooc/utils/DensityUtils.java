package com.upc.learnmooc.utils;

import android.content.Context;

/**
 * Created by Explorer on 2016/1/16.
 */
public class DensityUtils {

	/**
	 * dp转px
	 */
	public static int dp2px(Context ctx, float dp) {
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density + 0.5f);// 4.9->5 4.4->4

		return px;
	}


	/**
	 * px转dp
	 * @param ctx
	 * @param px
	 * @return
	 */
	public static float px2dp(Context ctx, int px) {
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;

		return dp;
	}
}
