package com.upc.learnmooc.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.upc.learnmooc.domain.CourseHistory;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

import java.util.ArrayList;

/**
 * 学习历史记录
 * Created by Explorer on 2016/2/29.
 */
public class CourseHistoryActivity extends BaseActivity {


	private ListView mListView;
	private String mUrl;
	private ArrayList<CourseHistory.HistoryData> historyData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_activity);
		initViews();
		initData();
	}


	@Override
	public void initViews() {
		mListView = (ListView) findViewById(R.id.lv_timeTree);
	}


	private void initData() {
		mUrl = GlobalConstants.GET_HISTORY_COURSE;
		String cache = UserInfoCacheUtils.getCache(mUrl, CourseHistoryActivity.this);
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		//get请求参数据---用户id
		RequestParams params = new RequestParams();
		String id = UserInfoCacheUtils.getInt(CourseHistoryActivity.this, "id", 0) + "";//从缓存到本地的用户信息中拿到id 默认为0 也就是没有用户
		params.addQueryStringParameter("id", id);
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result);

				//设置缓存
				UserInfoCacheUtils.setCache(mUrl, responseInfo.result, CourseHistoryActivity.this);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				ToastUtils.showToastShort(CourseHistoryActivity.this, "网络链接失败");
			}
		});
	}

	private void parseData(String result) {
		Gson gson = new Gson();
		CourseHistory courseHistory = gson.fromJson(result, CourseHistory.class);
		historyData = courseHistory.historyData;
		if (historyData != null) {
			mListView.setAdapter(new MyTimeListAdapater());
		}
	}

	/**
	 * 外层listview 数据适配器
	 */
	class MyTimeListAdapater extends BaseAdapter {

		@Override
		public int getCount() {
			return historyData.size();
		}

		@Override
		public Object getItem(int position) {
			return historyData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CourseHistoryActivity.this, R.layout.item_history_listview, null);
				holder = new ViewHolder();
				holder.llContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
				holder.llBlank = (LinearLayout) convertView.findViewById(R.id.ll_blank);//填充区域
				holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
				holder.viewLine = convertView.findViewById(R.id.v_view2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			//动态添加控件之前要先把之前的清空  不然会混带在下一个item里
			holder.llContent.removeAllViews();
			//设置历史时间
			holder.tvTime.setText(historyData.get(position).getTime());
			ArrayList<CourseHistory.CourseData> historyCourse = historyData.get(position).historyCourse;
			//设置上 只有存在浏览历史的课程才有记录 这里判断避免出现nullPoint
			if (historyCourse != null) {
				//根据历史课程的数量 动态添加到rlContent
				for (int i = 0; i < historyCourse.size(); i++) {
					TextView tvCourse = CreateTextView(CourseHistoryActivity.this, historyCourse.get(i).getCourseName());

					holder.llContent.addView(tvCourse);
				}
				//根据llContent的高度动态给llBlank设置高度 将下边的线撑起来
				holder.llContent.measure(0, 0);
				int measuredHeight = holder.llContent.getMeasuredHeight();
				holder.llBlank.setLayoutParams(new LinearLayout.LayoutParams(0,measuredHeight + 1));
			}
			return convertView;
		}

	}

	/**
	 * 动态产生TextView
	 */
	private TextView CreateTextView(Context ctx, String text) {
		TextView tvCourse = new TextView(ctx);
		tvCourse.setTextColor(getResources().getColor(R.color.history_course_name_color));
		tvCourse.setText(text);
		tvCourse.setTextSize(16);
		tvCourse.setSingleLine();
		tvCourse.setMaxEms(15);
		tvCourse.setEllipsize(TextUtils.TruncateAt.END);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
//		params.gravity = Gravity.CENTER;
		tvCourse.setLayoutParams(params);
		tvCourse.setPadding(25, 50, 0, 25);
		return tvCourse;
	}

	static class ViewHolder {
		public LinearLayout llContent;
		public LinearLayout llBlank;
		public TextView tvTime;
		public View viewLine;
	}

	/**
	 * 清空记录
	 */
//	public void ClearAll(View view) {
//
//	}
}
