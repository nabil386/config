<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Style sheet for converting RATES_DATA xml into an editable HTML table.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
	xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils"
  exclude-result-prefixes="bidi-utils request-utils">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />
  <!--
    PARAMETERS SECTION
  -->
  <!-- User current locale -->
  <xsl:param name="locale" />
  <!-- Absolute number of columns -->
  <xsl:param name="total-cols" />
  <!-- Number of row elements -->
  <xsl:param name="num-rows" />
  <!-- Number of sub-column elements -->
  <xsl:param name="num-sub-cols" />
  <!-- Read-only rates table flag -->
  <xsl:param name="read-only" />
  <!-- Any parameters to add to hyperlinks -->
  <xsl:param name="o3-parameters" />
  <!-- Whether sub-row headers are visible. This is false if all rows contain
       less than 2 sub-rows. -->
  <xsl:param name="sub-rowHeadersVisible" />
  <!-- The url to the web server with the static content -->
  <xsl:param name="static-content-server-url"/>
  <!-- The node-set for the configuration data. -->
  <xsl:param name="rates-config" />

  <!-- Tooltip parameter -->
  <xsl:param name="ManageColumn.Tooltip"/>
  <!-- Tooltip parameter -->
  <xsl:param name="ManageRow.Tooltip"/>
  <!-- Range seperator -->
  <xsl:param name="Range.Separator"/>
  <xsl:param name="Rates.Table.Summary"/>
  <!-- Code table nodes -->
  <xsl:param name="code-table-row"/>
  <xsl:param name="code-table-column"/>
  <!-- The username variable is supplied by the Transformer object in the jsp tag. -->
  <xsl:param name="username" />

  <!-- Prameters from represent values in property files -->
  <xsl:param name="ModifySubColumn.Image"/>
  <xsl:param name="DeleteSubColumn.Image"/>
  <xsl:param name="NewSubColumn.Image"/>
  <xsl:param name="AddCellData.Image"/>
  <xsl:param name="ModifyCellData.Image"/>
  <xsl:param name="RemoveCellData.Image"/>
  <xsl:param name="AddCellData.Image"/>
  <xsl:param name="NewRow.Image"/>
  <xsl:param name="NewColumn.Image"/>
  <xsl:param name="DeleteSubRow.Image"/>
  <xsl:param name="AddSubRow.Below.Image"/>
  <xsl:param name="AddRow.Below.Image"/>
  <xsl:param name="AddSubRow.Beginning.Image"/>
  <xsl:param name="NewColumn.ToRight.Image"/>
  <xsl:param name="AddSubColumn.Beginning.Image"/>

  <!-- Tooltip properties from property file -->
  <xsl:param name="ModifySubColumn.Tooltip"/>
  <xsl:param name="DeleteSubColumn.Tooltip"/>
  <xsl:param name="NewSubColumn.Tooltip"/>
  <xsl:param name="AddCellData.Tooltip"/>
  <xsl:param name="ModifyCellData.Tooltip"/>
  <xsl:param name="RemoveCellData.Tooltip"/>
  <xsl:param name="AddCellData.Tooltip"/>
  <xsl:param name="Global.NewRow.Tooltip"/>
  <xsl:param name="Global.NewColumn.Tooltip"/>
  <xsl:param name="DeleteSubRow.Tooltip"/>
  <xsl:param name="AddSubRow.Below.Tooltip"/>
  <xsl:param name="AddRow.Below.Tooltip"/>
  <xsl:param name="AddSubRow.Beginning.Tooltip"/>
  <xsl:param name="NewColumn.ToRight.Tooltip"/>
  <xsl:param name="AddSubColumn.Beginning.Tooltip"/>


  <!--
    VARIABLES SECTION
  -->
  <!--The rates table configuration file.-->
  <xsl:variable name="config" select="$rates-config" />
  <xsl:variable name="EQUALS" select="'='" />
  <xsl:variable name="QUESTION_MARK" select="'?'" />
  <xsl:variable name="AMPERSAND" select="'&amp;'" />
  <!--
    ATTRIBUTE-SETS SECTION
  -->
  <!-- Rates table attribute set-->
  <xsl:attribute-set name="rates-table">
    <xsl:attribute name="border">1</xsl:attribute>
    <xsl:attribute name="width">100%</xsl:attribute>
    <xsl:attribute name="id">RatesTable</xsl:attribute>
    <xsl:attribute name="summary">
      <xsl:value-of select="$Rates.Table.Summary"/>
    </xsl:attribute>
    <xsl:attribute name="class">rates-table</xsl:attribute>
  </xsl:attribute-set>
  <!-- Rates table row and col header attribute sets-->
  <xsl:attribute-set name="header">
    <xsl:attribute name="class">header</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="span-two-rows" use-attribute-sets="header">
    <xsl:attribute name="style">width:120px</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="first-column-span-two-cols" use-attribute-sets="header">
    <xsl:attribute name="style">width:240px</xsl:attribute>
    <xsl:attribute name="colspan">2</xsl:attribute>
    <xsl:attribute name="scope">col</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="span-two-cols" use-attribute-sets="header">
    <xsl:attribute name="colspan">2</xsl:attribute>
    <xsl:attribute name="style">width:240px</xsl:attribute>
    <xsl:attribute name="scope">col</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="span-two-cols-two-rows"
                   use-attribute-sets="header">
    <xsl:attribute name="colspan">2</xsl:attribute>
    <xsl:attribute name="style">width:240px</xsl:attribute>
    <xsl:attribute name="rowspan">2</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="sub-row-header">
    <xsl:attribute name="class">sub-row-header</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="sub-column-header">
    <xsl:attribute name="class">sub-column-header</xsl:attribute>
    <xsl:attribute name="scope">col</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="row-header">
    <xsl:attribute name="style">width:240px</xsl:attribute>
    <xsl:attribute name="class">row-header</xsl:attribute>
  </xsl:attribute-set>
  <!-- Cell text attribute set-->
  <xsl:attribute-set name="cell-text">
    <xsl:attribute name="style">width:120px</xsl:attribute>
    <xsl:attribute name="class">cell-text</xsl:attribute>
  </xsl:attribute-set>

  <!--
    TEMPLATES SECTION
  -->
  <!-- Main template -->
  <xsl:template match="RATES_DATA">
   <div style="overflow-x:auto;overflow-y:hidden;padding-bottom:18px">
   
   
   
    <table xsl:use-attribute-sets="rates-table">
        <tr xsl:use-attribute-sets="header">
          <xsl:call-template name="do-top-left-corner" />
          <xsl:for-each select="COLUMN">
            <xsl:call-template name="do-column" />
          </xsl:for-each>
        </tr>
        <xsl:if test="number($num-sub-cols) &gt; 0">
          <tr xsl:use-attribute-sets="header">
            <!-- Create a <td> to fill in a space -->
            <th scope="col" class="sub-column-header" colspan="2" style="width:360px;"><span class="border-span"><xsl:text>&#160;</xsl:text></span></th>      
            
            <xsl:for-each select="/RATES_DATA/COLUMN">
            <xsl:choose>
              <xsl:when test="SUB_COLUMN">
                <xsl:apply-templates select="." mode="do-sub-column" />
              </xsl:when>
              <xsl:otherwise>
                <th scope="col" class="empty-cell"><xsl:text>&#160;</xsl:text></th>
              </xsl:otherwise>
            </xsl:choose>
            </xsl:for-each>
          </tr>
        </xsl:if>
        <xsl:if test="number($num-rows) &gt; 0">
          <xsl:choose>
            <!-- The boundary condition where we have rows on the root level
                 but no cells. -->
            <xsl:when test="./ROW">
              <xsl:for-each select="./ROW">
                <xsl:call-template name="do-row-no-cells" />
              </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              <xsl:call-template name="do-row" />
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
    </table>
   </div>
  </xsl:template>

  <!-- Template for outputting the top left corner with the new row and new
       column buttons. -->
  <xsl:template name="do-top-left-corner">
    <xsl:choose>
      <!-- If sub-cols >= 1 then rowspan is two.-->
      <xsl:when test="number($num-sub-cols) &gt;= 1">
        <xsl:choose>
          <!-- If num rows > 0 then colspan is two.-->
          <xsl:when test="number($num-rows) &gt; 0 and
                          boolean($sub-rowHeadersVisible)">
            <th xsl:use-attribute-sets="span-two-cols">
              <span class="border-span"><xsl:call-template name="output-top-left-buttons" /></span>
            </th>
          </xsl:when>
          <xsl:otherwise>
            <th xsl:use-attribute-sets="first-column-span-two-cols">
              <span class="border-span"><xsl:call-template name="output-top-left-buttons" /></span>
            </th>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:choose>
          <!-- If num rows > 0 then colspan is two.-->
          <xsl:when test="number($num-rows) &gt; 0
                          and boolean($sub-rowHeadersVisible)">
            <th xsl:use-attribute-sets="span-two-cols">
              <span class="border-span"><xsl:call-template name="output-top-left-buttons" /></span>
            </th>
          </xsl:when>
          <xsl:otherwise>
            <td>
              <span class="border-span"><xsl:call-template name="output-top-left-buttons" /></span>
            </td>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Output all rows for a column or sub-column -->
  <xsl:template name="do-row" >
    <xsl:variable name="first-sub-col-rows"
      select="count(/RATES_DATA/COLUMN[position() = 1]
                    /SUB_COLUMN[position() = 1]/ROW)" />

    <xsl:choose>
      <xsl:when test="$first-sub-col-rows &gt; 0">
        <xsl:for-each
          select="/RATES_DATA/COLUMN[position() = 1]
                  /SUB_COLUMN[position() = 1]/ROW">
          <xsl:call-template name="do-row-with-cells" />
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each
          select="/RATES_DATA/COLUMN[position() = 1]/ROW">
          <xsl:call-template name="do-row-with-cells" />
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Output a row which contains cells.-->
  <xsl:template name="do-row-with-cells">
    <xsl:variable name="sub-rows" select="@NUM_SUB_ROWS" />

    <tr class="rates-table-body">
      <td xsl:use-attribute-sets="row-header">
        <xsl:if test="number($sub-rows) &gt; 1">
          <xsl:attribute name="rowspan">
            <xsl:value-of select="number($sub-rows)" />
          </xsl:attribute>
        </xsl:if>
        <xsl:call-template name="do-row-buttons" />
      </td>
      <xsl:call-template name="first-sub-row-header" />
      <xsl:call-template name="do-cells">
        <xsl:with-param name="row" select="number(@ROW_INDEX) + 1" />
        <xsl:with-param name="j" select="0" />
      </xsl:call-template>
    </tr>
    <xsl:call-template name="sub-row-header">
      <xsl:with-param name="i" select="1" />
      <xsl:with-param name="num-sub-rows" select="$sub-rows" />
      <xsl:with-param name="has-cells" select="'true'" />
    </xsl:call-template>
  </xsl:template>

  <!-- Output a row which contains no cells.-->
  <xsl:template name="do-row-no-cells">
    <xsl:variable name="sub-rows" select="@NUM_SUB_ROWS" />

    <tr class="rates-table-body">
      <td xsl:use-attribute-sets="row-header">
        <xsl:if test="number($sub-rows) &gt; 1">
          <xsl:attribute name="rowspan">
            <xsl:value-of select="number($sub-rows)" />
          </xsl:attribute>
        </xsl:if>
        <xsl:call-template name="do-row-buttons" />
      </td>
      <xsl:call-template name="first-sub-row-header-no-cells" />
    </tr>
    <xsl:call-template name="sub-row-header">
      <xsl:with-param name="i" select="1" />
      <xsl:with-param name="num-sub-rows" select="$sub-rows" />
      <xsl:with-param name="has-cells" select="'false'" />
    </xsl:call-template>
  </xsl:template>

  <!-- First sub row header template (It can't be in a separate row) -->
  <xsl:template name="first-sub-row-header">
    <xsl:choose>
      <xsl:when test="number(@NUM_SUB_ROWS) &gt; 1">
        <td xsl:use-attribute-sets="sub-row-header">
          <xsl:call-template name="do-sub-row-buttons">
            <xsl:with-param name="sub-row-index" select="0" />
          </xsl:call-template>
        </td>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="boolean($sub-rowHeadersVisible)">
          <!-- Enter a blank cell to act as a place holder. -->
          <td class="row-header">
          </td>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- First sub row header template (It can't be in a separate row) -->
  <xsl:template name="first-sub-row-header-no-cells">
    <xsl:choose>
      <xsl:when test="number(@NUM_SUB_ROWS) &gt; 1">
        <td xsl:use-attribute-sets="sub-row-header" >
          <xsl:call-template name="do-sub-row-buttons">
            <xsl:with-param name="sub-row-index" select="0" />
          </xsl:call-template>
        </td>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="boolean($sub-rowHeadersVisible)">
          <!-- Enter a blank cell to act as a place holder. -->
          <td class="row-header">
          </td>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
    <!-- Do cells -->
    <xsl:call-template name="do-cells">
      <xsl:with-param name="row" select="number(@ROW_INDEX) + 1" />
      <xsl:with-param name="j" select="0" />
    </xsl:call-template>
  </xsl:template>

  <!-- Sub-row header template-->
  <xsl:template name="sub-row-header">
    <xsl:param name="i" />
    <xsl:param name="num-sub-rows" />
    <xsl:param name="has-cells" />

    <xsl:if test="$i &lt; $num-sub-rows">
      <tr class="rates-table-body">
        <xsl:if test="number(@NUM_SUB_ROWS) &gt; 1">
          <td xsl:use-attribute-sets="sub-row-header" >
            <xsl:call-template name="do-sub-row-buttons">
              <xsl:with-param name="sub-row-index" select="$i" />
            </xsl:call-template>
          </td>
        </xsl:if>
        <!-- Do cells -->
        <xsl:if test="boolean($has-cells)">
          <xsl:call-template name="do-cells">
            <xsl:with-param name="row"
                            select="number(@ROW_INDEX) + number($i) + 1" />
            <xsl:with-param name="j" select="0" />
          </xsl:call-template>
        </xsl:if>
      </tr>
      <xsl:call-template name="sub-row-header">
        <xsl:with-param name="i" select="$i + 1" />
        <xsl:with-param name="num-sub-rows" select="$num-sub-rows" />
        <xsl:with-param name="has-cells" select="$has-cells"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!-- Output a single column-->
  <xsl:template name="do-column">
    <xsl:variable name="num-sub-cols-current-col" select="count(./SUB_COLUMN)" />
    <xsl:choose>
      <!-- When overall number sub-cols >= 1 and this col doesn't have sub-cols
           rowspan = 2, otherwise rowspan = 1 -->
      <xsl:when test="$num-sub-cols &gt;= 1 and number($num-sub-cols-current-col) = 0">
        <th xsl:use-attribute-sets="span-two-rows">
          <xsl:attribute name="scope">col</xsl:attribute>
          <xsl:if test="position() = last()">
            <xsl:attribute name="class">header last-header</xsl:attribute>
          </xsl:if>
          <xsl:call-template name="do-column-buttons" />
        </th>
      </xsl:when>
      <xsl:otherwise>        
        <th class="header">
          <xsl:if test="position() = last()">
            <xsl:attribute name="class">header last-header</xsl:attribute>
            <xsl:attribute name="scope">col</xsl:attribute>
          </xsl:if>
          <xsl:if test="$num-sub-cols-current-col &gt; 1">
            <xsl:attribute name="colspan">
              <xsl:value-of select="$num-sub-cols-current-col" />
            </xsl:attribute>
            <xsl:attribute name="style"><xsl:text>width:</xsl:text><xsl:value-of select="$num-sub-cols-current-col * 100" /><xsl:text>px</xsl:text></xsl:attribute>
          </xsl:if>
          <xsl:call-template name="do-column-buttons" />
        </th>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Outputs a single sub-column -->
  <xsl:template match="SUB_COLUMN" mode="do-sub-column">
    <th xsl:use-attribute-sets="sub-column-header">
      <span class="border-span">
      <!-- The maximum, minimum, and value for the sub-column-->
        <span class="sub-column-span">
          <xsl:call-template name="do-range-value">
            <xsl:with-param name="minimum" select="@MINIMUM" />
            <xsl:with-param name="maximum" select="@MAXIMUM" />
            <xsl:with-param name="value" select="@VALUE" />
          </xsl:call-template>
        </span>
        <xsl:if test="not($read-only)">
          <!-- Modify sub-column button-->
        
          <xsl:call-template name="output-button">
            <xsl:with-param name="href"
                      select="$config/RATES_TABLE_CONFIG/MODIFY_SUB_COL_PAGE"/>
            <xsl:with-param name="parameters"
                      select="concat('RateSubColumnID', $EQUALS, @ID)"/>
            <xsl:with-param name="href-prop" select="'ModifySubColumn.Image'"/>
            <xsl:with-param name="alt-prop" select="'ModifySubColumn.Tooltip'"/>
          </xsl:call-template>
        
          <!-- Delete sub-column button-->
          <xsl:call-template name="output-button">
            <xsl:with-param name="href"
                      select="$config/RATES_TABLE_CONFIG/DELETE_SUB_COL_PAGE"/>
            <xsl:with-param name="parameters"
                      select="concat('RateSubColumnID', $EQUALS, @ID)"/>
            <xsl:with-param name="href-prop" select="'DeleteSubColumn.Image'"/>
            <xsl:with-param name="alt-prop" select="'DeleteSubColumn.Tooltip'"/>
          </xsl:call-template>
        </xsl:if>
      
        <xsl:if test="not($read-only)">
          <!-- New sub-column to the right of this sub-column button-->
          <xsl:call-template name="output-button">
            <xsl:with-param name="href"
                      select="$config/RATES_TABLE_CONFIG/ADD_SUB_COL_PAGE"/>
            <xsl:with-param name="parameters"
                      select="concat('RateColumnID', $EQUALS, ../@ID,
                                    $AMPERSAND,  'RateSubColumnIndex', $EQUALS,
                                    string(number(@INDEX) + 1))"/>
            <xsl:with-param name="href-prop" select="'NewSubColumn.Image'"/>
            <xsl:with-param name="alt-prop" select="'NewSubColumn.Tooltip'"/>        	        	                
          </xsl:call-template>
        
        </xsl:if>
      </span>
    </th>
  </xsl:template>

  <!-- Output a <td> for each cell required, if there is a matching cell,
    enter its values, otherwise output a new cell. -->
  <xsl:template name="do-cells">
    <xsl:param name="row" />
    <xsl:param name="j" />

    <xsl:if test="$j &lt; $total-cols">
      <td xsl:use-attribute-sets="cell-text">

        <xsl:if test="number($j) = number($total-cols) - 1">
          <xsl:attribute name="class">cell-text last-cell</xsl:attribute>
        </xsl:if>
        <xsl:choose>
          <xsl:when test="boolean(/RATES_DATA/COLUMN[@COL_INDEX = number($j) + 1][not(./SUB_COLUMN)])">
            <xsl:variable name="current-col"
                 select="/RATES_DATA/COLUMN[@COL_INDEX = number($j) + 1]" />
            <xsl:choose>
              <xsl:when test="$current-col/ROW/CELL[@COLUMN_INDEX = $row]">
                <xsl:call-template name="do-cell">
                  <xsl:with-param name="current-cell"
                       select="$current-col/ROW/CELL[@COLUMN_INDEX = $row]" />
                </xsl:call-template>
              </xsl:when>
              <xsl:otherwise>
                <!-- Empty cell is in a column. -->
                <xsl:call-template name="add-cell-button">
                  <xsl:with-param name="sub-col-index" select="0" />
                  <xsl:with-param name="abs-row-index" select="$row" />
                  <xsl:with-param name="col-type" select="$current-col/@TYPE" />
                  <xsl:with-param name="col-ID" select="$current-col/@ID" />
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:otherwise>
            <xsl:variable name="current-sub-col" select="/RATES_DATA/COLUMN/
                                SUB_COLUMN[@COL_INDEX = number($j) + 1]" />
            <xsl:choose>
              <xsl:when test="$current-sub-col/ROW/CELL[@COLUMN_INDEX = $row]">
                <xsl:call-template name="do-cell">
                  <xsl:with-param name="current-cell"
                    select="$current-sub-col/ROW/CELL[@COLUMN_INDEX = $row]" />
                </xsl:call-template>
              </xsl:when>
              <xsl:otherwise>
                <!-- Empty cell is in a sub-column. -->
                <xsl:call-template name="add-cell-button">
                  <xsl:with-param name="sub-col-index"
                            select="$current-sub-col/@INDEX" />
                  <xsl:with-param name="abs-row-index" select="$row" />
                  <xsl:with-param name="col-type"
                            select="$current-sub-col/../@TYPE" />
                  <xsl:with-param name="col-ID"
                            select="$current-sub-col/../@ID" />
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <xsl:call-template name="do-cells">
        <xsl:with-param name="row" select="$row" />
        <xsl:with-param name="j" select="number($j) + 1" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!-- Template for a cell with values. -->
  <xsl:template name="do-cell">
    <xsl:param name="current-cell" />

    <!-- The maximum, minimum, and values for the cell-->
    <span class="cell-span">
    <xsl:call-template name="do-range-value">
      <xsl:with-param name="minimum" select="$current-cell/@MINIMUM" />
      <xsl:with-param name="maximum" select="$current-cell/@MAXIMUM" />
      <xsl:with-param name="value" select="$current-cell/@VALUE" />            
    </xsl:call-template>
    </span>
    <xsl:call-template name="regular-cell-buttons">
      <xsl:with-param name="cell" select="$current-cell"/>
    </xsl:call-template>
  </xsl:template>

  <!-- Outputs an add cell data button for case where there is no CELL
       element -->
  <xsl:template name="add-cell-button">
    <xsl:param name="sub-col-index" />
    <xsl:param name="abs-row-index" />
    <xsl:param name="col-type" />
    <xsl:param name="col-ID" />

    <xsl:if test="not($read-only)">
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/ADD_CELL_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('RateColumnID', $EQUALS, $col-ID,
                  $AMPERSAND, 'RateRowID', $EQUALS, @ID,
                  $AMPERSAND, 'RateCellIndex', $EQUALS, $abs-row-index,
                  $AMPERSAND, 'RateCellType', $EQUALS, $col-type,
                  $AMPERSAND, 'RateSubColIndex', $EQUALS, $sub-col-index)"/>
        <xsl:with-param name="href-prop" select="'AddCellData.Image'"/>
        <xsl:with-param name="alt-prop" select="'AddCellData.Tooltip'"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!-- Outputs both buttons for a regular cell. -->
  <xsl:template name="regular-cell-buttons">
    <xsl:param name="cell" />

    <xsl:if test="not($read-only)">
      <xsl:choose>
        <xsl:when test="boolean($cell/@VALUE) or boolean($cell/@MAXIMUM)
                        or boolean($cell/@MINIMUM)">
          <xsl:call-template name="output-button">
            <xsl:with-param name="href"
                      select="$config/RATES_TABLE_CONFIG/MODIFY_CELL_PAGE"/>
            <xsl:with-param name="parameters"
                      select="concat('RateCellID', $EQUALS, $cell/@ID)"/>
            <xsl:with-param name="href-prop" select="'ModifyCellData.Image'"/>
            <xsl:with-param name="alt-prop" select="'ModifyCellData.Tooltip'"/>
          </xsl:call-template>
          
          <xsl:call-template name="output-button">
            <xsl:with-param name="href"
                      select="$config/RATES_TABLE_CONFIG/DELETE_CELL_PAGE"/>
            <xsl:with-param name="parameters"
                      select="concat('RateCellID', $EQUALS, $cell/@ID)"/>
            <xsl:with-param name="href-prop" select="'RemoveCellData.Image'"/>
            <xsl:with-param name="alt-prop" select="'RemoveCellData.Tooltip'"/>
          </xsl:call-template>
          
        </xsl:when>
        <xsl:otherwise>
          <!-- Only have a single button pointing to the add new cell page,
               also with a cell id -->
          <xsl:call-template name="output-button">
            <xsl:with-param name="href"
                      select="$config/RATES_TABLE_CONFIG/ADD_CELL_PAGE"/>
            <xsl:with-param name="parameters"
                      select="concat('RateCellID', $EQUALS, $cell/@ID)"/>
            <xsl:with-param name="href-prop" select="'AddCellData.Image'"/>
            <xsl:with-param name="alt-prop" select="'AddCellData.Tooltip'"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <!-- Outputs the new row and new col button for top left cell.-->
  <xsl:template name="output-top-left-buttons">
    <xsl:if test="not($read-only)">
      <!-- New Row Button-->
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/NEW_ROW_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('RowIndex', $EQUALS, '1')"/>
        <xsl:with-param name="href-prop" select="'NewRow.Image'"/>
        <xsl:with-param name="alt-prop" select="'Global.NewRow.Tooltip'"/>
      </xsl:call-template>
      
      <!-- New Column Button-->
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/NEW_COLUMN_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('ColumnIndex', $EQUALS, '1')"/>
        <xsl:with-param name="href-prop" select="'NewColumn.Image'"/>
        <xsl:with-param name="alt-prop" select="'Global.NewColumn.Tooltip'"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!-- Output buttons for sub-row header -->
  <xsl:template name="do-sub-row-buttons">
    <xsl:param name="sub-row-index" />
    <xsl:choose>
      <xsl:when test="not($read-only)">
        
        <!-- Delete This Sub Row Button-->
        <xsl:call-template name="output-button">
          <xsl:with-param name="href"
                    select="$config/RATES_TABLE_CONFIG/DELETE_SUB_ROW_PAGE"/>
          <xsl:with-param name="parameters"
                    select="concat('RateRowID', $EQUALS, @ID, $AMPERSAND,
                 'SubRowIndex', $EQUALS, string(number($sub-row-index) + 1))"/>
          <xsl:with-param name="href-prop" select="'DeleteSubRow.Image'"/>
          <xsl:with-param name="alt-prop" select="'DeleteSubRow.Tooltip'"/>
        </xsl:call-template>
        
        <!-- Add Sub-row below the current row button-->
        <xsl:call-template name="output-button">
          <xsl:with-param name="href"
                    select="$config/RATES_TABLE_CONFIG/ADD_SUB_ROW_PAGE"/>
          <xsl:with-param name="parameters"
                    select="concat('RateRowID', $EQUALS, @ID, $AMPERSAND,
                 'SubRowIndex', $EQUALS, string(number($sub-row-index) + 2))"/>
          <xsl:with-param name="href-prop" select="'AddSubRow.Below.Image'"/>
          <xsl:with-param name="alt-prop" select="'AddSubRow.Below.Tooltip'"/>
        </xsl:call-template>
        
      </xsl:when>
      <xsl:otherwise>
        
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Output buttons and title for a row -->
  <xsl:template name="do-row-buttons">
  
    <xsl:variable name="url-no-hash">
      <xsl:value-of select="concat($config/RATES_TABLE_CONFIG/MANAGE_ROW_PAGE, 'Page.do', 
                 $QUESTION_MARK, $o3-parameters,
                 $AMPERSAND, 'RateRowID', $EQUALS, @ID)"/>
    </xsl:variable>
    <xsl:variable name="url-with-hash">
      <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
    </xsl:variable>
  
    <xsl:choose>
      <xsl:when test="not($read-only)">      
        <a href="#" onclick="dojo.stopEvent(arguments[0]);curam.util.openModalDialog({{href: '{$url-with-hash}' }}); return false;"
          title="{$ManageRow.Tooltip}">        
        	<xsl:attribute name="dir">
		    	<xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(ManageRow.Tooltip)"/>
        	</xsl:attribute>                       
          <xsl:call-template name="read-code">
            <xsl:with-param name="code" select="string(@TYPE)"/>
            <xsl:with-param name="row-code" select="1"/>
          </xsl:call-template>
        </a>
      </xsl:when>
      <xsl:otherwise>
        <!-- Column Heading -->
        <xsl:call-template name="read-code">
          <xsl:with-param name="code" select="string(@TYPE)"/>
          <xsl:with-param name="row-code" select="1"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:if test="not($read-only)">
      <!-- Add New row below this row button-->
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/NEW_ROW_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('RowIndex', $EQUALS,
                          string(number(@INDEX) + 1))"/>
        <xsl:with-param name="href-prop" select="'AddRow.Below.Image'"/>
        <xsl:with-param name="alt-prop" select="'AddRow.Below.Tooltip'"/>
      </xsl:call-template>
      
      <!-- Add Sub-row to top of this row button-->
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/ADD_SUB_ROW_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('RateRowID', $EQUALS, @ID,
                          $AMPERSAND, 'SubRowIndex', $EQUALS, '1')"/>
        <xsl:with-param name="href-prop" select="'AddSubRow.Beginning.Image'"/>
        <xsl:with-param name="alt-prop"
                  select="'AddSubRow.Beginning.Tooltip'"/>
      </xsl:call-template>
      
    </xsl:if>
  </xsl:template>

  <!-- Output buttons and title for a column -->
  <xsl:template name="do-column-buttons">
  
  <xsl:variable name="url-no-hash">
      <xsl:value-of select="concat($config/RATES_TABLE_CONFIG/MANAGE_COLUMN_PAGE,
                'Page.do', $QUESTION_MARK, $o3-parameters,
                $AMPERSAND, 'RateColumnID', $EQUALS, @ID)"/>
    </xsl:variable>
    <xsl:variable name="url-with-hash">
      <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
    </xsl:variable>
  
    <xsl:choose>
      <xsl:when test="not($read-only)">
        <a href="#" onclick="dojo.stopEvent(arguments[0]);curam.util.openModalDialog({{ href: '{$url-with-hash}' }}); return false;"
          title="{$ManageColumn.Tooltip}">
        	<xsl:attribute name="dir">
		    	<xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(ManageColumn.Tooltip)"/>
        	</xsl:attribute>
          
          <xsl:call-template name="read-code">
            <xsl:with-param name="code" select="string(@TYPE)"/>
            <xsl:with-param name="row-code" select="0"/>
          </xsl:call-template>
        </a>
      </xsl:when>
      <xsl:otherwise>
        <!-- Column Heading -->
        <xsl:call-template name="read-code">
          <xsl:with-param name="code" select="string(@TYPE)"/>
          <xsl:with-param name="row-code" select="0"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:if test="not($read-only)">
      <!-- Add New column to the right of this column button-->
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/NEW_COLUMN_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('ColumnIndex',
                         $EQUALS, string(number(@INDEX) + 1))"/>
        <xsl:with-param name="href-prop" select="'NewColumn.ToRight.Image'"/>
        <xsl:with-param name="alt-prop" select="'NewColumn.ToRight.Tooltip'"/>
      </xsl:call-template>
      
      <!-- Add Sub-column to beginning of this column button-->
      <xsl:call-template name="output-button">
        <xsl:with-param name="href"
                  select="$config/RATES_TABLE_CONFIG/ADD_SUB_COL_PAGE"/>
        <xsl:with-param name="parameters"
                  select="concat('RateColumnID', $EQUALS, @ID,
                          $AMPERSAND, 'SubColumnIndex', $EQUALS, '1')"/>
        <xsl:with-param name="href-prop"
                  select="'AddSubColumn.Beginning.Image'"/>
        <xsl:with-param name="alt-prop"
                  select="'AddSubColumn.Beginning.Tooltip'"/>
      </xsl:call-template>
      
    </xsl:if>
  </xsl:template>

  <!-- Output maximum, minimum and value of range -->
  <xsl:template name="do-range-value">
    <xsl:param name="minimum" />
    <xsl:param name="maximum" />
    <xsl:param name="value" />

    
    <xsl:if test="$minimum">
      <xsl:value-of select="$minimum" />
    </xsl:if>
    <xsl:if test="boolean($minimum) or boolean($maximum)">
      <xsl:value-of select="$Range.Separator" />
    </xsl:if>
    <xsl:if  test="$maximum">
      <xsl:value-of select="$maximum" />
    </xsl:if>
    <xsl:if  test="(boolean($minimum) or boolean($maximum)) and
                    boolean($value)">
      <xsl:value-of select="$Range.Separator" />
    </xsl:if>
    <xsl:if test="$value">
      <xsl:value-of select="$value" />
    </xsl:if>
    
  </xsl:template>

  <!-- Read code table template -->
  <xsl:template name="read-code">
    <xsl:param name="code"/>
    <!-- If row-code parameter is 1 then a look-up is done on the row types
         code table, otherwise the look-up is on the column types table. -->
    <xsl:param name="row-code"/>

    <xsl:choose>
      <xsl:when test="number($row-code) = 1">
        
        <xsl:value-of select="$code-table-row/item[@code = $code]/description"/>
        
      </xsl:when>
      <xsl:otherwise>
        
        <xsl:value-of select="$code-table-column/item[@code = $code]/description"/>
        
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Template for outputting a "button"-->
  <xsl:template name="output-button">
    <xsl:param name="href" />
    <xsl:param name="parameters" />
    <xsl:param name="href-prop" />
    <xsl:param name="alt-prop" />
    
    <xsl:variable name="url-no-hash">
      <xsl:value-of select="concat($href,
                  'Page.do', $QUESTION_MARK, $o3-parameters, $AMPERSAND,
                  $parameters)"/>
    </xsl:variable>
    <xsl:variable name="url-with-hash">
      <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
    </xsl:variable>
    

    <a href="#" onclick="dojo.stopEvent(arguments[0]);curam.util.openModalDialog( {{href: '{$url-with-hash}' }}); return false;">   
      <img>
        <xsl:attribute name="src">
          <xsl:value-of select="$static-content-server-url"/>
          <xsl:text>/</xsl:text>
          <xsl:choose>
            <xsl:when test="$href-prop = 'ModifySubColumn.Image'">
        <xsl:value-of select="$ModifySubColumn.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'DeleteSubColumn.Image'">
        <xsl:value-of select="$DeleteSubColumn.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'NewSubColumn.Image'">
        <xsl:value-of select="$NewSubColumn.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'AddCellData.Image'">
        <xsl:value-of select="$AddCellData.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'ModifyCellData.Image'">
        <xsl:value-of select="$ModifyCellData.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'RemoveCellData.Image'">
        <xsl:value-of select="$RemoveCellData.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'AddCellData.Image'">
        <xsl:value-of select="$AddCellData.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'NewRow.Image'">
        <xsl:value-of select="$NewRow.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'NewColumn.Image'">
        <xsl:value-of select="$NewColumn.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'DeleteSubRow.Image'">
        <xsl:value-of select="$DeleteSubRow.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'AddSubRow.Below.Image'">
        <xsl:value-of select="$AddSubRow.Below.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'AddRow.Below.Image'">
        <xsl:value-of select="$AddRow.Below.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'AddSubRow.Beginning.Image'">
        <xsl:value-of select="$AddSubRow.Beginning.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'NewColumn.ToRight.Image'">
        <xsl:value-of select="$NewColumn.ToRight.Image"/>
      </xsl:when>
      <xsl:when test="$href-prop = 'AddSubColumn.Beginning.Image'">
        <xsl:value-of select="$AddSubColumn.Beginning.Image"/>
      </xsl:when>
          </xsl:choose>
          </xsl:attribute>
          <xsl:attribute name="alt">
      <xsl:choose>
        <xsl:when test="$alt-prop = 'ModifySubColumn.Tooltip'">
          <xsl:value-of select="$ModifySubColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'DeleteSubColumn.Tooltip'">
        <xsl:value-of select="$DeleteSubColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'NewSubColumn.Tooltip'">
        <xsl:value-of select="$NewSubColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddCellData.Tooltip'">
        <xsl:value-of select="$AddCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'ModifyCellData.Tooltip'">
        <xsl:value-of select="$ModifyCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'RemoveCellData.Tooltip'">
        <xsl:value-of select="$RemoveCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddCellData.Tooltip'">
        <xsl:value-of select="$AddCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'Global.NewRow.Tooltip'">
        <xsl:value-of select="$Global.NewRow.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'Global.NewColumn.Tooltip'">
        <xsl:value-of select="$Global.NewColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'DeleteSubRow.Tooltip'">
        <xsl:value-of select="$DeleteSubRow.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddSubRow.Below.Tooltip'">
        <xsl:value-of select="$AddSubRow.Below.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddRow.Below.Tooltip'">
        <xsl:value-of select="$AddRow.Below.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddSubRow.Beginning.Tooltip'">
        <xsl:value-of select="$AddSubRow.Beginning.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'NewColumn.ToRight.Tooltip'">
        <xsl:value-of select="$NewColumn.ToRight.Tooltip"/>
       </xsl:when>
      <xsl:when test="$alt-prop = 'AddSubColumn.Beginning.Tooltip'">
        <xsl:value-of select="$AddSubColumn.Beginning.Tooltip"/>
      </xsl:when>
        <xsl:otherwise></xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
    <xsl:attribute name="title">
      <xsl:choose>
        <xsl:when test="$alt-prop = 'ModifySubColumn.Tooltip'">
          <xsl:value-of select="$ModifySubColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'DeleteSubColumn.Tooltip'">
        <xsl:value-of select="$DeleteSubColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'NewSubColumn.Tooltip'">
        <xsl:value-of select="$NewSubColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddCellData.Tooltip'">
        <xsl:value-of select="$AddCellData.Tooltip"/>
      </xsl:when>
        <xsl:when test="$alt-prop = 'ModifyCellData.Tooltip'">
        <xsl:value-of select="$ModifyCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'RemoveCellData.Tooltip'">
        <xsl:value-of select="$RemoveCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddCellData.Tooltip'">
        <xsl:value-of select="$AddCellData.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'Global.NewRow.Tooltip'">
        <xsl:value-of select="$Global.NewRow.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'Global.NewColumn.Tooltip'">
        <xsl:value-of select="$Global.NewColumn.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'DeleteSubRow.Tooltip'">
        <xsl:value-of select="$DeleteSubRow.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddSubRow.Below.Tooltip'">
        <xsl:value-of select="$AddSubRow.Below.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddRow.Below.Tooltip'">
        <xsl:value-of select="$AddRow.Below.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddSubRow.Beginning.Tooltip'">
        <xsl:value-of select="$AddSubRow.Beginning.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'NewColumn.ToRight.Tooltip'">
        <xsl:value-of select="$NewColumn.ToRight.Tooltip"/>
      </xsl:when>
      <xsl:when test="$alt-prop = 'AddSubColumn.Beginning.Tooltip'">
        <xsl:value-of select="$AddSubColumn.Beginning.Tooltip"/>
      </xsl:when>
      </xsl:choose>
     </xsl:attribute>     
     	
        <xsl:attribute name="dir">
			<xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(current()/@LABEL)"/>
        </xsl:attribute>
                  
      </img>
    </a>
  </xsl:template>

</xsl:stylesheet>
