package com.upc.learnmooc.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.upc.learnmooc.R;

/**
 * 设置页面的item
 * Created by Explorer on 2016/4/12.
 */
public class SettingsItemLayout extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

	private TextView tvTitle;
	private String mTitle;
	private int titleColor;
	private int titleSize;

	private static final int DEFAULT_TITLE_COLOR = Color.GRAY;
	private static final int DEFAULT_TITLE_SIZE = 16;

	public SettingsItemLayout(Context context) {
		super(context);
		initViews();
	}


	public SettingsItemLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SettingsItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mTitle = attrs.getAttributeValue(NAMESPACE, "leftText");

		System.out.println("Title is " +mTitle);
		titleColor = attrs.getAttributeResourceValue(NAMESPACE, "leftTextColor", DEFAULT_TITLE_COLOR);
		titleSize = attrs.getAttributeIntValue(NAMESPACE, "leftTextSize", DEFAULT_TITLE_SIZE);
		initViews();
	}

	private void initViews() {
		View.inflate(getContext(), R.layout.setting_item_layout, this);
		tvTitle = (TextView) findViewById(R.id.tv_setting_title);
		tvTitle.setText(mTitle);
		System.out.println("Title2 is " + tvTitle.getText());
		tvTitle.setTextColor(titleColor);
		tvTitle.setTextSize(titleSize);
	}

}
