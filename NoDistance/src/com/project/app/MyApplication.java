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
		//如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk 
			// 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfoprocessName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		applicationContext = this;
		instance = this;
		// 初始化环信SDK,一定要先调用init()
		Log.d("EMChat Demo", "initialize EMChat SDK");
		EMChat.getInstance().init(applicationContext);
		SDKInitializer.initialize(applicationContext); 
		// debugmode设为true后，就能看到sdk打印的log了
		EMChat.getInstance().setDebugMode(true);

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());
		
		//设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {
			
			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if(chatType == ChatType.Chat){ //单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				}else{ //群聊信息
					//message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		//设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		
		//取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		options.setNotifyText(new OnMessageNotifyListener() {
			
			@Override
			public String onNewMessageNotify(EMMessage message) {
				//可以根据message的类型提示不同文字，demo简单的覆盖了原来的提示
				return "你的好丽友" + message.getFrom() + "发来了一条消息哦";
			}
			
			@Override
			public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
				return fromUsersNum + "个好友，发来" + messageNum + "条消息";
			}
		});
		
		
		MobclickAgent.onError(applicationContext);
		
		initImageLoader(applicationContext);
	}
	
	/**
	 * 初始化ImageLoader
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
	 * 获取内存中的纸条内容
	 * 
	 */
	
	public ArrayList<BottleItem> getBottleList(){
		
		return null;
	}
	
	
	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
//			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	public void setStrangerList(Map<String, User> List) {

	}

	/**
	 * 获取当前登陆用户名
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
	 * 获取当前用户的nick
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
	 * 获取当前用户的nick
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
	 * 获取密码
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
	 * 设置用户名
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
	 * 设置nick
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
	 * 设置sex
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
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
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
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
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

	
	
	
	//设置监听账户重复登录
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
