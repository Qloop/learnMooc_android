package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * Created by Explorer on 2016/3/13.
 */
public class VideoDetail {

	public String courseName;
	public String detail;
	public TeacherInfo teacherInfo;


	public class TeacherInfo{
		public String name;
		public String introduction;

		@Override
		public String toString() {
			return "TeacherInfo{" +
					"name='" + name + '\'' +
					", introduction='" + introduction + '\'' +
					'}';
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}

		public String getName() {
			return name;
		}

		public String getIntroduction() {
			return introduction;
		}
	}
}
