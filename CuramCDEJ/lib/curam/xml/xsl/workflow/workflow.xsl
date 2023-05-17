<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="bidi-utils">
  <xsl:import href="../common/runtime-params.xsl"/>
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />

  <xsl:param name="data"/>
  <xsl:param name="workflow-id"/>
  <xsl:param name="link-location"/>
  <xsl:param name="notification-page"/>
  <xsl:param name="readonly-view"/>
  <xsl:param name="loading-text"/>
  <xsl:param name="order-text"/>
  <xsl:param name="start-type"/>
  <xsl:param name="end-type"/>
  <xsl:param name="config-height"/>
  <xsl:param name="instance-data-required"/>
  <xsl:param name="instance-data"/>
  <xsl:param name="instance-head"/>
  <xsl:param name="instance-data-label-1"/>
  <xsl:param name="instance-data-label-2"/>
  <xsl:param name="instance-data-label-3"/>
  <xsl:param name="instance-data-label-4"/>
  <xsl:param name="instance-data-label-5"/>
  <xsl:param name="instance-data-label-6"/>
  <xsl:param name="instance-data-label-7"/>
  <xsl:param name="instance-data-label-8"/>
  <xsl:param name="instance-data-label-9"/>

  <xsl:variable name="loadingSpan" select="concat('_o3ld_', $workflow-id)"/>
  <xsl:variable name="height">
    <xsl:choose>
      <!-- If height is explicitly set in the config file, use it. -->
      <xsl:when test="$config-height">
        <xsl:value-of select="$config-height"/>
      </xsl:when>
      <!-- Otherwise, estimate a height that best uses available space -->
      <xsl:when test="$instance-data-required">
        <xsl:text>self.screen.availHeight / 2.5</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>self.screen.availHeight / 2</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:template match="WORKFLOW">
    <div id="__o3__WF_div" style="overflow:visible;width:100%">
      <script type="text/javascript">
        <xsl:text disable-output-escaping="yes">//&lt;![CDATA[</xsl:text>
        checkAndGetSVGViewer();
        var height = <xsl:value-of select="$height"/>;
        var orderText = '<xsl:value-of select="$order-text"/>';
        var linkLocation = '<xsl:value-of select="$link-location" />';
        var notificationPage = '<xsl:value-of select="$notification-page" />';
        var readonlyView = '<xsl:value-of select="$readonly-view" />';
        var startType = '<xsl:value-of select="$start-type" />';
        var endType = '<xsl:value-of select="$end-type" />';
        var id1 = new Array();
        var id2 = new Array();
        var id3 = new Array();
        var id4 = new Array();
        var id5 = new Array();
        var id6 = new Array();
        var id7 = new Array();
        var id8 = new Array();
        var id9 = new Array();
        <xsl:if test="$instance-data-required">
          <xsl:for-each select="$instance-data//data">
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="1"/>
              <xsl:with-param name="string-in" select="@item1"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="2"/>
              <xsl:with-param name="string-in" select="@item2"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="3"/>
              <xsl:with-param name="string-in" select="@item3"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="4"/>
              <xsl:with-param name="string-in" select="@item4"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="5"/>
              <xsl:with-param name="string-in" select="@item5"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="6"/>
              <xsl:with-param name="string-in" select="@item6"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="7"/>
              <xsl:with-param name="string-in" select="@item7"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="8"/>
              <xsl:with-param name="string-in" select="@item8"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>
            <xsl:call-template name="output-instance-data">
              <xsl:with-param name="index" select="9"/>
              <xsl:with-param name="string-in" select="@item9"/>
              <xsl:with-param name="id" select="@id"/>
            </xsl:call-template>            
          </xsl:for-each>
        </xsl:if>
        function getWorkflowData() {
          return '<xsl:value-of disable-output-escaping="yes" select="$data"/>';
        }
       <xsl:value-of select="concat('emitSVG(', $squot,
                   'src=', $dquot, $static-content-server-url,
                   '/CDEJ/svg/workflow.svg', $dquot,
                   ' name=', $dquot, '__o3__SVGWorkflow', $dquot,
                   ' id=', $dquot, '__o3__SVGWorkflow', $dquot,
                   ' height=', $dquot, $squot, ' + height + ', $squot, $dquot,
                   ' width=', $dquot, '100%', $dquot,
                   ' wmode=', $dquot, 'opaque', $dquot,
                   ' type=', $dquot, 'image/svg+xml', $dquot, $squot, ');')" />
<xsl:text disable-output-escaping="yes">//]]&gt;</xsl:text>
    </script>
  </div>
  <xsl:if test="$instance-data-required">
    <div class="cluster label-field">
      <div class="header">
        <h2>
         <xsl:attribute name="dir">
         <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-head)"/>
         </xsl:attribute>        
        <xsl:value-of select="$instance-head"/>
        </h2>
      </div>
      <table>
        <col width="25%"/><col width="25%"/><col width="25%"/><col width="25%"/>
        <tbody>
          <tr>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-1)"/>
              </xsl:attribute>                    
              <xsl:value-of select="$instance-data-label-1"/>
            </td>
            <td><span class="label" id="o3wf_id_1"></span></td>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-2)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-2"/>
            </td>
            <td><span class="label" id="o3wf_id_2"></span></td>
          </tr>
          <tr>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-3)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-3"/>
            </td>
            <td><span class="label" id="o3wf_id_3"></span></td>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-4)"/>
              </xsl:attribute>                                            
              <xsl:value-of select="$instance-data-label-4"/>
            </td>
            <td><span class="label" id="o3wf_id_4"></span></td>
          </tr>
          <tr>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-5)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-5"/>
            </td>
            <td><span class="label" id="o3wf_id_5"></span></td>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-6)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-6"/>
            </td>
            <td><span class="label" id="o3wf_id_6"></span></td>
          </tr>
          <tr>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-7)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-7"/>
            </td>
            <td><span class="label" id="o3wf_id_7"></span></td>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-8)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-8"/>
            </td>
            <td><span class="label" id="o3wf_id_8"></span></td>
          </tr>
          <tr>
            <td class="label">
              <xsl:attribute name="dir">
              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(instance-data-label-9)"/>
              </xsl:attribute>                                
              <xsl:value-of select="$instance-data-label-9"/>
            </td>
            <td><span class="label" id="o3wf_id_9"></span></td>
          </tr>
        </tbody>
      </table>
    </div>
  </xsl:if>
  </xsl:template>
  
  <xsl:template name="output-instance-data">
    <xsl:param name="string-in" />
    <xsl:param name="index"/>
    <xsl:param name="id"/>
    <xsl:text>id</xsl:text>
    <xsl:value-of select="$index"/>
    <xsl:text>['</xsl:text>
    <xsl:value-of select="$id"/>
    <xsl:text>'] = "</xsl:text>
    <xsl:call-template name="replace-chars-in-string">
      <xsl:with-param name="string-in" select="$string-in" />
      <xsl:with-param name="chars-in" select="'&quot;'"/>
      <xsl:with-param name="chars-out" select="'\&quot;'"/>
    </xsl:call-template>
    <xsl:text>";&#xa;</xsl:text>
  </xsl:template>
  
  <xsl:template name="replace-chars-in-string">
    <xsl:param name="string-in" />
    <xsl:param name="chars-in"/>
    <xsl:param name="chars-out"/>

    <xsl:choose>
      <xsl:when test="contains($string-in, $chars-in)">
        <xsl:value-of disable-output-escaping="yes"
                      select="concat(substring-before($string-in, $chars-in), $chars-out)"/>
        <xsl:call-template name="replace-chars-in-string">
          <xsl:with-param name="string-in"
                          select="substring-after($string-in, $chars-in)"/>
          <xsl:with-param name="chars-in" select="$chars-in"/>
          <xsl:with-param name="chars-out" select="$chars-out"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of disable-output-escaping="yes" select="$string-in"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>