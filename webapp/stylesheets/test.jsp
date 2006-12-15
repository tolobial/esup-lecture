<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:e="http://commons.esup-portail.org">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<e:page stringsVar="msgs" menuItem="welcome"
		locale="#{testController.locale}">
		<t:stylesheet path="/media/lecture.css" media="screen"/>
		<h:form id="home">	
			<t:htmlTag value="h1">
				<h:outputText value="INIT"/>
			</t:htmlTag>
			<h:commandButton value="delete object tree" action="#{testController.delete}"/>
			<t:htmlTag value="br"/>
			<h:commandButton value="create object tree" action="#{testController.create}"/>
			<t:htmlTag value="h1">
				<h:outputText value="TESTS"/>
			</t:htmlTag>
			<h:commandButton value="get object tree" action="#{testController.test1}"/>
			<t:htmlTag value="br"/>
			<h:commandButton value="read save read" action="#{testController.test2}"/>
		</h:form>
	</e:page>
</jsp:root>

