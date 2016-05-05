package com.upc.learnmooc.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.utils.ToastUtils;

/**
 * 意见反馈
 * Created by Explorer on 2016/4/13.
 */
public class SuggestionFragment extends BaseFragment {

	@ViewInject(R.id.et_suggestion)
	private EditText etSuggestionContent;
	@ViewInject(R.id.tv_word_count)
	private TextView tvWordNumber;
	@ViewInject(R.id.btn_submit)
	private Button btnSubmit;

	private static final int num = 1000;//允许输入的字数最大值

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.suggestion_fragment, null);
		ViewUtils.inject(this, view);

		etSuggestionContent.addTextChangedListener(new TextWatcher() {
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
				selectionStart = etSuggestionContent.getSelectionStart();
				selectionEnd = etSuggestionContent.getSelectionEnd();
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					etSuggestionContent.setText(s);
					etSuggestionContent.setSelection(tempSelection);//设置光标在最后
				}
			}
		});

		//提交反馈
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtils.showToastShort(mActivity,"提交成功");
				mActivity.finish();
			}
		});
		return view;
	}
}
