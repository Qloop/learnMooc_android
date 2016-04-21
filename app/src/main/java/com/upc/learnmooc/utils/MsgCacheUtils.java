package com.upc.learnmooc.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 推送消息缓存工具类
 * Created by Explorer on 2016/4/19.
 */
public class MsgCacheUtils {
	private final static String PREF_NAME = "push_msg";

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
		setString(ctx, key, value);
	}

	/**
	 * 获取缓存 key是url
	 */
	public static String getCache(String key,Context ctx){
		return getString(ctx,key,null);
	}

	/**
	 * 清空指定缓存信息
	 */
	public static void ClearCache(Context ctx,String[] key){
		for(int i = 0;i < key.length;i++){
			setString(ctx,key[i],null);
		}
	}
}
