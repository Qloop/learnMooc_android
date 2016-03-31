package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 实验列表数据
 * Created by Explorer on 2016/3/26.
 */
public class ExpertList {

	public ArrayList<ExpertData> expertList;

	public class ExpertData {
		public int expertId;
		public String expertName;
		public String expertPic;
		public int num;

		@Override
		public String toString() {
			return "ExpertData{" +
					"expertId=" + expertId +
					", expertName='" + expertName + '\'' +
					", expertPic='" + expertPic + '\'' +
					", num=" + num +
					'}';
		}

		public void setExpertId(int expertId) {
			this.expertId = expertId;
		}

		public void setExpertName(String expertName) {
			this.expertName = expertName;
		}

		public void setExpertPic(String expertPic) {
			this.expertPic = expertPic;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getExpertId() {
			return expertId;
		}

		public String getExpertName() {
			return expertName;
		}

		public String getExpertPic() {
			return expertPic;
		}

		public int getNum() {
			return num;
		}
	}

}
