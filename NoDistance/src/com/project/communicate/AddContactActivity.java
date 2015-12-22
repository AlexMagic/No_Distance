package com.project.communicate;



import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

import com.easemob.chat.EMContactManager;
import com.project.R;
import com.project.app.MyApplication;
import com.project.domain.User;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactActivity extends Activity implements OnClickListener{
	
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	private Button btn_back;
	private Button addFrd;
	
	private ContactView mContactView;
	private User user;
	
	private boolean isExist = false;
	private List<LinkedHashMap<String, Object>> list;
	private Map<String, User> userlist = new HashMap<String, User>();
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		mContactView = new ContactView(this , this);
		
		setUpView();
		setListner();
		
	}
	
	public void setUpView(){
		editText = (EditText) findViewById(R.id.edit_note);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		addFrd = (Button) findViewById(R.id.indicator);
		btn_back = (Button) findViewById(R.id.btn_back);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	public void setListner(){
		searchBtn.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		addFrd.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.search:
			final String search_item = editText.getText().toString();
			String saveText = searchBtn.getText().toString();
			
			if (getString(R.string.button_search).equals(saveText)) {
//				System.out.println("search_item---:"+search_item);
				toAddUsername = search_item;
				if(TextUtils.isEmpty(search_item)) {
//					startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "请输入用户名"));
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_LONG).show();
						}
					});
					
					return;
				}
				
				// TODO 从服务器获取此contact,如果不存在提示不存在此用户
				new Thread(new Runnable() {
					
					@Override
					public void run() {
//						final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
						
						list = findUser(search_item);
						userlist = new HashMap<String, User>();
						
						if(list != null){
							for (int i = 0; i < list.size(); i++) {  
								Map<String, Object> map = list.get(i);  
				                Set<String> set = map.keySet();  
						        user = new User();
				                for (Iterator<String> it = set.iterator(); it.hasNext();) {  
						          String key = it.next();  
						          System.out.println(key + ":" + map.get(key)); 
						          if(key.equals("AppID")){user.setUsername((String) map.get(key)); }
						          else if(key.equals("NickName"))  user.setNickname((String) map.get(key));
						          else if(key.equals("studentID")) user.setStuId((String) map.get(key));
						          else if(key.equals("Email")) user.setEmail((String) map.get(key));
						          else if(key.equals("Sex")) user.setSex((String) map.get(key));
						         
						        } 
				                System.out.println("username---:"+user.getUsername());
//						        setUserHearder(user.getUsername(), user);
				                
//				                userlist.put(user.getUsername(), user);
						     }
							//服务器存在此用户，显示此用户和添加按钮
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									searchedUserLayout.setVisibility(View.VISIBLE);
									nameText.setText(toAddUsername);
								}
							});
							
						
						}else{
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), "此用户不存在", Toast.LENGTH_LONG).show();
								}
							});
						}
					}
				}).start();
				
				
				
			} 
			break;
			
		case R.id.indicator:
			//添加好友
			if(isExist){
				addFrd();
			}else{
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "此用户不存在 ", Toast.LENGTH_LONG).show();
					}
				});
			}
			break;
		}
	}
	
	public void addFrd(){
		if(MyApplication.getInstance().getUserName().equals(nameText.getText().toString())){
//			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "不能添加自己"));
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "不能添加自己", Toast.LENGTH_LONG).show();
				}
			});
			return;
		}
		
		if(MyApplication.getInstance().getContactList().containsKey(nameText.getText().toString())){
//			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你的好友"));
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "此用户已是你的好友", Toast.LENGTH_LONG).show();
				}
			});
			
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在发送请求...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				
				try {
//					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
//					params.add(new BasicNameValuePair("userID",MyApplication.getInstance().getUserName()));
//					params.add(new BasicNameValuePair("friendID",toAddUsername));
//					
//					String url = "http://1.nodistanceservice.sinaapp.com/AddFriend.php";
//					String result = HttpUtil.queryStringForPost(url, params);
//					System.out.println("result----: " + result);
//					if(result=="1"){
						//demo写死了个reason，实际应该让用户手动填入
						EMContactManager.getInstance().addContact(toAddUsername, "加个好友呗");
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), "发送请求成功,等待对方验证", 1).show();
								finish();
							}
						});
//					}
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "请求添加好友失败:" + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}
	
	
	public List<LinkedHashMap<String, Object>> findUser(String username){
		List<LinkedHashMap<String, Object>> tempList = null;
		
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("appID",username));
		
		String url = "http://1.nodistanceservice.sinaapp.com/selectUser.php";
		String result = HttpUtil.queryStringForPost(url, params);
		
		if(result != "-1" && !result.equals("-1")){
			System.out.println("result111------------------:"+result);
			isExist = true;
			tempList = JsonUtil.json2List(result);
		}else{
			isExist = false;
		}
		
		
		return tempList;
		
	}
	
}
