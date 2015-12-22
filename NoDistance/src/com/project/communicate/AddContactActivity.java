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
//					startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "�������û���"));
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "�������û���", Toast.LENGTH_LONG).show();
						}
					});
					
					return;
				}
				
				// TODO �ӷ�������ȡ��contact,�����������ʾ�����ڴ��û�
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
							//���������ڴ��û�����ʾ���û�����Ӱ�ť
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
									Toast.makeText(getApplicationContext(), "���û�������", Toast.LENGTH_LONG).show();
								}
							});
						}
					}
				}).start();
				
				
				
			} 
			break;
			
		case R.id.indicator:
			//��Ӻ���
			if(isExist){
				addFrd();
			}else{
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "���û������� ", Toast.LENGTH_LONG).show();
					}
				});
			}
			break;
		}
	}
	
	public void addFrd(){
		if(MyApplication.getInstance().getUserName().equals(nameText.getText().toString())){
//			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "��������Լ�"));
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "��������Լ�", Toast.LENGTH_LONG).show();
				}
			});
			return;
		}
		
		if(MyApplication.getInstance().getContactList().containsKey(nameText.getText().toString())){
//			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "���û�������ĺ���"));
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "���û�������ĺ���", Toast.LENGTH_LONG).show();
				}
			});
			
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("���ڷ�������...");
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
						//demoд���˸�reason��ʵ��Ӧ�����û��ֶ�����
						EMContactManager.getInstance().addContact(toAddUsername, "�Ӹ�������");
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), "��������ɹ�,�ȴ��Է���֤", 1).show();
								finish();
							}
						});
//					}
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "������Ӻ���ʧ��:" + e.getMessage(), 1).show();
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
