package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 章节标签数据
 * Created by Explorer on 2016/3/13.
 */
public class VideoChapter {

	public ArrayList<ChapterInfo> chapterInfo;

	/**
	 * 章节内容
	 */
	public class ChapterInfo{
		public String chapter;//章标题
		public ArrayList<SecionInfo> section;


		@Override
		public String toString() {
			return "ChapterInfo{" +
					"chapter='" + chapter + '\'' +
					", section=" + section +
					'}';
		}


		public void setChapter(String chapter) {
			this.chapter = chapter;
		}

		public void setSection(ArrayList<SecionInfo> section) {
			this.section = section;
		}

		public String getChapter() {
			return chapter;
		}

		public ArrayList<SecionInfo> getSection() {
			return section;
		}
	}

	/**
	 * 节标题
	 */
	public class SecionInfo{
		public String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public String toString() {
			return "SecionInfo{" +
					"content='" + content + '\'' +
					'}';
		}
	}
}
