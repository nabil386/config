<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2006, 2017. All Rights Reserved.

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
<xsl:import href="UICommon.xslt"/>
<!--<xsl:import href="CreateProperties.xslt"/>-->
<xsl:output method="xml" indent="yes"/>


<xsl:template name="CreateUI">

  <xsl:param name="path"/>
  <xsl:param name="UIName"/>

  <xsl:param name="capName"/>
  <xsl:param name="parents"/>
  <xsl:param name="firstParentName" />

  <xsl:param name="childUIInd"/>

  <xsl:param name="fromParentName"/>

  <!-- BEGIN, CR00101875, POB -->
  <xsl:param name="fromChildCreate"/>
  <!-- END, CR00101875 -->


  <!-- Evidence Entity Specific Variables -->
  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>

  <!-- BEGIN, CR00098559, POB -->
  <xsl:variable name="createContentUIName"><xsl:choose>
    <xsl:when test="$fromParentName=''"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>_content</xsl:when>
        <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>From<xsl:value-of select="$fromParentName"/>_content</xsl:otherwise></xsl:choose></xsl:variable>
  <!-- END, CR00098559, POB -->

  <!-- BEGIN, CR00101739, CH -->
    <xsl:variable name="preAssocCreatePage"><xsl:value-of select="$prefix"/>_select<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>PreAssociation</xsl:variable>
  <!-- END, CR00101739 -->

<!-- BEGIN, CR00101875, POB  -->
  <xsl:variable name="listUIName"><xsl:choose>
    <xsl:when test="$fromChildCreate!=''"><xsl:value-of select="$prefix"/>_resolveCreate<xsl:value-of select="$fromChildCreate"/><xsl:value-of select="$caseType"/>FromParentCreate</xsl:when>
    <xsl:otherwise><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>List</xsl:otherwise>
    </xsl:choose></xsl:variable>
 <!-- END, CR00101875 -->
  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>
  <!-- BEGIN,  CR00128375, POB -->
  <xsl:variable name="EmploymentType"><xsl:value-of select="$prefix"/>_listRelatedEntriesFor<xsl:value-of select="@name"/><xsl:value-of select="$caseType"/></xsl:variable>
  <!-- END, CR00128375 -->

  <xsl:variable name="childLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName" select="$capName"/>
    </xsl:call-template>
  </xsl:variable>

  <!-- BEGIN, CR00114711, CD -->
  <xsl:variable name="childButtons">
    <xsl:call-template name="GetChildButtons">
      <xsl:with-param name="capName" select="$capName"/>
      <xsl:with-param name="parents" select="$parents"/>
      <xsl:with-param name="fromChildCreate" select="$fromChildCreate"/>
      <xsl:with-param name="childLevelNo" select="$childLevelNo"/>
      <xsl:with-param name="childUIInd" select="$childUIInd"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="modal">No</xsl:with-param>
    </xsl:call-template>
  </xsl:variable>
  <!-- END, CR00114711 -->

  <redirect:write select="concat($filepath, '.uim')">
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE
  PAGE_ID="{$UIName}"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  WINDOW_OPTIONS="width=900"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>

<!-- BEGIN, CR00101875 -->
  <!--
    Need to store the other parents ID and type if being created from the childs create screen
  -->
  <xsl:if test="$fromChildCreate!=''">
  <PAGE_PARAMETER NAME="otherParentID"/>
  <PAGE_PARAMETER NAME="otherParentType"/>
  </xsl:if>
  <!-- END, CR00101875 -->

  <xsl:if test="$childLevelNo>=2">
    <PAGE_PARAMETER NAME="grandParEvID"/>
    <PAGE_PARAMETER NAME="grandParEvType"/>
  </xsl:if>
  <xsl:if test="$childLevelNo>=3">
    <PAGE_PARAMETER NAME="greatGrandParEvID"/>
    <PAGE_PARAMETER NAME="greatGrandParEvType"/>
  </xsl:if>

  <xsl:if test="count($EvidenceEntity/Relationships/Parent)>0 or ($EvidenceEntity/Relationships/@association='Yes' and count($EvidenceEntity/Relationships/Association[@from!=''])>0)">
    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
      <TARGET NAME="ACTION" PROPERTY="{$facadeParentEvidenceAgg}${$evidenceID}"/>
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
      <TARGET NAME="ACTION" PROPERTY="{$facadeParentEvidenceAgg}${$evidenceType}"/>
    </CONNECT>
    </xsl:if>

  <xsl:if test="$EvidenceEntity/Relationships/@exposeOperation='Yes'">

      <xsl:if test="count(../Parent)>0">

      <CONNECT>
        <SOURCE PROPERTY="parEvID" NAME="PAGE"/>
        <TARGET PROPERTY="evidenceKey$evidenceID" NAME="RELATEDATTRIBUTES"/>
      </CONNECT>
      <!--
      <CONNECT>
        <SOURCE PROPERTY="parEvType" NAME="PAGE"/>
        <TARGET PROPERTY="evType" NAME="DISPLAY"/>
      </CONNECT>
      -->
      </xsl:if>

  </xsl:if>

  <!-- BEGIN, CR00221984, CD -->
  <!-- Removing duplicate action set -->
  <!-- END, CR00221984, CD -->

  <INCLUDE FILE_NAME="{$createContentUIName}.vim"/>

    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="false"
    >


      <ACTION_CONTROL
        IMAGE="SaveButton"
        LABEL="ActionControl.Label.Save"
        TYPE="SUBMIT"
      >
        <LINK
          PAGE_ID="{$listUIName}"
          SAVE_LINK="false"
        >
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromChildCreate!=''">
            <xsl:attribute name="DISMISS_MODAL">false</xsl:attribute>
          </xsl:if>
          <!-- END, CR00101875 -->
          <CONNECT>
            <SOURCE PROPERTY="caseID" NAME="PAGE"  />
            <TARGET PROPERTY="caseID" NAME="PAGE"  />
          </CONNECT>
          <CONNECT>
            <SOURCE PROPERTY="contextDescription" NAME="PAGE"/>
            <TARGET PROPERTY="contextDescription" NAME="PAGE"/>
          </CONNECT>
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromChildCreate!=''">
            <CONNECT>
              <SOURCE PROPERTY="otherParentID" NAME="PAGE"  />
              <TARGET PROPERTY="parEvID" NAME="PAGE"  />
            </CONNECT>
            <CONNECT>
              <SOURCE PROPERTY="otherParentType" NAME="PAGE"/>
              <TARGET PROPERTY="parEvType" NAME="PAGE"/>
            </CONNECT>
          </xsl:if>
          <!-- END, CR00101875 -->
          <xsl:if test="$fromParentName!='' or $childLevelNo>=1 or $childUIInd='Yes'">
    <CONNECT>
      <SOURCE PROPERTY="parEvID" NAME="PAGE"/>
      <TARGET PROPERTY="evidenceID" NAME="PAGE"/>
    </CONNECT>
    <CONNECT>
      <SOURCE PROPERTY="parEvType" NAME="PAGE"/>
      <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
    </CONNECT>
  </xsl:if>
  <xsl:if test="$childLevelNo>=2">
    <CONNECT>
      <SOURCE PROPERTY="grandParEvID" NAME="PAGE"/>
      <TARGET PROPERTY="parEvID" NAME="PAGE"/>
    </CONNECT>
    <CONNECT>
      <SOURCE PROPERTY="grandParEvType" NAME="PAGE"/>
      <TARGET PROPERTY="parEvType" NAME="PAGE"/>
    </CONNECT>
  </xsl:if>
  <xsl:if test="$childLevelNo>=3">
    <CONNECT>
      <SOURCE PROPERTY="greatGrandParEvID" NAME="PAGE"/>
      <TARGET PROPERTY="grandParEvID" NAME="PAGE"/>
    </CONNECT>
    <CONNECT>
      <SOURCE PROPERTY="greatGrandParEvType" NAME="PAGE"/>
      <TARGET PROPERTY="grandParEvType" NAME="PAGE"/>
    </CONNECT>
  </xsl:if>
        </LINK>
      </ACTION_CONTROL>

<xsl:if test="$EvidenceEntity/UserInterfaceLayer/@saveAndNewButton='Yes'">
      <ACTION_CONTROL
        IMAGE="SaveAndNewButton"
        LABEL="ActionControl.Label.SaveAndNew"
        TYPE="SUBMIT"
      >
      <xsl:choose>
        <xsl:when test="Relationships/Related/@to='Employment'">
          <LINK PAGE_ID="{$EmploymentType}" DISMISS_MODAL="FALSE">
            <CONNECT>
              <SOURCE
                NAME="ACTION"
                PROPERTY="{$facadeCaseIDAgg}$caseID"
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
              NAME="ACTION"
                PROPERTY="{$facadeReturnDetails}${$facadeEvidenceAgg}${$evidenceType}"
              />
              <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
              />
            </CONNECT>
            <xsl:if test="$childUIInd='Yes'">
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
          </LINK>
        </xsl:when>
        <!-- BEGIN, CR00101739, CH -->
        <xsl:when test="Relationships/@preAssociation='Yes'">
          <LINK PAGE_ID="{$preAssocCreatePage}" DISMISS_MODAL="FALSE">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="caseID"/>
              <TARGET NAME="PAGE" PROPERTY="caseID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
              <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
            </CONNECT>
            <xsl:if test="$childUIInd='Yes'">
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
    </LINK>
        </xsl:when>
        <!-- END, CR00101739 -->
        <xsl:otherwise>

        <LINK PAGE_ID="{$UIName}">
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromChildCreate!=''">
            <xsl:attribute name="DISMISS_MODAL">false</xsl:attribute>
          </xsl:if>
          <!-- END, CR00101875 -->
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="{$facadeCaseIDAgg}$caseID"
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
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromChildCreate!=''">
            <CONNECT>
              <SOURCE PROPERTY="otherParentID" NAME="PAGE"  />
              <TARGET PROPERTY="parEvID" NAME="PAGE"  />
            </CONNECT>
            <CONNECT>
              <SOURCE PROPERTY="otherParentType" NAME="PAGE"/>
              <TARGET PROPERTY="parEvType" NAME="PAGE"/>
            </CONNECT>
          </xsl:if>
          <!-- END, CR00101875 -->
  <xsl:if test="$childLevelNo>=1 or $childUIInd='Yes'">
    <CONNECT>
      <SOURCE PROPERTY="parEvID" NAME="PAGE"/>
      <TARGET PROPERTY="parEvID" NAME="PAGE"/>
    </CONNECT>
    <CONNECT>
      <SOURCE PROPERTY="parEvType" NAME="PAGE"/>
      <TARGET PROPERTY="parEvType" NAME="PAGE"/>
    </CONNECT>
  </xsl:if>
  <xsl:if test="$childLevelNo>=2">
    <CONNECT>
      <SOURCE PROPERTY="grandParEvID" NAME="PAGE"/>
      <TARGET PROPERTY="grandParEvID" NAME="PAGE"/>
    </CONNECT>
    <CONNECT>
      <SOURCE PROPERTY="grandParEvType" NAME="PAGE"/>
      <TARGET PROPERTY="grandParEvType" NAME="PAGE"/>
    </CONNECT>
  </xsl:if>
  <xsl:if test="$childLevelNo>=3">
    <CONNECT>
      <SOURCE PROPERTY="greatGrandParEvID" NAME="PAGE"/>
      <TARGET PROPERTY="greatGrandParEvID" NAME="PAGE"/>
    </CONNECT>
    <CONNECT>
      <SOURCE PROPERTY="greatGrandParEvType" NAME="PAGE"/>
      <TARGET PROPERTY="greatGrandParEvType" NAME="PAGE"/>
    </CONNECT>
  </xsl:if>
        </LINK>
        </xsl:otherwise>
      </xsl:choose>
      </ACTION_CONTROL>
</xsl:if>

      <ACTION_CONTROL
        IMAGE="CancelButton"
        LABEL="ActionControl.Label.Cancel"
        >
        <LINK
          PAGE_ID="{$listUIName}"
          SAVE_LINK="false"
          >
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromChildCreate!=''">
            <xsl:attribute name="DISMISS_MODAL">false</xsl:attribute>
          </xsl:if>
          <!-- END, CR00101875 -->
          <CONNECT>
            <SOURCE PROPERTY="caseID" NAME="PAGE"  />
            <TARGET PROPERTY="caseID" NAME="PAGE"  />
          </CONNECT>
          <CONNECT>
            <SOURCE PROPERTY="contextDescription" NAME="PAGE"/>
            <TARGET PROPERTY="contextDescription" NAME="PAGE"/>
          </CONNECT>
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromChildCreate!=''">
            <CONNECT>
              <SOURCE PROPERTY="otherParentID" NAME="PAGE"  />
              <TARGET PROPERTY="parEvID" NAME="PAGE"  />
            </CONNECT>
            <CONNECT>
              <SOURCE PROPERTY="otherParentType" NAME="PAGE"/>
              <TARGET PROPERTY="parEvType" NAME="PAGE"/>
            </CONNECT>
          </xsl:if>
          <!-- END, CR00101875 -->
          <xsl:if test="$fromParentName!='' or $childLevelNo>=1 or $childUIInd='Yes'">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
            </CONNECT>
          </xsl:if>
          <xsl:if test="$childLevelNo>=2">
            <CONNECT>
              <SOURCE PROPERTY="grandParEvID" NAME="PAGE"/>
              <TARGET PROPERTY="parEvID" NAME="PAGE"/>
            </CONNECT>
            <CONNECT>
              <SOURCE PROPERTY="grandParEvType" NAME="PAGE"/>
              <TARGET PROPERTY="parEvType" NAME="PAGE"/>
            </CONNECT>
          </xsl:if>
          <xsl:if test="$childLevelNo>=3">
            <CONNECT>
              <SOURCE PROPERTY="greatGrandParEvID" NAME="PAGE"/>
              <TARGET PROPERTY="grandParEvID" NAME="PAGE"/>
            </CONNECT>
            <CONNECT>
              <SOURCE PROPERTY="greatGrandParEvType" NAME="PAGE"/>
              <TARGET PROPERTY="grandParEvType" NAME="PAGE"/>
            </CONNECT>
          </xsl:if>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


  <!-- BEGIN, CR00112474, DG -->
  <xsl:copy-of select="$childButtons"/>
  <!-- END, CR00112474 -->


</PAGE>

  </redirect:write>

  <!-- BEGIN, PADDY -->
  <xsl:call-template name="write-all-locales-create-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
    <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
  </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>


<!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-create-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
       <xsl:param name="EvidenceEntity"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-create-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-create-properties">
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
             <xsl:call-template name="write-create-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-create-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
         <xsl:with-param name="EvidenceEntity"
                              select="$EvidenceEntity"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>


    <xsl:template name="write-create-properties">

      <xsl:param name="locale"/>
      <xsl:param name="filepath"/>
      <xsl:param name="EvidenceEntity"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <redirect:write select="concat($filepath, $locale, '.properties')">
      <xsl:if test="count($generalProperties/Help.PageDescription.CreateEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.CreateEntity"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
	  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
    </xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/Page.Title.NewEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.NewEntity"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
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

<!--        <xsl:if test="$EvidenceEntity/UserInterfaceLayer/@saveAndNewButton='Yes'">-->
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndNew"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
<!--        </xsl:if> -->

        <xsl:if test="count($EvidenceEntity/Relationships/Child)>0">

          <xsl:for-each select="$EvidenceEntity/Relationships/Child[@createButton='Yes']">

            <xsl:variable name="childLogicalName" select="@name"/>

    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAddButton"/>
      <xsl:with-param name="altPropertyName">ActionControl.Label.SaveAndAdd<xsl:value-of select="$childLogicalName"/>Button</xsl:with-param>
      <xsl:with-param name="evidenceNode" select="../../../EvidenceEntity[@name=$childLogicalName]"/>
    </xsl:call-template>

          </xsl:for-each>

        </xsl:if>


        <xsl:for-each select="$EvidenceEntity/Relationships/Association[@createButton='Yes']">
          <xsl:if test="@from!=''">
            <xsl:variable name="childLogicalName" select="@from"/>
            <xsl:call-template name="callGenerateProperties">
              <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAssociateButton"/>
              <xsl:with-param name="altPropertyName">ActionControl.Label.SaveAndAssociate<xsl:value-of select="$childLogicalName"/>Button</xsl:with-param>
              <xsl:with-param name="evidenceNode" select="../../../EvidenceEntity[@name=$childLogicalName]"/>
            </xsl:call-template>
          </xsl:if>

          <xsl:if test="@to!=''">
            <xsl:variable name="childLogicalName" select="@to"/>
            <xsl:call-template name="callGenerateProperties">
              <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAssociateButton"/>
              <xsl:with-param name="altPropertyName">ActionControl.Label.SaveAndAssociate<xsl:value-of select="$childLogicalName"/>Button</xsl:with-param>
              <xsl:with-param name="evidenceNode" select="../../../EvidenceEntity[@name=$childLogicalName]"/>
            </xsl:call-template>
          </xsl:if>
        </xsl:for-each>

        <!-- BEGIN, CR00113102, DG -->
        <xsl:if test="(count($EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink)&gt;0)">

          <xsl:variable name="linkPropertyName">
            <xsl:call-template name="replaceAll">
              <xsl:with-param name="stringToTransform" select="$EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink/@displayName"/>
              <xsl:with-param name="target" select="' '"/>
              <xsl:with-param name="replacement" select="''"/>
            </xsl:call-template>
          </xsl:variable>

          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAddButton"/>
            <xsl:with-param name="altPropertyName">ActionControl.Label.SaveAndAdd<xsl:value-of select="$linkPropertyName"/>Button</xsl:with-param>
            <xsl:with-param name="evidenceNode" select="$EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink"/>

          </xsl:call-template>
        </xsl:if>

        <!-- END, CR00113102 -->

  </redirect:write>
  </xsl:if>
    </xsl:template>

</xsl:stylesheet>
