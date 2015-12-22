package com.project.sendfromposition;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
















import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.OSSException;
import com.aliyun.mbaas.oss.model.TokenGenerator;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.storage.OSSFile;
import com.aliyun.mbaas.oss.storage.TaskHandler;
import com.aliyun.mbaas.oss.util.OSSToolKit;
import com.project.R;
import com.project.activity.ShowBigImage;
import com.project.app.MyApplication;
import com.project.util.BimpManage;
import com.project.util.CommonUtils;
import com.project.util.Distance;
import com.project.util.Distance.PointCallback;
import com.project.util.HttpUtil;
import com.project.util.ImageUtils;

public class WritePlaceActivity extends Activity {
	
	public static final String accessKey = "03KYB2AtkMSC0NQE"; 			// 测试代码没有考虑AK/SK的安全性
    public static final String screctKey = "fBgtjQ9vlmdqxeGcylXQidbmFC7pIe";
	
	private static final int REQUEST_CAMERA  =  0;
	private static final int REQUEST_LOCAL =  1;
	
	private static final int MAX_INPUT = 50;
	
	private static Distance distance;
	
	private TaskHandler tk;
	private Intent intent;
	private String place;
	private String path;
	private String fileName;
	private String position;
	private int current = 0;
	private int curLen = 0;
	private int curLeft = MAX_INPUT;
	private boolean isSuccess = false;
	
	private File cameraFile;
	
	private Button send_pointment;
	private TextView tv_position;
	private TextView tv_max;
	private EditText edittext;
	
	private GridView noScrollgridview;
	private GridAdapter mAdapter;
	
	private Handler successHandler = new SucessHandler();
	private Handler testHandler = new TestHandler();
    private Handler failHandler = new FailHandler();

    public OSSBucket bucket;

    private TaskHandler tHandler;
	
    private List<OSSFile> fileList = new ArrayList<OSSFile>();
    private List<String> compressList = new ArrayList<String>();
    
    
    class FailHandler extends Handler{
    	@Override
    	public void handleMessage(Message msg) {
    		
    		if(msg.what==0){
    			Toast.makeText(getApplicationContext(),  "上传失败!", Toast.LENGTH_SHORT).show();
    		}else if(msg.what==1){
    			Toast.makeText(getApplicationContext(),  "邀请描述不能为空", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    
    
    class SucessHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	ImageUtils.deleteDir();
            Toast.makeText(getApplicationContext(),  "发布成功!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    class TestHandler extends Handler{
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		Bundle b = msg.getData();
    		System.out.println("当前data---："+b.getInt("current")+":-----------总："+b.getInt("total"));
    	}
    }
    
    private static void updateBar(String objectKey, int current, int total, Handler handler) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("objectKey", objectKey);
        b.putInt("current", current);
        b.putInt("total", total);
        msg.setData(b);
        handler.sendMessage(msg);
    }
    
    private static void makeToast(String oper, Handler handler) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("operation", oper);
//        b.putByteArray("data", data);
        msg.setData(b);
        handler.sendMessage(msg);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.to_my_place);
		
//		distance = Distance.getInstance(getApplicationContext());
		
		initOSS();
		
		setUpView();
		
		setListner();
	}
	
	
	public void initOSS(){
		OSSClient.setApplicationContext(getApplicationContext()); // 传入应用程序context
        OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                    String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;

                return OSSToolKit.generateToken(accessKey, screctKey, content);
            }
        });
        OSSClient.setGlobalDefaultHostId("oss-cn-shenzhen.aliyuncs.com"); // 设置全局默认数据中心域名
        OSSClient.setGlobalDefaultACL(AccessControlList.PRIVATE); // 设置全局默认bucket访问权限
        
        // 开始单个Bucket的设置
        bucket = new OSSBucket("test327393059");
        bucket.setBucketACL(AccessControlList.PUBLIC_READ_WRITE); // 如果这个Bucket跟全局默认的访问权限不一致，就需要单独设置
        // sampleBucket.setBucketHostId("oss-cn-hangzhou.aliyuncs.com"); // 如果这个Bucket跟全局默认的数据中心不一致，就需要单独设置
        // sampleBucket.setBucketTokenGen(new TokenGenerator() {...}); // 如果这个Bucket跟全局默认的加签方法不一致，就需要单独设置
        // sampleBucket.setBucketAccessRefer("your.refer.com"); // 如果这个Bucket开启了防盗链功能，就需要通过这个接口设置reference
	}

	private void setUpView() {
		send_pointment = (Button) findViewById(R.id.send);
		tv_position = (TextView) findViewById(R.id.tv_position);
		position = Distance.getInstance(getApplicationContext()).getPosition();
//		System.out.println("position------:"+position);
		tv_position.setText(position+")");
		tv_max = (TextView) findViewById(R.id.tv_max);
		edittext = (EditText) findViewById(R.id.edittext);
		
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new GridAdapter(this);
		mAdapter.loading();
		noScrollgridview.setAdapter(mAdapter);
	}

	private void setListner() {
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				if(position==BimpManage.bmp.size()){
					new PopupWindows(WritePlaceActivity.this, noScrollgridview);
				}else{
					Intent intent = new Intent(WritePlaceActivity.this,ShowBigImage.class);
					String imgPath = BimpManage.drr.get(position);
				}
			}
		});
		
		send_pointment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//发布按钮
				if(!edittext.getText().toString().equals("")){
					new PopupLoadingWindows(getApplicationContext(), noScrollgridview);
				}else{
					failHandler.sendEmptyMessage(1);
				}
			}
		});
		
		edittext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String curStr = edittext.getText().toString();
				curLen = curStr.length();
				curLeft = MAX_INPUT- curLen;
				
				if(curLen<MAX_INPUT){
					tv_max.setTextColor(Color.BLACK);
					tv_max.setText("你还能输入"+curLeft+"个字");
				}else{
					tv_max.setTextColor(Color.RED);
					tv_max.setText("你还能输入"+curLeft+"个字");
					s.delete(curLen+curLeft, curLen);
					edittext.setSelection(curLen);
					curLeft = 0;
				}
				
			}
		});
		
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
			Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
			
			return;
		}

//		File imgDir = new File(IMG_PATH);
//		if(!imgDir.exists()){
//			imgDir.mkdir();
//		}
		
		cameraFile = new File(ImageUtils.IMG_PATH, MyApplication.getInstance().getUserName()+"_"
				+ System.currentTimeMillis() + ".jpg");
		
		cameraFile.getParentFile().mkdirs();
		path = cameraFile.getPath();
		
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CAMERA);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case REQUEST_CAMERA:
//			System.out.println("path--:"+path);
			if (BimpManage.drr.size() < 8 && resultCode == -1) {
				BimpManage.drr.add(path);
			}
			break;
		case REQUEST_LOCAL:
			if (BimpManage.drr.size() < 8 && resultCode == -1) {
				if(data != null){
					Uri uri = data.getData();
					if(uri!=null){              
			            String uriStr=uri.toString();  
			            System.out.println("uriStr-------:"+uriStr);
			            path=uriStr.substring(10,uriStr.length());  
			            System.out.println("pathhh-------:"+path);
//			            BimpManage.drr.add(path);
			            if(path.startsWith("com.sec.android.gallery3d")){  
			                Log.e("TEST", "It's auto backup pic path:"+uri.toString());  
			                return;
			            }  
					}
				}
			}
			break;
		}
	}
	
	class GridAdapter extends BaseAdapter{

		private Context mContext;
		private int selectedPosition = -1;// 选中的位置
		
		public GridAdapter(Context mContext){
			this.mContext = mContext;
		}
		
		@Override
		public int getCount() {
			return  BimpManage.bmp.size()+1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		
		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView==null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_published_grida, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (position == BimpManage.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 8) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(BimpManage.bmp.get(position));
			}
			
			
			return convertView;
		}
		
		class ViewHolder{
			ImageView image;
		}
		
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mAdapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void update() {
			loading();
		}
		
		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (BimpManage.max == BimpManage.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = BimpManage.drr.get(BimpManage.max);
								System.out.println(path);
								Bitmap bm = BimpManage.revitionImageSize(path);
								BimpManage.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								ImageUtils.saveBitmap(bm, "" + newStr);
								compressList.add(ImageUtils.IMG_PATH+newStr+".JPEG");
								BimpManage.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}
	
	//自定义Popupwindows
	class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			final LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.roll_up));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					selectPicFromCamera() ;
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					selectPicFromLocal();
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if(view != ll_popup){
						dismiss();
					}
				}
			});
		}
	}
	
	
	class PopupLoadingWindows extends PopupWindow{
		
		private TextView tv_progress;
		private Handler progressHandler = new ProgressHandler();
		private String str = edittext.getText().toString();
		@SuppressWarnings("deprecation")
		public PopupLoadingWindows(final Context mContext , View parent){
			View view = View
					.inflate(mContext, R.layout.item_popuploadingwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			tv_progress = (TextView) view.findViewById(R.id.tv_progress);
			if(compressList.size()>0)
				tv_progress.setText("上传图片("+current+"/"+BimpManage.drr.size()+")");
			else if(str!="" && !str.equals("")){
					tv_progress.setText("上传邀请中");
			}else{
				dismiss();
			}
			
//			while(temp<BimpManage.drr.size()){
//			System.out.println("name-----:"+BimpManage.drr.get(current));
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					//上传图片到阿里云OSS
					if(compressList.size()>0){
						while(current<compressList.size()){
							fileName = compressList.get(current).substring(compressList.get(current).lastIndexOf("/")+1);
							OSSFile ossFile = new OSSFile(bucket,fileName);
							ossFile.setUploadFilePath(compressList.get(current), "image/jpg");
							try {
								ossFile.upload();
							} catch (FileNotFoundException e) {
								failHandler.sendEmptyMessage(0);
								dismiss();
							} catch (OSSException e) {
								failHandler.sendEmptyMessage(0);
								dismiss();
							}
							current++;
							System.out.println("current--------:"+current);
							Message msg = new Message();
							Bundle b = new Bundle();
							b.putInt("current",current); 
							msg.setData(b);
							progressHandler.sendMessage(msg);
						}
						
						send();
					}else{
						send();
					}
				}
			}).start();
//				}
				
//				tk = ossFile.ResumableUploadInBackground(new SaveCallback(){
//
//					@Override
//					public void onSuccess(String objectKey) {
//						current++;
//						isSuccess = true;
//						if(current==BimpManage.drr.size()){
//							dismiss();
//						}
//						
//						System.out.println("current--------:"+current);
//						
//						Message msg = new Message();
//						Bundle b = new Bundle();
//						b.putInt("current",current); 
//						msg.setData(b);
//						progressHandler.sendMessage(msg);
//					}
//	
//					@Override
//					public void onFailure(String objectKey, OSSException ossException) {
//						
//						Message msg = new Message();
//						Bundle b = new Bundle();
//						b.putString("msg", ossException.toString());
//						msg.setData(b);
//						failHandler.sendMessage(msg);
//						dismiss();
//					}
//	
//					@Override
//					public void onProgress(String objectKey, int byteCount, int totalSize) {
//						updateBar(objectKey, byteCount, totalSize, testHandler);
//					}
//				});
//			}
			
		}
		
		
		public void send(){
			
			Date curDate  =  new Date(System.currentTimeMillis());//获取当前时间       
//						final String time = formatter.format(curDate); 
			final long time = curDate.getTime();
			
			
				try{
					//上传发布
					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
					
					String url = "http://1.nodistanceservice.sinaapp.com/operationInvite.php";
					
					params.add(new BasicNameValuePair("Operation", "insert"));
					params.add(new BasicNameValuePair("AppID", MyApplication.getInstance().getUserName()));
					params.add(new BasicNameValuePair("inviteContent",str));
					params.add(new BasicNameValuePair("startTime", time+""));
					params.add(new BasicNameValuePair("place",position));
					params.add(new BasicNameValuePair("Lat", Distance.getLat()+""));
					params.add(new BasicNameValuePair("Lng",Distance.getLng()+""));
					
					if(BimpManage.drr.size()>0){
						for(int i=0 ; i<BimpManage.drr.size() ; i++){
							String filename = BimpManage.drr.get(i).substring(BimpManage.drr.get(i).lastIndexOf("/")+1,BimpManage.drr.get(i).lastIndexOf("."));
							params.add(new BasicNameValuePair("imgUrl"+i, filename+".JPEG"));
						}
					}
					
					HttpUtil.queryStringForPost(url, params);
				
				}catch(Exception e){
					e.printStackTrace();
				}
		}
		
		
		
		
		class ProgressHandler extends Handler {
	        @SuppressLint("HandlerLeak")
			@Override
	        public void handleMessage(Message msg) {
	            Bundle b = msg.getData();
	            int currentUpload = b.getInt("current");
	            tv_progress.setText("上传图片("+currentUpload+"/"+BimpManage.drr.size()+")");
	            if(currentUpload==BimpManage.drr.size()){
	            	dismiss();
	            	Message msg1 = new Message();
	            	msg1.arg1=1;
	            	successHandler.sendMessage(msg1);
				}
	        }
	    }
	}
		
	
	
	@Override
	protected void onRestart() {
		mAdapter.update();
		super.onRestart();
	}
}
