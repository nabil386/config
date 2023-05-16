<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2009, 2014. All Rights Reserved.

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
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the details of an adjustment financial    -->
<!-- instruction.                                                           -->
<?curam-deprecated Since Curam 6.0, replaced by Financial_viewAdjustmentInstructionView1.vim?>
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
    <!-- BEGIN, CR00160310, MC -->
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="contextDescription"
      />
    </CONNECT>
    <!-- END, CR00160310 -->
  </PAGE_TITLE>


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="CloseButton"
      LABEL="ActionControl.Label.Close"
    >
      <LINK
        PAGE_ID="Participant_resolveFinancialList"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="adjustmentHeader$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="adjustmentHeader$concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readAdjustmentInstruction"
  />


  <PAGE_PARAMETER NAME="finInstructionID"/>
  <PAGE_PARAMETER NAME="description"/>


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
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$adjustmentHeader$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$adjustmentHeader$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$adjustmentHeader$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.AdjustmentDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$adjustmentHeader$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$adjustmentHeader$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$adjustmentHeader$foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AdjustmentReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$adjustmentHeader$reasonCode"
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
      HEIGHT="12"
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


  <LIST TITLE="Cluster.Title.AdjustmentLineItems">


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="12"
    >


      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_viewLineItem"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$instructLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lineItemID"
            />
          </CONNECT>
          <!-- BEGIN, CR00160310, MC -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <!-- END, CR00160310 -->
        </LINK>
      </ACTION_CONTROL>


    </CONTAINER>


    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Debit"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="debitAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Credit"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="creditAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
