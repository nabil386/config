<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2010,2014. All Rights Reserved.

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

<xsl:output method="xml" indent="yes"/>

<xsl:template name="ResolveEvidenceCreateWizard">
        
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>
  <xsl:param name="capName" />

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>
          
  <xsl:variable name="numberOfIDAndTypePairsRequired">
    <xsl:variable name="stringToGetLengthOf">
      <xsl:if test="count(Relationships/Parent) &gt; 0">P</xsl:if>
      <xsl:if test="Relationships/@mulitplePreAssociationID='Yes' and 
                    count(Relationships/PreAssociation[@name!='']) &gt; 0"
                    ><xsl:for-each select="Relationships/PreAssociation[@name!='']">A</xsl:for-each></xsl:if>
      <xsl:if test="count(Relationships/PreAssociation[@to!='']) &gt; 0">B</xsl:if>
      <xsl:if test="count(Relationships/Related[@to!='']) &gt; 0">R</xsl:if>
      <xsl:if test="count(Relationships/MandatoryParents/Parent) &gt; 0"
                    ><xsl:for-each select="Relationships/MandatoryParents/Parent">M</xsl:for-each></xsl:if>
    </xsl:variable>    
<!-- Potentially a useful debugging message
<xsl:message>

EVGEN DEBUG OUT (<xsl:value-of select="$capName"/>): 
  stringToGetLengthOf=<xsl:value-of select="$stringToGetLengthOf"/>
  numberOfIDAndTypePairsRequired=<xsl:value-of select="string-length($stringToGetLengthOf)"/>
</xsl:message>
-->
    <xsl:value-of select="string-length($stringToGetLengthOf)"/>
  </xsl:variable>
  
    
      
  <xsl:if test="$numberOfIDAndTypePairsRequired &gt; 0">
                            
                   
  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE  PAGE_ID="{$UIName}">
  <JSP_SCRIPTLET>
          
    curam.omega3.request.RequestHandler 
      rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);

    String context = request.getContextPath() + "/";
    context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/";

    String successionID = request.getParameter("successionID");

    String pageName="";
    String url = context;

    if (successionID == null) {
      core.ScriptletMissingParamException e = 
        new core.ScriptletMissingParamException(-20002, "successionID");
      System.out.println(e);
      throw e;
    }

    //
    // get the caseID and evidence id and type of the related record
    //    
    
    curam.interfaces.EvidencePkg.Evidence_getEvidenceAndCaseFromSuccession_TH
      resolveEvidenceAndCase = new curam.interfaces.EvidencePkg.Evidence_getEvidenceAndCaseFromSuccession_TH();

    resolveEvidenceAndCase.setFieldValue(resolveEvidenceAndCase.key$successionID_idx, successionID);

    resolveEvidenceAndCase.callServer();

    String caseID = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$caseIDKey$caseID_idx);
    String inputID = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$evidenceKey$evidenceID_idx);
    String inputType = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$evidenceKey$evType_idx);
    
   <!-- Note: These checks are done in blocks. So for parent types there's a 
              block of if{} else if{} statements, and the next block begins 
              with an if{}. This should not cause an issue as the evidence 
              type should only be in one of the relationships. -->
   <xsl:choose>
     <xsl:when test="$numberOfIDAndTypePairsRequired &gt; 1">       
     
       <xsl:if test="count(Relationships/Parent) &gt; 0">
         <xsl:for-each select="Relationships/Parent">    
           <xsl:variable name="currentType">
             <xsl:call-template name="JavaID2CodeValue"> 
               <xsl:with-param name="java_identifier" select="@type"/>  
               <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
               <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
             </xsl:call-template>
           </xsl:variable> 
     
    <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@name"/>
      url = context 
            + "<xsl:value-of select="$prefix"/>_wizSelect<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>"
            + "Page.do?parEvID="+ inputID
            + "&amp;parEvType="+ inputType;
    }
        </xsl:for-each>
       </xsl:if>
   
       <xsl:if test="count(Relationships/MandatoryParents/Parent) &gt; 0">   
         <xsl:for-each select="Relationships/MandatoryParents/Parent">    
           <xsl:variable name="currentType">
             <xsl:call-template name="JavaID2CodeValue"> 
               <xsl:with-param name="java_identifier" select="@type"/>  
               <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
               <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
             </xsl:call-template>
           </xsl:variable> 

    <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@name"/>
      url = context 
            + "<xsl:value-of select="$prefix"/>_wizSelect<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>"
            + "Page.do?<xsl:value-of select="@name"/>ParEvID="+ inputID
            + "&amp;<xsl:value-of select="@name"/>ParEvType="+ inputType;
    }
         </xsl:for-each>
       </xsl:if> 
         
       <xsl:choose> 

         <xsl:when test="Relationships/@mulitplePreAssociationID='Yes' and 
                         count(Relationships/PreAssociation[@name!='']) &gt; 0">  

           <xsl:for-each select="Relationships/PreAssociation[@name!='']">    
              <xsl:variable name="currentType">
                <xsl:call-template name="JavaID2CodeValue"> 
                  <xsl:with-param name="java_identifier" select="@name"/>  
                  <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                  <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
                </xsl:call-template>
               </xsl:variable>

    <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@name"/>
      url = context 
            + "<xsl:value-of select="$prefix"/>_wizSelect<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>"
            + "Page.do?<xsl:value-of select="@name"/>PreAssocID="+ inputID
            + "&amp;<xsl:value-of select="@name"/>PreAssocType="+ inputType;
    }
          </xsl:for-each>

         </xsl:when>

         <xsl:when test="count(Relationships/PreAssociation[@to!='']) &gt; 0"> 

           <xsl:for-each select="Relationships/PreAssociation[@to!='']">    
              <xsl:variable name="currentType">
                <xsl:call-template name="JavaID2CodeValue"> 
                  <xsl:with-param name="java_identifier" select="@to"/>  
                  <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                  <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
                </xsl:call-template>
               </xsl:variable>

    <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@to"/>
      url = context 
            + "<xsl:value-of select="$prefix"/>_wizSelect<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>"
            + "Page.do?preAssocID="+ inputID
            + "&amp;preAssocType="+ inputType;
    }
          </xsl:for-each>
         </xsl:when>
       </xsl:choose> 

       <!-- not 100% sure if the business object tab of Related/@to type has option to create evidence it's related from -->
       <xsl:if test="count(Relationships/Related[@to!='']) &gt; 0">
         <xsl:for-each select="Relationships/Related[@to!='']">    
            <xsl:variable name="currentType">
              <xsl:call-template name="JavaID2CodeValue"> 
                <xsl:with-param name="java_identifier" select="@to"/>  
                <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
              </xsl:call-template>
            </xsl:variable> 

    <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@to"/>
      url = context 
            + "<xsl:value-of select="$prefix"/>_wizSelect<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>"
            + "Page.do?relEvidenceID="+ inputID
            + "&amp;relEvidenceType="+ inputType;
    }
         </xsl:for-each>
       </xsl:if>

    </xsl:when>

    <xsl:otherwise>
    <!-- only one set of ids required so redirect to simple create (with parent or preassoc already selected) -->
    url = context 
         + "<xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>"
         <xsl:choose>
           <xsl:when test="count(Relationships/Parent) &gt; 0">
         + "Page.do?parEvID="+ inputID
         + "&amp;parEvType="+ inputType
           </xsl:when>
           <xsl:when test="count(Relationships/PreAssociation) &gt; 0">
         + "Page.do?preAssocID="+ inputID
         + "&amp;preAssocType="+ inputType
           </xsl:when>
           <xsl:when test="count(Relationships/Related[@to!='']) &gt; 0">
         + "Page.do?relEvidenceID="+ inputID
         + "&amp;relEvidenceType="+ inputType</xsl:when></xsl:choose>;
    </xsl:otherwise>
  </xsl:choose>

    url += "&amp;caseID=" + curam.omega3.request.RequestUtils.escapeURL(caseID)
         + "&amp;" + rh.getSystemParameters(); 

    // Send the response
    response.sendRedirect(response.encodeRedirectURL(url));
    
  </JSP_SCRIPTLET>

</PAGE>
  
  </redirect:write>
  
  </xsl:if>

</xsl:template>

</xsl:stylesheet>