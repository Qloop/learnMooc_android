package com.upc.learnmooc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.SearchResult;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.SearchHistoryCacheUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 * Created by Explorer on 2016/4/29.
 */
public class SearchListActivity extends BaseActivity {


	private EditText etSearchContent;
	private TextView tvSearch;
	private ListView mListViewResult;
	private ListView mListViewHistory;
	private ArrayAdapter<String> historyAdapter;
	private LinearLayout llHistory;
	private LinearLayout llResult;
	private String mUrl = GlobalConstants.GET_SEARCH_RESULT;
	private ArrayList<SearchResult.ListCourse> searchResultInfo;
	private ViewStub vsNetError;
	private ViewStub vsBlankContent;
	private ImageView ivClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		initViews();
	}

	@Override
	public void initViews() {
		vsNetError = (ViewStub) findViewById(R.id.vs_net_error);
		vsBlankContent = (ViewStub) findViewById(R.id.vs_blank_content);

		ivClear = (ImageView) findViewById(R.id.iv_clear_content);
		etSearchContent = (EditText) findViewById(R.id.et_search_content);
		tvSearch = (TextView) findViewById(R.id.tv_search);
		llHistory = (LinearLayout) findViewById(R.id.ll_search_history);
		llResult = (LinearLayout) findViewById(R.id.ll_search_result);
		mListViewHistory = (ListView) findViewById(R.id.lv_search_history);
		mListViewResult = (ListView) findViewById(R.id.lv_search_result);
		etSearchContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 0) {//相关课程listview隐藏 搜索历史显示
					llResult.setVisibility(View.GONE);
					llHistory.setVisibility(View.VISIBLE);
				} else {//相关课程listview显示 搜索历史隐藏
					if (llHistory.getVisibility() == View.VISIBLE) {
						llHistory.setVisibility(View.GONE);
					}
					if (ivClear.getVisibility() == View.GONE) {
						ivClear.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivClear.setVisibility(View.GONE);
				}
			}
		});

		initSearchHistory();
	}

	/**
	 * 初始化搜索历史的记录显示
	 */
	private void initSearchHistory() {
		String cache = SearchHistoryCacheUtils.getCache(SearchListActivity.this);
		if (cache != null) {
			List<String> historyRecordList = new ArrayList<>();
			for (String record : cache.split(",")) {
				historyRecordList.add(record);
			}
			historyAdapter = new ArrayAdapter<String>(SearchListActivity.this,
					R.layout.item_search_history, historyRecordList);
			if (historyRecordList.size() > 0) {
				mListViewHistory.setAdapter(historyAdapter);
				mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						etSearchContent.setText("");
						etSearchContent.setText(historyAdapter.getItem(position));
					}
				});
			}
		} else {
			llHistory.setVisibility(View.GONE);
		}

	}

	private void save(String text) {
		String oldCache = SearchHistoryCacheUtils.getCache(SearchListActivity.this);
		StringBuilder builder = new StringBuilder(text);
		if (oldCache == null) {
			SearchHistoryCacheUtils.setCache(builder.toString(), SearchListActivity.this);
			updateData();
		} else {
			builder.append("," + oldCache);
//			if(oldCache.contains(text)){
//
//			}
			if (!oldCache.contains(text)) {//避免缓存重复的数据
				SearchHistoryCacheUtils.setCache(builder.toString(), SearchListActivity.this);
				updateData();
			}
		}
	}

	/**
	 * 更新搜索历史数据显示
	 */
	private void updateData() {
		String cache = SearchHistoryCacheUtils.getCache(SearchListActivity.this);
		String[] recordData = new String[]{};
		if (cache != null) {
			recordData = cache.split(",");
		}
		historyAdapter = new ArrayAdapter<String>(SearchListActivity.this, R.layout.item_search_history, recordData);
		mListViewHistory.setAdapter(historyAdapter);
		mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				etSearchContent.setText("");
				etSearchContent.setText(historyAdapter.getItem(position));
			}
		});
		historyAdapter.notifyDataSetChanged();
	}

	/**
	 * 点击"搜索"
	 */
	public void StartSearch(View view) {
		String text = etSearchContent.getText().toString();
		if (TextUtils.isEmpty(text)) {
			return;
		}
		//缓存搜索历史
		save(text);

		//网络请求
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5000);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("courseName", text);
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				vsNetError.inflate().setVisibility(View.VISIBLE);
			}
		});

	}

	private void parseData(String result) {
		Gson gson = new Gson();
		SearchResult searchResult = gson.fromJson(result, SearchResult.class);
		searchResultInfo = searchResult.searchResult;
		if (searchResultInfo != null) {
			mListViewResult.setAdapter(new ResultListAdapter());
			mListViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent();
					intent.setClass(SearchListActivity.this, VideoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putLong("id", searchResultInfo.get(position).getCourseId());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});

			if (llResult.getVisibility() == View.GONE) {
				llResult.setVisibility(View.VISIBLE);
			}

			//收起软键盘 避免遮挡
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} else {
			View inflate = vsBlankContent.inflate();
			TextView hint = (TextView) inflate.findViewById(R.id.tv_hint);
			TextView hintDetail = (TextView) inflate.findViewById(R.id.tv_hint_detail);
			hint.setText("没有相关的课程T_T");
			hintDetail.setText("试试其他搜索词吧*.*");
		}
	}

	/**
	 * 搜索结果 课程列表listview适配器
	 */
	class ResultListAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public ResultListAdapter() {
			bitmapUtils = new BitmapUtils(SearchListActivity.this);
			bitmapUtils.configDefaultLoadingImage(R
					.drawable.course_default_bg2);//设置默认显示的图片
		}

		@Override
		public int getCount() {
			return searchResultInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return searchResultInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(SearchListActivity.this, R.layout.item_course_list, null);
				holder = new ViewHolder();
				holder.ivCourseImg = (ImageView) convertView.findViewById(R.id.iv_course_img);
				holder.tvCourseName = (TextView) convertView.findViewById(R.id.tv_course_description);
				holder.tvLearnerNum = (TextView) convertView.findViewById(R.id.tv_learner_number);
				holder.tvCoursedate = (TextView) convertView.findViewById(R.id.tv_update_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SearchResult.ListCourse listCourse = searchResultInfo.get(position);
			holder.tvCourseName.setText(listCourse.getCourseName());
			holder.tvLearnerNum.setText(listCourse.getNum() + "");
			holder.tvCoursedate.setText(listCourse.getPubdate());
			bitmapUtils.display(holder.ivCourseImg, listCourse.getThumbnailUrl());

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivCourseImg;
		public TextView tvCourseName;
		public TextView tvLearnerNum;
		public TextView tvCoursedate;

	}

	/**
	 * 清空输入框
	 */
	public void ClearContent(View view) {
		etSearchContent.setText("");
		ivClear.setVisibility(View.GONE);
	}

	/**
	 * 清空搜索历史
	 */
	public void ClearSearchHistory(View view) {
		SearchHistoryCacheUtils.ClearCache(SearchListActivity.this);
		updateData();
	}
}
