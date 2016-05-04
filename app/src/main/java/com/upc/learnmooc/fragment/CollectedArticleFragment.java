package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.ArticleActivity;
import com.upc.learnmooc.domain.ArticleList;
import com.upc.learnmooc.utils.RecyclerAdapter;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

import java.util.ArrayList;

/**
 * Created by Explorer on 2016/4/3.
 */
public class CollectedArticleFragment extends BaseFragment {

	private View view;
	private String mUrl;

	@ViewInject(R.id.recycler_view)
	private RecyclerView mRecyclerView;
	private ArrayList<ArticleList.ArticleInfo> articleData;
	@ViewInject(R.id.vs_blank_content)
	private ViewStub viewStub;

	@Override
	public View initViews() {
		if (view == null) {
			view = View.inflate(mActivity, R.layout.item_collection_article_viewpager, null);
		}

		ViewUtils.inject(this, view);
		Bundle arguments = getArguments();
		if (arguments != null) {
			mUrl = arguments.getString("url");
		}
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);

		//GET参数为用户id
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(mActivity, "id", 0) + "");

		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
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
		ArticleList articleList = gson.fromJson(result, ArticleList.class);
		articleData = articleList.articleData;
		if (articleData != null) {
			System.out.println("data is " + articleData.toString());

			mRecyclerView.setHasFixedSize(true);
			mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
			//设置显示动画
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			RecyclerAdapter adapter = new com.upc.learnmooc.utils.RecyclerAdapter(articleData, mActivity);
			mRecyclerView.setAdapter(adapter);

			adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					Intent intent = new Intent();
					intent.setClass(mActivity, ArticleActivity.class);
					intent.putExtra("url", articleData.get(position).getArticleUrl());
					intent.putExtra("title", articleData.get(position).getTitle());
					startActivity(intent);
				}
			});

		} else {
			View inflate = viewStub.inflate();
			inflate.setVisibility(View.VISIBLE);
			TextView tvHint = (TextView)inflate.findViewById(R.id.tv_hint);
			TextView tvDetailHint = (TextView)inflate.findViewById(R.id.tv_hint_detail);
			tvHint.setText("没有文章");
//			tvDetailHint.setText("请前往web端发布文章吧8.8");
		}
	}

	/**
	 * listview数据适配器
	 */
//	class ListArticleAdapter extends BaseAdapter {
//
//
//		private final BitmapUtils bitmapUtils;
//
//		public ListArticleAdapter() {
//			bitmapUtils = new BitmapUtils(mActivity);
//			bitmapUtils.configDefaultLoadingImage(R.drawable.img_artilce);//默认加载图
//		}
//
//		@Override
//		public int getCount() {
//			return articleData.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return articleData.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = View.inflate(mActivity, R.layout.item_article_detail_listview, null);
//				holder = new ViewHolder();
//				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
//				holder.tvClassify = (TextView) convertView.findViewById(R.id.tv_classify_article);
//				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
//				holder.tvDetail = (TextView) convertView.findViewById(R.id.tv_detail);
//				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_cover_img);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			//设置填充内容
//			ArticleList.ArticleInfo articleInfo = articleData.get(position);
//			holder.tvTitle.setText(articleInfo.getTitle());
//			holder.tvClassify.setText(articleInfo.getClassify());
//			holder.tvNum.setText(articleInfo.getNum() + "");
//			bitmapUtils.display(holder.ivPic, articleInfo.getImg());
//
//			//设置文章内容部分展示的填充
//			//又后台获取html内容 解析后返回给前端
//			//直接又前端解析 耗时大 显示延迟严重
//			holder.tvDetail.setText(articleInfo.getAbstractInfo());
//
//			return convertView;
//		}
//	}
//
//	static class ViewHolder {
//		public TextView tvTitle;
//		public TextView tvClassify;
//		public TextView tvNum;
//		public TextView tvDetail;
//		public ImageView ivPic;
//	}

}
