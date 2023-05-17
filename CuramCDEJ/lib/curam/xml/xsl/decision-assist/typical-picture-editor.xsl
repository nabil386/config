<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright (c) 2010 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:code-table="http://xml.apache.org/xalan/java/curam.omega3.codetable.CodeTableRepository"
	xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils" 
	exclude-result-prefixes="code-table bidi-utils">
  <xsl:import href="../common/ui-field.xsl"/>
  <xsl:import href="../dynamic-menu/build-menu.xsl"/>
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
  <xsl:param name="bean-param-name"/>
  <xsl:param name="field-param-name"/>
  <xsl:param name="table-header-question"/>
  <xsl:param name="table-header-value"/>
  <xsl:param name="table-header-min"/>
  <xsl:param name="table-header-max"/>
  <xsl:param name="label-minimum"/>
  <xsl:param name="label-maximum"/>
  <xsl:template match="TYPICAL_PICTURE">
    <div class="cda-typical-picture-editor">
    <table class="list">
      <thead class="column-header">
        <tr>
          <th class="first-header">
          	<span>
          	  <xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-header-question)"/>
          	  </xsl:attribute>          	
              <xsl:value-of select="$table-header-question"/>
            </span>
          </th>
          <xsl:if test="@SHOW_MIN_MAX = 'false'">
            <th class="last-header">
              <span>          
          	  <xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-header-value)"/>
          	  </xsl:attribute>          	
                <xsl:value-of select="$table-header-value"/>
              </span>
            </th>
          </xsl:if>
          <xsl:if test="@SHOW_MIN_MAX = 'true'">
            <th>
              <span>          
          	  <xsl:attribute name="dir">
          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-header-value)"/>
          	  </xsl:attribute>          	
                <xsl:value-of select="$table-header-value"/>
              </span>
            </th>
            <th>
            	<span>
	          	  <xsl:attribute name="dir">
    	      	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-header-min)"/>
        	  	  </xsl:attribute>          	            	
                <xsl:value-of select="$table-header-min"/>
              </span>
            </th>
            <th class="last-header">
            	<span>
          		  <xsl:attribute name="dir">
	          	  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-header-max)"/>
	          	  </xsl:attribute>          	            	
                <xsl:value-of select="$table-header-max"/>
              </span>
            </th>
          </xsl:if>
        </tr>
      </thead>
      <tbody>
        <xsl:apply-templates select="QUESTION"/>
      </tbody>
    </table>
    </div>
  </xsl:template>
  <xsl:template match="QUESTION">
    <xsl:variable name="prefix" select="'__o3tpe'"/>
    <xsl:variable name="prefix-name" select="concat($prefix, '.', $bean-param-name, '.', $field-param-name)"/>
    <xsl:variable name="prefix-hidden" select="'__o3fwlc'"/>
    <xsl:variable name="prefix-hidden-name" select="concat($prefix-hidden, '.', $bean-param-name, '.', $field-param-name)"/>
    <xsl:variable name="question-id" select="@QUESTION_ID"/>
    <xsl:variable name="label" select="@LABEL"/>
    <xsl:variable name="domain" select="@DOMAIN"/>
    <xsl:variable name="root-domain" select="@ROOT_DOMAIN"/>
    <tr>
      <!-- Create a td and if it is last QUESTION then add a first-col class to this td's class 
           This class is added to implement roundy corners on the footer of a table-->
      <td class="column widget-border left-col">
        <xsl:if test="position() = last()">
          <xsl:attribute name="class">column widget-border left-col first-footer</xsl:attribute>
        </xsl:if>
        <xsl:attribute name="dir">
          <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(label)"/>
        </xsl:attribute>          	            	        
        <xsl:value-of select="$label"/>
      </td>
      
      <td class="column widget-border">
        <!-- Apply the following template if position==last and min/max are not present.
               The following template will add a class for roundy corners -->
        <xsl:if test="/TYPICAL_PICTURE/@SHOW_MIN_MAX = 'false'">
          <xsl:attribute name="class">column widget-border right-col</xsl:attribute>
          <xsl:if test="position() = last()">
            <xsl:attribute name="class">column widget-border right-col last-footer</xsl:attribute>
          </xsl:if>
        </xsl:if>
      <xsl:variable name="input-hidden-name" select="concat($prefix-hidden-name, '.', $question-id, '.', 'VALUE')"/>
        <input type="hidden" id="{$input-hidden-name}" name="{$input-hidden-name}" value="{$label}"/>
        <xsl:choose>
          <xsl:when test="@CODETABLE">
            <xsl:variable name="ct-name" select="@CODETABLE"/>
            <xsl:variable name="code-table" select="code-table:getCodeTableNodeSet($ct-name, $locale)"/>
            <xsl:call-template name="gen-code-table-list-field">
              <xsl:with-param name="name" select="concat($prefix-name, '.',  $domain, '.', $locale, '.', $question-id, '.', 'VALUE')"/>
              <xsl:with-param name="value" select="@VALUE"/>
              <xsl:with-param name="code-table" select="$code-table"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:when test="$root-domain='SVR_BOOLEAN'">
             <xsl:call-template name="gen-true-false-list-field">
              <xsl:with-param name="id" select="@QUESTION_ID"/>
              <xsl:with-param name="name" select="concat($prefix-name, '.',  $domain, '.', $locale, '.', $question-id, '.', 'VALUE')"/>
              <xsl:with-param name="value" select="@VALUE"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="gen-input-field">
              <xsl:with-param name="name" select="concat($prefix-name, '.',  $domain, '.', $locale, '.', $question-id, '.', 'VALUE')"/>
              <xsl:with-param name="value" select="@VALUE"/>
              <xsl:with-param name="domain" select="$domain"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <xsl:if test="/TYPICAL_PICTURE/@SHOW_MIN_MAX = 'true'">
        <td class="column widget-border">
          <xsl:choose>
            <xsl:when test="@MIN">
              <xsl:variable name="input-hidden-name-min" select="concat($prefix-hidden-name, '.', $question-id, '.', 'MIN')"/>
              <xsl:variable name="label-min" select="concat($label, ' - ', $label-minimum)"/>
              <input type="hidden" id="{$input-hidden-name-min}" name="{$input-hidden-name-min}" value="{$label-min}"/>                       	            	                     
              <xsl:call-template name="gen-input-field">
                <xsl:with-param name="name" select="concat($prefix-name, '.',  $domain, '.', $locale, '.', $question-id, '.', 'MIN')"/>
                <xsl:with-param name="value" select="@MIN"/>
              </xsl:call-template>            
            </xsl:when>
            <!-- Have to ensure table cells aren't empty so IE will draw borders -->
            <xsl:otherwise>&#160;</xsl:otherwise>
          </xsl:choose>
        </td>
        <td class="column widget-border right-col">
        <!-- Apply the following template if position==last.
             The following template will add a class for roundy corners -->
          <xsl:if test="position() = last()">
            <xsl:attribute name="class">column widget-border right-col last-footer</xsl:attribute>
          </xsl:if>
          <xsl:choose>
            <xsl:when test="@MAX">
              <xsl:variable name="input-hidden-name-max" select="concat($prefix-hidden-name, '.', $question-id, '.', 'MAX')"/>
              <xsl:variable name="label-max" select="concat($label, ' - ', $label-maximum)"/>
              <input type="hidden" id="{$input-hidden-name-max}" name="{$input-hidden-name-max}" value="{$label-max}"/>                        	            	                                    
              <xsl:call-template name="gen-input-field">
                <xsl:with-param name="name" select="concat($prefix-name, '.',  $domain, '.', $locale, '.', $question-id, '.', 'MAX')"/>
                <xsl:with-param name="value" select="@MAX"/>
              </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>&#160;</xsl:otherwise>
          </xsl:choose>
        </td>
      </xsl:if>
    </tr>
  </xsl:template>
  
</xsl:stylesheet>
