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
 * �������ӣ���������
 * 
 * @author Tomorrow
 * 
 */

public class Connection {
	
	private static String result="";//���ؽ��html
	private static HttpGet httpGet;//��������
	private static HttpPost httpPost;//��������
	private static DefaultHttpClient httpClient;//���ӱ���
	private static HttpResponse httpResponse;//������
	private static CookieStore cookies;//����cookie
	/**
	 * ִ��get����
	 * @param url ��������  
	 * @param in �Ƿ��ǵ�¼������
	 * @return �����Ƿ�ɹ�
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
		httpResponse = httpClient.execute(httpGet);//ִ������
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){ 
			isSuccessful =  true;
//			cookies = ((DefaultHttpClient)httpClient).getCookieStore();
		}
		return isSuccessful;
	}
	/**
	 * ִ��post����
	 * @param url ��������
	 * @param formEntity post����
	 * @param in �Ƿ��ǵ�¼�������
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
			//ֻ����httpClient����cookie
			((DefaultHttpClient)httpClient).setCookieStore(MyAccount.getCookies());
		}
		
		httpResponse = httpClient.execute(httpPost);//ִ������

		//��鷵����
		//200 ok
		//302 �ض���
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
	 * ��ȡ���ؽ��
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
	 * �Ͽ�����
	 * @throws IOException
	 * @author Tomorrow
	 */
	public static void disConnection() throws IOException{
		httpClient.getConnectionManager().shutdown();
	}

	/**
	 * ����Ƿ�����������
	 * @param context Ҫ���Ļ���
	 * @return �����Ƿ���Ч
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
	 * ��ȡcookie
	 * @return the cookies
	 * @author Tomorrow
	 */
	public static CookieStore getCookies() {
		return cookies;
	}
	/**��ȡ���ؽ��
	 * @return the httpResponse
	 * @author Tomorrow
	 */
	public static HttpResponse getHttpResponse() {
		return httpResponse;
	}
	/**
	 * ���������ͷ����Ϣ
	 * �÷������Ϊ���Ǻ�������򽻵�
	 * ����һЩ����ľܾ�
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
			//ֻ����httpClient����cookie
			((DefaultHttpClient)httpClient).setCookieStore(MyAccount.getCookies());			
		}
		
	}
	/**
	 * �������֮ǰ��һЩ����
	 * @author Tomorrow
	 */
	private static void prepareHttpClient(){
//		if(httpClient==null){
			// Set the timeout in milliseconds until a connection is established.  
			HttpParams params = new BasicHttpParams();
			
			//���ӳ�ʱ
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			//��ʱ
			HttpConnectionParams.setSoTimeout(params, 5000);
			//�����Զ��ض���
			HttpClientParams.setRedirecting(params, true);
			
			httpClient = new DefaultHttpClient(params);
//		}
		
	}

}
