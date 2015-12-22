package com.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.v4.util.LruCache;

public class ImageCache {
	
	private LruCache<String, Bitmap> cache = null;
	
	private static ImageCache instance = null;
	
	// ��ȡ�������ڴ�����ֵ��ʹ���ڴ泬�����ֵ������OutOfMemory�쳣�� 
    // LruCacheͨ�����캯�����뻺��ֵ����KBΪ��λ�� 
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); 
    
 // ʹ���������ڴ�ֵ��1/8��Ϊ����Ĵ�С�� 
    int cacheSize = maxMemory / 8; 
	
	private ImageCache(){
		// use 1/8 of available heap size
		cache = new LruCache<String, Bitmap>(cacheSize){
			
			// ��д�˷���������ÿ��ͼƬ�Ĵ�С��Ĭ�Ϸ���ͼƬ������
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	public static synchronized ImageCache getInstance(){
		if(instance == null){
			instance = new ImageCache();
		}
		
		return instance;
	}
	
	/**
	 * put bitmap to image cache
	 * ��һ��ͼƬ�洢��LruChache��
	 * @param key
	 * @param value
	 * @return  the puts bitmap
	 */
	public Bitmap put(String key, Bitmap value){
		return cache.put(key, value);
	}
	
	/**
	 * return the bitmap
	 * �ӻ����л�ȡһ��ͼƬ
	 * @param key
	 * @return
	 */
	public Bitmap get(String key){
		return cache.get(key);
	}
	
	/**
	 * �����ͼƬ�Ŀ��
	 */
	private int calculateInSampleSize(BitmapFactory.Options options , int columW , int columH){
		int inSimpalSize = 1;
		//ԴͼƬ�Ŀ��
		final int width = options.outWidth;
		final int height = options.outHeight;
		int widthR = 1 , heightR = 1;
		if(width>columW){
			widthR = Math.round((float)width/(float)columW);
			heightR = Math.round((float)height/(float)columH);
		}
		
		inSimpalSize = widthR > heightR ?heightR:widthR;
		return inSimpalSize;
	}
	
	/**
	 * ��ȡͼƬ
	 */
	
	public Bitmap decodeSampledBitmapResource(String path,int columW , int columH){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);  
        // �������涨��ķ�������inSampleSizeֵ  
        options.inSampleSize = calculateInSampleSize(options, columW , columH);  
        // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ  
        options.inJustDecodeBounds = false; 
		return BitmapFactory.decodeFile(path, options);
	}
	
}
