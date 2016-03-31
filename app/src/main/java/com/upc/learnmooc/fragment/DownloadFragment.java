package com.upc.learnmooc.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.upc.learnmooc.R;

/**
 * 下载页
 * Created by Explorer on 2016/2/10.
 */
public class DownloadFragment extends BaseFragment{
	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.download_fragment,null);

		//view和事件注入
		ViewUtils.inject(this,view);
		return view;
	}
}
