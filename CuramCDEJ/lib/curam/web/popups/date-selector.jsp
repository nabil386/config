<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0"> <jsp:directive.page contentType="text/html; charset=UTF-8" errorPage="/en/CuramErrorPage.do" isErrorPage="false" language="java" pageEncoding="UTF-8"/> <jsp:directive.page import="curam.omega3.user.InfrastructureUserPreferences"/> <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/> <jsp:directive.page import="curam.omega3.user.UserPreferences"/> <jsp:directive.page import="curam.omega3.util.CDEJResources"/> <jsp:directive.page import="curam.omega3.taglib.ScreenContext"/> <jsp:directive.page import="java.util.Calendar"/> <jsp:directive.page import="curam.util.client.jsp.JspUtil"/> <jsp:directive.page import="curam.util.common.util.JavaScriptEscaper"/> <jsp:directive.page import="curam.util.common.useragent.UserAgent"/> <jsp:directive.page import="curam.util.client.tab.ApplicationConfigUtils"/> <jsp:output omit-xml-declaration="yes"/> <jsp:text><![CDATA[<!DOCTYPE html>]]></jsp:text> <curam:userPreferences localeCode="${pageScope.pageLocale}"/> <jsp:scriptlet><![CDATA[ final String serverRoot = JspUtil.getServerRootURL(2); UserPreferences prefs = UserPreferencesFactory.getUserPreferences(session); final java.util.Locale loc = prefs.getLocale(); pageContext.setAttribute("landmarkLabel", CDEJResources.getProperty("modal.panel.frame.title")); pageContext.setAttribute("pageLocale", loc.toString()); pageContext.setAttribute("dojoConfig", JspUtil.getDojoConfig(loc)); try { pageContext.setAttribute("highContrastMode", prefs.getUserPreference(InfrastructureUserPreferences.HIGH_CONTRAST_MODE)); } catch (final curam.util.common.JDEException e) { pageContext.setAttribute("highContrastMode", "false"); } pageContext.setAttribute("o3__serverURL", serverRoot); final String cssCommon = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + serverRoot; final String cssThemed=cssCommon + "themes/"; final String cssPrint="\" media=\"screen, print\"/>"; final String cssAll="\" media=\"all\" />"; final String scriptStart = "<script text=\"text/javascript\""; final String scriptFileStart = scriptStart + " src=\"" + serverRoot + "CDEJ/jscript/"; final String scriptEnd = "</script>";
      final String scriptFileEnd = "\">//dummy script content" + scriptEnd;
      final String dateBundle="curam.omega3.i18n.DateTimeSelector";
      final String confirmationScript = "require(['dojo/ready','curam/util/ui/AppExitConfirmation'],"
        +" function(ready,appExit){ ready(function(){ appExit.install(); });});";
      pageContext.setAttribute("o3__serverURL", serverRoot);
      final String dialogTitle = CDEJResources.getProperty(dateBundle, "dateSelector.title");
      pageContext.setAttribute("dialogTitle", dialogTitle);
    ]]></jsp:scriptlet>
   
    <jsp:scriptlet><![CDATA[
      boolean isRTL = false;
      String hDir = (String) pageContext.getAttribute("htmlDirection");
      if (hDir != null) {
        isRTL = hDir.equals("rtl");
      }
      pageContext.setAttribute("classDirection", (isRTL ? "rtl" : ""));
    ]]></jsp:scriptlet>
    <html lang="${htmlLanguage}" dir="${htmlDirection}" class="${classDirection}">
      <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
      <title>
        <jsp:scriptlet><![CDATA[out.print(dialogTitle);]]></jsp:scriptlet>
      </title>
      <jsp:scriptlet><![CDATA[
        out.print(cssThemed + "v6/css/v6_main.css" + cssPrint
                    + cssCommon + "CDEJ/jscript/dojotk/dijit/themes/soria/soria.css" + cssPrint
                    + cssThemed + "soria/css/soria_main.css" + cssPrint
                    + cssThemed + "curam/css/curam_main.css" + cssPrint
                    + cssThemed + "curam/fonts/main-ibm-font.css" + cssPrint);
        if (isRTL) {
          out.print(cssThemed + "v6_rtl/css/v6_rtl_main.css" + cssAll
        		  + cssThemed + "curam_rtl/css/curam_rtl_main.css" + cssPrint
        		  + cssThemed + "soria_rtl/css/soria_rtl_main.css" + cssAll);
        }
        out.print("<!--[if IE]>" + cssThemed + "v6/css/v6_cc_IE.css" + cssAll + "<![endif]-->");
        out.print("<!--[if !IE]>" + cssThemed + "v6/css/v6_cc_notIE.css" + cssAll + cssThemed + "v6/css/v6_cc_IE10.css" + cssAll + "<![endif]-->");
        UserAgent ua = new UserAgent(request.getHeader("User-Agent"));
        session.setAttribute(curam.util.client.domain.render.RendererUtils.UA_SESSION_KEY, ua);
        final boolean isMobile=ua.isMobileBrowser();
        pageContext.setAttribute("mobileUserAgent", String.valueOf(isMobile));
        if (isMobile) {
          out.print(cssThemed + "mobile/css/mobile_main.css" + cssAll);
          pageContext.setAttribute("mobileClassName", "mobile");
        }
        out.print(scriptStart + ">");
        JspUtil.outputDojoLoaderConfiguration(pageContext, loc);
        out.print(scriptEnd);
        pageContext.setAttribute("monthTitle", CDEJResources.getJSSafeProperty(dateBundle, "dateSelector.input.month"));
        pageContext.setAttribute("yearTitle", CDEJResources.getJSSafeProperty(dateBundle, "dateSelector.input.year"));
        pageContext.setAttribute("localCurrentDay", CDEJResources.getJSSafeProperty("date.selector.today.alt"));
        pageContext.setAttribute("localSelectedtDay", CDEJResources.getJSSafeProperty("date.selector.selected.alt"));
        final String topicID = (String) JavaScriptEscaper.escapeText(request.getParameter("topicId"), true);
        pageContext.setAttribute("dateTopicId", topicID != null ? "DSU.topic='"+ topicID +"'": "");
        int startDayOfWeek = Calendar.SUNDAY;
        try {
          if (Boolean.parseBoolean(CDEJResources.getProperty("enable.locale.aware.startdayofweek"))) {
        	boolean isMonday = (new java.util.GregorianCalendar(loc).getFirstDayOfWeek() == Calendar.MONDAY);
            if (isMonday) {
              startDayOfWeek = Calendar.MONDAY;
            }
          }
        } catch (Exception e) { /*Default to Sunday if property not found.*/ }
       startDayOfWeek -= 1; /* 0 or 1 */
       pageContext.setAttribute("startDayOfWeek", String.valueOf(startDayOfWeek));
       int[] wDays = {7,1,2,3,4,5,6,7};
       StringBuilder cHeaders = new StringBuilder();
       final int endDay = startDayOfWeek + 7;
       for (int i = startDayOfWeek; i < endDay; i++) {
    	 final String longDay = CDEJResources.getProperty("Text.longDay" + wDays[i]);
         final char ld = longDay.charAt(0);
         final char sd = CDEJResources.getProperty("Text.shortDay" + wDays[i]).charAt(0);
         cHeaders.append("<th class=\"day\">").append(sd)
                  .append("<abbr class=\"hidden\" title=\"" + longDay + "\">").append(longDay)
                  .append("</abbr></th>");
       }
       pageContext.setAttribute("confirmationScript",
         ApplicationConfigUtils.isGuardAgainstLeavingSet(false) ? confirmationScript : "");
       ScreenContext dsScreenContext = new ScreenContext();
       dsScreenContext.setInt(ScreenContext.MODAL|ScreenContext.POPUP);
       pageContext.setAttribute("dsScreenContext", dsScreenContext.getValue());
      ]]></jsp:scriptlet>
      
      <curam:jsUserPreferences />
      
      <jsp:scriptlet><![CDATA[
        out.print(scriptFileStart + "dojotk/dojo/dojo.js" + scriptFileEnd
        		   + scriptFileStart + "dojotk/dijit/dijit.js" + scriptFileEnd);
        out.print(scriptStart + ">");
        JspUtil.outputJSModulePaths(pageContext);
        out.print(scriptEnd);       
      ]]></jsp:scriptlet>

      <script type="text/javascript"><jsp:text><![CDATA[  
       /* force focus on the month combo*/
       var monthCombo = null, yearInput = null, refocus = null;
       function focusFirst() {
    	   refocus = function(combo) {
    	     return function() {
    	       combo.focus();
    	     }
    	 }
       }
       require(['dojo/on','curam/dateSelectorUtil','curam/util', 'curam/dialog', 'curam/util/onLoad',
                  'dojo/dom-construct', 'dijit/form/Select', 'curam/widget/Select',
                  'dojo/sniff', 'curam/util/ScreenContext'],
         function(On, DSU, _U, Dialog, OnLoad, domConstruct, DijitSelect, CuramSelect, has) {
    	   dojo.global.jsBaseURL = _U.retrieveBaseURL();
    	   dojo.global.jsScreenContext = new curam.util.ScreenContext();
    	   dojo.global.jsScreenContext.setContext('POPUP|MODAL');
           dojo.global.LOCALISED_CURRENT_DAY = '${localCurrentDay}';
           dojo.global.LOCALISED_SELECTED_DAY = '${localSelectedtDay}';
           Dialog.initModal('date-selector');
           // register publisher for the page height information
           OnLoad.addPublisher(function(cx) {
             cx.height = _U.getPageHeight();
             cx.title = window.document.title;
           });
           var opts = LOCALIZED_MONTH_NAMES.map(function(month, ix){
             return {value:ix, label:month};
           });
           dojo.addOnLoad(function() {
    	     OnLoad.execute();
    	     var Select = CuramSelect;
    	     var monthSelector = new Select({id:'month', name:'month',
    	    	                             maxHeight:'147',
    	    	                             title:'${monthTitle}',
    	    	                             options:opts}, 'monthSelector');
             DSU.dayTitle = '${dialogTitle}';
    	     DSU.initFieldsAndListeners();
             DSU.initCalendar();
             monthCombo = DSU.monthCombo;
             yearInput = DSU.yearInput;
           });
           On(window, 'unload', function() {
	         var topWin = _U.getTopmostWindow();
	         topWin.dojo.publish('curam/modal/close/ignoreRefresh');
           });
           DSU.startWeekDay = '${startDayOfWeek}';
           _U.setupGenericKeyHandler();
           ${dateTopicId}
           ${confirmationScript}
       });]]></jsp:text>
     </script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/SPMUIComponents/spm-uicomponents-main.bundle.js"> dummy script content</script> </head> <body tabindex="-1" aria-label="${landmarkLabel}" id="Curam_date-selector" class="${classDirection} ${htmlLanguage} curam soria ${mobileClassName}"> <div id="content"> <form id="calendar-form"> <table class="month-year-selector" role="presentation"> <tr><td id="month-year-wrapper" aria-live="polite" aria-atomic="true" tabindex="-1"> <select id='monthSelector' class='codetable widget-medium' role="listbox"/> <input id='year' name='year' type='text' class='widget-medium' aria-label='${yearTitle}' title='${yearTitle}'/> </td></tr> </table> <input type='hidden' id='o3ctx' value='${dsScreenContext}'/> <table id="calendarData" class="calendar"> <thead><tr class="day-names"><jsp:scriptlet><![CDATA[out.print(cHeaders);]]></jsp:scriptlet></tr></thead> <tbody> <jsp:scriptlet><![CDATA[ StringBuilder tContent = new StringBuilder(); for (int w = 0; w < 6; w++) { String aClass = (w == 0) ? " class='week-one'" : ""; tContent.append("<tr").append(aClass).append(">"); for (int d = 0; d < 7; d++) { tContent.append("<td aria-hidden=\"true\" class=\"calendar-value\">&amp;nbsp;</td>"); } tContent.append("</tr>"); } out.print(tContent.toString()); ]]></jsp:scriptlet> </tbody> </table> </form> <div id="bottom-bar" aria-hidden="true"></div> </div> </body> </html>
</jsp:root>