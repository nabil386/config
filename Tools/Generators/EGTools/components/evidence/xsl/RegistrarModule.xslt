<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2010, 2014, 2015. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2010 Curam Software Ltd.  All rights reserved.

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
<xsl:variable name="filename"><xsl:value-of select="$prefix"/>RegistrarModule.java</xsl:variable>

<redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             
      
package <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl;
      
      
import curam.codetable.CASEEVIDENCE;
import com.google.inject.AbstractModule;
import curam.core.impl.FactoryMethodHelper;
import java.lang.reflect.Method;
import com.google.inject.multibindings.MapBinder;
import curam.core.impl.RegistrarImpl;
import curam.core.impl.Registrar.RegistrarType;
<!-- BEGIN, CR00459476, ZV -->
import curam.evidence.impl.EvidenceTypeRelationship;
<!-- END, CR00459476 -->


      
/**
 * A module class which provides registration for all of the evidence hook implementations.
 */
@SuppressWarnings("all")
public class <xsl:value-of select="$prefix"/>RegistrarModule extends AbstractModule {


  /**
   * Registers the hook implementations to a corresponding type. The type could
   * be evidence type, product type.
   */
  @Override
  public void configure() {
    // Register all hook implementations which implement the interface
    // EvidenceInterface.
    registerEvidenceHookImplementations();
    
    // Register all hook implementations for evidence entity details.
    registerEvidenceEntityDtlsHookImplementations();
  
    // Register all hook implementations which implement the interface
    // Evidence2Compare.
    registerEvidenceToCompareHookImplementations();

    <!-- BEGIN, CR00459476, ZV -->
    // Register EvidenceTypeRelationship implementations.
    registerEvidenceTypeRelationshipImplementations();
    <!-- END, CR00459476 -->
  }
  
  
  
  /**
   * Registers all hook implementations which implement the interface
   * EvidenceInterface.
   */
  protected void registerEvidenceHookImplementations() {
    MapBinder&lt;String, Method&gt; evidenceInterfaceMapBinder = MapBinder
        .newMapBinder(binder(), String.class, Method.class, new RegistrarImpl(
            RegistrarType.EVIDENCE));
  
  
    <xsl:for-each select="EvidenceEntity">
    evidenceInterfaceMapBinder
      .addBinding(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)
        .toInstance(FactoryMethodHelper.getNewInstanceMethod(
          <xsl:value-of select="$javaEvidenceCodePath"/>.entity.fact.<xsl:value-of select="@name"/>Factory.class));
    </xsl:for-each>
     
  }
  
  
  /**
   * Register all hook implementations for evidence entity details.
   */
  protected void registerEvidenceEntityDtlsHookImplementations() {
  
    MapBinder&lt;String, Class&gt; evidenceEntityDtlsMapBinder = MapBinder
        .newMapBinder(binder(), String.class, Class.class);
  
    <xsl:for-each select="EvidenceEntity">
    evidenceEntityDtlsMapBinder
      .addBinding(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)
        .toInstance(<xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="@name"/>Dtls.class);
    </xsl:for-each>
  
  }
  
  
  /**
   * Registers all hook implementations which implement the interface
   * Evidence2Compare.
   */
  protected void registerEvidenceToCompareHookImplementations() {
    
    
    MapBinder&lt;String, Method&gt; evidence2CompareMapBinder = MapBinder
        .newMapBinder(binder(), String.class, Method.class, new RegistrarImpl(
            RegistrarType.EVIDENCE_TO_COMPARE));
  
  
    <xsl:for-each select="EvidenceEntity">
    evidence2CompareMapBinder
      .addBinding(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)
        .toInstance(FactoryMethodHelper.getNewInstanceMethod(
         <xsl:value-of select="$javaEvidenceCodePath"/>.service.fact.<xsl:value-of select="@name"/>Factory.class));
    </xsl:for-each>
   
  }

  <!-- BEGIN, CR00459476, ZV -->
  /**
   * Registers EvidenceTypeRelationship implementations.
   */
  protected void registerEvidenceTypeRelationshipImplementations() {

    MapBinder&lt;String, EvidenceTypeRelationship&gt; evidenceTypeRelationshipMapBinder = MapBinder
        .newMapBinder(binder(), String.class, EvidenceTypeRelationship.class);

    <xsl:for-each select="EvidenceEntity">
      <xsl:if test="count(./Relationships/Child)&gt;0 or count(./Relationships/Parent)&gt;0 or count(./Relationships/MandatoryParents/Parent)&gt;0">
    evidenceTypeRelationshipMapBinder
      .addBinding(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)
        .to(<xsl:value-of select="$javaEvidenceCodePath"/>.service.impl.<xsl:value-of select="@name"/>EvidenceTypeRelationshipImpl.class);
      </xsl:if>
    </xsl:for-each>
  }
  <!-- END, CR00459476 -->
}
</redirect:write>
<redirect:close select="$filename"/>
</xsl:template>
<xsl:template match="text()"/>
</xsl:stylesheet>