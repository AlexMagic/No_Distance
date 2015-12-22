package com.project.communicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.project.R;
import com.project.activity.DesktopActivity;
//import com.project.activity.base.FlipperLayout.OnOpenListener;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
import com.project.activity.base.MenuForCommuPopWindow;
import com.project.domain.User;

public class StudentCommunicate {
	
//	private DesktopActivity test;
	private Activity mActivity;
	private Context mContext;
	private View mCommu;
	
	private View popView;
	private ChatHistoryView chatHistoryView;
	private View findFriendsView;
	private View momentView;
	private ContactView contactView;
	
	private FrameLayout mLayout;
	
	private MenuForCommuPopWindow mPopWindow;
	
	private Button btn_popWindow;
	private Button btn_search_content;
	private Button btn_add_frd;
	private ImageView mFlip;
	
	private ListView listView;
	
	
	private int mWidth , mHeight;
	private int mChooseId=0;
	
	private boolean isChangeItem = false;
	private boolean isItemBarOpen = false;
	
	private OnOpenListener mOnOpenListener;
	
	public StudentCommunicate(Activity activity ,Context context){
		mContext = context;
		mActivity = activity;
		
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;//宽度
		mHeight = dm.heightPixels ;//高度
		
		mCommu = LayoutInflater.from(context).inflate(R.layout.communicate, null);
		chatHistoryView = new ChatHistoryView(mActivity , mContext);
		findFriendsView = LayoutInflater.from(context).inflate(R.layout.find_friend, null);
		momentView = LayoutInflater.from(context).inflate(R.layout.friends_moments, null);
		contactView = new ContactView(mActivity , mContext);
		chatHistoryView.refresh();
		contactView.refresh();
		initView();
		
		setLinster();
	}
	
	
	
	private void initView() {
		
		mFlip = (ImageView) mCommu.findViewById(R.id.btn_goto_residenmenu);
		btn_search_content = (Button) mCommu.findViewById(R.id.btn_search_content);
		btn_search_content.setVisibility(View.VISIBLE);
		btn_popWindow = (Button) mCommu.findViewById(R.id.other_oper_chat);
		btn_add_frd = (Button) mCommu.findViewById(R.id.btn_add_frd);
		
//		//注册上下文菜单
//		mActivity.registerForContextMenu(chatHisListView);
		
//		changeView(chatHistoryView.getView());
//		chatHistoryView.refresh();

		mLayout = (FrameLayout) mCommu.findViewById(R.id.commu_layout);
		
		mLayout.addView(chatHistoryView.getView());
	}
	
	
	private void setLinster() {
		
		mFlip.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		
		btn_popWindow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popWindowOpen(mActivity , mChooseId);
			}
		});
		
		btn_add_frd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 进入添加好友页
				btn_add_frd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mActivity.startActivity(new Intent(mActivity, AddContactActivity.class));
					}
				});
			}
		});
	}
	
	
	

	//打开popwindow
	public void popWindowOpen(final Activity activity , int chooseId){
		
		if(mPopWindow == null){
			mPopWindow = new MenuForCommuPopWindow(mActivity, itemsOnClick , chooseId);
		}
			
		if(mPopWindow.isShowing()){
				mPopWindow.dismiss();
		}else{
			mPopWindow.setAnimationStyle(R.style.ModePopupAnimation);
			mPopWindow.showAtLocation(mCommu.findViewById(R.id.other_oper_chat), Gravity.CENTER, mWidth/2, mHeight/2);
			
		}
		
		mPopWindow.popListView(chooseId);
		
	}
	

	
	public OnItemClickListener itemsOnClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			mChooseId = position;
			mPopWindow.dismiss();
			switch(position){
			case 0:
				btn_search_content.setVisibility(View.GONE);
				btn_add_frd.setVisibility(View.GONE);
				changeView(chatHistoryView.getView());
				chatHistoryView.refresh();
				break;
//			case 1:
//				btn_search_content.setVisibility(View.GONE);
//				btn_add_frd.setVisibility(View.GONE);
//				changeView(findFriendsView);
//				break;
//			case 2:
//				btn_search_content.setVisibility(View.GONE);
//				btn_add_frd.setVisibility(View.GONE);
//				changeView(momentView);
//				break;
			case 1:
				btn_add_frd.setVisibility(View.VISIBLE);
				btn_search_content.setVisibility(View.GONE);
				changeView(contactView.getView());
				contactView.refresh();
				break;
			}
		}
	};
	
	
	
	public void changeView(View changeView){
		mLayout.removeAllViews();
		mLayout.addView(changeView);
	}
	
	public Activity getActivity(){
		return mActivity;
	}
	
	public View getView(){
		return mCommu;
	}
	
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

}
