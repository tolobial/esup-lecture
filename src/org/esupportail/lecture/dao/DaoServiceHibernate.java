/**
 * Hibernate Doa Service implementation 
 */
package org.esupportail.lecture.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.model.CustomCategory;
import org.esupportail.lecture.domain.model.CustomContext;
import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.UserProfile;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author bourges
 */
public class DaoServiceHibernate extends HibernateDaoSupport {
	/**
	 * Log instance 
	 */
	private static final Log log = LogFactory.getLog(DaoServiceHibernate.class);
	
	/**
	 * boolena flag in order to use flush during work
	 * should be false (true for test)
	 */
	private static final boolean useFlush = false;

	/**
	 * @param userId 
	 * @return UserProfile
	 * @see org.esupportail.lecture.dao.DaoService#getUserProfile(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public UserProfile getUserProfile(String userId) {
		if (log.isDebugEnabled()) {
			log.debug("getUserProfile("+userId+")");			
		}
		UserProfile ret = null;
		if (userId != null) {
			String query = "select userProfile from UserProfile userProfile where userProfile.userId = ?";
		    List<UserProfile> list = getHibernateTemplate().find(query, userId);
		    if (list.size()>0) {
			    ret = list.get(0);				
			}
		}
		else {
			String msg = "userId is null: can't find it in database";
			log.error(msg);
			//TODO RB --> throw new InfoDaoException(msg);
		}
		return ret;
	}

	/**
	 * @param userProfile 
	 * @return UserProfile
	 * @see org.esupportail.lecture.dao.DaoService#refreshUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public UserProfile refreshUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("refreshUserProfile("+(userProfile != null ? userProfile.getUserId() : "null")+")");			
		}
		UserProfile ret = userProfile;
		getHibernateTemplate().lock(userProfile, LockMode.NONE);
		//ret = (UserProfile)getHibernateTemplate().merge(userProfile);
		return ret;
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#saveUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void saveUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("saveUserProfile PK="+userProfile.getUserProfilePK());			
		}
		//userProfile = (UserProfile)getHibernateTemplate().merge(userProfile);
		getHibernateTemplate().saveOrUpdate(userProfile);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#deleteUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void deleteUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("deleteUserProfile PK="+userProfile.getUserProfilePK());			
		}
		getHibernateTemplate().delete(userProfile);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param userProfile 
	 * @see org.esupportail.lecture.dao.DaoService#updateUserProfile(org.esupportail.lecture.domain.model.UserProfile)
	 */
	public void updateUserProfile(UserProfile userProfile) {
		if (log.isDebugEnabled()) {
			log.debug("updateUserProfile PK="+userProfile.getUserProfilePK());			
		}
		//userProfile = (UserProfile)getHibernateTemplate().merge(userProfile);
		getHibernateTemplate().saveOrUpdate(userProfile);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param customContext 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void updateCustomContext(CustomContext customContext) {
		if (log.isDebugEnabled()) {
			log.debug("updateCustomContext PK="+customContext.getCustomContextPK());			
		}
		//customContext = (CustomContext)getHibernateTemplate().merge(customContext);
		getHibernateTemplate().saveOrUpdate(customContext);
	}

	/**
	 * @param cco 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomContext(org.esupportail.lecture.domain.model.CustomContext)
	 */
	public void deleteCustomContext(CustomContext cco) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCustomContext PK="+cco.getCustomContextPK());			
		}
		getHibernateTemplate().delete(cco);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param cca 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void deleteCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCustomCategory PK="+cca.getCustomCategoryPK());			
		}
		getHibernateTemplate().delete(cca);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param cca 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomCategory(org.esupportail.lecture.domain.model.CustomCategory)
	 */
	public void updateCustomCategory(CustomCategory cca) {
		if (log.isDebugEnabled()) {
			log.debug("updateCustomCategory PK="+cca.getCustomCategoryPK());			
		}
		//cca = (CustomCategory)getHibernateTemplate().merge(cca);
		getHibernateTemplate().saveOrUpdate(cca);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param cs 
	 * @see org.esupportail.lecture.dao.DaoService#deleteCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void deleteCustomSource(CustomSource cs) {
		if (log.isDebugEnabled()) {
			log.debug("deleteCustomSource PK="+cs.getCustomSourcePK());			
		}
		getHibernateTemplate().delete(cs);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

	/**
	 * @param source 
	 * @see org.esupportail.lecture.dao.DaoService#updateCustomSource(org.esupportail.lecture.domain.model.CustomSource)
	 */
	public void updateCustomSource(CustomSource source) {
		if (log.isDebugEnabled()) {
			log.debug("updateCustomSource PK="+source.getElementId());			
		}
		//source = (CustomSource)getHibernateTemplate().merge(source);
		getHibernateTemplate().saveOrUpdate(source);
		if (useFlush) {
			getHibernateTemplate().flush();
		} 
	}

}
