<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<f:subview id="rightSubview">
		<!-- MENU with Source name, sort list and zoom -->
		<t:htmlTag value="p" styleClass="portlet-section-header  !!">
			<h:outputText value="#{editController.selectionTitle}" />
		</t:htmlTag>
		<!-- Sources display -->
		<t:htmlTag value="ul">
			<t:dataList value="#{editController.selectedCat.sources}" var="src"
				layout="simple">
				<t:htmlTag value="li" styleClass="edit">
					<h:commandButton action="#{editController.toogleSourceSubcribtion}"
						image="/media/subscribe.png" alt="#{msgs['subscribeSource']}"
						title="#{msgs['subscribeSource']}" rendered="#{src.notSubscribed}">
						<t:updateActionListener property="#{editController.sourceId}"
							value="#{src.id}" />
					</h:commandButton>
					<h:commandButton action="#{editController.toogleSourceSubcribtion}"
						image="/media/unsubscribe.png" alt="#{msgs['unsubscribeSource']}"
						title="#{msgs['unsubscribeSource']}" rendered="#{src.subscribed}">
						<t:updateActionListener property="#{editController.sourceId}"
							value="#{src.id}" />
					</h:commandButton>
					<h:graphicImage value="/media/forced.png"
						alt="#{msgs['forcedSource']}" title="#{msgs['forcedSource']}"
						rendered="#{src.obliged}" />
					<h:outputText value="#{src.name}" />
				</t:htmlTag>
			</t:dataList>
		</t:htmlTag>
	</f:subview>
</jsp:root>
