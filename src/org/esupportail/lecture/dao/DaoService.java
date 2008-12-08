package org.esupportail.lecture.dao;
import java.util.List;

import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.ManagedCategory;
import org.esupportail.lecture.domain.model.ManagedCategoryProfile;
import org.esupportail.lecture.domain.model.ManagedSourceProfile;
import org.esupportail.lecture.domain.model.Source;
import org.esupportail.lecture.domain.model.UserProfile;
import org.esupportail.lecture.domain.model.VersionManager;
import org.esupportail.lecture.exceptions.dao.InfoDaoException;
import org.esupportail.lecture.exceptions.dao.InternalDaoException;
import org.esupportail.lecture.exceptions.dao.NoUserIdException;
import org.esupportail.lecture.exceptions.dao.TimeoutException;

/**
 * Interface Service to Data Access Object.
 * @author gbouteil
 *
 */
public interface DaoService {
	
	/* Remote data */
	
	/**
	 * Get a managed category from a remote place.
	 * @param profile of the category to get
	 * @return the managedCategory
	 * @throws InfoDaoException 
	 * @throws TimeoutException 
	 * @throws InfoDaoException 
	 */
	ManagedCategory getManagedCategory(ManagedCategoryProfile profile) throws InfoDaoException ;

	/**
	 * Get a managed category from a remote place.
	 * @param profile of the category to get
	 * @param ptCas proxy ticket CAS used in case of CAS protected source
	 * @return the managedCategory
	 * @throws InfoDaoException 
	 * @throws InfoDaoException 
	 */
	ManagedCategory getManagedCategory(ManagedCategoryProfile profile, String ptCas) throws InfoDaoException ;

	/**
	 * get a source from a remote place.
	 * @param profile of the source to get
	 * @return the source
	 * @throws InternalDaoException 
	 * @throws InfoDaoException 
	 * @throws InfoDaoException 
	 */
	Source getSource(ManagedSourceProfile profile) throws InternalDaoException;
	
	/**
	 * get a source from a remote place.
	 * @param profile of the source to get
	 * @param ptCas proxy ticket CAS used in case of CAS protected source
	 * @return the source
	 * @throws InternalDaoException 
	 * @throws InfoDaoException 
	 */
	Source getSource(ManagedSourceProfile profile, String ptCas) throws InternalDaoException ;

	/* User Profile */
	
	/**
	 * Returns the userProfile that is identified with "userId" 
	 * and null if no user profile exists with this userId.
	 * @param userId : user identifient provided by portlet request
	 * @return user profile 
	 * @throws NoUserIdException 
	 */
	UserProfile getUserProfile(String userId);

	/**
	 * Return a "fresh" userProfile from data base. 
	 * @param userProfile
	 * @return a "fresh" userProfile
	 */
	UserProfile refreshUserProfile(UserProfile userProfile);

	/**
	 * Add a user profile to persistent data.
	 * @param userProfile : user to add
	 */
	void saveUserProfile(UserProfile userProfile);

	/**
	 * Delete userProfile that is identified with "userId". 
	 * @param userProfile : userProfile to delete
	 */
	// TODO (GB later) creer un service pour supprimer définitivement un userProfile par l'admin ?
	void deleteUserProfile(UserProfile userProfile);

	/**
	 * update userProfile.
	 * @param userProfile
	 */
	void updateUserProfile(UserProfile userProfile);
	
	/* CustomContext */

	/**
	 * update custom context.
	 * @param customContext
	 */
	void updateCustomContext(CustomContext customContext);

	/**
	 * delete custom context.
	 * @param cco
	 */
	// TODO (GB later) creer un service pour supprimer définitivement un contexte par l'admin ?
	void deleteCustomContext(CustomContext cco);
	
	/* CustomCategory */
	
	/**
	 * delete custom category.
	 * @param cca
	 */
	void deleteCustomCategory(CustomCategory cca);

	/**
	 * update custom category.
	 * @param cca
	 */
	void updateCustomCategory(CustomCategory cca);

	/* CustomSource */
	
	/**
	 * delete custom source.
	 * @param cs
	 */
	void deleteCustomSource(CustomSource cs);

	/**
	 * update custom source.
	 * @param source
	 */
	void updateCustomSource(CustomSource source);

	/**
	 * @return all the VersionManager instances of the database.
	 */
	List<VersionManager> getVersionManagers();

	/**
	 * Add a versionManaer.
	 * @param versionManager
	 */
	void addVersionManager(VersionManager versionManager);

	/**
	 * Update a VersionManager.
	 * @param versionManager
	 */
	void updateVersionManager(VersionManager versionManager);
		
}
