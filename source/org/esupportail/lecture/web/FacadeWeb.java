/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.web;

import org.esupportail.lecture.domain.service.FacadeService;

/**
 * Entry point to access domain service, used by org.esupportail.lecture.web package
 * @author gbouteil
 *
 */
public class FacadeWeb {
	
	private FacadeService facadeService;
	
	/**
	 * Return a FacadeService
	 * @return facadeService
	 */
	public FacadeService getFacadeService() {
		return facadeService;
	}

	/**
	 * Sets a FacadeService
	 * @param facadeService
	 */
	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}
	
}