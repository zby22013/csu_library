package com.csu.booch.mylibrary.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.data.domain.ShelfNode;
import com.csu.booch.mylibrary.persistence.BookCoverImageCache;
import com.csu.booch.mylibrary.persistence.HtmlAnalysis;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.persistence.connection.ConnectionURL;

/**
 * 数据服务类
 * @name DataService.java
 * @author Tomorrow
 * @since  2014-5-3
 */
public class DataService {
	private HtmlAnalysis analysis;
	private BookCoverImageCache bookCover;//书籍封面
	public DataService() {
		analysis = new HtmlAnalysis();
		bookCover = new BookCoverImageCache();
	}
	/**
	 * 获取书籍数据列表
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param keyWord
	 * @param currentPage
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<Book> getBookList(String type,String keyWord, int currentPage) throws IOException{
		ArrayList<Book> bookList = null;
		if(Connection.doGet(ConnectionURL.getSearchURL(type ,keyWord,currentPage),false)){
			bookList = analysis.getBookList(Connection.getResult());
		}
		//断开连接
		Connection.disConnection();
		return bookList;
	}
	
	public Book getBook(String name) throws ClientProtocolException, IOException{
		Book book = null;
		if(Connection.doGet(ConnectionURL.getSearchURL("name",name,1),false)){
			book = analysis.getBookList(Connection.getResult()).get(0);
		}
		//断开连接
		Connection.disConnection();
		return book;
	}

	/**
	 * 获取页数 
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @return
	 */
	public int getPageNum() {
		return analysis.getPageNum();
	}
	
	
	/**
	 * 获取封面文件地址列表
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @return
	 */
	public List<File> getImageFielList(ArrayList<String> IbsnList) {
		
		List<File> imageFielList = null;
		if(bookCover.isSDcardAvailable()){
//			bookCover.setIsbnList(analysis.getIbsnList());
			if(bookCover.downLoadImages(IbsnList)){
				imageFielList =bookCover.getImageFielList();
			}
		}
		return imageFielList;
	}
	/**
	 * 获取错误信息
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @return
	 */
	public String getMsg() {
		return bookCover.getMsg();
	}
	/**
	 * 获取用户名
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param html
	 * @return
	 */
	public String getUserName(String html) {
		return analysis.getUserName(html);
	}
	/**
	 * 获取馆藏信息
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param str
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ArrayList<String> getHoldConditionTableInfo(String id) throws ClientProtocolException, IOException {
		ArrayList<String> tableInfo = null;
		if(Connection.doGet(ConnectionURL.getDetailURL(id),false)){
			tableInfo = analysis.getHoldConditionTableInfo(Connection.getResult());
		}
		Connection.disConnection();
		return tableInfo;
	}
	/**
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param result
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ArrayList<String> getUserInfo() throws ClientProtocolException, IOException {
		ArrayList<String> list=null;
		if(Connection.doGet(ConnectionURL.getInfoURL(), true)){
			list = analysis.getUserInfo(Connection.getResult());
		}
		Connection.disConnection();
		return list;
	}
	/**
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ArrayList<ShelfNode> getBookShelfData() throws ClientProtocolException, IOException {
		ArrayList<ShelfNode> list = null;
		if(Connection.doGet(ConnectionURL.getMybookshelfURL(), true)){
			list = analysis.getBookShelfData(Connection.getResult());
		}
		else{
			list = new ArrayList<ShelfNode>();
			ShelfNode node = new ShelfNode();
			node.setName("null");
			node.setAuthor("null");
			node.setDateCome("null");
			list.add(node);
		}
		return list;
	}
	
	/**
	 * 获得借阅史信息
	 */
	public ArrayList<String> getHistoryData(String html) throws ClientProtocolException, IOException {
		ArrayList<String> list = null;
		list = analysis.getHistoryTableInfo(html);
		return list;
	}
	 
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param ibsn
	 * @return
	 */
	public Bitmap getBookCover(String ibsn){
		Bitmap cover = null;
		if(bookCover.isSDcardAvailable()){
			ArrayList<File> imageFielList = (ArrayList<File>) bookCover.getImageFielList();
			imageFielList.clear();
			bookCover.setImageFielList(imageFielList);
			if(bookCover.download(ibsn).equals("")){
				imageFielList = (ArrayList<File>) bookCover.getImageFielList();
				cover = BitmapFactory.decodeFile(imageFielList.get(0).getPath());
			}
			else{
				
			}
		}
		return cover;
	} 
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-6
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public boolean deleteBookStore(String id) throws ClientProtocolException, IOException{
		if(Connection.doGet(ConnectionURL.getDeletebook(id), true)){
			return true;
		}
		Connection.disConnection();
		return false;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param html
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public ArrayList<Book> getRemindBookList(String html) throws ClientProtocolException, IOException {
		ArrayList<String> remindBookISBNList = analysis.getRemindBookISBNList(html);
		ArrayList<Book> remindBookList = new ArrayList<Book>();
		for(int i = 0;i<remindBookISBNList.size();i++){
			if(Connection.doGet(ConnectionURL.getSearchURL("isbn", remindBookISBNList.get(i), 1), false)){
				remindBookList.addAll(analysis.getBookList(Connection.getResult()));
			}
			Connection.disConnection();
		}
		return remindBookList;
	}
	
	
	public String getMyMessage() 
			throws IllegalStateException, ClientProtocolException, IOException {
		String message = "";
		if(Connection.doGet(ConnectionURL.getMessgae(), true)){
			message = analysis.getMyMessage(Connection.getResult());
		}
		Connection.disConnection();
		return message;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 * @throws IllegalStateException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ArrayList<String> getRenewBorrowTable() 
			throws IllegalStateException, ClientProtocolException, IOException {
		ArrayList<String> table=null;
		if(Connection.doGet(ConnectionURL.getRenewborrowinfo(), true)){
			table = analysis.getRenewTable(Connection.getResult());
		}
		Connection.disConnection();
		return table; 
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getMyArrearage() throws ClientProtocolException, IOException{
		String arrearage="";
		if(Connection.doGet(ConnectionURL.getArrearage(), true)){
			arrearage = analysis.getMyArrearage(Connection.getResult());
		}
		Connection.disConnection();
		return arrearage;
	}
	
	public ArrayList<String> renewMyBooks(String ids)
			throws IllegalStateException, ClientProtocolException, IOException{
		ArrayList<String> msgBack = null;
		if(Connection.doGet(ConnectionURL.getRenewborrow(ids), true)){
			msgBack = analysis.getRenewMsgBack(Connection.getResult());
		}
		return msgBack;
	}
}
