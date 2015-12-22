package com.project.communicate;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class CommuModeAdapter extends BaseAdapter{

	private String[] name;
	private int[] icons;
	private int mChooseId;
	private Context context;
	
	public CommuModeAdapter(Context context , String[] name , int[] icons , int chooseId){
		this.name = name;
		this.icons = icons;
		this.context = context;
		mChooseId = chooseId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
//		System.out.println("mChooseId-----:"+ mChooseId);
		if(convertView == null){
//			System.out.println("view is null");
			convertView = LayoutInflater.from(context).inflate(R.layout.popwindow_item, null);
			holder.checked = (ImageView) convertView.findViewById(R.id.mode_pop_checked);
			holder.icon = (CheckBox) convertView.findViewById(R.id.mode_pop_icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.icon.setText(name[position]);
		holder.icon.setButtonDrawable(icons[position]);
		
		if(position == mChooseId){
			holder.icon.setChecked(true);
			holder.checked.setVisibility(View.VISIBLE);
		}else{
			holder.icon.setChecked(false);
			holder.checked.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		CheckBox icon;
		ImageView checked;
	}
	
}
