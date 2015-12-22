package com.project.communicate;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.project.R;
import com.project.activity.DesktopActivity;
import com.project.app.MyApplication;
import com.project.util.CommonUtils;

public class ChatActivity extends Activity implements OnClickListener{
	
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;
	
	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	
	private View recordingLayout;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private EditText mEditText;
	private Button buttonSetModeKeyboard;
	private Button buttonSetModeVoice;
	private Button buttonSend;
	private Button buttonMore;
	private LinearLayout buttonPressToSpeak;
	private ViewPager expressionViewpager;
	private LinearLayout expressionContainer;
	private LinearLayout btnContainer;
	private ImageView locationImgview;
	private View more;
	private int position;
	
	private StudentCommunicate mComm;
	
	private ClipboardManager clipboard;
	private InputMethodManager manager;
	
	private File cameraFile;
	private List<String> reslist;
	private Drawable[] micImages; 
	private int chatType;
	private EMConversation conversation;
	private NewMessageBroadcastReceiver receiver;
	public static ChatActivity activityInstance = null;
	
	
	//��˭������Ϣ
	private String toChatUsername;
	private String toChatUserNic;
	
	private VoiceRecorder voiceRecorder;
	private WakeLock wakeLock;
	
	private MessageAdapter adapter;

	private ChatHistoryView mChatHistoryView;
	
	private ImageView iv_back;
	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmore;
	private boolean isLoading;
	private final int pageSize = 20;
	private boolean haveMoreData = true;
	
	private Handler micImageHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//�л�msg �л�ͼƬ
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.chat);
		
		mChatHistoryView = new ChatHistoryView(this, this);
		mComm = new StudentCommunicate(this, this);
		initView();
		setUpView();
	}
	
	private void initView(){
		recordingLayout = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (ListView) findViewById(R.id.list);
		buttonMore = (Button) findViewById(R.id.btn_more);
		mEditText = (EditText) findViewById(R.id.et_sendmessage);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = (Button) findViewById(R.id.btn_set_mode_voice);
		buttonSetModeKeyboard = (Button) findViewById(R.id.btn_set_mode_keyboard);
		buttonSend = (Button) findViewById(R.id.btn_send);
		buttonPressToSpeak = (LinearLayout) findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		locationImgview = (ImageView) findViewById(R.id.btn_location);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmore = (ProgressBar) findViewById(R.id.pb_load_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		buttonMore.setOnClickListener(this);
		// ������Դ�ļ�,����¼������ʱ
		micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02), getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04), getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06), getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08), getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10), getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12), getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };
		//����list �� ����
		//��ʼ�������viewpager , �ݲ�ʵ��
		
		voiceRecorder = new VoiceRecorder(micImageHandler);
		
		mEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s)){
					buttonSetModeVoice.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				}else{
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {
						buttonSetModeVoice.setVisibility(View.VISIBLE);
						buttonSend.setVisibility(View.GONE);
					}
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpView(){
		activityInstance = this;
//		iv_emoticons_normal.setOnClickListener(this);
//		iv_emoticons_checked.setOnClickListener(this);
		
		//����ϵͳ��ط���
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "wakelock");
	
		//�ж��ǵ��Ļ���Ⱥ��
		chatType =  getIntent().getIntExtra("chattpye", CHATTYPE_SINGLE);
		
		if(chatType == CHATTYPE_SINGLE){
			toChatUsername = getIntent().getStringExtra("userId");
			System.out.println("username--:"+toChatUsername);
			toChatUserNic = getIntent().getStringExtra("usernic");
			if(!"".equals(toChatUserNic))
				((TextView) findViewById(R.id.name_on_bar)).setText(toChatUserNic);
			else 
				((TextView) findViewById(R.id.name_on_bar)).setText(toChatUsername);
		}else{ 
			//Ⱥ��
		}
		
		conversation = EMChatManager.getInstance().getConversation(toChatUsername);
		// �Ѵ˻Ự��δ������Ϊ0
		conversation.resetUnsetMsgCount();
		
		adapter = new MessageAdapter(this, toChatUsername, toChatUserNic, chatType);
		//��ʾ��Ϣ
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new ListScrollListener());
		
		int count = listView.getCount();
		if (count > 0) {
			listView.setSelection(count - 1);
		}
		
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
//				iv_emoticons_normal.setVisibility(View.VISIBLE);
//				iv_emoticons_checked.setVisibility(View.INVISIBLE);
//				expressionContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
		
		// ע�������Ϣ�㲥
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		// ���ù㲥�����ȼ������Mainacitivity,���������Ϣ����ʱ��������chatҳ�棬ֱ����ʾ��Ϣ����������ʾ��Ϣδ��
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);
		
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			setResult(RESULT_OK);
			finish();
			return;
		}
		if(requestCode == REQUEST_CODE_CONTEXT_MENU){
//			switch(resultCode){
//				
//			}
		}
	}
	
	/**
	 * listview��������listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getFirstVisiblePosition() == 0 && !isLoading && haveMoreData) {
					loadmore.setVisibility(View.VISIBLE);
					// sdk��ʼ�����ص������¼Ϊ20��������ʱȥdb���ȡ����
					List<EMMessage> messages;
					try {
						// ��ȡ����messges�����ô˷�����ʱ���db��ȡ��messages
						// sdk���Զ����뵽��conversation��
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(((EMMessage) adapter.getItem(0)).getMsgId(), pageSize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(((EMMessage) adapter.getItem(0)).getMsgId(), pageSize);
					} catch (Exception e1) {
						loadmore.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// ˢ��ui
						adapter.notifyDataSetChanged();
						listView.setSelection(messages.size() - 1);
						if (messages.size() != pageSize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmore.setVisibility(View.GONE);
					isLoading = false;
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}
	
	
	private void showMore(){
		if(more.getVisibility() == View.GONE){
			System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
		}else{
			more.setVisibility(View.GONE);
//			btnContainer.setVisibility(View.GONE);
		}
	}
	
	
	
	/**
	 * ��Ϣ�㲥������
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String username = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			// �յ�����㲥��ʱ��message�Ѿ���db���ڴ����ˣ�����ͨ��id��ȡmesage����
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
			// �����Ⱥ����Ϣ����ȡ��group id
			if (message.getChatType() == ChatType.GroupChat) {
				username = message.getTo();
			}
			if (!username.equals(toChatUsername)) {
				// ��Ϣ���Ƿ�����ǰ�Ự��return
				return;
			}
			// conversation =
			// EMChatManager.getInstance().getConversation(toChatUsername);
			
			// ֪ͨadapter������Ϣ������ui
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			// �ǵðѹ㲥���ս��
			abortBroadcast();
		}
	}

	
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btn_send) {// ������Ͱ�ť(�����ֺͱ���)
			String s = mEditText.getText().toString();
			sendText(s);
		} else if (id == R.id.btn_take_picture) {
			selectPicFromCamera();// �������ͼ��
		} else if (id == R.id.btn_picture) {
			selectPicFromLocal(); // ���ͼƬͼ��
		} else if (id == R.id.btn_location) { // λ��
//			startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
		} else if (id == R.id.iv_emoticons_normal) { // �����ʾ�����
//			more.setVisibility(View.VISIBLE);
//			iv_emoticons_normal.setVisibility(View.INVISIBLE);
//			iv_emoticons_checked.setVisibility(View.VISIBLE);
//			btnContainer.setVisibility(View.GONE);
//			expressionContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // ������ر����
//			iv_emoticons_normal.setVisibility(View.VISIBLE);
//			iv_emoticons_checked.setVisibility(View.INVISIBLE);
//			btnContainer.setVisibility(View.VISIBLE);
//			expressionContainer.setVisibility(View.GONE);
//			more.setVisibility(View.GONE);

		} else if (id == R.id.btn_video) {
			// �������ͼ��
//			Intent intent = new Intent(ChatActivity.this, ImageGridActivity.class);
//			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
		}else if(id == R.id.iv_back){
			finish();
		}else if(id == R.id.btn_more){
			System.out.println("333");
			showMore();
		}
	}
	
	/**
	 * ��ͼ���ȡͼƬ
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * �����ȡͼƬ
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getApplicationContext(), "SD�������ڣ���������", 0).show();
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(), MyApplication.getInstance().getUserName()
				+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
//		EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
		// ע���㲥
		try {
			unregisterReceiver(receiver);
			receiver = null;
		} catch (Exception e) {
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.refresh();
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
	
	@Override
	protected void onNewIntent(Intent intent) {
		// ���notification bar��������ҳ�棬��ֻ֤��һ������ҳ��
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}
	}
	
	/**
	 * ����ͼƬ
	 * 
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���
		if (chatType == CHATTYPE_GROUP)
			message.setChatType(ChatType.GroupChat);

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// Ĭ�ϳ���100k��ͼƬ��ѹ���󷢸��Է����������óɷ���ԭͼ
		 body.setSendOriginalImage(true);
		message.addBody(body);
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		adapter.refresh();
		listView.setSelection(listView.getCount() - 1);
		setResult(RESULT_OK);
//		 more(more);
	}
	
	/**
	 * ����ͼ��ͼƬuri����ͼƬ
	 * @param selectImg
	 */
	private void sendPicByUri(Uri selectImg){
		
	}

	private void sendText(String content) {
		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���
			if (chatType == CHATTYPE_GROUP)
				message.setChatType(ChatType.GroupChat);
			TextMessageBody txtBody = new TextMessageBody(content);
			// ������Ϣbody
			message.addBody(txtBody);
			// ����Ҫ����˭,�û�username����Ⱥ��groupid
			message.setReceipt(toChatUsername);
			// ��messgage�ӵ�conversation��
			conversation.addMessage(message);
			// ֪ͨadapter����Ϣ�䶯��adapter����ݼ��������message��ʾ��Ϣ�͵���sdk�ķ��ͷ���
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			mEditText.setText("");

			setResult(RESULT_OK);

		}
	}
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		DesktopActivity.getRoot().close(DesktopActivity.getCommView());
		mChatHistoryView.refresh();
		finish();
	}
	
	public String getToChatUsername(){
		return toChatUsername;
	}
}
