package com.project.schoolservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.R;
import com.project.app.MyApplication;
import com.project.db.CommentDao;
import com.project.db.KeepBookDao;
import com.project.db.RemindBookDao;
import com.project.domain.BookKeep;
import com.project.domain.RemindBack;
import com.project.find_index.CutString;
import com.project.find_index.Location;


public class BookDetailActivity extends Activity implements OnClickListener , OnGestureListener{
	
	private  static final int FLING_MIN_DISTANCE = 100;
	
	
	private ImageView img_back;
	private TextView book_name;
	private TextView book_index;
	private TextView book_location;
	private Button btn_to_send;
	private Button btn_send;
	private LinearLayout bar_bottom;
	private EditText edittext;
	private RelativeLayout more_oper;
	
	
	private ImageView iv_menu;
	private ImageView iv_keep_book;
	private ImageView iv_remind;
	
	private ListView listview;
	private CommentAdapter adapter;
	
	private GestureDetector mGestureDetector;
	private InputMethodManager manager;
	
	private List<CommentListItem> list;
	private String bookname;
	private String bookindex;
	private String bookInfo;
	private String usernick;
	
	private Boolean isClickable = false;
	
//	private List<LinkedHashMap<String, Object>> list = null;
//	private ArrayList<CommentListItem> commentItem = new ArrayList<CommentListItem>();
	
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				if(list.size()>0){
					adapter = new CommentAdapter(BookDetailActivity.this , list);
					listview.setAdapter(adapter);
					listview.setSelection(0);
				}
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail);
		overridePendingTransition(R.anim.translate_right, R.anim.translate_left);
		
		mGestureDetector = new GestureDetector(this, this);
		
		Intent intent = getIntent();
		
		bookname = intent.getStringExtra("bookname");
		bookindex = intent.getStringExtra("index");
		System.out.println("bookname---:"+bookname+"    bookindex---: "+bookindex);
		getComment();
		
		setUpView();
		
		setListner();
	}
	
	public void transferIndex(String index){
//		System.out.println("index----:"+index);
		if(!"".equals(index)){
			if (index.charAt(0) >= 'A' && index.charAt(0) <= 'Z' ) {
				CutString cs = new CutString(index);
				Location location = new Location(cs);
				int direct = location.getRoom();
				int secondDir = location.getShelf();
				int aaa = secondDir;
				String position = "";
				position = location.getLocation();
	
				bookInfo = bookname + "\n" + bookindex + "\n" + position ; // 书的详细信息
				book_location.setText(position);
			}
		} else {
			bookInfo = bookname + "\n";
			book_location.setText("新书编目中！");
		}
	}
	
	public void setUpView(){
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		img_back = (ImageView) findViewById(R.id.img_back);
		book_name = (TextView) findViewById(R.id.name_on_bar);
		book_name.setText(bookname);
		
		book_index = (TextView) findViewById(R.id.tv_bookindex);
		book_index.setText(bookindex);
		
		book_location = (TextView) findViewById(R.id.tv_booklocation);
		more_oper = (RelativeLayout) findViewById(R.id.more_oper);
		bar_bottom = (LinearLayout)findViewById(R.id.bar_bottom);
		
		listview = (ListView) findViewById(R.id.comment_list);
		adapter = new CommentAdapter(BookDetailActivity.this , list);
		listview.setAdapter(adapter);
		
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
		iv_keep_book = (ImageView) findViewById(R.id.iv_keep_book);
		iv_remind = (ImageView) findViewById(R.id.iv_remind);
		
		
		btn_to_send = (Button) findViewById(R.id.to_send);
		btn_send = (Button) findViewById(R.id.btn_send);
		edittext = (EditText) findViewById(R.id.et_sendmessage);
		
		transferIndex(bookindex);
	}
	
	public void setListner(){
		img_back.setOnClickListener(this);
		btn_to_send.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		iv_keep_book.setOnClickListener(this);
		iv_remind.setOnClickListener(this);
		
		final  AnimationSet set1=new AnimationSet(true);
		final  AnimationSet set2=new AnimationSet(true);
		final Animation anim_1 = AnimationUtils.loadAnimation(this, R.anim.rotate);
		final Animation anim_2 = AnimationUtils.loadAnimation(this, R.anim.rotate_banck);
		final Animation alphaIn = new AlphaAnimation(0.0f,1.0f);
		final Animation alphaOut = new AlphaAnimation(1.0f,0.0f);
		
		iv_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isClickable){
					
					isClickable = true; 
					iv_keep_book.setVisibility(View.VISIBLE);
					iv_remind.setVisibility(View.VISIBLE);
					anim_1.setFillAfter(true);
					anim_1.setDuration(500);
					anim_1.setFillEnabled(true);
					iv_menu.startAnimation(anim_1);
//					set1.addAnimation(alphaIn);
//					set2.addAnimation(alphaIn);
//					set1.setDuration(1500);
//					set2.setDuration(3500);
//					set1.setFillAfter(true);                 //停留在最后的位置  
//			        set1.setFillEnabled(true);
//					set2.setFillAfter(true);                 //停留在最后的位置  
//			        set2.setFillEnabled(true);
//			       
////					iv_keep_book.startAnimation(set1);
////					
////					iv_remind.startAnimation(set2);
				}else{
					isClickable = false;
					anim_2.setDuration(500);
					iv_keep_book.setVisibility(View.GONE);
					iv_remind.setVisibility(View.GONE);
					
					anim_2.setFillAfter(true);
					anim_2.setDuration(500);
					anim_2.setFillEnabled(true);
					iv_menu.startAnimation(anim_2);
//					set1.addAnimation(alphaOut);
//					set2.addAnimation(alphaOut);
//					set1.setDuration(1500);
//					set2.setDuration(3500);
//					set1.setFillAfter(true);                 //停留在最后的位置  
//			        set1.setFillEnabled(true);
//					set2.setFillAfter(true);                 //停留在最后的位置  
//			        set2.setFillEnabled(true);
				
//			        iv_keep_book.startAnimation(set1);
//					
//					iv_remind.startAnimation(set2);
				}
			}
		});
		
		listview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				hideKeyboard();
				return false;
			}
		});
	}
	
//	/**
//	 * 发送评论
//	 * @param comment
//	 */
//	public void sendComment(final String comment){
//		username = MyApplication.getInstance().getUserName();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
//				params.add(new BasicNameValuePair("Operation" , "insert"));
//				params.add(new BasicNameValuePair("appID" , username));
//				params.add(new BasicNameValuePair("BookName" , bookname));
//				params.add(new BasicNameValuePair("Comment" , comment));
//				
//				String url = "http://1.nodistanceservice.sinaapp.com/BookComment.php";
//				String result = HttpUtil.queryStringForPost(url, params);
//				System.out.println("result-------comment:  "+ result);
//				if(result != "-1"){
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							Toast.makeText(BookDetailActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
//						}
//					});
//				}
//			}
//		}).start();
//		
//	}
	
//	/**
//	 * 获取评论
//	 */
//	public void getComment(){
//		new Thread(new Runnable() {
//					
//			@Override
//			public void run() {
//				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
//				params.add(new BasicNameValuePair("Operation" , "select"));
//				params.add(new BasicNameValuePair("BookName" , bookname));
//				
//				String url = "http://1.nodistanceservice.sinaapp.com/BookComment.php";
//				String result = HttpUtil.queryStringForPost(url, params);
//				if(result != "-1" && !result.equals("-1")){
//					list = JsonUtil.json2List(result);
//				}
//				
//				if(list != null){
//					commentItem = new ArrayList<CommentListItem>();
//					for (int i = list.size() ; i>0; i++) {  
//						Map<String, Object> map = list.get(i);  
//		                Set<String> set = map.keySet();  
//		                CommentListItem comment = new CommentListItem();
//		                for (Iterator<String> it = set.iterator(); it.hasNext();) {  
//				          String key = it.next();  
//				          System.out.println(key + ":" + map.get(key)); 
//				          if(key.equals("BookName")){comment.setBookname((String) map.get(key)); }
//				          else if(key.equals("AppID"))  comment.setUsername((String) map.get(key));
//				          else if(key.equals("Comment")) comment.setComment((String) map.get(key));
//				          else if(key.equals("CommentDate")) comment.setTime((String) map.get(key));
//				         
//				        }
//		                commentItem.add(comment);
//				     }
//					
//					handler.sendEmptyMessage(0);
//				}
//				
//			}
//		}).start();
//	}
	
	/**
	 * 获取评论
	 * @param commentInfo
	 * @return
	 */
	public void getComment(){
		System.out.println("bookname----:"+bookname);
		CommentDao commentDao = new CommentDao(BookDetailActivity.this);
		Map<String , CommentListItem> comment = commentDao.getComment(bookname);
		list = new ArrayList<CommentListItem>(comment.values());
	}
	
	
	/**
	 * 发送评论
	 * @param bookInfo
	 * @return
	 */
	public void sendComment(String comment){
		CommentDao commentDao = new CommentDao(BookDetailActivity.this);
		CommentListItem commentData = new CommentListItem();
		
		SimpleDateFormat   formatter    =   new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");       
		Date curDate  =  new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate); 
		
		commentData.setComment(comment);
		commentData.setTime(str);
		commentData.setBookname(bookname);
		commentData.setUsername(MyApplication.getInstance().getUserName());
		commentData.setNickname(MyApplication.getInstance().getNickName());
		
		commentDao.saveComment(commentData);
		
	}
	
	
	public boolean keep(String bookInfo){
		if(bookInfo!=null){
			KeepBookDao kb = new KeepBookDao(BookDetailActivity.this);
			BookKeep bk = new BookKeep();
			
			String[] info = bookInfo.split("\n");
			
//			System.out.println("info.length:  "+info.length);
			
			bk.setBookname(info[0]);
			bk.setBookindex(info[1]);
			bk.setBookloca(info[2]);
			
			boolean isExist = kb.keepBook(bk);
			if(isExist){
				Toast.makeText(this, " 藏书成功 ", Toast.LENGTH_SHORT).show();
//				return isExist;
			}else{
				Toast.makeText(this, " 此书已藏 ", Toast.LENGTH_SHORT).show();
			}
			return isExist;
		}
		return false;
			
	}
	
	public void remind(){
//		System.out.println("position------:"+position);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.get_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
//		final TimePicker timePicker=(TimePicker)view.findViewById(R.id.timePicker1);
		builder.setView(view);

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), null);
		
		builder.setTitle("选取起始时间");
		builder.setPositiveButton("确  定",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
//						BookKeep bk = getItem(position);
						
						RemindBookDao rbDao = new RemindBookDao(BookDetailActivity.this);
						
						RemindBack rb = new RemindBack();
						
						String time = (String.format("%d-%02d-%02d",
								datePicker.getYear(),
								datePicker.getMonth() + 1,
								datePicker.getDayOfMonth()));
						
						
						
						rb.setBookname(bookname);
						rb.setRemindTime(time);
						
						System.out.println(rb.getBookname());
						
						if(rbDao.saveRemind(rb)){
							int id = rbDao.getRemindId(bookname);
							
							Intent intent = new Intent(BookDetailActivity.this , MyReceiver.class);
							Bundle bundle = new Bundle();
							bundle.putString("book_name", bookname);
							bundle.putString("remind_time", time);
							bundle.putInt("requestCode", id);
							intent.putExtras(bundle);
							AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	//						setAlerm(am);
							PendingIntent pi = PendingIntent.getBroadcast(BookDetailActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	//						setPendingIntent(pi);
							// 过10s 执行这个闹铃  
//							Calendar calendar = Calendar.getInstance();  
//							calendar.setTimeInMillis(System.currentTimeMillis());  
//							calendar.add(Calendar.SECOND, 10);  
							
							am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() , pi);
	
							Toast.makeText(BookDetailActivity.this, "添加提醒成功",Toast.LENGTH_SHORT).show();
							dialog.cancel();
						}else{
							Toast.makeText(BookDetailActivity.this, "重复添加提醒",Toast.LENGTH_SHORT).show();
						}
					}
						
				});

		Dialog dialog = builder.create();
		dialog.show();
	}
	
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.img_back:
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
			String comment = edittext.getText().toString();
			System.out.println("comment---:"+comment);
			if(comment!=""){
				System.out.println("ssssss");
				sendComment(comment);
				hideKeyboard();
				edittext.setText("");
				getComment();
				adapter = new CommentAdapter(BookDetailActivity.this , list);
				listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(this, "评论不能为空哦", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_keep_book:
			//收藏图书
			if(isClickable){
				keep(bookInfo);
//				System.out.println("click--keep");
			}
			break;
		case R.id.iv_remind:
			//记录归还时间
			if(isClickable){
				if(keep(bookInfo)){
//					Intent intent = new Intent(BookDetailActivity.this , BookRemindActivity.class);
					remind();
				}else{
					Toast.makeText(BookDetailActivity.this, "请先将此书收藏", Toast.LENGTH_SHORT);
				}
			}
			break;
		}
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE) {
			finish();
			overridePendingTransition(R.anim.translate_right, R.anim.translate_left);
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
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
	
}
