package com.csu.booch.mylibrary.persistence;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csu.booch.mylibrary.data.domain.Book;
import com.csu.booch.mylibrary.data.domain.ShelfNode;
import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.persistence.connection.ConnectionURL;

/**
 * 解析HTML，返回数据集合
 * @name HtmlAnalysis.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class HtmlAnalysis {
//	private ArrayList<Book> BookList;//当前页图书数据数组
	private Document doc;//保存数据，用于解析的变量
//	private String html;//分析的HTML数据
	//IBSN数组
//	private ArrayList<String> IbsnList;
	private int pageNum = 0;
	public HtmlAnalysis(){
//		BookList = new ArrayList<Book>();
//		IbsnList = new ArrayList<String>();
	}
//	public HtmlAnalysis(String html){
//		this.html = html;
//		BookList = new ArrayList<Book>();
//		IbsnList = new ArrayList<String>();
//	}

	/**
	 * 解析获取的HTML
	 * 返回数据数组
	 * @return bookList
	 */
	public ArrayList<Book> getBookList(String html){
		ArrayList<Book> bookList = new ArrayList<Book>();
		if(!html.equals("")){
			doc = Jsoup.parse(html);
			//清空当前数据,减少内存消耗
			
			bookList.clear();
//			IbsnList.clear();
			//获取页数
			pageNum = Integer.parseInt(doc.select("div.ctrl").get(0).getElementById("labConutPage").text());
			//选定内容标签
			Element div = doc.select("div.main").first();
			Elements uls = div.getElementsByClass("resultlist");
			String [] infoList = new String[10];
			String [] iDList = new String[uls.size()];
			for(int j = 0;j<uls.size();j++){
				String bookName = uls.get(j).select("h3.title").text();//获取书名
				String author = uls.get(j).select("span.author")
						.get(0).getElementsByTag("strong").text();//获取作者
				String publisher = uls.get(j).select("span.publisher")
						.get(0).getElementsByTag("strong").text();//获取出版社
				String description =  uls.get(j).select("div.text").text();//获取描述
				String id = uls.get(j).getElementById("StrTmpRecno").val();//获取书籍id，用于获取动态加载数据
				iDList[j] = id;
				Elements dates = uls.get(j).select("span.dates");
				for(int i= 0;i< dates.size();i++){
					infoList[i] = dates.get(i).getElementsByTag("strong").text();
				}
//				IbsnList.add(infoList[1]);
				Book book = new Book(bookName, //名字
						author, //作者
						publisher, //出版社
						infoList[0], //出版日期
						infoList[1], //ISBN
						infoList[2], //索书号
						infoList[3], //分类号
						infoList[4], //页数
						infoList[5], //价格
						0,//副本数   这四个数据为动态数据，为了提高效率，后面统一加载 。
						0,//在馆数
						0,//累借次数
						0,//累借天数
						description, 
						null);
				book.setiD(id);
				bookList.add(book);
			}
			infoList = null;
			getMoreInformation(iDList,bookList);
			iDList = null;
		}
		return bookList;
	}
	/**
	 * 获取动态数据
	 * @param bookList 
	 */
	private void getMoreInformation(String [] iDList, ArrayList<Book> bookList){
		try {
			String id = "";//id长串
			for(int i = 0; i < iDList.length;i++){
				id += iDList[i]+";";
			}
			if( Connection.
					doGet(ConnectionURL.getMoreURL(id),false)){
				String temp = Connection.getResult();
				Document docTemp = Jsoup.parse(temp);
				//获取books标签下所有内容
				Elements books =  docTemp.getElementsByTag("books");
				//根据索书号获取所需信息
				String callno ="";
				for(int i = 0;i <books.size();i++ ){
					//获取一个books下所有book标签下所有内容
					Elements e = books.get(i).getElementsByTag("book");
					//如果索书号相等，则说明是同一本书，跳过
					if(callno.equals(e.get(0).getElementsByTag("callno").text())){
						continue;
					}
					else{
						//设置数据
						callno = e.get(0).getElementsByTag("callno").text();
						if(bookList.get(i).getSearchID().equals(callno)){
							bookList.get(i).setLendDayCount(
									Integer.parseInt(e.get(0).getElementsByTag("loanDatanum").text()));
							bookList.get(i).setLendTimesCount(
									Integer.parseInt(e.get(0).getElementsByTag("loannum").text()));
							bookList.get(i).setHoldNumber(
									Integer.parseInt(e.get(0).getElementsByTag("hldcount").text()));
							bookList.get(i).setCopyNumber(
									Integer.parseInt(e.get(0).getElementsByTag("hldallnum").text()));
						}
					}

				}
			}
			Connection.disConnection();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * @param html the html to set
	 */
//	public void setHtml(String html) {
//		this.html = html;
//	}

//	/**
//	 * @return the IbsnList
//	 */
//	public ArrayList<String> getIbsnList() {
//		return IbsnList;
//	}
	/**
	 * 获取馆藏信息表的信息
	 * @param html
	 * @return
	 */
	public ArrayList<String> getHoldConditionTableInfo(String html){
		ArrayList<String> infoList = new ArrayList<String>();
		doc = Jsoup.parse(html);
		Elements table = doc.select("tbody");
		Elements trs = table.select("tr");
		for(int j = 0;j<trs.size();j++){
			Elements tds = trs.get(j).select("td");
			for(int i = 0;i<tds.size();i++){
				if(tds.get(i).text().equals("")){
					infoList.add("无");
				}
				else{
					infoList.add(tds.get(i).text());
				}

			}
		}
		return infoList;
	}
	
	/**
	 * 获取借阅史信息表的信息
	 * @param html
	 * @return
	 */
	public ArrayList<String> getHistoryTableInfo(String html){
		ArrayList<String> infoList = new ArrayList<String>();
		doc = Jsoup.parse(html);
		Elements trs = doc.select("tr");
		for(int j = 0;j<trs.size();j++){
			Elements tds = trs.get(j).select("td");
			for(int i = 0;i<tds.size();i++){
				if(tds.get(i).text().equals("")){
					infoList.add("无");
				}
				else{
					infoList.add(tds.get(i).text());
				}

			}
		}
		return infoList;
	}

	/**
	 * 获取全局用户名
	 * @param html
	 * @return
	 */
	public String getUserName(String html){
		String username="null";
		doc = Jsoup.parse(html);
		String title = doc.title();
		if(title.equals("我的图书馆")){
			username = doc.getElementById("LabUserName").text();
		}
		return username;
	}

	/**
	 * 获取用户信息
	 * @author Tomorrow
	 * @param html
	 * @return
	 */
	public ArrayList<String> getUserInfo(String html){
		ArrayList<String> infoList = new ArrayList<String>();
		doc = Jsoup.parse(html);
		Elements table = doc.select("tbody");
		Elements trs = table.select("tr");
		for(int j = 0;j<trs.size();j++){
			Elements tds = trs.get(j).select("td");
			for(int i = 0;i<tds.size();i++){
				if(tds.get(i).text().equals("")){
					if(!tds.get(i).select("input").attr("value").equals("")){
						infoList.add(tds.get(i).select("input").attr("value"));
					}
					else{
						infoList.add("");
					}
				}
				else{
					infoList.add(tds.get(i).text());
				}

			}
		}
		return infoList;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-4-28
	 * @param html
	 * @return
	 */
	/*
	public String getPermissionDetail(String html){
		String reault="";
		doc = Jsoup.parse(html);
		Elements spans = doc.select("span");
		for(Element e :spans){
//			Elements trs = e.select("TR");
//			for(Element t:trs){
//				if(!t.text().equals("")){
//					reault+=t.text()+"  ";
//				}
//			}
			reault+=e.toString();
		}
		return reault;
	}
	*/
	/**
	 *@author Tomorrow
	 *@since 2014-5-3
	 * @param result
	 * @return
	 */
	public ArrayList<ShelfNode> getBookShelfData(String result) {
		ArrayList<ShelfNode> bookList = new ArrayList<ShelfNode>();
		doc = Jsoup.parse(result);
		Elements table = doc.select("table");
		Elements trs = table.select("tr");
		for(int i = 0;i<trs.size();i++){
			if(trs.get(i).text().length()>15){
				ShelfNode node = new ShelfNode();
				String str = trs.get(i).text();
				node.setName(str.substring(0, str.indexOf("作")));
				node.setAuthor(str.substring(str.indexOf("作"), str.indexOf("入")));
				node.setDateCome(str.substring(str.indexOf("入")));
				Elements a= trs.get(i).select("a");
				for(int x = 0;x<a.size();x++){
					if(a.get(x).attr("href").contains("BookRecno=")){
						node.setId(a.get(x).attr("href").substring(a.get(x).attr("href").indexOf("BookRecno=")+10, a.get(x).attr("href").indexOf("&")));
						break;
					}
				}
				bookList.add(node);
			}
		}
		
		return bookList;
	}
	
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param html
	 * @return
	 */
	public ArrayList<String> getRemindBookISBNList(String html){
		ArrayList<String> remindBookISBNList  = new ArrayList<String>();
		doc = Jsoup.parse(html);
		Elements lis = doc.select("li");
		for(int i = 0;i<lis.size();i++){
			remindBookISBNList.add(lis.get(i).text());
		}
		return remindBookISBNList;
	}

	/**
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param result
	 * @return
	 */
	public String getMyMessage(String html) {
		String msg = "";
		doc = Jsoup.parse(html);
		Elements table = doc.select("table");
		for(Element e:table){
			msg = e.text();
		}
		return msg;
	}

	/**
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param result
	 * @return
	 */
	public String getMyArrearage(String html) {
		String arr="";
		doc = Jsoup.parse(html);
		arr = doc.body().text();
		return arr;
	}

	/**
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param result
	 * @return
	 */
	public ArrayList<String> getRenewTable(String result) {
		ArrayList<String> table = new ArrayList<String>();
		doc = Jsoup.parse(result);
		Elements tbody = doc.select("tbody");
		Elements trs = tbody.select("tr");
		for(int i = 0;i<trs.size();i++){
			Elements tds = trs.get(i).select("td");
			for(int j = 0 ;j<tds.size();j++){
				if(!tds.get(j).text().equals("")){
					table.add(tds.get(j).text());
				}
				
			}
		}
		return table;
	}
	/**
	 * 
	 *@author Tomorrow
	 *@since 2014-5-12
	 * @param html
	 * @return
	 */
	public String getMessage(String html){
		String message = "";
		doc = Jsoup.parse(html);
		message = doc.text();
		return message;
	}

	/**
	 *@author Tomorrow
	 *@since 2014-5-12
	 * @param result
	 * @return
	 */
	public ArrayList<String> getRenewMsgBack(String html) {
		ArrayList<String> msgBack=new ArrayList<String>();
		doc = Jsoup.parse(html);
		Elements bs = doc.select("b");
		for(Element e:bs){
			msgBack.add(e.text());
		}
		return msgBack;
	}
}

