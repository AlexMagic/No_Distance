package com.project.invitesuccess;


import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.project.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class InviteSucView {
	
	private Activity mActivity;
	private Context mContext;
	
	private View inviteSucView;
	
	private ImageView mFlip;
	
	private PullToRefreshListView  listview;
	private ListView actualListView ;
	
	private InviteSucAdapter adapter;
	
	public InviteSucView(Activity mActivity , Context mContext){
		this.mActivity = mActivity;
		this.mContext = mContext;
		
		inviteSucView = LayoutInflater.from(mContext).inflate(R.layout.school_map, null);
		
		setUpview();
		
		setListner();
	}

	private void setUpview() {
		mFlip = (ImageView) inviteSucView.findViewById(R.id.btn_goto_residenmenu);
		listview = (PullToRefreshListView) inviteSucView.findViewById(R.id.pull_refresh_list);
	}

	private void setListner() {
		
	}
}
