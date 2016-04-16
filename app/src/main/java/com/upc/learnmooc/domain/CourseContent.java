package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 课程视频的基本信息数据
 * Created by Explorer on 2016/3/12.
 */
public class CourseContent {

	public int courseId;
	public String courseName;
	public ArrayList<VideoContent> videoUrl;

	public class VideoContent {
		public String url;

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}
	}


	@Override
	public String toString() {
		return "CourseContent{" +
				"courseId=" + courseId +
				", courseName='" + courseName + '\'' +
				", videoUrl=" + videoUrl +
				'}';
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public int getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return courseName;
	}

}
