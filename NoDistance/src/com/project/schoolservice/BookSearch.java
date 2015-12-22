package com.project.schoolservice;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.R;

public class BookSearch {
	
	private Activity mActivity;
	private Context mContext;
	
	
	private View book_search;
	private View loadmore;
	
	private EditText editText;
	private ImageButton mClear;
	private Button btn_search;
	private ListView listView;
	
	private BookListAdapter adapter;
	
	private InputMethodManager manager;
	private ProgressBar mLoadmore;
	private ProgressBar mProgressBar;
	
	private int nextPageNum;
	
	private ArrayList bookno=new ArrayList();
	private ArrayList stitle=new ArrayList();
	private ArrayList booknoSingle=new ArrayList();
	private ArrayList save=new ArrayList();
	
	private ArrayList<BookListItem> list = new ArrayList<BookListItem>();
	
	private String globalLink;//全局链接
	private int ten = 1;
	private int indexNum = 0;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			System.out.println("22222222");
			
			if(msg.what==1){
				Toast.makeText(mActivity, "加载成功", Toast.LENGTH_LONG).show();
				
				if(save.size()%10==0 ){
					
					loadmore.setVisibility(View.VISIBLE);
					loadmore.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View view) {
							mProgressBar.setVisibility(View.VISIBLE);
							mLoadmore.setVisibility(View.VISIBLE);
							getContent(globalLink);
//							mLoadmore.setVisibility(View.GONE);
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
				listView = (ListView) book_search.findViewById(R.id.book_list);
				listView.setAdapter(adapter);
				listView.setSelection(ten-11);
				adapter.notifyDataSetChanged();
			}
			
//			if(msg.what==0){
//				getContent(globalLink);
//				adapter.notifyDataSetChanged();
//				loadmore.setVisibility(View.GONE);
//			}
		};
	};
	
	
	public BookSearch(Activity mActivity , Context mContext){
		this.mActivity = mActivity;
		this.mContext = mContext;
		
//		mSchoolService = new SchoolService(mContext, mActivity);
		
		book_search = LayoutInflater.from(mContext).inflate(R.layout.book_search, null);
		loadmore = LayoutInflater.from(mContext).inflate(R.layout.loadmore, null);
//		adapter = new 
		setUpView();
		
		setListner();
	}
	
	
	private void setUpView() {
		manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		
//		mProgressBar = (ProgressBar) book_search.findViewById(R.id.loading);
		
		editText = (EditText) book_search.findViewById(R.id.search_book_edit);
		mClear = (ImageButton) book_search.findViewById(R.id.book_searchclear);
		btn_search = (Button) book_search.findViewById(R.id.btn_search_result);
		listView = (ListView) book_search.findViewById(R.id.book_list);
		
		mLoadmore = (ProgressBar) loadmore.findViewById(R.id.loadmore);
		mProgressBar = (ProgressBar) book_search.findViewById(R.id.loading);
//		adapter = new BookListAdapter(mContext, list);
//		listView.setAdapter(adapter);
	}



	private void setListner() {
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
				mProgressBar.setVisibility(View.VISIBLE);
				nextPageNum = 1;
				String bookname = editText.getText().toString();
				System.out.println("bookname-------:"+bookname);
				if("".equals(bookname)){
					System.out.println("kong");
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(mActivity, "输入书名不能为空", Toast.LENGTH_LONG).show();
							return ;
						}
					});
				}else{
				
					String input = bookname.trim().replace(" ", "+");
					String url = "http://opac.szpt.edu.cn:8991/F/?request="+input+"&func=find-b&adjacent=N&local_base=SZY01&filter_code_1=WLN&filter_code_4=WFM&find_code=WRD";
					
					getContent(url);
				}
			
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
			}
		});
//		listView.setOnScrollListener(this);
	}

	
	//获取学校图书查询网站的html的内容
	public void getContent(final String url){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String html = getHtmlByUrl(url);
				
				if(html!=null && !"".equals(html)){
					Document doc = Jsoup.parse(html.replace("&nbsp;",""));
					
					Elements mylink=doc.select("a");
					
					for(int kk=36;kk<mylink.size();kk++){
//						System.out.println("myLink----:"+ mylink.text());
						if(mylink.get(kk).toString().contains("hint")){
						String allk="";
						String[] my=mylink.get(kk).toString().replace("/    ","/").split("     ");
						
							if(my.length>1){
								
							for(int c=1;c<my.length;c++)
							{
//								System.out.println("my--------:"+my[c]);
								if(my[c-1].contains("LXDZW"))
								{
								allk+="留仙洞"+my[c].substring(0,4);
								}else if(my[c-1].contains("DLTK")){
									allk+="西丽湖"+my[c].substring(0,4);
								}else{
									allk+="其他库"+my[c].substring(0,4);
								}
								System.out.println("allk---: " + allk);
							}
							
							}
							save.add(allk);
						}
						
					}
					
					//下一页
					Element links = doc.select("a[href]").first();
					
					String nextLink=links.toString();
//					System.out.println("nextLink---:"+nextLink);
					Log.v("nn", nextLink);
					int end=nextLink.indexOf("?");
					nextLink=nextLink.substring(9, end);
//					System.out.println("nextLink----:"+nextLink);
					int skil=Integer.parseInt(nextLink.substring(nextLink.length()-5));
					skil+=39;//下一页的链接随机号码增加39
					nextLink=nextLink.substring(0,nextLink.length()-5);
					nextLink+=skil;
					nextLink+="?func=short-jump&jump=";
					ten+=10;
					nextLink+=ten;//跳页加	10条
					globalLink=nextLink;
					System.out.println("globalLink---:"+globalLink);
					
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
//								System.out.println("getno----:"+getno);
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
//										Map<String , Object> info = new HashMap<String, Object>();
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
										
//										bookno.add(one.trim()+"\n");	
										one="";
									}
									
								}
							}
						
						//获取书名
						int j=0;
						for(Element ele:title)
						{
//							BookListItem temp;
							String gettitle=new String(title.get(j).text().getBytes("ISO-8859-1"),"utf-8");
							System.out.println("gettitle-----:"+gettitle);
							BookListItem item=list.get(indexNum);
							item.setBookName(gettitle);
//							list.add(item);
//							stitle.add(gettitle);
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
		return book_search;
	}

//	private int lastItem;
//
//	public void onScrollStateChanged(AbsListView view, int scrollState)  {
//		if(save.size()%10==0 && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
//			//下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
//            if(lastItem == save.size()  && scrollState == this.SCROLL_STATE_IDLE){ 
////                    Log.i(TAG, "拉到最底部");
//                    loadmore.setVisibility(view.VISIBLE);
//             
//                handler.sendEmptyMessage(0);
//            }
//		}
//	}
//
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//            int visibleItemCount, int totalItemCount) {
//		// TODO Auto-generated method stub
//		lastItem = firstVisibleItem + visibleItemCount - 1;
//	}
}
