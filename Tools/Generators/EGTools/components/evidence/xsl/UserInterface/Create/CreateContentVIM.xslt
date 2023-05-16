<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2010 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="../UICommon.xslt"/>
  <xsl:import href="CreateUtilityTemplates.xslt"/>

  <xsl:output method="xml" indent="yes"/>


  <!--
    Template to write the content vim and associated properties files for the entities create page
    
    @param entityElement XML Element for the entity
    @param baseDirectory Base Directory to write the files into
    @param fileName Name of the file to create (with no file extension)
    @param baseAggregation The base for all dtls connect statements (e.g. dtls$)
  -->
  <xsl:template name="CreateContentVIM">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="fileName"/>
    <xsl:param name="baseAggregation"/>
    <xsl:param name="endDateWizard"/>
    
    <!-- Name of the entity -->
    <xsl:variable name="entityName"><xsl:value-of select="$entityElement/@name"/></xsl:variable>
    
    <!-- facade create method -->
    <xsl:variable name="facadeCreateMethod">
    <!-- BEGIN, 191675, JAY -->
    	<xsl:choose>
    		<xsl:when test="$endDateWizard = 'true'">createAutoEndDate<xsl:value-of select="$entityName"/>Evidence</xsl:when>
    		<xsl:otherwise>create<xsl:value-of select="$entityName"/>Evidence</xsl:otherwise>
    	</xsl:choose>
    <!-- END, 191675, JAY -->
    </xsl:variable>
    
    <!-- Full name and directory of the file to create, minus the file extension -->
    <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>
    
    <!-- Write the main VIM file -->
    <redirect:write select="concat($fullFileName, '.vim')">
      
      <!-- add copyright notice -->
      <xsl:call-template name="printXMLCopyright">
        <xsl:with-param name="date" select="$date"/>
      </xsl:call-template>

      <VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
        
        <!-- Page Parameters and their standard connections -->
        
        <!-- CaseID -->
        <PAGE_PARAMETER NAME="caseID"/>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="ACTION" PROPERTY="dtls${$facadeCaseIDAgg}$caseID"/>
        </CONNECT>
        <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="caseID"/>
          </CONNECT>
        </xsl:if>
      	<!-- BEGIN, 191675, JAY -->
       	 <PAGE_PARAMETER NAME="evidenceType"/>
       	<!-- END, 191675, JAY -->
        
        <!-- Parent -->
        <xsl:if test="count($entityElement/Relationships/Parent) &gt; 0">
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/Parent[1]/@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/Parent[1]/@name"/>
          </xsl:call-template></xsl:variable>
          <PAGE_PARAMETER NAME="{$idParamName}"/>
          <PAGE_PARAMETER NAME="{$typeParamName}"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadeParentEvidenceAgg}${$evidenceID}"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadeParentEvidenceAgg}${$evidenceType}"/>
          </CONNECT>
          <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">      
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
                <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKey$evidenceID"/>
              </CONNECT>                        
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
                <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKey$evType"/>
              </CONNECT>          
            </xsl:if>
        </xsl:if>
        
        <!-- Multiple Mandatory Parents -->
        <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
          <xsl:variable name="aggregationName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="@name"/>
          </xsl:call-template></xsl:variable>
          <PAGE_PARAMETER NAME="{$idParamName}"/>
          <PAGE_PARAMETER NAME="{$typeParamName}"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$aggregationName}ParEvKey$evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$aggregationName}ParEvKey$evType"/>
          </CONNECT>
        </xsl:for-each>  
        
        <!-- PreAssociations -->
        <xsl:if test="$entityElement/Relationships/@preAssociation='Yes'">
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
          <PAGE_PARAMETER NAME="{$idParamName}"/>
          <PAGE_PARAMETER NAME="{$typeParamName}"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadePreAssociationAgg}$evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadePreAssociationAgg}$evType"/>
          </CONNECT>
          <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="preAssocKey$evidenceID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="preAssocKey$evType"/>
            </CONNECT>
          </xsl:if>
        </xsl:if>
        
        <!-- Related Employment -->
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <PAGE_PARAMETER NAME="employmentID"/>
          <PAGE_PARAMETER NAME="employerConcernRoleID"/>
          <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
        </xsl:if>
        
        <!-- Server Interfaces -->
        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="ACTION"
          OPERATION="{$facadeCreateMethod}" PHASE="ACTION"/>
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
          OPERATION="readNameByCaseParticipantRole" NAME="PARTICIPANTDETAILS"/>
        </xsl:if>
        <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
          <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
          OPERATION="get{$entityName}RelatedEntityAttributes" NAME="RELATEDATTRIBUTES"/>
        </xsl:if>
        
        <!-- Standard connections -->
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
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
       
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <CONNECT>
            <SOURCE PROPERTY="employmentID" NAME="PAGE"/>
            <TARGET PROPERTY="employmentID" NAME="ACTION"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
            <TARGET NAME="ACTION" PROPERTY="dtls${@relateEvidenceParticipantID}"/>
          </CONNECT>
        </xsl:if>
      
        <xsl:for-each select="$entityElement/UserInterfaceLayer/Cluster[not(@create) or @create!='No']">
          <xsl:for-each select="Field[@metatype=$metatypeCaseParticipantSearch or @fullCreateParticipant='Yes']">
            <xsl:call-template name="CreateParticipantSearchInterface">
              <xsl:with-param name="capName" select="$entityName"/>
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
            <xsl:call-template name="CreateContentVIM-EmployerSearchInterface">
              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="employmentClass" select="$employmentClass"/>
              <xsl:with-param name="entityName" select="$entityName"/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
            </xsl:call-template>
          </xsl:for-each>
        </xsl:for-each>

        <INFORMATIONAL>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="msg"/>
          </CONNECT>
        </INFORMATIONAL>

        <xsl:variable name="AllClusters"
          select="$entityElement/UserInterfaceLayer/Cluster[(not(@create) or @create!='No')]"/>

        <xsl:if test="count($entityElement/UserInterfaceLayer/Cluster[(not(@create) or @create!='No')]/EvidenceHeader)=0">
        <CLUSTER LABEL_WIDTH="40" NUM_COLS="2" BEHAVIOR="NONE">
          <INCLUDE FILE_NAME="{$evidenceHeaderCreate}"/>
        </CLUSTER>
        </xsl:if>

        <xsl:call-template name="CreateContentVIM-WriteClusters">
          <xsl:with-param name="Clusters" select="$AllClusters"/>
          <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
          <xsl:with-param name="entityName" select="$entityName"/>
        </xsl:call-template>
      </VIEW>

    </redirect:write>

    <!-- Create Content Vim Properties -->
    <xsl:call-template name="write-all-locales-createcontentvim-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
    </xsl:call-template>

  </xsl:template>

  
  
  <xsl:template name="CreateContentVIM-WriteClusters">

    <xsl:param name="baseAggregation"/>
    <xsl:param name="Clusters"/>
    <xsl:param name="entityName"/>
    
    <xsl:for-each select="$Clusters">

      <xsl:sort select="@order" data-type="number"/>
      
      <xsl:variable name="clusterChildNodes" select="child::node()"/>

      <xsl:if test="count(Field)&gt;0 or count(SkipField)&gt;0 or count(EvidenceHeader)&gt;0">
      
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
  
        <CLUSTER LABEL_WIDTH="{$labelWidth}" NUM_COLS="{$numCols}" SHOW_LABELS="{$showLabelsInd}" BEHAVIOR="NONE">

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
          
          
          <xsl:for-each select="$clusterChildNodes">
            
            <xsl:choose>
              <xsl:when test="local-name()='SkipField'">
                <FIELD CONTROL="SKIP"/>
              </xsl:when>
              
              <xsl:when test="local-name()='EvidenceHeader'">
                <INCLUDE FILE_NAME="{$evidenceHeaderCreate}"/>
              </xsl:when>
              
              <xsl:when test="local-name()='Field'">
                
            <xsl:choose>
              <xsl:when test="@fullCreateParticipant='Yes'">
                
                <xsl:call-template name="printFullCreateParticipantClusters">
                  <xsl:with-param name="capName" select="$entityName"/>
                  <xsl:with-param name="currentField" select="."/>
                  <xsl:with-param name="nsStruct" select="@nsStruct"/>
                </xsl:call-template>
                  
              </xsl:when>
              
                            
              <xsl:otherwise>
              
            <FIELD>

              <xsl:if test="@label!=''">
                <xsl:attribute name="LABEL">
                  <xsl:value-of select="@label"/>
                </xsl:attribute>
              </xsl:if>
              
              <!-- Copy in the USE_DEFAULT and USE_BLANK options specified in the EUIM file -->
              <xsl:if test="@use_blank!=''">
                <xsl:attribute name="USE_BLANK"><xsl:value-of select="@use_blank"/></xsl:attribute>
              </xsl:if>
              <xsl:if test="@use_default!=''">
                <xsl:attribute name="USE_DEFAULT"><xsl:value-of select="@use_default"/></xsl:attribute>
              </xsl:if>
              

              <xsl:choose>

                <!-- Related Entity Attribute -->
                <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">
                  <CONNECT>
                    <SOURCE NAME="RELATEDATTRIBUTES" PROPERTY="result${@columnName}"/>
                  </CONNECT>
                </xsl:when>
                
                <!-- Case Participant Search -->
                <xsl:when test="@metatype=$metatypeCaseParticipantSearch">

                      <xsl:attribute name="USE_BLANK">true</xsl:attribute>
                      <xsl:attribute name="USE_DEFAULT">false</xsl:attribute>
                  
                  <xsl:variable name="offEntityBaseAggregation"><xsl:value-of select="$baseAggregation"/><xsl:if test="@notOnEntity='Yes'">nonEvidenceDetails$</xsl:if></xsl:variable>

                      <xsl:variable name="columnName">
                        <xsl:choose>
                          <!-- Handle Off Entity fields -->
                          <xsl:when test="@name!='' and @createCaseParticipant='Yes'"><xsl:value-of select="$offEntityBaseAggregation"/><xsl:value-of select="@name"/>CaseParticipantDetails$caseParticipantRoleID</xsl:when>
                          <xsl:otherwise><xsl:value-of select="$offEntityBaseAggregation"/><xsl:value-of select="@columnName"/></xsl:otherwise>
                        </xsl:choose>
                      </xsl:variable>

                      <CONNECT>
                        <INITIAL HIDDEN_PROPERTY="caseParticipantRoleID" NAME="{$entityName}{@columnName}"
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
                    <SOURCE NAME="{$entityName}{@columnName}" PROPERTY="employerName"/>
                  </CONNECT>

                </xsl:when>
                
                <xsl:when test="@metatype=$metatypeDisplayCaseParticipant">

                
                  <CONNECT>
                    <SOURCE NAME="PARTICIPANTDETAILS" PROPERTY="nameAndAgeOpt"/>
                  </CONNECT>
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
                          <xsl:when test="@notOnEntity='Yes'">
                            <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}nonEvidenceDetails${@columnName}"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}dtls${@columnName}"/>
                          </xsl:otherwise>
                        </xsl:choose>
                      </xsl:otherwise>
                    </xsl:choose>

                  </CONNECT>

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

  <xsl:template name="CreateContentVIM-EmployerSearchInterface">

    <xsl:param name="Field"/>
    <xsl:param name="employmentClass"/>
    <xsl:param name="baseAggregation"/>
    <xsl:param name="entityName"/>

    <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="{$entityName}{@columnName}"
      OPERATION="{$employmentReadByCaseParticipantMethod}"/>

    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="employerConcernRoleID"/>
      <TARGET NAME="{$entityName}{@columnName}" PROPERTY="concernRoleID"/>
    </CONNECT>

  </xsl:template>

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-createcontentvim-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-createcontentvim-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-createcontentvim-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-createcontentvim-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-createcontentvim-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-createcontentvim-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>
    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    
    <xsl:if test="count($entityElement//*[@locale=$locale])&gt;0">
      <redirect:write select="concat($fullFileName, $locale, '.properties')">

        <xsl:for-each select="$entityElement/UserInterfaceLayer/Cluster[not(@create) or @create!='No']">

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
            
            <xsl:if test="@fullCreateParticipant='Yes'">        
              <xsl:call-template name="printFullCreateParticipantClusterDynamicProperties">
                <xsl:with-param name="dynamicProperties" select="Property"/>
                <xsl:with-param name="locale" select="$locale"/>
              </xsl:call-template>              
            </xsl:if>
          </xsl:for-each>
          
        </xsl:for-each>
        
        
        <xsl:if test="count($entityElement/UserInterfaceLayer/Cluster[not(@create) or @create!='No']/Field[@fullCreateParticipant='Yes']) &gt; 0">             
          <xsl:call-template name="printFullCreateParticipantClusterStaticProperties">
            <xsl:with-param name="generalProperties" select="$generalProperties"/>
          </xsl:call-template>
        </xsl:if>
      </redirect:write>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
