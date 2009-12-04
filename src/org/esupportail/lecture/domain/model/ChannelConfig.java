/**
* ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
* For any information please refer to http://esup-helpdesk.sourceforge.net
* You may obtain a copy of the licence at http://www.esup-portail.org/license/
*/
package org.esupportail.lecture.domain.model;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.esupportail.lecture.dao.FreshXmlFileThread;
import org.esupportail.lecture.domain.DomainTools;
import org.esupportail.lecture.exceptions.dao.XMLParseException;
import org.esupportail.lecture.exceptions.domain.ChannelConfigException;

/**
 * Channel Config : class used to load and parse XML channel file config.
 * @author gbouteil
 */
public class ChannelConfig  {
	
	/* 
	 ********************** PROPERTIES**************************************/ 
	/**
	 * Log instance. 
	 */
	private static final Log LOG = LogFactory.getLog(ChannelConfig.class);
	
	/**
	 * Instance of this class.
	 */
	private static ChannelConfig singleton;
	
	/**
	 * XML file loaded.
	 */
	private static Document xmlFile;
	
	/**
	 *  relative classpath of the file to load.
	 */
	private static String filePath;
	
	/**
	 *  Base path of the file to load.
	 */
	private static String fileBasePath;
	
	/**
	 * Indicates if file has been modified since last getInstance() calling.
	 */
	private static boolean modified;
	
	/**
	 * Numbers of category profiles declared in the xml file.
	 */
	private static int nbProfiles;
	
	/**
	 * Numbers of contexts declared in the xml file.
	 */
	private static int nbContexts;

	private static int xmlFileTimeOut;
	
	/*
	 ************************** INIT *********************************/	

	/**
	 * Private Constructor .
	 * @throws ChannelConfigException 
	 */
	private ChannelConfig() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ChannelConfig()");
		}
	}

	/*
	 *********************** METHODS *****************************************/
	
	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @param configFilePath file path of the channel config
	 * @param defaultTtl 
	 * @return an instance of the file to load (singleton)
	 * @throws ChannelConfigException 
	 * @see ChannelConfig#singleton
	 */
	protected static ChannelConfig getInstance(final String configFilePath, final int defaultTimeOut) throws ChannelConfigException {
		filePath = configFilePath;
		xmlFileTimeOut = defaultTimeOut;
		return getInstance();
		
	}
	
	/**
	 * Return a singleton of this class used to load ChannelConfig file.
	 * @return an instance of the file to load (singleton)
	 * @throws ChannelConfigException 
	 * @see ChannelConfig#singleton
	 */
	protected static synchronized ChannelConfig getInstance() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getInstance()");
		}

		if (singleton == null) {
			singleton = new ChannelConfig();
		}
		return singleton;
	}
	
	protected static synchronized void getConfigFile() throws ChannelConfigException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getConfigFile()");
		}
	
		if (filePath == null) {
			String errorMsg = "Config file path not defined, see in domain.xml file.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		
		URL url = ChannelConfig.class.getResource(filePath);
		if (url == null) {
			String errorMsg = "Config file: " + filePath + " not found.";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		}
		File file = new File(url.getFile());
		fileBasePath = file.getAbsolutePath();
		Document xmlFileLoading = null;
		
		xmlFileLoading = getFreshConfigFile(fileBasePath);
		
		xmlFileLoading = checkConfigFile(xmlFileLoading);
		if (xmlFileLoading == null) {
			String errorMsg = "Impossible to load XML Channel config (esup-lecture.xml)";
			LOG.error(errorMsg);
			throw new ChannelConfigException(errorMsg);
		} else {
			xmlFile = xmlFileLoading;
		}
	}
	/**
	 * Check syntax file that cannot be checked by DTD.
	 * @throws ChannelConfigException 
	 */
	private synchronized static Document checkConfigFile(Document xmlFileChecked) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("checkXmlFile()");
		}
		// Merge categoryProfilesUrl and check number of contexts + categories
		Document xmlFileLoading = getFreshConfigFile(fileBasePath);
		Element root = xmlFileLoading.getRootElement();
		Element channelConfig = root.element("channelConfig");
		List<Node> contexts = channelConfig.selectNodes("/context");
		nbContexts = contexts.size();
		if (nbContexts == 0) {
			LOG.warn("No context declared in channel config (esup-lecture.xml)");
		}
		
		// 1. merge categoryProfilesUrls
		for (Node context : contexts) {
			List<Node> categoryProfilesUrls = context.selectNodes("/categoryProfilesUrl");
			for (Node categoryProfilesUrl : categoryProfilesUrls) {
				URL url = ChannelConfig.class.getResource(categoryProfilesUrl.getStringValue());
				if (url == null) {
					String errorMsg = "URL of : categoryProfilesUrl " + filePath + " not found.";
					LOG.warn(errorMsg);
				} else {
					File file = new File(url.getFile());
					String categoryProfilesUrlPath = file.getAbsolutePath();
					Document categoryProfilesFile = getFreshConfigFile(categoryProfilesUrlPath);
					if (categoryProfilesFile == null) {
						String errorMsg = "Impossible to load categoryProfilesUrl " + categoryProfilesUrlPath;
						LOG.warn(errorMsg);
					} else {
						// merge one categoryProfilesUrl
						// add categoryProfile
						Element rootCategoryProfilesFile = categoryProfilesFile.getRootElement();
						channelConfig.add(rootCategoryProfilesFile);
					}
				}
				
			}
		}
		for (Node context : contexts) {
			List<Node> categoryProfilesUrls = context.selectNodes("/categoryProfilesUrl");
			for (Node categoryProfilesUrl : categoryProfilesUrls) {
				// delete node categoryProfilesUrl
			}
		}
		List<Node> categoryProfiles = root.selectNodes("/categoryProfile");
		nbProfiles = categoryProfiles.size();
		if (nbProfiles == 0) {
			LOG.warn("checkXmlConfig :: No managed category profile declared in channel config");
		}
		return xmlFileChecked;
		
	}

	/**
	 * @param configFilePath 
	 * @return
	 */
	protected synchronized static Document getFreshConfigFile(String configFilePath) {
		// Assign null to configFileLoaded during the loading
		Document ret = null;
		// Launch thread
		FreshXmlFileThread thread = new FreshXmlFileThread(configFilePath);
		
		int timeout = 0;
		try {
			thread.start();
			timeout = xmlFileTimeOut;
			thread.join(timeout);
			Exception e = thread.getException();
			if (e != null) {
				String msg = "Thread getting Source launches XMLParseException";
				LOG.warn(msg);
				throw new XMLParseException(msg, e);
			}
	        if (thread.isAlive()) {
	    		thread.interrupt();
				String msg = "configFile not loaded in " + timeout + " milliseconds";
				LOG.warn(msg);
	        }	
			ret = thread.getXmlFile();
		} catch (InterruptedException e) {
			String msg = "Thread getting ConfigFile interrupted";
			LOG.warn(msg);
			ret = null;
		} catch (IllegalThreadStateException e) {
			String msg = "Thread getting ConfigFile launches IllegalThreadStateException";
			LOG.warn(msg);
			ret = null;
		} catch (XMLParseException e) {
			String msg = "Thread getting Source launches XMLParseException";
			LOG.warn(msg);
			ret = null;
		} finally {
			return ret;
		}
	}

	/**
	 * Load attribute that identified guest user name (guestUser).
	 */
	protected static synchronized void loadGuestUser() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadGuestUser()");
		}
		Element root = xmlFile.getRootElement();
		String guestUser = root.valueOf("/channelConfig/guestUser");
		if (guestUser == null || guestUser.equals("")) {
			guestUser = "guest";
		}
		DomainTools.setGuestUser(guestUser);
	}

	/**
	 * Load the ttl of the config file.
	 */
	protected static synchronized void loadConfigTtl() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadGuestUser()");
		}
		//int configTtl = xmlFile.getInt("ttl", DomainTools.getConfigTtl());
		Element root = xmlFile.getRootElement();
		String configTtl = root.valueOf("/channelConfig/ttl");
		DomainTools.setConfigTtl(Integer.parseInt(configTtl));
	}

	/**
	 * Load a DefinitionSets that is used to define visibility groups of a managed category profile.
	 * @param fatherName name of the father XML element refered to (which visibility group)
	 * @param pathCategoryProfile index of the XML element category profile
	 * @return the initialized DefinitionSets
	 */
	private static synchronized DefinitionSets loadDefAndContentSets(final String fatherName, final Node categoryProfile) {
		DefinitionSets defAndContentSets = new DefinitionSets();
		// pathCategoryProfile = "categoryProfile(" + j + ")";
		// String fatherPath = pathCategoryProfile + ".visibility." + fatherName;

		String fatherPath = "/visibility/" + fatherName + "/group";
		if (LOG.isDebugEnabled()) {
			LOG.debug("loadDefAndContentSets(" + fatherName + "," + categoryProfile.valueOf("@id") + ")");
		}
		
		List<Node> groups = categoryProfile.selectNodes(fatherPath);
		for (Node group : groups) {
			defAndContentSets.addGroup(group.valueOf("@name"));
		}

		List<Node> regulars = categoryProfile.selectNodes(fatherPath);
		for (Node regular : regulars) {
   			RegularOfSet regularOfSet = new RegularOfSet();
   			regularOfSet.setAttribute(regular.valueOf("@attribute]"));
   			regularOfSet.setAttribute(regular.valueOf("@value]"));
   			defAndContentSets.addRegular(regularOfSet);
		}

/*		int nbGroups = xmlFile.getMaxIndex(fatherPath + ".group") + 1;
		for (int i = 0; i < nbGroups; i++) {
			defAndContentSets.addGroup(xmlFile.getString(fatherPath + ".group(" + i + ")[@name]"));
		}
   		// Definition by regular 
   		int nbRegulars = xmlFile.getMaxIndex(fatherPath + ".regular") + 1;   	
   		for (int i = 0; i < nbRegulars; i++) {
   			//RegularOfSet regular = new RegularOfSet();
   			regular.setAttribute(xmlFile.getString(fatherPath + ".regular(" + i + ")[@attribute]"));
   			regular.setValue(xmlFile.getString(fatherPath + ".regular(" + i + ")[@value]"));	
   			defAndContentSets.addRegular(regular);
   		}
*/
   		return defAndContentSets;
	}
	
	
	
	/*
	 *********************** ACCESSORS *****************************************/

	/**
	 * Returns the relative classpath file path of the channel config.
	 * @return configFilePath
	 * @see ChannelConfig#filePath
	 */
	protected static String getfilePath() {
		return filePath;
	}

	/**
	 * Set the relative classpath file path of the channel config.
	 * @param filePath
	 * @see ChannelConfig#filePath
	 */
	protected static synchronized void setfilePath(final String filePath) {
		// TODO (GB later) sera utilisé lorsque le file sera externalisé
		LOG.debug("setFilePath(" + filePath + ")");
		ChannelConfig.filePath = filePath;
	}
	/**
	 * @return true if the channel config file has been modified since last "getInstance"
	 */
	protected static boolean isModified() {
		return modified;
	}

	/**
	 * @param channel
	 */
	public static void loadContextsAndCategoryprofiles(final Channel channel) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("loadContextsAndCategoryprofiles()");
    	}
		//nbContexts = xmlFile.getMaxIndex("context") + 1;
		String pathCategoryProfile = "categoryProfile(" + 0 + ")";
		String categoryProfileId = "";
		Element root = xmlFile.getRootElement();
		Node channelConfig = root.selectSingleNode("/channelConfig");
		List<Node> contexts = channelConfig.selectNodes("/context");
		for (Node context : contexts) {
			Context c = new Context();
			c.setId(context.valueOf("@id"));
			c.setName(context.valueOf("@name"));
			c.setTreeVisible(getBoolean(context.valueOf("@treeVisible"), true));
			
	    	if (LOG.isDebugEnabled()) {
	    		LOG.debug("loadContextsAndCategoryprofiles() : contextId " + c.getId());
	    	}
	    	Node description = context.selectSingleNode("/description");
			c.setDescription(description.getStringValue());
			List<Node> refCategoryProfiles = context.selectNodes("/refCategoryProfile");
			
			// Lire les refCategoryProfilesUrl puis :
			// - les transformer en refCategoryProfile ds le context
			// - ajouter les categoryProfile
			// A faire dans checkXmlFile ?
			
			Map<String, Integer> orderedCategoryIDs = 
				Collections.synchronizedMap(new HashMap<String, Integer>());
			int xmlOrder = 1;
			
			// On parcours les refCategoryProfile de context
			for (Node refCategoryProfile : refCategoryProfiles) {
				String refId;
				// Ajout mcp
				boolean profileFound = false;
				refId = refCategoryProfile.valueOf("@refId");
		    	if (LOG.isDebugEnabled()) {
		    		LOG.debug("loadContextsAndCategoryprofiles() : refCategoryProfileId " + refId );
		    	}
				List<Node> categoryProfiles = channelConfig.selectNodes("/categoryProfile");
				// On parcours les categoryProfile de root
				for (Node categoryProfile : categoryProfiles) {
//					pathCategoryProfile = "categoryProfile(" + j + ")";
					categoryProfileId = categoryProfile.valueOf("@id");
			    	if (LOG.isDebugEnabled()) {
			    		LOG.debug("loadContextsAndCategoryprofiles() : is categoryProfileId " + categoryProfileId + " matching ?");
			    	}
					if (categoryProfileId.compareTo(refId) == 0) {
						profileFound = true;
				    	if (LOG.isDebugEnabled()) {
				    		LOG.debug("loadContextsAndCategoryprofiles() : categoryProfileId " + refId + " matches... create mcp");
				    	}
						ManagedCategoryProfile mcp = new ManagedCategoryProfile();
						// Id = long Id
						String mcpProfileID = categoryProfileId;
						mcp.setFileId(c.getId(), mcpProfileID);
				    	if (LOG.isDebugEnabled()) {
				    		LOG.debug("loadContextsAndCategoryprofiles() : categoryProfileId " + mcp.getId() + " matches... create mcp");
				    	}
		
						mcp.setName(categoryProfile.valueOf("@name"));
						mcp.setCategoryURL(categoryProfile.valueOf("@urlCategory"));
						mcp.setTrustCategory(getBoolean(categoryProfile.valueOf("@trustCategory"), false));
						mcp.setUserCanMarkRead(getBoolean(categoryProfile.valueOf("@userCanMarkRead"), true));

						String ttl = categoryProfile.valueOf("@ttl");
						mcp.setTtl(Integer.parseInt(ttl));
						String timeout = categoryProfile.valueOf("@timeout");
						mcp.setTimeOut(Integer.parseInt(timeout));
						mcp.setName(categoryProfile.valueOf("@name"));
						
					    // Accessibility
					    String access = categoryProfile.valueOf("@access");
					    if (access.equalsIgnoreCase("public")) {
							mcp.setAccess(Accessibility.PUBLIC);
						} else if (access.equalsIgnoreCase("cas")) {
							mcp.setAccess(Accessibility.CAS);
						}
					    // Visibility
					    VisibilitySets visibilitySets = new VisibilitySets();  
					    // foreach (allowed / autoSubscribed / Obliged
					    visibilitySets.setAllowed(loadDefAndContentSets("allowed", categoryProfile));
					    visibilitySets.setAutoSubscribed(loadDefAndContentSets("autoSubscribed", categoryProfile));
					   	visibilitySets.setObliged(loadDefAndContentSets("obliged", categoryProfile));
					    mcp.setVisibility(visibilitySets);
					    
					    channel.addManagedCategoryProfile(mcp);    
						c.addRefIdManagedCategoryProfile(mcp.getId());
						orderedCategoryIDs.put(mcp.getId(), xmlOrder);
				    	
						break;
					}
				}
				xmlOrder += 1;				
			}
			c.setOrderedCategoryIDs(orderedCategoryIDs);
			channel.addContext(c);
		}
    }

	/**
	 * @param valueOf
	 * @param b
	 * @return
	 */
	private static boolean getBoolean(String valueOf, boolean b) {
		if (valueOf != null) {
			if (valueOf.equalsIgnoreCase("true")) {
				return true;
			} else if (valueOf.equalsIgnoreCase("false")) {
				return false;
			} else {
				return b;
			}
		}
		return b;
	}    
 		

}
