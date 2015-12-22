package com.project.activity.base;



import android.content.Context;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup{

	 private VelocityTracker mVelocityTracker;  			  
	 private static final int SNAP_VELOCITY = 400;        
	 private Scroller  mScroller;							
	 private int mCurScreen;    		//当前的viewNum
	 private int mDefaultScreen = 0;    //默认的第一个viewNum						 
	 private float mLastMotionX;       
	 private float mLastMotionY;       
	    
	 private boolean isPass = false;
	
	private OnViewChangeListener mOnViewChangeListener;
	
	
	public ScrollLayout(Context context) {
		super(context);
		init(context);
	}
	
	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context){
		mCurScreen = mDefaultScreen;
		
		mScroller = new Scroller(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			int childLeft = 0;
			final int childCount = getChildCount();
			
			for(int i=0; i<childCount ; i++){
				final View childView  = getChildAt(i);
				if(childView.getVisibility() != View.GONE){
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft+childWidth, childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height =  MeasureSpec.getSize(heightMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec); 
		final int count = getChildCount();
		
		setMeasuredDimension(width, height);
		
		for(int i=0 ; i<count ; i++){
			 getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);  
		}
		scrollTo(mCurScreen * width, 0);
	}
	
	public void snapToDestination() {    
        final int screenWidth = getWidth();    
        final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;    
        snapToScreen(destScreen);    
 }  

 public void snapToScreen(int whichScreen) {    	
        // get the valid layout page    
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));    
        if (getScrollX() != (whichScreen*getWidth())) {    	                
            final int delta = whichScreen*getWidth()-getScrollX();    
      	           
            mScroller.startScroll(getScrollX(), 0, delta, 0, 300);
            
            mCurScreen = whichScreen;    
            invalidate();       // Redraw the layout    	            
            if (mOnViewChangeListener != null)
            {
            	mOnViewChangeListener.OnViewChange(mCurScreen);
            }
        }    
    }    
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {    
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();    
        }   
	}
	
	public interface OnViewChangeListener{
		public void OnViewChange(int view);
	}
	
	public void SetOnViewChangeListener(OnViewChangeListener listener)
	{
		mOnViewChangeListener = listener;
	}

}
