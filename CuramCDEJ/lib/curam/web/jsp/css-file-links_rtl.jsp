<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0"> <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/> <jsp:output omit-xml-declaration="yes"/> <jsp:scriptlet> curam.omega3.taglib.ScreenContext screenContext = (curam.omega3.taglib.ScreenContext) pageContext.findAttribute( curam.omega3.taglib.ScreenContext.CTX_ATTR); if (screenContext.hasContextBits(screenContext.EXTAPP)) { pageContext.setAttribute("o3__isExtApp", "true"); } </jsp:scriptlet> <c:choose> <c:when test="${pageScope.o3__isExtApp == 'true'}"> <link rel="stylesheet" type="text/css" media="all" href="../CDEJ/extapp/uim_rtl.css"/> <link media="all" href="../CDEJ/jscript/dojotk/dijit/themes/dijit_rtl.css" rel="stylesheet" type="text/css"/> </c:when> <c:otherwise> <link rel="stylesheet" type="text/css" media="all" href="../themes/v6_rtl/css/v6_rtl_main.css"/> <link rel="stylesheet" type="text/css" media="all" href="../themes/curam_rtl/css/curam_rtl_main.css"/> <link rel="stylesheet" type="text/css" media="all" href="../themes/curam/fonts/main-ibm-font.css"/> <jsp:text>&lt;!--[if !IE]&gt;--&gt;</jsp:text> <link rel="stylesheet" type="text/css" media="all" href="../themes/v6_rtl/css/v6_rtl_cc_notIE.css"/> <jsp:text>&lt;!--&lt;![endif]--&gt;</jsp:text> <link rel="stylesheet" type="text/css" media="all" href="../themes/soria_rtl/css/soria_rtl_main.css"/> </c:otherwise> </c:choose> </jsp:root>