package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.CourseHistoryActivity;
import com.upc.learnmooc.activity.SelfArticleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<Map<String, Object>> listData;
	private static final int FOURSED_COURSE = 0;
	private static final int MY_PLAN = 1;
	private static final int MY_MESSAGES = 2;
	private static final int COLLECTED_ARTICLE = 3;
	private static final int MY_NOTE = 4;
	private static final int STUDY_HISTORY = 5;


	@ViewInject(R.id.gv_menu)
	private GridView mGridView;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.mine_fragment, null);
		//注入view和事件
		ViewUtils.inject(this, view);
		listData = new ArrayList<>();
		SimpleAdapter gridViewAdapter = new SimpleAdapter(mActivity, getData(), R.layout.item_mine_gridview,
				new String[]{"iv_pic","tv_name"}, new int[]{R.id.iv_pic, R.id.tv_name});
		mGridView.setAdapter(gridViewAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
					case FOURSED_COURSE:
						break;
					case MY_PLAN:
						break;
					case MY_MESSAGES:
						break;
					case COLLECTED_ARTICLE:
						startActivity(new Intent(mActivity, SelfArticleActivity.class));
						break;
					case MY_NOTE:
						break;
					case STUDY_HISTORY:
						startActivity(new Intent(mActivity, CourseHistoryActivity.class));
						break;
					default:
						break;
				}
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
	private List<Map<String, Object>> getData() {
		for (int i = 0; i < imgList.length; i++) {
			Map<String, Object> list = new HashMap<String, Object>();
			list.put("iv_pic", imgList[i]);
			list.put("tv_name", imgNameList[i]);
			listData.add(list);
		}
		return listData;
	}
}
