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
  
<xsl:template match="EvidenceEntity[Relationships/@relatedEntityAttributes='Yes']">
    
  <xsl:variable name="EntityName"><xsl:value-of select="@name"/></xsl:variable>
  <xsl:variable name="RelatedEntityAttributesName"><xsl:value-of select="$EntityName"/>RelatedEntityAttributes</xsl:variable>
  <xsl:variable name="filename"><xsl:value-of select="$RelatedEntityAttributesName"/>.java</xsl:variable>

<xsl:variable name="codePath">  
  <xsl:choose>
    <xsl:when test="$entitiesToGenerate='CUSTOM_EXTENSION'"><xsl:value-of select="$customJavaEvidenceCodePath"/></xsl:when>  
    <xsl:otherwise><xsl:value-of select="$javaEvidenceCodePath"/></xsl:otherwise>  
  </xsl:choose>
</xsl:variable>
          
<xsl:if test="($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer[Override])=0) or
              ($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer/Override[@newEntity!='Yes'])=1) or
              ($entitiesToGenerate='CUSTOM_NEW' and count(EntityLayer/Override[@newEntity='Yes'])&gt;0) or
              ($entitiesToGenerate='CUSTOM_EXTENSION' and count(EntityLayer/Override[@relatedAttribute='Yes'])&gt;0)"> 
               
<xsl:variable name="keyStruct">
  <xsl:choose>
    <xsl:when test="Relationships/@preAssociation='Yes'">curam.core.sl.struct.PreAssocCaseEvKey</xsl:when>
    <xsl:otherwise>curam.core.sl.struct.EvidenceCaseKey</xsl:otherwise>
  </xsl:choose>
</xsl:variable>

<redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
            
package <xsl:value-of select="$codePath"/>.relatedattribute.impl;
                  
import curam.core.sl.struct.EvidenceCaseKey;
import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$RelatedEntityAttributesName"/>Details;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Class for retrieving the related entity attribute for a specified entity.
 */
            
public abstract class <xsl:value-of select="$EntityName"/>RelatedEntityAttributes 
  extends <xsl:value-of select="$codePath"/>.relatedattribute.base.<xsl:value-of select="$EntityName"/>RelatedEntityAttributes {
            
  // ___________________________________________________________________________
  /**
   * Retrieves the related entity attribute for a specified piece of evidence.
   * A related entity attribute is an attribute that appears on an evidence
   * page but is not stored on that particular entity. An example would be 
   * a course name appearing on a Student details page. The course name will
   * be stored on a Course entity but needs to be retrieved for viewing on the
   * Student details page.
   *
   * @param evidenceKey Contains the evidenceID / evidenceType pairing
   * @param caseKey Contains the case identifier
   *
   * @return Related entity attribute
   */
  public <xsl:value-of select="$RelatedEntityAttributesName"/>Details getRelatedEntityAttributes(
    <xsl:value-of select="$keyStruct"/> key) 
    throws AppException, InformationalException {   
            
    // Return struct
    <xsl:value-of select="$RelatedEntityAttributesName"/>Details relatedEntityAttributeDetails = 
      new <xsl:value-of select="$RelatedEntityAttributesName"/>Details(); 
            
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    relatedEntityAttributeDetails = super.getRelatedEntityAttributes(evidenceCaseKey);
    </xsl:if>
    // @TODO Write getRelatedEntityAttributes code
            
    return relatedEntityAttributeDetails;         
  }
            
}    
</redirect:write>
<redirect:close select="$filename"/>    

</xsl:if> <!-- end if replaceSuperclass -->
<!-- END, CR00100405 -->

</xsl:template>
<xsl:template match="text()"/>
</xsl:stylesheet>