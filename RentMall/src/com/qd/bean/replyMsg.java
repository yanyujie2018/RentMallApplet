package com.qd.bean;

public class replyMsg {
	private int Id;
	private String replyId;
	private String replyToId;
	private int sequence;
	private int isRead;
	private int replyComment;
	private String avatarUrl_from;  //�ظ������ߵ�ͷ��
	private String userName_from;  //�ظ������������
	private String userName_to;    //�ظ����������
	private String nickName_from;  //�ظ������ߵ��ǳ�
	private String nickName_to;	   //�ظ�������ǳ�
	private String userId_from;  //�ظ������ߵ�userID
	private String userId_to;  //�ظ������userId
	private String replyDate;  //�ظ�ʱ��
	private String replyContent;  //�ظ�����
	
	
	
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}
	public String getUserId_from() {
		return userId_from;
	}
	public void setUserId_from(String userId_from) {
		this.userId_from = userId_from;
	}
	public String getUserId_to() {
		return userId_to;
	}
	public void setUserId_to(String userId_to) {
		this.userId_to = userId_to;
	}
	public String getAvatarUrl_from() {
		return avatarUrl_from;
	}
	public void setAvatarUrl_from(String avatarUrl_from) {
		this.avatarUrl_from = avatarUrl_from;
	}
	public String getUserName_from() {
		return userName_from;
	}
	public void setUserName_from(String userName_from) {
		this.userName_from = userName_from;
	}
	public String getUserName_to() {
		return userName_to;
	}
	public void setUserName_to(String userName_to) {
		this.userName_to = userName_to;
	}
	public String getNickName_from() {
		return nickName_from;
	}
	public void setNickName_from(String nickName_from) {
		this.nickName_from = nickName_from;
	}
	public String getNickName_to() {
		return nickName_to;
	}
	public void setNickName_to(String nickName_to) {
		this.nickName_to = nickName_to;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getReplyToId() {
		return replyToId;
	}
	public void setReplyToId(String replyToId) {
		this.replyToId = replyToId;
	}
	
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public int getReplyComment() {
		return replyComment;
	}
	public void setReplyComment(int replyComment) {
		this.replyComment = replyComment;
	}
	
	
}
