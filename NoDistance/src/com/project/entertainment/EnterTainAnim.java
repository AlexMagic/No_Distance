package com.project.entertainment;



import com.project.R;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

public class EnterTainAnim {
	
	public static Animation menuAnimDown(Context context ){
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.setFillAfter(true);
		
		Animation first = AnimationUtils.loadAnimation(context, R.anim.roll_down);
		Animation second = AnimationUtils.loadAnimation(context, R.anim.fade_out);
		animationSet.addAnimation(first);
		animationSet.addAnimation(second);
		animationSet.start();
		animationSet.startNow();
		
		return animationSet;
	}
	
	
	public static Animation menuAnimUp(Context context ){
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.setFillAfter(true);
		
		Animation first = AnimationUtils.loadAnimation(context, R.anim.roll_up);
		Animation second = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		
		animationSet.addAnimation(first);
		animationSet.addAnimation(second);
		animationSet.start();
		animationSet.startNow();
		
		return animationSet;
	}

}
