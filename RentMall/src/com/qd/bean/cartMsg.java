package com.qd.bean;

public class cartMsg {
	private int cartId;
	private int cartGoodId;
	private String cartUserId;
	private String goodName;
	private double price;
	private int cartNum;
	private int timeLen;
	private int deposit;
	private int unit;
	private String image1;
	private int num;
	private String userId; //这里是指发布商品的用户账号
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getCartGoodId() {
		return cartGoodId;
	}
	public void setCartGoodId(int cartGoodId) {
		this.cartGoodId = cartGoodId;
	}
	
	public String getCartUserId() {
		return cartUserId;
	}
	public void setCartUserId(String cartUserId) {
		this.cartUserId = cartUserId;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCartNum() {
		return cartNum;
	}
	public void setCartNum(int cartNum) {
		this.cartNum = cartNum;
	}
	public int getTimeLen() {
		return timeLen;
	}
	public void setTimeLen(int timeLen) {
		this.timeLen = timeLen;
	}
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public int getUnit() {
		return unit;
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
}
