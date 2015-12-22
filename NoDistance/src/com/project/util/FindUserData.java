package com.project.util;

public class FindUserData {
	private int ID;
	private String AppID;
	private String NickName;
	private String Sex;
	private double Lat;
	private double Lng;	
	private String Distance;
	
	
	public FindUserData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FindUserData(int iD, String appID, String nickName, String sex,
			double lat, double lng, String distance) {
		super();
		ID = iD;
		AppID = appID;
		NickName = nickName;
		Sex = sex;
		Lat = lat;
		Lng = lng;
		Distance = distance;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
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

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
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
