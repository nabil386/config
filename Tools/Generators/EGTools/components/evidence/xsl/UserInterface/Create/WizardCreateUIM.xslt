<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2010, 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2010-2011 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"

>

<!-- Global Variables -->
<xsl:import href="../UICommon.xslt"/>

<!-- Create specific templates -->
<xsl:import href="CreateUtilityTemplates.xslt"/>

<xsl:output method="xml" indent="yes"/>

  <!--
    Template to write a wizard create page for an entity.

    @param entityElement XML Element containing the entity
    @param caseType The case type being generated
    @param baseDirectory Base Directory to generate to
    @param fileName The name of the file to generate (minus the file extension)
    @param includeVIMFileName The name of the VIM file to include
    @param wizardSelectUIMName The name of the page for the back button to link to.
    @param relatedEntityName The name of the related entity
  -->
  <xsl:template name="WizardCreateUIM">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="fileName"/>
    <xsl:param name="includeVIMFileName"/>
    <xsl:param name="wizardSelectUIMName"/>
    <xsl:param name="wizardEndDateUIMName" />
    <xsl:param name="relatedEntityName"/>
    <xsl:param name="wizardName"/>
    <xsl:param name="endDateWizard" />
	<xsl:param name="baseAggregation" />

    <!-- Name of the entity -->
    <xsl:variable name="entityName" select="$entityElement/@name"/>

    <!-- Full name of the file -->
    <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>

    <redirect:write select="concat($fullFileName, '.uim')">

      <!-- add copyright notice -->
      <xsl:call-template name="printXMLCopyright">
        <xsl:with-param name="date" select="$date"/>
      </xsl:call-template>

      <PAGE
        PAGE_ID="{$fileName}"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
        WINDOW_OPTIONS="height=600,width=900"
        >
        <PAGE_TITLE>
          <CONNECT>
            <SOURCE
              NAME="TEXT"
              PROPERTY="Page.Title"
            />
          </CONNECT>
        </PAGE_TITLE>

        <!-- Wizards are named after the starting page for them -->
        <MENU MODE="WIZARD_PROGRESS_BAR">
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="{$wizardName}.wizardMenu"
            />
          </CONNECT>
        </MENU>

        <INCLUDE FILE_NAME="{$includeVIMFileName}.vim"/>
        
		<xsl:if test="$endDateWizard = 'true' and 
				$entityElement/UserInterfaceLayer/@saveAndNewButton = 'Yes'">
		<PAGE_PARAMETER NAME="evidenceID"/>
		<PAGE_PARAMETER NAME="evidenceIDTabList"/>
		<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
		<PAGE_PARAMETER NAME="participantID"/>
		<PAGE_PARAMETER NAME="participantIDTabList"/>
		</xsl:if>
		<JSP_SCRIPTLET>
			String context = request.getContextPath() + "/";
      		context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/";
			String evidenceID = request.getParameter("evidenceID");
			String evidenceIDTabList = request.getParameter("evidenceIDTabList");
			String caseID = request.getParameter("caseID");
			String evidenceType = request.getParameter("evidenceType");
			String contextDescription = request.getParameter("contextDescription");
			String url = context + "<xsl:value-of select="$wizardName"/>Page.do?caseID=" + curam.omega3.request.RequestUtils.escapeURL(caseID) 
			+ "&amp;evidenceType=" + curam.omega3.request.RequestUtils.escapeURL(evidenceType);
			if(evidenceID != null) {
				if(evidenceIDTabList.equals("")) {
					evidenceIDTabList = evidenceID;
				} else {
					evidenceIDTabList += "\t" + evidenceID;
				}
				
				url += "&amp;evidenceIDTabList=" + curam.omega3.request.RequestUtils.escapeURL(evidenceIDTabList);
				
				<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
				String participantID = request.getParameter("participantID");
				String participantIDTabList = request.getParameter("participantIDTabList");
				if(participantID != null) {
					if(participantIDTabList.equals("")) {
						participantIDTabList = participantID;
					} else {
						participantIDTabList += "\t" + participantID;
					}
				}
				
				url += "&amp;participantIDTabList=" + curam.omega3.request.RequestUtils.escapeURL(participantIDTabList);
				</xsl:if>
				
				curam.omega3.request.RequestHandler rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);

				url += "&amp;contextDescription=" + curam.omega3.request.RequestUtils.escapeURL(contextDescription) + "&amp;" + rh.getSystemParameters();

				response.sendRedirect(response.encodeRedirectURL(url));
			}
		</JSP_SCRIPTLET>
		</xsl:if>
		
        <ACTION_SET
          ALIGNMENT="RIGHT"
          >

          <ACTION_CONTROL
            IMAGE="CancelButton"
            LABEL="ActionControl.Label.Cancel"
            ALIGNMENT="LEFT"
          />

		<!-- BEGIN, 191675, JAY -->
		<!-- If end date wizard enabled then link end date page to "Next" and -->
		<xsl:choose>
			<xsl:when test="$endDateWizard = 'true'">
				<ACTION_CONTROL IMAGE="SaveAndExitButton" LABEL="ActionControl.Label.SaveAndExit"
						TYPE="SUBMIT" />
				<xsl:if
						test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
					<ACTION_CONTROL IMAGE="SaveAndNewButton"
						LABEL="ActionControl.Label.SaveAndNew" TYPE="SUBMIT">
						<xsl:call-template name="linkToPage">
							<xsl:with-param name="pageUIMName" select="$wizardName" />
							<xsl:with-param name="endDateWizard" select="$endDateWizard" />
							<xsl:with-param name="entityElement" select="$entityElement" />
							<xsl:with-param name="baseAggregation" select="$baseAggregation" />
						</xsl:call-template>
					</ACTION_CONTROL>
				</xsl:if>
				<ACTION_CONTROL IMAGE="SaveAndNextButton"
					LABEL="ActionControl.Label.SaveAndNext" TYPE="SUBMIT">
					<xsl:call-template name="linkToPage">
						<xsl:with-param name="pageUIMName" select="$wizardEndDateUIMName" />
						<xsl:with-param name="endDateWizard" select="$endDateWizard" />
						<xsl:with-param name="entityElement" select="$entityElement" />
						<xsl:with-param name="baseAggregation" select="$baseAggregation" />
					</xsl:call-template>
				</ACTION_CONTROL>
			</xsl:when>
			<!-- END, 191675, JAY -->
			<xsl:otherwise>		  
          <ACTION_CONTROL
            IMAGE="BackButton"
            LABEL="ActionControl.Label.Back"
          >
            <xsl:call-template name="linkToPage">
              <xsl:with-param name="pageUIMName" select="$wizardSelectUIMName"/>
              <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
              <xsl:with-param name="entityElement" select="$entityElement"/>
            </xsl:call-template>
          </ACTION_CONTROL>

          <!-- BEGIN, CR00266438, CD -->
          <xsl:if test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
          <ACTION_CONTROL
            IMAGE="SaveAndNewButton"
            LABEL="ActionControl.Label.SaveAndNew"
            TYPE="SUBMIT"
          >
            <xsl:call-template name="linkToPage">
              <xsl:with-param name="pageUIMName" select="$wizardSelectUIMName"/>
              <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
              <xsl:with-param name="entityElement" select="$entityElement"/>
            </xsl:call-template>
          </ACTION_CONTROL>
          </xsl:if>
          <!-- END, CR00266438, CD -->

          <ACTION_CONTROL
            IMAGE="FinishButton"
            LABEL="ActionControl.Label.Finish"
            TYPE="SUBMIT"
          />
          </xsl:otherwise>
          </xsl:choose>
        </ACTION_SET>

      </PAGE>

    </redirect:write>

    <xsl:call-template name="write-all-locales-wizardcreateuim-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
    </xsl:call-template>

  </xsl:template>

  <xsl:template name="write-all-locales-wizardcreateuim-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!-- tokens still exist -->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-wizardcreateuim-properties">
            <xsl:with-param name="locale"
              select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName"
              select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-wizardcreateuim-properties">
            <xsl:with-param name="locales"
              select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName"
              select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-wizardcreateuim-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName"
              select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-wizardcreateuim-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>


  <!-- BEGIN, CR00266438, CD -->
  <xsl:template name="linkToPage">
    <xsl:param name="pageUIMName"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:param name="entityElement"/>
    <xsl:param name="endDateWizard" />
	<xsl:param name="baseAggregation" />
    <LINK PAGE_ID="{$pageUIMName}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
      <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="caseID"/>
        <TARGET NAME="PAGE" PROPERTY="caseID"/>
      </CONNECT>
      <xsl:if test="$relatedEntityName!=''">
      <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="fromRelated" select="$relatedEntityName"/>
      </xsl:call-template></xsl:variable>
      <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="fromRelated" select="$relatedEntityName"/>
      </xsl:call-template></xsl:variable>
      <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
        <TARGET NAME="PAGE" PROPERTY="{$idParamName}"/>
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
        <TARGET NAME="PAGE" PROPERTY="{$typeParamName}"/>
      </CONNECT>
      </xsl:if>
      <!-- BEGIN, 191675, JAY -->
      <!-- Link to End Date wizard page from Create evidence page -->
			<xsl:if test="$endDateWizard = 'true'">
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="evidenceType" />
					<TARGET NAME="PAGE" PROPERTY="evidenceType" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="ACTION" PROPERTY="result$evidenceID"/>
					<TARGET NAME="PAGE" PROPERTY="evidenceID"/>
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="ACTION" PROPERTY="result$startDateStr"/>
					<TARGET NAME="PAGE" PROPERTY="startDateStr"/>
				</CONNECT>
				<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
					<CONNECT>
						<SOURCE NAME="ACTION" PROPERTY="result$participantID" />
						<TARGET NAME="PAGE" PROPERTY="participantID" />
					</CONNECT>
				</xsl:if>
				<xsl:if test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
					<CONNECT>
						<SOURCE NAME="PAGE" PROPERTY="evidenceIDTabList" />
						<TARGET NAME="PAGE" PROPERTY="evidenceIDTabList" />
					</CONNECT>
					<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
					<CONNECT>
						<SOURCE NAME="PAGE" PROPERTY="participantIDTabList" />
						<TARGET NAME="PAGE" PROPERTY="participantIDTabList" />
					</CONNECT>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		<!-- END, 191675, JAY -->
    </LINK>
  </xsl:template>
  <!-- END, CR00266438, CD -->

  <xsl:template name="write-wizardcreateuim-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

      <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <redirect:write select="concat($fullFileName, $locale, '.properties')">
        <xsl:if test="count($generalProperties/Help.PageDescription.CreateEntity)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.CreateEntity"/>
            <xsl:with-param name="evidenceNode" select="$entityElement"/>
	        <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
          </xsl:call-template>
        </xsl:if>
        <xsl:if test="count($generalProperties/Page.Title.NewEntity)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.NewEntity"/>
            <xsl:with-param name="evidenceNode" select="$entityElement"/>
	        <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
          </xsl:call-template>
          <xsl:text>&#xa;</xsl:text>
        </xsl:if>

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Finish"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>

        <!-- BEGIN, CR00266438, CD -->
        <xsl:if test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndNew"/>
            <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
          </xsl:call-template>
        </xsl:if>
        <!-- END, CR00266438, CD -->

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Back"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
		<!-- BEGIN, 191675, JAY -->
		<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/ActionControl.Label.Next" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>
	
		<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/ActionControl.Label.Save" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/ActionControl.Label.SaveAndNext" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>
				
		<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/ActionControl.Label.SaveAndExit" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>
		<!-- END, 191675, JAY -->

      </redirect:write>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
