//package com.project.personal;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.DialogInterface.OnCancelListener;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.EMCallBack;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMGroupManager;
//import com.easemob.util.EMLog;
//import com.project.R;
//import com.project.activity.DesktopActivity;
//import com.project.activity.RegisterActivity;
//import com.project.activity.base.FlipperLayout.OnOpenListener;
//import com.project.app.MyApplication;
//import com.project.constane.Constant;
//import com.project.domain.User;
//import com.project.domain.UserDao;
//import com.project.util.HttpUtil;
//
//public class Personal implements OnClickListener{
//	
//	private Activity mActivity;
//	private Context mContext;
//	
//	private View mLoginView;
//	private View mPersonal;
//	
//	private FrameLayout mFrameLayout;
//	
//	private ShowUserInfo mShowUserInfo ; 
//	
//	private Button btn_register;
//	private Button btn_login;
//	private Button mFlip;
//	private TextView tv_title;
//	private TextView tv_reg;
//	private EditText et_username;
//	private EditText et_pwd;
//	
//	private String username ;
//	private boolean progressShow;
//	private boolean isRun = true;
//	
//	private OnOpenListener mOnOpenListener;
//	
//	public Personal(Activity mActivity , Context mContext , String username){
//		this.mActivity = mActivity;
//		this.mContext = mContext;
//		mPersonal = LayoutInflater.from(mContext).inflate(R.layout.personal, null);
//		mFrameLayout = (FrameLayout) mPersonal.findViewById(R.id.personal_layout);
//		
//		tv_reg = (TextView) mPersonal.findViewById(R.id.tv_reg);
//		tv_title = (TextView) mPersonal.findViewById(R.id.tv_title);
//		mFlip = (Button) mPersonal.findViewById(R.id.btn_goto_residenmenu);
//		
////		username = MyApplication.getInstance().getUserName();
//		
//		//����ǰ��¼���û���Ϊ�� �� Ϊ��ʾ�û���¼�Ľ���
//		if(username==null){
//			mLoginView = LayoutInflater.from(mContext).inflate(R.layout.login, null);
//			tv_reg.setVisibility(View.VISIBLE);
//			tv_title.setVisibility(View.GONE);
//			findViewById();
//			changeView(mLoginView);
//			
//			setListner();
//		}else{
//			mShowUserInfo = new ShowUserInfo(mActivity, mContext, username);
//			tv_reg.setVisibility(View.GONE);
//			tv_title.setVisibility(View.VISIBLE);
//			
//			changeView(mShowUserInfo.getView());
//		}
//	}
//	
//
//	
//	
//	public void findViewById(){
//		btn_register = (Button) mLoginView.findViewById(R.id.btn_reg);
//		btn_login = (Button) mLoginView.findViewById(R.id.btn_login);
//		et_username = (EditText) mLoginView.findViewById(R.id.et_username);
//		et_pwd = (EditText) mLoginView.findViewById(R.id.et_password);
//	}
//	
//	public void setListner(){
//		btn_register.setOnClickListener(this);
//		btn_login.setOnClickListener(this);
//		mFlip.setOnClickListener(this);
//		
//	}
//	
//	
//	public void changeView(View view){
//		mFrameLayout.removeAllViews();
//		mFrameLayout.addView(view);
//	}
//	
//	public View getView(){
//		return mPersonal;
//	}
//	
//	public void setOnOpenListener(OnOpenListener onOpenListener) {
//		mOnOpenListener = onOpenListener;
//	}
//
//	public void test(final String username , final String password){
////		MyApplication.currentUserNick=data.getStringExtra("edittext");
//		
////		final String username = et_username.getText().toString();
////		final String password = et_pwd.getText().toString();
////		
//		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//			progressShow = true;
//			final ProgressDialog pd = new ProgressDialog(mActivity);
//			pd.setCanceledOnTouchOutside(false);
//			pd.setOnCancelListener(new OnCancelListener() {
//
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					progressShow = false;
//				}
//			});
//			pd.setMessage("���ڵ�½...");
//			pd.show();
//			// ����sdk��½������½���������
//			EMChatManager.getInstance().login(username, password, new EMCallBack() {
//
//				@Override
//				public void onSuccess() {
//					if (!progressShow) {
//						return;
//					}
//					// ��½�ɹ��������û�������
//					MyApplication.getInstance().setUserName(username);
//					MyApplication.getInstance().setPassword("123");
//					mActivity.runOnUiThread(new Runnable() {
//						public void run() {
//							pd.setMessage("���ڻ�ȡ���Ѻ�Ⱥ���б�...");
//						}
//					});
//					try {
//						// demo�м򵥵Ĵ����ÿ�ε�½��ȥ��ȡ����username���������Լ������������
//						List<String> usernames = EMChatManager.getInstance().getContactUserNames();
//						Map<String, User> userlist = new HashMap<String, User>();
//						for (String username : usernames) {
//							User user = new User();
//							user.setUsername(username);
////							setUserHearder(username, user);
//							userlist.put(username, user);
//						}
//						// ���user"������֪ͨ"
//						User newFriends = new User();
//						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//						newFriends.setNick("������֪ͨ");
//						newFriends.setHeader("");
//						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//						// ���"Ⱥ��"
//						User groupUser = new User();
//						groupUser.setUsername(Constant.GROUP_USERNAME);
//						groupUser.setNick("Ⱥ��");
//						groupUser.setHeader("");
//						userlist.put(Constant.GROUP_USERNAME, groupUser);
//
//						// �����ڴ�
//						MyApplication.getInstance().setContactList(userlist);
//						// ����db
//						UserDao dao = new UserDao(mActivity);
//						List<User> users = new ArrayList<User>(userlist.values());
//						dao.saveContactList(users);
//
//						// ��ȡȺ���б�(Ⱥ����ֻ��groupid��groupname�ļ���Ϣ),sdk���Ⱥ����뵽�ڴ��db��
//						EMGroupManager.getInstance().getGroupsFromServer();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
////							boolean updatenick = EMChatManager.getInstance()
////									.updateCurrentUserNick(MyApplication.currentUserNick);
////							if (!updatenick) {
////								EMLog.e("LoginActivity",
////										"update current user nick fail");
////							}
//
////					if (!LoginActivity.this.isFinishing())
//						pd.dismiss();
//					// ������ҳ��
////					startActivity(new Intent(LoginActivity.this, MainActivity.class));
////					mActivity.finish();
//				}
//
//				@Override
//				public void onProgress(int progress, String status) {
//
//				}
//
//				@Override
//				public void onError(int code, final String message) {
//					if (!progressShow) {
//						return;
//					}
//					mActivity.runOnUiThread(new Runnable() {
//						public void run() {
//							pd.dismiss();
//							Toast.makeText(mContext, "��¼ʧ��: " + message, 0).show();
//
//						}
//					});
//				}
//			});
//		}
//	}
//	
//	
//	public void login(final String userName , final String password , final ProgressDialog pd){
//		System.out.println("ttttt");
//		
//		// ����sdk��½������½���������
//		EMChatManager.getInstance().login(username, password, new EMCallBack() {
//
//			@Override
//			public void onSuccess() {
//				System.out.println("test!!!!!!!!!!!!!!!---onSuccess");
//				if (!progressShow) {
//					return;
//				}
//				// ��½�ɹ��������û�������
//				MyApplication.getInstance().setUserName(username);
//				MyApplication.getInstance().setPassword(password);
//				mActivity.runOnUiThread(new Runnable() {
//					public void run() {
//						pd.setMessage("���ڻ�ȡ���Ѻ�Ⱥ���б�...");
//					}
//				});
//				try {
//					// demo�м򵥵Ĵ����ÿ�ε�½��ȥ��ȡ����username���������Լ������������
////					List<String> usernames = EMChatManager.getInstance().getContactUserNames();
////					Map<String, User> userlist = new HashMap<String, User>();
////					for (String username : usernames) {
////						User user = new User();
////						user.setUsername(username);
//////						setUserHearder(username, user);
////						userlist.put(username, user);
////					}
////					System.out.println("test!!!!!!!!!!!!!!!");
////					// ���user"������֪ͨ"
////					User newFriends = new User();
////					newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
////					newFriends.setNick("������֪ͨ");
////					newFriends.setHeader("");
////					userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//					// ���"Ⱥ��"
////					User groupUser = new User();
////					groupUser.setUsername(Constant.GROUP_USERNAME);
////					groupUser.setNick("Ⱥ��");
////					groupUser.setHeader("");
////					userlist.put(Constant.GROUP_USERNAME, groupUser);
//
//					// �����ڴ�
////					MyApplication.getInstance().setContactList(userlist);
//					// ����db
////					UserDao dao = new UserDao(mActivity);
////					List<User> users = new ArrayList<User>(userlist.values());
////					dao.saveContactList(users);
//
//					// ��ȡȺ���б�(Ⱥ����ֻ��groupid��groupname�ļ���Ϣ),sdk���Ⱥ����뵽�ڴ��db��
////					EMGroupManager.getInstance().getGroupsFromServer();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
////							boolean updatenick = EMChatManager.getInstance()
////									.updateCurrentUserNick(MyApplication.currentUserNick);
////							if (!updatenick) {
////								EMLog.e("LoginActivity",
////										"update current user nick fail");
////							}
//
//				
////						pd.dismiss();
//				// ������ҳ��
////					startActivity(new Intent(LoginActivity.this, MainActivity.class));
//					mActivity.finish();
//			}
//
//			@Override
//			public void onProgress(int progress, String status) {
//				System.out.println("test!!!!!!!!!!!!!!!-----onPro");
//			}
//
//			@Override
//			public void onError(int code, final String message) {
//				System.out.println("test!!!!!!!!!!!!!!!-----error");
//				if (!progressShow) {
//					return;
//				}
//				mActivity.runOnUiThread(new Runnable() {
//					public void run() {
//						pd.dismiss();
//						Toast.makeText(mContext, "��¼ʧ��: " + message, 0).show();
//
//					}
//				});
//			}
//		});
//	}
//	
//	public void refresh(){
//    	mActivity.finish();
//    	Intent intent = new Intent(mActivity , DesktopActivity.class);
//    	mActivity.startActivity(intent);
//    }
//
//	@Override
//	public void onClick(View view) {
//		switch(view.getId()){
//		case R.id.btn_reg:
////			System.out.println("test..................");
//			Intent intent = new Intent(mActivity , RegisterActivity.class);
////			mActivity.startActivity(intent);
//			mActivity.startActivityForResult(intent, RegisterActivity.REQUEST_CODE_REGISTER_SUCCESS);
//			break;
//		case R.id.btn_login:
//			
//			final String username = et_username.getText().toString().trim();
//			final String pwd = et_pwd.getText().toString().trim();
////			
////			if (TextUtils.isEmpty(username)) {
////				Toast.makeText(mContext, "�û�������Ϊ�գ�", Toast.LENGTH_SHORT).show();
////				et_username.requestFocus();
////				break;
////			} else if (TextUtils.isEmpty(pwd)) {
////				Toast.makeText(mContext, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
////				et_pwd.requestFocus();
////				break;
////			} 
////			
////			final ProgressDialog pd = new ProgressDialog(mActivity);
////			pd.setCanceledOnTouchOutside(false);
////			pd.setOnCancelListener(new OnCancelListener() {
////
////				@Override
////				public void onCancel(DialogInterface dialog) {
////					progressShow = false;
////				}
////			});
////			pd.setMessage("���ڵ�½...");
////			pd.show();
//			
////			Thread thread = new Thread(){
////				@Override
////				public void run() {
////					
////					while(isRun){
////						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
////						params.add(new BasicNameValuePair("appID",username));
////						params.add(new BasicNameValuePair("password", pwd));
////						
////						String url = "http://1.nodistanceservice.sinaapp.com/Login.php"; 
////						
////						String result = null;
////						
////						result = HttpUtil.queryStringForPost(url, params);
////						Log.i("main", "result = " +result); //��ȡ��Ӧ����  
////						
////						if(result == "1" || result.equals("1")){
////							isRun = false;
////							System.out.println("test --" + isRun);
////							
////							
//////							MyApplication.getInstance().setUserName(username);
//////							refresh();
////						}
////					}
////					destroy();
////				}
////			};
////			
////			thread.start();
//////			if(!isRun){
//////				System.out.println("des");
//////				thread.destroy();
//////			}
////			login(username , pwd , pd);		//��½����
//			test(username , pwd);
//			break;
//		case R.id.btn_goto_residenmenu:
//			if (mOnOpenListener != null) {
//				mOnOpenListener.open();
//			}
//			break;
//		}
//	}
//}
