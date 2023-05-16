<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2005, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the details of an approval event made on  -->
<!-- a product delivery.                                                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="readApprovalEvent"
  />
  <PAGE_PARAMETER NAME="approvalID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="approvalID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseApprovalID"
    />
  </CONNECT>
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ApproverType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approverTypeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.UserName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fullUserName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalMethod"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ApprovalDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Outcome">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalOutcome"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Rules"
  >
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL
        IMAGE="FullTreeButton"
        LABEL="ActionControl.FullTreeButton.label"
      >
        <LINK
          PAGE_ID="Case_viewApprovalFullTree"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="approvalID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="approvalID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD
      CONFIG="NoDecisionID"
      CONTROL="DYNAMIC"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="resultText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
