package com.upc.learnmooc.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.upc.learnmooc.R;

/**
 * 关于我们
 * Created by Explorer on 2016/4/13.
 */
public class AboutUsFragment extends BaseFragment {

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.about_us_fragment, null);
		ViewUtils.inject(this, view);
		return view;
	}
}
