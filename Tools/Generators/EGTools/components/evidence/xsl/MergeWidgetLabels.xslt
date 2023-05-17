<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2008,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2008 Curam Software Ltd.  All rights reserved.

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
  <xsl:import href="EvidenceCommon.xslt"/>
    
  <xsl:output method="text" indent="yes"/>
  
  <xsl:param name="localeList"/>
  <xsl:param name="outputDir"/>
  
  <xsl:template match="EvidenceEntities">  
  
    <xsl:for-each select="EvidenceEntity">
     
      <xsl:variable name="filepath"><xsl:value-of select="$outputDir"/>/<xsl:value-of select="@name"/>Labels</xsl:variable>

      <xsl:call-template name="write-all-locales-view-content-properties">
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$filepath"/>
      </xsl:call-template>
      
    </xsl:for-each>

  </xsl:template>
      
      
  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-view-content-properties">

     <xsl:param name="locales"/>
     <xsl:param name="filepath"/>

     <!--tokens still exist-->
     <xsl:if test="$locales">

       <xsl:choose>

         <!--more than one-->
         <xsl:when test="contains($locales,',')">

           <xsl:call-template name="write-view-content-properties">
             <xsl:with-param name="locale"
                            select="concat('_', substring-before($locales,','))"/>
             <xsl:with-param name="filepath"
                            select="$filepath"/>
           </xsl:call-template>

           <!-- Recursively call self to process all locales -->
           <xsl:call-template name="write-all-locales-view-content-properties">
             <xsl:with-param name="locales"
                             select="substring-after($locales,',')"/>
             <xsl:with-param name="filepath"
                            select="$filepath"/>
           </xsl:call-template>

         </xsl:when>

         <!--only one token left-->
         <xsl:otherwise>

           <!-- Call for the final locale -->
           <xsl:call-template name="write-view-content-properties">
             <xsl:with-param name="locale" select="concat('_', $locales)"/>
             <xsl:with-param name="filepath"
                            select="$filepath"/>
           </xsl:call-template>

           <!-- Finally call for the default locale -->
           <xsl:call-template name="write-view-content-properties">
             <xsl:with-param name="locale"/>
             <xsl:with-param name="filepath" select="$filepath"/>
           </xsl:call-template>

         </xsl:otherwise>

       </xsl:choose>

     </xsl:if>

  </xsl:template>

  <xsl:template name="write-view-content-properties">

    <xsl:param name="locale"/>
    <xsl:param name="filepath"/>

    <xsl:variable name="EntityName" select="@name"/>
    
    <xsl:if test="count(.//*[@locale=$locale])&gt;0">

<redirect:write select="concat($filepath, $locale, '.properties')">

  <xsl:for-each select="UserInterfaceLayer/Cluster[not(@view) or @view!='No']">
    
    <xsl:for-each select="Field">
    
      <xsl:variable name="columnName"><xsl:choose>            
                
        <xsl:when test="@metatype=$metatypeParentCaseParticipant or
                        @metatype=$metatypeEmployerCaseParticipant or
                        @metatype=$metatypeCaseParticipantSearch or
                        @metatype=$metatypeDisplayCaseParticipant"><xsl:choose>
            <xsl:when test="@name and @name!=''"><xsl:value-of select="concat(@name, 'C')"/></xsl:when>
            <xsl:otherwise>c</xsl:otherwise>
          </xsl:choose>aseParticipantDetails.<xsl:choose>
        <xsl:when test="@metatype=$metatypeEmployerCaseParticipant">employerName</xsl:when>
        <xsl:otherwise>caseParticipantName</xsl:otherwise>
      </xsl:choose></xsl:when>                

        <xsl:otherwise><xsl:value-of select="@columnName"/></xsl:otherwise>              

      </xsl:choose></xsl:variable>            

      <xsl:variable name="qualifiedName"><xsl:choose>
          <xsl:when test="@metatype=$metatypeParentCaseParticipant">nameOf_<xsl:value-of select="@name"/></xsl:when>
          <xsl:otherwise>result.<xsl:choose>
            <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">relatedEntityAttributes.</xsl:when>
            <xsl:when 
              test="@metatype=$metatypeEmployerCaseParticipant or
                    @metatype=$metatypeCaseParticipantSearch or
                    @metatype=$metatypeDisplayCaseParticipant"></xsl:when>
            <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails.</xsl:when>
            <xsl:otherwise>dtls.</xsl:otherwise>
          </xsl:choose><xsl:value-of select="$columnName"/></xsl:otherwise>
      </xsl:choose></xsl:variable>   
      
      <xsl:choose>
        <xsl:when test="@metatype=$metatypeComments">
      
      <xsl:if test="../@label!=''">
        <xsl:if test="count(../Label[@locale=$locale])&gt;0">
          <xsl:value-of select="$qualifiedName"/>=<xsl:value-of select="../Label[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
        </xsl:if>
      </xsl:if>
          
        </xsl:when>
        <xsl:otherwise>
      
      <xsl:if test="@label!=''">
        <xsl:if test="count(./Label[@locale=$locale])&gt;0">
          <xsl:value-of select="$qualifiedName"/>=<xsl:value-of select="./Label[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
        </xsl:if>
      </xsl:if>
          
        </xsl:otherwise>
      </xsl:choose>
      
    </xsl:for-each>

  </xsl:for-each>

  </redirect:write>

    </xsl:if>
</xsl:template>

</xsl:stylesheet>