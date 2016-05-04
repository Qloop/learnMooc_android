package com.upc.learnmooc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.upc.learnmooc.domain.CourseClassify;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 分类
 * Created by Explorer on 2016/2/27.
 */
public class ClassifyActivity extends BaseActivity {

	private ExpandableListView mExpandableListView;
	private String mUrl;
	private ArrayList<CourseClassify.ClassifyData> classifyData;
	private ViewStub viewStub;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classify_activity);

		initViews();
		initData();
	}

	/**
	 * 返回主页面
	 */
	public void BackToMain(View view) {
		startActivity(new Intent(ClassifyActivity.this, MainActivity.class));
		finish();
	}

	/**
	 * 搜索
	 */
	public void ToSearch(View view) {
		startActivity(new Intent(ClassifyActivity.this, SearchListActivity.class));
		finish();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mUrl = GlobalConstants.GET_COUTSE_CLASSIFY;
		getDataFromServer();
	}

	@Override
	public void initViews() {
//		mExpandableListView = (ExpandableListView) findViewById(R.id.el_course_classify);
//		mExpandableListView.setGroupIndicator(null);//去掉向下的箭头
		mListView = (ListView) findViewById(R.id.lv_course_classify);

		viewStub = (ViewStub) findViewById(R.id.vs_net_error);
	}

	public void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseData(result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				ToastUtils.showToastShort(ClassifyActivity.this, "分类数据获取失败");
				viewStub.inflate().setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 解析返回的json数据
	 */
	private void parseData(String result) {
		Gson gson = new Gson();
		CourseClassify courseClassifyInfo = gson.fromJson(result, CourseClassify.class);
		classifyData = courseClassifyInfo.classifyData;

		if (classifyData != null) {
//			MyExpandAdapter adapter = new MyExpandAdapter();
//			mExpandableListView.setAdapter(adapter);
//			//设置默认全部展开
//			for (int i = 0; i < adapter.getGroupCount(); i++) {
//				mExpandableListView.expandGroup(i);
//			}
			mListView.setAdapter(new ClassifyListViewAdapter());
		}
	}

	class ClassifyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return classifyData.size();
		}

		@Override
		public Object getItem(int position) {
			return classifyData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(ClassifyActivity.this, R.layout.item_classify_listview, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_classify_name);
				holder.gvCourse = (GridView) convertView.findViewById(R.id.gv_classify);
				holder.mAdapter = new GridViewAdapter();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final CourseClassify.ClassifyData classifyData = ClassifyActivity.this.classifyData.get(position);
			holder.tvName.setText(classifyData.getClassifyName());
			holder.mAdapter.setData(classifyData.getCourseInfo());
			holder.gvCourse.setAdapter(holder.mAdapter);
			holder.gvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent();
					intent.setClass(ClassifyActivity.this, VideoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putLong("id",classifyData.getCourseInfo().get(position).getCourseId());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});

			return convertView;
		}

		class ViewHolder {
			public TextView tvName;
			public GridView gvCourse;
			public GridViewAdapter mAdapter;
		}
	}

	/**
	 * listview中的每个item的gridview适配器
	 */
	class GridViewAdapter extends BaseAdapter {

		private ArrayList<CourseClassify.CourseInfo> mCourseInfos;
		private final BitmapUtils bitmapUtils;


		public GridViewAdapter() {
			bitmapUtils = new BitmapUtils(ClassifyActivity.this);
//			bitmapUtils.configDefaultLoadingImage(R.drawable.html);
		}


		public void setData(ArrayList<CourseClassify.CourseInfo> courseInfos) {
			this.mCourseInfos = courseInfos;
		}


		@Override
		public int getCount() {
			return mCourseInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return mCourseInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(ClassifyActivity.this, R.layout.item_classify_gridview, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
				holder.tvCourseName = (TextView) convertView.findViewById(R.id.tv_course_title);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_learner);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CourseClassify.CourseInfo courseInfo = mCourseInfos.get(position);

			holder.tvCourseName.setText(courseInfo.getCourseName());
			holder.tvNum.setText(courseInfo.getNum()+"");
			bitmapUtils.display(holder.ivPic, courseInfo.getThumbnail());

			return convertView;
		}

		class ViewHolder {
			public ImageView ivPic;
			public TextView tvCourseName;
			public TextView tvNum;
		}

	}

	/**
	 * expandablelistView数据适配器
	 */
//	class MyExpandAdapter extends BaseExpandableListAdapter {
//
//		private final BitmapUtils bitmapUtils;
//
//		public MyExpandAdapter() {
//			bitmapUtils = new BitmapUtils(ClassifyActivity.this);
//			bitmapUtils.configDefaultLoadingImage(R.drawable.html);
//		}
//
//		@Override
//		public int getGroupCount() {
//			return classifyData.size();
//		}
//
//		@Override
//		public int getChildrenCount(int groupPosition) {
//			if (classifyData.get(groupPosition).courseInfo.size() % 2 == 0) {
//				return classifyData.get(groupPosition).courseInfo.size() / 2;
//			} else {
//				return classifyData.get(groupPosition).courseInfo.size() / 2 + 1;
//			}
//		}
//
//		@Override
//		public Object getGroup(int groupPosition) {
//			return classifyData.get(groupPosition).classifyName;
//		}
//
//		@Override
//		public Object getChild(int groupPosition, int childPosition) {
//			int pos = childPosition * 2;
//			return classifyData.get(groupPosition).courseInfo.get(pos);
//		}
//
//		@Override
//		public long getGroupId(int groupPosition) {
//			return groupPosition;
//		}
//
//		@Override
//		public long getChildId(int groupPosition, int childPosition) {
//			return childPosition;
//		}
//
//		@Override
//		public boolean hasStableIds() {
//			return true;
//		}
//
//		@Override
//		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//			convertView = View.inflate(ClassifyActivity.this, R.layout.item_group_expandlistview, null);
//			TextView classifyName = (TextView) convertView.findViewById(R.id.tv_classify_name);
//			classifyName.setText(classifyData.get(groupPosition).classifyName);
//			return convertView;
//		}
//
//		@Override
//		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = View.inflate(ClassifyActivity.this, R.layout.item_child_expandlistview, null);
//				holder = new ViewHolder();
//				holder.ivCourseImg1 = (ImageView) convertView.findViewById(R.id.iv_thumbnail1);
//				holder.tvCourseName1 = (TextView) convertView.findViewById(R.id.tv_course_title1);
//				holder.tvNum1 = (TextView) convertView.findViewById(R.id.tv_learner1);
//				holder.secondLayout = (LinearLayout) convertView.findViewById(R.id.ll_second_layout);
//
//				holder.ivCourseImg2 = (ImageView) convertView.findViewById(R.id.iv_thumbnail2);
//				holder.tvCourseName2 = (TextView) convertView.findViewById(R.id.tv_course_title2);
//				holder.tvNum2 = (TextView) convertView.findViewById(R.id.tv_learner2);
//
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			//手动将第二项初始为可见  否则会出现显示混乱
//			holder.secondLayout.setVisibility(View.VISIBLE);
//
//			int pos = childPosition * 2;
//			//这一行的第一项填充数据
//			CourseClassify.CourseInfo courseInfo = classifyData.get(groupPosition).courseInfo.get(pos);
//			bitmapUtils.display(holder.ivCourseImg1, courseInfo.getThumbnail());
//			holder.tvCourseName1.setText(courseInfo.getCourseName());
//			holder.tvNum1.setText(courseInfo.getNum() + "");
//
//			if (pos == (classifyData.get(groupPosition).courseInfo.size() - 1)) {
//				holder.secondLayout.setVisibility(View.GONE);
//			} else {
//				//这一行的第二项填充数据
//				CourseClassify.CourseInfo courseInfo2 = classifyData.get(groupPosition).courseInfo.get(pos + 1);
//				if (courseInfo2 != null) {
//					bitmapUtils.display(holder.ivCourseImg2, courseInfo2.getThumbnail());
//					holder.tvCourseName2.setText(courseInfo2.getCourseName());
//					holder.tvNum2.setText(courseInfo2.getNum() + "");
//				} else {
//					holder.secondLayout.setVisibility(View.GONE);
//				}
//			}
//
//			return convertView;
//		}
//
//		@Override
//		public boolean isChildSelectable(int groupPosition, int childPosition) {
//			return true;
//		}
//	}

	static class ViewHolder {
		public ImageView ivCourseImg1;
		public TextView tvCourseName1;
		public TextView tvNum1;

		public ImageView ivCourseImg2;
		public TextView tvCourseName2;
		public TextView tvNum2;

		public LinearLayout secondLayout;
	}

}
