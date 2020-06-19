package com.qd.bean;

public class afterChatMsg {
	private int Id;
	private int mainChatId;
	private String chatRightIdA;
	private String chatLeftIdA;
	private int isRead;
	private String chatDate;
	private String chatTime;
	private String chatContent;
	private int isImage;  //判断消息内容是否为图片，0为不是
	
	
	public int getIsImage() {
		return isImage;
	}
	public void setIsImage(int isImage) {
		this.isImage = isImage;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getMainChatId() {
		return mainChatId;
	}
	public void setMainChatId(int mainChatId) {
		this.mainChatId = mainChatId;
	}
	public String getChatRightIdA() {
		return chatRightIdA;
	}
	public void setChatRightIdA(String chatRightIdA) {
		this.chatRightIdA = chatRightIdA;
	}
	
	public String getChatLeftIdA() {
		return chatLeftIdA;
	}
	public void setChatLeftIdA(String chatLeftIdA) {
		this.chatLeftIdA = chatLeftIdA;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getChatDate() {
		return chatDate;
	}
	public void setChatDate(String chatDate) {
		this.chatDate = chatDate;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public String getChatContent() {
		return chatContent;
	}
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
	
}
