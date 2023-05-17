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
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:output method="text"/>

  
  <xsl:include href="../EvidenceCommon.xslt" />
 
  
  <xsl:param name="date"/>
  <xsl:param name="localeList"/>
  
  
  <!-- Main Evidence Business Object Tab Generation Template -->
  <xsl:template match="EvidenceEntity[ServiceLayer]">

<xsl:if test="count(Relationships/Child)&gt;0">
    <xsl:variable name="entityName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="rootPath"/>
    <xsl:variable name="path" select="concat($rootPath, $entityName , '')"/>
  
  
  <xsl:variable name="filename"><xsl:value-of select="$path"/>BObjMenuLoader</xsl:variable>

  <redirect:write select="concat($filename, '.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
  

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.tab.impl;

import java.util.Map;

import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.SuccessionID;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import curam.util.resources.Trace;

@SuppressWarnings("all")
public class <xsl:value-of select="$entityName"/>BObjMenuLoader 
  implements DynamicMenuStateLoader {  

  /**
   * Constructor.
   */
  public <xsl:value-of select="$entityName"/>BObjMenuLoader() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public MenuState loadMenuState(MenuState menuState, Map&lt;String, String&gt; pageParameters,
      String[] idsToUpdate) {
    
    // configure menuState
    MenuState returnState = menuState;

    
      
    return returnState;
  }

}

</redirect:write>

<xsl:if test="count(Relationships/Child)&gt;0">
<xsl:call-template name="write-menu-const">
   
    
  
  <xsl:with-param name="filepath" select="$path"/>
  <xsl:with-param name="EvidenceEntity" select="$entityName"/>
  <xsl:with-param name="Relationships" select="Relationships"/>
</xsl:call-template>
</xsl:if>
  
  
  
  </xsl:if>
</xsl:template>

  
  
  
<!--iterate through each token, generating each element-->
<xsl:template name="write-menu-const">

  
  <xsl:param name="filepath"/>
  <xsl:param name="EvidenceEntity"/>
  <xsl:param name="Relationships"/>

<xsl:variable name="constfilename"><xsl:value-of select="$filepath"/>BObjMenuLoaderConst</xsl:variable>

<redirect:write select="concat($constfilename, '.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
  

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.tab.impl;

/**
 * This class assigns values to the Tab Loader constants
 */
public abstract class <xsl:value-of select="$EvidenceEntity"/>BObjMenuLoaderConst {  

  
 /**
   * String constants for Business Object Tab.
   */
  
  <xsl:for-each select="$Relationships/Child">
          
  <xsl:variable name="childName" select="@name"/>
          
  public static final String k<xsl:value-of select="$EvidenceEntity"/>New<xsl:value-of select="$childName"/> = "New<xsl:value-of select="$childName"/>";
          
  </xsl:for-each> 
  
  
 

}



</redirect:write>


</xsl:template>
  
</xsl:stylesheet> 







