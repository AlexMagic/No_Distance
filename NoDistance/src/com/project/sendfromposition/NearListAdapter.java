package com.project.sendfromposition;

import java.util.ArrayList;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearListAdapter extends BaseAdapter{

	private static final int IV_WIDTH = 200;
	private static final int IV_HEIGHT = 200;
	
	private Context mContent;
	private ArrayList<String> places;
	
	public NearListAdapter(Context mContext , ArrayList<String> ttplaces){
		this.mContent = mContext;
		//this.places = places;
		places = new ArrayList<String>();
		places.add("咖啡厅");
		places.add("机场");
		places.add("书店");
		places.add("商场");
		places.add("旅游");
		places.add("学校");
		places.add("KTV");
		places.add("餐厅");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return places.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return places.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String place = places.get(position);
		ViewHolder holder;
		
		if(convertView==null){
			holder = new ViewHolder();
			
			convertView = LayoutInflater.from(mContent).inflate(R.layout.places, null);
			
			holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
			holder.iv_place = (ImageView) convertView.findViewById(R.id.iv_place);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
//		switch(position){
//		case 0:
//			holder.iv_place.setBackgroundResource(R.drawable.type_1);
//			break;
//		case 1:
//			holder.iv_place.setBackgroundResource(R.drawable.type_2);
//			break;
//		case 2:
//			holder.iv_place.setBackgroundResource(R.drawable.type_3);
//			break;
//		case 3:
//			holder.iv_place.setBackgroundResource(R.drawable.type_4);
//			break;
//		case 4:
//			holder.iv_place.setBackgroundResource(R.drawable.type_5);
//			break;
//		case 5:
//			holder.iv_place.setBackgroundResource(R.drawable.type_6);
//			break;
//		case 6:
//			holder.iv_place.setBackgroundResource(R.drawable.type_7);
//			break;
//		case 7:
//			holder.iv_place.setBackgroundResource(R.drawable.type_8);
//			break;
//		}
		holder.tv_place.setText(place);
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_place;
		ImageView iv_place;
	}

}
