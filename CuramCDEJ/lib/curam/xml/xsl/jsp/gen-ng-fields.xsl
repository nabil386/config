<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2007,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Generates the "next generation" JSP tags for FIELD elements that are associated
with domain definitions.
-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cing="http://www.curamsoftware.com/curam/jde/client/curam-ng">

  <!--
  Prefix paths ("pp" for short) for the common paths that will be constructed
  from the XIM content. All have a trailing "/" character for convenience and
  consistency. These prefixes must match those defined in the configuration
  file for the path resolvers.
  -->
  <xsl:variable name="si-pp" select="'/data/si/'"/>
  <xsl:variable name="param-pp" select="'/data/param/'"/>

  <!--
  Handles domain-based fields by passing the details on for resolution to a
  domain renderer plug-in at run-time.
  -->
  
  <xsl:template match="FIELD[@DOMAIN]" mode="output-ng-field">
    <xsl:param name="editable-list"/>
    <xsl:variable name="domain">
      <xsl:choose>
        <xsl:when test="@EXTENDED_DOMAIN">
          <xsl:value-of select="@EXTENDED_DOMAIN"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@DOMAIN"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <cing:field domain="{$domain}">
      <xsl:apply-templates mode="add-path-attributes-from-si"
        select="@TARGET_BEAN | @SOURCE_BEAN | @INITIAL_BEAN"/>
      <xsl:apply-templates select="." mode="add-derived-attributes"/>
      <xsl:if test="$editable-list = 'true'">
         <cing:param name="CHECKBOX_CONTROLLED_EDITABLE_LIST" value="true"/>
      </xsl:if>    
      <xsl:apply-templates mode="add-basic-parameter"
        select="@CONFIG | @MANDATORY | @USE_DEFAULT | @WIDTH_UNITS | @CONTROL
                | @CONTROL_REF | @HEIGHT | @INITIAL_FOCUS | @PROMPT | @USE_BLANK"/>
      <xsl:apply-templates select="." mode="add-derived-parameters"/>
      <xsl:apply-templates select="SCRIPT" mode="add-script-parameters"/>
    </cing:field>
  </xsl:template>

  <!--
  Handles non-domain-based fields: either retrieving a parameter value or
  displaying a literal value.
  -->
  <xsl:template match="FIELD" mode="output-ng-field">
    <xsl:choose>
      <xsl:when test="@PARAMETER_NAME">
        <cing:get xsl:use-attribute-sets="source-path-from-param"/>
      </xsl:when>
      <xsl:when test="@VALUE">
        <xsl:value-of select="@VALUE"/>
      </xsl:when>
      <xsl:otherwise>&amp;nbsp;</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
  Adds a basic parameter to the field. Some parameters are only added if the
  attribute does not have the recognized default value. This filtering saves
  a lot of space in the JSP.
  -->
  <xsl:template match="FIELD/@*" mode="add-basic-parameter">
    <!-- Attributes without default values. -->
    <cing:param name="{name(.)}" value="{.}"/>
  </xsl:template>

  <xsl:template
      match="FIELD/@MANDATORY | FIELD/@USE_BLANK | FIELD/@INITIAL_FOCUS"
      mode="add-basic-parameter">
    <!-- Attributes whose default is "false". -->
    <xsl:if test="not(. = 'false')">
      <cing:param name="{name(.)}" value="{.}"/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="FIELD/@USE_DEFAULT" mode="add-basic-parameter">
    <!-- Attributes whose default is "true". -->
    <xsl:if test="not(. = 'true')">
      <cing:param name="{name(.)}" value="{.}"/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="FIELD/@CONTROL" mode="add-basic-parameter">
    <!-- Attributes whose default is "DEFAULT". -->
    <xsl:if test="not(. = 'DEFAULT')">
      <cing:param name="{name(.)}" value="{.}"/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="FIELD/@WIDTH_UNITS" mode="add-basic-parameter">
    <!-- Attributes whose default is "PERCENT". -->
    <xsl:if test="not(. = 'PERCENT')">
      <cing:param name="{name(.)}" value="{.}"/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="FIELD/@HEIGHT" mode="add-basic-parameter">
    <!-- Attributes whose default is "1". -->
    <xsl:if test="not(. = '1')">
      <cing:param name="{name(.)}" value="{.}"/>
    </xsl:if>
  </xsl:template>

  <!--
  Adds a derived parameters to the field. Only add the parameter if it has
  a value other than the appropriate default.
  -->
  <xsl:template match="FIELD" mode="add-derived-parameters">
    <!-- Width has no default value, but the template might produce a blank. -->
    <xsl:variable name="width">
      <xsl:call-template name="current-width"/>
    </xsl:variable>
    <xsl:if test="$width and not($width = '')">
      <cing:param name="WIDTH" value="{$width}"/>
    </xsl:if>

    <!--
    The form of the label that is normally visible. Use the body content for
    this value.
    -->
    <xsl:variable name="label">
      <xsl:apply-templates select="." mode="get-label-field-prop"/>
    </xsl:variable>
    
    <!--
    An alternative form of the label suitable for a tooltip. Use the body
    content for this value. Only emit this if it is different from the LABEL
    parameter value.
    -->
    <xsl:variable name="tooltip">
      <xsl:apply-templates select="." mode="add-tooltip-text"/>
    </xsl:variable>
    <xsl:if test="$tooltip and not($tooltip = $label)
                  and not(normalize-space($tooltip) = '')">
      <cing:param name="TOOLTIP">
        <xsl:value-of select="$tooltip"/>
      </cing:param>
    </xsl:if>

    <!--
    Parameters used when filling in the body of a CLUSTER. XIM uses
    NUM_COLUMNS, but UIM uses NUM_COLS, so we use the UIM name for the
    parameter. The default values ("1" for columns and "LABEL" for layout
    order) are not emitted to save space.
    -->
    <xsl:if test="parent::CLUSTER_ROW">
      <xsl:if test="../../@NUM_COLUMNS and not(../../@NUM_COLUMNS = '1')">
        <cing:param name="NUM_COLS" value="{../../@NUM_COLUMNS}"/>
      </xsl:if>
      <xsl:if test="../../@LAYOUT_ORDER and not(../../@LAYOUT_ORDER = 'LABEL')">
        <cing:param name="LAYOUT_ORDER" value="{../../@LAYOUT_ORDER}"/>
      </xsl:if>
      <!--  Adding label width parameter to the field if:
            (1) The domain/base domain on the field is 
                'ADDRESS_DATA' (for addresses).
            (2) If the 'LABEL_WIDTH' attribute is present on the CLUSTER
            (3) If the 'LABEL_WIDTH' attribute is not the default value (50)-->
      <xsl:if test="../../@LABEL_WIDTH and not(../../@LABEL_WIDTH = '50')
         and (@DOMAIN = 'ADDRESS_DATA' or @BASE_DOMAIN='ADDRESS_DATA')">
        <cing:param name="LABEL_WIDTH" value="{../../@LABEL_WIDTH}"/>
      </xsl:if>
    </xsl:if>

    <!--
    Parameter used for "sourced lists". Will become a plug-in configuration
    property at a later time. For efficiency, it is only set if @INITIAL_BEAN
    is set: all "sourced lists" require that attribute, so there is no point
    accessing "curam-config.xml" unless the result will be used.
    -->
    <xsl:variable name="domain">
      <xsl:choose>
        <xsl:when test="@EXTENDED_DOMAIN">
          <xsl:value-of select="@EXTENDED_DOMAIN"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@DOMAIN"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:if test="@INITIAL_BEAN and @INITIAL_IS_LIST = 'true'
                  and $multiple-select-domain-names = $domain">
      <cing:param name="MULTIPLE_SELECT" value="true"/>
    </xsl:if>
  </xsl:template>

  <!--
  Adds a derived attributes to the field. Only add the attribute if it has
  a value other than the appropriate default. NB: this template must be invoked
  before any of child elements (e.g. cing:param element) are added to the field.
  TODO: move tooltip child parameter to attribute in the same way as label? 
  -->
  <xsl:template match="FIELD" mode="add-derived-attributes">
    <!--
    The form of the label that is normally visible. Use the body content for
    this value.
    -->
    <xsl:variable name="label">
      <xsl:choose>
            <xsl:when test="TITLE and count(TITLE/TITLE_ELEMENT) &gt; 0">
               <xsl:apply-templates select="." mode="get-label-text-for-element"/>
             </xsl:when>
             <xsl:otherwise>
                <xsl:apply-templates select="." mode="get-label-field-prop"/>
             </xsl:otherwise>
          </xsl:choose>   
    </xsl:variable>
    
    <xsl:if test="$label and not(normalize-space($label) = '')">
      <xsl:attribute name="label">
        <xsl:value-of select="$label"/>
      </xsl:attribute>
    </xsl:if>
    
    <xsl:if test="TITLE and count(TITLE/TITLE_ELEMENT) &gt; 0">
     <xsl:attribute name="labelProp">
        <xsl:apply-templates select="TITLE/TITLE_ELEMENT" mode="get-label-element-field-prop"/>
      </xsl:attribute>
    </xsl:if>

  </xsl:template>


  <!--
  Adds SCRIPT parameters to the field.
  -->
  <xsl:template match="FIELD/SCRIPT" mode="add-script-parameters">
    <!-- Convert the event name to upper-case. -->
    <xsl:variable name="event"
                  select="translate(@EVENT, 'abcdefghijklmnopqrstuvwxyz',
                                            'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    <!-- Becomes an attribute in HTML, so use the value attribute here. -->
    <cing:param name="{$event}_ACTION"
                value="{concat('return ', @ACTION, ';')}"/>
  </xsl:template>

  <!--
  Create a "targetPath" attribute from "TARGET_BEAN" and "TARGET_FIELD"
  attributes. A target cannot be a list.
  -->
  <xsl:template match="FIELD/@TARGET_BEAN" mode="add-path-attributes-from-si">
    <xsl:attribute name="targetPath">
      <xsl:value-of select="concat($si-pp, ., '/', ../@TARGET_FIELD)"/>
       <!-- Fields with extended paths may already have an empty predicate. -->
      <xsl:if test="../@TARGET_IS_LIST = 'true'
                    and not(contains(@TARGET_FIELD, '[]'))">
        <xsl:text>[]</xsl:text>
      </xsl:if>
    </xsl:attribute>
  </xsl:template>

  <!--
  Create a "sourcePath" attribute and possibly a "sourceAliasPath" attribute
  from "SOURCE_BEAN", "SOURCE_FIELD", "INITIAL_BEAN" and "INITIAL_FIELD"
  attributes. The INITIAL_FIELD cannot be a list and INITIAL_HIDDEN_FIELD must
  not be set.
  -->
  <xsl:template match="FIELD/@SOURCE_BEAN" mode="add-path-attributes-from-si">
    <xsl:attribute name="sourcePath">
      <xsl:value-of select="concat($si-pp, ., '/', ../@SOURCE_FIELD)"/>
      <!-- Fields with extended paths may already have an empty predicate. -->
      <xsl:if test="../@SOURCE_IS_LIST = 'true'
                    and not(contains(@SOURCE_FIELD, '[]'))">
        <xsl:text>[]</xsl:text>
      </xsl:if>
    </xsl:attribute>
    <xsl:if test="../@INITIAL_BEAN and not(../@INITIAL_IS_LIST = 'true')
                  and not(../@INITIAL_HIDDEN_FIELD)">
      <xsl:attribute name="sourceAliasPath">
        <xsl:value-of
            select="concat($si-pp, ../@INITIAL_BEAN, '/', ../@INITIAL_FIELD)"/>
      </xsl:attribute>
    </xsl:if>
  </xsl:template>

  <!--
  Create an "optionsPath" attribute and possibly an "optionsAliasPath"
  attribute from "INITIAL_BEAN", "INITIAL_FIELD" and "INITIAL_HIDDEN_FIELD"
  attributes. This is only applicable when the connection is to a list.
  -->
  <xsl:template match="FIELD/@INITIAL_BEAN" mode="add-path-attributes-from-si">
    <xsl:if test="../@INITIAL_IS_LIST = 'true'">
      <xsl:choose>
        <xsl:when test="../@INITIAL_HIDDEN_FIELD">
          <xsl:attribute name="optionsPath">
            <!-- No support yet for extended paths with hidden fields. -->
            <xsl:value-of
              select="concat($si-pp, ., '/', ../@INITIAL_HIDDEN_FIELD, '[]')"/>
          </xsl:attribute>
          <xsl:attribute name="optionsAliasPath">
            <xsl:value-of select="concat($si-pp, ., '/', ../@INITIAL_FIELD)"/>
            <xsl:if test="not(contains(@INITIAL_FIELD, '[]'))">
              <xsl:text>[]</xsl:text>
            </xsl:if>
          </xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="optionsPath">
            <xsl:value-of select="concat($si-pp, ., '/', ../@INITIAL_FIELD)"/>
            <xsl:if test="not(contains(@INITIAL_FIELD, '[]'))">
              <xsl:text>[]</xsl:text>
            </xsl:if>
          </xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <!--
  Create a "sourcePath" attribute from a "PARAMETER_NAME" attribute.
  -->
  <xsl:attribute-set name="source-path-from-param">
    <xsl:attribute name="sourcePath">
      <xsl:value-of select="concat($param-pp, @PARAMETER_NAME)"/>
    </xsl:attribute>
  </xsl:attribute-set>

</xsl:stylesheet>
