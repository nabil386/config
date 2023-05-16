<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2006, 2010 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the details of a decision made on         -->
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
    CLASS="ProductDelivery"
    NAME="DISPLAY"
    OPERATION="readDecision"
  />


  <PAGE_PARAMETER NAME="decisionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="decisionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseDecisionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >

    <FIELD LABEL="Field.Label.Decision">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="resultCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EffectiveFrom">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionFromDate"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="initReasonCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.EffectiveTo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionToDate"
        />
      </CONNECT>
    </FIELD>

  </CLUSTER>

  <LIST TITLE="List.Title.Objectives">

    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Value"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>

  </LIST>

</VIEW>
