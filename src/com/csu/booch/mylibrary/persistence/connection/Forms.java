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
 * ��ȡ�ύ�ı�
 * @author Tomorrow
 *
 */
public class Forms {

	private Document doc = null;//���ڽ�����doc

	/**
	 * ��ȡ��½������д
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @param url ����
	 * @param userName �û���
	 * @param passWord ����
	 * @param Logintype ��¼��ʽ
	 * @return entity ��
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
					//�Ѿ���ֵ����Ŀ
					if(!i.attr("value").equals("")){
						//ѡ���¼��ʽ
						if(i.attr("name").equals("Logintype")){
							//ѡ��Ĭ�Ϸ�ʽ����ΪĬ�Ϸ�ʽֻ��һ�������Կ��Եõ�һ��Ψһ�ģ����û�����Ҫ�ĵ�¼��ʽ��ֵ
							if(i.attr("checked").equals("checked")){
								formParams.add(new BasicNameValuePair(i.attr("name"), Logintype));
							}	
						}
						//��������Ŀ,ֱ�Ӹ�ֵ
						else{
							formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
						}

					}
					//û�еĸ�ֵ��input
					else{
						//�����û���
						if(i.attr("name").equals("txtName")){
							formParams.add(new BasicNameValuePair(i.attr("name"), userName));
						}
						//��������
						if(i.attr("name").equals("txtPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), passWord));
						}
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//�ͷ���Դ
		formParams = null;
		doc = null;
		return entity;
	}


	/**
	 * ��ȡ��д�޸�����ı�
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
					//�Ѿ���ֵ����Ŀ
					if(!i.attr("value").equals("")){
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
					//û�еĸ�ֵ��input
					else{
						//�����������
						if(i.attr("name").equals("txtOldPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), oldPWS));
						}
						//����������
						else if(i.attr("name").equals("txtNewPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), newPWS));
						}
						//����ȷ��������
						else if(i.attr("name").equals("txtNewPassWordOK")){
							formParams.add(new BasicNameValuePair(i.attr("name"), reNewPWD));
						}
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//�ͷ���Դ
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
					//ȥ��"�鿴����"���input����������
					if(!i.attr("value").equals("�鿴����")&&!i.attr("value").equals("")){
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//�ͷ���Դ
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
					//ȥ��"�鿴����"���input����������
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
		//�ͷ���Դ
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
					//�Ѿ���ֵ����Ŀ
					if(!i.attr("value").equals("")&&!i.attr("value").equals("�� ��")){
						formParams.add(new BasicNameValuePair(i.attr("name"), i.attr("value")));
					}
					//û�еĸ�ֵ��input
					else{
						//���뵥λ
						if(i.attr("name").equals("txtWorkAdress")){
							formParams.add(new BasicNameValuePair(i.attr("name"), company));
						}
						//����סַ
						else if(i.attr("name").equals("txtAddress")){
							formParams.add(new BasicNameValuePair(i.attr("name"), address));
						}
						//��������
						else if(i.attr("name").equals("txtEmail")){
							formParams.add(new BasicNameValuePair(i.attr("name"), email));
						}
						//����绰
						else if(i.attr("name").equals("txtTeleNum")){
							formParams.add(new BasicNameValuePair(i.attr("name"), phone));
						}
						//��������
						else if(i.attr("name").equals("txtPassWord")){
							formParams.add(new BasicNameValuePair(i.attr("name"), password));
						}
					}
				}
			}
		}
		entity = new UrlEncodedFormEntity(formParams, "UTF-8");
		//�ͷ���Դ
		formParams = null;
		doc = null;
		return entity;
	}

}
