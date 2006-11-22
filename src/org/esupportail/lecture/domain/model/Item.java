package org.esupportail.lecture.domain.model;

/**
 * @author bourges
 * used to store item informations
 */
public class Item {
	/**
	 * id of item
	 */
	private String id;
	/**
	 * html content of item
	 */
	private String htmlContent;
	/**
	 * store if item is read or not
	 */
	private boolean read;
	/**
	 * @return html content of item
	 */
	public String getHtmlContent() {
		return htmlContent;
	}
	/**
	 * @param htmlContent
	 */
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	/**
	 * @return id of item
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return if item is read or not
	 */
	public boolean isRead() {
		return read;
	}
	/**
	 * @param read
	 */
	public void setRead(boolean read) {
		this.read = read;
	}
	

}
