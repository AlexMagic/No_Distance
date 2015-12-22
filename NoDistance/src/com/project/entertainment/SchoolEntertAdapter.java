package com.project.entertainment;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.R;

public class SchoolEntertAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<SchoolEntertListItem> list = new ArrayList<SchoolEntertListItem>();
	
	public SchoolEntertAdapter(Context context ,ArrayList<SchoolEntertListItem> list){
		this.context = context;
		this.list = list;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SchoolEntertListItem item = list.get(position);
		ViewHolder v = null;
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.entert_school_recent_item, null);
			
			v =new ViewHolder();
			
			v.aca_logo = (ImageView) convertView.findViewById(R.id.academy_logo);
			v.aca_name = (TextView) convertView.findViewById(R.id.academy_name);
			v.aca_news = (TextView) convertView.findViewById(R.id.academy_news);
			
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		v.aca_logo.setImageResource(Integer.parseInt(item.getAcademyLogo()));
		v.aca_name.setText(item.getAcademyName());
		v.aca_news.setText(item.getAcademyNews());
		return convertView;
	}
	
	class ViewHolder{
		ImageView aca_logo;
		TextView aca_name;
		TextView aca_news;
	}

}
