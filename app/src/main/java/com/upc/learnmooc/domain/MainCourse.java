package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 课程首页 （topCourse/listCourse）
 * Created by Explorer on 2016/2/14.
 */
public class MainCourse {
	public ArrayList<TopCourse> topCourse;
	public ArrayList<ListCourse> listCourse;
	public String more;//分页加载时  下一页的url

	/**
	 * 头部轮播课程数据
	 */
	public class TopCourse {
		public int courseId;
		public String pubdate;
		public String topCourseImgUrl;

		public void setCourseId(int courseId) {
			this.courseId = courseId;
		}

		public void setPubdate(String pubdate) {
			this.pubdate = pubdate;
		}

		public void setTopCourseImgUrl(String topCourseImgUrl) {
			this.topCourseImgUrl = topCourseImgUrl;
		}

		public int getCourseId() {
			return courseId;
		}

		public String getPubdate() {
			return pubdate;
		}

		public String getTopCourseImgUrl() {
			return topCourseImgUrl;
		}

		@Override
		public String toString() {
			return "TopCourse{" +
					"courseId=" + courseId +
					", pubdate='" + pubdate + '\'' +
					", topCourseImgUrl='" + topCourseImgUrl + '\'' +
					'}';
		}
	}

	/**
	 * 课程列表数据
	 */
	public class ListCourse {
		public int courseId;
		public String courseName;
		public int num;//选修人数
		public String pubdate;
		public String thumbnailUrl;


		public int getCourseId() {
			return courseId;
		}

		public String getCourseName() {
			return courseName;
		}

		public int getNum() {
			return num;
		}

		public String getPubdate() {
			return pubdate;
		}

		public String getThumbnailUrl() {
			return thumbnailUrl;
		}

		public void setCourseId(int courseId) {
			this.courseId = courseId;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public void setPubdate(String pubdate) {
			this.pubdate = pubdate;
		}

		public void setThumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
		}

		@Override
		public String toString() {
			return "ListCourse{" +
					"courseId=" + courseId +
					", courseName='" + courseName + '\'' +
					", num=" + num +
					", pubdate='" + pubdate + '\'' +
					", thumbnailUrl='" + thumbnailUrl + '\'' +
					'}';
		}
	}
}
