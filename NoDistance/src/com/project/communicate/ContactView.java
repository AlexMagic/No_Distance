package com.project.communicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.R;
import com.project.activity.base.Sidebar;
import com.project.app.MyApplication;
import com.project.constane.Constant;
import com.project.domain.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactView {
	private Activity mActivity;
	private Context mContext;
	
	private ContactAdapter adapter;
	private List<User> contactList;
	private ListView listView;
	private boolean hidden;
	private Sidebar sidebar;
	private InputMethodManager inputMethodManager;
	
	private View contactView;
	
	
	public ContactView(Activity mActivity, Context mContext){
		this.mActivity = mActivity;
		this.mContext = mContext;
		
		contactView = LayoutInflater.from(mContext).inflate(R.layout.comm_contact_list, null);
		
		setUpView();
		
		setListner();
	}
	
	private void setUpView() {
		listView = (ListView) contactView.findViewById(R.id.list);
		sidebar = (Sidebar) contactView.findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		contactList = new ArrayList<User>();
		// 获取设置contactlist
		getContactList();
		
		// 设置adapter
		if(contactList!=null && contactList.size()>0){
			adapter = new ContactAdapter(mActivity, R.layout.row_contact, contactList, sidebar);
			listView.setAdapter(adapter);
		}
	}
	

	private void setListner() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String username = adapter.getItem(position).getUsername();
				String nick = adapter.getItem(position).getNickname();
				if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
					// 进入申请与通知页面
					User user = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
					user.setUnreadMsgCount(0);
					mActivity.startActivity(new Intent(mActivity, NewFriendsMsgActivity.class));
				} else if (Constant.GROUP_USERNAME.equals(username)) {
					// 进入群聊列表页面
//					mActivity.startActivity(new Intent(mActivity, GroupsActivity.class));
				} else {
					// demo中直接进入聊天页面，实际一般是进入用户详情页
					System.out.println("userId---:"+ nick);
					Intent intent = new Intent(mActivity, ChatActivity.class);
					intent.putExtra("userId",username);
					intent.putExtra("usernic",nick);
					mActivity.startActivity(intent);
				}
			}
		});
		
//		listView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// 隐藏软键盘
//				if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
//					if (mActivity.getCurrentFocus() != null)
//						inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
//								InputMethodManager.HIDE_NOT_ALWAYS);
//				}
//				return false;
//			}
//		});
	}
	
	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					System.out.println("contact refresh");
					getContactList();
					adapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getContactList() {
		contactList.clear();
		Map<String, User> users = MyApplication.getInstance().getContactList();
		System.out.println("nummmmm---:"+users.size());
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
//			System.out.println("entry--:"+entry.getValue().getUsername());
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME))
				contactList.add(entry.getValue());
		}
		// 排序
		Collections.sort(contactList, new Comparator<User>() {

			@Override
			public int compare(User lhs, User rhs) {
				return lhs.getUsername().compareTo(rhs.getUsername());
			}
		});

//		// 加入"申请与通知"和"群聊"
		contactList.add(0, users.get(Constant.GROUP_USERNAME));
		// 把"申请与通知"添加到首位
		contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
	}
	
	
	public View getView(){
		return contactView;
	}
	
}
