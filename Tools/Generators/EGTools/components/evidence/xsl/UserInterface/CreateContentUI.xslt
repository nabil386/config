<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2006-2008, 2010 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="UICommon.xslt"/>

  <xsl:output method="xml" indent="yes"/>


  <xsl:template name="CreateContentUI">
    
    <xsl:param name="EvidenceEntityElem"/>
    <xsl:param name="path"/>
    <xsl:param name="UIName"/>
    <xsl:param name="baseAggregation"/>
    <xsl:param name="fromParentName"/>

    <xsl:variable name="capName">
      <xsl:value-of select="$EvidenceEntityElem/@name"/>
    </xsl:variable>
    <xsl:variable name="facadeCreateMethod">create<xsl:value-of select="$capName"/>Evidence</xsl:variable>

    <xsl:variable name="filepath">
      <xsl:value-of select="$path"/>
      <xsl:value-of select="$UIName"/>
    </xsl:variable>

    <xsl:variable name="childLevelNo">
      <xsl:call-template name="GetChildLevel">
        <xsl:with-param name="capName" select="$capName"/>
      </xsl:call-template>
    </xsl:variable>
    <redirect:write select="concat($filepath, '.vim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

      <VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="ACTION"
          OPERATION="{$facadeCreateMethod}" PHASE="ACTION"/>

        <!-- BEGIN, CR00098722, CD -->
        <xsl:if test="$EvidenceEntityElem/Relationships/@exposeOperation='Yes'">

          <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
            OPERATION="get{$capName}RelatedEntityAttributes" NAME="RELATEDATTRIBUTES"/> 
                        
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="caseID"/>
          </CONNECT>          
          <xsl:if test="count(Relationships/Parent)>0 or (Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>0)">                        
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
            <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKey$evidenceID"/>
          </CONNECT>                        
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
            <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKey$evType"/>
          </CONNECT>          
          </xsl:if>
          
          <xsl:if test="$EvidenceEntityElem/Relationships/@preAssociation='Yes'">
          
            <xsl:choose>
              <xsl:when test="$EvidenceEntityElem/Relationships/@mulitplePreAssociationID='Yes' and count($EvidenceEntityElem/Relationships/PreAssociation[@name!='']) &gt; 0">
                <xsl:for-each select="$EvidenceEntityElem/Relationships/PreAssociation[@name!='']">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="{@name}PreAssocID"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKeyList"/>
            </CONNECT>                        
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="{@name}PreAssocType"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKeyList"/>
            </CONNECT>                                        
                </xsl:for-each>
              </xsl:when>
              <xsl:otherwise>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="preAssocID"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="preAssocKey$evidenceID"/>
            </CONNECT>                        
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="preAssocType"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="preAssocKey$evType"/>
            </CONNECT>                        
              </xsl:otherwise>
            </xsl:choose>

          </xsl:if>          
          
        </xsl:if>
        <!-- END, CR00098722 -->
        
        <PAGE_PARAMETER NAME="caseID"/>
        <xsl:if
          test="count($EvidenceEntityElem/Relationships/Parent)>0 or ($EvidenceEntityElem/Relationships/@association='Yes' and count($EvidenceEntityElem/Relationships/Association[@from!=''])>0) or count($EvidenceEntityElem/Relationships/MandatoryParents/Parent) &gt; 0">
          <PAGE_PARAMETER NAME="parEvType"/>
          <PAGE_PARAMETER NAME="parEvID"/>
        </xsl:if>
        <PAGE_PARAMETER NAME="contextDescription"/>
        
        <xsl:if test="$EvidenceEntityElem/Relationships/@preAssociation='Yes'">

          <xsl:choose>
            <xsl:when test="$EvidenceEntityElem/Relationships/@mulitplePreAssociationID='Yes' and count($EvidenceEntityElem/Relationships/PreAssociation[@name!='']) &gt; 0">
              <xsl:for-each select="$EvidenceEntityElem/Relationships/PreAssociation[@name!='']">        
          <PAGE_PARAMETER NAME="{@name}PreAssocID"/>
          <PAGE_PARAMETER NAME="{@name}PreAssocType"/>              
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{@name}PreAssocID"/>
            <TARGET NAME="ACTION" PROPERTY="{@name}PreAssocKey$evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{@name}PreAssocType"/>
            <TARGET NAME="ACTION" PROPERTY="{@name}PreAssocKey$evType"/>
          </CONNECT>          
              </xsl:for-each>
            </xsl:when>
            
            <xsl:otherwise>     
          <PAGE_PARAMETER NAME="preAssocID"/>
          <PAGE_PARAMETER NAME="preAssocType"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="preAssocID"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadePreAssociationAgg}$evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="preAssocType"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadePreAssociationAgg}$evType"/>
          </CONNECT>          
            </xsl:otherwise>
          </xsl:choose>            
          
        </xsl:if>

        <xsl:if test="$EvidenceEntityElem/Relationships/Related/@to='Employment'">
          <PAGE_PARAMETER NAME="employmentID"/>
          <PAGE_PARAMETER NAME="employerConcernRoleID"/>
          <PAGE_PARAMETER NAME="relEvidenceID"/>
          <PAGE_PARAMETER NAME="relEvidenceType"/>
          <PAGE_PARAMETER NAME="concernRoleID"/>
          <PAGE_PARAMETER NAME="caseParticipantRoleID"/>

          <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
            OPERATION="readNameByCaseParticipantRole" NAME="PARTICIPANTDETAILS"/>
            
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PARTICIPANTDETAILS" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
            <TARGET NAME="PARTICIPANTDETAILS" PROPERTY="key$caseParticipantRoleID"/>
          </CONNECT>      
          <CONNECT>
            <SOURCE PROPERTY="employerConcernRoleID" NAME="PAGE"/>
            <TARGET PROPERTY="empCaseParticipantDetails$participantRoleID" NAME="ACTION"/>
          </CONNECT>
            
        </xsl:if>

        <!-- For each optional Page Parameter create link -->
        <xsl:for-each select="$EvidenceEntityElem/UserInterfaceLayer/PageParams/PageParam">
          <PAGE_PARAMETER NAME="{To/@property}"/>
        </xsl:for-each>
        
        <!-- BEGIN, CR00098559, POB -->
        <!--
          Changed to a choose operation to allow for different connect statements for multiple mandatory parents
        -->
        <xsl:choose>
          <xsl:when test="count($EvidenceEntityElem/Relationships/Parent)>0">
        </xsl:when>
          <xsl:when test="count($EvidenceEntityElem/Relationships/MandatoryParents/Parent)&gt;0">
            
            <xsl:if test="$fromParentName!=''">
            <xsl:variable name="lcFromParentName"><xsl:value-of select="translate(substring($fromParentName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($fromParentName, 2)"/></xsl:variable>
            <!--
              Connect the page parameters to the relevant ParEVKey 
            -->
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
              <TARGET NAME="ACTION" PROPERTY="{$lcFromParentName}ParEvKey$evidenceID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
              <TARGET NAME="ACTION" PROPERTY="{$lcFromParentName}ParEvKey$evType"/>
            </CONNECT>
              
            <SERVER_INTERFACE NAME="{$fromParentName}ParentEvidenceLink" CLASS="{$prefix}{$facadeClass}" OPERATION="getParentEvidenceLink"/>
              
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
                <TARGET NAME="{$fromParentName}ParentEvidenceLink" PROPERTY="evidenceID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
                <TARGET NAME="{$fromParentName}ParentEvidenceLink" PROPERTY="evidenceType"/>
              </CONNECT>
            </xsl:if>
            
            <!--
              For all other mandatory parents a server interface is required to read the possible values 
            -->
            <xsl:for-each select="$EvidenceEntityElem/Relationships/MandatoryParents/Parent[@name!=$fromParentName]">
              
              <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
              
              <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
                OPERATION="getEvidenceListByType" NAME="{@name}ParentList"/>
              
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                <TARGET NAME="{@name}ParentList" PROPERTY="key$caseID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="CONSTANT" PROPERTY="{@name}.EvidenceType"/>
                <TARGET NAME="{@name}ParentList" PROPERTY="key$evidenceType"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="{@name}ParentList" PROPERTY="key$evidenceType"/>
                <TARGET NAME="ACTION" PROPERTY="{$lcParentName}ParEvKey$evType"/>
              </CONNECT>
            </xsl:for-each>
          </xsl:when>
        </xsl:choose>
        <!-- END, CR00098559, POB -->

        <xsl:for-each select="$EvidenceEntityElem/UserInterfaceLayer/Cluster[not(@create) or @create!='No']">
          <!-- END, 25/02/2008, CD -->
          <xsl:for-each select="Field[@metatype=$metatypeCaseParticipantSearch or @fullCreateParticipant='Yes']">
            
            
            
            <xsl:call-template name="CreateParticipantSearchInterface">

              <xsl:with-param name="capName" select="$capName"/>
              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>

            </xsl:call-template>

          </xsl:for-each>
          
          <xsl:for-each select="Field[@metatype=$metatypeParentCaseParticipant]">
            
            
            
            <xsl:call-template name="CreatePage_ParentParticipantSearchInterface">

              <xsl:with-param name="prefix" select="$prefix" />
              <xsl:with-param name="columnName" select="@columnName" />

            </xsl:call-template>

          </xsl:for-each>

          <xsl:for-each select="Field[@metatype=$metatypeEmployerCaseParticipant]">

            <xsl:call-template name="EmployerSearchInterface">

              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="employmentClass" select="$employmentClass"/>
              <xsl:with-param name="capName" select="$capName"/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>

            </xsl:call-template>

          </xsl:for-each>

        </xsl:for-each>


        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="ACTION" PROPERTY="dtls${$facadeCaseIDAgg}$caseID"/>
        </CONNECT>


        <xsl:if test="$EvidenceEntityElem/Relationships/Related/@to='Employment'">
          <CONNECT>
            <SOURCE PROPERTY="employmentID" NAME="PAGE"/>
            <TARGET PROPERTY="employmentID" NAME="ACTION"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
            <TARGET NAME="ACTION" PROPERTY="dtls${@relateEvidenceParticipantID}"/>
          </CONNECT>
        </xsl:if>


        <INFORMATIONAL>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="msg"/>
          </CONNECT>
        </INFORMATIONAL>
        
        <!-- BEGIN, CR00098559, POB -->
        <!--
          Create a seperate cluster which provides a drop down list of all mandatory parent evidence types excluding the 
          type which the child is beng created from
        -->
        <xsl:if test="count($EvidenceEntityElem/Relationships/MandatoryParents/Parent[@name!=$fromParentName]) &gt; 0">
        <CLUSTER LABEL_WIDTH="30" NUM_COLS="1">
          
          <!-- BEGIN, CR00101875, POB -->
          <xsl:if test="$fromParentName!=''">
          <FIELD LABEL="Parents.Field.{$fromParentName}" USE_BLANK="true" USE_DEFAULT="false">
            <CONNECT>
              <SOURCE NAME="{$fromParentName}ParentEvidenceLink" PROPERTY="displayText"/>
            </CONNECT>
            <LINK
              OPEN_NEW="true"
              PAGE_ID="Evidence_resolveViewEvidencePage"
              >
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
                  NAME="{$fromParentName}ParentEvidenceLink"
                  PROPERTY="parentID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="evidenceID"
                />
              </CONNECT>
              <CONNECT>
                <SOURCE
                  NAME="{$fromParentName}ParentEvidenceLink"
                  PROPERTY="parentType"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="evidenceType"
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
            </LINK>
          </FIELD>
          </xsl:if>
          <!-- END, CR00101875 -->
          <xsl:for-each select="$EvidenceEntityElem/Relationships/MandatoryParents/Parent[@name!=$fromParentName]">
            
            <xsl:variable name="ucParentName" select="@name"/>
            <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
            
            <CONTAINER LABEL="Parents.Field.{@name}">
            <FIELD USE_BLANK="true" USE_DEFAULT="false">
              <CONNECT>
                <INITIAL PROPERTY="result$parentList$dtls$summary" NAME="{@name}ParentList" HIDDEN_PROPERTY="result$parentList$dtls$evidenceID"/>
              </CONNECT>
              <CONNECT>
                <TARGET PROPERTY="{$lcParentName}ParEvKey$evidenceID" NAME="ACTION"/>
              </CONNECT>
            </FIELD>
              
              <ACTION_CONTROL LABEL="Field.Label.AddNew{$ucParentName}">
                <LINK
                  PAGE_ID="{$prefix}_create{$ucParentName}{$caseType}FromChild{$capName}Create"
                  SAVE_LINK="false"
                  DISMISS_MODAL="false"
                  >
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
                      NAME="PAGE"
                      PROPERTY="parEvID"
                    />
                    <TARGET
                      NAME="PAGE"
                      PROPERTY="otherParentID"
                    />
                  </CONNECT>
                  <CONNECT>
                    <SOURCE
                      NAME="PAGE"
                      PROPERTY="parEvType"
                    />
                    <TARGET
                      NAME="PAGE"
                      PROPERTY="otherParentType"
                    />
                  </CONNECT>
                </LINK>
              </ACTION_CONTROL>
            </CONTAINER>
          </xsl:for-each>
        </CLUSTER> 
        </xsl:if>
        <!-- END, CR00098559, POB -->

        <xsl:variable name="AllClusters"
          select="$EvidenceEntityElem/UserInterfaceLayer/Cluster[(not(@create) or @create!='No')]"/>

        <xsl:call-template name="CreateContentUI-WriteClusters">
          <xsl:with-param name="Clusters" select="$AllClusters[1]"/>
          <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
          <xsl:with-param name="capName" select="$capName"/>
        </xsl:call-template>

        <xsl:if test="count($EvidenceEntityElem/UserInterfaceLayer/Cluster[(not(@create) or @create!='No')]/EvidenceHeader)=0">
        <CLUSTER LABEL_WIDTH="40" NUM_COLS="2">
          <INCLUDE FILE_NAME="{$evidenceHeaderCreate}"/>
        </CLUSTER>
        </xsl:if>
        
        <xsl:variable name="OtherClusters"
          select="$EvidenceEntityElem/UserInterfaceLayer/Cluster[(not(@create) or @create!='No') and @order!='1']"/>

        <xsl:call-template name="CreateContentUI-WriteClusters">
          <xsl:with-param name="Clusters" select="$AllClusters[position() > 1]"/>
          <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
          <xsl:with-param name="capName" select="$capName"/>
        </xsl:call-template>
        
        <!-- BEGIN, PADDY, CR00094077 -->
        <xsl:for-each select="$EvidenceEntityElem/Relationships/Child[@coCreate='Yes']">
          <CLUSTER>
            <INCLUDE FILE_NAME="{$prefix}_coCreate{@name}{$caseType}_ChildContent.vim"/>
          </CLUSTER>  
        </xsl:for-each>
        <!-- END, PADDY, CR00094077 -->
      </VIEW>

    </redirect:write>

    <!-- BEGIN, PADDY -->
    <!-- View Content Vim Properties -->
    <xsl:call-template name="write-all-locales-create-content-vim-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="filepath" select="$filepath"/>
      <xsl:with-param name="EvidenceEntityElem" select="$EvidenceEntityElem"/>
    </xsl:call-template>
    <!-- END, PADDY -->


    <redirect:open select="concat($path, $constantFileName)" method="text" append="true"/>
    <redirect:write select="concat($path, $constantFileName)">

      <!-- BEGIN, CR00100405, CD -->
      <xsl:variable name="evidenceType">
        <xsl:call-template name="JavaID2CodeValue">
          <xsl:with-param name="java_identifier" select="$capName"/>
          <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
          <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
        </xsl:call-template>
      </xsl:variable>
      <!-- END, CR00100405 -->
      <xsl:value-of select="$capName"/>.EvidenceType=<xsl:value-of select="$evidenceType"
      /><xsl:text>&#xa;</xsl:text>
      <xsl:text>&#xa;</xsl:text>
      <!-- BEGIN, CR00098559, POB -->
      <xsl:for-each select="$EvidenceEntityElem/Relationships/MandatoryParents/Parent">
        <xsl:variable name="evidenceType">
          <xsl:call-template name="JavaID2CodeValue">
            <xsl:with-param name="java_identifier" select="@name"/>
            <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
            <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="@name"/>.EvidenceType=<xsl:value-of select="$evidenceType"/><xsl:text>&#xa;</xsl:text>
      </xsl:for-each>
      <!-- END, CR00098559, POB -->
      
      <xsl:for-each select="$EvidenceEntityElem/UserInterfaceLayer/Cluster/Field[count(ParticipantSearchType)>0]">
        <xsl:variable name="participantSearchValue">
          <xsl:for-each select="ParticipantSearchType">

            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@type"/>
              <xsl:with-param name="codetableName">CaseParticipantRoleType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
            </xsl:call-template>
            <xsl:text>|</xsl:text>
          </xsl:for-each>
        </xsl:variable>
        <xsl:variable name="participantSearchValue2">
          <xsl:choose>
            <xsl:when
              test="substring($participantSearchValue, string-length($participantSearchValue), 1)='|'">
              <xsl:value-of
                select="substring($participantSearchValue, 1, string-length($participantSearchValue)-1)"
              />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$participantSearchValue"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:text>&#xa;</xsl:text><xsl:value-of select="$capName"/>.<xsl:value-of
          select="@columnName"/>.CaseParticipantType=<xsl:value-of select="$participantSearchValue2"
        /><xsl:text>|</xsl:text>
      </xsl:for-each>
      
      <!-- BEGIN, CR00118883, POB -->
      <xsl:if test="count($EvidenceEntityElem/Relationships/Related/ParticipantType) &gt; 0">
        
        <xsl:variable name="participantTypeCodeValue">
          <xsl:for-each select="$EvidenceEntityElem/Relationships/Related/ParticipantType">
            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@type"/>
              <xsl:with-param name="codetableName">CaseParticipantRoleType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
            </xsl:call-template>
            <xsl:text>|</xsl:text>
          </xsl:for-each>
        </xsl:variable>
        
        <xsl:variable name="participantTypeCodeValue2">
          <xsl:choose>
            <xsl:when
              test="substring($participantTypeCodeValue, string-length($participantTypeCodeValue), 1)='|'">
              <xsl:value-of
                select="substring($participantTypeCodeValue, 1, string-length($participantTypeCodeValue)-1)"
              />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$participantTypeCodeValue"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <!-- BEGIN, CR00129346, POB -->
        <xsl:text>&#xa;</xsl:text>CaseParticipantRoleType.<xsl:value-of select="$capName"/>.ListForRelatedEvidence=<xsl:value-of select="$participantTypeCodeValue2"/><xsl:text>|</xsl:text>
        <!-- END, CR00129346 -->
      </xsl:if>
      <!-- END, CR00118883 -->

    </redirect:write>
    <redirect:close select="concat($path, $constantFileName)"/>

  </xsl:template>

  <xsl:template name="CreateContentUI-CoCreate-Vim">
    
    <xsl:param name="path"/>
    <xsl:param name="UIName"/>
    <xsl:param name="baseAggregation"/>
    
    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="facadeCreateMethod">create<xsl:value-of select="$capName"/>Evidence</xsl:variable>
    
    <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>  
    
    <xsl:variable name="childLevelNo">
      <xsl:call-template name="GetChildLevel">
        <xsl:with-param name="capName" select="$capName"/>
      </xsl:call-template>
    </xsl:variable>    
    
    <redirect:write select="concat($filepath, '.vim')">
      
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>
      
      <VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
        
        <xsl:for-each select="UserInterfaceLayer/Cluster[not(@create) or @create!='No']">
          <xsl:for-each
            select="Field[@metatype=$metatypeCaseParticipantSearch]">
            
            <xsl:call-template name="CreateParticipantSearchInterface">
              
              <xsl:with-param name="capName" select="$capName"/>
              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
              
            </xsl:call-template>
            
          </xsl:for-each>
          <xsl:for-each
            select="Field[@metatype=$metatypeParentCaseParticipant]">
            
            <xsl:call-template name="CreatePage_ParentParticipantSearchInterface">
              
              <xsl:with-param name="prefix" select="$prefix" />
              <xsl:with-param name="columnName" select="@columnName" />
              
            </xsl:call-template>
            
          </xsl:for-each>
          <xsl:for-each select="Field[@metatype=$metatypeEmployerCaseParticipant]">
            
            <xsl:call-template name="EmployerSearchInterface">
              
              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="employmentClass" select="$employmentClass"/>
              <xsl:with-param name="capName" select="$capName"/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
              
            </xsl:call-template>
            
          </xsl:for-each>
          
        </xsl:for-each>
        
        <xsl:call-template name="CreateContentUI-WriteClusters">
          <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
          <xsl:with-param name="Clusters" select="UserInterfaceLayer/Cluster[not(@create) or @create!='No']"/>
          <xsl:with-param name="capName" select="$capName"/>
        </xsl:call-template>
        
      </VIEW>
    </redirect:write>
    
    <!-- View Content Vim Properties -->
    <xsl:call-template name="write-all-locales-create-content-vim-properties">   
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="filepath" select="$filepath"/>
      <xsl:with-param name="EvidenceEntityElem" select="."/>
    </xsl:call-template>
    
  </xsl:template>
  
  <xsl:template name="CreateContentUI-WriteClusters">

    <xsl:param name="baseAggregation"/>
    <xsl:param name="Clusters"/>
    <xsl:param name="capName"/>

    <xsl:for-each select="$Clusters">

      <xsl:sort select="@order" data-type="number"/>
      
      <!-- BEGIN, CR00100369, POB -->
      <xsl:variable name="clusterChildNodes" select="child::node()"/>

      <xsl:if test="count(Field)&gt;0 or count(SkipField)&gt;0 or count(EvidenceHeader)&gt;0">
      <!-- END, CR00100369, POB -->
        <xsl:variable name="firstFieldName" select="Field/@columnName"/>

        <xsl:variable name="firstAttributeType" select="Field/@metatype"/>

        <xsl:variable name="numCols">
          <xsl:choose>
            <xsl:when test="@numCols!=''">
              <xsl:value-of select="@numCols"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$firstAttributeType=$metatypeComments">1</xsl:when>
                <xsl:otherwise>2</xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <xsl:variable name="labelWidth">
          <xsl:choose>
            <xsl:when test="@labelWidth!=''">
              <xsl:value-of select="@labelWidth"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$numCols=1">20</xsl:when>
                <xsl:otherwise>40</xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <xsl:variable name="showLabelsInd">
          <xsl:choose>
            <xsl:when test="$firstAttributeType=$metatypeComments">false</xsl:when>
            <xsl:otherwise>true</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
  
        <CLUSTER LABEL_WIDTH="{$labelWidth}" NUM_COLS="{$numCols}" SHOW_LABELS="{$showLabelsInd}">

          <xsl:if test="@label!=''">
            <xsl:attribute name="TITLE">
              <xsl:value-of select="@label"/>
            </xsl:attribute>
          </xsl:if>

          <xsl:if test="@description!=''">
            <xsl:attribute name="DESCRIPTION">
              <xsl:value-of select="@description"/>
            </xsl:attribute>
          </xsl:if>
          
          <xsl:if test="count(Field[@fullCreateParticipant='Yes'])&gt;0">
            <xsl:attribute name="STYLE">cluster-cpr-no-internal-padding</xsl:attribute>
          </xsl:if>
          
          <!-- BEGIN, CR00100369, POB -->
          <xsl:for-each select="$clusterChildNodes">
            
            <xsl:choose>
              <xsl:when test="local-name()='SkipField'">
                <FIELD CONTROL="SKIP"/>
              </xsl:when>
              <!-- BEGIN, CR00101071, POB -->
              <xsl:when test="local-name()='EvidenceHeader'">
                <INCLUDE FILE_NAME="{$evidenceHeaderCreate}"/>
              </xsl:when>
              <!-- END, CR00101071 -->
              <xsl:when test="local-name()='Field'">
                <!-- END, CR00100369, POB -->
                
            <!-- BEGIN, CRxxxx, CH -->
            <xsl:choose>
              <xsl:when test="@fullCreateParticipant='Yes'">
                
                <xsl:call-template name="printFullCreateParticipantClusters">
                  <xsl:with-param name="capName" select="$capName"/>
                  <xsl:with-param name="currentField" select="."/>
                  <xsl:with-param name="nsStruct" select="@nsStruct"/>
                </xsl:call-template>
                  
              </xsl:when>
              
                            
              <xsl:otherwise>
                
                      <!-- END, CRxxxx, CH -->            
              
            <FIELD>

              <xsl:if test="@label!=''">
                <xsl:attribute name="LABEL">
                  <xsl:value-of select="@label"/>
                </xsl:attribute>
              </xsl:if>
              
              <!-- BEGIN, CR00099871, POB -->
              <!-- Copy in the USE_DEFAULT and USE_BLANK options specified in the EUIM file -->
              <xsl:if test="@use_blank!=''">
                <xsl:attribute name="USE_BLANK"><xsl:value-of select="@use_blank"/></xsl:attribute>
              </xsl:if>
              <xsl:if test="@use_default!=''">
                <xsl:attribute name="USE_DEFAULT"><xsl:value-of select="@use_default"/></xsl:attribute>
              </xsl:if>
              <!-- END, CR00099871, POB -->

              <xsl:choose>

                <!-- Related Entity Attribute -->
                <!-- BEGIN, CR00098722, CD -->
                <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">
                  <CONNECT>
                    <SOURCE NAME="RELATEDATTRIBUTES" PROPERTY="result${@columnName}"/>
                  </CONNECT>
                </xsl:when>
                <!-- END, CR00098722 -->
                
                <!-- Case Participant Search -->
                <xsl:when test="@metatype=$metatypeCaseParticipantSearch">


                    <!-- Paddy
                      <xsl:when test="@readOnly='Yes'">

                      <CONNECT>
                        <SOURCE NAME="{$capName}{@columnName}" PROPERTY="nameAndAgeOpt"/>
                      </CONNECT>
                      <LINK OPEN_NEW="true" PAGE_ID="{$resolveParticipantHome}">
                        <CONNECT>
                          <SOURCE NAME="{$capName}{@columnName}" PROPERTY="caseParticipantRoleID"/>
                          <TARGET NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
                        </CONNECT>
                      </LINK>

                    </xsl:when> -->

                      <xsl:attribute name="USE_BLANK">true</xsl:attribute>
                      <xsl:attribute name="USE_DEFAULT">false</xsl:attribute>
                  
                  <xsl:variable name="offEntityBaseAggregation"><xsl:value-of select="$baseAggregation"/><xsl:if test="@notOnEntity='Yes'">nonEvidenceDetails$</xsl:if></xsl:variable>

                      <xsl:variable name="columnName">
                        <xsl:choose>
                          <!-- BEGIN, CR00101071, POB -->
                          <!-- Handle Off Entity fields -->
                          <xsl:when test="@name!='' and @createCaseParticipant='Yes'"><xsl:value-of select="$offEntityBaseAggregation"/><xsl:value-of select="@name"/>CaseParticipantDetails$caseParticipantRoleID</xsl:when>
                          <xsl:otherwise><xsl:value-of select="$offEntityBaseAggregation"/><xsl:value-of select="@columnName"/></xsl:otherwise>
                          <!-- END, CR00101071 -->
                        </xsl:choose>
                      </xsl:variable>

                      <CONNECT>
                        <INITIAL HIDDEN_PROPERTY="caseParticipantRoleID" NAME="{$capName}{@columnName}"
                          PROPERTY="nameAndAgeOpt"/>
                      </CONNECT>
                      <CONNECT>
                        <TARGET NAME="ACTION" PROPERTY="{$columnName}"/>
                      </CONNECT>


                </xsl:when>

                <!-- Parent Case Participant -->
                <xsl:when test="@metatype=$metatypeParentCaseParticipant">

                  <CONNECT>
                    <SOURCE NAME="{@columnName}" PROPERTY="nameAndAgeOpt"/>
                  </CONNECT>
                </xsl:when>

                <!-- Alternate ID Search -->
                <xsl:when test="@metatype=$metatypeAlternateIDSearch">

                  <CONNECT>
                    <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}{@columnName}"/>
                  </CONNECT>
                  <LINK>
                    <CONNECT>
                      <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                      <TARGET NAME="PAGE" PROPERTY="caseID"/>
                    </CONNECT>
                  </LINK>
                </xsl:when>

                <!-- Employer Case Participants -->
                <xsl:when test="@metatype=$metatypeEmployerCaseParticipant">

                  <CONNECT>
                    <SOURCE NAME="{$capName}{@columnName}" PROPERTY="employerName"/>
                  </CONNECT>
                  <LINK OPEN_NEW="true" PAGE_ID="{$resolveEmployerHome}">
                    <CONNECT>
                      <SOURCE NAME="PAGE" PROPERTY="employerConcernRoleID"/>
                      <TARGET NAME="PAGE" PROPERTY="concernRoleID"/>
                    </CONNECT>
                    <CONNECT>
                      <SOURCE NAME="{$capName}{@columnName}" PROPERTY="employerType"/>
                      <TARGET NAME="PAGE" PROPERTY="employerType"/>
                    </CONNECT>
                  </LINK>

                </xsl:when>
                
                <xsl:when test="@metatype=$metatypeDisplayCaseParticipant">

                
                  <CONNECT>
                    <SOURCE NAME="PARTICIPANTDETAILS" PROPERTY="nameAndAgeOpt"/>
                  </CONNECT>
                  <LINK OPEN_NEW="true" PAGE_ID="{$resolveParticipantHome}">
                    <CONNECT>
                      <SOURCE NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
                      <TARGET NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
                    </CONNECT>
                  </LINK>
                </xsl:when>

                <!-- For All Other Field Types -->
                <xsl:otherwise>

                  <xsl:variable name="columnName" select="@columnName"/>

                  <xsl:variable name="fieldHeight">
                    <xsl:choose>
                      <xsl:when test="@metatype=$metatypeComments">5</xsl:when>
                      <xsl:otherwise>1</xsl:otherwise>
                    </xsl:choose>
                  </xsl:variable>

                  <xsl:attribute name="HEIGHT">
                    <xsl:value-of select="$fieldHeight"/>
                  </xsl:attribute>

                  <CONNECT>
                    <xsl:choose>
                      <xsl:when test="@readOnly='Yes'">
                        <SOURCE NAME="PAGE" PROPERTY="{@columnName}"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:choose>
                          <xsl:when test="@createCaseParticipantAttribute='Yes'">
                            <xsl:variable name="fullNameAttribute"><xsl:value-of select="$baseAggregation"/><xsl:value-of select="@name"/>CaseParticipantDetails$<xsl:value-of select="@columnName"/>
                            </xsl:variable>
                            <TARGET NAME="ACTION" PROPERTY="{$fullNameAttribute}"/>
                          </xsl:when>
                          <!-- BEGIN, CR00094128, CD -->
                          <xsl:when test="@notOnEntity='Yes'">
                            <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}nonEvidenceDetails${@columnName}"/>
                          </xsl:when>
                          <!-- END, CR00094128 -->
                          <xsl:otherwise>
                            <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}dtls${@columnName}"/>
                          </xsl:otherwise>
                        </xsl:choose>
                      </xsl:otherwise>
                    </xsl:choose>

                  </CONNECT>

                  <!-- No idea about this. Ciaran did it! -->
                  <xsl:if test="RelatedAttribute[@popupParameter!='']">

                    <xsl:variable name="popupParameter">
                      <xsl:value-of select="RelatedAttribute/@popupParameter"/>
                    </xsl:variable>

                    <LINK>
                      <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="{$popupParameter}"/>
                        <TARGET NAME="PAGE" PROPERTY="{$popupParameter}"/>
                      </CONNECT>
                    </LINK>
                  </xsl:if>
                </xsl:otherwise>
              </xsl:choose>
            </FIELD>
                
                
              </xsl:otherwise>
            </xsl:choose>
              </xsl:when>
              </xsl:choose>
              
          </xsl:for-each>
        </CLUSTER>
      </xsl:if>
    </xsl:for-each>

  </xsl:template>

  <xsl:template name="EmployerSearchInterface">

    <xsl:param name="Field"/>
    <xsl:param name="employmentClass"/>
    <xsl:param name="baseAggregation"/>
    <xsl:param name="capName"/>

    <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="{$capName}{@columnName}"
      OPERATION="{$employmentReadByCaseParticipantMethod}"/>

    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="employerConcernRoleID"/>
      <TARGET NAME="{$capName}{@columnName}" PROPERTY="concernRoleID"/>
    </CONNECT>

  </xsl:template>

  <!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-create-content-vim-properties">

    <xsl:param name="locales"/>
    <xsl:param name="filepath"/>
    <xsl:param name="EvidenceEntityElem"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-create-content-vim-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="EvidenceEntityElem" select="$EvidenceEntityElem"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-create-content-vim-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="EvidenceEntityElem" select="$EvidenceEntityElem"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-create-content-vim-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="EvidenceEntityElem" select="$EvidenceEntityElem"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-create-content-vim-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="EvidenceEntityElem" select="$EvidenceEntityElem"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-create-content-vim-properties">

    <xsl:param name="locale"/>
    <xsl:param name="filepath"/>
    <xsl:param name="EvidenceEntityElem"/>
    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    
    <xsl:if test="count($EvidenceEntityElem//*[@locale=$locale])&gt;0">
      <redirect:write select="concat($filepath, $locale, '.properties')">

        <!-- BEGIN, 25/02/2008, CD -->
        <xsl:for-each select="$EvidenceEntityElem/UserInterfaceLayer/Cluster[not(@create) or @create!='No']">
          <!-- END, 25/02/2008, CD -->
          <xsl:if test="@label!=''">
            <xsl:if test="count(./Label[@locale=$locale])&gt;0">
              <xsl:value-of select="@label"/>=<xsl:value-of select="./Label[@locale=$locale]/@value"/>
              <xsl:text>&#xa;</xsl:text>
            </xsl:if>
            <!-- 
            If help is present on this Cluster title it is a comments Cluster.
          -->
            <xsl:if test="@help!=&apos;&apos;">
              <xsl:if test="count(./Help[@locale=$locale])&gt;0">
                <xsl:value-of select="@help"/>=<xsl:value-of select="./Help[@locale=$locale]/@value"/>
                <xsl:text>&#xa;</xsl:text>
              </xsl:if>
            </xsl:if>
          </xsl:if>
          <xsl:if test="@description!=''">
            <xsl:if test="count(./Description[@locale=$locale])&gt;0">
              <xsl:value-of select="@description"/>=<xsl:value-of
                select="./Description[@locale=$locale]/@value"/>
              <xsl:text>&#xa;</xsl:text>
            </xsl:if>
          </xsl:if>

          <xsl:for-each select="Field">
            <xsl:if test="@label!=''">
              <xsl:if test="count(./Label[@locale=$locale])&gt;0">
                <xsl:value-of select="@label"/>=<xsl:value-of
                  select="./Label[@locale=$locale]/@value"/>
                <xsl:text>&#xa;</xsl:text>
              </xsl:if>
              <xsl:if test="@help!=&apos;&apos;">
                <xsl:if test="count(./Help[@locale=$locale])&gt;0">
                  <xsl:value-of select="@help"/>=<xsl:value-of
                    select="./Help[@locale=$locale]/@value"/>
                  <xsl:text>&#xa;</xsl:text>
                </xsl:if>
              </xsl:if>
            </xsl:if>
            
            <!-- BEGIN, CR00100986, CD -->
            <xsl:if test="@fullCreateParticipant='Yes'">        
              <xsl:call-template name="printFullCreateParticipantClusterDynamicProperties">
                <xsl:with-param name="dynamicProperties" select="Property"/>
                <xsl:with-param name="locale" select="$locale"/>
              </xsl:call-template>              
            </xsl:if>
          </xsl:for-each>
          
        </xsl:for-each>

        <xsl:for-each select="$EvidenceEntityElem/Relationships/MandatoryParents/Parent">
          
          <xsl:variable name="parentName" select="@name"/>
		  <xsl:variable name="parentEntityLabel"><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		    <xsl:with-param name="locale" select="$locale"/>        
		    <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$parentName]"/>          
		  </xsl:call-template></xsl:variable>
Parents.Field.<xsl:value-of select="$parentName"/>=<xsl:value-of select="$parentEntityLabel"/><xsl:text>&#xa;</xsl:text>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.AddNewParent"/>
  <xsl:with-param name="altPropertyName">Field.Label.AddNew<xsl:value-of select="$parentName"/></xsl:with-param>
  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$parentName]"/>
</xsl:call-template>
          
        </xsl:for-each>
        
        
        
        <xsl:if test="count($EvidenceEntityElem/UserInterfaceLayer/Cluster[not(@create) or @create!='No']/Field[@fullCreateParticipant='Yes']) &gt; 0">             
          <xsl:call-template name="printFullCreateParticipantClusterStaticProperties">
            <xsl:with-param name="generalProperties" select="$generalProperties"/>
          </xsl:call-template>
        </xsl:if>
        <!-- END, CR00100986, CD -->
      </redirect:write>
    </xsl:if>
  </xsl:template>

  <!-- END, PADDY -->
</xsl:stylesheet>
