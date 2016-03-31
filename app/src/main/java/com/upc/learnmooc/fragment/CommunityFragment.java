package com.upc.learnmooc.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.upc.learnmooc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区页
 * Created by Explorer on 2016/2/10.
 */
public class CommunityFragment extends BaseFragment {

	@ViewInject(R.id.rl_top)
	private RelativeLayout rlTop;

	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;

	@ViewInject(R.id.tv_article)
	private TextView tvArticle;
	@ViewInject(R.id.tv_rank)
	private TextView tvRank;
	@ViewInject(R.id.v_indictor_line)
	private View vIndicvtorLine;

	private FragmentPagerAdapter mAdapter;

	private List<Fragment> mFragments;
	private int mWidth;
	//	private final static String[] titleArray = new String[]{"文章", "排位"};

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.community_fragment, null);
		//注入view和事件
		ViewUtils.inject(this, view);

		//初始化文章和排位两个frgment
		Fragment articlrFrgment = new ArticleFragment();
		Fragment rankingFragment = new RankingFragment();

		//初始化viewpager数据源
		mFragments = new ArrayList<>();
		mFragments.add(articlrFrgment);
		mFragments.add(rankingFragment);

		mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mFragments.get(position);
			}
		};

		// 获取视图树, 对layout结束事件进行监听
		rlTop.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					// 当layout执行结束后回调此方法
					@Override
					public void onGlobalLayout() {
						System.out.println("layout 结束");
						rlTop.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mWidth = rlTop.getChildAt(1).getLeft()
								- rlTop.getChildAt(0).getLeft();
						System.out.println("textview 距离 is: " + mWidth);
					}
				});
		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				int len = (int) (mWidth * positionOffset) + position
						* mWidth + 30;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vIndicvtorLine
						.getLayoutParams();// 获取当前横线的布局参数
				params.leftMargin = len;// 设置左边距

				vIndicvtorLine.setLayoutParams(params);// 重新给横线设置布局参数
			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					tvArticle.setTextColor(getResources().getColor(R.color.colorWhite));
					tvRank.setTextColor(getResources().getColor(R.color.normal_community_word));
				} else if (position == 1) {
					tvArticle.setTextColor(getResources().getColor(R.color.normal_community_word));
					tvRank.setTextColor(getResources().getColor(R.color.colorWhite));
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		tvArticle.setTextColor(getResources().getColor(R.color.colorWhite));
		tvRank.setTextColor(getResources().getColor(R.color.normal_community_word));
		return view;
	}

	@Override
	public void initData() {
	}

	@OnClick(R.id.tv_article)
	public void Article(View view) {
		mViewPager.setCurrentItem(0);
		tvArticle.setTextColor(getResources().getColor(R.color.colorWhite));
		tvRank.setTextColor(getResources().getColor(R.color.normal_community_word));
	}

	@OnClick(R.id.tv_rank)
	public void Rank(View view) {
		mViewPager.setCurrentItem(1);
		tvArticle.setTextColor(getResources().getColor(R.color.normal_community_word));
		tvRank.setTextColor(getResources().getColor(R.color.colorWhite));
	}
}
