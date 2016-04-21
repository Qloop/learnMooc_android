package com.upc.learnmooc;

import android.app.Application;

import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDResolutionManager;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Explorer on 2016/3/10.
 */
public class MyApplication extends Application {

	public static ArrayList<String> JPushMsg = new ArrayList<>();
	public static int JPushMsgSize = 0;

	@Override
	public void onCreate() {
		super.onCreate();

		/**
		 * 极光推送初始化
		 */
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);


		// 播放器初始化，要在app启动前进行初始化，才能解压出相应的解码器
		VDApplication.getInstance().initPlayer(this);
		VDResolutionManager.getInstance(this).init(
				VDResolutionManager.RESOLUTION_SOLUTION_NONE);
	}

	public static ArrayList<String> getJPushMsgList() {
		return JPushMsg;
	}
}
