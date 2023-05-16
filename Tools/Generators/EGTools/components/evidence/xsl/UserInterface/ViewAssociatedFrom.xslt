<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

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

  <!-- Global Variables -->
  <xsl:import href="UICommon.xslt"/>

  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="ViewAssociatedFrom">

    <xsl:param name="path"/>
    <xsl:param name="entityFrom"/>
    <xsl:param name="associatedEntity"/>

    <xsl:variable name="capNameCaseType"><xsl:value-of select="$associatedEntity"/><xsl:value-of select="$caseType"/></xsl:variable>

    <xsl:variable name="CurrentEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$entityFrom][1]"/>

    <xsl:variable name="currentPageName"><xsl:value-of select="$prefix"/>_viewAssociated<xsl:value-of select="$associatedEntity"/>From<xsl:value-of select="$entityFrom"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:variable name="associatedListPage"><xsl:value-of select="$prefix"/>_listAssociationsFrom<xsl:value-of select="$entityFrom"/>To<xsl:value-of select="$associatedEntity"/>Evidence</xsl:variable>

    <xsl:variable name="viewContentUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>_content</xsl:variable>

    <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$currentPageName"/></xsl:variable>

    <xsl:variable name="fromChildLevelNo">
      <xsl:call-template name="GetChildLevel">
        <xsl:with-param name="capName" select="$entityFrom"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:variable name="associatedChildLevelNo">
      <xsl:call-template name="GetChildLevel">
        <xsl:with-param name="capName" select="$associatedEntity"/>
      </xsl:call-template>
    </xsl:variable>

    <redirect:write select="concat($filepath, '.uim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

      <PAGE PAGE_ID="{$currentPageName}" WINDOW_OPTIONS="width=900" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

        <PAGE_TITLE>
          <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        </PAGE_TITLE>


        <ACTION_SET ALIGNMENT="CENTER">

          <ACTION_CONTROL IMAGE="CloseButton" LABEL="ActionControl.Label.Close">

            <LINK
              PAGE_ID="{$associatedListPage}"
              SAVE_LINK="false"
            >
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                <TARGET NAME="PAGE" PROPERTY="caseID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
                <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
              </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="associatedEvID"/>
                  <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="associatedEvType"/>
                  <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
                </CONNECT>
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
                <TARGET NAME="PAGE" PROPERTY="associatedType"/>
              </CONNECT>
              <xsl:if test="$fromChildLevelNo &gt; 0">
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="associatedParEvID"/>
                  <TARGET NAME="PAGE" PROPERTY="parEvID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="associatedParEvType"/>
                  <TARGET NAME="PAGE" PROPERTY="parEvType"/>
                </CONNECT>
              </xsl:if>
              <xsl:if test="$fromChildLevelNo &gt; 1">
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="associatedGrandParEvID"/>
                  <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="associatedGrandParEvType"/>
                  <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
                </CONNECT>
              </xsl:if>
            </LINK>

          </ACTION_CONTROL>
        </ACTION_SET>

        <xsl:if test="$associatedChildLevelNo=0">
          <PAGE_PARAMETER NAME="associatedEvID"/>
          <PAGE_PARAMETER NAME="associatedEvType"/>
        </xsl:if>
        <xsl:if test="$fromChildLevelNo &gt; 0 and $associatedChildLevelNo &lt; 2">
          <PAGE_PARAMETER NAME="associatedParEvID"/>
          <PAGE_PARAMETER NAME="associatedParEvType"/>
        </xsl:if>
        <xsl:if test="$fromChildLevelNo &gt; 1 and $associatedChildLevelNo &lt; 3">
          <PAGE_PARAMETER NAME="associatedGrandParEvID"/>
          <PAGE_PARAMETER NAME="associatedGrandParEvType"/>
        </xsl:if>

        <INCLUDE FILE_NAME="{$viewContentUIName}.vim"/>


      </PAGE>

    </redirect:write>



  <!-- BEGIN, PADDY -->
      <xsl:call-template name="write-all-locales-view-assoc-from-properties">
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$filepath"/>
        <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
      </xsl:call-template>
    <!-- END, PADDY -->

  </xsl:template>



  <!-- BEGIN, PADDY -->

    <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-view-assoc-from-properties">

         <xsl:param name="locales"/>
         <xsl:param name="filepath"/>
        <xsl:param name="CurrentEvidenceEntity"/>

         <!--tokens still exist-->
         <xsl:if test="$locales">

           <xsl:choose>

             <!--more than one-->
             <xsl:when test="contains($locales,',')">

               <xsl:call-template name="write-view-assoc-from-properties">
                 <xsl:with-param name="locale"
                                select="concat('_', substring-before($locales,','))"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
               </xsl:call-template>

               <!-- Recursively call self to process all locales -->
               <xsl:call-template name="write-all-locales-view-assoc-from-properties">
                 <xsl:with-param name="locales"
                                 select="substring-after($locales,',')"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
               </xsl:call-template>

             </xsl:when>

             <!--only one token left-->
             <xsl:otherwise>

               <!-- Call for the final locale -->
               <xsl:call-template name="write-view-assoc-from-properties">
                 <xsl:with-param name="locale" select="concat('_', $locales)"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
               </xsl:call-template>

               <!-- Finally call for the default locale -->
               <xsl:call-template name="write-view-assoc-from-properties">
           <xsl:with-param name="locale"/>
           <xsl:with-param name="filepath" select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
               </xsl:call-template>

             </xsl:otherwise>

           </xsl:choose>

         </xsl:if>

      </xsl:template>

  <xsl:template name="write-view-assoc-from-properties">

          <xsl:param name="locale"/>
          <xsl:param name="filepath"/>
          <xsl:param name="CurrentEvidenceEntity"/>

      <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

    <redirect:write select="concat($filepath, $locale, '.properties')">
      <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
      <xsl:if test="count($generalProperties/Help.PageDescription.ViewEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.ViewEntity"/>
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

      <xsl:if test="count($generalProperties/ActionControl.Label.Close)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Close"/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>
</xsl:if>

    </redirect:write>
      </xsl:if>
    </xsl:template>

</xsl:stylesheet>
