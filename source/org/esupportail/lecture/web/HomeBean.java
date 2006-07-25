/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import java.util.List;
import org.esupportail.lecture.domain.model.Category;

public class HomeBean {
	private List<Category> categories;
	private FacadeWeb facadeWeb;
	
	public void setFacadeWeb(FacadeWeb facadeWeb) {
		this.facadeWeb = facadeWeb;
	}
	
	public List<Category> getCategories() {
		this.categories = facadeWeb.getLectureService().getCategories();
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
