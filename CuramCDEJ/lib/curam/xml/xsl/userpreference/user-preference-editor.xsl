<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2022. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:code-table="http://xml.apache.org/xalan/java/curam.omega3.codetable.CodeTableRepository"
  xmlns:domain-info="http://xml.apache.org/xalan/java/curam.util.client.domain.util.DomainUtils"
  xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
  exclude-result-prefixes="code-table domain-info bidi-utils" version="1.0">
  <xsl:import href="../common/ui-field.xsl"/>
  <xsl:import href="../common/string-utils.xsl" />
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
  <xsl:param name="locale"/>
  <xsl:param name="yes"/>
  <xsl:param name="no"/>
  <xsl:param name="externalUserInd"/>
  
    <xsl:template match="/PreferenceSet">
      <xsl:for-each select="Preference">
      <!-- Create and set the visibleAttribute, if the user is external, then set the 
           visibleAttribute to be the value of externalVisible, otherwise the user must
           be internal, so set the visibleAttribute to be the value of visible. -->
      <xsl:variable name="falseConstant" select="'false'"/>
      <xsl:variable name="trueConstant" select="'true'" />
      <xsl:variable name="encoded-name">
        <xsl:call-template name="replace-chars-in-string">
          <xsl:with-param name="string-in" select="@name"/>
          <xsl:with-param name="chars-in" select="'.'"/>
          <xsl:with-param name="chars-out" select="'\.'"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="visibleAttribute">
        <xsl:choose>
          <xsl:when test="$externalUserInd = 'true'">
            <xsl:choose>
              <!-- As the externalVisible element is optional, we need to check for its
                   existence. If it does not exist, then set the visibleAttribute to false -->
              <xsl:when test="./externalVisible">
                <xsl:value-of select="./externalVisible"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$falseConstant"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
            <!-- As the visible element is optional, we need to check for its existence. 
                 If it does not exist, then set the visibleAttribute to true -->
              <xsl:when test="./visible">
                <xsl:value-of select="./visible"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$trueConstant"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="field-name" select="concat('__o3urpf.userPreferenceBean.userPreferenceList$userPrefXML.', ./type)"/>
      <xsl:choose>
        <xsl:when test="$visibleAttribute = 'true'">
          <div class="bx--row">
            <div class="bx--col bx--col-sm-4 bx--col-md-8 bx--col-lg-16 bx--col-xlg-16 bx--col-max-16">
              <xsl:choose>
                <xsl:when test="./readonly = 'true'">
                  <dl class="bx--cluster__item bx--cluster__item--read-only-field">
                    <dt title="Label" class="bx--label">
                      <xsl:value-of select="./title" />
                    </dt>
                    <dd class="bx--field">
                      <xsl:call-template name="read-only-field">
                        <xsl:with-param name="name" select="concat(concat(concat(concat($field-name, '.'), $locale), '.'), $encoded-name)"/>
                        <xsl:with-param name="title" select="title"/>
                        <xsl:with-param name="value" select="value"/>
                        <xsl:with-param name="domain" select="type"/>
                      </xsl:call-template>
                    </dd>
                  </dl>
                </xsl:when>
                <xsl:otherwise>
                  <div class="bx--cluster__item bx--cluster__item--input">
                    <label title="Label" class="bx--label">
                      <xsl:value-of select="./title" />
                    </label>
                    <div class="bx--field">
                      <xsl:call-template name="gen-field">
                         <xsl:with-param name="name" select="concat(concat(concat(concat($field-name, '.'), $locale), '.'), $encoded-name)"/>
                        <xsl:with-param name="title" select="title"/>
                        <xsl:with-param name="value" select="value"/>
                        <xsl:with-param name="domain" select="type"/>
                      </xsl:call-template>
                    </div>
                  </div>
                </xsl:otherwise>
              </xsl:choose>
            </div>
          </div>
        </xsl:when>
        <!-- Output the hidden input field when a preference option is set to invisible -->
        <xsl:otherwise>
           <xsl:variable name="name" select="concat(concat(concat(concat($field-name, '.'), $locale), '.'), $encoded-name)"/>
          <input type="hidden" name="{$name}" value="{value}" title="{title}"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
            
    </xsl:template>

    <!-- Handles read only rendering of userpreference fields -->
    <xsl:template name="read-only-field">
      <xsl:param name="name" />
      <xsl:param name="title" />
      <xsl:param name="value" />
      <xsl:param name="domain"/>
      
      <xsl:variable name="ct-name" select="domain-info:getDomainNodeSet($domain)/@code-table-name" />
      
      <!-- Handles generation of hidden input fields depending on type -->
      <xsl:choose>
         <xsl:when test="$ct-name">
           <xsl:variable name="code-table" select="code-table:getCodeTableNodeSet($ct-name, $locale)" />
           <xsl:for-each select="$code-table/item">
              <xsl:if test="@code = $value">
                <span class="read-only">
                  <xsl:attribute name="dir">
                    <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(description/text())"/>
                  </xsl:attribute>                
                  <xsl:value-of select="description/text()" />
                </span>
                <input type="hidden" name="{$name}" value="{$value}" title="{$title}"/>
              </xsl:if>
           </xsl:for-each>
         </xsl:when>
         <xsl:when test="./type = 'SVR_BOOLEAN'">
             <xsl:choose>
                 <xsl:when test="./value = 'true'">
                   <span class="read-only"><xsl:value-of select="$yes"/></span>
                   <input type="hidden" name="{$name}" title="{$title}" value="true" />
                 </xsl:when>
                 <xsl:otherwise>
                   <span class="read-only"><xsl:value-of select="$no"/></span>
                   <input type="hidden" name="{$name}" title="{$title}" value="false" />
                 </xsl:otherwise>
             </xsl:choose>
         </xsl:when>
         <xsl:otherwise>
             <span class="read-only"><xsl:value-of select="value" /></span>
             <input type="hidden" name="{$name}" value="{$value}" title="{$title}"/>
         </xsl:otherwise>
      </xsl:choose>

    </xsl:template>

    <!-- Handles generation of userpreference fields depending on type -->
    <xsl:template name="gen-field">
      <xsl:param name="name" />
      <xsl:param name="title" />
      <xsl:param name="value" />
      <xsl:param name="domain" />
      <xsl:param name="style" />

      <xsl:variable name="ct-name"
        select="domain-info:getDomainNodeSet($domain)/@code-table-name" />

      <xsl:variable name="domain-nodeset"
        select="domain-info:getDomainNodeSet($domain)" />

      <xsl:variable name="root-domain-name" select="$domain-nodeset/@root-domain" />

      <xsl:choose>
        <xsl:when test="$ct-name">
          <xsl:variable name="code-table" select="code-table:getCodeTableNodeSet($ct-name, $locale)" />
          <xsl:call-template name="gen-code-table-list-field">
            <xsl:with-param name="name" select="$name" />
            <xsl:with-param name="title" select="$title" />
            <xsl:with-param name="value" select="$value" />
            <xsl:with-param name="code-table" select="$code-table" />
            <xsl:with-param name="style" select="$style" />
          </xsl:call-template>
        </xsl:when>
         <xsl:when test="./type = 'SVR_BOOLEAN'"> 
            <xsl:choose>
                <xsl:when test="./value = 'true'">
                    <input id="{$name}_yes" class="radio-yes curam-checkbox" type="radio" name="{$name}" title="{$title}" checked="checked" value="true"/>
                    <label for="{$name}_yes" aria-hidden="true">&#160;</label>
                    <label for="{$name}_yes" class="checkbox-touchable-area" title="{$title}" aria-hidden="true">&#160;</label>
                    <label for="{$name}_yes" class="label"><xsl:value-of select="$yes"/></label>       
                    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    <input id="{$name}_no" class="radio-no curam-checkbox" type="radio" name="{$name}" title="{$title}" value="false"/>
                    <label for="{$name}_no" aria-hidden="true">&#160;</label>
                    <label for="{$name}_no" class="checkbox-touchable-area" title="{$title}" aria-hidden="true">&#160;</label>
                    <label for="{$name}_no" class="label"><xsl:value-of select="$no"/></label> 
                </xsl:when>
                <xsl:otherwise>
                    <input id="{$name}_yes" class="radio-yes curam-checkbox" type="radio" name="{$name}" title="{$title}" value="true"/>
                    <label for="{$name}_yes" aria-hidden="true">&#160;</label>
                    <label for="{$name}_yes" class="checkbox-touchable-area" title="{$title}" aria-hidden="true">&#160;</label>
                    <label for="{$name}_yes" class="label"><xsl:value-of select="$yes"/></label>       
                    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    <input id="{$name}_no" class="radio-no curam-checkbox" type="radio" name="{$name}" title="{$title}" value="false" checked="checked"/>
                    <label for="{$name}_no" aria-hidden="true">&#160;</label>
                    <label for="{$name}_no" class="checkbox-touchable-area" title="{$title}" aria-hidden="true">&#160;</label>
                    <label for="{$name}_no" class="label"><xsl:value-of select="$no"/></label> 
                </xsl:otherwise>                
            </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
            <input class="non-radio" type="text" name="{$name}" value="{$value}" title="{$title}"/>
        </xsl:otherwise>
      </xsl:choose>

    </xsl:template>

</xsl:stylesheet>