package com.project.sendfromposition;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class NearListItem implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id ;
	private	String appID;
	private String username;
	private String meetingContent;
	private String dateTime;
	private String meetingPos;
	private double distance;
	private ArrayList<String> picPath; 
	
	
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMeetingContent() {
		return meetingContent;
	}
	public void setMeetingContent(String meetingContent) {
		this.meetingContent = meetingContent;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getMeetingPos() {
		return meetingPos;
	}
	public void setMeetingPos(String meetingPos) {
		this.meetingPos = meetingPos;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public ArrayList<String> getPicPath() {
		return picPath;
	}
	public void setPicPath(ArrayList<String> picPath) {
		this.picPath = picPath;
	}
	
	
}
