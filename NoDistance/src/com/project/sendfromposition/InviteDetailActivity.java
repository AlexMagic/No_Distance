package com.project.sendfromposition;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.R;

public class InviteDetailActivity extends Activity {
	
	private LinearLayout invite_layout = null;
	private Button btn_invite = null;
	private ImageView iv_userhead = null;
	private TextView tv_place;
	private TextView tv_distance;
	private TextView tv_invite;
	private TextView tv_time;
	
	private NearListItem item ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.invite_detail);
		
		item = (NearListItem) getIntent().getSerializableExtra("item");
		
		setUpView();
		
		setListner();
	}

	private void setUpView() {
		invite_layout = (LinearLayout) findViewById(R.id.invite_layout);
		btn_invite = (Button) findViewById(R.id.btn_invite);
		iv_userhead = (ImageView) findViewById(R.id.user_head);
		tv_place = (TextView) findViewById(R.id.tv_place);
		tv_invite = (TextView) findViewById(R.id.tv_invite);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_time = (TextView) findViewById(R.id.tv_invite_time);
	}

	private void setListner() {
		
	}
}
