package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 学习课程历史记录
 * Created by Explorer on 2016/3/1.
 */
public class CourseHistory {

	public ArrayList<HistoryData> historyData;

	public class HistoryData {
		public ArrayList<CourseData> historyCourse;

		public String time;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	public class CourseData {
		public int courseId;
		public String courseName;

		public int getCourseId() {
			return courseId;
		}

		public String getCourseName() {
			return courseName;
		}

		public void setCourseId(int courseId) {
			this.courseId = courseId;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
	}
}
