<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2014. All Rights Reserved.

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
  <!-- Global Variables -->
  <xsl:import href="UICommon.xslt"/>
  
  <xsl:output method="xml" indent="yes"/>
  
  <xsl:param name="date"/>

  <xsl:template match="products">
  
    <xsl:variable name="pagename">Evidence_resolveWorkspace</xsl:variable>
    
    <xsl:for-each select="product">   
      
    <redirect:write select="concat(@clientDir, '/', $pagename, '.uim')"> 
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>
<xsl:comment> Description                                                            </xsl:comment>
<xsl:comment> ===========                                                            </xsl:comment>
<xsl:comment> This script opens the sample evidence view pages.                      </xsl:comment>

<PAGE PAGE_ID="{$pagename}">
  <JSP_SCRIPTLET>    


    curam.omega3.request.RequestHandler 
      rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);

    String context = request.getContextPath() + "/";
    context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/";
       
    String caseID = request.getParameter("caseID");
    String evidenceType = request.getParameter("evidenceType");

    <!-- BEGIN, CR00383917, ELG -->
    String evidenceID = request.getParameter("evidenceID");
    String contextDescription = request.getParameter("contextDescription");
    String parentEvidenceType = request.getParameter("parentEvidenceType");

    if (caseID == null) {
      throw new Exception("'caseID' parameter does not exist");
    } else {
      // sanitize parameter value
      caseID = curam.omega3.request.RequestUtils.escapeURL(caseID);
    }
    
    if (evidenceType == null) {
      throw new Exception("'evidenceType' parameter does not exist");
    } else {
      // sanitize parameter value
      evidenceType = curam.omega3.request.RequestUtils.escapeURL(evidenceType);
    }

    if (null != evidenceID) {
      // sanitize parameter value
      evidenceID = curam.omega3.request.RequestUtils.escapeURL(evidenceID);
    }

    if (null != contextDescription) {
      // sanitize parameter value
      contextDescription = curam.omega3.request.RequestUtils.escapeURL(contextDescription);
    }
    
    if (null != parentEvidenceType) {
      // sanitize parameter value
      parentEvidenceType = curam.omega3.request.RequestUtils.escapeURL(parentEvidenceType);
    }
    <!-- END, CR00383917 -->
        
    <!-- BEGIN, CR00221984, CD -->
    <!-- Removing save link code as it was no longer referenced -->
    <!-- END, CR00221984, CD -->
    
    //
    // get the case type portion of the url
    //

    curam.interfaces.CasePkg.Case_resolveCaseType_TH 
       resolveCaseType = new curam.interfaces.CasePkg.Case_resolveCaseType_TH();

    resolveCaseType.setFieldValue(resolveCaseType.readCaseTypeDetails$caseID_idx, caseID);

    resolveCaseType.callServer();

    String caseTypeCode = resolveCaseType.getFieldValue(resolveCaseType.result$caseTypeCode$caseTypeCode_idx);

    String caseTypeValue = "Evidence";

    if (caseTypeCode.equals("CT2")) {

      caseTypeValue = "ProductEvidence";

    }
    
    //
    // ensure this isn't participant evidence as that can be viewed only
    // through the participant manager section
    //
      
    curam.interfaces.EvidencePkg.Evidence_isEvidenceParticipantData_TH
            pd_TH = new curam.interfaces.EvidencePkg.Evidence_isEvidenceParticipantData_TH();
    pd_TH.setFieldValue(pd_TH.evidenceTypeKey$evidenceType_idx, evidenceType);

    pd_TH.callServer();

    String pd = pd_TH.getFieldValue(pd_TH.result$participantDataInd_idx);
    
    String url = "";
    
    if (pd.equals("true")) {

      url = context + "Evidence_participantDataWarningPage.do?" + rh.getSystemParameters();

    } else {      
    
      <xsl:for-each select="//products/product[not(substring-after(@clientDir,'webclient/components/')='custom')]">

        <xsl:if test="position()>1">else </xsl:if>
        <xsl:for-each select="evidence">    
          <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">  || </xsl:if>evidenceType.equals("<xsl:value-of select="@type"/>")
        </xsl:for-each> ) {
        url = context + "<xsl:value-of select="@prefix"/>_resolve" + caseTypeValue + "WorkspacePage.do?";
      }
      </xsl:for-each>
      
      // Finally, check custom/overridden evidence and redirect accordingly
      <xsl:for-each select="//products/product[substring-after(@clientDir,'webclient/components/')='custom']">
      
        <xsl:for-each select="evidence">    
          <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">  || </xsl:if>evidenceType.equals("<xsl:value-of select="@type"/>")
        </xsl:for-each> ) {
        url = context + "<xsl:value-of select="@prefix"/>_resolve" + caseTypeValue + "WorkspacePage.do?";
      }
      </xsl:for-each>      

      <!-- BEGIN, CR00383917, ELG -->
      <!-- BEGIN, CR00221984, CD -->
      url += "caseID=" + caseID
          + "&amp;" + "evidenceID=" + evidenceID
          + "&amp;" + "evidenceType=" + evidenceType 
          + "&amp;" + "contextDescription=" + contextDescription
          + "&amp;" + "parentEvidenceType=" + parentEvidenceType
          + "&amp;" + rh.getSystemParameters();
      <!-- END, CR00221984, CD -->
      <!-- END, CR00383917 -->
          
    }
                
    response.sendRedirect(response.encodeRedirectURL(url));
  
    
  </JSP_SCRIPTLET>
</PAGE>
     </redirect:write>
      
    </xsl:for-each>

  </xsl:template>

</xsl:stylesheet>