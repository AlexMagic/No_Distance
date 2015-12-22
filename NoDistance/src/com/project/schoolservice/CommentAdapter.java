package com.project.schoolservice;

import java.util.List;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter{

	private Context context;
	private List<CommentListItem> list;
	
	public CommentAdapter(Context context,List<CommentListItem> list){
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder v = null;
		
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item, null);
			v = new ViewHolder();
			
			v.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
			v.tv_time = (TextView) convertView.findViewById(R.id.timestamp);
			v.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment_content);
			
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		
		v.tv_username.setText(list.get(position).getNickname());
		v.tv_time.setText(list.get(position).getTime());
		v.tv_comment.setText(list.get(position).getComment());
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_username;
		TextView tv_time;
		TextView tv_comment;
	}
	
	
	
}
