package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**我的成绩
 * Created by Explorer on 2016/4/21.
 */
public class Score {

	public ArrayList<ScoreData> scoreList;


	public class ScoreData {
		public String courseName;
		public int scores;


		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public void setScores(int scores) {
			this.scores = scores;
		}

		public String getCourseName() {
			return courseName;
		}

		public int getScores() {
			return scores;
		}
	}
}
