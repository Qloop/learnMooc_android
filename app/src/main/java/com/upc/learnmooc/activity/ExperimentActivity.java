package com.upc.learnmooc.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.ExpertList;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 实验室
 * Created by Explorer on 2016/3/25.
 */
public class ExperimentActivity extends Activity {

	private ViewPager mViewPager;
	private ViewPager mBgViewPager;
	private ImageView ivBgPic;

	private String mUrl;
	private ArrayList<ExpertList.ExpertData> expertDatas;
	private ViewStub viewStub;

//	private Bitmap gaussBitmap;
//	private Bitmap midBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.experiment_statusbar_color);//通知栏所需颜色
		}

		setContentView(R.layout.experiment_activity);
		initViews();
		initData();
	}


	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.vp_pager);
		mBgViewPager = (ViewPager) findViewById(R.id.vp_bg_pager);

		mViewPager.setOffscreenPageLimit(3);
//		mViewPager.setPageTransformer(true,new com.upc.learnmooc.view.DepthPageTransformer());
		mViewPager.setPageTransformer(true, new com.upc.learnmooc.view.ZoomOutPageTransformer());
		mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
	}

	private void initData() {
		mUrl = GlobalConstants.GET_EXPERT_LIST;
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
//				ToastUtils.showToastShort(ExperimentActivity.this, "网络错误");
			}
		});

	}

	private void parseDate(String result) {
		Gson gson = new Gson();
		ExpertList expertList = gson.fromJson(result, ExpertList.class);
		expertDatas = expertList.expertList;
		if (expertDatas != null) {
			MyViewPagerAdapter mAdapter = new MyViewPagerAdapter();
			mViewPager.setAdapter(mAdapter);
			mViewPager.setOffscreenPageLimit(3);
			mBgViewPager.setAdapter(new BgViewPagerAdapter());
			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//				BitmapUtils bitmapUtils = new BitmapUtils(ExperimentActivity.this);

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}

				@Override
				public void onPageSelected(int position) {
//					bitmapUtils.display(ivBgPic, expertDatas.get(position).getExpertPic());
//					if (midBitmap != null) {
//						blur(midBitmap, ivBgPic, 21f);
//					}
					mBgViewPager.setCurrentItem(position, false);
				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});
//			createGaussPic(midBitmap);

		}

	}

	/**
	 *  毛玻璃效果
	 */
//	private void blur(Bitmap bkg, ImageView view, float radius) {
//		Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//		Matrix matrix = new Matrix();
//		matrix.postScale(6.0f,16.0f);
//		Bitmap bitmap = Bitmap.createBitmap(bkg, 0, 0,bkg.getWidth(), bkg.getHeight(),matrix,true);
//		view.setImageBitmap(bitmap);
//		Canvas canvas = new Canvas(overlay);
//		canvas.drawBitmap(bitmap, -view.getLeft(), -view.getTop(), null);
//		RenderScript rs = RenderScript.create(this);
//		Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
//		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
//		blur.setInput(overlayAlloc);
//		blur.setRadius(radius);
//		blur.forEach(overlayAlloc);
//		overlayAlloc.copyTo(overlay);
//		view.setImageBitmap(overlay);

//		rs.destroy();
//	}


//	public void createGaussPic(final Bitmap bitmap) {
//		BitMapThread thread = new BitMapThread(bitmap);
//		thread.run();
//		if (gaussBitmap != null) {
//			ivBgPic.setImageBitmap(gaussBitmap);
//		}
//	}


	/**
	 * Viewpager数据适配器
	 */
	class MyViewPagerAdapter extends PagerAdapter {
		private LinkedList<View> mViewCache = null;
		private final BitmapUtils bitmapUtils;
		final Message msg = Message.obtain();
		private ViewHolder holder;

		public MyViewPagerAdapter() {
			mViewCache = new LinkedList<>();
			bitmapUtils = new BitmapUtils(ExperimentActivity.this);
			bitmapUtils.configDefaultLoadingImage(R.drawable.exper_default);
		}

		@Override
		public int getCount() {
			return expertDatas.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			holder = null;
			View convertView = null;
			if (mViewCache.size() == 0) {
				convertView = View.inflate(ExperimentActivity.this, R.layout.item_exper_viewpager, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_title_pic);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_exper_name);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
				holder.vLine = convertView.findViewById(R.id.v_line);

				convertView.setTag(holder);
			} else {
				convertView = mViewCache.removeFirst();
				holder = (ViewHolder) convertView.getTag();
			}

			final ExpertList.ExpertData expertData = expertDatas.get(position);

			bitmapUtils.display(holder.ivPic, expertData.getExpertPic());
//			bitmapUtils.display(holder.ivPic, expertData.getExpertPic(), new BitmapLoadCallBack<ImageView>() {
//				@Override
//				public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
//					if (bitmap != null) {
//						midBitmap = bitmap;
////						ivBgPic.setImageBitmap(midBitmap);
////						bitmap.recycle();
//					}
//				}
//
//				@Override
//				public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
//
//				}
//			});
			holder.tvName.setText(expertData.getExpertName());
			holder.tvNum.setText(expertData.getNum() + "");

			/* 动态设置view 横线 让它和上方的文字等宽*/
			holder.tvName.measure(0, 0);
			int measuredWidth = holder.tvName.getMeasuredWidth();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(measuredWidth, 1);
			params.addRule(RelativeLayout.BELOW, R.id.tv_exper_name);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//			holder.vLine.setPadding(0, DensityUtils.dp2px(ExperimentActivity.this,1000), 0, 0);
			holder.vLine.setLayoutParams(params);


			container.addView(convertView);
			return convertView;
		}


		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			mViewCache.add((View) object);
		}

		//View复用
		public final class ViewHolder {
			public TextView tvName;
			public TextView tvNum;
			public ImageView ivPic;
			public View vLine;
		}
	}


	class BgViewPagerAdapter extends PagerAdapter {

		private LinkedList<View> mViewCache = null;
		private final BitmapUtils bitmapUtils;

		public BgViewPagerAdapter() {
			mViewCache = new LinkedList<>();
			bitmapUtils = new BitmapUtils(ExperimentActivity.this);
			bitmapUtils.configDefaultLoadingImage(R.drawable.exper_default);
		}

		@Override
		public int getCount() {
			return expertDatas.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ViewHolder holder = null;
			View convertView = null;
			if (mViewCache.size() == 0) {
				convertView = View.inflate(ExperimentActivity.this, R.layout.item_expert_bg_viewpager, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_bg);

				convertView.setTag(holder);
			} else {
				convertView = mViewCache.removeFirst();
				holder = (ViewHolder) convertView.getTag();
			}

			ExpertList.ExpertData expertData = expertDatas.get(position);

			bitmapUtils.display(holder.ivPic, expertData.getExpertPic());
			container.addView(convertView);
			return convertView;
		}


		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			mViewCache.add((View) object);
		}

		//View复用
		public final class ViewHolder {
			public ImageView ivPic;
		}
	}

	/**
	 * 返回前一页
	 */
	public void GoBack(View view) {
		finish();
	}
}
