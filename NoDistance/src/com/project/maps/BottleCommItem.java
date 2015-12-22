package com.project.maps;

public class BottleCommItem {
	private int id;
	private int noteId;
	private String commFromUsername;
	private String toUsername;
	private String comment;
	private String reply;
	private String commTime;
	private String replyTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getNotetId() {
		return noteId;
	}
	public void setNotetId(int noteId) {
		this.noteId = noteId;
	}
	public String getCommFromUsername() {
		return commFromUsername;
	}
	public void setCommFromUsername(String commFromUsername) {
		this.commFromUsername = commFromUsername;
	}
	public String getToUsername() {
		return toUsername;
	}
	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	

	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	
	public String getCommTime() {
		return commTime;
	}
	
	public void setCommTime(String commTime) {
		this.commTime = commTime;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	
}
