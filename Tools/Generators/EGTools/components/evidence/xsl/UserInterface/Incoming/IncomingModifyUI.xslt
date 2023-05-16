<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2017. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
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

<xsl:template name="IncomingModifyUI">

  <xsl:param name="path" />
  <xsl:param name="UIName" />

  <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
  <xsl:variable name="capNameCaseType"><xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/></xsl:variable>

  <!-- BEGIN, CR00100657, POB -->
  <xsl:variable name="modifiable" select="@modify"/>
  <!-- END, CR00100657, POB -->

  <xsl:variable name="incomingModifyContentUIName"><xsl:value-of select="$prefix"/>_incomingModify<xsl:value-of select="$capNameCaseType"/>_content</xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <xsl:variable name="childLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName" select="$capName"/>
    </xsl:call-template>
  </xsl:variable>

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
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <ACTION_SET ALIGNMENT="CENTER">

<xsl:if test="$modifiable='Yes'">
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT" >
      <LINK PAGE_ID="AdvancedEvidenceSearch_afterActionResolve" />
    </ACTION_CONTROL>
</xsl:if>

    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />

  </ACTION_SET>


  <INCLUDE FILE_NAME="{$incomingModifyContentUIName}.vim"/>


</PAGE>


  </redirect:write>

<!-- BEGIN, PADDY -->
  <xsl:call-template name="write-all-locales-incoming-modify-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
  </xsl:call-template>
  <!-- END, PADDY -->
</xsl:template>

<!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-incoming-modify-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-incoming-modify-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-modify-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-incoming-modify-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-incoming-modify-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>

    <xsl:template name="write-incoming-modify-properties">

        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

<redirect:write select="concat($filepath, $locale, '.properties')">
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    <xsl:if test="count($generalProperties/Help.PageDescription.ModifyEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.ModifyEntity"/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
</xsl:call-template>
    </xsl:if>

    <xsl:if test="count($generalProperties/Page.Title.ModifyEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ModifyEntity"/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
</xsl:call-template>
    <xsl:text>&#xa;</xsl:text>
     </xsl:if>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Save"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

  </redirect:write>
    </xsl:if>
    </xsl:template>

</xsl:stylesheet>