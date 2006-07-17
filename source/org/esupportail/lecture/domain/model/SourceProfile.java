package org.esupportail.lecture.domain.model;

public abstract class SourceProfile {

/* ************************** PROPERTIES ******************************** */	

	private String id;

	private String name = "";

	private String sourceURL = "";



/* ************************** METHODS ******************************** */	

/* ************************** ACCESSORS ******************************** */	

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

	public String getSourceURL() {
		return sourceURL;
	}
	
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

}
