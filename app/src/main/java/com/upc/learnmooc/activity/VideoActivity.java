package com.upc.learnmooc.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.CourseContent;
import com.upc.learnmooc.fragment.VideoChapterFragment;
import com.upc.learnmooc.fragment.VideoCommentFragment;
import com.upc.learnmooc.fragment.VideoDetailFragment;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.utils.UserInfoCacheUtils;
import com.upc.learnmooc.view.CommunityViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程视频页
 * Created by Explorer on 2016/3/10.
 */
public class VideoActivity extends FragmentActivity implements VDVideoExtListeners.OnVDVideoPlaylistListener {

	private VDVideoView mVDVideoView = null;
	private CommunityViewPager mViewPager;

	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragment;
	private LinearLayout llTop;
	private TextView tvChapter;
	private TextView tvComment;
	private TextView tvDetail;
	private View vIndictorLine;
	private int mWidth;
	private String mUrl = GlobalConstants.GET_VIDEO;
	private String url = GlobalConstants.POST_HISTORY;
	private String collectionUrl = GlobalConstants.POST_COURSE_COLLECTION;
	private String rmCollectionUrl =
			GlobalConstants.RM_COURSE_COLLECTION;

	private CourseContent courseContent;
	private TextView tvCourseName;
	private long courseId;
	private ImageView ivCollection;

	private String[] videoUrlList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);

		initData();
		initViews();
		setCollection(false);
		ivCollection.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setCollection(true);
			}
		});
	}

	public void initViews() {
		mViewPager = (CommunityViewPager) findViewById(R.id.vp_content);
		llTop = (LinearLayout) findViewById(R.id.ll_top);
		tvChapter = (TextView) findViewById(R.id.tv_chapter);
		tvComment = (TextView) findViewById(R.id.tv_comment);
		tvDetail = (TextView) findViewById(R.id.tv_detail);
		vIndictorLine = findViewById(R.id.v_indictor_line);
		ivCollection = (ImageView) findViewById(R.id.iv_collection);

		tvCourseName = (TextView) findViewById(R.id.tv_course_title);

		mFragment = new ArrayList<>();
		Bundle bundle = new Bundle();
		bundle.putLong("id", courseId);
		Fragment chapterFragment = new VideoChapterFragment();
		chapterFragment.setArguments(bundle);
		Fragment commentFragment = new VideoCommentFragment();
		commentFragment.setArguments(bundle);
		Fragment detailFragment = new VideoDetailFragment();
		detailFragment.setArguments(bundle);

		mFragment.add(chapterFragment);
		mFragment.add(commentFragment);
		mFragment.add(detailFragment);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mFragment.get(position);
			}

			@Override
			public int getCount() {
				return mFragment.size();
			}
		};

		// 获取视图树, 对layout结束事件进行监听
		llTop.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					// 当layout执行结束后回调此方法
					@Override
					public void onGlobalLayout() {
						System.out.println("layout 结束");
						llTop.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mWidth = llTop.getChildAt(1).getLeft()
								- llTop.getChildAt(0).getLeft();
						System.out.println("textview 距离 is: " + mWidth);
					}
				});
		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				int len = (int) (mWidth * positionOffset) + position
						* mWidth + 73;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vIndictorLine
						.getLayoutParams();// 获取当前横线的布局参数
				params.leftMargin = len;// 设置左边距

				vIndictorLine.setLayoutParams(params);// 重新给横线设置布局参数
			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					tvChapter.setTextColor(getResources().getColor(R.color.video_word_foused_color));
					tvComment.setTextColor(getResources().getColor(R.color.video_word_normal_color));
					tvDetail.setTextColor(getResources().getColor(R.color.video_word_normal_color));
				} else if (position == 1) {
					tvChapter.setTextColor(getResources().getColor(R.color.video_word_normal_color));
					tvComment.setTextColor(getResources().getColor(R.color.video_word_foused_color));
					tvDetail.setTextColor(getResources().getColor(R.color.video_word_normal_color));
				} else if (position == 2) {
					tvChapter.setTextColor(getResources().getColor(R.color.video_word_normal_color));
					tvComment.setTextColor(getResources().getColor(R.color.video_word_normal_color));
					tvDetail.setTextColor(getResources().getColor(R.color.video_word_foused_color));
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		//给三个指示标签初始化颜色
		tvChapter.setTextColor(getResources().getColor(R.color.video_word_foused_color));
		tvComment.setTextColor(getResources().getColor(R.color.video_word_normal_color));
		tvDetail.setTextColor(getResources().getColor(R.color.video_word_normal_color));

		mVDVideoView = (VDVideoView) findViewById(R.id.vd_video_view);
		// 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
		mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
				.getParent());

	}

	public void initData() {
		Bundle extras = getIntent().getExtras();
		courseId = extras.getLong("id");
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		//get请求参数加 courseId
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("courseId", courseId + "");
		System.out.println(params.getQueryStringParams());

		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseDate(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
//				ToastUtils.showToastShort(VideoActivity.this, courseId + "");
				System.out.println(mUrl + " " + params.getQueryStringParams());
				ToastUtils.showToastShort(VideoActivity.this, "网络错误");
			}
		});
	}

	private void parseDate(String result) {
		Gson gson = new Gson();
		courseContent = gson.fromJson(result, CourseContent.class);
		if (courseContent != null) {
			tvCourseName.setText(courseContent.getCourseName());
			String videoUrl = courseContent.videoUrl;
			videoUrlList = videoUrl.split(";");

			VDVideoListInfo infoList = new VDVideoListInfo();
			VDVideoInfo info;
			for (int i = 0; i < videoUrlList.length; i++) {
				info = new VDVideoInfo();
				info.mTitle = courseContent.getCourseName() + (i + 1);
//			info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
				info.mPlayUrl = videoUrlList[i];
				infoList.addVideoInfo(info);
			}


//			info = new VDVideoInfo();
//			info.mTitle = "这就是一个测试视频1";
//			info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
//			infoList.addVideoInfo(info);
//
//			info = new VDVideoInfo();
//			info.mTitle = "这就是一个测试视频2";
//			info.mPlayUrl = "http://120.27.47.134/video/20160118nx.mp4";
//			infoList.addVideoInfo(info);
//
//			info = new VDVideoInfo();
//			info.mTitle = "这就是一个测试视频3";
//			info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
//			infoList.addVideoInfo(info);

			mVDVideoView.setPlaylistListener(this);

			// 设置播放列表类型
			VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
			if (listView != null) {
				listView.onVideoList(infoList);
			}

			// 初始化播放器以及播放列表
			mVDVideoView.open(VideoActivity.this, infoList);
			// 开始播放，直接选择序号即可
			mVDVideoView.play(0);
			SubmitHistory();
		}
	}

	/**
	 * 提交学习历史
	 */
	private void SubmitHistory() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		//get请求参数加 courseId
		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(VideoActivity.this, "id", 0) + "");
		params.addQueryStringParameter("courseId", courseId + "");

		httpUtils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		int index = mVDVideoView.getListInfo().mIndex;
		if (courseContent != null) {
			long position = mVDVideoView.getListInfo().getCurrInfo().mVideoPosition;
			mVDVideoView.play(index, position);
		} else {

		}

	}

	@Override
	protected void onPause() {
		System.out.println("pause");
		super.onPause();
		mVDVideoView.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	protected void onStop() {
		System.out.println("stop");
		super.onStop();
		mVDVideoView.onStop();
	}

	@Override
	protected void onDestroy() {
		System.out.println("destory");
		mVDVideoView.release(false);
		super.onDestroy();
	}

	@Override
	public void onPlaylistClick(VDVideoInfo info, int p) {
		mVDVideoView.play(p);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mVDVideoView.setIsFullScreen(true);
			LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---横屏");
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mVDVideoView.setIsFullScreen(false);
			LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
		}

	}

	/**
	 * 广告部分以后需要再加进来
	 */

	public void ToMainCourse(View view) {
		finish();
	}

	/**
	 * 点击 "章节"
	 */
	public void Chapter(View view) {
		tvChapter.setTextColor(getResources().getColor(R.color.video_word_foused_color));
		tvComment.setTextColor(getResources().getColor(R.color.video_word_normal_color));
		tvDetail.setTextColor(getResources().getColor(R.color.video_word_normal_color));

		mViewPager.setCurrentItem(0);
	}

	/**
	 * 点击 "评论"
	 */
	public void Comment(View view) {
		tvChapter.setTextColor(getResources().getColor(R.color.video_word_normal_color));
		tvComment.setTextColor(getResources().getColor(R.color.video_word_foused_color));
		tvDetail.setTextColor(getResources().getColor(R.color.video_word_normal_color));

		mViewPager.setCurrentItem(1);
	}

	/**
	 * 点击 "详情"
	 */
	public void Detail(View view) {
		tvChapter.setTextColor(getResources().getColor(R.color.video_word_normal_color));
		tvComment.setTextColor(getResources().getColor(R.color.video_word_normal_color));
		tvDetail.setTextColor(getResources().getColor(R.color.video_word_foused_color));

		mViewPager.setCurrentItem(2);
	}

	/**
	 * 课程收藏
	 */
	public void setCollection(boolean isClick) {
		//课程的url  key:url  value:url
		String article = UserInfoCacheUtils.getCache(courseId + "", VideoActivity.this);
		System.out.println("文章地址 " + article);

		if (isClick) {
			//如果没有收藏这个课程  设置收藏 缓存 改变图标
			if (article == null) {
				UserInfoCacheUtils.setCache(courseId + "", courseId + "", VideoActivity.this);
				ivCollection.setImageResource(R.drawable.has_collection);

				Snackbar.make(ivCollection, "已收藏！^_^~~~", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				//数据库保存用户收藏的课程
				SubCollection();

			} else {
				System.out.println("文章 is " + article);
				//如果已经收藏  取消收藏 清除缓存 改变图标
				UserInfoCacheUtils.setCache(courseId + "", null, VideoActivity.this);
				ivCollection.setImageResource(R.drawable.collection);
				Snackbar.make(ivCollection, "取消收藏！T^T~~~", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();

				//数据库删除用户收藏的课程 的记录
				RemoveCollection();
			}
		} else {
			if (article == null) {
				ivCollection.setImageResource(R.drawable.collection);
			} else {
				ivCollection.setImageResource(R.drawable.has_collection);
			}
		}

	}

	private void RemoveCollection() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(VideoActivity.this, "id", 0) + "");
		params.addQueryStringParameter("courseId", courseId + "");

		httpUtils.send(HttpRequest.HttpMethod.GET, rmCollectionUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
			}
		});
	}

	private void SubCollection() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		final RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(VideoActivity.this, "id", 0) + "");
		params.addQueryStringParameter("courseId", courseId + "");

		httpUtils.send(HttpRequest.HttpMethod.GET, collectionUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
			}
		});
	}

	public void ToMarkerDown(View view) {
		Intent intent = new Intent(VideoActivity.this, NoteActivity.class);
		Bundle bundle = new Bundle();
		if (courseContent != null) {
			bundle.putString("courseName", courseContent.getCourseName());
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void VideoDownLoad(View view) {
		System.out.println("下载视频！！！！！！！");
		if (courseContent != null) {
			String target = null;

			//如果有SD卡 则下载到SD卡中
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				target = Environment.getExternalStorageDirectory() + "/downloadVieo/" + mVDVideoView.getListInfo().getCurrInfo().mTitle;


			} else {
				//如果没有SD卡
				target = Environment.getDataDirectory() + "/downloadVieo/" + mVDVideoView.getListInfo().getCurrInfo().mTitle;

			}

			int currentVideo = 0;
			currentVideo = (int) mVDVideoView.getListInfo().getCurrInfo().mVideoPosition;
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(videoUrlList[currentVideo], target, true, false,
					new RequestCallBack<File>() {
						@Override
						public void onStart() {
							if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
								ToastUtils.showToastLong(VideoActivity.this, "正在下载———" +
										Environment.getExternalStorageDirectory() + "/downloadVieo/" + mVDVideoView.getListInfo().getCurrInfo().mTitle);


							} else {
								//如果没有SD卡
								ToastUtils.showToastLong(VideoActivity.this, "正在下载———" +
										Environment.getDataDirectory() + "/downloadVieo/" + mVDVideoView.getListInfo().getCurrInfo().mTitle);
							}

						}

						@Override
						public void onLoading(long total, long current,
											  boolean isUploading) {
							super.onLoading(total, current, isUploading);
						}

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							ToastUtils.showToastLong(VideoActivity.this, "下载成功");
						}

						@Override
						public void onFailure(HttpException error, String msg) {
						}
					});


		}

	}

}
