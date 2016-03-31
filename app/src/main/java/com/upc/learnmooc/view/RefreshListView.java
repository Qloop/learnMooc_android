package com.upc.learnmooc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.upc.learnmooc.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的ListView
 * Created by Explorer on 2016/2/22.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新


	private View mRefreshHeard;
	private int mHeardViewHeight;
	private int startY = -1;
	private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态

	//布局控件
	private ImageView ivArrow;
	private TextView tvTitle;
	private TextView tvTime;
	private ProgressBar pbProgress;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	private View mFooterView;
	private int mFooterViewHeight;

	public RefreshListView(Context context) {
		super(context);
		initHeardView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeardView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeardView();
		initFooterView();
	}

	/**
	 * 初始化头布局
	 */
	private void initHeardView() {
		mRefreshHeard = View.inflate(getContext(), R.layout.heard_refresh_listview, null);
		this.addHeaderView(mRefreshHeard);

		ivArrow = (ImageView) mRefreshHeard.findViewById(R.id.iv_refresh_arrow);
		tvTitle = (TextView) mRefreshHeard.findViewById(R.id.tv_refresh_title);
		tvTime = (TextView) mRefreshHeard.findViewById(R.id.tv_refresh_date);
		pbProgress = (ProgressBar) mRefreshHeard.findViewById(R.id.pb_progress);


		mRefreshHeard.measure(0, 0);
		mHeardViewHeight = mRefreshHeard.getMeasuredHeight();
		mRefreshHeard.setPadding(0, -mHeardViewHeight, 0, 0);

		initArrowAnim();
		tvTime.setText("最近刷新时间:" + getCurrentTime());
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(),
				R.layout.footer_loader_more_listview, null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();

		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

		this.setOnScrollListener(this);
	}
	/**
	 * 下拉箭头的动画
	 */
	private void initArrowAnim() {
		// 箭头向上动画
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		// 箭头向下动画
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (startY == -1) {
					startY = (int) ev.getRawY();
				}
				if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
					break;
				}

				int endY = (int) ev.getRawY();
				int dy = endY - startY;
				//向下滑动 && 是头部显示的时候 进行下拉
				if (dy > 0 && getFirstVisiblePosition() == 0) {
					int padding = dy - mHeardViewHeight;
					mRefreshHeard.setPadding(0, padding, 0, 0);

					if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
						mCurrrentState = STATE_RELEASE_REFRESH;
						refreshState();
					} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
						mCurrrentState = STATE_PULL_REFRESH;
						refreshState();
					}

					//在return之前调用 解决触摸事件和点击事件的冲突
					super.onTouchEvent(ev);

					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				startY = -1;

				if (mCurrrentState == STATE_RELEASE_REFRESH) {
					mCurrrentState = STATE_REFRESHING;// 正在刷新
					mRefreshHeard.setPadding(0, 0, 0, 0);// 显示
					refreshState();
				} else if (mCurrrentState == STATE_PULL_REFRESH) {
					mRefreshHeard.setPadding(0, - mHeardViewHeight, 0, 0);// 隐藏
				}
				break;

		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新下拉控件的布局
	 */
	private void refreshState() {
		switch (mCurrrentState) {
			case STATE_PULL_REFRESH:
				tvTitle.setText("下拉刷新");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);
				ivArrow.startAnimation(animDown);
				break;
			case STATE_RELEASE_REFRESH:
				tvTitle.setText("松开刷新");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);
				ivArrow.startAnimation(animUp);
				break;
			case STATE_REFRESHING:
				tvTitle.setText("正在刷新...");
				ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
				ivArrow.setVisibility(View.INVISIBLE);
				pbProgress.setVisibility(View.VISIBLE);

				if (mListener != null) {
					mListener.onRefresh();
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 收起下拉刷新的控件
	 */
	public void onRefreshComplete(boolean success) {
		if (isLoadingMore) {// 正在加载更多...
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
			isLoadingMore = false;
		} else {
			mCurrrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			mRefreshHeard.setPadding(0, -mHeardViewHeight, 0, 0);// 隐藏

			if (success) {
				tvTime.setText("最近刷新时间:" + getCurrentTime());
			}
		}
	}
	OnRefreshListener mListener;

	public void setOnRefreshListener(OnRefreshListener listener){
		mListener = listener;
	}

	private boolean isLoadingMore;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {

			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
//				System.out.println("到底了.....");
				mFooterView.setPadding(0, 0, 0, 0);// 显示
				setSelection(getCount() - 1);// 改变listview显示位置

				isLoadingMore = true;

				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	public interface OnRefreshListener{
		public void onRefresh();
		public void onLoadMore();
	}

	public String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	OnItemClickListener mItemClickListener;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);

		mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
	}
}
