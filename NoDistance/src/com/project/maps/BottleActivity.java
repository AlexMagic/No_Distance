package com.project.maps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.project.R;
import com.project.app.MyApplication;
import com.project.schoolservice.BookDetailActivity;
import com.project.schoolservice.CommentAdapter;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;

public class BottleActivity extends Activity implements OnClickListener{
	
	private static final String SEND_COMMENT = "insert";
	private static final String GET_COMMENT = "select";
	private static final int COMMENT = 0;
	private static final int REPLY = 1;
	
	private Intent intent;
	
	private InputMethodManager manager;
	
	private TextView tv_content;
	private ImageView iv_background;
	private ImageView iv_header;
	private ImageView iv_heart;
	private TextView tv_heartNum;
	private TextView tv_time;
	private TextView tv_distance;
	private TextView tv_isfriends;
	private TextView tv_no_comment;
	private ListView commentListView;
	private ImageView btn_back;
	private Button btn_to_send;
	private Button btn_send;
	private LinearLayout bar_bottom;
	private EditText edittext;
	
	private BottleCommentAdapter adapter;
	private BottleCommItem item;
	
	private ArrayList<BottleCommItem> commentList ;
	private Map<Integer,BottleCommItem> commentMap ;
	private List<LinkedHashMap<String, Object>> list;
	
	private int ID;
	private String username;
	
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				tv_no_comment.setVisibility(View.GONE);
				commentListView.setVisibility(View.VISIBLE);
				
				adapter = new BottleCommentAdapter(commentList,BottleActivity.this,edittext);
				commentListView.setAdapter(adapter);
				setListHeight(commentListView);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_bottle);
		
		intent = getIntent();
		
		manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		ID = intent.getIntExtra("ID", -1);
		username = intent.getStringExtra("username");
//		item = (BottleItem) intent.getSerializableExtra("item");
		
		item = new BottleCommItem();
		
		
		setUpView();
		
		setListner();
	}
	
	public void setUpView(){
		btn_back = (ImageView) findViewById(R.id.btn_back);
		commentListView = (ListView) findViewById(R.id.comment_list);
		tv_no_comment = (TextView) findViewById(R.id.none_comment);
		tv_content = (TextView) findViewById(R.id.bottle_content);
		tv_heartNum = (TextView) findViewById(R.id.goods_num);
		tv_time = (TextView) findViewById(R.id.bottle_time);
		tv_distance = (TextView) findViewById(R.id.distance);
		iv_background = (ImageView) findViewById(R.id.iv_background);
		tv_isfriends = (TextView) findViewById(R.id.tv_friendsOrNot);
		
		btn_to_send = (Button) findViewById(R.id.to_send);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setTag(COMMENT);
		bar_bottom = (LinearLayout)findViewById(R.id.bar_bottom);
		
		tv_content.setText(intent.getStringExtra("content"));
		tv_time.setText(intent.getStringExtra("time"));
		tv_distance.setText(intent.getStringExtra("distance"));
		tv_isfriends.setText(intent.getStringExtra("from"));
		
		edittext = (EditText) findViewById(R.id.et_sendmessage);
		
		//获取显示评论
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				commentList = new ArrayList<BottleCommItem>();
				try {
					commentList = getComment();
					
					
					handler.sendEmptyMessage(0);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	
	public void setListner(){
		btn_to_send.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.translate_right, R.anim.translate_left);
			break;
		case R.id.to_send:
			 AnimationSet set=new AnimationSet(true);
			 Animation alpha = new AlphaAnimation(1.0f,0.3f);
			 Animation tran = new TranslateAnimation(0, 0, 0, 300);
			 set.addAnimation(alpha);
			 set.addAnimation(tran);
			 set.setDuration(500);
			 set.setFillAfter(true);                 //停留在最后的位置  
	         set.setFillEnabled(true);
	         btn_to_send.setAnimation(set);
	         btn_to_send.startAnimation(set);
	         set.startNow();
	         btn_to_send.setVisibility(View.GONE);
	         Animation alpha1 = new AlphaAnimation(0.0f,1.0f);
	         alpha1.setDuration(500);
	         bar_bottom.setAnimation(alpha1);
	         bar_bottom.startAnimation(alpha1);
	         bar_bottom.setVisibility(View.VISIBLE);
	         break;
		case R.id.btn_send:
			
			final String comment = edittext.getText().toString();
			System.out.println("comment---:"+comment);
			
			int oper = (Integer) view.getTag();
			
			if(comment!=""){
				//评论
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						//上传评论
						sendComment(comment);
					}
				});
				
				hideKeyboard();
				edittext.setText("");
				
				
				adapter = new BottleCommentAdapter(commentList,BottleActivity.this,edittext);
				commentListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(this, "评论不能为空哦", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	
	
	public void sendComment(String comment){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		String url = "http://1.nodistanceservice.sinaapp.com/operationNoteComment.php";
		params.add(new BasicNameValuePair("Operation", SEND_COMMENT));
		params.add(new BasicNameValuePair("AppID", MyApplication.getInstance().getUserName()));
		params.add(new BasicNameValuePair("noteID", ID+""));
		params.add(new BasicNameValuePair("comment", comment));
		
		Date curDate  =  new Date(System.currentTimeMillis());//获取当前时间       
		final long time = curDate.getTime();
		params.add(new BasicNameValuePair("time", time+""));
		
		item.setNotetId(ID);
		item.setCommFromUsername(MyApplication.getInstance().getUserName());
		item.setComment(comment);
		item.setToUsername(username);
		item.setCommTime(time+"");
		
		commentList.add(item);
		
		String result = HttpUtil.queryStringForPost(url, params); 
	}
	
	
	public ArrayList<BottleCommItem> getComment() throws JSONException{
		
		ArrayList<BottleCommItem> temp = new ArrayList<BottleCommItem>();
		
//		List<LinkedHashMap<String, Object>> tempList = null;
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		
		String url = "http://1.nodistanceservice.sinaapp.com/operationNoteComment.php";
		
		params.add(new BasicNameValuePair("Operation", GET_COMMENT));
		params.add(new BasicNameValuePair("noteID", ID+""));
		
		String result = HttpUtil.queryStringForPost(url, params);
		System.out.println("result----getCommentList-:"+result);
		
		JSONArray array = new JSONArray(result);
		for(int i=0 ; i<array.length() ; i++){
			JSONObject entityObj = array.getJSONObject(i);
			BottleCommItem item = new BottleCommItem();
			
//			System.out.println(entityObj.get("ID"));
//			System.out.println(entityObj.get("noteID"));
//			System.out.println(entityObj.get("fromAppID"));
//			System.out.println(entityObj.get("Content"));
//			System.out.println(entityObj.get("Time"));
//			System.out.println(entityObj.get("reply"));
			
			item.setId(Integer.parseInt(entityObj.get("ID").toString()));
			item.setNotetId(Integer.parseInt(entityObj.get("noteID").toString()));
			item.setCommFromUsername(entityObj.get("fromAppID").toString());
			item.setComment(entityObj.get("Content").toString());
			item.setCommTime(entityObj.get("Time").toString());
			item.setReply(entityObj.get("reply").toString());
			
			
			temp.add(item); 
		}
		
		
		return temp;
	}
	
	
	//计算出listview的高度 ， 解决scrollview的冲突
	private void setListHeight(ListView listview){
		ListAdapter listAdapter = listview.getAdapter();
		
		if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listview);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listview.getLayoutParams();   
        params.height = totalHeight+ (listview.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度    
        listview.setLayoutParams(params);   
	}
 
 /**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (this.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
