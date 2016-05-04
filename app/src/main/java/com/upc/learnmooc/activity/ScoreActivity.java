package com.upc.learnmooc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.Score;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

import java.util.ArrayList;

/**
 * 我的成绩
 * Created by Explorer on 2016/4/21.
 */
public class ScoreActivity extends BaseActivity {

	private ListView mListView;
	private String mUrl;
	private ArrayList<Score.ScoreData> scoreList;
	private ViewStub viewStubNet;
	private ViewStub viewStubBlank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_activity);
		initViews();
		initData();
	}

	@Override
	public void initViews() {
		mListView = (ListView) findViewById(R.id.lv_score);
		viewStubNet = (ViewStub) findViewById(R.id.vs_net_error);
		viewStubBlank = (ViewStub) findViewById(R.id.vs_blank_content);
	}

	private void initData() {
		mUrl = GlobalConstants.GET_SCORE_LIST;
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5000);
		httpUtils.configCurrentHttpCacheExpiry(5000);
		//GET参数为用户id
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(ScoreActivity.this, "id", 0) + "");
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				viewStubNet.inflate().setVisibility(View.VISIBLE);
			}
		});
	}

	private void parseData(String result) {
		Gson gson = new Gson();
		Score score = gson.fromJson(result, Score.class);
		scoreList = score.scoreList;
		if (scoreList != null) {
			mListView.setAdapter(new ScoreListAdpater());
		} else {
			View contentView = viewStubBlank.inflate();
			contentView.setVisibility(View.VISIBLE);
			TextView tvHint = (TextView) contentView.findViewById(R.id.tv_hint);
			TextView tvHintDetail = (TextView) contentView.findViewById(R.id.tv_hint_detail);
			tvHint.setText("没有成绩信息");
			tvHintDetail.setText("快去学习新知识吧^_^");
		}
	}

	class ScoreListAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			return scoreList.size();
		}

		@Override
		public Object getItem(int position) {
			return scoreList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(ScoreActivity.this, R.layout.item_score_listview, null);
				holder = new ViewHolder();
				holder.tvCourseName = (TextView) convertView.findViewById(R.id.tv_course_name);
				holder.tvScores = (TextView) convertView.findViewById(R.id.tv_score);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Score.ScoreData scoreData = scoreList.get(position);
			holder.tvCourseName.setText(scoreData.getCourseName());
			holder.tvScores.setText(scoreData.getScores() + "");

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvCourseName;
		public TextView tvScores;
	}
}
