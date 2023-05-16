<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

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

 <xsl:template name="JavaID2CodeValue">

   <xsl:param name="java_identifier"/>
   <xsl:param name="codetableName"/>   
   <!-- BEGIN, CR00094128, CD -->
   <xsl:param name="serverBuildDir"/>
  
   <xsl:variable name="codetableDir"><xsl:value-of select="$serverBuildDir"/>/svr/codetable/scp</xsl:variable>
   <!-- END, CR00094128 -->
                          
   <!-- To allow for Uppercase to Lowercase conversion -->
   <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
   <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>

   <!-- Get Java Identifier in uppercase -->
   <xsl:variable name="javaIdentifierUC">
     <xsl:value-of select="translate($java_identifier, $lcletters, $ucletters)"/>
   </xsl:variable>

  <xsl:variable name="returnValue">
    <xsl:call-template name="get-one-value">       
      <xsl:with-param name="javaIdentifierUC" select="$javaIdentifierUC"/>
      <xsl:with-param name="codetableName" select="$codetableName"/>
      <xsl:with-param name="codetableDir" select="$codetableDir"/>
    </xsl:call-template> 
   </xsl:variable>

   <xsl:value-of select="$returnValue"/>

 </xsl:template>
 
 
 <xsl:template name="get-one-value">

   <xsl:param name="javaIdentifierUC"/>
   <xsl:param name="codetableName"/>
   <xsl:param name="codetableDir"/>
   
   <xsl:variable name="fileName"><xsl:value-of select="$codetableDir"/>/CT_<xsl:value-of select="$codetableName"/>.ctx</xsl:variable>  
   
   <xsl:value-of select="document($fileName)/codetables/codetable/code[@java_identifier=$javaIdentifierUC]/@value"/>
    
  </xsl:template>

 
 </xsl:stylesheet>