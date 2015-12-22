package com.project.activity.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.project.R;
import com.project.communicate.CommuModeAdapter;

public class MenuForCommuPopWindow extends PopupWindow {
	
	private Activity activity;
	
	private View mMenuView;
	private LinearLayout[] commuLayouts;
	private ListView listview;
	
	private String[] itemName = {"     聊天吧" ,"     通讯录" };
	private int[] icons = {
			R.drawable.ofm_profile_icon , 
			R.drawable.ofm_add_icon , 
			R.drawable.ofm_qrcode_icon , 
			R.drawable.ofm_card_icon};
	
	private int mChooseId=0;
	
	private OnItemClickListener itemsOnClick;
	
	
	public MenuForCommuPopWindow( Activity activity ,OnItemClickListener itemsOnClick , int chooseId){
		super(activity);
		
		this.activity = activity;
		this.itemsOnClick = itemsOnClick;
		
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mMenuView = inflater.inflate(R.layout.commu_popwindowxml, null);
		
//		listview = (ListView) mMenuView.findViewById(R.id.mode_pop_list);
//		CommuModeAdapter adapter = new CommuModeAdapter(activity, itemName, mChooseId);
//		listview.setAdapter(adapter);
		
		//当前activity的宽高
		int h = activity.getWindowManager().getDefaultDisplay().getHeight(); 
		int w = activity.getWindowManager().getDefaultDisplay().getWidth();
		
		//设置popwindow的View
		this.setContentView(mMenuView);
		//设置弹出窗体的宽高
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置弹出窗口可点击
		this.setFocusable(true);
		
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		

		
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				int height = mMenuView.findViewById(R.id.mode_pop_list).getTop();
				int y = (int) event.getY();

//				System.out.println(v.getId());
				
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}
				
				return true;
			}
		});
		
//		listview.setOnItemClickListener(itemsOnClick);
		
	}
	
	public void popListView(int mChooseId){
		
		listview = (ListView) mMenuView.findViewById(R.id.mode_pop_list);
		CommuModeAdapter adapter = new CommuModeAdapter(activity, itemName , icons, mChooseId);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(itemsOnClick);
	}
	
	public void setChooseId(int id){
		mChooseId = id;
	}
	
	public ListView getPopView(){
		return listview;
	}
	
}
