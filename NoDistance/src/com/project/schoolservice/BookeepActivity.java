package com.project.schoolservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.project.R;
import com.project.db.KeepBookDao;
import com.project.domain.BookKeep;
import com.project.schoolservice.BookKeepAdatper.ViewHolder;

public class BookeepActivity extends Activity implements OnClickListener , OnGestureListener{
	
	private  static final int FLING_MIN_DISTANCE = 100;
	
	private GestureDetector mGestureDetector;
	
	private LinearLayout bar_bottom;
	private ImageView iv_bin;
	private Button btn_sel_all;
	private Button btn_sel_cancel;
	private Button btn_delete;
	private ListView listview;
	private ImageView img_back;
	private BookKeepAdatper adapter ;
	
	private boolean isClick = false;
	
	private Map<String , String> infos = new HashMap<String, String>();
	private BookKeep bookKeep;
	
	private List<BookKeep> booklist;
	
	private List<String> selBookName = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_keep);
		overridePendingTransition(R.anim.translate_right, R.anim.translate_left);
		mGestureDetector = new GestureDetector(this, this);
		
		getBookInfo();
		
		setUpView();
		
		setListner();

	}
	
	public void setUpView(){
		bar_bottom = (LinearLayout)findViewById(R.id.bar_bottom);
		iv_bin = (ImageView) findViewById(R.id.iv_clean);
		btn_sel_all = (Button) findViewById(R.id.btn_selall);
		btn_sel_cancel = (Button) findViewById(R.id.btn_sel_cancel);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		img_back = (ImageView) findViewById(R.id.img_back);
		
		listview = (ListView) findViewById(R.id.book_list);
		adapter = new BookKeepAdatper(this, booklist, false);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setAdapter(adapter);
	}
	
	public void setListner(){
		iv_bin.setOnClickListener(this);
		img_back.setOnClickListener(this);
//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				
////				bookKeep = view.get
//								
//				ViewHolder v = (ViewHolder) view.getTag();
//				v.checkBox.toggle();
//				BookKeepAdatper.getIsSelected().put(position, v.checkBox.isChecked());
//				if(v.checkBox.isChecked()==false){
//					v.iv_checked.setVisibility(View.GONE);
//					bookKeep = (BookKeep)listview.getItemAtPosition(position);
////					System.out.println("ischeck--false-:"+bookKeep.getBookname());
//					infos.remove(bookKeep.getBookname());
//				}else{
//					v.iv_checked.setVisibility(View.VISIBLE);
//					bookKeep = (BookKeep)listview.getItemAtPosition(position);
////					System.out.println("ischeck--true-:"+bookKeep.getBookname());
//					infos.put(bookKeep.getBookname(), bookKeep.getBookname());
//				}
//			}
//		});
	}
	
	
	public void getBookInfo(){
		KeepBookDao kb = new KeepBookDao(BookeepActivity.this);
		Map<String , BookKeep > infos = kb.getBookInfo();
		booklist = new ArrayList<BookKeep>(infos.values());
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_clean:
			if(!isClick){
				Animation alpha1 = new AlphaAnimation(0.0f,1.0f);
		        alpha1.setDuration(600);
		        bar_bottom.setAnimation(alpha1);
		        bar_bottom.startAnimation(alpha1);
		        bar_bottom.setVisibility(View.VISIBLE);
		       
		        btn_sel_all.setOnClickListener(this);
		        
		        btn_sel_cancel.setOnClickListener(this);
		        
		        btn_delete.setOnClickListener(this);
		        
//		        BookKeepAdatper adapter = new BookKeepAdatper(this, booklist , true);
		        BookKeepAdatper.setIsSel(true);
		        listview.setItemsCanFocus(false);
				listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						
//						bookKeep = view.get
										
						ViewHolder v = (ViewHolder) view.getTag();
						v.checkBox.toggle();
						BookKeepAdatper.getIsSelected().put(position, v.checkBox.isChecked());
						if(v.checkBox.isChecked()==false){
							v.iv_checked.setVisibility(View.GONE);
							bookKeep = (BookKeep)listview.getItemAtPosition(position);
//							System.out.println("ischeck--false-:"+bookKeep.getBookname());
							infos.remove(bookKeep.getBookname());
						}else{
							v.iv_checked.setVisibility(View.VISIBLE);
							bookKeep = (BookKeep)listview.getItemAtPosition(position);
//							System.out.println("ischeck--true-:"+bookKeep.getBookname());
							infos.put(bookKeep.getBookname(), bookKeep.getBookname());
						}
					}
				});
		        isClick = true;
			}else{
				
				 AnimationSet set=new AnimationSet(true);
				 Animation alpha = new AlphaAnimation(1.0f,0.0f);
				 Animation tran = new TranslateAnimation(0, 0, 0, 300);
				 set.addAnimation(alpha);
				 set.addAnimation(tran);
				 set.setDuration(600);
				 set.setFillAfter(true);                 //停留在最后的位置  
		         set.setFillEnabled(true);

		         bar_bottom.setAnimation(set);
		         bar_bottom.startAnimation(set);
		         bar_bottom.setVisibility(View.GONE);
//		         btn_sel_all.setOnClickListener(null);
//		         btn_sel_cancel.setOnClickListener(null);
		         btn_sel_all.setClickable(false);
		         btn_sel_cancel.setClickable(false);
		         btn_delete.setClickable(false);
		         listview.setOnItemClickListener(null);
		         BookKeepAdatper.setIsSel(false);
		         adapter.notifyDataSetChanged();
		         for(int i=0;i<booklist.size();i++){
						BookKeepAdatper.getIsSelected().put(i, false);
		         }
				 infos.clear();
//				 refresh();
		         isClick = false;
			}
	        break;
	        
		case R.id.btn_sel_cancel:
			System.out.println("取消 --："+infos.size());
			for(int i=0;i<booklist.size();i++){
				BookKeepAdatper.getIsSelected().put(i, false);
			}
			infos.clear();
			refresh();
			break;
		
		case R.id.btn_selall:
			System.out.println("全选 ："+infos.size());
			for(int i=0;i<booklist.size();i++){
				BookKeepAdatper.getIsSelected().put(i, true);
				bookKeep = (BookKeep)listview.getItemAtPosition(i);
				infos.put(bookKeep.getBookname(), bookKeep.getBookname());
			}
			refresh();
			break;
		case R.id.btn_delete:
			
			if(infos!=null && infos.size()>0){
				KeepBookDao kb = new KeepBookDao(BookeepActivity.this);
				selBookName.addAll(infos.values());
				
				
				
				kb.deleteKeeping(selBookName);
				selBookName.clear();
				
				getBookInfo();
				adapter = new BookKeepAdatper(this,booklist , false);
				listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(BookeepActivity.this, "请选择你要删除的书籍", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.img_back:
			finish();
			break;
		}
	}
	
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE) {
			finish();
			overridePendingTransition(R.anim.translate_right, R.anim.translate_left);
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public void refresh(){
		getBookInfo();
//		adapter = new BookKeepAdatper(this,booklist , false);
//		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
