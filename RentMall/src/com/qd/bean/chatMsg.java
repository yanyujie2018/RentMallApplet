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
	private int isShowR;  //�����ж�������Ϣ�᲻������������Ϣ��openId=chatRightId���û�����Ϣ������ʾ�����Ϊ0����ʾ�����Ϊ1����ʾ������û���ҳ���ϵ������Ϣɾ����ť��˵�����û������Ϣ������ʾ������Ϣ����ô�ͽ�isShow����Ϊ1��������Ϣ����ʱ�ٴ��޸�Ϊ0
	private int isShowL;  //�����ж�������Ϣ�᲻������������Ϣ��openId=chatLeftId���û�����Ϣ������ʾ��
	private List<afterChatMsg> afterChatList;   //�������������������Ϣ
	private int noRead;   //�������Ϊ�Ķ����������������������Լ�
	private afterChatMsg lastChat;  //������һ����������
	private String userName_other;  //���������������Ϣ�У��Է�������
	private String nickName_other;  //���������������Ϣ�У��Է����ǳ�
	private String avatarUrl_other;   //���������������Ϣ�У��Է���ͷ��
	private int isImage;  //�ж���Ϣ�����Ƿ�ΪͼƬ��0Ϊ����
	
	
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
