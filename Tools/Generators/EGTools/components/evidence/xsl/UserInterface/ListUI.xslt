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
<!-- This template is in process of being replaced by ListTypeWorkspaceUI.xslt -->
<xsl:template name="ListUI">

  <xsl:param name="path"/>
  <xsl:param name="EvidenceEntity"/>
  <xsl:param name="UIName"/>
  <xsl:param name="associationInd"/> <!-- To indicate that this is for standalone association evidence list-->
  <xsl:param name="listPageType"/>
  <xsl:param name="parentName"/>

  <xsl:variable name="capName"><xsl:value-of select="$EvidenceEntity/@name"/></xsl:variable>
  <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>
  <xsl:variable name="capNameCaseType"><xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/></xsl:variable>


  <xsl:variable name="assocInd">
    <xsl:choose>
      <xsl:when test="$associationInd='Yes'">Yes</xsl:when>
      <xsl:otherwise>No</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="relatedEvidenceInd">
    <xsl:choose>
      <xsl:when test="$EvidenceEntity/Relationships/@related='Yes' and count($EvidenceEntity/Relationships/Related[@to!=''])>0">Yes</xsl:when>
      <xsl:otherwise>No</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="associatedEvidenceInd">
    <xsl:choose>
      <xsl:when test="$assocInd!='Yes' and $EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0">Yes</xsl:when>
      <xsl:otherwise>No</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="preAssociationEvidenceInd">
    <xsl:choose>
      <xsl:when test="$EvidenceEntity/Relationships/@preAssociation='Yes' and count($EvidenceEntity/Relationships/PreAssociation[@to!=''])>0">Yes</xsl:when>
      <xsl:otherwise>No</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="createUIName">
    <xsl:choose>
      <xsl:when test="$preAssociationEvidenceInd='Yes'"><xsl:value-of select="$prefix"/>_select<xsl:value-of select="$capNameCaseType"/>PreAssociation</xsl:when>
      <!-- BEGIN, CR00128375, POB -->
      <xsl:when test="$relatedEvidenceInd='Yes'"><xsl:value-of select="$prefix"/>_listRelatedEntriesFor<xsl:value-of select="@name"/><xsl:value-of select="$caseType"/></xsl:when>
      <!-- END, CR00128375 -->
      <xsl:when test="$associatedEvidenceInd='Yes'"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/>ForAssociation</xsl:when>
      <xsl:when test="Relationships/Related/@to='Employment'"><xsl:value-of select="$prefix"/>_listRelatedClaimParticipant<xsl:value-of select="$caseType"/></xsl:when>
      <xsl:when test="$parentName!='' and count($EvidenceEntity/Relationships/MandatoryParents/Parent)&gt;0"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capNameCaseType"/>From<xsl:value-of select="$parentName"/></xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capNameCaseType"/></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="validateUIName"><xsl:value-of select="$prefix"/>_validate<xsl:value-of select="$capNameCaseType"/></xsl:variable>

  <xsl:variable name="facadeListMethod">list<xsl:value-of select="$EntityName"/>Evidence</xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <xsl:variable name="tempChildLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName" select="$capName"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="childLevelNo">
    <xsl:choose>
      <xsl:when test="count($EvidenceEntity/Relationships/Parent)=0 and count($EvidenceEntity/Relationships/MandatoryParents)=0">0</xsl:when>
      <xsl:when test="$tempChildLevelNo=0 and $EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0 and $assocInd='No'">1</xsl:when>
      <xsl:otherwise><xsl:value-of select="$tempChildLevelNo"/></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="evidenceListLayout">
    <xsl:choose>
      <xsl:when test="$childLevelNo=1">Evidence_listLayoutChild.vim</xsl:when>
      <xsl:when test="$childLevelNo=2">Evidence_listLayoutGrandChild.vim</xsl:when>
      <xsl:when test="$childLevelNo=3">Evidence_listLayoutGreatGrandChild.vim</xsl:when>
      <xsl:otherwise>Evidence_listLayout.vim</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="siteMap">
    <xsl:choose>
      <xsl:when test="$caseType=PRODUCT">ICSample_sportingGrantSiteMap</xsl:when>
      <xsl:otherwise>ICSample_sportingGrantSiteMap</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="defaultPageParams">
    <PAGE_PARAMETER NAME="caseID"/>
            <PAGE_PARAMETER NAME="contextDescription"/>
    <xsl:comment>
    No need for parEvID and parEvType as evidenceID and evidenceType
    would be duplicates of these values.  This is the case simply because
    the evidence infrastructure list layout VIM files require evidenceID
    and evidenceType (which it considers the parent's).
    </xsl:comment>
    <PAGE_PARAMETER NAME="evidenceID"/>
    <PAGE_PARAMETER NAME="evidenceType"/>
  </xsl:variable>

  <redirect:write select="concat($filepath, '.uim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<xsl:choose>
  <xsl:when test="$EvidenceEntity/UserInterfaceLayer/@UIType=$UITypeSummary">

      <PAGE PAGE_ID="{$UIName}" WINDOW_OPTIONS="width=900" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


        <PAGE_TITLE>
          <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="contextDescription"/>
          </CONNECT>
        </PAGE_TITLE>


        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="DISPLAY" OPERATION="{$facadeListMethod}"/>

        <xsl:copy-of select="$defaultPageParams"/>

        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="DISPLAY" PROPERTY="key$caseID"/>
        </CONNECT>

        <CONNECT>
          <SOURCE PROPERTY="{$capName}.EvidenceType" NAME="CONSTANT"/>
          <TARGET PROPERTY="key$evidenceType" NAME="DISPLAY"/>
        </CONNECT>

        <LIST>


          <CONTAINER LABEL="Container.Label.Action" SEPARATOR="Container.Separator" WIDTH="15">


            <ACTION_CONTROL LABEL="ActionControl.Label.View">
              <LINK PAGE_ID="">
              </LINK>
            </ACTION_CONTROL>


          </CONTAINER>


          <FIELD LABEL="Field.Label.Participant" WIDTH="25">
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$concernRoleName"/>
            </CONNECT>
          </FIELD>


          <FIELD LABEL="Field.Label.StartDate" WIDTH="25">
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$startDate"/>
            </CONNECT>
          </FIELD>

          <FIELD LABEL="Field.Label.EndDate" WIDTH="25">
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$endDate"/>
            </CONNECT>
          </FIELD>



        </LIST>

        <ACTION_SET TOP="false" ALIGNMENT="CENTER">
          <ACTION_CONTROL IMAGE="CloseButton" LABEL="ActionControl.Label.Close">

          </ACTION_CONTROL>
        </ACTION_SET>



      </PAGE>

    </xsl:when>
  <xsl:otherwise>

    <PAGE PAGE_ID="{$UIName}" WINDOW_OPTIONS="width=900" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


      <PAGE_TITLE>
        <CONNECT>
          <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="contextDescription"/>
        </CONNECT>
      </PAGE_TITLE>


      <SERVER_INTERFACE CLASS="Evidence" NAME="DISPLAY" OPERATION="listEvidence"/>

      <CONNECT>
        <SOURCE PROPERTY="{$capName}.EvidenceType" NAME="CONSTANT"/>
        <TARGET PROPERTY="key$evidenceType" NAME="DISPLAY"/>
      </CONNECT>

      <INCLUDE FILE_NAME="{$prefix}_{$caseType}Tab_content.vim"/>


      <xsl:copy-of select="$defaultPageParams"/>

      <!-- No need for parEvID and parEvType as evidenceID and evidenceType
           would be duplicates of these values.  This is the case simply because
           the evidence infrastructure list layout VIM files require evidenceID
           and evidenceType (which it considers the parent's).  -->
      <xsl:if test="$childLevelNo&gt;1">
        <PAGE_PARAMETER NAME="parEvID"/>
        <PAGE_PARAMETER NAME="parEvType"/>
      </xsl:if>
      <xsl:if test="$childLevelNo&gt;1">
        <PAGE_PARAMETER NAME="grandParEvID"/>
        <PAGE_PARAMETER NAME="grandParEvType"/>
      </xsl:if>
      <xsl:if test="$childLevelNo&gt;2">
        <PAGE_PARAMETER NAME="greatGrandParEvID"/>
        <PAGE_PARAMETER NAME="greatGrandParEvType"/>
      </xsl:if>

      <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="caseID"/>
        <TARGET NAME="DISPLAY" PROPERTY="key$caseID"/>
      </CONNECT>


      <xsl:if test="$childLevelNo&gt;0 or (count($EvidenceEntity/Relationships/Association[@from!=''])>0 and $assocInd!='Yes')">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
          <TARGET NAME="DISPLAY" PROPERTY="key$parentEvidenceID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
          <TARGET NAME="DISPLAY" PROPERTY="key$parentEvidenceType"/>
        </CONNECT>
      </xsl:if>

      <!--
      <MENU MODE="NAVIGATION">
        <ACTION_CONTROL LABEL="Field.Label.SiteMap">
          <LINK PAGE_ID="{$siteMap}">
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
          </LINK>
        </ACTION_CONTROL>
      </MENU>
      -->

      <CLUSTER NUM_COLS="2" SHOW_LABELS="false">

      <xsl:variable name="createLink">
        <LINK PAGE_ID="{$createUIName}" OPEN_MODAL="true">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="contextDescription"/>
            <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        <xsl:if test="count($EvidenceEntity/Relationships/Parent)&gt;0 or count($EvidenceEntity/Relationships/MandatoryParents/Parent)&gt;0">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="parEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="parEvType"/>
          </CONNECT>
        </xsl:if>
        <xsl:if test="$childLevelNo&gt;1">
          <!-- BEGIN, CR00114602, POB -->
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
          </CONNECT>
          <!-- END, CR00114602 -->
        </xsl:if>
        <xsl:if test="$childLevelNo&gt;2">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="grandParEvID"/>
            <TARGET NAME="PAGE" PROPERTY="greatGrandParEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="grandParEvType"/>
            <TARGET NAME="PAGE" PROPERTY="greatGrandParEvType"/>
          </CONNECT>
        </xsl:if>
        <xsl:if test="(count($EvidenceEntity/Relationships/Association[@from!=''])>0 and $assocInd!='Yes')">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="linkedEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="linkedEvType"/>
          </CONNECT>
        </xsl:if>
        <xsl:if test="$relatedEvidenceInd='Yes'">
          <CONNECT>
            <SOURCE NAME="CONSTANT" PROPERTY="{$capName}.EvidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
          </CONNECT>
        </xsl:if>
        </LINK>
      </xsl:variable>

      <xsl:variable name="validateLink">
        <LINK PAGE_ID="{$validateUIName}">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="contextDescription"/>
            <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        <xsl:if test="count($EvidenceEntity/Relationships/Parent)&gt;0 or (count($EvidenceEntity/Relationships/Association[@from!=''])>0 and $assocInd!='Yes')">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="parEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="parEvType"/>
          </CONNECT>
        </xsl:if>
        <xsl:if test="$childLevelNo&gt;1">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
          </CONNECT>
        </xsl:if>
        <xsl:if test="$childLevelNo&gt;2">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="grandParEvID"/>
            <TARGET NAME="PAGE" PROPERTY="greatGrandParEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="grandParEvType"/>
            <TARGET NAME="PAGE" PROPERTY="greatGrandParEvType"/>
          </CONNECT>
        </xsl:if>
        </LINK>
      </xsl:variable>


        <CONTAINER>
          <ACTION_CONTROL IMAGE="NewProduct" LABEL="Field.Label.New">
            <xsl:copy-of select="$createLink"/>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="Field.Label.New">
            <xsl:copy-of select="$createLink"/>
          </ACTION_CONTROL>
        </CONTAINER>


        <CONTAINER>

          <ACTION_CONTROL IMAGE="NewCaseMember" LABEL="Field.Label.Validate">
            <xsl:copy-of select="$validateLink"/>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="Field.Label.Validate">
            <xsl:copy-of select="$validateLink"/>
          </ACTION_CONTROL>
        </CONTAINER>
      </CLUSTER>


      <INCLUDE FILE_NAME="{$evidenceListLayout}"/>


    </PAGE>


  </xsl:otherwise>
</xsl:choose>



  </redirect:write>

<!-- BEGIN, PADDY -->
  <xsl:call-template name="write-all-locales-list-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
    <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
    <xsl:with-param name="parentName" select="$parentName"/>
  </xsl:call-template>
  <!-- END, PADDY -->
</xsl:template>

<!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-list-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
       <xsl:param name="EvidenceEntity"/>
      <xsl:param name="parentName"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-list-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
               <xsl:with-param name="parentName" select="$parentName"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-list-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
               <xsl:with-param name="parentName" select="$parentName"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-list-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
               <xsl:with-param name="parentName" select="$parentName"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-list-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
         <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
               <xsl:with-param name="parentName" select="$parentName"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>

    <xsl:template name="write-list-properties">

        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>
        <xsl:param name="EvidenceEntity"/>
      <xsl:param name="parentName"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

<redirect:write select="concat($filepath, $locale, '.properties')">

<xsl:if test="count($generalProperties/Help.PageDescription.List.EvidenceEntities)&gt;0"><xsl:call-template name="callGenerateProperties">
        <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.List.EvidenceEntities"/>
        <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
	    <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
      </xsl:call-template>
    </xsl:if>

<xsl:if test="count($generalProperties/Page.Title.EntityWorkspace)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.EntityWorkspace"/>
  <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
</xsl:call-template>
<xsl:text>&#xa;</xsl:text>
</xsl:if>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.New"/>
  <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.Validate"/>
  <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.SiteMap"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

  </redirect:write>
    </xsl:if>
    </xsl:template>

</xsl:stylesheet>
