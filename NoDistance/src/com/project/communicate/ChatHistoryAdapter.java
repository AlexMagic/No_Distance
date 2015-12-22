package com.project.communicate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.project.R;
import com.project.app.MyApplication;
import com.project.domain.User;
//import com.project.util.UserHeadUtil;

public class ChatHistoryAdapter extends ArrayAdapter<EMContact> {


	private Context context;
	private LayoutInflater inflater;
	private Map<String , User> frdList;
	
	public ChatHistoryAdapter(Context context, int textViewResourceId,List<EMContact> objects) {
		super(context,  textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		frdList = MyApplication.getInstance().getContactList();
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		EMContact user = (EMContact) getItem(position);
		
		ViewHolder h = null;
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.chat_list_item, parent , false);
		}
		
		
		h = (ViewHolder) convertView.getTag();
		
		if(h==null){
			h = new ViewHolder();
			
			h.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			h.name = (TextView) convertView.findViewById(R.id.user_name);
			h.lastMsg = (TextView) convertView.findViewById(R.id.lastmsg);
			h.lastTime = (TextView) convertView.findViewById(R.id.time);
			h.unReadable = (TextView) convertView.findViewById(R.id.unread_msg_number);
			h.msgState = convertView.findViewById(R.id.msg_state);
			h.list_item_bar = (RelativeLayout) convertView.findViewById(R.id.item_bar);
			convertView.setTag(h);
		}
		
		
		
		String username = user.getUsername();
		// 获取与此用户/群组的会话
		EMConversation conversation = EMChatManager.getInstance().getConversation(username);
		User userTemp = frdList.get(username);
		h.name.setText(userTemp.getNickname()!=null ?userTemp.getNickname() :userTemp.getUsername());

		System.out.println("username-----hisadapter:"+userTemp.getNickname());
		System.out.println("conversation.getUnreadMsgCount()------:"+conversation.getUnreadMsgCount());
		if(conversation.getUnreadMsgCount()>0){
			h.unReadable.setText(String.valueOf((conversation.getUnreadMsgCount())));
			h.unReadable.setVisibility(View.VISIBLE);
		}else{
			h.unReadable.setVisibility(View.GONE);
		}
		
		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			h.lastMsg.setText(getMessageDigest(lastMessage , this.getContext()));
			System.out.println(getMessageDigest(lastMessage , this.getContext()));
			h.lastTime.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
				h.msgState.setVisibility(View.VISIBLE);
			} else {
				h.msgState.setVisibility(View.GONE);
			}
			
//			Bitmap bitmap = UserHeadUtil.getInstance().getHeaderFromMap(username);
			
//			if(bitmap!=null){
//				h.avatar.setImageBitmap(bitmap);
//			}else{
//				System.out.println("nulllllllllllllll");
//			}
		}
		
		return convertView;
	}
	
	public String getMessageDigest(EMMessage msg , Context context){
		String digest = "";
		switch(msg.getType()){
		case LOCATION: //位置信息
			if (msg.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getString(context, R.string.location_recv);
				digest = String.format(digest, msg.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getString(context, R.string.location_prefix);
			}
			break;
		case IMAGE: //图片信息
			ImageMessageBody imageBody = (ImageMessageBody) msg.getBody();
			digest = getString(context , R.string.picture);
			break;
		case VOICE: //语音信息
			digest = getString(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getString(context, R.string.video);
			break;
		case TXT: // 文本消息
			TextMessageBody txtBody = (TextMessageBody) msg.getBody();
			digest = txtBody.getMessage();
			break;
		default:
			System.out.println("unknow tpye");
			return "";
		}
		
		return digest;
	}
	
	
	class ViewHolder{
		//用户头像
		ImageView avatar;
		//未读消息
		TextView unReadable;
		//最后一条发送或者接收消息
		TextView lastMsg;
		//最后发送或者接收的时间
		TextView lastTime;
		//用户名
		TextView name;
		//最后一条发送消息的状态
		View msgState;
		//整个list中每一行总布局
		RelativeLayout list_item_bar;
	}
	
	public String getString(Context context  , int resId){
		return context.getResources().getString(resId);
	}
	
}
