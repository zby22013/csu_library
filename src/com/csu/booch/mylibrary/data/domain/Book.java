package com.csu.booch.mylibrary.data.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 书本信息保存类
 * @name Book.java
 * @author Tomorrow
 * @since  2014-4-28
 */
public class Book implements Parcelable {

	private String bookName="null";//书名
	private String author="null";//作者
	private String publisher="null";//出版社
	private String publishDate="null";//出版日期
	private String ISBN="null";//ISBN号码
	private String searchID="null";//索书号
	private String classifyID="null";//分类号
	private String pageNumber="null";//页数
	private String price="null";//价格
	private int copyNumber = 0;//副本数
	private int holdNumber = 0;//馆藏数
	private int lendTimesCount= 0;//累计借出次数
	private int lendDayCount= 0;//累计借出天数
	private String description="null";//书本描述
	private Bitmap coverImage;//封面
	private String iD="";
	
	public Book() {
	}
	public Book(String bookName, String author, String publisher,
			String publishDate, String iSBN, String searchID, String classifyID,
			String pageNumber, String price, int copyNumber, int holdNumber,
			int lendTimesCount, int lendDayCount, String description,
			Bitmap coverImage) {
		super();
		this.bookName = bookName;
		this.author = author;
		this.publisher = publisher;
		this.publishDate = publishDate;
		ISBN = iSBN;
		this.searchID = searchID;
		this.classifyID = classifyID;
		this.pageNumber = pageNumber;
		this.price = price;
		this.copyNumber = copyNumber;
		this.holdNumber = holdNumber;
		this.lendTimesCount = lendTimesCount;
		this.lendDayCount = lendDayCount;
		this.description = description;
		this.coverImage = coverImage;
	}

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return bookName;
	}
	/**
	 * @param bookName the bookName to set
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}
	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	/**
	 * @return the publishDate
	 */
	public String getPublishDate() {
		return publishDate;
	}
	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	/**
	 * @return the iSBN
	 */
	public String getISBN() {
		return ISBN;
	}
	/**
	 * @param iSBN the iSBN to set
	 */
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	/**
	 * @return the searchID
	 */
	public String getSearchID() {
		return searchID;
	}
	/**
	 * @param searchID the searchID to set
	 */
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	/**
	 * @return the classifyID
	 */
	public String getClassifyID() {
		return classifyID;
	}
	/**
	 * @param classifyID the classifyID to set
	 */
	public void setClassifyID(String classifyID) {
		this.classifyID = classifyID;
	}
	/**
	 * @return the pageNumber
	 */
	public String getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the copyNumber
	 */
	public int getCopyNumber() {
		return copyNumber;
	}
	/**
	 * @param copyNumber the copyNumber to set
	 */
	public void setCopyNumber(int copyNumber) {
		this.copyNumber = copyNumber;
	}
	/**
	 * @return the holdNumber
	 */
	public int getHoldNumber() {
		return holdNumber;
	}
	/**
	 * @param holdNumber the holdNumber to set
	 */
	public void setHoldNumber(int holdNumber) {
		this.holdNumber = holdNumber;
	}
	/**
	 * @return the lendTimesCount
	 */
	public int getLendTimesCount() {
		return lendTimesCount;
	}
	/**
	 * @param lendTimesCount the lendTimesCount to set
	 */
	public void setLendTimesCount(int lendTimesCount) {
		this.lendTimesCount = lendTimesCount;
	}
	/**
	 * @return the lendDayCount
	 */
	public int getLendDayCount() {
		return lendDayCount;
	}
	/**
	 * @param lendDayCount the lendDayCount to set
	 */
	public void setLendDayCount(int lendDayCount) {
		this.lendDayCount = lendDayCount;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the coverImage
	 */
	public Bitmap getCoverImage() {
		return coverImage;
	}
	/**
	 * @param coverImage the coverImage to set
	 */
	public void setCoverImage(Bitmap coverImage) {
		this.coverImage = coverImage;
	}
	/**
	 * @return the iD
	 */
	public String getiD() {
		return iD;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setiD(String iD) {
		this.iD = iD;
	}
	/* 
	 * 返回对象的描述
	 */
	@Override
	public String toString() {
		return "bookName=" + bookName + "\nauthor=" + author
				+ "\npublisher=" + publisher + "\npublishDate=" + publishDate
				+ "\nISBN=" + ISBN + "\nsearchID=" + searchID + "\nclassifyID="
				+ classifyID + "\npageNumber=" + pageNumber + "\nprice="
				+ price + "\ncopyNumber=" + copyNumber + "\nholdNumber="
				+ holdNumber + "\nlendTimesCount=" + lendTimesCount
				+ "\nlendDayCount=" + lendDayCount + "\ndescription="
				+ description + "\ncoverImage=" + coverImage ;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(bookName);
		dest.writeString(author);
		dest.writeString(publisher);
		dest.writeString(publishDate);
		dest.writeString(ISBN);
		dest.writeString(pageNumber);
		dest.writeString(searchID);
		dest.writeString(classifyID);
		dest.writeString(price);
		dest.writeString(description);
		dest.writeParcelable(coverImage, flags);
		dest.writeString(iD);
		dest.writeString(holdNumber+"");
	}
	public static final Parcelable.Creator<Book> CREATOR
	= new Parcelable.Creator<Book>() {
		public Book createFromParcel(Parcel in) {
			Book book = new Book();
			book.bookName = in.readString();
			book.author = in.readString();
			book.publisher = in.readString();
			book.publishDate = in.readString();
			book.ISBN = in.readString();
			book.pageNumber = in.readString();
			book.searchID = in.readString();
			book.classifyID = in.readString();
			book.price = in.readString();
			book.description = in.readString();
			book.coverImage = in.readParcelable(Bitmap.class.getClassLoader());
			book.iD = in.readString();
			book.holdNumber = Integer.parseInt(in.readString());
			return book;
		}
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};

	

}
