package com.upc.learnmooc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;

/**
 * 重置密码
 * Created by Explorer on 2016/2/5.
 */
public class ResetPwdActivity extends BaseActivity {

	private static final int RESET_SUCCESS = 0;//重置请求发送成功
	private EditText etResetEmail;
	private String mEmail;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == RESET_SUCCESS) {
				startActivity(new Intent(ResetPwdActivity.this, LoginActivity.class));
				finish();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password_activity);
		initViews();
	}

	/**
	 * 初始化布局
	 */
	@Override
	public void initViews() {
		etResetEmail = (EditText) findViewById(R.id.et_resetpwd_email);
	}

	/**
	 * 重置密码逻辑处理
	 *
	 * @param view
	 */
	public void ResetDeal(View view) {
		HttpUtils httpUtils = new HttpUtils();
		//超时时间5s
		httpUtils.configCurrentHttpCacheExpiry(1000 * 5);
		String url = GlobalConstants.BASE_URL + "/user/reset_mail";
		mEmail = etResetEmail.getText().toString();
		final Message msg = Message.obtain();
		if (mEmail == null) {
			//提示邮箱为空
			ToastUtils.showToastShort(ResetPwdActivity.this, "请输入邮箱");
		} else {
			RequestParams params = new RequestParams();
			params.addBodyParameter("mail", mEmail);
			httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					System.out.println("重置结果 " + responseInfo);
					ToastUtils.showToastLong(ResetPwdActivity.this, "申请已提交，请查看邮件");
					msg.what = RESET_SUCCESS;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFailure(HttpException e, String s) {
					e.printStackTrace();
					ToastUtils.showToastShort(ResetPwdActivity.this, "网络异常");
				}
			});
		}
	}

	/**
	 * 返回前一个页面
	 */
	public void BackToPrePager(View view) {
		startActivity(new Intent(ResetPwdActivity.this, LoginActivity.class));
		finish();
	}
}
