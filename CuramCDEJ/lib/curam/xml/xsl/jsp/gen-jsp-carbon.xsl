<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2021,2023. All rights reserved.

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

  <!-- Generate the HTML "head" tag and its content. -->
  <xsl:template match="DISPLAY" mode="carbon--html-head">
    <xsl:param name="page-locale"/>

    <jsp:scriptlet>{pageContext.setAttribute("dojoConfig",
          curam.util.client.jsp.JspUtil.getDojoConfig(prefs.getLocale()));}
    </jsp:scriptlet>

    <xsl:if test="$v6-theme-enabled">
      <jsp:scriptlet>
        pageContext.setAttribute("theme", "v6", pageContext.APPLICATION_SCOPE);
      </jsp:scriptlet>
    </xsl:if>

    <head>
      <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
      <meta name="GENERATOR" content="Curam Software Ltd. Curam CDEJ"/>
      <xsl:if test="../INFO/HAS_BAR_CHART">
        <meta http-equiv="imagetoolbar" content="no"/>
      </xsl:if>

      <!-- including all CSS files on page -->
      <jsp:include page="../../css-file-links.jsp" />
      <jsp:scriptlet>
        if(pageContext.getAttribute("htmlDirection").equals("rtl")) {
          </jsp:scriptlet><jsp:include page="../../css-file-links_rtl.jsp"/><jsp:scriptlet>
        }
      </jsp:scriptlet>

      <!-- TODO: CSS Printing -->
      <!--  <link rel="stylesheet" type="text/css" media="print"
            href="{$static-content-server-url}/themes/classic/css/curam_print.css"/>
      <link rel="stylesheet" type="text/css" media="print"
            href="{$static-content-server-url}/CDEJ/css/custom_print.css"/>
      -->

      <link rel="shortcut icon"
            href="{concat($static-content-server-url,
                          '/themes/classic/images/icons/curam.ico')}"/>
      <link rel="icon"
            href="{concat($static-content-server-url,
                          '/themes/classic/images/icons/curam.ico')}"/>

      <xsl:apply-templates select="BEAN_SET_FIELD[@TARGET_BEAN = 'BROWSER']"
                           mode="browser"/>
      <xsl:apply-templates select="BEAN_SET_FIELD[@TARGET_BEAN = 'JSCRIPT_REF']"
                           mode="nsbrowser"/>

      <script type="text/javascript">
      <jsp:scriptlet>
        <xsl:text>curam.util.client.jsp.JspUtil.outputDojoLoaderConfiguration(pageContext, prefs.getLocale());</xsl:text>
      </jsp:scriptlet>
      </script>
       <script type="text/javascript" src="/CDEJ/jscript/dojotk/dojo/dojo.js">// dummy script content</script>
      <script type="text/javascript" src="/CDEJ/jscript/dojo.layer.js">// dummy script content</script>
      <script type="text/javascript">
        <xsl:text>var jsPageID="${requestScope.pageId}";</xsl:text>
        require.on("error", function(error) {
          console.log(error.src, error.id);
        });
      </script>
      <jsp:scriptlet>
        <xsl:text>curam.util.client.jsp.JspUtil.outputJSLocalisedValues(pageContext);</xsl:text>
      </jsp:scriptlet>
      <script type="text/javascript" src="/CDEJ/jscript/cdej-cm.js">// dummy script content</script>
      <script type="text/javascript" src="/CDEJ/jscript/appvars.js">// dummy script content</script>
      <script type="text/javascript" src="/CDEJ/jscript/cdej.js">// dummy script content</script>
      <script type="text/javascript" src="/CDEJ/jscript/cdsl.js">// dummy script content</script>

      <script type="text/javascript">
        /* Load the modules needed by the page itself plus preload some more.
           This is to avoid referencing non-AMD modules which causes poor
           performance */
        require(["curam/core-uim"]);
      </script>

       <curam:jsUserPreferences/>

      <script type="text/javascript">require(["curam/util"], function(){curam.util.redirectDirectUrl();});</script>
      <script type="text/javascript" src="/CDEJ/jscript/popup-config.js">// dummy script content</script>
      <xsl:choose>
        <xsl:when test="../INFO/HAS_TREE_FRAMESET">
          <script type="text/javascript" src="/CDEJ/jscript/curam/widget/orgTree/dtree.js">// dummy script content</script>
          <link rel="stylesheet" type="text/css" media="all"
                href="../CDEJ/jscript/dTree/dtree.css"/>
        </xsl:when>
        <xsl:otherwise>
          <jsp:scriptlet>
            if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TREE)) {
          </jsp:scriptlet>
         <script type="text/javascript">dojo.addOnLoad(function(){ require(["curam/widget/orgTree/treecontent"], function(tc){ tc.redrawTreeContents();});});</script>
          <jsp:scriptlet>}</jsp:scriptlet>
        </xsl:otherwise>
      </xsl:choose>
      <script type="text/javascript">
      <!--
      NB: The include of curam/dialog must be before the call to
      JspUtil.outputJavaScriptInitialization because that method outputs
      modal dialog initialization code.
      -->
      <jsp:scriptlet>if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)
                       || screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.POPUP)) {</jsp:scriptlet>
      <jsp:scriptlet>}</jsp:scriptlet>
        <jsp:scriptlet>
          <xsl:text>curam.util.client.jsp.JspUtil.outputJavaScriptInitialization(pageContext,</xsl:text>
          <xsl:text>(Boolean)request.getAttribute("__o3InPageNavigation"),</xsl:text>
             <xsl:value-of select="../@TYPE = 'DETAILS'"/>
          <xsl:text>);</xsl:text>
        </jsp:scriptlet>
      </script>
      <jsp:scriptlet>if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TAB)) {
      </jsp:scriptlet>
      <!-- If the UIM is opened in a TAB, then we have to "adapt" it to function
           in this context. The "curam.ui.UIMPageAdaptor" JavaScript object
           takes care of this.
        -->
      <script type="text/javascript">
        dojo.addOnLoad(function(){require(["curam/ui/UIMPageAdaptor"], function(){curam.ui.UIMPageAdaptor.initialize();});});
      </script>

      <jsp:scriptlet>}</jsp:scriptlet>
      <jsp:scriptlet>
          Boolean isExternalApp = false;
          if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.EXTAPP)){
             isExternalApp = true;
          }
          if (curam.util.client.tab.ApplicationConfigUtils.isGuardAgainstLeavingSet(isExternalApp)) {
      </jsp:scriptlet>
     <script type="text/javascript">
          require(["dojo/ready","curam/util/ui/AppExitConfirmation"], function(ready,appExit){
          ready(function(){
            appExit.install();

          });});
      </script>
       <jsp:scriptlet> } </jsp:scriptlet>


      <xsl:apply-templates select="." mode="add-js-widget-support"/>
      <xsl:call-template name="add-js-validation-support">
        <xsl:with-param name="page-locale" select="$page-locale"/>
      </xsl:call-template>
      <xsl:if test="../INFO/HAS_TREE_WIDGET">
        <script type="text/javascript" src="/CDEJ/jscript/tree.js">// dummy script content</script>
      </xsl:if>
      <xsl:if test="../INFO/HAS_WIZARD_FRAMESET or ../INFO/HAS_WIZARD_SUMMARY">
        <script type="text/javascript" src="/CDEJ/jscript/curam/widget/wizard/wizard.js">// dummy script content</script>
      </xsl:if>

      <!-- If there are expandable lists, setup the toggle handler. -->
      <xsl:if test="count(//DETAILS_ROW) &gt; 0">
        <script type="text/javascript">
        <xsl:text>require(["curam/widget/_TabButton", "curam/util/ExpandableLists"], function(){
          curam.util.ExpandableLists.setupToggleHandler();
        });</xsl:text>
        </script>
      </xsl:if>
      <!-- This is pulling in the new SPMUIComponents that is based on carbon. Dynamic UIM and CDAG pages should also pull in -->
      <script type="text/javascript" src="/CDEJ/jscript/SPMUIComponents/spm-uicomponents-main.bundle.js"> dummy script content</script>
      <!-- This is pulling in the custom bundle from external IBM repository - https://github.com/IBM/spm-ui-components -->
      <!-- This file not exist OOTB, only for custom pages. To remedy this the build scripts (cdejbuild.xml) insert a dummy file
           just to prevent log pollution -->
      <script type="text/javascript" src="/CDEJ/jscript/SPMUIComponents/spm-custom-carbon-addons-main.bundle.js"> dummy script content</script>
 
      <xsl:apply-templates select="." mode="include-uim-scripts"/>

      <!--
      The host page for a dynamic UIM page may have no title of its own, but
      the dynamic title place-holder still needs to be added, so simply applying
      templates to the "PAGE_TITLE" element is insufficient and a template needs
      to be called here.
      -->
      <xsl:call-template name="add-window-title"/>


    </head>
  </xsl:template>

  <!-- Generate the HTML "body" tag and its content. -->
  <xsl:template match="DISPLAY" mode="carbon--html-body">
    <xsl:param name="is-error-page"/>

    <xsl:variable name="action-control-exist"
                  select="ACTION_SET" />

    <xsl:variable name="not-in-cluster"
                  select="not(parent::ACTION_SET = CLUSTER)" />
    <xsl:variable name="not-in-list"
                  select="not(parent::ACTION_SET = LIST)" />
    <xsl:variable name="sidebar-omitted"
        select="parent::PAGE/@COMPONENT_STYLE = $omit-sidebar-style"/>

    <!--
    Redirect pages, those containing nothing but a single JSP_SCRIPTLET
    element, do not have a "body" and do not have a page ID. This
    characteristic is exploited when compensating for redirect pages in
    a modal dialog. The page ID on the body can also be used when
    customizing the CSS selectively for a page.
    -->

    <xsl:variable name="is-page-type-details"
      select="../@TYPE = 'DETAILS'"/>

    <xsl:variable name="is-duim-host-page"
      select="ancestor::PAGE/@COMPONENT_STYLE = $duim-host-style"/>

    <body id="Curam_${{requestScope.pageId}}"
          aria-label="${{landmarkLabel}}" onscroll="${{scrollFunctionIos}}">

      <xsl:attribute name="class">
        <!--
        NOTE: the htmlDirection variable will have a space in it if needed,
        otherwise it will be empty.
         -->
        <xsl:text>${appID} ${bClass} ${htmlLanguage} ${htmlDirection}</xsl:text>
        <xsl:if test="../INFO/HAS_ORG_TREE">
          <xsl:text> orgTreeBody</xsl:text>
        </xsl:if>
        <xsl:if test="$is-page-type-details">
        <xsl:text> details</xsl:text>
        </xsl:if>
        <xsl:if test="$sidebar-omitted">
          <xsl:text> no-sidebar</xsl:text>
        </xsl:if>
        <xsl:if test="not(../INFO/HAS_WIZARD_FRAMESET)
                      or not(../INFO/HAS_ORG_TREE)">
          <xsl:text> soria</xsl:text>
        </xsl:if>
        <!-- TODO: V8 toggle. Put this scoped class name on if toggle to switch to V8 is enabled e.g JSPUtils -->
        <xsl:text> spm-component</xsl:text>
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="../INFO/HAS_WIZARD_FRAMESET">
          <xsl:attribute name="role">
            <xsl:text>navigation</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="tabindex">
            <xsl:text>0</xsl:text>
          </xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="role">
            <xsl:text>document</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="tabindex">
            <xsl:text>-1</xsl:text>
          </xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>

      <xsl:if test="$is-page-type-details">
        <div id="tab-name" style="display:none">
          <xsl:apply-templates select="TAB_NAME" mode="html-page-title"/>
        </div>
        <div id="tab-title">
          <xsl:call-template name="add-window-title-content-only"/>
        </div>
      </xsl:if>

      <cing:page pageID="${{requestScope.pageId}}">

        <!--
        The SVG widgets do not work "reliably" in a Dojo BorderContainer. The
        only workaround is to disable these containers when an SVG widget exists
        on the page.
        -->
        <xsl:if test="../INFO/HAS_BASIC_SVG">
          <c:set var="page-has-svg-widget" value="true"/>
        </xsl:if>
        <xsl:if test="../INFO/HAS_WIZARD_FRAMESET">
          <c:set var="page-has-wizard" value="true"/>
        </xsl:if>
        <xsl:if test="not(../INFO/HAS_WIZARD_FRAMESET)">
          <xsl:call-template name="add-page-title"/>
        </xsl:if>
        <jsp:scriptlet>
          if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.LIST_ROW_INLINE_PAGE)) {
        </jsp:scriptlet>
          <div class="top">
            <div class="left"></div>
            <div class="center">
              <div class="right"></div>
            </div>
          </div>
        <jsp:scriptlet>}</jsp:scriptlet>


        <xsl:if test="$is-duim-host-page" >
            <cing:component
              style="curam-util-client::duim-wizard-progress-bar"/>
        </xsl:if>
        <xsl:if test="MENU[@MODE = 'WIZARD_PROGRESS_BAR']">
          <xsl:apply-templates
            select="MENU[@MODE = 'WIZARD_PROGRESS_BAR']"/>
        </xsl:if>

        <div>
          <xsl:attribute name="id">
            <xsl:choose>
              <xsl:when test="../INFO/HAS_WIZARD_FRAMESET">
                <xsl:text>wizard-content</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>content</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
          <xsl:attribute name="class">
            <xsl:text>title-exists </xsl:text>
            <xsl:text>bx--page__content </xsl:text>
           <xsl:choose>
              <xsl:when test="PAGE_TITLE/@DESCRIPTION or PAGE_TITLE/DESCRIPTION
                              or ancestor::PAGE//FIELD/@TARGET_FIELD">
                <xsl:text> desc-exists </xsl:text>
              </xsl:when>
              <!--Dynamically adding the CSS class names for dynamic UIM pages
              now as opposed to hardcoding, so that they are added in the same
              way that it is added to static UIM pages. -->
              <xsl:when test="$is-duim-host-page">
                <xsl:text> ${duimDescClass} ${duimActionsetClass} ${duimWizardBar}</xsl:text>
              </xsl:when>
              <xsl:when test="../INFO/HAS_WIZARD_FRAMESET"> orgTreeBody</xsl:when>
            </xsl:choose>
            <xsl:if test="ACTION_SET">
              <xsl:text> action-exists</xsl:text>
            </xsl:if>
            <xsl:if test="MENU[@MODE = 'WIZARD_PROGRESS_BAR']">
              <xsl:text> wizard-exists</xsl:text>
            </xsl:if>
          </xsl:attribute>

          <jsp:scriptlet>if (screenContext.hasContextBits(
                                     curam.omega3.taglib.ScreenContext.TREE
                                   | curam.omega3.taglib.ScreenContext.ACTION)
                             &amp;&amp; !screenContext.hasContextBits(
                                      curam.omega3.taglib.ScreenContext.ERROR)
                             &amp;&amp; !screenContext.hasContextBits(
                                   curam.omega3.taglib.ScreenContext.RESOLVE)) {
          </jsp:scriptlet>
            <script id="rScript" type="text/javascript">
            <xsl:text>dojo.addOnLoad(function(){ require(["curam/widget/orgTree/treecontent", 'curam/util'], function(tc) { tc.refreshTree(); curam.util.fireRefreshTreeEvent(); })});</xsl:text>
            </script>
          <jsp:scriptlet>}</jsp:scriptlet>

          <script type="text/javascript">
            <xsl:text>var messageTitleAppend="</xsl:text>
            <xsl:text>${o3_messageTitleAppend}</xsl:text>
            <xsl:text>";</xsl:text>
          </script>

           <jsp:scriptlet>if (screenContext.hasContextBits(
                               curam.omega3.taglib.ScreenContext.EXTAPP)) {
          </jsp:scriptlet>
            <script type="text/javascript">
              <xsl:text>dojo.addOnLoad(function(){curam.util.addClassToLastNodeInContentArea();});</xsl:text>
            </script>
          <jsp:scriptlet>}</jsp:scriptlet>

          <xsl:if test="not(../INFO/HAS_WIZARD_FRAMESET)">
            <xsl:apply-templates select="." mode="display-messages">
              <xsl:with-param name="is-error-page" select="$is-error-page"/>
            </xsl:apply-templates>
          </xsl:if>

          <!-- In the External App's modal dialog, we need include the page
               description inside this content DIV to meet the new styling spec.
               So that the page description and the mandatory text will be part
               of the scrolling content when the scrollbar presents. At the
               same time, we will hide the normal page title by using
               "display:none" in CSS. -->
          <jsp:scriptlet>if (screenContext.hasContextBits(
                               curam.omega3.taglib.ScreenContext.EXTAPP)
                             &amp;&amp; screenContext.hasContextBits(
                               curam.omega3.taglib.ScreenContext.MODAL)) {
          </jsp:scriptlet>
            <xsl:if test="not(../INFO/HAS_WIZARD_FRAMESET)">
              <xsl:call-template name="add-page-description-for-external-app-modal"/>
            </xsl:if>
          <jsp:scriptlet>}</jsp:scriptlet>


          <xsl:choose>
            <xsl:when test="MENU[@MODE = 'IN_PAGE_NAVIGATION']">
              <script type="text/javascript"><jsp:text><![CDATA[
                require(["curam/widget/_TabButton"]);
              ]]></jsp:text></script>
              <xsl:apply-templates select="MENU[@MODE = 'IN_PAGE_NAVIGATION']"/>
              <div class="in-page-nav-contentWrapper">
                <xsl:apply-templates select="." mode="carbon--general"/>
              </div>
            </xsl:when>
            <xsl:when test="MENU[@MODE = 'INTEGRATED_CASE']">
              <curam:dynamicMenu type="INTEGRATED_CASE"
                                 sourceBean="{MENU/@SOURCE_BEAN}"
                                 sourceField="{MENU/@SOURCE_FIELD}"/>
              <div class="tab">
                <xsl:apply-templates select="." mode="carbon--general"/>
              </div>
            </xsl:when>
            <xsl:when test="MENU[@MODE = 'DYNAMIC']">
              <curam:dynamicMenu type="DYNAMIC"
                                 sourceBean="{MENU/@SOURCE_BEAN}"
                                 sourceField="{MENU/@SOURCE_FIELD}"/>
              <xsl:apply-templates select="." mode="carbon--general"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="." mode="carbon--general"/>
            </xsl:otherwise>
          </xsl:choose>
        </div>

        <script type="text/javascript">
          <xsl:text>require(["curam/util"], function(){curam.util.getClusterActionSet();});</xsl:text>
        </script>

        <!--
        Only add this content pane if a page level action set exists and if
        that page does not have an SVG widget. See note earlier in this
        template that checks INFO/HAS_BASIC_SVG, for more details.
        -->
        <jsp:scriptlet>
          if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL) &amp;&amp;
          !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.AGENDA) &amp;&amp;
          !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TREE)) {
        </jsp:scriptlet>
        
        <!-- A passive modal in Carbon is consider one where there is no buttons 
             e.g No  ACTION_SET containing ACTION_CONTROLS -->
        <xsl:if test="not(ACTION_SET) and not($is-duim-host-page)">
         <script type="text/javascript"> <xsl:text>require(["curam/util"], function(){
           curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/nonStandardModalFooter", [{"numButtons": "0"}]);});</xsl:text>
          </script>
        </xsl:if>

        <xsl:if test="ACTION_SET and not(../INFO/HAS_BASIC_SVG)">
            <xsl:choose>
              <xsl:when test="count(ACTION_SET/ACTION_CONTROL) = 1">
                  <script type="text/javascript"> <xsl:text>require(["curam/util"], function(){
                    curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/nonStandardModalFooter", [{"numButtons": "1"}]);});</xsl:text>
                  </script>
              </xsl:when>
              <xsl:otherwise>
                <!-- Multiple Buttons, check for multiple submit buttons -->
                <xsl:if test="count(ACTION_SET/ACTION_CONTROL[@ACTION_TYPE='SUBMIT']) > 1">
                  <xsl:choose>
                    <xsl:when test="count(ACTION_SET/ACTION_CONTROL[@DEFAULT='true']) > 0">
                      <script type="text/javascript"> <xsl:text>require(["curam/util"], function(){
                        curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/nonStandardModalFooter", [{"hasDefaultSubmitButton": "true"}]);});</xsl:text>
                      </script>
                    </xsl:when>
                    <xsl:otherwise>
                       <script type="text/javascript"> <xsl:text>require(["curam/util"], function(){
                        curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/nonStandardModalFooter", [{"hasDefaultSubmitButton": "false"}]);});</xsl:text>
                      </script>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:if>
             </xsl:otherwise>
           </xsl:choose>
            <div id="actions-panel">
              <xsl:if test="ACTION_SET/ACTION_CONTROL[@ALIGNMENT='LEFT']">
                <div>
                <xsl:choose>
                  <xsl:when test="ACTION_SET/CONDITION">
                    <xsl:apply-templates select="ACTION_SET" mode="conditional">
                      <xsl:with-param name="rounded" select="'true'" />
                      <xsl:with-param name="is-carbon-modal-button" select="'true'" />
                    </xsl:apply-templates>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:apply-templates select="ACTION_SET/ACTION_CONTROL[@ALIGNMENT='LEFT']"
                    mode="carbon--modal-button"/>
                  </xsl:otherwise>
                </xsl:choose>
                </div>
              </xsl:if>
              <xsl:if test="ACTION_SET/ACTION_CONTROL[@ALIGNMENT='RIGHT']">
                <div>
                <xsl:choose>
                  <xsl:when test="ACTION_SET/CONDITION">
                    <xsl:apply-templates select="ACTION_SET" mode="conditional">
                      <xsl:with-param name="rounded" select="'true'" />
                      <xsl:with-param name="is-carbon-modal-button" select="'true'" />
                    </xsl:apply-templates>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:apply-templates select="ACTION_SET/ACTION_CONTROL[@ALIGNMENT='RIGHT']"
                    mode="carbon--modal-button"/>
                  </xsl:otherwise>
                </xsl:choose>
                </div>
              </xsl:if>
              <xsl:if test="ACTION_SET/ACTION_CONTROL[@ALIGNMENT='CENTER']">
                <div>
                <xsl:choose>
                  <xsl:when test="ACTION_SET/CONDITION">
                    <xsl:apply-templates select="ACTION_SET" mode="conditional">
                      <xsl:with-param name="rounded" select="'true'" />
                      <xsl:with-param name="is-carbon-modal-button" select="'true'" />
                    </xsl:apply-templates>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:apply-templates select="ACTION_SET/ACTION_CONTROL[@ALIGNMENT='CENTER']"
                    mode="carbon--modal-button"/>
                  </xsl:otherwise>
                </xsl:choose>
                </div>
              </xsl:if>
            </div>
          </xsl:if>
          <xsl:if test="$is-duim-host-page" >
            <cing:component
              style="curam-util-client::duim-page-modal-action-set"/>
          </xsl:if>
        <jsp:scriptlet>}</jsp:scriptlet>

        <script type="text/javascript">
          <xsl:text>require(["curam/util"], function(){curam.util.adjustActionButtonWidth();});</xsl:text>
        </script>

        <jsp:scriptlet>
          if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.LIST_ROW_INLINE_PAGE)) {
        </jsp:scriptlet>
          <div class="bottom">
            <div class="left"></div>
            <div class="center">
              <div class="right"></div>
            </div>
          </div>
        <jsp:scriptlet>}</jsp:scriptlet>

        <script type="text/javascript">
          <xsl:call-template name="include-frame-event-handler"/>
          <xsl:apply-templates select="SCRIPT" mode="add-page-level-events"/>
          <xsl:if test="../INFO/HAS_TREE_WIDGET">
            <xsl:text>dojo.addOnLoad(function(){collapseTree('Collapse entry.', 'Entry is collapsed.', '../Images/minus.gif', 'Expand entry.', 'Entry is expanded.', '../Images/plus.gif')});</xsl:text>
          </xsl:if>
          <xsl:if test="../INFO/HAS_HEATMAP">
            <xsl:text>dojo.addOnLoad(function(){heatmapObject.drawMap()});</xsl:text>
          </xsl:if>
          <xsl:if test="../INFO/HAS_TREE_FRAMESET">
            <xsl:text>dojo.addOnLoad(function(){ require(["curam/widget/orgTree/dtree"], function(tc) { tc.redrawTree});});</xsl:text>
          </xsl:if>
          <!-- Check if any informational messages need to be loaded -->
          <xsl:text>require(["curam/util"], function(){curam.util.loadInformationalMsgs();
          curam.util.addContentWidthListener("</xsl:text>
            <xsl:if test="../INFO/HAS_WIZARD_FRAMESET">wizard-</xsl:if>
          <xsl:text>content");});</xsl:text>

          <jsp:scriptlet>if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.SMART_PANEL)) {</jsp:scriptlet>
          <xsl:text>dojo.addOnLoad(function(){require(["curam/tab"], function(){curam.tab.publishSmartPanelContentReady();});});</xsl:text>
          <jsp:scriptlet>}</jsp:scriptlet>

          <jsp:scriptlet>if (screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)
                           &amp;&amp; !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TREE)) {</jsp:scriptlet>
          <xsl:text>require(["curam/util"], function() {curam.util.addActionControlClickListener("modal-actions-panel");});</xsl:text>
          <jsp:scriptlet>}</jsp:scriptlet>
          <!--
          Struts action class will supply three request parameters containing the
          value and description to be returned from the popup and an indicator to
          say a "submit and dismiss" has taken place. In this case we execute the
          popup mappings and close the window.
          This needs to be the last script so that the CDEJ libraries are included
          and initialized.
          -->
          <xsl:if test="descendant::ACTION_CONTROL[@ACTION_TYPE
                                                   = 'SUBMIT_AND_DISMISS']">
            <jsp:scriptlet>
              String value = request.getParameter("value");
              String desc = request.getParameter("description");
              String submitanddismiss=request.getParameter("o3submitanddismiss");
              if (submitanddismiss != null) {
                out.print("dojo.addOnLoad(function(){");
                out.print("executeOpenerMapping(\"value\",\"" + value + "\");");
                out.print("executeOpenerMapping(\"description\",\""+desc+"\");");
                out.print("setParentFocus(null)");
                out.print("});");
              }
            </jsp:scriptlet>
          </xsl:if>
        </script>
      </cing:page>
    </body>
  </xsl:template>
  
  <!-- Template for modal button in carbon -->
  <xsl:template match="ACTION_CONTROL" mode="carbon--modal-button">
    <xsl:apply-templates select="." mode="action-round-corners">
      <!-- Indicating that this is a modal button in carbon. -->
      <xsl:with-param name="is-carbon-modal-button" select="'true'" />
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="CONDITION" mode="conditional-renderer">
    <xsl:param name="page-level-menu" />
    <xsl:param name="num-items-to-surface"/>
    <cing:container style="curam-util-client::condition">
      <cing:param name="doHideContent" value="${{hideConditionalLinks}}"/>
      <xsl:apply-templates select="IS_TRUE" mode="conditional-renderer">
        <xsl:with-param name="page-level-menu" select="$page-level-menu" />
      </xsl:apply-templates>
      <xsl:apply-templates select="IS_FALSE" mode="conditional-renderer">
        <xsl:with-param name="page-level-menu" select="$page-level-menu" />
      </xsl:apply-templates>
      <xsl:if test="parent::name() = 'ACTION_CONTROL'">
       <xsl:apply-templates select="parent::ACTION_CONTROL" mode="carbon-action-control">
         <xsl:with-param name="page-level-menu" select="$page-level-menu" />
       </xsl:apply-templates>
      </xsl:if>
      <xsl:if test="parent::name() = 'ACTION_SET'">
       <xsl:choose>
          <xsl:when test="$page-level-menu = 'true'">
            <xsl:apply-templates select="parent::ACTION_SET" mode="carbon-page-action-set"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="parent::ACTION_SET" mode="carbon-list-action-set"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
    </cing:container>
  </xsl:template>
  
  <xsl:template match="IS_TRUE | IS_FALSE" mode="conditional-renderer">
  <xsl:param name="page-level-menu"/>
  <xsl:variable name="node-name" select="local-name()"/>
   <cing:param name="operator" value="{$node-name}"/>
    <cing:connector>
      <xsl:attribute name="source">
        <xsl:value-of select="concat($si-pp, @NAME,'/', @PROPERTY)"/>
            <xsl:text>[${pageScope.loopIndex+1}]</xsl:text> 
      </xsl:attribute>
   </cing:connector>
   
 </xsl:template>
 
 
 <xsl:template match="ACTION_SET" mode="carbon-page-action-set">
   <cing:container style="curam-util-client::page-action-menu">
     <cing:param name="wrapper-div-id" value="page-level-action-menu"/>
     <cing:param name="page-label-enabled" value="true"/>
      <xsl:variable name="numItemsToSurface">
         <xsl:choose>
          <xsl:when test="@MAX_INLINE_ITEMS">
            <xsl:value-of select="@MAX_INLINE_ITEMS"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>${sessionScope['curam.actionmenus.max-inline-items.page']}</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <cing:param name="NUM_ITEMS_TO_SURFACE" value="{$numItemsToSurface}"/>
      <xsl:apply-templates select="SEPARATOR | ACTION_CONTROL" mode="carbon-menu-item">
        <xsl:with-param name="num-items-to-surface" select="$numItemsToSurface" />
        <xsl:with-param name="page-level-menu" select="'true'" />
      </xsl:apply-templates> 
   </cing:container>
 </xsl:template>
 
 <xsl:template match="ACTION_SET" mode="carbon-list-action-set">
   <cing:container style="curam-util-client::list-action-menu-layout">
     <cing:container style="curam-util-client::list-action-menu">
       <cing:param name="wrapper-div-id" value="list-actions-menu-{generate-id(.)}"/>
       <xsl:variable name="numItemsToSurface">
         <xsl:choose>
           <xsl:when test="@MAX_INLINE_ITEMS">
             <xsl:value-of select="@MAX_INLINE_ITEMS"/>
           </xsl:when>
           <xsl:otherwise>
             <xsl:text>${sessionScope['curam.actionmenus.max-inline-items.list']}</xsl:text>
           </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <cing:param name="NUM_ITEMS_TO_SURFACE" value="{$numItemsToSurface}"/>
      <cing:param name="row-index" value="${{pageScope.loopIndex+1}}"/>
      <xsl:apply-templates select="SEPARATOR | ACTION_CONTROL" mode="carbon-menu-item">
        <xsl:with-param name="num-items-to-surface" select="$numItemsToSurface" />
      </xsl:apply-templates>     
    </cing:container>
  </cing:container>
 </xsl:template>
 
 <xsl:template match="ACTION_SET" mode="carbon-page-level-actions">
    <jsp:scriptlet>if ((!screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)
                        || screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TREE))
                      &amp;&amp; !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.LIST_ROW_INLINE_PAGE)) {
    </jsp:scriptlet>
    <div>
      <xsl:attribute name="class">
        <xsl:choose>
          <xsl:when test="$help-node/@INCLUDE = 'true'">
            <xsl:text>page-level-menu</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>page-level-menu help-disabled</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="count(ACTION_CONTROL) = 1">
          <xsl:text> one</xsl:text>
        </xsl:if>
      </xsl:attribute>
    <xsl:choose>
      <xsl:when test="$preview = 'false'">
        <xsl:choose>
           <xsl:when test="self::node()/CONDITION and not(self::node()/CONDITION/@TYPE = 'DYNAMIC')">
            <xsl:apply-templates select="self::node()/CONDITION" mode="conditional-renderer">
              <xsl:with-param name="page-level-menu" select="'true'" />
            </xsl:apply-templates>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="." mode="carbon-page-action-set"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <xsl:when test="count(ACTION_CONTROL) &gt; 2 and $preview = 'true'">
       <div data-dojo-type="curam/widget/DeferredDropDownButton"
            id="page-level-action-menu"
            title="${{pageScope.actionsMenuTitle}}">
         <span>
           <img src="/themes/v6/images/actions-page-nor.png"
                alt="${{pageScope.actionsMenuTitle}}">
           </img>
         </span>
         <script type="text/javascript">
           <xsl:text>require(["curam/widget/DeferredDropDownButton"], function() {
                     if(!curam.widgetTemplates){curam.widgetTemplates={};}
                     curam.widgetTemplates['page-level-action-menu']='&lt;div data-dojo-type="dijit/Menu" data-dojo-props="autoFocus:true" class="expand-list-control"&gt;</xsl:text>
          
          <xsl:choose>
            <xsl:when test="CONDITION">
              <xsl:apply-templates select="../ACTION_SET" mode="conditional"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates select="ACTION_CONTROL | SEPARATOR" mode="page-menu-item"/>
            </xsl:otherwise>
          </xsl:choose>

           <xsl:text>&lt;/div&gt;'; });</xsl:text>
         </script>
       </div>

             <div id="preview-modals-menu" style="display:none">
               <xsl:choose>
                 <xsl:when test="CONDITION">
                   <xsl:apply-templates select="../ACTION_SET" mode="conditional"/>
                 </xsl:when>
                 <xsl:otherwise>
                   <xsl:apply-templates select="ACTION_CONTROL"/>
                 </xsl:otherwise>

        </xsl:choose>
        </div>
      </xsl:when>
    </xsl:choose>
  </div>
  <jsp:scriptlet>}</jsp:scriptlet>
  </xsl:template>
 
 
 <xsl:template match="ACTION_SET[@TYPE='LIST_ROW_MENU']" mode="carbon-list">
   <xsl:param name="empty" select="0"/>
    <xsl:param name="packForPagination" select="'false'" />
    <xsl:param name="is-page-menu" select="'false'" />
    <xsl:variable name="unique-menu-id">
      <xsl:value-of select="generate-id()"/>
    </xsl:variable>
    <xsl:variable name="list-actions-menu-id">
      <xsl:text>list-actions-menu-</xsl:text>
      <xsl:value-of select="$unique-menu-id"/>
    </xsl:variable>
    <td class="last-field">
      <xsl:choose>
        <xsl:when test="self::node()/CONDITION and not(self::node()/CONDITION/@TYPE = 'DYNAMIC')">
          <xsl:apply-templates select="self::node()/CONDITION" mode="conditional-renderer"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="." mode="carbon-list-action-set"/>
        </xsl:otherwise>
      </xsl:choose>
    </td>
  </xsl:template>
 
 
  <xsl:template match="SEPARATOR" mode="carbon-menu-item">
    <cing:container style="curam-util-client::list-action-menu-separator">
      <cing:param name="exclude-as-menu-item" value="true"/>  
    </cing:container>
  </xsl:template>

  <xsl:template match="ACTION_CONTROL" mode="carbon-menu-item">
    <xsl:param name="page-level-menu" />
    <xsl:param name="num-items-to-surface"/>
    <xsl:choose>
      <xsl:when test="self::node()/CONDITION and not(self::node()/CONDITION/@TYPE = 'DYNAMIC')">
        <xsl:apply-templates select="self::node()/CONDITION" mode="conditional-renderer">
          <xsl:with-param name="num-items-to-surface" select="$num-items-to-surface" />
          <xsl:with-param name="page-level-menu" select="$page-level-menu" />
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
       <xsl:apply-templates select="." mode="carbon-action-control">
         <xsl:with-param name="page-level-menu" select="$page-level-menu" />
       </xsl:apply-templates>
     </xsl:otherwise>
   </xsl:choose>
    
  </xsl:template>
 
  <xsl:template match="ACTION_CONTROL" mode="carbon-action-control">
    <xsl:param name="is-page-menu"/>
    <xsl:param name="page-level-menu" />
    <xsl:param name="is-file-download"/>
    <xsl:variable name="hideLinks">
      <xsl:text>${hideConditionalLinks}</xsl:text>      
    </xsl:variable>
    <xsl:variable name="action-control-type" select="@ACTION_TYPE"/>
    <xsl:variable name="generated-id">
      <xsl:choose>
        <xsl:when test="$page-level-menu = 'true'">
          <xsl:text>page-menu-item_</xsl:text>
           <xsl:value-of select="generate-id()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>list-menu-item_</xsl:text>
          <xsl:value-of select="generate-id()"/>
          <xsl:text>-${pageScope.loopIndex+1}</xsl:text>
        </xsl:otherwise>       
      </xsl:choose>
    </xsl:variable>
    <cing:container style="curam-util-client::action-control">
      <xsl:attribute name="id">
        <xsl:choose>
          <xsl:when test="@ACTION_ID">
            <xsl:value-of select="@ACTION_ID"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="@ACTION_IDENTIFIER">
                <xsl:value-of select="@ACTION_IDENTIFIER"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$generated-id"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      <xsl:if test="@ACTION_TYPE = 'SUBMIT'">     
         <cing:param name="TYPE" value="ACTION_MENU_SUBMIT"/>
      </xsl:if>
      <xsl:if test="@ACTION_TYPE = 'ACTION'">
        <cing:param name="TYPE" value="ACTION_MENU_ACTION"/>
      </xsl:if>
      <xsl:if test="@ACTION_TYPE = 'FILE_DOWNLOAD'">
        <cing:param name="TYPE" value="ACTION_MENU_ACTION"/>
        <cing:param name="isFileDownload" value="true"/>
      </xsl:if>
       <xsl:if test="@LABEL_PROP">
        <cing:param name="LABEL_PROP" value="{@LABEL_PROP}"/>
      </xsl:if>
      <xsl:if test="@LABEL">
        <cing:param name="LABEL" value="{@LABEL}"/>
      </xsl:if>
      <xsl:if test="@STYLE">
        <cing:param name="extra-wrapper-div-class" value="{@STYLE}"/>
      </xsl:if>
      <xsl:apply-templates select="SCRIPT" mode="add-script-parameters"/>
      <xsl:choose>
        <xsl:when test="@ACTION_TYPE = 'FILE_DOWNLOAD'">
          <xsl:apply-templates select="LINK" mode="carbon-link">
            <xsl:with-param name="is-file-download" select="'true'" />
           </xsl:apply-templates>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="LINK" mode="carbon-link">
            <xsl:with-param name="action-control-type" select="$action-control-type"/>
          </xsl:apply-templates>
        </xsl:otherwise>
      </xsl:choose>
    </cing:container>  
 </xsl:template>
 
 <!--
  Adds SCRIPT parameters to the field.
  -->
  <xsl:template match="SCRIPT" mode="add-script-parameters">
    <!-- Convert the event name to upper-case. -->
    <xsl:variable name="event"
                  select="translate(@EVENT, 'abcdefghijklmnopqrstuvwxyz',
                                            'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
    <!-- Becomes an attribute in HTML, so use the value attribute here. -->
    <cing:param name="{$event}_ACTION"
                value="{concat('return ', @ACTION, ';')}"/>
  </xsl:template>
 
 <xsl:template match="LINK" mode="carbon-link">
     <xsl:param name="is-file-download"/>
     <xsl:param name="action-control-type"/>
      <xsl:if test="@OPEN_MODAL">
        <cing:param name="OPEN_MODAL" value="{@OPEN_MODAL}"/>
      </xsl:if>
      <xsl:if test="@DISMISS_MODAL">
        <cing:param name="DISMISS_MODAL" value="{@DISMISS_MODAL}"/>
      </xsl:if>
      <xsl:if test="@WINDOW_OPTIONS">
        <cing:param name="WINDOW_OPTIONS" value="{@WINDOW_OPTIONS}"/>
      </xsl:if>
      <xsl:if test="@SAVE_LINK">
        <cing:param name="SAVE_LINK" value="{@SAVE_LINK}"/>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="$is-file-download = 'true'">
          <cing:linkcomponent linkUrl="/literal/..servlet"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="@PAGE_ID">
              <cing:linkcomponent linkUrl="/literal/{@PAGE_ID}Page.do"/>
            </xsl:when>
            <xsl:otherwise>
              <cing:linkcomponent>
                <xsl:attribute name="linkUrl">
                  <xsl:value-of select="concat($si-pp, @URI_BEAN,'/', @URI_FIELD)"/>
                  <xsl:if test="@SOURCE_IS_LIST = 'true'">
                     <xsl:text>[${pageScope.loopIndex+1}]</xsl:text>
                   </xsl:if>
                </xsl:attribute>
              </cing:linkcomponent>
            </xsl:otherwise>
           </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="$is-file-download = 'true'">
       <cing:linkparamcomponent name="pageID" sourcePath="/literal/${{requestScope.pageId}}"/>
     </xsl:if>
      <xsl:apply-templates select="LINK_PARAMETER" mode="carbon-link">
        <xsl:with-param name="action-control-type" select="$action-control-type"/>
      </xsl:apply-templates>
   
  </xsl:template>
  
  <xsl:template match="LINK_PARAMETER" mode="carbon-link">
    <xsl:param name="action-control-type"/>
    <xsl:variable name="type-is-not-submit" select="($action-control-type != 'SUBMIT' and $action-control-type != 'SUBMIT_AND_DISMISS')"/>
    <xsl:if test="@VALUE or @PARAMETER_NAME or 
       (@SOURCE_BEAN and @SOURCE_FIELD and (ancestor::PAGE/descendant::BEAN_SET_FIELD/@VALUE and ancestor::PAGE/descendant::BEAN_SET_FIELD/@TARGET_FIELD = @SOURCE_FIELD) 
         or $type-is-not-submit)">
      <cing:linkparamcomponent name="{@NAME}">
         <xsl:attribute name="sourcePath">
           <xsl:choose>
           <xsl:when test="@VALUE">
               <xsl:value-of select="concat('/literal/', @VALUE)"/>
             </xsl:when>
             <xsl:when test="@PARAMETER_NAME">
               <xsl:value-of select="concat($param-pp, @PARAMETER_NAME)"/>
             </xsl:when>
             <xsl:when test="@SOURCE_BEAN and @SOURCE_FIELD">
               <xsl:choose>
                 <xsl:when test="ancestor::PAGE/descendant::BEAN_SET_FIELD/@VALUE and (ancestor::PAGE/descendant::BEAN_SET_FIELD/@TARGET_FIELD = @SOURCE_FIELD)">
                   <xsl:value-of select="concat('/literal/', ancestor::PAGE/descendant::BEAN_SET_FIELD/@VALUE)"/>
                 </xsl:when>
                 <xsl:otherwise>
                   <xsl:if test="$type-is-not-submit">
                     <xsl:value-of select="concat($si-pp, @SOURCE_BEAN, '/', @SOURCE_FIELD)"/>
                     <xsl:if test="@SOURCE_IS_LIST = 'true'">
                       <xsl:text>[${pageScope.loopIndex+1}]</xsl:text>
                     </xsl:if>
                   </xsl:if>
                 </xsl:otherwise>
               </xsl:choose>
            </xsl:when>
          </xsl:choose>
        </xsl:attribute>
      </cing:linkparamcomponent>
    </xsl:if>
  </xsl:template>

  <!--
  The DISPLAY section invokes any specified server interfaces and contains the
  main content for the page. If there is an ACTION_CONTROL in the DISPLAY
  section that has a SUBMIT ACTION_TYPE, the main content will be contained in
  a form. The main content can contain an ACTION_SET (to be displayed at the
  top and/or bottom of the content), and a number of CLUSTER and LIST elements.

  There is a special case for dynamic UIM host pages where the form will always
  be generated regardless of the content on that host page.
  -->
  <xsl:template match="DISPLAY" mode="carbon--general">
    <xsl:variable name="submit-action-control"
                  select="descendant::ACTION_CONTROL[
                              @ACTION_TYPE = 'SUBMIT'
                              or @ACTION_TYPE = 'SUBMIT_AND_DISMISS']"/>
    <xsl:variable name="is-duim-host-page"
        select="ancestor::PAGE/@COMPONENT_STYLE = $duim-host-style"/>

    <xsl:choose>
      <xsl:when test="$submit-action-control or $is-duim-host-page">
        <cing:form method="post" action="${{requestScope.pageId}}Action.do">
          <xsl:attribute name="returnToSelf">
            <xsl:choose>
              <xsl:when test="$submit-action-control/LINK[@PAGE_ID = 'THIS']">
                <xsl:text>true</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>false</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>

          <xsl:if test="../INFO/HAS_FILE_UPLOAD">
            <!-- File uploads are rare, so this does not need to be fast. -->
            <xsl:attribute name="encodingType">
              <xsl:text>multipart/form-data</xsl:text>
            </xsl:attribute>
          </xsl:if>

          <xsl:apply-templates select="HIDDEN_FIELD"/>
          <xsl:apply-templates select="." mode="carbon--main-content">
            <xsl:with-param name="is-form" select="'true'"/>
          </xsl:apply-templates>

          <!--
          NB: These functions need to be at the end of the form to ensure the
          JavaScript DOM for the form has been initialized fully.
          -->
          <script type="text/javascript">
            <xsl:if test="not(../INFO/HAS_DECISION_MATRIX)">
              <xsl:text>require(["curam/util"], function() {curam.util.setFocus();});</xsl:text>
            </xsl:if>
          </script>
            <!-- Hide submit buttons on pages with MS Word control -->
            <xsl:if test="../INFO/HAS_FILE_EDIT">
              <xsl:text>curam.util.WordFileEdit.hideSubmitButtons();</xsl:text>
            </xsl:if>
            <jsp:scriptlet>if (screenContext.hasContextBits(
                                 curam.omega3.taglib.ScreenContext.TREE
                               | curam.omega3.taglib.ScreenContext.ACTION)
                               &amp;&amp; !screenContext.hasContextBits(
                                     curam.omega3.taglib.ScreenContext.ERROR)
                               &amp;&amp; !screenContext.hasContextBits(
                                   curam.omega3.taglib.ScreenContext.RESOLVE)) {
            </jsp:scriptlet>
            <script type="text/javascript">
            <xsl:text>dojo.addOnLoad(function(){ require(["curam/widget/orgTree/treecontent", 'curam/util'], function(tc) { tc.refreshTree(); curam.util.fireRefreshTreeEvent(); })});</xsl:text>
            </script>
            <jsp:scriptlet>}</jsp:scriptlet>
        </cing:form>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="." mode="carbon--main-content"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="DISPLAY" mode="carbon--main-content">
    <xsl:param name="is-form" select="'false'"/>

    <xsl:choose>
      <xsl:when test="../INFO/HAS_BASIC_SVG">
        <!--
        When a page contains an SVG widget, we disable the Dojo widgets on that
        page. See note in DISPLAY mode="html-body" template. As a result of
        disabling these widgets it means we lose the new button bar at the
        bottom of a modal dialog (again see the DISPLAY mode="html-body"
        template for where this is disabled, there is a check for
        INFO/HAS_BASIC_SVG wrapping an ACTION_SET template). Because we don't
        have the button bar, we have to ensure the old style buttons are
        displayed at the bottom of the page. However page level action sets are
        now hidden on modal pages by setting the "page-action-set-position"
        parameter to the ACTION_SET template. Therefore, this apply templates
        intentionally does not pass this parameter to ensure the action set is
        visible. All of these workarounds can be removed when we move actions
        sets to the page title bar as a menu.
        -->
        <xsl:apply-templates select="ACTION_SET" mode="common"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="ACTION_SET[@TOP = 'true']" mode="common">
          <!--  indicate this is the top page-level action set  -->
          <xsl:with-param name="page-action-set-position" select="'top'"/>
        </xsl:apply-templates>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:apply-templates select="JSP_SCRIPTLET | CLUSTER | LIST" mode="carbon"/>
    <xsl:apply-templates select="FIELD | CONTAINER | WIDGET"/>

    <xsl:apply-templates select="ACTION_SET[@BOTTOM = 'true']" mode="common">
      <!-- indicate this is the bottom page-level action set -->
      <xsl:with-param name="page-action-set-position" select="'bottom'"/>
    </xsl:apply-templates>
  </xsl:template>
  
 <xsl:template match="JSP_SCRIPTLET" mode="carbon">
    <jsp:scriptlet>
      <xsl:value-of select="."/>
    </jsp:scriptlet>
  </xsl:template>

  <!-- These elements can all have an optional CONDITION child element. -->
  <xsl:template match="CLUSTER | LIST" mode="carbon">
    <xsl:param name="is-page-menu" />
    <xsl:param name="rounded" />
    <xsl:param name="in-cluster" select="'false'" />

    <xsl:variable name="num-cols" select="number(@NUM_COLUMNS)"/>
    <xsl:variable name="is-page-type-details"
                  select="ancestor::PAGE/@TYPE = 'DETAILS'"/>
    <xsl:variable name="dcl-id">
      <xsl:choose>
        <xsl:when test="name(.) = 'CLUSTER' and CONDITION and CONDITION/@TYPE='DYNAMIC'">
          <xsl:value-of select="generate-id()"/>
        </xsl:when>
        <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <xsl:choose>
      <xsl:when test="$preview = 'true'">
        <xsl:apply-templates select="." mode="preview"/>
      </xsl:when>
      <xsl:when test="CONDITION and not(CONDITION/@TYPE = 'DYNAMIC')">
        <xsl:apply-templates select="." mode="carbon--conditional">
          <xsl:with-param name="rounded" select="$rounded" />
          <xsl:with-param name="action-control-sequence" select="position()"/>
        </xsl:apply-templates>
      </xsl:when>

      <!-- Assume CONDITION never used for wizard navigation frame. -->
      <xsl:when test="../../INFO/HAS_WIZARD_FRAMESET">
        <xsl:apply-templates select="." mode="carbon--cluster-wizard">
          <xsl:with-param name="num-cols" select="$num-cols"/>
        </xsl:apply-templates>
      </xsl:when>
      <xsl:when test="$is-page-type-details">
        <xsl:choose>
          <xsl:when test="name(.) = 'CLUSTER' or name(.) = 'LIST'">
            <div>
              <xsl:attribute name="class">
                <xsl:if test="@STYLE">
                  <xsl:value-of select="@STYLE" />
                  <xsl:text>-wrapper</xsl:text>
                </xsl:if>
                <xsl:text> details-cluster-wrapper</xsl:text>
              </xsl:attribute>
              <xsl:apply-templates select="." mode="top-detail-cluster-corner" />
              <xsl:apply-templates select="." mode="carbon--common"/><!-- do we need to pass dcl-id here as well?? chances are we need-->
              <xsl:apply-templates select="." mode="bottom-detail-cluster-corner" />
            </div>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="." mode="carbon--common">
              <xsl:with-param name="action-control-sequence" select="position()"/>
              <xsl:with-param name="dcl-id" select="$dcl-id"/>
            </xsl:apply-templates>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="." mode="carbon--common">
          <xsl:with-param name="is-page-menu" select="$is-page-menu"/>
          <xsl:with-param name="rounded" select="$rounded"/>
          <xsl:with-param name="action-control-sequence" select="position()"/>
          <xsl:with-param name="dcl-id" select="$dcl-id"/>
          <xsl:with-param name="in-cluster" select="$in-cluster"/>
        </xsl:apply-templates>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:if test="$dcl-id and not($dcl-id='false')">
        <!--  add the dynamic condition output here  -->
        <script type="text/javascript"><xsl:text>require(['curam/dcl'], function(dcl) { dcl.bindCluster('</xsl:text>
        <xsl:value-of select="$dcl-id"/>
        <xsl:text>','</xsl:text>
        <xsl:if test="count(ancestor::CLUSTER[CONDITION/@TYPE='DYNAMIC']) &gt; 0">
          <xsl:value-of select="ancestor::CLUSTER/CONDITION[@TYPE='DYNAMIC'][last()]/SCRIPT/@EXPRESSION"/>
        </xsl:if>
        <xsl:text>','</xsl:text>
          <xsl:value-of select="CONDITION/SCRIPT/@EXPRESSION"/><!-- assume valid XIM, no further checks -->
        <xsl:text>');});</xsl:text>
        </script>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="WIDGET" mode="carbon">
    <xsl:param name="packForPagination" select="'false'" />

    <xsl:choose>
      <xsl:when test="$preview = 'true'">
        <xsl:apply-templates select="." mode="preview"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'USER_PREFERENCE_EDITOR'">
        <curam:userPreferenceEditor/>
      </xsl:when>
      <xsl:when test="@TYPE = 'FILE_UPLOAD'">
        <script type="text/javascript">
          <jsp:scriptlet>{String propVal = curam.omega3.util.CDEJResources.getProperty(
            "curam.validation.invalidFilePathToUploadEntered");
             String escapedPropVal
               = curam.util.common.util.JavaScriptEscaper.escapeText(propVal);
               out.print("curam.validation.invalidPathMsg='" + escapedPropVal + "';");}
          </jsp:scriptlet>
          <xsl:text>curam.validation.FILE_UPLOAD_FLGS.push("</xsl:text>
              <xsl:value-of select="generate-id()"/>
          <xsl:text>");
            curam.validation.activateFileUploadChecker();
            curam.util.addFileUploadResizeListener();</xsl:text>
        </script>
        <!-- the span below is to flag the following fileUpload to speed up later DOM search. -->
        <span>
          <xsl:attribute name="id"><xsl:value-of select="generate-id()"/></xsl:attribute>
          <xsl:attribute name="style">display:none</xsl:attribute>
          c
        </span>
        <curam:fileUpload
            contentTargetBean="{WIDGET_PARAMETER[@NAME = 'CONTENT']
                                /@TARGET_BEAN}"
            contentTargetField="{WIDGET_PARAMETER[@NAME = 'CONTENT']
                                 /@TARGET_FIELD}">
          <xsl:if test="WIDGET_PARAMETER[@NAME = 'CONTENT_TYPE']">
            <xsl:attribute name="contentTypeTargetBean">
              <xsl:value-of select="WIDGET_PARAMETER[@NAME = 'CONTENT_TYPE']
                                    /@TARGET_BEAN"/>
            </xsl:attribute>
            <xsl:attribute name="contentTypeTargetField">
              <xsl:value-of select="WIDGET_PARAMETER[@NAME = 'CONTENT_TYPE']
                                    /@TARGET_FIELD"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="WIDGET_PARAMETER[@NAME = 'ACCEPTABLE_CONTENT_TYPES']">
           <xsl:attribute name="acceptContentTypes">
              <xsl:for-each select="WIDGET_PARAMETER[@NAME = 'ACCEPTABLE_CONTENT_TYPES']">
                <xsl:value-of select="current()/@VALUE"/>
                <xsl:if test="position() != last()">
                  <xsl:text>,</xsl:text>
                </xsl:if>
              </xsl:for-each>
            </xsl:attribute>
          </xsl:if>

          <xsl:if test="WIDGET_PARAMETER[@NAME = 'FILE_NAME']">
            <xsl:attribute name="fileNameTargetBean">
              <xsl:value-of
                select="WIDGET_PARAMETER[@NAME = 'FILE_NAME']/@TARGET_BEAN"/>
            </xsl:attribute>
            <xsl:attribute name="fileNameTargetField">
              <xsl:value-of select="WIDGET_PARAMETER[@NAME = 'FILE_NAME']
                                    /@TARGET_FIELD"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:value-of select="@LABEL"/>

        </curam:fileUpload>
      </xsl:when>
      <xsl:when test="@TYPE = 'FILE_EDIT'">
        <curam:fileEdit
          sourceBean="{WIDGET_PARAMETER[@NAME = 'DOCUMENT']/@SOURCE_BEAN}"
          sourceField="{WIDGET_PARAMETER[@NAME = 'DOCUMENT']/@SOURCE_FIELD}"
          targetBean="{WIDGET_PARAMETER[@NAME = 'DOCUMENT']/@TARGET_BEAN}"
          targetField="{WIDGET_PARAMETER[@NAME = 'DOCUMENT']/@TARGET_FIELD}"
          detailsSourceBean="{WIDGET_PARAMETER[@NAME = 'DETAILS']/@SOURCE_BEAN}"
          detailsSourceField="{WIDGET_PARAMETER[@NAME = 'DETAILS']
                               /@SOURCE_FIELD}"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'MULTISELECT'">
        <curam:multiSelectCheckBox mode="multiselect"
          targetBean="{WIDGET_PARAMETER[@NAME = 'MULTI_SELECT_TARGET']
                       /@TARGET_BEAN}"
          targetField="{WIDGET_PARAMETER[@NAME = 'MULTI_SELECT_TARGET']
                        /@TARGET_FIELD}"
          selectionSourceBean="{WIDGET_PARAMETER[@NAME = 'MULTI_SELECT_INITIAL']
                                /LINK_PARAMETER/@SOURCE_BEAN}"
          selectionSourceField="{WIDGET_PARAMETER[@NAME='MULTI_SELECT_INITIAL']
                                 /LINK_PARAMETER/@SOURCE_FIELD}">
          <xsl:attribute name="escapeScripts">
            <xsl:choose>
              <xsl:when test="$packForPagination = 'true'"><xsl:text>true</xsl:text></xsl:when>
              <xsl:otherwise><xsl:text>false</xsl:text></xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
          <xsl:attribute name="dataTestidComponentPrefix">
            <xsl:text>multiselect</xsl:text>
          </xsl:attribute>
          <xsl:if test="ancestor::LIST/@TITLE">
            <xsl:attribute name="listTitle">
             <xsl:value-of select="ancestor::LIST/@TITLE" />
            </xsl:attribute>
            <xsl:attribute name="listTitleProp">
             <xsl:value-of select="ancestor::LIST/@TITLE_PROP" />
            </xsl:attribute>
          </xsl:if>
           <xsl:if test="ancestor::LIST/FIELD[1]/@SOURCE_BEAN">
            <xsl:attribute name="columnContextField">
             <xsl:value-of select="ancestor::LIST/FIELD[1]/@SOURCE_FIELD"/>
            </xsl:attribute>
            <xsl:attribute name="columnContextBean">
             <xsl:value-of select="ancestor::LIST/FIELD[1]/@SOURCE_BEAN"/>
            </xsl:attribute>
             <xsl:attribute name="columnContextIndex">
             <xsl:text>${pageScope.loopIndex}</xsl:text>
            </xsl:attribute>
          </xsl:if>
          <xsl:for-each select="WIDGET_PARAMETER[@NAME = 'MULTI_SELECT_SOURCE']
                                /LINK_PARAMETER">
            <curam:linkParameter name="unnamed" useLoop="true"
                                 xsl:use-attribute-sets="source"/>
          </xsl:for-each>
        </curam:multiSelectCheckBox>
      </xsl:when>
      <xsl:when test="@TYPE = 'SINGLESELECT'">
        <curam:multiSelectCheckBox mode="singleselect"
          targetBean="{WIDGET_PARAMETER[@NAME = 'SELECT_TARGET']/@TARGET_BEAN}"
          targetField="{WIDGET_PARAMETER[@NAME = 'SELECT_TARGET']
                        /@TARGET_FIELD}"
          selectionSourceBean="{WIDGET_PARAMETER[@NAME = 'SELECT_INITIAL']
                                /LINK_PARAMETER/@SOURCE_BEAN}"
          selectionSourceField="{WIDGET_PARAMETER[@NAME = 'SELECT_INITIAL']
                                 /LINK_PARAMETER/@SOURCE_FIELD}">
          <xsl:attribute name="escapeScripts">
            <xsl:choose>
              <xsl:when test="$packForPagination = 'true'"><xsl:text>true</xsl:text></xsl:when>
              <xsl:otherwise><xsl:text>false</xsl:text></xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
          <xsl:attribute name="dataTestidComponentPrefix">
            <xsl:text>singleselect</xsl:text>
          </xsl:attribute>
          <xsl:if test="ancestor::LIST/@TITLE">
            <xsl:attribute name="listTitle">
             <xsl:value-of select="ancestor::LIST/@TITLE" />
            </xsl:attribute>
            <xsl:attribute name="listTitleProp">
             <xsl:value-of select="ancestor::LIST/@TITLE_PROP" />
            </xsl:attribute>
          </xsl:if>
           <xsl:if test="ancestor::LIST/FIELD[1]/@SOURCE_BEAN">
            <xsl:attribute name="columnContextField">
             <xsl:value-of select="ancestor::LIST/FIELD[1]/@SOURCE_FIELD"/>
            </xsl:attribute>
            <xsl:attribute name="columnContextBean">
             <xsl:value-of select="ancestor::LIST/FIELD[1]/@SOURCE_BEAN"/>
            </xsl:attribute>
             <xsl:attribute name="columnContextIndex">
             <xsl:text>${pageScope.loopIndex}</xsl:text>
            </xsl:attribute>
          </xsl:if>
          <xsl:for-each select="WIDGET_PARAMETER[@NAME = 'SELECT_SOURCE']
                                /LINK_PARAMETER">
            <curam:linkParameter name="unnamed" useLoop="true"
                                 xsl:use-attribute-sets="source"/>
          </xsl:for-each>
        </curam:multiSelectCheckBox>
      </xsl:when>
      <xsl:when test="@TYPE = 'EVIDENCE_COMPARE'">
        <curam:evidenceCompare
          sourceBean="{WIDGET_PARAMETER[@NAME = 'OLD_EVIDENCE']/@SOURCE_BEAN}"
          oldEvidenceField="{WIDGET_PARAMETER[@NAME = 'OLD_EVIDENCE']
                             /@SOURCE_FIELD}"
          newEvidenceField="{WIDGET_PARAMETER[@NAME = 'NEW_EVIDENCE']
                             /@SOURCE_FIELD}"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'RULES_SIMULATION_EDITOR'">
        <curam:rulesSimulationEditor
          sourceBean="{WIDGET_PARAMETER[@NAME = 'VALUES']/@SOURCE_BEAN}"
          sourceField="{WIDGET_PARAMETER[@NAME = 'VALUES']/@SOURCE_FIELD}"
          targetBean="{WIDGET_PARAMETER[@NAME = 'VALUES']/@TARGET_BEAN}"
          targetField="{WIDGET_PARAMETER[@NAME = 'VALUES']/@TARGET_FIELD}"
          metaDataSourceBean="{WIDGET_PARAMETER[@NAME = 'META_DATA']
                               /@SOURCE_BEAN}"
          metaDataSourceField="{WIDGET_PARAMETER[@NAME = 'META_DATA']
                                /@SOURCE_FIELD}"
          addButtonCaption="{WIDGET_PARAMETER[@NAME = 'ADD_BUTTON_CAPTION']
                             /@VALUE}"
          addButtonImage="{WIDGET_PARAMETER[@NAME = 'ADD_BUTTON_IMAGE']/@VALUE}"
          labelWidth="{ancestor::CLUSTER/@LABEL_WIDTH}"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'RULES_DECISION_TREE'">
        <curam:rules
          sourceBean="{WIDGET_PARAMETER[@NAME = 'DECISION_DATA']
                       /LINK_PARAMETER/@SOURCE_BEAN}"
          sourceField="{WIDGET_PARAMETER[@NAME = 'DECISION_DATA']
                        /LINK_PARAMETER/@SOURCE_FIELD}"
          sourceDecisionIDParameter="{WIDGET_PARAMETER[@NAME = 'DECISION_ID']
                                      /LINK_PARAMETER/@PARAMETER_NAME}"
          targetDecisionIDParameter="{WIDGET_PARAMETER[@NAME = 'DECISION_ID']
                                      /LINK_PARAMETER/@NAME}"
          view="DYNAMIC" width="{@WIDTH}" config="{@CONFIG}"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'RULES_DECISION_FULL_TREE'">
        <curam:rules
          sourceBean="{WIDGET_PARAMETER[@NAME = 'DECISION_DATA']
                       /LINK_PARAMETER/@SOURCE_BEAN}"
          sourceField="{WIDGET_PARAMETER[@NAME = 'DECISION_DATA']
                         /LINK_PARAMETER/@SOURCE_FIELD}"
          sourceDecisionIDParameter="{WIDGET_PARAMETER[@NAME = 'DECISION_ID']
                                      /LINK_PARAMETER/@PARAMETER_NAME}"
          targetDecisionIDParameter="{WIDGET_PARAMETER[@NAME = 'DECISION_ID']
                                      /LINK_PARAMETER/@NAME}"
          view="DYNAMIC_FULL_TREE" width="{@WIDTH}" config="{@CONFIG}"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'DYNAMIC_RULES_EDITOR'">
        <curam:rulesEditor
          sourceBean="{WIDGET_PARAMETER[@NAME = 'DECISION_DATA']
                       /LINK_PARAMETER/@SOURCE_BEAN}"
          sourceField="{WIDGET_PARAMETER[@NAME = 'DECISION_DATA']
                        /LINK_PARAMETER/@SOURCE_FIELD}"
          sourceDecisionIDParameter="{WIDGET_PARAMETER[@NAME = 'DECISION_ID']
                                      /LINK_PARAMETER/@PARAMETER_NAME}"
          targetDecisionIDParameter="{WIDGET_PARAMETER[@NAME = 'DECISION_ID']
                                      /LINK_PARAMETER/@NAME}"
          view="DYNAMIC" width="{@WIDTH}" config="{@CONFIG}"/>
      </xsl:when>
      <xsl:when test="@TYPE = 'FILE_DOWNLOAD'">
        <curam:fileDownload
            sourceBean="{WIDGET_PARAMETER[@NAME = 'LINK_TEXT']/@SOURCE_BEAN}"
            sourceField="{WIDGET_PARAMETER[@NAME = 'LINK_TEXT']/@SOURCE_FIELD}">
          <curam:linkParameter name="pageID" value="${{requestScope.pageId}}"/>
          <xsl:apply-templates
              select="WIDGET_PARAMETER[@NAME = 'PARAMS']/LINK_PARAMETER"/>
        </curam:fileDownload>
      </xsl:when>
      <xsl:when test="@TYPE = 'WIZARD_SUMMARY'">
        <curam:wizardSummary/>
      </xsl:when>
      <xsl:when test="@TYPE = 'CONTENT'">
        <!--
        CONTENT widgets without a component style will probably cause a
        run-time error when the style is not found.
        -->
        <cing:component style="{@COMPONENT_STYLE}">
          <xsl:if test="@CONFIG">
            <cing:param name="config" value="{@CONFIG}"/>
          </xsl:if>
        </cing:component>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!--
  A CLUSTER can contain an optional ACTION_SET and any number of CLUSTER_ROW
  elements. The ACTION_SET may be repeated at the top and/or bottom of the
  CLUSTER. Titles and the main content are only generated if they are not
  empty.

  A LIST is displayed as a grid of values. The contained ACTION_CONTROL, FIELD
  and CONTAINER elements form rows that are repeated in a table. The elements'
  LABEL attributes are the headings for the columns in the table. The title or
  main content of the list is not generated if it is empty.
  -->
  <xsl:template match="CLUSTER | LIST" mode="carbon--common">
    <xsl:param name="dcl-id" select="false"/>
    <xsl:param name="in-cluster"/>

    <xsl:variable name="heading-id" select="concat(generate-id(), '-', 'heading')"/>
    <xsl:variable name="content-id" select="concat(generate-id(), '-', 'content')"/>
    <xsl:variable name="has-title" select="self::node()[@TITLE or TITLE]"/>
    <xsl:variable name="has-header"
        select="$has-title or self::node()[@DESCRIPTION or DESCRIPTION]"/>
    <!-- prefix for 'data-testid' attribute (automated testing) -->
    <xsl:variable name="data-test-id-prefix">
      <xsl:choose>
        <xsl:when test="name(.) = 'CLUSTER'">
          <xsl:text>cluster</xsl:text>
        </xsl:when>
        <xsl:otherwise>list</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- suffix with label value for 'data-testid' attribute (automated testing) -->
     <xsl:variable name="data-test-id-suffix">
      <xsl:choose>
        <xsl:when test="@TITLE_PROP">
          <xsl:value-of select="@TITLE_PROP"/>
        </xsl:when>
        <xsl:when test="TITLE">
          <xsl:value-of select="TITLE/TITLE_ELEMENT[1]/@TITLE_PROP"/>
        </xsl:when>
         <xsl:when test="@TITLE">
          <xsl:value-of select="@TITLE"/>
        </xsl:when>
        <xsl:otherwise>NO_LABEL</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- 'data-testid' attribute for cluster and list in STATIC UIM (automated testing) -->
    <xsl:variable name="root-data-test-id" select="concat($data-test-id-prefix, '_', $data-test-id-suffix)"/>
    <xsl:variable name="heading-data-test-id" select="concat($data-test-id-prefix, '_', 'heading', '_', $data-test-id-suffix)"/>
    <xsl:variable name="content-data-test-id" select="concat($data-test-id-prefix, '_', 'content', '_', $data-test-id-suffix)"/>

    <xsl:variable name="behavior">
      <xsl:choose>
        <xsl:when test="$collapsible-clusters-enabled = 'false'
                        or @BEHAVIOR = 'NONE'">
          <xsl:value-of select="'NONE'"/>
        </xsl:when>
        <xsl:when test="@BEHAVIOR">
          <xsl:value-of select="@BEHAVIOR"/>
        </xsl:when>
        <xsl:otherwise>EXPANDED</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="collapsible">
      <xsl:choose>
        <xsl:when test="$behavior = 'EXPANDED' or $behavior = 'COLLAPSED'">
          <xsl:text>true</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>false</xsl:text>
          <xsl:value-of select="$behavior"></xsl:value-of>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="maxHeight">
      <xsl:choose>
        <xsl:when test="@SCROLL_HEIGHT > 0">
          <xsl:value-of select="@SCROLL_HEIGHT"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of
            select="$curam-config/SCROLLBAR_CONFIG
                      /ENABLE_SCROLLBARS[@TYPE = 'CLUSTER']/@MAX_HEIGHT"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!--
    LISTs can now have a second type of action set for the new row actions menu.
    Therefore only the "default" action set type should be output here.
    -->
    <xsl:apply-templates
      select="ACTION_SET[@TOP = 'true' and (not(@TYPE) or @TYPE='DEFAULT')]"/>
    <div id="{generate-id()}" data-testid="{$root-data-test-id}">
      <xsl:if test="@SUMMARY">
        <xsl:attribute name="aria-label">
          <xsl:value-of select="@SUMMARY"/>
        </xsl:attribute>
        <xsl:attribute name="role">
          <xsl:text>group</xsl:text>
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates select="." mode="carbon--add-css-class">
        <xsl:with-param name="other-classes">
          <xsl:choose>
            <xsl:when test="self::LIST and $has-header">
              <xsl:text> list-with-header</xsl:text>
            </xsl:when>
          </xsl:choose>
          <xsl:if test="self::CLUSTER and $dcl-id and not($dcl-id='false')">
            <xsl:value-of select="concat(' hide-dynamic-cluster ', $dcl-id)"/>
          </xsl:if>
        </xsl:with-param>
      </xsl:apply-templates>

      <xsl:if test="@WIDTH">
        <xsl:attribute name="style">
          <xsl:value-of select="concat('width: ', @WIDTH, '%;')"/>
        </xsl:attribute>
      </xsl:if>

      <section class="bx--accordion bx--accordion--section">
        <xsl:attribute name="class">
          <xsl:text>bx--accordion</xsl:text>
          <xsl:text> bx--accordion--section</xsl:text>
          <xsl:if test="not($has-title) or $behavior = 'NONE'">
            <xsl:text> bx--accordion--disabled</xsl:text>
          </xsl:if>
        </xsl:attribute>
        <xsl:if test="not($behavior = 'NONE')">
          <xsl:attribute name="data-accordion">
            <xsl:text></xsl:text>
          </xsl:attribute>
        </xsl:if>
        <div>
          <xsl:if test="not($behavior = 'NONE')">
            <xsl:attribute name="data-accordion-item">
              <xsl:text></xsl:text>
            </xsl:attribute>
          </xsl:if>
          <xsl:attribute name="class">
            <xsl:text>bx--accordion__item</xsl:text>
            <xsl:if test="$behavior = 'EXPANDED' or $behavior = 'NONE'">
              <xsl:text> bx--accordion__item--active</xsl:text>
            </xsl:if>
          </xsl:attribute>
          <xsl:if test="$has-title">
            <xsl:choose>
              <xsl:when test="$behavior = 'NONE'">
                <div id="{$heading-id}" class="bx--accordion__heading">
                  <xsl:call-template name="carbon--title">
                    <xsl:with-param name="in-cluster" select="$in-cluster"/>
                  </xsl:call-template>
                </div>
              </xsl:when>
              <xsl:otherwise>
              <jsp:scriptlet>
                  <xsl:text>pageContext.setAttribute("clusterTitle","List.Label.Children");</xsl:text>
               </jsp:scriptlet>
                <button id="{$heading-id}" class="bx--accordion__heading" aria-controls="{$content-id}" data-testid="{$heading-data-test-id}" type="button">
                  <xsl:attribute name="aria-expanded">
                    <xsl:choose>
                      <xsl:when test="$behavior = 'EXPANDED'">
                        <xsl:text>true</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:text>false</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
                  <svg focusable="false" preserveAspectRatio="xMidYMid meet" style="will-change: transform;" xmlns="http://www.w3.org/2000/svg" class="bx--accordion__arrow" width="16" height="16" viewBox="0 0 16 16" aria-hidden="true"><path d="M11 8l-5 5-.7-.7L9.6 8 5.3 3.7 6 3z"></path></svg>
                  <xsl:call-template name="carbon--title">
                    <xsl:with-param name="in-cluster" select="$in-cluster"/>
                  </xsl:call-template>
                </button>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
          <div id="{$content-id}" data-testid="{$content-data-test-id}">
            <xsl:attribute name="class">
              <xsl:text>bx--accordion__content</xsl:text>
              <xsl:if test="not($maxHeight = '')">
                <xsl:text> bx--accordion__content--scrollable</xsl:text>
              </xsl:if>
            </xsl:attribute>
            

            <xsl:if test="DESCRIPTION or @DESCRIPTION">
              <p class="bx--cluster__description">
                <xsl:choose>
                  <xsl:when test="DESCRIPTION">
                    <xsl:apply-templates select="DESCRIPTION/DESCRIPTION_ELEMENT"/>
                    <!-- tooltip for cluster description  -->
                    <xsl:if test="DESCRIPTION and not (DESCRIPTION/DESCRIPTION_ELEMENT/@VALUE = '' )" >
                    <xsl:attribute name="title"><xsl:apply-templates select="DESCRIPTION/DESCRIPTION_ELEMENT"/></xsl:attribute>
                    </xsl:if>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:if test="@DESCRIPTION and not (@DESCRIPTION = '' )" >
                    <xsl:attribute name="title"><xsl:value-of select="@DESCRIPTION"/></xsl:attribute>
                    </xsl:if>
                    <xsl:value-of select="@DESCRIPTION"/>
                  </xsl:otherwise>
                </xsl:choose>
              </p>
            </xsl:if>
            <xsl:apply-templates mode="carbon--main-content" select="self::node()[ACTION_CONTROL or FIELD or CONTAINER or WIDGET or FOOTER_ROW or CLUSTER_ROW]">
              <xsl:with-param name="maxHeight" select="$maxHeight"/>
            </xsl:apply-templates>
          </div>
        </div>
      </section>
    </div>

    <!--
    LISTs can now have a second type of action set for the new row actions menu.
    Therefore only the "default" action set type should be output here.
    -->
    <xsl:apply-templates
       select="ACTION_SET[@BOTTOM = 'true'
                          and (not(@TYPE) or @TYPE = 'DEFAULT')]"/>
  </xsl:template>

  <xsl:template name="carbon--title">
    <xsl:param name="in-cluster"/>

    <!-- Add a result count to the title of lists on search pages. -->
    <xsl:variable name="search-page"
                  select="ancestor::PAGE/descendant::ACTION_SET
                          /ACTION_CONTROL[@ACTION_TYPE = 'SUBMIT']
                          /LINK[@PAGE_ID = 'THIS'] and self::LIST"/>

  <xsl:choose>
      <xsl:when test="$in-cluster = 'true'">
      <h4 class="bx--accordion__title">
        <xsl:if test="$search-page">
          <xsl:attribute name="data-search-page">
            <xsl:text></xsl:text>
          </xsl:attribute>
        </xsl:if>
        <xsl:call-template name="carbon--title-text">
          <xsl:with-param name="search-page" select="$search-page"/>
        </xsl:call-template>
      </h4>
    </xsl:when>
    <xsl:otherwise>
    <h3 class="bx--accordion__title">
      <xsl:if test="$search-page">
        <xsl:attribute name="data-search-page">
          <xsl:text></xsl:text>
        </xsl:attribute>
      </xsl:if>
      <xsl:call-template name="carbon--title-text">
        <xsl:with-param name="search-page" select="$search-page"/>
      </xsl:call-template>
    </h3>
    </xsl:otherwise>
  </xsl:choose>
  </xsl:template>

  <xsl:template name="carbon--title-text">
    <xsl:param name="search-page"/>

    <xsl:choose>
      <xsl:when test="TITLE">
        <xsl:apply-templates select="TITLE/TITLE_ELEMENT"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="@TITLE"/>

        <xsl:if test="$search-page">
          <curam:displayRecordNumber sourceBean="{@LOOP_BEAN}"
                                     sourceField="{@LOOP_FIELD}"/>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="CLUSTER" mode="carbon--main-content">
    <xsl:param name="maxHeight"/>
    <xsl:call-template name="cluster-grid">
      <xsl:with-param name="maxHeight" select="$maxHeight"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="cluster-grid">
    <xsl:param name="maxHeight"/>

    <xsl:variable name="num-cols" select="number(@NUM_COLUMNS)"/>
    <xsl:variable name="is-field-first" select="@LAYOUT_ORDER = 'FIELD'"/>
    <xsl:variable name="show-labels" select="@SHOW_LABELS = 'true'"/>
    <xsl:variable name="label-width" select="number(@LABEL_WIDTH)"/>

    <div>
      <xsl:attribute name="class">
        <xsl:text>bx--grid</xsl:text>
        <xsl:text> bx--grid--contained</xsl:text>
        <xsl:if test="@TAB_ORDER = 'ROW'">
            <xsl:text> bx--grid--direction-row</xsl:text>
        </xsl:if>
        <xsl:if test="@WIDTH">
          <xsl:text> bx--grid--full-width</xsl:text>
        </xsl:if>
      </xsl:attribute>
      <xsl:if test="not($maxHeight = '')">
        <xsl:attribute name="style">
          <xsl:value-of select="concat('height: ', $maxHeight, 'px;')"/>
        </xsl:attribute>
      </xsl:if>

      <xsl:choose>
        <xsl:otherwise>
          <xsl:apply-templates select="CLUSTER_ROW" mode="carbon">
            <xsl:with-param name="num-cols" select="$num-cols"/>
            <xsl:with-param name="is-field-first" select="$is-field-first"/>
            <xsl:with-param name="show-labels" select="$show-labels"/>
            <xsl:with-param name="label-width" select="$label-width"/>
          </xsl:apply-templates>
        </xsl:otherwise>
      </xsl:choose>
    </div>
     
    <!-- Clear the page scope variable with the key to the current cluster title -->
    <jsp:scriptlet>
      <xsl:text>pageContext.removeAttribute("clusterTitle");</xsl:text>
    </jsp:scriptlet>
  </xsl:template>

  <xsl:template match="CLUSTER | LIST" mode="carbon--cluster-list-header">
    <xsl:param name="collapsible"/>
    <xsl:param name="behavior"/>

    <xsl:if test="TITLE or @TITLE">
      <!-- Set a page scope variable with the key to the current cluster title -->
      <xsl:variable name="clusterTitle1" select="substring-after(@TITLE,'${cing:esc(o3_pb,')" />
      <xsl:variable name="clusterTitle2" select="translate($clusterTitle1, ')}', '')" />
      <xsl:if test="$clusterTitle2">
        <jsp:scriptlet>
          <xsl:text>pageContext.setAttribute("clusterTitle",</xsl:text>
          <xsl:value-of select="$clusterTitle2" />
          <xsl:text>);</xsl:text>
        </jsp:scriptlet>
      </xsl:if>

      <h3>
        <!-- tooltip  for cluster title-->
        <xsl:if test="@TITLE and not(@TITLE= '')">
        <xsl:attribute name="title"> <xsl:value-of select="@TITLE"/></xsl:attribute>
          </xsl:if>
        <xsl:choose>
          <xsl:when test="$collapsible = 'true'">
            <xsl:attribute name="class">collapse</xsl:attribute>
          </xsl:when>
        </xsl:choose>

        <span>
          <xsl:attribute name="class">
            <xsl:text>collapse-title</xsl:text>
          </xsl:attribute>
          <xsl:choose>
            <xsl:when test="TITLE">
              <xsl:apply-templates select="TITLE/TITLE_ELEMENT"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="@TITLE"/>
              <!-- Add a result count to the title of lists on search pages. -->
              <xsl:variable name="search-page"
                            select="ancestor::PAGE/descendant::ACTION_SET
                                    /ACTION_CONTROL[@ACTION_TYPE = 'SUBMIT']
                                    /LINK[@PAGE_ID = 'THIS']"/>
              <xsl:if test="self::LIST and $search-page">
                <curam:displayRecordNumber sourceBean="{@LOOP_BEAN}"
                                           sourceField="{@LOOP_FIELD}"/>
              </xsl:if>
            </xsl:otherwise>
          </xsl:choose>
        </span>
            <!-- tooltip  for cluster title-->
            <xsl:if test="TITLE and not (TITLE/TITLE_ELEMENT/@VALUE = '' )" >
            <xsl:attribute name="title"><xsl:apply-templates select="TITLE/TITLE_ELEMENT"/></xsl:attribute>
            </xsl:if>
      </h3>
      <button title="${{clusterToggleArrow}}">
          <xsl:attribute name="class">
            <xsl:text>grouptoggleArrow</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="tabIndex">
            <xsl:text>0</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="onclick">
            <xsl:text>toggleCluster(this,arguments[0]);</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="onKeyPress">
              <xsl:text>if(curam.util.enterKeyPress(event)){toggleCluster(this,arguments[0]);}</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="type">
            <xsl:text>button</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="aria-label">
            <xsl:value-of select="concat(TITLE/TITLE_ELEMENT/@VALUE, @TITLE, $curam-cluster-toggle-spantext)"/>
          </xsl:attribute>
          <xsl:attribute name="aria-expanded">
            <xsl:choose>
              <xsl:when test="$behavior = 'COLLAPSED'">
                <xsl:text>false</xsl:text>
              </xsl:when>
              <xsl:when test="$behavior = 'EXPANDED'">
                <xsl:text>true</xsl:text>
              </xsl:when>
            </xsl:choose>
          </xsl:attribute>
          <xsl:text>&amp;nbsp;</xsl:text>
          <img src="/themes/curam/images/chevron--down20-enabled.svg" alt="" onLoad="setToggleClusterIcon(this);" class="defaultIcon"/>
          <img src="/themes/curam/images/chevron--down20-enabled.svg" alt="" onLoad="setToggleClusterIcon(this);" class="hoverIcon"/>

          <span>
          <xsl:attribute name="class">
            <xsl:text>hidden</xsl:text>
          </xsl:attribute>
          <xsl:text></xsl:text>
            <xsl:value-of select="$curam-cluster-toggle-spantext"/>
         </span>
        </button>
    </xsl:if>
    <xsl:if test="DESCRIPTION or @DESCRIPTION">
      <p class="bx--cluster__description">
        <xsl:choose>
          <xsl:when test="DESCRIPTION">
            <xsl:apply-templates select="DESCRIPTION/DESCRIPTION_ELEMENT"/>
            <!-- tooltip for cluster description  -->
            <xsl:if test="DESCRIPTION and not (DESCRIPTION/DESCRIPTION_ELEMENT/@VALUE = '' )" >
            <xsl:attribute name="title"><xsl:apply-templates select="DESCRIPTION/DESCRIPTION_ELEMENT"/></xsl:attribute>
             </xsl:if>
          </xsl:when>
          <xsl:otherwise>
            <xsl:if test="@DESCRIPTION and not (@DESCRIPTION = '' )" >
            <xsl:attribute name="title"><xsl:value-of select="@DESCRIPTION"/></xsl:attribute>
             </xsl:if>
            <xsl:value-of select="@DESCRIPTION"/>
          </xsl:otherwise>
        </xsl:choose>
      </p>
    </xsl:if>
  </xsl:template>

  <xsl:template match="CLUSTER" mode="carbon--cluster-wizard">
    <div id="wizardHolder" class="cluster-wizard">
      <xsl:apply-templates select="CLUSTER_ROW/FIELD[1]"/>
    </div>
  </xsl:template>

  <xsl:template match="CLUSTER | LIST" mode="carbon--in-cluster">
    <xsl:param name="num-cols"/>
    <xsl:variable name="col-size--big">
      <xsl:choose>
        <xsl:when test="$num-cols = 1">
          <xsl:text>16</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="floor(16 div (2 * ceiling($num-cols div 2)))"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="col-size--small" select="floor($col-size--big div 2)"/>

    <div>
      <xsl:attribute name="class">
        <xsl:text>bx--col</xsl:text>
        <xsl:text> bx--col-sm-4</xsl:text>
        <xsl:text> bx--col-md-</xsl:text><xsl:value-of select="$col-size--small"/>
        <xsl:text> bx--col-lg-</xsl:text><xsl:value-of select="$col-size--big"/>
        <xsl:text> bx--col-xlg-</xsl:text><xsl:value-of select="$col-size--big"/>
        <xsl:text> bx--col-max-</xsl:text><xsl:value-of select="$col-size--big"/>
      </xsl:attribute>

      <div class="bx--cluster__item bx--cluster__item--in-cluster">
        <xsl:apply-templates select="." mode="carbon">
          <xsl:with-param name="in-cluster" select="'true'"/>
        </xsl:apply-templates>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="CLUSTER_ROW" mode="carbon">
    <xsl:param name="num-cols"/>
    <xsl:param name="is-field-first"/>
    <xsl:param name="show-labels"/>
    <xsl:param name="label-width"/>

    <xsl:variable name="row-id" select="concat(generate-id(), '-', position())"/>

    <!--
    Special handling for certain domains. In these cases, the cluster's <tbody>
    element will be empty and the renderer will be responsible for filling it
    in with rows and cells as needed. Any other fields in the CLUSTER_ROW will
    be ignored.
    -->
    <xsl:variable name="first-field" select="FIELD[1]"/>

    <xsl:choose>
      <xsl:when test="$first-field/@DOMAIN = 'ADDRESS_DATA'">
          <xsl:if test="$first-field/@MANDATORY = 'true' and not($show-labels)">
            <div>
              <img src="/themes/classic/images/icons/mandatory.gif"
                  alt="${o3_mandatoryTooltipText}"
                  title="${o3_mandatoryTooltipText}"/>
            </div>
          </xsl:if>
          <xsl:apply-templates select="$first-field"/>
      </xsl:when>
      <xsl:otherwise>
        <div id="{$row-id}" class="bx--row">
          <xsl:choose>
            <xsl:when test="$first-field/@DOMAIN = 'PARAM_TAB_LIST'">
              <xsl:apply-templates select="$first-field"/>
            </xsl:when>
            <xsl:when test="$first-field/@DOMAIN = 'SCHEDULE_DATA'">
              <xsl:apply-templates select="$first-field" mode="output-schedule"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:apply-templates mode="carbon--in-cluster">
                <xsl:with-param name="row-id" select="$row-id"/>
                <xsl:with-param name="num-cols" select="$num-cols"/>
                <xsl:with-param name="is-field-first" select="$is-field-first"/>
                <xsl:with-param name="show-labels" select="$show-labels"/>
                <xsl:with-param name="label-width" select="$label-width"/>
              </xsl:apply-templates>
            </xsl:otherwise>
          </xsl:choose>
        </div>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="CONTAINER | FIELD | WIDGET" mode="carbon--in-cluster">
    <xsl:param name="row-id"/>
    <xsl:param name="num-cols"/>
    <xsl:param name="is-field-first"/>
    <xsl:param name="show-labels"/>
    <xsl:param name="label-width"/>

    <xsl:variable name="col-size--big">
      <xsl:choose>
        <xsl:when test="$num-cols = 1">
          <xsl:text>16</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="floor(16 div (2 * ceiling($num-cols div 2)))"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="col-size--small" select="floor($col-size--big div 2)"/>

    <xsl:variable name="col-id"
                  select="concat($row-id, '-', position())" />

    <xsl:variable name="is-mandatory"
                  select="@MANDATORY = 'true' or
                          self::CONTAINER/*[@MANDATORY = 'true']"/>

    <xsl:variable name="count">
              <xsl:value-of select="../@NUM_COLUMNS - count(EMPTY_FIELD | FIELD | CONTAINER
                                                | CLUSTER | WIDGET | LIST)"/>
            </xsl:variable>

    <div>
      <xsl:attribute name="class">
        <xsl:text>bx--col</xsl:text>
        <xsl:text> bx--col-sm-4</xsl:text>
        <xsl:text> bx--col-md-</xsl:text><xsl:value-of select="$col-size--small"/>
        <xsl:text> bx--col-lg-</xsl:text><xsl:value-of select="$col-size--big"/>
        <xsl:text> bx--col-xlg-</xsl:text><xsl:value-of select="$col-size--big"/>
        <xsl:text> bx--col-max-</xsl:text><xsl:value-of select="$col-size--big"/>
      </xsl:attribute>

        <div>
        <xsl:attribute name="class">
            <xsl:text>bx--cluster__item</xsl:text>
            <xsl:choose>
                <xsl:when test="@TARGET_FIELD or FIELD/@TARGET_FIELD">
                    <xsl:text> bx--cluster__item--input</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text> bx--cluster__item--read-only-field</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>

        <xsl:call-template name="carbon--cluster-wrapper">
            <xsl:with-param name="is-field-first" select="$is-field-first" />
            <xsl:with-param name="show-labels" select="$show-labels" />
            <xsl:with-param name="is-mandatory" select="$is-mandatory" />
            <xsl:with-param name="row-id" select="$row-id" />
            <xsl:with-param name="col-id" select="$col-id" />
            <xsl:with-param name="label-width" select="$label-width" />
        </xsl:call-template>
        </div>
    </div>
  </xsl:template>

  <xsl:template name="carbon--cluster-wrapper">
        <xsl:param name="is-field-first"/>
        <xsl:param name="show-labels"/>
        <xsl:param name="is-mandatory"/>
        <xsl:param name="row-id"/>
        <xsl:param name="col-id"/>
        <xsl:param name="label-width"/>

        <xsl:call-template name="carbon--cluster-item">
            <xsl:with-param name="is-field-first" select="$is-field-first" />
            <xsl:with-param name="show-labels" select="$show-labels" />
            <xsl:with-param name="is-mandatory" select="$is-mandatory" />
            <xsl:with-param name="row-id" select="$row-id" />
            <xsl:with-param name="col-id" select="$col-id" />
            <xsl:with-param name="label-width" select="$label-width" />
        </xsl:call-template>

  </xsl:template>
  
  <xsl:template name="carbon--cluster-item">
    <xsl:param name="is-field-first"/>
    <xsl:param name="show-labels"/>
    <xsl:param name="is-mandatory"/>
    <xsl:param name="row-id"/>
    <xsl:param name="col-id"/>
    <xsl:param name="label-width"/>

    <xsl:variable name="is-input-cluster" select="@TARGET_FIELD or FIELD/@TARGET_FIELD"/>
    <xsl:variable name="field-width" select="100 - $label-width"/>

    <xsl:if test="(TITLE/TITLE_ELEMENT or @LABEL) and $show-labels and not($is-field-first)">
        <label>
          <xsl:call-template name="carbon--output-label">
            <xsl:with-param name="is-mandatory" select="$is-mandatory"/>
            <xsl:with-param name="col-id" select="$col-id"/>
            <xsl:with-param name="label-width" select="$label-width"/>
          </xsl:call-template>
        </label>
    </xsl:if>

    <div>
      <xsl:call-template name="carbon--cluster-item-field">
        <xsl:with-param name="show-labels" select="$show-labels"/>
        <xsl:with-param name="is-mandatory" select="$is-mandatory"/>
        <xsl:with-param name="is-input-cluster" select="$is-input-cluster"/>
        <xsl:with-param name="label-width" select="$label-width"/>
      </xsl:call-template>
    </div>

    <xsl:if test="(TITLE/TITLE_ELEMENT or @LABEL) and $show-labels and $is-field-first">
        <label>
          <xsl:call-template name="carbon--output-label">
            <xsl:with-param name="is-mandatory" select="$is-mandatory"/>
            <xsl:with-param name="col-id" select="$col-id"/>
            <xsl:with-param name="label-width" select="$label-width"/>
          </xsl:call-template>
        </label>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="carbon--cluster-item-field">
    <xsl:param name="show-labels"/>
    <xsl:param name="is-mandatory"/>
  
    <xsl:apply-templates select="." mode="carbon--add-css-class">
      <xsl:with-param name="other-classes">
        <xsl:text>bx--field</xsl:text>
        <xsl:if test="$is-mandatory and not($show-labels)">
          <xsl:text> mandatory-cluster-field</xsl:text>
        </xsl:if>
      </xsl:with-param>
    </xsl:apply-templates>

    <xsl:apply-templates select="." mode="add-hidden-label-setter"/>
    <xsl:apply-templates select="." mode="carbon"/>
  </xsl:template>

  <xsl:template match="EMPTY_FIELD" mode="carbon--in-cluster">
    <xsl:param name="num-cols"/>

    <xsl:variable name="col-size--big">
          <xsl:choose>
            <xsl:when test="$num-cols = 1">
              <xsl:text>16</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="floor(16 div (2 * ceiling($num-cols div 2)))"/>
            </xsl:otherwise>
          </xsl:choose>
    </xsl:variable>
    <xsl:variable name="col-size--small" select="floor($col-size--big div 2)"/>

    <div>
      <xsl:attribute name="class">
        <xsl:text>bx--col</xsl:text>
        <xsl:text> bx--col-sm-4</xsl:text>
        <xsl:text> bx--col-md-</xsl:text><xsl:value-of select="$col-size--small"/>
        <xsl:text> bx--col-lg-</xsl:text><xsl:value-of select="$col-size--big"/>
        <xsl:text> bx--col-xlg-</xsl:text><xsl:value-of select="$col-size--big"/>
        <xsl:text> bx--col-max-</xsl:text><xsl:value-of select="$col-size--big"/>
      </xsl:attribute>

      <xsl:call-template name="carbon--cluster-wrapper">
      </xsl:call-template>
    </div>
  </xsl:template>

  <!--
  Displays the label and adds a colon to the end if the label is shown first
  and if colons are enabled either globally (curam-config.xml) or locally (on
  the page).
  -->
  <xsl:template name="carbon--output-label">
    <xsl:param name="is-mandatory"/>
    <xsl:param name="col-id"/>
    <xsl:param name="label-width"/>

    <xsl:attribute name="id">
      <xsl:value-of select="$col-id"/>
    </xsl:attribute>
    <xsl:attribute name="class">
      <xsl:text>bx--label</xsl:text>
      <xsl:if test="$is-mandatory">
        <xsl:text> bx--label--required</xsl:text>
      </xsl:if>
    </xsl:attribute>
    <xsl:if test="$label-width > 0">
      <xsl:attribute name="style">
        <xsl:value-of select="concat('flex-basis:', $label-width, '%;')"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:if test="TITLE | @LABEL">
      <!-- Adds tooltip to the read only fields of cluster -->
      <!-- The tooltip will be displayed based on the label of the element -->

      <xsl:attribute name="title">
        <xsl:apply-templates select="." mode="get-label-text"/>
      </xsl:attribute>

      <xsl:apply-templates select="." mode="get-label-text"/>
      <xsl:choose>
        <xsl:when test="not(ancestor::PAGE/@APPEND_COLON)">
          <!-- Attribute is not set: use application-wide setting. -->
          <xsl:value-of select="$colon"/>
        </xsl:when>
        <xsl:when test="ancestor::PAGE/@APPEND_COLON = 'true'">
          <xsl:text>:</xsl:text>
        </xsl:when>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
  
   <xsl:template match="FIELD" mode="get-label-text-for-element">
     <xsl:choose>
      <xsl:when test="TITLE">
       <xsl:apply-templates select="." mode="get-label-text">
          <xsl:with-param name="field-has-label-element" select="'true'" />
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
         <xsl:apply-templates select="."
                             mode="get-label-field-prop"/>
      </xsl:otherwise>
    </xsl:choose>
            
  </xsl:template>

  <!--
  A FIELD is used for the input or display of data. Fields that have targets
  are displayed as input controls. Fields that do not have targets are
  displayed as plain text output. FIELD elements that are contained in a LOOP
  element will use the loop index.

  If a field contains a LINK with a PAGE_ID or URI or URI_BEAN/URI_FIELD
  attributes, then a <curam:link> tag is used around the field value. It is
  assumed that such fields will not resolve to input controls, etc. This should
  be checked at the earlier validation phase.

  If a field contains a LINK with no PAGE_ID it is assumed to be used to pass
  parameters to a popup page. See the FIELD with the is-popup mode for how this
  is used.
  -->
  <xsl:template match="FIELD" mode="carbon">
    <xsl:choose>
      <xsl:when test="LINK/CONDITION">
        <xsl:apply-templates select="." mode="conditional"/>
      </xsl:when>
      <xsl:when test="LINK">
        <xsl:apply-templates select="." mode="common"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="." mode="check-for-image-map"/>
        <!-- ellipsis support to FIELD element. Irrespective of  its child elements(LINK etc) if the FIELD element
          has APPEND_ELLIPSIS  attribute set to true, the ellipsis character will be appended to -->
        <xsl:if test="@APPEND_ELLIPSIS='true'">
          <xsl:text>&#8230;</xsl:text>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
  A CONTAINER can appear in a LIST or a CLUSTER in place of a FIELD and can
  contain FIELD elements and ACTION_CONTROL elements laid out horizontally.
  -->
  <xsl:template match="CONTAINER" mode="carbon">
    <xsl:param name="packForPagination" select="'false'" />

    <span>
      <xsl:apply-templates select="." mode="carbon--add-css-class"/>
      <xsl:if test="@WIDTH and not(parent::LIST)">
        <xsl:attribute name="style">
          <xsl:value-of select="concat('width: ', @WIDTH, '%;')"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates mode="in-container">
        <xsl:with-param name="packForPagination" select="'true'" />
      </xsl:apply-templates>
    </span>
  </xsl:template>

  <xsl:template match="FIELD | ACTION_CONTROL | ACTION_SET | CONTAINER | WIDGET | CLUSTER | LIST | PAGE_TITLE" mode="carbon--add-css-class">
    <xsl:param name="attribute-name" select="'class'"/>
    <xsl:param name="other-classes" select="''"/>
    <xsl:param name="is-header" select="''"/>
    <xsl:param name="action-control-sequence"/>

    <!-- Try to keep these tests in order of probability. -->
    <xsl:variable name="attribute-value">
      <xsl:choose>
        <xsl:when test="self::ACTION_CONTROL">
          <xsl:text>ac</xsl:text>
          <xsl:if test="self::ACTION_CONTROL[@ACTION_TYPE = 'FILE_DOWNLOAD']">
            <xsl:text> file-download</xsl:text>
          </xsl:if>
          <xsl:if test="$action-control-sequence = 1">
            <xsl:text> first-action-control</xsl:text>
          </xsl:if>
        </xsl:when>
        <xsl:when test="self::ACTION_SET">action-set</xsl:when>
        <xsl:when test="self::CLUSTER">
          <xsl:text>bx--cluster</xsl:text>
          <xsl:if test="@WIDTH">
            <xsl:text> bx--cluster--full-width</xsl:text>
          </xsl:if>
          <xsl:if test="CLUSTER_ROW/FIELD/@TARGET_FIELD or CLUSTER_ROW/CONTAINER/FIELD/@TARGET_FIELD">
            <xsl:text> bx--cluster--input</xsl:text>
            <xsl:if test="@SUMMARY">
              <xsl:attribute name="role">
                <xsl:text>group</xsl:text>
              </xsl:attribute>
            </xsl:if>
          </xsl:if>
        </xsl:when>
        <xsl:when test="self::LIST">list bx--cluster</xsl:when>
        <xsl:when test="self::PAGE_TITLE">title</xsl:when>
        <!--
        The next condition is for backward-compatibility only and should
        be removed when widgets are no longer hidden within containers.
        -->
        <xsl:when test="self::CONTAINER/WIDGET[@TYPE = 'MULTISELECT']
                        or self::WIDGET[@TYPE = 'MULTISELECT']">
          <xsl:text>multiselect</xsl:text>
          <xsl:if test="position() = 1 and not(ancestor::LIST/DETAILS_ROW)
                        and $is-header = 'true'">
            <xsl:text> first-header</xsl:text>
          </xsl:if>
        </xsl:when>
        <xsl:when test="self::CONTAINER/WIDGET[@TYPE = 'SINGLESELECT']
                        or self::WIDGET[@TYPE = 'SINGLESELECT']">
          <xsl:text> singleselect</xsl:text>
          <xsl:if test="position() = 1 and not(ancestor::LIST/DETAILS_ROW)
                       and $is-header = 'true'">
            <xsl:text> first-header</xsl:text>
          </xsl:if>
        </xsl:when>
        <xsl:when test="self::CONTAINER">container</xsl:when>
        <xsl:when test="self::WIDGET">widget</xsl:when>
      </xsl:choose>

      <!-- Space is normalized later, so it is not a concern here. -->
      <xsl:value-of select="concat(' ', $other-classes)"/>
      <xsl:value-of select="concat(' ', @STYLE)"/>
      <xsl:value-of select="concat(' ', translate(@ALIGNMENT, 'CEFGHILNRT',
                                                  'cefghilnrt'))"/>
    </xsl:variable>

    <xsl:attribute name="{$attribute-name}">
      <!-- Normalize to remove excessive whitespace characters. -->
      <xsl:value-of select="normalize-space($attribute-value)"/>
    </xsl:attribute>
  </xsl:template>

  <xsl:template match="LIST" mode="carbon--main-content">
    <xsl:param name="scrollable-id" select="generate-id()"/>
    <xsl:param name="pagination-id" select="generate-id()"/>

    <xsl:apply-templates select="HIDDEN_FIELD"/>

    <xsl:variable name="scrollbar">
      <xsl:choose>
        <xsl:when test="$curam-config/SCROLLBAR_CONFIG/ENABLE_SCROLLBARS[@TYPE = 'LIST']/@MAX_HEIGHT">
          <xsl:choose>
            <xsl:when test="@SCROLL_HEIGHT = -1"><xsl:text>false</xsl:text></xsl:when>
            <xsl:otherwise><xsl:text>true</xsl:text></xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="@SCROLL_HEIGHT > 0"><xsl:text>true</xsl:text></xsl:when>
            <xsl:otherwise><xsl:text>false</xsl:text></xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="maxHeight">
      <xsl:choose>
        <xsl:when test="@SCROLL_HEIGHT > 0">
          <xsl:value-of select="@SCROLL_HEIGHT"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$curam-config/SCROLLBAR_CONFIG/ENABLE_SCROLLBARS[@TYPE = 'LIST']/@MAX_HEIGHT" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="listPaginationEnabled">
      <xsl:choose>
        <!-- no pagination for the scrollable list -->
        <xsl:when test="$scrollbar = 'true'">false</xsl:when>
        <!-- no pagination for the MULTISELECT widget currently  -->
        <xsl:when test="child ::CONTAINER/WIDGET[@TYPE = 'MULTISELECT']
                        or child::WIDGET[@TYPE = 'MULTISELECT']">false</xsl:when>
        <xsl:when test="@PAGINATED"><xsl:value-of select="@PAGINATED"/></xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$curam-config/PAGINATION"><xsl:value-of select="$curam-config/PAGINATION/@ENABLED"/></xsl:when>
            <xsl:otherwise><xsl:text>true</xsl:text></xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <jsp:scriptlet>
      <xsl:text>pageContext.setAttribute("doPagination",</xsl:text>
      <xsl:value-of select="$listPaginationEnabled"/>
      <xsl:text>);</xsl:text>
    </jsp:scriptlet>
    <xsl:variable name="listPageHeight">
      <xsl:choose>
        <xsl:when test="$listPaginationEnabled='true'">
          <xsl:choose>
            <xsl:when test="@PAGINATED_HEIGHT"><xsl:value-of select="@PAGINATED_HEIGHT"/></xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$curam-config/PAGINATION and $curam-config/PAGINATION/DEFAULT_PAGE_SIZE">
                  <xsl:value-of select="$curam-config/PAGINATION/DEFAULT_PAGE_SIZE"/>
                </xsl:when>
                <xsl:otherwise><xsl:text>15</xsl:text></xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise><xsl:text>0</xsl:text></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="listPageThreshold">
      <xsl:choose>
        <xsl:when test="$listPaginationEnabled='true'">
          <xsl:choose>
            <xsl:when test="@PAGINATED_THRESHOLD">
              <xsl:value-of select="@PAGINATED_THRESHOLD"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$curam-config/PAGINATION
                      and $curam-config/PAGINATION/PAGINATION_THRESHOLD">
                  <xsl:value-of
                    select="$curam-config/PAGINATION/PAGINATION_THRESHOLD"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="$listPageHeight"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>0</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- Put together a unique identifier of the list to be used
     for list-specific data storage. -->
    <xsl:variable name="list-id" select="concat(concat(ancestor::PAGE/@PAGE_ID, '-'), generate-id())"/>
    <xsl:variable name="list-id-class" select="concat('list-id-', $list-id)"/>


    <xsl:choose>
      <xsl:when test="$scrollbar = 'true'">
        <!--
        Scrollable lists are implemented with two tables, one for the
        header the other for the scollable body. First the "header table" is
        ouput.
        -->
        <xsl:apply-templates select="."
          mode="scrollable-list-column-header-table"/>

        <div class="scrollable">
          <xsl:attribute name="id"><xsl:value-of select="concat('scrollable',$scrollable-id)"/></xsl:attribute>
          <xsl:attribute name="style"><xsl:value-of select="concat('height: ', $maxHeight, 'px;')"/></xsl:attribute>
          <xsl:apply-templates select="." mode="list-table">
            <xsl:with-param name="pagination-enabled" select="$listPaginationEnabled"/>
            <xsl:with-param name="listPageHeight" select="$listPageHeight"/>
            <xsl:with-param name="pagination-id" select="$pagination-id"/>
            <xsl:with-param name="list-id-class" select="$list-id-class"/>
            <xsl:with-param name="scrollable-list-body-table" select="'true'"/>
          </xsl:apply-templates>
        </div>
        <script type="text/javascript">
          <xsl:value-of select="concat('curam.util.alterScrollableListBottomBorder(&quot;scrollable', $scrollable-id, '&quot;&#44;', $maxHeight, ');')"/>
        </script>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="." mode="list-table">
          <xsl:with-param name="pagination-enabled" select="$listPaginationEnabled"/>
          <xsl:with-param name="listPageHeight" select="$listPageHeight"/>
          <xsl:with-param name="pagination-id" select="$pagination-id"/>
          <xsl:with-param name="list-id-class" select="$list-id-class"/>
        </xsl:apply-templates>
      </xsl:otherwise>
    </xsl:choose>

    <!-- Add the script that will add pagination. -->
    <xsl:if test="$listPaginationEnabled='true'">
      <script type="text/javascript">
        <xsl:text>require(["curam/pagination", "curam/pagination/DefaultListModel", "curam/pagination/ExpandableListModel"], function() {</xsl:text>
        <jsp:scriptlet>
          curam.util.client.jsp.JspUtil.outputListPaginationProps(pageContext);
        </jsp:scriptlet>
        <xsl:text>dojo.addOnLoad(function() {</xsl:text>
        <xsl:text>curam.pagination.defaultPageSize=</xsl:text><xsl:value-of select="$listPageHeight"/><xsl:text>;</xsl:text>
        <xsl:text>curam.pagination.threshold=</xsl:text><xsl:value-of select="$listPageThreshold"/><xsl:text>;</xsl:text>
        <xsl:choose>
          <xsl:when test="./DETAILS_ROW">
            <xsl:text>var listModel = new curam.pagination.ExpandableListModel("</xsl:text>
              <xsl:value-of select="$pagination-id"/><xsl:text>");</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>var listModel = new curam.pagination.DefaultListModel("</xsl:text>
              <xsl:value-of select="$pagination-id"/><xsl:text>");</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text>curam.pagination.addPagination(listModel, dojo.query("div#pagination-gui-</xsl:text>
        <xsl:value-of select="$pagination-id"/>
        <xsl:text>")[0]);</xsl:text>
        <xsl:text>});});</xsl:text>
      </script>
    </xsl:if>

    <xsl:if test="./DETAILS_ROW">
     <script type="text/javascript">
      <!-- Set the minimum allowed height for expanded details row in this list. -->
      <xsl:text>require(["curam/util/ExpandableLists"], function() {</xsl:text>
      <xsl:text>curam.util.ExpandableLists.setMinimumExpandedHeight('</xsl:text>
      <xsl:value-of select="$list-id"/>
      <xsl:text>', </xsl:text>
      <xsl:choose>
        <xsl:when test="./DETAILS_ROW/@MINIMUM_EXPANDED_HEIGHT">
          <xsl:value-of select="./DETAILS_ROW/@MINIMUM_EXPANDED_HEIGHT"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>30</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:text>);</xsl:text>

      <!-- Load data for this list. -->
      <!-- Intentionally running this AFTER the list model is created as they
        both affect the expanded state of detail rows and this code
        must "win". -->
      <xsl:text>curam.util.ExpandableLists.loadStateData('</xsl:text>
      <xsl:value-of select="$list-id"/>
      <xsl:text>');});</xsl:text>
    </script>
    </xsl:if>

  </xsl:template>


  <!--
  Wraps the "common" mode template output with a dynamically evaluated
  conditional element.
  -->
  <xsl:template
    match="CLUSTER | LIST | ACTION_CONTROL | ACTION_SET | DETAILS_ROW | FIELD"
    mode="carbon--conditional">
    
    <xsl:param name="rounded" />
    <xsl:param name="action-control-sequence"/>

    <xsl:variable name="name">
      <xsl:choose>
        <xsl:when test="LINK/CONDITION">
          <xsl:value-of select="LINK/CONDITION//@NAME" />
        </xsl:when>
        <xsl:when test="CONDITION/@TYPE='DYNAMIC'">
          <xsl:text>JSCRIPT_REF</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="CONDITION//@NAME" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

   <xsl:variable name="prop">
      <xsl:choose>
        <xsl:when test="LINK/CONDITION">
          <xsl:value-of select="LINK/CONDITION//@PROPERTY" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="CONDITION//@PROPERTY" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:if test="not($name = 'CONTEXT') and not($name = 'JSCRIPT_REF')">
      <curam:setConditionalDisplayValue beanName="{$name}" fieldName="{$prop}"/>
    </xsl:if>

    <xsl:variable name="test-expression">
      <xsl:choose>
        <xsl:when test="CONDITION/IS_TRUE">
          <xsl:text>"true".equals(</xsl:text>
        </xsl:when>
        <xsl:when test="CONDITION/IS_FALSE">
          <xsl:text>"false".equals(</xsl:text>
        </xsl:when>
        <xsl:when test="LINK/CONDITION/IS_TRUE">
          <xsl:text>"true".equals(</xsl:text>
        </xsl:when>
        <xsl:when test="LINK/CONDITION/IS_FALSE">
          <xsl:text>"false".equals(</xsl:text>
        </xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$name = 'CONTEXT' and $prop = 'inWizard'">
          <xsl:text>pageContext.getAttribute("inAgenda"))</xsl:text>
        </xsl:when>
        <xsl:when test="$name = 'CONTEXT' and $prop = 'isDesktop'">
          <xsl:text>pageContext.getAttribute("isDesktop"))</xsl:text>
        </xsl:when>
        <xsl:when test="not($name = 'CONTEXT')">
          <xsl:text>pageContext.getAttribute("value"))</xsl:text>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>

    <!--
    CONDITIONs used in page level ACTION_CONTROLS is common and was contributing
    a lot to the WLS JSP size issue (UDI-1222). Therefore, the JSTL tags have
    been replaced with a simple if statement in a JSP scriptlet. It makes the
    code below slightly harder to follow and care needs to be taken to ensure
    the brackets around the statements are correctly output. However not using
    the JSTL tags provides a significant saving in the generated servlet size
    when conditions are used on page level action sets.
    TODO: At the time of writing, page level action sets are duplicated on the
    page up to 4 times: page top\bottom, modal button bar, page dropdown menu.
    If this duplication can be reduced, then it may be possible to revert to
    using JSTL conditional tags here. However, removing the JSTL tags was the
    best short-term approach.
    -->
    <jsp:scriptlet>
      <!--
      Start of condition. If the condition is true, show the content as it
      would normally be displayed.
      -->
      <xsl:text>if(</xsl:text>
      <xsl:value-of select="$test-expression"/>
      <xsl:text>){</xsl:text>
    </jsp:scriptlet>
    <xsl:choose>
        <xsl:when test="parent::CONTAINER.actioncol and preceding-sibling::*
                      and ../@SEPARATOR" />
        <xsl:when test="parent::FIELD.actioncol and preceding-sibling::*
                      and ../@SEPARATOR" />

        <xsl:when test="parent::CONTAINER and preceding-sibling::*
                      and ../@SEPARATOR">
          <!--
          Add the separator, it was not added in the "in-container" mode for
          the element because it had a nested CONDITION.
          -->

             <span class="separator">
               <xsl:value-of select="../@SEPARATOR"/>
             </span>
        </xsl:when>
         </xsl:choose>
        <xsl:choose>
          <!--
          Page level action controls numbering more than two need to be displayed
          in a dropdown menu. This dropdown will be part of the title bar.
           -->
          <xsl:when test="count(parent::ACTION_SET/ACTION_CONTROL) &gt; 2
                          and (not(ancestor::CLUSTER) and not(ancestor::LIST))">
            <xsl:apply-templates select="." mode="common">
              <xsl:with-param name="is-page-menu" select="'true'" />
              <xsl:with-param name="rounded" select="$rounded" />
            </xsl:apply-templates>
          </xsl:when>
          <xsl:when test="name() = 'ACTION_SET'">
            <xsl:choose>
              <!-- An action set which is either LIST[LIST_ROW_MENU] type or page level with more than 2 action controls -->
              <xsl:when test="(parent::DISPLAY and count(ACTION_CONTROL) &gt; 2) or @TYPE='LIST_ROW_MENU'">
                <xsl:apply-templates select="ACTION_CONTROL" mode="common">
                  <xsl:with-param name="is-page-menu" select="'true'" />
                  <xsl:with-param name="rounded" select="$rounded" />
                </xsl:apply-templates>
              </xsl:when>
              <xsl:otherwise>
                <xsl:choose>
                  <xsl:when test="parent::DISPLAY">
                    <xsl:apply-templates select="ACTION_CONTROL" mode="common" >
                      <xsl:with-param name="rounded" select="$rounded" />
                    </xsl:apply-templates>
                  </xsl:when>
                  <xsl:when test="parent::CLUSTER or parent::LIST">
                    <xsl:apply-templates select="../ACTION_SET" mode="common" />
                  </xsl:when>
                </xsl:choose>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:when test="name() = 'ACTION_CONTROL'">
            <xsl:choose>
              <xsl:when test="parent::ACTION_SET[@TYPE='LIST_ROW_MENU']">
                <xsl:apply-templates select="." mode="common">
                  <xsl:with-param name="is-page-menu" select="'true'" />
                  <xsl:with-param name="rounded" select="$rounded" />
                  <xsl:with-param name="action-control-sequence" select="$action-control-sequence"/>
                </xsl:apply-templates>
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="." mode="common">
                  <xsl:with-param name="rounded" select="$rounded" />
                  <xsl:with-param name="action-control-sequence" select="$action-control-sequence"/>
                </xsl:apply-templates>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="." mode="carbon--common"/>
          </xsl:otherwise>
        </xsl:choose>
      <jsp:scriptlet>
        <!-- "else" if the condition is false, we firstly check if we want to
             show the content, but disabled.
        -->
        <xsl:text>}else if("false".equals(pageContext.getAttribute("hideConditionalLinks"))){</xsl:text>
      </jsp:scriptlet>
          <xsl:choose>
            <xsl:when test="parent::CONTAINER">
              <xsl:if test="preceding-sibling::* and ../@SEPARATOR">
                <span class="disabled-link">
                  <xsl:value-of select="../@SEPARATOR"/>
                </span>
              </xsl:if>
              <span class="disabled-link">
                <xsl:value-of select="@LABEL"/>
              </span>
            </xsl:when>

            <!--
              When the condition is set at the action set level, it is applied
              to all child action controls within the action set.
            -->
            <xsl:when test="name() ='ACTION_SET'">
              <xsl:choose>
                <!--
                  If the action set is within a list row menu, return all as
                  menu items.
                -->
                <xsl:when test="@TYPE='LIST_ROW_MENU'">
                  <xsl:for-each select="ACTION_CONTROL">
                    <span data-dojo-type="curam/widget/MenuItem" disabled="true">
                      <xsl:apply-templates select="." mode="link-text-or-image"/>
                    </span>
                  </xsl:for-each>
                </xsl:when>
                <!--
                  Page level action sets with more than 2 action controls should be
                  displayed as menu items, except for modals and pages within expandable
                  list rows (as both these contexts do not contain a title bar)
                -->
                <xsl:when test="(parent::DISPLAY and count(ACTION_CONTROL) &gt; 2)">
                  <jsp:scriptlet>if ((!screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)
                      || screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TREE))
                    &amp;&amp; !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.LIST_ROW_INLINE_PAGE)) {
                  </jsp:scriptlet>
                    <xsl:for-each select="ACTION_CONTROL">
                      <span data-dojo-type="curam/widget/MenuItem" disabled="true">
                        <xsl:apply-templates select="." mode="link-text-or-image"/>
                      </span>
                    </xsl:for-each>
                  <jsp:scriptlet>} else {</jsp:scriptlet>
                    <xsl:apply-templates select="." mode="disabled_action_set"/>
                  <jsp:scriptlet>}</jsp:scriptlet>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                    <!--
                      Page level action set of two or less action controls, just
                      display disabled text.
                    -->
                    <xsl:when test="parent::DISPLAY and count(ACTION_CONTROL) &lt;= 2">
                      <xsl:for-each select="ACTION_CONTROL">
                        <span class="disabled-link">
                          <xsl:apply-templates select="." mode="link-text-or-image"/>
                        </span>
                      </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="parent::DISPLAY or parent::CLUSTER or parent::LIST">
                      <xsl:apply-templates select="." mode="disabled_action_set"/>
                    </xsl:when>
                  </xsl:choose>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:when test="name() ='ACTION_CONTROL'">
              <xsl:choose>
                <!--
                  If the action control's parent is a list row menu then it has to
                  be output as a disabled dijit MenuItem..
                -->
                <xsl:when test="parent::ACTION_SET[@TYPE='LIST_ROW_MENU']">
                  <span data-dojo-type="curam/widget/MenuItem" disabled="true">
                    <xsl:apply-templates select="." mode="link-text-or-image"/>
                  </span>
                </xsl:when>
                <!--
                If the action control's grand parent is a PAGE and there are more
                than 2 sibling action controls then it is also a disabled dijit MenuItem.
                -->
                <xsl:when test="name(../..) = 'DISPLAY' and count(../ACTION_CONTROL) &gt; 2">
                  <jsp:scriptlet>if (!screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)
                    &amp;&amp; !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.LIST_ROW_INLINE_PAGE)) {
                  </jsp:scriptlet>
                    <span data-dojo-type="curam/widget/MenuItem" disabled="true">
                      <xsl:apply-templates select="." mode="link-text-or-image"/>
                    </span>
                  <jsp:scriptlet>} else {</jsp:scriptlet>
                    <xsl:apply-templates select="." mode="disabled_button"/>
                  <jsp:scriptlet>}</jsp:scriptlet>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:choose>
                    <xsl:when test="(name(../..) = 'DISPLAY' and count(../ACTION_CONTROL) &lt;= 2)">
                      <span class="disabled-link">
                        <xsl:apply-templates select="." mode="link-text-or-image"/>
                      </span>
                    </xsl:when>
                    <xsl:when test="name(../..) = 'DISPLAY' or name(../..) = 'CLUSTER' or name(../..) = 'LIST'">
                      <xsl:apply-templates select="." mode="disabled_button"/>
                    </xsl:when>
                  </xsl:choose>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:when test="LINK/CONDITION and self::FIELD">
              <span class="disabled-link">
                <xsl:apply-templates select="." mode="check-for-image-map"/>
              </span>
            </xsl:when>
          </xsl:choose>
    <jsp:scriptlet>
      <!--
        Finally, if the condition is false and the hide-conditional-links flag is true
        then we would typically not display anything. However if more than two action
        controls exist and the condition is set at the action set level, then an empty
        drop down menu would be displayed (for page level and row list menu actions sets).
        This empty menu causes a Javascript error when selected, as it contains no menu items.
        A menu item has therefore been added with a title which explains to the user that
        the menu contains no content.
       -->
      <xsl:text>} else {</xsl:text>
    </jsp:scriptlet>

      <jsp:scriptlet>
        if ((!screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)
              || screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.TREE))
              &amp;&amp; !screenContext.hasContextBits(curam.omega3.taglib.ScreenContext.LIST_ROW_INLINE_PAGE)) {
      </jsp:scriptlet>
      <xsl:if test="name()='ACTION_SET' and (@TYPE='LIST_ROW_MENU'
                    or (parent::DISPLAY and count(ACTION_CONTROL) &gt; 2))">
        <jsp:scriptlet>
          <xsl:text>if(!(</xsl:text>
            <xsl:value-of select="$test-expression"/>
          <xsl:text>)){</xsl:text>
        </jsp:scriptlet>
          <span data-dojo-type="curam/widget/MenuItem" disabled="true">
            <jsp:scriptlet>
              String emptyMenu = curam.omega3.util.CDEJResources.getProperty("curam.dropdown.emptyMenu");
              out.print(curam.util.common.util.JavaScriptEscaper.escapeText(emptyMenu));
            </jsp:scriptlet>
          </span>
        <jsp:scriptlet>}</jsp:scriptlet>
      </xsl:if>

      <jsp:scriptlet>} }</jsp:scriptlet><!-- End of page check statement and outer if/else -->

  </xsl:template>

</xsl:stylesheet>