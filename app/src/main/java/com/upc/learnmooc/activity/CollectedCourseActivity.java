package com.upc.learnmooc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.MainCourse;
import com.upc.learnmooc.global.GlobalConstants;

import java.util.ArrayList;

/**
 * 关注的课程
 * Created by Explorer on 2016/4/5.
 */
public class CollectedCourseActivity extends BaseActivity {

	private ListView mListView;
	private String mUrl;
	private ArrayList<MainCourse.ListCourse> listCourse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_course_activity);
		initViews();
		initData();
	}

	@Override
	public void initViews() {
		mListView = (ListView) findViewById(R.id.lv_collect_course);
	}

	private void initData() {
		mUrl = GlobalConstants.GET_MAIN_COURSE_URL;
		getDataFromServer();

	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		//GET参数为用户id
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("id", UserInfoCacheUtils.getInt(CollectedCourseActivity.this,"id",0)+"");

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
		MainCourse mainCourse = gson.fromJson(result, MainCourse.class);
		listCourse = mainCourse.listCourse;
		if(listCourse != null){
			mListView.setAdapter(new ListCourseAdapter());
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent();
					intent.setClass(CollectedCourseActivity.this, VideoActivity.class);
					intent.putExtra("id", listCourse.get(position).getCourseId());
					startActivity(intent);
				}
			});
		}

	}

	/**
	 * 课程列表listview适配器
	 */
	class ListCourseAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public ListCourseAdapter() {
			bitmapUtils = new BitmapUtils(CollectedCourseActivity.this);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);//设置默认显示的图片
		}

		@Override
		public int getCount() {
			return listCourse.size();
		}

		@Override
		public MainCourse.ListCourse getItem(int position) {
			return listCourse.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CollectedCourseActivity.this, R.layout.item_course_list, null);
				holder = new ViewHolder();
				holder.ivCourseImg = (ImageView) convertView.findViewById(R.id.iv_course_img);
				holder.tvCourseName = (TextView) convertView.findViewById(R.id.tv_course_description);
				holder.tvLearnerNum = (TextView) convertView.findViewById(R.id.tv_learner_number);
				holder.tvCoursedate = (TextView) convertView.findViewById(R.id.tv_update_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MainCourse.ListCourse item = getItem(position);
			holder.tvCourseName.setText(item.getCourseName());
			holder.tvLearnerNum.setText(item.getNum() + "");
			holder.tvCoursedate.setText(item.getPubdate());
			bitmapUtils.display(holder.ivCourseImg, item.getThumbnailUrl());

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivCourseImg;
		public TextView tvCourseName;
		public TextView tvLearnerNum;
		public TextView tvCoursedate;

	}

}
