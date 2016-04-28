package com.upc.learnmooc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不滑动的gridview 有多少内容就显示多少内容
 * Created by Explorer on 2016/4/28.
 */
public class NoScrollGridView extends GridView {
	public NoScrollGridView(Context context) {
		super(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
