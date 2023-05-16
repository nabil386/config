<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2004 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of utility payments.          -->
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
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readUtilityPayment"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="ACTION"
    OPERATION="modifyUtilityPayment"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="householdBudgetID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="householdBudgetID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="getClientUtilityPaymentItemKey$householdBudgetID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="householdBudgetID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="householdBudgetID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Component">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rulesObjectiveType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PaymentAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="customerAccName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="customerAccName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="customerAccNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="customerAccNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>