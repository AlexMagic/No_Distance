package com.project.communicate;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.FileMessageBody;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.DateUtils;
import com.easemob.util.LatLng;
import com.project.R;
import com.project.activity.ShowBigImage;
import com.project.task.LoadImageTask;
import com.project.util.ImageCache;
import com.project.util.ImageUtils;











import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

public class MessageAdapter extends BaseAdapter{

	
	private static final String TAG = "msg";
	
	private static final int MSG_TYPE_RECV_TXT = 0;
	private static final int MSG_TYPE_SEND_TXT = 1;
	private static final int MSG_TYPE_RECV_IMG = 2;
	private static final int MSG_TYPE_SEND_IMG = 3;
	private static final int MSG_TYPE_RECV_LOC = 4;
	private static final int MSG_TYPE_SEND_LOC = 5;
	private static final int MSG_TYPE_RECV_VOI = 6;
	private static final int MSG_TYPE_SEND_VOI = 7;
	private static final int MSG_TYPE_RECV_VIDEO = 8;
	private static final int MSG_TYPE_SEND_VIDEO = 9;
	



	private Context mContext;
	private Activity mActivity;
	
	private String username;
	private String userNic;
	private LayoutInflater inflater;
	
	private EMConversation conversation;
	
	public MessageAdapter(Context mContext , String username  , String userNic, int chatType){
		this.mContext = mContext;
		this.userNic = userNic;
		this.username = username;
		mActivity = (Activity)mContext;
		inflater = LayoutInflater.from(mContext);
		this.conversation = EMChatManager.getInstance().getConversation(username);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return conversation.getMsgCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (EMMessage)conversation.getMessage(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemViewType(int position) {
		EMMessage message = conversation.getMessage(position);
		
		if (message.getType() == EMMessage.Type.TXT) {
			return message.direct == EMMessage.Direct.RECEIVE ? MSG_TYPE_RECV_TXT : MSG_TYPE_SEND_TXT;
		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MSG_TYPE_RECV_IMG : MSG_TYPE_SEND_IMG;

		}
		if (message.getType() == EMMessage.Type.LOCATION) {
			return message.direct == EMMessage.Direct.RECEIVE ? MSG_TYPE_RECV_LOC : MSG_TYPE_SEND_LOC;
		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MSG_TYPE_RECV_VOI : MSG_TYPE_SEND_VOI;
		}
		if (message.getType() == EMMessage.Type.VIDEO) {
			return message.direct == EMMessage.Direct.RECEIVE ? MSG_TYPE_RECV_VIDEO : MSG_TYPE_SEND_VIDEO;
		}
		
		return -1; //错误类型
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 12;
	}
	
	//根据类型创建消息的item界面
	private View createViewByMessage(EMMessage message, int position) {
		switch (message.getType()) {
		case LOCATION:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_location, null) : inflater.inflate(
					R.layout.row_sent_location, null);
		case IMAGE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture, null) : inflater.inflate(
					R.layout.row_sent_picture, null);

		case VOICE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(
					R.layout.row_sent_voice, null);
		case VIDEO:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_video, null) : inflater.inflate(
					R.layout.row_sent_video, null);
		default:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_message, null) : inflater.inflate(
					R.layout.row_sent_message, null);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final EMMessage message = (EMMessage) getItem(position);
		ChatType chatType = message.getChatType();
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewByMessage(message, position);
			if (message.getType() == EMMessage.Type.IMAGE) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
					holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.TXT) {
				try {
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
					// 这里是文字内容
					holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.VOICE) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
					holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
					holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.LOCATION) {
				try {
					holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.VIDEO) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.chatting_content_iv));
					holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.size = (TextView) convertView.findViewById(R.id.chatting_size_iv);
					holder.timeLength = (TextView) convertView.findViewById(R.id.chatting_length_iv);
					holder.playBtn = (ImageView) convertView.findViewById(R.id.chatting_status_btn);
					holder.container_status_btn = (LinearLayout) convertView.findViewById(R.id.container_status_btn);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);

				} catch (Exception e) {
				}
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//群聊时 ， 显示接收的消息的发送人的名称
		if(chatType == chatType.GroupChat && message.direct == EMMessage.Direct.RECEIVE){
			String name =  userNic == null  ? username : userNic;
			holder.tv_userId.setText(name);
		}
		
		switch (message.getType()) {
//		// 根据消息type显示item
		case IMAGE:
			handleImageMessage(message, holder, position, convertView);
			break;
		case TXT:
			handleTextMessage(message, holder, position);
			break;
		case LOCATION:
			handleLocationMessage(message, holder, position, convertView);
			break;
		case VOICE:
			handleVoiceMessage(message, holder, position, convertView);
			break;
		case VIDEO:
			handleVideoMessage(message, holder, position, convertView);
			break;
		default:
			// not supported
		}
		
		if (message.direct == EMMessage.Direct.SEND) {
			View statusView = convertView.findViewById(R.id.msg_status);
			//重发按钮点击事件
			statusView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// 显示重发消息的自定义alertdialog
//					Intent intent = new Intent(mActivity, AlertDialog.class);
//					intent.putExtra("msg", mActivity.getString(R.string.confirm_resend));
//					intent.putExtra("title", mActivity.getString(R.string.resend));
//					intent.putExtra("cancel", true);
//					intent.putExtra("position", position);
//					if (message.getType() == EMMessage.Type.TXT)
//						mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_TEXT);
//					else if (message.getType() == EMMessage.Type.VOICE)
//						mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VOICE);
//					else if (message.getType() == EMMessage.Type.IMAGE)
//						mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_PICTURE);
//					else if (message.getType() == EMMessage.Type.LOCATION)
//						mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_LOCATION);
//					else if (message.getType() == EMMessage.Type.FILE)
//						mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_FILE);
//					else if (message.getType() == EMMessage.Type.VIDEO)
//						mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VIDEO);

				}
			});

		}else{
			//长按头像，移入黑名单
			holder.head_iv.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
//					Intent intent = new Intent(mActivity, AlertDialog.class);
//					intent.putExtra("msg", "移入到黑名单？");
//					intent.putExtra("cancel", true);
//					intent.putExtra("position", position);
//					mActivity.startActivityForResult(intent,ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);
					return true;
				}
			});
		}
		
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

		if (position == 0) {
			timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
			timestamp.setVisibility(View.VISIBLE);
		} else {
			//两条消息时间离得如果稍长，显示时间
			if (DateUtils.isCloseEnough(message.getMsgTime(), conversation.getMessage(position - 1).getMsgTime())) {
				timestamp.setVisibility(View.GONE);
			} else {
				timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
				timestamp.setVisibility(View.VISIBLE);
			}
		}
		
		return convertView;
	}
	
	
	
	
	private void handleVideoMessage(EMMessage message, ViewHolder holder,
			int position, View convertView) {
		
	}

	private void handleVoiceMessage(EMMessage message, ViewHolder holder,
			int position, View convertView) {
		
	}

	/**
	 * 处理地理信息
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleLocationMessage(EMMessage message, ViewHolder holder,
			int position, View convertView) {
		TextView locationView = ((TextView) convertView.findViewById(R.id.tv_location));
		LocationMessageBody locBody = (LocationMessageBody) message.getBody();
		locationView.setText(locBody.getAddress());
		LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
		//设置点击位置信息的监听
		locationView.setOnClickListener(new MapClickListener(loc, locBody.getAddress()));
		locationView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		if(message.direct == EMMessage.Direct.RECEIVE){
			return ;
		}
		switch(message.status){
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			break;
		default:
			sendMsgInBackground(message, holder);
		}
	}

	private void handleImageMessage(final EMMessage message, final ViewHolder holder,
			int position, View convertView) {
		holder.pb.setTag(position);
		holder.iv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
//				activity.startActivityForResult(
//						(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
//								EMMessage.Type.IMAGE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});
		
		if (message.direct == EMMessage.Direct.RECEIVE) {
			//"it is receive msg";
			if (message.status == EMMessage.Status.INPROGRESS) {
				//"!!!! back receive";
				holder.iv.setImageResource(R.drawable.default_avatar);
				showDownloadImageProgress(message, holder);
			} else { 
				//"!!!! not back receive, show image directly");
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.iv.setImageResource(R.drawable.default_avatar);
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				if (imgBody.getLocalUrl() != null) {
					String remotePath=imgBody.getRemoteUrl();
					String filePath=ImageUtils.getImagePath(remotePath);
					String thumbnailPath = ImageUtils.getThumbnailImagePath(filePath);
					showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message);
				}
			}
			return;
		}
		
		// process send message
		// send pic, show the pic directly
		ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
		String filePath = imgBody.getLocalUrl();
		if (filePath!=null&&new File(filePath).exists())
			showImageView(filePath, holder.iv, filePath, null, message);
	
	
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			// set a timer
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					mActivity.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
//								 message.setSendingStatus(Message.SENDING_STATUS_FAIL);
//								 message.setProgress(0);
								holder.staus_iv.setVisibility(View.VISIBLE);
//								Toast.makeText(mActivity,
//										mActivity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0)
//										.show();
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			sendPictureMessage(message, holder);
		}
		
	}
	
	
	/**
	 * send message with new sdk
	 */
	private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {
		// TODO Auto-generated method stub
//		String to = message.getTo();
		try {
		// before send, update ui
		holder.staus_iv.setVisibility(View.GONE);
		holder.pb.setVisibility(View.VISIBLE);
		holder.tv.setVisibility(View.VISIBLE);
		holder.tv.setText("0%");
		
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "send image message successfully");
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						// send success
						holder.pb.setVisibility(View.GONE);
						holder.tv.setVisibility(View.GONE);
					}
				});
			}

			@Override
			public void onError(int code, String error) {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						holder.pb.setVisibility(View.GONE);
						holder.tv.setVisibility(View.GONE);
						// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
						holder.staus_iv.setVisibility(View.VISIBLE);
//						Toast.makeText(mActivity,
//								mActivity.getString(R.string.send_fail) + mActivity.getString(R.string.connect_failuer_toast), 0).show();
					}
				});
			}

			@Override
			public void onProgress(final int progress, String status) {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						holder.tv.setText(progress + "%");
					}
				});
			}

		});
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	/**
	 * load image into image view
	 * 
	 * @param thumbernailPath
	 * @param iv
	 * @param position
	 * @return the image exists or not
	 */
	private boolean showImageView(String thumbnailPath, ImageView iv,
			final String filePath, String remoteDir, final EMMessage message) {
		final String remote = remoteDir;
		
		Bitmap bitmap = ImageCache.getInstance().get(thumbnailPath);
		
		if(bitmap != null){
			iv.setImageBitmap(bitmap);
			iv.setClickable(true);
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					System.err.println("image view on click");
					Intent intent = new Intent(mActivity, ShowBigImage.class);
					File file = new File(filePath);
					if (file.exists()) {
						Uri uri = Uri.fromFile(file);
						intent.putExtra("uri", uri);
						System.err.println("here need to check why download everytime");
					} else {
						// The local full size pic does not exist yet.
						// ShowBigImage needs to download it from the server
						// first
						// intent.putExtra("", message.get);
						ImageMessageBody body = (ImageMessageBody) message.getBody();
						intent.putExtra("secret", body.getSecret());
						intent.putExtra("remotepath", remote);
					}
//					if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
//							&& message.getChatType() != ChatType.GroupChat) {
//						try {
//							EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
//							message.isAcked = true;
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
					mActivity.startActivity(intent);
				}
			});
			return true;
		}else{
			new LoadImageTask().execute(thumbnailPath, filePath, remote, message.getChatType(), iv, mActivity, message);
			return true;
		}
		
		
	}

	
	//
	private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {
		// TODO Auto-generated method stub
		final FileMessageBody msgbody = (FileMessageBody) message.getBody();
		msgbody.setDownloadCallback(new EMCallBack() {
			
			@Override
			public void onSuccess() {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// message.setBackReceive(false);
						if (message.getType() == EMMessage.Type.IMAGE) {
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
						}
						notifyDataSetChanged();
					}
				});
			}
			
			@Override
			public void onProgress(final int progress, String arg1) {
				if (message.getType() == EMMessage.Type.IMAGE) {
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							holder.tv.setText(progress + "%");
						}
					});
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(mContext, "接受图片失败", 3000);
			}
		});
	}

	
	
	//文本消息
	private void handleTextMessage(EMMessage message, ViewHolder holder,final int position) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		//暂无表情
		holder.tv.setText(txtBody.getMessage());
		holder.tv.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				Intent intent = new Intent(mActivity , ContextMenu.class);
				intent.putExtra("position" , position);
				intent.putExtra("type" , EMMessage.Type.TXT.ordinal());
				mActivity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});
		
		if(message.direct == EMMessage.Direct.SEND){
			switch(message.status){
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:break;
			default:
				sendMsgInBackground(message, holder);
			}
		}
	}

	/**
	 * 发送消息
	 * @param message
	 * @param holder
	 */
	private void sendMsgInBackground(final EMMessage message,final ViewHolder holder) {
		holder.staus_iv.setVisibility(View.GONE);
		holder.pb.setVisibility(View.VISIBLE);
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				updateSendedView(message, holder);
			}

			@Override
			public void onError(int code, String error) {
				updateSendedView(message, holder);
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});
	}

	protected void updateSendedView(final EMMessage message, final ViewHolder holder) {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(message.getType() == EMMessage.Type.VIDEO){
					holder.tv.setVisibility(View.GONE);
				}
				if(message.status == EMMessage.Status.SUCCESS){
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.GONE);
				}else if(message.status == EMMessage.Status.FAIL){
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.VISIBLE);
				}
			}
		});
		
	}
	
	class MapClickListener implements OnClickListener{

		LatLng loc;
		String address;
		
		MapClickListener(LatLng loc , String address){
			this.loc = loc;
			this.address = address;
		}
		
		@Override
		public void onClick(View arg0) {
			//跳转到百度地图
		}
		
	}

	public static class ViewHolder {
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView head_iv;
		TextView tv_userId;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
	}

}
