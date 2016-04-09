package com.upc.learnmooc.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.upc.learnmooc.R;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_activity);
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
				temp  = s;
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
