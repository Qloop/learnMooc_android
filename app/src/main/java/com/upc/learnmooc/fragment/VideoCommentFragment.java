package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.PubCommentActivity;
import com.upc.learnmooc.domain.VideoComment;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.view.CircleImageView;

import java.util.ArrayList;

/**
 * 课程评论签
 * Created by Explorer on 2016/3/13.
 */
public class VideoCommentFragment extends BaseFragment {


	@ViewInject(R.id.lv_comment)
	private ListView mListView;
	@ViewInject(R.id.fab_add_comment)
	private FloatingActionButton fabAddComment;
	private String mUrl = GlobalConstants.GET_VIDEO_COMMENTINFO;
	private ArrayList<VideoComment.CommentInfo> commentInfos;
	private long courseId;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.video_comment_fragment, null);
		ViewUtils.inject(this, view);

		Bundle arguments = getArguments();
		courseId = arguments.getLong("id", 1);
		fabAddComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, PubCommentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("courseId", courseId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
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
		params.addQueryStringParameter("courseid", courseId + "");
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
		VideoComment videoComment = gson.fromJson(result, VideoComment.class);
		commentInfos = videoComment.comment;
		if (commentInfos != null) {
			mListView.setAdapter(new CommentListAdapter());
		}
	}

	class CommentListAdapter extends BaseAdapter {

		BitmapUtils bitmapUtils;

		public CommentListAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.mine_normal);
		}

		@Override
		public int getCount() {
			return commentInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return commentInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_comment_listview, null);
				holder = new ViewHolder();
				holder.civAvatar = (CircleImageView) convertView.findViewById(R.id.civ_avatar);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

				//填充数据
				bitmapUtils.display(holder.civAvatar, commentInfos.get(position).getAvatarUrl());
				holder.tvName.setText(commentInfos.get(position).getNickname());
				holder.tvComment.setText(commentInfos.get(position).getCommentContent());
			}

			return convertView;
		}
	}

	static class ViewHolder {
		public CircleImageView civAvatar;
		public TextView tvName;
		public TextView tvComment;
	}
}
