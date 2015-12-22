package com.project.db;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.domain.BookKeep;
import com.project.domain.RemindBack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RemindBookDao {
	
	public static final String TABLE_NAME = "remind_book";
	
	public static final String COLUMN_BOOK_ID = "id";
	public static final String COLUMN_BOOK_NAME = "remind_book_name";
	public static final String COLUMN_REMIND_TIME = "remind_time" ;
	
	private DBHelper dbHelper;
	
	public RemindBookDao(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}
	
	public boolean saveRemind(RemindBack rb){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			try {
				Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +" where remind_book_name =? ", new String[]{(new String(rb.getBookname().getBytes() , "UTF-8"))});
				if(cursor.getCount()==0){
					ContentValues values = new ContentValues();
					values.put(COLUMN_BOOK_NAME, new String(rb.getBookname().getBytes(), "UTF-8"));
					values.put(COLUMN_REMIND_TIME, rb.getRemindTime());
					db.replace(TABLE_NAME, null, values);
				}
				cursor.close();
				return true;
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}	
	
	public Map<String , RemindBack> getBookInfo(){
		Map<String , RemindBack > infos = new HashMap<String, RemindBack>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String bookname = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME));
				String remindTime = cursor.getString(cursor.getColumnIndex(COLUMN_REMIND_TIME));
				
				RemindBack rb = new RemindBack();
				
				rb.setBookname(bookname);
				rb.setRemindTime(remindTime);
				
				infos.put(bookname, rb);
			}
			cursor.close();
		}
		return infos;
	}
	
	public void deleteKeeping(List<String> booknames){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			for(int i=0 ; i<booknames.size() ; i++){
				db.delete(TABLE_NAME, COLUMN_BOOK_NAME + " = ?", new String[]{booknames.get(i)});
			}
		}
	}
	
	public void upDateTime(String bookname  ,ContentValues values){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.update(TABLE_NAME, values, COLUMN_BOOK_NAME + " = ?", new String[]{bookname});
		}
	}
	
	public int getRemindId(String bookname){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		int id = -1;
		if(db.isOpen()){
			try {
				Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +" where remind_book_name =? ", new String[]{(new String(bookname.getBytes() , "UTF-8"))});
				if(cursor.getCount()==0){
					id =  cursor.getInt(cursor.getColumnIndex(COLUMN_BOOK_ID));
				}
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return id;
	}
}

