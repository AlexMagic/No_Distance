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
    	
    	// ע��һ��������Ϣ��BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
    	
		// ע��һ��������Ϣ��BroadcastReceiver
		IntentFilter offlineMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getOfflineMessageBroadcastAction());
		registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);
		
		// setContactListener������ϵ�˵ı仯��
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// ע��һ����������״̬��listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// ֪ͨsdk��UI �Ѿ���ʼ����ϣ�ע������Ӧ��receiver��listener, ���Խ���broadcast��
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
//					Toast.makeText(DesktopActivity.this, "���޿�ͨ", Toast.LENGTH_SHORT).show();
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
	 * ��ȡδ��������֪ͨ��Ϣ
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
	 * ��ȡδ����Ϣ��
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
	 * ˢ��δ����Ϣ��
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
	 * ����Ϣ�㲥������
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//��ҳ���յ���Ϣ����ҪΪ����ʾδ����ʵ����Ϣ������Ҫ��chatҳ��鿴
			
			// ��Ϣid
			String msgId = intent.getStringExtra("msgid");
			// �յ�����㲥��ʱ��message�Ѿ���db���ڴ����ˣ�����ͨ��id��ȡmesage����
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
	        builder.setContentTitle("δ����Ϣ").setContentText("���Ժ��ѣ�"+message.getFrom()).setSmallIcon(R.drawable.logo_icon).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);
	        
	        manager.notify(0, builder.build());
			 
//			System.out.println("message"+message.toString());
			// ˢ��bottom bar��Ϣδ����
			updateUnreadLabel(); 
			chatHistoryView.refresh();
			mRoot.close(mRoot.getChildAt(1));
			
			// ע���㲥��������ChatActivity�л��յ�����㲥
			abortBroadcast();
		}
	}
	
	
	
	
	/**
	 * ������ϢBroadcastReceiver
	 * sdk ��¼�󣬷�����������������Ϣ��client�����receiver����֪ͨUI ����Щ�˷�����������Ϣ
	 * UI ��������Ӧ�Ĳ��������������û���Ϣ
	 */
	private BroadcastReceiver offlineMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String[] users = intent.getStringArrayExtra("fromuser");
			String[] groups = intent.getStringArrayExtra("fromgroup");
			if (users != null) {
				for (String user : users) {
					System.out.println("�յ�user������Ϣ��" + user);
				}
			}
			if (groups != null) {
				for (String group : groups) {
					System.out.println("�յ�group������Ϣ��" + group);
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
	 * ���ѱ仯listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// �������ӵ���ϵ��
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				//�ӷ�������ȡ��ϵ�˵���Ϣ�б�
				User user = getContact(username);
				// ��ʱ�и�bug����Ӻ���ʱ���ܻ�ص�added��������
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// ˢ��ui
//			mContactView.refresh();
		}

		
		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// ��ɾ��
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					//�����������û�������ҳ��
					if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
						Toast.makeText(DesktopActivity.this, ChatActivity.activityInstance.getToChatUsername()+"�Ѱ�����������б����Ƴ�", 1).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
				}
			});
			// ˢ��ui
//			if (currentTabIndex == 1)
//				contactListFragment.refresh();

//			mContactView.refresh();
		}

		@Override
		public void onContactInvited(String username, String reason) {
			// �ӵ��������Ϣ�����������(ͬ���ܾ�)�����ߺ󣬷��������Զ��ٷ����������Կͻ��˲�Ҫ�ظ�����
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
//			// �Լ���װ��javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "�������Ϊ����,reason: " + reason);
			// ������Ӧstatus
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

			// ˢ��ui
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
			// �Լ���װ��javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "ͬ������ĺ�������");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// �ο�ͬ�⣬������ʵ�ִ˹���,demoδʵ��
			
		}

	}
	
	
	/**
	 * ���Ӽ���listner
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
				//��ʾ�˺��������豸��½
				showConflictDialog();
			}else {
//				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
//				if(NetUtils.hasNetwork(DesktopActivity.this))
//					chatHistoryFragment.errorText.setText("���Ӳ������������");
//				else
//					chatHistoryFragment.errorText.setText("��ǰ���粻���ã�������������");
					
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
			
			// ������
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "�����������Ⱥ��"));
			// ����������Ϣ
			EMChatManager.getInstance().saveMessage(msg);
			// ��������Ϣ
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
//					// ˢ��ui
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
			// ��ʾ�û���T�ˣ�demoʡ�Դ˲���
			// ˢ��ui
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
			// Ⱥ����ɢ
			// ��ʾ�û�Ⱥ����ɢ,demoʡ��
			// ˢ��ui
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
			// �û��������Ⱥ��
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " �������Ⱥ�ģ�" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			//��Ⱥ���뱻ͬ��
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "ͬ�������Ⱥ������"));
			// ����ͬ����Ϣ
			EMChatManager.getInstance().saveMessage(msg);
			// ��������Ϣ
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
			
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// ˢ��ui
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
			//��Ⱥ���뱻�ܾ���demoδʵ��
		}

	}

	
	
	
	
	/**
	 * ������ʾ����Ϣ
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// ��ʾ������Ϣ
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// ˢ��bottom bar��Ϣδ����
		updateUnreadAddressLable();
		// ˢ�º���ҳ��ui
//		if (currentTabIndex == 1)
//			mContactView.refresh();
	}
	/**
	 * ���������msg
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// ����msg
		inviteMessgeDao.saveMessage(msg);
		// δ������1
		User user = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}
	
	
	/**
	 * ˢ��������֪ͨ��Ϣ��
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
		// ע���㲥������
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
	 * ��ʾ�ʺ��ڱ𴦵�¼dialog
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
					conflictBuilder.setTitle("����֪ͨ");
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
    	long currentTime = System.currentTimeMillis();// �˳�����
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
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
