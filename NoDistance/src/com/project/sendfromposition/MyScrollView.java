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
	
	//ÿһ�еĿ��
	private int columWidth;
	
	//ÿһ��ĸ߶�
	private int itemHeight;
	
	//��ǰ�ĵ�һ�еĸ߶�
	private int curFirstHeight;
	//���ڵĵڶ��еĸ߶�
	private int curSecondHeight;
	
	private LinearLayout firstColum;
	private LinearLayout secondColum;
	
	//ͼƬ���������
	private ImageCache mImageCache;
	//�Ƿ��Ѽ��ع�һ��onLayout 
	private boolean isLoadOnce;
	
	//MyScrollView�µ��Ӳ���
	private View scrollView;
	
	/** 
     * ��¼�����������ػ�ȴ����ص����� 
     */  
//    private static Set<LoadImageTask> taskCollection;  
	
	/** 
     * MyScrollView���ֵĸ߶ȡ� 
     */  
    private static int scrollViewHeight; 
    
    /** 
     * ��¼�ϴ�ֱ����Ĺ������롣 
     */  
    private static int lastScrollY = -1;  
  
    /** 
     * ��¼���н����ϵ�ͼƬ�����Կ�����ʱ���ƶ�ͼƬ���ͷš� 
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
//			//��ʼ���ص�һҳ������
//		}
//	}
	
	

}
