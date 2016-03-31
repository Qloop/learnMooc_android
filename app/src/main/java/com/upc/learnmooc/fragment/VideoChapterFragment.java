package com.upc.learnmooc.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.VideoChapter;
import com.upc.learnmooc.global.GlobalConstants;

import java.util.ArrayList;

/**
 * 课程章节签
 * Created by Explorer on 2016/3/13.
 */
public class VideoChapterFragment extends BaseFragment implements VDVideoExtListeners.OnVDVideoPlaylistListener{

	@ViewInject(R.id.elv_chapter)
	private ExpandableListView mEListView;
	private String mUrl = GlobalConstants.GET_VIDEO_CHAPTERINFO;
	private ArrayList<VideoChapter.ChapterInfo> chapterInfos;
	private VDVideoView mVDVideoView;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.video_chapter_fragment, null);
		ViewUtils.inject(this, view);

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

		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
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
		VideoChapter videoChapter = gson.fromJson(result, VideoChapter.class);
		chapterInfos = videoChapter.chapterInfo;
		if (chapterInfos != null) {
			final MyExpandAdapter adapter = new MyExpandAdapter();
			mEListView.setAdapter(adapter);
			//设置默认全部展开
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				mEListView.expandGroup(i);
			}

			mEListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					mVDVideoView = (VDVideoView) mActivity.findViewById(R.id.vd_video_view);
					int dex = (groupPosition + 1) * childPosition;
					mVDVideoView.play(dex);

					return true;
				}
			});
		}

	}

	@Override
	public void onPlaylistClick(VDVideoInfo info, int p) {
		mVDVideoView.play(p);
	}

	class MyExpandAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return chapterInfos.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return chapterInfos.get(groupPosition).section.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return chapterInfos.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return chapterInfos.get(groupPosition).section.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_chapter_parent_listview, null);
				holder = new ViewHolder();
				holder.tvChapterName = (TextView) convertView.findViewById(R.id.tv_chapter_name);
				convertView.setTag(holder);
//
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvChapterName.setText(chapterInfos.get(groupPosition).getChapter());
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_chapter_child_listview, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivChapter = (ImageView) convertView.findViewById(R.id.iv_chapter);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//			if (groupPosition == 0 && childPosition == 0) {
//				holder.ivChapter.setImageResource(R.drawable.chapter_arrow_foursed);
//				holder.tvName.setTextColor(getResources().getColor(R.color.word_pressed_color));
//			} else {
//				holder.ivChapter.setImageResource(R.drawable.chapter_arrow_normal);
//			}
			if (chapterInfos.get(groupPosition).section.get(childPosition).content != null && holder.tvName != null) {
				holder.tvName.setText(chapterInfos.get(groupPosition).section.get(childPosition).content);
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	static class ViewHolder {
		public ImageView ivChapter;
		public TextView tvChapterName;
		public TextView tvName;
	}

}
