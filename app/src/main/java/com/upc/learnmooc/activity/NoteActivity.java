package com.upc.learnmooc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.utils.UserInfoCacheUtils;
import com.upc.learnmooc.view.TopBarView;

/**
 * 记笔记
 * Created by Explorer on 2016/4/6.
 */
public class NoteActivity extends BaseActivity {

	private EditText etNoteContent;
	private TextView tvWordNumber;
	private TopBarView topBar;
	private static final int num = 1000;//允许输入的字数最大值
	private String courseName;
	private String mUrl = GlobalConstants.POST_NOTE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_activity);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		courseName = extras.getString("courseName");
		initViews();
	}

	@Override
	public void initViews() {
		etNoteContent = (EditText) findViewById(R.id.et_note);
		tvWordNumber = (TextView) findViewById(R.id.tv_word_number);
		topBar = (TopBarView) findViewById(R.id.topbar);
		topBar.setOnSubTitleListener(new TopBarView.OnSubTitleListener() {
			@Override
			public void onClickSubTitle() {
				//保存笔记
				HttpUtils httpUtils = new HttpUtils();
				httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
				httpUtils.configTimeout(1000 * 5);

				//GET参数为用户id
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("userId", UserInfoCacheUtils.getLong(NoteActivity.this, "id", 0) + "");
				params.addQueryStringParameter("noteContent", etNoteContent.getText().toString());
				params.addQueryStringParameter("courseName", courseName);

				httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.d("call_back", responseInfo.result);
						ToastUtils.showToastShort(NoteActivity.this,"发布成功");
						finish();
					}

					@Override
					public void onFailure(HttpException e, String s) {
						e.printStackTrace();
					}
				});
			}
		});
		etNoteContent.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = num - s.length();
				tvWordNumber.setText("" + number);
				selectionStart = etNoteContent.getSelectionStart();
				selectionEnd = etNoteContent.getSelectionEnd();
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					etNoteContent.setText(s);
					etNoteContent.setSelection(tempSelection);//设置光标在最后
				}
			}
		});
	}
}
