package com.upc.learnmooc.domain;

import java.util.ArrayList;

/**
 * 技能树
 * Created by Explorer on 2016/3/22.
 */
public class AspectData {

	public ArrayList<AspectInfo> aspectData;


	public class AspectInfo {
		public String aspectName;
		public String url;


		@Override
		public String toString() {
			return "AspectInfo{" +
					"aspectName='" + aspectName + '\'' +
					", url='" + url + '\'' +
					'}';
		}

		public void setAspectName(String aspectName) {
			this.aspectName = aspectName;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getAspectName() {
			return aspectName;
		}

		public String getUrl() {
			return url;
		}
	}


}
