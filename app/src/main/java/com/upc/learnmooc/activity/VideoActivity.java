package com.upc.learnmooc.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
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
import com.upc.learnmooc.view.CommunityViewPager;

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
	private CourseContent courseContent;
	private TextView tvCourseName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);

		initViews();
		initData();
	}

	public void initViews() {
		mViewPager = (CommunityViewPager) findViewById(R.id.vp_content);
		llTop = (LinearLayout) findViewById(R.id.ll_top);
		tvChapter = (TextView) findViewById(R.id.tv_chapter);
		tvComment = (TextView) findViewById(R.id.tv_comment);
		tvDetail = (TextView) findViewById(R.id.tv_detail);
		vIndictorLine = findViewById(R.id.v_indictor_line);

		tvCourseName = (TextView) findViewById(R.id.tv_course_title);

		mFragment = new ArrayList<>();
		Fragment chapterFragment = new VideoChapterFragment();
		Fragment commentFragment = new VideoCommentFragment();
		Fragment detailFragment = new VideoDetailFragment();

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
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseDate(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				ToastUtils.showToastShort(VideoActivity.this, "网络错误");
			}
		});
	}

	private void parseDate(String result) {
		Gson gson = new Gson();
		courseContent = gson.fromJson(result, CourseContent.class);
		if (courseContent != null) {
			tvCourseName.setText(courseContent.getCourseName());


			VDVideoListInfo infoList = new VDVideoListInfo();
			VDVideoInfo info = new VDVideoInfo();
			info.mTitle = courseContent.getCourseName();
//			info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
			info.mPlayUrl = courseContent.getVideoUrl();
			infoList.addVideoInfo(info);


			info = new VDVideoInfo();
			info.mTitle = "这就是一个测试视频1";
			info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
			infoList.addVideoInfo(info);

			info = new VDVideoInfo();
			info.mTitle = "这就是一个测试视频2";
			info.mPlayUrl = "http://120.27.47.134/video/20160118nx.mp4";
			infoList.addVideoInfo(info);

			info = new VDVideoInfo();
			info.mTitle = "这就是一个测试视频3";
			info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
			infoList.addVideoInfo(info);

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
		}
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

}
