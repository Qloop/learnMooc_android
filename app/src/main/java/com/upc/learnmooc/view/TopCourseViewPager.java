package com.upc.learnmooc.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头部课程轮播viewpager
 * Created by Explorer on 2016/2/13.
 */
public class TopCourseViewPager extends ViewPager {
	public TopCourseViewPager(Context context) {
		super(context);
	}

	public TopCourseViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	/**
	 * 父控件不再拦截本控件的事件（解决滑动冲突）
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}
}
