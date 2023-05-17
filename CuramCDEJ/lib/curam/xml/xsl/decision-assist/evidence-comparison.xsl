<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
  xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils"
  exclude-result-prefixes="bidi-utils request-utils">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>

  <xsl:param name="questionnaire-id-param-name"/>
  <xsl:param name="question-id-param-name"/>
  <xsl:param name="page-id-param-name"/>
  <xsl:param name="determination-delivery-id-param-name"/>
  <xsl:param name="page-link"/>
  <xsl:param name="table-header-label"/>
  <xsl:param name="o3rpu-param"/>
  <xsl:param name="table-different-previous"/>
  <xsl:param name="table-different-next"/>
  <!-- The username variable is supplied by the Transformer object in the jsp tag. -->
  <xsl:param name="username"/>

  <xsl:template match="EVIDENCE_COMPARISON">
    <xsl:variable name="node-id"
                  select="concat('mainTabContainer_', generate-id(.))"/>
    <div id="{$node-id}" dojoType="dijit.layout.TabContainer"
         doLayout="false" class="evidence-comparison">
         <xsl:attribute name="textDir">
           <xsl:value-of select="bidi-utils:getBaseTextDirection()"/>
         </xsl:attribute>          
      <xsl:apply-templates select="QUESTIONNAIRE"/>
    </div>    
    <script type="text/javascript">
			<xsl:text>require(["dijit/layout/TabContainer","dijit/layout/ContentPane"]);</xsl:text>
    </script>
  </xsl:template>

  <xsl:template match="QUESTIONNAIRE">
    <div label="{@LABEL}" dojoType="dijit.layout.ContentPane" id="{@LABEL}"
         class="list list-with-header" title="{@LABEL}">
         <xsl:attribute name="textDir">
           <xsl:value-of select="bidi-utils:getBaseTextDirection()"/>
         </xsl:attribute> 
      <table>
        <thead>
          <tr>
            <th class="field first-header">
            	<span>
              	<xsl:attribute name="dir">
              	   <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-header-label)"/>
              	</xsl:attribute>               	
              	<xsl:value-of select="$table-header-label"/>              	
              </span>
            </th>
            <th class="field">
            	<span>
             	 <xsl:value-of select="/EVIDENCE_COMPARISON/@BASELINE1_LABEL"/>
             	</span>
            </th>
            <th class="field last-header">
            	<span>
              	<xsl:value-of select="/EVIDENCE_COMPARISON/@BASELINE2_LABEL"/>
              </span>
            </th>
          </tr>
        </thead>
        <tbody>
          <xsl:apply-templates select="QUESTION"/>
        </tbody>
      </table>
    </div>
  </xsl:template>

  <xsl:template match="QUESTION">
    <xsl:variable name="css-class">
      <xsl:choose>
        <xsl:when test="@HIGHLIGHT = 'false'">field</xsl:when>
        <xsl:otherwise>field changed</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <tr>
      <xsl:if test="position() = last()">
        <xsl:attribute name="class">last-row</xsl:attribute>
      </xsl:if>
      <td class="{$css-class} first-field">
        <xsl:attribute name="dir">
          <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@LABEL)"/>
        </xsl:attribute>      
        <xsl:value-of select="@LABEL"/>
      </td>
      <td class="{$css-class}">
        <xsl:apply-templates select="BASELINE1"/>
        <xsl:if test="not(@HIGHLIGHT = 'false')">
          <span class="changed">
          	<xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-different-next)"/>
          	</xsl:attribute>                
          	<xsl:value-of select="$table-different-next" />
          </span>
        </xsl:if>
      </td>
      <td class="{$css-class} last-field">
        <xsl:apply-templates select="BASELINE2"/>
        <xsl:if test="not(@HIGHLIGHT = 'false')">
          <span class="changed">
          	<xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-different-previous)"/>
          	</xsl:attribute>                
            <xsl:value-of select="$table-different-previous" />
          </span>
        </xsl:if>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="BASELINE1 | BASELINE2">
    <!-- Text takes precedence over answer value. -->
    <xsl:choose>
      <xsl:when test="@DISPLAY_TEXT">
          	<xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@DISPLAY_TEXT)"/>
          	</xsl:attribute>                      
        <xsl:value-of select="@DISPLAY_TEXT"/>          
      </xsl:when>
      <xsl:otherwise>
          	<xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@ANSWER)"/>
          	</xsl:attribute>                      
        <xsl:value-of select="@ANSWER"/>          
      </xsl:otherwise>
    </xsl:choose>
    
    <xsl:text>&#x2029;</xsl:text><!-- Bidi paragraph separator -->

    <xsl:if test="@SHOW_LINK = 'true'">
      <xsl:variable name="linkURI"
          select="concat($page-id-param-name, 'Page.do?',
              $questionnaire-id-param-name, '=', ../../@QUESTIONNAIRE_ID,
              '&amp;', $question-id-param-name, '=', ../@QUESTION_ID, 
              '&amp;', $determination-delivery-id-param-name, '=',
                  /EVIDENCE_COMPARISON/@DETERMINATION_DELIVERY_ID,
              '&amp;', $o3rpu-param)"/>
       <xsl:variable name="linkURIWithHash">
         <xsl:value-of select="request-utils:replaceOrAddURLHashToken($linkURI, $username)"/>
       </xsl:variable>
      <xsl:text> [</xsl:text>
      <a href="{$linkURIWithHash}"><xsl:value-of select="$page-link"/></a>
      <xsl:text>]</xsl:text>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>