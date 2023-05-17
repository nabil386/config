<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/wrapper">
    <APP_CONFIG>
      <xsl:call-template name="multiple-popup-domains"/>
      <xsl:call-template name="popup_pages"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/HELP)[1]"/>
      <xsl:call-template name="multiple_select"/>
      <xsl:call-template name="file_download_config"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/APPEND_COLON)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/APPLICATION_MENU)[1]"/>
      <xsl:call-template name="address_config"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/CLIENT_JAVASCRIPT_VALIDATIONS)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/LANGUAGE_SELECTION_PANEL)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/ADMIN)[1]" />
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/STATIC_CONTENT_SERVER)[1]" />
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/FIELD_ERROR_INDICATOR)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/LIST_ROW_COUNT)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/SECURITY_CHECK_ON_PAGE_LOAD)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/ENABLE_SELECT_ALL_CHECKBOX)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/TRANSFER_LISTS_MODE)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/ENABLE_ACTION_SET_IMAGES)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/ENABLE_COLLAPSIBLE_CLUSTERS)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/APPLICATION_THEME)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/HIDE_CONDITIONAL_LINKS)[1]"/>
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/SCROLLBAR_CONFIG)[1]" />
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/CONTEXT_PANEL_SPLITTER)[1]" />
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/SMART_PANEL_SPLITTER)[1]" />
      <xsl:apply-templates select="(/wrapper/APP_CONFIG/DISABLE_AUTO_COMPLETE)[1]" />
      <xsl:call-template name="pagination-section"/>
      <xsl:call-template name="process-error-pages"/>
    </APP_CONFIG>
  </xsl:template>

  <xsl:template name="multiple-popup-domains">
    <xsl:if test="count(/wrapper/APP_CONFIG/MULTIPLE_POPUP_DOMAINS) &gt; 0">
      <MULTIPLE_POPUP_DOMAINS>
        <CLEAR_TEXT_IMAGE>
        <xsl:value-of select="(/wrapper/APP_CONFIG/MULTIPLE_POPUP_DOMAINS/CLEAR_TEXT_IMAGE)[1]"/>
        </CLEAR_TEXT_IMAGE>
        <xsl:apply-templates select="//MULTIPLE_POPUP_DOMAIN"/>
      </MULTIPLE_POPUP_DOMAINS>
    </xsl:if>
  </xsl:template>

  <xsl:template match="MULTIPLE_POPUP_DOMAIN">
    <xsl:if test="not(preceding::MULTIPLE_POPUP_DOMAIN[DOMAIN=current()/DOMAIN])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template name="popup_pages">
    <xsl:if test="count(/wrapper/APP_CONFIG/POPUP_PAGES) &gt; 0">
      <POPUP_PAGES>
        <xsl:if test="/wrapper/APP_CONFIG/POPUP_PAGES/@DISPLAY_IMAGES">
          <xsl:attribute name="DISPLAY_IMAGES">
            <xsl:value-of select="(/wrapper/APP_CONFIG/POPUP_PAGES/@DISPLAY_IMAGES)[1]"/>
          </xsl:attribute>
        </xsl:if>
        <CLEAR_TEXT_IMAGE>
          <xsl:value-of select="(/wrapper/APP_CONFIG/POPUP_PAGES/CLEAR_TEXT_IMAGE)[1]"/>
        </CLEAR_TEXT_IMAGE>
        <xsl:apply-templates select="//POPUP_PAGE"/>
      </POPUP_PAGES>
    </xsl:if>
  </xsl:template>

  <xsl:template match="POPUP_PAGE">
    <xsl:if test="not(preceding::POPUP_PAGE[@PAGE_ID=current()/@PAGE_ID])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template name="multiple_select">
    <xsl:if test="count(/wrapper/APP_CONFIG/MULTIPLE_SELECT) &gt; 0">
      <MULTIPLE_SELECT>
        <xsl:apply-templates select="//APP_CONFIG/MULTIPLE_SELECT/DOMAIN"/>
      </MULTIPLE_SELECT>
    </xsl:if>
  </xsl:template>
  <xsl:template match="//APP_CONFIG/MULTIPLE_SELECT/DOMAIN">
    <xsl:if test="not(preceding::DOMAIN[@NAME=current()/@NAME])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template name="file_download_config">
    <xsl:if test="count(/wrapper/APP_CONFIG/FILE_DOWNLOAD_CONFIG) &gt; 0">
      <FILE_DOWNLOAD_CONFIG>
        <xsl:apply-templates select="//FILE_DOWNLOAD"/>
      </FILE_DOWNLOAD_CONFIG>
    </xsl:if>
  </xsl:template>
  <xsl:template match="FILE_DOWNLOAD">
    <xsl:if test="not(preceding::FILE_DOWNLOAD[@PAGE_ID=current()/@PAGE_ID])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template name="address_config">
    <xsl:if test="count(/wrapper/APP_CONFIG/ADDRESS_CONFIG) &gt; 0">
      <!--<ADDRESS_CONFIG DEFAULT_FORMAT="{(/wrapper/APP_CONFIG/ADDRESS_CONFIG[@DEFAULT_FORMAT])[1]/@DEFAULT_FORMAT}" DEFAULT_COUNTRY_CODE="{(/wrapper/APP_CONFIG/ADDRESS_CONFIG[@DEFAULT_COUNTRY_CODE])[1]/@DEFAULT_COUNTRY_CODE}">-->
      <ADDRESS_CONFIG>
        <xsl:apply-templates select="//LOCALE_MAPPING"/>
        <xsl:apply-templates select="//ADDRESS_FORMAT"/>
      </ADDRESS_CONFIG>
    </xsl:if>
  </xsl:template>

  <xsl:template match="LOCALE_MAPPING">
    <xsl:if test="not(preceding::LOCALE_MAPPING[@LOCALE=current()/@LOCALE])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ADDRESS_FORMAT">
    <xsl:if test="not(preceding::ADDRESS_FORMAT[@NAME=current()/@NAME])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="HELP | ERROR_PAGE | APPEND_COLON | APPLICATION_MENU | CLIENT_JAVASCRIPT_VALIDATIONS | ADMIN | STATIC_CONTENT_SERVER | FIELD_ERROR_INDICATOR | LANGUAGE_SELECTION_PANEL | LIST_ROW_COUNT | SECURITY_CHECK_ON_PAGE_LOAD | ENABLE_SELECT_ALL_CHECKBOX | TRANSFER_LISTS_MODE  | ENABLE_ACTION_SET_IMAGES | ENABLE_COLLAPSIBLE_CLUSTERS | APPLICATION_THEME | HIDE_CONDITIONAL_LINKS | SCROLLBAR_CONFIG | CONTEXT_PANEL_SPLITTER | SMART_PANEL_SPLITTER | DISABLE_AUTO_COMPLETE">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template name="process-error-pages">
    <xsl:apply-templates select="/wrapper/APP_CONFIG/ERROR_PAGE"/>
  </xsl:template>

  <xsl:template match="ERROR_PAGE">
    <!-- Output if current node has not type and no preceding nodes had no type. -->
    <xsl:if test="current()[not(@TYPE)] and not(preceding::ERROR_PAGE[not(@TYPE)])">
      <xsl:copy-of select="."/>
    </xsl:if>
    <!-- Output if current node has a type and its not equal to a preceding node type. -->
    <xsl:if test="current()[@TYPE] and not(preceding::ERROR_PAGE[@TYPE=current()/@TYPE])">
      <xsl:copy-of select="."/>
    </xsl:if>
  </xsl:template>
  
<xsl:template name="pagination-section">
  <xsl:if test="count(/wrapper/APP_CONFIG/PAGINATION) &gt; 0">
    <!-- nothing in the output if no component defines this section, defaults will be used -->
    <xsl:choose>
      <xsl:when test="string(/wrapper/APP_CONFIG/PAGINATION[1]/@ENABLED) = 'true'">
        <PAGINATION ENABLED="true">      
        <!-- collect elements -->
          <DEFAULT_PAGE_SIZE>
          <xsl:choose>
            <xsl:when test="count(/wrapper/APP_CONFIG/PAGINATION/DEFAULT_PAGE_SIZE) &gt; 0">
              <xsl:value-of select="/wrapper/APP_CONFIG/PAGINATION/DEFAULT_PAGE_SIZE[1]"/>
            </xsl:when>
            <xsl:otherwise>15</xsl:otherwise>
          </xsl:choose>
          </DEFAULT_PAGE_SIZE>
          <PAGINATION_THRESHOLD>
          <xsl:choose>
            <xsl:when test="count(/wrapper/APP_CONFIG/PAGINATION/PAGINATION_THRESHOLD) &gt; 0">
              <xsl:value-of select="/wrapper/APP_CONFIG/PAGINATION/PAGINATION_THRESHOLD[1]"/>
            </xsl:when>
            <xsl:otherwise>
              <!-- no independent setting for threshold, take either page size if defined, or default -->
              <xsl:choose>
                <xsl:when test="count(/wrapper/APP_CONFIG/PAGINATION/DEFAULT_PAGE_SIZE) &gt; 0">
                  <xsl:value-of select="/wrapper/APP_CONFIG/PAGINATION/DEFAULT_PAGE_SIZE[1]"/>
                </xsl:when>
                <xsl:otherwise>15</xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
          </PAGINATION_THRESHOLD>
        </PAGINATION>      
      </xsl:when>
      <xsl:otherwise>
      <!-- enabled was set to false -->
        <PAGINATION ENABLED="false"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:if>
</xsl:template>
</xsl:stylesheet>