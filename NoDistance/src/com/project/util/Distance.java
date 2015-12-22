package com.project.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class Distance {
	private static LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

//	MapView mMapView;
//	BaiduMap mBaiduMap;
	Context context;
	boolean isLoc = true;// 是否首次定位
	private static double Lat;
	private static double Lng;
	
	private static String position;
	
	private static Distance instance;
	
	private PointCallback callback;
	
//	 public final Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			
//		}
//    };
	
	public static Distance getInstance(Context context){
		if(instance==null)
			return new Distance(context , true , null);
		
		return instance;
	}
	
//	public Distance(Context context , boolean isLocation){
//		this.context = context;
//		// 地图初始化
//		//mMapView = (MapView) findViewById(R.id.bmapView);
////		mBaiduMap = mMapView.getMap();
////		mBaiduMap.setMyLocationEnabled(true);
//		isLoc = isLocation;
//		// 定位初始化
//		mLocClient = new LocationClient(context);
//		mLocClient.registerLocationListener(myListener);
//		
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
////		option.setScanSpan(4000);//监听时间间隔
////		option.setTimeOut(arg0);
//		option.setAddrType("all");//返回的定位结果包含地址信息
//
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//	}
	
	public Distance(Context context , boolean isLocation , PointCallback callback){
		this.context = context;
		this.callback = callback;
		// 地图初始化
		//mMapView = (MapView) findViewById(R.id.bmapView);
//		mBaiduMap = mMapView.getMap();
//		mBaiduMap.setMyLocationEnabled(true);
		isLoc = isLocation;
		// 定位初始化
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(4000);//监听时间间隔
//		option.setTimeOut(arg0);
		option.setAddrType("all");//返回的定位结果包含地址信息

		mLocClient.setLocOption(option);
		mLocClient.start();
		
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null )
				return;				
//			MyLocationData locData = new MyLocationData.Builder()
//					.accuracy(location.getRadius())
//					.direction(100).latitude(location.getLatitude())
//					.longitude(location.getLongitude()).build();
//				mBaiduMap.setMyLocationData(locData);

//			if (isLoc) {
//				isLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				Lat = location.getLatitude();
				Lng = location.getLongitude();
				setLat(Lat);
				setLng(Lng);
				System.out.println("Lat11111111---:"+Lat);
				System.out.println("Lng11111111---:"+Lng);

				position = location.getAddrStr();
				
				mLocClient.stop();
				
				if(callback!=null){
					callback.callback();
				}
			
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	
	public static double getLat() {
		return Lat;
	}



	public void setLat(double lat) {
		Lat = lat;
	}


	public static double getLng() {
		return Lng;
	}



	public void setLng(double lng) {
		Lng = lng;
	}


	public static LocationClient getLocClient(){
		return mLocClient;
	}
	
	


	public static String getPosition() {
		return position;
	}

	public static void setPosition(String position) {
		Distance.position = position;
	}

	/**
	 * 获得两个坐标间的距离
	 * @param llA 我当前的坐标
	 * @param llB 纸条的坐标
	 * @return
	 */
	public static double distance(LatLng llA,LatLng llB){
		DistanceUtil distanceU = new DistanceUtil();
		double distance = distanceU.getDistance(llA, llB);
		return distance;
	}
	
	public void setCallBack(PointCallback callback){
		this.callback = callback;
	}
	
	public PointCallback getCallback(){
		return callback;
	}
	
	public interface PointCallback{
		public void callback();
	}

}

