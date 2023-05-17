<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:format="http://xml.apache.org/xalan/java/java.text.NumberFormat"
  xmlns:double="http://xml.apache.org/xalan/java/java.lang.Double"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils"
  exclude-result-prefixes="format double xalan request-utils">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" /> 

  <!-- standard curam return page parameter-->
  <xsl:param name="returnPageParam" />
  <!-- a Java number formatting object  -->
  <xsl:param name="numberFormat" />
  <!-- the outputType parameter to switch between payment and liability statement output -->
  <xsl:param name="outputType" />
  <!-- Resource strings passed to stylesheet from tag -->
  <xsl:param name="PaymentStatement.Text.Action" />
  <xsl:param name="PaymentStatement.Text.Period" />
  <xsl:param name="PaymentStatement.Text.Desc" />
  <xsl:param name="PaymentStatement.Text.Actual" />
  <xsl:param name="PaymentStatement.Text.Reassessed" />
  <xsl:param name="PaymentStatement.Text.Received" />
  <xsl:param name="PaymentStatement.Text.Diff" />
  <xsl:param name="PaymentStatement.RowLink.PageID" />
  <xsl:param name="PaymentStatement.RowLink.ParameterName" />
  <xsl:param name="PaymentStatement.RowLink.Image" />
  <xsl:param name="PaymentStatement.RowLink.Label" />
  <xsl:param name="PaymentStatement.Text.fromToDateSeparator" />
  <xsl:param name="PaymentStatement.Text.GrossTotal" />
  <xsl:param name="PaymentStatement.Text.TaxTotal" />
  <xsl:param name="PaymentStatement.Text.UtilityTotal" />
  <xsl:param name="PaymentStatement.Text.LiabilityTotal" />
  <xsl:param name="PaymentStatement.Text.NetTotal" />
  <xsl:param name="PaymentStatement.Table.Summary" />
  <xsl:param name="static-content-server-url" />
  <!-- The username variable is supplied by the Transformer object in the jsp tag. -->
  <xsl:param name="username" />

  <!--
    ATTRIBUTE-SETS SECTION
  -->
  <!-- Payment Statement table attribute set-->
  <xsl:attribute-set name="payment-statement-table">
    <xsl:attribute name="summary">
      <xsl:value-of select="$PaymentStatement.Table.Summary"/>
    </xsl:attribute>
  </xsl:attribute-set>
  
  <!-- Payment Statement resource bundle -->
  <xsl:template match="PAYMENT_STATEMENT">
    <table id="paymentStatement" class="payment-statement" xsl:use-attribute-sets="payment-statement-table">
	    <col class="actionCol" />
	    <col class="periodCol" />
	    <col class="descCol" />
	    <col class="actualCol" />
	    <col class="reassessedCol" />
	    <xsl:if test="$outputType='liability'">
	      <col class="receivedCol" />
	    </xsl:if>
	    <col class="diffCol" />
      <thead class="header">
        <tr>
          <th scope="col" class= "field first-header">
          <span>
          <xsl:value-of select="$PaymentStatement.Text.Action" />
          </span>
          </th>
          <th scope="col">
          <span>
            <xsl:value-of select="$PaymentStatement.Text.Period" />
            </span>
          </th>
          <th scope="col">
          <span>
            <xsl:value-of select="$PaymentStatement.Text.Desc" />
            </span>
          </th>
          <th scope="col" class="field right">
          <span>
            <xsl:value-of select="$PaymentStatement.Text.Actual" />
            </span>
          </th>
          <th scope="col" class="field right">
          <span>
            <xsl:value-of select="$PaymentStatement.Text.Reassessed" />
          </span>            
          </th>
          <xsl:if test="$outputType='liability'">
            <th scope="col" class="field right">
            <span>
              <xsl:value-of select="$PaymentStatement.Text.Received" />
              </span>
            </th>
          </xsl:if>
          <th scope="col" class= "field last-header">
          <span>
            <xsl:value-of select="$PaymentStatement.Text.Diff" />
            </span>
          </th>
        </tr>
      </thead>
      <tfoot>
        <xsl:if test="not($outputType='liability')">
          <xsl:call-template name="print-total">
            <xsl:with-param name="total-name"
              select="$PaymentStatement.Text.GrossTotal" />
            <xsl:with-param name="total-amount" select="TOTAL_GROSS" />
          </xsl:call-template>
          <xsl:call-template name="print-total">
            <xsl:with-param name="total-name"
              select="$PaymentStatement.Text.TaxTotal" />
            <xsl:with-param name="total-amount" select="TOTAL_TAX" />
          </xsl:call-template>
          <xsl:call-template name="print-total">
            <xsl:with-param name="total-name"
              select="$PaymentStatement.Text.UtilityTotal" />
            <xsl:with-param name="total-amount" select="TOTAL_UTILITY" />
          </xsl:call-template>
          <xsl:call-template name="print-total">
            <xsl:with-param name="total-name"
              select="$PaymentStatement.Text.LiabilityTotal" />
            <xsl:with-param name="total-amount" select="TOTAL_LIABILITY" />
          </xsl:call-template>
        </xsl:if>
        <xsl:call-template name="print-total">
          <xsl:with-param name="total-name"
            select="$PaymentStatement.Text.NetTotal" />
          <xsl:with-param name="total-amount" select="TOTAL_NET" />
        </xsl:call-template>
      </tfoot>
      <tbody>
        <xsl:apply-templates select="ROW" />
      </tbody>
    </table>
  </xsl:template>

  <xsl:template match="ROW">
   <xsl:variable name="class">
   <xsl:choose>
   <xsl:when test="position() mod 2 = 0">even</xsl:when>
   <xsl:otherwise>odd</xsl:otherwise>
   </xsl:choose>
   </xsl:variable> 
    <tr class="{$class}">
      <td class="action">
        <xsl:apply-templates select="." mode="action" />
      </td>
      <td class="period">
        <xsl:apply-templates select="." mode="from-to-date" />
      </td>
      <td class="description">
        <xsl:apply-templates select="ITEM" mode="description" />
      </td>
      <td class="actual">
        <xsl:apply-templates select="ITEM/ACTUAL_AMOUNT" />
      </td>
      <td class="reassessed">
        <xsl:apply-templates select="ITEM/REASSESS_AMOUNT" />
      </td>
      <xsl:if test="$outputType='liability'">
        <td class="received">
          <xsl:apply-templates select="ITEM/RECEIVED_AMOUNT" />
        </td>
      </xsl:if>
      <td class="difference">
        <xsl:apply-templates select="ITEM/DIFF_AMOUNT" />
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="ROW" mode="action">
    <a>
      <xsl:attribute name="href">
        <xsl:variable name="url-no-hash">
         <xsl:value-of select="$PaymentStatement.RowLink.PageID" />
         <xsl:text>Page.do?</xsl:text>
         <xsl:value-of select="$PaymentStatement.RowLink.ParameterName" />
         <xsl:text>=</xsl:text>
         <xsl:value-of select="REASSESS_INFO_ID" />
         <xsl:if test="$returnPageParam">
           <xsl:text>&amp;</xsl:text>
           <xsl:value-of select="$returnPageParam" />
         </xsl:if>
        </xsl:variable>
        <xsl:variable name="url-with-hash">
          <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
        </xsl:variable>
        
        <xsl:value-of select="$url-with-hash"/>
      </xsl:attribute>
      <xsl:attribute name="title">
        <xsl:value-of select="$PaymentStatement.RowLink.Label" />
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="$PaymentStatement.RowLink.Image">
          <img src="{concat($static-content-server-url, '/',
                            $PaymentStatement.RowLink.Image)}"
               alt="{$PaymentStatement.RowLink.Label}" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$PaymentStatement.RowLink.Label" />
        </xsl:otherwise>
      </xsl:choose>
    </a>
  </xsl:template>

  <xsl:template match="ROW" mode="from-to-date">  
    <xsl:variable name="TextWithFromDate">
      <xsl:call-template name="string-replace-all">
        <xsl:with-param name="text" select="$PaymentStatement.Text.fromToDateSeparator" />
        <xsl:with-param name="replace" select="'%fromDate'" />
        <xsl:with-param name="by" select="FROM_DATE" />
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="FromToDateText">
      <xsl:call-template name="string-replace-all">
        <xsl:with-param name="text" select="$TextWithFromDate" />
        <xsl:with-param name="replace" select="'%toDate'" />
        <xsl:with-param name="by" select="TO_DATE" />
      </xsl:call-template>
    </xsl:variable>
    
    <xsl:value-of select="$FromToDateText" />
    
  </xsl:template>
  
  <xsl:template match="ITEM" mode="description">
    <xsl:for-each select="TYPE">
      <xsl:value-of select="." />
      <br />
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="ACTUAL_AMOUNT | REASSESS_AMOUNT | RECEIVED_AMOUNT | DIFF_AMOUNT">
    <xsl:variable name="amount" select="double:valueOf(.)" />
    <xsl:value-of select="format:format($numberFormat, $amount)" />
    <br />
  </xsl:template>
  
  
  <xsl:template name="print-total">
    <xsl:param name="total-name" />
    <xsl:param name="total-amount" />
    <tr>
          <xsl:if test="$total-amount = TOTAL_NET">
          <xsl:attribute name="class">
              <xsl:text>last-row</xsl:text>
          </xsl:attribute>
      </xsl:if> 
      <td class="first-field" colspan="4">
        <xsl:value-of select="$total-name" />
      </td>
      <td class="last-field" colspan="4">
        <xsl:variable name="amount" select="double:valueOf($total-amount)" />
        <xsl:value-of select="format:format($numberFormat, $amount)" />
      </td>
    </tr>
  </xsl:template>
  
  <!-- The template to replace each substring of this string that matches the 
       given string with the given replacement. -->
  <xsl:template name="string-replace-all">
    <xsl:param name="text" />
    <xsl:param name="replace" />
    <xsl:param name="by" />
    <xsl:choose>
      <xsl:when test="contains($text, $replace)">
        <xsl:value-of select="substring-before($text,$replace)" />
        <xsl:value-of select="$by" />
        <xsl:call-template name="string-replace-all">
          <xsl:with-param name="text"
                          select="substring-after($text,$replace)" />
          <xsl:with-param name="replace" select="$replace" />
          <xsl:with-param name="by" select="$by" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>