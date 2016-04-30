package com.upc.learnmooc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.User;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

/**
 * Created by Explorer on 2016/1/26.
 */
public class LoginActivity extends BaseActivity {


	private EditText etLoginEmail;
	private EditText etLoginPwd;

	//登录信息
	private String mEmail;
	private String mPwd;
	private static final int LOGIN_SUCCESS = 0;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == LOGIN_SUCCESS) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}

		}
	};
	private SharedPreferences user_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		initViews();
	}

	@Override
	public void initViews() {
		etLoginEmail = (EditText) findViewById(R.id.et_login_email);
		etLoginPwd = (EditText) findViewById(R.id.et_login_pwd);
	}

	/**
	 * 返回前一个界面
	 */
	public void BackToPrePager(View view) {
		startActivity(new Intent(LoginActivity.this, GuidePagerActivity.class));
		finish();
	}

	/**
	 * 跳转注册界面
	 */
	public void TurnToRegister(View view) {
		startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
		finish();
	}

	/**
	 * 登录逻辑与服务器处理
	 */
	public void LoginDeal(View view) {
		HttpUtils httpUtils = new HttpUtils();
		//设置超时时间为 5s
		httpUtils.configCurrentHttpCacheExpiry(1000 * 5);
		httpUtils.configTimeout(1000 * 5);
		//获取登录信息
		getLoginInfo();

		RequestParams params = new RequestParams();
		params.addBodyParameter("mail", mEmail);
		params.addBodyParameter("password", mPwd);

		String url = GlobalConstants.LOGIN_URL;
		final Message msg = Message.obtain();
		httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("登录成功 response is " + responseInfo);
				refreshUserInfo();
				System.out.println("信息缓存完成");
//				User userInfo = getData(responseInfo.result);
//
//				//登录成功的话 更新一下用户信息到本地
//				SharedPreferences spf = getSharedPreferences("user_info", MODE_PRIVATE);
//				spf.edit().putString("password", userInfo.getPassword())
//						.putString("nickname", userInfo.getNickname())
//						.putString("avatar", userInfo.getAvatar())
//						.putInt("roleType", userInfo.getRoleType())
//						.apply();
//				msg.what = LOGIN_SUCCESS;

			}

			@Override
			public void onFailure(HttpException e, String s) {
				ToastUtils.showToastShort(LoginActivity.this, "登录异常");
			}
		});

	}

	/**
	 * 忘记密码时跳转
	 */
	public void FindPwd(View view) {
		startActivity(new Intent(LoginActivity.this,ResetPwdActivity.class));
		finish();
	}

	/**
	 * 获取登录信息
	 */
	public void getLoginInfo() {
		user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		if (user_info.getString("user_info", null) != null) {
			if (user_info.getString("mail", null) != null) {
				//从配置文件读取登录信息登录
				mEmail = user_info.getString("mail", null);
				mPwd = user_info.getString("password", null);
			} else {
				//从输入框读取信息登录
				if (checkEdit()) {
					mEmail = etLoginEmail.getText().toString();
					mPwd = etLoginPwd.getText().toString();
				}
			}
		} else {
			//从输入框读取信息登录
			if (checkEdit()) {
				mEmail = etLoginEmail.getText().toString();
				mPwd = etLoginPwd.getText().toString();
			}
		}
	}

	/**
	 * 检查登录项是否为空
	 */
	private boolean checkEdit() {
		mEmail = etLoginEmail.getText().toString();
		mPwd = etLoginPwd.getText().toString();
		if (TextUtils.isEmpty(mEmail)) {
			ToastUtils.showToastShort(LoginActivity.this, "邮箱不能空");
			return false;
		} else if (TextUtils.isEmpty(mPwd)) {
			ToastUtils.showToastShort(LoginActivity.this, "密码不能空");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 解析json
	 */
	public User getData(String data) {
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
		return user;
	}

	/**
	 * 登录成功后获取用户基本信息 缓存到本地
	 */
	private void refreshUserInfo() {
		HttpUtils httpRefresh = new HttpUtils();
		//设置超时时间为 5s
		httpRefresh.configCurrentHttpCacheExpiry(1000 * 5);
		httpRefresh.configTimeout(1000 * 5);

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("nickname", user_info.getString("nickname", null));

		String url = GlobalConstants.GET_USER_INFO;
		final Message msg = Message.obtain();
		httpRefresh.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("GET RESULT " + responseInfo.result);
				User userInfo = getData(responseInfo.result);

				//登录成功的话 更新一下用户信息到本地
				UserInfoCacheUtils.setInt(LoginActivity.this,"id",userInfo.getId());
				UserInfoCacheUtils.setString(LoginActivity.this, "password", userInfo.getPassword());
				UserInfoCacheUtils.setString(LoginActivity.this, "mail", userInfo.getMail());
				UserInfoCacheUtils.setString(LoginActivity.this, "nickname", userInfo.getNickname());
				UserInfoCacheUtils.setString(LoginActivity.this, "avatar", userInfo.getAvatar());
				UserInfoCacheUtils.setInt(LoginActivity.this, "roleType", userInfo.getRoleType());

				msg.what = LOGIN_SUCCESS;
				mHandler.sendMessage(msg);

			}

			@Override
			public void onFailure(HttpException e, String s) {
				ToastUtils.showToastShort(LoginActivity.this, "信息缓存失败");
			}
		});
	}
}
