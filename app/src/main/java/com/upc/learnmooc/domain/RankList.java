package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 排位信息
 * Created by Explorer on 2016/3/8.
 */
public class RankList {

	public ArrayList<RankInfo> rankData;

	public class RankInfo {
		public int id;
		public String nickname;
		public String avatar;
		public int score;


		@Override
		public String toString() {
			return "RankInfo{" +
					"id=" + id +
					", nickname='" + nickname + '\'' +
					", avatar='" + avatar + '\'' +
					", score=" + score +
					'}';
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public int getId() {
			return id;
		}

		public String getNickname() {
			return nickname;
		}

		public String getAvatar() {
			return avatar;
		}

		public int getScore() {
			return score;
		}
	}
}
