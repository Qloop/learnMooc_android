package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 文章列表数据
 * Created by Explorer on 2016/3/5.
 */
public class ArticleList {

	public ArrayList<ArticleInfo> articleData;
	public String more;


	public class ArticleInfo {
		public String title;
		public String classifyName;
		public String img;
		public String articleUrl;
		public String abstractInfo;
		public int num;

		@Override
		public String toString() {
			return "ArticleInfo{" +
					"title='" + title + '\'' +
					", classifyName='" + classifyName + '\'' +
					", img='" + img + '\'' +
					", articleUrl='" + articleUrl + '\'' +
					", abstractInfo='" + abstractInfo + '\'' +
					", num=" + num +
					'}';
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setClassifyName(String classifyName) {
			this.classifyName = classifyName;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public void setArticleUrl(String articleUrl) {
			this.articleUrl = articleUrl;
		}

		public void setAbstractInfo(String abstractInfo) {
			this.abstractInfo = abstractInfo;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getTitle() {
			return title;
		}

		public String getClassifyName() {
			return classifyName;
		}

		public String getImg() {
			return img;
		}

		public String getArticleUrl() {
			return articleUrl;
		}

		public String getAbstractInfo() {
			return abstractInfo;
		}

		public int getNum() {
			return num;
		}

		public ArticleInfo(String title, String classifyName, String img, String articleUrl, String abstractInfo, int num) {
			this.title = title;
			this.classifyName = classifyName;
			this.img = img;
			this.articleUrl = articleUrl;
			this.abstractInfo = abstractInfo;
			this.num = num;
		}
	}
}
