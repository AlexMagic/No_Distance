package com.project.entertainment;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;

import com.project.R;
//import com.project.activity.base.FlipperLayout.OnOpenListener;
import com.project.activity.base.FlipperLayout1.OnOpenListener;

import com.project.activity.base.ScrollLayout;

public class EnterTainMent implements OnClickListener{
	
	private final static int SCHOOL_ENTERT = 0;
	private final static int NEAR_ENTERT = 1;
	private final static int SELECT_ACADEMY = 2;
	private final static int CLOSE_WINDOW = 3;
	
	private Activity activity;
	private Context context;
	
	private View mEnterTainMent;
	private View mSelAcaView;
	
	private PopupWindow mPopWindow;
	
	private ScrollLayout mScrollLayout;
	
	private ImageView mFlip;
	private Button mSchoolEntert;
	private Button mNearEntert;
	private Button mSelAcademy;
	private Button mClosePopWindow;
	
	
	private ListView listview;
	
	private boolean isChange = false;
	private boolean isSelAca = false;
	
	private ArrayList<SchoolEntertListItem> list = new ArrayList<SchoolEntertListItem>();
	
	private int mViewCount;
	
	private OnOpenListener mOnOpenListener;
	
	public EnterTainMent(Activity activity , Context context){
		this.activity = activity;
		this.context = context;
		
		init();
		
		setListner();
	}
	
	
	private void init(){
		mEnterTainMent = LayoutInflater.from(context).inflate(R.layout.entertainment, null);
		mSelAcaView = LayoutInflater.from(context).inflate(R.layout.entert_popwindow, null);
		
		mFlip  = (ImageView) mEnterTainMent.findViewById(R.id.btn_goto_residenmenu);
		
		mScrollLayout = (ScrollLayout) mEnterTainMent.findViewById(R.id.srocll_layout);
		LinearLayout linearLayout = (LinearLayout) mEnterTainMent.findViewById(R.id.tab_layout);
		
		
		mViewCount = mScrollLayout.getChildCount();
//		mBtnTab = new Button[mViewCount];
		
		mSelAcademy = (Button) mEnterTainMent.findViewById(R.id.select_aca);
		mSelAcademy.setId(SELECT_ACADEMY);
		
		mSchoolEntert = (Button) linearLayout.findViewById(R.id.school_entert);
		mSchoolEntert.setId(SCHOOL_ENTERT);
		
		mNearEntert = (Button) linearLayout.findViewById(R.id.near_entert);
		mNearEntert.setId(NEAR_ENTERT);
		
		mClosePopWindow = (Button) mSelAcaView.findViewById(R.id.pop_close);
		mClosePopWindow.setId(CLOSE_WINDOW);
	
		mPopWindow = new PopupWindow(mSelAcaView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setAnimationStyle(R.style.ModePopupAnimation);
		
		listview = (ListView) mEnterTainMent.findViewById(R.id.school_entert_list);
		SchoolEntertAdapter adapter = new SchoolEntertAdapter(context, getList());
		listview.setAdapter(adapter);
		setListHeight(listview);
	
	}
	
	
	private void setListner() {
		
		mSchoolEntert.setOnClickListener(this);
		mNearEntert.setOnClickListener(this);
		mSelAcademy.setOnClickListener(this);
		mClosePopWindow.setOnClickListener(this);
		
		mFlip.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
	}
	
	
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	public View getView(){
		return mEnterTainMent;
	}


	public ArrayList<SchoolEntertListItem> getList(){
		SchoolEntertListItem item = new SchoolEntertListItem();
		
		item.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item);
		
		SchoolEntertListItem item1 = new SchoolEntertListItem();
		
		item1.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item1.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item1.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item1);
		
		SchoolEntertListItem item2 = new SchoolEntertListItem();
		
		item2.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item2.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item2.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item2);
		
		SchoolEntertListItem item3 = new SchoolEntertListItem();
		
		item3.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item3.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item3.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item3);
		
		SchoolEntertListItem item4 = new SchoolEntertListItem();
		
		item4.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item4.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item4.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item4);
		
		SchoolEntertListItem item5 = new SchoolEntertListItem();
		
		item5.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item5.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item5.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item5);
		
		SchoolEntertListItem item6 = new SchoolEntertListItem();
		
		item6.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item6.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item6.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item6);
		
		SchoolEntertListItem item7 = new SchoolEntertListItem();
		
		item7.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item7.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item7.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item7);
		
		SchoolEntertListItem item8 = new SchoolEntertListItem();
		
		item8.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item8.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item8.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item8);
		
		SchoolEntertListItem item9 = new SchoolEntertListItem();
		
		item9.setAcademyLogo(R.drawable.btn_school_service_selector+"");
		item9.setAcademyName("¼ÆËã»ú¹¤³ÌÑ§ÔºÊ®´ó¸èÊÖ");
		item9.setAcademyNews("¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ....¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ");
		list.add(item9);
		
		return list;
	}
	
	//¼ÆËã³ölistviewµÄ¸ß¶È £¬ ½â¾öscrollviewµÄ³åÍ»
	private void setListHeight(ListView listview){
		ListAdapter listAdapter = listview.getAdapter();
		
		if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()·µ»ØÊý¾ÝÏîµÄÊýÄ¿   
            View listItem = listAdapter.getView(i, null, listview);   
            // ¼ÆËã×ÓÏîView µÄ¿í¸ß   
            listItem.measure(0, 0);    
            // Í³¼ÆËùÓÐ×ÓÏîµÄ×Ü¸ß¶È   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listview.getLayoutParams();   
        params.height = totalHeight+ (listview.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()»ñÈ¡×ÓÏî¼ä·Ö¸ô·ûÕ¼ÓÃµÄ¸ß¶È   
        // params.height×îºóµÃµ½Õû¸öListViewÍêÕûÏÔÊ¾ÐèÒªµÄ¸ß¶È    
        listview.setLayoutParams(params);   
	}
	
	
	
	public void initPopWindow(){
		
		if(mPopWindow == null){
			mPopWindow = new PopupWindow(mSelAcaView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
			mPopWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopWindow.setAnimationStyle(R.style.ModePopupAnimation);
		}else if(mPopWindow.isShowing()){
			mPopWindow.dismiss();
		}else{
			mPopWindow.showAtLocation(mEnterTainMent, Gravity.NO_GRAVITY , 0, 0);
		}
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case SCHOOL_ENTERT:
			if(isChange){
				mScrollLayout.snapToScreen(SCHOOL_ENTERT);
				mSchoolEntert.setBackgroundResource(R.drawable.tab_selected_pressed);
				mNearEntert.setBackgroundResource(R.drawable.tab_selected);
				isChange = false;
			}
			break;
			
		case NEAR_ENTERT:
			if(!isChange){
				mScrollLayout.snapToScreen(NEAR_ENTERT);
				mSchoolEntert.setBackgroundResource(R.drawable.tab_selected);
				mNearEntert.setBackgroundResource(R.drawable.tab_selected_pressed);
				isChange = true;
			}
			break;
			
		case SELECT_ACADEMY:
			
			Animation translateLeft=AnimationUtils.loadAnimation(context, R.anim.translate_left);
			translateLeft.setFillAfter(true);
			translateLeft.setFillBefore(true);
			
			
//			mSelAcademy.startAnimation(translateLeft);
			initPopWindow();
			
			break;
			
		case CLOSE_WINDOW:
			Animation translateRight=AnimationUtils.loadAnimation(context, R.anim.translate_right);
			translateRight.setFillAfter(true);
			translateRight.setFillBefore(true);
			
			mPopWindow.dismiss();
//			mSelAcademy.startAnimation(translateRight);
		}
	}

	
}
