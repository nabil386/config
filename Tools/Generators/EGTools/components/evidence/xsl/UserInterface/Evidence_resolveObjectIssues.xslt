<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2011 Curam Software Ltd.  All rights reserved.

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
  
    <xsl:variable name="pagePostfix">Issues</xsl:variable>
    <xsl:variable name="pagename">Evidence_resolveObject<xsl:value-of select="$pagePostfix"/></xsl:variable>
    
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
       
    String successionID = request.getParameter("successionID");

    <!-- BEGIN, CR00383917, ELG -->
    if (successionID == null) {
      throw new Exception("'successionID' parameter does not exist");
    } else {
      // sanitize parameter value
      successionID = curam.omega3.request.RequestUtils.escapeURL(successionID);
    }
    <!-- END, CR00383917 -->
        
    //
    // get the caseID and evidence type
    //    
    curam.interfaces.EvidencePkg.Evidence_getEvidenceAndCaseFromSuccession_TH
      resolveEvidenceAndCase = new curam.interfaces.EvidencePkg.Evidence_getEvidenceAndCaseFromSuccession_TH();

    resolveEvidenceAndCase.setFieldValue(resolveEvidenceAndCase.key$successionID_idx, successionID);

    resolveEvidenceAndCase.callServer();

    String caseID = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$caseIDKey$caseID_idx);
    String evidenceType = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$evidenceKey$evType_idx);
        
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

      url = context + "Evidence_resolveParticipantObject<xsl:value-of select="$pagePostfix"/>Page.do?successionID=" + successionID + "&amp;";

    } else {      
    
      <xsl:for-each select="//products/product[not(substring-after(@clientDir,'webclient/components/')='custom')]">

        <xsl:if test="position()>1">else </xsl:if>
        <xsl:for-each select="evidence">    
          <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">  || </xsl:if>evidenceType.equals("<xsl:value-of select="@type"/>")
        </xsl:for-each> ) {
        url = context + "<xsl:value-of select="@prefix"/>_resolve" + caseTypeValue + "Object<xsl:value-of select="$pagePostfix"/>Page.do?";
      }
      </xsl:for-each>
      
      // Finally, check custom/overridden evidence and redirect accordingly
      <xsl:for-each select="//products/product[substring-after(@clientDir,'webclient/components/')='custom']">
      
        <xsl:for-each select="evidence">    
          <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">  || </xsl:if>evidenceType.equals("<xsl:value-of select="@type"/>")
        </xsl:for-each> ) {
        url = context + "<xsl:value-of select="@prefix"/>_resolve" + caseTypeValue + "Object<xsl:value-of select="$pagePostfix"/>Page.do?";
      }
      </xsl:for-each>      

      <!-- BEGIN, CR00383917, ELG -->
      url += "successionID=" + successionID
          + "&amp;" + "evidenceType=" + evidenceType;
      <!-- END, CR00383917 -->
          
    }
                
    url += "&amp;" + rh.getSystemParameters();
    response.sendRedirect(response.encodeRedirectURL(url));
  
    
  </JSP_SCRIPTLET>
</PAGE>
     </redirect:write>
      
    </xsl:for-each>

  </xsl:template>

</xsl:stylesheet>