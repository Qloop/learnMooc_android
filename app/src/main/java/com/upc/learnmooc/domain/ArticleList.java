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
		public String classify;
		public String img;
		public String url;
		public String abstractInfo;
		public int num;


		@Override
		public String toString() {
			return "ArticleInfo{" +
					"title='" + title + '\'' +
					", classify='" + classify + '\'' +
					", img='" + img + '\'' +
					", url='" + url + '\'' +
					", num=" + num +
					'}';
		}

		public String getAbstractInfo() {
			return abstractInfo;
		}

		public void setAbstractInfo(String abstractInfo) {
			this.abstractInfo = abstractInfo;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setClassify(String classify) {
			this.classify = classify;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getTitle() {
			return title;
		}

		public String getClassify() {
			return classify;
		}

		public String getImg() {
			return img;
		}

		public String getUrl() {
			return url;
		}

		public int getNum() {
			return num;
		}
	}

}
