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
  
  <xsl:param name="outputDir"/>
  <xsl:param name="serverBuildDir"/>
  
  <xsl:template match="EvidenceEntities">  
      
      <xsl:variable name="CuramXML"><xsl:value-of select="$serverBuildDir"/>/Curam.xml</xsl:variable>  
      
      <xsl:for-each select="EvidenceEntity">
      
      <xsl:variable name="EntityName" select="@name"/>        
      <xsl:variable name="allViewFields" select="UserInterfaceLayer/Cluster[@view='Yes']/Field"/>
      <!-- No need to generate properties in multiple locales as these properties are not for localization -->
      <xsl:variable name="filename"><xsl:value-of select="$outputDir"/>/<xsl:value-of select="$EntityName"/>Domains.properties</xsl:variable>
      
      <redirect:open select="$filename" method="text" append="false"/>
        <redirect:write select="$filename">
        
        
          <xsl:for-each select="$allViewFields">
            
          
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
            
            <xsl:variable name="domainType">
            
              <xsl:choose>
                <xsl:when 
                  test="@metatype=$metatypeParentCaseParticipant or
                        @metatype=$metatypeEmployerCaseParticipant or
                        @metatype=$metatypeCaseParticipantSearch or
                        @metatype=$metatypeDisplayCaseParticipant">CONCERN_ROLE_NAME</xsl:when>
                              
                <xsl:otherwise>
            
                  <xsl:variable name="className">
                    <xsl:choose>
                      <xsl:when test="@metatype=$metatypeRelatedEntityAttribute"><xsl:value-of select="$EntityName"/>RelatedEntityAttributesDetails</xsl:when>
                      <xsl:when test="@notOnEntity='Yes'"><xsl:value-of select="$EntityName"/>NonEvidenceDetails</xsl:when>
                      <xsl:otherwise><xsl:value-of select="$EntityName"/></xsl:otherwise>
                    </xsl:choose>                
                  </xsl:variable>      

                  <xsl:variable name="stereotype">
                    <xsl:choose>
                      <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">struct</xsl:when>
                      <xsl:when test="@notOnEntity='Yes'">struct</xsl:when>
                      <xsl:otherwise>entity</xsl:otherwise>
                    </xsl:choose>
                  </xsl:variable>

                  <!-- Look for an extension to the entity if one exists -->
                  <xsl:variable name="ExtendedType">
                    <xsl:choose>
                      <xsl:when test="$stereotype='entity'"><xsl:value-of select="document($CuramXML)/MODEL/CLASS[@NAME=$className and @STEREOTYPE='extension']/CLASS.ATTRIBUTE[@NAME=$columnName]/@TYPE"/></xsl:when>
                      <xsl:otherwise/>
                    </xsl:choose>
                  </xsl:variable>

                  <!--
                  <xsl:message>

                  NEXT ATTRIBUTE - @columnName - <xsl:value-of select="@columnName"/>            
                  NEXT ATTRIBUTE - columnName - <xsl:value-of select="$columnName"/>          
                  NEXT ATTRIBUTE - qualifiedName - <xsl:value-of select="$qualifiedName"/>                
                  NEXT ATTRIBUTE - className - <xsl:value-of select="$className"/>          
                  NEXT ATTRIBUTE - stereotype - <xsl:value-of select="$stereotype"/>          
                  NEXT ATTRIBUTE - ExtendedType - "<xsl:value-of select="$ExtendedType"/>"

                  </xsl:message>
                  -->

                  <xsl:variable name="returnValue">
                    <xsl:choose>

                      <xsl:when test="$ExtendedType and $ExtendedType!=''">
                        <!--
                        <xsl:message>

                        PRINTING ExtendedType - <xsl:value-of select="$ExtendedType"/>

                        </xsl:message>
                        -->

                        <xsl:value-of select="$ExtendedType"/>                  
                      </xsl:when>

                      <xsl:otherwise>
                        <xsl:variable name="CuramType" 
                          select="document($CuramXML)/MODEL/CLASS[@NAME=$className and @STEREOTYPE=$stereotype]/CLASS.ATTRIBUTE[@NAME=$columnName]/@TYPE"/>
                        <!--
                        <xsl:message>

                        PRINTING CuramType - <xsl:value-of select="$CuramType"/>

                        </xsl:message>
                        -->
                        <xsl:value-of select="$CuramType"/>
                      </xsl:otherwise>

                    </xsl:choose>                
                  </xsl:variable>
                  
                  <!--
                  <xsl:message>
                  
                  returnValue - <xsl:value-of select="$returnValue"/>

                  </xsl:message>
                  -->

                  <xsl:value-of select="$returnValue"/>   
                  
                </xsl:otherwise>
                
              </xsl:choose>
              
            </xsl:variable>
            <!--      
            <xsl:message>

            domainType - <xsl:value-of select="$domainType"/>

            </xsl:message>
            -->    
            <xsl:copy-of select="$qualifiedName"/>=<xsl:copy-of select="$domainType"/><xsl:text>&#xa;</xsl:text>
              
                        
          </xsl:for-each>

        </redirect:write>
      <redirect:close select="$filename"/>
    </xsl:for-each>
    
</xsl:template>


<!-- CDUFFY - COMMENTING OUT AS DGARDNER JUST EXPLAINED THAT THE BASE TYPE ISN'T REQUIRED
<xsl:template name="resolveBaseDomainType">
  
  <xsl:param name="file"/>
  <xsl:param name="domainType"/>  
  
  
  <xsl:variable name="DerivedType" select="document($file)/MODEL/CLASS[@NAME=$domainType]/CLASS.ATTRIBUTE[@NAME=$domainType]/@TYPE"/>
  
  !-
  <xsl:if test="not($DerivedType) or $DerivedType=''">
    <xsl:message>

      resolveBaseDomainType domainType=<xsl:value-of select="$domainType"/>
      resolveBaseDomainType DerivedType=<xsl:value-of select="$DerivedType"/>

    </xsl:message>
  </xsl:if>
  -
  
  <xsl:choose>
    
    !- return domainType for CODETABLE_CODE to get specific codetable type -
    <xsl:when test="$DerivedType='CODETABLE_CODE'"><xsl:value-of select="$domainType"/></xsl:when>
    
    !- if any absolute base types are found, stop searching and return them -
    <xsl:when 
      test="$DerivedType='SVR_STRING' or
            $DerivedType='SVR_DATE' or
            $DerivedType='SVR_DATETIME'"><xsl:value-of select="$DerivedType"/></xsl:when>
    
    !- if no widget friendly base types are found, 
         map the base type to a widget friendly base type  -
    !- If you get a StackOverflow error, it means a type needs to be added here.
         Uncomment the message output above to debug and find out the type. -
    <xsl:when 
      test="substring($DerivedType, 1, 10)='SVR_STRING' or
            $DerivedType='INTERNAL_ID' or
            substring($DerivedType, 1, 7)='SVR_INT' or
            $DerivedType='SVR_DOUBLE' or
            $DerivedType='SVR_MONEY' or
            $DerivedType='CURAM_INDICATOR' or
            $DerivedType='SVR_BOOLEAN'">SVR_STRING</xsl:when>
    
    !- otherwise recursively call this method -
    <xsl:otherwise>
    
      <xsl:variable name="RecursivelyDerivedType">
        <xsl:call-template name="resolveBaseDomainType">
          <xsl:with-param name="file" select="$file"/>
          <xsl:with-param name="domainType" select="$DerivedType"/>
        </xsl:call-template>        
      </xsl:variable>
      
      !-
      <xsl:message>
      
      OTHERWISE RecursivelyDerivedType=<xsl:value-of select="$RecursivelyDerivedType"/>
      
      </xsl:message>
      -
      
      <xsl:choose>
      
        !- when returned value is blank (CODETABLE_CODE), use the derived type -
        <xsl:when test="$RecursivelyDerivedType='CODETABLE_CODE'"><xsl:value-of select="$RecursivelyDerivedType"/></xsl:when>
        
        !- when returned value is not blank, do nothing as the 
             bottom level of recursion will print what it finds. -
        <xsl:otherwise><xsl:value-of select="$RecursivelyDerivedType"/></xsl:otherwise>
      </xsl:choose>
    
    </xsl:otherwise>
    
  </xsl:choose>
  
</xsl:template>
-->

</xsl:stylesheet>