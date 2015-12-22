package com.project.activity.base;

import com.project.R;
import com.project.app.MyApplication;
import com.project.util.ViewChangUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Desktop implements OnClickListener{
	
	private Context mContext;
	private View mDesktop;
	private LinearLayout mInformation;
//	private ImageView mUserHead;
	private TextView unReadLabel;
//	private TextView mUserName;
	
	private Button btn_school_map;
	private Button btn_school_serivce;
	private Button btn_communicate;
	private Button btn_nearby;
	private Button btn_setting;
	private Button btn_mine;
	
	private String username ;
	
	private OnChangeViewListener mOnChangeViewListener;

	public Desktop(Context context){
		mContext = context;
		mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
		
		username = MyApplication.getInstance().getNickName() !=null ? MyApplication.getInstance().getNickName(): MyApplication.getInstance().getUserName();
//		System.out.println("nick---------:"+ MyApplication.getInstance().getNickName());
		findViewById();
		
		setListener();
	}

	
	public View getunReadLabelView(){
		return unReadLabel;
	}
	
	private void findViewById() {
//		mInformation = (LinearLayout) mDesktop.findViewById(R.id.desktop_top_layout);
//		mUserHead = (ImageView) mDesktop.findViewById(R.id.desktop_top_avatar);
		
		unReadLabel = (TextView) mDesktop.findViewById(R.id.unread_msg_number);
		
//		mUserName = (TextView) mDesktop.findViewById(R.id.user_name);
//		mUserName.setId(6);
//		if(username != null){
//		mUserName.setText(username);
//		}
		
		btn_school_map = (Button) mDesktop.findViewById(R.id.btn_school_map);
		btn_school_map.setId(0);
		
//		btn_school_serivce = (Button) mDesktop.findViewById(R.id.btn_school_service);
//		btn_school_serivce.setId(1);
		
		btn_communicate = (Button) mDesktop.findViewById(R.id.btn_communicate);
		btn_communicate.setId(2);
		
		btn_nearby = (Button) mDesktop.findViewById(R.id.btn_nearby);
		btn_nearby.setId(3);
		
//		btn_setting = (Button) mDesktop.findViewById(R.id.btn_setting);
//		btn_setting.setId(4);
		
		btn_mine = (Button) mDesktop.findViewById(R.id.btn_mine);
		btn_mine.setId(5);
		
		
	}
	
	
	
	public void setListener(){
		
		btn_school_map.setOnClickListener(this);
//		btn_school_serivce.setOnClickListener(this);
		btn_communicate.setOnClickListener(this);
		btn_nearby.setOnClickListener(this);
//		btn_setting.setOnClickListener(this);
		btn_mine.setOnClickListener(this);
	
	}
	
	
	
	public View getView(){
		return mDesktop;
	}
	
	public interface OnChangeViewListener{
		public abstract void onChangeView(int id);
	}
	
	public void setOnChangeViewListener(OnChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		if (mOnChangeViewListener != null) {
			switch(v.getId()){
			case 0:
				mOnChangeViewListener.onChangeView(ViewChangUtil.SCHOOL_MAP);
//				System.out.println("maps");
				break;
			case 1:
				mOnChangeViewListener.onChangeView(ViewChangUtil.SCHOOL_SERVICE);
//				System.out.println("service");
				break;
			case 2:
				mOnChangeViewListener.onChangeView(ViewChangUtil.COMMUNICATION);
//				System.out.println("commu");
				break;
			case 3:
				mOnChangeViewListener.onChangeView(ViewChangUtil.NEARBY);
//				System.out.println("enter");
				break;
			case 5:
				mOnChangeViewListener.onChangeView(ViewChangUtil.USER_INFORMATION);
				break;
			
			}
		}
	}
	
}
