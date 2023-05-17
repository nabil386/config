<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2011,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2011 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Global Variables -->
<xsl:import href="UserInterface/UICommon.xslt"/>

<xsl:param name="localeList"/>
<xsl:param name="messageDir"/>
<xsl:param name="date"/>

<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>

<xsl:variable name="defaultLocale">en</xsl:variable>

<xsl:variable name="filename"><xsl:value-of select="$messageDir"/><xsl:value-of select="$prefixGeneralError"/>.xml</xsl:variable>

<xsl:template match="EvidenceEntities">
    
    <redirect:write select="$filename">
    
	<xsl:call-template name="printXMLCopyright">
	  <xsl:with-param name="date" select="$date"/>
	</xsl:call-template>
    
    <messages package="curam.message">
	  <message name="ERR_FV_NO_ACTIVE_PARENT_RECORD">
      <xsl:call-template name="write-message-all-locales">    
        <xsl:with-param name="locales" select="$localeList"/>   
        <xsl:with-param name="messageProperty">PRODUCTGENERALERROR.ERR_FV_NO_ACTIVE_PARENT_RECORD</xsl:with-param> 
      </xsl:call-template> 
	  </message>	  
	  <message name="ERR_EVIDENCE_CANNOT_BE_SHARED_AS_RELATED_CASE_MEMBER_DOES_NOT_EXIST_ON_TARGET_CASE">
      <xsl:call-template name="write-message-all-locales">    
        <xsl:with-param name="locales" select="$localeList"/>   
        <xsl:with-param name="messageProperty">PRODUCTGENERALERROR.ERR_EVIDENCE_CANNOT_BE_SHARED_AS_RELATED_CASE_MEMBER_DOES_NOT_EXIST_ON_TARGET_CASE</xsl:with-param> 
      </xsl:call-template> 
	  </message>
    </messages>
    </redirect:write>
  
  
</xsl:template>

<xsl:template name="write-message-all-locales">      
  <xsl:param name="locales"/>    
  <xsl:param name="messageProperty"/>   
        
    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">		  
		  
          <xsl:call-template name="write-message">   
            <xsl:with-param name="locale"
                          select="substring-before($locales,',')"/> 
            <xsl:with-param name="messageProperty" select="$messageProperty"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-message-all-locales">   
            <xsl:with-param name="locales"
                            select="substring-after($locales,',')"/>
            <xsl:with-param name="messageProperty" select="$messageProperty"/>
          </xsl:call-template>

        </xsl:when>
      
        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-message">   
            <xsl:with-param name="locale" select="$locales"/>
            <xsl:with-param name="messageProperty" select="$messageProperty"/>
         </xsl:call-template>

         <!-- Finally call for the default locale -->
          <xsl:call-template name="write-message">  
            <xsl:with-param name="locale"/>
            <xsl:with-param name="messageProperty" select="$messageProperty"/>
         </xsl:call-template>

       </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

</xsl:template>

<xsl:template name="write-message">  
  
  <xsl:param name="locale"/>    
  <xsl:param name="messageProperty"/>    
  
  <xsl:if test="$locale!=$defaultLocale">
  
  <xsl:variable name="evidenceEntitiesLocale"><xsl:choose>
    <xsl:when test="$locale=''"><xsl:value-of select="$locale"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="concat('_', $locale)"/></xsl:otherwise></xsl:choose></xsl:variable>
  
  <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$evidenceEntitiesLocale]/General"/>  
  <xsl:variable name="msgLocale"><xsl:choose>
    <xsl:when test="$locale=''"><xsl:value-of select="$defaultLocale"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="$locale"/></xsl:otherwise></xsl:choose></xsl:variable>
  
  <xsl:variable name="language"><xsl:call-template name="getLanguageFromLocale">
	<xsl:with-param name="fullLocale" select="$msgLocale"/></xsl:call-template></xsl:variable>
  <xsl:variable name="country"><xsl:call-template name="getCountryFromLocale">
	<xsl:with-param name="fullLocale" select="$msgLocale"/></xsl:call-template></xsl:variable>
  
  <xsl:variable name="localizedMessage"><xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/*[name() = $messageProperty]"/>
            <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
          </xsl:call-template></xsl:variable>
	  <xsl:choose>
		<xsl:when test="$localizedMessage='' and $locale!=$defaultLocale"><xsl:comment> No message defined for language '<xsl:value-of select="$language"/>'<xsl:if test="string-length($country)&gt;0">, country '<xsl:value-of select="$country"/>'</xsl:if> </xsl:comment></xsl:when>
		<xsl:otherwise><locale language="{$language}"><xsl:if test="string-length($country)&gt;0"><xsl:attribute name="country"><xsl:value-of select="$country"/></xsl:attribute></xsl:if><xsl:value-of select="substring-after($localizedMessage,'=')"/></locale></xsl:otherwise>
      </xsl:choose>
      
  </xsl:if>  
</xsl:template>

</xsl:stylesheet>