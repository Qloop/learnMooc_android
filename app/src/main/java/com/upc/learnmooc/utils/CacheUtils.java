package com.upc.learnmooc.utils;

import android.content.Context;

/**
 * 缓存工具类
 * Created by Explorer on 2016/2/23.
 */
public class CacheUtils {

	/**
	 * 设置缓存
	 * @param key url
	 * @param value json
	 */
	public static void setCache(String key,String value,Context ctx){
		PrefUtils.setString(ctx,key,value);
	}

	/**
	 * 获取缓存 key是url
	 */
	public static String getCache(String key,Context ctx){
		return PrefUtils.getString(ctx,key,null);
	}
}
