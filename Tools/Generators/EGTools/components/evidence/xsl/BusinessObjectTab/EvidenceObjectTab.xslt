<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2010-2011 Curam Software Ltd.  All rights reserved.

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

<xsl:output method="xml" indent="yes"/>

<!--<xsl:include href="CreateProperties.xslt"/>-->
<xsl:template name="EvidenceObjectTab">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="entityElement" />
  <xsl:param name="caseType" />
  
  <xsl:variable name="entityName" select="$entityElement/@name"/>
  
  <xsl:variable name="filename"><xsl:value-of select="$path"/>Object.tab</xsl:variable>

  <redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
  

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             

<tc:tab-config 
  id="{$entityName}Object"
  xmlns:tc="http://www.curamsoftware.com/curam/util/client/tab-config" 
>

  <tc:page-param name="successionID"/>

  <!-- BEGIN, CR00266438, CD -->
  <xsl:if test="count($entityElement/Relationships/Child)&gt;0 or 
                count(//EvidenceEntities/EvidenceEntity/Relationships/PreAssociation[@to=$entityName])&gt;0">
  <!-- END, CR00266438, CD -->
  <tc:menu id="{$entityName}ObjectMenu"/> 
  </xsl:if>
  
  <!-- Context panel -->
  <tc:context 
    height="0" 
    page-id="{$prefix}_view{$entityName}{$caseType}ObjSummary"
    tab-name="Tab.Name"
    tab-title="Tab.Title" />

  <!-- See Object.nav for details of the navigation links. -->
  <tc:navigation id="{$entityName}Object"/>
  
  <tc:smart-panel page-id="Evidence_smartPanelBusObj" title="Smart Panel" collapsed="true" width="200" />
  
  <tc:tab-refresh>  
        
    <tc:onsubmit page-id="{$prefix}_modify{$entityName}{$caseType}_fromList" navigation="false" context="true" menu-bar="true"/>
    <tc:onsubmit page-id="Evidence_removeActiveObject" navigation="false" context="true" menu-bar="true"/>
    <tc:onsubmit page-id="Evidence_discardPendingUpdateObject" navigation="false" context="true" menu-bar="true"/> 
    <tc:onsubmit page-id="Evidence_discardPendingRemoveForActiveObject" navigation="false" context="true" menu-bar="true"/>    
  </tc:tab-refresh>
  
</tc:tab-config>
 
   
 
</redirect:write>
<redirect:close select="$filename"/>
  
  </xsl:template>
  
</xsl:stylesheet> 