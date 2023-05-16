<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
  
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
<!-- The included view to display the details of an adjustment financial    -->
<!-- instruction.                                                           -->
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
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readAdjustmentInstruction1"
  />


  <PAGE_PARAMETER NAME="finInstructionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$financialInstructionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.Amount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AdjustmentDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
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


    <FIELD LABEL="Field.Label.AdjustmentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adjustmentTypeDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AdjustmentReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.Amount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AdjustmentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adjustmentTypeDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AdjustmentReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AdjustmentDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
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
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="5"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
