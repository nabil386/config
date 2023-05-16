<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2017. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
  Copyright (c) 2006-2008,2010 Curam Software Ltd.  All rights reserved.

  This software is the confidential and proprietary information of Curam
  Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
  disclose such Confidential Information and shall use it only in accordance
  with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="UICommon.xslt"/>

  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="ViewUI">

    <xsl:param name="path"/>
    <xsl:param name="UIName"/>

    <!-- BEGIN, CR00219910, CD -->
    <xsl:param name="viewVersion"/>

    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="capNameCaseType"><xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/></xsl:variable>

     <xsl:variable name="CurrentEvidenceEntity" select="."/>

    <xsl:variable name="listUINameFromView"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$capNameCaseType"/>List</xsl:variable>

    <xsl:variable name="confirmCancelAssociationUIName"><xsl:value-of select="$prefix"/>_confirmCancel<xsl:value-of select="$capName"/>EvidenceAssociation</xsl:variable>

    <xsl:variable name="viewContentUIName">
      <xsl:choose>
        <xsl:when test="$viewVersion=$businessObjectViewVersion"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>Object_content</xsl:when>
        <xsl:otherwise><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>_content</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- END, CR00219910 -->

    <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

    <!-- BEGIN, CR00222728, POB -->
    <xsl:variable name="correctionHistoryPage"><xsl:value-of select="$UIName"/>CH</xsl:variable>
    <!-- END, CR00222728, POB -->

    <xsl:variable name="childLevelNo">
      <xsl:call-template name="GetChildLevel">
        <xsl:with-param name="capName" select="$capName"/>
      </xsl:call-template>
    </xsl:variable>
    <redirect:write select="concat($filepath, '.uim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

      <PAGE PAGE_ID="{$UIName}" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

        <xsl:if test="$viewVersion!=$businessObjectViewVersion">
          <xsl:attribute name="WINDOW_OPTIONS">width=900</xsl:attribute>
        </xsl:if>

        <xsl:if test="$viewVersion!=$businessObjectViewVersion and $viewVersion!=$historyRecordViewVersion">
        <MENU MODE="IN_PAGE_NAVIGATION">
          <ACTION_CONTROL STYLE="in-page-current-link" LABEL="ActionControl.Label.Details">
            <LINK PAGE_ID="{$UIName}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
              <CONNECT>
                <SOURCE PROPERTY="caseID" NAME="PAGE"/>
                <TARGET PROPERTY="caseID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceID" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL STYLE="in-page-link" LABEL="ActionControl.Label.History">
            <LINK PAGE_ID="{$correctionHistoryPage}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
              <CONNECT>
                <SOURCE PROPERTY="caseID" NAME="PAGE"/>
                <TARGET PROPERTY="caseID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceID" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </MENU>
        </xsl:if>

        <PAGE_TITLE>
          <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
          </CONNECT>
        </PAGE_TITLE>


        <INCLUDE FILE_NAME="{$viewContentUIName}.vim"/>

      </PAGE>

    </redirect:write>



  <!-- BEGIN, PADDY -->
      <xsl:call-template name="write-all-locales-view-properties">
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$filepath"/>
        <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
        <xsl:with-param name="viewVersion" select="$viewVersion"/>
      </xsl:call-template>
    <!-- END, PADDY -->

  </xsl:template>



  <!-- BEGIN, PADDY -->

    <!--iterate through each token, generating each element-->
      <xsl:template name="write-all-locales-view-properties">

         <xsl:param name="locales"/>
         <xsl:param name="filepath"/>
        <xsl:param name="CurrentEvidenceEntity"/>
        <xsl:param name="viewVersion"/>

         <!--tokens still exist-->
         <xsl:if test="$locales">

           <xsl:choose>

             <!--more than one-->
             <xsl:when test="contains($locales,',')">

               <xsl:call-template name="write-view-properties">
                 <xsl:with-param name="locale"
                                select="concat('_', substring-before($locales,','))"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
                 <xsl:with-param name="viewVersion" select="$viewVersion"/>
               </xsl:call-template>

               <!-- Recursively call self to process all locales -->
               <xsl:call-template name="write-all-locales-view-properties">
                 <xsl:with-param name="locales"
                                 select="substring-after($locales,',')"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
                 <xsl:with-param name="viewVersion" select="$viewVersion"/>
               </xsl:call-template>

             </xsl:when>

             <!--only one token left-->
             <xsl:otherwise>

               <!-- Call for the final locale -->
               <xsl:call-template name="write-view-properties">
                 <xsl:with-param name="locale" select="concat('_', $locales)"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
                 <xsl:with-param name="viewVersion" select="$viewVersion"/>
               </xsl:call-template>

               <!-- Finally call for the default locale -->
               <xsl:call-template name="write-view-properties">
           <xsl:with-param name="locale"/>
           <xsl:with-param name="filepath" select="$filepath"/>
                 <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
                 <xsl:with-param name="viewVersion" select="$viewVersion"/>
               </xsl:call-template>

             </xsl:otherwise>

           </xsl:choose>

         </xsl:if>

      </xsl:template>

      <xsl:template name="write-view-properties">

          <xsl:param name="locale"/>
          <xsl:param name="filepath"/>
          <xsl:param name="CurrentEvidenceEntity"/>
          <xsl:param name="viewVersion"/>

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

      <xsl:if test="count($generalProperties/Page.Title.ViewEntity)&gt;0 and $viewVersion!=$businessObjectViewVersion">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ViewEntity "/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
</xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/leaf.title.Home)&gt;0 and $viewVersion=$businessObjectViewVersion">
Page.Title=<xsl:value-of select="$generalProperties/leaf.title.Home/@value"/>
      </xsl:if>
<xsl:text>&#xa;</xsl:text>
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Close "/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Details "/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.History "/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>

<xsl:if test="(Relationships/@association='Yes' and count(Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0)">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.RemoveAssociation"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

</xsl:if>
<!-- BEGIN, CR00100657, POB -->
      <xsl:for-each select="UserInterfaceLayer/ViewAssociated">

        <xsl:variable name="assocName" select="@name"/>
        <xsl:variable name="currentAssocEl" select="/EvidenceEntities/EvidenceEntity[@name=$assocName][1]"/>

<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.ViewAssociated"/>
          <xsl:with-param name="altPropertyName">ActionControl.Label.ViewAssociated<xsl:value-of select="$assocName"/></xsl:with-param>
          <xsl:with-param name="evidenceNode" select="$currentAssocEl"/>
        </xsl:call-template>

      </xsl:for-each>
<!-- END, CR00100657, POB -->
    </redirect:write>
      </xsl:if>
    </xsl:template>

</xsl:stylesheet>
