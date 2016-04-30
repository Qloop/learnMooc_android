package com.upc.learnmooc.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.upc.learnmooc.MyApplication;
import com.upc.learnmooc.R;
import com.upc.learnmooc.fragment.CommunityFragment;
import com.upc.learnmooc.fragment.CourseFragment;
import com.upc.learnmooc.fragment.DownloadFragment;
import com.upc.learnmooc.fragment.MineFragment;
import com.upc.learnmooc.utils.SystemBarTintManager;
import com.upc.learnmooc.view.TipsView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 * Created by Explorer on 2016/1/16.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

	private ViewPager mViewPager;
	private List<Fragment> mFragments;
	private FragmentPagerAdapter mAdapter;

	private LinearLayout courseTab;
	private LinearLayout communityTab;
	private LinearLayout downloadTab;
	private LinearLayout mineTab;

	private ImageView ivCourse;
	private ImageView ivCommunity;
	private ImageView ivDownload;
	private ImageView ivMine;

	private TextView tvCourse;
	private TextView tvCommunity;
	private TextView tvDownload;
	private TextView tvMine;
	private static final int BACK_TO_ARTICLE_LIST = 0;
	private double exitTime = 0.1;
	private ImageView ivMineMsg;
	public static TextView tvMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initViews();
		initEvents();

		//默认开始显示course页面
		setSelect(0);
	}


	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 初始化布局
	 */
	private void initViews() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.status_color);//通知栏所需颜色
		}

		ivMineMsg = (ImageView) findViewById(R.id.iv_mine_img);
		tvMsg = (TextView) findViewById(R.id.notify_text);
		mViewPager = (ViewPager) findViewById(R.id.vp_main);
		//获取底部四个tab布局
		courseTab = (LinearLayout) findViewById(R.id.tab_course);
		communityTab = (LinearLayout) findViewById(R.id.tab_community);
		downloadTab = (LinearLayout) findViewById(R.id.tab_download);
		mineTab = (LinearLayout) findViewById(R.id.tab_mine);

		//获取底部四个tab图标及对应的文字
		ivCourse = (ImageView) findViewById(R.id.iv_course_img);
		ivCommunity = (ImageView) findViewById(R.id.iv_community_img);
		ivDownload = (ImageView) findViewById(R.id.iv_downLoad_img);
		ivMine = (ImageView) findViewById(R.id.iv_mine_img);

		tvCourse = (TextView) findViewById(R.id.tv_course);
		tvCommunity = (TextView) findViewById(R.id.tv_community);
		tvDownload = (TextView) findViewById(R.id.tv_downLoad);
		tvMine = (TextView) findViewById(R.id.tv_mine);

		//初始化四个fragment
		Fragment courseFragment = new CourseFragment();
		Fragment CommunityFragment = new CommunityFragment();
		Fragment DownloadFragment = new DownloadFragment();
		final Fragment MineFragment = new MineFragment();

		//初始化viewpager适配器数据源
		mFragments = new ArrayList<>();
		mFragments.add(courseFragment);
		mFragments.add(CommunityFragment);
		mFragments.add(DownloadFragment);
		mFragments.add(MineFragment);

		//初始化适配器
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mFragments.get(position);
			}

			@Override
			public int getCount() {
				return mFragments.size();
			}
		};

		//为viewpager设置适配器
		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				System.out.println("positon is " + position);
				System.out.println("CurrentItem is " + mViewPager.getCurrentItem());
				setSelect(mViewPager.getCurrentItem());
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});


//		jPushReceiver = new MyReceiver();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
//		registerReceiver(jPushReceiver, intentFilter);

//		Intent intent = getIntent();
//		ArrayList<String> jPushMsgList = intent.getStringArrayListExtra("JPush");
		MyApplication application = (MyApplication) getApplication();
		final ArrayList<String> jPushMsgList = application.getJPushMsgList();

		if (jPushMsgList != null && jPushMsgList.size() != 0) {//&&短路效果 不会出现空指针异常

			tvMsg.setVisibility(View.VISIBLE);
			tvMsg.setText(jPushMsgList.size() + "");

			//设置消息提醒(消息数目)
			TipsView.create(this).attach(tvMsg, new TipsView.DragListener() {
				@Override
				public void onStart() {
				}

				@Override
				public void onComplete() {
					jPushMsgList.clear();
				}

				@Override
				public void onCancel() {
					tvMsg.setVisibility(View.VISIBLE);
				}
			});
		}
	}


	/**
	 * 初始化底部tab的点击事件
	 */
	private void initEvents() {
		courseTab.setOnClickListener(this);
		communityTab.setOnClickListener(this);
		downloadTab.setOnClickListener(this);
		mineTab.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tab_course:
				setSelect(0);
				break;
			case R.id.tab_community:
				setSelect(1);
				break;
			case R.id.tab_download:
				setSelect(2);
				break;
			case R.id.tab_mine:
				setSelect(3);
				break;
			default:
				break;
		}
	}

	/**
	 * 设置显示页&底部tab变化
	 */
	private void setSelect(int i) {
		initTabImage();
		mViewPager.setCurrentItem(i, false);
		switch (i) {
			case 0:
				ivCourse.setImageResource(R.drawable.course_pressed);
				tvCourse.setTextColor(getResources().getColor(R.color.word_pressed_color));
				break;
			case 1:
				ivCommunity.setImageResource(R.drawable.community_pressed);
				tvCommunity.setTextColor(getResources().getColor(R.color.word_pressed_color));
				break;
			case 2:
				ivDownload.setImageResource(R.drawable.download_pressed);
				tvDownload.setTextColor(getResources().getColor(R.color.word_pressed_color));
				break;
			case 3:
				ivMine.setImageResource(R.drawable.mine_pressed);
				tvMine.setTextColor(getResources().getColor(R.color.word_pressed_color));
				break;
			default:
				break;
		}
	}

	/**
	 * 初始化底部tab为暗色调
	 */
	private void initTabImage() {
		ivCourse.setImageResource(R.drawable.course_normal);
		ivCommunity.setImageResource(R.drawable.community_normal);
		ivDownload.setImageResource(R.drawable.download_normal);
		ivMine.setImageResource(R.drawable.mine_normal);

		tvCourse.setTextColor(getResources().getColor(R.color.word_normal_color));
		tvCommunity.setTextColor(getResources().getColor(R.color.word_normal_color));
		tvDownload.setTextColor(getResources().getColor(R.color.word_normal_color));
		tvMine.setTextColor(getResources().getColor(R.color.word_normal_color));
	}

	/**
	 * 跳课程分类
	 */
	public void ToCourseClassify(View view) {
		startActivity(new Intent(this, ClassifyActivity.class));
	}

	/**
	 * 跳课程搜索
	 */
	public void ToSearchCourse(View view) {
		startActivity(new Intent(MainActivity.this, SearchListActivity.class));
	}

	/**
	 * 跳转 学习历史记录
	 */
	public void ToCourseHistory(View view) {
		startActivity(new Intent(MainActivity.this, CourseHistoryActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case BACK_TO_ARTICLE_LIST:
//				ToastUtils.showToastLong(MainActivity.this,"返回处理");
				setSelect(1);
				break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
