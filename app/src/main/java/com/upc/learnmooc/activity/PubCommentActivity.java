package com.upc.learnmooc.activity;

import android.os.Bundle;
import android.view.View;

import com.upc.learnmooc.R;

/**
 * 发表评论
 * Created by Explorer on 2016/3/15.
 */
public class PubCommentActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub_comment_activity);
	}

	@Override
	public void initViews() {

	}

	public void Back(View view){
		finish();
	}
}
