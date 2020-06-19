package com.qd.bean;

public class followMsg {
	int Id;
	String followId;
	String followToId;
	userMsg user;
	
	
	public userMsg getUser() {
		return user;
	}
	public void setUser(userMsg user) {
		this.user = user;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getFollowId() {
		return followId;
	}
	public void setFollowId(String followId) {
		this.followId = followId;
	}
	public String getFollowToId() {
		return followToId;
	}
	public void setFollowToId(String followToId) {
		this.followToId = followToId;
	}
	
}
