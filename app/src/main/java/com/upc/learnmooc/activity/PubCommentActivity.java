package com.upc.learnmooc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

/**
 * 发表评论
 * Created by Explorer on 2016/3/15.
 */
public class PubCommentActivity extends BaseActivity {


	private EditText etCommentContent;
	private String mUrl = GlobalConstants.POST_COMMENT;
	private long courseId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub_comment_activity);
		initViews();
	}

	@Override
	public void initViews() {
		etCommentContent = (EditText) findViewById(R.id.et_edit_comment);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		courseId = extras.getLong("courseId");
	}

	public void Back(View view) {
		finish();
	}

	/**
	 * 发表评论
	 */
	public void PublishComment(View view) {
		if (TextUtils.isEmpty(etCommentContent.getText().toString())) {
			//内容为空 不提交
			ToastUtils.showToastShort(PubCommentActivity.this, "说点啥再发布吧*.0");
		} else {
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.configTimeout(5000);
			RequestParams params = new RequestParams();
			params.addBodyParameter("commentContent", etCommentContent.getText().toString());
			params.addBodyParameter("userId", UserInfoCacheUtils.getLong(PubCommentActivity.this, "id", 0) + "");
			params.addBodyParameter("courseId", courseId + "");
			httpUtils.send(HttpRequest.HttpMethod.POST, mUrl, params, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					ToastUtils.showToastLong(PubCommentActivity.this, responseInfo.result);
					finish();
				}

				@Override
				public void onFailure(HttpException e, String s) {
					ToastUtils.showToastLong(PubCommentActivity.this, s);
				}
			});
		}
	}
}
