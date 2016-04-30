package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 搜索结果返回数据集
 * Created by Explorer on 2016/4/30.
 */
public class SearchResult {

	public ArrayList<ListCourse> searchResult;

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
