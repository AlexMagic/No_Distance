package com.project.activity.base;

import com.project.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class SelAcademyPopWindow extends PopupWindow{
	
	private Activity activity;
	
	private View mSelView;
	
	public SelAcademyPopWindow(Activity activity ){
		this.activity = activity;
		
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mSelView = inflater.inflate(R.layout.entert_popwindow, null);
		
		//当前activity的宽高
		int h = activity.getWindowManager().getDefaultDisplay().getHeight(); 
		int w = activity.getWindowManager().getDefaultDisplay().getWidth();
		
		//设置popwindow的View
		this.setContentView(mSelView);
		//设置弹出窗体的宽高
		this.setWidth(w/2+50);
		this.setHeight(LayoutParams.FILL_PARENT);
		//设置弹出窗口可点击
		this.setFocusable(true);
		
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		
//		mSelView.seto
	}
	
}
