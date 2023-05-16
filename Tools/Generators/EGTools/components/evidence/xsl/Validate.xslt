<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006, 2014. All Rights Reserved.

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
    
<xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
<xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>
<xsl:variable name="EntityValidateName">Validate<xsl:value-of select="$EntityName"/></xsl:variable>

<xsl:variable name="codePath">  
  <xsl:choose>
    <xsl:when test="$entitiesToGenerate='CUSTOM_EXTENSION'"><xsl:value-of select="$customJavaEvidenceCodePath"/></xsl:when>  
    <xsl:otherwise><xsl:value-of select="$javaEvidenceCodePath"/></xsl:otherwise>  
  </xsl:choose>
</xsl:variable>
          
<xsl:if test="($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer[Override])=0) or
              ($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer/Override[@newEntity!='Yes'])=1) or
              ($entitiesToGenerate='CUSTOM_NEW' and count(EntityLayer/Override[@newEntity='Yes'])&gt;0) or
              ($entitiesToGenerate='CUSTOM_EXTENSION' and count(EntityLayer/Override[@validation='Yes'])&gt;0)"> 
               
<redirect:write select="concat($EntityValidateName, &apos;.java&apos;)">

<xsl:variable name="CuramEvidenceDetails"><xsl:value-of select="$EntityName"/>Dtls</xsl:variable>

<xsl:variable name="productEvidenceDetails"><xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

package <xsl:value-of select="$codePath"/>.validation.impl;
        
import curam.core.sl.infrastructure.struct.ValidateMode;
import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$CuramEvidenceDetails"/>;
import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$productEvidenceDetails"/>;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
          
public abstract class Validate<xsl:value-of select="$EntityName"/> extends <xsl:value-of select="$codePath"/>.validation.base.Validate<xsl:value-of select="$EntityName"/> { 
  
  // ___________________________________________________________________________
  /**
   * Performs processing prior to the insertion of the adoption record.
   *
   * @param dtls The evidence details to be inserted
   */
  public void preInsertValidate(<xsl:value-of select="$CuramEvidenceDetails"/> dtls) 
    throws AppException,InformationalException {
  
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.preInsertValidate(dtls);
    </xsl:if>
    
    // Validate the details
    validateDetails(dtls);  
  }
          
  // ___________________________________________________________________________
  /**
   * Performs processing prior to the modification of the adoption record.
   *
   * @param dtls The evidence details to be modified.
   */
  public void preModifyValidate(<xsl:value-of select="$CuramEvidenceDetails"/> dtls) 
    throws AppException,InformationalException { 
  
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.preModifyValidate(dtls);
    </xsl:if>
    
    // Validate the details
    validateDetails(dtls);  
  }  
          
  // ___________________________________________________________________________
  /**
   * Validates the evidence details.
   *
   * @param details contains alien sponsor evidence details to validate.
   */
  public void validateDetails(<xsl:value-of select="$CuramEvidenceDetails"/> dtls) 
    throws AppException,InformationalException {
  
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.validateDetails(dtls);
    </xsl:if>
    
    // @TODO write implementation code    
  }                        
          
  // ___________________________________________________________________________
  /**
   * Validates the evidence details
   *
   * @param dtls The evidence details
   * @param mode Validate mode (insert, delete, applyChanges, modify)
   */            
  public void validate(ValidateMode mode, <xsl:value-of select="$productEvidenceDetails"/> dtls) 
    throws AppException, InformationalException {
  
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    super.validate(mode, dtls);
    </xsl:if>
    
    // @TODO write implementation code     
  }
     
}
</redirect:write>

</xsl:if> <!-- end if replaceSuperclass -->
<!-- END, CR00100405 -->

<xsl:value-of select="@name"/><xsl:text>
</xsl:text>
</xsl:template>
<xsl:template match="text()"/>
</xsl:stylesheet>