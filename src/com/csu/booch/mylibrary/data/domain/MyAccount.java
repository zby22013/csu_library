package com.csu.booch.mylibrary.data.domain;

import java.util.ArrayList;

import org.apache.http.client.CookieStore;

import android.app.Application;

/**
 * 保存cookie
 * 全局变量，可以在整个程序里面使用
 * @name MyCookie.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class MyAccount extends Application {
	
	private static CookieStore cookies;  
    private static String userName="";
    private static String userId="";
    static int size=0;
    private static ArrayList<Book> remindList = new ArrayList<Book>(); 
	/**
	 * @return the userName
	 */
	public static String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public static void setUserName(String username) {
		userName = username;
	}
	/**
	 * @return the cookies
	 */
    public static ArrayList<Book> getRemindList() {
		return remindList;
	}
    /**
	 * @param remindList the remindList to set
	 */
	public static void setRemindList(ArrayList<Book> remindList) {
		MyAccount.remindList = remindList;
	}
	/**
	 * @return the remindList
	 */
	public static CookieStore getCookies() {
		return cookies;
	}
	/**
	 * @param cookies the cookies to set
	 */
	public static void setCookies(CookieStore cookie) {
		cookies = cookie;
	}
	
	public static int getSize() {
		size = cookies.getCookies().size();
		return size;
	}
	/**
	 *@author Tomorrow
	 *@since 2014-5-10
	 * @param string
	 */
	public static void setUserId(String userID) {
		userId = userID;
	}
	
	public static String getUserId() {
		return userId;
	}
	
	
	
	
    
}