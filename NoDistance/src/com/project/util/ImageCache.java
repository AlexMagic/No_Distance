package com.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.v4.util.LruCache;

public class ImageCache {
	
	private LruCache<String, Bitmap> cache = null;
	
	private static ImageCache instance = null;
	
	// 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。 
    // LruCache通过构造函数传入缓存值，以KB为单位。 
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); 
    
 // 使用最大可用内存值的1/8作为缓存的大小。 
    int cacheSize = maxMemory / 8; 
	
	private ImageCache(){
		// use 1/8 of available heap size
		cache = new LruCache<String, Bitmap>(cacheSize){
			
			// 重写此方法来衡量每张图片的大小，默认返回图片数量。
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
	 * 将一张图片存储在LruChache中
	 * @param key
	 * @param value
	 * @return  the puts bitmap
	 */
	public Bitmap put(String key, Bitmap value){
		return cache.put(key, value);
	}
	
	/**
	 * return the bitmap
	 * 从缓存中获取一张图片
	 * @param key
	 * @return
	 */
	public Bitmap get(String key){
		return cache.get(key);
	}
	
	/**
	 * 计算出图片的宽高
	 */
	private int calculateInSampleSize(BitmapFactory.Options options , int columW , int columH){
		int inSimpalSize = 1;
		//源图片的宽高
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
	 * 获取图片
	 */
	
	public Bitmap decodeSampledBitmapResource(String path,int columW , int columH){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);  
        // 调用上面定义的方法计算inSampleSize值  
        options.inSampleSize = calculateInSampleSize(options, columW , columH);  
        // 使用获取到的inSampleSize值再次解析图片  
        options.inJustDecodeBounds = false; 
		return BitmapFactory.decodeFile(path, options);
	}
	
}
