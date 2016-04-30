package com.upc.learnmooc.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.upc.learnmooc.R;
import com.upc.learnmooc.fragment.CollectedArticleFragment;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * "收藏的文章"
 */
public class SelfArticleActivity extends AppCompatActivity {

	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private List<String> mUrlLsit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.status_color);//通知栏所需颜色
		}
		setContentView(R.layout.self_article_activity);
		initData();
		initViews();
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

	private void initViews() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setHomeAsUpIndicator(R.drawable.toolbar_back);
		supportActionBar.setDisplayHomeAsUpEnabled(true);

		mTabLayout = (TabLayout) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.vp_articles);
		setupViewPager();

	}

	private void initData() {
		mUrlLsit = new ArrayList<>();
		mUrlLsit.add(GlobalConstants.GET_COLLECTED_ARTICLE);
		mUrlLsit.add(GlobalConstants.GET_SELF_ARTICLE);
	}

	private void setupViewPager() {
		final List<String> titles = new ArrayList<>();
		final List<Fragment> fragments = new ArrayList<>();
		titles.add("收藏");
		titles.add("原创");
		for (int i = 0; i < mUrlLsit.size(); i++) {
			mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
			Fragment fragment = new CollectedArticleFragment();
			Bundle bundle = new Bundle();
			bundle.putString("url", mUrlLsit.get(i));
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}
		FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return fragments.get(position);
			}

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles.get(position);
			}
		};
		mViewPager.setAdapter(adapter);
		mTabLayout.setupWithViewPager(mViewPager);
		mTabLayout.setTabsFromPagerAdapter(adapter);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

