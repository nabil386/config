<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2007 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Global Variables -->
<xsl:import href="EvidenceCommon.xslt"/>

<xsl:output method="xml" indent="yes"/>

<xsl:variable name="defaultLocale">en</xsl:variable>

<xsl:param name="messageDir"/>
<xsl:param name="date"/>

<xsl:template match="EvidenceEntities">
  
  <xsl:apply-templates/>

</xsl:template>


<!-- Main template for writing the file -->
<xsl:template match="EvidenceEntity">

  <!-- Only Generate the file if there are mandatory fields -->
  <xsl:if 
    test="@modify='No' or count(.//Field[@mandatory='Yes'])&gt;0
          or (ServiceLayer/@createRelatedParticipant='Yes' 
              and count(ServiceLayer/RelatedParticipantDetails[@createParticipant='Yes'])&gt;0)">

    <!-- Create fileName -->
    <xsl:variable name="validationMessageFile">
      <xsl:call-template name="entity-validation-message-file">
        <xsl:with-param name="entityName" select="@name"/>
      </xsl:call-template>
    </xsl:variable>
    
    <redirect:write select="concat($messageDir, $validationMessageFile)">
    
	<xsl:call-template name="printXMLCopyright">
	  <xsl:with-param name="date" select="$date"/>
	</xsl:call-template>
    
    <messages package="curam.message">
    
    <!-- BEGIN, CR00100657, POB -->
    <xsl:if test="@modify='No' and count(//EvidenceEntities/Properties/General/Page.Informational.NotModifiable) &gt; 0">
      <message name="PAGE_NOT_MODIFIABLE">
        <xsl:for-each select="//EvidenceEntities/Properties/General/Page.Informational.NotModifiable">
          <xsl:variable name="fullLocale" select="../../@locale"/>
          <xsl:choose>
            <xsl:when test="$fullLocale=''">
              <locale language="{$defaultLocale}"><xsl:value-of select="@value"/></locale>
            </xsl:when>
            <xsl:otherwise>
			  <xsl:variable name="language"><xsl:call-template name="getLanguageFromLocale">
			    <xsl:with-param name="fullLocale" select="$fullLocale"/></xsl:call-template></xsl:variable>
			  <xsl:variable name="country"><xsl:call-template name="getCountryFromLocale">
			    <xsl:with-param name="fullLocale" select="$fullLocale"/></xsl:call-template></xsl:variable>
              <locale language="{$language}"><xsl:if test="string-length($country)&gt;0"><xsl:attribute name="country"><xsl:value-of select="$country"/></xsl:attribute></xsl:if><xsl:value-of select="@value"/></locale>
            </xsl:otherwise>
          </xsl:choose>
          
        </xsl:for-each>
      </message>
      
      <message name="ENTITY_DISPLAY_NAME">
        <locale language="{$defaultLocale}"><xsl:call-template name="getEntityLabelForXMLFile"> 
			  <xsl:with-param name="locale" select="$defaultLocale"/>        
			  <xsl:with-param name="evidenceNode" select="."/>          
			</xsl:call-template></locale>
      </message>
    </xsl:if>
      <!-- END, CR00100657 -->
    <xsl:for-each select=".//Field[@mandatory='Yes']">
    
       <xsl:variable name="ucFieldName">
         <xsl:value-of select="translate(translate(@label, '.' , '_'), $lcletters, $ucletters)"/>
       </xsl:variable>
       
       <xsl:variable name="messageName">
         <xsl:call-template name="entity-field-validation-message">
           <xsl:with-param name="fieldLabel" select="@label"/>
         </xsl:call-template>
       </xsl:variable>
       
       <message name="{$messageName}">
         <xsl:for-each select="./Label">
            <xsl:choose>
              <xsl:when test="@locale=''">
                <locale language="{$defaultLocale}"><xsl:value-of select="@value"/></locale>
              </xsl:when>
              <xsl:otherwise>
              
				<xsl:variable name="language"><xsl:call-template name="getLanguageFromLocale">
				  <xsl:with-param name="fullLocale" select="@locale"/></xsl:call-template></xsl:variable>
				<xsl:variable name="country"><xsl:call-template name="getCountryFromLocale">
				  <xsl:with-param name="fullLocale" select="@locale"/></xsl:call-template></xsl:variable>
                
                <locale language="{$language}"><xsl:if test="string-length($country)&gt;0"><xsl:attribute name="country"><xsl:value-of select="$country"/></xsl:attribute></xsl:if><xsl:value-of select="@value"/></locale>
                
              </xsl:otherwise>
            </xsl:choose>
         </xsl:for-each>
       </message>
    </xsl:for-each>
    
    <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
      <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@createParticipant='Yes']">    
                    
        <xsl:variable name="rpdColumnName" select="@columnName"/>
       
        <xsl:variable name="ucFieldLabel">
          <xsl:value-of select="translate($rpdColumnName, $lcletters, $ucletters)"/>
        </xsl:variable>
       
       <message name="INF_{$ucFieldLabel}">
         <xsl:variable name="relatedFields" select="../../UserInterfaceLayer/Cluster/Field[@columnName=$rpdColumnName]"/> 
         <xsl:if test="count($relatedFields)&gt;0">
           <xsl:for-each select="$relatedFields[1]/Label">
              <xsl:choose>
                <xsl:when test="@locale=''">
                 <locale language="{$defaultLocale}"><xsl:value-of select="@value"/></locale>
                </xsl:when>
                <xsl:otherwise>

				  <xsl:variable name="language"><xsl:call-template name="getLanguageFromLocale">
				    <xsl:with-param name="fullLocale" select="@locale"/></xsl:call-template></xsl:variable>
				  <xsl:variable name="country"><xsl:call-template name="getCountryFromLocale">
				    <xsl:with-param name="fullLocale" select="@locale"/></xsl:call-template></xsl:variable>

                <locale language="{$language}"><xsl:if test="string-length($country)&gt;0"><xsl:attribute name="country"><xsl:value-of select="$country"/></xsl:attribute></xsl:if><xsl:value-of select="@value"/></locale>

                </xsl:otherwise>
              </xsl:choose>
           </xsl:for-each>
         </xsl:if>
       </message>
      </xsl:for-each>
    </xsl:if>
    
    </messages>

    </redirect:write>
  
  </xsl:if>
  
</xsl:template>

</xsl:stylesheet>