<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2004-2005 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose such
Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:redirect="http://xml.apache.org/xalan/redirect"
                extension-element-prefixes="redirect" >

  <xsl:output method="html" indent="yes" omit-xml-declaration="yes"
              encoding="ISO-8859-1" />

  <xsl:param name="time-stamp" select="''" />
  <xsl:param name="file-name" select="''" />
  <!--
  Xalan will redirect output to the same directory as the main output
  directory, but XSLTC will not, so the output directory must be set
  explicitly.
  -->
  <xsl:param name="output-dir" select="'.'" />

  <xsl:key name="beans-by-if" match="BEAN" use="@INTERFACE" />
  <xsl:key name="domain-by-name" match="DOMAIN" use="@NAME" />

  <!--
  Marginally faster than "../PROPERTY" XPath because it is used
  a huge number of times.
  -->
  <xsl:key name="props-by-bean-id" match="PROPERTY" use="generate-id(..)" />
  <xsl:key name="prop-names-by-bean-id" match="PROPERTY/@NAME"
           use="generate-id(../..)" />

  <!--
  Common header and footer menus for all pages.
  -->
  <xsl:variable name="html-menu">
    <div class="menu">
      <a href="index.html">
        <xsl:text>Home</xsl:text>
      </a>
      <xsl:text> | </xsl:text>
      <a href="index-domain.html">
        <xsl:text>Domain Definitions</xsl:text>
      </a>
      <xsl:text> | </xsl:text>
      <a href="index-class.html">
        <xsl:text>Classes</xsl:text>
      </a>
      <xsl:text> | </xsl:text>
      <a href="index-operation.html">
        <xsl:text>Operations</xsl:text>
      </a>
      <xsl:text> | </xsl:text>
      <a href="index-code-table.html">
        <xsl:text>Code Tables</xsl:text>
      </a>
    </div>
  </xsl:variable>

  <xsl:variable name="html-footer">
    <hr/>
    <xsl:copy-of select="$html-menu" />
    <xsl:if test="not($time-stamp = '') and not($file-name = '')">
      <div class="generated">
        <xsl:text>Generated on </xsl:text>
        <xsl:value-of select="$time-stamp" />
        <xsl:text> from </xsl:text>
        <i><xsl:value-of select="$file-name" /></i>
        <xsl:text>.</xsl:text>
      </div>
    </xsl:if>
  </xsl:variable>

  <!--
  The heading for the properties table on an operation page.
  -->
  <xsl:variable name="properties-heading">
    <thead>
      <tr>
        <th><xsl:text>&#160;</xsl:text></th>
        <th><xsl:text>Unique Name</xsl:text></th>
        <th><xsl:text>Full Name</xsl:text></th>
        <th><xsl:text>Direction</xsl:text></th>
        <th><xsl:text>Mandatory</xsl:text></th>
        <th><xsl:text>List</xsl:text></th>
        <th><xsl:text>Domain Name</xsl:text></th>
      </tr>
    </thead>
  </xsl:variable>

  <xsl:template match="SERVER_ACCESS_BEANS">

    <xsl:call-template name="generate-home-page" />
    <xsl:call-template name="generate-domain-index" />
    <xsl:call-template name="generate-code-table-index" />
    <xsl:call-template name="generate-class-index" />
    <xsl:call-template name="generate-operation-index" />

  </xsl:template>

  <xsl:template name="generate-home-page">
    <!--
    Main output from stylesheet.
    -->
    <html>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="'Introduction'" />
      </xsl:call-template>
      <body>
        <xsl:call-template name="html-title">
          <xsl:with-param name="title" select="'Introduction'" />
        </xsl:call-template>
        <p>
          <xsl:text>This is the reference guide to the server interfaces
          that can be used in UIM documents. A server interface is
          identified by a </xsl:text>
          <i><xsl:text>class</xsl:text></i>
          <xsl:text> name and an </xsl:text>
          <i><xsl:text>operation</xsl:text></i>
          <xsl:text> name. Each </xsl:text>
          <i><xsl:text>operation</xsl:text></i>
          <xsl:text> may have a number of named </xsl:text>
          <i><xsl:text>properties.</xsl:text></i>
          <xsl:text>The reference page for each operation has a </xsl:text>
          <b><xsl:text>Copy</xsl:text></b>
          <xsl:text> button beside each name. Pressing the copy button will
          copy the name into the clipboard so that it can be pasted into
          your UIM document.</xsl:text>
        </p>
        <p>
          <xsl:text>Start by choosing one of the main alphabetical indexes
          below:</xsl:text>
        </p>
        <ul>
          <li>
            <a href="index-class.html">
              <xsl:text>Class (Facade) A-Z Index</xsl:text>
            </a>
          </li>
          <li>
            <a href="index-operation.html">
              <xsl:text>Operation A-Z Index</xsl:text>
            </a>
          </li>
          <li>
            <a href="index-domain.html">
              <xsl:text>Domain Definition A-Z Index</xsl:text>
            </a>
          </li>
          <li>
            <a href="index-code-table.html">
              <xsl:text>Code Table A-Z Index</xsl:text>
            </a>
          </li>
        </ul>
        <xsl:copy-of select="$html-footer" />
      </body>
    </html>
  </xsl:template>

  <xsl:template name="generate-domain-index">
    <xsl:variable name="file-name"
                  select="concat($output-dir, '/index-domain.html')" />
    <redirect:write file="{$file-name}">
      <html>
        <xsl:call-template name="html-head">
          <xsl:with-param name="title" select="'Domains'" />
        </xsl:call-template>
        <body>
          <xsl:call-template name="html-title">
            <xsl:with-param name="title" select="'Domains'" />
          </xsl:call-template>
          <h3>
            <xsl:value-of select="concat('Number of Domains: ',
                                         count(DOMAIN))" />
          </h3>
          <table border="1">
            <thead>
              <tr>
                <th><xsl:text>Domain Name</xsl:text></th>
                <th><xsl:text>Derived From</xsl:text></th>
                <th><xsl:text>Length</xsl:text></th>
                <th><xsl:text>Code Table Name</xsl:text></th>
              </tr>
            </thead>
            <tbody>
              <xsl:apply-templates select="DOMAIN" mode="domain-index">
                <xsl:sort data-type="text" order="ascending" select="@NAME"/>
              </xsl:apply-templates>
            </tbody>
          </table>
          <xsl:copy-of select="$html-footer" />
        </body>
      </html>
    </redirect:write>
  </xsl:template>

  <xsl:template name="generate-class-index">
    <!--
    Get just one BEAN element for each INTERFACE name.
    -->
    <xsl:variable name="classes"
                  select="BEAN[generate-id(key('beans-by-if', @INTERFACE)[1])
                               = generate-id()]" />
    <xsl:variable name="file-name"
                  select="concat($output-dir, '/index-class.html')" />
    
    <redirect:write file="{$file-name}">
      <html>
        <xsl:call-template name="html-head">
          <xsl:with-param name="title" select="'Classes'" />
        </xsl:call-template>
        <body>
          <xsl:call-template name="html-title">
            <xsl:with-param name="title" select="'Classes'" />
          </xsl:call-template>
          <h3>
            <xsl:value-of select="concat('Number of Classes: ',
                                         count($classes))" />
          </h3>
          <table border="1">
            <thead>
              <tr>
                <th><xsl:text>Class Name</xsl:text></th>
                <th><xsl:text>Operations</xsl:text></th>
              </tr>
            </thead>
            <tbody>
              <xsl:for-each select="$classes">
                <xsl:sort data-type="text" order="ascending"
                          select="@INTERFACE"/>
                <xsl:apply-templates select="." mode="class-index" />
              </xsl:for-each>
            </tbody>
          </table>
          <xsl:copy-of select="$html-footer" />
        </body>
      </html>
    </redirect:write>
  </xsl:template>

  <xsl:template name="generate-operation-index">
    <xsl:variable name="file-name"
                  select="concat($output-dir, '/index-operation.html')" />
    <redirect:write file="{$file-name}">
      <html>
        <xsl:call-template name="html-head">
          <xsl:with-param name="title" select="'Operations'" />
        </xsl:call-template>
        <body>
          <xsl:call-template name="html-title">
            <xsl:with-param name="title" select="'Operations'" />
          </xsl:call-template>
          <h3>
            <xsl:value-of select="concat('Number of Operations: ',
                                         count(BEAN))" />
          </h3>

          <table border="1">
            <thead>
              <tr>
                <th><xsl:text>Operation Name</xsl:text></th>
                <th><xsl:text>Class Name</xsl:text></th>
                <th><xsl:text>Properties</xsl:text></th>
                <th><xsl:text>List Properties</xsl:text></th>
                <th><xsl:text>Mandatory Properties</xsl:text></th>
              </tr>
            </thead>
            <tbody>
              <xsl:for-each select="BEAN" >
                <xsl:sort data-type="text" order="ascending"
                          select="@OPERATION"/>
                <xsl:sort data-type="text" order="ascending"
                          select="@INTERFACE"/>
                <xsl:apply-templates select="." mode="operation-index" />
              </xsl:for-each>
            </tbody>
          </table>
          <xsl:copy-of select="$html-footer" />
        </body>
      </html>
    </redirect:write>
  </xsl:template>

  <xsl:template name="generate-code-table-index">
    <xsl:variable name="file-name"
                  select="concat($output-dir, '/index-code-table.html')" />
    <redirect:write file="{$file-name}">
      <html>
        <xsl:call-template name="html-head">
          <xsl:with-param name="title" select="'Code Tables'" />
        </xsl:call-template>
        <body>
          <xsl:call-template name="html-title">
            <xsl:with-param name="title" select="'Code Tables'" />
          </xsl:call-template>
          <h3>
            <xsl:value-of select="concat('Number of Code Tables: ',
                                         count(DOMAIN[@CODETABLE]))" />
          </h3>
          <table border="1">
            <thead>
              <tr>
                <th><xsl:text>Code Table Name</xsl:text></th>
                <th><xsl:text>Domain Name</xsl:text></th>
              </tr>
            </thead>
            <tbody>
              <xsl:apply-templates select="DOMAIN[@CODETABLE]"
                                   mode="code-table-index">
                <xsl:sort data-type="text" order="ascending"
                          select="@CODETABLE"/>
              </xsl:apply-templates>
            </tbody>
          </table>
          <xsl:copy-of select="$html-footer" />
       </body>
      </html>
    </redirect:write>
  </xsl:template>

  <xsl:template match="BEAN" mode="operation-index">
    <xsl:variable name="properties"
                  select="key('props-by-bean-id', generate-id(.))" />
    <tr>
      <td>
        <a href="{concat('cls-', @INTERFACE, '.html#op-', @OPERATION)}">
          <xsl:value-of select="@OPERATION" />
        </a>
      </td>
      <td>
        <a href="{concat('cls-', @INTERFACE, '.html')}">
          <xsl:value-of select="@INTERFACE" />
        </a>
      </td>
      <td align="right">
        <xsl:value-of select="count($properties)" />
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="$properties/@LIST != '0'">
            <xsl:text>Yes</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>No</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="$properties/@MODE = 'in_mandatory'">
            <xsl:text>Yes</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>No</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="BEAN" mode="class-index">
    <tr>
      <td>
        <a href="{concat('cls-', @INTERFACE, '.html')}">
          <xsl:value-of select="@INTERFACE" />
        </a>
      </td>
      <td align="right">
        <!-- The "class-page" will produce the operation count. -->
        <xsl:apply-templates select="." mode="class-page" />
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="BEAN" mode="class-page">

    <xsl:variable name="operation-count"
                  select="count(key('beans-by-if', @INTERFACE))" />
    <xsl:value-of select="$operation-count" />

    <xsl:variable name="file-name"
                  select="concat($output-dir, '/cls-', @INTERFACE, '.html')" />
    <redirect:write file="{$file-name}">
      <html>
        <xsl:call-template name="html-head">
          <xsl:with-param name="title" select="'Class'" />
          <xsl:with-param name="sub-title" select="@INTERFACE" />
        </xsl:call-template>
        <body>
          <xsl:call-template name="html-title">
            <xsl:with-param name="title" select="'Class'" />
            <xsl:with-param name="sub-title" select="@INTERFACE" />
          </xsl:call-template>
          <h3>
            <xsl:value-of select="concat('Number of Operations: ',
                                         $operation-count)" />
          </h3>
          <table border="1">
            <thead>
              <tr>
                <th><xsl:text>Operation</xsl:text></th>
                <th><xsl:text>Properties</xsl:text></th>
                <th><xsl:text>List Properties</xsl:text></th>
                <th><xsl:text>Mandatory Properties</xsl:text></th>
              </tr>
            </thead>
            <tbody>
              <xsl:apply-templates select="key('beans-by-if', @INTERFACE)"
                                   mode="class-operation-index">
                <xsl:sort data-type="text" order="ascending"
                          select="@OPERATION"/>
              </xsl:apply-templates>
            </tbody>
          </table>
          <xsl:apply-templates select="key('beans-by-if', @INTERFACE)"
                               mode="operation-details">
            <xsl:sort data-type="text" order="ascending"
                      select="@OPERATION"/>
          </xsl:apply-templates>
          <xsl:copy-of select="$html-footer" />
        </body>
      </html>
    </redirect:write>
  </xsl:template>

    <xsl:template match="BEAN" mode="operation-details">
    <xsl:param name="properties"
               select="key('props-by-bean-id', generate-id(.))" />

    <xsl:variable name="property-count" select="count($properties)" />

    <hr/>
    <a name="{concat('op-', @OPERATION)}" />
    <xsl:call-template name="html-title-operation"/>
    <h3>
      <xsl:value-of select="concat('Number of Properties: ',
                                   $property-count)" />
    </h3>
    <table border="1">
      <xsl:copy-of select="$properties-heading" />
      <tbody>
        <!--
        Sort the properties by LIST first, so that properties in
        the same list are grouped together, and then by NAME.
        -->
        <xsl:apply-templates select="$properties"
                             mode="operation-property-index">
          <xsl:sort data-type="number" order="ascending"
                    select="@LIST"/>
          <xsl:sort data-type="text" order="ascending"
                    select="@NAME"/>
        </xsl:apply-templates>
      </tbody>
    </table>
  </xsl:template>

  <xsl:template match="BEAN" mode="class-operation-index">
    <xsl:variable name="properties"
                  select="key('props-by-bean-id', generate-id(.))" />
    <tr>
      <td>
        <a href="{concat('#op-', @OPERATION)}">
          <xsl:value-of select="@OPERATION" />
        </a>
      </td>
      <td align="right">
        <xsl:value-of select="count($properties)" />
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="$properties/@LIST != '0'">
            <xsl:text>Yes</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>No</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="$properties/@MODE = 'in_mandatory'">
            <xsl:text>Yes</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>No</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="PROPERTY" mode="operation-property-index">

    <xsl:variable name="short-name">
      <xsl:call-template name="get-short-name" />
    </xsl:variable>

    <tr>
      <td>
        <input type="button" value="Copy"
               onclick="{concat('clipboardData.setData(&quot;Text&quot;, ',
                                '&quot;', $short-name, '&quot;)')}" />
      </td>
      <td>
        <xsl:value-of select="$short-name" />
      </td>
      <td>
        <xsl:value-of select="@NAME" />
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="@MODE = 'out'">
            <xsl:text>Out</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>In</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="@MODE = 'in_mandatory'">
            <xsl:text>Yes</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>No</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td align="center">
        <xsl:choose>
          <xsl:when test="@LIST = '0'">
            <xsl:text>None</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>List </xsl:text>
            <xsl:value-of select="@LIST" />
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="starts-with(@DOMAIN, 'SVR_')">
            <xsl:value-of select="@DOMAIN" />
          </xsl:when>
          <xsl:otherwise>
            <a href="{concat('index-domain.html#', @DOMAIN)}">
              <xsl:value-of select="@DOMAIN" />
            </a>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="DOMAIN" mode="domain-index">
    <tr>
      <td>
        <a name="{@NAME}">
          <xsl:value-of select="@NAME" />
        </a>
      </td>
      <td>
        <!--
        Only link to domains from the NEXT column if the domain
        is in the file. Base domains (usually "SVR_...") are not
        listed and cannot be linked.
        -->
        <xsl:choose>
          <xsl:when test="key('domain-by-name', @NEXT)">
            <a href="{concat('index-domain.html#', @NEXT)}">
              <xsl:value-of select="@NEXT" />
            </a>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="@NEXT" />
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="@LENGTH">
            <xsl:value-of select="@LENGTH" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>&#160;</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="@CODETABLE">
            <a href="{concat('index-code-table.html#', @CODETABLE)}">
              <xsl:value-of select="@CODETABLE" />
            </a>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>&#160;</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="DOMAIN" mode="code-table-index">
    <tr>
      <td>
        <a name="{@CODETABLE}">
          <xsl:value-of select="@CODETABLE" />
        </a>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="key('domain-by-name', @NAME)">
            <a href="{concat('index-domain.html#', @NAME)}">
              <xsl:value-of select="@NAME" />
            </a>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="@NAME" />
          </xsl:otherwise>
        </xsl:choose>
      </td>
    </tr>
  </xsl:template>

  <xsl:template name="html-title-operation">
    <xsl:call-template name="html-one-title">
      <xsl:with-param name="title" select="'Class'" />
      <xsl:with-param name="sub-title" select="@INTERFACE" />
      <xsl:with-param name="sub-title-copy" select="1" />
    </xsl:call-template>
    <xsl:call-template name="html-one-title">
      <xsl:with-param name="title" select="'Operation'" />
      <xsl:with-param name="sub-title" select="@OPERATION" />
      <xsl:with-param name="sub-title-copy" select="1" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="html-head">
    <xsl:param name="title" select="''" />
  
    <head>
      <title>
        <xsl:text>Server Interface Reference</xsl:text>
        <xsl:if test="not($title = '')">
          <xsl:value-of select="concat(' - ', $title)" />
        </xsl:if>
      </title>
    </head>
  </xsl:template>

  <xsl:template name="html-title">
    <xsl:param name="title" select="''" />
    <xsl:param name="sub-title" select="''" />
    <xsl:param name="sub-title-copy" select="0" />

    <xsl:copy-of select="$html-menu" />
    <hr/>
    <h1>
      <xsl:text>Server Interface Reference</xsl:text>
    </h1>
    <hr/>
    <xsl:call-template name="html-one-title">
      <xsl:with-param name="title" select="$title" />
      <xsl:with-param name="sub-title" select="$sub-title" />
      <xsl:with-param name="sub-title-copy" select="$sub-title-copy" />
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="html-one-title">
    <xsl:param name="title" select="''" />
    <xsl:param name="sub-title" select="''" />
    <xsl:param name="sub-title-copy" select="0" />

    <xsl:if test="not($title = '')">
      <h2>
        <xsl:value-of select="$title" />
        <xsl:if test="not($sub-title = '')">
          <xsl:text>: </xsl:text>
          <i>
            <xsl:value-of select="$sub-title" />
          </i>
          <xsl:if test="$sub-title-copy != 0">
            <xsl:text>&#160;</xsl:text>
            <input type="button" value="Copy"
                   onclick="{concat('clipboardData.setData(&quot;Text&quot;, ',
                                    '&quot;', $sub-title, '&quot;)')}" />
          </xsl:if>
        </xsl:if>
      </h2>
    </xsl:if>  
  </xsl:template>

  <!--
  Get the shortest unique name for the PROPERTY. The expectation is that
  the last name component is usually unique, so depth-first recursion is
  used to avoid too many tests for uniqueness. Uniqueness is when there is
  one (or zero, see below) matching PROPERTY (the context PROPERTY itself).
  The approach used is somewhat obscure, but is much faster than any more
  obvious approach.
  -->
  <xsl:template name="get-short-name">
    <xsl:param name="name" select="@NAME" />

    <xsl:if test="not($name = '')">
      <xsl:variable name="unique-short-name">
        <xsl:call-template name="get-short-name">
          <xsl:with-param name="name" select="substring-after($name, '$')" />
        </xsl:call-template>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="$unique-short-name = ''">
          <xsl:variable name="found-endings">
            <xsl:variable name="ending" select="concat('$', $name, '.')" />
            <xsl:for-each
                select="key('prop-names-by-bean-id', generate-id(..))">
              <xsl:if test="contains(concat(., '.'), $ending)">
                <xsl:value-of select="'f'" />
              </xsl:if>
            </xsl:for-each>
          </xsl:variable>
          <xsl:if test="string-length($found-endings) &lt;= 1">
            <xsl:value-of select="$name" />
          </xsl:if>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$unique-short-name" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>

  </xsl:template>

</xsl:stylesheet>
