package com.upc.learnmooc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.upc.learnmooc.domain.NoteInfo;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

import java.util.ArrayList;

/**
 * 我的笔记
 * Created by Explorer on 2016/4/7.
 */
public class MineNoteActivity extends BaseActivity {

	private ListView mListView;
	private String mUrl;
	private ArrayList<NoteInfo.NoteData> listCourse;
	private ViewStub viewStubNet;
	private ViewStub viewStubBlank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_note);

		initViews();
		initData();
	}


	@Override
	public void initViews() {
		mListView = (ListView) findViewById(R.id.lv_note);
		viewStubNet = (ViewStub) findViewById(R.id.vs_net_error);
		viewStubBlank = (ViewStub) findViewById(R.id.vs_blank_content);
	}

	private void initData() {
		mUrl = GlobalConstants.GET_NOTE_LIST;
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		//GET参数为用户id
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(MineNoteActivity.this, "id", 0) + "");

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
		NoteInfo noteInfo = gson.fromJson(result, NoteInfo.class);
		listCourse = noteInfo.listCourse;
		if (listCourse != null) {
			mListView.setAdapter(new ListViewAdapter());
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent();
					intent.putExtra("courseName", listCourse.get(position).getCourseName());
					intent.setClass(MineNoteActivity.this, NoteDetailActivity.class);
					startActivity(intent);
				}
			});
		} else {
			View contentView = viewStubBlank.inflate();
			contentView.setVisibility(View.VISIBLE);
			TextView tvHint = (TextView) contentView.findViewById(R.id.tv_hint);
			TextView tvHintDetail = (TextView) contentView.findViewById(R.id.tv_hint_detail);
			tvHint.setText("没有笔记");
			tvHintDetail.setText("去边看视频边记笔记吧^_^");
		}
	}

	class ListViewAdapter extends BaseAdapter {


		private final BitmapUtils bitmapUtils;

		public ListViewAdapter() {
			bitmapUtils = new BitmapUtils(MineNoteActivity.this);
			bitmapUtils.configDefaultLoadingImage(R.drawable.course_default_bg2);
		}

		@Override
		public int getCount() {
			return listCourse.size();
		}

		@Override
		public Object getItem(int position) {
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
				convertView = View.inflate(MineNoteActivity.this, R.layout.item_note_listview, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_note_num);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NoteInfo.NoteData noteData = listCourse.get(position);
//			bitmapUtils.display(holder.ivPic, noteData.getThumbnailUrl());
			holder.tvName.setText(noteData.getCourseName());
//			holder.tvNum.setText(noteData.getNoteNum() + "");

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivPic;
		public TextView tvName;
		public TextView tvNum;
	}

}
