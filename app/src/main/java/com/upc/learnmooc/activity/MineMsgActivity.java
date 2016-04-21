package com.upc.learnmooc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.upc.learnmooc.R;
import com.upc.learnmooc.utils.MsgCacheUtils;

import java.util.ArrayList;

/**
 * "我的消息"
 * Created by Explorer on 2016/4/19.
 */
public class MineMsgActivity extends BaseActivity {


	private ListView mListView;
	private ArrayList<String> jPushMsgList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_msg_activity);
		initViews();

	}

	@Override
	public void initViews() {
		mListView = (ListView) findViewById(R.id.lv_msg);
//		MyApplication application = (MyApplication) getApplication();
//		jPushMsgList = application.getJPushMsgList();
		loadArray();

		if (jPushMsgList != null && jPushMsgList.size() != 0) {
			mListView.setAdapter(new MsgListViewAdapter());
		}

	}

	private void loadArray() {
		int msg_size = MsgCacheUtils.getInt(MineMsgActivity.this, "Msg_Size", 0);
		for (int i = 0; i < msg_size; i++) {
			jPushMsgList.add(MsgCacheUtils.getString(MineMsgActivity.this, "Msg_" + (i + 1), null));
		}
	}

	class MsgListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return jPushMsgList.size();
		}

		@Override
		public Object getItem(int position) {
			return jPushMsgList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(MineMsgActivity.this, R.layout.item_mine_msg_listview, null);
				holder = new ViewHolder();
				holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvContent.setText(jPushMsgList.get(position));

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvSender;
		public TextView tvContent;
	}
}
