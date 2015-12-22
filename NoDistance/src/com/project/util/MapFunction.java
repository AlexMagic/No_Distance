package com.project.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.project.R;
import com.project.db.UserDao;

public class MapFunction {
	private BaiduMap mBaiduMap;
	
	
	private Context context;
	
	public MapFunction(BaiduMap mBaiduMap,Context context){
		this.mBaiduMap = mBaiduMap;
		this.context = context;
	}
	/**
	 * 在地图上画一个多边形，并标注
	 * @param pts
	 * @param llText
	 * @param str
	 */
	public void drawPolygon(List<LatLng> pts,LatLng llText,String str){
		OverlayOptions ooPolygon = new PolygonOptions().points(pts)
				.stroke(new Stroke(6, 0xAA8DB6CD)).fillColor(0xAA8DB6CD);
		mBaiduMap.addOverlay(ooPolygon);
		OverlayOptions ooText = new TextOptions().bgColor(0x00FFFF00)
				.fontSize(24).fontColor(0xFF9E9E9E).text(str).rotate(-30)
				.position(llText);
		mBaiduMap.addOverlay(ooText);
	}
	
	/**
	 * 设置一个Find标识
	 * @param ll
	 * @param mMarkerA
	 */
	public Marker setFindMarker(LatLng ll,String AppID,String nickname){
		double tLat = ll.latitude+0.000300;
		double tLng = ll.longitude;
		UserDao userDao = new UserDao(context);
		BitmapDescriptor bd;
		if(userDao.isExist(AppID)){
			bd = BitmapDescriptorFactory
					.fromResource(R.drawable.map_friendmarker);
		}else{
			bd = BitmapDescriptorFactory
				.fromResource(R.drawable.map_marker);
		}
		OverlayOptions ooA = new MarkerOptions().position(ll).icon(bd)
				.zIndex(9).draggable(true);
		Marker mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
		mMarker.setTitle(AppID);
		//设置中心点
		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(status);
		//设置名字
		LatLng tll = new LatLng(tLat,tLng);
		OverlayOptions ooText = new TextOptions().bgColor(0x00FFFF00)
				.fontSize(24).fontColor(0xFF8B1C62).text(nickname).rotate(0)
				.position(tll);
		mBaiduMap.addOverlay(ooText);
		return mMarker;
	}
	
	/**
	 * 设置一个好友标识
	 * @param ll
	 * @param mMarkerA
	 */
	public Marker setFriendMarker(LatLng ll,String AppID,String nickname){
		double tLat = ll.latitude+0.000300;
		double tLng = ll.longitude;
		BitmapDescriptor bd = BitmapDescriptorFactory
				.fromResource(R.drawable.map_friendmarker);
		OverlayOptions ooA = new MarkerOptions().position(ll).icon(bd)
				.zIndex(9).draggable(true);
		Marker mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
		mMarker.setTitle(AppID);
		//设置中心点
		MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(status);
		//设置名字
		LatLng tll = new LatLng(tLat,tLng);
		OverlayOptions ooText = new TextOptions().bgColor(0x00FFFF00)
				.fontSize(24).fontColor(0xFF8B1C62).text(nickname).rotate(0)
				.position(tll);
		mBaiduMap.addOverlay(ooText);
		return mMarker;
	}
	
	/**
	 * 获得两个坐标间的距离
	 * @param llA
	 * @param llB
	 * @return
	 */
	public static double distance(LatLng llA,LatLng llB){
		DistanceUtil distanceU = new DistanceUtil();
		double distance = distanceU.getDistance(llA, llB);
		return distance;
	}
	
	/**
	 * 设置地图类型 1：普通地图 2：卫星地图
	 * @param type
	 * @return
	 */
	public String setMapType(int type){
		if(type==1){
			//普通地图  
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); 
			return "普通地图";
		}
		if(type==2){
			//卫星地图  
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			return "卫星地图";
		}
		return null; 

	}
	
	public void drawPolygonA(){
		LatLng pt1 = new LatLng(22.600280, 113.950565);
		LatLng pt2 = new LatLng(22.596326, 113.949119);
		LatLng pt3 = new LatLng(22.594866, 113.950988);
		LatLng pt4 = new LatLng(22.599413, 113.952164);
		List<LatLng> pts = new ArrayList<LatLng>();
		pts.add(pt1);
		pts.add(pt2);
		pts.add(pt3);
		pts.add(pt4);
		LatLng llText = new LatLng(22.597686, 113.950700);
		MapFunction mf = new MapFunction(mBaiduMap , context);
		mf.drawPolygon(pts,llText,"北");
	}
	
	public void drawPolygonB(){
		LatLng pt1 = new LatLng(22.587993, 113.940199);
		LatLng pt2 = new LatLng(22.587434, 113.940136);
		LatLng pt3 = new LatLng(22.586516, 113.943999);
		LatLng pt4 = new LatLng(22.586633, 113.949317);
		LatLng pt5 = new LatLng(22.589144, 113.949308);
		LatLng pt6 = new LatLng(22.589160, 113.943622);
		List<LatLng> pts = new ArrayList<LatLng>();
		pts.add(pt1);
		pts.add(pt2);
		pts.add(pt3);
		pts.add(pt4);
		pts.add(pt5);
		pts.add(pt6);
		LatLng llText = new LatLng(22.588560, 113.945167);
		MapFunction mf = new MapFunction(mBaiduMap , context);
		mf.drawPolygon(pts,llText,"西");
	}
	
	public void drawPolygonC(){
		LatLng pt1 = new LatLng(22.596468, 113.952111);
		LatLng pt2 = new LatLng(22.595817, 113.953171);
		LatLng pt3 = new LatLng(22.592806, 113.956422);
		LatLng pt4 = new LatLng(22.591646, 113.957096);
		LatLng pt5 = new LatLng(22.590036, 113.957590);
		LatLng pt6 = new LatLng(22.590128, 113.959207);
		LatLng pt7 = new LatLng(22.589636, 113.961067);
		LatLng pt8 = new LatLng(22.591897, 113.961408);
		LatLng pt9 = new LatLng(22.593548, 113.961130);
		LatLng pt10 = new LatLng(22.595050, 113.959737);
		LatLng pt11 = new LatLng(22.596460, 113.956988);
		LatLng pt12 = new LatLng(22.599229, 113.952784);
		List<LatLng> pts = new ArrayList<LatLng>();
		pts.add(pt1);
		pts.add(pt2);
		pts.add(pt3);
		pts.add(pt4);
		pts.add(pt5);
		pts.add(pt6);
		pts.add(pt7);
		pts.add(pt8);
		pts.add(pt9);
		pts.add(pt10);
		pts.add(pt11);
		pts.add(pt12);
		LatLng llText = new LatLng(22.593607, 113.958075);
		MapFunction mf = new MapFunction(mBaiduMap , context);
		mf.drawPolygon(pts,llText,"东");
	}
	
	public void drowCircle(LatLng llCircle,int radius){
		// 添加圆
		OverlayOptions ooCircle = new CircleOptions().fillColor(0x20FF8C00)
				.center(llCircle).stroke(new Stroke(5, 0xA0FF4500))
				.radius(radius);
		mBaiduMap.addOverlay(ooCircle);
	}

}
