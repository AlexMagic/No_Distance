package com.project.util;

public class FriendLatLngData {
	private int ID;
	private String AppID;
	private String Sex;
	private String Date;
	private double Lat;
	private double Lng;
	private String nickname;
	
	
	public FriendLatLngData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FriendLatLngData(int id,String appID, String sex, String date, double lat,
			double lng ) {
		super();
		ID = id;
		AppID = appID;
		Sex = sex;
		Date = date;
		Lat = lat;
		Lng = lng;
	}
	
	

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getAppID() {
		return AppID;
	}
	public void setAppID(String appID) {
		AppID = appID;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public double getLat() {
		return Lat;
	}
	public void setLat(double lat) {
		Lat = lat;
	}
	public double getLng() {
		return Lng;
	}
	public void setLng(double lng) {
		Lng = lng;
	}
	
	

}