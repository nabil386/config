<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2017. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template name="ViewUIFromAssociationList">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>
  <xsl:param name="capName"/>
  <xsl:param name="childEvidenceName"/>
  <xsl:param name="fromCreateParentInd"/>

  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>
  <xsl:variable name="ChildEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$childEvidenceName]"/>

  <xsl:variable name="childLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName">
    <xsl:choose>
      <xsl:when test="$EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0"><xsl:value-of select="$capName"/></xsl:when>
      <xsl:when test="$EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@to!=''])>0"><xsl:value-of select="$childEvidenceName"/></xsl:when>
    </xsl:choose>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="listUIName"><xsl:if test="$EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@from!='']) > 0">
      <xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>ForAssociation</xsl:if></xsl:variable>
    <!--<xsl:choose>
      <xsl:when test="$EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0">
        <xsl:choose>
          <xsl:when test="$fromCreateParentInd='Yes'"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>ForAssociation_fromCreateParent</xsl:when>
          <xsl:otherwise><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>ForAssociation</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@to!=''])>0">
        <xsl:choose>
          <xsl:when test="$fromCreateParentInd='Yes'"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>ChildAssociationList_fromCreate</xsl:when>
          <xsl:otherwise><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>ChildAssociationList</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
    </xsl:choose>-->


  <xsl:variable name="viewContentUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>_content</xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <redirect:write select="concat($filepath, '.uim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE PAGE_ID="{$UIName}" WINDOW_OPTIONS="width=900" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
    </CONNECT>
  </PAGE_TITLE>

  <PAGE_PARAMETER NAME="linkedEvID"/>
  <PAGE_PARAMETER NAME="linkedEvType"/>
  <xsl:if test="$childLevelNo>=1">
  <PAGE_PARAMETER NAME="parEvID"/>
  <PAGE_PARAMETER NAME="parEvType"/>
  </xsl:if>
  <xsl:if test="$childLevelNo>=2">
  <PAGE_PARAMETER NAME="grandParEvID"/>
  <PAGE_PARAMETER NAME="grandParEvType"/>
  </xsl:if>
  <xsl:if test="$childLevelNo=3">
  <PAGE_PARAMETER NAME="greatGrandParEvID"/>
  <PAGE_PARAMETER NAME="greatGrandParEvType"/>
  </xsl:if>

  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL IMAGE="CloseButton" LABEL="ActionControl.Label.Close">


      <LINK PAGE_ID="{$listUIName}">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
          <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="linkedEvID"/>
          <TARGET NAME="PAGE" PROPERTY="linkedEvID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="linkedEvType"/>
          <TARGET NAME="PAGE" PROPERTY="linkedEvType"/>
        </CONNECT>
  <xsl:if test="$childLevelNo>=1">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
          <TARGET NAME="PAGE" PROPERTY="parEvID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
          <TARGET NAME="PAGE" PROPERTY="parEvType"/>
        </CONNECT>
  </xsl:if>
  <xsl:if test="$childLevelNo>=2">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="grandParEvID"/>
          <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="grandParEvType"/>
          <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
        </CONNECT>
  </xsl:if>
  <xsl:if test="$childLevelNo=3">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="greatGrandParEvID"/>
          <TARGET NAME="PAGE" PROPERTY="greatGrandParEvID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="greatGrandParEvType"/>
          <TARGET NAME="PAGE" PROPERTY="greatGrandParEvType"/>
        </CONNECT>
  </xsl:if>
      </LINK>


    </ACTION_CONTROL>


  </ACTION_SET>


  <INCLUDE FILE_NAME="{$viewContentUIName}.vim"/>


</PAGE>

  </redirect:write>


<!-- BEGIN, PADDY -->
  <!-- View Content Vim Properties -->
  <xsl:call-template name="write-all-locales-view-from-association-list-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
  </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>

<!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-view-from-association-list-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-view-from-association-list-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-view-from-association-list-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-view-from-association-list-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-view-from-association-list-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>

    <xsl:template name="write-view-from-association-list-properties">

        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
    <redirect:write select="concat($filepath, $locale, '.properties')">


    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

    <xsl:if test="count($generalProperties/Help.PageDescription.ViewEntity.FromAssociation)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.ViewEntity.FromAssociation"/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
</xsl:call-template>
    </xsl:if>

    <xsl:if test="count($generalProperties/Page.Title.ViewEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ViewEntity "/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
</xsl:call-template>
    <xsl:text>&#xa;</xsl:text>
    </xsl:if>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Close "/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>

  </redirect:write>
    </xsl:if>
    </xsl:template>

</xsl:stylesheet>
