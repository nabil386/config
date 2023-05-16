<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004-2005, 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <FIELD LABEL="Field.Label.ReferenceNumber">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="caseReference"
      />
    </CONNECT>
    <LINK PAGE_ID="Case_resolveIssueCaseHome">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="parentCaseID"
        />
      </CONNECT>
    </LINK>
  </FIELD>


  <FIELD LABEL="Field.Label.ReceivedDate">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="receivedDate"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.ReceiptNoticeIndicator">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="receiptNoticeIndicator"
      />
    </CONNECT>
  </FIELD>


  <!-- BEGIN, CR00198210, PS -->
  <FIELD
    LABEL="Field.Label.RecordStatus"
    WIDTH="10"
  >
    <!-- END, CR00198210 -->
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="recordStatus"
      />
    </CONNECT>
  </FIELD>


</VIEW>
