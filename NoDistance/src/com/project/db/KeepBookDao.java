package com.project.db;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.domain.BookKeep;

public class KeepBookDao {
	
	public static final String TABLE_NAME = "keep_book";
	
	public static final String COLUMN_BOOK_ID = "id";
	public static final String COLUMN_BOOK_NAME = "bookname";
	public static final String COLUMN_BOOK_INDEX = "bookindex";
	public static final String COLUMN_BOOK_LOC = "booklocation";
	
	private DBHelper dbHelper;
	
	public KeepBookDao(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}
	
	public boolean keepBook(BookKeep bk){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int id = -1;
		if(db.isOpen()){
//			db.delete(TABLE_NAME, null, null);
			try {
//				System.out.println( new String(bk.getBookname().getBytes() , "UTF-8"));
				Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +" where bookname =? ", new String[]{(new String(bk.getBookname().getBytes() , "UTF-8"))});
				if(cursor.getCount()==0){
					ContentValues values = new ContentValues();
					try {
						values.put(COLUMN_BOOK_NAME, new String(bk.getBookname().getBytes() , "UTF-8"));
						values.put(COLUMN_BOOK_INDEX, new String(bk.getBookindex().getBytes() , "UTF-8"));
						values.put(COLUMN_BOOK_LOC, new String(bk.getBookloca().getBytes() , "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					db.insert(TABLE_NAME, null, values);
					System.out.println("true");
					cursor.close();
					return true;
				}
			
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}
	
	public Map<String , BookKeep> getBookInfo(){
		Map<String , BookKeep > infos = new HashMap<String, BookKeep>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String bookname = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME));
				String bookindex = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_INDEX));
				String bookloc = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_LOC));
				
				BookKeep bk = new BookKeep();
				
				bk.setBookname(bookname);
				bk.setBookindex(bookindex);
				bk.setBookloca(bookloc);
				
				infos.put(bookname, bk);
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
	
	
}
