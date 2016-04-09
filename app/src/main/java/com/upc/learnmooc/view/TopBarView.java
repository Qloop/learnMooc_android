package com.upc.learnmooc.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upc.learnmooc.R;

/**
 * 头部bar
 * Created by Explorer on 2016/4/5.
 */
public class TopBarView extends LinearLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
	private ImageView ivBack;
	private TextView tvTitle;
	private TextView tvSubTitle;
	private String mTitle;
	private String mSubTitle;
	private int topIcon;


	public TopBarView(Context context) {
		super(context);
		initViews();
	}

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAMESPACE, "topTitle");// 根据属性名称,获取属性的值
		mSubTitle = attrs.getAttributeValue(NAMESPACE, "topSubtitle");
		topIcon = attrs.getAttributeResourceValue(NAMESPACE, "topIcon", R.drawable.back);
		initViews();
	}

	public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initViews();
	}

	private void initViews() {
		// 将自定义好的布局文件设置给当前的TopBarView
		View.inflate(getContext(), R.layout.top_bar, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvSubTitle = (TextView) findViewById(R.id.tv_subtitle);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setImageResource(topIcon);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity)getContext()).finish();
			}
		});

		tvSubTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onClickSubTitle();
			}
		});

		setTitle(mTitle);
		setSubTitle(mSubTitle);
	}

	OnSubTitleListener mListener;
	public void setOnSubTitleListener(OnSubTitleListener subTitleListener){
		mListener = subTitleListener;
	}

	public interface OnSubTitleListener{
		public void onClickSubTitle();
	}

	private void setSubTitle(String subTitle) {
		if (subTitle != null){
			tvSubTitle.setText(subTitle);
		}
	}

	private void setTitle(String title) {
		if(title != null){
			tvTitle.setText(title);
		}
	}

}
