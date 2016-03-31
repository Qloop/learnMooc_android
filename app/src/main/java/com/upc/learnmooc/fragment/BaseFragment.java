package com.upc.learnmooc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Explorer on 2016/2/8.
 */
public abstract class BaseFragment extends Fragment {
	public Activity mActivity;//依附的Activity

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initViews();
	}

	/**
	 * 依附的activity创建完成  初始化页面数据
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}


	//初始化页面数据 不必须
	public void initData() {

	}

	//子类必须实现布局初始化
	public abstract View initViews();

}
