<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0">
<jsp:directive.page buffer="32kb" contentType="application/xml; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <curam:useTextHelper className="curam.interfaces.ContextFacadePkg.ContextFacade_getContextDetails_TH" beanName="theBean" />
  <jsp:scriptlet><![CDATA[String param0 = request.getParameter("contextID_context_id");
	theBean.setFieldValue(theBean.contextID$context_id_idx, param0);
	String param1 = request.getParameter("contextID_contextType");
	theBean.setFieldValue(theBean.contextID$contextType_idx, param1);]]></jsp:scriptlet>
  <curam:callServer beanName="theBean" />
  <data>
    <jsp:scriptlet><![CDATA[out.print("<result_contextTree>");
out.print(theBean.getFieldValue(theBean.result$contextTree_idx));
out.println("</result_contextTree>");
	out.print("<result_contextImage>");
out.print(theBean.getFieldValue(theBean.result$contextImage_idx));
out.println("</result_contextImage>");
	out.print("<result_ssn>");
out.print(theBean.getFieldValue(theBean.result$ssn_idx));
out.println("</result_ssn>");
	out.print("<result_gender>");
out.print(theBean.getFieldValue(theBean.result$gender_idx));
out.println("</result_gender>");
	out.print("<result_dob>");
out.print(theBean.getFieldValue(theBean.result$dob_idx));
out.println("</result_dob>");
	out.print("<result_name>");
out.print(theBean.getFieldValue(theBean.result$name_idx));
out.println("</result_name>");
	out.print("<result_address>");
out.print(theBean.getFieldValue(theBean.result$address_idx));
out.println("</result_address>");
	out.print("<result_ssnLabel>");
out.print(theBean.getFieldValue(theBean.result$ssnLabel_idx));
out.println("</result_ssnLabel>");
	out.print("<result_genderLabel>");
out.print(theBean.getFieldValue(theBean.result$genderLabel_idx));
out.println("</result_genderLabel>");
	out.print("<result_nameLabel>");
out.print(theBean.getFieldValue(theBean.result$nameLabel_idx));
out.println("</result_nameLabel>");
	out.print("<result_dobLabel>");
out.print(theBean.getFieldValue(theBean.result$dobLabel_idx));
out.println("</result_dobLabel>");
	out.print("<result_addressLabel>");
out.print(theBean.getFieldValue(theBean.result$addressLabel_idx));
out.println("</result_addressLabel>");]]></jsp:scriptlet>
  </data>
</jsp:root>

