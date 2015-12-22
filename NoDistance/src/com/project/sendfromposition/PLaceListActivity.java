package com.project.sendfromposition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.project.R;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;

public class PLaceListActivity extends Activity{
	
	private static final int FIRST_LOADING = 0;
	private static final int PULL_TO_REFRESH = 1;
	private static final int CLICK_TO_LOAD = 2;
	private static final String GET_BOTTLE_CONTENT_FIRST = "select";
	private static final String PULL_REFRESH_BOTTLE_CONTENT = "selectdown";
	private static final String CLICK_BOTTLE_CONTENT = "selectup";
	
	private int id;
	private String place;
	
	private PullToRefreshListView listview;
	private ProgressBar loading;
	private TextView tv_place;
	private Button btn_invite;
	
	
	private PLaceListAdapter mAdapter;
	
	private ArrayList<NearListItem> nearList;
	private ArrayList<String> picPath;
	private List<LinkedHashMap<String, Object>> list;
	
	private int num;
	private boolean isLoadOnce = false;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(getApplicationContext(), "获取失败", Toast.LENGTH_SHORT).show();
				loading.setVisibility(View.GONE);
				break;
			case 1:
				loading.setVisibility(View.GONE);
				
				mAdapter = new PLaceListAdapter(getApplicationContext(), nearList);
				
				ListView actualListView = listview.getRefreshableView();
				
				actualListView.setAdapter(mAdapter);
				
				Toast.makeText(getApplicationContext(), "获取成功", Toast.LENGTH_SHORT).show();
				break;
			}
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_place);
		
		Intent intent = getIntent();
		place = intent.getStringExtra("place");
		
		setUpView();
		
		setListner();
	}



	private void setUpView() {
		
		tv_place = (TextView) findViewById(R.id.tv_place);
		tv_place.setText(place);
		listview = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listview.setMode(Mode.BOTH);
		
		loading = (ProgressBar) findViewById(R.id.loading);
		
		btn_invite = (Button) findViewById(R.id.btn_invite);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("hahah----2");
				list = getPlace(FIRST_LOADING);
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
		                		id = Integer.parseInt((String)map.get(key));
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
					handler.sendEmptyMessage(1);
				}else{
					System.out.println("null");
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}


	
	private void setListner() {
		listview.setKeepScreenOn(true);
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>(){

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
				refreshView.getLoadingLayoutProxy().setPullLabel(getApplicationContext().getString(R.string.pull_to_refresh_pull_label));
				refreshView.getLoadingLayoutProxy().setRefreshingLabel(getApplicationContext().getString(R.string.pull_to_refresh_refreshing_label));
				refreshView.getLoadingLayoutProxy().setReleaseLabel(getApplicationContext().getString(R.string.pull_to_refresh_release_label));
			
//				new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// 上拉加载
				
			} 
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				NearListItem item = (NearListItem)parent.getItemAtPosition(position);
				Intent intent = new Intent(PLaceListActivity.this,InviteDetailActivity.class);
				intent.putExtra("item", item);
				startActivity(intent);
			}
		});
		
		btn_invite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(PLaceListActivity.this,WritePlaceActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	//刷新
	public void refreshPlace(int oper){
		list = getPlace(oper);
		
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> map = list.get(i);
				Set<String> set = map.keySet();  
				NearListItem item = new NearListItem();
				for (Iterator<String> it = set.iterator(); it.hasNext();){
                	String key = it.next();  
                	//根据key获取数据
				}
				nearList.add(item);
			}
			// 排序
//			Collections.sort(nearList , new Comparator<NearListItem>() {
//
//				@Override
//				public int compare(NearListItem item0, NearListItem item1) {
//					return 0;
//				}
//			});
			
			num = nearList.size();
		}
	}
	
	//加载
	public void loadPlace(){
		
	}
	
	
	private List<LinkedHashMap<String,Object>> getPlace(int oper){
		List<LinkedHashMap<String, Object>> tempList = null;
		
		String url = "http://1.nodistanceservice.sinaapp.com/operationInvite.php";
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		
		switch(oper){
		case 0:
			params.add(new BasicNameValuePair("Operation", "select"));
			break;
		}
		
		
		String result = HttpUtil.queryStringForPost(url, params);
		System.out.println("result----getBottleList-:"+result);
		
		tempList = JsonUtil.json2List(result);
		return tempList;
	}
	
	class GetDataTask extends AsyncTask<Integer, Void, String>{

		@Override
		protected String doInBackground(Integer... arg0) {
			
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
	
}
