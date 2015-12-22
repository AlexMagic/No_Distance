package com.project.activity;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.project.R;
import com.project.app.MyApplication;
import com.project.util.CommonUtils;
import com.project.util.HttpUtil;


/**
 * 
 * 注册
 * 
 * 用户的信息存入自己服务器的DB里 
 * @author Administrator
 *
 */
public class RegisterActivity extends Activity{

	public static final int REQUEST_CODE_REGISTER_SUCCESS = 1;
	public static final String REQUEST_USERNAME = "username";
	public static final String REQUEST_PWD = "password";
	
	private InputMethodManager manager;
	
	private EditText mEditUserId;
	private EditText mEditPwd;
	private EditText mEditConfirmPwd;
	private EditText mEditNick;
	private EditText mEditEmail;
	private EditText mEditSchoolId;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private Button btn_register;
	
	private ProgressDialog pd ;
	
	private TextView tv_checkUser;
	private ImageView btn_back;
	
	private String sex;
	
	private List<String> userRegInfo = new ArrayList<String>();
	
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				tv_checkUser.setVisibility(View.GONE);
				mEditUserId.setTextColor(android.graphics.Color.BLACK);
				break;
			case 0:
				if (!RegisterActivity.this.isFinishing()){
					pd.dismiss();
					hideKeyboard();
				}
				
				Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
				
				break;
			case -1:
				//用户名已存在
				tv_checkUser.setVisibility(View.VISIBLE);
				mEditUserId.setTextColor(android.graphics.Color.RED);
				break;
			}
//			
			
		};
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		setUpView();
		setListner();
	}
	
	public void setUpView(){
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		btn_register = (Button) findViewById(R.id.register_btn);
		
		mEditUserId = (EditText) findViewById(R.id.edit_username);
		mEditPwd = (EditText) findViewById(R.id.edit_password);
		mEditConfirmPwd = (EditText) findViewById(R.id.edit_confirm_password);
		mEditNick = (EditText) findViewById(R.id.edit_register_nickname);
		mEditEmail = (EditText) findViewById(R.id.edit_register_email);
		mEditSchoolId = (EditText) findViewById(R.id.register_studentID);
		
		radioGroup = (RadioGroup)this.findViewById(R.id.radioGroup_sex);
		
		tv_checkUser = (TextView) findViewById(R.id.check_username);
		btn_back = (ImageView) findViewById(R.id.iv_back);
		
	}
	
	
	public void setListner(){
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(CommonUtils.isNetWorkConnected(getApplicationContext())){
					register();
				}else{
					Toast.makeText(getApplicationContext(), "网络连接有问题 ， 请检查", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                 //根据ID获取RadioButton的实例
                radioButton = (RadioButton)RegisterActivity.this.findViewById(radioButtonId);
//                radioButton.set
                 //更新文本内容，以符合选中项
//                Toast.makeText(RegisterActivity.this, radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
//                tv.setText("您的性别是：" + rb.getText());
                String temp;
                
                temp = radioButton.getText().toString();
                System.out.println("sex------: " + sex);
                if(temp.equals("男")){
                	sex = "boy";
                }else{
                	sex = "girl";
                }
            }
        });
		
		
		mEditUserId.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				final String userId = String.valueOf(s.toString());
				
				new Thread(){
					@Override
					public void run() {
						
						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
						params.add(new BasicNameValuePair("appID",mEditUserId.getText().toString()));
						params.add(new BasicNameValuePair("check", "check_AppId")); 
						
						String url = "http://1.nodistanceservice.sinaapp.com/Register.php"; 
						String result = null;
		
						result = HttpUtil.queryStringForPost(url, params);
//						Log.i("main", "result = " +result); //获取响应内容  
						
						
						
						Message msg = new Message();
						msg.what = Integer.parseInt(result);
						handler.sendMessage(msg);
						
					};
				}.start();
			}
		});
	}
	
	public void register(){
		final String username = mEditUserId.getText().toString().trim();
		final String pwd = mEditPwd.getText().toString().trim();
		String confirm_pwd = mEditConfirmPwd.getText().toString().trim();
		final String nickname = mEditNick.getText().toString().trim();
		final String email = mEditEmail.getText().toString().trim();
		final String studentID = mEditSchoolId.getText().toString().trim();
		
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		
		
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			mEditUserId.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			mEditPwd.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
			mEditConfirmPwd.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
			return;
		}else if(!matcher.find()){
			Toast.makeText(this, "邮箱格式不正确，请重新输入！", Toast.LENGTH_SHORT).show();
			return;
		}else if (TextUtils.isEmpty(studentID)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			mEditSchoolId.requestFocus();
			return;
		}else if (TextUtils.isEmpty(nickname)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			mEditNick.requestFocus();
			return;
		}
		
//		System.out.println("username : " + username + "   pwd :"+ pwd + "  email :" + email + "  nickname :" + nickname + "  sex  :" + sex + "  schoolid : " + studentID) ;
//		System.out.println(!TextUtils.isEmpty(username));
//		System.out.println(!TextUtils.isEmpty(pwd));
//		System.out.println(!TextUtils.isEmpty(email));
//		System.out.println(!TextUtils.isEmpty(sex));
//		System.out.println(!TextUtils.isEmpty(nickname));
//		System.out.println(!TextUtils.isEmpty(studentID));
		
		
		if(true){
			
			System.out.println("here-------------");
	
			userRegInfo.add(username);
			
			pd = new ProgressDialog(this);
			pd.setMessage("正在注册...");
			pd.show();
			
			new Thread(){
				@Override
				public void run() {
					
					
					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
					params.add(new BasicNameValuePair("appID",username));
					params.add(new BasicNameValuePair("password", pwd));
					params.add(new BasicNameValuePair("nickName", nickname));
					params.add(new BasicNameValuePair("email", email));
					params.add(new BasicNameValuePair("studentID", studentID));
					params.add(new BasicNameValuePair("sex", sex));
					params.add(new BasicNameValuePair("check", "check_ok"));
					
					
					String url = "http://1.nodistanceservice.sinaapp.com/Register.php"; 
					String result = null;
	
					result = HttpUtil.queryStringForPost(url, params);
					Log.i("main", "result = " +result); //获取响应内容  
					
					result = "0";
					
					MyApplication.getInstance().setUserName(username);
					MyApplication.getInstance().setPassword(pwd);
					MyApplication.getInstance().setNickName(nickname);
					
					Message msg = new Message();
					msg.what = Integer.parseInt(result);
					msg.obj = username;
					handler.sendMessage(msg);
					
					setResult(RESULT_OK , new Intent().putExtra(REQUEST_USERNAME, username));
					finish();
				};
			}.start();
			
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 if(event.getAction() == MotionEvent.ACTION_UP) {  
	            	hideKeyboard();
		 }
		return super.onTouchEvent(event);
	}
	
	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

//	public void finish(String username) {
//		// TODO Auto-generated method stub
//		super.finish();
//	}
	
	
	
//	private void back(View view){
//		finish();
//	}
}
