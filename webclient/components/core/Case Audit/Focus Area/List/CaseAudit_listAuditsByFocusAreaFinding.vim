<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of case audits for the audit  -->
<!-- plan.                                                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="auditPlanID"/>
  <PAGE_PARAMETER NAME="auditFocusAreaID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="auditPlanID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$auditPlanID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="auditFocusAreaID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$auditFocusAreaID"
    />
  </CONNECT>


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="CloseButton"
      LABEL="ActionControl.Label.Close"
    />
  </ACTION_SET>


  <LIST>


    <FIELD
      LABEL="List.Title.AuditReference"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$caseAuditReference"
        />
      </CONNECT>
      <LINK PAGE_ID="CaseAudit_resolveCaseAuditHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="List.Title.AssignedTo"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$auditorFullName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.ClientName"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$caseClientFullName"
        />
      </CONNECT>


    </FIELD>


    <FIELD
      LABEL="List.Title.Status"
      WIDTH="28"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$caseAuditStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
