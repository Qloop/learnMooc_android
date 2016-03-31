package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * Created by Explorer on 2016/3/13.
 */
public class VideoComment {

	public ArrayList<CommentInfo> comment;

	/**
	 * 评论
	 */
	public class CommentInfo {
		public String avatarUrl;
		public String nickname;
		public String commentContent;

		@Override
		public String toString() {
			return "CommentInfo{" +
					"avatarUrl='" + avatarUrl + '\'' +
					", nickname='" + nickname + '\'' +
					", commentContent='" + commentContent + '\'' +
					'}';
		}

		public void setAvatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public void setCommentContent(String commentContent) {
			this.commentContent = commentContent;
		}

		public String getAvatarUrl() {
			return avatarUrl;
		}

		public String getNickname() {
			return nickname;
		}

		public String getCommentContent() {
			return commentContent;
		}
	}
}
