package com.project.schoolservice;


import com.project.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

	private NotificationManager manager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Bundle bundle = intent.getExtras();
		System.out.println("ssssssssssss---:"+bundle.getString("book_name"));
		System.out.println("here");
		String bookname = bundle.getString("book_name");
		String time = bundle.getString("remind_time");
		int id = bundle.getInt("requestCode");
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
//		String id = "1";
		//MainActivity是你点击通知时想要跳转的Activity
        Intent playIntent = new Intent(context, BookRemindActivity.class);
//        playIntent.putExtra("test", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("还书提醒").setContentText("你的还书提醒："+bookname).setSmallIcon(R.drawable.logo_icon).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true).setSubText("应还时间： "+time);
        manager.notify(id, builder.build());
		
	}

}
