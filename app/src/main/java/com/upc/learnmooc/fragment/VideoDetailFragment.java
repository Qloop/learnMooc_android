package com.upc.learnmooc.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.VideoDetail;
import com.upc.learnmooc.global.GlobalConstants;

/**
 * 课程详情签
 * Created by Explorer on 2016/3/13.
 */
public class VideoDetailFragment extends BaseFragment {

	@ViewInject(R.id.tv_detail_name)
	private TextView tvCourseName;
	@ViewInject(R.id.tv_detail_description)
	private TextView tvDetailDesription;
	@ViewInject(R.id.tv_teacher_name)
	private TextView tvTeacherName;
	@ViewInject(R.id.tv_teacher_introduction)
	private TextView tvTeacherIntro;
	private String mUrl = GlobalConstants.GET_VIDEO_DETAILTINFO;
	private long courseId;

	@Override

	public View initViews() {
		View view = View.inflate(mActivity, R.layout.video_detail_fragment, null);
		ViewUtils.inject(this, view);
		Bundle arguments = getArguments();
		courseId = arguments.getLong("id", 1);
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("courseId", courseId + "");
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseDate(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
//				ToastUtils.showToastShort(VideoActivity.this, "网络错误");
			}
		});
	}

	private void parseDate(String result) {
		Gson gson = new Gson();
		VideoDetail videoDetail = gson.fromJson(result, VideoDetail.class);

		//给各个textview填充数据
		tvCourseName.setText(videoDetail.courseName);
		tvDetailDesription.setText(videoDetail.detail);
		tvTeacherName.setText(videoDetail.teacherInfo.getName());
//		tvTeacherIntro.setText(videoDetail.teacherInfo.getIntroduction());
	}
}
