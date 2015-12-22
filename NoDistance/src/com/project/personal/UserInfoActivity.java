package com.project.personal;

import com.project.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	private String username;
	private Intent intent;
	private Bitmap mBitmap;
	private SeekBar seekBar;
	
	private TextView tv_username;
	private TextView tv_test;
	private ImageView iv_userhead;
	private ProgressBar pb_loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal);
		
	}
	
	public void setUpView(){
		//获取用户数据
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
	}
	
	public void setListner(){
		
	}
	
	
}
