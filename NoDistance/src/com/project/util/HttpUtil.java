package com.project.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;


/**
 * http������
 * @author Administrator
 *
 */
public class HttpUtil {
	// ����URL  
    // ���Get�������request 
	public static HttpGet getHttpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		return httpGet;
	}
	
	// ���Post�������request  
	public static HttpPost getHttpPost(String url){
		HttpPost httpPost = new HttpPost(url);
		return httpPost;
	}
	
	// ������������Ӧ����response  
    public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{  
        HttpResponse response = new DefaultHttpClient().execute(request);  
        return response;  
    }  
   
    // ������������Ӧ����response  
    public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{  
        HttpResponse response = new DefaultHttpClient().execute(request);  
        return response;  
    }
    
    // ����Post����(��������)�������Ӧ��ѯ���  
    public static String queryStringForPost(String url , List<BasicNameValuePair> params){  
       
        String result = null;  
        
        try {  
        	
        	// ����url���HttpPost����  
            HttpPost request = HttpUtil.getHttpPost(url);  
            if(params != null)
            	request.setEntity((new UrlEncodedFormEntity(params, "utf-8")));
            
            // �����Ӧ����  
            HttpResponse response = HttpUtil.getHttpResponse(request);  
            
            // �ж��Ƿ�����ɹ�  
            if(response.getStatusLine().getStatusCode()==200){  
                // �����Ӧ  
                result = EntityUtils.toString(response.getEntity());  
                result=new  String(result.getBytes("8859_1"),"utf-8");  //����Ҫ�ɲ�Ҫ�����㲻��������Ϊ׼  
                Log.i("main", "resCode = " + response.getStatusLine()); //��ȡ��Ӧ��  
//			    Log.i("main", "result = " + EntityUtils.toString(response.getEntity(), "utf-8")); //��ȡ��Ӧ����
                return result;  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
            result = "�����쳣��";  
            return result;  
        } catch (IOException e) {  
            e.printStackTrace();  
            result = "�����쳣��";  
            return result;  
        }  
        return null;  
    }  
    
    
	
}
