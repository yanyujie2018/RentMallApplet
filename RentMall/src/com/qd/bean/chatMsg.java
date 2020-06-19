package com.qd.bean;

import java.util.List;

public class chatMsg {
	private int Id;
	private int chatGoodId;
	private String chatRightId;
	private String chatLeftId;
	private String chatDate;
	private String chatContent;
	private String chatTime;
	private int isRead;
	private int isShowR;  //用来判断这条信息会不会在主留言信息中openId=chatRightId的用户的消息界面显示，如果为0则显示，如果为1则不显示；如果用户在页面上点击了信息删除按钮，说明如果没有新信息不再显示此条信息，那么就将isShow设置为1，有新信息加入时再次修改为0
	private int isShowL;  //用来判断这条信息会不会在主留言信息中openId=chatLeftId的用户的消息界面显示，
	private List<afterChatMsg> afterChatList;   //用来存放下属的聊天信息
	private int noRead;   //用来存放为阅读的留言数，包括主留言自己
	private afterChatMsg lastChat;  //存放最后一条下属留言
	private String userName_other;  //用来存放主聊天信息中，对方的姓名
	private String nickName_other;  //用来存放主聊天信息中，对方的昵称
	private String avatarUrl_other;   //用来存放主聊天信息中，对方的头像
	private int isImage;  //判断消息内容是否为图片，0为不是
	
	
	public int getIsImage() {
		return isImage;
	}
	public void setIsImage(int isImage) {
		this.isImage = isImage;
	}
	public String getUserName_other() {
		return userName_other;
	}
	public void setUserName_other(String userName_other) {
		this.userName_other = userName_other;
	}
	public String getNickName_other() {
		return nickName_other;
	}
	public void setNickName_other(String nickName_other) {
		this.nickName_other = nickName_other;
	}
	public String getAvatarUrl_other() {
		return avatarUrl_other;
	}
	public void setAvatarUrl_other(String avatarUrl_other) {
		this.avatarUrl_other = avatarUrl_other;
	}
	public int getNoRead() {
		return noRead;
	}
	public void setNoRead(int noRead) {
		this.noRead = noRead;
	}
	public afterChatMsg getLastChat() {
		return lastChat;
	}
	public void setLastChat(afterChatMsg lastChat) {
		this.lastChat = lastChat;
	}
	public List<afterChatMsg> getAfterChatList() {
		return afterChatList;
	}
	public void setAfterChatList(List<afterChatMsg> afterChatList) {
		this.afterChatList = afterChatList;
	}
	
	public int getIsShowR() {
		return isShowR;
	}
	public void setIsShowR(int isShowR) {
		this.isShowR = isShowR;
	}
	public int getIsShowL() {
		return isShowL;
	}
	public void setIsShowL(int isShowL) {
		this.isShowL = isShowL;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getChatGoodId() {
		return chatGoodId;
	}
	public void setChatGoodId(int chatGoodId) {
		this.chatGoodId = chatGoodId;
	}
	public String getChatRightId() {
		return chatRightId;
	}
	public void setChatRightId(String chatRightId) {
		this.chatRightId = chatRightId;
	}
	public String getChatLeftId() {
		return chatLeftId;
	}
	public void setChatLeftId(String chatLeftId) {
		this.chatLeftId = chatLeftId;
	}
	public String getChatDate() {
		return chatDate;
	}
	public void setChatDate(String chatDate) {
		this.chatDate = chatDate;
	}
	public String getChatContent() {
		return chatContent;
	}
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
	
}
