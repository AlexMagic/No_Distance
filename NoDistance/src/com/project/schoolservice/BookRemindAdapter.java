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

public class BookRemindAdapter extends BaseAdapter{

	private Context context;
	private List<RemindBack> list;
	
	private static AlarmManager manager;
	private static PendingIntent pi;
	
	private static Map<Integer , Boolean> isSelected;
	
	private static boolean isSel;
	
//	private int pos;
	
	public BookRemindAdapter(Context context , List<RemindBack> list , boolean istrue,
			AlarmManager manager , PendingIntent pi){
		this.context = context;
		this.list = list;
		this.manager =manager;
		this.pi = pi;
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
	public RemindBack getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder v = null;
		
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.book_remind_item, null);
			v= new ViewHolder();
			v.bookname = (TextView) convertView.findViewById(R.id.bookname);
			v.remind_time = (TextView) convertView.findViewById(R.id.tv_time);
			v.checkBox = (CheckBox) convertView.findViewById(R.id.item_cb);
			v.iv_checked = (ImageView) convertView.findViewById(R.id.iv_checked);
			v.iv_set_remind = (ImageView) convertView.findViewById(R.id.iv_remind);
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		
		
		v.bookname.setText(list.get(position).getBookname());
		v.remind_time.setText(list.get(position).getRemindTime());
		
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
		TextView bookname;
		TextView remind_time;
		CheckBox checkBox;
		ImageView iv_checked;
		ImageView iv_set_remind;
	}
	
	
	public static void setIsSel(boolean isSel){
		BookRemindAdapter.isSel = isSel;
	}
	
	public static boolean getIsSel(){
		return isSel;
	}
	
	
	public void setIsSelected(Map<Integer , Boolean> isSelected){
		BookRemindAdapter.isSelected = isSelected;
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
						RemindBack rb = getItem(position);
						
						RemindBookDao rbDao = new RemindBookDao(context);
						
//						RemindBack rb = new RemindBack();
						
						String time = (String.format("%d-%02d-%02d",
								datePicker.getYear(),
								datePicker.getMonth() + 1,
								datePicker.getDayOfMonth()));
						
						
						
						rb.setBookname(rb.getBookname());
						rb.setRemindTime(time);
						
						System.out.println(rb.getBookname());
						
						rbDao.saveRemind(rb);
						
						System.out.println("bk.getBookname()---:"+rb.getBookname()+"----time:  "+time);

//						int id = rbDao.getRemindId(rb.getBookname());
						
						Intent intent = new Intent(context , MyReceiver.class);
						Bundle bundle = new Bundle();
						bundle.putString("book_name", rb.getBookname());
						bundle.putString("remind_time", rb.getRemindTime());
//						bundle.putInt("requestCode", id);
						intent.putExtras(bundle);
						manager = getAlerm();
						setAlerm(manager);
						pi = getPi();
//						setPendingIntent(pi);
						// 过10s 执行这个闹铃  
//						Calendar calendar = Calendar.getInstance();  
//						calendar.setTimeInMillis(System.currentTimeMillis());  
//						calendar.add(Calendar.SECOND, 10);  
						
						manager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() , pi);
						
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
	
	public  void setAlerm(AlarmManager manager){
		this.manager = manager;
	}
	
	public static AlarmManager getAlerm(){
		return manager;
	}
	
}
