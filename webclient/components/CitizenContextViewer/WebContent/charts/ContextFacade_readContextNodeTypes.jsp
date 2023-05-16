<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0">
<jsp:directive.page buffer="32kb" contentType="application/xml; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <curam:useTextHelper className="curam.interfaces.ContextFacadePkg.ContextFacade_readContextNodeTypes_TH" beanName="theBean" />
  <curam:callServer beanName="theBean" />
  <data>
    <jsp:scriptlet><![CDATA[out.print("<result_contextNodeTypeXML>");
out.print(theBean.getFieldValue(theBean.result$contextNodeTypeXML_idx));
out.println("</result_contextNodeTypeXML>");
	out.print("<result_contextMenuXML>");
out.print(theBean.getFieldValue(theBean.result$contextMenuXML_idx));
out.println("</result_contextMenuXML>");]]></jsp:scriptlet>
  </data>
</jsp:root>

