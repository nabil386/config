<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2006-2008 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not disclose such
Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
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
  
<xsl:template match="EvidenceEntity">
    
<xsl:variable name="EntityHookName"><xsl:value-of select="@name"/>Hook</xsl:variable>
<xsl:variable name="filename"><xsl:value-of select="$EntityHookName"/>.java</xsl:variable>

<xsl:variable name="codePath">  
  <xsl:choose>
    <xsl:when test="$entitiesToGenerate='CUSTOM_EXTENSION'"><xsl:value-of select="$customJavaEvidenceCodePath"/></xsl:when>  
    <xsl:otherwise><xsl:value-of select="$javaEvidenceCodePath"/></xsl:otherwise>  
  </xsl:choose>
</xsl:variable>
          
<xsl:if test="($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer[Override])=0) or
              ($entitiesToGenerate='OOTB_ONLY' and count(EntityLayer/Override[@newEntity!='Yes'])=1) or
              ($entitiesToGenerate='CUSTOM_NEW' and count(EntityLayer/Override[@newEntity='Yes'])&gt;0) or
              ($entitiesToGenerate='CUSTOM_EXTENSION' and count(EntityLayer/Override[@hook='Yes'])&gt;0)"> 


<redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
                        
package <xsl:value-of select="$codePath"/>.hook.impl;
                             
import curam.core.sl.infrastructure.entity.struct.AttributedDateDetails;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIFieldsForListDisplayDtls;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
                
public abstract class <xsl:value-of select="$EntityHookName"/> extends <xsl:value-of select="$codePath"/>.hook.base.<xsl:value-of select="$EntityHookName"/> {

  // ___________________________________________________________________________
  /**
   * Performs Attribution specific to this Entity
   *
   * @param evidenceKey The evidence key
   * @param caseKey The case key
   *
   * @return Attribution dates
   */                          
  public AttributedDateDetails calcAttributionDatesForCase(CaseKey caseKey, 
    EIEvidenceKey evidenceKey) 
    throws AppException, InformationalException {   
             
    // Return struct
    AttributedDateDetails attributedDateDetails = new AttributedDateDetails(); 
            
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    attributedDateDetails = super.calcAttributionDatesForCase(caseKey, evidenceKey);
    </xsl:if>
    //@TODO Write attribution code
                          
    return attributedDateDetails;         
  }
  
  //___________________________________________________________________________
  /**
   * Get evidence details for the list display
   *
   * @param key Evidence key containing the evidenceID and evidenceType
   *
   * @return Evidence details to be displayed on the list page
   */
  public EIFieldsForListDisplayDtls getDetailsForListDisplay(EIEvidenceKey key)
    throws AppException, InformationalException {
  
    // Return object
    EIFieldsForListDisplayDtls eiFieldsForListDisplayDtls =
      new EIFieldsForListDisplayDtls();
    
    <xsl:if test="$entitiesToGenerate='CUSTOM_EXTENSION'">
    // Calling the OOTB solution. Remove if unwanted.
    eiFieldsForListDisplayDtls = super.getDetailsForListDisplay(key);
    </xsl:if>
    // @TODO Write code for list display details here
    
    return eiFieldsForListDisplayDtls;
  }
  
}        

</redirect:write>
<redirect:close select="$filename"/>

</xsl:if> <!-- end if replaceSuperclass -->
<!-- END, CR00100405 -->

</xsl:template>
<xsl:template match="text()"/>
</xsl:stylesheet>