package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.ArticleActivity;
import com.upc.learnmooc.domain.ArticleList;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.view.RefreshListView;

import java.util.ArrayList;

/**
 * 社区页中的文章板块
 * Created by Explorer on 2016/3/3.
 */
public class ArticleFragment extends BaseFragment {


	@ViewInject(R.id.lv_artilce)
	private RefreshListView mListView;

	private String mUrl;
	private ArrayList<ArticleList.ArticleInfo> articleData;
	private String mMoreUrl;
	private ListArticleAdapter adapter;
	private int BACK_TO_ARTICLE_LIST = 0;
	private ViewStub viewStub;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.community_article_frgment, null);

		ViewUtils.inject(this, view);

		mListView = (RefreshListView) view.findViewById(R.id.lv_artilce);
		viewStub = (ViewStub) view.findViewById(R.id.vs_net_error);


		//下拉刷新和加载更多
		mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
					System.out.println("MoreUrl is " + mMoreUrl);
				} else {
					ToastUtils.showToastShort(mActivity, "最后一页了");
					mListView.onRefreshComplete(false);// 收起加载更多的布局
				}
			}
		});

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mActivity, ArticleActivity.class);
				intent.putExtra("url", articleData.get(position).getArticleUrl());
				intent.putExtra("title", articleData.get(position).getTitle());
				startActivityForResult(intent, BACK_TO_ARTICLE_LIST);
			}
		});
		return view;
	}


	@Override
	public void initData() {
		mUrl = GlobalConstants.GET_ARTICLE_LIST;
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(5000);
		httpUtils.configTimeout(5000);

		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseDate(responseInfo.result, false);
				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				mListView.onRefreshComplete(false);
				viewStub.inflate().setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 加载下一页数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;

				parseDate(result, true);

				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				ToastUtils.showToastShort(mActivity, "加载失败");
				mListView.onRefreshComplete(false);
			}
		});
	}

	private void parseDate(String result, boolean isMore) {
		Gson gson = new Gson();
		ArticleList articleList = gson.fromJson(result, ArticleList.class);
		// 处理下一页链接
		String more = articleList.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = GlobalConstants.BASE_URL + more;
		} else {
			mMoreUrl = null;
		}

		if (!isMore) {
			articleData = articleList.articleData;
			if (articleData != null) {
				if (mListView != null) {
					adapter = new ListArticleAdapter();
					mListView.setAdapter(adapter);

				}
			}
		} else {//加载下一页  追加数据集合
			ArrayList<ArticleList.ArticleInfo> article = articleList.articleData;
			articleData.addAll(article);//将数据追加给原来的集合
			adapter.notifyDataSetChanged();//适配器刷新数据
		}
	}


	/**
	 * 使用jsoup获取网页内容
	 */
//	public void getHtmlContent(String url) {
//		ssss = url;
//		HttpUtils httpUtils = new HttpUtils();
//		httpUtils.configCurrentHttpCacheExpiry(5000);
//		httpUtils.configTimeout(5000);
//		httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				html = responseInfo.result;
//				System.out.println("html有值 " + html.charAt(10));
//
////				Document document = Jsoup.parse(html);
////				holder.tvDetail.setText(document.body().text().substring(0,80));
////				System.out.println("链接 is" + ssss + "text is " + holder.tvDetail.getText());
//			}
//
//			@Override
//			public void onFailure(HttpException e, String s) {
//				e.printStackTrace();
//			}
//		});
//	}

	/**
	 * listview数据适配器
	 */
	class ListArticleAdapter extends BaseAdapter {


		private final BitmapUtils bitmapUtils;

		public ListArticleAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.img_artilce);//默认加载图
		}

		@Override
		public int getCount() {
			return articleData.size();
		}

		@Override
		public Object getItem(int position) {
			return articleData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_article_detail_listview, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvClassify = (TextView) convertView.findViewById(R.id.tv_classify_article);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
				holder.tvDetail = (TextView) convertView.findViewById(R.id.tv_detail);
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_cover_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			//设置填充内容
			ArticleList.ArticleInfo articleInfo = articleData.get(position);
			holder.tvTitle.setText(articleInfo.getTitle());
			holder.tvClassify.setText(articleInfo.getClassifyName());
			holder.tvNum.setText(articleInfo.getNum() + "");
			bitmapUtils.display(holder.ivPic, articleInfo.getImg());

			//设置文章内容部分展示的填充
			//又后台获取html内容 解析后返回给前端
			//直接又前端解析 耗时大 显示延迟严重
			holder.tvDetail.setText(articleInfo.getAbstractInfo());

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvClassify;
		public TextView tvNum;
		public TextView tvDetail;
		public ImageView ivPic;
	}
}
