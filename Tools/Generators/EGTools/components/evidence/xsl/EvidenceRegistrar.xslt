<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2006-2008,2010 Curam Software Ltd.  All rights reserved.

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
<xsl:include href="EvidenceCommon.xslt" />  
  
<xsl:output method="text"/>
<xsl:param name="date"/>

<xsl:template match="EvidenceEntities">
    
      
<!-- Open Service file for writing and write in header  -->
<xsl:variable name="filename"><xsl:value-of select="$prefix"/>EvidenceRegistrar.java</xsl:variable>

<redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
      
package <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl;
      
      
import curam.codetable.CASEEVIDENCE;
import curam.core.sl.infrastructure.impl.EvidenceController;
import curam.core.sl.infrastructure.impl.EvidenceEntityRegistrar;
import curam.core.sl.infrastructure.impl.EvidenceMap;
<xsl:for-each select="EvidenceEntity">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.fact.<xsl:value-of select="@name"/>Factory;
</xsl:for-each>
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
      
/**
 * @deprecated Since Curam 6.0, replaced with
 *             {@link <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl.<xsl:value-of select="$prefix"/>RegistrarModule}.
 *             <P>
 *             As part of registrars modernization, all type/object hook
 *             mappings of this class have been moved to an AbstractModule class
 *             <xsl:value-of select="$prefix"/>RegistrarModule. See release note: CEF-586.
 *             </P>
 * Evidence Entity Registrar implementation. Registrars must
 * implement the EvidenceEntityRegistrar interface.
 * 
 */
@Deprecated
@SuppressWarnings("all")
public abstract class <xsl:value-of select="$prefix"/>EvidenceRegistrar extends <xsl:value-of select="$javaEvidenceCodePath"/>.service.base.<xsl:value-of select="$prefix"/>EvidenceRegistrar
  implements EvidenceEntityRegistrar {
      
  /**
   * @deprecated Since Curam 6.0, replaced with
   *             {@link <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl.<xsl:value-of select="$prefix"/>RegistrarModule} configure method.
   *             <P>
   *             As part of registrars modernization, all type/object hook
   *             mappings of this class have been moved to an AbstractModule class
   *             <xsl:value-of select="$prefix"/>RegistrarModule. See release note: CEF-586.
   *             </P>
   *
   * Inherited from EvidenceEntityRegistrar interface. Must be implemented
   * by all evidence entity registrars.
   */
  @Deprecated
  public void register() throws AppException, InformationalException {
      
    <!-- BEGIN, CR00100405, CD -->
    EvidenceMap map = EvidenceController.getEvidenceMap();
    <xsl:for-each select="EvidenceEntity">
    map.putEvidenceType(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>,
      <xsl:value-of select="@name"/>Factory.class);
    </xsl:for-each>
    <!-- END, CR00100405 -->
  }
      
}
</redirect:write>
<redirect:close select="$filename"/>
</xsl:template>
<xsl:template match="text()"/>
</xsl:stylesheet>