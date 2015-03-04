package com.csu.booch.mylibrary.persistence.connection;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import com.csu.booch.mylibrary.data.domain.MyAccount;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * 建立连接，发送请求
 * 
 * @author Tomorrow
 * 
 */

public class Connection {
	
	private static String result="";//返回结果html
	private static HttpGet httpGet;//请求类型
	private static HttpPost httpPost;//请求类型
	private static DefaultHttpClient httpClient;//连接变量
	private static HttpResponse httpResponse;//请求结果
	private static CookieStore cookies;//保存cookie
	/**
	 * 执行get请求
	 * @param url 请求链接  
	 * @param in 是否是登录后请求
	 * @return 请求是否成功
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author Tomorrow
	 */
	public static boolean doGet(String url,boolean in) 
			throws IllegalStateException,ClientProtocolException, IOException{
		boolean isSuccessful = false;
		prepareHttpClient();
		
		httpGet = new HttpGet(url);
		setHeaders(httpGet,in);
		httpResponse = httpClient.execute(httpGet);//执行请求
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){ 
			isSuccessful =  true;
//			cookies = ((DefaultHttpClient)httpClient).getCookieStore();
		}
		return isSuccessful;
	}
	/**
	 * 执行post请求
	 * @param url 请求链接
	 * @param formEntity post内容
	 * @param in 是否是登录后的请求
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author Tomorrow
	 */
	public static boolean doPost(String url,UrlEncodedFormEntity formEntity ,boolean in) 
			throws IllegalStateException,ClientProtocolException, IOException{
		boolean isSuccessful = false;
		prepareHttpClient();
		httpPost = new HttpPost(url);
		httpPost.setEntity(formEntity);
		if(in){
			//只需在httpClient设置cookie
			((DefaultHttpClient)httpClient).setCookieStore(MyAccount.getCookies());
		}
		
		httpResponse = httpClient.execute(httpPost);//执行请求

		//检查返回码
		//200 ok
		//302 重定向
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK||
				httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
				||httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY){ 
//			result = EntityUtils.toString(httpResponse.getEntity());
			isSuccessful =  true;
			if(!in){
				cookies = ((DefaultHttpClient)httpClient).getCookieStore();
			}
		}
		return isSuccessful;
	}
	/**
	 * 获取返回结果
	 * @return result 
	 * @author Tomorrow
	 */
	public static String getResult(){
		try {
			result = EntityUtils.toString(httpResponse.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 断开连接
	 * @throws IOException
	 * @author Tomorrow
	 */
	public static void disConnection() throws IOException{
		httpClient.getConnectionManager().shutdown();
	}

	/**
	 * 检查是否有网络链接
	 * @param context 要检查的环境
	 * @return 返回是否有效
	 * @author Tomorrow
	 * @since 20140329 11:19 
	 */
	public static boolean isNetworkConnected(Context context) { 
		if (context != null) { 
			ConnectivityManager connectivityManager = (ConnectivityManager) context 
					.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo(); 
			if (networkInfo != null&&networkInfo.isConnected()) {
				if(networkInfo.getState()==NetworkInfo.State.CONNECTED){
					return networkInfo.isAvailable(); 
				}
			} 
		} 
		return false; 
	}
	/**
	 * 获取cookie
	 * @return the cookies
	 * @author Tomorrow
	 */
	public static CookieStore getCookies() {
		return cookies;
	}
	/**获取返回结果
	 * @return the httpResponse
	 * @author Tomorrow
	 */
	public static HttpResponse getHttpResponse() {
		return httpResponse;
	}
	/**
	 * 设置请求的头部信息
	 * 让服务端以为他是和浏览器打交道
	 * 避免一些请求的拒绝
	 * @param httpGet
	 * @param in 
	 * @author Tomorrow
	 */
	private static void setHeaders(HttpGet httpGet, boolean in){
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;"); 
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8"); 
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36"); 
		httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		httpGet.setHeader("Keep-Alive", "300"); 
		httpGet.setHeader("Connection", "keep-alive"); 
		httpGet.setHeader("Cache-Control", "max-age=0"); 
		httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		
		if(in){
			//只需在httpClient设置cookie
			((DefaultHttpClient)httpClient).setCookieStore(MyAccount.getCookies());			
		}
		
	}
	/**
	 * 完成链接之前的一些设置
	 * @author Tomorrow
	 */
	private static void prepareHttpClient(){
//		if(httpClient==null){
			// Set the timeout in milliseconds until a connection is established.  
			HttpParams params = new BasicHttpParams();
			
			//链接超时
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			//超时
			HttpConnectionParams.setSoTimeout(params, 5000);
			//设置自动重定向
			HttpClientParams.setRedirecting(params, true);
			
			httpClient = new DefaultHttpClient(params);
//		}
		
	}

}
