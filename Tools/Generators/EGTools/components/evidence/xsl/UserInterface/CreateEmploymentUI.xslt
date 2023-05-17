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
Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

  <!-- Include Section -->

  <!-- Global Variables -->
  <xsl:include href="Employment/EmploymentCommon.xslt" />

  <!-- Modify Employment User Interface Template -->
  <xsl:include href="Employment/EmploymentModifyUI.xslt" />

  <!-- Cancel Employment User Interface Template -->
  <xsl:include href="Employment/EmploymentCancelUI.xslt" />

  <!-- Read Employment User Interface Template -->
  <xsl:include href="Employment/EmploymentReadUI.xslt" />

  <xsl:template name="CreateEmploymentUI">
  
    <xsl:param name="prefix" />
    <xsl:param name="path" />

    <!-- Create Cancel Employment User Interface -->
    <xsl:call-template name="EmploymentCancelUI">
  
      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="$path" />
      <xsl:with-param name="UIName" select="concat($prefix, $caseType,  $employmentCancelUIName)" />
    
    </xsl:call-template>

    <!-- Create Modify Employment User Interface -->
    <xsl:call-template name="EmploymentModifyUI">
  
      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="$path" />
      <xsl:with-param name="UIName" select="concat($prefix, $caseType,  $employmentModifyUIName)" />
    
    </xsl:call-template>

    <!-- Create Read Employment User Interface -->
    <xsl:call-template name="EmploymentReadUI">
  
      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="$path" />
      <xsl:with-param name="UIName" select="concat($prefix, $caseType,  $employmentReadUIName)" />
    
    </xsl:call-template>

  </xsl:template>

</xsl:stylesheet>