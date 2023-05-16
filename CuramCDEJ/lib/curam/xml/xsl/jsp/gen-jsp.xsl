<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2021. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:curam="http://www.curamsoftware.com/curam"
  xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:cing="http://www.curamsoftware.com/curam/jde/client/curam-ng">

  <xsl:output method="xml" indent="no" omit-xml-declaration="no" />

  <xsl:strip-space elements="*" />

  <!--
    The XML data processed by this stylesheet is in XIM format. However,
    the stylesheet may be presented with a single XIM document with a
    "PAGE" root element or multiple XIM documents within a "_PAGES" root
    element containing multiple "PAGE" elements. In the latter case, the
    "jsp:root" elements created will be split into separate documents
    later. The consequences of this are that keys and XPath expressions
    must allow for the presence of other "PAGE" elements within the document.
  -->

  <xsl:param name="preview" />
  <xsl:param name="preview-fail-on-error" />
  <xsl:param name="package-prefix" />
  <xsl:param name="static-content-server-url" />
  <xsl:param name="help-node" />
  <xsl:param name="list-sort-anchor-spantext" />
  <xsl:param name="list-header-detailsrow-spantext" />
  <xsl:param name="list-header-actions-spantext" />
  <xsl:param name="list-header-singleselect-spantext" />
  <xsl:param name="curam-cluster-toggle-spantext" />
  <xsl:param name="page-style" />

  <xsl:variable name="image-domains"
    select="document('project:WebContent/WEB-INF/CDEJ/config/ImageMapConfig.xml')
              /map/domain/@name" />
  <xsl:variable name="curam-config"
    select="document('project:WebContent/WEB-INF/curam-config.xml')/APP_CONFIG"/>

  <xsl:variable name="error-page-id"
    select="$curam-config/ERROR_PAGE/@PAGE_ID" />

  <!--
  This feature is obsolete. Quickly disabled by ensuring comparison never matches.
  -->
  <xsl:variable name="is-client-validation"
    select="$curam-config/CLIENT_JAVASCRIPT_VALIDATIONS_XXDISABLEDXX = 'true'" />

  <xsl:variable name="clear-icon-multiple"
    select="$curam-config/MULTIPLE_POPUP_DOMAINS/CLEAR_TEXT_IMAGE" />
  <xsl:variable name="clear-icon-single"
    select="$curam-config/POPUP_PAGES/CLEAR_TEXT_IMAGE" />

  <xsl:variable name="pop-up-domains"
    select="$curam-config/POPUP_PAGES/POPUP_PAGE/DOMAIN" />
  <xsl:variable name="multiple-pop-up-domains"
    select="$curam-config/MULTIPLE_POPUP_DOMAINS/MULTIPLE_POPUP_DOMAIN/DOMAIN"/>
  <xsl:variable name="pop-up-domain-names"
    select="$pop-up-domains/text() | $multiple-pop-up-domains/text()" />
  <xsl:variable name="multiple-select-domain-names"
    select="$curam-config/MULTIPLE_SELECT/DOMAIN[@MULTIPLE = 'true']/@NAME" />
  <xsl:variable name="v6-theme-enabled" select="true()" />

  <!--
  This was to disable action set images for the "classic" theme and was set from
  ENABLE_ACTION_SET_IMAGES in curam-config.xml. That setting is now "deprecated"
  for V6. For now, we explicitly set it to "false". Eventually this code will
  have to be removed.
  -->
  <xsl:variable name="action-set-images-enabled" select="'false'" />

  <xsl:variable name="collapsible-clusters-enabled">
    <xsl:choose>
      <!--
        When the configuration setting has not been specified, the default is
        to have collapsible clusters enabled.
      -->
      <xsl:when test="not($curam-config/ENABLE_COLLAPSIBLE_CLUSTERS)">
        <xsl:text>true</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$curam-config/ENABLE_COLLAPSIBLE_CLUSTERS" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="colon">
    <xsl:if test="$curam-config/APPEND_COLON = 'true'">
      <xsl:text>:</xsl:text>
    </xsl:if>
  </xsl:variable>

  <!-- Special page style names used by this stylesheet. -->
  <xsl:variable name="duim-host-style"
                select="'curam-util-client::dynamic-uim-host'"/>
  <xsl:variable name="omit-sidebar-style" select="'page-omit-sidebar'"/>

  <!--
    Key used to improve performance when finding duplicate targets. This
    is specific to the PAGE ancestor, so its ID is used as part of the
    key string.
  -->
  <xsl:key name="all-by-target" match="FIELD | WIDGET_PARAMETER"
    use="concat(ancestor::PAGE/@PAGE_ID, '.', @TARGET_BEAN,
                       '.', @TARGET_FIELD)" />

  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-previews.xsl" />
  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-fileedit-accessor.xsl" />
  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-frameset.xsl" />
  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-ng-fields.xsl" />
  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-fragment-page.xsl" />
  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-jsp-external.xsl" />
  <xsl:include href="cdej:lib/curam/xml/xsl/jsp/gen-jsp-carbon.xsl" />

  <!--
    Catch any "stray" elements or attributes to prevent unnoticed
    errors.
  -->
  <xsl:template match="*">
    <xsl:message terminate="no">
      <xsl:text>WARNING: Unmatched XIM element: </xsl:text>
      <xsl:value-of select="name()" />
    </xsl:message>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:message terminate="no">
      <xsl:text>WARNING: Unmatched XIM attribute: </xsl:text>
      <xsl:value-of select="concat(name(..), '/@', name())" />
    </xsl:message>
  </xsl:template>

  <!--
    Main entry point for processing the XIM document. The root element
    could be a "chunks" element that contains one or more "chunk"
    elements each containing a "PAGE" element, or the root could just be
    a "PAGE" element.
  -->
  <xsl:template match="/">
    <chunks>
      <xsl:apply-templates select="chunks/chunk/PAGE | PAGE" />
    </chunks>
  </xsl:template>

  <!--
    Each JSP page or HTML preview page is placed within a "chunk" for
    later determination of the files to be serialized. Additional chunks
    may be required for frame sets.
  -->
  <xsl:template match="PAGE">
    <xsl:variable name="is-fragment-page"
      select="@COMPONENT_STYLE = 'PAGE_FRAGMENT'"/>
    <xsl:variable name="is-data-service-request"
      select="@COMPONENT_STYLE = 'DATASERVICE'"/>
    <xsl:choose>
      <xsl:when test="$preview = 'true'">
        <chunk type="preview" name="{@PAGE_ID}">
          <xsl:apply-templates select="DISPLAY" mode="preview" />
        </chunk>
      </xsl:when>
      <xsl:otherwise>
        <!-- Generate the frameset chunks if needed. -->
        <xsl:if test="@TYPE = 'SPLIT_WINDOW'">
          <xsl:choose>
            <xsl:when test="INFO/HAS_WIZARD_FRAMESET">
              <xsl:apply-templates select="."
                mode="frameset-wizard" />
            </xsl:when>
            <xsl:when test="INFO/HAS_TREE_FRAMESET">
              <xsl:apply-templates select="."
                mode="frameset-treecontrol" />
            </xsl:when>
          </xsl:choose>
        </xsl:if>
        <xsl:choose>
          <xsl:when test="$is-data-service-request">
            <chunk type="jsp" name="{@PAGE_ID}">
              <xsl:apply-templates select="DISPLAY" mode="data-service" />
            </chunk>
          </xsl:when>
          <xsl:when test="$is-fragment-page">
            <chunk type="jsp" name="{@PAGE_ID}">
              <xsl:apply-templates select="DISPLAY" mode="fragment-page" />
            </chunk>
          </xsl:when>
          <xsl:when test="INFO/HAS_FILE_EDIT">
            <xsl:apply-templates select="." mode="file-edit-control"/>
            <chunk type="jsp" name="{@PAGE_ID}">
              <xsl:apply-templates select="." mode="file-edit-base"/>
            </chunk>
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="$page-style = 'v7style'">
                <xsl:variable name="filename">
                  <xsl:value-of select="concat(@PAGE_ID, '-', $page-style)" />
                </xsl:variable>
                <chunk type="jsp" name="{$filename}">
                  <xsl:apply-templates select="DISPLAY" mode="main" />
                </chunk>
              </xsl:when>
              <xsl:otherwise>
                <xsl:variable name="filename">
                  <xsl:value-of select="@PAGE_ID" />
                </xsl:variable>
                <chunk type="jsp" name="{$filename}">
                  <xsl:apply-templates select="DISPLAY" mode="main" />
                </chunk>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Create a JSP page. -->
  <xsl:template match="DISPLAY" mode="main">
    <xsl:variable name="static-page-id" select="ancestor::PAGE/@PAGE_ID"/>
    <xsl:variable name="page-locale" select="../@LOCALE" />
    <xsl:variable name="is-error-page"
        select="$static-page-id = $error-page-id"/>
    <xsl:variable name="has-scriptlet-only"
        select="count(JSP_SCRIPTLET) = 1 and count(*) = 1" />
    <xsl:variable name="is-duim-host-page"
        select="ancestor::PAGE/@COMPONENT_STYLE = $duim-host-style"/>

    <xsl:call-template name="output-copyright" />

    <jsp:root version="2.0">
      <xsl:apply-templates select="." mode="page-directive">
        <xsl:with-param name="page-locale" select="$page-locale" />
        <xsl:with-param name="is-error-page" select="$is-error-page" />
      </xsl:apply-templates>

      <jsp:output omit-xml-declaration="yes" />

      <xsl:choose>
        <xsl:when test="not($has-scriptlet-only)">
          <c:set var="isScriptlet" value="false" scope="page" />
        </xsl:when>
        <xsl:otherwise>
          <c:set var="isScriptlet" value="true" scope="page" />
        </xsl:otherwise>
      </xsl:choose>

      <xsl:if test="$is-duim-host-page">
        <jsp:scriptlet>
          <xsl:text>pageContext.setAttribute("isDUIMHost", "true");</xsl:text>
        </jsp:scriptlet>
      </xsl:if>

      <!--
      The "pageId" request attribute will be set on return from this template.
      -->
      <xsl:apply-templates select="." mode="user-preferences">
        <xsl:with-param name="page-locale" select="$page-locale" />
      </xsl:apply-templates>
      <jsp:scriptlet>
          curam.util.common.useragent.UserAgent ua =
          new curam.util.common.useragent.UserAgent(request.getHeader("User-Agent"));
          request.getSession().setAttribute("ua-session-key", ua);
          pageContext.setAttribute("mobileUserAgent", String.valueOf(ua.isMobileBrowser()));

          pageContext.setAttribute("isDesktop", String.valueOf(!ua.isMobileBrowser()));

          if ("true".equals(pageContext.getAttribute("mobileUserAgent"))){
            pageContext.setAttribute("mobileClassName", "mobile");
            pageContext.setAttribute("scrollFunctionIos", "curam.util.removeTopScrollForIos();");
          }else{
            pageContext.setAttribute("scrollFunctionIos", "");
          }
          curam.omega3.user.UserPreferences prefs
            = curam.omega3.user.UserPreferencesFactory.getUserPreferences(session);
      </jsp:scriptlet>
      <jsp:scriptlet>
        <xsl:text>curam.util.client.jsp.JspUtil.initializeUIM(</xsl:text>
        <xsl:text>pageContext,(String)request.getAttribute("pageId"),</xsl:text>
        <xsl:text>(String)request.getAttribute("__o3WindowOptions"),</xsl:text>
        <xsl:text>(Boolean)request.getAttribute("__o3InPageNavigation")</xsl:text>
        <xsl:text>);</xsl:text>
        curam.omega3.taglib.ScreenContext screenContext =
          (curam.omega3.taglib.ScreenContext) pageContext.getAttribute(
            curam.omega3.taglib.ScreenContext.CTX_ATTR);

      <!--
	    If the page has included the atributte "invalidateSession" as true
	    the session is invalidated.
      -->
      if ("true".equals(request.getParameter("invalidateSession"))) {
          request.getSession().invalidate();
      }

      </jsp:scriptlet>
      <c:choose>
        <!--
        Special response from non-"resolve" pages when "o3resolve" is set. When
        the page is a Dynamic UIM host page, the reported page ID is not the ID
        of the host page, but the ID of the Dynamic UIM page.
        -->
        <c:when test="${{param.o3resolve=='true' and !isScriptlet}}">
          <xsl:text>{"pageID": "${requestScope.pageId}", "pageURL": "</xsl:text>
          <jsp:scriptlet>
            out.write(curam.omega3.request.RequestHandlerFactory
                         .getRequestHandler(request).getEncodedURL(false));
          </jsp:scriptlet>
          <xsl:text>"}</xsl:text>
        </c:when>
        <c:otherwise>
          <xsl:if test="not($has-scriptlet-only)">
            <xsl:text>&lt;!DOCTYPE html&gt;</xsl:text>
          </xsl:if>

          <xsl:if test="../ACTION/ACTION_CONTROL/LINK/@PAGE_ID = 'THIS'">
            <xsl:apply-templates select="../ACTION/SERVER_INTERFACE"/>
          </xsl:if>

          <!-- do not add action phase interfaces to the display phase -->
          <xsl:apply-templates select="SERVER_INTERFACE[not(@PHASE='ACTION')]"/>
          <xsl:apply-templates
            select="BEAN_SET_FIELD[not(@TARGET_BEAN = 'BROWSER') and not(@TARGET_BEAN = 'JSCRIPT_REF')]" mode="usual"/>

          <xsl:if test="not($is-error-page) and not($has-scriptlet-only)">
            <!--
            Do not clear messages before server call on error or redirect page.

            Custom tag that clears session scope messages for the display phase.
            Unfortunately this cannot be done by the "callServer" tag because it
            is possible to have multiple display phase calls and messages for
            each of them must be kept. Also, a lot of pages have no display
            phase calls and the messages must still be cleared, hence the need
            for a new custom tag.
            -->
            <curam:removeMessages/>
          </xsl:if>

          <xsl:apply-templates select="CALL_SERVER"/>
          <xsl:if test="$is-duim-host-page">
            <cing:page pageID="${{requestScope.pageId}}" noScripts="true">
              <cing:component style="curam-util-client::duim-si-content"/>
            </cing:page>
          </xsl:if>
          <xsl:apply-templates select="INFORMATIONAL"/>
          <xsl:apply-templates select="." mode="init-frames"/>

          <xsl:choose>
            <xsl:when test="$has-scriptlet-only">
              <xsl:apply-templates select="JSP_SCRIPTLET"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$page-style = 'parent'">
                  <xsl:apply-templates select="." mode="parent">
                    <xsl:with-param name="page-locale" select="$page-locale"/>
                    <xsl:with-param name="is-error-page" select="$is-error-page"/>
                    <xsl:with-param name="static-page-id" select="$static-page-id"/>
                  </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$page-style = 'v7style'">
                  <xsl:apply-templates select="." mode="v7style">
                    <xsl:with-param name="page-locale" select="$page-locale"/>
                    <xsl:with-param name="is-error-page" select="$is-error-page"/>
                    <xsl:with-param name="static-page-id" select="$static-page-id"/>
                  </xsl:apply-templates>
                </xsl:when>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </c:otherwise>
      </c:choose>
    </jsp:root>
  </xsl:template>

  <xsl:template match="DISPLAY" mode="parent">
    <xsl:param name="page-locale"/>
    <xsl:param name="is-error-page"/>
    <xsl:param name="static-page-id"/>

    <html lang="${{htmlLanguage}}" dir="${{htmlDirection}}" class="${{htmlDirection}}" role="region" aria-label="${{landmarkLabel}}">

      <jsp:scriptlet>if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.EXTAPP)) {</jsp:scriptlet>
        <jsp:include page="{$static-page-id}-v7style.jspx"/>
      <jsp:scriptlet>} else {</jsp:scriptlet>

        <xsl:apply-templates select="." mode="carbon--html-head">
          <xsl:with-param name="page-locale" select="$page-locale"/>
        </xsl:apply-templates>
        <xsl:apply-templates select="." mode="carbon--html-body">
          <xsl:with-param name="is-error-page" select="$is-error-page"/>
        </xsl:apply-templates>

      <jsp:scriptlet>}</jsp:scriptlet>

    </html>
  </xsl:template>

  <!--
    Creates the v7 styling templates. This method, along with the 'if' branch above can be removed once we migrate the external pages over to v8 carbon styling.
  -->
  <xsl:template match="DISPLAY" mode="v7style">
    <xsl:param name="page-locale"/>
    <xsl:param name="is-error-page"/>
    <xsl:param name="static-page-id"/>

    <xsl:apply-templates select="." mode="html-head">
      <xsl:with-param name="page-locale" select="$page-locale"/>
    </xsl:apply-templates>
    <xsl:apply-templates select="." mode="html-body">
      <xsl:with-param name="is-error-page" select="$is-error-page"/>
    </xsl:apply-templates>

  </xsl:template>

</xsl:stylesheet>
