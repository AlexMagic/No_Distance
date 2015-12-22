package com.project.maps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.R;
import com.project.app.MyApplication;
import com.project.db.UserDao;
import com.project.util.ImageCache;

public class DriftBottleAdapter extends BaseAdapter{
	
	private static final int HANDLER_HEARTS = 0;
	private static final int HANDLER_BACKGROUND = 1;
	
	private Context mContext  ;
	private ArrayList<BottleItem> list ;
	
	private String username;
	private boolean isHeart = false; 
	private int height;
	
	
	private Bitmap mBitmap;
	
	private String from;
	private String distance;
	
	
	public DriftBottleAdapter(Context mContext , ArrayList<BottleItem> list , int height ) {
		this.mContext = mContext;
		this.list = list;
		username = MyApplication.getInstance().getUserName();
		this.height = height;
	}
	

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		System.out.println("getCount------:"+list.size());
		return list.size();
	}

	@Override
	public BottleItem getItem(int position) {
		// TODO Auto-generated method stub
		System.out.println("getItem-----position---:"+position);
		BottleItem item = list.get(position);
		System.out.println("getItem-------:"+item.getId() + "   getItem --------:"+item.getContent());
		return item;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
//		int temp = position;
		System.out.println("getItemId-----:"+position);
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if(position<list.size()){
			final BottleItem item = list.get(position);
	//		String[] goods = item.getGoods();
			System.out.println("getView-----position----:"+position);
			System.out.println("getView-------:"+item.getId() + "   getView --------:"+item.getContent());
			
			final ViewHolder v ;
			
			isHeart = item.isHeart();
			
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.driftbottle_item, null);
				v= new ViewHolder();
				
				v.tv_content = (TextView) convertView.findViewById(R.id.bottle_content);
				v.tv_heartNum = (TextView) convertView.findViewById(R.id.goods_num);
				v.tv_time = (TextView) convertView.findViewById(R.id.bottle_time);
				v.tv_distance = (TextView) convertView.findViewById(R.id.distance);
				v.iv_background = (ImageView) convertView.findViewById(R.id.iv_background);
				v.tv_isfriends = (TextView) convertView.findViewById(R.id.tv_friendsOrNot);
				
				if(isHeart)
					v.iv_heart = (ImageView) convertView.findViewById(R.id.goods);
				else
					v.iv_heart = (ImageView) convertView.findViewById(R.id.goods);
				
				convertView.setTag(v);
			}else{
				v = (ViewHolder) convertView.getTag();
			}
			
			
			                
	        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height/2);
	       
	        convertView.setLayoutParams(lp);
			
			v.tv_content.setText(item.getContent());
			
			Date start = new Date(Long.parseLong(item.getTime()));
			Date end = new Date(System.currentTimeMillis());
			final String time = twoDateDistance(start, end);
			
			v.tv_time.setText(time);
			v.tv_heartNum.setText(getHeartNum(item.getGoods())+"");
			
			
			
			if(!item.getUsername().equals(MyApplication.getInstance().getUserName())){
				distance = item.getDistance()+" 米";
				v.tv_distance.setText(item.getDistance()+" 米");
				UserDao dao = new UserDao(mContext);
				if(dao.isExist(item.getUsername())){
					from = "来自好友";
					v.tv_isfriends.setText("来自好基友");
				}else{
					from = "来自陌生人";
					v.tv_isfriends.setText("来自陌生人");
				}
			}
			else{
				distance = "自己";
				v.tv_distance.setText("自己");
			}
			
			
			v.iv_heart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					System.out.println("click");
					handlerMsg(v ,null , position , isHeart);
				}
			});
			
			//显示图片
	//		handlerImage(item.getId(), convertView , v);
			
			
	//		Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext , BottleActivity.class);
					intent.putExtra("ID", item.getId());
					intent.putExtra("username", item.getUsername());
					intent.putExtra("content", item.getContent());
					intent.putExtra("time", time);
					intent.putExtra("from", from);
					intent.putExtra("distance", distance);
					mContext.startActivity(intent);
				}
			});
		}
		
		
		
		return convertView;
	}
	
//	public boolean isClick(String[] strs ,String s){
//		for (int i = 0; i < strs.length; i++) {
//			if(strs[i].indexOf(s)!=-1)
//				return false;
//		}
//		return true;
//	}
	
	//获取该纸条的背景图片
	public void handlerImage(final int id , final View convertView , final ViewHolder v){
		String sId = id+"";
		Bitmap bitmap = ImageCache.getInstance().get(sId);
		if(bitmap!=null){
//			BitmapDrawable bd =new BitmapDrawable(mBitmap);
//			convertView.setBackgroundDrawable(bd);
			v.iv_background.setImageBitmap(bitmap);
			return ;
		}else{
			//到服务器获取图片
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
				}
			}).start();
		}
	}
	
	
	/** 
	    * 计算两个日期型的时间相差多少时间 
	    * @param startDate  开始日期 
	    * @param endDate    结束日期 
	      * @return 
	    */  
    public  String twoDateDistance(Date startDate,Date endDate){  
          
        if(startDate == null ||endDate == null){  
            return null;  
        }  
        long timeLong = endDate.getTime() - startDate.getTime();  
        if (timeLong<60*1000)  
            return timeLong/1000 + "秒前";  
        else if (timeLong<60*60*1000){  
            timeLong = timeLong/1000 /60;  
            return timeLong + "分钟前";  
        }  
        else if (timeLong<60*60*24*1000){  
            timeLong = timeLong/60/60/1000;  
            return timeLong+"小时前";  
        }  
        else if (timeLong<60*60*24*1000*7){  
            timeLong = timeLong/1000/ 60 / 60 / 24;  
            return timeLong + "天前";  
        }  
        else if (timeLong<60*60*24*1000*7*4){  
            timeLong = timeLong/1000/ 60 / 60 / 24/7;  
            return timeLong + "周前";  
        }  
        else {  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));  
            return sdf.format(startDate);  
        }  
	}  
	
	
	public void handlerMsg(final ViewHolder v , View convertView , final int position , boolean isHeart){
		
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case HANDLER_HEARTS:
					int num = msg.arg1;
					TextView heart =(TextView) msg.obj;
					heart.setText(num+"");
					break;
				}
			}
		};
		
		if(isHeart){
			BottleItem item = list.get(position);
			String good = item.getGood();
			
			good = ","+MyApplication.getInstance().getUserName();
			
			String[] goods = item.getGoods();
			final int temp = getHeartNum(goods)+1;
			//上传同时刷新UI
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
					Message msg= new Message();
					msg.obj = v.tv_heartNum;
					msg.arg1 = temp;
					msg.what = HANDLER_HEARTS;
					handler.sendMessage(msg);
				}
			}).start();
		}
	}
	
	public void update(String good){
		
	}
	
	public static class ViewHolder{
		TextView tv_content;
		ImageView iv_background;
		ImageView iv_header;
		ImageView iv_heart;
		TextView tv_heartNum;
		TextView tv_time;
		TextView tv_distance;
		TextView tv_isfriends;
	}
	
	public int getHeartNum(String[] goods){
//		System.out.println("goods.length---:"+goods.length);
		return goods.length-1;
	}
	
}
