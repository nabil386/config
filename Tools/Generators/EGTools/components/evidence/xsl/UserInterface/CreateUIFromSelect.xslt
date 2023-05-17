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
<xsl:import href="UICommon.xslt"/>
<!--<xsl:import href="CreateProperties.xslt"/>-->
<xsl:output method="xml" indent="yes"/>


<xsl:template name="CreateUIFromSelect">

  <xsl:param name="path"/>
  <xsl:param name="UIName"/>

  <xsl:param name="capName"/>
  <xsl:param name="parents"/>
  <xsl:param name="firstParentName" />

  <xsl:param name="childUIInd"/>
  <xsl:param name="linkedPage" />
  
  <!-- BEGIN, CR00128375, POB -->
  <xsl:param name="fileAppend"/>
  <!-- END, CR00128375 -->

  <!-- Evidence Entity Specific Variables -->
  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>

  <xsl:variable name="createContentUIName"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>_content</xsl:variable>

  <xsl:variable name="listUIName">
    <xsl:variable name="ParentEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$firstParentName]"/>  
    <xsl:choose>
      <xsl:when test="count($ParentEvidenceEntity/Relationships/Parent)>1"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$firstParentName"/>EvidenceList</xsl:when>  
      <xsl:when test="$ParentEvidenceEntity/Relationships/@association='Yes' and count($ParentEvidenceEntity/Relationships/Association[@from!=''])>1"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$firstParentName"/>EvidenceList</xsl:when>  
      <xsl:otherwise><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$firstParentName"/><xsl:value-of select="$caseType"/></xsl:otherwise>
    </xsl:choose>    
  </xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>
  <!-- BEGIN,  CR00128375, POB -->
  <xsl:variable name="EmploymentType"><xsl:value-of select="$prefix"/>_listRelatedEntriesFor<xsl:value-of select="@name"/><xsl:value-of select="$caseType"/><xsl:value-of select="$fileAppend"/></xsl:variable>
  <xsl:variable name="preAssocCreatePage"><xsl:value-of select="$prefix"/>_select<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>PreAssociation</xsl:variable>
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
      <xsl:with-param name="childLevelNo" select="$childLevelNo"/>
      <xsl:with-param name="childUIInd" select="$childUIInd"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="modal">Yes</xsl:with-param>
    </xsl:call-template>
  </xsl:variable>
  <!-- END, CR00114711 -->
  
  <!-- BEGIN, CR00113102, DG-->
  <xsl:variable name="saveAddButtons">
    <ACTION_SET ALIGNMENT="CENTER" BOTTOM="false">

      <ACTION_CONTROL
        LABEL="ActionControl.Label.Save"
        TYPE="SUBMIT"
      >
        <LINK 
          PAGE_ID="{$linkedPage}"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE PROPERTY="caseID" NAME="PAGE"  />
            <TARGET PROPERTY="caseID" NAME="PAGE"  />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00114587, POB -->
      <xsl:if test="$EvidenceEntity/UserInterfaceLayer/@saveAndNewButton='Yes'">
        <!-- END, CR00114587 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.SaveAndNew"
        TYPE="SUBMIT"
      >
        <!-- BEGIN, CR00128375 , POB -->
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
          <xsl:when test="Relationships/@preAssociation='Yes'">
            <LINK PAGE_ID="{$preAssocCreatePage}">
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                <TARGET NAME="PAGE" PROPERTY="caseID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
                <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
              </CONNECT>
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="evidenceType"
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
          <xsl:otherwise>
            
            <LINK PAGE_ID="{$UIName}">
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
                  NAME="PAGE"
                  PROPERTY="evidenceType"
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
          </xsl:otherwise>
        </xsl:choose>
        <!-- END, CR00128375 -->
      </ACTION_CONTROL>
        <!-- BEGIN, CR00114587, POB -->
      </xsl:if>
      <!-- END, CR00114587 -->
      
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Cancel"
        >
        <LINK 
          PAGE_ID="{$linkedPage}"
          SAVE_LINK="false"
          >
          <CONNECT>
            <SOURCE PROPERTY="caseID" NAME="PAGE"  />
            <TARGET PROPERTY="caseID" NAME="PAGE"  />
          </CONNECT>   
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
  </xsl:variable>
  
  <xsl:variable name="saveAddExternalLink">

    <CLUSTER STYLE="outer-cluster-borderless">

      <ACTION_SET ALIGNMENT="CENTER" BOTTOM="false">

        <xsl:for-each select="$EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink">
        <!-- create a variable of the display name with spaces removed.-->
        <xsl:variable name="linkName">
          <xsl:call-template name="replaceAll">
            <xsl:with-param name="stringToTransform" select="@displayName"/>
            <xsl:with-param name="target" select="' '"/>
            <xsl:with-param name="replacement" select="''"/>
          </xsl:call-template>
        </xsl:variable>
        <ACTION_CONTROL
          IMAGE="SaveAndAdd{$linkName}Button"
          LABEL="ActionControl.Label.SaveAndAdd{$linkName}Button"
          TYPE="SUBMIT"
        >
          <LINK SAVE_LINK="false" PAGE_ID="{@pageName}">
            <xsl:for-each select="Parameters/Parameter">
              <CONNECT>
                <SOURCE NAME="{@source}" PROPERTY="{@from}" />
                <TARGET NAME="PAGE" PROPERTY="{@to}" />
              </CONNECT>
            </xsl:for-each>
          </LINK>
        </ACTION_CONTROL>
      </xsl:for-each>
      </ACTION_SET>
    </CLUSTER>
  </xsl:variable>
  <!-- END, CR00113102 -->
  
  
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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>

  <!-- BEGIN, CR00113102 -->
  <xsl:copy-of select="$saveAddButtons"/>  
  
  <!-- BEGIN, CR00114711, CD -->
  <xsl:copy-of select="$childButtons"/>
  <!-- END, CR00114711 -->
      
  <!-- BEGIN, CR00221984, CD -->
  <!-- Removing duplicate saveAndAddExternal action set -->
  <!-- END, CR00221984, CD -->
  
  <!-- END, CR00113102 -->
  
  <INCLUDE FILE_NAME="{$createContentUIName}.vim"/>
  
  <!-- This is specifically to search for parents of a child evidence type -->
  <PAGE_PARAMETER NAME="evidenceType"/>
  
  <xsl:if test="count($EvidenceEntity/Relationships/Parent)>=1">
  <SERVER_INTERFACE 
    CLASS="{$prefix}{$facadeClass}" 
    NAME="DISPLAY" 
    OPERATION="getParentEvidenceList" />

  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID" />
    <TARGET NAME="DISPLAY" PROPERTY="caseID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="evidenceType" />
    <TARGET NAME="DISPLAY" PROPERTY="key$evidenceType" />
  </CONNECT>


  <LIST 
    TITLE="List.Title.SelectParentEvidence" 
    DESCRIPTION="List.Description.SelectParentEvidence">

    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="5"
    >
      <WIDGET TYPE="SINGLESELECT">
        <WIDGET_PARAMETER NAME="SELECT_SOURCE">
          <CONNECT>
            <SOURCE PROPERTY="parentList$dtls$evidenceID" NAME="DISPLAY"/>
          </CONNECT>
          <CONNECT>
            <SOURCE PROPERTY="parentList$dtls$evidenceType" NAME="DISPLAY"/>
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="SELECT_TARGET">
          <CONNECT>
            <TARGET PROPERTY="selectedParent$tabbedDetails" NAME="ACTION"/>
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>    
    <FIELD LABEL="List.Title.InEditIndication" WIDTH="5">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="parentList$dtls$wipPendingCode" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="List.Title.StatusCode" WIDTH="10">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="parentList$dtls$statusCode" />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.ID"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="parentList$dtls$correctionSetID"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Type"
      WIDTH="16"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="parentList$dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="parentList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="parentList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Details"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="parentList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  </xsl:if>
  
  <xsl:if test="(count($EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink)&gt;0)">
    <xsl:copy-of select="$saveAddExternalLink"/>
  </xsl:if>
  <!-- END, CR00113102 -->
</PAGE>

  </redirect:write>
  
  <!-- BEGIN, PADDY -->
    <!-- View Content Vim Properties -->
    <xsl:call-template name="write-all-locales-create-from-select-properties">   
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="filepath" select="$filepath"/>
      <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
    </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>

<!-- BEGIN, PADDY -->
  
  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-create-from-select-properties">
       
       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
       <xsl:param name="EvidenceEntity"/>
       
       <!--tokens still exist-->
       <xsl:if test="$locales">               
         
         <xsl:choose>
         
           <!--more than one-->
           <xsl:when test="contains($locales,',')">
             
             <xsl:call-template name="write-create-from-select-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
             
             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-create-from-select-properties">   
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
             
           </xsl:when>
           
           <!--only one token left-->
           <xsl:otherwise>
           
             <!-- Call for the final locale -->
             <xsl:call-template name="write-create-from-select-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
           
             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-create-from-select-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
         <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
           
           </xsl:otherwise>
         
         </xsl:choose>
       
       </xsl:if>
    
    </xsl:template>  
    
    <xsl:template name="write-create-from-select-properties">
          
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

 
<!--    <xsl:if test="$EvidenceEntity/UserInterfaceLayer/@saveAndNewButton='Yes'">-->
 
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndNew"/>
  <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
</xsl:call-template>
  
<!--    </xsl:if> -->


    <xsl:if test="count($EvidenceEntity/Relationships/Child)>0">

      <xsl:for-each select="$EvidenceEntity/Relationships/Child[@createButton='Yes']">
    
        <xsl:variable name="childLogicalName" select="@name"/>
        
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAddButton"/>
  <xsl:with-param name="evidenceNode" select="../../../EvidenceEntity[@name=$childLogicalName]"/>
</xsl:call-template>

      </xsl:for-each>
      

    </xsl:if>
 
    <xsl:for-each select="$EvidenceEntity/Relationships/Association[@createButton='Yes']">
    

      <xsl:if test="@from!=''">
      
        <xsl:variable name="childLogicalName" select="@from"/>
        
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAssociateFromButton"/>
  <xsl:with-param name="evidenceNode" select="../../../EvidenceEntity[@name=$childLogicalName]"/>
</xsl:call-template>

      </xsl:if>

    
    <xsl:if test="count($generalProperties/ActionControl.Label.SaveAndAssociateToButton)&gt;0">
      <xsl:if test="@to!=''">
      
        <xsl:variable name="childLogicalName" select="@to"/>
      
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAssociateToButton"/>
  <xsl:with-param name="evidenceNode" select="../../../EvidenceEntity[@name=$childLogicalName]"/>
</xsl:call-template>

      </xsl:if>
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
    
<xsl:if test="count($EvidenceEntity/Relationships/Parent)>0">   

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.SelectParentEvidence"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>  

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Description.SelectParentEvidence"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template> 

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/Container.Label.Action"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.InEditIndication"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.StatusCode"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.ID"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Type"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Name"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.EffectiveDate"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>

  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Details"/>
    <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
  </xsl:call-template>      
</xsl:if>    

<!-- BEGIN, CR00114711, CD -->
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
<!-- END, CR00114711 -->

  </redirect:write>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>