package com.project.sendfromposition;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.project.R;
import com.project.util.ImageCache;

public class MyScrollView extends ScrollView{

	
	private static final int PAGE_SIZE = 15;
	
	private int curPage ;
	
	//每一列的宽度
	private int columWidth;
	
	//每一项的高度
	private int itemHeight;
	
	//当前的第一列的高度
	private int curFirstHeight;
	//当期的第二列的高度
	private int curSecondHeight;
	
	private LinearLayout firstColum;
	private LinearLayout secondColum;
	
	//图片缓存管理工具
	private ImageCache mImageCache;
	//是否已加载过一次onLayout 
	private boolean isLoadOnce;
	
	//MyScrollView下的子布局
	private View scrollView;
	
	/** 
     * 记录所有正在下载或等待下载的任务。 
     */  
//    private static Set<LoadImageTask> taskCollection;  
	
	/** 
     * MyScrollView布局的高度。 
     */  
    private static int scrollViewHeight; 
    
    /** 
     * 记录上垂直方向的滚动距离。 
     */  
    private static int lastScrollY = -1;  
  
    /** 
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。 
     */  
    private List<ImageView> viewList = new ArrayList<ImageView>();  
	
	
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		// TODO Auto-generated method stub
//		super.onLayout(changed, l, t, r, b);
//		if(changed && !isLoadOnce){
//			scrollViewHeight = getHeight();
//			scrollView = getChildAt(0);
//			firstColum = (LinearLayout) findViewById(R.id.first_colum);
//			secondColum = (LinearLayout) findViewById(R.id.second_colum);
//			columWidth = firstColum.getHeight();
//			isLoadOnce = true;
//			//开始加载第一页的内容
//		}
//	}
	
	

}
