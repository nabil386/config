<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.

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

<xsl:import href="EmploymentCommon.xslt"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template name="EmploymentReadUI">

  <xsl:param name="prefix" />
  <xsl:param name="path" />
  <xsl:param name="UIName" />

  <xsl:variable name="modifyEmploymentUI"><xsl:value-of select="$prefix"/>Evidence<xsl:value-of select="$employmentModifyUIName"/></xsl:variable>
  <xsl:variable name="cancelEmploymentUI"><xsl:value-of select="$prefix"/>Evidence<xsl:value-of select="$employmentCancelUIName"/></xsl:variable>
  <xsl:variable name="listEmploymentUI"><xsl:value-of select="$prefix"/>_listCoreEmploymentEvidenceDetails</xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>
  
  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE
  PAGE_ID="{$UIName}"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>


<PAGE_PARAMETER NAME="employmentID"/>
<PAGE_PARAMETER NAME="caseID"/>
<PAGE_PARAMETER NAME="relEvidenceID"/>
<PAGE_PARAMETER NAME="relEvidenceType"/>
<PAGE_PARAMETER NAME="evidenceType"/>
<PAGE_PARAMETER NAME="contextDescription"/>
<PAGE_PARAMETER NAME="caseParticipantRoleID"/>

<ACTION_SET ALIGNMENT="CENTER">

  <ACTION_CONTROL LABEL="ActionControl.Label.Edit" IMAGE="EditButton">
    <LINK SAVE_LINK="false" PAGE_ID="{$modifyEmploymentUI}" DISMISS_MODAL="false">
      <CONNECT>
        <SOURCE PROPERTY="employmentID" NAME="PAGE"/>
        <TARGET PROPERTY="employmentID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="caseID" NAME="PAGE"/>
        <TARGET PROPERTY="caseID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="relEvidenceID" NAME="PAGE"/>
        <TARGET PROPERTY="relEvidenceID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="relEvidenceType" NAME="PAGE"/>
        <TARGET PROPERTY="relEvidenceType" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
        <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="contextDescription" NAME="PAGE"/>
        <TARGET PROPERTY="contextDescription" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="caseParticipantRoleID" NAME="PAGE"/>
        <TARGET PROPERTY="caseParticipantRoleID" NAME="PAGE"/>
      </CONNECT>      
    </LINK>
  </ACTION_CONTROL>

  <ACTION_CONTROL LABEL="ActionControl.Label.Delete" IMAGE="DeleteButton">
    <LINK SAVE_LINK="false" PAGE_ID="{$cancelEmploymentUI}" DISMISS_MODAL="false">
      <CONNECT>
        <SOURCE PROPERTY="employmentID" NAME="PAGE"/>
        <TARGET PROPERTY="employmentID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="versionNo" NAME="DISPLAY"/>
        <TARGET PROPERTY="versionNo" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="caseID" NAME="PAGE"/>
        <TARGET PROPERTY="caseID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="relEvidenceID" NAME="PAGE"/>
        <TARGET PROPERTY="relEvidenceID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="relEvidenceType" NAME="PAGE"/>
        <TARGET PROPERTY="relEvidenceType" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
        <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="contextDescription" NAME="PAGE"/>
        <TARGET PROPERTY="contextDescription" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="caseParticipantRoleID" NAME="PAGE"/>
        <TARGET PROPERTY="caseParticipantRoleID" NAME="PAGE"/>
      </CONNECT>      
    </LINK>
  </ACTION_CONTROL>

  <ACTION_CONTROL LABEL="ActionControl.Label.Close" IMAGE="CloseButton">
    <LINK PAGE_ID="{$listEmploymentUI}" DISMISS_MODAL="false">
      <CONNECT>
        <SOURCE PROPERTY="caseID" NAME="PAGE"/>
        <TARGET PROPERTY="caseID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="relEvidenceID" NAME="PAGE"/>
        <TARGET PROPERTY="relEvidenceID" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="relEvidenceType" NAME="PAGE"/>
        <TARGET PROPERTY="relEvidenceType" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
        <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="contextDescription" NAME="PAGE"/>
        <TARGET PROPERTY="contextDescription" NAME="PAGE"/>
      </CONNECT>
      <CONNECT>
        <SOURCE PROPERTY="caseParticipantRoleID" NAME="PAGE"/>
        <TARGET PROPERTY="caseParticipantRoleID" NAME="PAGE"/>
      </CONNECT>      
    </LINK>
  </ACTION_CONTROL>
  
</ACTION_SET>

<INCLUDE FILE_NAME="Person_viewEmploymentView.vim"/>

</PAGE>
  
  </redirect:write>

  <xsl:call-template name="write-all-locales-EmploymentReadUI-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="fullFileName" select="$filepath"/>
  </xsl:call-template>

</xsl:template>

<!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-EmploymentReadUI-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-EmploymentReadUI-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-EmploymentReadUI-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-EmploymentReadUI-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-EmploymentReadUI-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-EmploymentReadUI-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    
	<xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/*)&gt;0">
    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    
      <redirect:write select="concat($fullFileName, $locale, '.properties')">

    <xsl:variable name="employmentProperties" select="//EvidenceEntities/Properties[@locale=$locale]/Employment"/>
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$employmentProperties/Help.ViewEmployment.PageDescription"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$employmentProperties/Page.Title.View.Employment "/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  <xsl:with-param name="altPropertyName">PageTitle.StaticText1</xsl:with-param>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$employmentProperties/ActionControl.Label.Delete "/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template> 

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$employmentProperties/ActionControl.Label.Edit"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template> 

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Close"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template> 

  </redirect:write>
	
  </xsl:if>

</xsl:template>

</xsl:stylesheet>