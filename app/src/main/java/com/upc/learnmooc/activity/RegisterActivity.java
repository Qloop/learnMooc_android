package com.upc.learnmooc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * 注册页
 * Created by Explorer on 2016/1/26.
 */
public class RegisterActivity extends BaseActivity {

	private EditText etNickname;//昵称
	private EditText etEmail;//邮箱
	private EditText etPwd;//密码
	private Button btnRegister;
	private String mNickname;
	private String mEmail;
	private String mPwd;

	//注册返回result
	private static final int RESULT_FAILED = 0;//注册失败 后台异常
	private static final int RESULT_HAS_REGISTER = 1;//昵称已注册
	private static final int RESULT_SUCCESS = 2;//注册成功

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case RESULT_FAILED:
					showMsgDialog("注册失败,请稍后重试~T_T~", "原谅ta了");
					break;
				case RESULT_HAS_REGISTER:
					showMsgDialog("昵称已注册啦~换一个独一无二的名字吧！", "这就换！");
					break;
				case RESULT_SUCCESS:
					showSuccessDialog("注册成功，/n 开始体验吧^_^", "马上体验");
					break;
				default:
					showMsgDialog("注册异常,请稍后重试~T_T~", "原谅ta了");
					break;
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		initViews();
	}


	/**
	 * 初始化页面
	 */
	@Override
	public void initViews() {
		etNickname = (EditText) findViewById(R.id.et_nickname);
		etEmail = (EditText) findViewById(R.id.et_email);
		etPwd = (EditText) findViewById(R.id.et_pwd);
		btnRegister = (Button) findViewById(R.id.btn_register);

	}

	/**
	 * 返回前一个界面
	 */
	public void BackToPrePager(View view) {
		startActivity(new Intent(RegisterActivity.this, GuidePagerActivity.class));
		finish();
	}

	/**
	 * 跳转登录界面
	 */
	public void TurnToLogin(View view) {
//		ToastUtils.showToastLong(RegisterActivity.this, "跳登录");
		startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
		finish();
	}

	/**
	 * 注册逻辑与服务器处理
	 */
	public void RegisterDeal(View view) {
		HttpUtils httpUtils = new HttpUtils();

		//设置超时时间为 5s
		httpUtils.configCurrentHttpCacheExpiry(1000 * 5);
		httpUtils.configTimeout(1000 * 5);
		String url = GlobalConstants.REGISTER_URL;
		mNickname = etNickname.getText().toString();
		mEmail = etEmail.getText().toString();
		mPwd = etPwd.getText().toString();


		final Message msg = Message.obtain();
		if (checkEdit()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("name", mNickname);
			params.addBodyParameter("mail", mEmail);
			params.addBodyParameter("password", mPwd);

			System.out.println("name: " + mNickname + "mail " + mEmail + "password " + mPwd);

			httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					User user = getData(responseInfo.result);
					System.out.println("返回信息 is " + user);

					if (user.getResult().equals("注册失败")) {
						msg.what = RESULT_FAILED;
					} else if (user.getResult().equals("昵称已注册")) {
						msg.what = RESULT_HAS_REGISTER;
					} else if (user.getResult().equals("注册成功")) {
						msg.what = RESULT_SUCCESS;

						//注册成功的话保存用户信息到本地 下次打开的时候自动登录、进入的时候初始化UI信息
						SharedPreferences spf = getSharedPreferences("user_info", MODE_PRIVATE);
						spf.edit().putLong("id", user.getId())
								.putString("password", user.getPassword())
								.putString("nickname", user.getNickname())
								.putString("mail", user.getMail())
								.putString("avatar", user.getAvatar())
								.putInt("roleType", user.getRoleType())
								.apply();
					}
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFailure(HttpException e, String s) {
					e.printStackTrace();
					ToastUtils.showToastLong(RegisterActivity.this, s);
				}
			});
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
	 * 检查注册项是否为空
	 */
	private boolean checkEdit() {
		if (TextUtils.isEmpty(mNickname)) {
			ToastUtils.showToastShort(RegisterActivity.this, "昵称不能空");
			return false;
		} else if (TextUtils.isEmpty(mEmail)) {
			ToastUtils.showToastShort(RegisterActivity.this, "注册邮箱不能空");
			return false;
		} else if (TextUtils.isEmpty(mPwd)) {
			ToastUtils.showToastShort(RegisterActivity.this, "密码不能空");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 注册返回信息时提示弹窗
	 */
	public void showMsgDialog(String msg, String confirMsg) {
		final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
		dialog.setTitleText(msg)
				.setConfirmText(confirMsg)
				.show();
		dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 注册成功时提示弹窗
	 */
	public void showSuccessDialog(String msg, String confirMsg) {
		final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
		dialog.setTitleText(msg)
				.setConfirmText(confirMsg)
				.show();
		dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				dialog.dismiss();
				startActivity(new Intent(RegisterActivity.this, MainActivity.class));
			}
		});
	}

}

