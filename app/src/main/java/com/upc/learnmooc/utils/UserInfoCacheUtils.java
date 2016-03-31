package com.upc.learnmooc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户信息缓存工具类
 * Created by Explorer on 2016/3/1.
 */
public class UserInfoCacheUtils {

	private final static String PREF_NAME = "user_info";

	public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
		SharedPreferences spf = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		return spf.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences spf = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		spf.edit().putBoolean(key, value).apply();
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

	public static int getInt(Context ctx, String key, int defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static void setInt(Context ctx, String key, int value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).apply();
	}

	/**
	 * 设置缓存
	 */
	public static void setCache(String key,String value,Context ctx){
		setString(ctx,key,value);
	}

	/**
	 * 获取缓存 key是url
	 */
	public static String getCache(String key,Context ctx){
		return getString(ctx,key,null);
	}

}
