package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.upc.learnmooc.MyApplication;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.CollectedCourseActivity;
import com.upc.learnmooc.activity.CourseHistoryActivity;
import com.upc.learnmooc.activity.MineMsgActivity;
import com.upc.learnmooc.activity.MineNoteActivity;
import com.upc.learnmooc.activity.SelfArticleActivity;
import com.upc.learnmooc.activity.SettingsActivity;

/**
 * 个人主页
 * Created by Explorer on 2016/2/10.
 */
public class MineFragment extends BaseFragment {

	private static final int[] imgList = new int[]{
			R.drawable.mine_fouse_course, R.drawable.mine_plan,
			R.drawable.mine_infomation, R.drawable.mine_article,
			R.drawable.mine_note, R.drawable.mine_history
	};

	private static final String[] imgNameList = new String[]{
			"关注的课程", "我的计划", "我的消息",
			"收藏的文章", "我的笔记", "最近学习"
	};
	//	private List<Map<String, Object>> listData;
	private static final int FOURSED_COURSE = 0;
	private static final int MY_PLAN = 1;
	private static final int MY_MESSAGES = 2;
	private static final int COLLECTED_ARTICLE = 3;
	private static final int MY_NOTE = 4;
	private static final int STUDY_HISTORY = 5;


	@ViewInject(R.id.gv_menu)
	private GridView mGridView;
	@ViewInject(R.id.iv_setting)
	private ImageView ivSettings;
	private Shimmer shimmer;
	private ViewHolder holder;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.mine_fragment, null);
		//注入view和事件
		ViewUtils.inject(this, view);
//		listData = new ArrayList<>();
//		SimpleAdapter gridViewAdapter = new SimpleAdapter(mActivity, getData(), R.layout.item_mine_gridview,
//				new String[]{"iv_pic", "tv_name"}, new int[]{R.id.iv_pic, R.id.tv_name});
		mGridView.setAdapter(new MyGridViewAdapter());
		shimmer = new Shimmer();
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case FOURSED_COURSE:
						startActivity(new Intent(mActivity, CollectedCourseActivity.class));
						break;
					case MY_PLAN:
						break;
					case MY_MESSAGES:
						//点击查看后取消红点提示
						MyApplication application = (MyApplication) mActivity.getApplication();
						application.getJPushMsgList().clear();
						mActivity.findViewById(R.id.notify_text).setVisibility(View.INVISIBLE);

						holder.tvName.setTextColor(getResources().getColor(R.color.light_black_color));
						shimmer.cancel();//停止闪烁
						startActivity(new Intent(mActivity, MineMsgActivity.class));
						break;
					case COLLECTED_ARTICLE:
						startActivity(new Intent(mActivity, SelfArticleActivity.class));
						break;
					case MY_NOTE:
						startActivity(new Intent(mActivity, MineNoteActivity.class));
						break;
					case STUDY_HISTORY:
						startActivity(new Intent(mActivity, CourseHistoryActivity.class));
						break;
					default:
						break;
				}
			}
		});
		ivSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mActivity, SettingsActivity.class));
			}
		});

		return view;
	}

//	@TargetApi(19)
//	private void setTranslucentStatus(boolean on) {
//		Window win = mActivity.getWindow();
//		WindowManager.LayoutParams winParams = win.getAttributes();
//		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//		if (on) {
//			winParams.flags |= bits;
//		} else {
//			winParams.flags &= ~bits;
//		}
//		win.setAttributes(winParams);
//	}

	/**
	 * 封装gridview数据源
	 */
//	private List<Map<String, Object>> getData() {
//		for (int i = 0; i < imgList.length; i++) {
//			Map<String, Object> list = new HashMap<String, Object>();
//			list.put("iv_pic", imgList[i]);
//			list.put("tv_name", imgNameList[i]);
//			listData.add(list);
//		}
//		return listData;
//	}

	class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return imgList.length;
		}

		@Override
		public Object getItem(int position) {
			return imgList[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_mine_gridview, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvName = (ShimmerTextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.ivIcon.setImageResource(imgList[position]);
			holder.tvName.setText(imgNameList[position]);

			if (holder.tvName.getText().equals("我的消息")) {
				int visibility = mActivity.findViewById(R.id.notify_text).getVisibility();
				if (visibility == View.VISIBLE) {
					holder.tvName.setTextColor(getResources().getColor(R.color.status_color));
					shimmer.start(holder.tvName);
				}
			}
			return convertView;

		}
	}

	static class ViewHolder {
		public ImageView ivIcon;
		public ShimmerTextView tvName;
	}
}
