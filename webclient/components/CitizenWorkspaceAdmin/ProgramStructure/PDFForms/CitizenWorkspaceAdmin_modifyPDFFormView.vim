<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright (c) 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE CLASS="PDFForm" NAME="DISPLAY" OPERATION="viewPDFForm" />
  <SERVER_INTERFACE CLASS="PDFForm" NAME="ACTION" OPERATION="modifyPDFForm" PHASE="ACTION" />
  <PAGE_PARAMETER NAME="pdfFormID" />
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="pdfFormID" />
    <TARGET NAME="DISPLAY" PROPERTY="key$pdfFormID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="pdfFormID" />
    <TARGET NAME="ACTION" PROPERTY="pdfFormID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$dtls$versionNo" />
    <TARGET NAME="ACTION" PROPERTY="versionNo" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$dtls$nameTextID" />
    <TARGET NAME="ACTION" PROPERTY="nameTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$dtls$descriptionTextID" />
    <TARGET NAME="ACTION" PROPERTY="descriptionTextID" />
  </CONNECT>
  <CLUSTER LABEL_WIDTH="30" NUM_COLS="2" >
    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$name" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="name" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.PDFForm">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="pdfFormResourceName" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER TITLE="Cluster.Title.Description" SHOW_LABELS="false">
    <FIELD HEIGHT="240">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$description" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="description" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
