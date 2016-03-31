package com.upc.learnmooc.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Explorer on 2016/1/18.
 */
public class ToastUtils {

	/**
	 * 长时间的土司
	 */
	public static void showToastLong(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 短时间的土司
	 */
	public static void showToastShort(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
	}
}
