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

    <xsl:variable name="entityName" select="@name"/>
    <xsl:variable name="rootPath"/>
    <xsl:variable name="path" select="concat($rootPath, $entityName , '')"/>
  
  
  <xsl:variable name="filename"><xsl:value-of select="$path"/>BObjNavLoader</xsl:variable>
  <xsl:if test="count(Relationships/Parent)&gt;0">
  <redirect:write select="concat($filename, '.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
  

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.tab.impl;

import java.util.Map;
import curam.codetable.CASEEVIDENCE;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.intf.EvidenceController;
import curam.core.sl.infrastructure.struct.BusinessObjectEvidenceTypeKey;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.tab.impl.DynamicNavStateLoader;
import curam.util.tab.impl.NavigationState;

@SuppressWarnings("all")
public class <xsl:value-of select="$entityName"/>BObjNavLoader 
  implements DynamicNavStateLoader {  

  /**
   * Constructor.
   */
  public <xsl:value-of select="$entityName"/>BObjNavLoader() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public NavigationState loadNavState(NavigationState navigation, Map&lt;String, String&gt; pageParameters,
      String[] idsToUpdate) {
    
    // configure menuState
    long sucessionID = Long.parseLong(pageParameters.get(TabLoaderConst.kSuccessionID));
    
    // show/hide and enable/disable links
    try{
      <xsl:for-each select="Relationships/Parent">
      <xsl:variable name="parentName" select="@name"/>
      <xsl:variable name="parentType" select="@type"/>
      navigation.setVisible(isParentPresent(CASEEVIDENCE.<xsl:value-of select="$parentType"/>, sucessionID), <xsl:value-of select="$entityName"/>BObjNavLoaderConst.k<xsl:value-of select="$parentName"/>);
      </xsl:for-each>
      <xsl:for-each select="Relationships/PreAssociation">
      <xsl:variable name="preAssocName" select="@to"/>
      <xsl:variable name="preAssocType" select="translate($preAssocName, $lcletters, $ucletters)"/>
      navigation.setVisible(isParentPresent(CASEEVIDENCE.<xsl:value-of select="$preAssocType"/>, sucessionID), <xsl:value-of select="$entityName"/>BObjNavLoaderConst.k<xsl:value-of select="$preAssocName"/>);
      </xsl:for-each>
     
      
    }catch (AppException e) {
      if (Trace.atLeast(Trace.kTraceUltraVerbose)) { 
        Trace.kTopLevelLogger.info( Trace.exceptionStackTraceAsString(e));
      }
            
    } catch (InformationalException e) {
      if (Trace.atLeast(Trace.kTraceUltraVerbose)) { 
        Trace.kTopLevelLogger.info( Trace.exceptionStackTraceAsString(e));
      }
    }
    
   
    navigation.setVisible(true, TabLoaderConst.kVerifications);
    navigation.setEnabled(true, TabLoaderConst.kVerifications);
    navigation.setVisible(true, TabLoaderConst.kIssues);
    navigation.setEnabled(true, TabLoaderConst.kIssues);
        
    return navigation;
  }    
    
  
  /**
    * Checks if a parent evidence type is present for a child that can have one of multiple parents.
    * @param parentEvidenceType The evidence type of the parent,
    * @param sucessionID the succession identifier for the case that the evidence exists on.
    * @return true if a parent exists, false if a parent doesn't exist.
    * @throws AppException Generic Exception Signature.
    * @throws InformationalException Generic Exception Signature
    */
  private boolean isParentPresent(String parentEvidenceType, long sucessionID) throws AppException, InformationalException {
    BusinessObjectEvidenceTypeKey businessObjectEvidenceTypeKey = new BusinessObjectEvidenceTypeKey();
    businessObjectEvidenceTypeKey.successionID = sucessionID;
    businessObjectEvidenceTypeKey.evidenceType = parentEvidenceType;
    EvidenceController evidenceController = EvidenceControllerFactory.newInstance();
    if(evidenceController.listBusinessObjectsForEvidenceType(businessObjectEvidenceTypeKey).dtls.size()>0){
      return true;
    }
    else{
      return false;
    }
  }

}

</redirect:write>
</xsl:if>

<xsl:if test="count(Relationships/Parent)&gt;0">
<xsl:call-template name="write-menu-const">
   
    
  
  <xsl:with-param name="filepath" select="$path"/>
  <xsl:with-param name="entityName" select="$entityName"/>
  <xsl:with-param name="Relationships" select="Relationships"/>
</xsl:call-template>
</xsl:if>
  
  
  
  
</xsl:template>

  
  
  
<!--iterate through each token, generating each element-->
<xsl:template name="write-menu-const">

  
  <xsl:param name="filepath"/>
  <xsl:param name="entityName"/>
  <xsl:param name="Relationships"/>

<xsl:variable name="constfilename"><xsl:value-of select="$filepath"/>BObjNavLoaderConst</xsl:variable>

<redirect:write select="concat($constfilename, '.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
  

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.tab.impl;

/**
 * This class assigns values to the Nav Loader constants
 */
public abstract class <xsl:value-of select="$entityName"/>BObjNavLoaderConst {  

  
 /**
   * String constants for Business Object Tab.
   */
  
  <xsl:for-each select="$Relationships/Parent">
          
  <xsl:variable name="parentName" select="@name"/>
          
  public static final String k<xsl:value-of select="$parentName"/> = "<xsl:value-of select="$parentName"/>";
          
  </xsl:for-each>
  
  <xsl:for-each select="$Relationships/PreAssociation">
          
  <xsl:variable name="preAssocName" select="@to"/>
          
  public static final String k<xsl:value-of select="$preAssocName"/> = "<xsl:value-of select="$preAssocName"/>";
          
  </xsl:for-each> 
  
 

}



</redirect:write>


</xsl:template>
  
</xsl:stylesheet> 







