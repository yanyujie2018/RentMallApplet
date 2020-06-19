package com.qd.bean;

public class commentMsg {
	private int commentId;
	private int commentGoodId;
	private String content;
	private int degree;
	private String commentUserId;
	private int anonymous;
	private String date;
	private String userName;
	private int idState;
	private String avatarUrl;
	private String nickName;
	private int isRead;
	private int replyTotal;  //所有回复数
	private int replyNoRead;  //未阅读的回复数
	private replyMsg lastReplyContent;  //如果有回复，那么这是最后一条回复信息
	private int score;
	
	
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public replyMsg getLastReplyContent() {
		return lastReplyContent;
	}
	public void setLastReplyContent(replyMsg lastReplyContent) {
		this.lastReplyContent = lastReplyContent;
	}
	public int getReplyTotal() {
		return replyTotal;
	}
	public void setReplyTotal(int replyTotal) {
		this.replyTotal = replyTotal;
	}
	public int getReplyNoRead() {
		return replyNoRead;
	}
	public void setReplyNoRead(int replyNoRead) {
		this.replyNoRead = replyNoRead;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getIdState() {
		return idState;
	}
	public void setIdState(int idState) {
		this.idState = idState;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getCommentGoodId() {
		return commentGoodId;
	}
	public void setCommentGoodId(int commentGoodId) {
		this.commentGoodId = commentGoodId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public String getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}
	public int getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(int anonymous) {
		this.anonymous = anonymous;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
