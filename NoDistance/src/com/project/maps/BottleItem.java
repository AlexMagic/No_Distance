package com.project.maps;

import java.io.Serializable;

public class BottleItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String content;
	private String username;
	private String time;
	private String good;
	private int distance;
	private String[] goods ;
	private boolean isHeart;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getGood() {
		return good;
	}
	public void setGood(String good) {
		this.good = good;
	}
	public void setGoods(String[] goods) {
		this.goods = goods;
	}
	public String[] getGoods() {
		return goods;
	}
	public boolean isHeart() {
		return isHeart;
	}
	public void setHeart(boolean isHeart) {
		this.isHeart = isHeart;
	}
	
	
	
	
}
