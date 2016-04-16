package com.upc.learnmooc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.activity.ChangeAvatarActivity;
import com.upc.learnmooc.utils.UserInfoCacheUtils;
import com.upc.learnmooc.view.CircleImageView;

/**
 * 个人信息  头像 昵称等
 * Created by Explorer on 2016/4/14.
 */
public class UserFragment extends BaseFragment {

	@ViewInject(R.id.rl_avatar)
	private RelativeLayout rlLayout;
	@ViewInject(R.id.user_avatar)
	private CircleImageView civAvatar;
	@ViewInject(R.id.tv_nickname_setting)
	private TextView tvNickname;
	@ViewInject(R.id.tv_mail)
	private TextView tvMail;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.user_fragment, null);
		ViewUtils.inject(this, view);
		Bundle arguments = getArguments();
		String avatarUrl = arguments.getString("avatarUrl");
		String nickname = arguments.getString("nickname");
		ShowAvatar(avatarUrl);
		SetNickname(nickname);

		//更换头像
		rlLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mActivity, ChangeAvatarActivity.class));
			}
		});
		return view;

	}

	private void SetNickname(String nickname) {
		if (nickname != null) {
			tvNickname.setText(nickname);
		} else {
			tvNickname.setText("未登录");
		}
	}

	/**
	 * 显示头像
	 */
	private void ShowAvatar(String avatarUrl) {
		if (avatarUrl != null) {
			BitmapUtils bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.display(civAvatar, avatarUrl);
		}
	}

	@Override
	public void initData() {
		String mail = UserInfoCacheUtils.getString(mActivity, "mail", null);
		ShowMail(mail);
	}

	/**
	 * 显示邮箱
	 */
	private void ShowMail(String mail) {
		if (mail != null) {
			tvMail.setText(mail);
		} else {
			tvMail.setText("未登录");
		}


	}
}
