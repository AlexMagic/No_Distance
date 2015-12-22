package com.project.activity.smack;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;


public class SmackImp implements Smack{

	private static XMPPConnection mXMMPconnection;
	private static ConnectionConfiguration mXMPPConfig;
	
	
//	public static XMPPConnection getInstance(){
//		if(mXMMPconnection == null){
//			mXMMPconnection = new XMPPConnection();
//		}
//		return mXMMPconnection;
//	}
	
	
	@Override
	public boolean login(String account, String password) throws XXException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean logout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addRosterItem(String user, String alias, String group)
			throws XXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRosterItem(String user) throws XXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameRosterItem(String user, String newName)
			throws XXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRosterItemToGroup(String user, String group)
			throws XXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameRosterGroup(String group, String newGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAuthorizationForRosterItem(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRosterGroup(String group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatusFromConfig() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String user, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendServerPing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNameForJID(String jid) {
		// TODO Auto-generated method stub
		return null;
	}

}
