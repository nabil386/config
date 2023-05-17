<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2006-2008 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>


<!-- Global Variables -->
<xsl:include href="EvidenceCommon.xslt" />  
<xsl:param name="date"/>

<!-- BEGIN, CR00100405, CD -->
<!-- Possible values for entitiesToGenerate:
       OOTB_ONLY) Write to OOTB dir for all OOTB product entities
      CUSTOM_NEW) Write dummy OOTB version to BUILD dir for brand new entities 
                  that appear in a CUSTOM product which overrides an OOTB 
                  product. Note - this means that service and facade layer code
                  can import this dummy OOTB one which makes maintaining the 
                  generator a lot easier.  The dummy OOTB one will be 
                  overridden by (CUSTOM_EXTENSION) using the replace superclass
                  option.        
CUSTOM_EXTENSION) Write to CUSTOM dir for all extension entities that appear in
                  a CUSTOM product which overrides an OOTB product.
-->
<xsl:param name="entitiesToGenerate"/>

<xsl:template match="EvidenceEntity">
    
<xsl:variable name="entityName"><xsl:value-of select="@name"/></xsl:variable>
<xsl:variable name="entityCustomiseName">Customise<xsl:value-of select="$entityName"/></xsl:variable>

<xsl:variable name="codePath">  
  <xsl:choose>
    <xsl:when test="$entitiesToGenerate='CUSTOM_EXTENSION'"><xsl:value-of select="$customJavaEvidenceCodePath"/></xsl:when>  
    <xsl:otherwise><xsl:value-of select="$javaEvidenceCodePath"/></xsl:otherwise>  
  </xsl:choose>
</xsl:variable>
          
<xsl:if test="($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer[Override])=0) or
              ($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer/Override[@newEntity!='Yes'])=1) or
              ($entitiesToGenerate='CUSTOM_NEW' and count(EntityLayer/Override[@newEntity='Yes'])&gt;0) or
              ($entitiesToGenerate='CUSTOM_EXTENSION' and count(EntityLayer/Override[@customize='Yes'])&gt;0)">  

<redirect:write select="concat($entityCustomiseName, &apos;.java&apos;)">


<xsl:variable name="productEvidenceDetails"><xsl:value-of select="$entityName"/>EvidenceDetails</xsl:variable>
    <xsl:variable name="readEvidenceDetails">Read<xsl:value-of select="$productEvidenceDetails"/></xsl:variable>

<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

package <xsl:value-of select="$codePath"/>.customise.impl;

import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.sl.struct.ReturnEvidenceDetails;
import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$productEvidenceDetails"/>;
import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$readEvidenceDetails"/>;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
          
public abstract class <xsl:value-of select="$entityCustomiseName"/> extends <xsl:value-of select="$codePath"/>.customise.base.<xsl:value-of select="$entityCustomiseName"/> { 
  
  // ___________________________________________________________________________
  /**
   * Performs custom processing at the beginning of the normal service layer
   * create() method
   *
   * @param dtls The evidence details to be inserted
   */
  public void preCreate(<xsl:value-of select="$productEvidenceDetails"/> dtls) 
    throws AppException,InformationalException {
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.preCreate(dtls);
    </xsl:if>
    // @TODO write implementation code here
  }
  
  
  // ___________________________________________________________________________
  /**
   * Performs custom processing at the end of the normal service layer create()
   * method
   *
   * @param dtls The evidence details to be inserted
   * @param returnDtls The result of the create method
   */
  public void postCreate(<xsl:value-of select="$productEvidenceDetails"/> dtls, ReturnEvidenceDetails returnDtls) 
    throws AppException,InformationalException {
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.postCreate(dtls, returnDtls);
    </xsl:if>    
    // @TODO write implementation code here
  }
  
  
  // ___________________________________________________________________________
  /**
   * Performs custom processing at the beginning of the normal service layer
   * modify() method
   *
   * @param dtls The evidence details to be inserted
   */
  public void preModify(<xsl:value-of select="$productEvidenceDetails"/> dtls) 
    throws AppException,InformationalException {
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.preModify(dtls);
    </xsl:if>    
    // @TODO write implementation code here
  }
  
  
  // ___________________________________________________________________________
  /**
   * Performs custom processing at the end of the normal service layer modify()
   * method
   *
   * @param dtls The evidence details to be inserted
   * @param returnDtls The result of the modify method
   */
  public void postModify(<xsl:value-of select="$productEvidenceDetails"/> dtls, ReturnEvidenceDetails returnDtls) 
    throws AppException,InformationalException {
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.postModify(dtls, returnDtls);
    </xsl:if>    
    // @TODO write implementation code here
  }
  
  <!-- BEGIN, 25/02/2008, CD -->
  // ___________________________________________________________________________
  /**
   * Performs custom processing at the beginning of the normal service layer
   * read() method
   *
   * @param key The evidence and case identifiers of the details to be read
   */
  public void preRead(EvidenceCaseKey key) 
    throws AppException,InformationalException {
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.preRead(key);
    </xsl:if>
    // @TODO write implementation code here
  }
  

  // ___________________________________________________________________________
  /**
   * Performs custom processing at the end of the normal service layer read()
   * method
   *
   * @param key The evidence and case identifiers of the details to be read
   * @param readEvidenceDetails The result of the read method
   */
  public void postRead(EvidenceCaseKey key, <xsl:value-of select="$readEvidenceDetails"/> readEvidenceDetails) 
    throws AppException,InformationalException {
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.postRead(key, readEvidenceDetails);
    </xsl:if>
    // @TODO write implementation code here
  }
  <!-- END, 25/02/2008, CD -->   
}
</redirect:write>

</xsl:if> <!-- end if replaceSuperclass -->
<!-- END, CR00100405 -->

<xsl:value-of select="@name"/><xsl:text>
</xsl:text>
</xsl:template>
<xsl:template match="text()"/>
</xsl:stylesheet>