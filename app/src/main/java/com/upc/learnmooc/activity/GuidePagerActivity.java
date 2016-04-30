package com.upc.learnmooc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.upc.learnmooc.R;
import com.upc.learnmooc.utils.DensityUtils;
import com.upc.learnmooc.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导页
 * Created by Explorer on 2016/1/16.
 */
public class GuidePagerActivity extends Activity {

	private final static int[] mImagIds = new int[]{
			R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3, R.drawable.guide_4
	};
	private ViewPager vpGuide;
	private ArrayList<ImageView> mImageList;
	private LinearLayout llPointGroup;// 引导圆点的父控件
	private int mPointWidth;
	private View viewRedPoint;
	private Button btnLogin;
	private Button btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_activity);

		vpGuide = (ViewPager) findViewById(R.id.vp_guidePager);
		llPointGroup = (LinearLayout) findViewById(R.id.point_gray);
		viewRedPoint = findViewById(R.id.view_point_red);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnRegister = (Button) findViewById(R.id.btn_register);

		initViews();
		vpGuide.setAdapter(new GuideAdapter());
		vpGuide.addOnPageChangeListener(new GuidePageListener());
	}

	/**
	 * 初始化页面
	 */
	private void initViews() {
//		PrefUtils.setBoolean(this,"is_user_guide_showed",true);


		mImageList = new ArrayList<ImageView>();

		for (int i = 0; i < mImagIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImagIds[i]);
			mImageList.add(imageView);
		}

		//引导页圆点
		for (int i = 0; i < mImagIds.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					DensityUtils.dp2px(this, 12), DensityUtils.dp2px(this, 12));

			if (i > 0) {
				params.leftMargin = DensityUtils.dp2px(this, 10);
			}
			point.setLayoutParams(params);

			llPointGroup.addView(point);
		}
		// 获取视图树, 对layout结束事件进行监听
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					// 当layout执行结束后回调此方法
					@Override
					public void onGlobalLayout() {
						System.out.println("layout 结束");
						llPointGroup.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mPointWidth = llPointGroup.getChildAt(1).getLeft()
								- llPointGroup.getChildAt(0).getLeft();
						System.out.println("圆点距离:" + mPointWidth);
					}
				});

	}

	/**
	 * viewpager适配器
	 */
	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImagIds.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mImageList.get(position));
			return mImageList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * viewpager的滑动监听
	 *
	 * @author Kevin
	 */
	class GuidePageListener implements ViewPager.OnPageChangeListener {

		// 滑动事件
		@Override
		public void onPageScrolled(int position, float positionOffset,
								   int positionOffsetPixels) {
			// System.out.println("当前位置:" + position + ";百分比:" + positionOffset
			// + ";移动距离:" + positionOffsetPixels);
			int len = (int) (mPointWidth * positionOffset) + position
					* mPointWidth;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint
					.getLayoutParams();// 获取当前红点的布局参数
			params.leftMargin = len;// 设置左边距

			viewRedPoint.setLayoutParams(params);// 重新给小红点设置布局参数
		}

		// 某个页面被选中
		@Override
		public void onPageSelected(int position) {
			if (position == mImagIds.length - 1) {// 最后一个
				//显示登录 注册按钮
				btnLogin.setVisibility(View.VISIBLE);
				btnRegister.setVisibility(View.VISIBLE);
			} else {
				btnLogin.setVisibility(View.INVISIBLE);
				btnRegister.setVisibility(View.INVISIBLE);
			}
		}

		// 滑动状态发生变化
		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	/**
	 * 登录处理
	 * @param View
	 */
	public void login(View View){
		PrefUtils.setBoolean(GuidePagerActivity.this, "is_user_guide_hasShowed", true);
		Intent intent = new Intent(GuidePagerActivity.this,LoginActivity.class);
		startActivity(intent);
	}

	/**
	 * 注册处理
	 * @param View
	 */
	public void register(View View){
		PrefUtils.setBoolean(GuidePagerActivity.this, "is_user_guide_hasShowed", true);
		Intent intent = new Intent(GuidePagerActivity.this,RegisterActivity.class);
		startActivity(intent);
	}
}
