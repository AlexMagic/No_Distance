package com.project.communicate;

import java.util.ArrayList;
import java.util.List;

import com.project.R;
import com.project.activity.base.Sidebar;
import com.project.constane.Constant;
import com.project.domain.User;
//import com.project.util.UserHeadUtil;
//import com.project.util.UserHeadUtil.ImageCallBack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<User> implements SectionIndexer{
	
	private LayoutInflater layoutInflater;
	private int resource;
	
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	
	private Sidebar sidebar;
	private EditText query;
	private ImageButton clearSearch;
	
	private Bitmap mBitmap;
	
	public ContactAdapter(Context context, int resource, List<User> objects , Sidebar sidebar){
		super(context, resource, objects);
		this.resource = resource;
		this.sidebar = sidebar;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? 0 : 1;
	}

	
	
	@Override
	public User getItem(int position) {
		return position == 0 ? new User() : super.getItem(position - 1);
	}
	
	@Override
	public int getCount() {
		//��������cout+1
		return super.getCount() + 1;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ������
		if(position == 0){
			if(convertView == null){
				convertView = layoutInflater.inflate(R.layout.search_bar, null);
				query = (EditText) convertView.findViewById(R.id.query);
				clearSearch = (ImageButton) convertView.findViewById(R.id.search_clear);
				query.addTextChangedListener(new TextWatcher() {
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						getFilter().filter(s);
						if (s.length() > 0) {
							clearSearch.setVisibility(View.VISIBLE);
							if (sidebar != null)
								sidebar.setVisibility(View.GONE);
						} else {
							clearSearch.setVisibility(View.INVISIBLE);
							if (sidebar != null)
								sidebar.setVisibility(View.VISIBLE);
						}
					}
	
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}
	
					public void afterTextChanged(Editable s) {
					}
				});
				clearSearch.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						if (((Activity) getContext()).getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
							if (((Activity) getContext()).getCurrentFocus() != null)
							manager.hideSoftInputFromWindow(((Activity) getContext()).getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
						//�������������
						query.getText().clear();
					}
				});
			}
		}else{
			if(convertView == null){
				convertView = layoutInflater.inflate(resource, null);
			}
			
			final ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
			TextView unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
			TextView nameTextview = (TextView) convertView.findViewById(R.id.name);
			TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
			User user = getItem(position);
			//����û�������nick���滻
//			System.out.println("user---:"+user.getUsername());
			final String username = user.getUsername() ;
			System.out.println("username----:"+username);
			
			String nickname = user.getNickname()!=null ? user.getNickname(): null ;
//			System.out.println("username-----:"+username);
			String header = user.getHeader();
			if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
				if ("".equals(header)) {
					tvHeader.setVisibility(View.GONE);
				} else {
					tvHeader.setVisibility(View.VISIBLE);
					tvHeader.setText(header);
				}
			} else {
				tvHeader.setVisibility(View.GONE);
			}
			
			//��ʾ������֪ͨitem
			if(username.equals(Constant.NEW_FRIENDS_USERNAME)){
				nameTextview.setText(user.getNick());
				avatar.setImageResource(R.drawable.new_friends_icon);
				if(user.getUnreadMsgCount() > 0){
					unreadMsgView.setVisibility(View.VISIBLE);
					unreadMsgView.setText(user.getUnreadMsgCount()+"");
				}else{
					unreadMsgView.setVisibility(View.INVISIBLE);
				}
			}else if(username.equals(Constant.GROUP_USERNAME)){
				//Ⱥ��item
				nameTextview.setText(user.getNick());
				avatar.setImageResource(R.drawable.groups_icon);
			}else{
				nameTextview.setText(nickname);
				if(unreadMsgView != null)
					unreadMsgView.setVisibility(View.INVISIBLE);
				
				
//				if(UserHeadUtil.getInstance().getHeaderFromMap(username)!=null){
//					avatar.setImageBitmap(UserHeadUtil.getInstance().getHeaderFromMap(username));
//				}else{
//					UserHeadUtil.getInstance().getFrdHeader(username , new ImageCallBack() {
//						
//						@Override
//						public void callback(Bitmap bitmap) {
//							// TODO Auto-generated method stub
//							if(bitmap!=null){
////								System.out.println("not null");
//								avatar.setImageBitmap(bitmap);
//							}else{
////								System.out.println("null");
//								avatar.setImageResource(R.drawable.default_avatar);
//							}
//						}
//					});
//				}
			}
		}
			
		
		return convertView;
	}

	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add(getContext().getString(R.string.search_header));
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = getItem(i).getHeader();
			System.out.println("contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getUsername());
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}
	
}
