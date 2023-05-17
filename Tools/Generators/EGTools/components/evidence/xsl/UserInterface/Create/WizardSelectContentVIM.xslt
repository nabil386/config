<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010-2014,2018. All Rights Reserved.
 
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

  <xsl:output method="xml" indent="yes"/>


  <!--
    Template to write the content vim and associated properties files for the entities wizard select pages
    
    @param entityElement XML Element for the entity
    @param baseDirectory Base Directory to write the files into
    @param fileName Name of the file to create (with no file extension)
    @param relatedEntityName The name of the entity that was pre selected (if any)
  -->
  <xsl:template name="WizardSelectContentVIM">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="fileName"/>
    <xsl:param name="relatedEntityName"/>
    
    <!-- Name of the entity -->
    <xsl:variable name="entityName"><xsl:value-of select="$entityElement/@name"/></xsl:variable>
    
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
        
        <!-- Page Parameters -->
        <PAGE_PARAMETER NAME="caseID"/>
       <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">
         <PAGE_PARAMETER NAME="employmentID"/>
         <PAGE_PARAMETER NAME="employerConcernRoleID"/>
         <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
       </xsl:if>
        <xsl:if test="$relatedEntityName!=''">
          <xsl:if test="count($entityElement/Relationships/Parent[@name=$relatedEntityName]) = 1">
            <!-- Parent is already selected so add in correct parameters -->
            <PAGE_PARAMETER NAME="parEvID"/>
            <PAGE_PARAMETER NAME="parEvType"/>
          </xsl:if>
          <xsl:if test="count($entityElement/Relationships/PreAssociation[@to=$relatedEntityName]) = 1">
            <!-- Pre Association is already selected so add in correct parameters -->
            <PAGE_PARAMETER NAME="preAssocID"/>
            <PAGE_PARAMETER NAME="preAssocType"/>
          </xsl:if>
          <xsl:if test="count($entityElement/Relationships/MandatoryParents/Parent[@name=$relatedEntityName]) = 1">
            <!-- Mandatory Parent is already selected so add in correct parameters -->
            <PAGE_PARAMETER NAME="{$relatedEntityName}ParEvID"/>
            <PAGE_PARAMETER NAME="{$relatedEntityName}ParEvType"/>
          </xsl:if>
        </xsl:if>
        
        <xsl:if test="count($entityElement/Relationships/Parent) &gt; 0">
          <xsl:if test="count($entityElement/Relationships/Parent[@name=$relatedEntityName]) = 0">
          <xsl:call-template name="Wizard-WriteRadioButtonSelectList">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="relationshipType">PARENT</xsl:with-param>
          </xsl:call-template>
          </xsl:if>
        </xsl:if>
        <xsl:if test="$entityElement/Relationships/@preAssociation='Yes'">
          <xsl:if test="count($entityElement/Relationships/PreAssociation[@to=$relatedEntityName]) = 0">
          <xsl:call-template name="Wizard-WriteRadioButtonSelectList">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="relationshipType">PREASSOC</xsl:with-param>
          </xsl:call-template>
          </xsl:if>
        </xsl:if>
        <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
          <xsl:if test="@name!=$relatedEntityName">
          <xsl:call-template name="Wizard-WriteRadioButtonSelectList">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="relationshipType" select="@name"/>
          </xsl:call-template>
          </xsl:if>
        </xsl:for-each>
      </VIEW>

    </redirect:write>
    
    <xsl:call-template name="write-all-locales-selectallcontent-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
    </xsl:call-template>

  </xsl:template>
  
  <!--
    Template to write the radion button select list for a given relationship type
    
    @param relationshipType
    
  -->
  <xsl:template name="Wizard-WriteRadioButtonSelectList">
    <xsl:param name="entityElement"/>
    <xsl:param name="relationshipType"/>
    
    <SERVER_INTERFACE OPERATION="listPossibleRelatedObjects" NAME="{$relationshipType}LIST" CLASS="Evidence"/>
    <SERVER_INTERFACE PHASE="ACTION" OPERATION="getEvidenceAndCaseFromSuccession" NAME="{$relationshipType}PARAMS" CLASS="{$prefix}{$facadeClass}"/>
    
    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="caseID"/>
      <TARGET NAME="{$relationshipType}LIST" PROPERTY="key$caseID"/>
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="CONSTANT" PROPERTY="{$entityElement/@name}.Wizard.TypeList.{$relationshipType}"/>
      <TARGET NAME="{$relationshipType}LIST" PROPERTY="key$evidenceTypeList"/>
    </CONNECT>
    
    <CLUSTER>
    <CLUSTER
      SHOW_LABELS="false"
      STYLE="outer-cluster-borderless"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="TEXT"
          >
            <xsl:choose>
              <xsl:when test="count($entityElement/UserInterfaceLayer/WizardSelectDescription[@type=$relationshipType])&gt;0">
                <xsl:attribute name="PROPERTY">
                  <xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type=$relationshipType]/@description"/>
                </xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="PROPERTY">Wizard.Select<xsl:value-of select="$relationshipType"/>.Description</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
          </SOURCE>
        </CONNECT>
      </FIELD>
    </CLUSTER>
    
    <LIST>
      <CONTAINER WIDTH="5">
        <WIDGET TYPE="SINGLESELECT">
          <WIDGET_PARAMETER NAME="SELECT_SOURCE">
            <CONNECT>
              <SOURCE NAME="{$relationshipType}LIST" PROPERTY="result$dtls$successionID"/>
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET NAME="{$relationshipType}PARAMS" PROPERTY="key$successionID"/>
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD WIDTH="23" LABEL="List.Title.Type">
        <CONNECT>
          <SOURCE PROPERTY="result$dtls$evidenceType" NAME="{$relationshipType}LIST"/>
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="17" LABEL="List.Title.Participant">
        <CONNECT>
          <SOURCE PROPERTY="result$dtls$participantName" NAME="{$relationshipType}LIST"/>
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="40" LABEL="List.Title.Description">
        <CONNECT>
          <SOURCE PROPERTY="result$dtls$summary" NAME="{$relationshipType}LIST"/>
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="20" LABEL="List.Title.Period">
        <CONNECT>
          <SOURCE PROPERTY="result$dtls$period" NAME="{$relationshipType}LIST"/>
        </CONNECT>
      </FIELD>
    </LIST>
    </CLUSTER>
  </xsl:template>
  
  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-selectallcontent-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-selectallcontent-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-selectallcontent-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-selectallcontent-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-selectallcontent-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-selectallcontent-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>
    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    
    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0 or count($entityElement//*[@locale=$locale])&gt;0">
    <redirect:write select="concat($fullFileName, $locale, '.properties')">
    
      <xsl:if test="count($generalProperties/List.Title.Type)&gt;0">
        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Type"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/List.Title.Participant)&gt;0">
        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Participant"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/List.Title.Description)&gt;0">
        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Description"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/List.Title.Period)&gt;0">
        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Period"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
      </xsl:if>

      <xsl:variable name="start" select="$generalProperties/Wizard.SelectEvidence.Description.Start/@value"/>
      <xsl:variable name="end" select="$generalProperties/Wizard.SelectEvidence.Description.End/@value"/>
      
      <xsl:if test="count($entityElement/Relationships/Parent)&gt;0">      
        <xsl:choose>
          <xsl:when test="count($entityElement/UserInterfaceLayer/WizardSelectDescription[@type='PARENT'])&gt;0">
<xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type='PARENT']/@description"/>=<xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type='PARENT']/Label[@locale=$locale]/@value"/>
          </xsl:when>
          <xsl:otherwise>
          <xsl:if test="count($generalProperties/Wizard.SelectEvidence.Description.Start)&gt;0">
          <xsl:variable name="propValue"><xsl:for-each select="$entityElement/Relationships/Parent"><xsl:variable 
		  name="parentName" select="@name"/><xsl:variable name="parentLabel"><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		    <xsl:with-param name="locale" select="$locale"/>        
		    <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$parentName]"/>          
		  </xsl:call-template></xsl:variable><xsl:text> </xsl:text><xsl:value-of select="$parentLabel"/>,</xsl:for-each></xsl:variable>
Wizard.SelectPARENT.Description=<xsl:value-of select="$start"/><xsl:value-of select="substring($propValue,0,string-length($propValue))"/><xsl:text> </xsl:text><xsl:value-of select="$end"/>
Wizard.SelectPARENT.Description.Help=<xsl:value-of select="$generalProperties/Wizard.SelectEvidence.Description/Help/@value"/>
          </xsl:if>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text>&#xa;</xsl:text>
      </xsl:if>
      
      <xsl:if test="count($entityElement/Relationships/PreAssociation)&gt;0">      
        <xsl:choose>
          <xsl:when test="count($entityElement/UserInterfaceLayer/WizardSelectDescription[@type='PREASSOC'])&gt;0">
<xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type='PREASSOC']/@description"/>=<xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type='PREASSOC']/Label[@locale=$locale]/@value"/>
          </xsl:when>
          <xsl:otherwise>
          <xsl:if test="count($generalProperties/Wizard.SelectEvidence.Description.Start)&gt;0">
          <xsl:variable name="propValue"><xsl:for-each select="$entityElement/Relationships/PreAssociation"><xsl:variable 
		  name="preAssocName" select="@to"/><xsl:variable name="preAssocLabel"><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		    <xsl:with-param name="locale" select="$locale"/>        
        <!-- BEGIN, CR00443631, JAF -->
        <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$preAssocName]"/>
      </xsl:call-template></xsl:variable><xsl:text> </xsl:text><xsl:value-of select="$preAssocLabel"/><xsl:if test="position()!=last()">,</xsl:if></xsl:for-each></xsl:variable>
        <!-- END, CR00443631 -->
        <!-- BEGIN, WI222394, DR -->
Wizard.SelectPREASSOC.Description=<xsl:value-of select="$start"/><xsl:value-of select="substring($propValue,1,string-length($propValue))"/><xsl:text> </xsl:text><xsl:value-of select="$end"/>
        <!-- END, WI222394 -->
Wizard.SelectPREASSOC.Description.Help=<xsl:value-of select="$generalProperties/Wizard.SelectEvidence.Description/Help/@value"/>
          </xsl:if>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text>&#xa;</xsl:text>
      </xsl:if>
      
      <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
        <xsl:variable name="parentName" select="@name"/>
        <xsl:choose>
          <xsl:when test="count($entityElement/UserInterfaceLayer/WizardSelectDescription[@type=$parentName])&gt;0">
<xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type=$parentName]/@description"/>=<xsl:value-of select="$entityElement/UserInterfaceLayer/WizardSelectDescription[@type=$parentName]/Label[@locale=$locale]/@value"/>
          </xsl:when>
          <xsl:otherwise>
          <xsl:if test="count($generalProperties/Wizard.SelectEvidence.Description.Start)&gt;0">
		  <xsl:variable name="propValue"><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		    <xsl:with-param name="locale" select="$locale"/>        
		    <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$parentName]"/>          
		  </xsl:call-template></xsl:variable>
Wizard.Select<xsl:value-of select="$parentName"/>.Description=<xsl:value-of select="$start"/><xsl:text> </xsl:text><xsl:value-of select="$propValue"/><xsl:text> </xsl:text><xsl:value-of select="$end"/>
Wizard.Select<xsl:value-of select="$parentName"/>.Description.Help=<xsl:value-of select="$generalProperties/Wizard.SelectEvidence.Description/Help/@value"/>
          </xsl:if>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text>&#xa;</xsl:text>
      </xsl:for-each>
    </redirect:write>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
