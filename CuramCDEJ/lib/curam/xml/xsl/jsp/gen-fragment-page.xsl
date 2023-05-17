<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2002-2018 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose such
Confidential Information and shall use it only in accordance with the terms
of the license agreement you entered into with Curam Software.
-->
<!--
Generates the "next generation" JSP tags for FIELD elements that are associated
with domain definitions.
-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:curam="http://www.curamsoftware.com/curam"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:cing="http://www.curamsoftware.com/curam/jde/client/curam-ng">

  <xsl:template match="DISPLAY" mode="fragment-page">

    <xsl:variable name="static-page-id" select="ancestor::PAGE/@PAGE_ID"/>
    <xsl:variable name="page-locale" select="../@LOCALE" />
    <xsl:variable name="is-error-page"
        select="$static-page-id = $error-page-id"/>
    
    
    <jsp:root version="2.0">
      <xsl:apply-templates select="." mode="page-directive">
        <xsl:with-param name="page-locale" select="$page-locale" />
        <xsl:with-param name="is-error-page" select="$is-error-page" />
        <xsl:with-param name="is-data-service-page" select="true()" />
      </xsl:apply-templates>

      <jsp:output omit-xml-declaration="yes" />

      <!--Fragment pages, unlike reg UIM do not have a built in check against this attack-->
      <jsp:scriptlet>
        <![CDATA[curam.util.client.jsp.JspUtil.detectMHTMLXSSAttack(pageContext);]]>
      </jsp:scriptlet>

      <xsl:variable name="has-scriptlet-only" 
                    select="count(JSP_SCRIPTLET) = 1 and count(*) = 1" />
      <xsl:choose>
        <xsl:when test="$has-scriptlet-only">
          <xsl:apply-templates select="JSP_SCRIPTLET"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="." mode="fragment-page-user-preferences">
            <xsl:with-param name="page-locale" select="$page-locale" />
          </xsl:apply-templates>
          <!-- do not add action phase interfaces to the display phase -->
          <xsl:apply-templates select="SERVER_INTERFACE[not(@PHASE='ACTION')]"/>
          <xsl:apply-templates
            select="BEAN_SET_FIELD[not(@TARGET_BEAN = 'BROWSER')]" mode="usual"/>
          <xsl:apply-templates select="CALL_SERVER"/>
          <xsl:apply-templates select="." mode="blank-page-body"/>
        </xsl:otherwise>
      </xsl:choose>
    </jsp:root>

  </xsl:template>

  <!--generate user preferences for Data Service request -->
  <xsl:template match="DISPLAY" mode="fragment-page-user-preferences">
    <xsl:param name="page-locale"/>

    <curam:userPreferences>
      <xsl:apply-templates
        select="SERVER_INTERFACE | ../ACTION/SERVER_INTERFACE"
        mode="security-check"/>
    </curam:userPreferences>

    <c:set var="pageId" scope="request">
      <xsl:attribute name="value">
        <xsl:choose>
          <xsl:when test="ancestor::PAGE/@COMPONENT_STYLE = $duim-host-style">
            <xsl:text>${param.__o3dpid}</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="ancestor::PAGE/@PAGE_ID"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </c:set>

  </xsl:template>

  <xsl:template match="DISPLAY" mode="blank-page-body" >
  
    <xsl:variable name="submit-action-control"
                  select="descendant::ACTION_CONTROL[
                              @ACTION_TYPE = 'SUBMIT'
                              or @ACTION_TYPE = 'SUBMIT_AND_DISMISS']"/>
    <curam:removeMessages/>
    <cing:page pageID="${{requestScope.pageId}}" 
               fragmentUIM="true">
      <curam:ifMessagesExist>
        <div id="error-messages-container" aria-live="assertive">
            <ul id="error-messages" class="messages" tabindex="0">
              <curam:messageLoop>
                <curam:getCurrentMessage/>
              </curam:messageLoop>
            </ul>
        </div>
      </curam:ifMessagesExist>
      <xsl:choose>
        <xsl:when test="$submit-action-control">
        <cing:form method="post" action="${{requestScope.pageId}}Action.do"
                   fragmentUIM="true">
          <xsl:apply-templates select="HIDDEN_FIELD" />        
          <xsl:apply-templates select="FIELD" />        
        </cing:form>        
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="HIDDEN_FIELD" />        
          <xsl:apply-templates select="FIELD" />    
        </xsl:otherwise>
      </xsl:choose>
    </cing:page>
  </xsl:template>

</xsl:stylesheet>
