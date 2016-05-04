package com.upc.learnmooc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.upc.learnmooc.activity.MainActivity;
import com.upc.learnmooc.utils.MsgCacheUtils;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Explorer on 2016/4/16.
 */
public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		Bundle bundle = intent.getExtras();
		if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {

			String title = bundle.getString(JPushInterface.EXTRA_TITLE);
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

			MyApplication.getJPushMsgList().add(message);
//			saveArray(context, MyApplication.getJPushMsgList());//把消息缓存到本地
			MyApplication.JPushMsgSize++;
			MsgCacheUtils.setInt(context, "Msg_Size", MyApplication.JPushMsgSize);
			MsgCacheUtils.setString(context, "Msg_" + MyApplication.JPushMsgSize, message);

//			Toast.makeText(context, "Message title:" + title + "  content:" + message, Toast.LENGTH_LONG).show();

//			MainActivity.tvMsg.setText(MyApplication.jPushNum + "");
//			ToastUtils.showToastLong(context, MainActivity.tvMsg.getText()+"");


			Intent i = new Intent(context, MainActivity.class);

//			i.putStringArrayListExtra("JPush",JPushMsg);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

			context.startActivity(i);

		}
//		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//
//			String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			try {
//				JSONObject jsonObject = new JSONObject(type);
//				String str = jsonObject.getString("key");
//				if (str.equals("1")) {
//					//打开自定义的Activity
//					Intent i = new Intent(context, MainActivity.class);
//					bundle.putInt("index", 1);
//					i.putExtras(bundle);
//
//					//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					context.startActivity(i);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void saveArray(Context context, ArrayList<String> list) {
		MsgCacheUtils.setInt(context, "Msg_Size", MyApplication.JPushMsgSize + list.size());
		for (int i = MyApplication.JPushMsgSize; i < MyApplication.JPushMsgSize + list.size(); i++) {
//			MsgCacheUtils.ClearCache(context, new String[]{"Msg_" + i});
			MsgCacheUtils.setString(context, "Msg_" + i, list.get(i - MyApplication.JPushMsgSize));
		}
	}
}
