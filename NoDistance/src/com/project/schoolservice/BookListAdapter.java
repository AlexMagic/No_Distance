package com.project.schoolservice;

import java.util.ArrayList;

import com.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

	private Context mContext ;
	private ArrayList<BookListItem> list = new ArrayList<BookListItem>();
	
	
	public BookListAdapter(Context context , ArrayList<BookListItem> list){
		mContext = context;
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
		BookListItem item = list.get(position);
		ViewHolder v = null;
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.book_list_item, null);
			
			v = new ViewHolder();
			
			v.img_book = (ImageView) convertView.findViewById(R.id.img_book);
			v.tv_bookname = (TextView) convertView.findViewById(R.id.tv_bookname);
			v.tv_book_author = (TextView) convertView.findViewById(R.id.tv_book_author);
			v.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
			v.tv_publish = (TextView) convertView.findViewById(R.id.tv_publish);
			v.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
			v.tv_save = (TextView) convertView.findViewById(R.id.tv_save);
			
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		
		
			v.img_book.setImageResource(R.drawable.live_user_week_pressed);
			v.tv_bookname.setText(item.getBookName());
			v.tv_book_author.setText(item.getWriter());
			v.tv_index.setText(item.getIndex());
			v.tv_publish.setText(item.getPublish());
			v.tv_year.setText(item.getYear());
			v.tv_save.setText(item.getSave());
		
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView img_book;
		TextView tv_bookname;
		TextView tv_book_author;
		TextView tv_index;
		TextView tv_year;
		TextView tv_publish;
		TextView tv_save;
	}

}
