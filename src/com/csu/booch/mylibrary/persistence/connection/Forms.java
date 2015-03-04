package com.csu.booch.mylibrary.persistence.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 获取提交的表单
 * @author Tomorrow
 *
 */
public class Forms {

	private Document doc = null;//用于解析的doc

	/**
	 * 获取登陆表单并填写
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @param url 链接
	 * @param userName 用户名
	 * @param passWord 密码
	 * @param Logintype 登录方式
	 * @return entity 表单
	 * @throws IOException
	 */
	public UrlEncodedFormEntity getLoginFormEntity(String url,String userName
			,String passWord, String Logintype) 
					throws IOException{
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity = null;
		doc = Jsoup.connect(url).get();
		Elements forms = doc.select("form");
		for(Element e :forms){
			if(e.attr("method").equals("post")){
				Elements inputs = e.getElementsByTag("input");
				for(Element i : inputs){
					//已经有值的项目
					if(!i.attr("value").equals("")){
						//选择登录方式
						if(i.attr("name").equals("Logintype")){
							//选定默认方式，因为默认方式只有一个，所以可以得到一个唯一的，将用户所需要的登录方式赋值
							if(i.attr("checked").equals("checked")){
								formParams.add(new BasicNameValuePair(i.attr("name"), Logintype));
							}	
						}
						//其他的项目,直接赋值
						else{
							formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
						}

					}
					//没有的赋值的input
					else{
						//加入用户名
						if(i.attr("name").equals("txtName")){
							formParams.add(new BasicNameValuePair(i.attr("name"), userName));
						}
						//加入密码
						if(i.attr("name").equals("txtPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), passWord));
						}
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//释放资源
		formParams = null;
		doc = null;
		return entity;
	}


	/**
	 * 获取填写修改密码的表单
	 * @param url
	 * @param oldPWS
	 * @param newPWS
	 * @param reNewPWD
	 * @return
	 * @throws Exception
	 */
	public UrlEncodedFormEntity getChangePasswordForm(String url,String oldPWS,
			String newPWS,
			String reNewPWD) throws Exception{

		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity = null;
		if(Connection.doGet(ConnectionURL.getChangepasswordURL(),true)){
			doc = Jsoup.parse(Connection.getResult());
		}
		Elements forms = doc.select("form");
		for(Element e :forms){
			if(e.attr("method").equals("post")){
				Elements inputs = e.getElementsByTag("input");
				for(Element i : inputs){
					//已经有值的项目
					if(!i.attr("value").equals("")){
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
					//没有的赋值的input
					else{
						//加如入旧密码
						if(i.attr("name").equals("txtOldPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), oldPWS));
						}
						//加入新密码
						else if(i.attr("name").equals("txtNewPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), newPWS));
						}
						//加入确认新密码
						else if(i.attr("name").equals("txtNewPassWordOK")){
							formParams.add(new BasicNameValuePair(i.attr("name"), reNewPWD));
						}
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//释放资源
		formParams = null;
		doc = null;
		return entity;
	}

	public UrlEncodedFormEntity getShelfPostForm(String id) throws Exception{

		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity = null;
		if(Connection.doGet(ConnectionURL.getDetailURL(id),true)){
			doc = Jsoup.parse(Connection.getResult());
		}
		Elements forms = doc.select("form");
		for(Element e :forms){
			if(e.attr("method").equals("post")){
				Elements inputs = e.getElementsByTag("input");
				for(Element i : inputs){
					//去掉"查看评论"这个input和其他链接
					if(!i.attr("value").equals("查看评论")&&!i.attr("value").equals("")){
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//释放资源
		formParams = null;
		doc = null;
		return entity;
	}
	
	
	public UrlEncodedFormEntity getHistoryPostForm(String startDate, String endDate) throws Exception{
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity = null;
		if(Connection.doGet(ConnectionURL.getHistory(),true)){
			doc = Jsoup.parse(Connection.getResult());
		}
		Elements forms = doc.select("form");
		for(Element e :forms){
			if(e.attr("method").equals("post")){
				Elements inputs = e.getElementsByTag("input");
				for(Element i : inputs){
					//去掉"查看评论"这个input和其他链接
					if(i.attr("name").equals("txtBegDate")) {
						formParams.add(new BasicNameValuePair(i.attr("name"), startDate));
					}
					else if(i.attr("name").equals("txtEndDate")) {
						formParams.add(new BasicNameValuePair(i.attr("name"), endDate));
					}
					else {
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//释放资源
		formParams = null;
		
		doc = null;
		return entity;
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
	public UrlEncodedFormEntity getChangeInfoForm(String company,
			String address, String email, String phone, String password) throws IllegalStateException, ClientProtocolException, IOException {
		
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity = null;
		if(Connection.doGet(ConnectionURL.getInfoURL(),true)){
			doc = Jsoup.parse(Connection.getResult());
		}
		Elements forms = doc.select("form");
		for(Element e :forms){
			if(e.attr("method").equals("post")){
				Elements inputs = e.getElementsByTag("input");
				for(Element i : inputs){
					//已经有值的项目
					if(!i.attr("value").equals("")&&!i.attr("value").equals("重 填")){
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
					//没有的赋值的input
					else{
						//加入单位
						if(i.attr("name").equals("txtWorkAdress")){
							formParams.add(new BasicNameValuePair(i.attr("name"), company));
						}
						//加入住址
						else if(i.attr("name").equals("txtAddress")){
							formParams.add(new BasicNameValuePair(i.attr("name"), address));
						}
						//加入邮箱
						else if(i.attr("name").equals("txtEmail")){
							formParams.add(new BasicNameValuePair(i.attr("name"), email));
						}
						//加入电话
						else if(i.attr("name").equals("txtTeleNum")){
							formParams.add(new BasicNameValuePair(i.attr("name"), phone));
						}
						//加入密码
						else if(i.attr("name").equals("txtPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), password));
						}
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//释放资源
		formParams = null;
		doc = null;
		return entity;
	}

}
