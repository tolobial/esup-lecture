package org.esupportail.lecture.domain.model;


import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.ExternalService;



/**
 * Class where are defined user profile (and customizations ...)
 * @author gbouteil
 *
 */
public class UserProfile {
	
	/*
	 ************************** PROPERTIES *********************************/	
	
	/**
	 * Log instance
	 */
	protected static final Log log = LogFactory.getLog(UserProfile.class);
	
	/**
	 * Id of the user, get from portlet request by USER_ID, defined in the channel config
	 * @see LectureTools#USER_ID
	 * @see ChannelConfig#loadUserId()
	 */
	private String userId;
	
	/**
	 * Hashtable of CustomContexts defined for the user, indexed by contexID.
	 */
	private Map<String,CustomContext> customContexts;

	/**
	 * Hashtable of CustomManagedCategory defined for the user, indexed by ManagedCategoryProfilID.
	 */
	// TODO pourquoi pas customCategories ?
	private Map<String,CustomManagedCategory> customManagedCategories;

	/*
	 ************************** Initialization ************************************/
	
	/**
	 * Constructor
	 */
	public UserProfile(){
		customContexts = new Hashtable<String,CustomContext>();
		customManagedCategories = new Hashtable<String, CustomManagedCategory>();
	}
	
	/**
	 * Constructor
	 */
	public UserProfile(String userId){
		customContexts = new Hashtable<String,CustomContext>();
		customManagedCategories = new Hashtable<String, CustomManagedCategory>();
		this.setUserId(userId);
	}
	/*
	 *************************** METHODS ************************************/

	
	/**
	 * Return the customContext identified by the id of the context (contextId)id" 
	 * if exists, else create it.
	 * @param contextId identifier of the context refered by the customContext
	 * @return customContext (or null)
	 */
	public CustomContext getCustomContext(String contextId){
		CustomContext customContext = 
				customContexts.get(contextId);
		if (customContext == null){
			customContext = new CustomContext(contextId,this);
			addCustomContext(customContext);
		}
		
		return customContext;
	}
	
	public CustomManagedCategory getCustomManagedCategory(String categoryId){
		// TODO revoir avec customCategory et customManagedCategory
		CustomManagedCategory customCategory = 
			customManagedCategories.get(categoryId);
		if(customCategory == null){
			customCategory = new CustomManagedCategory(categoryId);
			addCustomCategory(customCategory);
		}
		return customCategory;
	}
	
	
	
	
	/* see later */

	
	/* ************************** ACCESSORS ********************************* */

		public void addCustomContext(CustomContext customContext){
			customContexts.put(customContext.getContextId(),customContext);
		}
		
		public void addCustomCategory(CustomManagedCategory customCategory){
			customManagedCategories.put(customCategory.getCategoryProfileID(),customCategory);
		}


//	/**
//	 * @return Returns the customContexts.
//	 */
//	public Hashtable<String, CustomContext> getCustomContexts() {
//		return customContexts;
//	}
//
//	/**
//	 * @param customContexts The customContexts to set.
//	 */
//	public void setCustomContexts(Hashtable<String, CustomContext> customContexts) {
//		this.customContexts = customContexts;
//	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, CustomContext> getCustomContexts() {
		return customContexts;
	}

	public void setCustomContexts(Map<String, CustomContext> customContexts) {
		this.customContexts = customContexts;
	}

	public Map<String, CustomManagedCategory> getCustomManagedCategories() {
		return customManagedCategories;
	}

	public void setCustomMabagedCategories(
			Map<String, CustomManagedCategory> customManagedCategories) {
		this.customManagedCategories = customManagedCategories;
	}

//	/**
//	 * @uml.property  name="customManagedCategories"
//	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="composite" inverse="userProfile:org.esupportail.lecture.domain.model.CustomManagedCategory"
//	 */
//	private Collection customManagedCategories;
//
//	/**
//	 * @uml.property  name="customManagedSources"
//	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="composite" inverse="userProfile:org.esupportail.lecture.domain.model.CustomManagedSource"
//	 */
//	private Collection customManagedSources;

	

}
