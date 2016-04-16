package com.upc.learnmooc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference工具类
 * Created by Explorer on 2016/1/16.
 */
public class PrefUtils {
	private final static String PREF_NAME = "config";

	public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
		SharedPreferences spf = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		return spf.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences spf = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		spf.edit().putBoolean(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).apply();
	}

	public static void ClearPreCache(Context ctx,String[] key){
		for(int i =0;i<key.length;i++){
			setBoolean(ctx,key[i],false);
		}
	}
}
