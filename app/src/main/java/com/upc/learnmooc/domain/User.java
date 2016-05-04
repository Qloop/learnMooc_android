package com.upc.learnmooc.domain;

/**
 * 用户返回信息
 * Created by Explorer on 2016/2/1.
 */
public class User {
	public long id;
	public String result;//注册的返回结果 eg:"注册成功"
	public String password;
	public String mail;
	public String nickname;//昵称
	public String avatar;//头像url
	public String name;//真实姓名
	public int roleType;//用户身份 0：学生 1:老师
	public String no;//学号
	public String major;//专业
	private String academy;//学院
	public int status;//账户状态 1:正常 3 未激活
//	public Date createDate;

	public int learnTime;
	public int exp;
	public String commentNum;  //改为String方便解析数据
	public long collectCourse;
	public long collectArticle;


	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setCollectCourse(long collectCourse) {
		this.collectCourse = collectCourse;
	}

	public void setCollectArticle(long collectArticle) {
		this.collectArticle = collectArticle;
	}

	public long getCollectCourse() {
		return collectCourse;
	}

	public long getCollectArticle() {
		return collectArticle;
	}

	public void setLearnTime(int learnTime) {
		this.learnTime = learnTime;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}


	public int getLearnTime() {
		return learnTime;
	}

	public int getExp() {
		return exp;
	}


	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy;
	}

	public String getName() {
		return name;
	}

	public String getNo() {
		return no;
	}

	public String getMajor() {
		return major;
	}

	public int getStatus() {
		return status;
	}

//	public Date getCreateDate() {
//		return createDate;
//	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public void setStatus(int status) {
		this.status = status;
	}

//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}

	public String getResult() {
		return result;
	}

	public String getPassword() {
		return password;
	}

	public String getMail() {
		return mail;
	}

	public String getNickname() {
		return nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
}
