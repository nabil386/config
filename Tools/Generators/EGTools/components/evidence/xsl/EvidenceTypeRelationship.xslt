<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2015. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:output method="text" indent="yes"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />

  <xsl:param name="date"/>

  <xsl:template match="EvidenceEntity[EntityLayer]">

    <xsl:variable name="EntityName"><xsl:value-of select="@name"/></xsl:variable>

    <xsl:if test="count(./Relationships/Child)&gt;0 or count(./Relationships/Parent)&gt;0 or count(./Relationships/MandatoryParents/Parent)&gt;0">

    <redirect:write select="concat($EntityName, 'EvidenceTypeRelationshipImpl.java')">
    <xsl:call-template name="printJavaCopyright">
      <xsl:with-param name="date" select="$date"/>
    </xsl:call-template>
package <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl;
    <xsl:call-template name="GetImports"/>
/**
 * <xsl:value-of select="$EntityName"/> implementation for {@link curam.evidence.impl.EvidenceTypeRelationship}
 */
public class <xsl:value-of select="concat($EntityName, 'EvidenceTypeRelationshipImpl')"/> extends curam.evidence.impl.DefaultEvidenceTypeRelationshipImpl {
      <xsl:if test="count(./Relationships/Parent)&gt;0 or count(./Relationships/MandatoryParents/Parent)&gt;0">
  /**
   * {@inheritDoc}
   */
  @Override
  public EvidenceTypeDtlsList getParentTypeList(final CASEEVIDENCEEntry evidenceType) 
    throws AppException, InformationalException {

    final EvidenceTypeDtlsList evidenceTypeDtlsList = new EvidenceTypeDtlsList();
    EvidenceTypeDtls evidenceTypeDtls;
      <xsl:for-each select="./Relationships/Parent">
    evidenceTypeDtls = new EvidenceTypeDtls();
    evidenceTypeDtls.evidenceType = CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>;
    evidenceTypeDtlsList.dtls.addRef(evidenceTypeDtls);
      </xsl:for-each>
      <xsl:for-each select="./Relationships/MandatoryParents/Parent">
    evidenceTypeDtls = new EvidenceTypeDtls();
    evidenceTypeDtls.evidenceType = CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>;
    evidenceTypeDtlsList.dtls.addRef(evidenceTypeDtls);
      </xsl:for-each>

    return evidenceTypeDtlsList;
  }
      </xsl:if>
      <xsl:if test="count(./Relationships/Child)&gt;0">
  /**
   * {@inheritDoc}
   */
  @Override
  public EvidenceTypeDtlsList getChildTypeList(final CASEEVIDENCEEntry evidenceType) 
    throws AppException, InformationalException {

    final EvidenceTypeDtlsList evidenceTypeDtlsList = new EvidenceTypeDtlsList();
    EvidenceTypeDtls evidenceTypeDtls;
      <xsl:for-each select="./Relationships/Child">
    evidenceTypeDtls = new EvidenceTypeDtls();
    evidenceTypeDtls.evidenceType = CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>;
    evidenceTypeDtlsList.dtls.addRef(evidenceTypeDtls);
      </xsl:for-each>
    return evidenceTypeDtlsList;
  }
      </xsl:if>
}
    </redirect:write>

    </xsl:if>

</xsl:template>

<xsl:template name="GetImports">
import curam.codetable.CASEEVIDENCE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.struct.EvidenceTypeDtls;
import curam.core.sl.infrastructure.struct.EvidenceTypeDtlsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
</xsl:template>

</xsl:stylesheet>