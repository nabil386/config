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

<!-- 
  This template writes the content VIM File for choosing an evidence record for
  pre association
  
  @param path Path where the file is to be written
  @param entityName The name of the Entity being generated
  @param pageName The name of the page to generate
-->
    <xsl:template name="EvidenceListViewForPreAssociation">

  <xsl:param name="path"/>
  <xsl:param name="entityName"/>
  <xsl:param name="pageName"/>
  
  <!-- The name of the normal create page for this entity -->
  <xsl:variable name="entityCreatePage"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityName"/><xsl:value-of select="$caseType"/></xsl:variable>
  
  <!-- the child level of this entity -->
  <xsl:variable name="childLevelNo"><xsl:call-template name="GetChildLevel">
    <xsl:with-param name="capName" select="$entityName"/>
  </xsl:call-template></xsl:variable>
  
  <!-- The base element for the entity being generated -->
  <xsl:variable name="currentEntityElem" select="//EvidenceEntities/EvidenceEntity[@name=$entityName]"/>
  
  <!-- The name of the entity being pre associated -->
  <xsl:variable name="preAssociationEntityName" select="$currentEntityElem/Relationships/PreAssociation/@to"/>
  
  <redirect:write select="concat($path, $pageName, '.vim')">
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<VIEW 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

  
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <xsl:if test="$childLevelNo>0">
    <PAGE_PARAMETER NAME="parEvID"/>
    <PAGE_PARAMETER NAME="parEvType"/>
  </xsl:if>
  <xsl:if test="$childLevelNo>1">
    <PAGE_PARAMETER NAME="grandParEvID"/>
    <PAGE_PARAMETER NAME="grandParEvType"/>
  </xsl:if>
  
  <!-- List to display the records of the given type that have been found -->
  <LIST TITLE="List.Title.SearchResults">
    <CONTAINER
      LABEL="List.Title.Action"
      WIDTH="20"
      >
      <ACTION_CONTROL LABEL="ActionControl.Label.SelectPreAssociation">
        <LINK PAGE_ID="{$entityCreatePage}" SAVE_LINK="FALSE" DISMISS_MODAL="FALSE">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PREASSOCLIST"
              PROPERTY="result$parentList$dtls$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="preAssocID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PREASSOCLIST"
              PROPERTY="result$parentList$dtls$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="preAssocType"
            />
          </CONNECT>
          <xsl:if test="$childLevelNo>0">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvType"
              />
            </CONNECT>
          </xsl:if>
          <xsl:if test="$childLevelNo>1">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
            </CONNECT>
          </xsl:if>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="18"
      >
      <CONNECT>
        <SOURCE
          NAME="PREASSOCLIST"
          PROPERTY="result$parentList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="13"
      >
      <CONNECT>
        <SOURCE
          NAME="PREASSOCLIST"
          PROPERTY="result$parentList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Details"
      WIDTH="23"
      >
      <CONNECT>
        <SOURCE
          NAME="PREASSOCLIST"
          PROPERTY="result$parentList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  
</VIEW>

  </redirect:write>

  <!-- BEGIN, PADDY -->
  <xsl:call-template name="write-all-locales-preassoclistview-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="concat($path, $pageName)"/>
    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
  </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>


<!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-preassoclistview-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
      <xsl:param name="currentEntityElem"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-preassoclistview-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-preassoclistview-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-preassoclistview-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-preassoclistview-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>


  <xsl:template name="write-preassoclistview-properties">

      <xsl:param name="locale"/>
      <xsl:param name="filepath"/>
      <xsl:param name="currentEntityElem"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <redirect:write select="concat($filepath, $locale, '.properties')">
      
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/List.Title.Action)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Action"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/List.Title.SearchResults)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.SearchResults"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/List.Title.Name)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Name"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/List.Title.Details)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Details"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/List.Title.EffectiveDate)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.EffectiveDate"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/ActionControl.Label.SelectPreAssociation)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SelectPreAssociation"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
  </redirect:write>
  </xsl:if>
    </xsl:template>

</xsl:stylesheet>