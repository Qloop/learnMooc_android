package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * Created by Explorer on 2016/3/23.
 */
public class AspectInfo {
	public String more;
	public String aspectName;
	public ArrayList<Aspect> aspectInfo;

	public class Aspect {
		public String pic;
		public String courseName;
		public int courseId;
		public int learnNum;


		@Override
		public String toString() {
			return "Aspect{" +
					"pic='" + pic + '\'' +
					", courseName='" + courseName + '\'' +
					", courseId=" + courseId +
					", learnNum=" + learnNum +
					'}';
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public void setCourseId(int courseId) {
			this.courseId = courseId;
		}

		public void setLearnNum(int learnNum) {
			this.learnNum = learnNum;
		}

		public String getPic() {
			return pic;
		}

		public String getCourseName() {
			return courseName;
		}

		public int getCourseId() {
			return courseId;
		}

		public int getLearnNum() {
			return learnNum;
		}
	}

}
