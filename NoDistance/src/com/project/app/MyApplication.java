package com.project.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.chat.EMMessage.ChatType;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.project.activity.DesktopActivity;
import com.project.communicate.ChatActivity;
import com.project.db.DBHelper;
import com.project.db.UserDao;
import com.project.domain.User;
import com.project.maps.BottleItem;
import com.project.util.PreferenceUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyApplication extends Application {
	
	private static  Context applicationContext;
	private static MyApplication instance;
	//login user name 
	public static final String PREF_USERNAME = "username";
	private String userName = null;
	//login password
	private static final String PREF_PWD = "pwd";
	private String pwd = null;
	//user nick
	private static final String PREF_NICKNAME = "nickname";
	private String nickname;
	
	//user sex
	private static final String PRE_SEX = "sex";
	private String sex;
	
	private Map<String , User> contactList;
	
	public static MyApplication getInstance(){
		if(instance == null)
			instance = new MyApplication();
		
		return instance;
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		System.out.println("processAppName------:"+processAppName);
		//���ʹ�õ��ٶȵ�ͼ������������remote service�ĵ������⣬���if�жϲ�����
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk 
			// �ٶȶ�λsdk����λ����������һ�������Ľ��̣�ÿ�ζ�λ����������ʱ�򣬶������application::onCreate
			// �����µĽ��̡�
			// �����ŵ�sdkֻ��Ҫ���������г�ʼ��һ�Ρ� ������⴦���ǣ������pid �Ҳ�����Ӧ��processInfoprocessName��
			// ���application::onCreate �Ǳ�service ���õģ�ֱ�ӷ���
			return;
		}
		applicationContext = this;
		instance = this;
		// ��ʼ������SDK,һ��Ҫ�ȵ���init()
		Log.d("EMChat Demo", "initialize EMChat SDK");
		EMChat.getInstance().init(applicationContext);
		SDKInitializer.initialize(applicationContext); 
		// debugmode��Ϊtrue�󣬾��ܿ���sdk��ӡ��log��
		EMChat.getInstance().setDebugMode(true);

		// ��ȡ��EMChatOptions����
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// Ĭ����Ӻ���ʱ���ǲ���Ҫ��֤�ģ��ĳ���Ҫ��֤
		options.setAcceptInvitationAlways(false);
		// �����յ���Ϣ�Ƿ�������Ϣ֪ͨ��Ĭ��Ϊtrue
		options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
		// �����յ���Ϣ�Ƿ���������ʾ��Ĭ��Ϊtrue
		options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
		// �����յ���Ϣ�Ƿ��� Ĭ��Ϊtrue
		options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
		// ����������Ϣ�����Ƿ�����Ϊ���������� Ĭ��Ϊtrue
		options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());
		
		//����notification��Ϣ���ʱ����ת��intentΪ�Զ����intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {
			
			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if(chatType == ChatType.Chat){ //������Ϣ
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				}else{ //Ⱥ����Ϣ
					//message.getTo()ΪȺ��id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		//����һ��connectionlistener�����˻��ظ���½
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		
		//ȡ��ע�ͣ�app�ں�̨��������Ϣ��ʱ��״̬������Ϣ��ʾ�����Լ�д��
		options.setNotifyText(new OnMessageNotifyListener() {
			
			@Override
			public String onNewMessageNotify(EMMessage message) {
				//���Ը���message��������ʾ��ͬ���֣�demo�򵥵ĸ�����ԭ������ʾ
				return "��ĺ�����" + message.getFrom() + "������һ����ϢŶ";
			}
			
			@Override
			public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
				return fromUsersNum + "�����ѣ�����" + messageNum + "����Ϣ";
			}
		});
		
		
		MobclickAgent.onError(applicationContext);
		
		initImageLoader(applicationContext);
	}
	
	/**
	 * ��ʼ��ImageLoader
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * ��ȡ�ڴ��е�ֽ������
	 * 
	 */
	
	public ArrayList<BottleItem> getBottleList(){
		
		return null;
	}
	
	
	/**
	 * ��ȡ�ڴ��к���user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
//			// ��ȡ���غ���user list���ڴ�,�����Ժ��ȡ����list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * ���ú���user list���ڴ���
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	public void setStrangerList(Map<String, User> List) {

	}

	/**
	 * ��ȡ��ǰ��½�û���
	 * 
	 * @return
	 */
	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PREF_USERNAME, null);
		}
		return userName;
	}
	
	/**
	 * ��ȡ��ǰ�û���nick
	 * @return
	 */
	public String getNickName(){
		if(nickname==null){
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PREF_NICKNAME, null);
		}
		return nickname;
	}

	
	/**
	 * ��ȡ��ǰ�û���nick
	 * @return
	 */
	public String getSex(){
		if(sex==null){
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PRE_SEX, null);
		}
		return sex;
	}
	
	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public String getPassword() {
		if (pwd == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			pwd = preferences.getString(PREF_PWD, null);
		}
		return pwd;
	}

	/**
	 * �����û���
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, username).commit()) {
				userName = username;
			}
		}
	}
	
	/**
	 * ����nick
	 */
	public void setNickName(String nickname){
		if(nickname != null){
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if(editor.putString(PREF_NICKNAME, nickname).commit()){
				this.nickname = nickname;
			}
		}
	}
	
	/**
	 * ����sex
	 */
	public void setSex(String sex){
		if(nickname != null){
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if(editor.putString(PRE_SEX, sex).commit()){
				this.sex = sex;
			}
		}
	}

	/**
	 * �������� �����ʵ������ ֻ��demo��ʵ�ʵ�Ӧ������Ҫ��password ���ܺ���� preference ����sdk
	 * �ڲ����Զ���¼��Ҫ�����룬�Ѿ����ܴ洢��
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_PWD, pwd).commit()) {
			this.pwd = pwd;
		}
	}

	/**
	 * �˳���¼,�������
	 */
	public void logout() {
		// �ȵ���sdk logout��������app���Լ�������
		EMChatManager.getInstance().logout();
		DBHelper.getInstance(applicationContext).closeDB();
		// reset password to null
		setPassword(null);
		setContactList(null);
		
	}

	private String getAppName(int pid) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List list = am.getRunningAppProcesses();
		
		PackageManager pm = this.getPackageManager();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			RunningAppProcessInfo info = (RunningAppProcessInfo) iterator.next();
			
				try {
					if(info.pid == pid){
						CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
						processName = info.processName;
						
						return processName;
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return processName;
	}

	
	
	
	//���ü����˻��ظ���¼
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
			// TODO Auto-generated method stub
			if(errorString != null && errorString.contains("conflict")){
				System.out.println("--------conflict");
				Intent intent = new Intent(applicationContext, DesktopActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("conflict", true);
				startActivity(intent);
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
	
}
