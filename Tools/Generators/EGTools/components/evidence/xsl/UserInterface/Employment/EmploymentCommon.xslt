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

  <xsl:variable name="employmentCreateUIName">_createEmployment</xsl:variable>
  <xsl:variable name="employmentCancelUIName">_cancelEmployment</xsl:variable>
  <xsl:variable name="employmentModifyUIName">_modifyEmployment</xsl:variable>
  <xsl:variable name="employmentReadUIName">_viewEmployment</xsl:variable>
  
  <xsl:variable name="employmentCreateMethod">createEmployment</xsl:variable> 
  <xsl:variable name="employmentCancelMethod">cancelEmployment</xsl:variable> 
  <xsl:variable name="employmentModifyMethod">modifyEmployment</xsl:variable> 
  <xsl:variable name="employmentReadMethod">readEmployment</xsl:variable> 
  

</xsl:stylesheet>
