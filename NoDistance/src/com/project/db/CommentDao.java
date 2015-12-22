package com.project.db;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.domain.RemindBack;
import com.project.schoolservice.CommentListItem;

public class CommentDao {
	public final static String TABLE_NAME = "users_comment";
	
	public final static String COLUMN_COMMENT_ID = "id";
	public final static String COLUMN_USER_NAME = "user_name";
	public final static String COLUMN_USER_NICK = "user_nick";
	public final static String COLUMN_USER_COMMENT = "user_comment";
	public final static String COLUMN_COMMENT_TIME = "comment_time";
	public final static String COLUMN_BOOK_NAME = "book_name";
	
	private DBHelper dbHelper;
	
	public CommentDao(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	//保存评论
	public void saveComment(CommentListItem comment){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			try {
				ContentValues values = new ContentValues();
				values.put(COLUMN_BOOK_NAME, new String(comment.getBookname().getBytes(), "UTF-8"));
				values.put(COLUMN_COMMENT_TIME, comment.getTime());
				values.put(COLUMN_USER_COMMENT, comment.getComment());
				values.put(COLUMN_USER_NAME, comment.getUsername());
				values.put(COLUMN_USER_NICK, comment.getNickname());
				db.replace(TABLE_NAME, null, values);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	//获取评论
	public Map<String , CommentListItem> getComment(String bookname){
		Map<String , CommentListItem> infos = new HashMap<String, CommentListItem>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor;
			try {
				cursor = db.rawQuery("select * from " + TABLE_NAME + " where book_name =? " /* + " desc" */, new String[]{(new String(bookname.getBytes() , "UTF-8"))});
				System.out.println("cursor.getCount()--:"+cursor.getCount());
				if(cursor.getCount()!=0){
					while (cursor.moveToNext()) {
						int id = cursor.getInt(cursor.getColumnIndex(COLUMN_COMMENT_ID));
						String book_comment = cursor.getString(cursor.getColumnIndex(COLUMN_USER_COMMENT));
						String user_nick = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NICK));
						String comment_time = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT_TIME));
						String userid = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
						
						CommentListItem comment = new CommentListItem();
						
						comment.setComment(book_comment);
						comment.setNickname(user_nick);
						comment.setTime(comment_time);
						comment.setUsername(userid);
						
						infos.put(id+"", comment);
					}
				}
				cursor.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return infos;
	}
}
