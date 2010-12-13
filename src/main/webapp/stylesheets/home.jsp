<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:e="http://commons.esup-portail.org"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:jdt="http://www.jenia.org/jsf/dataTools">
	<ui:composition template="/stylesheets/template.jspx">
		<ui:define name="content">
			<e:form id="home" showSubmitPopupText="false"
				showSubmitPopupImage="false">
				<!-- ********* Rendering ********* -->
				<t:div id="panels-layout" forceId="true"
					rendered="#{homeController.treeVisible and !homeController.guestMode}">
					<t:div id="panelLeft-ui" styleClass="ui-layout-west" forceId="true"
						style="width: #{homeController.treeSize}%">
						<jsp:include page="homeLeft.jsp" />
					</t:div>
					<t:div id="panelRight-ui" styleClass="ui-layout-center"
						forceId="true" style="width: #{99-homeController.treeSize}%">
						<jsp:include page="homeRight.jsp" />
					</t:div>
				</t:div>
				<t:div id="onePanel" forceId="true"
					rendered="#{!homeController.treeVisible or homeController.guestMode}">
					<t:div id="panelRight" forceId="true">
						<jsp:include page="homeRight.jsp" />
					</t:div>
				</t:div>
			</e:form>
			<script type="text/javascript">
			if (document.getElementById("home:itemDisplayModeButton")) {
				document.getElementById("home:itemDisplayModeButton").style.display = "none";
			}
        </script>
		</ui:define>
	</ui:composition>
</jsp:root>

