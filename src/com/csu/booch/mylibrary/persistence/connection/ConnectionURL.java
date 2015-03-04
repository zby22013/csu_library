package com.csu.booch.mylibrary.persistence.connection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * �������ӵ��о������ض����������
 * @author Tomorrow
 * 
 */
public class ConnectionURL {
	//�����ؼ����������ӵ�ǰ׺
	//��������������ؼ���
	private final static String SEARCHURLPREFIX = "http://opac.its.csu.edu.cn/NTRdrBookRetr.aspx?";
//	//�����ؼ����������ӵĺ�׺
//	private final static String SEARCHURLSUFFIX = "&strSortType=&strpageNum=10&strSort=desc";
	//��ȡ��̬��Ϣ�������ӵ�ǰ׺
	//�����������:�鼮id
	private final static String MOERURLPREFIX = "http://opac.its.csu.edu.cn/GetlocalInfoAjax.aspx?ListRecno=";
	//��ȡ��ϸ��Ϣ�����ǰ׺
	//�����������:�鼮id
	private final static String DETAILURLPREFIX = "http://opac.its.csu.edu.cn/NTRdrBookRetrInfo.aspx?BookRecno=";
	//��ȡ�鼮��������  
	//����������� ISBN
	private final static String BOOKCOVERPREFIX = "http://202.112.150.126/index.php?client=csu&isbn=";
	//��¼ҳ������
	private final static String LOGINPAGE = "http://opac.its.csu.edu.cn/NTRdrLogin.aspx";
	//�޸�����
	private final static String CHANGEPASSWORD = "http://opac.its.csu.edu.cn/NTchangePassWord.aspx";
	//����ͼ��
	private final static String RETRBORROW  = "http://opac.its.csu.edu.cn/NTBookLoanRetr.aspx";
	//��������
	private final static String INFO="http://opac.its.csu.edu.cn/NTChangesInfo.aspx";
	//����Ȩ�޲�ѯ
	private final static String PERMISSION="http://opac.its.csu.edu.cn/NTRdrRightRetr.aspx";
	// �������
	private final static String MYBOOKSHELF = "http://opac.its.csu.edu.cn/Mybookshelf.aspx";
	
	//ɾ������鼮
	private final static String DELETEBOOK= "http://opac.its.csu.edu.cn/Mybookshelf.aspx?strRecno=";
	//�鼮����������վ
	//����ѷ
	private final static String AMAZON = "http://www.amazon.cn/s/ref=nb_sb_noss_2?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99&url=search-alias%3Dstripbooks&field-keywords=";
	//�Ա�
	private final static String TAOBAOPREFIX = "http://list.taobao.com/itemlist/default.htm?cat=0&viewIndex=1&as=0&commend=all&atype=b&style=grid&pcat=food2011&q=";
	private final static String TAOBAO = "&same_info=1&isnew=2&tid=0&_input_charset=utf-8";
	//����
	private final static String JINGDONGREFIX = "http://search.jd.com/Search?keyword=";
	private final static String JINGDONG="&enc=utf-8&book=y";
	
	//��������ַ
	private final static String DATABASE="http://ontheair.sinaapp.com/";
	//��Ҫ������Ŀ�ϴ������ݿ�
	private final static String UPLOADREMIND="http://ontheair.sinaapp.com/add.php";
	//����Ҫ���ѵ���Ŀ
	private final static String SEARCHEMIND="http://ontheair.sinaapp.com/search.php";
	
	//ͼ������
	private final static String RENEWBORROW="http://opac.its.csu.edu.cn/NTBookloanResult.aspx?barno=";
	private final static String RENEWBORROWINFO="http://opac.its.csu.edu.cn/NTBookLoanRetr.aspx";
	//������ʷ
	private final static String HISTORY="http://opac.its.csu.edu.cn/NTBookLoanLogCheck.aspx";
	//�ƾ���ѯ
	private final static String ARREARAGE="http://opac.its.csu.edu.cn/NTRdrFinRetr.aspx";
	//��Ϣ����
	private final static String MESSGAE="http://opac.its.csu.edu.cn/NTInfoMeun.aspx";	
	
	private final static String DELETEREMIND="http://ontheair.sinaapp.com/delete.php";
	/**
	 * ��ȡ��������������
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @param keyWord �ؼ���
	 * @param page �����ҳ��
	 * @return URL
	 */
	public static String getSearchURL(String type,String keyWord , int page){
		String url = "null";
		try {
			keyWord = URLEncoder.encode(keyWord,"utf-8");
			type = URLEncoder.encode(type,"utf-8");
			url = SEARCHURLPREFIX +"strType="+type+"&strKeyValue="+ keyWord +"&page=" + page ;//+ SEARCHURLSUFFIX;
		} catch (UnsupportedEncodingException e) {
			
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * ��ȡ�������ݵ���������
	 * @param id
	 * @return URL
	 */
	public static String getMoreURL(String id){
		String url = "null";
		try {
			id = URLEncoder.encode(id,"utf-8");
			url = MOERURLPREFIX + id;
			
		} catch (UnsupportedEncodingException e) {
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * ��ȡ��ϸ��Ϣ����������
	 * @param id
	 * @return URL
	 */
	public static String getDetailURL(String id){
		String url = "null";
		try {
			id = URLEncoder.encode(id,"utf-8");
			url = DETAILURLPREFIX + id;
			
		} catch (UnsupportedEncodingException e) {
			
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * ��ȡ�鼮�������������
	 * @param id
	 * @return URL
	 */
	public static String getBookCoverURL(String isbn){
		String url = "null";
		try {
			isbn = URLEncoder.encode(isbn,"utf-8");
			url = BOOKCOVERPREFIX + isbn+"/cover";
			
		} catch (UnsupportedEncodingException e) {
			
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * ��ȡ��¼����
	 * @return LOGINPAGEURL 
	 */
	public static String getLoginURL(){
		return LOGINPAGE;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @return CHANGEPASSWORDURL
	 */
	public static String getChangepasswordURL() {
		return CHANGEPASSWORD;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @return RETRBORROWURL
	 */
	public static String getRetrborrow() {
		return RETRBORROW;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @return INFOURL
	 */
	public static String getInfoURL() {
		return INFO;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @return PERMISSIONURL
	 */
	public static String getPermissionURL() {
		return PERMISSION;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @return MYBOOKSHELF
	 */
	public static String getMybookshelfURL() {
		return MYBOOKSHELF;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param bookName
	 * @return
	 */
	public static String getAmazon(String bookName) {
		String url = "null";
		try {
			bookName = URLEncoder.encode(bookName,"utf-8");
			url = AMAZON + bookName;
			
		} catch (UnsupportedEncodingException e) {
			
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param bookName
	 * @return
	 */
	public static String getTaobao(String bookName) {
		String url = "null";
		try {
			bookName = URLEncoder.encode(bookName,"utf-8");
			url = TAOBAOPREFIX + bookName+TAOBAO;
			
		} catch (UnsupportedEncodingException e) {
			
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param bookName
	 * @return
	 */
	public static String getJindong(String bookName) {
		String url = "null";
		try {
			bookName = URLEncoder.encode(bookName,"utf-8");
			url = JINGDONGREFIX + bookName+JINGDONG;
			
		} catch (UnsupportedEncodingException e) {
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param id
	 * @return
	 */
	public static String getDeletebook(String id) {
		String url = "null";
		try {
			id = URLEncoder.encode(id,"utf-8");
			url = DELETEBOOK + id;
			
		} catch (UnsupportedEncodingException e) {
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 */
	public static String getUploadremind() {
		return UPLOADREMIND;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 */
	public static String getSearchemind() {
		return SEARCHEMIND;
	}

	public static String getDatabase() {
		return DATABASE;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 */
	public static String getRenewborrow(String no) {
		String url = "null";
		try {
			no = URLEncoder.encode(no,"utf-8");
			url = RENEWBORROW + no;
			
		} catch (UnsupportedEncodingException e) {
			url = "null";
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 */
	public static String getHistory() {
		return HISTORY;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 */
	public static String getArrearage() {
		return ARREARAGE;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 */
	public static String getMessgae() {
		return MESSGAE;
	}

	public static String getRenewborrowinfo() {
		return RENEWBORROWINFO;
	}

	public static String getDeleteremind() {
		return DELETEREMIND;
	}
	
}
