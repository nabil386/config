<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Included view to display a list of payments received that have been    -->
<!-- allocated towards a liability.                                         -->
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
    OPERATION="listPaymentsReceived"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$dtls$caseID"
    />
  </CONNECT>


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL
        IMAGE="ReverseButton"
        LABEL="ActionControl.Label.Reverse"
      >


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="reverseActionInd"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_createInstructionReversal"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <!-- BEGIN, CR00328565, CSH -->
      <ACTION_CONTROL LABEL="ActionControl.Label.Allocate">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="allocateActionInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_allocatePaymentReceivedSelectLiability1"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="instructionLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lineItemID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Refund">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="refundActionInd"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_createRefund"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="unprocessedAmount"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="unallocatedAmount"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- END, CR00328565 -->


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.AllocationDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocationDate"
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
          PROPERTY="allocatedFromType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ReceivedFrom"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receivedFrom"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.AmountAllocated"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocationAmount"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Financial_viewPaymentReceivedInstruction1">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="instructionLineItemType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionLineItemType"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
