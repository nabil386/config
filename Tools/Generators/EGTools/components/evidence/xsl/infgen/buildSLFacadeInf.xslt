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
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:include href="../EvidenceCommon.xslt" />

  <!-- Facade and Service Layer templates, containing templates for generating all necessary
       operations, structs and attributes for each layer -->

  <xsl:include href="BuildFacadeLayerInf.xslt"/>
  <xsl:include href="BuildServiceLayerInf.xslt"/>

  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

  <xsl:template match="EvidenceEntities">

    <!-- Name of final output file that will be used in appbuild -->
    <xsl:variable name="filename">Curam2.xml</xsl:variable>

    <redirect:open select="$filename" method="xml" append="false"/>
    <redirect:write select="$filename">

      <!-- Call main Service Layer template passing product name -->
      <xsl:call-template name="buildServiceLayerInf">

        <xsl:with-param select="$prefix" name="prefix"/>
        <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
        <xsl:with-param select="$product" name="product"/>

      </xsl:call-template>

      <!-- Call main Facade Layer template passing product name -->
      <xsl:call-template name="buildFacadeLayerInf">

        <xsl:with-param select="$prefix" name="prefix"/>
        <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
        <xsl:with-param select="$product" name="product"/>

      </xsl:call-template>

      <xsl:call-template name="buildPackageStructure"/>

    </redirect:write>
    <redirect:close select="$filename"/>

   </xsl:template>

  <xsl:template name="buildPackageStructure">

    <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::Service" QUID="{$product}::Evidence::Service">
      <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$modelEvidenceCodePath}.service"/>
    </PACKAGE>

    <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade" QUID="{$product}::Evidence::Facade">
      <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$modelEvidenceCodePath}.facade"/>
    </PACKAGE>

    <xsl:for-each select="EvidenceEntity">

      <xsl:variable name="EntityName">
        <!--<xsl:value-of select="$prefix"/>-->
        <xsl:value-of select="@name"/>
      </xsl:variable>
      <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::Service::{$EntityName}" PARENT_QUID="{$product}::Evidence::Service" QUID="{$product}::Evidence::Service::{$EntityName}"/>

    </xsl:for-each>

    <MODULE NAME="EJBServer" QUID="EJBServer" STEREOTYPE="ejb">
      <MODULE.REALISES NAME="{$prefix}EvidenceMaintenance" QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::{$prefix}EvidenceMaintenance" QUID="{$product}::Evidence::Facade::{$prefix}EvidenceMaintenance"/>
    </MODULE>
  </xsl:template>

</xsl:stylesheet>