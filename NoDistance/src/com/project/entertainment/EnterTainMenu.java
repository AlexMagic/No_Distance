package com.project.entertainment;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.project.R;

public class EnterTainMenu extends FrameLayout{

	private ImageView imgMenu;
	private Animation menuAnimaitonUp , menuAnimationDown;
	private AtomicBoolean plusAnimationActive = new AtomicBoolean(false);
	private boolean trans = false;
	
	
	public EnterTainMenu(Context context) {
		super(context);
		init(context , null , 0);
	}
	
	
	public EnterTainMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context , attrs , 0);
	}
	
	public EnterTainMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context , attrs , defStyle);
	}
	
	private void init(Context context, AttributeSet attrs, int defStyle){
		LayoutInflater.from(context).inflate(R.layout.enter_menu, this, true);
		imgMenu = (ImageView) findViewById(R.id.enterain_menu);
		
		menuAnimaitonUp = EnterTainAnim.menuAnimUp(context);
		menuAnimationDown = EnterTainAnim.menuAnimDown(context);
		
		//¶¯»­¼àÌý
		AnimationListener menuLinstener = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				plusAnimationActive.set(false);
			}
		};
		
		menuAnimaitonUp.setAnimationListener(menuLinstener);
		menuAnimationDown.setAnimationListener(menuLinstener);
		
		imgMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EnterTainMenu.this.onClick();
			}
		});
	}

	protected void onClick() {
		// TODO Auto-generated method stub
		if (plusAnimationActive.compareAndSet(false, true)){
			if(!trans){
				imgMenu.startAnimation(menuAnimaitonUp);
			}else{
				imgMenu.startAnimation(menuAnimationDown);
			}
			
			trans = !trans;
		}
	}
	
	
	
}
