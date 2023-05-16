<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2020. All rights reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:domain="http://xml.apache.org/xalan/java/curam.util.common.util.JavaScriptEscaper"
    xmlns:xalan="http://xml.apache.org/xalan"
    xmlns:ibm="http://ibm.com"
    extension-element-prefixes="xalan"
    exclude-result-prefixes="domain ibm">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/> 
  
  
  <ibm:colors> 
	<ibm:color>6929c4</ibm:color> 
	<ibm:color>1192E8</ibm:color> 
	<ibm:color>005D5D</ibm:color> 
	<ibm:color>9F1853</ibm:color> 
	<ibm:color>FA4D56</ibm:color>
	<ibm:color>520408</ibm:color>
	<ibm:color>198038</ibm:color>
  </ibm:colors>

  <xsl:strip-space elements="*" />
  


<xsl:param name="static-content-server-url"/>
<xsl:param name="locale"/>
<xsl:param name="context"/>
<xsl:param name="chart-num"/>
<xsl:param name="chart-title"/>
<xsl:param name="page-id"/>
<xsl:param name="caption-page-id"/>
<xsl:param name="page-params"/>
<xsl:param name="data-params"/>
<xsl:param name="caption-page-params"/>
<xsl:param name="caption-data-params"/>
<xsl:param name="chart-legend"/>
<xsl:param name="ax-label"/>
<xsl:param name="ay-label"/>
<xsl:param name="axis-max"/>
<xsl:param name="increment"/>
<xsl:param name="min-height"/>
<xsl:param name="max-height"/>
<xsl:param name="show-legend"/>
<xsl:param name="legend-items"/>
<xsl:param name="total-text"/>
<xsl:param name="orientation" select="'vertical'"/>
  
<xsl:variable name="ident" select="concat('fchart', domain:escapeText($chart-num))"/>
  
<xsl:template match="CHART|BAR_CHART">
  
  <xsl:variable name="num-items" select="count(UNIT)"/>
  <xsl:variable name="actual-min">
    <xsl:choose>
      <xsl:when test="$min-height='0'">240</xsl:when>
      <xsl:otherwise><xsl:value-of select="$min-height"/></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="actual-max">
    <xsl:choose>
      <xsl:when test="$max-height='-1'">10000</xsl:when>
      <xsl:when test="$max-height='0'">240</xsl:when>
      <xsl:otherwise><xsl:value-of select="$max-height"/></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="height-request" select="($num-items * 40) + 57"/>
  <xsl:variable name="chart-height">
    <xsl:choose>
      <xsl:when test="$height-request &lt; $actual-min">
        <xsl:value-of select="$actual-min"/>
      </xsl:when>
      <xsl:otherwise>
       <xsl:choose>
         <xsl:when test="$height-request &gt; $actual-max">
           <xsl:value-of select="$actual-max"/>
         </xsl:when>
         <xsl:otherwise><xsl:value-of select="$height-request"/></xsl:otherwise>
       </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="link-base">
    <xsl:value-of select="concat($static-content-server-url, '/', $locale, '/', $page-id, 'Page.do')"/>
  </xsl:variable>
  <xsl:variable name="caption-link-base">
    <xsl:choose>
      <xsl:when test="$caption-page-id = ''"></xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="concat($static-content-server-url, '/', $locale, '/', $caption-page-id, 'Page.do')"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:variable name="apos">'</xsl:variable>

  <script type="text/javascript">
    <xsl:text>var linkData</xsl:text><xsl:value-of select="$ident"/><xsl:text>= new Array();</xsl:text>
    <xsl:apply-templates select="UNIT" mode="links">
      	<xsl:with-param name="chart-id" select="$ident"/>
    </xsl:apply-templates>

    <xsl:text>var captionsData</xsl:text><xsl:value-of select="$ident"/><xsl:text>=[</xsl:text>
      	<xsl:apply-templates select="UNIT" mode="caption-links"/>
    <xsl:text>];</xsl:text> 
    <xsl:text>var chartData</xsl:text><xsl:value-of select="$ident"/><xsl:text> = {</xsl:text>
		<xsl:text>labels: [</xsl:text><xsl:apply-templates select="UNIT" mode="labels"/><xsl:text>]</xsl:text><xsl:choose><xsl:when test="$orientation='horizontal'"><xsl:text>.reverse()</xsl:text></xsl:when></xsl:choose><xsl:text>,</xsl:text> 
		<xsl:text>general: { title:'</xsl:text><xsl:value-of select="$chart-title"/><xsl:text>'</xsl:text> 
		<xsl:text>},</xsl:text> 
		<xsl:text>datasets: [</xsl:text> 
		<xsl:call-template name="repeatable" />
		<xsl:text> ],</xsl:text> 
	<xsl:text>};</xsl:text>
  <xsl:text>function processTabbedURL(url) {if (curam.tab) { curam.tab.getTabController().processURL(url);} else {console.error('No tabbed context');}}</xsl:text>
	
  </script>

  <div id="chart-outer-div{$ident}" height="{$chart-height}" class="spm-component spm-chart">
    <div id="buttons-container{$ident}" className="spm-chart__button-container"><xsl:comment /></div>
    <div className="spm-chart__legend-container">
      <div id="bar-chart{$ident}"><!-- a marker div --></div>
    </div>
  </div>

  <script>
	   <xsl:variable name="varsToPass">
		    <xsl:value-of select="concat('ident: ', $apos, $ident, $apos,
		                                 ', title: ',$apos,$chart-title,$apos,
		                                 ', linkBase: ', $apos,$link-base,$apos,
		                                 ', captionLinkBase: ', $apos,$caption-link-base,$apos,
		                                 ', xLabel: ', $apos,$ay-label,$apos,
		                                 ', yLabel: ', $apos,$ax-label,$apos,
		                                 ', axisMax: ', $apos,$axis-max,$apos,
		                                 ', increment: ', $apos,$increment,$apos,
		                                 ', o3ctx: ', $apos,$context,$apos,
		                                 ', legendOn: ', $show-legend,
		                                 ', orientation: ', $apos,$orientation,$apos,                                 
		                                 ', captionDataParams: ', $caption-data-params,                         
		                                 ', dataParams: ', $data-params,                                                                  
		                                 ', chartHeight: ', $apos,$chart-height,$apos,
		                                 )"/>
	  	</xsl:variable>
	    <xsl:text>var containersID = { chartID:  'bar-chart</xsl:text><xsl:value-of select="$ident"/><xsl:text>'}; </xsl:text>
	    <xsl:text>spm.renderers.Charts.ChartsRenderer(containersID, {</xsl:text><xsl:value-of select="$varsToPass"/><xsl:text>},chartData</xsl:text><xsl:value-of select="$ident"/><xsl:text>, captionsData</xsl:text><xsl:value-of select="$ident"/><xsl:text></xsl:text><xsl:choose><xsl:when test="$orientation='horizontal'"><xsl:text>.reverse()</xsl:text></xsl:when></xsl:choose><xsl:text>, linkData</xsl:text><xsl:value-of select="$ident"/><xsl:text></xsl:text><xsl:choose><xsl:when test="$orientation='horizontal'"><xsl:text>.reverse()</xsl:text></xsl:when></xsl:choose> <xsl:text>, 'buttons-container</xsl:text><xsl:value-of select="$ident"/><xsl:text>'</xsl:text><xsl:text> )</xsl:text>
	</script>
  
</xsl:template>

<xsl:template match="UNIT" mode="labels">
  <xsl:variable name="this-unit" select="."/>
  <xsl:text>'</xsl:text><xsl:value-of select="CAPTION/@parsed-caption"/>
  <xsl:text>'</xsl:text>
  <xsl:if test="not(position() = last())">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template name="repeatable" >
    <xsl:param name="index" select="1" />
    <xsl:param name="code" select="$legend-items/ITEM[1]" />
    <xsl:param name="total" select="count($legend-items/ITEM)"/>
    
    <xsl:variable name="this-unit" select="."/>
    <xsl:text>  {</xsl:text> 
	<xsl:text>    data: [</xsl:text> 
	  <xsl:apply-templates select="UNIT" mode="data3">
         <xsl:with-param name="currentLegend" select="$code"/>
      </xsl:apply-templates>
	<xsl:text>]</xsl:text><xsl:choose><xsl:when test="$orientation='horizontal'"><xsl:text>.reverse()</xsl:text></xsl:when></xsl:choose><xsl:text>,</xsl:text> 
	<xsl:text>    label: [</xsl:text><xsl:value-of select="$chart-legend"/><xsl:text>][</xsl:text><xsl:value-of select="$index"/><xsl:text>-1],</xsl:text> 	
	<xsl:text>    backgroundColor: '#</xsl:text><xsl:value-of select="document('')/xsl:stylesheet/ibm:colors/ibm:color[$index]"/><xsl:text>',</xsl:text> 
	<xsl:text>    hoverBackgroundColor: '#</xsl:text><xsl:value-of select="document('')/xsl:stylesheet/ibm:colors/ibm:color[$index]"/><xsl:text>',</xsl:text> 
	<xsl:text>   },</xsl:text> 
    <xsl:if test="not($index = $total)">
        <xsl:call-template name="repeatable">                      
            <xsl:with-param name="index" select="$index + 1" />
            <xsl:with-param name="code" select="$legend-items/ITEM[$index + 1]" />
        </xsl:call-template>
    </xsl:if>
</xsl:template>

<xsl:template match="UNIT" mode="data">
  <xsl:variable name="this-unit" select="."/>
  <xsl:text>{xAxis:&quot;</xsl:text><xsl:value-of select="CAPTION/@parsed-caption"/><xsl:text>&quot;,</xsl:text>
  <xsl:for-each select="$legend-items/ITEM">
    <xsl:text>col</xsl:text><xsl:value-of select="position() - 1"/><xsl:text>:</xsl:text>
      <xsl:choose>
        <xsl:when test="$this-unit/BLOCK[@TYPE=current()/@CODE]">
          <xsl:value-of select="$this-unit/BLOCK[@TYPE=current()/@CODE]/@LENGTH"/>
        </xsl:when>
        <xsl:otherwise><xsl:text>0</xsl:text></xsl:otherwise>
      </xsl:choose>
    <xsl:if test="not(position() = last())">
      <xsl:text>, </xsl:text>
    </xsl:if>
  </xsl:for-each>
  <xsl:text>}</xsl:text>
  <xsl:if test="not(position() = last())">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="UNIT" mode="data3">
<xsl:param name="currentLegend"/>
  <xsl:variable name="this-unit" select="."/>
      <xsl:choose>
        <xsl:when test="$this-unit/BLOCK[@TYPE=$currentLegend/@CODE]">
          <xsl:value-of select="$this-unit/BLOCK[@TYPE=$currentLegend/@CODE]/@LENGTH"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text> 0 </xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    <xsl:if test="not(position() = last())">
      <xsl:text>, </xsl:text>
    </xsl:if>
</xsl:template>


<xsl:template match="UNIT" mode="links">
  <xsl:param name="chart-id"/>
  <xsl:variable name="this-unit" select="."/>
  <xsl:variable name="group-idx" select="position() - 1"/>
  <xsl:text>linkData</xsl:text><xsl:value-of select="$chart-id"/>
  <xsl:text>[</xsl:text><xsl:value-of select="$group-idx"/><xsl:text>] = new Array();</xsl:text>
  <xsl:for-each select="$legend-items/ITEM">
    <xsl:text>linkData</xsl:text><xsl:value-of select="$chart-id"/>
    <xsl:text>[</xsl:text><xsl:value-of select="$group-idx"/><xsl:text>]</xsl:text>
    <xsl:text>[</xsl:text><xsl:value-of select="position() - 1"/><xsl:text>]={</xsl:text>
    <xsl:choose>
      <xsl:when test="$this-unit/BLOCK[@TYPE=current()/@CODE]">
        <xsl:apply-templates select="$this-unit/BLOCK[@TYPE=current()/@CODE]" mode="output-params"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="count($this-unit/BLOCK) &gt;=1">
          <xsl:apply-templates select="$this-unit/BLOCK[1]" mode="output-empty"/>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
</xsl:template>

<xsl:template match="BLOCK" mode="output-params">
  <xsl:for-each select="@*">
    <xsl:value-of select="name()"/><xsl:text>:&quot;</xsl:text><xsl:value-of select="."/><xsl:text>&quot;</xsl:text>
    <xsl:if test="not(position() = last())">
      <xsl:text>, </xsl:text>
    </xsl:if>
  </xsl:for-each>
  <xsl:if test="preceding-sibling::CAPTION/@TEXT">
    <xsl:text>, TEXT:&quot;</xsl:text><xsl:value-of select="preceding-sibling::CAPTION/@TEXT"/><xsl:text>&quot;</xsl:text>
  </xsl:if>
  <xsl:if test="preceding-sibling::CAPTION/@START_DATE">
    <xsl:text>, START_DATE:&quot;</xsl:text><xsl:value-of select="preceding-sibling::CAPTION/@START_DATE"/><xsl:text>&quot;</xsl:text>
  </xsl:if>
  <xsl:if test="preceding-sibling::CAPTION/@END_DATE">
    <xsl:text>, END_DATE:&quot;</xsl:text><xsl:value-of select="preceding-sibling::CAPTION/@END_DATE"/><xsl:text>&quot;</xsl:text>
  </xsl:if>
  <xsl:text>};</xsl:text>
</xsl:template>

<xsl:template match="BLOCK" mode="output-empty">
  <xsl:for-each select="@*">
    <xsl:value-of select="name()"/><xsl:text>:&quot;&quot;</xsl:text>
    <xsl:if test="not(position() = last())">
      <xsl:text>, </xsl:text>
    </xsl:if>
  </xsl:for-each>  
  <xsl:if test="preceding-sibling::CAPTION/@TEXT">
    <xsl:text>, TEXT:&quot;</xsl:text><xsl:value-of select="preceding-sibling::CAPTION/@TEXT"/><xsl:text>&quot;</xsl:text>
  </xsl:if>
  <xsl:if test="preceding-sibling::CAPTION/@START_DATE">
    <xsl:text>, START_DATE:&quot;</xsl:text><xsl:value-of select="preceding-sibling::CAPTION/@START_DATE"/><xsl:text>&quot;</xsl:text>
  </xsl:if>
  <xsl:if test="preceding-sibling::CAPTION/@END_DATE">
    <xsl:text>, END_DATE:&quot;</xsl:text><xsl:value-of select="preceding-sibling::CAPTION/@END_DATE"/><xsl:text>&quot;</xsl:text>
  </xsl:if>
  <xsl:text>};</xsl:text>
</xsl:template>
  
<xsl:template match="UNIT" mode="caption-links">
  <xsl:for-each select="CAPTION">
    <xsl:text>{</xsl:text>
    <xsl:for-each select="@*">
      <xsl:choose>
        <xsl:when test="name()='parsed-caption'">
          <xsl:text>parsedCaption:&quot;</xsl:text><xsl:value-of select="."/><xsl:text>&quot;</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="name()"/><xsl:text>:&quot;</xsl:text><xsl:value-of select="."/><xsl:text>&quot;</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="not(position() = last())">
        <xsl:text>, </xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:text>}</xsl:text>
    <xsl:if test="count(following::CAPTION) > 0">
      <xsl:text>, </xsl:text>
    </xsl:if>
  </xsl:for-each>
</xsl:template>

</xsl:stylesheet>