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

<!-- Global Variables -->
<xsl:import href="../UICommon.xslt"/>

<xsl:output method="xml" indent="yes"/>


<xsl:template name="ChangeHistoryUI">

  <xsl:param name="path"/>
  <xsl:param name="UIName"/>
  <xsl:param name="capName"/>
  
  

  <!-- Evidence Entity Specific Variables -->
  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>

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
      <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
    </CONNECT>
  </PAGE_TITLE>
    
  <INCLUDE FILE_NAME="Evidence_listEvdInstanceChangesView.vim"/>
   
 </PAGE>
   
</redirect:write>
   



  <xsl:call-template name="write-all-locales-change-history-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
    <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
  </xsl:call-template>

</xsl:template>




  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-change-history-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
       <xsl:param name="EvidenceEntity"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-change-history-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-change-history-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-change-history-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-change-history-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
         <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>


    <xsl:template name="write-change-history-properties">

      <xsl:param name="locale"/>
      <xsl:param name="filepath"/>
      <xsl:param name="EvidenceEntity"/>

      <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

        <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>


          <xsl:if test="count($generalProperties/leaf.title.ChangeHistory)&gt;0">
          <redirect:write select="concat($filepath, $locale, '.properties')">
<xsl:if test="count($generalProperties/Help.PageDescription.List.ChangeHistory)&gt;0"><xsl:call-template name="callGenerateProperties">
                <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.List.ChangeHistory"/>
                <xsl:with-param name="evidenceNode" select="."/>
	            <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
              </xsl:call-template>
            </xsl:if><xsl:text>&#xa;</xsl:text>
      
            <xsl:if test="count($generalProperties/leaf.title.ChangeHistory)&gt;0">
Page.Title=<xsl:value-of select="$generalProperties/leaf.title.ChangeHistory/@value"/>
            <xsl:text>&#xa;</xsl:text>     
            </xsl:if>

    </redirect:write>
          </xsl:if>
  </xsl:if>
  </xsl:template>

</xsl:stylesheet>