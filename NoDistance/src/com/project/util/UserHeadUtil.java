//package com.project.util;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.lang.ref.SoftReference;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Handler;
//import android.os.Message;
//
//import com.aliyun.openservices.ClientException;
//import com.aliyun.openservices.oss.OSSClient;
//import com.aliyun.openservices.oss.OSSException;
//import com.aliyun.openservices.oss.model.ObjectMetadata;
//
//
///**
// * 从阿里云oss上获取用户头像
// * @author Administrator
// *
// */
//
//public class UserHeadUtil {
//	private static final String ACCESS_ID = "03KYB2AtkMSC0NQE";
//    private static final String ACCESS_KEY = "fBgtjQ9vlmdqxeGcylXQidbmFC7pIe";
//    private static final String BUCKET_NAME = "test327393059";
//    private static final String TYPE_PNG = ".png";
//    private static final String TYPE_JPG = ".jpg";
//    private final static String DEFAULT_URL = "http://test327393059.oss-cn-shenzhen.aliyuncs.com/v5_0_1_widget_default_head.png";
//    private final static String OSS_URL = "http://test327393059.oss-cn-shenzhen.aliyuncs.com/";
//    //    private String bucketName = "";
//    private String username;
//    private String filename;
//    private Bitmap mBitmap;
//    
//    //缓存图片 把图片的软引用放到map中
//    private Map<String, SoftReference<Bitmap>> imageMap;
//    
//    private OSSClient client;
//    private String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
//    
//    private ImageCallBack callback;
//    
//    public final Handler handler = new Handler(){
//    	@Override
//    	public void handleMessage(Message msg) {
//    		callback.callback((Bitmap)msg.obj);
//    	}
//    };
//    
//    
//    private static UserHeadUtil instance = null;
//    
//    public static UserHeadUtil getInstance(){
//    	if(instance==null){
//    		instance = new UserHeadUtil();
//    	}
//    	
//    	return instance;
//    }
//    
//    public UserHeadUtil(){
//    	// 使用默认的OSS服务器地址创建OSSClient对象。
////        client = new OSSClient(endpoint,ACCESS_ID, ACCESS_KEY);
//    	this.imageMap = new HashMap<String, SoftReference<Bitmap>>();
//    }
//    
//    //下载头像
//    public Bitmap getFrdHeader(String username , ImageCallBack callback){
//    	final String url =OSS_URL+username+TYPE_PNG;  
//    	
//    	this.callback = callback;
//    	
//    	//先看缓存(Map)中是否存在
//        if(imageMap.containsKey(url)){
//          SoftReference<Bitmap> softReference = imageMap.get(url);
//          Bitmap mBitmap = softReference.get();
//          if(mBitmap != null){
//            return mBitmap;
//          }
//        }
//    	
//    	new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					mBitmap = BitmapFactory.decodeStream(getImageStream(url));  
//	       
////			    	if(mBitmap==null){
////			        	mBitmap = BitmapFactory.decodeStream(getImageStream(DEFAULT_URL));  
////			        }
//			    	
//			    	imageMap.put(url, new SoftReference<Bitmap>(mBitmap));  
//			    	
//			    	Message message = handler.obtainMessage(0, mBitmap);  
//			    	handler.sendMessage(message);  
//			    	
//				} catch (Exception e) {  
//					e.printStackTrace();  
//	        	}
//			}
//		}).start();
//    	  
//    	return null;
//    }
//    
//    public Bitmap getHeaderFromMap(String username){
////    	Bitmap bitmap = null;
//    	String key = OSS_URL+username+TYPE_PNG;
//    	
//    	if(imageMap.containsKey(key)){
//    		SoftReference<Bitmap> softReference = imageMap.get(key);
//            Bitmap mBitmap = softReference.get();
//            if(mBitmap != null){
//              return mBitmap;
//            }
//    	}
//    	
//    	return null;
//    }
//    
//    
//    /** 
//     * Get image from newwork 
//     * @param path The path of image 
//     * @return byte[] 
//     * @throws Exception 
//     */  
//    public byte[] getImage(String path) throws Exception{  
//        URL url = new URL(path);  
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
//        conn.setConnectTimeout(5 * 1000);  
//        conn.setRequestMethod("GET");  
//        InputStream inStream = conn.getInputStream();  
//        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
//            return readStream(inStream);  
//        }  
//        return null;  
//    }  
//  
//    /** 
//     * Get image from newwork 
//     * @param path The path of image 
//     * @return InputStream 
//     * @throws Exception 
//     */  
//    public InputStream getImageStream(String path) throws Exception{  
//        URL url = new URL(path);  
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
//        conn.setConnectTimeout(5 * 1000);  
//        conn.setRequestMethod("GET");  
//        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
//            return conn.getInputStream();  
//        }  
//        return null;  
//    } 
//
//    
//    
//    
//    /** 
//     * Get data from stream 
//     * @param inStream 
//     * @return byte[] 
//     * @throws Exception 
//     */  
//    public static byte[] readStream(InputStream inStream) throws Exception{  
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
//        byte[] buffer = new byte[1024];  
//        int len = 0;  
//        while( (len=inStream.read(buffer)) != -1){  
//            outStream.write(buffer, 0, len);  
//        }  
//        outStream.close();  
//        inStream.close();  
//        return outStream.toByteArray();  
//    }  
//    
//    
//    
//    //上传头像
//    public void upLoadHeader(String username , String filename) throws OSSException, ClientException, FileNotFoundException{
//    	 File file = new File(filename);
//    	 
//    	 ObjectMetadata objectMeta = new ObjectMetadata();
//         objectMeta.setContentLength(file.length());
//         // 可以在metadata中标记文件类型
//         objectMeta.setContentType("image/*");
//
//         InputStream input = new FileInputStream(file);
//         client.putObject(BUCKET_NAME, username+TYPE_JPG, input, objectMeta);
//    }
//    
//    public interface ImageCallBack{
//    	public void callback(Bitmap bitmap);
//    }
//    
//}
