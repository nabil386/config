<?xml version="1.0"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:domain="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.EvidenceTag"
        xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils"
        xmlns:xalan="http://xml.apache.org/xalan"
        extension-element-prefixes="xalan"
        exclude-result-prefixes="domain request-utils">

    <xsl:import href="../common/ui.xsl" />
    <xsl:import href="../common/string-utils.xsl" />
    <xsl:import href="ui-evidence-styles.xsl" />

        <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />
    <xsl:strip-space elements="*"/>

    <xsl:param name="Title"/>
    <xsl:param name="Mode"/>
    <xsl:param name="locale"/>
    <xsl:param name="Description.Column.Header"/>
    <xsl:param name="Group.Column.Header"/>
    <xsl:param name="Value.Column.Header"/>

    <xsl:param name="Value.Column.Visible"/>
    <xsl:param name="Group.Column.Visible"/>
    <xsl:param name="Description.Column.Visible"/>
    <xsl:param name="Description.Column.Link"/>
    <xsl:param name="Group.Column.Link"/>
    
    <xsl:param name="Message.No.Value"/>
    <xsl:param name="Message.Item.Joint"/>
    <!-- The username variable is supplied by the Transformer object in the jsp tag. -->
    <xsl:param name="username" />


    <xsl:variable name="structure">
        <COLUMN name="Description"/>
        <COLUMN name="Group"/>
        <COLUMN name="Value"/>
    </xsl:variable>

<!-- List title -->
  <xsl:template match="evidence">
    <xsl:choose>
      <xsl:when test="$Mode='text'">
        <xsl:apply-templates select="." mode="text"/>
      </xsl:when>
      <xsl:when test="$Mode='xml'">
        <xsl:apply-templates select="." mode="xml"/>
      </xsl:when>
    </xsl:choose>
    </xsl:template>

  <xsl:template match="evidence" mode="text">
    <xsl:call-template name="gen-heading">
      <xsl:with-param name="title" select="$Title"/>
      <xsl:with-param name="context" select="'list'"/>
    </xsl:call-template>
    <xsl:for-each select="group">
      <xsl:call-template name="gen-list">
        <xsl:with-param name="title" select="./@display-name"/>
      </xsl:call-template>    
    </xsl:for-each>
  </xsl:template>


  <xsl:template match="evidence" mode="xml">
      <xsl:call-template name="gen-list">
        <xsl:with-param name="title" select="$Title"/>
      </xsl:call-template>
  </xsl:template>
  
<!-- Column definitions -->

  <xsl:template name="gen-list-column-defs">
    <xsl:variable name="denominator">
      <xsl:call-template name="num-visible">
        <xsl:with-param name="current" select="count(xalan:nodeset($structure)/COLUMN)"/>
        <xsl:with-param name="shown" select="count(xalan:nodeset($structure)/COLUMN)"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:call-template name="gen-list-column-def">
      <xsl:with-param name="all-cols" select="$denominator"/>
      <xsl:with-param name="curr-col" select="1"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="gen-list-column-def">
        <xsl:param name="all-cols"/>
        <xsl:param name="curr-col"/>
        <xsl:param name="percents" select="round(100 div $all-cols)"/>
        <col width="{$percents}%" xsl:use-attribute-sets="list-column-attributes"/>
        <xsl:if test="$curr-col &lt; $all-cols">
         <xsl:call-template name="gen-list-column-def">
                <xsl:with-param name="curr-col" select="$curr-col + 1"/>
                <xsl:with-param name="all-cols" select="$all-cols"/>
         </xsl:call-template>
        </xsl:if>
  </xsl:template>
  
<!-- List header fields -->

  <xsl:template name="gen-list-header-fields">
    <xsl:call-template name="gen-list-header-field">
        <xsl:with-param name="which-column" select="1"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="gen-list-header-field">
    <xsl:param name="which-column"/>
    <xsl:if test="$which-column &lt;= count(xalan:nodeset($structure)/COLUMN)">
      <xsl:variable name="prefix" select="xalan:nodeset($structure)/COLUMN[position() = $which-column]/@name"/>
      <xsl:if test="$prefix = 'Description'">
        <xsl:if test="$Description.Column.Visible = 'true'">
          <th>
            <xsl:if test="name() = 'group' and position() = 1">
              <xsl:attribute name="class">field first-header</xsl:attribute>
            </xsl:if>
            <xsl:if test="name() = 'group' and position() = last()">
              <xsl:attribute name="class">field first-header</xsl:attribute>
            </xsl:if>
            <xsl:if test="name() = 'evidence' and position() = 1">
              <xsl:attribute name="class">field first-header</xsl:attribute>
            </xsl:if>
          	<span>
            	<xsl:value-of select="$Description.Column.Header"/>
            </span>
          </th>
        </xsl:if>
      </xsl:if>
      <xsl:if test="$prefix = 'Group'">
        <xsl:if test="(($Group.Column.Visible = 'true') and ($Mode='xml'))">
          <th>
          	<span>
            	<xsl:value-of select="$Group.Column.Header"/>
            </span>
          </th>
        </xsl:if>
      </xsl:if>
      <xsl:if test="$prefix = 'Value'">
        <xsl:if test="$Value.Column.Visible = 'true'">
          <th>
            <xsl:if test="name() = 'group' and position() = 1">
              <xsl:attribute name="class">field last-header</xsl:attribute>
            </xsl:if>
            <xsl:if test="name() = 'group' and position() = last()">
              <xsl:attribute name="class">field last-header</xsl:attribute>
            </xsl:if>
            <xsl:if test="name() = 'evidence' and position() = last()">
              <xsl:attribute name="class">field last-header</xsl:attribute>
            </xsl:if>
          	<span>
            	<xsl:value-of select="$Value.Column.Header"/>
            </span>
          </th>
        </xsl:if>
      </xsl:if>
      <xsl:call-template name="gen-list-header-field">
        <xsl:with-param name="which-column" select="$which-column + 1"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
<!-- List body-->

  <xsl:template name="gen-list-body-rows">
    <xsl:for-each select="item[(not(@visible) or @visible='true')]">
      <xsl:call-template name="gen-list-body-row"/>
     </xsl:for-each>        
     <xsl:for-each select="group/item[(not(@visible) or @visible='true')]">
       <xsl:call-template name="gen-list-body-row"/>
     </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="gen-list-body-row">
    <xsl:param name="index" select="-1" />
    <xsl:choose>
      <xsl:when test="@no-value='true'">
        <tr>
          <xsl:call-template name="gen-list-body-fields">
            <xsl:with-param name="index" select="$index" />
            <xsl:with-param name="item-text" select="@display-name"/>
            <xsl:with-param name="group-text" select="../@display-name"/>
            <xsl:with-param name="val" select="$Message.No.Value"/>
          </xsl:call-template>
        </tr>
      </xsl:when>
      <xsl:otherwise>
        <tr>
          <xsl:call-template name="gen-list-body-fields">
            <xsl:with-param name="index" select="$index" />
            <xsl:with-param name="item-text" select="@display-name"/>
            <xsl:with-param name="group-text" select="../@display-name"/>
            <xsl:with-param name="val" select="domain:getFormatedValue(@type, @initial-value, $locale)"/>
          </xsl:call-template>
        </tr>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:for-each select="value">
      <tr>
        <xsl:call-template name="gen-list-body-fields">
          <xsl:with-param name="index" select="$index" />
          <xsl:with-param name="item-text">
            <xsl:value-of select="../@display-name"/><xsl:value-of select="concat(' ', $Message.Item.Joint, ' ')"/><xsl:value-of select="@description"/>
          </xsl:with-param>
          <xsl:with-param name="group-text" select="../../@display-name"/>
          <xsl:with-param name="val" select="domain:getFormatedValue(../@type, @value, $locale)"/>
        </xsl:call-template>
      </tr>
    </xsl:for-each>

  </xsl:template>
  
  <xsl:template name="gen-list-body-fields">
    <xsl:param name="item-text"/>
    <xsl:param name="group-text"/>
    <xsl:param name="val"/>
    <xsl:call-template name="gen-list-body-field">
      <xsl:with-param name="which-column" select="1"/>
      <xsl:with-param name="item-text" select="$item-text"/>
      <xsl:with-param name="group-text" select="$group-text"/>
      <xsl:with-param name="val" select="$val"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="gen-list-body-field">
    <xsl:param name="which-column"/>
    <xsl:param name="item-text"/>
    <xsl:param name="group-text"/>
    <xsl:param name="val"/>
    <xsl:if test="$which-column &lt;= count(xalan:nodeset($structure)/COLUMN)">
      <xsl:variable name="column-count" select="count(xalan:nodeset($structure)/COLUMN)"/>
      <xsl:variable name="prefix" select="xalan:nodeset($structure)/COLUMN[position() = $which-column]/@name"/>
      <xsl:choose>
        <xsl:when test="$prefix = 'Description'">
          <xsl:if test="($Description.Column.Visible = 'true')">
            <td>
              <xsl:attribute name="class">
                <xsl:value-of select="$list-body-field-class"/>
                <xsl:text> widget-border</xsl:text>
                <xsl:if test="$which-column = 1">
                  <xsl:text> first</xsl:text>
                </xsl:if>
              </xsl:attribute>
              <xsl:call-template name="gen-list-body-field-content">
                <xsl:with-param name="col-num" select="$which-column"/>
                <xsl:with-param name="item-text" select="$item-text"/>
                <xsl:with-param name="group-text" select="$group-text"/>
                <xsl:with-param name="val" select="$val"/>
              </xsl:call-template>
            </td>
          </xsl:if>
        </xsl:when>
        <xsl:when test="$prefix = 'Group'">
          <xsl:if test="(($Group.Column.Visible = 'true') and ($Mode='xml'))">
            <td class="{$list-body-field-class} widget-border">
              <xsl:call-template name="gen-list-body-field-content">
                <xsl:with-param name="col-num" select="$which-column"/>
                <xsl:with-param name="item-text" select="$item-text"/>
                <xsl:with-param name="group-text" select="$group-text"/>
                <xsl:with-param name="val" select="$val"/>
              </xsl:call-template>
            </td>
          </xsl:if>
        </xsl:when>
        <xsl:when test="$prefix = 'Value'">
          <xsl:if test="$Value.Column.Visible = 'true'">
            <td class="{$list-body-field-class} widget-border">
              <xsl:attribute name="class">
                <xsl:value-of select="$list-body-field-class"/>
                <xsl:text> widget-border</xsl:text>
                <xsl:if test="$which-column = $column-count">
                  <xsl:text> last</xsl:text>
                </xsl:if>
              </xsl:attribute>
              <xsl:call-template name="gen-list-body-field-content">
                <xsl:with-param name="col-num" select="$which-column"/>
                <xsl:with-param name="item-text" select="$item-text"/>
                <xsl:with-param name="group-text" select="$group-text"/>
                <xsl:with-param name="val" select="$val"/>
              </xsl:call-template>
            </td>
          </xsl:if>
        </xsl:when>
      </xsl:choose>
      <xsl:call-template name="gen-list-body-field">
        <xsl:with-param name="which-column" select="$which-column + 1"/>
        <xsl:with-param name="item-text" select="$item-text"/>
        <xsl:with-param name="group-text" select="$group-text"/>
        <xsl:with-param name="val" select="$val"/>        
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <xsl:template name="gen-list-body-field-content">
    <xsl:param name="col-num"/>
    <xsl:param name="item-text"/>
    <xsl:param name="group-text"/>
    <xsl:param name="val"/>
    <xsl:variable name="prefix" select="xalan:nodeset($structure)/COLUMN[position() = $col-num]/@name"/>
    <xsl:variable name="a-link-param">
      <xsl:choose>
        <xsl:when test="$prefix = 'Description'">
          <xsl:value-of select="$Description.Column.Link"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:if test="$prefix = 'Group'">
            <xsl:value-of select="$Group.Column.Link"/>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$col-num = 1">
        <xsl:call-template name="probably-linked-value">
          <xsl:with-param name="textual-contents" select="$item-text"/>
          <xsl:with-param name="a-link"><xsl:value-of select="$a-link-param"/></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$col-num = 2">
        <xsl:call-template name="probably-linked-value">
          <xsl:with-param name="textual-contents" select="$group-text"/>
          <xsl:with-param name="a-link"><xsl:value-of select="$a-link-param"/></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$col-num &gt;= 2">
        <xsl:value-of select="$val"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

<!-- auxillary-->
    <xsl:template name="probably-linked-value">
      <xsl:param name="textual-contents"/>
      <xsl:param name="a-link"/>
      <xsl:choose>
        <xsl:when test="$a-link != ''">
          <xsl:choose>
            <xsl:when test="@initial-value">
              <xsl:variable name="url-no-hash">
		        <xsl:value-of select="concat($a-link, 'Page.do?', 'itemID=',@name, '&amp;groupID=', ../@id)"/>
		      </xsl:variable>
		      <xsl:variable name="url-with-hash">
		        <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
              </xsl:variable>
              <a href="{$url-with-hash}"><xsl:value-of select="$textual-contents"/></a>
            </xsl:when>
            <xsl:otherwise>
              <xsl:variable name="url-no-hash">
		        <xsl:value-of select="concat($a-link, 'Page.do?', 'itemID=',../@name, '&amp;groupID=', ../../@id)"/>
		      </xsl:variable>
		      <xsl:variable name="url-with-hash">
		        <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
              </xsl:variable>
              <a href="{$url-with-hash}"><xsl:value-of select="$textual-contents"/></a>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
        <xsl:value-of select="$textual-contents"/>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>
  
  <xsl:template name="num-visible">
    <xsl:param name="current"/>
    <xsl:param name="shown"/>
    <xsl:choose>
      <xsl:when test="$current &gt;= 1">
        <xsl:variable name="prefix" select="xalan:nodeset($structure)/COLUMN[$current]/@name"/>
        <xsl:variable name="invisible">
          <xsl:if test="$prefix = 'Group'">
            <xsl:value-of select="number($Group.Column.Visible = 'false' or $Mode='text')"/>
          </xsl:if>
          <xsl:if test="$prefix = 'Description'">
            <xsl:value-of select="number($Description.Column.Visible = 'false')"/>
          </xsl:if>
          <xsl:if test="$prefix = 'Value'">
            <xsl:value-of select="number($Value.Column.Visible = 'false')"/>
          </xsl:if>
        </xsl:variable>
        <xsl:call-template name="num-visible">
          <xsl:with-param name="current" select="$current - 1"/>
          <xsl:with-param name="shown" select="$shown - $invisible"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$shown"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
<!-- PLACEHOLDERS -->

  <xsl:template name="gen-action-set">
    <xsl:param name="position" />
    <xsl:param name="context" />
  </xsl:template>

  <xsl:template name="gen-action-control">
    <xsl:param name="context" />
  </xsl:template>

</xsl:stylesheet>