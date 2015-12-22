package com.project.service;

import java.util.HashMap;
import java.util.Map;

import com.project.communicate.ChatActivity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;

public class BaseSerivce extends Service {

	private static final String TAG = "BaseSerivce";
	private static final String APP_NAME = "nodistance";
	private static final int MAX_TRICKER_MSG_LEN = 50;
	private static int SERVICE_NOTIFICATION = 1;
	
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private Intent mNotificationIntent;
	private Vibrator mVibrator;
	private WakeLock mWakeLock;
	
	//初始化通知的数量
	private Map<String , Integer> mNotificationCount = new HashMap<String, Integer>(2);
	//初始化通知的ID
	private Map<String , Integer> mNotificationId  = new HashMap<String, Integer>(2);
	
	//最后一次通知的ID
	private int mLastNotificationId = 2;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "called onUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		Log.i(TAG, "called onRebind()");
		super.onRebind(intent);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_NAME);
		
		addNotificationMGR();
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "called onDestory()");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "called onStartCommand()");
		return START_STICKY;
	}
	
	private void addNotificationMGR(){
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotificationIntent = new Intent(this, ChatActivity.class);
	}
	

}
