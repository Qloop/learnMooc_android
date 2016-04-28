package com.upc.learnmooc.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.AspectData;
import com.upc.learnmooc.fragment.AspectFrgment;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 技能树
 * Created by Explorer on 2016/3/22.
 */
public class SkillTreeActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private String mUrl = GlobalConstants.GET_ASPECT_DATA;
	private ArrayList<AspectData.AspectInfo> aspect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.apsect_status_color);//通知栏所需颜色
		}

		setContentView(R.layout.skill_tree_activity);

		initViews();
		initData();
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
		mTabLayout = (TabLayout) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

	}

	private void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result);

			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
			}
		});
	}

	private void parseData(String result) {
		Gson gson = new Gson();
		com.upc.learnmooc.domain.AspectData aspectData = gson.fromJson(result, AspectData.class);
		aspect = aspectData.aspectData;

		if (aspect != null) {
			setUpViewPager();
		}
	}

	private void setUpViewPager() {
		final List<Fragment> fragments = new ArrayList<>();
		for (int i = 0; i < aspect.size(); i++) {
			mTabLayout.addTab(mTabLayout.newTab().setText(aspect.get(i).getAspectName()));

			Fragment fragment = new AspectFrgment();
			Bundle bundle = new Bundle();
			bundle.putString("url",aspect.get(i).getUrl());
			fragment.setArguments(bundle);//
			fragments.add(fragment);
		}

		FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
				return aspect.get(position).getAspectName();
			}
		};

		mViewPager.setAdapter(mAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
		mTabLayout.setTabsFromPagerAdapter(mAdapter);
	}

	public void GoBack(View view){
		finish();
	}
}
