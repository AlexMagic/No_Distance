package com.project.db;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;





import com.easemob.util.HanziToPinyin;
import com.project.constane.Constant;
import com.project.domain.User;

public class UserDao {
	public static final String TABLE_NAME = "users";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_IS_STRANGER = "is_stranger";
	public static final String COLUMN_SEX = "sex";
	public static final String COLUMN_STUID = "student_id";
	public static final String COLUMN_EMAIL = "email";
//	public static final String COLUMN_FAVOUR = "favourite";
	
	private DBHelper dbHelper;
	
	public UserDao(Context applicationContext) {
		dbHelper = DBHelper.getInstance(applicationContext);
	}


	/**
	 * 保存好友的list
	 */
	public void saveContactList(List<User> contactList){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, null, null);
			for(User user : contactList){
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, user.getUsername());
				if(user.getNickname()!= null) {
					values.put(COLUMN_NAME_NICK, user.getNickname());
				}else 
					values.put(COLUMN_NAME_NICK, "");
				
				values.put(COLUMN_SEX, user.getSex());
				values.put(COLUMN_STUID, user.getStuId());
				values.put(COLUMN_EMAIL, user.getEmail());
				
				db.replace(TABLE_NAME, null, values);
			}
		}
	}

	/**
	 * 获取好友list
	 * @return
	 */
	public Map<String, User> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
				String sex = cursor.getString(cursor.getColumnIndex(COLUMN_SEX));
				String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
				String stuId = cursor.getString(cursor.getColumnIndex(COLUMN_STUID));
				User user = new User();
				user.setUsername(username);
				user.setNickname(nick);
				user.setEmail(email);
				user.setNickname(nick);
				user.setStuId(stuId);
				user.setSex(sex);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNickname())) {
					headerName = user.getNickname();
				} else {
					headerName = user.getUsername();
				}
				
				if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
							.get(0).target.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				users.put(username, user);
			}
			cursor.close();
		}
		return users;
	}
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[]{username});
		}
	}
	
	
	public String getNickName(String appId){
		String nick = null;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if(db.isOpen()){
			try {
				Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + "  where username =?  " /* + " desc" */, new String[]{(new String(appId.getBytes() , "UTF-8"))});
				if(cursor.getCount()!=0){
					cursor.moveToNext();
					nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
					return nick;
				}
				cursor.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return nick;
	}
	
	public boolean isExist(String appID){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if(db.isOpen()){
			try {
				Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where username =? " /* + " desc" */, new String[]{(new String(appID.getBytes() , "UTF-8"))});
				if(cursor.getCount()!=0){
					cursor.close();
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(User user){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, user.getUsername());
		if(user.getNickname() != null)
			values.put(COLUMN_NAME_NICK, user.getNickname());
		else 
			values.put(COLUMN_NAME_NICK, "");
		
		values.put(COLUMN_SEX, user.getSex());
		values.put(COLUMN_STUID, user.getStuId());
		values.put(COLUMN_EMAIL, user.getEmail());
		
		if(db.isOpen()){
			db.replace(TABLE_NAME, null, values);
		}
	}
}
