package com.project.sendfromposition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.maxwin.view.XListView;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.project.R;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
import com.project.activity.base.ScrollLayout;
import com.project.util.Distance;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;

import com.project.util.Distance.PointCallback;

public class ListFromNearBy implements OnItemClickListener , OnClickListener{
	
	private ScrollLayout mScrollLayout;
	private LinearLayout[] mTextItems;
	private FrameLayout mFrameLayout;
	private View mView_1;
	private View mView_2;
	
	private OnOpenListener mOnOpenListener ;
	private Activity mActivity;
	private Context mContext;
	
	private View mNearPlace;
	private View mNearByMe;
	
	private TextView tv_nearBy;
	private TextView tv_place;
	private ProgressBar loading;
	
	private Double lat;
	private Double lng;
	
//	private ImageFetcher mImageFetcher;
	
	private static Distance distance;
	
    private XListView mListView = null;
    private PullToRefreshListView listview = null;
	
    private PLaceListAdapter mAdapter1;
    private NearListAdapter mAdapter;
    
	private LinearLayout firstLayout;
	private LinearLayout secondLayout;
	
	private List<LinkedHashMap<String, Object>> list;
	private ArrayList<NearListItem> nearList;
	
	private int mViewCount;
	private int mCurSel;
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				
				loading.setVisibility(View.GONE);
				
				mAdapter1 = new PLaceListAdapter(mContext, nearList);
				listview.setAdapter(mAdapter1);
				ListView actualListView = listview.getRefreshableView();
				actualListView.setAdapter(mAdapter1);
				mView_1.invalidate();
				break;
			case 1:
				break;
			}
		}
	};
	
	public ListFromNearBy(Activity mActivity,Context mContext){
		this.mActivity = mActivity;
		this.mContext = mContext;
		
//		distance = Distance.getInstance(mContext,null);
		Distance.getInstance(mContext).getLocClient().start();
		mNearPlace = LayoutInflater.from(mContext).inflate(R.layout.list_nearby, null);
		mView_1 =  LayoutInflater.from(mContext).inflate(R.layout.first_view, null);
		mView_2 =  LayoutInflater.from(mContext).inflate(R.layout.second_view, null);
		setUpView();
		
		setListner();
	}
	

	
	//findView
	public void setUpView(){
		
		mFrameLayout = (FrameLayout) mNearPlace.findViewById(R.id.framelayout);
		mFrameLayout.addView(mView_1);
		mListView = (XListView) mView_2.findViewById(R.id.list);
		listview = (PullToRefreshListView) mView_1.findViewById(R.id.refresh_list);
		listview.setMode(Mode.BOTH);
		listview.setKeepScreenOn(true);
		loading = (ProgressBar) mNearPlace.findViewById(R.id.loading);
		
		tv_nearBy = (TextView) mNearPlace.findViewById(R.id.tv_nearby);
		tv_place = (TextView) mNearPlace.findViewById(R.id.tv_place);
		
		mAdapter = new NearListAdapter(mContext, null);
		mListView.setAdapter(mAdapter);
		LinearLayout linearLayout = (LinearLayout) mNearPlace.findViewById(R.id.lllayout);
		
		mViewCount = linearLayout.getChildCount();
		
		mTextItems = new LinearLayout[mViewCount];
		System.out.println("mViewCount----:"+mViewCount);
		for(int i=0; i<mViewCount; i++){
			
			mTextItems[i] = (LinearLayout) linearLayout.getChildAt(i);
			mTextItems[i].setEnabled(true);
			mTextItems[i].setTag(i);
			mTextItems[i].setOnClickListener(this);
		}
		
		mCurSel = 0;
		mTextItems[mCurSel].setEnabled(false);
		

				
		new Distance(mContext,true,new PointCallback() {
			
			@Override
			public void callback() {
				// TODO Auto-generated method stub
				
				Distance.getInstance(mContext).getLocClient().stop();
				lat = Distance.getLat();
				lng =  Distance.getLng();
				System.out.println(" Distance.getLat()---:"+ lat);
				System.out.println(" Distance.getLng()---:"+ lng);
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						list = getPlace();
						nearList = new ArrayList<NearListItem>();
						if(list != null){
							System.out.println("not null");
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> map = list.get(i); 
								ArrayList<String> imgList = new ArrayList<String>();
				                Set<String> set = map.keySet();  
				                NearListItem item = new NearListItem();
				                for (Iterator<String> it = set.iterator(); it.hasNext();){
				                	String key = it.next();  
				                	
				                	if(key.equals("AppID")){item.setAppID((String)map.get(key));}
				                	else if(key.equals("ID")){
				                		int id = Integer.parseInt((String)map.get(key));
				                		item.setId(id);
				                	}
				                	else if(key.equals("Content")){item.setMeetingContent((String)map.get(key));}
				                	else if(key.equals("startTime")){item.setDateTime((String)map.get(key));}
				                	else if(key.equals("place")){item.setMeetingPos((String)map.get(key));}
				                	else if(key.contains("imgUrl")){
				                		if((String)map.get(key)!=""){
				                			imgList.add((String)map.get(key));
				                		}
				                	}
				                }
				                item.setPicPath(imgList);
				                
				                nearList.add(item);
				                
							}
							handler.sendEmptyMessage(0);
						}else{
							handler.sendEmptyMessage(1);
						}
					}
				}).start();
			}
		});
	
	}
	
	//°ó¶¨¼àÌýÆ÷
	public void setListner(){
//		mListView.setXListViewListener(this);
		
		mListView.setOnItemClickListener(this);
	}
	
	
	public View getView(){
		return mNearPlace;
	}



	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}


	@Override
	public void onItemClick(PLA_AdapterView<?> parent, View view, int position,
			long id) {
		
		if(position-1<mAdapter.getCount()){
			String place = (String) mAdapter.getItem(position-1);
			System.out.println(place);
			Intent intent = new Intent(mActivity , PLaceListActivity.class);
			intent.putExtra("place", place);
			mActivity.startActivity(intent);
		}
	}
	
	
	public void setCurTextItem(int index){
		if (index < 0 || index > mViewCount - 1 || mCurSel == index){
    		return ;
    	}  
		
		mTextItems[mCurSel].setEnabled(true);
		mTextItems[index].setEnabled(false);
		mCurSel = index;
		
		if(index == 0){
			tv_nearBy.setTextColor(0xff2ea3fe);
    		tv_place.setTextColor(0xff999999);
		}else if(index == 1){
			tv_nearBy.setTextColor(0xff999999);
			tv_place.setTextColor(0xff2ea3fe);
		}
	}
	
	
	private List<LinkedHashMap<String,Object>> getPlace(){
		List<LinkedHashMap<String, Object>> tempList = null;
		
		String url = "http://1.nodistanceservice.sinaapp.com/operationInvite.php";
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		
		params.add(new BasicNameValuePair("Operation", "selectlike"));

		String tempLat =Double.toString(lat);
		String tempLng = Double.toString(lng);
		
		params.add(new BasicNameValuePair("likeLat",tempLat.substring(0, tempLat.indexOf(".")+3)));
		params.add(new BasicNameValuePair("likeLng", tempLng.substring(0,tempLng.indexOf(".")+3)));
		
		System.out.println("lat---:"+tempLat.substring(0, tempLat.indexOf(".")+3));
		System.out.println("lng---:"+tempLng.substring(0,tempLng.indexOf(".")+3));
		String result = HttpUtil.queryStringForPost(url, params);
		System.out.println("result----getBottleList-:"+result);
		
		tempList = JsonUtil.json2List(result);
		return tempList;
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer) v.getTag();
		switch(pos){
		case 0:
			setCurTextItem(pos);
			mFrameLayout.removeAllViews();
			mFrameLayout.addView(mView_1);
			break;
		case 1:
			setCurTextItem(pos);
			mFrameLayout.removeAllViews();
			mFrameLayout.addView(mView_2);
			break;
		}
	}
}
