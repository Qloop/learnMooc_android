package com.upc.learnmooc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.upc.learnmooc.R;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.PrefUtils;
import com.upc.learnmooc.utils.StreamUtils;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.view.UpdateProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 启动页
 * Created by Explorer on 2016/1/16.
 */
public class SplashActivity extends Activity {

	private static final int CODE_GET_VERSION = 0;
	private static final int CODE_URL_ERROR = 1;
	private static final int CODE_NET_ERROR = 2;
	private static final int CODE_JSON_ERROR = 3;
	private static final int CODE_ENTER_HOME = 4;

	LinearLayout mRootLayout;
	//从服务器获取的版本信息
	private String mVersionName;
	private int mVersionCode;
	private String mDescription;
	private String mDownloadUrl;
	private String mApkName;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CODE_GET_VERSION:
					showUpdateDialog();
					break;
				case CODE_URL_ERROR:
					ToastUtils.showToastShort(SplashActivity.this, "URL错误");
					enterHome();
					break;
				case CODE_NET_ERROR:
					ToastUtils.showToastShort(SplashActivity.this, "网络异常");
					enterHome();
					break;
				case CODE_JSON_ERROR:
					ToastUtils.showToastShort(SplashActivity.this, "数据解析错误");
					enterHome();
					break;
				case CODE_ENTER_HOME:
					jumpToNextPager();
					break;

			}
		}
	};
	private SweetAlertDialog pDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		startAnim();
		checkVersion();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		mRootLayout = (LinearLayout) findViewById(R.id.layout_root);
	}


	/**
	 * 启动页动画
	 */
	private void startAnim() {
		AnimationSet animationSet = new AnimationSet(false);
		//渐变
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);//2s
		alphaAnimation.setFillAfter(true);

//		//缩放
//		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		scaleAnimation.setDuration(2000);
//		scaleAnimation.setFillAfter(true);

		animationSet.addAnimation(alphaAnimation);
//		animationSet.addAnimation(scaleAnimation);
//		animationSet.setAnimationListener(new Animation.AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//
//			}
//		});
		mRootLayout.startAnimation(animationSet);
	}

	/**
	 * 跳转下一个页面
	 * 如果是第一次打开则跳转新手引导页
	 * 否则跳转主页
	 */
	private void jumpToNextPager() {
		boolean isShowed = PrefUtils.getBoolean(this, "is_user_guide_hasShowed", false);
		if (!isShowed) {
			Intent intent = new Intent(SplashActivity.this, GuidePagerActivity.class);
			startActivity(intent);
			finish();
		} else {
			enterHome();
		}
		finish();
	}


	/**
	 * 进入主页面
	 */
	private void enterHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 获取本机的版本名
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (PackageManager.NameNotFoundException e) {
			//报名没找到
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取本机的版本号
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
//			System.out.println("版本号" + versionCode);
			return versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			//报名没找到
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 检查版本更新
	 */
	private int checkVersion() {

		final Message msg = Message.obtain();
		new Thread() {

			HttpURLConnection conn = null;

			@Override
			public void run() {
				try {
					URL url = new URL(GlobalConstants.DOWNLOAD_URL);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();

					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						String result = StreamUtils.readFromStream(is);
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDescription = jo.getString("description");
						mApkName = jo.getString("apkName");
						mDownloadUrl = jo.getString("downloadUrl");
						if (mVersionCode > getVersionCode()) {
							msg.what = CODE_GET_VERSION;
//							System.out.println("服务器拿到的版本号" + mVersionCode);
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					//Url异常
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					//网络错误异常
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					//json解析异常
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
					mHandler.sendMessage(msg);
				}
			}
		}.start();
		return msg.what;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			jumpToNextPager();
//			if(pDialog != null){
//				jumpToNextPager();
//				pDialog.dismiss();
//			}
		}
		return false;
	}

	/**
	 * 显示更新提示的弹窗
	 */
	protected void showUpdateDialog() {
		pDialog = new SweetAlertDialog(this)
				.setTitleText("新版本")
				.setContentText(mDescription)
				.setConfirmText("立即更新")
				.setCancelText("我再想想");
		//取消
		pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				pDialog.dismiss();
				jumpToNextPager();
			}
		});

		//立即更新
		pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				pDialog.dismiss();
				downLoadTask task = new downLoadTask(SplashActivity.this, mDownloadUrl, mApkName);
				task.execute();
			}
		});
		pDialog.show();

	}

	/**
	 * 异步下载、安装
	 */
	class downLoadTask extends AsyncTask<String, Integer, File> {

		private Context mContext;
		private String mDownloadUrl;
		private String mApkName;
		private UpdateProgressDialog mProgressDialog;
		public int flag; //1下载成功 2下载失败
		public File file;

		public downLoadTask(Context ctx, String url, String apkName) {
			mContext = ctx;
			mApkName = apkName;
			mDownloadUrl = url;
		}

		/**
		 * 后台下载更新的apk
		 */
		@Override
		protected File doInBackground(String... params) {

			HttpUtils httpUtils = new HttpUtils();
			String target = null;

			//如果有SD卡 则下载到SD卡中
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				target = Environment.getExternalStorageDirectory() + "/" + mApkName;


			} else {
				//如果没有SD卡
				target = Environment.getDownloadCacheDirectory() + "/" + mApkName;

			}

			httpUtils.download(mDownloadUrl, target, new RequestCallBack<File>() {

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("下载进度:" + current + "/" + total);
					publishProgress((int) (current * 100 / total));
				}

				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					System.out.println("下载成功");
					flag = 1;
//					file = responseInfo.result;
					install(responseInfo.result);
				}

				@Override
				public void onFailure(HttpException e, String s) {
					flag = 2;
				}
			});

			return file;

		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = new UpdateProgressDialog(mContext);
			mProgressDialog.setTitle("下载进度");
			mProgressDialog.setMessage("更新中...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
				}
			});
			mProgressDialog.show();
			mProgressDialog.setOnKeyListener(onKeyListener);//处理返回键 隐藏进度条 进入主页
		}


		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressDialog.setProgress(values[0]);
		}

		/**
		 * 后台下载完成后进行安装更新
		 *
		 * @param file 下载完的apk文件
		 */
		protected void install(File file) {
			mProgressDialog.dismiss();
			// 跳转到系统下载页面
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 0);// 如果用户取消安装的话,
			// 会返回结果,回调方法onActivityResult
			android.os.Process.killProcess(android.os.Process.myPid());
		}

		/**
		 * 下载过程中 按下返回键 就进入主页
		 */
		private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//					mProgressDialog.dismiss();
					Intent intent = new Intent(mContext, MainActivity.class);
					startActivity(intent);
				}
				return false;
			}
		};


	}


	/**
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//加switch 便于以后扩展
		switch (requestCode) {
			case 0:
				enterHome();//如果下载完成后 用户点击取消安装 则进入主页
				break;
			default:
				enterHome();
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
