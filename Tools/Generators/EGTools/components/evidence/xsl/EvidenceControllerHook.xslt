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
    
    <xsl:variable name="className"><xsl:value-of select="$prefix"/>EvidenceControllerHook</xsl:variable>
    
    <redirect:write select="concat($className,'.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl;

import curam.codetable.CASEEVIDENCE;
import curam.core.sl.infrastructure.struct.EvidenceTypeDtls;
import curam.core.sl.infrastructure.struct.EvidenceTypeDtlsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * This process class allows methods in the core evidence controller hook to
 * be overridden.
 *
 */
public abstract class <xsl:value-of select="$className"/>
  extends <xsl:value-of select="$javaEvidenceCodePath"/>.service.base.<xsl:value-of select="$className"/> {
  
  
  //___________________________________________________________________________
  /**
   * Lists all top level evidence types regardless of case type.
   *
   * @return List of evidence types
   */
  public EvidenceTypeDtlsList getTopLevelEvidence()
    throws AppException, InformationalException {

    // Hardcoded list of top level evidence,
    // i.e. evidence with no parent evidence
    String[] crossCaseTypeTopLevelEvidenceList = {
      <xsl:for-each select="//EvidenceEntities/EvidenceEntity[count(Relationships/Parent)=0]">CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/><xsl:if test="position()!=last()">,
      </xsl:if>
      </xsl:for-each>};    

    EvidenceTypeDtlsList evidenceTypeDtlsList = new EvidenceTypeDtlsList();
    EvidenceTypeDtls evidenceTypeDtls;
    for (int i=0; i &lt; crossCaseTypeTopLevelEvidenceList.length; i++) {
      evidenceTypeDtls = new EvidenceTypeDtls();
      evidenceTypeDtls.evidenceType = crossCaseTypeTopLevelEvidenceList[i];
      evidenceTypeDtlsList.dtls.add(evidenceTypeDtls);
    }
    return evidenceTypeDtlsList;
  }

}

        
    </redirect:write>
    
  </xsl:template> 
  
</xsl:stylesheet>
