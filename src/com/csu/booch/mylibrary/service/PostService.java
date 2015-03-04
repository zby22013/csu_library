package com.csu.booch.mylibrary.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.persistence.connection.ConnectionURL;
import com.csu.booch.mylibrary.persistence.connection.Forms;

/**
 * @name PostService.java
 * @author Tomorrow
 * @since  2014-5-3
 */
public class PostService {
	private Forms forms;
	private DataService dataService;
	public PostService() {
		forms = new Forms();
		dataService = new DataService();
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param userName
	 * @param psw
	 * @return
	 * @throws IOException
	 */
	public String logInPost(String userName, String psw) throws IOException{
		String result = "";
		//获取要提交的表单
		UrlEncodedFormEntity formEntity =forms.getLoginFormEntity(
										ConnectionURL.getLoginURL(),
										userName, psw, "RbntRecno");
		if(Connection.doPost(ConnectionURL.getLoginURL(), formEntity,false)){
			result =dataService.getUserName(Connection.getResult());
		}
		Connection.disConnection();
		return result;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param op
	 * @param np
	 * @param renp
	 * @return
	 * @throws Exception
	 */
	public boolean changePwdPost(String op ,String np , String renp) throws Exception{
		boolean result=false;
		UrlEncodedFormEntity entity = forms.getChangePasswordForm(
														ConnectionURL.getChangepasswordURL(),
														op,
														np,
														renp);
		if(Connection.doPost(ConnectionURL.getChangepasswordURL(),entity, true)){
			String str = Connection.getResult();
			if(str.contains("<script>alert('修改成功！')</script>")){
				result = true;
			}
		}
		Connection.disConnection();
		return result;
		
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean putToShelfPost(String id) throws Exception{
		boolean result = false;
		UrlEncodedFormEntity entity = forms.getShelfPostForm(id);
		if(Connection.doPost(ConnectionURL.getDetailURL(id),entity, true)){
			result = true;
		}
		Connection.disConnection();
		return result;
	}
	
	/**
	 * 
	 *@author 聂佳俊
	 *@since 2014-5-11
	 * @param startDate
	 * @param endDate
	 * @return result
	 */
	public String getHistoryPost(String startDate, String endDate) throws Exception{
		String result = "";
		UrlEncodedFormEntity entity = forms.getHistoryPostForm(startDate, endDate);
		if(Connection.doPost(ConnectionURL.getHistory(),entity, true)){
			//result = true;
			result = Connection.getResult();
		}
		Connection.disConnection();
		return result;
	}
	
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-11
	 * @param userId
	 * @param ibsn
	 * @return
	 * @throws IOException 
	 */
	public boolean deleteRemindBookPost(String userId, String isbn) throws IOException {
		boolean result = false;
		String str="";
		Map<String, String> data = new HashMap<String, String>() ;
		data.put("userId", userId);
		data.put("bookISBN", isbn);
		Document doc = Jsoup.connect(ConnectionURL.getDeleteremind()).data(data).post();
		str = doc.body().text();
		if(str.contains("ok"))
		{
			result = true;
		}
		else{
			result = false;
		}
		return result;
	}
	
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param userId
	 * @param ibsn
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws Exception
	 */
	public boolean putToBookRemindPost(String userId,String isbn) throws ClientProtocolException, IOException {
		boolean result = false;
		String str="";
		Map<String, String> data = new HashMap<String, String>() ;
		data.put("userId", userId);
		data.put("bookISBN", isbn);
		Document doc = Jsoup.connect(ConnectionURL.getUploadremind()).data(data).post();
		str = doc.body().text();
		if(str.contains("ok"))
		{
			result = true;
		}
		else{
			result = false;
		}
		return result;
	}
	
	public String getRemindListPost(String userId) throws ClientProtocolException, IOException {
		String html="";
		Map<String, String> data = new HashMap<String, String>();
		data.put("userId", userId);
		Document doc = Jsoup.connect(ConnectionURL.getSearchemind())
							.data(data)
							.post();
		html = doc.html();
		return html;
	}
	/**
	 *@author Tomorrow
	 *@since 2014-5-12
	 * @param company
	 * @param address
	 * @param email
	 * @param phone
	 * @param password
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws IllegalStateException 
	 */
	public boolean changeInfoPost(String company, String address, String email,
			String phone, String password) throws IllegalStateException, ClientProtocolException, IOException {
		boolean result = false;
		String str="";
		UrlEncodedFormEntity entity = forms.getChangeInfoForm(company,address,email,phone,password);
		if(Connection.doPost(ConnectionURL.getInfoURL(), entity, true)){
			str= Connection.getResult();
			if(str.contains("修改成功！")){
				result = true;
			}
		}
		return result;
	}
}
