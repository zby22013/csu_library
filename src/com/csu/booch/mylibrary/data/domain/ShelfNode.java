/**
 * 
 */
package com.csu.booch.mylibrary.data.domain;

/**
 * @name ShelfNode.java
 * @author Tomorrow
 * @since  2014-5-3
 */
public class ShelfNode {
	private String name="";
	private String author="";
	private String dateCome="";
	private String id="";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDateCome() {
		return dateCome;
	}
	public void setDateCome(String dateCome) {
		this.dateCome = dateCome;
	}
	
}
