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
  <xsl:output method="text"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />
  <xsl:param name="date"/>

  <xsl:template match="EvidenceEntities">
    
    <xsl:variable name="className"><xsl:value-of select="$prefix"/>EvidenceControllerHookRegistrar</xsl:variable>
    
    <redirect:write select="concat($className,'.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl;


import curam.core.sl.infrastructure.impl.EvidenceControllerHookRegistrar;
import curam.core.sl.infrastructure.impl.EvidenceControllerHookManager;
import <xsl:value-of select="$javaEvidenceCodePath"/>.service.fact.<xsl:value-of select="$prefix"/>EvidenceControllerHookFactory;


/**
 * This process class is used to register products with the evidence controller.
 *
 */
public abstract class <xsl:value-of select="$className"/> extends
  <xsl:value-of select="$javaEvidenceCodePath"/>.service.base.<xsl:value-of select="$className"/>
  implements EvidenceControllerHookRegistrar  {
  
  
  //___________________________________________________________________________
  /**
   * This method registers the product with the evidence controller.
   */
  public void register() {

    EvidenceControllerHookRegistrar.HookMap map =
      EvidenceControllerHookManager.get();

    // TODO - The PRODUCTTYPE code is hardcoded to SampleSportingGrant.
    // We need to find a way to generate this for a new component.
    map.addMapping("PC9",
      <xsl:value-of select="$prefix"/>EvidenceControllerHookFactory.class);
    map.addMapping("PT30",
      <xsl:value-of select="$prefix"/>EvidenceControllerHookFactory.class);
  
  }

}

        
    </redirect:write>
    
  </xsl:template> 
  
</xsl:stylesheet>
