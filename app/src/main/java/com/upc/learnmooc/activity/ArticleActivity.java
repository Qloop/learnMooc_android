package com.upc.learnmooc.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.upc.learnmooc.R;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 文章内容
 * Created by Explorer on 2016/3/7.
 */
public class ArticleActivity extends AppCompatActivity {

	private WebView mWebView;
	private ProgressBar mProgressBar;
	private ImageView ivCollection;
	private ImageView ivShare;
	private String url;
	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.article_acticity);
		setContentView(R.layout.activity_self_article);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		toolBarLayout.setExpandedTitleTextAppearance(R.style.MyTitle);
		toolBarLayout.setTitle(getIntent().getStringExtra("title"));

		initViews();
		fab = (FloatingActionButton) findViewById(R.id.fab);
		setCollection(false);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCollection(true);
			}
		});
//		ivCollection.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void initViews() {
		mWebView = (WebView) findViewById(R.id.wb_article);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
//		ivCollection = (ImageView) findViewById(R.id.iv_collection);
		ivShare = (ImageView) findViewById(R.id.iv_article_share);

		url = getIntent().getStringExtra("url");

		WebSettings settings = mWebView.getSettings();


		settings.setJavaScriptEnabled(true);//支持js
		settings.setUseWideViewPort(true);//支持双击缩放

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			/**
			 * 所有跳转链接的回调
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;
			}
		});

		mWebView.loadUrl(url);
	}


	/**
	 * 返回前一个页面
	 * 不是主页面  而是社区页的文章板块
	 */
	public void ToArticleList(View view) {
//		Intent intent = new Intent();
//		intent.setClass(ArticleActivity.this, MainActivity.class);
//		setResult(0, intent);//返回 result == 0 MainActivity处理 显示
//		startActivity(intent);
		finish();
	}


	/**
	 * 收藏
	 */
	public void setCollection(boolean isClick) {
		//文章的url  key:url  value:url
		String article = UserInfoCacheUtils.getCache(url, ArticleActivity.this);
		System.out.println("文章地址 " + article);

		if (isClick) {
			//如果没有收藏这个文章  设置收藏 缓存 改变图标
			if (article == null) {
				UserInfoCacheUtils.setCache(url, url, ArticleActivity.this);
				fab.setImageResource(R.drawable.has_collection);

				Snackbar.make(fab, "已收藏！^_^~~~", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				//数据库保存用户收藏的文章

			} else {
				System.out.println("文章 is " + article);
				//如果已经收藏  取消收藏 清除缓存 改变图标
				UserInfoCacheUtils.setCache(url, null, ArticleActivity.this);
				fab.setImageResource(R.drawable.collection);
				Snackbar.make(fab, "取消收藏！T^T~~~", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();

				//数据库删除用户收藏的文章的记录
			}
		} else {
				fab.setImageResource(R.drawable.collection);
			} else {
				fab.setImageResource(R.drawable.has_collection);
			}
		}

	}

	/**
	 * 分享
	 */
	public void ArticleShare(View view) {
		showShare();
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//		oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("ShareSDK测试");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
		oks.show(this);
	}
}
