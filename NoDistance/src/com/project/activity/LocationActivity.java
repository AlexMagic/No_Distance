package com.project.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.project.R;
import com.project.animation.MyAnimation;
import com.project.app.MyApplication;
import com.project.communicate.ChatActivity;
import com.project.db.UserDao;
import com.project.util.FindUserData;
import com.project.util.FriendLatLngData;
import com.project.util.HttpUtil;
import com.project.util.JsonUtil;
import com.project.util.MapFunction;


/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡�?
 * 
 */
public class LocationActivity extends Activity {
	
	TextView text;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	ImageButton map_pointer_Button;
	ImageButton map_type_Button;
	ImageButton map_updateLatLng_Button;
	ImageButton map_showFriendLatLng_Button;
	ImageButton map_find_Button;
	boolean isFirstLoc = true;// 是否首次定位
	
	private LatLng currentPt;
	private LatLng localLatLng;
	private double dLat;
	private double dLng;
	private String sex;
	private String username;
	private String nickname;
	
	private Marker mMarkerA;
	private MapFunction mf;
	private int mapType = 1;
	private double myLat;
	private double myLng;
	private boolean isOpenUpdate = false;
	private boolean isOpenFind = false;
//	private String str;
	boolean isRun = false;
	private List<FriendLatLngData> FriendDataList;
	private List<FindUserData> FindUserDataList;
	private OverlayOptions ooCircle;
	private AlertDialog dialog;
	private AlertDialog markerDialog;
	
	private List<Map<String,Object>> dataList;
	private List<Map<String,Object>> FindUserList;
	private Thread athread;
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		mCurrentMode = LocationMode.NORMAL;
		init();
		setListener();
		
		pd = new ProgressDialog(LocationActivity.this);
//		Thread athread = new Thread(){
//			public void run(){
//				dataList = getFriendMapList("test111");
//				FindUserList = getFindMapList();
//				pd.dismiss();
//			}
//		};
		pd.setMessage("正在加载...");
		pd.show();
//		athread.start();

		
	}

	private void init() {
		//用户信息初始化
		username = MyApplication.getInstance().getUserName();
		nickname = MyApplication.getInstance().getNickName();
		sex = MyApplication.getInstance().getSex();
		
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		//初始化地图工具类
		mf = new MapFunction(mBaiduMap , this);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		
		mLocClient.setLocOption(option);
		mLocClient.start();
		mf.drawPolygonA();
		mf.drawPolygonB();
		mf.drawPolygonC();
		text = (TextView) findViewById(R.id.textView1);
		map_pointer_Button = (ImageButton) findViewById(R.id.map_pointer_1);
		map_pointer_Button.getBackground().setAlpha(130);
		map_type_Button = (ImageButton) findViewById(R.id.map_type_2);
		map_type_Button.getBackground().setAlpha(130);
		map_updateLatLng_Button = (ImageButton) findViewById(R.id.map_updateLatLng_3);
		map_updateLatLng_Button.getBackground().setAlpha(130);
		map_showFriendLatLng_Button = (ImageButton) findViewById(R.id.map_showFriendLatLng_4);
		map_showFriendLatLng_Button.getBackground().setAlpha(130);
		map_find_Button = (ImageButton) findViewById(R.id.map_find_5);
		map_find_Button.getBackground().setAlpha(130);
		
	}
	
	private void setListener(){
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				Intent playIntent = new Intent(LocationActivity.this, ChatActivity.class);
				UserDao userDao = new UserDao(LocationActivity.this);
		        String fusername = marker.getTitle();
		        System.out.println("fusername========="+fusername);
		        
		        boolean isfriend = userDao.isExist(fusername);
		        if(isfriend){
		        	String fnickname = userDao.getNickName(fusername);
		        	System.out.println("fnickname=========="+fnickname);
//		        	if(message.getChatType() == ChatActivity.CHATTYPE_SINGLE)
		        	playIntent.putExtra("userId", fusername);
		        	playIntent.putExtra("usernic",fnickname);
		        	startActivity(playIntent);
//					Toast.makeText(LocationActivity.this, string, Toast.LENGTH_SHORT).show();
		        	return true;
		        }
		        else{
		        	dialog(fusername,"boy");
		        	return true;
		        }
		        	

			}
		});
		
		OnClickListener pointerClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					Toast.makeText(LocationActivity.this, "跟随模式", Toast.LENGTH_SHORT).show();
					break;
				case COMPASS:
					mCurrentMode = LocationMode.NORMAL;
					mMapView.getMap().clear();
					mf.drawPolygonA();
					mf.drawPolygonB();
					mf.drawPolygonC();
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					Toast.makeText(LocationActivity.this, "普通模式", Toast.LENGTH_SHORT).show();
					break;
				case FOLLOWING:
					mCurrentMode = LocationMode.COMPASS;
					LatLng llCircle = new LatLng(22.591221, 113.951473);
					mf.drowCircle(llCircle,1200);
					Toast.makeText(LocationActivity.this, "学校范围", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		map_pointer_Button.setOnClickListener(pointerClickListener);
		
		OnClickListener typeClickListener = new OnClickListener() {
			public void onClick(View v) {
				if(mapType==1){
					mapType = 2;
					String str = mf.setMapType(mapType);
					mMapView.getMap().clear();
					Toast.makeText(LocationActivity.this, str, Toast.LENGTH_SHORT).show();
					map_type_Button.setBackgroundResource(R.drawable.maptype2);
					map_type_Button.getBackground().setAlpha(130);
					
				}else if(mapType==2){
					mapType = 1;					
					String str = mf.setMapType(mapType);
					mMapView.getMap().clear();
					mf.drawPolygonA();
					mf.drawPolygonB();
					mf.drawPolygonC();
					Toast.makeText(LocationActivity.this, str, Toast.LENGTH_SHORT).show();
					map_type_Button.setBackgroundResource(R.drawable.maptype1);
					map_type_Button.getBackground().setAlpha(130);
					
				}
					
				
			}
		};
		map_type_Button.setOnClickListener(typeClickListener);
		
		map_type_Button.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				mMapView.getMap().clear();
				mf.drawPolygonA();
				mf.drawPolygonB();
				mf.drawPolygonC();
				return true;
			}
		});
		
		OnClickListener updateLatLngClickListener = new OnClickListener() {
			public void onClick(View v) {
				new Thread(){
					public void run() {
						updateUserLocation(username, myLat, myLng);
					}
				}.start();
				Toast.makeText(LocationActivity.this, "定位发送成功", Toast.LENGTH_SHORT).show();
									
			}
			
		};
		map_updateLatLng_Button.setOnClickListener(updateLatLngClickListener);
		
		map_updateLatLng_Button.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if(!isOpenUpdate){
					isOpenUpdate = true;
					map_updateLatLng_Button.setBackgroundResource(R.drawable.mapll2);
					map_updateLatLng_Button.getBackground().setAlpha(150);
					Toast.makeText(LocationActivity.this, "时时发送模式开启", Toast.LENGTH_SHORT).show();
				}
					
				else if(isOpenUpdate){
					isOpenUpdate = false;
					map_updateLatLng_Button.setBackgroundResource(R.drawable.mapll1);
					map_updateLatLng_Button.getBackground().setAlpha(130);
					Toast.makeText(LocationActivity.this, "关闭时时定位模式", Toast.LENGTH_SHORT).show();
				}
					
				
				return true;
			}
		});
		
		OnClickListener showFriendClickListener = new OnClickListener() {
			public void onClick(View v) {
				showFriendList();
			}
		};
		
		map_showFriendLatLng_Button.setOnClickListener(showFriendClickListener);
		
		OnClickListener findClickListener = new OnClickListener() {
			public void onClick(View v) {
				if(!isOpenFind){
					Toast.makeText(LocationActivity.this, "Find模式没有开启，不能进行交友活动。长按第五个按钮开启Find模式", Toast.LENGTH_SHORT).show();
				}
				else if(isOpenFind){
					showFindList();
				}
			}
		};
		map_find_Button.setOnClickListener(findClickListener);
		
		map_find_Button.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if(!isOpenFind){
					isOpenFind = true;
					new Thread(){
						public void run(){
							updateFindUser(username, nickname, sex, dLat, dLng);
						}
						
					}.start();
					
					map_find_Button.setBackgroundResource(R.drawable.mapfind);
					map_find_Button.getBackground().setAlpha(150);
					Toast.makeText(LocationActivity.this, "Find模式开启", Toast.LENGTH_SHORT).show();
				}
					
				else if(isOpenFind){
					isOpenFind = false;
					new Thread(){
						public void run(){
							deleteFindUser(username);
						}
						
					}.start();
					
					map_find_Button.setBackgroundResource(R.drawable.mapnofind);
					map_find_Button.getBackground().setAlpha(130);
					Toast.makeText(LocationActivity.this, "Find模式关闭", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		
		
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view �?��后不在处理新接收的位�?
			if (location == null || mMapView == null)
				return;				
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,(float) 16);
				mBaiduMap.animateMapStatus(u);
				MapFunction mf = new MapFunction(mBaiduMap , LocationActivity.this);
				//获取用户定位信息
				localLatLng = new LatLng(location.getLatitude(),location.getLongitude());
				dLat = location.getLatitude();
				dLng = location.getLongitude();
				String strInfo = "纬度："+location.getLatitude()+"经度"+location.getLongitude();
				LatLng llA = new LatLng(location.getLatitude(), location.getLongitude());
				String Add = location.getAddrStr();
				myLat = location.getLatitude();
				myLng = location.getLongitude();
				new Thread(){
					public void run(){
						dataList = getFriendMapList(username);
						FindUserList = getFindMapList();
						updateUserLocation(username, myLat, myLng);
						pd.dismiss();
					}
				}.start();
				
			}
			

			
			if(isOpenUpdate){
				new Thread(){
					@SuppressWarnings("deprecation")
					public void run() {
						String username = "maptest";
						updateUserLocation(username, myLat, myLng);
					}
				}.start();
			}
			
			
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	
	public String show(LatLng point){
		String state = String.format("经度%f \n纬度%f",
				currentPt.longitude, currentPt.latitude);
		return state;
	}	

	
	public void updateUserLocation(String username, double Lat, double Lng){
		String sLat = Lat+"";
		String sLng = Lng+"";
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("appID",username));
		params.add(new BasicNameValuePair("Lat", sLat));
		params.add(new BasicNameValuePair("Lng", sLng));
		
		String url = "http://1.nodistanceservice.sinaapp.com/updateLatLng.php"; 
		
		HttpUtil.queryStringForPost(url, params);
	}
	
	private void showFriendList() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
		builder.setTitle("好友列表");
		View FriendListView = LayoutInflater.from(this).inflate(R.layout.friend_list_view, null);
		
		final ListView listView = (ListView) FriendListView.findViewById(R.id.mapfriendlistView);
		SimpleAdapter simple_adapter = new SimpleAdapter(LocationActivity.this,
		dataList, R.layout.list_item,
		new String[] { "image", "NickName","Sex","Date" }, new int[] { R.id.image,
				R.id.map_AppID,R.id.map_Sex,R.id.map_Date });
		listView.setAdapter(simple_adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				System.out.println(position);
				showFriendLatLng(position);
				dialog.dismiss();
			}
		});
        builder.setView(FriendListView);
		dialog = builder.create();
		dialog.show();
		
	}
	
	public String getJson(String username){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appID",username));
		String url = "http://1.nodistanceservice.sinaapp.com/getFriendLLData.php";
		String result = HttpUtil.queryStringForPost(url, params);
		return result;
	}
	
	public List<FriendLatLngData> getFriendDataList(String username){
		String result = getJson(username);
		List<FriendLatLngData> fData = new ArrayList<FriendLatLngData>();
		if(result.equals("-1") || result=="-1"){
			List<FriendLatLngData> nullDataList = new ArrayList<FriendLatLngData>();
			FriendLatLngData nullData = new FriendLatLngData(1,"没有一个好友记录位置","0","0",myLat,myLat);
			nullDataList.add(nullData);
			return nullDataList;
		}else{
		    List<LinkedHashMap<String, Object>> resultList = JsonUtil.json2List(result);
		
			Iterator<LinkedHashMap<String, Object>> iterator = resultList.iterator();
			int i = 0;
			while(iterator.hasNext()){
				i++;
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) iterator.next();
				String AppID = (String) map.get("AppID");
				String Sex = (String) map.get("Sex");
				String date = (String) map.get("Date");
				String sLat = (String) map.get("Lat");
				String sLng = (String) map.get("Lng");
				if(sLat=="0.000000"||sLat.equals("0.000000")){
					FriendLatLngData nullData = new FriendLatLngData(i,AppID,Sex,"无记录",myLat,myLng);
					fData.add(nullData);
					continue;
				}
				double Lat = Double.valueOf(sLat.toString());
				double Lng = Double.valueOf(sLng.toString());
				LatLng friendLatLng = new LatLng(Lat, Lng);
				mf.distance(localLatLng, friendLatLng);
				FriendLatLngData aData = new FriendLatLngData(i,AppID,Sex,date,Lat,Lng);
				String nickname = (String) map.get("NickName");
				aData.setNickname(nickname);
				fData.add(aData);
			}
			return fData;
		}
						

		
	}
	
	public List<Map<String,Object>> getFriendMapList(String username){
		List<FriendLatLngData> fData = new ArrayList<FriendLatLngData>();
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		fData = getFriendDataList(username);
		FriendDataList = fData;
		if(fData!=null){
			Iterator<FriendLatLngData> iterator = fData.iterator();
		    while(iterator.hasNext()){
		    	Map<String,Object> aMap = new HashMap<String, Object>();
				FriendLatLngData aData = iterator.next();
				if(aData.getSex().equals("boy"))
					aMap.put("image", R.drawable.sex_boy);
				else
					aMap.put("image", R.drawable.sex_girl);
				
				aMap.put("NickName",aData.getNickname());
				aMap.put("Sex",aData.getSex());
				aMap.put("Date",aData.getDate());
				
				mapList.add(aMap);
			}
		}
		return mapList;
		
	}
	
	public void showFriendLatLng(int which){
		List<FriendLatLngData> showData = new ArrayList<FriendLatLngData>();
		if(FriendDataList!=null){
			double Lat = 0.0;
			double Lng = 0.0;
			String userID = "";
			String fnickname = "";
			showData = FriendDataList;
			Iterator<FriendLatLngData> iterator = showData.iterator();
			FriendLatLngData Data = new FriendLatLngData();
			while(iterator.hasNext()){
				Data = iterator.next();
				if(Data.getID()-1==which){
					Lat = Data.getLat();
					Lng = Data.getLng();
					fnickname = Data.getNickname();
					userID = Data.getAppID();
				}
			}
			LatLng fLatLng = new LatLng(Lat,Lng);
			mf.setFriendMarker(fLatLng,userID,fnickname);
			
		}	
	}
	
	public void updateFindUser(String username, String nickname, String sex, double Lat, double Lng){
		String sLat = Lat+"";
		String sLng = Lng+"";
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("operation","insert"));
		params.add(new BasicNameValuePair("AppID",username));
		params.add(new BasicNameValuePair("NickName",nickname));
		params.add(new BasicNameValuePair("sex",sex));
		params.add(new BasicNameValuePair("Lat", sLat));
		params.add(new BasicNameValuePair("Lng", sLng));
		String url = "http://1.nodistanceservice.sinaapp.com/findUser.php"; 
		
		HttpUtil.queryStringForPost(url, params);
		
	}
	
	public void deleteFindUser(String username){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("operation","delete"));
		params.add(new BasicNameValuePair("AppID",username));
		String url = "http://1.nodistanceservice.sinaapp.com/findUser.php"; 
		
		HttpUtil.queryStringForPost(url, params);
	}
	
	public String selectFindUser(){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("operation","select"));
		String url = "http://1.nodistanceservice.sinaapp.com/findUser.php";
		String result = HttpUtil.queryStringForPost(url, params);
		return result;
	}
	
	public List<FindUserData> getFindDataList(){
		String result = selectFindUser();
		if(result =="-1"||result.equals("-1")){
			List<FindUserData> nullDataList = new ArrayList<FindUserData>();
			FindUserData nullData = new FindUserData(1,"没有陌生人记录","0","0",myLat,myLat,"0");
			nullDataList.add(nullData);
			return nullDataList;
		}else{
		List<FindUserData> fData = new ArrayList<FindUserData>();
		if(result!=null || result!=""){
						
			List<LinkedHashMap<String, Object>> resultList = JsonUtil.json2List(result);
		
			Iterator<LinkedHashMap<String, Object>> iterator = resultList.iterator();
			int i = 0;
			while(iterator.hasNext()){
				i++;
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) iterator.next();
				String AppID = (String) map.get("AppID");
				String Sex = (String) map.get("Sex");
				String NickName = (String) map.get("NickName");
				String sLat = (String) map.get("Lat");
				String sLng = (String) map.get("Lng");
				double Lat = Double.valueOf(sLat.toString());
				double Lng = Double.valueOf(sLng.toString());
				LatLng findLatLng = new LatLng(Lat, Lng);
				LatLng myLatLng = new LatLng(myLat,myLng);
				double distance = mf.distance(myLatLng, findLatLng);
				int idistance = (int)Math.floor(distance);
				String sDistance = "距离："+idistance+"m";
				FindUserData aData = new FindUserData(i,AppID,NickName,Sex,Lat,Lng,sDistance);
				fData.add(aData);
			}
		}
		
		return fData;
		}
	}
	
	/**
	 * 获取FindMapList
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getFindMapList(){
		List<FindUserData> fData = new ArrayList<FindUserData>();
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		fData = getFindDataList();
		FindUserDataList = fData;
		if(fData!=null){
			Iterator<FindUserData> iterator = fData.iterator();
		    while(iterator.hasNext()){
		    	Map<String,Object> aMap = new HashMap<String, Object>();
		    	FindUserData aData = iterator.next();
				if(aData.getSex().equals("boy"))
					aMap.put("image", R.drawable.sex_boy);
				else
					aMap.put("image", R.drawable.sex_girl);

				aMap.put("NickName",aData.getNickName());			
				aMap.put("AppID",aData.getAppID());
				aMap.put("distance",aData.getDistance());
				
				mapList.add(aMap);
			}
		}
		return mapList;
		
	}
	
	/**
	 * 显示Find列表
	 */
	private void showFindList() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
		builder.setTitle("Find列表");
		View FriendListView = LayoutInflater.from(this).inflate(R.layout.friend_list_view, null);
		
		final ListView listView = (ListView) FriendListView.findViewById(R.id.mapfriendlistView);
		
		SimpleAdapter simple_adapter = new SimpleAdapter(LocationActivity.this,
		FindUserList, R.layout.list_item,
		new String[] { "image", "NickName","AppID","distance" }, new int[] { R.id.image,
				R.id.map_AppID,R.id.map_Sex,R.id.map_Date });
		
		listView.setAdapter(simple_adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				System.out.println(position);
				showFindUserLatLng(position);
				dialog.dismiss();
			}
		});
        builder.setView(FriendListView);
		dialog = builder.create();
		dialog.show();
		
	}
	
	/**
	 * 显示陌生人坐标
	 * @param which
	 */
	public void showFindUserLatLng(int which){
		List<FindUserData> showData = new ArrayList<FindUserData>();
		if(FriendDataList!=null){
			double Lat = 0.0;
			double Lng = 0.0;
			String userID = "";
			String fnickname = "";
			showData = FindUserDataList;
			Iterator<FindUserData> iterator = showData.iterator();
			FindUserData Data = new FindUserData();
			while(iterator.hasNext()){
				Data = iterator.next();
				if(Data.getID()-1==which){
					Lat = Data.getLat();
					Lng = Data.getLng();
					fnickname = Data.getNickName();
					userID = Data.getAppID();
				}
			}
			LatLng fLatLng = new LatLng(Lat,Lng);
			mf.setFindMarker(fLatLng,userID,fnickname);
			
		}	
	}
	
	private void showFindDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
		builder.setTitle("好友列表");
		View FriendListView = LayoutInflater.from(this).inflate(R.layout.friend_list_view, null);
		
		final ListView listView = (ListView) FriendListView.findViewById(R.id.mapfriendlistView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				System.out.println(position);
				showFriendLatLng(position);
				dialog.dismiss();
			}
		});
        builder.setView(FriendListView);
		dialog = builder.create();
		dialog.show();
		
	}
	
	public void dialog(final String fNickname,String fSex) {
		AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
		if(fSex=="boy")
			builder.setIcon(R.drawable.sex_boy);
		else
			builder.setIcon(R.drawable.sex_girl);
		builder.setTitle(fNickname);
//	    builder.setMessage("你们还不是好友，是否添加ta为好友？");
		builder.setMessage("此为陌生人");
	    builder.setPositiveButton("聊天", new DialogInterface.OnClickListener() {  
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    		Intent playIntent = new Intent(LocationActivity.this, ChatActivity.class);
		        
//		        	if(message.getChatType() == ChatActivity.CHATTYPE_SINGLE)
		        	playIntent.putExtra("userId", fNickname);
		        	playIntent.putExtra("usernic","");
		        	startActivity(playIntent);
	        }
	    });  
	    builder.setNegativeButton("添加好友", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	Toast.makeText(LocationActivity.this, "暂无法添加", Toast.LENGTH_SHORT).show();
	        }
	    });  
	    builder.create().show();
	}


}
