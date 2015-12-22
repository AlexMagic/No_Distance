package com.project.sendfromposition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.TokenGenerator;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.util.OSSToolKit;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.project.R;
import com.project.sendfromposition.WritePlaceActivity.GridAdapter.ViewHolder;

public class PLaceListAdapter extends BaseAdapter {

	public static final String accessKey = "03KYB2AtkMSC0NQE"; 			// 测试代码没有考虑AK/SK的安全性
    public static final String screctKey = "fBgtjQ9vlmdqxeGcylXQidbmFC7pIe";
	
    private final static String OSS_URL = "http://test327393059.oss-cn-shenzhen.aliyuncs.com/";
    
	private Context mContext;
	private ArrayList<NearListItem> list;
	private GridViewAdapter mAdapter; 
	private ArrayList<String> picPath;
	
	public OSSBucket bucket;
	
	private DisplayImageOptions options;
	
	public PLaceListAdapter(Context mContext , ArrayList<NearListItem> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		picPath = new ArrayList<String>();
		
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer())
			.build();
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public NearListItem getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final NearListItem item  = list.get(position);
		picPath = item.getPicPath();
		ViewHolder v = null;
		
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.to_place_list_item, null);
			v = new ViewHolder();
			
			v.user_head = (ImageView) convertView.findViewById(R.id.user_head);
			v.tv_invite_place = (TextView) convertView.findViewById(R.id.tv_place);
			v.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
			v.tv_invite_content = (TextView) convertView.findViewById(R.id.tv_invite);
			v.tv_time = (TextView) convertView.findViewById(R.id.tv_invite_time);
			v.tv_temp = (View) convertView.findViewById(R.id.temp);
			v.tv_temp.setVisibility(View.VISIBLE);
			v.gridView = (GridView) convertView.findViewById(R.id.noScrollgridview);
			convertView.setTag(v);
		}else{
			v = (ViewHolder) convertView.getTag();
		}
		
		v.tv_invite_place.setText(item.getMeetingPos());
		v.tv_invite_content.setText(item.getMeetingContent());
		v.tv_username.setText(item.getAppID());
		
		
		
		Date start = new Date(Long.parseLong(item.getDateTime()));
		Date end = new Date(System.currentTimeMillis());
		final String time = twoDateDistance(start, end);
		
		v.tv_time.setText(time);
		
		
		if(picPath.size()==0 || picPath==null){
			v.tv_temp.setVisibility(View.GONE);
		}else{
			mAdapter = new GridViewAdapter(mContext , picPath);
			v.gridView.setAdapter(mAdapter);
		}

		
//		v.user_head.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext , UserInfoActivity.class);
//				intent.putExtra("appID", item.getAppID());
//				mContext.startActivity(intent);
//			}
//		});
		
		 
		
		
//		v.gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
//				
//			}
//		});
		
		return convertView;
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

	class ViewHolder{
		ImageView user_head;
		View tv_temp;
		TextView tv_username;
		TextView tv_invite_place;
		TextView tv_invite_content;
		TextView tv_time;
		GridView gridView;
	}
	
	
	class GridViewAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		private ArrayList<String> picPath;
		
		public GridViewAdapter(Context mContext , ArrayList<String> picPath){
			inflater = LayoutInflater.from(mContext);
			this.picPath = picPath;
			System.out.println("listLen-------:"+picPath.size());
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return picPath.size();
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return picPath.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			
			if(convertView==null){
				convertView = inflater.inflate(R.layout.item_published_grida, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			for(int i=0;i<picPath.size();i++){
//				System.out.println("imgUrl-------:"+picPath.get(i));
				ImageLoader.getInstance().displayImage(OSS_URL+picPath.get(i), holder.image,options);
			}
			
			return convertView;
		}
		
		class ViewHolder{
			ImageView image;
		}
	}
}
