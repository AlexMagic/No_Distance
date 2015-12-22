package com.project.activity.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public class SwitchButton extends View implements OnClickListener{

	private Bitmap mSwitchButton , mSwitchFrame , mSwitchMask , mSwitchThumb;
	
	public SwitchButton(Context context) {
		super(context);
		init(context , null,0);
	}
	
	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context , attrs , 0);
	}
	
	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context , attrs , defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		
		
	}

	@Override
	public void onClick(View v) {
		
	}

}
