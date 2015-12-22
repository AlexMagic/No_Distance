package com.project.maps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.project.R;
import com.project.app.MyApplication;
import com.project.util.CommonUtils;
import com.project.util.Distance;
import com.project.util.HttpUtil;
import com.project.util.ImageUtils;

/**
 * 发布纸条类
 * 背景可选用自己的图库上的图片 ， 或者可拍照
 * 发布结束后finish()自己
 * @author Administrator
 *
 */

public class WriteBottleActivity extends Activity implements OnClickListener{
	
	private static final int REQUEST_CAMERA  =  0;
	private static final int REQUEST_LOCAL =  1;
	
	private static final String OPRE_INSERT = "insert";
	
//	private final static String IMG_PATH  
//	    = Environment.getExternalStorageDirectory() + "/myApp_img/";
	
	private EditText et_content;
	private ImageView iv_photo_camera;
	private ImageView iv_photo_local;
	private Button btn_send;
//	private SeekBar seekBar;
	private ImageView btn_back;
	private RelativeLayout layout;
	
	
	private File cameraFile;
	
	private Bitmap mBitmap;
	private BitmapDrawable bd ;
	
	private boolean isClickBg= false;
	
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			
			switch(msg.what){
			case 0:
				System.out.println("what---:"+what);
				Toast.makeText(WriteBottleActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.write_bottle);
		
		Distance.getInstance(this).getLocClient().start();
		
		setUpView();
		
		setListner();
	}


	private void setUpView() {
		et_content = (EditText) findViewById(R.id.edittext);
//		iv_photo_camera = (ImageView) findViewById(R.id.iv_take_picture);
//		iv_photo_local = (ImageView) findViewById(R.id.iv_local_picture);
		btn_send = (Button) findViewById(R.id.btn_send);
		layout = (RelativeLayout) findViewById(R.id.bottle_layout);
		btn_back = (ImageView) findViewById(R.id.btn_back);
	}
	
	private void setListner(){
		iv_photo_local.setOnClickListener(this);
		iv_photo_camera.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		layout.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CAMERA){
			if (cameraFile != null && cameraFile.exists()){
				
				//更新背景
				mBitmap = BitmapFactory.decodeFile(cameraFile.getAbsolutePath());
				mBitmap = this.zoomImage(mBitmap, 600, 600);
				bd=new BitmapDrawable(mBitmap);  
				layout.setBackgroundDrawable(bd);
				
				et_content.setTextColor(android.graphics.Color.WHITE);
			}
		}
		
		if(requestCode == REQUEST_LOCAL){
			if(data != null){
				System.out.println("data not null");
				Uri uri = data.getData();
				if(uri!=null){              
		            String uriStr=uri.toString();  
		            String path=uriStr.substring(10,uriStr.length());  
		            if(path.startsWith("com.sec.android.gallery3d")){  
		                Log.e("TEST", "It's auto backup pic path:"+uri.toString());  
		                return;
		            }  
					
				}
				reFreshBackground(uri);
			}
		}
	}

	
	//把背景换成从图库选取的图片
	private void reFreshBackground(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			String picturePath = cursor.getString(columnIndex);
			
			System.out.println("path---:"+picturePath);
			
			cursor.close();
			cursor = null;

			
			
			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			
			//更新背景
			mBitmap = BitmapFactory.decodeFile(picturePath);
			mBitmap = this.zoomImage(mBitmap, 600, 600);
			bd=new BitmapDrawable(mBitmap);  
			layout.setBackgroundDrawable(bd);
			et_content.setTextColor(android.graphics.Color.WHITE);
		}else {
			File file = new File(uri.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
		}
	}


	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			System.out.println("sdk_int----:"+"<19");
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_LOCAL);
	}

	
	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照", 0).show();
			return;
		}

//		File imgDir = new File(IMG_PATH);
//		if(!imgDir.exists()){
//			imgDir.mkdir();
//		}
		
		cameraFile = new File(ImageUtils.IMG_PATH, MyApplication.getInstance().getUserName()
				+ System.currentTimeMillis() + ".jpg");
		
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CAMERA);
	}
	

	@Override
	public void onClick(View view) {
		switch(view.getId()){
//		case R.id.iv_take_picture:
//			selectPicFromCamera();
//			break;
//		case R.id.iv_local_picture:
//			selectPicFromLocal();
//			break;
		case R.id.btn_send:	
			
//			String username = MyApplication.getInstance().getUserName();
//			String nickname = MyApplication.getInstance().getNickName();
			
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");       
			Date curDate  =  new Date(System.currentTimeMillis());//获取当前时间       
//			final String time = formatter.format(curDate); 
			final long time = curDate.getTime();
			
			final String content = et_content.getText().toString();
			System.out.println("Lat-----write:"+(Double)Distance.getInstance(this).getLat()+"");
			System.out.println("Lng-----:"+Distance.getInstance(this).getLng());
			final double lat = Distance.getInstance(this).getLat();
			final double lng = Distance.getInstance(this).getLng();
			if(content.length()>0){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try{
							//上传发布
							List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
							
							String url = "http://1.nodistanceservice.sinaapp.com/operationNote.php";
							
							params.add(new BasicNameValuePair("Operation", OPRE_INSERT));
							params.add(new BasicNameValuePair("AppID", MyApplication.getInstance().getUserName()));
							params.add(new BasicNameValuePair("Content",content));
							params.add(new BasicNameValuePair("Time", time+""));
							params.add(new BasicNameValuePair("Goods",""));
							params.add(new BasicNameValuePair("Lat", lat+""));
							params.add(new BasicNameValuePair("Lng",lng+""));
							
							HttpUtil.queryStringForPost(url, params);
						
						}catch(Exception e){
							e.printStackTrace();
						}
						
						//上传图片
						if(mBitmap != null){
							
						}
						
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				}).start();
			}
			break;
		case R.id.bottle_layout:
			if(isClickBg)
				layout.setBackgroundDrawable(null);
			else
				layout.setBackgroundDrawable(bd);
			
			isClickBg = !isClickBg;
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {  
		  
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

        // DocumentProvider  
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
            // ExternalStorageProvider  
            if (isExternalStorageDocument(uri)) {  
                final String docId = DocumentsContract.getDocumentId(uri);  
                final String[] split = docId.split(":");  
                final String type = split[0];  

                if ("primary".equalsIgnoreCase(type)) {  
                    return Environment.getExternalStorageDirectory() + "/" + split[1];  
                }  

            }  
            // DownloadsProvider  
            else if (isDownloadsDocument(uri)) {  
                final String id = DocumentsContract.getDocumentId(uri);  
                final Uri contentUri = ContentUris.withAppendedId(  
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

                return getDataColumn(context, contentUri, null, null);  
            }  
            // MediaProvider  
            else if (isMediaDocument(uri)) {  
                final String docId = DocumentsContract.getDocumentId(uri);  
                final String[] split = docId.split(":");  
                final String type = split[0];  

                Uri contentUri = null;  
                if ("image".equals(type)) {  
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                } else if ("video".equals(type)) {  
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
                } else if ("audio".equals(type)) {  
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
                }  

                final String selection = "_id=?";  
                final String[] selectionArgs = new String[] {  
                        split[1]  
                };  

                return getDataColumn(context, contentUri, selection, selectionArgs);  
            }  
        }  
        // MediaStore (and general)  
        else if ("content".equalsIgnoreCase(uri.getScheme())) {  
            // Return the remote address  
            if (isGooglePhotosUri(uri))  
                return uri.getLastPathSegment();  

            return getDataColumn(context, uri, null, null);  
        }  
        // File  
        else if ("file".equalsIgnoreCase(uri.getScheme())) {  
            return uri.getPath();  
        }  

        return null;  
    }  
	  /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is ExternalStorageProvider. 
     */  
    public static boolean isExternalStorageDocument(Uri uri) {  
        return "com.android.externalstorage.documents".equals(uri.getAuthority());  
    }  

    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is DownloadsProvider. 
     */  
    public static boolean isDownloadsDocument(Uri uri) {  
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
    }  

    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is MediaProvider. 
     */  
    public static boolean isMediaDocument(Uri uri) {  
        return "com.android.providers.media.documents".equals(uri.getAuthority());  
    }  

    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is Google Photos. 
     */  
	public static boolean isGooglePhotosUri(Uri uri) {  
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
    }  
	
	 /** 
     * Get the value of the data column for this Uri. This is useful for 
     * MediaStore Uris, and other file-based ContentProviders. 
     * 
     * @param context The context. 
     * @param uri The Uri to query. 
     * @param selection (Optional) Filter used in the query. 
     * @param selectionArgs (Optional) Selection arguments used in the query. 
     * @return The value of the _data column, which is typically a file path. 
     */  
    public static String getDataColumn(Context context, Uri uri, String selection,  
            String[] selectionArgs) {  

        Cursor cursor = null;  
        final String column = "_data";  
        final String[] projection = {  
                column  
        };  

        try {  
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
                    null);  
            if (cursor != null && cursor.moveToFirst()) {  
                final int index = cursor.getColumnIndexOrThrow(column);  
                return cursor.getString(index);  
            }  
        } finally {  
            if (cursor != null)  
                cursor.close();  
        }  
        return null;  
    }  
    
    
    
    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */ 
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, 
                    double newHeight) { 
            // 获取这个图片的宽和高 
            float width = bgimage.getWidth(); 
            float height = bgimage.getHeight(); 
            // 创建操作图片用的matrix对象 
            Matrix matrix = new Matrix(); 
            // 计算宽高缩放率 
            float scaleWidth = ((float) newWidth) / width; 
            float scaleHeight = ((float) newHeight) / height; 
            // 缩放图片动作 
            matrix.postScale(scaleWidth, scaleHeight); 
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, 
                            (int) height, matrix, true); 
            return bitmap; 
    } 
    
}
