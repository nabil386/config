<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2009, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the details of a reversed financial       -->
<!-- instruction that relates to a cancelled payment.                       -->
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
    OPERATION="readReversalInstruction1"
  />


  <PAGE_PARAMETER NAME="finInstructionID"/>


  <!--<ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL LABEL="ActionControl.Label.ReissuePayment">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Financial_regeneratePayment1"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalFinInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$reversalInstructionHeaderDetails$caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CloseButton"
      LABEL="ActionControl.Label.Close"
    />


  </ACTION_SET>-->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$finInstructionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.ReversalDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Nominee">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReversalReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$reasonCodeDescription"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.ReversalDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Nominee">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReversalReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$reasonCodeDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="Cluster.Title.ReversedLineItems">


    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Component"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CoversPeriod"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="coversPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Credit"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="creditAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Debit"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="debitAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
