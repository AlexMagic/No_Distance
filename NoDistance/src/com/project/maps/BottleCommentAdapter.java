package com.project.maps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottleCommentAdapter extends BaseAdapter implements OnClickListener{

	private ArrayList<BottleCommItem> list ;
	private Context mContext;
	private EditText edittext;
	
	public BottleCommentAdapter(ArrayList<BottleCommItem> list ,Context mContext , EditText edittext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.edittext = edittext;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public BottleCommItem getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		BottleCommItem item = getItem(position);
		ViewHolder v = null;
		
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_list_item, null);
			v = new ViewHolder();
			v.iv_header = (ImageView) convertView.findViewById(R.id.iv_userhead);
			v.tv_from_username = (TextView) convertView.findViewById(R.id.tv_username);
			v.tv_to_username = (TextView) convertView.findViewById(R.id.reply_to);
			v.commLayout = (RelativeLayout) convertView.findViewById(R.id.comment_layout);
			v.replyLayout = (RelativeLayout) convertView.findViewById(R.id.reply_layout);
			v.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment_content);
			v.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply_content);
			v.tv_time = (TextView) convertView.findViewById(R.id.timestamp);
			v.replyCommLayout = (RelativeLayout) convertView.findViewById(R.id.reply_to_comm_layout);
			v.tv_reply_comm = (TextView) convertView.findViewById(R.id.tv_reply_comment_content);
			
			
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		
//		v.tv_from_username.setText(item.getCommFromUsername());
		
		
		if(item.getReply()!="" && item.getReply().length()>2){
			v.commLayout.setVisibility(View.GONE);
			v.replyLayout.setVisibility(View.VISIBLE);
			v.replyCommLayout.setVisibility(View.VISIBLE);
			
			String replyJson = item.getReply();
			try{
				JSONArray array = new JSONArray(replyJson);
				JSONObject object = array.getJSONObject(0);
//				System.out.println("--------"+object.get("Content"));
//				System.out.println("------------"+object.get("toAppID").toString());
//				System.out.println("------------"+object.get("fromAppID").toString());
				v.tv_from_username.setText((object.get("fromAppID").toString()));
				v.tv_reply_comm.setText(item.getComment());
				v.tv_to_username.setText(object.get("toAppID").toString());
				v.tv_reply.setText(object.get("Content").toString());
			
				Date start = new Date(Long.parseLong(object.get("Time").toString()));
				Date end = new Date(System.currentTimeMillis());
				final String time = twoDateDistance(start, end);
				
				v.tv_time.setText(time);
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
//			v.tv_to_username.setText(item.getToUsername());
//			v.tv_reply.setText(item.getReply());
		}else{
			System.out.println("here");
			v.commLayout.setVisibility(View.VISIBLE);
			v.replyLayout.setVisibility(View.GONE);
			v.tv_from_username.setText(item.getCommFromUsername());
			v.tv_comment.setText(item.getComment());
			
			Date start = new Date(Long.parseLong(item.getCommTime()));
			Date end = new Date(System.currentTimeMillis());
			final String time = twoDateDistance(start, end);
			
			v.tv_time.setText(time);
		}
		
		
		
		
		//点击layout可以回复
		v.commLayout.setOnClickListener(this);
		v.replyLayout.setOnClickListener(this);
		//点击回复用户名进入用户信息界面
		v.tv_to_username.setOnClickListener(this);
		//点击头像进去用户信息界面
		v.iv_header.setOnClickListener(this);
		
		return convertView;
	}
	
	
	/** 
	    * 计算两个日期型的时间相差多少时间 
	    * @param startDate  开始日期 
	    * @param endDate    结束日期 
	      * @return 
	    */  
	public  String twoDateDistance(Date startDate,Date endDate){  
	    
	  if(startDate == null ||endDate == null){  
	      return null;  
	  }  
	  long timeLong = endDate.getTime() - startDate.getTime();  
	  if (timeLong<60*1000)  
	      return timeLong/1000 + "秒前";  
	  else if (timeLong<60*60*1000){  
	      timeLong = timeLong/1000 /60;  
	      return timeLong + "分钟前";  
	  }  
	  else if (timeLong<60*60*24*1000){  
	      timeLong = timeLong/60/60/1000;  
	      return timeLong+"小时前";  
	  }  
	  else if (timeLong<60*60*24*1000*7){  
	      timeLong = timeLong/1000/ 60 / 60 / 24;  
	      return timeLong + "天前";  
	  }  
	  else if (timeLong<60*60*24*1000*7*4){  
	      timeLong = timeLong/1000/ 60 / 60 / 24/7;  
	      return timeLong + "周前";  
	  }  
	  else {  
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));  
	      return sdf.format(startDate);  
	  }  
	}
	
	class ViewHolder{
		ImageView iv_header;
		TextView tv_comment;
		TextView tv_from_username;
		TextView tv_to_username;
		TextView tv_reply;
		TextView tv_time;
		TextView tv_reply_comm;
		RelativeLayout commLayout;
		RelativeLayout replyLayout;
		RelativeLayout replyCommLayout;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch(id){
		case R.id.iv_userhead:
			break;
		case R.id.reply_to:
			break;
		case R.id.comment_layout:
		case R.id.reply_layout:
			
			break;
			
		}
	}

}
