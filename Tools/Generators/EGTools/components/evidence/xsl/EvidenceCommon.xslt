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

  <xsl:variable name="typeCaseParticipantRoleID">CASE_PARTICIPANT_ROLE_ID</xsl:variable>    
  <xsl:variable name="typeAlternateID">ALTERNATE_ID</xsl:variable>    
  <xsl:variable name="typeAddressID">ADDRESS_ID</xsl:variable>    
  <xsl:variable name="typeAmount">CURAM_AMOUNT</xsl:variable>
  <xsl:variable name="typeIndicator">CURAM_INDICATOR</xsl:variable>    
  <xsl:variable name="typeSSSNID">SSNID</xsl:variable>   
  <xsl:variable name="typeDriversLicenseID">DRIVERSLICENSEID</xsl:variable>   
  
  
  <xsl:variable name="metatypeParentCaseParticipant">PARENT_CASE_PARTICIPANT_ROLE_ID</xsl:variable>
  <xsl:variable name="metatypeEmployerCaseParticipant">EMPLOYER_CASE_PARTICIPANT_ROLE_ID</xsl:variable>
  <xsl:variable name="metatypeCaseParticipantSearch">CASE_PARTICIPANT_SEARCH</xsl:variable>
  <xsl:variable name="metatypeCodetable">CODETABLE_CODE</xsl:variable>
  <xsl:variable name="metatypeAlternateIDSearch">ALTERNATE_ID_SEARCH</xsl:variable>     
  <xsl:variable name="metatypeRelatedEntityID">RELATED_ENTITY_ID</xsl:variable>
  <xsl:variable name="metatypeRelatedEntityAttribute">RELATED_ENTITY_ATTRIBUTE</xsl:variable>
  <xsl:variable name="metatypeAssociationID">ASSOCIATION_ID</xsl:variable>    
  <xsl:variable name="metatypeRepresentativeLink">REPRESENTATIVE_LINK</xsl:variable>    
  <xsl:variable name="metatypeDisplayCaseParticipant">DISPLAY_CASE_PARTICIPANT</xsl:variable>    
  <xsl:variable name="metatypeAssocStartDate">ASSOC_START_DATE</xsl:variable>    
  <xsl:variable name="metatypeAssocEndDate">ASSOC_END_DATE</xsl:variable>    
  
  <xsl:variable name="metatypeStartDate">START_DATE</xsl:variable>
  <xsl:variable name="metatypeEndDate">END_DATE</xsl:variable>
  <xsl:variable name="metatypeCuramDate">CURAM_DATE</xsl:variable>
  <xsl:variable name="metatypeComments">COMMENTS</xsl:variable>
  
  <xsl:variable name="employmentEntityName">Employment</xsl:variable>
  
  <xsl:variable name="evidenceTypeSummary">summary</xsl:variable>    

  <xsl:variable name="ParticipantTypePerson">Person</xsl:variable>
  <xsl:variable name="ParticipantTypeCoreEmployer">CoreEmployer</xsl:variable>
  <xsl:variable name="ParticipantTypeEmployer">Employer</xsl:variable>
  <xsl:variable name="ParticipantTypeServiceProvider">ServiceProvider</xsl:variable>
  <xsl:variable name="ParticipantTypeUnion">Union</xsl:variable>
  <xsl:variable name="ParticipantTypeUnknown">Unknown</xsl:variable>
  
  <!-- For SiteMap building  -->
  <xsl:variable name="SiteMapIntegratedCase">IntegratedCase</xsl:variable>
  <xsl:variable name="SiteMapProductDelivery">ProductDelivery</xsl:variable>
  <xsl:variable name="SiteMapIntegratedCaseListName">Evidence</xsl:variable>
  <xsl:variable name="SiteMapProductDeliveryListName">ProductEvidence</xsl:variable>
  <xsl:variable name="SiteMapInterstate">ProductDeliveryInterState</xsl:variable>


  <!--                 Event                         -->
  <xsl:variable name="eventTypeActivate">Activate</xsl:variable>
  <xsl:variable name="eventTypeModify">Modify</xsl:variable>
  
  <xsl:variable name="WorkSeparation">WorkSeparation</xsl:variable>
  
  <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
  <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
  
  <xsl:variable name="modelEvidenceCodePath">  
    <xsl:choose>
      <xsl:when test="//EvidenceEntities/@package='evidence' ">evidence</xsl:when>  
      <xsl:otherwise><xsl:value-of select="//EvidenceEntities/@package"/>.evidence</xsl:otherwise>  
    </xsl:choose>
  </xsl:variable>
  
  <xsl:variable name="javaEvidenceCodePath">curam.<xsl:value-of select="$modelEvidenceCodePath"/></xsl:variable>
  
  <!-- BEGIN, CR00100405, CD -->
  <xsl:variable name="customModelEvidenceCodePath"> 
    <xsl:choose>
      <xsl:when test="//EvidenceEntities/@package='evidence' ">custom.evidence</xsl:when>  
      <xsl:otherwise>custom.<xsl:value-of select="//EvidenceEntities/@package"/>.evidence</xsl:otherwise>  
    </xsl:choose>
  </xsl:variable>
  <!-- END, CR00100405 -->
  
  <xsl:variable name="customJavaEvidenceCodePath">curam.<xsl:value-of select="$customModelEvidenceCodePath"/></xsl:variable>
  
  <xsl:variable name="prefix"><xsl:value-of select="//EvidenceEntities/@prefix"/></xsl:variable>
  <!-- ucPrefix is used for message file names, and SEGEvidence.evx -->
  <xsl:variable name="ucPrefix"><xsl:value-of select="translate($prefix, $lcletters, $ucletters)"/></xsl:variable>
  <xsl:variable name="lcPrefix"><xsl:value-of select="translate($prefix, $ucletters, $lcletters)"/></xsl:variable>      
  
  <!--xsl:variable name="lcPackage"><xsl:value-of select="//EvidenceEntities/@package"/></xsl:variable-->
  <!--xsl:variable name="ucPackage"><xsl:value-of select="translate($lcPackage, $lcletters, $ucletters)"/></xsl:variable-->      
  <xsl:variable name="product"><xsl:value-of select="//EvidenceEntities/@product"/></xsl:variable>
  
  <xsl:template name="toUpper">
    <xsl:param name="convertThis"/>
    <xsl:value-of select="translate($convertThis, $lcletters, $ucletters)"/>
  </xsl:template>
  
  <xsl:template name="toLower">
    <xsl:param name="convertThis"/>
    <xsl:value-of select="translate($convertThis, $ucletters, $lcletters)"/>
  </xsl:template>
  
  <xsl:template name="capitalize">
    <xsl:param name="convertThis"/>
    <xsl:value-of select="translate(substring($convertThis,1,1), $lcletters, $ucletters)"/><xsl:value-of select="translate(substring($convertThis,2), $ucletters, $lcletters)"/>
  </xsl:template>
  
  <xsl:template name="equalsIgnoreCase">
    <xsl:param name="a"/>
    <xsl:param name="b"/>
    <xsl:value-of select="translate($a, $lcletters, $ucletters)=translate($b, $lcletters, $ucletters)"/>
  </xsl:template>
  
  <!-- BEGIN, PADDY -->
  
  <xsl:variable name="entityValidationMessageFilePrefix">EntityGen</xsl:variable>
  <xsl:variable name="entityValidationMessageFilePostfix">Validation</xsl:variable>
  
  <xsl:template name="entity-validation-message-file">
    <xsl:param name="entityName"/>
    <xsl:value-of select="concat($entityValidationMessageFilePrefix, $entityName, $entityValidationMessageFilePostfix, '.xml')"/>
  </xsl:template>
  
  <xsl:template name="entity-validation-message-class">
    <xsl:param name="entityName"/>
    <xsl:call-template name="toUpper">
      <xsl:with-param name="convertThis" select="concat($entityValidationMessageFilePrefix, $entityName, $entityValidationMessageFilePostfix)"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="entity-field-validation-message">
    <xsl:param name="fieldLabel"/>
    <xsl:value-of select="concat('ERR_', translate(translate($fieldLabel, '.' , '_'), $lcletters, $ucletters), '_EMPTY')"/>
  </xsl:template>
  <!-- END, PADDY -->
  
    
  <xsl:template name="getNumberOfOptionalParticipants">

      <xsl:param name="relatedParticipantDetails"/>
      <xsl:param name="numberOfOptionalParticipants"/>

      <xsl:choose>  

        <xsl:when test="$relatedParticipantDetails">

          <xsl:variable name="rPcolumnName" select="$relatedParticipantDetails[position()=1]/@columnName"/>
          
          <xsl:variable name="increment" select="count($relatedParticipantDetails[position()=1]/../../UserInterfaceLayer/Cluster/Field[@columnName=$rPcolumnName and @mandatory!='Yes'])"/>

          <xsl:call-template name="getNumberOfOptionalParticipants">              
            <xsl:with-param name="relatedParticipantDetails" select="$relatedParticipantDetails[position()!=1]"/>
            <xsl:with-param name="numberOfOptionalParticipants" 
              select="$numberOfOptionalParticipants + $increment"/>
          </xsl:call-template>

        </xsl:when>

        <xsl:otherwise><xsl:value-of select="$numberOfOptionalParticipants"/></xsl:otherwise>

      </xsl:choose>
  
  </xsl:template> 
  
  <xsl:variable name="facade_modifyViewPage_GetAssociatedCaseParticipantReadMethod">getParentCaseParticipantRoleID</xsl:variable>  
  <xsl:variable name="facade_createPage_GetAssociatedCaseParticipantReadMethod">getCaseParticipantRoleID</xsl:variable>
  
  <xsl:variable name="prefixGeneralError"><xsl:value-of select="$prefix"/>GeneralError</xsl:variable>
  <xsl:variable name="ucPrefixGeneralError"><xsl:value-of select="translate($prefixGeneralError, $lcletters, $ucletters)"/></xsl:variable>
  
  <!--
      Template to get the localized display name of an entity via it's associated Label nodes
  -->
  <xsl:template name="getEntityLabelForXMLFile">
    <xsl:param name="locale"/>      
    <xsl:param name="evidenceNode"/>    
	<xsl:choose>
	  <xsl:when test="$evidenceNode/Label[$locale=@locale]"><xsl:value-of select="$evidenceNode/Label[$locale=@locale]/@value"/></xsl:when>
	  <xsl:otherwise><xsl:value-of select="$evidenceNode/@displayName"/></xsl:otherwise>
	</xsl:choose>    
  </xsl:template>
  
  <!--
    Get the language portion of the fully qualified locale string
  -->
  <xsl:template name="getLanguageFromLocale">
      <xsl:param name="fullLocale"/>
	  <xsl:variable name="language"><xsl:choose>		  
		<xsl:when test="string-length($fullLocale)-string-length(translate($fullLocale,'_','')) = 2"><xsl:value-of select="substring-before(substring-after($fullLocale, '_'), '_')"/></xsl:when>
		<xsl:when test="string-length($fullLocale)-string-length(translate($fullLocale,'_','')) = 1"><xsl:choose>		  
		  <xsl:when test="not(starts-with($fullLocale,'_'))"><xsl:value-of select="substring-before($fullLocale, '_')"/></xsl:when>
		  <xsl:otherwise><xsl:value-of select="translate($fullLocale,'_','')"/></xsl:otherwise>
	      </xsl:choose></xsl:when>
		<xsl:otherwise><xsl:value-of select="$fullLocale"/></xsl:otherwise>
	  </xsl:choose></xsl:variable>
	  <xsl:value-of select="$language"/>
  </xsl:template>
  
  <!--
    Get the country portion of the fully qualified locale string
  -->
  <xsl:template name="getCountryFromLocale">
      <xsl:param name="fullLocale"/>
	  <xsl:variable name="country"><xsl:choose>
		<xsl:when test="string-length($fullLocale)-string-length(translate($fullLocale,'_','')) = 2"><xsl:value-of select="substring-after(substring-after($fullLocale, '_'), '_')"/></xsl:when>
		<xsl:when test="not(starts-with($fullLocale,'_')) and string-length($fullLocale)-string-length(translate($fullLocale,'_','')) = 1"><xsl:value-of select="substring-after($fullLocale, '_')"/></xsl:when>
		<xsl:otherwise></xsl:otherwise>
	  </xsl:choose></xsl:variable>
	  <xsl:value-of select="$country"/>
  </xsl:template>
  
  <!--
    Standard Copyright notice to be placed at the top of all generated Java files
  -->
  <xsl:template name="printJavaCopyright">
      <xsl:param name="date"/>
/*
 * Generated by IBM Curam Evidence Generator.
 *
 * Licensed Materials - Property of IBM.
 * Generator Copyright IBM Corporation 2006, <xsl:value-of select="substring($date, 1, 4)"/>. All Rights Reserved.
 *
 * US Government User Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
  </xsl:template>
  
  <!--
    Standard Copyright notice to be placed at the top of all generated XML (UIM,VIM,SEC,TAB,NAV,MNU,DMX) files
  -->
  <xsl:template name="printXMLCopyright">
      <xsl:param name="date"/>
<xsl:comment>
  Generated by IBM Curam Evidence Generator.
  
  Licensed Materials - Property of IBM.
  Generator Copyright IBM Corporation 2006, <xsl:value-of select="substring($date, 1, 4)"/>. All Rights Reserved.
  
  US Government User Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
</xsl:comment>
  </xsl:template> 
    
</xsl:stylesheet>
