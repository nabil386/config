<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to view details of all recent changes made     -->
<!-- on the case audit.                                                     -->
<VIEW
  PAGE_ID="CaseAudit_listTransactionLogView"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="caseAuditID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseAuditID"
    />
    <TARGET
      NAME="TRANSDISPLAY"
      PROPERTY="key$caseAuditID"
    />
  </CONNECT>


  <LIST TITLE="List.Title.Details">


    <FIELD
      LABEL="Field.Label.TransactionType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="TRANSDISPLAY"
          PROPERTY="transactionType"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CaseAudit_viewTransactionLogRecord"
      >
        <CONNECT>
          <SOURCE
            NAME="TRANSDISPLAY"
            PROPERTY="transactionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="transactionID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Description"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="TRANSDISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.TransactionDateTime"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="TRANSDISPLAY"
          PROPERTY="transactionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.UserFullname"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="TRANSDISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="TRANSDISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </LIST>


</VIEW>
