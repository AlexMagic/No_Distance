//package com.project.schoolservice;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.project.R;
////import com.project.activity.base.FlipperLayout.OnOpenListener;
//import com.project.activity.base.FlipperLayout1.OnOpenListener;
//import com.project.activity.base.ScrollLayout;
//
//public class SchoolService implements OnClickListener{
//	
//	private ScrollLayout mScrollLayout;
//	private LinearLayout[] mTextItems;
//	private FrameLayout mLayout_1;
//	private FrameLayout mLayout_2;
//	private FrameLayout mLayout_3;
//	private View mSchoolSerive;
//	private View serivce_under_bar;
//	
//	private BookSearch bookSearch;
//	
//	private ListView listView;
//	
//	private Activity mActivity;
//	private Context mContext;
//	
//	private ImageView mFlip ;
//	private Button mSerivceMenu ;
//	private ImageButton mSearch;
//	
//	private TextView searchBook;
//	private TextView teacherCommu;
//	private TextView opinion;
//	
//	private EditText editText;
//	
//	private int mViewCount;
//	private int mCurSel;
//	
//	private String bookName;
//	
//	private boolean isMenuOnClick = false;
//	
//	private OnOpenListener mOnOpenListener;
//	
//	public SchoolService(Context context , Activity activity){
//		mContext = context ;
//		mActivity = activity;
//		
//		
//		mSchoolSerive = LayoutInflater.from(context).inflate(R.layout.school_server, null);
////		bookSearch = new BookSearch(activity, context);
//		
//		init();
//		
//		setListner();
//	}
//
//	
//	public void init(){
//		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.FILL_PARENT);
//		
//		serivce_under_bar = mSchoolSerive.findViewById(R.id.serivce_under_bar);
//		
//		mFlip = (ImageView) mSchoolSerive.findViewById(R.id.btn_goto_residenmenu);
//		mSerivceMenu = (Button) mSchoolSerive.findViewById(R.id.serivce_popmenu);
////		mSearch = (ImageButton) mSchoolSerive.findViewById(R.id.book_searchclear);
//		
//		
//		searchBook = (TextView) mSchoolSerive.findViewById(R.id.book_search);
//		teacherCommu = (TextView) mSchoolSerive.findViewById(R.id.teacher_commu);
//		opinion = (TextView) mSchoolSerive.findViewById(R.id.opinion);
//	
//		
//		listView = (ListView) mSchoolSerive.findViewById(R.id.book_list);
//		
//		mScrollLayout = (ScrollLayout) mSchoolSerive.findViewById(R.id.srocll_layout);
//		
//		mLayout_1 = (FrameLayout) mSchoolSerive.findViewById(R.id.framelayout_1);
//		mLayout_2 = (FrameLayout) mSchoolSerive.findViewById(R.id.framelayout_2);
//		mLayout_3 = (FrameLayout) mSchoolSerive.findViewById(R.id.framelayout_3);
//		
//		LinearLayout linearLayout = (LinearLayout) mSchoolSerive.findViewById(R.id.lllayout);
//		
//		
//		
//		mViewCount = mScrollLayout.getChildCount();
//		
//		mTextItems = new LinearLayout[mViewCount];
////		mLayout_1.addView(bookSearch.getView());
//		for(int i=0; i<mViewCount; i++){
//			mTextItems[i] = (LinearLayout) linearLayout.getChildAt(i);
////			mLayout[i].addView(bookSearch.getView());
//			mTextItems[i].setEnabled(true);
//			mTextItems[i].setTag(i);
//			mTextItems[i].setOnClickListener(this);
//		}
//		
//		mCurSel = 0;
//		mTextItems[mCurSel].setEnabled(false);
//	}
//	
//	private void setListner() {
//		mFlip.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				if (mOnOpenListener != null) {
//					mOnOpenListener.open();
//				}
//			}
//		});
//		
//		mSerivceMenu.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				if(!isMenuOnClick){
//					Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in) ;
//					serivce_under_bar.setAnimation(animation);
//					serivce_under_bar.setVisibility(View.VISIBLE);
//					isMenuOnClick = true;
//				}else{
//					Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out) ;
//					serivce_under_bar.setAnimation(animation);
//					serivce_under_bar.setVisibility(View.GONE);
//					isMenuOnClick = false;
//				}
//				
//			}
//		});
//		
//		
//		
//	}
//	
//	
//	public View getView(){
//		return mSchoolSerive;
//	}
//	
//	public void setOnOpenListener(OnOpenListener onOpenListener) {
//		mOnOpenListener = onOpenListener;
//	}
//
//	public void setCurTextItem(int index){
//		if (index < 0 || index > mViewCount - 1 || mCurSel == index){
//    		return ;
//    	}  
//		
//		mTextItems[mCurSel].setEnabled(true);
//		mTextItems[index].setEnabled(false);
//		mCurSel = index;
//		
//		if(index == 0){
//			searchBook.setTextColor(0xff2ea3fe);
//    		teacherCommu.setTextColor(0xff999999);
//    		opinion.setTextColor(0xff999999);
//		}else if(index == 1){
//			searchBook.setTextColor(0xff999999);
//    		teacherCommu.setTextColor(0xff2ea3fe);
//    		opinion.setTextColor(0xff999999);
//		}else if(index == 2){
//			searchBook.setTextColor(0xff999999);
//    		teacherCommu.setTextColor(0xff999999);
//    		opinion.setTextColor(0xff2ea3fe);
//		}
//	}
//	
//
//	public void show(){
//		if(!isMenuOnClick){
//			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in) ;
//			serivce_under_bar.setAnimation(animation);
//			serivce_under_bar.setVisibility(View.VISIBLE);
//			isMenuOnClick = true;
//		}else{
//			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out) ;
//			serivce_under_bar.setAnimation(animation);
//			serivce_under_bar.setVisibility(View.GONE);
//			isMenuOnClick = false;
//		}
//	}
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		int pos = (Integer) v.getTag();
//		setCurTextItem(pos);
//		mScrollLayout.snapToScreen(pos);
//	}
//}



package com.project.schoolservice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.project.R;
import com.project.activity.base.FlipperLayout1.OnOpenListener;
import com.project.activity.base.ScrollLayout;

public class SchoolService implements OnClickListener{
	
	private ScrollLayout mScrollLayout;
	private LinearLayout[] mTextItems;
	private View mSchoolSerive;
	private View serivce_under_bar;
	
	private ListView listView;
	
	private Activity mActivity;
	private Context mContext;
	
	private ImageView mFlip ;
	private Button mSerivceMenu ;
	private ImageButton mClear;
	
	private TextView searchBook;
	private TextView teacherCommu;
	private TextView opinion;
	private Button btn_keep;
	private Button btn_alarms;
	
	private EditText editText;
	
	private int mViewCount;
	private int mCurSel;
	
	private String bookName;
	
	private InputMethodManager manager;
	private ProgressBar mLoadmore;
	private ProgressBar mProgressBar;
	private Button btn_search;
	private View book_search;
	private View loadmore;
	private int nextPageNum;
	
	private ArrayList bookno=new ArrayList();
	private ArrayList stitle=new ArrayList();
	private ArrayList booknoSingle=new ArrayList();
	private ArrayList save=new ArrayList();
	
	private ArrayList<BookListItem> list = new ArrayList<BookListItem>();
	private BookListAdapter adapter;
	private String globalLink;//全局链接
	private int ten = 1;
	private int indexNum = 0;
	
	private boolean isMenuOnClick = false;
	
	private OnOpenListener mOnOpenListener;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			System.out.println("22222222");
			
			if(msg.what==1){
				
				Toast.makeText(mActivity, "加载成功", Toast.LENGTH_LONG).show();
				if(save.size()%10==0 && save.size()!=0){
					
					loadmore.setVisibility(View.VISIBLE);
					loadmore.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View view) {
							mProgressBar.setVisibility(View.VISIBLE);
							mLoadmore.setVisibility(View.VISIBLE);
							listView.removeFooterView(loadmore);
							getContent(globalLink);
						}
					});
					listView.addFooterView(loadmore);
					
				}else{
					listView.removeFooterView(loadmore);
				}
				
				for(int i=0;i<save.size();i++){
					BookListItem item=list.get(i);
					item.setSave((String)save.get(i));
				}
				
				System.out.println("save.size()-----:"+save.size());
				
				mProgressBar.setVisibility(View.GONE);
				mLoadmore.setVisibility(View.GONE);
				adapter = new BookListAdapter(mContext, list);
				listView.setAdapter(adapter);
				listView.setSelection(ten-11);
				adapter.notifyDataSetChanged();
				
				
				if(!isMenuOnClick){
					Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in) ;
					serivce_under_bar.setAnimation(animation);
					serivce_under_bar.setVisibility(View.VISIBLE);
					isMenuOnClick = true;
				}else{
					Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out) ;
					serivce_under_bar.setAnimation(animation);
					serivce_under_bar.setVisibility(View.GONE);
					isMenuOnClick = false;
				}
			}
			
			listView.setOnItemClickListener(new OnItemClickListener(){
				
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					BookListItem item = (BookListItem) adapter.getItem(position);
					String bookname = item.getBookName();
					System.out.println(bookname);
					String bookIndex = item.getIndex();
					System.out.println(bookIndex);
					Intent intent = new Intent(mActivity , BookDetailActivity.class);
					intent.putExtra("index", bookIndex);
					intent.putExtra("bookname",bookname);
					mActivity.startActivity(intent);
				}
			});
			
			listView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					hideKeyboard();
					return false;
				}
			});
			
		};
	};
	
	
	
	
	public SchoolService(Context context , Activity activity){
		mContext = context ;
		mActivity = activity;
		
		mSchoolSerive = LayoutInflater.from(context).inflate(R.layout.school_server, null);
		loadmore = LayoutInflater.from(mContext).inflate(R.layout.loadmore, null);
		
		init();
		
		setListner();
	}

	
	public void init(){
		manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		serivce_under_bar = mSchoolSerive.findViewById(R.id.serivce_under_bar);
		
		btn_keep = (Button) mSchoolSerive.findViewById(R.id.btn_keep);
		btn_alarms = (Button) mSchoolSerive.findViewById(R.id.btn_alarms);
		
		mFlip = (ImageView) mSchoolSerive.findViewById(R.id.btn_goto_residenmenu);
		mSerivceMenu = (Button) mSchoolSerive.findViewById(R.id.serivce_popmenu);
		mClear = (ImageButton) mSchoolSerive.findViewById(R.id.book_searchclear);
		
		btn_search = (Button) mSchoolSerive.findViewById(R.id.btn_search_result);
		searchBook = (TextView) mSchoolSerive.findViewById(R.id.book_search);
		teacherCommu = (TextView) mSchoolSerive.findViewById(R.id.teacher_commu);
		opinion = (TextView) mSchoolSerive.findViewById(R.id.opinion);
	
		editText = (EditText) mSchoolSerive.findViewById(R.id.search_book_edit);
		mLoadmore = (ProgressBar) loadmore.findViewById(R.id.loadmore);
		mProgressBar =  (ProgressBar)mSchoolSerive.findViewById(R.id.loading);
		listView = (ListView) mSchoolSerive.findViewById(R.id.book_list);
		
		mScrollLayout = (ScrollLayout) mSchoolSerive.findViewById(R.id.srocll_layout);
		LinearLayout linearLayout = (LinearLayout) mSchoolSerive.findViewById(R.id.lllayout);
		
		mViewCount = mScrollLayout.getChildCount();
		
		mTextItems = new LinearLayout[mViewCount];
		
		for(int i=0; i<mViewCount; i++){
			mTextItems[i] = (LinearLayout) linearLayout.getChildAt(i);
			mTextItems[i].setEnabled(true);
			mTextItems[i].setTag(i);
			mTextItems[i].setOnClickListener(this);
		}
		
		mCurSel = 0;
		mTextItems[mCurSel].setEnabled(false);
	}
	
	private void setListner() {
		
		btn_keep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity , BookeepActivity.class);
				mActivity.startActivity(intent);
			}
		});
		
		
		btn_alarms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity , BookRemindActivity.class);
				mActivity.startActivity(intent);
			}
		});
		
		mFlip.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mOnOpenListener != null) {
					mOnOpenListener.open();
				}
			}
		});
		
		mSerivceMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!isMenuOnClick){
					Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in) ;
					serivce_under_bar.setAnimation(animation);
					serivce_under_bar.setVisibility(View.VISIBLE);
					isMenuOnClick = true;
				}else{
					Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out) ;
					serivce_under_bar.setAnimation(animation);
					serivce_under_bar.setVisibility(View.GONE);
					isMenuOnClick = false;
				}
				
			}
		});
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(editText.getText().length()>0){
					mClear.setVisibility(View.VISIBLE);
				}else 
					mClear.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if(editText.getText().length()>0){
					mClear.setVisibility(View.VISIBLE);
				}else 
					mClear.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		mClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				editText.setText("");
				
			}
		});
	
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//获取书列表
				hideKeyboard();
				save.clear();
				list.clear();
				indexNum=0;
				nextPageNum = 1;
				String bookname = editText.getText().toString();
				System.out.println("bookname-------:"+bookname);
				if("".equals(bookname)){
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(mActivity, "输入书名不能为空", Toast.LENGTH_LONG).show();
							return ;
						}
					});
				}else{
					mProgressBar.setVisibility(View.VISIBLE);
					System.out.println("test---------");
					String input = bookname.trim().replace(" ", "+");
					String url = "http://opac.szpt.edu.cn:8991/F/?request="+input+"&func=find-b&adjacent=N&local_base=SZY01&filter_code_1=WLN&filter_code_4=WFM&find_code=WRD";
					
					getContent(url);
				}
			
			}
		});
	}



	//获取学校图书查询网站的html的内容
	public void getContent(final String url){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!isMenuOnClick){
							Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in) ;
							serivce_under_bar.setAnimation(animation);
							serivce_under_bar.setVisibility(View.VISIBLE);
							isMenuOnClick = true;
						}else{
							Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out) ;
							serivce_under_bar.setAnimation(animation);
							serivce_under_bar.setVisibility(View.GONE);
							isMenuOnClick = false;
						}
					}
				});
				
				
				
				String html = getHtmlByUrl(url);
				
				if(html!=null && !"".equals(html)){
					Document doc = Jsoup.parse(html.replace("&nbsp;",""));
					
					Elements mylink=doc.select("a");
					
					for(int kk=36;kk<mylink.size();kk++){
	//					System.out.println("myLink----:"+ mylink.text());
						if(mylink.get(kk).toString().contains("hint")){
						String allk="";
						String[] my=mylink.get(kk).toString().replace("/    ","/").split("     ");
						
							if(my.length>1){
								
							for(int c=1;c<my.length;c++)
							{
	//							System.out.println("my--------:"+my[c]);
								if(my[c-1].contains("LXDZW"))
								{
								allk+="留仙洞"+my[c].substring(0,4);
								}else if(my[c-1].contains("XLHZW")){
									allk+="西丽湖"+my[c].substring(0,4);
								}else{
									allk+="其他库"+my[c].substring(0,4);
								}
//								System.out.println("allk---: " + allk);
							}
							
							}
							save.add(allk);
						}
						
					}
					
					//下一页
					Element links = doc.select("a[href]").first();
					
					String nextLink=links.toString();
	//				System.out.println("nextLink---:"+nextLink);
					Log.v("nn", nextLink);
					int end=nextLink.indexOf("?");
					nextLink=nextLink.substring(9, end);
	//				System.out.println("nextLink----:"+nextLink);
					int skil=Integer.parseInt(nextLink.substring(nextLink.length()-5));
					skil+=39;//下一页的链接随机号码增加39
					nextLink=nextLink.substring(0,nextLink.length()-5);
					nextLink+=skil;
					nextLink+="?func=short-jump&jump=";
					ten+=10;
					nextLink+=ten;//跳页加	10条
					globalLink=nextLink;
//					System.out.println("globalLink---:"+globalLink);
					
					Log.v("nn", globalLink);
					//获取作者，索书号，出版社，日期
					int i=0;//循环变量
					Elements es = doc.getElementsByAttributeValue("class","content");//获取class为content的标签内容
					Elements title=doc.getElementsByAttributeValue("class","itemtitle");
					String one="";
					Log.v("ssss",String.valueOf(es.size()));
					System.out.println(es.size());
					try
					{
						for (Element ele:es) 
							{
							if(es.size()>i)
								
								{		
								String	getno=new String(es.get(i).text().getBytes("ISO-8859-1"),"utf-8");
	//							System.out.println("getno----:"+getno);
								if(i%6==1&&i!=61&&i!=122&&i!=183&&i!=244)
								{
									booknoSingle.add(getno.trim()+" ");//加空格防止空指针
									//Log.v("sssss", getno);
								}
								one+=getno.trim()+"\n";
								
								i++;
									if(i%6==0)
									{
										System.out.println("one--:"+one);
	//									Map<String , Object> info = new HashMap<String, Object>();
										BookListItem item = new BookListItem();
										String[] temp = one.split("\n");
										for (int j = 0; j < temp.length; j++) {
											switch(j){
											case 0:
												if(temp[j]!=null)
													item.setWriter(temp[j]);
												else 
													item.setWriter(" ");
												break;
											
											case 1:
												if(temp[j]!=null)
													item.setIndex(temp[j]);
												else
													item.setIndex(" ");
												break;
											case 2:
												if(temp[j]!=null)
													item.setPublish(temp[j]);
												else
													item.setPublish(" ");
												break;
											case 3:
												if(temp[j]!=null)
													item.setYear(temp[j]);
												else
													item.setYear(" ");
												break;
											}
										}
										
										list.add(item);
										
	//									bookno.add(one.trim()+"\n");	
										one="";
									}
									
								}
							}
						
						//获取书名
						int j=0;
						for(Element ele:title)
						{
	//						BookListItem temp;
							String gettitle=new String(title.get(j).text().getBytes("ISO-8859-1"),"utf-8");
							System.out.println("gettitle-----:"+gettitle);
							BookListItem item=list.get(indexNum);
							item.setBookName(gettitle);
	//						list.add(item);
	//						stitle.add(gettitle);
							indexNum++;
							j++;
						}
					} catch (UnsupportedEncodingException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					System.out.println("here");
					handler.sendEmptyMessage(1);
					
				}
			}
		}).start();
	}
	
	
	public void setBookInfo(){
		BookListItem item = new BookListItem();
		List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		for(int a=0;a<bookno.size();a++){
			Map<String,Object> listItem=new HashMap<String,Object>();
			listItem.put("title",stitle.get(a));
			System.out.println("title:"+stitle.get(a));
			listItem.put("bookno",(bookno.get(a).toString()+save.get(a).toString()));
			System.out.println("bookno----:"+(bookno.get(a).toString()+save.get(a).toString()));
			listItems.add(listItem);
		}
	}
	
	
	/**
	 * 根据URL获得所有的html信息
	 * @param url
	 * @return
	 */
	public static String getHtmlByUrl(String url){
		String html = null;
		HttpClient httpClient = new DefaultHttpClient();//创建httpClient对象
		HttpGet httpget = new HttpGet(url);//以get方式请求该URL
		try {
			HttpResponse responce = httpClient.execute(httpget);//得到responce对象
			int resStatu = responce.getStatusLine().getStatusCode();//返回码
			if (resStatu==HttpStatus.SC_OK) {//200正常  其他就不对
				//获得相应实体
				HttpEntity entity = responce.getEntity();
				if (entity!=null) {
					html = EntityUtils.toString(entity);//获得html源代码
				}
			}
		} catch (Exception e) {
			System.out.println("访问【"+url+"】出现异常!");
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return html;
	}

	

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (mActivity.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	public View getView(){
		return mSchoolSerive;
	}
	
	public void setOnOpenListener(OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

	public void setCurTextItem(int index){
		if (index < 0 || index > mViewCount - 1 || mCurSel == index){
    		return ;
    	}  
		
		mTextItems[mCurSel].setEnabled(true);
		mTextItems[index].setEnabled(false);
		mCurSel = index;
		
		if(index == 0){
			searchBook.setTextColor(0xff2ea3fe);
    		teacherCommu.setTextColor(0xff999999);
    		opinion.setTextColor(0xff999999);
		}else if(index == 1){
			searchBook.setTextColor(0xff999999);
    		teacherCommu.setTextColor(0xff2ea3fe);
    		opinion.setTextColor(0xff999999);
		}else if(index == 2){
			searchBook.setTextColor(0xff999999);
    		teacherCommu.setTextColor(0xff999999);
    		opinion.setTextColor(0xff2ea3fe);
		}
	}
	

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer) v.getTag();
		setCurTextItem(pos);
		mScrollLayout.snapToScreen(pos);
	}
}

