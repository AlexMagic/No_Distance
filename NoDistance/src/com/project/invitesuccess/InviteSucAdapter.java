package com.project.invitesuccess;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InviteSucAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	
	public InviteSucAdapter(Context mContext ){
		inflater = LayoutInflater.from(mContext);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.invite_success, null);
			holder = new ViewHolder();
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView iv_left_userHead;
		ImageView iv_left_img;
		ImageView iv_right_userHead;
		ImageView iv_right_img;
		TextView  tv_place;
	}

}
