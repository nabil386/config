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
  Template to produce a VIM File and its associated properties file for listing all 
  instances of a given Case Participant Role Type
  
  @param path The Path for storing the files in
  @param vimPageName The Name of the VIM Page and properties file to produce
  @param workspaceType The file name postfix showing the workspace type (i.e. Active, InEdit or blank)
  @param currentEntityElem Base XML Element of the current entity
-->
<xsl:template name="ListCPRForRelatedEntity">

  <xsl:param name="path"/>
  <xsl:param name="vimPageName"/>
  <xsl:param name="workspaceType"/>
  <xsl:param name="currentEntityElem"/>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$vimPageName"/><xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>

  <xsl:variable name="listEmploymentsForCPRPageName"><xsl:value-of select="$prefix"/>_listCoreEmploymentEvidenceDetails<xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>
  
  <redirect:write select="concat($filepath, '.vim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

    <VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
        
      <LIST TITLE="List.Title.RelatedCPR">
          
          <CONTAINER
            LABEL="List.Title.Action"
            WIDTH="10"
            >
            
            <ACTION_CONTROL LABEL="ActionControl.Label.Select.For.RelatedEntity.Association">
              <LINK PAGE_ID="{$listEmploymentsForCPRPageName}" DISMISS_MODAL="FALSE">
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
                    NAME="DISPLAYCPRLIST"
                    PROPERTY="caseParticipantRoleID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="caseParticipantRoleID"
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
                    NAME="CONSTANT"
                    PROPERTY="{$currentEntityElem/@name}.EvidenceType"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="evidenceType"
                  />
                </CONNECT>
              </LINK>
            </ACTION_CONTROL>
            
          </CONTAINER>
          
          <FIELD
            LABEL="List.Title.Name"
            WIDTH="15"
            >
            <CONNECT>
              <SOURCE
                NAME="DISPLAYCPRLIST"
                PROPERTY="nameAndAgeOpt"
              />
            </CONNECT>
            <LINK OPEN_NEW="true" PAGE_ID="{$resolveParticipantHome}">
              <CONNECT>
                <SOURCE NAME="DISPLAYCPRLIST" PROPERTY="caseParticipantRoleID"/>
                <TARGET NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
              </CONNECT>
            </LINK>
          </FIELD>
        <FIELD
          LABEL="List.Title.Type"
          WIDTH="20"
          >
          <CONNECT>
            <SOURCE
              NAME="DISPLAYCPRLIST"
              PROPERTY="type"
            />
          </CONNECT>
        </FIELD>
        <FIELD
          LABEL="List.Title.DateOfBirth"
          WIDTH="20"
          >
          <CONNECT>
            <SOURCE
              NAME="DISPLAYCPRLIST"
              PROPERTY="dateOfBirth"
            />
          </CONNECT>
        </FIELD>
        
        </LIST>
        
    </VIEW>
  </redirect:write>
  
    </xsl:template>
  
</xsl:stylesheet>