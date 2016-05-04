package com.upc.learnmooc.global;

/**
 * 全局常量
 * Created by Explorer on 2016/1/19.
 */
public class GlobalConstants {

	public final static String SERVER_URL = "http://120.27.47.134";
	//	public final static String REGISTER_URL = "http://10.0.3.2:8080/create";
//	public final static String LOGIN_URL = "http://10.0.3.2:8080/login";
//	public final static String GET_USER_INFO = "http://10.0.3.2:8080/user_info";
	//	public final static String SERVER_URL = "http://10.0.3.2:8050/VoicePrint";
//	public final static String SERVER_URL = "http://120.27.47.134/VoicePrint";
	public final static String DOWNLOAD_URL = SERVER_URL + "/update" + "/update.json";
	//	public final static String GET_MAIN_COURSE_URL = "http://120.27.47.134/mainCourse.json";


	//	public final static String GET_ARTICLE_LIST = "http://120.27.47.134/article/articleList.json";



	public final static String GET_EXPERT_LIST = "http://120.27.47.134/expert/expertInfo.json";


	//	public final static String GET_SELF_ARTICLE = "http://120.27.47.134/selfArticleList.json";
	public final static String BASE_URL = "http://120.27.47.134:8080";


	public final static String REGISTER_URL = BASE_URL + "/user" + "/create";  //注册

	public final static String LOGIN_URL = BASE_URL + "/user" + "/login";      //登录
	public final static String GET_USER_INFO = BASE_URL + "/user" + "/user_info";   //返回用户信息
	public final static String GET_ARTICLE_LIST = BASE_URL + "/articles";          //文章列表
	public final static String GET_MAIN_COURSE_URL = BASE_URL + "/maincourse";     //主页课程信息
	public final static String POST_COMMENT = BASE_URL + "/submit_comment";     //提交评论内容
	public final static String GET_USER_SETTINGS = BASE_URL + "/ones_comments";  //获取当前用户的评论总数
	public final static String GET_VIDEO_COMMENTINFO = BASE_URL + "/comment";   //获取某课程的所有评论
	public final static String GET_VIDEO_CHAPTERINFO = BASE_URL + "/chapterinfo"; //章节信息
	public final static String GET_VIDEO = BASE_URL + "/video";  //获取视频信息
	public final static String POST_HISTORY = BASE_URL + "/submit_history";  //提交学习历史
	public final static String GET_VIDEO_DETAILTINFO = BASE_URL + "/course_detail";    //课程详情
	public final static String POST_COURSE_COLLECTION = BASE_URL + "/course_collection";    //收藏课程
	public final static String RM_COURSE_COLLECTION = BASE_URL + "/rm_course_collection";    //取消收藏课程
	public final static String POST_ARTICLE_COLLECTION = BASE_URL + "/article_collection";    //收藏文章
	public final static String RM_ARTICLE_COLLECTION = BASE_URL + "/remove_collection";    //取消文章收藏
	public final static String GET_COLLECTED_COURSE = BASE_URL + "/has_collected_course";  //该用户收藏的课程列表
	public final static String GET_COLLECTED_ARTICLE = BASE_URL + "/has_collected_article";  //该用户收藏的文章列表

	public final static String GET_SELF_ARTICLE = BASE_URL + "/self_article"; //原创文章
	public final static String GET_SCORE_LIST = BASE_URL + "/get_score";//该用户的成绩
	public final static String POST_NOTE = BASE_URL + "/save_note";  //保存笔记
	public final static String GET_NOTE_DETAIL = BASE_URL + "/note_detail_list";    //"我的笔记"详情
	public final static String GET_NOTE_LIST = BASE_URL + "/note_list";        //"我的笔记"列表
	public final static String REMOVE_NOTE = BASE_URL + "/remove_note";        //"我的笔记"列表
	public final static String GET_ASPECT_DATA = "http://120.27.47.134/aspect/aspectData.json";   //技能树

	public final static String GET_HISTORY_COURSE = BASE_URL + "/get_history";  //历史记录

	public final static String GET_COUTSE_CLASSIFY = BASE_URL + "/classify";

	public final static String GET_SEARCH_RESULT = BASE_URL + "/search";  //搜索

	public final static String GET_RANK_LIST = BASE_URL + "/rank";  //排位

}

