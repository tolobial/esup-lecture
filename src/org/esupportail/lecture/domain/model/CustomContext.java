/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.domain.ExternalService;
import org.esupportail.lecture.exceptions.domain.ContextNotFoundException;
import org.esupportail.lecture.exceptions.domain.TreeSizeErrorException;

/**
 * Customizations on a context for a user profile 
 * @author gbouteil
 *
 */
public class CustomContext implements CustomElement {

	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(CustomContext.class);
	
	/**
	 * The context Id of this CustomContext refers to
	 */
	private String contextId;

	/**
	 * The context of this customization referred to, corresponding to the contextId
	 */
	private Context context;
	
	/**
	 * Subscriptions of CustomManagedCategory
	 */
	private Map<String,CustomManagedCategory> subscriptions = new Hashtable<String,CustomManagedCategory>();
	
	/**
	 * The userprofile owner
	 */
	private UserProfile userProfile;
	
	/**
	 * Tree size of the customContext
	 */
	private int treeSize;
	
	/**
	 * Set of id corresponding to unfolded categories
	 */
	private Set<String> unfoldedCategories = new HashSet<String>();

	/**
	 * Database Primary Key
	 */
	private long customContextPK; 

	
	/*
	 ************************** INIT *********************************/	
	
	/**
	 * Constructor
	 * @param contextId id of the context refered by this CustomContext
	 * @param user owner of this CustomContext
	 */
	protected CustomContext(String contextId, UserProfile user) {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - CustomContext("+contextId+","+user.getUserId()+")");
		}
		this.contextId = contextId;
		this.userProfile = user;
		treeSize = 20;
	}
	
	
	/*
	 *************************** METHODS ************************************/

	/**
	 * Return the list of sorted customCategories displayed by this customContext
	 * @param ex access to externalService
	 * @return the list of customCategories 
	 * @throws ContextNotFoundException 
	 */
	public List<CustomCategory> getSortedCustomCategories(ExternalService ex) throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - getSortedCustomCategories(externalService)");
		}
		// TODO (GB later) rewrite with custom personnal category (+ sorted display)
	
		/* update this customContext with context */
		getContext().updateCustom(this,ex);
		
		DomainTools.getDaoService().updateCustomContext(this);
		DomainTools.getDaoService().updateUserProfile(userProfile);
		
		List<CustomCategory> listCustomCategories = new Vector<CustomCategory>();
		for(CustomManagedCategory customCat : subscriptions.values()){
			// later : add other custom elements (imported et personal)
			listCustomCategories.add(customCat);
		}
		return listCustomCategories;
	}
	

	/**
	 * Add a subscription category to this custom context (if no exists, else do nothing) 
	 * and creates the corresponding customManagedCategory with the given profile
	 * This new customManagedCategory is also added to the userProfile (owner of all customElements)
	 * @param profile the managed category profile to subscribe
	 */
	protected void addSubscription(ManagedCategoryProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - addSubscription("+profile.getId()+")");
		}
		String profileId = profile.getId();
		
		if (!subscriptions.containsKey(profileId)){
			CustomManagedCategory customManagedCategory = new CustomManagedCategory(profileId,userProfile);
			subscriptions.put(profileId,customManagedCategory);
			userProfile.addCustomCategory(customManagedCategory);
		}
	}
	// TODO (GB later) addImportation(), addCreation())
	
	/**
	 * remove a CustomManagedCategory displayed in this CustomContext
	 * and also removes it from the userProfile
	 * @param profile the managedCategoryProfile associated to the CustomManagedCategory to remove
	 */
	protected void removeCustomManagedCategory(ManagedCategoryProfile profile) {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - removeCustomManagedCategory("+profile.getId()+")");
		}
		String profileId = profile.getId();
		CustomManagedCategory cmc = subscriptions.get(profileId);
		if (cmc != null) {
			subscriptions.remove(profileId);
			userProfile.removeCustomCategory(profile.getId());
			// TODO (gb later) : il faudra supprimer toutes les r�f�rences � cette cmc
		} 
		
	}
	// TODO (GB later)  removeCustomPersonalCategory()
	
	/**
	 * Returns the Context associated to this customContext
	 * @return context 
	 * @throws ContextNotFoundException 
	 */
	public Context getContext() throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - getContext()");
		}
		if (context == null) {
			context = DomainTools.getChannel().getContext(contextId);
		}
		return context;
	}
	
	/**
	 * Returns the name of this context
	 * @throws ContextNotFoundException 
	 * @see org.esupportail.lecture.domain.model.CustomElement#getName()
	 */
	public String getName() throws ContextNotFoundException {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - getName()");
		}
		return getContext().getName();
	}
	
	/**
	 * Modify the tree size of this customContext by checking
	 * min and max treeSize
	 * @param size
	 * @throws TreeSizeErrorException
	 */
	public void modifyTreeSize(int size)throws TreeSizeErrorException {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - modifyTreeSize(size "+size+")");
		}
		/* old name was setTreesize but it has been changed to prevent 
		 * loop by calling dao
		 */
		// TODO (GB later) externaliser les bornes
		if ((size >=0) && (size <=100)){
			treeSize = size;
			DomainTools.getDaoService().updateCustomContext(this);
		}else {
			String errorMsg = "TreeSize must be into 0 and 100";
			log.error(errorMsg);
			throw new TreeSizeErrorException(errorMsg);
		}
	}

	/**
	 * mark a customCategory contained in this CustomContext as folded
	 * @param catId id of the profile category associated to the customCategory
	 */
	public void foldCategory(String catId) {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - foldCategory(catId"+catId+")");
		}
		if (!unfoldedCategories.remove(catId)){
			log.warn("foldCategory("+catId+") is called in customContext "+contextId+" but this category is yet folded");
		} else {
			DomainTools.getDaoService().updateCustomContext(this);
		}
	}
	
	/**
	 * mark a customCategory contained in this CustomContext as unfolded
	 * @param catId id of the profile category associated to the customCategory
	 */
	public void unfoldCategory(String catId) {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - unfoldCategory(catId"+catId+")");
		}
		if(!unfoldedCategories.add(catId)){
			log.warn("unfoldCategory("+catId+") is called in customContext "+contextId+" but this category is yet unfolded");
		} else {
			DomainTools.getDaoService().updateCustomContext(this);
		}
	
	}

	/**
	 * Return true if the customCategory is folded in this customContext
	 * @param catId
	 * @return if category is folded or not
	 */
	public boolean isCategoryFolded(String catId) {
		if (log.isDebugEnabled()){
			log.debug("id="+contextId+" - isCategoryFolded(catId"+catId+")");
		}
		boolean ret = false;
		if(unfoldedCategories.contains(catId)){
			ret = false;
		}else {
			ret = true;
		}
		return ret;
	}
	
	
	/* 
	 ************************** ACCESSORS **********************************/

	/**
	 * @return contextId
	 * @see CustomContext#contextId
	 */
	public String getElementId() {
		return contextId;
	}

	/**
	 * @return userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

	/**
	 * @param size
	 */
	protected void setTreeSize(int size) {
		treeSize = size;
	}


	/**
	 * @return tree size
	 */
	public int getTreeSize() {
		return treeSize;
	}


	/**
	 * @return database primary Key
	 */
	public long getCustomContextPK() {
		return customContextPK;
	}

	/**
	 * @param customContextPK - database Primary Key
	 */
	public void setCustomContextPK(long customContextPK) {
		this.customContextPK = customContextPK;
	}

	/**
	 * @return context ID
	 */
	protected String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId
	 */
	protected void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @param userProfile
	 */
	protected void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

//	/**
//	 * @return hash of subscribed categories
//	 */
//	public Map<String, CustomManagedCategory> getSubscriptions() {
//		return subscriptions;
//	}

//	/**
//	 * @param subscriptions
//	 */
//	public void setSubscriptions(
//			Map<String, CustomManagedCategory> subscriptions) {
//		this.subscriptions = subscriptions;
//	}

//	/**
//	 * @return a set of folded categories ID 
//	 */
//	public Set<String> getUnfoldedCategories() {
//		return unfoldedCategories;
//	}
//
//	/**
//	 * @param foldedCategories - set of olded categories ID 
//	 */
//	public void setUnfoldedCategories(Set<String> foldedCategories) {
//		this.unfoldedCategories = foldedCategories;
//	}
}
