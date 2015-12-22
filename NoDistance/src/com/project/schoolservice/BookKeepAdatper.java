package com.project.schoolservice;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.R;
import com.project.db.RemindBookDao;
import com.project.domain.BookKeep;
import com.project.domain.RemindBack;

public class BookKeepAdatper extends BaseAdapter{
	
	private Context context;
	private List<BookKeep> list ;
	
	private static Map<Integer , Boolean> isSelected;
	
	private static boolean isSel;
	
	private int pos;
	
	private static AlarmManager am;
	private static PendingIntent pi;
	
	public BookKeepAdatper(Context context , List<BookKeep> list , boolean istrue) {
		this.context = context;
		this.list = list;
		
		isSelected = new HashMap<Integer, Boolean>();
		isSel = istrue;
		initSel();
	}
	
	
	
	
	private void initSel() {
		for(int i=0; i<list.size() ; i++){
			getIsSelected().put(i, false);
		}
	}




	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public BookKeep getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder v = null;
//		pos = position;
		if(convertView == null){
			v = new ViewHolder();
			
			convertView =  LayoutInflater.from(context).inflate(R.layout.book_keep_item, null);  
			
			v.tv_book_name = (TextView) convertView.findViewById(R.id.bookname);
			v.tv_book_index = (TextView) convertView.findViewById(R.id.tv_index);
			v.tv_book_location = (TextView) convertView.findViewById(R.id.tv_bookloc);
			v.checkBox = (CheckBox) convertView.findViewById(R.id.item_cb);
			v.iv_checked = (ImageView) convertView.findViewById(R.id.iv_checked);
			v.iv_set_remind = (ImageView) convertView.findViewById(R.id.iv_remind);
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		v.tv_book_name.setText(list.get(position).getBookname());;
		v.tv_book_index.setText(list.get(position).getBookindex());;
		v.tv_book_location.setText(list.get(position).getBookloca());
		v.checkBox.setChecked(getIsSelected().get(position));
		if(v.checkBox.isChecked()==false){
			v.iv_checked.setVisibility(View.GONE);
		}else{
			v.iv_checked.setVisibility(View.VISIBLE);
		}
		if(!isSel){
			v.iv_set_remind.setVisibility(View.VISIBLE);
			Animation alpha = new AlphaAnimation(0.0f,1.0f);
	        alpha.setDuration(500);
	        v.iv_set_remind.startAnimation(alpha);
			
			v.iv_set_remind.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					remind(position);
				}
			});
		}else{
			Animation alpha = new AlphaAnimation(1.0f,0.0f);
	        alpha.setDuration(500);
	        v.iv_set_remind.startAnimation(alpha);
			v.iv_set_remind.setVisibility(View.GONE);
			v.iv_set_remind.setClickable(false);
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_book_name;
		TextView tv_book_index;
		TextView tv_book_location;
		CheckBox checkBox;
		ImageView iv_checked;
		ImageView iv_set_remind;
	}
	
	public static void setIsSel(boolean isSel){
		BookKeepAdatper.isSel = isSel;
	}
	
	public static boolean getIsSel(){
		return isSel;
	}
	
	
	public void setIsSelected(Map<Integer , Boolean> isSelected){
		BookKeepAdatper.isSelected = isSelected;
	}
	
	public static Map<Integer , Boolean> getIsSelected(){
		return isSelected;
	}
	
	public void remind(final int position){
		System.out.println("position------:"+position);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = View.inflate(context, R.layout.get_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
//		final TimePicker timePicker=(TimePicker)view.findViewById(R.id.timePicker1);
		builder.setView(view);

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), null);
		
		builder.setTitle("选取起始时间");
		builder.setPositiveButton("确  定",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BookKeep bk = getItem(position);
						
						RemindBookDao rbDao = new RemindBookDao(context);
						
						RemindBack rb = new RemindBack();
						
						String time = (String.format("%d-%02d-%02d",
								datePicker.getYear(),
								datePicker.getMonth() + 1,
								datePicker.getDayOfMonth()));
						
						
						
						rb.setBookname(bk.getBookname());
						rb.setRemindTime(time);
						
						System.out.println(rb.getBookname());
						
						rbDao.saveRemind(rb);
						
						System.out.println("bk.getBookname()---:"+bk.getBookname()+"----time:  "+time);

						int id = rbDao.getRemindId(rb.getBookname());
						
						Intent intent = new Intent(context , MyReceiver.class);
						Bundle bundle = new Bundle();
						bundle.putString("book_name", bk.getBookname());
						bundle.putString("remind_time", rb.getRemindTime());
						bundle.putInt("requestCode", id);
						intent.putExtras(bundle);
						am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
						setAlerm(am);
						pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
						setPendingIntent(pi);
						// 过10s 执行这个闹铃  
//						Calendar calendar = Calendar.getInstance();  
//						calendar.setTimeInMillis(System.currentTimeMillis());  
//						calendar.add(Calendar.SECOND, 10);  
						
						am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() , pi);
						
						//editor.putInt("num", i);//设置提醒的序号
						//editor.putString("notetime", number+i+"#");//设置提示时间
//							editor.putBoolean("warn", false);
//							editor.commit();
						Toast.makeText(context, "添加提醒成功",Toast.LENGTH_SHORT).show();
						dialog.cancel();
						}
						
				});

		Dialog dialog = builder.create();
		dialog.show();
	}
	
	public void setPendingIntent(PendingIntent pi){
		this.pi = pi;
	}
	
	public static PendingIntent getPi(){
		return pi;
	}
	
	public void setAlerm(AlarmManager am){
		this.am = am;
	}
	
	public static AlarmManager getAlerm(){
		return am;
	}
}
