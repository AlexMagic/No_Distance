package com.project.db;

import com.project.app.MyApplication;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{


	public static final int DATABASE_VERSION= 1;
	private static DBHelper instance;
	
	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ UserDao.TABLE_NAME + " ("
			+ UserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY , "
			+ UserDao.COLUMN_NAME_NICK + " TEXT , "
			+ UserDao.COLUMN_SEX + " TEXT , "		
			+ UserDao.COLUMN_STUID + " TEXT , " 
			+ UserDao.COLUMN_EMAIL + " TEXT );";
	
	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ InviteMessageDao.TABLE_NAME + " ("
			+ InviteMessageDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ InviteMessageDao.COLUMN_NAME_FROM + " TEXT, "
			+ InviteMessageDao.COLUMN_NAME_GROUP_ID + " TEXT, "
			+ InviteMessageDao.COLUMN_NAME_GROUP_Name + " TEXT, "
			+ InviteMessageDao.COLUMN_NAME_REASON + " TEXT, "
			+ InviteMessageDao.COLUMN_NAME_STATUS + " INTEGER, "
			+ InviteMessageDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
			+ InviteMessageDao.COLUMN_NAME_TIME + " TEXT); ";
	

	private static final String KEEP_BOOK_TABLE_CREATE = "CREATE TABLE "
			+ KeepBookDao.TABLE_NAME + "("
			+ KeepBookDao.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
			+ KeepBookDao.COLUMN_BOOK_NAME + " TEXT ,"
			+ KeepBookDao.COLUMN_BOOK_INDEX + " TEXT ,"
			+ KeepBookDao.COLUMN_BOOK_LOC + " TEXT);";
			
	
	private static final String REMIND_BOOK_BACK_TABLE_CREATE = "CREATE TABLE "
			+ RemindBookDao.TABLE_NAME + " ("
			+ RemindBookDao.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ RemindBookDao.COLUMN_BOOK_NAME + " TEXT, "
			+ RemindBookDao.COLUMN_REMIND_TIME + " TEXT)";
	
	
	private static final String COMMENT_TABLE_CREATE = "CREATE TABLE "
			+ CommentDao.TABLE_NAME + " ( "
			+ CommentDao.COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+ CommentDao.COLUMN_BOOK_NAME + " TEXT, "
			+ CommentDao.COLUMN_USER_COMMENT + " TEXT, "
			+ CommentDao.COLUMN_COMMENT_TIME + " TEXT, "
			+ CommentDao.COLUMN_USER_NAME + " TEXT,"
			+ CommentDao.COLUMN_USER_NICK + " TEXT)";
	
	
	public static DBHelper getInstance(Context context){
		if(instance == null){
			instance = new DBHelper(context.getApplicationContext());
		}
		
		return instance;
	}
	
	public DBHelper(Context context) {
		super(context, getUserDBName(), null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public static String getUserDBName(){
		return MyApplication.getInstance().getUserName()+"_user.db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
		db.execSQL(KEEP_BOOK_TABLE_CREATE);
		db.execSQL(REMIND_BOOK_BACK_TABLE_CREATE);
		db.execSQL(COMMENT_TABLE_CREATE);
		
	}

	//如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        db.execSQL("ALTER TABLE users ADD COLUMN other STRING");  
        db.execSQL("ALTER TABLE new_friends_msgs ADD COLUMN other STRING");  
        db.execSQL("ALTER TABLE keep_book ADD COLUMN other STRING");
        db.execSQL("ALTER TABLE remind_book ADD COLUMN other STRING");
//      db.execSQL(REMIND_BOOK_BACK_TABLE_CREATE);
    } 
	
	public void closeDB(){
		if(instance != null){
		 try {
	        SQLiteDatabase db = instance.getWritableDatabase();
	        db.close();
		 }catch (Exception e) {
		        e.printStackTrace();
		    }
		    instance = null;
		}
	}

}
