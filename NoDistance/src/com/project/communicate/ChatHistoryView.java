package com.project.communicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.project.R;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
import com.project.app.MyApplication;
import com.project.domain.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ChatHistoryView {

	private Activity mActivity;
	private Context mContext;
	
	private InputMethodManager inputMethodManager;
	
	private View chatHisView;
	
	private ListView chatHisListView;
	
	private ChatHistoryAdapter adapter;
	
	private Map<String, User> contactList;
	
	
	public ChatHistoryView(Activity mActivity , Context mContext){
		this.mActivity = mActivity;
		this.mContext = mContext;
		chatHisView = LayoutInflater.from(mContext).inflate(R.layout.chat_history, null);
		initView();
		
		setUpView();
	}
	
	
	
	private void initView() {
		// contact list
		contactList = MyApplication.getInstance().getContactList();
		chatHisListView = (ListView) chatHisView.findViewById(R.id.chat_listView);
		
	
	}

	private void setUpView(){
		
		if(contactList!=null && contactList.size()>0){
			adapter = new ChatHistoryAdapter(mContext, 1,  loadUsersWithRecentChat());
			chatHisListView.setAdapter(adapter);
		}
		
		
		
		chatHisListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//				if(isItemBarOpen){
//					
//				}
				EMContact emContact = adapter.getItem(position);
				
				//进入聊天界面
				Intent intent = new Intent(mContext , ChatActivity.class);
				if(emContact instanceof EMGroup){
					// 点击的是群聊项
					intent.putExtra("chatTpye", ChatActivity.CHATTYPE_GROUP);
					intent.putExtra("groupId" ,  ((EMGroup) emContact).getGroupId());
				}else{
					//点击的是单聊项
					User user = contactList.get(emContact.getUsername());
					
					if(user.getNickname() != null)
						intent.putExtra("usernic", user.getNickname());
					
					intent.putExtra("userId", user.getUsername());
				}
				mContext.startActivity(intent);
			}
			
		});
		
		//长按弹出删除框 ， 用popupwindows
		chatHisListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				return true;
			}
		});
	}
	
	
	
	/**
	 * 根据最后一条消息的时间顺序
	 */
	public void sortUserByLastChatTime(List<EMContact> contactList){
		Collections.sort(contactList, new Comparator<EMContact>(){

			@Override
			public int compare(final EMContact user1, final EMContact user2) {
				EMConversation conversation1 = EMChatManager.getInstance().getConversation(user1.getUsername());
				EMConversation conversation2 = EMChatManager.getInstance().getConversation(user2.getUsername());
				
				EMMessage user1LastMessage = conversation1.getLastMessage();
				EMMessage user2LastMessage = conversation2.getLastMessage();
				
				if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
					return 0;
				} else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}
			
		});
	}
	
	
	/**
	 * 获取有聊天记录的users和groups
	 * 
	 * @param context
	 * @return
	 */
	private List<EMContact> loadUsersWithRecentChat() {
		List<EMContact> resultList = new ArrayList<EMContact>();
//		System.out.println("num----:"+resultList.size());
		
//		syso
		//获取有聊天记录的users，不包括陌生人
		for (EMContact user : contactList.values()) {
			EMConversation conversation = EMChatManager.getInstance().getConversation(user.getUsername());
//			System.out.println("username--------EMConversation:"+user.getUsername());
//			System.out.println("conversation unread--------------:"+conversation.getUnreadMsgCount());
//			System.out.println("conversation.getMsgCount()------:"+conversation.getMsgCount());
			
			if (conversation.getMsgCount() > 0) {
				resultList.add(user);
			}
		}
//		for(EMGroup group : EMGroupManager.getInstance().getAllGroups()){
//			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());
//			if(conversation.getMsgCount() > 0){
//				resultList.add(group);
//			}
//		}
		
		// 排序
		sortUserByLastChatTime(resultList);
		return resultList;
	}
	

	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		adapter = new ChatHistoryAdapter(mActivity,R.layout.chat_list_item, loadUsersWithRecentChat());
		chatHisListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		chatHisView.invalidate();
	}


	public View getView(){
		return chatHisView;
	}
	
}
