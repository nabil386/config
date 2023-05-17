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

  <!-- Global Variables -->
  <xsl:import href="UICommon.xslt"/>

  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="ViewAssociatedListFrom">

    <xsl:param name="path"/>
    <xsl:param name="entityFrom"/>
    <xsl:param name="associatedEntity"/>

    <xsl:variable name="associatedListPage"><xsl:value-of select="$prefix"/>_listAssociationsFrom<xsl:value-of select="$entityFrom"/>To<xsl:value-of select="$associatedEntity"/>Evidence</xsl:variable>

    <xsl:variable name="associatedViewPage"><xsl:value-of select="$prefix"/>_viewAssociated<xsl:value-of select="$associatedEntity"/>From<xsl:value-of select="$entityFrom"/><xsl:value-of select="$caseType"/></xsl:variable>

    <xsl:variable name="viewPage"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$entityFrom"/><xsl:value-of select="$caseType"/></xsl:variable>

    <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$associatedListPage"/></xsl:variable>

    <xsl:variable name="childLevelNo">
      <xsl:call-template name="GetChildLevel">
        <xsl:with-param name="capName" select="$entityFrom"/>
      </xsl:call-template>
    </xsl:variable>

    <redirect:write select="concat($filepath, '.uim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

      <PAGE PAGE_ID="{$associatedListPage}" WINDOW_OPTIONS="width=900" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

          <PAGE_TITLE>
              <CONNECT>
                  <SOURCE PROPERTY="Page.Title" NAME="TEXT"/>
              </CONNECT>
              <CONNECT>
                  <SOURCE PROPERTY="contextDescription" NAME="PAGE"/>
              </CONNECT>
          </PAGE_TITLE>

        <ACTION_SET ALIGNMENT="CENTER">
          <ACTION_CONTROL IMAGE="CloseButton" LABEL="ActionControl.Label.Close">

            <LINK
              PAGE_ID="{$viewPage}"
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
                <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
                <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
                <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
              </CONNECT>
              <xsl:if test="$childLevelNo>0">
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
                  <TARGET NAME="PAGE" PROPERTY="parEvID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
                  <TARGET NAME="PAGE" PROPERTY="parEvType"/>
                </CONNECT>
              </xsl:if>
              <xsl:if test="$childLevelNo>1">
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="grandParEvID"/>
                  <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="grandParEvType"/>
                  <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
                </CONNECT>
              </xsl:if>

            </LINK>
          </ACTION_CONTROL>
        </ACTION_SET>

        <SERVER_INTERFACE OPERATION="listAssociatedEvidence" NAME="DISPLAY" CLASS="{$prefix}{$facadeClass}"/>

          <PAGE_PARAMETER NAME="caseID"/>
          <PAGE_PARAMETER NAME="contextDescription"/>
          <PAGE_PARAMETER NAME="evidenceID"/>
          <PAGE_PARAMETER NAME="evidenceType"/>
          <PAGE_PARAMETER NAME="associatedType"/>
        <xsl:if test="$childLevelNo>0">
          <PAGE_PARAMETER NAME="parEvID"/>
          <PAGE_PARAMETER NAME="parEvType"/>
        </xsl:if>
        <xsl:if test="$childLevelNo>1">
          <PAGE_PARAMETER NAME="grandParEvID"/>
          <PAGE_PARAMETER NAME="grandParEvType"/>
        </xsl:if>

          <CONNECT>
              <SOURCE PROPERTY="caseID" NAME="PAGE"/>
              <TARGET PROPERTY="key$caseID" NAME="DISPLAY"/>
          </CONNECT>
          <CONNECT>
              <SOURCE PROPERTY="evidenceID" NAME="PAGE"/>
              <TARGET PROPERTY="key$parentEvidenceID" NAME="DISPLAY"/>
          </CONNECT>
          <CONNECT>
              <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
              <TARGET PROPERTY="key$parentEvidenceType" NAME="DISPLAY"/>
          </CONNECT>
          <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="associatedType"/>
              <TARGET NAME="DISPLAY" PROPERTY="key$evidenceType"/>
          </CONNECT>

        <LIST
          DESCRIPTION="Cluster.AssociatedList.Description"
          TITLE="Cluster.AssociatedList.Title"
          >
          <CONTAINER
            LABEL="List.Title.Action"
            WIDTH="20"
            >
            <ACTION_CONTROL LABEL="ActionControl.Label.View">
              <LINK PAGE_ID="{$associatedViewPage}">
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
                    NAME="DISPLAY"
                    PROPERTY="result$parentList$dtls$evidenceID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="evidenceID"
                  />
                </CONNECT>
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$parentList$dtls$evidenceType"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="evidenceType"
                  />
                </CONNECT>
                <CONNECT>
                  <SOURCE
                    NAME="PAGE"
                    PROPERTY="evidenceID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="associatedEvID"
                  />
                </CONNECT>
                <CONNECT>
                  <SOURCE
                    NAME="PAGE"
                    PROPERTY="evidenceType"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="associatedEvType"
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
                <xsl:if test="$childLevelNo>0">
                  <CONNECT>
                    <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
                    <TARGET NAME="PAGE" PROPERTY="associatedParEvID"/>
                  </CONNECT>
                  <CONNECT>
                    <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
                    <TARGET NAME="PAGE" PROPERTY="associatedParEvType"/>
                  </CONNECT>
                </xsl:if>
                <xsl:if test="$childLevelNo>1">
                  <CONNECT>
                    <SOURCE NAME="PAGE" PROPERTY="grandParEvID"/>
                    <TARGET NAME="PAGE" PROPERTY="associatedGrandParEvID"/>
                  </CONNECT>
                  <CONNECT>
                    <SOURCE NAME="PAGE" PROPERTY="grandParEvType"/>
                    <TARGET NAME="PAGE" PROPERTY="associatedGrandParEvType"/>
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
                  NAME="DISPLAY"
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
                  NAME="DISPLAY"
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
                  NAME="DISPLAY"
                  PROPERTY="result$parentList$dtls$summary"
                />
              </CONNECT>
            </FIELD>
        </LIST>


      </PAGE>

    </redirect:write>



  <!-- BEGIN, PADDY -->
      <xsl:call-template name="write-all-locales-view-assoc-list-properties">
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$filepath"/>
        <xsl:with-param name="associatedEntity" select="$associatedEntity"/>
      </xsl:call-template>
    <!-- END, PADDY -->

  </xsl:template>



  <!-- BEGIN, PADDY -->

    <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-view-assoc-list-properties">

         <xsl:param name="locales"/>
         <xsl:param name="filepath"/>
        <xsl:param name="associatedEntity"/>

         <!--tokens still exist-->
         <xsl:if test="$locales">

           <xsl:choose>

             <!--more than one-->
             <xsl:when test="contains($locales,',')">

                 <xsl:call-template name="write-view-assoc-list-properties">
                 <xsl:with-param name="locale"
                                select="concat('_', substring-before($locales,','))"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                   <xsl:with-param name="associatedEntity" select="$associatedEntity"/>
               </xsl:call-template>

               <!-- Recursively call self to process all locales -->
                 <xsl:call-template name="write-all-locales-view-assoc-list-properties">
                 <xsl:with-param name="locales"
                                 select="substring-after($locales,',')"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                   <xsl:with-param name="associatedEntity" select="$associatedEntity"/>
               </xsl:call-template>

             </xsl:when>

             <!--only one token left-->
             <xsl:otherwise>

               <!-- Call for the final locale -->
                 <xsl:call-template name="write-view-assoc-list-properties">
                 <xsl:with-param name="locale" select="concat('_', $locales)"/>
                 <xsl:with-param name="filepath"
                 select="$filepath"/>
                   <xsl:with-param name="associatedEntity" select="$associatedEntity"/>
               </xsl:call-template>

               <!-- Finally call for the default locale -->
                 <xsl:call-template name="write-view-assoc-list-properties">
           <xsl:with-param name="locale"/>
           <xsl:with-param name="filepath" select="$filepath"/>
                   <xsl:with-param name="associatedEntity" select="$associatedEntity"/>
               </xsl:call-template>

             </xsl:otherwise>

           </xsl:choose>

         </xsl:if>

      </xsl:template>

    <xsl:template name="write-view-assoc-list-properties">

          <xsl:param name="locale"/>
          <xsl:param name="filepath"/>
          <xsl:param name="associatedEntity"/>

      <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
    <redirect:write select="concat($filepath, $locale, '.properties')">

      <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <xsl:if test="count($generalProperties/Page.Title.ViewAssociatedEvidenceList)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ViewAssociatedEvidenceList"/>
          <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$associatedEntity]"/>
	      <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
        </xsl:call-template>
      </xsl:if>
<xsl:text>&#xa;</xsl:text>
      <xsl:if test="count($generalProperties/ActionControl.Label.Close)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Close"/>
          <xsl:with-param name="evidenceNode" select="."/>
        </xsl:call-template>
      </xsl:if>
<xsl:text>&#xa;</xsl:text>
      <xsl:if test="count($generalProperties/Cluster.AssociatedList.Title)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.AssociatedList.Title"/>
          <xsl:with-param name="evidenceNode" select="."/>
        </xsl:call-template>
      </xsl:if>
<xsl:text>&#xa;</xsl:text>
      <xsl:if test="count($generalProperties/Cluster.AssociatedList.Description)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.AssociatedList.Description"/>
          <xsl:with-param name="evidenceNode" select="."/>
        </xsl:call-template>
      </xsl:if>
<xsl:text>&#xa;</xsl:text>
      <xsl:if test="count($generalProperties/List.Title.Action)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Action"/>
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
      <xsl:if test="count($generalProperties/ActionControl.Label.View)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.View"/>
          <xsl:with-param name="evidenceNode" select="."/>
        </xsl:call-template>
      </xsl:if>
    </redirect:write>
        </xsl:if>

    </xsl:template>

</xsl:stylesheet>
