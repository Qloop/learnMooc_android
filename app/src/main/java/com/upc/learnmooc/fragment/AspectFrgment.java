package com.upc.learnmooc.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.paging.gridview.PagingBaseAdapter;
import com.paging.gridview.PagingGridView;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.AspectInfo;
import com.upc.learnmooc.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Explorer on 2016/3/22.
 */
public class AspectFrgment extends BaseFragment {

	private String mUrl;

	@ViewInject(R.id.gv_course_card)
	private PagingGridView mGridView;
	private View view;
	private String mMoreUrl;
	private ArrayList<AspectInfo.Aspect> aspectList;
	private MyPagingAdaper mAdapder;

	@Override
	public View initViews() {
		if (view == null) {
			view = View.inflate(mActivity, R.layout.aspect_fragment, null);
		}
		ViewUtils.inject(this, view);

		Bundle arguments = getArguments();
		if (arguments != null) {
			mUrl = arguments.getString("url");
		}

//		mGridView.setHasMoreItems(true);
		mGridView.setPagingableListener(new PagingGridView.Pagingable() {
			@Override
			public void onLoadMoreItems() {
				if(mMoreUrl != null){
					getMoreDataFromServer();
				}else {
					ToastUtils.showToastShort(mActivity,"到底啦-。-");
				}
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
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result, false);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
			}
		});
	}

	private void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		AspectInfo aspectInfo = gson.fromJson(result, AspectInfo.class);
		String more = aspectInfo.more;
		if (TextUtils.isEmpty(more)) {
			mMoreUrl = null;
			mGridView.setHasMoreItems(false);
		} else {
			mMoreUrl = more;
			mGridView.setHasMoreItems(true);
		}

		if (!isMore) {
			//第一页加载
			aspectList = aspectInfo.aspectInfo;
			mAdapder = new MyPagingAdaper();
			mGridView.setAdapter(mAdapder);
		} else {
			ArrayList<AspectInfo.Aspect> aspectInfo1 = aspectInfo.aspectInfo;
//			mGridView.onFinishLoading(mGridView.hasMoreItems(), aspectInfo1);

			mGridView.setHasMoreItems(mGridView.hasMoreItems());
			mGridView.setIsLoading(false);
			if(aspectInfo1 != null && aspectInfo1.size() > 0) {
				aspectList.addAll(aspectInfo1);//将数据追加给原来的集合
				mAdapder.notifyDataSetChanged();//适配器刷新数据
			}
		}


	}

	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseData(result, true);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				ToastUtils.showToastShort(mActivity, "加载失败");
			}
		});
	}

	/**
	 * GridView数据适配器
	 */
	public class MyPagingAdaper extends PagingBaseAdapter<AspectInfo.Aspect> {

		private final BitmapUtils bitmapUtils;

		public MyPagingAdaper() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}


		@Override
		public int getCount() {
			return aspectList.size();
		}

		@Override
		public Object getItem(int position) {
			return aspectList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_aspect_gridview, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			//填充数据
			AspectInfo.Aspect aspect = aspectList.get(position);
			bitmapUtils.display(holder.ivPic, aspect.getPic());
			holder.tvName.setText(aspect.getCourseName());
			holder.tvNum.setText(aspect.getLearnNum() + "");

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivPic;
		public TextView tvName;
		public TextView tvNum;

	}

}
