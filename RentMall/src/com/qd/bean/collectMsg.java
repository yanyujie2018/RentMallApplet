package com.qd.bean;

public class collectMsg {
	private int id;
	private String collectUserId;
	private int collectGoodId;
	private String collectDate;
	private double price;
	private int unit;
	private String goodName;
	private int num;
	private String image1;
	private int deposit;
	
	
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getUnit() {
		return unit;
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCollectUserId() {
		return collectUserId;
	}
	public void setCollectUserId(String collectUserId) {
		this.collectUserId = collectUserId;
	}
	public int getCollectGoodId() {
		return collectGoodId;
	}
	public void setCollectGoodId(int collectGoodId) {
		this.collectGoodId = collectGoodId;
	}
	public String getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	
}
