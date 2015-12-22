package com.project.activity;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.message.BasicNameValuePair;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.util.HanziToPinyin;
import com.project.R;
import com.project.activity.base.Desktop;
import com.project.activity.base.Desktop.OnChangeViewListener;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
//import com.project.activity.base.FlipperLayout;
import com.project.activity.base.FlipperLayout1;
import com.project.app.MyApplication;
import com.project.communicate.ChatActivity;
import com.project.communicate.ChatHistoryView;
import com.project.communicate.ContactView;
import com.project.communicate.StudentCommunicate;
import com.project.constane.Constant;
import com.project.db.InviteMessageDao;
import com.project.db.UserDao;
import com.project.domain.InviteMessage;
import com.project.domain.InviteMessage.InviteMesageStatus;
import com.project.domain.User;
import com.project.entertainment.EnterTainMent;
import com.project.maps.DriftBottleView;
//import com.project.personal.Personal;
import com.project.personal.ShowUserInfo;
import com.project.schoolservice.BookRemindActivity;
import com.project.schoolservice.SchoolService;
import com.project.sendfromposition.ListFromNearBy;
import com.project.util.Distance;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;
import com.project.util.ViewChangUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


public class DesktopActivity extends Activity  implements OnOpenListener{

	protected static final String TAG = "DesktopActivity";
	
	private OnOpenListener mOnOpenListener ;
	
	private Desktop mDesktop;
	private static FlipperLayout1 mRoot; 
	private DriftBottleView mBottles;
//	private SchoolService mService;
	private static StudentCommunicate mCommu;
	private ListFromNearBy mNearBy;
	private ShowUserInfo mShowUserInfo;
	private ChatHistoryView chatHistoryView;
//	private ContactView mContactView;
	
//	private static Distance distance;
	
	private String appId ; 
	private String appPwd ; 
	private boolean isLogin ;
	
	private android.app.AlertDialog.Builder conflictBuilder;
	private boolean isConflictDialogShow;
	private boolean isConflict;
	
	private String username;
	
	private UserDao userDao;
	private InviteMessageDao inviteMessgeDao;
	
	private NewMessageBroadcastReceiver msgReceiver ;
	
	private List<LinkedHashMap<String, Object>> list = null;
	/**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
//    	Distance.getInstance(getApplicationContext());
    	
    	mRoot = new FlipperLayout1(DesktopActivity.this);
    	LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mRoot.setLayoutParams(params);
		
		mDesktop = new Desktop(this);
		mBottles = new DriftBottleView(this, this);
//		mService = new SchoolService(this, this);
		mCommu = new StudentCommunicate(this, this);
		mNearBy = new ListFromNearBy(this, this);
		mShowUserInfo = new ShowUserInfo(this, this );
		chatHistoryView = new ChatHistoryView(this,this);
//		mContactView = new ContactView(this,this);
    	
		mRoot.addView(mDesktop.getView(), params);
    	mRoot.addView(mBottles.getView(), params);

    	setContentView(mRoot);
    	
    	setListner();
    	
    	// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
    	
		// 注册一个离线消息的BroadcastReceiver
		IntentFilter offlineMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getOfflineMessageBroadcastAction());
		registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);
		
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
    	
    }
    
    public void setListner(){
    		
    	
    	mBottles.setOnOpenListener(this);
//    	mService.setOnOpenListener(this);
    	mCommu.setOnOpenListener(this);
    	mNearBy.setOnOpenListener(this);
    	mShowUserInfo.setOnOpenListener(this);
    	
    	mDesktop.setOnChangeViewListener(new OnChangeViewListener(){

			@Override
			public void onChangeView(int id) {
				// TODO Auto-generated method stub
				switch(id){
				
				case ViewChangUtil.SCHOOL_MAP:
					mRoot.close(mBottles.getView());
					break;
				
//				case ViewChangUtil.SCHOOL_SERVICE:
//					mRoot.close(mService.getView());
//					break;
				
				case ViewChangUtil.COMMUNICATION:
					mRoot.close(mCommu.getView());
					break;
				
				case ViewChangUtil.NEARBY:
					mRoot.close(mNearBy.getView());
//					System.out.println("NN");
//					Toast.makeText(DesktopActivity.this, "暂无开通", Toast.LENGTH_SHORT).show();
					break;
				case ViewChangUtil.USER_INFORMATION:
//					System.out.println("5555555555555");
					mRoot.close(mShowUserInfo.getView());
					break;
				}
				
				mRoot.getChildAt(0).setFocusableInTouchMode(false);
			}
			
    		
    	});
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(resultCode){
    	case RESULT_OK:
    		if(requestCode == RegisterActivity.REQUEST_CODE_REGISTER_SUCCESS){
    			System.out.println("testing--------------1");
//    			refresh();
    		}
    		break;
    	}
    }
    
    
    
    /**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
	}
    
    
    /**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		System.out.println("unreadMsgCountTotal---------:"+unreadMsgCountTotal);
		return unreadMsgCountTotal;
	}
    
    
	
	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
//			unreadLabel.setText(String.valueOf(count));
//			unreadLabel.setVisibility(View.VISIBLE);
		} else {
//			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}
	
	
	private NotificationManager manager;
	
    /**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
			
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			 EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//			 Notif
			 
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
//			String id = "1";
	        Intent playIntent = new Intent(DesktopActivity.this, ChatActivity.class);
	        
//	        if(message.getChatType() == ChatActivity.CHATTYPE_SINGLE)
	        playIntent.putExtra("userId", message.getFrom());
	        playIntent.putExtra("usernic","");
	        
	        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, playIntent, PendingIntent.FLAG_ONE_SHOT);
	        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
	        builder.setContentTitle("未读信息").setContentText("来自好友："+message.getFrom()).setSmallIcon(R.drawable.logo_icon).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);
	        
	        manager.notify(0, builder.build());
			 
//			System.out.println("message"+message.toString());
			// 刷新bottom bar消息未读数
			updateUnreadLabel(); 
			chatHistoryView.refresh();
			mRoot.close(mRoot.getChildAt(1));
			
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}
	
	
	
	
	/**
	 * 离线消息BroadcastReceiver
	 * sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI 有哪些人发来了离线消息
	 * UI 可以做相应的操作，比如下载用户信息
	 */
	private BroadcastReceiver offlineMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String[] users = intent.getStringArrayExtra("fromuser");
			String[] groups = intent.getStringArrayExtra("fromgroup");
			if (users != null) {
				for (String user : users) {
					System.out.println("收到user离线消息：" + user);
				}
			}
			if (groups != null) {
				for (String group : groups) {
					System.out.println("收到group离线消息：" + group);
				}
			}
			abortBroadcast();
		}
	};
    
    
	@SuppressWarnings("unused")
	public User getContact(final String userName){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				List<LinkedHashMap<String, Object>> tempList = null;
				
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
				params.add(new BasicNameValuePair("appID",userName));
				
				String url = "http://1.nodistanceservice.sinaapp.com/getUserData.php";
				String result = HttpUtil.queryStringForPost(url, params);
				System.out.println("result--0--desktopactivity: " + result);
				if(result != "-1" && !result.equals("-1")){
					list = JsonUtil.json2List(result);
				}
			}
		}).start();
		
//		Map<String, User> userlist = new HashMap<String, User>();
		
		if(list != null){
			for (int i = 0; i < list.size(); i++) {  
				Map<String, Object> map = list.get(i);  
                Set<String> set = map.keySet();  
		        User user = new User();
                for (Iterator<String> it = set.iterator(); it.hasNext();) {  
		          String key = it.next();  
		          System.out.println(key + ":" + map.get(key)); 
		          if(key.equals("AppID")){user.setUsername((String) map.get(key)); }
		          else if(key.equals("NickName"))  user.setNickname((String) map.get(key));
		          else if(key.equals("studentID")) user.setStuId((String) map.get(key));
		          else if(key.equals("Email")) user.setEmail((String) map.get(key));
		          else if(key.equals("Sex")) user.setSex((String) map.get(key));
		         
		        } 
//                System.out.println("username---:"+user.getUsername());
                setUserHead(user.getUsername(), user);
                return user;
		     }
		}
		return null;
	}
	
	/***
	 * 好友变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				//从服务器获取联系人的信息列表
				User user = getContact(username);
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
//			mContactView.refresh();
		}

		
		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					//如果正在与此用户的聊天页面
					if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
						Toast.makeText(DesktopActivity.this, ChatActivity.activityInstance.getToChatUsername()+"已把你从他好友列表里移除", 1).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
				}
			});
			// 刷新ui
//			if (currentTabIndex == 1)
//				contactListFragment.refresh();

//			mContactView.refresh();
		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
//			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

			// 刷新ui
//			mContactView.refresh();
		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			
		}

	}
	
	
	/**
	 * 连接监听listner
	 */
	class MyConnectionListener implements ConnectionListener{

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnecting(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")){
				//显示账号在其他设备登陆
				showConflictDialog();
			}else {
//				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
//				if(NetUtils.hasNetwork(DesktopActivity.this))
//					chatHistoryFragment.errorText.setText("连接不到聊天服务器");
//				else
//					chatHistoryFragment.errorText.setText("当前网络不可用，请检查网络设置");
					
			}
		}

		@Override
		public void onReConnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReConnecting() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
			boolean hasGroup = false;
			for(EMGroup group : EMGroupManager.getInstance().getAllGroups()){
				if(group.getGroupId().equals(groupId)){
					hasGroup = true;
					break;
				}
			}
			if(!hasGroup)
				return;
			
			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
//					// 刷新ui
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
//					try {
//						updateUnreadLabel();
//						if (currentTabIndex == 0)
//							chatHistoryFragment.refresh();
//						if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//							GroupsActivity.instance.onResume();
//						}
//					} catch (Exception e) {
//						Log.e("###", "refresh exception " + e.getMessage());
//					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			//加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
			
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
			//加群申请被拒绝，demo未实现
		}

	}

	
	
	
	
	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
//		if (currentTabIndex == 1)
//			mContactView.refresh();
	}
	/**
	 * 保存邀请等msg
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}
	
	
	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
//					unreadAddressLable.setText(String.valueOf(count));
//					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
//					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(offlineMessageReceiver);
		} catch (Exception e) {
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

	}
	
	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		MyApplication.getInstance().logout();
		if (!DesktopActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					System.out.println("hrerere-------");
					conflictBuilder = new android.app.AlertDialog.Builder(DesktopActivity.this);
					conflictBuilder.setTitle("下线通知");
					conflictBuilder.setMessage(R.string.connect_conflict);
					conflictBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(DesktopActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###", "---------color conflictBuilder error" + e.getMessage());
			}
		}
	}
	
	
	/**
	 * set head
	 * @param username
	 * @return
	 */
	User setUserHead(String username , User user) {
//		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNickname())) {
			headerName = user.getNickname();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(
					0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isConflict){
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
			chatHistoryView.refresh();
		}
		
		
	}
	
//    public void refresh(){
//    	finish();
//    	Intent intent = new Intent(DesktopActivity.this , DesktopActivity.class);
//    	startActivity(intent);
//    }
    
    
    long waitTime = 2000;
	long touchTime = 0;
    
    @Override
    public void onBackPressed() {
    	long currentTime = System.currentTimeMillis();// 退出功能
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
			finish();
		}
    }
    
    
    @Override
	public void open() {
		// TODO Auto-generated method stub
		if (mRoot.getScreenState() == FlipperLayout1.SCREEN_STATE_CLOSE) {
			mRoot.open();
		}
	}
	
	
    public void setOnOpenListner(OnOpenListener onOpenListener){
    	mOnOpenListener = onOpenListener;
    }
    
//    public static View getCommView(){
////    	return mCommu.getView();
//    }
    
    public static FlipperLayout1 getRoot(){
    	return mRoot;
    }
    
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow)
			showConflictDialog();
	}

}
