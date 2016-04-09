package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 笔记数据
 * Created by Explorer on 2016/4/7.
 */
public class NoteInfo {

	public ArrayList<NoteData> listCourse;

	public class NoteData{
		public int courseId;
		public String courseName;
		public int noteNum;
		public String thumbnailUrl;


		@Override
		public String toString() {
			return "NoteData{" +
					"courseId=" + courseId +
					", courseName='" + courseName + '\'' +
					", noteNum=" + noteNum +
					", thumbnailUrl='" + thumbnailUrl + '\'' +
					'}';
		}

		public void setCourseId(int courseId) {
			this.courseId = courseId;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public void setNoteNum(int noteNum) {
			this.noteNum = noteNum;
		}

		public void setThumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
		}

		public int getCourseId() {
			return courseId;
		}

		public String getCourseName() {
			return courseName;
		}

		public int getNoteNum() {
			return noteNum;
		}

		public String getThumbnailUrl() {
			return thumbnailUrl;
		}
	}
}
