package com.upc.learnmooc.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.RankList;
import com.upc.learnmooc.global.GlobalConstants;

import java.util.ArrayList;

/**
 * 社区页中的排位板块
 * Created by Explorer on 2016/3/3.
 */
public class RankingFragment extends BaseFragment {

	@ViewInject(R.id.lv_rank_list)
	private ListView mListView;

	private String mUrl = GlobalConstants.GET_RANK_LIST;
	private ArrayList<RankList.RankInfo> rankInfos;
	private ViewStub viewStub;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.community_ranking_frgment, null);
		ViewUtils.inject(this, view);
		mListView = (ListView) view.findViewById(R.id.lv_rank_list);
		viewStub = (ViewStub) view.findViewById(R.id.vs_net_error);

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
				parseData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
//				ToastUtils.showToastShort(mActivity, "排位信息获取失败");
				viewStub.inflate().setVisibility(View.VISIBLE);
			}
		});
	}

	private void parseData(String result) {
		Gson gson = new Gson();
		RankList rankList = gson.fromJson(result, RankList.class);
		rankInfos = rankList.rankData;

		if (rankInfos != null) {
			mListView.setAdapter(new RankListAdapter());
		}
	}

	class RankListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return rankInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return rankInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_rank_listview, null);
				holder = new ViewHolder();
				holder.ivRank = (ImageView) convertView.findViewById(R.id.iv_rank);
				holder.tvRank = (TextView) convertView.findViewById(R.id.tv_rank);
				holder.tvNickName = (TextView) convertView.findViewById(R.id.tv_nickname);
				holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			RankList.RankInfo rankInfo = rankInfos.get(position);

			if (position == 0) {
				holder.ivRank.setVisibility(View.VISIBLE);
				holder.tvRank.setVisibility(View.GONE);

				holder.ivRank.setImageResource(R.drawable.first);
				holder.tvNickName.setText(rankInfo.getNickname());
				holder.tvNumber.setText(rankInfo.getScore() + "");

				holder.tvNickName.setTextSize(16);
				holder.tvNumber.setTextSize(16);
			} else if (position == 1) {
				holder.ivRank.setVisibility(View.VISIBLE);
				holder.tvRank.setVisibility(View.GONE);

				holder.ivRank.setImageResource(R.drawable.second);
				holder.tvNickName.setText(rankInfo.getNickname());
				holder.tvNumber.setText(rankInfo.getScore() + "");

				holder.tvNickName.setTextSize(16);
				holder.tvNumber.setTextSize(16);
			} else if (position == 2) {
				holder.ivRank.setVisibility(View.VISIBLE);
				holder.tvRank.setVisibility(View.GONE);

				holder.ivRank.setImageResource(R.drawable.third);
				holder.tvNickName.setText(rankInfo.getNickname());
				holder.tvNumber.setText(rankInfo.getScore() + "");

				holder.tvNickName.setTextSize(16);
				holder.tvNumber.setTextSize(16);
			} else {
				holder.ivRank.setVisibility(View.GONE);
				holder.tvRank.setVisibility(View.VISIBLE);

				holder.tvRank.setText(position + 1 + "");
				holder.tvNickName.setText(rankInfo.getNickname());
				holder.tvNumber.setText(rankInfo.getScore() + "");
			}

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivRank;
		public TextView tvRank;
		public TextView tvNickName;
		public TextView tvNumber;
	}
}
