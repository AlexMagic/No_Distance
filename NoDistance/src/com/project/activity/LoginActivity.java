package com.project.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.project.R;
import com.project.app.MyApplication;
import com.project.constane.Constant;
import com.project.db.UserDao;
import com.project.domain.User;
import com.project.util.CommonUtils;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;

public class LoginActivity extends Activity implements OnClickListener{
	
	private EditText mEditUsername;
	private EditText mEditPassword;
	private Button btn_reg;
	private Button btn_login;
	
	
	private List<LinkedHashMap<String, Object>> list;
	private List<LinkedHashMap<String, Object>> curUserList;
	private Map<String, User> userlist ;
	private boolean isRun = true;
	private boolean progressShow = true;
	
	private InputMethodManager manager;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case -1:
				
				Toast.makeText(getApplicationContext(), "�û������������ �� ����������", Toast.LENGTH_LONG).show();
				break;
			}
			
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		
//		if(MyApplication.getInstance().getUserName()!=null && MyApplication.getInstance().getPassword()!=null){
////			System.out.println("username-----:"+MyApplication.getInstance().getUserName());
////			System.out.println("pwd-----:"+ MyApplication.getInstance().getPassword());
//			startActivity(new Intent(this,DesktopActivity.class));
//			finish();
//		}
		
		setUpView();
		setListner();
		
	}

	private void setUpView() {
		mEditUsername = (EditText) findViewById(R.id.et_username);
		mEditPassword = (EditText) findViewById(R.id.et_password);
		btn_reg = (Button) findViewById(R.id.btn_reg);
		btn_login = (Button) findViewById(R.id.btn_login);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void setListner() {
		btn_reg.setOnClickListener(this);
		btn_login.setOnClickListener(this);
	}
	
	/**
	 * ��½
	 */
	public void login(final String username , final String password  , final String nickname , final String sex, final ProgressDialog pd){
		if(!CommonUtils.isNetWorkConnected(this)){
			Toast.makeText(getApplicationContext(), "�������������� �� ����", Toast.LENGTH_LONG).show();
			return ;
		}
		
		new Thread(){
			public void run() {
				
				//���������ȡ�����б�
				list = getFrdMap(username);
				userlist = new HashMap<String, User>();
//				System.out.println("listNum----"+list.size());
				if(list != null){
					for (int i = 0; i < list.size(); i++) {  
						Map<String, Object> map = list.get(i);  
		                Set<String> set = map.keySet();  
				        User user = new User();
		                for (Iterator<String> it = set.iterator(); it.hasNext();) {  
				          String key = it.next();  
//				          System.out.println(key + ":" + map.get(key)); 
				          if(key.equals("AppID")){user.setUsername((String) map.get(key)); }
				          else if(key.equals("NickName")){user.setNickname((String) map.get(key));}
				          else if(key.equals("studentID")) {user.setStuId((String) map.get(key));}
				          else if(key.equals("Email")) user.setEmail((String) map.get(key));
				          else if(key.equals("Sex")) user.setSex((String) map.get(key));
				        } 
//		                System.out.println("username---:"+user.getUsername());
				        setUserHearder(user.getUsername(), user);
//		                System.out.println(userlist.size());
		                userlist.put(user.getUsername(), user);
				     }
				}
				
				// ����sdk��½������½���������
				EMChatManager.getInstance().login(username, password, new EMCallBack() {

					@Override
					public void onSuccess() {
						
						System.out.println("onSuccess...");
						if (!progressShow) {
							System.out.println("return....");
							return;
						}
						// ��½�ɹ��������û�������
						MyApplication.getInstance().setUserName(username);
						MyApplication.getInstance().setPassword(password);
						MyApplication.getInstance().setNickName(nickname);
						MyApplication.getInstance().setSex(sex);
						
						
						runOnUiThread(new Runnable() {
							public void run() {
								pd.setMessage("���ڻ�ȡ���Ѻ�Ⱥ���б�...");
							}
						});
						try {
							// ���user"������֪ͨ"
							User newFriends = new User();
							newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
							newFriends.setNick("������֪ͨ");
							newFriends.setHeader("");
							userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
							// ���"Ⱥ��"
							User groupUser = new User();
							groupUser.setUsername(Constant.GROUP_USERNAME);
							groupUser.setNick("Ⱥ��");
							groupUser.setHeader("");
							userlist.put(Constant.GROUP_USERNAME, groupUser);
//							System.out.println("userNum---------:"+userlist.size());
							// �����ڴ�
							MyApplication.getInstance().setContactList(userlist);
							// ����db
							UserDao dao = new UserDao(LoginActivity.this);
							List<User> users = new ArrayList<User>(userlist.values());
							dao.saveContactList(users);

							// ��ȡȺ���б�(Ⱥ����ֻ��groupid��groupname�ļ���Ϣ),sdk���Ⱥ����뵽�ڴ��db��
							EMGroupManager.getInstance().getGroupsFromServer();
						} catch (Exception e) {
							e.printStackTrace();
						}
//											boolean updatenick = EMChatManager.getInstance()
//													.updateCurrentUserNick(MyApplication.currentUserNick);
//											if (!updatenick) {
//												EMLog.e("LoginActivity",
//														"update current user nick fail");
//											}

						if (!LoginActivity.this.isFinishing())
							pd.dismiss();
						// ������ҳ��
						startActivity(new Intent(LoginActivity.this, DesktopActivity.class));
						finish();
					}

					@Override
					public void onProgress(int progress, String status) {
						System.out.println("onProgress...");
					}

					@Override
					public void onError(int code, final String message) {
						System.out.println("onError...:"+message);
						
						if (!progressShow) {
							return;
						}
						runOnUiThread(new Runnable() {
							public void run() {
								pd.dismiss();
								Toast.makeText(getApplicationContext(), "��¼ʧ��: " + message, 0).show();
								
							}
						});
					}
				});
			};
		}.start();
	}
	
	

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_login:
			
			hideKeyboard();
			final String username = mEditUsername.getText().toString().trim();
			final String password = mEditPassword.getText().toString().trim();
			
			if (TextUtils.isEmpty(username)) {
				Toast.makeText(this, "�û�������Ϊ�գ�", Toast.LENGTH_SHORT).show();
				mEditUsername.requestFocus();
				break;
			} else if (TextUtils.isEmpty(password)) {
				Toast.makeText(this, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
				mEditPassword.requestFocus();
				break;
			} 
			
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});
			pd.setMessage("���ڵ�½...");
			pd.show();
			
			
			
			new Thread(){
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					
					while(isRun){
						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
						params.add(new BasicNameValuePair("appID",username));
						params.add(new BasicNameValuePair("password", password));
						
						String url = "http://1.nodistanceservice.sinaapp.com/Login.php"; 
						
						String result = null;
						
						result = HttpUtil.queryStringForPost(url, params);
						Log.i("main", "result = " +result); //��ȡ��Ӧ����  
						System.out.println("result----:"+result);
						if(result!="-1" && !"-1".equals(result) ){
							isRun = false;
							
//							curUserList = new ArrayList<LinkedHashMap<String, Object>>();
							curUserList = JsonUtil.json2List(result);
							//Map<String , User> map = new HashMap<String, User>();
//							System.out.println(curUserList==null);
							Map<String, Object> temp = curUserList.get(0);  
			                Set<String> set = temp.keySet();  
					        User user = new User();
			                for (Iterator<String> it = set.iterator(); it.hasNext();) {  
					          String key = it.next();  
//					          System.out.println(key + ":" + map.get(key)); 
					          if(key.equals("AppID")){user.setUsername((String) temp.get(key)); }
					          else if(key.equals("NickName")){user.setNickname((String) temp.get(key));}
					          else if(key.equals("studentID")) {user.setStuId((String) temp.get(key));}
					          else if(key.equals("Email")) user.setEmail((String) temp.get(key));
					          else if(key.equals("Sex")) {user.setSex((String) temp.get(key));};
					        } 
							String nickname = user.getNickname();
							String sex = user.getSex();
							System.out.println("nickname---:"+nickname);
							System.out.println("sex-----:" + sex);
							MyApplication.getInstance().setUserName(username);
							MyApplication.getInstance().setPassword(password);
							MyApplication.getInstance().setSex(sex);
							MyApplication.getInstance().setNickName(nickname);
							
							
							login(username , password ,nickname , sex, pd);
							
							break;
//							refresh();
						}else{
							System.out.println("hhhh");
							Message msg = new Message();
							msg.what = -1;
							pd.dismiss();
							handler.sendMessage(msg);
//							this.destroy();
							break;
						}
					}
					
//					if(isRun){
//						this.destroy();
//					}
				}
			}.start();
			
//			login(username , password , pd);
			
			break;
		case R.id.btn_reg:
			Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.getInstance().getUserName() != null) {
			mEditUsername.setText(MyApplication.getInstance().getUserName());
		}
	}
	
	
	/**
	 * ���������
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	public List<LinkedHashMap<String, Object>> getFrdMap(String username){
		
		List<LinkedHashMap<String, Object>> tempList ;
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("appID",username));
		
		String url = "http://1.nodistanceservice.sinaapp.com/getUserData.php";
		String result = HttpUtil.queryStringForPost(url, params);
		System.out.println("result----getFrdMap-:"+result);
		
		if(result==null){
			return null;
		}
		
		tempList = JsonUtil.json2List(result);
		
		return tempList;
	}
	
	/**
	 * ����hearder���ԣ�����ͨѶ�ж���ϵ�˰�header������ʾ���Լ�ͨ���Ҳ�ABCD...��ĸ�����ٶ�λ��ϵ��
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
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
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}
	
}
