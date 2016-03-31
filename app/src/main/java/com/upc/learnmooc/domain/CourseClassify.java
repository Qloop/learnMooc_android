package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 课程分类数据
 * Created by Explorer on 2016/2/28.
 */
public class CourseClassify {

	public ArrayList<ClassifyData> classifyData;

	public class ClassifyData {
		public String classifyName;
		public ArrayList<CourseInfo> courseInfo;
	}

	public class CourseInfo {
		private int courseId;
		private String courseName;
		private String thumbnail;
		private int num;

		public void setCourseId(int courseId) {
			this.courseId = courseId;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getCourseId() {

			return courseId;
		}

		public String getCourseName() {
			return courseName;
		}

		public String getThumbnail() {
			return thumbnail;
		}

		public int getNum() {
			return num;
		}

		@Override
		public String toString() {
			return "CourseInfo{" +
					"courseId=" + courseId +
					", courseName='" + courseName + '\'' +
					", thumbnail='" + thumbnail + '\'' +
					", num=" + num +
					'}';
		}
	}
}
