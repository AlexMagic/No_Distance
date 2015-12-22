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
		// ��ȡ����û�/Ⱥ��ĻỰ
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
			// �����һ����Ϣ��������Ϊitem��message����
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
		case LOCATION: //λ����Ϣ
			if (msg.direct == EMMessage.Direct.RECEIVE) {
				// ��sdk���ᵽ��ui�У�ʹ�ø��򵥲�����Ļ�ȡstring�ķ���
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
		case IMAGE: //ͼƬ��Ϣ
			ImageMessageBody imageBody = (ImageMessageBody) msg.getBody();
			digest = getString(context , R.string.picture);
			break;
		case VOICE: //������Ϣ
			digest = getString(context, R.string.voice);
			break;
		case VIDEO: // ��Ƶ��Ϣ
			digest = getString(context, R.string.video);
			break;
		case TXT: // �ı���Ϣ
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
		//�û�ͷ��
		ImageView avatar;
		//δ����Ϣ
		TextView unReadable;
		//���һ�����ͻ��߽�����Ϣ
		TextView lastMsg;
		//����ͻ��߽��յ�ʱ��
		TextView lastTime;
		//�û���
		TextView name;
		//���һ��������Ϣ��״̬
		View msgState;
		//����list��ÿһ���ܲ���
		RelativeLayout list_item_bar;
	}
	
	public String getString(Context context  , int resId){
		return context.getResources().getString(resId);
	}
	
}
