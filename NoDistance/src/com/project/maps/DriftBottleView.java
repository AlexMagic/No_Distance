package com.project.maps;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.project.R;
//import com.project.activity.base.FlipperLayout.OnOpenListener;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
import com.project.app.MyApplication;
import com.project.util.Distance;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;

public class DriftBottleView implements OnClickListener{
	
	private static final int FIRST_LOADING = 0;
	private static final int PULL_TO_REFRESH = 1;
	private static final int CLICK_TO_LOAD = 2;
	private static final String GET_BOTTLE_CONTENT_FIRST = "select";
	private static final String PULL_REFRESH_BOTTLE_CONTENT = "selectdown";
	private static final String CLICK_BOTTLE_CONTENT = "selectup";
	
	private View mMaps;
	private View loadmore;
	private View nomore;
	
//	private FrameLayout mLayout;
	
	private Context mContext;
	private Activity mActivity;
	
	private ImageView mFlip;
	private ImageView mWriteBottle;
	private PullToRefreshListView  listview;
	private ListView actualListView ;
	private ProgressBar loading;
	private ProgressBar click_loading;
	private RelativeLayout layout;
	
	private ArrayList<BottleItem> bottleList ;
	private Map<Integer,BottleItem> bottleMap ;
	private List<LinkedHashMap<String, Object>> list;
	
	private DriftBottleAdapter adapter;
	private OnOpenListener mOnOpenListener;
	
//	private int num = 10; //一次取十条记录
	private int id;
	private int height;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			loading.setVisibility(View.GONE);
			
			
			switch(msg.what){
			case 0:
				
				Toast.makeText(mContext, "无消息", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				adapter = new DriftBottleAdapter(mContext, bottleList , height);
				
				actualListView = listview.getRefreshableView();
				
				if(bottleList.size()%10==0 && bottleList.size()!=0){
					loadmore.setVisibility(View.VISIBLE);
					
					actualListView.addFooterView(loadmore);
				}else{
					actualListView.removeFooterView(loadmore);
					actualListView.addFooterView(nomore);
				}
		
				actualListView.setAdapter(adapter);
				Toast.makeText(mContext, "加载成功", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	
	public DriftBottleView(Context context , Activity activity){
		mContext = context ;
		mActivity = activity;
		
		mMaps = LayoutInflater.from(context).inflate(R.layout.school_map, null);
		loadmore = LayoutInflater.from(mContext).inflate(R.layout.loadmore, null);
		nomore = LayoutInflater.from(mContext).inflate(R.layout.nomoredata, null);
		Distance distance = new Distance(mContext , true , null);
		
		setUpView();
		
		setListner();
		
	}
	


	private void setUpView() {
		mFlip = (ImageView) mMaps.findViewById(R.id.btn_goto_residenmenu);
		mWriteBottle = (ImageView) mMaps.findViewById(R.id.write_bottle);
		listview = (PullToRefreshListView ) mMaps.findViewById(R.id.pull_refresh_list);
//		listview.setMode(Mode.PULL_FROM_END);// 设置底部下拉刷新模式
		
		loading = (ProgressBar) mMaps.findViewById(R.id.loading);
		click_loading = (ProgressBar) loadmore.findViewById(R.id.loadmore);
		height = getScreenHeight();
		 
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				bottleMap = new HashMap<Integer,BottleItem>();
				bottleList = new ArrayList<BottleItem>();
				try {
					list = getBottleList(FIRST_LOADING);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				double lat = 0 , lng = 0;
				if(list != null){
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = list.get(i);  
		                Set<String> set = map.keySet();  
		                BottleItem item = new BottleItem();
		                for (Iterator<String> it = set.iterator(); it.hasNext();){
		                	String key = it.next();  
		                	//根据key获取数据
		                	if(key.equals("ID")){ 
		                		id = Integer.parseInt((String)map.get(key));
		                		item.setId(id);
		                	}
		                	else if(key.equals("AppID")) item.setUsername((String)map.get(key));
		                	else if(key.equals("Content")) item.setContent((String)map.get(key));
		                	else if(key.equals("Time")) item.setTime((String)map.get(key));
		                	else if(key.equals("Goods")){
		                		
		                		String good = (String)map.get(key);
		                		String[] goods = good.split(",");
//		                		for(int n=0 ; n<goods.length ; n++)
//		                			System.out.println("goods---:"+goods[n]);
		                		if(isClick(goods,MyApplication.getInstance().getUserName()) ){
		                			item.setHeart(true);
		                		}else{
		                			item.setHeart(false);
		                		}
		                		
		                		item.setGood(good);
		                		item.setGoods(goods);
		                	}
		                	else if(key.equals("Lat")) lat=Double.parseDouble((String)map.get(key));
		                	else if(key.equals("Lng")) lng=Double.parseDouble((String)map.get(key));
		                	if(lat!=0 && lng!=0){
			                	LatLng llA = new LatLng(Distance.getLat(), Distance.getLng());
			                	LatLng llB = new LatLng(lat, lng);
			                	item.setDistance((int)Distance.distance(llA, llB));
//			                	System.out.println("item.setDistance-----:"+(int)Distance.distance(llA, llB));
		                	}
		                }
		                
		                bottleList.add(item);
					}
					
					
					handler.sendEmptyMessage(1);
				}else{
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	
	private void setListner() {
	
		
		mFlip.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});

		mWriteBottle.setOnClickListener(this);
		
//		listview.setOnItemClickListener(new OnItemClickListener(){
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
////				Drawable bd = view.getBackground();
////				
//				BottleItem item = adapter.getItem(position-1);
//				Intent intent = new Intent(mActivity , BottleActivity.class);
//				intent.putExtra("item", item);
////				
//				mActivity.startActivity(intent);
////				System.out.prin tln("position---:"+position);
//				System.out.println("id----:"+item.getId());
//				System.out.println("content----:"+item.getContent());
//			}
//		});
		
		
		
		loadmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				click_loading.setVisibility(View.VISIBLE);
				new GetDataTask().execute(CLICK_TO_LOAD);
			}
		});
		
		listview.setKeepScreenOn(true);
		
		listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				
			 String label = DateUtils.formatDateTime(mActivity, System.currentTimeMillis(),  
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
			 refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
			 refreshView.getLoadingLayoutProxy().setPullLabel(mContext.getString(R.string.pull_to_refresh_pull_label));
			 refreshView.getLoadingLayoutProxy().setRefreshingLabel(mContext.getString(R.string.pull_to_refresh_refreshing_label));
			 refreshView.getLoadingLayoutProxy().setReleaseLabel(mContext.getString(R.string.pull_to_refresh_release_label));
//				 RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(refreshView.getLayoutParams());
//				 lp.setMargins(0, 6, 0, 0);
//				 refreshView.setLayoutParams(lp);
			 new GetDataTask().execute(PULL_TO_REFRESH);  
			}
		});
	}
	
	public int getScreenHeight(){
		WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
		 int width = wm.getDefaultDisplay().getWidth();
	     int height = wm.getDefaultDisplay().getHeight();
	     
	     return height;
	}
	
	public View getView(){
		return mMaps;
	}
	
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.write_bottle: //写一则纸条
			Intent intent = new Intent (mActivity , WriteBottleActivity.class);
			mActivity.startActivity(intent);
			break;
		}
	}
	
	//从数据库上获取内容
	public List<LinkedHashMap<String, Object>> getBottleList(int oper) throws UnsupportedEncodingException{
		List<LinkedHashMap<String, Object>> tempList = null;
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		
		String url = "http://1.nodistanceservice.sinaapp.com/operationNote.php";
		
		
		
		//params.add(new BasicNameValuePair("GetNumPer", num+""));
		switch(oper){
		case PULL_TO_REFRESH:
			params.add(new BasicNameValuePair("Operation", PULL_REFRESH_BOTTLE_CONTENT));
			params.add(new BasicNameValuePair("PrecentMax", bottleList.get(0).getId()+""));
			System.out.println("bottleList.get(0).getId()----:"+bottleList.get(0).getId());
			break;
		case FIRST_LOADING:
			params.add(new BasicNameValuePair("Operation", GET_BOTTLE_CONTENT_FIRST));
			break;
		case CLICK_TO_LOAD:
			params.add(new BasicNameValuePair("Operation", CLICK_BOTTLE_CONTENT));
			System.out.println("iiii----:"+bottleList.get(bottleList.size()-1).getId());
			params.add(new BasicNameValuePair("UpdateMin", bottleList.get(bottleList.size()-1).getId()+""));
//			System.out.println("bottleList.get(0).getId()----:"+bottleList.get(bottleList.size()).getId());
			break;
		}
		
		
		String result = HttpUtil.queryStringForPost(url, params);
		System.out.println("result----getBottleList-:"+result);
		
		tempList = JsonUtil.json2List(result);
		
		return tempList;
	}
	
	public boolean isClick(String[] strs ,String s){
		for (int i = 0; i < strs.length; i++) {
			if(strs[i].indexOf(s)!=-1)
				return false;
		}
		return true;
	}
	
	//刷新listview
	public int refresh(){
		
		int refreshNum = 0;
		int curID = 0;
		bottleMap = new HashMap<Integer,BottleItem>();
		ArrayList<BottleItem> tempBottleList = new ArrayList<BottleItem>();
		
		
		try {
			list = getBottleList(PULL_TO_REFRESH);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double lat = 0 , lng = 0;
		if(list != null){
//			System.out.println("list != null");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);  
                Set<String> set = map.keySet();  
                BottleItem item = new BottleItem();
                for (Iterator<String> it = set.iterator(); it.hasNext();){
                	String key = it.next();  
                	//根据key获取数据
                	if(key.equals("ID")){ 
                		id = Integer.parseInt((String)map.get(key));
                		item.setId(id);
                		System.out.println("ID----:"+id);
                	}
                	else if(key.equals("AppID")) item.setUsername((String)map.get(key));
                	else if(key.equals("Content")) item.setContent((String)map.get(key));
                	else if(key.equals("Time")) item.setTime((String)map.get(key));
                	else if(key.equals("Goods")){
                		
                		String good = (String)map.get(key);
                		System.out.println("goods-----:"+good);
                		String[] goods = good.split(",");
                		
                		if(isClick(goods,MyApplication.getInstance().getUserName())){
                			item.setHeart(true);
                		}else{
                			item.setHeart(false);
                		}
                		
                		item.setGood(good);
                		item.setGoods(goods);
                	}
                	else if(key.equals("Lat")) lat=Double.parseDouble((String)map.get(key));
                	else if(key.equals("Lng")) lng=Double.parseDouble((String)map.get(key));
                	if(lat!=0 && lng!=0){
//	                	Distance.getInstance(mContext);
						LatLng llA = new LatLng(Distance.getLat(), Distance.getLng());
	                	LatLng llB = new LatLng(lat, lng);
	                	item.setDistance((int)Distance.distance(llA, llB));
	                	System.out.println("item.setDistance-----:"+(int)Distance.distance(llA, llB));
                	}
                }
                tempBottleList.add(item);
//                curID = bottleList.get(0).getId();
//                System.out.println("curID----:"+curID);
                bottleList.add(0,item);
			}
			
//			refreshNum = bottleList.get(0).getId()-curID;
//			System.out.println("refreshNum---------:"+refreshNum);
		}
		
		return tempBottleList.size();
	}
	
	//加载数据
	public int loadingData(){
		
		bottleMap = new HashMap<Integer,BottleItem>();
//		bottleList = new ArrayList<BottleItem>();
		ArrayList<BottleItem> temp = new ArrayList<BottleItem>();
		
		try {
			list = getBottleList(CLICK_TO_LOAD);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double lat = 0 , lng = 0;
		if(list != null){
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);  
                Set<String> set = map.keySet();  
                BottleItem item = new BottleItem();
                for (Iterator<String> it = set.iterator(); it.hasNext();){
                	String key = it.next();  
                	//根据key获取数据
                	if(key.equals("ID")){ 
                		id = Integer.parseInt((String)map.get(key));
                		item.setId(id);
                		System.out.println("ID----:"+id);
                	}
                	else if(key.equals("AppID")) item.setUsername((String)map.get(key));
                	else if(key.equals("Content")) item.setContent((String)map.get(key));
                	else if(key.equals("Time")) item.setTime((String)map.get(key));
                	else if(key.equals("Goods")){
                		
                		String good = (String)map.get(key);
                		String[] goods = good.split(",");
                		
                		if(isClick(goods,MyApplication.getInstance().getUserName())){
                			item.setHeart(true);
                		}else{
                			item.setHeart(false);
                		}
                		
                		item.setGood(good);
                		item.setGoods(goods);
                	}
                	else if(key.equals("Lat")) lat=Double.parseDouble((String)map.get(key));
                	else if(key.equals("Lng")) lng=Double.parseDouble((String)map.get(key));
                	if(lat!=0 && lng!=0){
	                	LatLng llA = new LatLng(Distance.getLat(), Distance.getLng());
	                	LatLng llB = new LatLng(lat, lng);
	                	item.setDistance((int)Distance.distance(llA, llB));
	                	System.out.println("item.setDistance-----:"+(int)Distance.distance(llA, llB));
                	}
                }
                temp.add(item);
                bottleList.add(bottleList.size(),item);
			}
		}
		return temp.size();
	}
	
	 class GetDataTask extends AsyncTask<Integer, Void, String> {//定义返回值的类型  
		    // 后台处理部分  
		    @Override  
		    protected String doInBackground(Integer... params) {  
		    	int num = -1;
		    	if(params[0]==PULL_TO_REFRESH){
					num = refresh();
			    	System.out.println("refresh()-------"+num+"_"+PULL_TO_REFRESH); 
			    	return num+"_"+PULL_TO_REFRESH;  
		    	}else if(params[0]==CLICK_TO_LOAD){
		    		num = loadingData();
		    		return num+"_"+CLICK_TO_LOAD;
		    	}
		    	return num+"";
		    }  
		  
		    //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中  
		    //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值  
		    protected void onPostExecute(String result) {  
		        //在头部增加新添内容  
		    	System.out.println("result----:"+result);
		    	String[] str = result.split("_");
		    	int resultNum = Integer.parseInt(str[0]);
		    	int type = Integer.parseInt(str[1]);
		         if(resultNum>0 && type==PULL_TO_REFRESH){
		        	 System.out.println("bottleMap.size()----:"+bottleMap.size());
		 			
//		        	 adapter = new DriftBottleAdapter(mContext, bottleList , height);
//		        	 
//		        	 ListView actualListView = listview.getRefreshableView();  
//		        	 actualListView.setAdapter(adapter);  
		        	 
			        //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合  
			        adapter.notifyDataSetChanged();  
			        mMaps.invalidate();
			        Toast.makeText(mContext, "更新了"+result+"个纸条", Toast.LENGTH_SHORT).show();
		         }else if(type==CLICK_TO_LOAD){
		        	 System.out.println("bottleMap.size()----:"+bottleMap.size());
			 			
//		        	 adapter = new DriftBottleAdapter(mContext, bottleList , height);
//		        	 
//		        	 ListView actualListView = listview.getRefreshableView();  
//		        	 actualListView.setAdapter(adapter);  
		        	 
			        //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合  
		        	if(resultNum>0){ 
				        adapter.notifyDataSetChanged();  
				        mMaps.invalidate();
				       
				        click_loading.setVisibility(View.GONE);
				        
				        Toast.makeText(mContext, "加载了"+result+"个纸条", Toast.LENGTH_SHORT).show();
		        	}else{
		        		actualListView.removeFooterView(loadmore);
		        		actualListView.addFooterView(nomore);
		        		Toast.makeText(mContext, "没有更多的纸条了", Toast.LENGTH_SHORT).show();
		        	}
		         }else{
		        	 System.out.println("bottleMap.size()----:"+bottleMap.size());
		        	 Toast.makeText(mContext, "没有更多纸条", Toast.LENGTH_SHORT).show();
		         }
		       
		        // Call onRefreshComplete when the list has been refreshed.  
		        
		        listview.onRefreshComplete();  
		  
		        super.onPostExecute(result);//这句是必有的，AsyncTask规定的格式  
		    }  
		}  
}
