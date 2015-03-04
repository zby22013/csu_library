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
 * �鼮���滺��
 * @name BookCoverImageCache.java
 * @author Tomorrow
 * @since  2014-4-28
 */
 
public class BookCoverImageCache {
	private List<File> imageFielList;//���滺���ļ��б�
	private String documentPath = "";//�ļ�����·��
//	private ArrayList<String> isbnList;//Ҫ���ص��鼮ͼƬISBN������
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
	 * �����鼮����
	 * ��isbn���ص���ͼƬ
	 * @param isbn
	 */
	public String download(String isbn)
	{
		msg = "";
		try {
			String name = isbn;
			File storeFile = new File(documentPath +"/" + name);
			//����ļ������ڣ������أ�����ļ����ڣ�������
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
						msg+="����Ϊ��";
					}
				}
				else{
					msg+="����ʧ��"+Connection.getHttpResponse().getStatusLine().getStatusCode();
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
	 * �ж�sdCard�Ƿ����
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
	 * ����һ��������ͼƬ
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
