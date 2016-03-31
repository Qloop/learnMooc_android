package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.ExperimentActivity;
import com.upc.learnmooc.activity.SkillTreeActivity;
import com.upc.learnmooc.activity.VideoActivity;
import com.upc.learnmooc.domain.MainCourse;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.CacheUtils;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.view.RefreshListView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 课程主页
 * Created by Explorer on 2016/2/9.
 */
public class CourseFragment extends BaseFragment {

	@ViewInject(R.id.vp_top_course)
	private ViewPager mViewPager;
	@ViewInject(R.id.top_course_indicator)
	private CirclePageIndicator mIndictor;
	@ViewInject(R.id.lv_list_course)
	private RefreshListView mListView;

	@ViewInject(R.id.skill_tree)
	private LinearLayout mLayout;
	private LinearLayout mExpertLayout;

	private MainCourse mainCourseInfo;//Course首页的数据对象
	private ArrayList<MainCourse.TopCourse> topCourseList;//头部轮播的数据对象
	private ArrayList<MainCourse.ListCourse> listCourseList;//课程列表的数据对象

	private String mMoreUrl;
	private String mUrl = GlobalConstants.GET_MAIN_COURSE_URL;

	//当前轮播页
	private int currentItem = 0;
	private ListCourseAdapter listCourseAdapter;
	private Handler mHandler;


	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.course_fragment, null);
		View heardView = View.inflate(mActivity, R.layout.heard_course_listview, null);

		mLayout = (LinearLayout) heardView.findViewById(R.id.skill_tree);
		mExpertLayout = (LinearLayout) heardView.findViewById(R.id.laboratory);
		mLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mActivity, SkillTreeActivity.class));
			}
		});

		mExpertLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mActivity, ExperimentActivity.class));
			}
		});

		//注入view和事件
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, heardView);

		//将头部的轮播等部分 作为listview的头布局
		mListView.addHeaderView(heardView);
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
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(mUrl, mActivity);

		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
		getDataFromServer();
	}

	/**
	 * ViewPager的监听器
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
				case 1:// 手势滑动，空闲中
					isAutoPlay = false;
					break;
				case 2:// 界面切换中
					isAutoPlay = true;
					break;
				case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
					if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
						mViewPager.setCurrentItem(0, false);
					}
					// 当前为第一张，此时从左向右滑，则切换到最后一张
					else if (mViewPager.getCurrentItem() == 0 && !isAutoPlay) {
						mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() - 1, false);
					}
					currentItem = mViewPager.getCurrentItem();
					break;
				default:
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int pos) {

		}

	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		//
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.GET_MAIN_COURSE_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result, false);
				mListView.onRefreshComplete(true);

				// 设置缓存
				CacheUtils.setCache(mUrl, responseInfo.result, mActivity);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				ToastUtils.showToastShort(mActivity, "请求数据失败 请检查网络");
				mViewPager.setAdapter(new TopCourseAdapter());//获取数据失败的时候设置适配器(进行读取缓存或默认加载处理)
				mIndictor.setViewPager(mViewPager);//给指示器绑定viewpager
				mIndictor.setSnap(true);//支持快照
				mListView.onRefreshComplete(false);
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

				parseData(result, true);

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

	/**
	 * 解析返回json数据
	 */
	private void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		mainCourseInfo = gson.fromJson(result, MainCourse.class);
		// 处理下一页链接
		String more = mainCourseInfo.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = more;
		} else {
			mMoreUrl = null;
		}


		if (!isMore) {
			topCourseList = mainCourseInfo.topCourse;
			listCourseList = mainCourseInfo.listCourse;

			if (topCourseList != null) {
				TopCourseAdapter topCourseAdapter = new TopCourseAdapter();
				mViewPager.setAdapter(topCourseAdapter);//成功获取数据后数据适配器
				mIndictor.setViewPager(mViewPager);//给指示器绑定viewpager
				mIndictor.setSnap(true);//支持快照
				mIndictor.setOnPageChangeListener(new MyPageChangeListener());
			}


			if (listCourseList != null) {
				listCourseAdapter = new ListCourseAdapter();
				mListView.setAdapter(listCourseAdapter);

				mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent();
						intent.setClass(mActivity, VideoActivity.class);
						intent.putExtra("id", listCourseList.get(position).getCourseId());
						startActivity(intent);
					}
				});
			}

			// 自动轮播条显示
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						currentItem = mViewPager.getCurrentItem();

						if (currentItem < topCourseList.size() - 1) {
							currentItem++;
							mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
						} else {
							currentItem = 0;
							mViewPager.setCurrentItem(currentItem, false);// 切换到下一个页面
						}

						mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息, 形成循环
					}

					;
				};

				mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
			}
		} else {// 如果是加载下一页,需要将数据追加给原来的集合
			ArrayList<MainCourse.ListCourse> course = mainCourseInfo.listCourse;
			listCourseList.addAll(course);//将数据追加给原来的集合
			listCourseAdapter.notifyDataSetChanged();//适配器刷新数据
		}

	}

	/**
	 * 头部课程轮播适配器
	 */
	class TopCourseAdapter extends PagerAdapter {

		private BitmapUtils bitmapUtils;
		private BitmapDisplayConfig config;


		public TopCourseAdapter() {
			config = new BitmapDisplayConfig();
			config.setLoadingDrawable(getResources().getDrawable(R.drawable.top_course_default));
			bitmapUtils = new BitmapUtils(mActivity);
			//设置默认记载过程中的默认显示图
			bitmapUtils.configDefaultLoadingImage(R.drawable.top_course_default);
		}

		@Override
		public int getCount() {
			if (topCourseList == null) {
				return 1;
			}
			return topCourseList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			if (topCourseList != null) {
				bitmapUtils.display(imageView, topCourseList.get(position).topCourseImgUrl, config);
			} else {
				//获取网络数据失败时 显示默认图片
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.top_course_default));
			}

			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * 课程列表listview适配器
	 */
	class ListCourseAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public ListCourseAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);//设置默认显示的图片
		}

		@Override
		public int getCount() {
			return listCourseList.size();
		}

		@Override
		public MainCourse.ListCourse getItem(int position) {
			return listCourseList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_course_list, null);
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
