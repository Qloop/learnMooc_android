package com.upc.learnmooc.domain;

/**
 * 课程视频的基本信息数据
 * Created by Explorer on 2016/3/12.
 */
public class CourseContent {

	public int courseId;
	public String courseName;
	public String videoUrl;


	@Override
	public String toString() {
		return "CourseContent{" +
				"courseId=" + courseId +
				", courseName='" + courseName + '\'' +
				", videoUrl=" + videoUrl +
				'}';
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
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
