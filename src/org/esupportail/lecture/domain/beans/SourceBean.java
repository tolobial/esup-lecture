/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.AvailabilityMode;
import org.esupportail.lecture.domain.model.CoupleProfileAvailability;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ElementProfile;
import org.esupportail.lecture.domain.model.ItemDisplayMode;
import org.esupportail.lecture.domain.model.SourceProfile;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryNotLoadedException;
import org.esupportail.lecture.exceptions.domain.ElementDummyBeanException;
import org.esupportail.lecture.exceptions.domain.ManagedCategoryProfileNotFoundException;
import org.esupportail.lecture.exceptions.domain.SourceProfileNotFoundException;

/**
 * used to store source informations.
 * @author bourges
 */
public class SourceBean {
	
	/* 
	 *************************** PROPERTIES ******************************** */	
	/**
	 * id of source.
	 */
	private String id;
	/**
	 * name of source.
	 */
	private String name;
	/**
	 * type of source.
	 * "subscribed" --> The source is allowed and subscribed by the user
	 * "notSubscribed" --> The source is allowed and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 * "owner" --> For personal sources
	 */
	private AvailabilityMode type;
	
	/**
	 * the item display mode of the source.
	 */
	private ItemDisplayMode itemDisplayMode = ItemDisplayMode.ALL;

	/*
	 *************************** INIT ************************************** */	
	
	/**
	 * default constructor.
	 */
	public SourceBean() {
		// empty
	}
	
	/**
	 * constructor initializing object with a customSource.
	 * @param customSource
	 * @throws SourceProfileNotFoundException 
	 * @throws ManagedCategoryNotLoadedException 
	 * @throws ManagedCategoryProfileNotFoundException 
	 */
	public SourceBean(final CustomSource customSource) throws ManagedCategoryProfileNotFoundException, 
			ManagedCategoryNotLoadedException, SourceProfileNotFoundException {
		SourceProfile profile = customSource.getProfile();
		
		this.name = profile.getName();
		this.id = profile.getId();
		this.itemDisplayMode = customSource.getItemDisplayMode();
	}

	/**
	 * constructor initializing object with CoupleProfileAvailability.
	 * @param profAv CoupleProfileAvailability
	 */
	public SourceBean(final CoupleProfileAvailability profAv) {
		ElementProfile elt = profAv.getProfile();
		this.name = elt.getName();
		this.id = elt.getId();
		this.type = profAv.getMode();
		
	}
	
	/*
	 *************************** ACCESSORS ********************************* */	
	


	/**
	 * @return name of source
	 * @throws ElementDummyBeanException 
	 */
	public String getName() throws ElementDummyBeanException {
		return name;
	}
	
	/**
	 * @param name
	 * @throws ElementDummyBeanException 
	 */
	public void setName(final String name) throws ElementDummyBeanException {
		this.name = name;
	}
	/**
	 * @return id of source
	 * @throws ElementDummyBeanException 
	 */
	public String getId()  throws ElementDummyBeanException {
		return id;
	}
	/**
	 * @param id
	 * @throws ElementDummyBeanException 
	 */
	public void setId(final String id)  throws ElementDummyBeanException {
		this.id = id;
	}
	
	/**
	 * @return type of source
	 * @throws ElementDummyBeanException 
	 */
	public AvailabilityMode getType()  throws ElementDummyBeanException {
		return type;
	}
	
	/**
	 * @param type
	 * @throws ElementDummyBeanException 
	 */
	public void setType(final AvailabilityMode type) throws ElementDummyBeanException {
		this.type = type;
	}
	
	/**
	 * @return item display mode
	 * @throws ElementDummyBeanException 
	 */
	public ItemDisplayMode getItemDisplayMode() throws ElementDummyBeanException {
		return itemDisplayMode;
	}

	/**
	 * @param itemDisplayMode
	 * @throws ElementDummyBeanException 
	 */
	public void setItemDisplayMode(final ItemDisplayMode itemDisplayMode) throws ElementDummyBeanException {
		this.itemDisplayMode = itemDisplayMode;
	}
	/*
	 *************************** METHODS *********************************** */	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Type = "; 
		if (type != null) {
			string += type;
		}
		string += "\n     displayMode = " + itemDisplayMode.toString() + "\n";
		
		return string;
	}

}
