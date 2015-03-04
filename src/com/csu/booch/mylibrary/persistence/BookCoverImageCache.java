package com.csu.booch.mylibrary.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import android.os.Environment;

import com.csu.booch.mylibrary.persistence.connection.Connection;
import com.csu.booch.mylibrary.persistence.connection.ConnectionURL;

/**
 * 书籍封面缓存
 * @name BookCoverImageCache.java
 * @author Tomorrow
 * @since  2014-4-28
 */
 
public class BookCoverImageCache {
	private List<File> imageFielList;//封面缓存文件列表
	private String documentPath = "";//文件保存路径
//	private ArrayList<String> isbnList;//要下载的书籍图片ISBN号数组
	private String msg = "";
	
//	public BookCoverImageCache(ArrayList<String> isbnList) {
//		this.isbnList = isbnList;
//		imageFielList = new ArrayList<File>();
//	}
	/**
	 * 
	 */
	public BookCoverImageCache() {
		imageFielList = new ArrayList<File>();
	}
	
	/**
	 * 下载书籍封面
	 * 按isbn下载单个图片
	 * @param isbn
	 */
	public String download(String isbn)
	{
		msg = "";
		try {
			String name = isbn;
			File storeFile = new File(documentPath +"/" + name);
			//如果文件不存在，则下载，如果文件存在，则跳过
			if(!storeFile.exists()){
				if(Connection.doGet(ConnectionURL.getBookCoverURL(isbn),false)){
					HttpResponse response = Connection.getHttpResponse();
					FileOutputStream fileOutputStream = new FileOutputStream(storeFile);
					FileOutputStream output = fileOutputStream;
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream instream = entity.getContent();
						byte b[] = new byte[1024];
						int j = 0;
						while( (j = instream.read(b))!=-1){
							output.write(b,0,j);
						}
						output.flush();
						output.close();
						imageFielList.add(storeFile);
					}
					else{
						msg+="返回为空";
					}
				}
				else{
					msg+="链接失败"+Connection.getHttpResponse().getStatusLine().getStatusCode();
				}
				Connection.disConnection();
			}
			else{
				imageFielList.add(storeFile);
			}
		} catch (IOException e) {
			msg+="IOException";
			e.printStackTrace();
		}
		catch (IllegalStateException e) {
			msg+="IllegalStateExceptions";
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 判断sdCard是否可用
	 * @return
	 */
	public boolean isSDcardAvailable(){
		boolean isAvailable = false;
		if(Environment.getExternalStorageState()   
				.equals(android.os.Environment.MEDIA_MOUNTED)){
			documentPath = Environment.getExternalStorageDirectory().getPath();
			documentPath +="/mylibrary/coverImageCache";
			File destDir = new File(documentPath);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			isAvailable = true; 
		}
		return isAvailable;
	}

	/**
	 * @return the imageFielList
	 */
	public List<File> getImageFielList(){
		msg = "";
		return imageFielList;
	}
	/**
	 * @return the documentPath
	 */
	public String getDocumentPath(){
		return documentPath;
	}
	/**
	 * 下载一定数量的图片
	 * @param isbnList
	 * @return
	 */
	public boolean downLoadImages(ArrayList<String> isbnList){
		boolean isSuccess = false;
		if(imageFielList!=null){
			imageFielList.clear();
		}
		if(isbnList != null){
			for(int i = 0;i<isbnList.size();i++){
				msg += download(isbnList.get(i));
			}
			if(msg.equals("")){
				isSuccess = true;
			}
		}
		else{
			msg= "isbnList = null";
		}
		return isSuccess;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param isbnList the isbnList to set
	 */
//	public void setIsbnList(ArrayList<String> isbnList) {
//		this.isbnList = isbnList;
//	}
	/**
	 * @return the isbnList
	 */
//	public ArrayList<String> getIsbnList() {
//		return isbnList;
//	}
	/**
	 * @param imageFielList the imageFielList to set
	 */
	public void setImageFielList(List<File> imageFielList) {
		this.imageFielList = imageFielList;
	}
	
}
