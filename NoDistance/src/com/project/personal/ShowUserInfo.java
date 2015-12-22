package com.project.personal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.R;
//import com.project.activity.base.FlipperLayout.OnOpenListener;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
import com.project.app.MyApplication;


public class ShowUserInfo {
	
	private Activity mActivity;
	private Context mContext;
	
	private FrameLayout mFrameLayout;
	
	private View mPersonView;
	private TextView tv_username;
	private ImageView mFlip;
	
	private String username;
	
	private OnOpenListener mOnOpenListener;
	
	public ShowUserInfo(Activity mActivity , Context mContext){
		this.mActivity = mActivity;
		this.mContext = mContext;
		
		
		mPersonView = LayoutInflater.from(mContext).inflate(R.layout.personal, null);
		
		setUpView();
//		
		setListner();
	}
	
	

	public void setUpView(){
		mFlip = (ImageView) mPersonView.findViewById(R.id.btn_back);
		
		tv_username = (TextView) mPersonView.findViewById(R.id.tv_username);
		if(MyApplication.getInstance().getUserName()==null)
			tv_username.setText("ÇëµÇÂ¼");
		else
			tv_username.setText(MyApplication.getInstance().getNickName());
		
		
	}
	
	private void setListner() {
		mFlip.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
	}
	
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	public View getView(){
		return mPersonView;
	}
	
}
