<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0">
<jsp:directive.page buffer="32kb" contentType="application/xml; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <curam:useTextHelper className="curam.interfaces.ContextFacadePkg.ContextFacade_readContextTree_TH" beanName="theBean" />
  <jsp:scriptlet><![CDATA[String param0 = request.getParameter("context_ID_context_id");
	theBean.setFieldValue(theBean.context_ID$context_id_idx, param0);
	String param1 = request.getParameter("context_ID_contextType");
	theBean.setFieldValue(theBean.context_ID$contextType_idx, param1);]]></jsp:scriptlet>
  <curam:callServer beanName="theBean" />
  <data>
    <jsp:scriptlet><![CDATA[out.print("<result_contextTree>");
out.print(theBean.getFieldValue(theBean.result$contextTree_idx));
out.println("</result_contextTree>");]]></jsp:scriptlet>
  </data>
</jsp:root>

